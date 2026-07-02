
package com.example.rental_platform.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.rental_platform.dto.request.ReviewRequest;
import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.RatingSummaryResponse;
import com.example.rental_platform.dto.response.ReviewResponse;
import com.example.rental_platform.exception.BookingNotFoundException;
import com.example.rental_platform.exception.UserNotFoundException;
import com.example.rental_platform.model.Booking;
import com.example.rental_platform.model.Review;
import com.example.rental_platform.model.User;
import com.example.rental_platform.repository.BookingRepository;
import com.example.rental_platform.repository.RentalItemRepository;
import com.example.rental_platform.repository.ReviewRepository;
import com.example.rental_platform.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    // =========================
    // REPOSITORIES
    // =========================
    private final ReviewRepository reviewRepository;

    private final BookingRepository bookingRepository;

    private final RentalItemRepository rentalItemRepository;

    private final UserRepository userRepository;

    // =========================
    // GET LOGGED-IN USER
    // =========================
    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"));
    }



// =========================
// ADD REVIEW
// =========================
public ApiResponse<String> addReview(
        Long bookingId,
        ReviewRequest request) {

    // Get logged-in user
    User user = getLoggedInUser();

    // Get booking
    Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() ->
                    new BookingNotFoundException(
                            "Booking not found"));

    // Only renter can review
    if (!booking.getRenter().getId().equals(user.getId())) {

        throw new IllegalArgumentException(
                "Only the renter can submit a review");
    }

    // Prevent duplicate review
    if (reviewRepository.existsByBookingId(bookingId)) {

        throw new IllegalArgumentException(
                "Review already submitted for this booking");
    }

    // Create review
    Review review = Review.builder()
            .booking(booking)
            .rentalItem(booking.getRentalItem())
            .reviewer(user)
            .rating(request.getRating())
            .comment(request.getComment())
            .createdAt(LocalDateTime.now())
            .build();

    reviewRepository.save(review);

    return ApiResponse.<String>builder()
            .success(true)
            .message("Review submitted successfully")
            .data(null)
            .build();
}


// =========================
// GET REVIEWS OF RENTAL ITEM
// =========================
public ApiResponse<List<ReviewResponse>> getReviewsByRentalItem(
        Long rentalItemId) {

    List<ReviewResponse> reviews = reviewRepository
            .findByRentalItemIdOrderByCreatedAtDesc(rentalItemId)
            .stream()
            .map(review -> ReviewResponse.builder()
                    .id(review.getId())
                    .reviewerName(review.getReviewer().getName())
                    .rating(review.getRating())
                    .comment(review.getComment())
                    .createdAt(review.getCreatedAt())
                    .build())
            .collect(Collectors.toList());

    return ApiResponse.<List<ReviewResponse>>builder()
            .success(true)
            .message("Reviews fetched successfully")
            .data(reviews)
            .build();
}



// =========================
// GET AVERAGE RATING
// =========================
public ApiResponse<Double> getAverageRating(
        Long rentalItemId) {

    Double average =
            reviewRepository.getAverageRating(
                    rentalItemId);

    if (average == null) {
        average = 0.0;
    }

    return ApiResponse.<Double>builder()
            .success(true)
            .message("Average rating fetched successfully")
            .data(average)
            .build();
}



// =========================
// GET RATING SUMMARY
// =========================
public ApiResponse<RatingSummaryResponse> getRatingSummary(
        Long rentalItemId) {

    Double average =
            reviewRepository.getAverageRating(rentalItemId);

    if (average == null) {
        average = 0.0;
    }

    long totalReviews =
            reviewRepository.countByRentalItemId(rentalItemId);

    RatingSummaryResponse response =
            RatingSummaryResponse.builder()
                    .averageRating(average)
                    .totalReviews(totalReviews)
                    .build();

    return ApiResponse.<RatingSummaryResponse>builder()
            .success(true)
            .message("Rating summary fetched successfully")
            .data(response)
            .build();
}



}