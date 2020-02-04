package com.congress.exception.entity;

import com.congress.exception.ApiException;

public class AboutNotFoundExcepetion extends ApiException {
    public AboutNotFoundExcepetion(long id) {
        super(String.format("About with id: %d not found.", id));
    }

    public AboutNotFoundExcepetion() {

    }
}
