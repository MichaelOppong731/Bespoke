package com.bespoke.Bespoke.controllers;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.entities.Video;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class VideoController {

    @Value("${upload.dir}") // Value from application.properties
    private String uploadDir;

    @Autowired
    private AppUserService appUserService;
    @Autowired
    private VideoService videoService;
//    @GetMapping("/watch")
//    public String getVideos(Model model, Principal principal){
//        // Load the user details from your appUserService
//        AppUser userDetails = (AppUser) appUserService.loadUserByUsernames(principal.getName());
//        model.addAttribute("userdetail", userDetails);
//        //Load all Available videos
//        List<Video> videos = videoService.getAllVideos();
//        model.addAttribute("videos", videos);
//
//        return "videosPage";
//    }


    @GetMapping("/video/{id}/play")
    public String playVideo(@PathVariable("id") Integer id, Model model, Principal principal) {
        // Retrieve user details from the principal
        AppUser userDetails = (AppUser) appUserService.loadUserByUsernames(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Retrieve video details based on the ID using videoService
        Optional<Video> optionalVideo = videoService.getVideoById(id);
        if (!optionalVideo.isPresent()) {
            // Handle case where video is not found
            return "403"; // Redirect to an error page
        }

        Video video = optionalVideo.get();

        // Construct the path to the video file
        Path videoPath = Paths.get(uploadDir, video.getFilePath());
        Resource resource;
        try {
            resource = new UrlResource(videoPath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "403"; // Redirect to an error page
        }

        // Pass user details, video details, and video file to the model
        model.addAttribute("userdetail", userDetails);
        model.addAttribute("video", video);
        model.addAttribute("videoFile", resource);

        return "videosPage"; // Return the videosPage template
    }




    // Endpoint to fetch video content based on video ID
//    @GetMapping("/video/{id}/play")
//    public ResponseEntity<Resource> playVideo(@PathVariable("id") Integer id) {
//        // Retrieve video content based on the ID using videoService
//        Optional<Video> optionalVideo = videoService.getVideoById(id);
//        if (!optionalVideo.isPresent()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        Video video = optionalVideo.get();
//
//        // Construct the path to the video file
//        Path videoPath = Paths.get(uploadDir, video.getFilePath());
//        Resource resource;
//        try {
//            resource = new UrlResource(videoPath.toUri());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType("video/mp4"))
//                .body(resource);
//    }

    @GetMapping("/upload")
    public String showUpLoadPage(HttpServletResponse response){
        // Set Cache-Control header to prevent caching
        response.setHeader("Cache-Control", "no-store");
        return "upload_video";
    }

    @PostMapping("/upload")
    public String uploadVideo(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            Model model
    ) throws IOException {
        try {
            // Save the uploaded file to the server
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File videoFile = new File(uploadDir + File.separator + fileName);
            file.transferTo(videoFile);

            // Save video details to the database
            Video video = new Video(title, description, fileName);
            videoService.uploadVideo(video);

            model.addAttribute("successMessage", true);
        } catch (IOException e) {
            // Handle file IO exception
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to upload video.");
        }

        return "redirect:/upload?success"; // Redirect to success page
    }


}
