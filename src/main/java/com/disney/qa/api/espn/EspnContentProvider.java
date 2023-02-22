package com.disney.qa.api.espn;


import java.util.Map;

public interface EspnContentProvider {

    <T> T postRetrieveSchedule(String binaryDataToSend, Class<T> returnType, String... pathSegments);
    <T> T getRetrieveEntitlements(Map<String,String> headers, Map<String, String> parameters, Class<T> returnType);
    <T> T postCancelSubscription(Map<String,String> headers, String binaryDataToSend, Class<T> returnType, String... pathSegments);
    <T> T postAddSubscription(Map<String,String> headers, String binaryDataToSend, Class<T> returnType);

}
