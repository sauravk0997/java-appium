package com.disney.qa.tests.disney.apple.ios;

import static com.disney.qa.common.utils.IOSUtils.DEVICE_TYPE;

import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.asserts.SoftAssert;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.pojos.ApiConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.common.utils.ios_settings.IOSSettingsMenuBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDownloadsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWhoseWatchingIOSPageBase;
import com.disney.qa.hora.validationservices.HoraValidator;
import com.disney.qa.tests.disney.apple.DisneyAppleBaseTest;
import com.zebrunner.carina.appcenter.AppCenterManager;
import com.zebrunner.carina.commons.artifact.IArtifactManager;
import com.zebrunner.carina.utils.Configuration.Parameter;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.utils.mobile.ArtifactProvider;

import io.appium.java_client.ios.IOSDriver;

@SuppressWarnings("squid:S2187")
public class DisneyBaseTest extends DisneyAppleBaseTest {
    public static final String DEFAULT_PROFILE = "Test";
    public static final String KIDS_PROFILE = "KIDS";
    public static final String SECONDARY_PROFILE = "Secondary";
    public static final String PHONE = "Phone";
    public static final String TABLET = "Tablet";
    public static final String JUNIOR_MODE_HELP_CENTER = "Junior Mode on Disney+";
    public static final String DISNEY_PLUS_HELP_CENTER = "Disney+ Help Center";
    public static final String RESTRICTED = "Restricted";
    public static final String SANDBOX_ACCOUNT_PREFIX = "dsqaaiap";
    public static final String SUBSCRIPTION_V1 = "V1";
    public static final String SUBSCRIPTION_V3 = "V3";
    public static final String SUBSCRIPTION_V2_ORDER = "V2-ORDER";
    //Keeping this not to a specific plan name to support localization tests
    //Plan names in non-us countries might differ from that in us.
    public static final String BUNDLE_PREMIUM = "Yearly";
    public static final String BUNDLE_BASIC = "Disney+ With Ads, Hulu with Ads, and ESPN+";

    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected ThreadLocal<DisneyContentApiChecker> disneyApiHandler = new ThreadLocal<>();
    protected ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    protected ThreadLocal<DisneyAccountApi> disneyAccountApi = new ThreadLocal<>();
    protected ThreadLocal<DisneyMobileConfigApi> configApi = new ThreadLocal<>();
    protected ThreadLocal<DisneySearchApi> searchApi = new ThreadLocal<>();
    
