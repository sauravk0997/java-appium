package com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config;

public enum Events {

    CONTAINER_VIEW("urn:dss:event:glimpse:impression:containerView"),
    INPUT("urn:dss:event:glimpse:engagement:input"),
    INTERACTION("urn:dss:event:glimpse:engagement:interaction"),
    PAGE_VIEW("urn:dss:event:glimpse:impression:pageView");

    private String event;

    Events(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

}
