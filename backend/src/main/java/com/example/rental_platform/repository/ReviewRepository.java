
package com.example.rental_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.rental_platform.model.Review;

@Repository
public interface ReviewRepository
        extends JpaRepository<Review, Long> {

    // =========================
    // GET REVIEWS OF A RENTAL ITEM
    // =========================
    List<Review> findByRentalItemIdOrderByCreatedAtDesc(
            Long rentalItemId);

    // =========================
    // CHECK IF REVIEW ALREADY EXISTS
    // =========================
    boolean existsByBookingId(Long bookingId);

    // =========================
    // FIND REVIEW BY BOOKING
    // =========================
    Optional<Review> findByBookingId(Long bookingId);

    // =========================
    // COUNT REVIEWS OF RENTAL ITEM
    // =========================
    long countByRentalItemId(Long rentalItemId);

    // =========================
    // GET AVERAGE RATING
    // =========================
    @Query("""
           SELECT AVG(r.rating)
           FROM Review r
           WHERE r.rentalItem.id = :rentalItemId
           """)
    Double getAverageRating(
            @Param("rentalItemId") Long rentalItemId);

}