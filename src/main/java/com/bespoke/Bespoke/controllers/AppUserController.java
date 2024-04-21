package com.bespoke.Bespoke.controllers;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.models.UserModel;
import com.bespoke.Bespoke.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AppUserController {
    @Autowired
    private AppUserService appUserService;


    //Read Form data to save into DataBase
    @PostMapping("/register")
    public String saveUser(@ModelAttribute("user") UserModel userModel, Model model){
        Optional<AppUser> user = appUserService.findByEmail(userModel.getEmail());
        if (user.isPresent()) {
            model.addAttribute("userexist", user);
            return "registration";

        }

            AppUser appUser = new AppUser();
            appUser.setFirstName(userModel.getFirstName());
            appUser.setLastName(userModel.getLastName());
            appUser.setUsername(userModel.getUsername());
            appUser.setEmail(userModel.getEmail());
            appUser.setPassword(userModel.getPassword());
            appUser.setRole("USER");
            appUserService.saveUser(appUser);

        return "redirect:/registration?success";
    }

}
