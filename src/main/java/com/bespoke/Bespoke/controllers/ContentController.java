package com.bespoke.Bespoke.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {

    //Go to homePage
    @GetMapping("/home")
    public String welcomePage(){
        return "home";
    }
//Handle Admin homepage
    @GetMapping("/admin/home")
    public String adminHome(){
        return "home_admin";
    }
// Handle User Homepage
    @GetMapping("/user/home")
    public String userHome(){
        return "home_user";
    }
// Handle Login
    @GetMapping("/login")
    public String loginPage(){
        return "custom_login";
    }

    @GetMapping("/register")
    public String registrationPage(){
        return "register";
    }
}
