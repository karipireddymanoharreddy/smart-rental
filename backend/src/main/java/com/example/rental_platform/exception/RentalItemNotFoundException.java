
package com.example.rental_platform.exception;

public class RentalItemNotFoundException extends RuntimeException {

    public RentalItemNotFoundException(String message) {
        super(message);
    }
}