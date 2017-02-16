package com.rajorshi.samay.model.repository;

public enum RequestStatus {

    Pending("pending"),
    Expired("expired"),
    Dispatched("dispatched"),
    DispatchFailed("dispatch_failed"),
    Retry("retry");

    private String value;

    RequestStatus(String value) {
        this.value = value;
    }

    public static RequestStatus fromString(String text) throws IllegalArgumentException {
        for (RequestStatus status : values()) {
            if (text.equalsIgnoreCase(status.value))
                return status;
        }
        throw new IllegalArgumentException(text + " is not valid request status");
    }
}
