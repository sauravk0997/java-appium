package com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config;

/**
 * Duplicate key mapping added to configPayload prior to validation query
 */
public enum DuplicateKeys {

    CONTAINER_VIEW("\"urn:dss:event:glimpse:impression:containerView\":[\"containerViewId\"]"),
    INPUT("\"urn:dss:event:glimpse:engagement:input\":[\"inputId\"]"),
    INTERACTION("\"urn:dss:event:glimpse:engagement:interaction\":[\"interactionId\",\"containerViewId\"]"),
    PAGE_VIEW("\"urn:dss:event:glimpse:impression:pageView\":[\"pageViewId\"]");

    private String duplicateKey;

    DuplicateKeys(String duplicateKey) {
        this.duplicateKey = duplicateKey;
    }

    public String getDuplicateKey() {
        return duplicateKey;
    }

}