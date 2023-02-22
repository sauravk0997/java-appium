package com.disney.qa.api.disney;

public enum DisneyTokenParameters {
    ACCOUNT_TOKEN_TYPE("urn:ietf:params:oauth:token-type:jwt"),
    DEVICE_TOKEN_TYPE("urn:bamtech:params:oauth:token-type:device");

    private String token;

    DisneyTokenParameters(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return this.token;
    }
}
