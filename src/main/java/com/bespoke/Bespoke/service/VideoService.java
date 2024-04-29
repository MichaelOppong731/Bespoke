package com.bespoke.Bespoke.service;

import com.bespoke.Bespoke.entities.Video;
import com.bespoke.Bespoke.exceptionhandlers.ResourceNotFoundException;
import com.bespoke.Bespoke.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class VideoService {

    @Autowired
    VideoRepository videoRepository;

    public Video uploadVideo(Video video){
        if (video.getTitle().isEmpty()){
            throw new ResourceNotFoundException(false,"Video Title cannot be empty");
        }
        try {
            video.setDateCreated(new Date());
            return videoRepository.save(video);
        }catch (Exception e){
            throw new ResourceNotFoundException(false, "something went wrong");
        }
    }

    public Video getById(Integer id){
        Video video = videoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(false, "Video not found"));
        return video;
    }

    public Video updateVideo(Video video, Integer id){
        Video oldVideo = videoRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException(false, "Video Not found"));
        oldVideo.setTitle(video.getTitle());
        oldVideo.setDescription(video.getDescription());
        oldVideo.setVideoName(video.getVideoName());
        oldVideo.setGenre(video.getGenre());
        oldVideo.setDateCreated(new Date());
        videoRepository.save(oldVideo);
        return oldVideo;
    }

    public void deleteVideo(Integer id){


    }

    public List<Video> getAllVideos(){
        return videoRepository.findAll();
    }
}
