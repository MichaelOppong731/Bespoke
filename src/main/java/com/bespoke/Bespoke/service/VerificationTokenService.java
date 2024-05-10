package com.bespoke.Bespoke.service;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.entities.VerificationToken;
import com.bespoke.Bespoke.repository.VerificationTokenRepository;
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
public class VerificationTokenService {
    private final static int MINUTES = 60 * 24;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private AppUserService appUserService;

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
                +"Thank you for signing up with us"
                +"Click the link below to Confirm your email"
                +"<p><a href=\"" + emailLink + "\">Verify Your Account Now!</a></p>"
                +"<br>"
                +"Ignore this mail if you did not make this request";

        helper.setText(mailContent,true);
        helper.setFrom("michaeloppong946@gmail.com", "Bespoke Customer Support");
        helper.setSubject(subject);
        helper.setTo(receiver);
        javaMailSender.send(message);
    }

    //Save Token
    public void saveToken(VerificationToken verificationToken) {
        verificationTokenRepository.save(verificationToken);
    }
    //Find token
    public VerificationToken findToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    //Check if a token is expired
    public boolean isExpired(VerificationToken forgotPasswordToken){
        return LocalDateTime.now().isAfter(forgotPasswordToken.getExpiration());
    }

    //Check for validity of token
    public String checkValidity(VerificationToken verificationToken, Model model){
        if (verificationToken == null){
            model.addAttribute("error", "Invalid Token!");
            return "error_page";
        } else if (verificationToken.isUsed()) {

            model.addAttribute("error", "Token is already used!");
            return "error_page";
        } else if (isExpired(verificationToken)) {
            model.addAttribute("error", "Token is Expired!");
            return "error_page";
        }

        else {

            //SET TOKEN VALUE AS USED
            verificationToken.setUsed(true);
            //SET TIME TOKEN WAS CONFIRMED
            verificationToken.setConfirmedAt(LocalDateTime.now());
            //SAVE VERIFICATION TOKEN
            verificationTokenRepository.save(verificationToken);
            return "validationSuccess";
        }

    }

}
