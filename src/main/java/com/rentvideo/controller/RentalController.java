package com.rentvideo.controller;

import com.rentvideo.domain.Rental;
import com.rentvideo.domain.User;
import com.rentvideo.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/videos/{videoId}/rent")
    public ResponseEntity<Rental> rentVideo(@AuthenticationPrincipal User currentUser, @PathVariable Long videoId) {
        Rental rental = rentalService.rentVideo(currentUser.getId(), videoId);
        return ResponseEntity.ok(rental);
    }

    @PostMapping("/videos/{videoId}/return")
    public ResponseEntity<Rental> returnVideo(@AuthenticationPrincipal User currentUser, @PathVariable Long videoId) {
        Rental rental = rentalService.returnVideo(currentUser.getId(), videoId);
        return ResponseEntity.ok(rental);
    }
}
