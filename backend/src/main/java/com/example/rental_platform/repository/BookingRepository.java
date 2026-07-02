
package com.example.rental_platform.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.rental_platform.enums.BookingStatus;
import com.example.rental_platform.model.Booking;

@Repository
public interface BookingRepository
        extends JpaRepository<Booking, Long> {

    // =========================
    // CHECK OVERLAPPING BOOKINGS
    // =========================
    @Query("""
           SELECT COUNT(b) > 0
           FROM Booking b
           WHERE b.rentalItem.id = :rentalItemId
           AND b.status = :status
           AND b.startDate <= :endDate
           AND b.endDate >= :startDate
           """)
    boolean existsOverlappingBooking(

            @Param("rentalItemId") Long rentalItemId,

            @Param("startDate") LocalDate startDate,

            @Param("endDate") LocalDate endDate,

            @Param("status") BookingStatus status);

    // =========================
    // GET BOOKINGS OF A RENTER
    // =========================
    List<Booking> findByRenterId(Long renterId);

    // =========================
    // GET BOOKINGS OF OWNER'S RENTAL ITEMS
    // =========================
    List<Booking> findByRentalItemOwnerId(Long ownerId);

}