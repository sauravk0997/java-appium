package com.disney.qa.api.disney;

public enum DisneyOrderParameters {
    TOKEN_ID_PROD("fecd86a4-5066-4304-838e-97d470645d26"),
    TOKEN_ID_QA("01a4f196-08c8-45f5-859e-959788967391");

    private String key;

    DisneyOrderParameters(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.key;
    }

    public static String getTokenIdForEnvironment() {
        String environment = DisneyParameters.getEnv();

        switch (DisneyParameters.getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return TOKEN_ID_PROD.getValue();
            case "QA":
                return TOKEN_ID_QA.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s order submission hosts", environment, DisneyParameters.INVALID_ENVIRONMENT));
        }
    }

    public static String getProperCreditString() {
        String environment = DisneyParameters.getEnv();

        switch (DisneyParameters.getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return "prod-cc";
            case "QA":
                return "cc";
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s order submission hosts", environment, DisneyParameters.INVALID_ENVIRONMENT));
        }
    }
}
