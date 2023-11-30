package com.disney.qa.tests.disney.apple.ios.localization;

import java.lang.invoke.MethodHandles;
import java.util.TreeMap;

import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.openqa.selenium.ScreenOrientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.carina.proxy.browserup.ProxyPool;
import com.zebrunner.carina.utils.DateUtils;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;

@SuppressWarnings("squid:S2187")
public class DisneyPlusAppleLocalizationBaseTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected ThreadLocal<String> baseDirectory = new ThreadLocal<>();
    protected ThreadLocal<String> pathToZip = new ThreadLocal<>();
    protected String zipTestName;

    protected boolean debugMode = Boolean.parseBoolean(R.CONFIG.get("custom_string5"));

    protected void setup() {
        String locale = getLocalizationUtils().getLocale();

        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).language(getLocalizationUtils().getUserLanguage()).build();
        DisneyOffer disneyOffer = getAccountApi().lookupOfferToUse(locale, "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V2").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccount = getAccountApi().createAccount(request);

        getAccountApi().addFlex(testAccount);
        setAccount(testAccount);
        baseDirectory.set(String.format("Screenshots/%s/%s/", getLocalizationUtils().getCountryName(), getLocalizationUtils().getUserLanguage()));
        setJarvisOverrides();
    }

    protected void setPathToZip() {
        pathToZip.set(String.format("%s_%s_%s_%s.zip",
                zipTestName,
                R.CONFIG.get("locale"),
                R.CONFIG.get("language"),
                DateUtils.now()));
    }

    protected void setZipTestName(String testName) {
        zipTestName = testName;
    }

    protected void loginDismiss(DisneyAccount testAccount) {
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
        boolean unpinDictionaries = Boolean.parseBoolean(R.CONFIG.get("custom_string"));
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

        if(debugMode) {
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
        UniversalUtils.archiveAndUploadsScreenshots(baseDirectory.get(), pathToZip.get());
    }

    @AfterMethod
    public void cleanProxies() {
        ProxyPool.stopProxy();
    }
}
