package com.example.sstest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/foo")
public class FooController {


    //@GetMapping("/test1")
    //@RequestMapping(path="/test1", method = RequestMethod.GET)
    @RequestMapping(path="/test1")
    public String test1(ModelMap model) {
        model.addAttribute("test1_attr", "test1_attr");
        return "test1";
    }

    @GetMapping("/test2")
    public String test2(ModelMap model) {
        model.addAttribute("test2_attr", "test2_attr");
        return "test2";
    }
    @GetMapping("/test2/test22")
    public String test22(ModelMap model) {

        model.addAttribute("test22_attr", "test22_attr");
        return "test22";
    }

    @RequestMapping("/test3/{pv}")
    public String test3(ModelMap model, @PathVariable String pv) {
        model.addAttribute("test3_attr1", "test3 "+pv);
        return "test3";
    }
}