    public enum Person {
        ADULT(DateHelper.Month.NOVEMBER, "5", "1955"),
        MINOR(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 5)),
        U13(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 12));

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
            if(leadingZero && this.day.length() == 1) {
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
     * Dismisses system alert popups
     */
    public void handleAlert() {
        handleAlert(IOSUtils.AlertButtonCommand.DISMISS);
    }

    public void handleAlert(IOSUtils.AlertButtonCommand command) {
        LOGGER.info("Checking for system alert to {}...", command);
        new IOSUtils().handleSystemAlert(command, 10);
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
        if (profileName.length > 0 && !(initPage(DisneyPlusHomeIOSPageBase.class).isOpened())) {
            initPage(DisneyPlusWhoseWatchingIOSPageBase.class).clickProfile(String.valueOf(profileName[0]), true);
        }
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
        handleAlert();
        if (disneyPlusWelcomeScreenIOSPageBase.isOpened()) {
            loginToHome(account, profileName);

        } else if (!homePage.isOpened()) {
            restart();
            initialSetup();
            loginToHome(account, profileName);
        } else {
            disneyPlusWelcomeScreenIOSPageBase.clickHomeIcon();
        }
        pause(3);
    }

    public void initialSetup() {
        new GeoedgeProxyServer().setProxyHostForSelenoid();
        if ("Tablet".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            new IOSUtils().setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        }
        initialSetup(R.CONFIG.get("locale"), R.CONFIG.get("language"));
    }

    public void initialSetup(String locale, String language, String... planType) {
        disneyApiHandler.set(new DisneyContentApiChecker());
        disneyAccountApi.set(new DisneyAccountApi(getApiConfiguration(DISNEY)));

        configApi.set(new DisneyMobileConfigApi(IOS, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DISNEY, getAppVersion()));
        languageUtils.set(new DisneyLocalizationUtils(locale, language, IOS, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DISNEY));
        languageUtils.get().setDictionaries(configApi.get().getDictionaryVersions());
        languageUtils.get().setLegalDocuments();
        searchApi.set(new DisneySearchApi(APPLE, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),  disneyApiHandler.get().getPartner()));
        String accountPlan = planType.length > 0 ? planType[0] : BUNDLE_PREMIUM;
        DisneyOffer offer = disneyAccountApi.get().lookupOfferToUse(locale, accountPlan);
        disneyAccount.set(disneyAccountApi.get().createAccount(offer, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUBSCRIPTION_V1));


        LOGGER.info("Starting API threads");
        iosUtils.set(new IOSUtils());
        // Call getDriver to set platform variables
        getDriver();
        handleAlert();
        setBuildType();

        if (buildType == BuildType.IAP) {
            LOGGER.info("IAP build detected. Cancelling Disney+ subscription.");
            initPage(IOSSettingsMenuBase.class).cancelActiveEntitlement("Disney+");
            relaunch();
        }

        try {
            LOGGER.warn("Commented restart as it seems useless after version detection refactoring");
            //restart();
            DisneyPlusApplePageBase.setDictionary(languageUtils.get());
            initPage(DisneyPlusLoginIOSPageBase.class).dismissNotificationsPopUp();
            LOGGER.info("API threads started.");
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new SkipException("There was a problem with the setup: " + e.getMessage());
        }
    }

    /**
     * Returns installed app version based on the filename pulled from AppCenter when the test started.
     * Similar to getInstalledAppVersionFull() except it does not include the build number
     * @return The app version number used in config calls and other displays (ex. 1.16.0)
     */
    private synchronized String getAppVersion() {
        String appLink = R.CONFIG.get("capabilities.app");
        LOGGER.debug("capabilities.app: {}", appLink);
        
        IArtifactManager artifactProvider = ArtifactProvider.getInstance();
        appLink = artifactProvider.getDirectLink(appLink);
        
        LOGGER.debug("app: {}", appLink);
        // override capabilities.app by presign url to avoid multiply calls to appcenter
        R.CONFIG.put("capabilities.app", appLink);
        
        String regex = String.format("_%s-(.+?)-", R.CONFIG.get(Parameter.ENV.getKey()));
        
        if ("prod".equalsIgnoreCase(R.CONFIG.get(Parameter.ENV.getKey()))) {
            // Unable to detect version via regex: _Prod-(.+?)- patterm from presign url: 
            // https://appcenter-filemanagement-distrib2ede6f06e.azureedge.net/6de8d71f-8948-4bf9-a9e5-38498f8cc606/Disney%2B-Dominguez_Non-IAP_Prod_Enterprise_for_Automation-2.24.0-59964.ipa?sv=2019-02-02&sr=c&sig=4p%2FWYUDPoTrJCrziq0wIrd7KI3ocVeSlipOuaz2hyW0%3D&se=2023-09-06T05%3A06%3A04Z&sp=r
            regex = "_Prod_Enterprise_for_Automation-(.+?)-";
        }
       
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(appLink);
        
        String version = "2.24.0"; // hardcode to have api calls workable even with old version.
        if (matcher.find()) {
            version = matcher.group(1);
        } else {
            LOGGER.error("Unable to detect version via regex: {} patterm from presign url: {}", regex, appLink);
        }
        
        LOGGER.info("version: {}", version);
        
        return version;
    }

    @AfterMethod(alwaysRun = true)
    public void cleanThreads() {
        LOGGER.info("Cleaning threads");
        disneyApiHandler.remove();
        languageUtils.remove();
        disneyAccount.remove();
        disneyAccountApi.remove();
        LOGGER.info("Threads cleaned");
    }

    /**
     * Performs an app reinstall/relaunch without terminating the Appium session.
     * Also gets rid of Network Visibility alert.
     */
    public void restart() {
        iosUtils.get().appReinstall(sessionBundles.get(APP), sessionBundles.get(DISNEY));
        handleAlert();
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
        boolean isInstalled = iosUtils.get().isAppInstalled(sessionBundles.get(JarvisAppleBase.JARVIS));
        if (isInstalled) {
            if (freshInstall) {
                iosUtils.get().terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
                iosUtils.get().removeApp(sessionBundles.get(JarvisAppleBase.JARVIS));
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

    public void rotateScreen(ScreenOrientation orientation) {
        try {
            IOSDriver driver = (IOSDriver) getCastedDriver();
            driver.rotate(orientation);
        } catch (WebDriverException wde) {
            LOGGER.error("Error rotating screen. Device may already be oriented.");
        }
    }

    public void startProxyAndRestart(String country) {
        startProxyAndRestart(country, true);
    }

    public void startProxyAndRestart(String country, boolean withDataCapture) {
        if (withDataCapture) {
            initiateProxy(country);
        } else {
            LOGGER.warn("MITM Capturing set to false. No HAR data will be available!");
            initiateProxy(country, null);
        }
        proxy.get().newHar();
        IOSDriver driver = (IOSDriver) getCastedDriver();
        driver.resetApp();
        handleAlert();
    }

    public void downloadApp(String version) {
        String appCenterAppName = R.CONFIG.get("capabilities.app");
        LOGGER.info("App Download: {}", appCenterAppName);
        if(appCenterAppName.contains("for_Automation")) {
            iosUtils.get().installApp(AppCenterManager.getInstance().getDownloadUrl("Dominguez-Non-IAP-Prod-Enterprise-for-Automation", "ios", "enterprise", version));
        } else if (appCenterAppName.contains("Disney")) {
            iosUtils.get().installApp(AppCenterManager.getInstance().getDownloadUrl("Disney-Prod-Enterprise", "ios", "enterprise", version));
        }
    }

    public ApiConfiguration getApiConfiguration(String partner) {
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                .platform(APPLE)
                .environment(DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).toLowerCase())
                .partner(partner)
                .useMultiverse(USE_MULTIVERSE)
                .multiverseAccountsUrl(R.CONFIG.get("multiverseAccountsUrl"))
                .build();
        return apiConfiguration;
    }
    protected boolean useMultiverse() {
        return R.CONFIG.getBoolean("useMultiverse");
    }
    public DisneyAccountApi getAccountApi() {
        if (disneyAccountApi.get() == null) {
            ApiConfiguration apiConfiguration = ApiConfiguration.builder().platform(APPLE).environment(DisneyParameters.getEnv())
                    .partner(PARTNER).useMultiverse(useMultiverse()).build();
            disneyAccountApi.set(new DisneyAccountApi(apiConfiguration));
        }
        return disneyAccountApi.get();
    }
    public void addHoraValidationSku(DisneyAccount accountToEntitle){
        if (horaEnabled()) {
            try {
                getAccountApi().entitleAccount(accountToEntitle, DisneySkuParameters.DISNEY_HORA_VALIDATION, "V1");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void checkAssertions(SoftAssert softAssert, String accountId, JSONArray checkList) {
        if (horaEnabled()) {
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
        return disneyAccountApi.get().createAccount(request);
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
