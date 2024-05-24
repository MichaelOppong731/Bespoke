package com.bespoke.Bespoke.controllers;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.entities.ForgotPasswordToken;
import com.bespoke.Bespoke.service.AppUserService;
import com.bespoke.Bespoke.service.ForgotPasswordService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@AllArgsConstructor
@Controller
public class ForgotPasswordController {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private ForgotPasswordService forgotPasswordService;



    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    //GET FORGOT-PASSWORD PAGE
    @GetMapping("/password-request")
    public String forgotPasswordPage(){

        return "forgotPassword";
    }

    //CREATE NEW ENTITY FOR FORGOT-PASSWORD AND ADD TO DATABASE
    @PostMapping("/password-request")
    public String savePasswordRequest(@RequestParam("email") String email, Model model){
        Optional<AppUser> user = appUserService.findByEmail(email);
        if (user.isEmpty()){
            model.addAttribute("error", "Email does not exit!");
            return "forgotPassword";
        }
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setExpiration(forgotPasswordService.expirationTime());
        forgotPasswordToken.setToken(forgotPasswordService.generateToken());
        forgotPasswordToken.setAppUser(user.get());
        forgotPasswordToken.setUsed(false);
        //Save token into the database
        forgotPasswordService.saveToken(forgotPasswordToken);


        //SET EMAIL LINK
        String emailLink = "https://pauls-bespoke.onrender.com/reset-password?token="+ forgotPasswordToken.getToken();
        try {
            forgotPasswordService.sendMail(user.get().getEmail(), "Password Reset Link",emailLink);
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", "Error while sending email");
            return "forgotPassword";
        }
        return "redirect:/forgotPassword?success";
    }

    //GET RESET-PASSWORD PAGE
    @GetMapping("/reset-password")
    public String resetPasswordPage(@Param(value = "token") String token , Model model, HttpSession session){
        session.setAttribute("token", token);//Begin a session using the token in the parameter of the url
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findtoken(token);
        return forgotPasswordService.checkValidity(forgotPasswordToken,model);

    }

    //GET NEW PASSWORD OF USER AND SAVE IN DATABASE
    @PostMapping("/reset-password")
    public String saveResetPassword(HttpServletRequest request, HttpSession session, Model model){
        String password = request.getParameter("password");
        String token = session.getAttribute("token").toString();
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findtoken(token);
        AppUser appUser = forgotPasswordToken.getAppUser();//Get the appUser associated with the account
        appUser.setPassword(passwordEncoder.encode(password));
        forgotPasswordToken.setUsed(true);//set isUsed to true
        //save changes made to appuser since password has been changed
        appUserService.updatePassword(appUser);
        //Save changes made to forgotPassword token since "isUsed" status has been set to true
        forgotPasswordService.saveToken(forgotPasswordToken);
        model.addAttribute("message", "Your password has been changed successfully!!");
        return "reset_password";
    }
}
