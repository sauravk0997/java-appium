package com.disney.qa.common.utils.helpers;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.zebrunner.carina.appcenter.AppCenterManager;
import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.utils.exception.InvalidConfigurationException;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import io.appium.java_client.remote.options.SupportsAppOption;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface IAPIHelper {
    Map<ImmutablePair<String, String>, DisneyLocalizationUtils> LOCALIZATION_UTILS = new ConcurrentHashMap<>();
    Logger I_API_HELPER_LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    LazyInitializer<DisneyMobileConfigApi> MOBILE_CONFIG_API = new LazyInitializer<>() {
        @Override
        protected DisneyMobileConfigApi initialize() {
            String version = "4.2.0";
//            String version = AppCenterManager.getInstance()
//                    .getAppInfo(WebDriverConfiguration.getAppiumCapability(SupportsAppOption.APP_OPTION)
//                            .orElseThrow(
//                                    () -> new InvalidConfigurationException("The configuration must contains the 'capabilities.app' parameter.")))
//                    .getVersion();
//            I_API_HELPER_LOGGER.info("App version: {}", version);
            return new DisneyMobileConfigApi("iOS", Configuration.getRequired(Configuration.Parameter.ENV), DisneyConfiguration.getPartner(),
                    version);
        }
    };

    /**
     * Get localization utils<br>
     * @return {@link DisneyLocalizationUtils}
     */
    default DisneyLocalizationUtils getLocalizationUtils() {
        return LOCALIZATION_UTILS.computeIfAbsent(new ImmutablePair<>(WebDriverConfiguration.getLocale()
                .getCountry(), WebDriverConfiguration.getLocale()
                .getLanguage()), pair -> {
            DisneyLocalizationUtils localizationUtils = new DisneyLocalizationUtils(pair.getLeft(), pair.getRight(), "iOS",
                    Configuration.getRequired(Configuration.Parameter.ENV),
                    DisneyConfiguration.getPartner());
            localizationUtils.setDictionaries(getMobileConfigApi().getDictionaryVersions());
            localizationUtils.setLegalDocuments();
            return localizationUtils;
        });
    }

    default DisneyMobileConfigApi getMobileConfigApi() {
        try {
            return MOBILE_CONFIG_API.get();
        } catch (ConcurrentException e) {
            return ExceptionUtils.rethrow(e);
        }
    }
}
