package com.congress.exception.entity;

import com.congress.exception.ApiException;

public class InteractiveFloorPlanNotFoundException extends ApiException {
    public InteractiveFloorPlanNotFoundException(long id) {
        super(String.format("Interactive floor plan with id: %d not found.", id));
    }

    public InteractiveFloorPlanNotFoundException() {

    }
}
