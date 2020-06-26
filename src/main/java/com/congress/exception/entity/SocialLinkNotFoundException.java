package com.congress.exception.entity;

import com.congress.exception.ApiException;

public class SocialLinkNotFoundException extends ApiException {
    public SocialLinkNotFoundException(long id) {
        super(String.format("Social link with id: %d not found.", id));
    }

    public SocialLinkNotFoundException() {

    }
}
