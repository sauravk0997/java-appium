package com.disney.qa.api.dgi.validationservices.multieventservice.endpointspojo.validate_with_config;

/**
 * Event sequences added to configPayload prior to validation query
 */
public enum EventSequences {

    DEFAULT("{}"),
    ONE_PAGE_ONE_CONTAINER("\"1Pageview-1ContainerView\": [[\"urn:dss:event:glimpse:impression:pageView\", 1], [\"urn:dss:event:glimpse:impression:containerView\", 1]]"),
    ONE_PAGE_X_CONTAINER("\"1Pageview-manyContainerViews\": [[\"urn:dss:event:glimpse:impression:pageView\", 1], [\"urn:dss:event:glimpse:impression:containerView\",\"x\"]]"),
    ONE_PAGE_ONE_CONTAINER_ONE_INTERACTION("\"1Pageview-1ContainerView-1Interaction\": [[\"urn:dss:event:glimpse:impression:pageView\", 1], [\"urn:dss:event:glimpse:impression:containerView\", 1], [\"urn:dss:event:glimpse:engagement:interaction\", 1]]"),
    ONE_PAGE_X_CONTAINER_ONE_INTERACTION("\"1Pageview-XContainerView-1Interaction\": [[\"urn:dss:event:glimpse:impression:pageView\", 1], [\"urn:dss:event:glimpse:impression:containerView\", \"x\"], [\"urn:dss:event:glimpse:engagement:interaction\", 1]]"),
    ONE_PAGE_X_CONTAINER_ONE_INTERACTION_ONE_INPUT("\"inputinteraction_1Input-manyContainerViews\": [[\"urn:dss:event:glimpse:impression:pageView\", 1], [\"urn:dss:event:glimpse:impression:containerView\", \"x\"], [\"urn:dss:event:glimpse:engagement:input\", 1], [\"urn:dss:event:glimpse:engagement:interaction\", 1]]");

    private String eventSequence;

    EventSequences(String eventSequence) {
        this.eventSequence = eventSequence;
    }

    public String getEventSequence() {
        return eventSequence;
    }

}


