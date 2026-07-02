
package com.example.rental_platform.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rental_platform.dto.request.ReviewRequest;
import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.RatingSummaryResponse;
import com.example.rental_platform.dto.response.ReviewResponse;
import com.example.rental_platform.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Validated
public class ReviewController {

    // =========================
    // SERVICE
    // =========================
    private final ReviewService reviewService;

    // =========================
    // ADD REVIEW
    // =========================
    @PostMapping("/{bookingId}")
    public ApiResponse<String> addReview(
            @PathVariable Long bookingId,
            @Valid @RequestBody ReviewRequest request) {

        return reviewService.addReview(
                bookingId,
                request);
    }

    // =========================
    // GET REVIEWS OF RENTAL ITEM
    // =========================
    @GetMapping("/rental-item/{rentalItemId}")
    public ApiResponse<List<ReviewResponse>> getReviews(
            @PathVariable Long rentalItemId) {

        return reviewService.getReviewsByRentalItem(
                rentalItemId);
    }

    // =========================
    // GET AVERAGE RATING
    // =========================
    @GetMapping("/rental-item/{rentalItemId}/average")
    public ApiResponse<Double> getAverageRating(
            @PathVariable Long rentalItemId) {

        return reviewService.getAverageRating(
                rentalItemId);
    }



// =========================
// GET RATING SUMMARY
// =========================
@GetMapping("/rental-item/{rentalItemId}/summary")
public ApiResponse<RatingSummaryResponse> getRatingSummary(
        @PathVariable Long rentalItemId) {

    return reviewService.getRatingSummary(
            rentalItemId);
}



}