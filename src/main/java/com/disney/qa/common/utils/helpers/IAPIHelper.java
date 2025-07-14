package com.disney.qa.common.utils.helpers;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface IAPIHelper {
    String TEST_FAIRY_APP_VERSION = R.CONFIG.get("test_fairy_app_version");
    String IOS_PLATFORM = "ios";
    String APPLE_TV_PLATFORM = "apple-tv";
    Map<ImmutablePair<String, String>, DisneyLocalizationUtils> LOCALIZATION_UTILS = new ConcurrentHashMap<>();
    Map<ImmutablePair<String, String>, DisneyLocalizationUtils> APPLE_TV_LOCALIZATION_UTILS = new ConcurrentHashMap<>();
    Logger I_API_HELPER_LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    default String getPinnedPlatformVersion() {
        Pattern pattern = Pattern.compile("^(\\d\\.\\d+)");
        Matcher matcher = pattern.matcher(TEST_FAIRY_APP_VERSION);
        if (matcher.find()) {
            I_API_HELPER_LOGGER.info("Pinned Platform Version: {}", matcher.group(0));
            return matcher.group(0);
        } else {
            throw new RuntimeException(String.format("Couldn't extract pinned platform version from APP version %s",
                    TEST_FAIRY_APP_VERSION));
        }
    }

    /**
     * Get localization utils<br>
     * @return {@link DisneyLocalizationUtils}
     */
    default DisneyLocalizationUtils getLocalizationUtils() {
        return LOCALIZATION_UTILS.computeIfAbsent(new ImmutablePair<>(WebDriverConfiguration.getLocale()
                .getCountry(), WebDriverConfiguration.getLocale()
                .getLanguage()), pair -> {
            DisneyLocalizationUtils localizationUtils = new DisneyLocalizationUtils(pair.getLeft(), pair.getRight(),
                    IOS_PLATFORM,
                    Configuration.getRequired(Configuration.Parameter.ENV),
                    DisneyConfiguration.getPartner(),
                    getPinnedPlatformVersion(),
                    true);
            localizationUtils.setDictionaries(localizationUtils.getPinnedDictionaryVersions());
            localizationUtils.setLegalDocuments();
            return localizationUtils;
        });
    }

    /**
     * Get localization utils for specific country and language
     * @param country
     * @param language
     * @return {@link DisneyLocalizationUtils}
     */
    default DisneyLocalizationUtils getLocalizationUtils(String country, String language) {
        return LOCALIZATION_UTILS.computeIfAbsent(new ImmutablePair<>(country, language), pair -> {
            DisneyLocalizationUtils localizationUtils = new DisneyLocalizationUtils(pair.getLeft(), pair.getRight(),
                    IOS_PLATFORM,
                    Configuration.getRequired(Configuration.Parameter.ENV),
                    DisneyConfiguration.getPartner(),
                    getPinnedPlatformVersion(),
                    true);
            localizationUtils.setDictionaries(localizationUtils.getPinnedDictionaryVersions());
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
                    APPLE_TV_PLATFORM,
                    Configuration.getRequired(Configuration.Parameter.ENV),
                    DisneyConfiguration.getPartner(),
                    getPinnedPlatformVersion(),
                    true);
            localizationUtils.setDictionaries(localizationUtils.getPinnedDictionaryVersions());
            localizationUtils.setLegalDocuments();
            return localizationUtils;
        });
    }
}
