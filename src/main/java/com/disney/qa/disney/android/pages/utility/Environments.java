package com.disney.qa.disney.android.pages.utility;

public enum Environments {

    PRODUCTION("PROD"),
    QA("QA"),
    EDITORIAL("EDITORIAL"),
    STAGING("STAGING");

    private String environment;

    Environments(String environment) {
        this.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }
}
