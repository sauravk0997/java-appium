package com.disney.qa.tests.disney.apple.ios;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.disney.jarvisutils.pages.apple.*;
import com.disney.qa.api.explore.request.ExploreSearchRequest;
import com.disney.qa.api.explore.response.*;
import com.disney.qa.api.explore.response.Set;
import com.disney.qa.api.pojos.*;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.household.pojos.*;
import com.disney.qa.api.household.response.*;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.gmail.exceptions.GMailUtilsException;
import com.disney.qa.hora.validationservices.HoraValidator;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.carina.utils.config.Configuration;
import com.zebrunner.carina.webdriver.config.WebDriverConfiguration;
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

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase.Brand;
import com.disney.qa.tests.disney.apple.DisneyAppleBaseTest;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM;
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
    public static final String KIDS_PROFILE = "KIDS";
    public static final String JUNIOR_PROFILE = "JUNIOR";
    public static final String SECONDARY_PROFILE = "Secondary";
    public static final String TERTIARY_PROFILE = "Tertiary";
    public static final String EXTRA_MEMBER_PROFILE = "EM";
    public static final String KIDS_DOB =
            Person.MINOR.getYear() + "-" + Person.MINOR.getMonth().getNum() + "-" + Person.MINOR.getDay(true);
    public static final String U18_DOB =
            Person.U18.getYear() + "-" + Person.U18.getMonth().getNum() + "-" + Person.U18.getDay(true);
    public static final String ADULT_DOB =
            Person.ADULT.getYear() + "-" + Person.ADULT.getMonth().getNum() + "-" + Person.ADULT.getDay(true);
    public static final String P13_DOB =
            Person.PERSON13.getYear() + "-" + Person.PERSON13.getMonth().getNum() + "-" + Person.PERSON13.getDay(true);
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
    public static final String DEEPLINKURL = "disneyplus://www.disneyplus.com/browse/";
    public static final String JARVIS_PLAYBACK = "Playback";
    public static final String JARVIS_OFFLINE_EXPIRED_LICENSE_OVERRIDE = "expiredAllowRenewal";
    public static final String ESPN_PLUS = "ESPN+";
    public static final String AUDIO_VIDEO_BADGE = "Audio_Video_Badge";
    public static final String RATING = "Rating";
    public static final String RELEASE_YEAR_DETAILS = "Release_Year";
    public static final String CONTENT_DESCRIPTION = "Content_Description";
    public static final String CONTENT_PROMO_TITLE = "Content_Promo_Title";
    public static final String CONTENT_TITLE = "Content_Title";

    //Common error messages
    public static final String DOWNLOADS_PAGE_NOT_DISPLAYED = "Downloads Page is not displayed";
    public static final String ADD_PROFILE_PAGE_NOT_DISPLAYED = "Add Profile Page is not displayed";
    public static final String CHOOSE_AVATAR_PAGE_NOT_DISPLAYED = "Choose Avatar Page is not displayed";
    public static final String ESPN_PAGE_DID_NOT_OPEN = "ESPN page did not open";
    public static final String LIVE_MODAL_NOT_DISPLAYED = "Live Modal is not displayed";
    public static final String EDIT_PROFILE_PAGE_NOT_DISPLAYED = "Edit Profile Page is not displayed";

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
        AGE_17(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 17)),
        OLDERTHAN125(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 130)),
        OLDERTHAN200(DateHelper.Month.NOVEMBER, "5", Integer.toString(LocalDate.now().getYear() - 205)),
        PERSON13(DateHelper.Month.valueOf(LocalDate.now().getMonth().name()),
                String.valueOf(LocalDate.now().getDayOfMonth()),
                String.valueOf(LocalDate.now().getYear() - 13));

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
     * Calculates person's age based on month, day, year provided from Person enum
     *
     * @param personMonth - Person's birth month
     * @param person      - Person for day and year
     */
    public int calculateAge(DateHelper.Month personMonth, Person person) {
        int month = Integer.parseInt(personMonth.getNum());
        int day = Integer.parseInt(person.day);
        int year = Integer.parseInt(person.year);
        LOGGER.info("Calculated age returned: {}", Period.between(LocalDate.of(year, month, day), LocalDate.now()).getYears());
        return Period.between(LocalDate.of(year, month, day), LocalDate.now()).getYears();
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

    public void loginToHome(UnifiedAccount account, String... profileName) {
        handleAlert();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickLogInButton();
        login(account);
        handleSystemAlert(AlertButtonCommand.DISMISS, 1);
        if (profileName.length > 0 && !(initPage(DisneyPlusHomeIOSPageBase.class).isOpened())) {
            initPage(DisneyPlusWhoseWatchingIOSPageBase.class).clickProfile(String.valueOf(profileName[0]), true);
        }
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

    public void setAppToHomeScreen(UnifiedAccount account, String... profileName) {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        if (welcomePage.isOpened()) {
            LOGGER.info("On Welcome page, begin login");
            loginToHome(account, profileName);
        } else {
            LOGGER.info("Welcome page is not opened, restarting the app");
            restart();
            handleAlert();
            loginToHome(account, profileName);
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
        handleSystemAlert(command, 10);
    }

    public void initialSetup() {
        getDriver();
        setBuildType();

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

    protected void installJarvis() {
        String testFairyJarvisUrl = R.CONFIG.get("test_fairy_jarvis_url");
        if (testFairyJarvisUrl.isEmpty()) {
            throw new RuntimeException("TEST FAIRY JARVIS CONFIG testFairyJarvisUrl IS MISSING!!!");
        }
        installApp(testFairyJarvisUrl);
    }

    public JarvisAppleBase getJarvisPageFactory() {
        switch (currentDevice.get().getDeviceType()) {
            case APPLE_TV:
                return new JarvisAppleTV(getDriver());
            case IOS_PHONE:
                return new JarvisHandset(getDriver());
            case IOS_TABLET:
                return new JarvisTablet(getDriver());
            default:
                throw new IllegalArgumentException(String.format("Invalid device type %s. No factory is available", currentDevice.get().getDeviceType()));
        }
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

    //Explore API methods
    public ExploreSearchRequest getExploreSearchRequest(String brand) {
        switch (brand.toUpperCase()) {
            case "HULU":
                return getHuluExploreSearchRequest();
            case "DISNEY":
            default:
                return getDisneyExploreSearchRequest();
        }
    }

    public ExploreContent getSeriesApi(String entityID, Brand brand) {
        try {
            return getExploreApi().getSeries(getExploreSearchRequest(brand.toString())
                    .setEntityId(entityID)
                    .setUnifiedAccount(getUnifiedAccount())
                    .setProfileId(getUnifiedAccount().getProfileId()));
        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ExploreContent getMovieApi(String entityID, Brand brand) {
        try {
            return getExploreApi().getMovie(getExploreSearchRequest(brand.toString())
                    .setEntityId(entityID)
                    .setUnifiedAccount(getUnifiedAccount())
                    .setProfileId(getUnifiedAccount().getProfileId()));
        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Container> getHuluAPIPage(String pageID) throws URISyntaxException, JsonProcessingException {
        return getExploreApi().getPage(getHuluExploreSearchRequest()
                        .setEntityId(pageID)
                        .setUnifiedAccount(getUnifiedAccount())
                        .setProfileId(getUnifiedAccount().getProfileId()))
                .getData()
                .getPage()
                .getContainers();
    }

    public Visuals getExploreAPIPageVisuals(String entityID) {
        ExplorePageResponse pageResponse;
        try {
            pageResponse = getExploreApi().getPage(getDisneyExploreSearchRequest()
                    .setEntityId(entityID)
                    .setUnifiedAccount(getUnifiedAccount())
                    .setProfileId(getUnifiedAccount().getProfileId()).setLimit(20));
        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException("Exception occurred..." + e);
        }
        return pageResponse.getData().getPage().getVisuals();
    }

    public ArrayList<Container> getDisneyAPIPage(String pageID, boolean... isKids) {
        UnifiedAccount account = null;
        boolean kidsMode = false;
        String profileId;
        if (isKids.length > 0 && isKids[0]) {
            kidsMode = true;
            profileId = getUnifiedAccount().getProfiles().stream()
                    .filter(profile -> profile.getAttributes().getKidsModeEnabled())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No KIDS profile found for this account"))
                    .getProfileId();
        } else {
            account = getUnifiedAccount();
            profileId = getUnifiedAccount().getProfileId();
        }
        try {
            return getExploreApi().getPage(getDisneyExploreSearchRequest()
                            .setEntityId(pageID)
                            .setUnifiedAccount(account)
                            .setKidsMode(kidsMode)
                            .setProfileId(profileId))
                    .getData()
                    .getPage()
                    .getContainers();
        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException("Exception occurred..." + e);
        }
    }

    public ArrayList<Container> getDisneyAPIPage(String pageID, String locale, String language) {
        ArrayList<Container> container;
        try {
            container = getExploreApi().getPage(getDisneyExploreSearchRequest()
                            .setEntityId(pageID)
                            .setUnifiedAccount(getUnifiedAccount())
                            .setProfileId(getUnifiedAccount().getProfileId())
                            .setCountryCode(locale)
                            .setMaturity(getMaxMaturityRating(locale))
                            .setRoamingDas(getRoamingDas(locale))
                            .setLanguage(language))
                    .getData().getPage().getContainers();
        } catch (URISyntaxException | JsonProcessingException e) {
            throw new RuntimeException("Exception occurred..." + e);
        }
        return container;
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
                    .setUnifiedAccount(getUnifiedAccount())
                    .setProfileId(getUnifiedAccount().getProfileId())
                    .setLimit(limit)).getData().getSet().getItems();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Set getExploreAPISet(String setId, int limit) {
        try {
            return getExploreApi().getSet(getDisneyExploreSearchRequest()
                            .setSetId(setId)
                            .setContentEntitlements(CONTENT_ENTITLEMENT_DISNEY)
                            .setUnifiedAccount(getUnifiedAccount())
                            .setProfileId(getUnifiedAccount().getProfileId())
                            .setLimit(limit))
                    .getData()
                    .getSet();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> getExploreAPIItemsFromSet(String setId, String locale, String language) throws URISyntaxException, JsonProcessingException {
        return getExploreApi().getSet(getDisneyExploreSearchRequest()
                        .setSetId(setId)
                        .setProfileId(getUnifiedAccount().getProfileId())
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

    public List<String> getContainerTitlesWithGivenRatingFromApi(String setID, int limit, String expectedRating) {
        List<Item> setItemsFromApi = getExploreAPIItemsFromSet(setID, limit);
        List<String> filteredTitlesFromApi = new ArrayList<>();
        setItemsFromApi.stream()
                .filter(item -> item.getVisuals().getMetastringParts().getRatingInfo().getRating().getText()
                        .equals(expectedRating))
                .forEach(item -> filteredTitlesFromApi.add(item.getVisuals().getTitle()));
        if (filteredTitlesFromApi.isEmpty()) {
            throw new NoSuchElementException("No titles found from Explore API using given rating");
        }
        return filteredTitlesFromApi;
    }

    public String getSetIdFromApi(String pageId, String containerName) {
        if (pageId == null || containerName == null) {
            throw new IllegalArgumentException("pageId or containerName parameters were null");
        }
        List<Container> pageContainers = getDisneyAPIPage(pageId);
        if (pageContainers.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("Containers for given page were not found");
        }
        for (Container container : pageContainers) {
            if (containerName.contains(container.getVisuals().getName())) {
                return container.getId();
            }
        }
        throw new IllegalArgumentException("Given container was not found on given page using Explore API");
    }

    public List<Item> getAvailableHuluTitlesForStandaloneUserFromApi() {
        List<Item> huluSeriesFromApi = getExploreAPIItemsFromSet
                (CollectionConstant.getCollectionName(CollectionConstant.Collection.ENJOY_THESE_SERIES_FROM_HULU),
                        20);
        List<Item> huluMoviesFromApi = getExploreAPIItemsFromSet
                (CollectionConstant.getCollectionName(CollectionConstant.Collection.ENJOY_THESE_MOVIES_FROM_HULU),
                        20);
        List<Item> huluContentFromApi = Stream.concat(
                        huluSeriesFromApi.stream(), huluMoviesFromApi.stream())
                .collect(Collectors.toList());
        if (huluContentFromApi.isEmpty()) {
            throw new NoSuchElementException("No available Hulu Titles found for standalone user using Explore API");
        }
        return huluContentFromApi;
    }

    public String getExploreAPIResponseOrErrorMsg(ExploreSearchRequest exploreSearchRequest) {
        ExplorePageResponse explorePageResponse = null;
        try {
            explorePageResponse = getExploreApi().getPage(exploreSearchRequest);
            return explorePageResponse.getData().toString().split("message=")[1].split(", iconType=")[0];
        } catch (Exception e) {
            if (explorePageResponse == null)
                Assert.fail("Expected response not received from API");
            return explorePageResponse.getData().toString().split("description=")[1].split("\\)")[0];
        }
    }

    protected List<String> getGenreMetadataLabels(Visuals visualsResponse) {
        List<String> metadataArray = new ArrayList();
        List<String> genreList = visualsResponse.getMetastringParts().getGenres().getValues();
        //get only first two values of genre
        if (genreList.size() > 2) {
            genreList = genreList.subList(0, 2);
        }
        genreList.forEach(genre -> metadataArray.add(genre));
        return metadataArray;
    }

    //Jarvis Methods
    public void setOverrideValue(String newValue) {
        DisneyPlusApplePageBase applePageBase = initPage(DisneyPlusApplePageBase.class);
        applePageBase.removeDomainIdentifier();
        applePageBase.getClearTextBtn().clickIfPresent(FIVE_SEC_TIMEOUT);
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

    public void enableJarvisSoftUpdate() {
        DisneyPlusApplePageBase applePageBase = initPage(DisneyPlusApplePageBase.class);
        JarvisAppleBase jarvis = getJarvisPageFactory();

        String updateNudgeConfig = "updateNudgeConfig";
        String updateAlertIsEnabled = "updateAlertIsEnabled";

        launchJarvis(true);
        jarvis.scrollToItem(JARVIS_APP_CONFIG).click();
        jarvis.scrollToItem(JARVIS_APP_EDIT_CONFIG).click();
        jarvis.scrollToItem(updateNudgeConfig).click();
        jarvis.scrollToItem(updateAlertIsEnabled).click();
        if (applePageBase.getStaticTextByLabelContains(JARVIS_NO_OVERRIDE_IN_USE_TEXT).isPresent(SHORT_TIMEOUT)) {
            LOGGER.info("Enabling updateNudgeConfig");
            applePageBase.clickToggleView();
        } else {
            LOGGER.info("updateNudgeConfig is already enabled");
        }

        //Relaunch Disney app
        terminateApp(sessionBundles.get(DISNEY));
        launchApp(sessionBundles.get(DISNEY));
    }

    public void jarvisEnableOfflineExpiredLicenseOverride() {
        JarvisAppleBase jarvis = getJarvisPageFactory();

        launchJarvis(true);

        //Enable Playback > Offline Expired License Override toggle
        jarvis.scrollToItem(JARVIS_PLAYBACK).click();
        jarvis.scrollToItem(JARVIS_OFFLINE_EXPIRED_LICENSE_OVERRIDE).click();

        //Relaunch Disney app
        terminateApp(sessionBundles.get(DISNEY));
        launchApp(sessionBundles.get(DISNEY));
    }

    public String convertMinutesIntoStringWithHourAndMinutes(int timeInMinutes) {
        long hours = timeInMinutes / 60;
        long minutes = timeInMinutes % 60;
        return String.format("%dh %dm", hours, minutes);
    }

    public String getFormattedDurationStringFromDurationInMs(int durationInMs) {
        // Convert to minutes using floating point for rounding
        long roundedMinutes = Math.round(durationInMs / 60000.0);

        // Derive hours and minutes using TimeUnit
        long hours = TimeUnit.MINUTES.toHours(roundedMinutes);
        long minutes = roundedMinutes - TimeUnit.HOURS.toMinutes(hours);

        StringBuilder result = new StringBuilder();
        if (hours > 0) {
            result.append(hours).append("h");
        }
        if (minutes > 0) {
            if (result.length() > 0) result.append(" ");
            result.append(minutes).append("m");
        }
        if (result.length() == 0) {
            result.append("0m");
        }

        LOGGER.info("Formatted duration: '{}'. Using '{}' ms as input ", result, durationInMs);
        return result.toString();
    }

    public String getOTPFromApi(UnifiedAccount testAccount) {
        int emailAPILatency = 10;
        try {
            String firstOTP = getEmailApi().getDisneyOTP(testAccount.getEmail());
            pause(emailAPILatency);
            String secondOTP = getEmailApi().getDisneyOTP(testAccount.getEmail());

            if (!secondOTP.equals(firstOTP)) {
                LOGGER.info("First and second OTP doesn't match, firstOTP: {}, secondOTP: {}", firstOTP, secondOTP);
                return secondOTP;
            } else {
                LOGGER.info("First and second OTP match, returning first OTP: {}", firstOTP);
                return firstOTP;
            }
        } catch (GMailUtilsException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public void handleGenericPopup(int timeout, int maxAttempts) {
        pause(timeout);
        handleSystemAlert(AlertButtonCommand.DISMISS, maxAttempts);
    }

    public void handleOneTrustPopUp() {
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        if (isAlertPresent()) {
            handleGenericPopup(FIVE_SEC_TIMEOUT, 1);
        }
    }

    public Map<String, Object> getContentMetadataFromAPI(Visuals visualsResponse) {
        Map<String, Object> exploreAPIMetadata = new HashMap<>();

        exploreAPIMetadata.put(CONTENT_TITLE, visualsResponse.getTitle());
        exploreAPIMetadata.put(CONTENT_DESCRIPTION, visualsResponse.getDescription().getBrief());
        if (visualsResponse.getPromoLabel() != null) {
            exploreAPIMetadata.put(CONTENT_PROMO_TITLE, visualsResponse.getPromoLabel().getHeader());
        }

        //Audio visual badge
        if (visualsResponse.getMetastringParts().getAudioVisual() != null) {
            List<String> audioVideoApiBadge = new ArrayList<>();
            visualsResponse.getMetastringParts().getAudioVisual().getFlags()
                    .forEach(flag -> audioVideoApiBadge.add(flag.getTts()));
            exploreAPIMetadata.put(AUDIO_VIDEO_BADGE, audioVideoApiBadge);
        }

        //Rating
        if (visualsResponse.getMetastringParts().getRatingInfo() != null) {
            exploreAPIMetadata.put(RATING, visualsResponse.getMetastringParts().getRatingInfo().getRating().getText());
        }

        //Release Year
        if (visualsResponse.getMetastringParts().getReleaseYearRange() != null) {
            exploreAPIMetadata.put(RELEASE_YEAR_DETAILS,
                    visualsResponse.getMetastringParts().getReleaseYearRange().getStartYear());
        }
        return exploreAPIMetadata;
    }

    public UnifiedAccount setHouseholdExperience(ExperienceId experienceId, Boolean isOTPAccount) {
        UnifiedAccount account = isOTPAccount ?
                getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                        getLocalizationUtils().getLocale(),
                        getLocalizationUtils().getUserLanguage())) :
                getUnifiedAccount();

        try {
            LOGGER.info("Attempting to set force detect + account block {} for account {}", experienceId.toString(),
                    account.getAccountId());
            getHouseholdApi().createHousehold(account.getAccountId());
            // Update household values based on household request
            getHouseholdApi().updateHousehold(account.getAccountId(), getHouseholdRequest());
            getHouseholdApi().setOverrideStatusForceDetect(account.getAccountId());
        } catch (IOException | URISyntaxException | IllegalAccessException e) {
            throw new RuntimeException("Unable to create/ update the household for the account with error: " + e);
        }

        try {
            getHouseholdApi().createHouseholdExperienceOverrides(
                    account.getAccountId(),
                    account.getDeviceId(),
                    experienceId);
            //Time needed to propagate the changes to account
            pause(8);
            ExperienceResponse experienceResponse =
                    getHouseholdApi().getHouseholdExperienceOverrides(account.getAccountId());
            Assert.assertEquals(experienceResponse.eventData.responseOverrides.experienceId, experienceId.toString(),
                    "Failed to override the household experience for the account");
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to override the household experience for the account" + e);
        }

        return account;
    }
}
