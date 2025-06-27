package com.disney.config;

import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.utils.config.IParameter;
import com.zebrunner.carina.utils.exception.InvalidConfigurationException;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

public final class DisneyConfiguration extends Configuration {
    private static final Set<String> PARTNERS = Set.of("disney", "star");
    private static final Set<String> DEVICE_TYPES = Set.of("Phone", "Tablet", "tvOS");

    public enum Parameter implements IParameter {

        /**
         * <b>for internal usage only. Use {@link #getPartner()} instead</b>
         */
        PARTNER("partner"),

        REPORTING_TCM_XRAY_TEST_EXECUTION_KEY("reporting.tcm.xray.test-execution-key"),

        MULTIVERSE_ACCOUNTS_URL("multiverseAccountsUrl"),

        USE_MULTIVERSE("useMultiverse"),

        ENABLE_ONE_TRUST_CONFIG("enableOneTrustConfig"),

        SAGEMAKER_ENDPOINT("sagemaker_endpoint"),

        APP_VERSION("appVersion"),

        UNPIN_DICTIONARIES("unpinDictionaries"),

        OLD_APP_VERSION("oldAppVersion");

        private final String name;

        Parameter(String name) {
            this.name = name;
        }

        @Override
        public String getKey() {
            return name;
        }
    }

    public static String getPartner() {
        String partner = getRequired(Parameter.PARTNER);
        if (!StringUtils.equalsAnyIgnoreCase(partner, PARTNERS.toArray(new String[0]))) {
            throw new InvalidConfigurationException(String.format("Provided '%s' value for '%s' parameter, but it could contains only: %s",
                    partner, Parameter.PARTNER.getKey(), PARTNERS));
        }
        return partner;
    }

    public static String getDeviceType() {
        String deviceType = WebDriverConfiguration.getZebrunnerCapability("deviceType").orElseThrow(() ->
                new InvalidConfigurationException("Please add 'capabilities.deviceType' to the configuration. "
                        + "Possible values: 'Phone', 'Tablet', 'tvOS'"));
        if (!StringUtils.equalsAnyIgnoreCase(deviceType, DEVICE_TYPES.toArray(new String[0]))) {
            throw new InvalidConfigurationException(String.format("Provided '%s' value for '%s' capability, but it could contains only: %s",
                    deviceType, "deviceType", DEVICE_TYPES));
        }
        return deviceType;
    }
}
