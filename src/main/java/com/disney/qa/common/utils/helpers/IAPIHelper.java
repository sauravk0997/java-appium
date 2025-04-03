package com.disney.qa.common.utils.helpers;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.disney.qa.common.constant.IConstantHelper.DEVICE_TYPE_TVOS;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.DEVICE_TYPE;

public interface IAPIHelper {
    String APP_VERSION = "4.3.0";
    Map<ImmutablePair<String, String>, DisneyLocalizationUtils> LOCALIZATION_UTILS = new ConcurrentHashMap<>();
    Map<ImmutablePair<String, String>, DisneyLocalizationUtils> APPLE_TV_LOCALIZATION_UTILS = new ConcurrentHashMap<>();
    Logger I_API_HELPER_LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    LazyInitializer<DisneyMobileConfigApi> MOBILE_CONFIG_API = new LazyInitializer<>() {
        @Override
        protected DisneyMobileConfigApi initialize() {
            String platform = (R.CONFIG.get(DEVICE_TYPE).equals(DEVICE_TYPE_TVOS)) ? "tvos" : "ios";
            I_API_HELPER_LOGGER.info("App version: {}", APP_VERSION);
            return new DisneyMobileConfigApi(platform, Configuration.getRequired(Configuration.Parameter.ENV), DisneyConfiguration.getPartner(),
                    APP_VERSION);
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

    /**
     * Get Apple TV specific localization utils
     * @return {@link DisneyLocalizationUtils}
     */
    default DisneyLocalizationUtils getAppleTVLocalizationUtils() {
        return APPLE_TV_LOCALIZATION_UTILS.computeIfAbsent(new ImmutablePair<>(WebDriverConfiguration.getLocale()
                .getCountry(), WebDriverConfiguration.getLocale()
                .getLanguage()), pair -> {
            DisneyLocalizationUtils localizationUtils = new DisneyLocalizationUtils(pair.getLeft(), pair.getRight(),
                    "apple-tv",
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
