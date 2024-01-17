package com.disney.qa.tests.disney.apple.ios;

import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Date;

import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.config.DisneyConfiguration;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.TestGroup;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.common.utils.ios_settings.IOSSettingsMenuBase;
import com.disney.qa.hora.validationservices.HoraValidator;
import com.disney.qa.tests.disney.apple.DisneyAppleBaseTest;
import com.zebrunner.carina.appcenter.AppCenterManager;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;

/**
 * Base class for ios
 */
@SuppressWarnings("squid:S2187")
public class DisneyBaseTest extends DisneyAppleBaseTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String DEFAULT_PROFILE = "Test";
    public static final String KIDS_PROFILE = "KIDS";
    public static final String JUNIOR_PROFILE = "JUNIOR";
    public static final String SECONDARY_PROFILE = "Secondary";
    public static final String KIDS_DOB = "2018-01-01";
    public static final String PHONE = "Phone";
    public static final String TABLET = "Tablet";
    public static final String JUNIOR_MODE_HELP_CENTER = "Junior Mode on Disney+";
    public static final String DISNEY_PLUS_HELP_CENTER = "Disney+ Help Center";
    public static final String RESTRICTED = "Restricted";
    public static final String SANDBOX_ACCOUNT_PREFIX = "dsqaaiap";
    public static final String RATING_MATURE = "TV-MA";
    public static final String RATING_TV14 = "TV-14";

    public static final String MULTIVERSE_STAGING_ENDPOINT = "https://multiverse-alice-client-staging.qateam.bamgrid.com";

//    @BeforeMethod(alwaysRun = true, onlyForGroups = TestGroup.PROXY)
//    public void initProxy() {
//        LOGGER.warn("Proxy logic disabled.");
        /*
        new GeoedgeProxyServer().setProxyHostForSelenoid();
        String country = getLocalizationUtils().getCountryName();
        GeoedgeProxyServer geoedgeProxyFreshInstance = new GeoedgeProxyServer();
        geoedgeProxyFreshInstance.setProxyHostForSelenoid();
        Map<String, String> headers = new HashMap<>();

        String countryCode = new DisneyCountryData()
                .searchAndReturnCountryData(country, "country", "code");
        R.CONFIG.put("browserup_proxy", "true");
        getDriver();
        DisneyGlobalUtils disneyGlobalUtils = new DisneyGlobalUtils();
        DisneyProductData productData = new DisneyProductData();
        boolean productHasLaunched = productData.searchAndReturnProductData("hasLaunched").equalsIgnoreCase("true");
        boolean countryHasNotLaunched = disneyGlobalUtils.getBooleanFromCountries(countryCode, "hasNotLaunched");

        if (DisneyParameters.getEnv().equalsIgnoreCase("prod")) {
            headers.put(DisneyHttpHeaders.DISNEY_STAGING, TRUE);
            if ((countryHasNotLaunched || !productHasLaunched)) {
                headers.put(DisneyHttpHeaders.BAMTECH_CDN_BYPASS, BAMTECH_CDN_BYPASS_VALUE);
            }
        }

        headers.put(DisneyHttpHeaders.BAMTECH_IS_TEST, "true");

        boolean isStar = PARTNER.equalsIgnoreCase("star");

        if (!isStar) {
            headers.put(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY);
        } else  {
            headers.put(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY_STAR);
        }

        if ((countryHasNotLaunched || !productHasLaunched)) {
            if (!isStar) {
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
            } else {
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_STAR);
            }
            headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
            headers.put(DisneyHttpHeaders.BAMTECH_CANONBALL_PREVIEW, BAMTECH_CANONBALL_PREVIEW_VALUE);
        }

        boolean isGeoEdgeUnsupportedRegion = disneyGlobalUtils.getBooleanFromCountries(countryCode, IS_GEOEDGE_UNSUPPORTED_REGION)
                || disneyGlobalUtils.getBooleanFromCountries(countryCode, IS_GEOEDGE_SUPPORTED_REGION_WITH_ISSUES);
        if (isGeoEdgeUnsupportedRegion) {
            headers.put(DisneyHttpHeaders.BAMTECH_DSS_PHYSICAL_COUNTRY_OVERRIDE, countryCode);
            if (isStar) {
                headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);
                headers.put(DisneyHttpHeaders.BAMTECH_GEO_OVERRIDE, countryCode);
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_STAR);
                headers.put(DisneyHttpHeaders.BAMTECH_AKA_USER_GEO_OVERRIDE, countryCode);
                headers.put(DisneyHttpHeaders.BAMTECH_PARTNER, PARTNER);
            }
        } else if (!countryCode.equals("US")) {
            headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);
        }

        try {
            ProxyPool.registerProxy(geoedgeProxyFreshInstance.getGeoedgeProxy(country));
            proxy.set(ProxyPool.getProxy());
            //            Set<CaptureType> captureTypeSet = new HashSet<>();
            //            if(captureTypes != null) {
            //                IntStream.range(0, captureTypes.length - 1).forEach(type -> captureTypeSet.add(captureTypes[type]));
            //            } else {
            //                proxy.get().setMitmDisabled(true);
            //            }
            proxy.get().addHeaders(headers);
            proxy.get();
            proxy.get().start(Integer.parseInt(getDevice().getProxyPort()));
        } catch (NullPointerException e) {
            e.printStackTrace();
            Assert.fail(String.format("Proxy Cannot be started for country '%s'. Manual validation is required.", country));
        }
         */
