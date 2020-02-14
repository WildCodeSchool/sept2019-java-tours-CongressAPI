package com.congress.exception;

public class ActivityNotFoundException extends ApiException {
    public ActivityNotFoundException(long id) {
        super(String.format("Activity with id: %d not found.", id));
    }

    public ActivityNotFoundException() {

    }
}
