package com.disney.qa.api.dgi;

public enum DgiEndpoints {

    DUST("/dust"),
    TELEMETRY("/telemetry");

    private String endpoints;

    DgiEndpoints(String endpoints) {
        this.endpoints = endpoints;
    }

    public String getEndpoint() {
        return endpoints;
    }
}
