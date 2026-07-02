
package com.example.rental_platform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.WishlistResponse;
import com.example.rental_platform.service.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    // =========================
    // SERVICE
    // =========================
    private final WishlistService wishlistService;

    // =========================
    // ADD TO WISHLIST
    // =========================
    @PostMapping("/{rentalItemId}")
    public ApiResponse<String> addToWishlist(
            @PathVariable Long rentalItemId) {

        return wishlistService.addToWishlist(rentalItemId);
    }

    // =========================
    // GET MY WISHLIST
    // =========================
    @GetMapping
    public ApiResponse<List<WishlistResponse>> getMyWishlist() {

        return wishlistService.getMyWishlist();
    }

    // =========================
// REMOVE FROM WISHLIST
// =========================
@DeleteMapping("/{rentalItemId}")
public ApiResponse<String> removeFromWishlist(
        @PathVariable Long rentalItemId) {

    return wishlistService.removeFromWishlist(
            rentalItemId);
}



// =========================
// CHECK IF ITEM IS IN WISHLIST
// =========================
@GetMapping("/{rentalItemId}/exists")
public ApiResponse<Boolean> isInWishlist(
        @PathVariable Long rentalItemId) {

    return wishlistService.isInWishlist(
            rentalItemId);
}

// =========================
// GET WISHLIST COUNT
// =========================
@GetMapping("/count")
public ApiResponse<Long> getWishlistCount() {

    return wishlistService.getWishlistCount();
}



}