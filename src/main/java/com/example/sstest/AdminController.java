package com.example.sstest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * *
 * <p>Created by irina on 2/2/2023.</p>
 * <p>Project: spring-security6-servlet-test</p>
 * *
 */
@RestController
public class AdminController {

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    @GetMapping("/admin0")
    public String admin0() {
        return "admin0";
    }
    @GetMapping("/admin1/{pv}")
    public String admin1(@PathVariable("pv") String pv) {
        return "admin1 + {" + pv + "}";
    }
}
