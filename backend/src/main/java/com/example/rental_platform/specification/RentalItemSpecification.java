
package com.example.rental_platform.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.example.rental_platform.model.RentalItem;

public class RentalItemSpecification {

    // =========================
    // FILTER BY CATEGORY
    // =========================
    public static Specification<RentalItem> hasCategory(String category) {

        return (root, query, criteriaBuilder) -> {

            if (category == null || category.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("category")),
                    category.toLowerCase());
        };
    }

    // =========================
    // FILTER BY LOCATION
    // =========================
    public static Specification<RentalItem> hasLocation(String location) {

        return (root, query, criteriaBuilder) -> {

            if (location == null || location.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("location")),
                    location.toLowerCase());
        };
    }

    // =========================
    // FILTER BY PRICE RANGE
    // =========================
    public static Specification<RentalItem> hasPriceBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        return (root, query, criteriaBuilder) -> {

            // No price filter
            if (minPrice == null && maxPrice == null) {
                return criteriaBuilder.conjunction();
            }

            // Only minimum price
            if (minPrice != null && maxPrice == null) {
                return criteriaBuilder.greaterThanOrEqualTo(
                        root.get("pricePerDay"),
                        minPrice);
            }

            // Only maximum price
            if (minPrice == null && maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(
                        root.get("pricePerDay"),
                        maxPrice);
            }

            // Price between min and max
            return criteriaBuilder.between(
                    root.get("pricePerDay"),
                    minPrice,
                    maxPrice);
        };
    }

    // =========================
    // FILTER BY TITLE
    // =========================
    public static Specification<RentalItem> hasTitle(String keyword) {

        return (root, query, criteriaBuilder) -> {

            if (keyword == null || keyword.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + keyword.toLowerCase() + "%");
        };
    }

}