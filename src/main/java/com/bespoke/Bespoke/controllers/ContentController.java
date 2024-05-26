package com.bespoke.Bespoke.controllers;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.entities.Video;
import com.bespoke.Bespoke.models.UserModel;
import com.bespoke.Bespoke.service.AppUserService;
import com.bespoke.Bespoke.service.VideoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class ContentController {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private VideoService videoService;



    //Handle Admin homepage
    @GetMapping("/admin/home")
    public String adminHome(Model model, Principal principal){
        // Load the user details from your appUserService
        AppUser userDetails = (AppUser) appUserService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Load all available videos
        //This part seeks to hide the videos page if the database is not populated
        List<Video> videos = videoService.getAllVideos();
        model.addAttribute("videos", videos);

       return "admin_home";
    }

    //Redirect to admin or user homepage when they click on the Home tab when logged in
    @GetMapping("/home")
    public String redirectToDashboard(Model model, Principal principal) {
        if (principal != null) {
            AppUser userDetails = (AppUser) appUserService.loadUserByUsername(principal.getName());
            boolean isAdmin = userDetails.getRole().contains("ADMIN");
            if (isAdmin) {
                return "redirect:/admin/home";
            } else {
                return "redirect:/user/home";
            }
        } else {
            // Handle the case where the user is not authenticated
            return "redirect:/login";
        }
    }




    // Handle User Homepage
    @GetMapping("/user/home")
    public String userHome(Model model, Principal principal){

        AppUser userDetails = (AppUser) appUserService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail" , userDetails);

        // Load all available videos
        //This part seeks to hide the videos page if the database is not populated
        List<Video> videos = videoService.getAllVideos();
        model.addAttribute("videos", videos);

        return "user_home";
    }
    // LOGIN PAGE
    @GetMapping("/login")
    public String loginPage(Model model, UserModel userModel){
        model.addAttribute("user", userModel);
        return "custom_login";
    }




}
