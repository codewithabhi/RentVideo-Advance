package com.rentvideo.repository;

import com.rentvideo.domain.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    long countByUserIdAndReturnedAtIsNull(Long userId);
    boolean existsByUserIdAndVideoIdAndReturnedAtIsNull(Long userId, Long videoId);
    Optional<Rental> findTopByUserIdAndVideoIdAndReturnedAtIsNull(Long userId, Long videoId);
}
