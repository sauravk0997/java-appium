package com.disney.qa.tests.disney.apple.ios;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.*;

import com.disney.qa.api.explore.request.ExploreSearchRequest;
import com.disney.qa.api.explore.response.*;
import com.disney.qa.api.pojos.*;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.hora.validationservices.HoraValidator;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.utils.exception.InvalidConfigurationException;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
import io.appium.java_client.remote.options.SupportsAppOption;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.common.utils.ios_settings.IOSSettingsMenuBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase.Brand;
import com.disney.qa.tests.disney.apple.DisneyAppleBaseTest;
import com.zebrunner.carina.appcenter.AppCenterManager;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;

import static com.disney.qa.common.constant.IConstantHelper.CONTENT_ENTITLEMENT_DISNEY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.common.constant.RatingConstant.getMaxMaturityRating;
import static com.disney.qa.common.constant.RatingConstant.getRoamingDas;

/**
 * Base class for ios
 */
@SuppressWarnings("squid:S2187")
public class DisneyBaseTest extends DisneyAppleBaseTest {

    private static final ThreadLocal<ITestContext> localContext = new ThreadLocal<>();
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String DEFAULT_PROFILE = "Test";
    public static final String DISNEY_URL = "disneyplus.com";
    public static final String KIDS_PROFILE = "KIDS";
    public static final String JUNIOR_PROFILE = "JUNIOR";
    public static final String SECONDARY_PROFILE = "Secondary";
    public static final String KIDS_DOB = Person.MINOR.getYear() + "-" + Person.MINOR.getMonth().getNum() + "-" + Person.MINOR.getDay(true);
    public static final String ADULT_DOB = Person.ADULT.getYear() + "-" + Person.ADULT.getMonth().getNum() + "-" + Person.ADULT.getDay(true);
    public static final String PHONE = "Phone";
    public static final String TABLET = "Tablet";
    public static final String JUNIOR_MODE_HELP_CENTER = "Junior Mode on Disney+";
    public static final String DISNEY_PLUS = "Disney Plus";
    public static final String DISNEY_PLUS_HELP_CENTER = "Disney+ Help Center";
    public static final String RESTRICTED = "Restricted";
    public static final String RATING_MATURE = "TV-MA";
    public static final String RATING_R = "R";
    public static final String RATING_TV14 = "TV-14";
    public static final String ENGLISH_LANG = "en";
    public static final String MULTIVERSE_STAGING_ENDPOINT = "https://multiverse-alice-client-staging.qateam.bamgrid.com";
    private static final String S3_BASE_PATH = "bamtech-qa-alice/disney/recognition/alice/";
    public static final String INVALID_PASSWORD = "Invalid#1234";
    public static final String PROFILE_PIN = "1234";
    public static final String PLAYER = "player";
    public static final String PICTURE_IN_PICTURE = "pictureInPicture";
    public static final String PARENTAL_CONTROLS_CONFIG = "parentalControlsConfig";
    public static final String FORCE_UPDATE_CONFIG = "forceUpdateConfig";
    public static final String SHOW_UPDATE_APP_ALERT = "showUpdateAppAlert";
    public static final String R21_PAUSE_TIMEOUT = "r21PauseTimeoutSeconds";
    public static final String DISABLED = "disabled";
    public static final String HULU = "Hulu";

