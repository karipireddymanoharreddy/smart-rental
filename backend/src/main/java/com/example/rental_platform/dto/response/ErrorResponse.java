
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
public class ErrorResponse {

    // =========================
    // SUCCESS FLAG
    // =========================
    private boolean success;

    // =========================
    // ERROR MESSAGE
    // =========================
    private String message;

    // =========================
    // HTTP STATUS
    // =========================
    private int status;

    // =========================
    // TIMESTAMP
    // =========================
    private LocalDateTime timestamp;

}