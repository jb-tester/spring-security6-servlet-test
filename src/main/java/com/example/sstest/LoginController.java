package com.example.sstest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
class LoginController {

    private String greeting = "hello! please login first";

    @GetMapping("/onEntering")
    String loginMethod() {
        this.greeting = "!!!!!";
        return "login";
    }

    @RequestMapping({"/home","/"})
    public String home() {
        return "home";
    }



    @PostMapping("/post")
    public String postSmth(@RequestBody @RequestParam String req){
        System.out.println("**********************************");
        System.out.println("post completed with " + req);
        System.out.println("**********************************");
        this.greeting = req;
        return "home";
    }

    @ModelAttribute("home_attr")
    public String homeAttr(){
        return this.greeting;
    }
}