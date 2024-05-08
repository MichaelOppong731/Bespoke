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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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




    //Get Videos Page
    @GetMapping("/videos")
    public String getVideos(Model model, Principal principal) {
        // Load the user details from appUserService
        AppUser userDetails = (AppUser) appUserService.loadUserByUsernames(principal.getName());
        model.addAttribute("userdetail", userDetails);

        return "videosPage";
    }


    //Endpoint to fetch video content based on video ID
    //Video Playback
    @GetMapping("/video/{id}/play")
    public ResponseEntity<Resource> playVideo(@PathVariable("id") Integer id) {
        // Retrieve video content based on the ID using videoService
        Optional<Video> optionalVideo = videoService.getVideoById(id);
        if (!optionalVideo.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Video video = optionalVideo.get();

        // Construct the path to the video file
        Path videoPath = Paths.get(uploadDir, video.getFilePath());
        Resource resource;
        try {
            resource = new UrlResource(videoPath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("video/mp4"))
                .body(resource);
    }


    //Get Video Upload Page
    @GetMapping("/upload")
    public String showUpLoadPage(Model model, Principal principal) {
        // Load the user details from appUserService
        AppUser userDetails = (AppUser) appUserService.loadUserByUsernames(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Load all available videos
        //This part seeks to hide the videos page if the database is not populated
        List<Video> videos = videoService.getAllVideos();
        model.addAttribute("videos", videos);
        return "upload_video";
    }


    //Post Video Upload Details to the Database
    @PostMapping("/upload")
    public String uploadVideo(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) throws IOException {
        try {
            // Save the uploaded file to the server
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            File videoFile = new File(uploadDir + File.separator + fileName);
            file.transferTo(videoFile);

            // Save video details to the database
            Video video = new Video(title, description, fileName);
            videoService.uploadVideo(video);

            redirectAttributes.addFlashAttribute("successMessage", "Video successfully uploaded!");
        } catch (IOException e) {
            // Handle file IO exception
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload video.");
        }

        return "redirect:/upload";
    }
}
