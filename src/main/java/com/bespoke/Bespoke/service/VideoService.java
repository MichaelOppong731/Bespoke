package com.bespoke.Bespoke.service;

import com.bespoke.Bespoke.entities.Video;
import com.bespoke.Bespoke.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    VideoRepository videoRepository;

    public Video uploadVideo(Video video){
        return videoRepository.save(video);

    }

    public List<Video> getAllVideos(){
        return videoRepository.findAll();
    }

    public Optional<Video> getVideoById(Integer id) {
        return videoRepository.findById(id);
    }
}
