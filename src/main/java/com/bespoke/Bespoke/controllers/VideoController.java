package com.bespoke.Bespoke.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.entities.Video;
import com.bespoke.Bespoke.service.AppUserService;
import com.bespoke.Bespoke.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class VideoController {

    @Autowired
    private AmazonS3 amazonS3; // Add AmazonS3 client

    @Autowired
    AppUserService appUserService;

    @Autowired
    private VideoService videoService;

    @Value("${aws.s3.bucket.name}")  // Get bucket name from properties
    private String bucketName;


    //GET ALL VIDEOS
    @GetMapping("/api/v1/videos")
    @ResponseBody
    public List<Video> getVideos() {
        List<Video> videos = videoService.getAllVideos();
        return videos;
    }

    //GET VIDEOS PAGE
    @GetMapping("/videos")
    public String getVideos(Model model, Principal principal) {
        // Load the user details from appUserService
        AppUser userDetails = (AppUser) appUserService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        return "videosPage";
    }

    // GET UPLOAD VIDEO PAGE
    @GetMapping("/upload")
    public String showUploadForm(Model model, Principal principal) {
        // Load the user details from appUserService
        AppUser userDetails = (AppUser) appUserService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        // Check from database if there are videos
        //If no there are no videos, the "watch videos" tab won't be displayed to the user
        List<Video> videos = videoService.getAllVideos();
        model.addAttribute("videos", videos);

        return "upload_video"; // Assuming this is your upload form template
    }

    @GetMapping("/videos/{id}")
    public String showVideoPage(@PathVariable("id") Integer id, Model model, Principal principal) {
        // Load the user details from appUserService
        AppUser userDetails = (AppUser) appUserService.loadUserByUsername(principal.getName());
        model.addAttribute("userdetail", userDetails);

        Optional<Video> optionalVideo = videoService.getVideoById(id);
        if (optionalVideo.isPresent()) {
            model.addAttribute("video", optionalVideo.get());
            // Add other necessary attributes to the model
            return "videosPage";
        } else {
            return "errorPage"; // Or handle the error differently
        }
    }


    // UPLOAD VIDEO METHOD
    @PostMapping("/upload")
    public String uploadVideo(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Generate a unique object key
            String objectKey = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Upload to S3
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectRequest request = new PutObjectRequest(bucketName, objectKey, file.getInputStream(), metadata);
            amazonS3.putObject(request);


            // SAVE VIDEO DETAILS TO DATABASE USING OBJECTKEY AS THE PATH
            Video video = new Video(title, description, objectKey, file.getSize()); // Use objectKey, not fileName
            videoService.uploadVideo(video);

            redirectAttributes.addFlashAttribute("successMessage", "Video successfully uploaded!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload video.");
        }

        return "redirect:/upload";
    }

    // VIDEO PLAYBACK METHOD THAT STREAMS THE VIDEO FROM S3 BUCKET
    @GetMapping(value = "/video/{id}/play", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<StreamingResponseBody> playVideo(@PathVariable("id") Integer id, @RequestHeader(value = "Range", required = false) String rangeHeader) {
        Optional<Video> optionalVideo = videoService.getVideoById(id);
        if (optionalVideo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Video video = optionalVideo.get();
        String filePath = video.getFilePath();

        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, filePath));
        InputStream inputStream = s3Object.getObjectContent();
        long contentLength = s3Object.getObjectMetadata().getContentLength();
        long start = 0;
        long end = contentLength - 1;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            try {
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    end = Long.parseLong(ranges[1]);
                }
            } catch (NumberFormatException ignored) {
                // If there's an error in parsing the range, ignore it and use full length
            }
        }

        final long finalStart = start;
        final long finalEnd = end;
        StreamingResponseBody responseBody = outputStream -> {
            inputStream.skip(finalStart);
            byte[] buffer = new byte[1024];
            long bytesRead = 0;
            while (bytesRead <= finalEnd - finalStart) {
                int read = inputStream.read(buffer);
                if (read == -1) {
                    break;
                }
                outputStream.write(buffer, 0, read);
                bytesRead += read;
            }
            inputStream.close();
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("video/mp4"));
        headers.add("Content-Range", "bytes " + finalStart + "-" + finalEnd + "/" + contentLength);
        headers.setContentLength(finalEnd - finalStart + 1);
        return new ResponseEntity<>(responseBody, headers, HttpStatus.PARTIAL_CONTENT);
    }

}






