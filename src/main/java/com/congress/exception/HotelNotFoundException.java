package com.congress.exception;

public class HotelNotFoundException extends ApiException {
    public HotelNotFoundException(long id) {
        super(String.format("Hotel with id: %d not found.", id));
    }

    public HotelNotFoundException() {

    }
}
