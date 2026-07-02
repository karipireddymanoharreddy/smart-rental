
package com.example.rental_platform.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    // =========================
    // REVIEW ID
    // =========================
    private Long id;

    // =========================
    // REVIEWER NAME
    // =========================
    private String reviewerName;

    // =========================
    // RATING
    // =========================
    private Integer rating;

    // =========================
    // COMMENT
    // =========================
    private String comment;

    // =========================
    // REVIEW DATE
    // =========================
    private LocalDateTime createdAt;

}