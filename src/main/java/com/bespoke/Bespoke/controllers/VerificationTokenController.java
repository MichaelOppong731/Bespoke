package com.bespoke.Bespoke.controllers;

import com.bespoke.Bespoke.entities.ForgotPasswordToken;
import com.bespoke.Bespoke.entities.VerificationToken;
import com.bespoke.Bespoke.service.VerificationTokenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account-verification")
public class VerificationTokenController {

    @Autowired
    VerificationTokenService verificationTokenService;



    @GetMapping
    public String validateAccount(@Param(value = "token") String token , Model model, HttpSession session){

        session.setAttribute("token", token);//Begin a session using the token in the parameter of the url
        VerificationToken verificationToken = verificationTokenService.findToken(token);
        return verificationTokenService.checkValidity(verificationToken,model);
    }

}
