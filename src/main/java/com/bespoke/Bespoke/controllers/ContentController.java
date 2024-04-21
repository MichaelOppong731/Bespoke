package com.bespoke.Bespoke.controllers;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.models.UserModel;
import com.bespoke.Bespoke.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ContentController {
    @Autowired
    private AppUserService appUserService;

    //Go to homePage
    @GetMapping("/home")
    public String welcomePage(){
        return "home";
    }
//Handle Admin homepage
    @GetMapping("/admin/home")
    public String adminHome(Model model, Principal principal){
        // Load the user details from your appUserService
       AppUser userDetails = (AppUser) appUserService.loadUserByUsernames(principal.getName());
//

      model.addAttribute("userdetail", userDetails);

        // Return the view name
       return "admin_home";
    }
// Handle User Homepage
    @GetMapping("/user/home")
    public String userHome(Model model, Principal principal){

        UserDetails userDetails = appUserService.loadUserByUsernames(principal.getName());
        model.addAttribute("userdetail" , userDetails);
        return "user_home";
    }
// Handle Login
    @GetMapping("/login")
    public String loginPage(Model model, UserModel userModel){
        model.addAttribute("user", userModel);
        return "custom_login";
    }

    @GetMapping("/error")
    public String error(){
        return "403";
    }

    @GetMapping("/register")
    public String registrationPage(Model model, UserModel userModel){
        model.addAttribute("user", userModel);
        return "registration";
    }
}
