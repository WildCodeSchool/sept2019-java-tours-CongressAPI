package com.congress.exception;

public class MapNotFoundException extends ApiException {
    public MapNotFoundException(long id) {
        super(String.format("Map with id: %d not found.", id));
    }

    public MapNotFoundException() {

    }
}
