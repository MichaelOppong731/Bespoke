package com.bespoke.Bespoke.controllers;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.models.UserModel;
import com.bespoke.Bespoke.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/bespoke/registration")
public class AppUserController {
    @Autowired
    private AppUserService appUserService;

    //Go to Registration Page
    @GetMapping("/register")
    public String registrationPage(){
        return "register";
    }

    //Read Form data to save into DataBase
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") UserModel model, Model msg){
        AppUser user = new AppUser();
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setUsername(model.getUserName());
        user.setEmail(model.getEmail());
        user.setPassword(model.getPassword());
        user.setRole("USER");
        appUserService.saveUser(user);
        msg.addAttribute("message", "Registered successfully!");

        return "register";
    }

}
