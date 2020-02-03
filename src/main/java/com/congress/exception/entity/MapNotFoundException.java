package com.congress.exception.entity;

import com.congress.exception.ApiException;

public class MapNotFoundException extends ApiException {
    public MapNotFoundException(long id) {
        super(String.format("Map with id: %d not found.", id));
    }

    public MapNotFoundException() {

    }
}
