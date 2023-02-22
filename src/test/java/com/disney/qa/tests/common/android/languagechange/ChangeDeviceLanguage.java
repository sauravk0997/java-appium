package com.disney.qa.tests.common.android.languagechange;

import com.disney.qa.tests.BaseMobileTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.android.IAndroidUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;


public class ChangeDeviceLanguage extends BaseMobileTest implements IAndroidUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String ES_LOCALE = "es";

    public static final String FR_CA_LOCALE = "fr_CA";

    public static final String FR_LOCALE = "fr";

    public static final int LANGUAGE_CHANGE_TIMEOUT = 20;


    public String origin_app;

    public String origin_locale;

    public String origin_language;

    @BeforeSuite(alwaysRun = true)
    public void setConfig() throws Throwable {
        LOGGER.info("Start ADB_Change_Language app here...");
        origin_app = R.CONFIG.get("mobile_app");

        try {
            origin_locale = R.CONFIG.get("locale");
            origin_language = R.CONFIG.get("language");
        } catch (Exception err) {
            LOGGER.error("Exception during getting config items: " + err);
        }

        LOGGER.info("Origin app is : " + origin_app);

    }

    @AfterSuite(alwaysRun = true)
    public void returnConfig() throws Throwable {
        //LOGGER.info("Origin app return back : " + origin_app);
        //R.CONFIG.put("mobile_app", origin_app);
    }

    @Test(description = "Change device language to ES")
    public void changeDeviceLanguageToES() {

        LOGGER.info("Change device language to " + ES_LOCALE);
        try {
            setDeviceLanguage(ES_LOCALE, LANGUAGE_CHANGE_TIMEOUT);
        } catch (Exception e) {
            LOGGER.error("Exception: " + e);
        }
    }

    @Test(description = "Change device language to FR_CA")
    public void changeDeviceLanguageToFrCA() {

        LOGGER.info("Change device language to " + FR_CA_LOCALE);
        try {
            setDeviceLanguage(FR_CA_LOCALE, LANGUAGE_CHANGE_TIMEOUT);
        } catch (Exception e) {
            LOGGER.error("Exception: " + e);
        }
    }

    @Test(description = "Change device language to FR")
    public void changeDeviceLanguageToFR() {

        LOGGER.info("Change device language to " + FR_LOCALE);
        try {
            setDeviceLanguage(FR_LOCALE, LANGUAGE_CHANGE_TIMEOUT);

            R.CONFIG.put("locale", FR_CA_LOCALE);
            R.CONFIG.put("language", FR_LOCALE);
        } catch (Exception e) {
            LOGGER.error("Exception: " + e);
        }
    }

    @Test(description = "Change device language to en_US")
    public void changeDeviceLanguageToEN() {
        LOGGER.info("Change device language to English");
        try {
            setDeviceLanguage("en_US", LANGUAGE_CHANGE_TIMEOUT);
        } catch (Exception e) {
            LOGGER.error("Exception: " + e);
        }
    }

    @Test(description = "Change device language to en_US")
    public void changeDeviceLanguageToDefault() {
        String setDevLang = "en_US";
        if (!origin_language.isEmpty() && !origin_language.isEmpty()) {
            setDevLang = origin_language + "_" + origin_locale;
        } else {
            origin_locale = "US";
            origin_language = "en";
        }

        LOGGER.info("Change device language to Default: " + setDevLang);
        try {
            setDeviceLanguage(setDevLang, LANGUAGE_CHANGE_TIMEOUT);
        } catch (Exception e) {
            LOGGER.error("Exception: " + e);
        }
    }
}
