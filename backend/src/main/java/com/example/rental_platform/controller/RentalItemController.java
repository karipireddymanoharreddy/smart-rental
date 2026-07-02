
package com.example.rental_platform.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rental_platform.dto.request.CreateRentalItemRequest;
import com.example.rental_platform.dto.request.UpdateRentalItemRequest;
import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.PaginationResponse;
import com.example.rental_platform.dto.response.RentalItemResponse;
import com.example.rental_platform.service.RentalItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rental-items")
@RequiredArgsConstructor
public class RentalItemController {

    private final RentalItemService rentalItemService;

    // =========================
    // CREATE RENTAL ITEM
    // =========================
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<RentalItemResponse> createRentalItem(
            @ModelAttribute CreateRentalItemRequest request) {

        return rentalItemService.createRentalItem(request);
    }

    // =========================
    // GET ALL RENTAL ITEMS
    // =========================
    @GetMapping
    public ApiResponse<List<RentalItemResponse>> getAllRentalItems() {

        return rentalItemService.getAllRentalItems();
    }

    // =========================
    // GET RENTAL ITEMS WITH PAGINATION & SORTING
    // =========================
    @GetMapping("/page")
    public ApiResponse<Page<RentalItemResponse>> getRentalItems(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return rentalItemService.getRentalItems(
                page,
                size,
                sortBy,
                direction);
    }

    // =========================
    // SEARCH RENTAL ITEMS BY TITLE
    // =========================
    @GetMapping("/search")
    public ApiResponse<List<RentalItemResponse>> searchRentalItems(
            @RequestParam String keyword) {

        return rentalItemService.searchRentalItems(keyword);
    }

    // =========================
    // SEARCH RENTAL ITEMS BY CATEGORY
    // =========================
    @GetMapping("/category")
    public ApiResponse<List<RentalItemResponse>> searchRentalItemsByCategory(
            @RequestParam String category) {

        return rentalItemService.searchRentalItemsByCategory(category);
    }

    // =========================
    // FILTER RENTAL ITEMS BY LOCATION
    // =========================
    @GetMapping("/location")
    public ApiResponse<List<RentalItemResponse>> filterRentalItemsByLocation(
            @RequestParam String location) {

        return rentalItemService.filterRentalItemsByLocation(location);
    }

    // =========================
    // FILTER RENTAL ITEMS BY PRICE RANGE
    // =========================
    @GetMapping("/price")
    public ApiResponse<List<RentalItemResponse>> filterRentalItemsByPriceRange(

            @RequestParam BigDecimal minPrice,

            @RequestParam BigDecimal maxPrice) {

        return rentalItemService.filterRentalItemsByPriceRange(
                minPrice,
                maxPrice);
    }

    // =========================
    // FILTER RENTAL ITEMS (DYNAMIC)
    // =========================
    @GetMapping("/filter")
    public ApiResponse<List<RentalItemResponse>> filterRentalItems(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) String category,

            @RequestParam(required = false) String location,

            @RequestParam(required = false) BigDecimal minPrice,

            @RequestParam(required = false) BigDecimal maxPrice,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction) {

        return rentalItemService.filterRentalItems(
                keyword,
                category,
                location,
                minPrice,
                maxPrice,
                sortBy,
                direction);
    }

    // =========================
    // GET RENTAL ITEM BY ID
    // =========================
    @GetMapping("/{id}")
    public ApiResponse<RentalItemResponse> getRentalItemById(
            @PathVariable Long id) {

        return rentalItemService.getRentalItemById(id);
    }

    // =========================
    // UPDATE RENTAL ITEM
    // =========================
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<RentalItemResponse> updateRentalItem(
            @PathVariable Long id,
            @ModelAttribute UpdateRentalItemRequest request) {

        return rentalItemService.updateRentalItem(id, request);
    }

    // =========================
    // DELETE RENTAL ITEM
    // =========================
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteRentalItem(
            @PathVariable Long id) {

        return rentalItemService.deleteRentalItem(id);
    }



    // =========================
    // GET RENTAL ITEMS (PAGINATION + SORTING + CATEGORY/LOCATION/PRICE FILTER)
    // =========================
    @GetMapping("/paginated")
    public ApiResponse<PaginationResponse<RentalItemResponse>>
    getAllRentalItemsPaginated(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy,

            @RequestParam(defaultValue = "asc") String direction,

            @RequestParam(required = false) String category,

            @RequestParam(required = false) String location,

            @RequestParam(required = false) BigDecimal minPrice,

            @RequestParam(required = false) BigDecimal maxPrice,

            @RequestParam(required = false) String keyword) {

        return rentalItemService.getAllRentalItems(
                page,
                size,
                sortBy,
                direction,
                category,
                location,
                minPrice,
                maxPrice,
                keyword);
    }



}