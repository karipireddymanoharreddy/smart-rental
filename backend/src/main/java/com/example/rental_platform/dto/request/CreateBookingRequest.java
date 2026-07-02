
package com.example.rental_platform.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CreateBookingRequest {

    private Long rentalItemId;

    private LocalDate startDate;

    private LocalDate endDate;

}