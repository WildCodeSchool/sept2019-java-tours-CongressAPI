package com.congress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SpeakerNotFoundException extends ApiException {
    public SpeakerNotFoundException(long id) {
        super(String.format("Speaker with id: %d not found.", id));
    }

    public SpeakerNotFoundException() {

    }
}

