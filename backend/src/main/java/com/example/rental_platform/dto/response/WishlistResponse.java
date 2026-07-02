
package com.example.rental_platform.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponse {

    // =========================
    // WISHLIST ID
    // =========================
    private Long id;

    // =========================
    // RENTAL ITEM ID
    // =========================
    private Long rentalItemId;

    // =========================
    // RENTAL ITEM TITLE
    // =========================
    private String rentalItemTitle;

    // =========================
    // IMAGE URL
    // =========================
    private String imageUrl;

    // =========================
    // PRICE PER DAY
    // =========================
    private BigDecimal pricePerDay;

    // =========================
    // LOCATION
    // =========================
    private String location;

    // =========================
    // ADDED TO WISHLIST AT
    // =========================
    private LocalDateTime createdAt;

}