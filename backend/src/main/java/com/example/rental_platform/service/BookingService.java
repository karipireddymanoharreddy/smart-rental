
package com.example.rental_platform.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.rental_platform.dto.request.CreateBookingRequest;
import com.example.rental_platform.dto.response.ApiResponse;
import com.example.rental_platform.dto.response.BookingResponse;
import com.example.rental_platform.enums.BookingStatus;
import com.example.rental_platform.enums.NotificationType;
import com.example.rental_platform.exception.RentalItemNotFoundException;
import com.example.rental_platform.exception.UserNotFoundException;
import com.example.rental_platform.model.Booking;
import com.example.rental_platform.model.RentalItem;
import com.example.rental_platform.model.User;
import com.example.rental_platform.repository.BookingRepository;
import com.example.rental_platform.repository.RentalItemRepository;
import com.example.rental_platform.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    private final RentalItemRepository rentalItemRepository;

    private final UserRepository userRepository;

    // =========================
    // NOTIFICATION SERVICE
    // =========================
    private final NotificationService notificationService;

    // =========================
    // EMAIL SERVICE
    // =========================
    private final EmailService emailService;

    // =========================
    // GET LOGGED-IN USER
    // =========================
    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));
    }

    // =========================
    // GET BOOKING BY ID
    // =========================
    private Booking getBooking(Long bookingId) {

        return bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Booking not found"));
    }

    // =========================
    // CREATE BOOKING
    // =========================
    public ApiResponse<BookingResponse> createBooking(
            CreateBookingRequest request) {

        // Get logged-in user
        User renter = getLoggedInUser();

        // Validate booking dates
        if (request.getStartDate().isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Start date cannot be in the past");
        }

        if (request.getEndDate().isBefore(request.getStartDate())) {

            throw new IllegalArgumentException(
                    "End date cannot be before start date");
        }

        // Booking must be at least one day
        if (request.getStartDate().isEqual(request.getEndDate())) {

            throw new IllegalArgumentException(
                    "Booking must be for at least one day");
        }

        // Find rental item
        RentalItem rentalItem = rentalItemRepository.findById(
                request.getRentalItemId())
                .orElseThrow(() ->
                        new RentalItemNotFoundException(
                                "Rental item not found"));

        // Owner cannot book their own rental item
        if (rentalItem.getOwner().getId().equals(renter.getId())) {

            throw new IllegalArgumentException(
                    "You cannot book your own rental item");
        }

        // Check for overlapping approved bookings
        boolean isBooked = bookingRepository.existsOverlappingBooking(
                rentalItem.getId(),
                request.getStartDate(),
                request.getEndDate(),
                BookingStatus.APPROVED);

        if (isBooked) {

            throw new IllegalArgumentException(
                    "Rental item is already booked for the selected dates");
        }

        // Create booking
        Booking booking = Booking.builder()
                .renter(renter)
                .rentalItem(rentalItem)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(BookingStatus.PENDING)
                .build();

        // Save booking
        booking = bookingRepository.save(booking);

        // =========================
        // CREATE NOTIFICATION FOR OWNER
        // =========================
        notificationService.createNotification(
                rentalItem.getOwner(),
                NotificationType.BOOKING_CREATED,
                "New Booking Request",
                renter.getName() + " requested to rent your " + rentalItem.getTitle());

        // =========================
        // SEND BOOKING REQUEST EMAIL
        // =========================
        emailService.sendSimpleEmail(
                booking.getRentalItem()
                        .getOwner()
                        .getEmail(),

                "New Booking Request",

                """
                Hello,

                You have received a new booking request.

                Rental Item: %s

                Requested By: %s

                Please log in to review the request.

                Regards,
                Rental Platform Team
                """.formatted(
                        booking.getRentalItem().getTitle(),
                        booking.getRenter().getName()));

        // Convert Entity -> DTO
        BookingResponse response = BookingResponse.builder()
                .id(booking.getId())
                .renterName(booking.getRenter().getName())
                .rentalItemTitle(booking.getRentalItem().getTitle())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();

        return ApiResponse.<BookingResponse>builder()
                .success(true)
                .message("Booking created successfully")
                .data(response)
                .build();
    }

    // =========================
    // APPROVE BOOKING
    // =========================
    public ApiResponse<BookingResponse> approveBooking(Long bookingId) {

        // Get logged-in user
        User owner = getLoggedInUser();

        // Find booking
        Booking booking = getBooking(bookingId);

        // Only owner can approve
        if (!booking.getRentalItem()
                .getOwner()
                .getId()
                .equals(owner.getId())) {

            throw new IllegalArgumentException(
                    "Only the owner can approve this booking");
        }

        // Booking must be in PENDING state
        if (booking.getStatus() != BookingStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Only pending bookings can be approved");
        }

        // Booking end date must not be in the past
        if (booking.getEndDate().isBefore(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "Cannot approve an expired booking");
        }

        // Check if another approved booking already exists
        boolean isBooked = bookingRepository.existsOverlappingBooking(
                booking.getRentalItem().getId(),
                booking.getStartDate(),
                booking.getEndDate(),
                BookingStatus.APPROVED);

        if (isBooked) {

            throw new IllegalArgumentException(
                    "Another booking has already been approved for these dates");
        }

        // Approve booking
        booking.setStatus(BookingStatus.APPROVED);

        // Save booking
        booking = bookingRepository.save(booking);

        // =========================
        // SEND BOOKING APPROVED EMAIL
        // =========================
        emailService.sendSimpleEmail(
                booking.getRenter().getEmail(),

                "Booking Approved",

                """
                Congratulations!

                Your booking has been approved.

                Rental Item: %s

                Owner: %s

                You can now contact the owner through the Rental Platform.

                Regards,
                Rental Platform Team
                """.formatted(
                        booking.getRentalItem().getTitle(),
                        booking.getRentalItem()
                                .getOwner()
                                .getName()));

        // Convert Entity -> DTO
        BookingResponse response = BookingResponse.builder()
                .id(booking.getId())
                .renterName(booking.getRenter().getName())
                .rentalItemTitle(booking.getRentalItem().getTitle())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();

        return ApiResponse.<BookingResponse>builder()
                .success(true)
                .message("Booking approved successfully")
                .data(response)
                .build();
    }

    // =========================
    // REJECT BOOKING
    // =========================
    public ApiResponse<BookingResponse> rejectBooking(Long bookingId) {

        // Get logged-in user
        User owner = getLoggedInUser();

        // Find booking
        Booking booking = getBooking(bookingId);

        // Only owner can reject
        if (!booking.getRentalItem()
                .getOwner()
                .getId()
                .equals(owner.getId())) {

            throw new IllegalArgumentException(
                    "Only the owner can reject this booking");
        }

        // Booking must be in PENDING state
        if (booking.getStatus() != BookingStatus.PENDING) {

            throw new IllegalArgumentException(
                    "Only pending bookings can be rejected");
        }

        // Reject booking
        booking.setStatus(BookingStatus.REJECTED);

        // Save booking
        booking = bookingRepository.save(booking);

        // =========================
        // SEND BOOKING REJECTED EMAIL
        // =========================
        emailService.sendSimpleEmail(
                booking.getRenter().getEmail(),

                "Booking Rejected",

                """
                Hello,

                Unfortunately, your booking request has been rejected.

                Rental Item: %s

                Owner: %s

                You may browse other rental items or contact the owner for more information.

                Regards,
                Rental Platform Team
                """.formatted(
                        booking.getRentalItem().getTitle(),
                        booking.getRentalItem()
                                .getOwner()
                                .getName()));

        // Convert Entity -> DTO
        BookingResponse response = BookingResponse.builder()
                .id(booking.getId())
                .renterName(booking.getRenter().getName())
                .rentalItemTitle(booking.getRentalItem().getTitle())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();

        return ApiResponse.<BookingResponse>builder()
                .success(true)
                .message("Booking rejected successfully")
                .data(response)
                .build();
    }

    // =========================
    // CANCEL BOOKING
    // =========================
    public ApiResponse<BookingResponse> cancelBooking(Long bookingId) {

        // Get logged-in user
        User renter = getLoggedInUser();

        // Find booking
        Booking booking = getBooking(bookingId);

        // Only renter can cancel
        if (!booking.getRenter()
                .getId()
                .equals(renter.getId())) {

            throw new IllegalArgumentException(
                    "Only the renter can cancel this booking");
        }

        // Booking can only be cancelled if it is PENDING or APPROVED
        if (booking.getStatus() != BookingStatus.PENDING
                && booking.getStatus() != BookingStatus.APPROVED) {

            throw new IllegalArgumentException(
                    "Only pending or approved bookings can be cancelled");
        }

        // Cancel booking
        booking.setStatus(BookingStatus.CANCELLED);

        // Save booking
        booking = bookingRepository.save(booking);

        // Convert Entity -> DTO
        BookingResponse response = BookingResponse.builder()
                .id(booking.getId())
                .renterName(booking.getRenter().getName())
                .rentalItemTitle(booking.getRentalItem().getTitle())
                .startDate(booking.getStartDate())
                .endDate(booking.getEndDate())
                .status(booking.getStatus())
                .build();

        return ApiResponse.<BookingResponse>builder()
                .success(true)
                .message("Booking cancelled successfully")
                .data(response)
                .build();
    }

    // =========================
    // GET MY BOOKINGS
    // =========================
    public ApiResponse<List<BookingResponse>> getMyBookings() {

        // Get logged-in user
        User renter = getLoggedInUser();

        // Fetch bookings
        List<Booking> bookings =
                bookingRepository.findByRenterId(renter.getId());

        // Convert Entity -> DTO
        List<BookingResponse> responses = bookings.stream()
                .map(booking -> BookingResponse.builder()
                        .id(booking.getId())
                        .renterName(booking.getRenter().getName())
                        .rentalItemTitle(booking.getRentalItem().getTitle())
                        .startDate(booking.getStartDate())
                        .endDate(booking.getEndDate())
                        .status(booking.getStatus())
                        .build())
                .collect(Collectors.toList());

        return ApiResponse.<List<BookingResponse>>builder()
                .success(true)
                .message("Bookings fetched successfully")
                .data(responses)
                .build();
    }

    // =========================
    // GET OWNER BOOKINGS
    // =========================
    public ApiResponse<List<BookingResponse>> getOwnerBookings() {

        // Get logged-in user
        User owner = getLoggedInUser();

        // Fetch bookings
        List<Booking> bookings =
                bookingRepository.findByRentalItemOwnerId(owner.getId());

        // Convert Entity -> DTO
        List<BookingResponse> responses = bookings.stream()
                .map(booking -> BookingResponse.builder()
                        .id(booking.getId())
                        .renterName(booking.getRenter().getName())
                        .rentalItemTitle(booking.getRentalItem().getTitle())
                        .startDate(booking.getStartDate())
                        .endDate(booking.getEndDate())
                        .status(booking.getStatus())
                        .build())
                .collect(Collectors.toList());

        return ApiResponse.<List<BookingResponse>>builder()
                .success(true)
                .message("Owner bookings fetched successfully")
                .data(responses)
                .build();
    }
}