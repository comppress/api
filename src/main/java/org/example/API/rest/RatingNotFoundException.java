package org.example.API.rest;

class RatingNotFoundException extends RuntimeException {

    RatingNotFoundException(Long id) {
        super("Could not find Rating " + id);
    }
}
