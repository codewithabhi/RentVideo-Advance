package com.rentvideo.service;

import com.rentvideo.domain.Video;
import com.rentvideo.exception.BusinessException;
import com.rentvideo.exception.ResourceNotFoundException;
import com.rentvideo.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VideoService {
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<Video> getAllAvailableVideos() {
        return videoRepository.findByAvailableTrueOrderByTitleAsc();
    }

    public Video createVideo(Video video) {
        if (video == null) {
            throw new BusinessException("Video details are required");
        }
        return videoRepository.save(video);
    }

    public Video getVideoById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + id));
    }

    public Video updateVideo(Long id, Video updatedVideo) {
        Video existing = getVideoById(id);
        existing.setTitle(updatedVideo.getTitle());
        existing.setDirector(updatedVideo.getDirector());
        existing.setGenre(updatedVideo.getGenre());
        existing.setAvailable(updatedVideo.isAvailable());
        return videoRepository.save(existing);
    }

    public void deleteVideo(Long id) {
        Video video = getVideoById(id);
        videoRepository.delete(video);
    }
}
