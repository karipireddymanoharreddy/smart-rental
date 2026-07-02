
package com.example.rental_platform.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.rental_platform.model.RentalItem;

@Repository
public interface RentalItemRepository
        extends JpaRepository<RentalItem, Long>,
                JpaSpecificationExecutor<RentalItem> {

    // =========================
    // SEARCH RENTAL ITEMS BY TITLE
    // =========================
    List<RentalItem> findByTitleContainingIgnoreCase(String keyword);

    // =========================
    // SEARCH RENTAL ITEMS BY CATEGORY
    // =========================
    List<RentalItem> findByCategoryIgnoreCase(String category);

    // =========================
    // GET RENTAL ITEMS BY CATEGORY (PAGINATION)
    // =========================
    Page<RentalItem> findByCategoryIgnoreCase(
            String category,
            Pageable pageable);

    // =========================
    // FILTER RENTAL ITEMS BY LOCATION
    // =========================
    List<RentalItem> findByLocationIgnoreCase(String location);

    // =========================
    // GET RENTAL ITEMS BY LOCATION (PAGINATION)
    // =========================
    Page<RentalItem> findByLocationIgnoreCase(
            String location,
            Pageable pageable);

    // =========================
    // FILTER RENTAL ITEMS BY PRICE RANGE
    // =========================
    List<RentalItem> findByPricePerDayBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice);

    // =========================
    // GET ALL RENTAL ITEMS (PAGINATION)
    // =========================
    Page<RentalItem> findAll(Pageable pageable);

}