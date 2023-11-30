package com.disney.qa.tests.disney.apple.ios.regression.basic;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.disney.util.TestGroup;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.ScreenOrientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.yaml.snakeyaml.Yaml;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.HARUtils;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.utils.resources.L10N;
import com.zebrunner.carina.webdriver.IDriverPool;

public class DisneyPlusPromotionalAssetValuesTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Yaml yaml = new Yaml();
    private final InputStream countryStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("YML_data/disney/country-promos.yaml");
    private final ArrayList<Object> countryYml = yaml.load(countryStream);

    private String newPortrait = "image_welcome_background_authenticated_unentitled_new_portrait_";
    private String returningPortrait = "image_welcome_background_authenticated_unentitled_returning_portrait_";
    private String newLandscape = "image_welcome_background_authenticated_unentitled_new_landscape_";
    private String returningLandscape = "image_welcome_background_authenticated_unentitled_returning_landscape_";
    private String ripcutHost = DisneyParameters.getRipcutHost();

    DisneyLocalizationUtils localLanguageUtils;

    @DataProvider
    private Object[] dataProvider() {
        List<String> tuid = new ArrayList<>();
        String singleRun = R.CONFIG.get("custom_string");

        if(!singleRun.equals("All")) {
            tuid.add("TUID: " + singleRun);
        } else {
            HashMap country;

            Iterator<Object> countryEntry = this.countryYml.iterator();
            while (countryEntry.hasNext()) {
                Object item = countryEntry.next();
                country = (HashMap) item;
                tuid.add("TUID: " + country.get("country").toString());
            }

            Collections.sort(tuid);
        }

        return tuid.toArray();
    }

    public void handleAlert() {
        LOGGER.info("Checking for alerts...");
        super.handleAlert();
    }

    private Object getData(String valueToSearchFor, String fieldToReturn) {
        LOGGER.info(String.format("Searching for (%s) in Field (%s) and Returning Field (%s)", valueToSearchFor, "country", fieldToReturn));
        Iterator<Object> countryAttribute = this.countryYml.iterator();

        HashMap country;
        String searchableField;
        do {
            if (!countryAttribute.hasNext()) {
                return "";
            }

            Object item = countryAttribute.next();
            country = (HashMap)item;
            searchableField = (String)country.get("country");
        } while(!searchableField.equalsIgnoreCase(valueToSearchFor));

        LOGGER.info(String.format("Returning Field Value: %s", country.get(fieldToReturn)));
        return country.get(fieldToReturn);
    }

    private void testSetup(String country) {
        localLanguageUtils = new DisneyLocalizationUtils(
                getData(country, "code").toString(),
                getData(country, "language").toString(),
                MobilePlatform.IOS,
                DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),
                DISNEY);
        String language = getData(country, "language").toString();
        initiateProxy(StringUtils.substringBefore(country, "-"));
        localLanguageUtils.setDictionaries(getConfigApi().getDictionaryVersions());
//        handleAlert();

        R.CONFIG.put("capabilities.language", language, true);
        R.CONFIG.put("capabilities.locale", getData(country, "code").toString(), true);
        L10N.load();
        restartDriver(true);
//        handleAlert();
    }

    @Test(dataProvider = "dataProvider", groups = TestGroup.PRE_CONFIGURATION)
    public void testPromotionalItems(String TUID) {
        SoftAssert softAssert = new SoftAssert();
        String country = StringUtils.substringAfter(TUID, "TUID: ");
        testSetup(country);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).isOpened();

        String portraitHash = getData(country, "portraitHashValue").toString();
        String landscapeHash = getData(country, "landscapeHashValue").toString();
        String newUserCode = getData(country, "newUserCode").toString();
        String returningUserCode = getData(country, "returningUserCode").toString();

        softAssert.assertEquals(localLanguageUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, String.format("%s%s", newPortrait, newUserCode)), portraitHash,
                "Expected - New Portrait image hash in dictionary to match regional expectation");

        softAssert.assertEquals(localLanguageUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, String.format("%s%s", newLandscape, newUserCode)), landscapeHash,
                "Expected - New Landscape image hash in dictionary to match regional expectation");

        softAssert.assertEquals(localLanguageUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, String.format("%s%s", returningPortrait, returningUserCode)), portraitHash,
                "Expected - Returning Portrait image hash in dictionary to match regional expectation");

        softAssert.assertEquals(localLanguageUtils.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, String.format("%s%s", returningLandscape, returningUserCode)), landscapeHash,
                "Expected - Returning Landscape Image hash in dictionary to match regional expectation");

//        softAssert.assertTrue(HARUtils.harContainsValue(proxy.get(), ripcutHost, HARUtils.RequestDataType.URL, portraitHash),
//                String.format("Expected - Ripcut image request for hash value '%s' to be made", portraitHash));

        if(IDriverPool.currentDevice.get().getDeviceType().equals(DeviceType.Type.IOS_TABLET)) {
            rotate(ScreenOrientation.LANDSCAPE);
            initPage(DisneyPlusWelcomeScreenIOSPageBase.class).isOpened();

//            softAssert.assertTrue(HARUtils.harContainsValue(proxy.get(), ripcutHost, HARUtils.RequestDataType.URL, landscapeHash),
//                    String.format("Expected - Ripcut image request for hash value '%s' to be made", landscapeHash));
            rotate(ScreenOrientation.LANDSCAPE);
        }

//        new HARUtils(proxy.get()).printSpecificHarDetails(Collections.singletonList(HARUtils.RequestDataType.URL), Collections.singletonList(ripcutHost));

        softAssert.assertAll();
    }
}
