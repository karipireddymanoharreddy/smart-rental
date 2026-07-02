
package com.example.rental_platform.model;

import java.time.LocalDate;

import com.example.rental_platform.enums.BookingStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // =========================
// RENTER
// =========================
@ManyToOne
@JoinColumn(name = "renter_id")
private User renter;


// =========================
// RENTAL ITEM
// =========================
@ManyToOne
@JoinColumn(name = "rental_item_id")
private RentalItem rentalItem;


// =========================
// BOOKING DATES
// =========================
private LocalDate startDate;

private LocalDate endDate;


// =========================
// BOOKING STATUS
// =========================
@Enumerated(EnumType.STRING)
private BookingStatus status;

}