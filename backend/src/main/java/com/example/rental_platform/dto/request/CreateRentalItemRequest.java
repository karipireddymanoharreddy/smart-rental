
package com.example.rental_platform.dto.request;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRentalItemRequest {

    private String title;

    private String description;

    private String category;

    private BigDecimal pricePerDay;

    private String location;

    private MultipartFile image;
}