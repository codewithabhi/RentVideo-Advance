package com.rentvideo.service;

import com.rentvideo.domain.Rental;
import com.rentvideo.domain.RentalStatus;
import com.rentvideo.domain.User;
import com.rentvideo.domain.Video;
import com.rentvideo.exception.BusinessException;
import com.rentvideo.exception.ResourceNotFoundException;
import com.rentvideo.repository.RentalRepository;
import com.rentvideo.repository.UserRepository;
import com.rentvideo.repository.VideoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class RentalService {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository, VideoRepository videoRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
    }

    public Rental rentVideo(Long userId, Long videoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + videoId));

        if (!video.isAvailable()) {
            throw new BusinessException("Video is not available for rent");
        }
        if (rentalRepository.existsByUserIdAndVideoIdAndReturnedAtIsNull(userId, videoId)) {
            throw new BusinessException("User already has this video rented");
        }
        if (rentalRepository.countByUserIdAndReturnedAtIsNull(userId) >= 2) {
            throw new BusinessException("User already has two active rentals");
        }

        Rental rental = new Rental(user, video);
        rentalRepository.save(rental);
        video.setAvailable(false);
        videoRepository.save(video);
        return rental;
    }

    public Rental returnVideo(Long userId, Long videoId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found with id: " + videoId));

        Rental rental = rentalRepository.findTopByUserIdAndVideoIdAndReturnedAtIsNull(userId, videoId)
                .orElseThrow(() -> new BusinessException("No active rental found for this user and video"));

        rental.setReturnedAt(LocalDateTime.now());
        rental.setStatus(RentalStatus.RETURNED);
        video.setAvailable(true);
        videoRepository.save(video);
        return rentalRepository.save(rental);
    }
}
