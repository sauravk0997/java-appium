package com.disney.qa.api.edge;

import com.disney.qa.api.disney.DisneyParameters;
import com.qaprosoft.carina.core.foundation.utils.R;

public enum EdgeParameters {

        EDGE_PROD_HOST("edge_prod_host"),
        EDGE_QA_HOST("edge_qa_host");

        private String key;
        private static String runtimeEnvironment;

        EdgeParameters(String key) {
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

        public static String getApiHost() {
            String environment = getEnv();
            switch (DisneyParameters.getEnvironmentType(runtimeEnvironment)) {
                case "PROD":
                    return EDGE_PROD_HOST.getValue();
                case "QA":
                    return EDGE_QA_HOST.getValue();
                default:
                    throw new IllegalArgumentException(
                            String.format("'%s' is an invalid environment parameter for available API Hosts", environment));
            }
    }
}
