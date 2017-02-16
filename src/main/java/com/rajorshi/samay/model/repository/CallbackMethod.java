package com.rajorshi.samay.model.repository;

public enum CallbackMethod {

    Http("http"),
    Varadhi("varadhi"),
    Kafka("kafka");

    private String value;

    CallbackMethod(String value) {
        this.value = value;
    }

    public static CallbackMethod fromString(String text) {
        for (CallbackMethod b : CallbackMethod.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("No callback method with protocol \'" + text + "\' found");
    }

}
