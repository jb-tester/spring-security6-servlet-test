package com.example.sstest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bar")
public class BarController {

    @GetMapping("/{pv}")
    public String test0(@PathVariable("pv") String pv) {
        return "bar + pathvar( " + pv + ")";
    }

    @GetMapping("/test1")
    public String test1() {
        return "bar test1";
    }

    @GetMapping("/test1/{pv}")
    public String test2(@PathVariable String pv) {
        return "bar test1 + pathvar( " + pv + ")";
    }

    @GetMapping("/test22")
    public String test2() {
        return "bar test2";
    }
}
