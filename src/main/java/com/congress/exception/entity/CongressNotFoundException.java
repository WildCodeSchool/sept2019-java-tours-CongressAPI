package com.congress.exception.entity;

import com.congress.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CongressNotFoundException extends ApiException {
    public CongressNotFoundException(long id) {
        super(String.format("Congress with id: %d not found.", id));
    }

    public CongressNotFoundException() {

    }
}

