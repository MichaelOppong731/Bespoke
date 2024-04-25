package com.bespoke.Bespoke.service;

import com.bespoke.Bespoke.entities.ForgotPasswordToken;
import com.bespoke.Bespoke.repository.ForgotPasswordRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ForgotPasswordService {
    private final static int MINUTES = 10;

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Autowired
    JavaMailSender javaMailSender;

    //Randomly generating token
    public String generateToken(){
        return UUID.randomUUID().toString();
    }
    // Setting expiration time limit
    public LocalDateTime expirationTime(){
        return LocalDateTime.now().plusMinutes(MINUTES);
    }

    //Send Mail
    public void sendMail(String receiver, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        //Email content
        String mailContent = "<p>Hello</p>"
                +"Click the link below to reset your password"
                +"<p><a href=\"" + emailLink + "\">Change My Password</a></p>"
                +"<br>"
                +"Ignore this mail if you did not make this request";

        helper.setText(mailContent,true);
        helper.setFrom("michaeloppong946@gmail.com", "Bespoke Customer Support");
        helper.setSubject(subject);
        helper.setTo(receiver);
        javaMailSender.send(message);
    }

    //Save Token
    public void saveToken(ForgotPasswordToken forgotPasswordToken) {
        forgotPasswordRepository.save(forgotPasswordToken);
    }
    //Find token
    public ForgotPasswordToken findtoken(String token) {
        return forgotPasswordRepository.findByToken(token);
    }

    //Check if a token is expired
    public boolean isExpired(ForgotPasswordToken forgotPasswordToken){
        return LocalDateTime.now().isAfter(forgotPasswordToken.getExpiration());
    }

    //Check for validity of token
    public String checkValidity(ForgotPasswordToken forgotPasswordToken, Model model){
        if (forgotPasswordToken == null){
            model.addAttribute("error", "Invalid Token!");
            return "error_page";
        } else if (forgotPasswordToken.isUsed()) {

            model.addAttribute("error", "Token is already used!");
            return "error_page";
        } else if (isExpired(forgotPasswordToken)) {
            model.addAttribute("error", "Token is Expired!");
            return "error_page";
        }

        else {
            return "reset_password";
        }

    }

}
