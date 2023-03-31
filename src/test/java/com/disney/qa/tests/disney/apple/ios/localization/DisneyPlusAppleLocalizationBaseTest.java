package com.disney.qa.tests.disney.apple.ios.localization;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.utils.DateUtils;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.ScreenOrientation;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;

import java.util.TreeMap;

@SuppressWarnings("squid:S2187")
public class DisneyPlusAppleLocalizationBaseTest extends DisneyBaseTest {

    protected ThreadLocal<String> baseDirectory = new ThreadLocal<>();
    protected ThreadLocal<String> pathToZip = new ThreadLocal<>();
    protected ThreadLocal<Integer> count = new ThreadLocal<>();

    protected boolean debugMode = Boolean.parseBoolean(R.CONFIG.get("custom_string5"));

    protected void setup() {
        setGlobalVariables();
        String locale = languageUtils.get().getLocale();

        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).language(languageUtils.get().getUserLanguage()).build();
        DisneyOffer disneyOffer = disneyAccountApi.get().lookupOfferToUse(locale, "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V2").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccount = disneyAccountApi.get().createAccount(request);

        disneyAccountApi.get().addFlex(testAccount);
        disneyAccount.set(testAccount);
        baseDirectory.set(String.format("Screenshots/%s/%s/", languageUtils.get().getCountryName(), languageUtils.get().getUserLanguage()));
        setJarvisOverrides();
        startProxyAndRestart(languageUtils.get().getCountryName());

        count.set(0);
    }

    protected void setPathToZip(String testName) {
        pathToZip.set(String.format("%s_%s_%s_%s.zip",
                testName,
                languageUtils.get().getLocale(),
                languageUtils.get().getUserLanguage(),
                DateUtils.now()));
    }

    protected void loginDismiss(DisneyAccount testAccount) {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        IOSUtils utils = iosUtils.get();
        welcomePage.clickLogInButton();
        loginPage.fillOutEmailField(testAccount.getEmail());
        loginPage.clickPrimaryButton();
        passwordPage.typePassword(testAccount.getUserPass());
        utils.dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();
    }

    protected void getScreenshots(String fileName) {
        rotateScreen(ScreenOrientation.PORTRAIT);
        if (getDevice().getDeviceType() == DeviceType.Type.IOS_TABLET) {
            UniversalUtils.storeAndUploadSS(fileName + "Portrait", count.get(), baseDirectory.get(), getCastedDriver());
            rotateScreen(ScreenOrientation.LANDSCAPE);
            new IOSUtils().dismissKeyboardByClicking(5, 3);
            pause(2);
            UniversalUtils.storeAndUploadSS(fileName + "Landscape", count.get(), baseDirectory.get(), getCastedDriver());
            rotateScreen(ScreenOrientation.PORTRAIT);
            pause(2);
        } else {
            UniversalUtils.storeAndUploadSS(fileName, count.get(), baseDirectory.get(), getCastedDriver());
        }
        count.set(count.get() + 1);
    }

    protected void getScreenshots(String fileName, boolean dismissKeyboard) {
        rotateScreen(ScreenOrientation.PORTRAIT);
        if (getDevice().getDeviceType() == DeviceType.Type.IOS_TABLET) {
            UniversalUtils.storeAndUploadSS(fileName + "Portrait", count.get(), baseDirectory.get(), getCastedDriver());
            rotateScreen(ScreenOrientation.LANDSCAPE);
            if (dismissKeyboard) {
                new IOSUtils().dismissKeyboardByClicking(5, 3);
            }
            pause(2);
            UniversalUtils.storeAndUploadSS(fileName + "Landscape", count.get(), baseDirectory.get(), getCastedDriver());
            rotateScreen(ScreenOrientation.PORTRAIT);
            pause(2);
        } else {
            UniversalUtils.storeAndUploadSS(fileName, count.get(), baseDirectory.get(), getCastedDriver());
        }
        count.set(count.get() + 1);
    }

    protected void getScreenshotsNoCountUpdate(String fileName) {
        rotateScreen(ScreenOrientation.PORTRAIT);
        if (getDevice().getDeviceType() == DeviceType.Type.IOS_TABLET) {
            UniversalUtils.storeAndUploadSS(fileName + "Portrait", count.get(), baseDirectory.get(), getCastedDriver());
            rotateScreen(ScreenOrientation.LANDSCAPE);
            new IOSUtils().dismissKeyboardByClicking(5, 3);
            pause(2);
            UniversalUtils.storeAndUploadSS(fileName + "Landscape", count.get(), baseDirectory.get(), getCastedDriver());
            rotateScreen(ScreenOrientation.PORTRAIT);
            pause(2);
        } else {
            UniversalUtils.storeAndUploadSS(fileName, count.get(), baseDirectory.get(), getCastedDriver());
        }
    }

    @DataProvider
    protected Object[] tuidGenerator() {
        languageUtils.set(new DisneyLocalizationUtils(R.CONFIG.get("locale"), R.CONFIG.get("language"), "ios", DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DISNEY));
        return new String[]{String.format("TUID: %s | %s", languageUtils.get().getCountryName(), languageUtils.get().getUserLanguage())};
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

    @AfterMethod
    public void cleanProxies() {
        ProxyPool.stopProxy();
        proxy.remove();
    }
}
