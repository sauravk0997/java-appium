package com.disney.qa.api.espn;

import com.qaprosoft.carina.core.foundation.utils.R;

public enum EspnParameters {

    ESPN_API_PROD_SEARCH_SCHEDULE("espn_api_prod_search_schedule"),
    ESPN_API_PROD_ENTITLEMENT("espn_api_prod_entitlement");

    private String key;

    private EspnParameters(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return R.TESTDATA.get(this.key);
    }

    public static String getEspnHost() {
        String environment = R.CONFIG.get("env");
        String host;
        switch (environment) {
            case "PROD":
                host = ESPN_API_PROD_SEARCH_SCHEDULE.getValue();
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is an invalid environment parameter for the ESPN API tests", environment));
        }
        return host;
    }
}
