package com.example.sstest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationManagers;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

/**
 * *
 * <p>Created by irina on 1/24/2023.</p>
 * <p>Project: ss-test</p>
 * *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public UserDetailsService users() {
        // The builder will ensure the passwords are encoded before saving in memory
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        UserDetails guest = users
                .username("guest")
                .password("jolt")
                .roles("GUEST")
                .build();
        UserDetails user = users
                .username("me")
                .password("jolt")
                .roles("USER")
                .build();
        UserDetails admin = users
                .username("admin")
                .password("jolt")
                .roles("USER", "ADMIN", "GUEST")
                .build();
        System.out.println("**********************************");
        System.out.println(admin.getPassword());
        System.out.println("**********************************");
        return new InMemoryUserDetailsManager(user, admin, guest);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        String fragment = "/test2";
        AntPathRequestMatcher antPathRequestMatcher = new AntPathRequestMatcher("/foo/test4/**", "GET");  // extra icon for httpMethod parameter
        AntPathRequestMatcher antPathRequestMatcher2 = new AntPathRequestMatcher("/foo"+"/test4"+"/**");  // no navigation in case of concatenation
        RegexRequestMatcher regexRequestMatcher = new RegexRequestMatcher("/foo/test\\d[\\d]*", "POST"); // no reference

        http
                .formLogin(form -> form
                        .loginPage("/onEntering")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/onExiting")
                        .logoutSuccessUrl("/home")
                        .invalidateHttpSession(true)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(antPathRequestMatcher).hasRole("USER")
                        .requestMatchers(regexRequestMatcher).hasRole("USER")
                        .requestMatchers("/resources/**", "/home", "/").permitAll()
                        .requestMatchers(HttpMethod.POST).hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET,"/post").denyAll()
                        .requestMatchers(antMatcher("/admin*/**")).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/foo/test1").access(new WebExpressionAuthorizationManager("hasRole('ADMIN') or hasRole('GUEST') or hasRole('USER')"))
                        // extra shields resulted in no correct navigation in case of concatenation:
                        .requestMatchers("/foo"+"/test2"+"/**").access(AuthorizationManagers.allOf(AuthorityAuthorizationManager.hasRole("ADMIN"), AuthorityAuthorizationManager.hasRole("USER")))
                        // extra shields resulted in no correct navigation in case of concatenation with variable:
                        .requestMatchers("/foo"+fragment+"/**").access(AuthorizationManagers.allOf(AuthorityAuthorizationManager.hasRole("ADMIN"), AuthorityAuthorizationManager.hasRole("USER")))
                        // compare: same url w/o concatenation:
                        .requestMatchers("/foo/test2/**").access(AuthorizationManagers.allOf(AuthorityAuthorizationManager.hasRole("ADMIN"), AuthorityAuthorizationManager.hasRole("USER")))
                        .requestMatchers("/foo/test3/{pathvar}").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(regexMatcher("/bar/test\\d[\\d]*")).hasAnyAuthority("ROLE_GUEST","ROLE_USER")
                        .requestMatchers("/bar/test1/*").access(AuthorizationManagers.anyOf(AuthorityAuthorizationManager.hasAuthority("ROLE_USER"), AuthorityAuthorizationManager.hasAuthority("ROLE_ADMIN")))
                        .anyRequest().denyAll()
                );;
        return http.build();
    }

    // mvcMatcherBuilder test: no urls are inserted - fixed for pattern() method
   /*@Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/");  // no reference here
        MvcRequestMatcher mvcMatcher = new MvcRequestMatcher(introspector,"/prohibited"); // no reference here
        http
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(mvcMatcherBuilder.pattern("/admin/**")).hasRole("ADMIN")
                        .requestMatchers(mvcMatcherBuilder.pattern("/foo/**")).hasRole("USER")
                        .requestMatchers(mvcMatcherBuilder.pattern("/bar/**")).hasRole("GUEST")
                        .requestMatchers(mvcMatcher).denyAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }*/
    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/bar/**","/foo/**")
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/resources/**", "/home", "/").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults());
        return http.build();
    }*/


}

