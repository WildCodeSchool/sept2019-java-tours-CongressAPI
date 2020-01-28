package com.congress.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SponsorNotFoundException extends ApiException {
    public SponsorNotFoundException(long id) {
        super(String.format("Sponsor with id: %d not found.", id));
    }

    public SponsorNotFoundException() {

    }
}

