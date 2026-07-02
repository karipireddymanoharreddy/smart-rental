
package com.example.rental_platform.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rental_platform.dto.request.CreateBookingRequest;
import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.BookingResponse;
import com.example.rental_platform.service.BookingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // =========================
// CREATE BOOKING
// =========================
@PostMapping
public ApiResponse<BookingResponse> createBooking(
        @RequestBody CreateBookingRequest request) {

    return bookingService.createBooking(request);
}


// =========================
// APPROVE BOOKING
// =========================
@PutMapping("/{bookingId}/approve")
public ApiResponse<BookingResponse> approveBooking(
        @PathVariable Long bookingId) {

    return bookingService.approveBooking(bookingId);
}


// =========================
// REJECT BOOKING
// =========================
@PutMapping("/{bookingId}/reject")
public ApiResponse<BookingResponse> rejectBooking(
        @PathVariable Long bookingId) {

    return bookingService.rejectBooking(bookingId);
}



// =========================
// CANCEL BOOKING
// =========================
@PutMapping("/{bookingId}/cancel")
public ApiResponse<BookingResponse> cancelBooking(
        @PathVariable Long bookingId) {

    return bookingService.cancelBooking(bookingId);
}


// =========================
// GET MY BOOKINGS
// =========================
@GetMapping("/my-bookings")
public ApiResponse<List<BookingResponse>> getMyBookings() {

    return bookingService.getMyBookings();
}


// =========================
// GET OWNER BOOKINGS
// =========================
@GetMapping("/owner-bookings")
public ApiResponse<List<BookingResponse>> getOwnerBookings() {

    return bookingService.getOwnerBookings();
}


}