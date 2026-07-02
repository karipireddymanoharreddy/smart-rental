
package com.example.rental_platform.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.rental_platform.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // =========================
    // USER NOT FOUND EXCEPTION
    // =========================
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(
            UserNotFoundException ex) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    // =========================
    // EMAIL ALREADY EXISTS EXCEPTION
    // =========================
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    // =========================
    // RENTAL ITEM NOT FOUND EXCEPTION
    // =========================
    @ExceptionHandler(RentalItemNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleRentalItemNotFoundException(
            RentalItemNotFoundException ex) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    // =========================
    // BOOKING NOT FOUND EXCEPTION
    // =========================
    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleBookingNotFoundException(
            BookingNotFoundException ex) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    // =========================
    // ILLEGAL ARGUMENT EXCEPTION
    // =========================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    // =========================
    // RUNTIME EXCEPTION
    // =========================
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleRuntimeException(
            RuntimeException ex) {

        return buildErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    // =========================
    // VALIDATION EXCEPTION
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildErrorResponse(
                message,
                HttpStatus.BAD_REQUEST);
    }

    // =========================
    // ACCESS DENIED EXCEPTION
    // =========================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedException(
            AccessDeniedException ex) {

        return buildErrorResponse(
                "Access denied. You do not have permission to perform this action.",
                HttpStatus.FORBIDDEN);
    }

    // =========================
    // GENERIC EXCEPTION
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(
            Exception ex) {

        // Print full stack trace in server logs
        ex.printStackTrace();

        return buildErrorResponse(
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // =========================
    // BUILD ERROR RESPONSE
    // =========================
    private ResponseEntity<ApiResponse<Object>> buildErrorResponse(
            String message,
            HttpStatus status) {

        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .message(message)
                .data(null)
                .build();

        return new ResponseEntity<>(response, status);
    }
}