package com.bespoke.Bespoke.controllers;

import com.bespoke.Bespoke.entities.Video;
import com.bespoke.Bespoke.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class VideoController {
    @Autowired
    private VideoService videoService;
    @PostMapping("/save-video")
    public ResponseEntity<?> saveVideo(@RequestBody Video video){
        return new ResponseEntity<Video>(videoService.uploadVideo(video), HttpStatus.OK);
    }

    @GetMapping("/all-videos")
    public ResponseEntity<?> getAllVideos(){
        return new ResponseEntity<List<Video>>(videoService.getAllVideos(),HttpStatus.OK);
    }

    @GetMapping("{id}")
    public Video getVideoById(@PathVariable Integer id){
        return videoService.getById(id);
    }

}
