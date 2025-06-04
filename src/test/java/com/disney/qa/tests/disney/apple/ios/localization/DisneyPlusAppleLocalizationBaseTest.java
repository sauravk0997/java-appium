package com.disney.qa.tests.disney.apple.ios.localization;

import java.lang.invoke.MethodHandles;
import java.util.TreeMap;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.requests.CreateUnifiedAccountRequest;
import com.disney.qa.api.client.requests.offer.UnifiedOfferRequest;
import com.disney.qa.api.pojos.UnifiedAccount;
import com.disney.qa.api.pojos.UnifiedEntitlement;
import com.disney.qa.api.pojos.UnifiedOffer;
import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.openqa.selenium.ScreenOrientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.carina.utils.DateUtils;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;

@SuppressWarnings("squid:S2187")
public class DisneyPlusAppleLocalizationBaseTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static final ThreadLocal<String> BASE_DIRECTORY = new ThreadLocal<>();
    protected static final ThreadLocal<String> PATH_TO_ZIP = new ThreadLocal<>();
    private static final ThreadLocal<String> ZIP_TEST_NAME = new ThreadLocal<>();
    protected static final boolean DEBUG_MODE = Boolean.parseBoolean(R.CONFIG.get("custom_string5"));

    protected void setup() {
        String locale = getLocalizationUtils().getLocale();

        CreateUnifiedAccountRequest request = CreateUnifiedAccountRequest.builder()
                .country(locale)
                .language(getLocalizationUtils().getUserLanguage())
                .build();

        UnifiedOffer offer = getUnifiedSubscriptionApi()
                .lookupUnifiedOffer(
                        UnifiedOfferRequest.builder()
                                .searchText("Yearly")
                                .build()
                );
        UnifiedEntitlement entitlement = new UnifiedEntitlement(offer, "V2");
        request.addEntitlement(entitlement);
        UnifiedAccount testAccount = getUnifiedAccountApi().createAccount(request);
        getUnifiedSubscriptionApi().addFlex(testAccount);
        setAccount(testAccount);
        BASE_DIRECTORY.set(String.format("Screenshots/%s/%s/", getLocalizationUtils().getCountryName(), getLocalizationUtils().getUserLanguage()));
        setJarvisOverrides();
    }

    protected void setPathToZip() {
        PATH_TO_ZIP.set(String.format("%s_%s_%s_%s.zip",
                ZIP_TEST_NAME.get(),
                getCountry(),
                getLanguage(),
                DateUtils.now()));
    }

    protected void setZipTestName(String testName) {
        ZIP_TEST_NAME.set(testName);
    }

    protected void loginDismiss(UnifiedAccount testAccount) {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        welcomePage.clickLogInButton();
        loginPage.fillOutEmailField(testAccount.getEmail());
        loginPage.clickPrimaryButton();
        passwordPage.typePassword(testAccount.getUserPass());
        dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();
    }

    protected void getScreenshots(String fileName) {
        rotateScreen(ScreenOrientation.PORTRAIT);
        if (getDevice().getDeviceType() == DeviceType.Type.IOS_TABLET) {
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE, fileName + "Portrait");
            rotateScreen(ScreenOrientation.LANDSCAPE);
            dismissKeyboardByClicking(5, 3);
            pause(2);
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE, fileName + "Landscape");
            rotateScreen(ScreenOrientation.PORTRAIT);
            pause(2);
        } else {
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE, fileName);
        }
    }

    protected void getScreenshots(String fileName, boolean dismissKeyboard) {
        rotateScreen(ScreenOrientation.PORTRAIT);
        if (getDevice().getDeviceType() == DeviceType.Type.IOS_TABLET) {
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE, fileName + "Portrait");
            rotateScreen(ScreenOrientation.LANDSCAPE);
            if (dismissKeyboard) {
                dismissKeyboardByClicking(5, 3);
            }
            pause(2);
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE, fileName + "Landscape");
            rotateScreen(ScreenOrientation.PORTRAIT);
            pause(2);
        } else {
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE, fileName);
        }
    }

    protected void getScreenshotsNoCountUpdate(String fileName) {
        rotateScreen(ScreenOrientation.PORTRAIT);
        if (getDevice().getDeviceType() == DeviceType.Type.IOS_TABLET) {
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE, fileName + "Portrait");
            rotateScreen(ScreenOrientation.LANDSCAPE);
            dismissKeyboardByClicking(5, 3);
            pause(2);
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE, fileName + "Landscape");
            rotateScreen(ScreenOrientation.PORTRAIT);
            pause(2);
        } else {
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE, fileName);
        }
    }

    @DataProvider
    protected Object[] tuidGenerator() {
        return new String[]{String.format("TUID: %s | %s", getLocalizationUtils().getCountryName(), getLocalizationUtils().getUserLanguage())};
    }

    public void setJarvisOverrides() {
        TreeMap<String, Object> overrides = new TreeMap<>();
        boolean unpinDictionaries = Configuration.get(DisneyConfiguration.Parameter.UNPIN_DICTIONARIES, Boolean.class)
                .orElse(false);
        String globalizationVersion = R.CONFIG.get("custom_string2");

        if (unpinDictionaries) {
            overrides.put("localization", "0.0");
        }

        if (!globalizationVersion.equals("NULL")) {
            overrides.put("globalizationVersion", globalizationVersion);
        }

        LOGGER.info("Attempting to launch Jarvis app...");
        launchJarvis(true);
        JarvisAppleBase jarvis = getJarvisPageFactory();

        if (!jarvis.isAppPresent(JarvisAppleBase.AppName.IOS_DISNEY)) {
            jarvis.clickPlaceholderJarvisApp();
            jarvis.selectApp(JarvisAppleBase.AppName.IOS_DISNEY);
        }

        jarvis.openAppConfigOverrides();
        overrides.keySet().forEach(override -> {
            String overrideValue = overrides.get(override).toString();
            switch (override) {
                case "localization":
                    jarvis.openOverrideSection(override);
                    for (JarvisAppleBase.Config_Localization dictionary : JarvisAppleBase.Config_Localization.values()) {
                        if (!dictionary.equals(JarvisAppleBase.Config_Localization.PLATFORM)) {
                            dictionary.setOverrideValue(overrideValue);
                        }
                    }
                    jarvis.activateOverrides();
                    jarvis.clickBackButton();
                    break;
                case "globalizationVersion":
                    JarvisAppleBase.Config_GlobalizationVersion.GLOBALIZATION_VERSION.setOverrideValue(overrideValue);
                    jarvis.activateOverrides();
                    break;
                default:
                    LOGGER.info("Invalid override set. Check parameters. Proceeding with test...");
            }
        });

        if(DEBUG_MODE) {
            while (!jarvis.getDebugDisplayOverrideValue().equals("key")) {
                jarvis.clickDebugDisplayOverride();
            }
        } else {
            while (!jarvis.getDebugDisplayOverrideValue().equals("off")) {
                jarvis.clickDebugDisplayOverride();
            }
        }
    }

    @AfterMethod(alwaysRun = true)
    public void uploadScreenshots(){
        LOGGER.info("Running after method upload screenshots");
        setPathToZip();
        UniversalUtils.archiveAndUploadsScreenshots(BASE_DIRECTORY.get(), PATH_TO_ZIP.get());
        BASE_DIRECTORY.remove();
        PATH_TO_ZIP.remove();
        ZIP_TEST_NAME.remove();
    }
}
