package com.disney.qa.api.dgi;

import com.qaprosoft.carina.core.foundation.utils.R;

public enum DgiParameters {

    SDP_VALIDATION_SERVICE_PROD_HOST("sdp_validation_service_prod"),
    SDP_VALIDATION_SERVICE_QA_HOST("sdp_validation_service_qa"),
    MULTI_EVENT_VALIDATION_SERVICE_HOST("multi_event_validation_service");

    private String key;
    private static String runtimeEnvironment;

    DgiParameters(String key) {
        this.key = key;
    }

    private String getValue() {
        return R.TESTDATA.get(this.key);
    }

    private static void setEnv() {
        if (!R.CONFIG.get("capabilities.custom_env").isEmpty()) {
            runtimeEnvironment = R.CONFIG.get("capabilities.custom_env");
        } else {
            runtimeEnvironment = R.CONFIG.get("env");
        }
    }

    private static String getEnv() {
        if (runtimeEnvironment == null) {
            setEnv();
        }
        return runtimeEnvironment;
    }

    public static String getSdpApiHost() {
        String environment = getEnv();
        switch (runtimeEnvironment) {
            case "PROD":
            case "BETA":
                return SDP_VALIDATION_SERVICE_PROD_HOST.getValue();
            case "QA":
                return SDP_VALIDATION_SERVICE_QA_HOST.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is an invalid environment parameter for available API Hosts", environment));
        }
    }

    public static String getMultiEventApiHost() {
        String environment = getEnv();
        switch (runtimeEnvironment) {
            case "PROD":
            case "BETA":
            case "QA":
                return MULTI_EVENT_VALIDATION_SERVICE_HOST.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is an invalid environment parameter for available API Hosts", environment));
        }
    }

}
