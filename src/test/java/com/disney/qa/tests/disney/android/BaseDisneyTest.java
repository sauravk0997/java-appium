package com.disney.qa.tests.disney.android;

import com.disney.listeners.MultiverseDevicesListener;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.jarvis.JarvisParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.common.google_play_store.android.pages.common.GooglePlayHandler;
import com.disney.qa.common.jarvis.android.JarvisAndroidBase;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.tests.BaseMobileTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.nordstrom.automation.testng.LinkedListeners;
import com.qaprosoft.appcenter.AppCenterManager;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import io.appium.java_client.AppiumDriver;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.core.har.HarEntry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.invoke.MethodHandles;
import java.util.*;

@SuppressWarnings("squid:S2187")
@LinkedListeners(MultiverseDevicesListener.class)
public class BaseDisneyTest extends BaseMobileTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static final String APP_PACKAGE = "com.disney.disneyplus";
    protected static final String APP_LAUNCH_ACTIVITY = "com.bamtechmedia.dominguez.main.MainActivity";
    protected static final String APP_NAME = "Disney+";
    protected static final String DEFAULT_PROFILE = "Test";
    protected static final String ENV = R.CONFIG.get("capabilities.custom_env").toLowerCase();
    protected static final String PARTNER = "disney";
    protected static final String PLATFORM = "android";
    protected static Set<String> devices = new HashSet<>();
    protected List<String> brands = new LinkedList<>();
    protected DisneyMobileConfigApi mobileConfigApi;

    protected ThreadLocal<AndroidUtilsExtended> androidUtils = new ThreadLocal<>();
    protected ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    protected ThreadLocal<DisneyAccountApi> accountApi = new ThreadLocal<>();
    protected ThreadLocal<DisneySearchApi> searchApi = new ThreadLocal<>();
    protected ThreadLocal<DisneyLocalizationUtils> languageUtils = new ThreadLocal<>();
    protected ThreadLocal<ITestContext> localContext = new ThreadLocal<>();

    private static final String DISNEY_PLUS_CUSTOM_APP_CENTER_VERSION = "2.17.0-rc5.2302152";
    private static final String JARVIS_APP_NAME = "Disney-Jarvis-1";
    private static final String JARVIS_BUILD_TYPE = "android";
    private static final String JARVIS_PLATFORM_NAME = "jarvis-dev-release";
    private static final String JARVIS_PLATFORM_VERSION = "latest";
    private static final Integer JARVIS_PLAYBACK_OVERRIDE = 8;
    private static final int JARVIS_INSTALLATION_WAIT = 45;
    private static final int JARVIS_POLLING_WAIT = 10;
    private static final int JARVIS_ENV_CONFIG_WAIT = 5;

    private DisneyLocalizationUtils rootLocalization;
    private static String defaultDevices;
    private static String appCenterDownloadUrl;
    private boolean testXmlEnableProxyValue = true;
    private boolean testXmlEnableProxy = false;
    private boolean downloadCustomBuild = false;

    public boolean chromecastEnabled;

    public void setLocalizationData() {
        languageUtils.set(new DisneyLocalizationUtils(R.CONFIG.get("locale"), R.CONFIG.get("language"), PLATFORM, DisneyParameters.getEnv(), PARTNER));
        languageUtils.get().setDictionaries(mobileConfigApi.getDictionaryVersions());
        languageUtils.get().setLegalDocuments();
    }

    public void setLocalizationDataByCountryName(String countryName, String language) {
        languageUtils.set(new DisneyLocalizationUtils(R.CONFIG.get("locale"), R.CONFIG.get("language"), PLATFORM, DisneyParameters.getEnv(), PARTNER));
        languageUtils.get().setCountryDataByName(countryName);
        languageUtils.get().setLanguageCode(language);
        R.CONFIG.put("locale", languageUtils.get().getLocale());
        setConstants(localContext.get());
    }

    public void setLocalizationDataByCountryCode(String countryCode, String language) {
        languageUtils.set(new DisneyLocalizationUtils(R.CONFIG.get("locale"), R.CONFIG.get("language"), PLATFORM, DisneyParameters.getEnv(), PARTNER));
        languageUtils.get().setCountryDataByCode(countryCode);
        languageUtils.get().setLanguageCode(language);
        R.CONFIG.put("locale", languageUtils.get().getLocale());
    }

    //Checks if WiFi is on but disconnected (signal is lost for some reason). Turns WiFi off to refresh.
    private void checkWiFi() {
        if (androidUtils.get().isWifiEnabled() && !androidUtils.get().isWiFiConnected()) {
            LOGGER.info("Device WiFi is enabled but does not have a connection. Restarting network...");
            androidUtils.get().disableWifi();
        }
        androidUtils.get().enableWifi();
        androidUtils.get().waitForWifiToStabilize();
    }

    private void resetLocaleLanguage() {
        R.CONFIG.put("locale", "US");
        R.CONFIG.put("language", "en");
    }

    private boolean isCustomTestXmlLocaleLanguageSet(ITestContext context) {
        return context.getCurrentXmlTest().getParameter("locale") != null
                && context.getCurrentXmlTest().getParameter("language") != null;
    }

    private void setDisneyPlusCustomAppCenterVersionUrl(ITestContext context) {
        if (context.getCurrentXmlTest().getParameter("jenkinsJobName") == null) {
            return;
        }

        downloadCustomBuild = true;
        String packageName =
                context.getCurrentXmlTest().getSuite().getName()
                        .contains("Amazon") ? "Disney-Fire-Tablet" : "Disney-Mobile";
        String buildType =
                context.getCurrentXmlTest().getSuite().getName()
                        .contains("Amazon") ? "mobile.amazon.debug" : "mobile.google.debug";

        appCenterDownloadUrl = AppCenterManager.getInstance().getDownloadUrl(
                packageName,
                "android",
                buildType,
                DISNEY_PLUS_CUSTOM_APP_CENTER_VERSION);
    }

    @BeforeSuite(alwaysRun = true)
    public void setCustomBuildVersion(ITestContext context) {
        if (R.CONFIG.getBoolean("appcenter_use_jenkins_build_version")) {
            LOGGER.info(
                    "appcenter_use_jenkins_build_version set to true and setting build version from Jenkins");
            return;
        }

        localContext.set(context);
        R.CONFIG.put("capabilities.fullReset", "false");
        R.CONFIG.put("capabilities.enforceAppInstall", "true");
        setDisneyPlusCustomAppCenterVersionUrl((context));
        if (downloadCustomBuild) {
            LOGGER.info("Custom build version being set to {}", DISNEY_PLUS_CUSTOM_APP_CENTER_VERSION);
            R.CONFIG.put("capabilities.app", appCenterDownloadUrl);
        }
    }

    @BeforeClass(alwaysRun = true)
    public void setConstants(ITestContext context) {
        localContext.set(context);

        if (isCustomTestXmlLocaleLanguageSet(context)) {
            resetLocaleLanguage();
        }

        String testXmlLocale = context.getCurrentXmlTest().getParameter("locale");
        String testXmlLanguage = context.getCurrentXmlTest().getParameter("language");
        String testXmlEnableProxy = context.getCurrentXmlTest().getParameter("enableProxy");

        LOGGER.info("Setting test constants...");
        R.CONFIG.put("max_driver_count", "10");
        R.CONFIG.put("capabilities.newCommandTimeout", "50000");

        if (isCustomTestXmlLocaleLanguageSet(context)) {
            LOGGER.info("Test run has custom locale and language configured from Test XML...");
            this.testXmlEnableProxyValue = Objects.equals(testXmlEnableProxy, "true");
            if (this.testXmlEnableProxyValue) {
                LOGGER.info("Test XML has param 'enableProxy' to true and enabling proxy");
                this.testXmlEnableProxy = true;
            }
            R.CONFIG.put("locale", testXmlLocale);
            R.CONFIG.put("language", testXmlLanguage);
        }

        locale = R.CONFIG.get("locale");
        language = R.CONFIG.get("language");
        rootLocalization = new DisneyLocalizationUtils(locale, language, PLATFORM, DisneyParameters.getEnv(), PARTNER);

        AndroidUtilsExtended util = new AndroidUtilsExtended();
        String version = util.getAppVersionName(APP_PACKAGE);
        String[] sections = version.split("\\.");
        version = String.format("%s.%s", sections[0], sections[1]);

        mobileConfigApi = new DisneyMobileConfigApi(PLATFORM, DisneyParameters.getEnv(), PARTNER, version);
        rootLocalization.setDictionaries(mobileConfigApi.getDictionaryVersions());
        rootLocalization.setLegalDocuments();
        LOGGER.info("Constants set.");
    }

    @BeforeMethod(alwaysRun = true)
    public void initialSetup(ITestContext context) {
        LOGGER.info("Setting up test parameters...");
        localContext.set(context);

        if(defaultDevices == null) {
            defaultDevices = R.CONFIG.get("capabilities.deviceName");
        }

        if(context.getCurrentXmlTest().getName().contains("IAP")) {
            limitDevicePoolForIAP();
        } else {
            R.CONFIG.put("capabilities.deviceName", defaultDevices);
        }

        languageUtils.set(rootLocalization);
        getDriver();
        androidUtils.set(new AndroidUtilsExtended());

        checkWiFi();
        jarvisConfigSetup();
        LOGGER.info("Checking proxy settings...");
        startProxyAndRestart();
        accountApi.set(new DisneyAccountApi(PLATFORM, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), PARTNER));
        disneyAccount.set(accountApi.get().createAccount(new DisneyOffer(), languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), "V1"));
        searchApi.set(new DisneySearchApi(PLATFORM, ENV, PARTNER));
        DisneyPlusCommonPageBase.setDictionary(languageUtils.get());

        /*
         * Sets a capability for tablet devices to force portrait mode as the default orientation.
         */
        if (isAndroidTablet()) {
            androidUtils.get().setPortraitCapability();
        }

        LOGGER.info("Pre-test configurations completed.");
    }

    /**
     * Executes a fresh Jarvis installation on the test device if it has not been used for the current test run.
     * Subsequent overrides for specific tests can use this installation with launchJarvis(false) if they wish to
     * use the GUI (requires setting useGUI to true) or by simply activating the overrides.
     * We will try to configure Jarvis via adb 5 times before failing out.
     */
    private void jarvisConfigSetup() {
        JarvisAndroidBase jarvis = initPage(JarvisAndroidBase.class);
        String device = getDevice().getUdid();
        boolean isJarvisConfigured = false;
        int jarvisAttempt = 1;

        if (!devices.contains(device)) {
            LOGGER.info("Device ID '{}' has not had Jarvis freshly installed for this test run. Installing latest version... ", device);
            devices.add(device);
            uninstallJarvis();
            installJarvis();
            verifyJarvisInstalled();
        }

        while (!isJarvisConfigured && jarvisAttempt < 6) {
            try {
                LOGGER.info("Attempt {} to configure Jarvis via adb", jarvisAttempt);
                JarvisAndroidBase.clearAdbOverrides();
                JarvisAndroidBase.CTV_Activation.DISABLE_CTV_ACTIVATION.setOverrideValue(true);
                JarvisAndroidBase.CTV_Activation.ENABLE_V2_FLOW.setOverrideValue(false);
                JarvisAndroidBase.Playback.HIDE_TIMEOUT.setOverrideValue(JARVIS_PLAYBACK_OVERRIDE);
                JarvisAndroidBase.Playback.QUICK_CHROME_FADEOUT.setOverrideValue(JARVIS_PLAYBACK_OVERRIDE);
                jarvis.activateOverrides();
                setEnvironment();
                pause(JARVIS_ENV_CONFIG_WAIT);
                clearAppCache();
                isJarvisConfigured = true;
                LOGGER.info("Successfully configured Jarvis on attempt {}", jarvisAttempt);
            } catch (Exception e) {
                LOGGER.error("Exception occurred configuring Jarvis on attempt {}", jarvisAttempt);
                uninstallJarvis();
                installJarvis();
                verifyJarvisInstalled();
                e.printStackTrace();
                jarvisAttempt++;
            }
        }

        if (!isJarvisConfigured) {
            Assert.fail(
                    "Jarvis configuration is failing on this device after "
                            + --jarvisAttempt + " attempts. Investigation is required.");
        }
    }

    /**
     * Compares the deviceName parameter from Jenkins to the devices subset on the handset and tablet IAP tests.
     * IF deviceName = "any" THEN apply the full subset for randomization.
     * ELSE IF deviceName = "<device name(s)>" THEN check if <device name(s)> is on the subset list.
     *      FOR EACH specified device name
     *          IF <device name> is on the subset list THEN keep
     *              IF kept names is empty, throw failure indicating specification is invalid.
     */
    private void limitDevicePoolForIAP() {
        LOGGER.warn("Limiting device pool for IAP test...");
        List<String> devices = List.of(R.CONFIG.get("capabilities.deviceName").split(","));
        String subset = localContext.get().getCurrentXmlTest().getParameter("iapDevices");
        LOGGER.info("Config Devices: {}", devices);
        if(devices.get(0).equals("any")) {
            LOGGER.info("deviceName set to 'any.' Using full subset.");
            R.CONFIG.put("capabilities.deviceName", subset, true);
        } else {
            LOGGER.info("Specific device found. Checking for matching entry");
            List<String> iapSubset = List.of(subset.split(","));
            List<String> customList = new LinkedList<>();
            devices.forEach(device -> {
                if(iapSubset.contains(device)) {
                    customList.add(device);
                }
            });

            if(customList.isEmpty()) {
                Assert.fail("No valid devices were provided for IAP test. Leave deviceName=any or set to valid devices: " + subset);
            } else {
                R.CONFIG.put("capabilities.deviceName", String.join(",", customList), true);
            }
        }
    }

    public DisneyAccountApi getAccountApi() {
        return accountApi.get();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (R.CONFIG.get(BROWSERMOB_PROXY).equals(TRUE)) {
            R.CONFIG.put(BROWSERMOB_PROXY, FALSE);
        }

        LOGGER.info("Cleaning threads");
        androidUtils.remove();
        disneyAccount.remove();
        accountApi.remove();
        searchApi.remove();
        languageUtils.remove();
        LOGGER.info("Threads cleaned");
    }

    public void login(DisneyAccount account, boolean withProfileSelect) {
        initPage(DisneyPlusWelcomePageBase.class).continueToLogin();
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        loginPageBase.proceedToPasswordMode(account.getEmail());
        loginPageBase.logInWithPassword(account.getUserPass());
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        if (withProfileSelect && loginPageBase.isProfileSelectDisplayed()) {
            loginPageBase.waitForLoading();
            commonPageBase.clickOnGenericTextExactElement(account.getProfiles().get(0).getProfileName());
        }
    }

    //Method proceeds through login with direct user email and password
    public void manualLogin(String email, String pass) {
        initPage(DisneyPlusWelcomePageBase.class).continueToLogin();
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        loginPageBase.proceedToPasswordMode(email);
        loginPageBase.logInWithPassword(pass);
    }

    //Method logs out of the app through native functions. Requires the app be displaying the nav menu
    public void logout() {
        DisneyContentApiChecker apiChecker = new DisneyContentApiChecker();
        JsonNode appDictionary = apiChecker.getFullDictionaryBody(languageUtils.get().getUserLanguage());
        initPage(DisneyPlusCommonPageBase.class).navigateToPage(
                apiChecker.getDictionaryItemValue(appDictionary, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        moreMenuPageBase.selectMenuItem(
                apiChecker.getDictionaryItemValue(appDictionary, DisneyPlusMoreMenuPageBase.MenuList.LOGOUT.getText()));
        if (moreMenuPageBase.isLogoutModalPresent()) {
            moreMenuPageBase.confirmLogout();
        }
        if (!initPage(DisneyPlusWelcomePageBase.class).isOpened()) {
            Assert.fail("ERROR: Something went wrong with Logout.");
        }
    }

    protected void internalSetup() {
        AndroidUtilsExtended util = new AndroidUtilsExtended();
        LOGGER.info("Closing app to begin test at landing page...");
        util.closeApp();
        util.launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_discover_deeplink"));
    }

    protected String clearLogs() {
        LOGGER.info("Flushing logs for: " + APP_PACKAGE + ".....");

        AndroidService androidService = AndroidService.getInstance();

        return androidService.executeAdbCommand("logcat -c " + APP_PACKAGE);
    }

    protected String generateLogs() {
        LOGGER.info("Generating logs for package: " + APP_PACKAGE + ".....");

        AndroidService androidService = AndroidService.getInstance();

        return androidService.executeAdbCommand("logcat -d " + APP_PACKAGE);
    }

    public boolean dismissChromecastOverlay() {
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        if (discoverPageBase.isChromecastPopupDisplayed()) {
            LOGGER.info("Popup is present. Dismissing...");
            chromecastEnabled = true;
            DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
            commonPageBase.waitForSuccessPopup();
            commonPageBase.clickOnMenuBox();
            discoverPageBase.waitForChromecastToClose();
        } else {
            LOGGER.info("Popup not present. Proceeding with test.");
            chromecastEnabled = new AndroidUtilsExtended().isWifiEnabled() && discoverPageBase.isChromecastAvailable();
        }
        return chromecastEnabled;
    }

    public void dismissSmartLockPopup() {
        initPage(DisneyPlusCommonPageBase.class).dismissSmartLock();
    }

    public void removeDownloads() {
        initPage(DisneyPlusDownloadsPageBase.class).deleteAllDownloads();
    }

    public void addNewProfile(String profileName) {
        LOGGER.info("Creating new profile: " + profileName);
        initPage(DisneyPlusDiscoverPageBase.class)
                .navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                        DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        initPage(DisneyPlusMoreMenuPageBase.class).addNewProfile(profileName, false, androidUtils.get());
        LOGGER.info("Completed.");
    }

    public void removeChildAccount(JsonNode dictionary) {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        commonPageBase.navigateToPage(new DisneyContentApiChecker().getDictionaryItemValue(
                dictionary, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));

        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        if (!moreMenuPageBase.isOpened()) {
            LOGGER.info("Something went wrong with the navigaiton. Trying again...");
            commonPageBase.navigateToPage(DisneyPlusCommonPageBase.MenuItem.MORE.getText());
        }
        initPage(DisneyPlusMoreMenuPageBase.class).deleteChildProfile(dictionary);
    }

    /**
     * Backgrounds the app and pauses for 3 seconds to ensure
     * that it is actually backgrounded. Helps ensure relaunch commands don't fire
     * while the background process is running.
     */
    public void closeAppForRelaunch() {
        androidUtils.get().closeApp();
        pause(3);
    }

    public boolean isVideoCallingCorrectID(BrowserMobProxy proxy, String mediaID) {
        for (HarEntry entry : proxy.getHar().getLog().getEntries()) {
            if (entry.getRequest().getUrl().contains(mediaID)) {
                return true;
            }
        }
        printProxyLog(proxy);
        return false;
    }

    public void printProxyLog(BrowserMobProxy proxy) {
        LOGGER.info("Printing all DSS related session requests/responses for test session:");
        LOGGER.info("Filtering to only URL requests containing 'bam, dss, and disney'...");
        for (HarEntry entry : proxy.getHar().getLog().getEntries()) {
            if (entry.getRequest().getUrl().contains("bam") || entry.getRequest().getUrl().contains("dss") ||
                    entry.getRequest().getUrl().contains("disney")) {
                LOGGER.info("Request: " + entry.getRequest().getUrl());
                LOGGER.info("Response: " + entry.getResponse().getContent().getText());
            }
        }
    }

    public JsonNode getFullDictionary(String lang) {
        DisneyApiProvider apiProvider = new DisneyApiProvider();
        return apiProvider.getFullDictionaryBody(lang);
    }

    public void activityAndPackageLaunch() {
        new AndroidUtilsExtended().launchAppThroughStartActivity(APP_PACKAGE, APP_LAUNCH_ACTIVITY);
    }

    public void cleanLaunch() {
        new AndroidUtilsExtended().closeAppStack((AppiumDriver) getCastedDriver());
        pause(3);
        activityAndPackageLaunch();
    }

    public void uninstallJarvis() {
        AndroidService androidService = AndroidService.getInstance();
        boolean isInstalled = !androidService.executeAdbCommand("shell pm list packages "+ JarvisParameters.ANDROID_JARVIS_PACKAGE.getValue()).isEmpty();
        if(isInstalled) {
            androidService.executeAdbCommand("uninstall " + JarvisParameters.ANDROID_JARVIS_PACKAGE.getValue());
        }
    }

    /**
     * Gets the device driver for setting env value via Jarvis
     */
    public void setEnvironment() {
        getDriver();
        String env = R.CONFIG.get("capabilities.custom_env");
        LOGGER.info("Setting environment to '{}'", env);
        switch (env) {
            case "PROD":
                setEnvironment(JarvisAndroidBase.DisneyEnvironments.PRODUCTION);
                LOGGER.info("Default environment in use. Proceeding with test...");
                break;
            case "QA":
                setEnvironment(JarvisAndroidBase.DisneyEnvironments.QA);
                break;
            case "EDITORIAL":
                setEnvironment(JarvisAndroidBase.DisneyEnvironments.EDITORIAL);
                break;
            case "STAGING":
                setEnvironment(JarvisAndroidBase.DisneyEnvironments.STAGING);
                break;
            case "STAR_PROD":
                setEnvironment(JarvisAndroidBase.DisneyEnvironments.STAR_PRODUCTION);
                break;
            case "STAR_QA":
                setEnvironment(JarvisAndroidBase.DisneyEnvironments.STAR_QA);
                break;
            default:
                Assert.fail("Invalid Environment for test");
        }
    }

    public void setEnvironment(JarvisAndroidBase.DisneyEnvironments environment) {
        JarvisAndroidBase jarvis = initPage(JarvisAndroidBase.class);
        jarvis.setDisneyEnvironment(environment);
    }

    //TODO: This would be better served inside of the Jarvis library itself to reduce redundancy between platforms.
    public void launchJarvis() {
        launchJarvis(true);
    }

    /**
     * Launches Jarvis
     *
     * @param freshInstall - If TRUE, check for pre-existing Jarvis package and uninstall/reinstall latst version
     *                     - If FALSE, does not uninstall prior to launch attempts.
     */
    public void launchJarvis(boolean freshInstall) {
        boolean isInstalled = !androidUtils.get().executeAdbCommand("shell pm list packages " + JarvisParameters.ANDROID_JARVIS_PACKAGE.getValue()).isEmpty();
        int tries = 0;
        if (freshInstall || !isInstalled) {
            installJarvis();
        }
        String output;
        do {
            output = AndroidService.getInstance().executeShell("am start -n " + JarvisParameters.getJarvisLaunchAdbCommand());
            tries++;
        } while (output.isEmpty() && tries < 3);

        Assert.assertFalse(output.isEmpty(),
                "Jarvis did not launch");
    }

    private void installJarvis() {
        androidUtils.get().executeAdbCommand("uninstall " + JarvisParameters.ANDROID_JARVIS_PACKAGE.getValue());
        androidUtils.get().installApp(
                AppCenterManager.getInstance().getDownloadUrl(
                        JARVIS_APP_NAME,
                        JARVIS_BUILD_TYPE,
                        JARVIS_PLATFORM_NAME,
                        JARVIS_PLATFORM_VERSION));
    }

    private void verifyJarvisInstalled() {
        LOGGER.info("Confirming successful installation of Jarvis");
        DisneyPlusCommonPageBase.fluentWait(
                        getDriver(),
                        JARVIS_INSTALLATION_WAIT,
                        JARVIS_POLLING_WAIT,
                        "Jarvis installation unsuccessful or passed allowed time threshold")
                .until(it -> isApplicationInstalled(JarvisParameters.ANDROID_JARVIS_PACKAGE.getValue()));
    }

    //Refreshes the app 5 times in an attempt to find the SKU in the Play Store
    public void verifyPurchaseOptionAvailable(JsonNode dictionary) {
        AndroidUtilsExtended util = new AndroidUtilsExtended();
        for (int i = 0; i < 5; i++) {
            if (initPage(DisneyPlusWelcomePageBase.class).isPurchaseAvailable(dictionary)) {
                return;
            } else {
                util.closeAppStack((AppiumDriver) getCastedDriver());
                pause(3);
                activityAndPackageLaunch();
            }
        }
        Assert.fail("ERROR: Connection with Google Play cannot be established in this session.");
    }

    public void iapSetup() {
        GooglePlayHandler googlePlayHandler = initPage(GooglePlayHandler.class);

        Assert.assertTrue(googlePlayHandler.openPlayStore(),
                "Play Store did not launch");

        googlePlayHandler.clearAppSubscriptionListings(APP_NAME);
        activityAndPackageLaunch();
        verifyConnectionIsGood();
    }

    public String getPaywallMonthlyButtonText() {
        String separator = StringUtils.substringBetween(
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, "btn_monthly_price"), "${PRICE_0}", "${TIME_UNIT_0}");
        if (separator == null) {
            return languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, "month") +
                    StringUtils.substringBetween(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, "btn_monthly_price"), "${TIME_UNIT_0}", " ${PRICE_0}");
        } else {
            return separator + languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, "month");
        }
    }

    public String generateNewUserEmail() {
        return "testguid+" + new Date().toString().replaceAll("[^a-zA-Z0-9+]", "-") + "@gsuite.disneyplustesting.com";
    }

    public DisneyPlusPaywallPageBase registerNewUser(JsonNode appDictionary, String user) {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);

        verifyPurchaseOptionAvailable(appDictionary);
        welcomePageBase.proceedToSignUp();

        initPage(DisneyPlusLoginPageBase.class)
                .registerNewEmail(user)
                .registerPassword();
        return initPage(DisneyPlusPaywallPageBase.class);
    }

    /*
     * Method clears the app cache and opened stack before registering a new user ID for a purchase restoration.
     * Returns a boolean value of whether a link error occurred, which according to product specifications
     * should only be on the third or higher D+ account registration.
     */
    public void cleanRegisterAndRestorePurchase(JsonNode dictionary, String user) {
        androidUtils.get().clearAppCache();
        activityAndPackageLaunch();
        verifyConnectionIsGood();
        registerNewUser(dictionary, user)
                .clickRestorePurchaseBtn();
    }

    public void startProxyAndRestart() {
        if (isRatingsProxyBypassEnabled()) {
            LOGGER.info("enable_ratings_configuration is set to true and bypassing proxy.");
            return;
        }
        if (!testXmlEnableProxyValue) {
            LOGGER.info("Test XML enable proxy is false and bypassing proxy.");
            return;
        }
        if (!isConfigLocaleUS() || horaEnabled() || testXmlEnableProxy) {
            startProxyAndRestart(languageUtils.get().getCountryName());
            verifyConnectionIsGood();
        }
    }

    public void startProxyAndRestart(String country) {
        try {
            initiateProxy(country);
        } catch (AssertionError ae) {
            LOGGER.info("There is no proxy registered for the current thread. Restarting the driver with required parameters...");
            R.CONFIG.put(BROWSERMOB_PROXY, TRUE, true);
            restartDriver(true);
            initiateProxy(country);
        }
        if (horaEnabled()) {
            proxy.get().newHar();
        }
        clearAppCache();
    }

    public boolean isConfigLocaleUS() {
        return R.CONFIG.get("locale").equals("US");
    }

    public boolean isRatingsProxyBypassEnabled() { return R.CONFIG.getBoolean("enable_ratings_configuration"); }

    /**
     * Starting the proxy server can cause the initial launch to have a connection error.
     * This will determine if the Sign-Up button is displayed, and restart the driver up to
     * a maximum of 3 times before proceeding and allowing a natural stack trace to display
     * an error.
     */
    public void verifyConnectionIsGood() {
        LOGGER.info("Checking network stability...");
        boolean welcomeLaunched = initPage(DisneyPlusWelcomePageBase.class).isSignUpButtonPresent();
        int attempts = 0;
        if (!welcomeLaunched) {
            do {
                LOGGER.info("Sign Up button was not detected. Relaunching the driver to clear all data...");
                restartDriver(true);
                welcomeLaunched = initPage(DisneyPlusWelcomePageBase.class).isSignUpButtonPresent();
                attempts++;
            } while (!welcomeLaunched && attempts < 3);
        }

        if (!welcomeLaunched) {
            Assert.fail("App Launch was unsuccessful. Either the app was not open or there is something blocking the network connection.");
        }
    }

    public void activateRatingsJarvisOverrides(String country, JarvisAndroidBase jarvisAndroidBase, boolean forceJarvisOverrides) {
        if (isRatingsProxyBypassEnabled() || forceJarvisOverrides) {
            LOGGER.info("Setting Jarvis location override to {}'", country);
            JarvisAndroidBase.Delorean.COUNTRY.setOverrideValue(country);
            jarvisAndroidBase.activateOverrides();
            clearAppCache();
        }
    }

    public List<String> getRefinedRatingSystemValues(String profileName) {
        List<String> ratingSystemValues = new ArrayList<>();
        List<String> ratings = disneyAccount.get().getProfile(profileName).getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues();
        for (int i = 0; i < ratings.size(); i++) {
            ratingSystemValues.add(ratings.get(i).replace("+", ", "));
        }
        return ratingSystemValues;
    }

    public void forceUnsupportedLocationJarvis() {
        JarvisAndroidBase jarvisHandset = initPage(JarvisAndroidBase.class);
        JarvisAndroidBase.LocationAvailability.FORCE_UNSUPPORTED_LOCATION.setOverrideValue(true);
        jarvisHandset.activateOverrides();
        new AndroidUtilsExtended().clearAppCache();
    }
}