//    }

    @BeforeMethod(alwaysRun = true, onlyForGroups = TestGroup.NO_RESET)
    public void enableNoTestReset() {
        R.CONFIG.put(MobileCapabilityType.NO_RESET, "true", true);
        R.CONFIG.put(MobileCapabilityType.FULL_RESET, "false", true);
    }

    @BeforeMethod(alwaysRun = true, onlyForGroups = TestGroup.PRE_CONFIGURATION)
    public void beforeAnyAppActions() {
        getDriver();
        if ("Tablet".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        }
        LOGGER.info("Starting API threads");
        // Call getDriver to set platform variables
        setBuildType();
        if (buildType == BuildType.IAP) {
            LOGGER.info("IAP build detected. Cancelling Disney+ subscription.");
            //initPage(IOSSettingsMenuBase.class).cancelActiveEntitlement("Disney+");
            //relaunch();
            //handleAlert();
        }

        try {
            initPage(DisneyPlusLoginIOSPageBase.class).dismissNotificationsPopUp();
            LOGGER.info("API threads started.");
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new SkipException("There was a problem with the setup: " + e.getMessage());
        }
        //        clearAppCache();
        handleAlert();
    }

    public enum Person {
        ADULT(DateHelper.Month.NOVEMBER, "5", "1955"),
        MINOR(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 4)),
        U13(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 12)),
        U18(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 16)),
        OLDERTHAN125(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 130));

        DateHelper.Month month;
        String day;
        String year;

        Person(DateHelper.Month month, String day, String year) {
            this.month = month;
            this.day = day;
            this.year = year;
        }

        public DateHelper.Month getMonth() {
            return this.month;
        }

        public String getDay() {
            return getDay(false);
        }

        public String getDay(Boolean leadingZero) {
            if (leadingZero && this.day.length() == 1) {
                return "0" + this.day;
            } else {
                return this.day;
            }
        }

        public String getYear() {
            return this.year;
        }
    }

    /**
     * Logs into the app by entering the provided account's credentials and username
     *
     * @param account - DisneyAccount generated for the test run
     */
    public void login(DisneyAccount account) {
        initPage(DisneyPlusLoginIOSPageBase.class).submitEmail(account.getEmail());
        initPage(DisneyPlusPasswordIOSPageBase.class).submitPasswordForLogin(account.getUserPass());
    }

    /**
     * Logs into the app by entering the provided account's credentials and username
     *
     * @param entitledUser - DisneyAccount generated for the test run
     * @param profileName  - Profile name to select after login,this is an optional param,
     *                     if you don't need to select a profile in your test, leave this param blank
     */
    public void loginToHome(DisneyAccount entitledUser, String... profileName) {
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();
        login(entitledUser);
        pause(5);
        initPage(DisneyPlusApplePageBase.class).dismissAppTrackingPopUp(5);
        if (profileName.length > 0 && !(initPage(DisneyPlusHomeIOSPageBase.class).isOpened())) {
            initPage(DisneyPlusWhoseWatchingIOSPageBase.class).clickProfile(String.valueOf(profileName[0]), true);
        }
    }

    public DisneyAccount createV2Account() {
        DisneyOffer offer = getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_PREMIUM);
        return getAccountApi().createAccount(offer, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2);
    }

    /**
     * Setup method intended to be used either in a @BeforeMethod annotation or manually
     * called in a test to set the device back to the HOME/Discover page for navigation purposes.
     * <p>
     * IF app is on Welcome, proceed through onboarding.
     * ELSE IF nav bar is not visible, restart driver and proceed through onboarding
     * ELSE tap on HOME to return to Home/Discover.
     *
     * @param account     DisneyAccount created for the test run
     * @param profileName Profile name to select after login,this is an optional param,
     *                    if you don't need to select a profile in your test, leave this param blank
     *                    call your method with just DisneyAccount param
     */
    public void setAppToHomeScreen(DisneyAccount account, String... profileName) {
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        //        handleAlert();
        //        initPage(DisneyPlusApplePageBase.class).dismissAppTrackingPopUp();
        if (disneyPlusWelcomeScreenIOSPageBase.isOpened()) {
            loginToHome(account, profileName);

        } else if (!homePage.isOpened()) {
            restart();
            handleAlert();
            //initialSetup();
            loginToHome(account, profileName);
        } else {
            disneyPlusWelcomeScreenIOSPageBase.clickHomeIcon();
        }
        pause(3);
    }

    /**
     * Dismisses system alert popups
     */
    public void handleAlert() {
        handleAlert(IOSUtils.AlertButtonCommand.DISMISS);
    }

    @Override
    public void handleAlert(IOSUtils.AlertButtonCommand command) {
        LOGGER.info("Checking for system alert to {}...", command);
        handleSystemAlert(command, 10);
    }

    public void initialSetup(String locale, String language) {
        getDriver();
        setBuildType();

        if (buildType == BuildType.IAP) {
            LOGGER.info("IAP build detected. Cancelling Disney+ subscription.");
            initPage(IOSSettingsMenuBase.class).cancelActiveEntitlement("Disney+");
            relaunch();
        }

        try {
            initPage(DisneyPlusLoginIOSPageBase.class).dismissNotificationsPopUp();
            LOGGER.info("API threads started.");
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new SkipException("There was a problem with the setup: " + e.getMessage());
        }
    }

    /**
     * Performs an app relaunch without terminating the Appium session.
     * Also gets rid of Network Visibility alert.
     */
    public void restart() {
        terminateApp(sessionBundles.get(APP));
        startApp(sessionBundles.get(DISNEY));
        //        handleAlert();
    }

    /**
     * Generates a gmail address with testUser parameters for email api partsing purposes
     *
     * @return - The email generated for use
     */
    public String generateGmailAccount() {
        return "mobile.automation.dss." + new Date().toString().replaceAll("[^a-zA-Z0-9+]", "-") + "@gmail.com";
    }

    //Common use navigation method with retries
    public void navigateToTab(DisneyPlusApplePageBase.FooterTabs tab) {
        int tries = 0;
        boolean isOpened = false;
        do {
            initPage(DisneyPlusApplePageBase.class).getDynamicAccessibilityId(tab.getLocator()).click();
            tries++;
            switch (tab) {
            case MORE_MENU:
                isOpened = initPage(DisneyPlusMoreMenuIOSPageBase.class).isOpened();
                break;
            case HOME:
                isOpened = initPage(DisneyPlusHomeIOSPageBase.class).isOpened();
                break;
            case SEARCH:
                isOpened = initPage(DisneyPlusSearchIOSPageBase.class).isOpened();
                break;
            case DOWNLOADS:
                isOpened = initPage(DisneyPlusDownloadsIOSPageBase.class).isOpened();
                break;
            }
        } while (!isOpened && tries < 3);
    }

    public void logout() {
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        if (initPage(DisneyPlusMoreMenuIOSPageBase.class).isOpened()) {
            initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT);
        }
    }

    public boolean isFooterTabPresent(DisneyPlusApplePageBase.FooterTabs tab) {
        return initPage(DisneyPlusApplePageBase.class).getDynamicAccessibilityId(tab.getLocator()).isElementPresent();
    }

    public void launchJarvis(boolean freshInstall) {
        boolean isInstalled = isAppInstalled(sessionBundles.get(JarvisAppleBase.JARVIS));
        if (isInstalled) {
            if (freshInstall) {
                terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
                removeApp(sessionBundles.get(JarvisAppleBase.JARVIS));
                installJarvis();
            }
        } else {
            installJarvis();
        }
        startApp(sessionBundles.get(JarvisAppleBase.JARVIS));

        JarvisAppleBase.fluentWait(getDriver(), 60, 0, "Unable to launch Jarvis")
                .until(it -> {
                    LOGGER.info("Jarvis is not launched, launching jarvis...");
                    pause(1);
                    boolean isRunning = isAppRunning(sessionBundles.get(JarvisAppleBase.JARVIS));
                    LOGGER.info("Is app running: {}", isRunning);
                    return isRunning;
                });
    }

    public void installAndLaunchJarvis() {
        installJarvis();
        startApp(sessionBundles.get(JarvisAppleBase.JARVIS));
        JarvisAppleBase.fluentWait(getDriver(), 60, 0, "Unable to launch Jarvis")
                .until(it -> isAppRunning(sessionBundles.get(JarvisAppleBase.JARVIS)));
    }

    public void rotateScreen(ScreenOrientation orientation) {
        try {
            rotate(orientation);
        } catch (WebDriverException wde) {
            LOGGER.error("Error rotating screen. Device may already be oriented.");
        }
    }

    public void downloadApp(String version) {
        String appCenterAppName = R.CONFIG.get("capabilities.app");
        LOGGER.info("App Download: {}", appCenterAppName);
        if (appCenterAppName.contains("for_Automation")) {
            installApp(AppCenterManager.getInstance()
                    .getAppInfo(String.format("appcenter://Dominguez-Non-IAP-Prod-Enterprise-for-Automation/ios/enterprise/%s", version))
                    .getDirectLink());
        } else if (appCenterAppName.contains("Disney")) {
            installApp(AppCenterManager.getInstance()
                    .getAppInfo(String.format("appcenter://Disney-Prod-Enterprise/ios/enterprise/%s", version))
                    .getDirectLink());
        }
    }

    public void downloadDisneyApp() {
        String appCenterAppName = R.CONFIG.get("capabilities.app");
        String appVersion = R.CONFIG.get("appVersion");
        LOGGER.info("App Download: {}", appCenterAppName);
        if (appCenterAppName.contains("for_Automation")) {
            installApp(AppCenterManager.getInstance()
                    .getAppInfo(String.format("appcenter://Dominguez-Non-IAP-Prod-Enterprise-for-Automation/ios/enterprise/%s", appVersion))
                    .getDirectLink());
        } else if (appCenterAppName.contains("Disney")) {
            installApp(AppCenterManager.getInstance()
                    .getAppInfo(String.format("appcenter://Disney-Prod-Enterprise/ios/enterprise/%s", appVersion))
                    .getDirectLink());
        }
    }

    public void addHoraValidationSku(DisneyAccount accountToEntitle) {
        if (DisneyConfiguration.isHoraEnabled()) {
            try {
                getAccountApi().entitleAccount(accountToEntitle, DisneySkuParameters.DISNEY_HORA_VALIDATION, "V1");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void checkAssertions(SoftAssert softAssert, String accountId, JSONArray checkList) {
        if (DisneyConfiguration.isHoraEnabled()) {
            HoraValidator hv = new HoraValidator(accountId);
            hv.assertValidation(softAssert);
            hv.checkListForPQOE(softAssert, checkList);
        }
    }

    public DisneyAccount createAccountWithSku(DisneySkuParameters sku, String country, String language) {
        CreateDisneyAccountRequest request = new CreateDisneyAccountRequest();
        request.addSku(sku);
        request.setCountry(country);
        request.setLanguage(language);
        return getAccountApi().createAccount(request);
    }

    public void setOneTrustConfig() {
        DisneyPlusApplePageBase applePageBase = initPage(DisneyPlusApplePageBase.class);
        JarvisAppleBase jarvis = getJarvisPageFactory();
        launchJarvisOrInstall();
        jarvis.openAppConfigOverrides();
        jarvis.openOverrideSection("platformConfig");
        applePageBase.scrollToItem("oneTrustConfig").click();
        LOGGER.info("fetching oneTrustConfig value from config file:" + R.CONFIG.get("oneTrustConfig"));
        boolean enableOneTrustConfig = Boolean.parseBoolean(R.CONFIG.get("enableOneTrustConfig"));
        if (enableOneTrustConfig) {
            LOGGER.info("Navigating to domainIdentifier..");
            applePageBase.scrollToItem("domainIdentifier").click();
            applePageBase.saveDomainIdentifier("ac7bd606-0412-421f-b094-4066acca7edd-test");
            applePageBase.navigateBack();
            LOGGER.info("Navigating to isEnabledV2..");
            applePageBase.scrollToItem("isEnabledV2").click();
            applePageBase.enableOneTrustConfig();
        } else {
            applePageBase.removeDomainIdentifier();
            applePageBase.navigateBack();
            applePageBase.disableOneTrustConfig();
        }
        LOGGER.info("Terminating Jarvis app..");
        terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
        LOGGER.info("Restart Disney app..");
        restart();
        LOGGER.info("Click allow to track your activity..");
                handleAlert();
    }

    public void disableBrazeConfig() {
        DisneyPlusApplePageBase applePageBase = initPage(DisneyPlusApplePageBase.class);
        JarvisAppleBase jarvis = getJarvisPageFactory();
        installAndLaunchJarvis();
        jarvis.openAppConfigOverrides();
        applePageBase.scrollToItem("brazeConfig").click();
        LOGGER.info("Navigating to isEnabled..");
        applePageBase.scrollToItem("isEnabled").click();
        applePageBase.disableBrazeConfig();
        LOGGER.info("Terminating Jarvis app..");
        terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
        LOGGER.info("Restart Disney app..");
        restart();
        LOGGER.info("Click allow to track your activity..");
        handleAlert();
    }

    public void launchJarvisOrInstall() {
        DisneyPlusApplePageBase applePageBase = initPage(DisneyPlusApplePageBase.class);
        boolean isInstalled = isAppInstalled(sessionBundles.get(JarvisAppleBase.JARVIS));
        LOGGER.info("Attempting to launch Jarvis app...");
        if (isInstalled) {
            launchJarvisNoInstall();
            if (!applePageBase.isCompatibleDisneyTextPresent()) {
                launchJarvis(true);
            }
        } else {
            launchJarvis(true);
        }
    }

    public void launchJarvisNoInstall() {
        startApp(sessionBundles.get(JarvisAppleBase.JARVIS));
        JarvisAppleBase.fluentWait(getDriver(), 60, 0, "Unable to launch Jarvis")
                .until(it -> {
                    LOGGER.info("Jarvis is not launched, launching jarvis...");
                    pause(1);
                    boolean isRunning = isAppRunning(sessionBundles.get(JarvisAppleBase.JARVIS));
                    LOGGER.info("Is app running: {}", isRunning);
                    return isRunning;
                });
    }

    //TODO: uncomment it after moving the Subscription test to a separate XML

    //    public void clearDSSSandboxAccountFor(String accountName) {
    //        LOGGER.info("Clearing purchase history for '{}' account", accountName);
    //        AppStoreConnectApi appStoreConnectApi = new AppStoreConnectApi();
    //        for (SandboxAccount account : DisneyPlusIAPStandardPurchaseTest.accountsList) {
    //            if (account.getAttributes().getAcAccountName().contains(accountName)) {
    //                Assert.assertTrue(appStoreConnectApi.clearAccountPurchaseHistory(account.getId()).getStatusCode()
    //                                .is2xxSuccessful(),
    //                        "Clear account purchase history for" + accountName + "was not successful!");
    //            }
    //        }
    //    }
}
