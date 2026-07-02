
package com.example.rental_platform.exception;

public class BookingNotFoundException
        extends RuntimeException {

    public BookingNotFoundException(String message) {
        super(message);
    }
}