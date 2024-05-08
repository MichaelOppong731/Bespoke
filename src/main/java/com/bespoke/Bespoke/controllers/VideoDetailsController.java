package com.bespoke.Bespoke.controllers;

import com.bespoke.Bespoke.entities.Video;
import com.bespoke.Bespoke.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/videos")
public class VideoDetailsController {

    @Autowired
    private VideoService videoService;


    // This endpoint fetches all the videos and render them as List
    @GetMapping
    public List<Video> getVideos() {
        List<Video> videos = videoService.getAllVideos();
        return videos;
    }
}
