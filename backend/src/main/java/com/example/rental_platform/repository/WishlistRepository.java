
package com.example.rental_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rental_platform.model.Wishlist;

@Repository
public interface WishlistRepository
        extends JpaRepository<Wishlist, Long> {

    // =========================
    // CHECK IF ITEM IS ALREADY IN WISHLIST
    // =========================
    boolean existsByUserIdAndRentalItemId(
            Long userId,
            Long rentalItemId);

    // =========================
    // GET USER WISHLIST
    // =========================
    List<Wishlist> findByUserIdOrderByCreatedAtDesc(
            Long userId);

    // =========================
    // REMOVE ITEM FROM WISHLIST
    // =========================
    void deleteByUserIdAndRentalItemId(
            Long userId,
            Long rentalItemId);

    // =========================
    // GET A SPECIFIC WISHLIST ENTRY
    // =========================
    Wishlist findByUserIdAndRentalItemId(
            Long userId,
            Long rentalItemId);

    // =========================
    // COUNT USER WISHLIST ITEMS
    // =========================
    long countByUserId(Long userId);

}