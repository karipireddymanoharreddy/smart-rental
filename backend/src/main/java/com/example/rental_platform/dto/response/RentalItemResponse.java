
package com.example.rental_platform.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalItemResponse {

    private Long id;

    private String title;

    private String description;

    private String category;

    private BigDecimal pricePerDay;

    private String location;

    private Boolean available;

    private String imageUrl;

    private String ownerName;
}