    @BeforeMethod(alwaysRun = true, onlyForGroups = TestGroup.PRE_CONFIGURATION)
    public void beforeAnyAppActions(ITestContext context) {
        if (R.CONFIG.get(DEVICE_TYPE).equals(TABLET)) {
            localContext.set(context);
        }
        getDriver();
        WebDriverConfiguration.getZebrunnerCapability("deviceType").ifPresent(type -> {
            if (StringUtils.equalsIgnoreCase(type, "Tablet")) {
                setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
            }
        });
        setBuildType();
        if (buildType == BuildType.IAP) {
            LOGGER.info("IAP build detected. Cancelling Disney+ subscription.");
        }

        try {
            initPage(DisneyPlusLoginIOSPageBase.class).dismissNotificationsPopUp();
        } catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new SkipException("There was a problem with the setup: " + e.getMessage());
        }
        handleAlert();
    }

    @Getter
    public enum Person {
        ADULT(DateHelper.Month.NOVEMBER, "5", "1955"),
        MINOR(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 4)),
        U13(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 12)),
        U18(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 16)),
        OLDERTHAN125(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 130)),
        OLDERTHAN200(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 205));

        private final DateHelper.Month month;
        private final String day;
        private final String year;

        Person(DateHelper.Month month, String day, String year) {
            this.month = month;
            this.day = day;
            this.year = year;
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
        handleSystemAlert(AlertButtonCommand.DISMISS, 1);
        if (profileName.length > 0 && !(initPage(DisneyPlusHomeIOSPageBase.class).isOpened())) {
            initPage(DisneyPlusWhoseWatchingIOSPageBase.class).clickProfile(String.valueOf(profileName[0]), true);
        }
    }

    public void loginForHuluHub(String Email) {
        initialSetup();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();
        initPage(DisneyPlusLoginIOSPageBase.class).submitEmail(Email);
        initPage(DisneyPlusPasswordIOSPageBase.class).submitPasswordForLogin("Test123!");
        pause(5);
        handleSystemAlert(AlertButtonCommand.DISMISS, 1);
        Assert.assertTrue(initPage(DisneyPlusHomeIOSPageBase.class).isOpened(),
                "Couldn't login into the Hulu-sub account");
    }

    /**
     * Logs into the app by entering the provided account's credentials and username
     *
     * @param entitledUser - Unified Account generated for the test run
     */
    public void loginToHome(UnifiedAccount entitledUser) {
        handleAlert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();
        login(entitledUser);
        handleSystemAlert(AlertButtonCommand.DISMISS, 1);
        Assert.assertTrue(initPage(DisneyPlusHomeIOSPageBase.class).isOpened(),
                "Home Page did not open after login");
    }

    /**
     * Logs into the app by entering the provided account's credentials and username
     *
     * @param account - UnifiedAccount generated for the test run
     */
    public void login(UnifiedAccount account) {
        initPage(DisneyPlusLoginIOSPageBase.class).submitEmail(account.getEmail());
        initPage(DisneyPlusPasswordIOSPageBase.class).submitPasswordForLogin(account.getUserPass());
    }

    public DisneyAccount createAccountFor(String country, String language) {
        DisneyOffer offer = getAccountApi().lookupOfferToUse(country, BUNDLE_PREMIUM);
        return getAccountApi().createAccount(offer, country, language, SUBSCRIPTION_V2);
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
        if (disneyPlusWelcomeScreenIOSPageBase.isOpened()) {
            loginToHome(account, profileName);

        } else if (!homePage.isOpened()) {
            restart();
            handleAlert();
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

    public void initialSetup() {
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

    public void removeJarvis() {
        terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
        removeApp(sessionBundles.get(JarvisAppleBase.JARVIS));
    }

    public void rotateScreen(ScreenOrientation orientation) {
        try {
            rotate(orientation);
        } catch (WebDriverException wde) {
            LOGGER.error("Error rotating screen. Device may already be oriented.");
        }
    }

    public void downloadApp(String version) {
        String appCenterAppName = WebDriverConfiguration.getAppiumCapability(SupportsAppOption.APP_OPTION)
                .orElseThrow(() -> new InvalidConfigurationException("Add 'app' capability to the configuration."));
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
        String appCenterAppName = WebDriverConfiguration.getAppiumCapability(SupportsAppOption.APP_OPTION)
                .orElseThrow(() -> new InvalidConfigurationException("Add 'app' capability to the configuration."));
        String appVersion = Configuration.getRequired(DisneyConfiguration.Parameter.APP_VERSION);
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
        if (Configuration.getRequired(DisneyConfiguration.Parameter.ENABLE_HORA_VALIDATION, Boolean.class)) {
            try {
                getSubscriptionApi().addEntitlementBySku(accountToEntitle, DisneySkuParameters.DISNEY_HORA_VALIDATION, "V2");
            } catch (URISyntaxException | MalformedURLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void checkAssertions(SoftAssert softAssert, String accountId, JSONArray checkList) {
        if (Configuration.getRequired(DisneyConfiguration.Parameter.ENABLE_HORA_VALIDATION, Boolean.class)) {
            HoraValidator hv = new HoraValidator(accountId);
            hv.assertValidation(softAssert);
            hv.checkListForPQOE(softAssert, checkList);
        }
    }

    public DisneyAccount createAccountWithSku(DisneySkuParameters sku) {
        CreateDisneyAccountRequest request = new CreateDisneyAccountRequest();
        request.addSku(sku);
        return getAccountApi().createAccount(request);
    }

    public DisneyAccount createAccountWithSku(DisneySkuParameters sku, String country, String language, boolean... ageVerified) {
        CreateDisneyAccountRequest request = new CreateDisneyAccountRequest();
        request.addSku(sku);
        request.setCountry(country);
        request.setLanguage(language);
        if (ageVerified.length > 0) {
            request.setAgeVerified(ageVerified[0]);
        }
        return getAccountApi().createAccount(request);
    }


    public void launchOrInstallJarvis() {
        boolean isInstalled = isAppInstalled(sessionBundles.get(JarvisAppleBase.JARVIS));
        if (isInstalled) {
            launchJarvis(false);

        } else {
            launchJarvis(true);
        }
    }

    public String buildS3BucketPath(String title, String feature) {
        String deviceName = R.CONFIG.get("capabilities.deviceName").toLowerCase().replace(' ', '_');
        if ("Tablet".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            return String.format(
                    S3_BASE_PATH + "apple-tablet/" + deviceName + "/" + feature + "/%s", title);
        } else {
            return String.format(
                    S3_BASE_PATH + "apple-handset/" + deviceName + "/" + feature + "/%s", title);
        }
    }

    public ExploreSearchRequest getExploreSearchRequest(String brand) {
        switch (brand.toUpperCase()){
            case "HULU":
                return getHuluExploreSearchRequest();
            case "DISNEY":
            default:
                return getDisneyExploreSearchRequest();
        }
    }

    //Explore API methods
    public ExploreContent getSeriesApi(String entityID, Brand brand) {
        try {
            return getExploreApi().getSeries(getExploreSearchRequest(brand.toString())
                    .setEntityId(entityID)
                    .setProfileId(getAccount().getProfileId()));
        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ExploreContent getMovieApi(String entityID, Brand brand) {
        try {
            return getExploreApi().getMovie(getExploreSearchRequest(brand.toString())
                    .setEntityId(entityID)
                    .setProfileId(getAccount().getProfileId()));
        } catch (URISyntaxException | JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Container> getDisneyAPIPage(String pageID, boolean... isKids) {
        try {
            return getExploreApi().getPage(getDisneyExploreSearchRequest()
                            .setEntityId(pageID)
                            .setKidsMode(isKids.length > 0 ? isKids[0] : false)
                            .setProfileId(getAccount().getProfileId()))
                    .getData()
                    .getPage()
                    .getContainers();
        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException("Exception occurred..." + e);
        }
    }

    public ArrayList<Container> getHuluAPIPage(String pageID) throws URISyntaxException, JsonProcessingException {
        return getExploreApi().getPage(getHuluExploreSearchRequest()
                .setEntityId(pageID)
                .setProfileId(getAccount().getProfileId()))
                .getData()
                .getPage()
                .getContainers();
    }

    public Visuals getExploreAPIPageVisuals(String entityID) {
        ExplorePageResponse pageResponse;
        try {
             pageResponse = getExploreApi().getPage(getDisneyExploreSearchRequest()
                    .setEntityId(entityID)
                    .setProfileId(getAccount().getProfileId()).setLimit(30));
        }
        catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException("Exception occurred..." + e);
        }
        return pageResponse.getData().getPage().getVisuals();
    }

    public ArrayList<Container> getDisneyAPIPage(String pageID, String locale, String language) {
        ArrayList<Container> container;
        try{
            container = getExploreApi().getPage(getDisneyExploreSearchRequest()
                    .setEntityId(pageID)
                    .setProfileId(getAccount().getProfileId())
                    .setCountryCode(locale)
                    .setMaturity(getMaxMaturityRating(locale))
                    .setRoamingDas(getRoamingDas(locale))
                    .setLanguage(language)).getData().getPage().getContainers();
        }
        catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException("Exception occurred..." + e);
        }
        return container;
    }

    public String getFirstContentIDForSet(String setID) {
        ExploreSetResponse setResponse;
        String firstContentID = null;
        try {
            setResponse = getExploreApi().getSet(getDisneyExploreSearchRequest()
                    .setSetId(setID)
                    .setProfileId(getAccount().getProfileId()));
            firstContentID = setResponse.getData().getSet().getItems().get(0).getActions().get(0).getDeeplinkId();
        } catch (IndexOutOfBoundsException | URISyntaxException e) {
            Assert.fail(e.getMessage());
        }
        return firstContentID;
    }

    public String getApiSeriesRatingDetails(ExploreContent apiContent) {
        String ratingDetail = null;
        try {
            ratingDetail = apiContent.getContainers().get(2).getVisuals().getRatingInfo().getRating().getText();
        } catch (IndexOutOfBoundsException e) {
            Assert.fail(e.getMessage());
        }
        return ratingDetail;
    }

    public String getApiMovieRatingDetails(ExploreContent apiContent) {
        String ratingDetail = null;
        try {
            ratingDetail = apiContent.getContainers().get(3).getVisuals().getRatingInfo().getRating().getText();
        } catch (IndexOutOfBoundsException e) {
            Assert.fail(e.getMessage());
        }
        return ratingDetail;
    }

    public String getApiContentReleasedYearDetails(ExploreContent apiContent) {
        return apiContent.getReleaseYearRange().getStartYear();
    }

    public List<Item> getExploreAPIItemsFromSet(String setId, int limit) {
        try {
            return getExploreApi().getSet(getDisneyExploreSearchRequest()
                    .setSetId(setId)
                    .setContentEntitlements(CONTENT_ENTITLEMENT_DISNEY)
                    .setDisneyAccount(getAccount())
                    .setProfileId(getAccount().getProfileId())
                    .setLimit(limit)).getData().getSet().getItems();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> getExploreAPIItemsFromSet(String setId, String locale, String language) throws URISyntaxException, JsonProcessingException {
        return getExploreApi().getSet(getDisneyExploreSearchRequest()
                        .setSetId(setId)
                        .setProfileId(getAccount().getProfileId())
                        .setCountryCode(locale)
                        .setMaturity(getMaxMaturityRating(locale))
                        .setRoamingDas(getRoamingDas(locale))
                        .setLanguage(language))
                .getData().getSet().getItems();
    }

    public List<String> getContainerTitlesFromApi(String setID, int limit) {
        List<Item> setItemsFromApi = getExploreAPIItemsFromSet(setID, limit);
        List<String> titlesFromApi = new ArrayList<>();
        setItemsFromApi.forEach(item ->
                titlesFromApi.add(item.getVisuals().getTitle()));
        return titlesFromApi;
    }

    public void setOverrideValue(String newValue) {
        DisneyPlusApplePageBase applePageBase = initPage(DisneyPlusApplePageBase.class);
        applePageBase.removeDomainIdentifier();
        applePageBase.getClearTextBtn().click();
        applePageBase.saveDomainIdentifier(newValue);
    }

    public void terminateJarvisInstallDisney() {
        LOGGER.info("Terminating Jarvis app..");
        terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
        LOGGER.info("Restart Disney app..");
        restart();
        LOGGER.info("Click allow to track your activity..");
        handleAlert();
    }

    public void setPictureInPictureConfig(String value) {
        JarvisAppleBase jarvis = getJarvisPageFactory();
        launchJarvis(true);
        jarvis.openAppConfigOverrides();
        jarvis.openOverrideSection(PLAYER);
        jarvis.openOverrideSection(PICTURE_IN_PICTURE);
        setOverrideValue(value);
        terminateJarvisInstallDisney();
    }

    public void setR21PauseTimeOut(int newPauseTime) {
        JarvisAppleBase jarvis = getJarvisPageFactory();
        launchJarvis(true);
        jarvis.openAppConfigOverrides();
        jarvis.openOverrideSection(PARENTAL_CONTROLS_CONFIG);
        try {
            jarvis.openOverrideSection(R21_PAUSE_TIMEOUT);
        } catch (NoSuchElementException e) {
            throw new SkipException("Failed to update R21 Pause TimeOut: {}", e);
        }
        setOverrideValue(String.valueOf(newPauseTime));
        terminateJarvisInstallDisney();
    }

    public void enableHardForceUpdateInJarvis() {
        DisneyPlusApplePageBase applePageBase = initPage(DisneyPlusApplePageBase.class);
        JarvisAppleBase jarvis = getJarvisPageFactory();
        launchJarvis(true);
        jarvis.openAppConfigOverrides();
        jarvis.openOverrideSection(FORCE_UPDATE_CONFIG);
        jarvis.openOverrideSection(SHOW_UPDATE_APP_ALERT);
        if (applePageBase.getStaticTextByLabelContains(FALSE).isPresent(SHORT_TIMEOUT)) {
            LOGGER.info("Enabling Hard Force Update");
            applePageBase.clickToggleView();
        }
        LOGGER.info("Terminating Jarvis app..");
        terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
        terminateApp(sessionBundles.get(DISNEY));
        relaunch();
    }

    public String convertMinutesIntoStringWithHourAndMinutes(int timeInMinutes) {
        long hours = timeInMinutes / 60;
        long minutes = timeInMinutes % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    public String getOTPFromApi(Date startTime, DisneyAccount testAccount) {
        int emailAPILatency = 10;
        String firstOTP = getEmailApi().getDisneyOTP(testAccount.getEmail(), startTime);
        pause(emailAPILatency);
        String secondOTP = getEmailApi().getDisneyOTP(testAccount.getEmail(), startTime);

        if (!secondOTP.equals(firstOTP)) {
            LOGGER.info("First and second OTP doesn't match, firstOTP: {}, secondOTP: {}", firstOTP, secondOTP);
            return secondOTP;
        } else {
            LOGGER.info("First and second OTP match, returning first OTP: {}", firstOTP);
            return firstOTP;
        }
    }

    public void jarvisDisableOneTrustBanner() {
        DisneyPlusApplePageBase applePageBase = initPage(DisneyPlusApplePageBase.class);
        boolean isOneTrustEnabled = true;
        int jarvisAttempt = 1;
        removeJarvis();
        installAndLaunchJarvis();
        // Validate Jarvis Config
        while (isOneTrustEnabled && jarvisAttempt < 3)
            try {
                LOGGER.info("Attempt {} to configure Jarvis", jarvisAttempt);
                if (isJarvisOneTrustDisabled(applePageBase)) {
                    isOneTrustEnabled = false;
                    LOGGER.info("Successfully disabled One Trust");
                } else {
                    terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
                    launchJarvis(false);
                    jarvisAttempt++;
                }
            } catch (Exception e) {
                LOGGER.info("Exception occurred attempting to disable One Trust");
                terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
                launchJarvis(false);
                jarvisAttempt++;
            }

        // Relaunch Disney app
        terminateApp(sessionBundles.get(DISNEY));
        launchApp(sessionBundles.get(DISNEY));
    }

    private boolean isJarvisOneTrustDisabled(DisneyPlusApplePageBase applePageBase) {
        applePageBase.scrollToItem(JARVIS_APP_CONFIG).click();
        applePageBase.scrollToItem(JARVIS_APP_EDIT_CONFIG).click();
        applePageBase.scrollToItem(JARVIS_APP_PLATFORM_CONFIG).click();
        applePageBase.scrollToItem(JARVIS_APP_ONE_TRUST_CONFIG).click();
        applePageBase.scrollToItem(JARVIS_APP_IS_ENABLED).click();
        if (applePageBase.getStaticTextByLabelContains(JARVIS_NO_OVERRIDE_IN_USE).isPresent(SHORT_TIMEOUT)) {
            LOGGER.info("oneTrustConfig is not enabled");
            return true;
        } else {
            LOGGER.info("Disabling oneTrustConfig");
            applePageBase.clickToggleView();
            return applePageBase.getStaticTextByLabelContains(JARVIS_NO_OVERRIDE_IN_USE).isPresent(SHORT_TIMEOUT);
        }
    }
}
