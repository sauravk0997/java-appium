package com.disney.qa.api.atbat;

import com.qaprosoft.carina.core.foundation.utils.R;

/**
 * Created by mk on 10/16/15.
 */
public enum AtBatParameters {
    MLB_ATBAT_CONTENT_SERVER_HOST("mlb_atbat_content_server_host"),
    MLB_ATBAT_CONTENT_SERVER_HOST_PROD("mlb_atbat_content_server_host_prod"),
    MLB_CONTENT_SERVER_UPDATE_TIMEOUT("mlb_content_server_update_timeout"),
    MLB_ATBAT_NEW_CONTENT_SERVER_HOST("mlb_atbat_new_content_server_host");

    private String key;

    private AtBatParameters(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return R.TESTDATA.get(this.key);
    }


    public static String getAtBatApiHost() {
        String environment = R.CONFIG.get("env");
        String host;
        switch (environment) {
            case "QA.Debug":
                host = MLB_ATBAT_CONTENT_SERVER_HOST.getValue();
                break;
            case "Prod.AdHoc":
                host = MLB_ATBAT_CONTENT_SERVER_HOST_PROD.getValue();
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is invalid environment parameter for AtBat API tests", environment));
        }
        return host;
    }
}
