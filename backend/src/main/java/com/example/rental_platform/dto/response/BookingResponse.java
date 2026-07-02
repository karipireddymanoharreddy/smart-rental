
package com.example.rental_platform.dto.response;

import java.time.LocalDate;

import com.example.rental_platform.enums.BookingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {

    private Long id;

    private String renterName;

    private String rentalItemTitle;

    private LocalDate startDate;

    private LocalDate endDate;

    private BookingStatus status;

}