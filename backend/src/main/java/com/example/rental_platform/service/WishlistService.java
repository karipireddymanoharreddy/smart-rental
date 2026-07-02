
package com.example.rental_platform.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.WishlistResponse;
import com.example.rental_platform.exception.RentalItemNotFoundException;
import com.example.rental_platform.exception.UserNotFoundException;
import com.example.rental_platform.model.RentalItem;
import com.example.rental_platform.model.User;
import com.example.rental_platform.model.Wishlist;
import com.example.rental_platform.repository.RentalItemRepository;
import com.example.rental_platform.repository.UserRepository;
import com.example.rental_platform.repository.WishlistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistService {

    // =========================
    // REPOSITORIES
    // =========================
    private final WishlistRepository wishlistRepository;

    private final UserRepository userRepository;

    private final RentalItemRepository rentalItemRepository;

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
// ADD TO WISHLIST
// =========================
public ApiResponse<String> addToWishlist(Long rentalItemId) {

    // Get logged-in user
    User user = getLoggedInUser();

    // Get rental item
    RentalItem rentalItem = rentalItemRepository
            .findById(rentalItemId)
            .orElseThrow(() ->
                    new RentalItemNotFoundException(
                            "Rental item not found"));

    // Prevent duplicate wishlist entries
    if (wishlistRepository.existsByUserIdAndRentalItemId(
            user.getId(),
            rentalItemId)) {

        throw new IllegalArgumentException(
                "Item is already in your wishlist");
    }

    // Create wishlist entry
    Wishlist wishlist = Wishlist.builder()
            .user(user)
            .rentalItem(rentalItem)
            .createdAt(LocalDateTime.now())
            .build();

    wishlistRepository.save(wishlist);

    return ApiResponse.<String>builder()
            .success(true)
            .message("Item added to wishlist successfully")
            .data(null)
            .build();
}



// =========================
// GET MY WISHLIST
// =========================
public ApiResponse<List<WishlistResponse>> getMyWishlist() {

    // Get logged-in user
    User user = getLoggedInUser();

    // Fetch wishlist
    List<WishlistResponse> wishlist = wishlistRepository
            .findByUserIdOrderByCreatedAtDesc(user.getId())
            .stream()
            .map(item -> WishlistResponse.builder()
                    .id(item.getId())
                    .rentalItemId(item.getRentalItem().getId())
                    .rentalItemTitle(item.getRentalItem().getTitle())
                    .imageUrl(item.getRentalItem().getImageUrl())
                    .pricePerDay(item.getRentalItem().getPricePerDay())
                    .location(item.getRentalItem().getLocation())
                    .createdAt(item.getCreatedAt())
                    .build())
            .collect(Collectors.toList());

    return ApiResponse.<List<WishlistResponse>>builder()
            .success(true)
            .message("Wishlist fetched successfully")
            .data(wishlist)
            .build();
}



// =========================
// REMOVE FROM WISHLIST
// =========================
public ApiResponse<String> removeFromWishlist(
        Long rentalItemId) {

    // Get logged-in user
    User user = getLoggedInUser();

    // Check if item exists in wishlist
    Wishlist wishlist = wishlistRepository
            .findByUserIdAndRentalItemId(
                    user.getId(),
                    rentalItemId);

    if (wishlist == null) {

        throw new IllegalArgumentException(
                "Item is not in your wishlist");
    }

    // Remove wishlist entry
    wishlistRepository.delete(wishlist);

    return ApiResponse.<String>builder()
            .success(true)
            .message("Item removed from wishlist successfully")
            .data(null)
            .build();
}



// =========================
// CHECK IF ITEM IS IN WISHLIST
// =========================
public ApiResponse<Boolean> isInWishlist(
        Long rentalItemId) {

    User user = getLoggedInUser();

    boolean exists =
            wishlistRepository.existsByUserIdAndRentalItemId(
                    user.getId(),
                    rentalItemId);

    return ApiResponse.<Boolean>builder()
            .success(true)
            .message("Wishlist status fetched successfully")
            .data(exists)
            .build();
}

// =========================
// GET WISHLIST COUNT
// =========================
public ApiResponse<Long> getWishlistCount() {

    User user = getLoggedInUser();

    long count =
            wishlistRepository.countByUserId(
                    user.getId());

    return ApiResponse.<Long>builder()
            .success(true)
            .message("Wishlist count fetched successfully")
            .data(count)
            .build();
}


}