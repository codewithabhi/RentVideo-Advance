package com.rentvideo.controller;

import com.rentvideo.domain.Video;
import com.rentvideo.dto.VideoRequest;
import com.rentvideo.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/videos")
    public ResponseEntity<List<Video>> getAllAvailableVideos() {
        return ResponseEntity.ok(videoService.getAllAvailableVideos());
    }

    @GetMapping("/videos/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.getVideoById(id));
    }

    @PostMapping("/videos")
    public ResponseEntity<Video> createVideo(@Valid @RequestBody VideoRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDirector(request.getDirector());
        video.setGenre(request.getGenre());
        video.setAvailable(request.getAvailable() == null || request.getAvailable());

        return ResponseEntity.ok(videoService.createVideo(video));
    }

    @PutMapping("/videos/{id}")
    public ResponseEntity<Video> updateVideo(@PathVariable Long id, @Valid @RequestBody VideoRequest request) {
        Video video = new Video();
        video.setTitle(request.getTitle());
        video.setDirector(request.getDirector());
        video.setGenre(request.getGenre());
        video.setAvailable(request.getAvailable() == null || request.getAvailable());

        return ResponseEntity.ok(videoService.updateVideo(id, video));
    }

    @DeleteMapping("/videos/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.noContent().build();
    }
}
