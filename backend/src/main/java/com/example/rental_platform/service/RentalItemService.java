
package com.example.rental_platform.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.rental_platform.dto.request.CreateRentalItemRequest;
import com.example.rental_platform.dto.request.UpdateRentalItemRequest;
import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.PaginationResponse;
import com.example.rental_platform.dto.response.RentalItemResponse;
import com.example.rental_platform.exception.RentalItemNotFoundException;
import com.example.rental_platform.exception.UserNotFoundException;
import com.example.rental_platform.model.RentalItem;
import com.example.rental_platform.model.User;
import com.example.rental_platform.repository.RentalItemRepository;
import com.example.rental_platform.repository.UserRepository;
import com.example.rental_platform.specification.RentalItemSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RentalItemService {

    private final RentalItemRepository rentalItemRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    private static final List<String> ALLOWED_SORT_FIELDS = List.of(
            "id",
            "title",
            "pricePerDay",
            "category",
            "location");

    // =========================
    // CREATE RENTAL ITEM
    // =========================
    public ApiResponse<RentalItemResponse> createRentalItem(
            CreateRentalItemRequest request) {

        // Get logged-in user
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        // Find user in database
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        // Upload image to Cloudinary
        String imageUrl = cloudinaryService.uploadImage(request.getImage());

        // Create Rental Item
        RentalItem rentalItem = RentalItem.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .pricePerDay(request.getPricePerDay())
                .location(request.getLocation())
                .imageUrl(imageUrl)
                .available(true)
                .owner(owner)
                .build();

        // Save Rental Item
        rentalItem = rentalItemRepository.save(rentalItem);

        // Convert Entity -> DTO
        RentalItemResponse response = RentalItemResponse.builder()
                .id(rentalItem.getId())
                .title(rentalItem.getTitle())
                .description(rentalItem.getDescription())
                .category(rentalItem.getCategory())
                .pricePerDay(rentalItem.getPricePerDay())
                .location(rentalItem.getLocation())
                .available(rentalItem.getAvailable())
                .imageUrl(rentalItem.getImageUrl())
                .ownerName(rentalItem.getOwner().getName())
                .build();

        return ApiResponse.<RentalItemResponse>builder()
                .success(true)
                .message("Rental item created successfully")
                .data(response)
                .build();
    }

    // =========================
    // GET ALL RENTAL ITEMS
    // =========================
    public ApiResponse<List<RentalItemResponse>> getAllRentalItems() {

        List<RentalItemResponse> rentalItems = rentalItemRepository.findAll()
                .stream()
                .map(item -> RentalItemResponse.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .category(item.getCategory())
                        .pricePerDay(item.getPricePerDay())
                        .location(item.getLocation())
                        .available(item.getAvailable())
                        .imageUrl(item.getImageUrl())
                        .ownerName(item.getOwner().getName())
                        .build())
                .toList();

        return ApiResponse.<List<RentalItemResponse>>builder()
                .success(true)
                .message("Rental items fetched successfully")
                .data(rentalItems)
                .build();
    }

    // =========================
    // GET ALL RENTAL ITEMS (PAGINATION + SORTING + CATEGORY/LOCATION/PRICE/KEYWORD FILTER)
    // =========================
    public ApiResponse<PaginationResponse<RentalItemResponse>> getAllRentalItems(
            int page,
            int size,
            String sortBy,
            String direction,
            String category,
            String location,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String keyword) {

        // =========================
        // VALIDATE PAGE NUMBER
        // =========================
        if (page < 0) {

            throw new IllegalArgumentException(
                    "Page number cannot be negative");
        }

        // =========================
        // VALIDATE PAGE SIZE
        // =========================
        if (size <= 0 || size > 100) {

            throw new IllegalArgumentException(
                    "Page size must be between 1 and 100");
        }

        // =========================
        // VALIDATE SORT FIELD
        // =========================
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {

            throw new IllegalArgumentException(
                    "Invalid sort field: " + sortBy);
        }

        direction = direction.trim().toLowerCase();

        Sort sort = direction.equals("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        sort);

        // =========================
        // TRIM FILTER INPUTS
        // =========================
        if (category != null) {
            category = category.trim();
        }

        if (location != null) {
            location = location.trim();
        }

        if (keyword != null) {
            keyword = keyword.trim();
        }

        Specification<RentalItem> specification =
                Specification
                        .where(
                                RentalItemSpecification.hasCategory(category))
                        .and(
                                RentalItemSpecification.hasLocation(location))
                        .and(
                                RentalItemSpecification.hasPriceBetween(
                                        minPrice,
                                        maxPrice))
                        .and(
                                RentalItemSpecification.hasTitle(keyword));

        Page<RentalItem> rentalItemPage =
                rentalItemRepository.findAll(
                        specification,
                        pageable);

        PaginationResponse<RentalItemResponse> response =
                PaginationResponse.<RentalItemResponse>builder()
                        .content(
                                rentalItemPage
                                        .getContent()
                                        .stream()
                                        .map(item -> RentalItemResponse.builder()
                                                .id(item.getId())
                                                .title(item.getTitle())
                                                .description(item.getDescription())
                                                .category(item.getCategory())
                                                .pricePerDay(item.getPricePerDay())
                                                .location(item.getLocation())
                                                .available(item.getAvailable())
                                                .imageUrl(item.getImageUrl())
                                                .ownerName(item.getOwner().getName())
                                                .build())
                                        .toList())
                        .currentPage(rentalItemPage.getNumber())
                        .pageSize(rentalItemPage.getSize())
                        .totalPages(rentalItemPage.getTotalPages())
                        .totalElements(rentalItemPage.getTotalElements())
                        .first(rentalItemPage.isFirst())
                        .last(rentalItemPage.isLast())
                        .build();

        return ApiResponse
                .<PaginationResponse<RentalItemResponse>>builder()
                .success(true)
                .message("Rental items fetched successfully")
                .data(response)
                .build();
    }

    // =========================
    // GET RENTAL ITEM BY ID
    // =========================
    public ApiResponse<RentalItemResponse> getRentalItemById(Long id) {

        RentalItem rentalItem = rentalItemRepository.findById(id)
                .orElseThrow(() ->
                        new RentalItemNotFoundException("Rental item not found"));

        RentalItemResponse response = RentalItemResponse.builder()
                .id(rentalItem.getId())
                .title(rentalItem.getTitle())
                .description(rentalItem.getDescription())
                .category(rentalItem.getCategory())
                .pricePerDay(rentalItem.getPricePerDay())
                .location(rentalItem.getLocation())
                .available(rentalItem.getAvailable())
                .imageUrl(rentalItem.getImageUrl())
                .ownerName(rentalItem.getOwner().getName())
                .build();

        return ApiResponse.<RentalItemResponse>builder()
                .success(true)
                .message("Rental item fetched successfully")
                .data(response)
                .build();
    }

    // =========================
    // UPDATE RENTAL ITEM
    // =========================
    public ApiResponse<RentalItemResponse> updateRentalItem(
            Long id,
            UpdateRentalItemRequest request) {

        RentalItem rentalItem = rentalItemRepository.findById(id)
                .orElseThrow(() ->
                        new RentalItemNotFoundException("Rental item not found"));

        // Update image if a new image is uploaded
        if (request.getImage() != null && !request.getImage().isEmpty()) {

            // Delete old image from Cloudinary
            if (rentalItem.getImageUrl() != null &&
                    !rentalItem.getImageUrl().isBlank()) {

                cloudinaryService.deleteImage(rentalItem.getImageUrl());
            }

            // Upload new image
            String imageUrl = cloudinaryService.uploadImage(request.getImage());

            // Save new image URL
            rentalItem.setImageUrl(imageUrl);
        }

        // Update other fields
        rentalItem.setTitle(request.getTitle());
        rentalItem.setDescription(request.getDescription());
        rentalItem.setCategory(request.getCategory());
        rentalItem.setPricePerDay(request.getPricePerDay());
        rentalItem.setLocation(request.getLocation());
        rentalItem.setAvailable(request.getAvailable());

        // Save updated item
        rentalItem = rentalItemRepository.save(rentalItem);

        // Convert Entity -> DTO
        RentalItemResponse response = RentalItemResponse.builder()
                .id(rentalItem.getId())
                .title(rentalItem.getTitle())
                .description(rentalItem.getDescription())
                .category(rentalItem.getCategory())
                .pricePerDay(rentalItem.getPricePerDay())
                .location(rentalItem.getLocation())
                .available(rentalItem.getAvailable())
                .imageUrl(rentalItem.getImageUrl())
                .ownerName(rentalItem.getOwner().getName())
                .build();

        return ApiResponse.<RentalItemResponse>builder()
                .success(true)
                .message("Rental item updated successfully")
                .data(response)
                .build();
    }

    // =========================
    // GET RENTAL ITEMS WITH PAGINATION (LEGACY - RETURNS Page<>)
    // =========================
    public ApiResponse<Page<RentalItemResponse>> getRentalItems(
            int page,
            int size,
            String sortBy,
            String direction) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(direction), sortBy));

        Page<RentalItemResponse> rentalItems = rentalItemRepository
                .findAll(pageable)
                .map(item -> RentalItemResponse.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .category(item.getCategory())
                        .pricePerDay(item.getPricePerDay())
                        .location(item.getLocation())
                        .available(item.getAvailable())
                        .imageUrl(item.getImageUrl())
                        .ownerName(item.getOwner().getName())
                        .build());

        return ApiResponse.<Page<RentalItemResponse>>builder()
                .success(true)
                .message("Rental items fetched successfully")
                .data(rentalItems)
                .build();
    }

    // =========================
    // SEARCH RENTAL ITEMS BY TITLE
    // =========================
    public ApiResponse<List<RentalItemResponse>> searchRentalItems(String keyword) {

        List<RentalItemResponse> rentalItems = rentalItemRepository
                .findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(item -> RentalItemResponse.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .category(item.getCategory())
                        .pricePerDay(item.getPricePerDay())
                        .location(item.getLocation())
                        .available(item.getAvailable())
                        .imageUrl(item.getImageUrl())
                        .ownerName(item.getOwner().getName())
                        .build())
                .toList();

        return ApiResponse.<List<RentalItemResponse>>builder()
                .success(true)
                .message("Rental items fetched successfully")
                .data(rentalItems)
                .build();
    }

    // =========================
    // SEARCH RENTAL ITEMS BY CATEGORY
    // =========================
    public ApiResponse<List<RentalItemResponse>> searchRentalItemsByCategory(
            String category) {

        List<RentalItemResponse> rentalItems = rentalItemRepository
                .findByCategoryIgnoreCase(category)
                .stream()
                .map(item -> RentalItemResponse.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .category(item.getCategory())
                        .pricePerDay(item.getPricePerDay())
                        .location(item.getLocation())
                        .available(item.getAvailable())
                        .imageUrl(item.getImageUrl())
                        .ownerName(item.getOwner().getName())
                        .build())
                .toList();

        return ApiResponse.<List<RentalItemResponse>>builder()
                .success(true)
                .message("Rental items fetched successfully")
                .data(rentalItems)
                .build();
    }

    // =========================
    // FILTER RENTAL ITEMS BY LOCATION
    // =========================
    public ApiResponse<List<RentalItemResponse>> filterRentalItemsByLocation(
            String location) {

        List<RentalItemResponse> rentalItems = rentalItemRepository
                .findByLocationIgnoreCase(location)
                .stream()
                .map(item -> RentalItemResponse.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .category(item.getCategory())
                        .pricePerDay(item.getPricePerDay())
                        .location(item.getLocation())
                        .available(item.getAvailable())
                        .imageUrl(item.getImageUrl())
                        .ownerName(item.getOwner().getName())
                        .build())
                .toList();

        return ApiResponse.<List<RentalItemResponse>>builder()
                .success(true)
                .message("Rental items fetched successfully")
                .data(rentalItems)
                .build();
    }

    // =========================
    // FILTER RENTAL ITEMS BY PRICE RANGE
    // =========================
    public ApiResponse<List<RentalItemResponse>> filterRentalItemsByPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        List<RentalItemResponse> rentalItems = rentalItemRepository
                .findByPricePerDayBetween(minPrice, maxPrice)
                .stream()
                .map(item -> RentalItemResponse.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .category(item.getCategory())
                        .pricePerDay(item.getPricePerDay())
                        .location(item.getLocation())
                        .available(item.getAvailable())
                        .imageUrl(item.getImageUrl())
                        .ownerName(item.getOwner().getName())
                        .build())
                .toList();

        return ApiResponse.<List<RentalItemResponse>>builder()
                .success(true)
                .message("Rental items fetched successfully")
                .data(rentalItems)
                .build();
    }

    // =========================
    // FILTER RENTAL ITEMS
    // =========================
    public ApiResponse<List<RentalItemResponse>> filterRentalItems(

            String keyword,

            String category,

            String location,

            BigDecimal minPrice,

            BigDecimal maxPrice,

            String sortBy,

            String direction) {

        Specification<RentalItem> specification =
                Specification.where(
                        RentalItemSpecification.hasTitle(keyword))
                        .and(RentalItemSpecification.hasCategory(category))
                        .and(RentalItemSpecification.hasLocation(location))
                        .and(RentalItemSpecification.hasPriceBetween(
                                minPrice,
                                maxPrice));

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        List<RentalItemResponse> rentalItems = rentalItemRepository
                .findAll(specification, sort)
                .stream()
                .map(item -> RentalItemResponse.builder()
                        .id(item.getId())
                        .title(item.getTitle())
                        .description(item.getDescription())
                        .category(item.getCategory())
                        .pricePerDay(item.getPricePerDay())
                        .location(item.getLocation())
                        .available(item.getAvailable())
                        .imageUrl(item.getImageUrl())
                        .ownerName(item.getOwner().getName())
                        .build())
                .toList();

        return ApiResponse.<List<RentalItemResponse>>builder()
                .success(true)
                .message("Rental items fetched successfully")
                .data(rentalItems)
                .build();
    }

    // =========================
    // DELETE RENTAL ITEM
    // =========================
    public ApiResponse<String> deleteRentalItem(Long id) {

        RentalItem rentalItem = rentalItemRepository.findById(id)
                .orElseThrow(() ->
                        new RentalItemNotFoundException("Rental item not found"));

        // Delete image from Cloudinary
        if (rentalItem.getImageUrl() != null &&
                !rentalItem.getImageUrl().isBlank()) {

            cloudinaryService.deleteImage(rentalItem.getImageUrl());
        }

        // Delete Rental Item
        rentalItemRepository.delete(rentalItem);

        return ApiResponse.<String>builder()
                .success(true)
                .message("Rental item deleted successfully")
                .data("Deleted Successfully")
                .build();
    }
}