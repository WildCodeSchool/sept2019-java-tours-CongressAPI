package com.congress.exception;

public class AboutNotFoundExcepetion extends ApiException {
    public AboutNotFoundExcepetion(long id) {
        super(String.format("About with id: %d not found.", id));
    }

    public AboutNotFoundExcepetion() {

    }
}
