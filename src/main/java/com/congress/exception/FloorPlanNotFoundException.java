package com.congress.exception;

public class FloorPlanNotFoundException extends ApiException {
    public FloorPlanNotFoundException(long id) {
        super(String.format("Floor plan with id: %d not found."));
    }

    public FloorPlanNotFoundException() {

    }
}
