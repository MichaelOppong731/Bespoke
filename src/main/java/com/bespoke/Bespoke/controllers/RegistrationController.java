package com.bespoke.Bespoke.controllers;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.entities.VerificationToken;
import com.bespoke.Bespoke.models.UserModel;
import com.bespoke.Bespoke.service.AppUserService;
import com.bespoke.Bespoke.service.VerificationTokenService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class RegistrationController {
    @Autowired
    private AppUserService appUserService;

    @Autowired
    private VerificationTokenService verificationTokenService;


    //GET REGISTRATION PAGE
    @GetMapping("/register")
    public String registrationPage(Model model, UserModel userModel){
        model.addAttribute("user", userModel);
        return "registration";
    }

    //READ FROM DATA TO SAVE INTO DATABASE
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
            appUser.setEmail(userModel.getEmail());
            appUser.setPassword(userModel.getPassword());
            appUser.setRole("ROLE_USER");
            appUser.setEnabled(true);
            appUserService.saveUser(appUser);

        // SET VERIFICATION TOKEN

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(verificationTokenService.generateToken());
        verificationToken.setCreatedAt(LocalDateTime.now());
        verificationToken.setUsed(false);
        verificationToken.setExpiration(verificationTokenService.expirationTime());
        verificationToken.setAppUser(appUser);

        //SAVE VERIFICATION TOKEN
        verificationTokenService.saveToken(verificationToken);

        // SEND TOKEN TO NEW USER

        String emailLink = "http://localhost:8080/account-verification?token="+ verificationToken.getToken();//Set emailLink
        try {
            verificationTokenService.sendMail(appUser.getEmail(), "Verify Email",emailLink);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", "Error while sending email");
            return "registration";
        }

        return "redirect:/register?success";
    }

}
