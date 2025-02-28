package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.client.responses.profile.Profile;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;
import static com.disney.qa.api.disney.DisneyEntityIds.IMAX_ENHANCED_SET;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG_13;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_PG;

public class DisneyPlusDetailsTest extends DisneyBaseTest {

    private static final String THE_LION_KINGS_TIMON_AND_PUUMBA = "The Lion King Timon Pumbaa";
    private static final String THE_ARISTOCATS = "The aristocats";
    private static final String TV_Y7 = "TV-Y7";
    private static final String SPIDERMAN_THREE = "SpiderMan 3";
    private static final double PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT = 40;
    private static final String SHOP_TAB_SERIES = "Agatha All Along";
    private static final String THE_BRAVEST_KNIGHT = "The Bravest Knight";
    private static final String BLUEY = "Bluey";
    private static final String AVAILABLE_WITH_HULU = "Available with Hulu Subscription";
    private static final String UNLOCK_HULU_ON_DISNEY = "Unlock Hulu on Disney+";
    private static final String AVAILABLE_WITH_ESPN_SUBSCRIPTION = "Available with ESPN+ Subscription";

    @DataProvider(name = "disneyPlanTypes")
    public Object[][] disneyWebPlanTypes() {
        return new Object[][]{{"Disney+ With Ads, Hulu with Ads, and ESPN+"},
                {"Disney+, Hulu No Ads, and ESPN+"}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71130"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyIMAXEnhancedBadges() {
        String filterValue = "IMAX Enhanced";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.clickMoviesTab();
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            searchPage.clickContentPageFilterDropDown();
            swipePageTillElementPresent(searchPage.getStaticTextByLabel(filterValue), 2, null,
                    Direction.DOWN, 100);
            searchPage.getStaticTextByLabel(filterValue).click();
        } else {
            searchPage.getTypeButtonByLabel(filterValue).click();
        }
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.isImaxEnhancedPresentInMediaFeaturesRow(), "IMAX Enhanced was not found in media features row");

        detailsPage.clickDetailsTab();
        scrollDown();
        sa.assertTrue(detailsPage.areFormatsDisplayed(), "Formats in details tab not found");
        sa.assertTrue(detailsPage.isImaxEnhancedPresentsInFormats(), "IMAX Enhanced was not found in details tab formats");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68648"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyNegativeStereotypeAdvisoryExpansion() {
        DisneyPlusHomeIOSPageBase home = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        //series
        home.clickSearchIcon();
        search.searchForMedia(THE_LION_KINGS_TIMON_AND_PUUMBA);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        sa.assertTrue(details.isContentDetailsPagePresent(),
                "Details tab was not found on details page");
        details.clickDetailsTab();
        sa.assertTrue(details.isNegativeStereotypeAdvisoryLabelPresent(),
                "Negative Stereotype Advisory text was not found on details page");

        //movie
        home.clickSearchIcon();
        search.clearText();
        search.searchForMedia(THE_ARISTOCATS);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        sa.assertTrue(details.isContentDetailsPagePresent(),
                "Details tab was not found on details page");
        details.clickDetailsTab();
        sa.assertTrue(details.isNegativeStereotypeAdvisoryLabelPresent(),
                "Negative Stereotype Advisory text was not found on details page");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66841"})
    @Test(description = "Maturity Rating Restriction on Detail Page", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyMaturityRatingRestrictionOnDetailPage() {
        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(TV_Y7).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang()).avatarId(RAYA).kidsModeEnabled(false).isStarOnboarded(true).build());
        Profile profile = getAccount().getProfile(TV_Y7);
        getAccountApi().editContentRatingProfileSetting(getAccount(),
                getAccountApi().getProfiles(getAccount()).get(1).getProfileId(),
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystem(),
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues().get(1));

        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(1).getProfileName());

        // Movies
        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_deeplink"));

        sa.assertFalse(detailsPage.getExtrasTab().isPresent(SHORT_TIMEOUT), "Extra tab is found.");
        sa.assertFalse(detailsPage.getSuggestedTab().isPresent(SHORT_TIMEOUT), "Suggested tab is found.");
        sa.assertFalse(detailsPage.getDetailsTab().isPresent(SHORT_TIMEOUT), "Details tab is found.");
        sa.assertFalse(detailsPage.getWatchlistButton().isPresent(SHORT_TIMEOUT), "Watchlist CTA found.");
        sa.assertFalse(detailsPage.getTrailerButton().isPresent(SHORT_TIMEOUT), "Trailer CTA found.");
        sa.assertFalse(detailsPage.getPlayButton().isPresent(SHORT_TIMEOUT), "Play CTA found.");

        sa.assertTrue(detailsPage.getRatingRestrictionDetailMessage().isPresent(), "Rating Restriction Detail Message not found");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Metadata label is displayed.");
        sa.assertTrue(detailsPage.getMediaTitle().contains("The Avengers"), "Media title not found.");

        // Series
        launchDeeplink(R.TESTDATA.get("disney_prod_dr_ks_exotic_animal_deeplink"));

        sa.assertFalse(detailsPage.getExtrasTab().isPresent(SHORT_TIMEOUT), "Extra tab is found.");
        sa.assertFalse(detailsPage.getSuggestedTab().isPresent(SHORT_TIMEOUT), "Suggested tab is found.");
        sa.assertFalse(detailsPage.getDetailsTab().isPresent(SHORT_TIMEOUT), "Details tab is found.");
        sa.assertFalse(detailsPage.getEpisodesTab().isPresent(SHORT_TIMEOUT), "Episodes tab is found.");
        sa.assertFalse(detailsPage.getWatchlistButton().isPresent(SHORT_TIMEOUT), "Watchlist CTA found.");
        sa.assertFalse(detailsPage.getTrailerButton().isPresent(SHORT_TIMEOUT), "Trailer CTA found.");
        sa.assertFalse(detailsPage.getPlayButton().isPresent(SHORT_TIMEOUT), "Play CTA found.");

        sa.assertTrue(detailsPage.getRatingRestrictionDetailMessage().isPresent(), "Rating Restriction Detail Message not found");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Metadata label is displayed.");
        sa.assertTrue(detailsPage.getMediaTitle().contains("Dr. K's Exotic Animal ER"), "Media title not found.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71128"})
    @Test(description = "Details Page - IMAX Enhanced - Versions Tab", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyIMAXEnhancedVersionTab() {
        String filterValue = "IMAX Enhanced";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        searchPage.clickMoviesTab();
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            searchPage.clickContentPageFilterDropDown();
            swipe(searchPage.getStaticTextByLabel(filterValue));
            searchPage.getStaticTextByLabel(filterValue).click();
        } else {
            searchPage.getTypeButtonByLabel(filterValue).click();
        }
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        String title = results.get(0).getText();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page was not opened");
        detailsPage.clickVersionsTab();
        sa.assertTrue(detailsPage.getVersionTab().isPresent(), "Versions was not found");
        sa.assertTrue(detailsPage.isIMAXEnhancedTitlePresentInVersionTab(), "IMAX Enhanced Title was not found");
        sa.assertTrue(detailsPage.isIMAXEnhancedThumbnailPresentInVersionTab(), "IMAX Enhanced Thumbnail was not found");
        sa.assertTrue(detailsPage.isIMAXEnhancedDescriptionPresentInVersionTab(), "IMAX Enhanced Description was not found");

        //get Video duration from API and verify that its present at last in IMAX Enhance Header
        String entityID = getFirstContentIDForSet(IMAX_ENHANCED_SET.getEntityId());
        if (entityID != null) {
            ExploreContent exploreMovieContent = getMovieApi(entityID, DisneyPlusBrandIOSPageBase.Brand.DISNEY);
            int duration = exploreMovieContent.getDurationMs();
            LOGGER.info("Duration returned from api: {}", duration);
            String durationTime = detailsPage.getHourMinFormatForDuration(duration);
            sa.assertTrue(detailsPage.getMovieNameAndDurationFromIMAXEnhancedHeader().equals(title + " " + durationTime), "Content name and duration was not found in IMAX Enhanced Header");
            sa.assertTrue(detailsPage.getMovieNameAndDurationFromIMAXEnhancedHeader().endsWith(durationTime), "Duration details not found at the end of IMAX Enhanced Header");
        } else {
            sa.assertTrue(false, "Entity ID for IMAX Enhanced set came back empty from api, check the IMAX_ENHANCED_SET_ID value");
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72725"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyShopPromoLabelInFeatureAreaOfDetailPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        //Verify Shop Promo for Series
        validateShopPromoLabelHeaderAndSubHeader(sa, SHOP_TAB_SERIES);

        //Verify Shop Promo for Movie
        detailsPage.getBackArrow().click();
        validateShopPromoLabelHeaderAndSubHeader(sa, SPIDERMAN_THREE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72730"})
    @Test(dataProvider = "disneyPlanTypes", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyShopTabInDetailsPage(String planType) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(getAccountApi().createAccount(getAccountApi().lookupOfferToUse(getCountry(), planType),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");

        //verify Shop Tab button is present and after clicking it focused or not
        validateShopTabButton(sa, SPIDERMAN_THREE);

        //Verify Shop tab button for series
        detailsPage.getBackArrow().click();
        validateShopTabButton(sa, SHOP_TAB_SERIES);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68171"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyProgressBarAfterUserWatchesContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusUpNextIOSPageBase disneyPlusUpNextIOSPageBase = initPage(DisneyPlusUpNextIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia(SPIDERMAN_THREE);
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayerPage.isOpened(), "Video player is not opened");
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.scrubToPlaybackPercentage(PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT);
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.clickPauseButton();
        String durationTime = videoPlayerPage.getRemainingTimeInStringWithHourAndMinutes();
        videoPlayerPage.clickBackButton();

        detailsPage.waitForPresenceOfAnElement(detailsPage.getProgressBar());
        sa.assertTrue(detailsPage.isContinueButtonPresent(),
                "Continue button not present after exiting playback");
        sa.assertTrue(detailsPage.isProgressBarPresent(),
                "Progress bar is not present after exiting playback");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().isPresent(),
                "Continue watching - time remaining is not present");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().getText().contains(durationTime),
                "Correct remaining time is not reflecting in progress bar");

        detailsPage.clickContinueButton();
        sa.assertTrue(videoPlayerPage.isOpened(), "Video player Page is not opened");
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.scrubToPlaybackPercentage(95);
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear();
        videoPlayerPage.clickPauseButton();
        videoPlayerPage.clickBackButton();
        detailsPage.waitForPresenceOfAnElement(detailsPage.getPlayButton());
        sa.assertFalse(detailsPage.getContinueButton().isPresent(FIVE_SEC_TIMEOUT),
                "Continue button on detail page is present after completing playback");
        sa.assertFalse(detailsPage.getProgressBar().isPresent(FIVE_SEC_TIMEOUT),
                "Progress bar on detail page is present after completing playback");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61128"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyContentTitleInNavigationBar() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_dr_ks_exotic_animal_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        String contentTitle = detailsPage.getContentTitle();
        swipeInContainer(detailsPage.getContentDetailsPage(), Direction.UP, 500);
        Assert.assertTrue(detailsPage.getStaticTextByLabel(contentTitle).isPresent(),
                "Content title is not found in navigation bar");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78024"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.EODPLUS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUpsellPromptScreenForEspnContent() {
        String espnContent = "NFL Matchup";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.getSearchBar().click();
        searchPage.searchForMedia(espnContent);
        searchPage.getDynamicAccessibilityId(espnContent).click();

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getUnlockButton().isPresent(), "Unlock Button not displayed");
        detailsPage.getUnlockButton().click();

        Assert.assertTrue(detailsPage.isOnlyAvailableWithESPNHeaderPresent(),
                "Ineligible Screen Header is not present");
        Assert.assertTrue(detailsPage.isIneligibleScreenBodyPresent(),
                "Ineligible Screen Body is not present");
        Assert.assertTrue(detailsPage.getCtaIneligibleScreen().isPresent(),
                "Ineligible Screen cta is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77920"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.EODPLUS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUpsellDetailPageForEspnAndHuluContent() {
        String espnContent = "NFL Matchup";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.getSearchBar().click();
        searchPage.searchForMedia(espnContent);
        searchPage.getDynamicAccessibilityId(espnContent).click();

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getUnlockButton().isPresent(), "Unlock Button not displayed");
        Assert.assertTrue(detailsPage.getStaticTextByLabel(AVAILABLE_WITH_ESPN_SUBSCRIPTION).isPresent(),
                AVAILABLE_WITH_ESPN_SUBSCRIPTION + " upsell message not displayed");

        detailsPage.getBackArrow().click();
        searchPage.getClearTextBtn().clickIfPresent();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDynamicAccessibilityId(ONLY_MURDERS_IN_THE_BUILDING).click();

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getUpgradeNowButton().isPresent(), "Upgrade Now Button not displayed");
        Assert.assertTrue(detailsPage.getStaticTextByLabel(UNLOCK_HULU_ON_DISNEY).isPresent(),
                UNLOCK_HULU_ON_DISNEY + " upsell message not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77990"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEspnHubSportPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String sportsLabel = "Sports";
        String leagues = "Leagues";
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE));
        setAppToHomeScreen(getAccount());

        homePage.clickEspnTile();
        Assert.assertTrue(homePage.isEspnBrandPageOpen(), "ESPN brand page did not open");

        swipePageTillElementPresent(homePage.getStaticTextByLabel(sportsLabel), 5,
                homePage.getBrandLandingView(), Direction.UP, 1000);

        // Get first sport and validate page
        sa.assertTrue(homePage.getCollection(DisneyEntityIds.SPORTS_PAGE.getEntityId()).isPresent(), "Sports container was not found");
        String sportTitle = getContainerTitlesFromApi(DisneyEntityIds.SPORTS_PAGE.getEntityId(), 5).get(0);
        if(!sportTitle.isEmpty()) {
            homePage.getTypeCellLabelContains(sportTitle).click();
            sa.assertTrue(homePage.isSportTitlePresent(sportTitle), "Sport title was not found");
            sa.assertTrue(homePage.getBackButton().isPresent(), "Back button is not present");
            sa.assertTrue(homePage.getStaticTextByLabelContains(leagues).isPresent(), "Leagues container is not present");
        } else {
            throw new IllegalArgumentException("No containers titles found for Sports");
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73820"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkSeriesAndMovieServiceAttribution() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        ArrayList<String> contentList = new ArrayList<>();
        contentList.add(ONLY_MURDERS_IN_THE_BUILDING);
        contentList.add(PREY);

        IntStream.range(0, contentList.size()).forEach(i -> {
            homePage.clickSearchIcon();
            if (searchPage.getClearTextBtn().isPresent(SHORT_TIMEOUT)) {
                searchPage.clearText();
            }
            searchPage.searchForMedia(contentList.get(i));
            List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
            results.get(0).click();
            detailsPage.isOpened();
            Assert.assertTrue(detailsPage.getServiceAttribution().isPresent(), "Service attribution was not found on Hulu series detail page.");
        });
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72248"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkExtrasTab() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.isExtrasTabPresent(), "Extras tab was not found.");

        detailsPage.clickExtrasTab();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            detailsPage.swipeUp(1500);
        }
        sa.assertTrue(detailsPage.isTabSelected(detailsPage.getExtrasTab().getAttribute(Attributes.NAME.getAttribute())),
                "EXTRAS Tab is not selected");
        sa.assertTrue(detailsPage.getPlayIcon().isPresent(), "Extras tab play icon was not found");
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(), "First extras title was not found");
        sa.assertTrue(detailsPage.getFirstDescriptionLabel().isPresent(), "First extras description was not found");
        sa.assertTrue(detailsPage.getFirstDurationLabel().isPresent(), "First extras runtime was not found");

        detailsPage.getPlayIcon().click();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.fluentWait(getDriver(), 60, 5, "Time remaining not found").until(it -> videoPlayer.getRemainingTime() <= 130);
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickSuggestedTab();
        detailsPage.waitForPresenceOfAnElement(detailsPage.getExtrasTab());
        detailsPage.clickExtrasTab();
        sa.assertTrue(detailsPage.isProgressBarPresent(), "Duration not displayed on extras trailer.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74254"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkShare() {
        String grootSeries = "I Am Groot";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoseWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount())
                .profileName(JUNIOR_PROFILE).dateOfBirth(KIDS_DOB)
                .language(getAccount().getProfileLang()).avatarId(BABY_YODA)
                .kidsModeEnabled(true).isStarOnboarded(true).build());
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        //Adult
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.searchForMedia(PREY);
        searchPage.getDynamicAccessibilityId(PREY).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not found.");
        detailsPage.getShareBtn().click();
        sa.assertTrue(detailsPage.getTypeOtherByLabel(String.format("%s | Disney+", PREY)).isPresent(),
                String.format("'%s | Disney+' title was not found on share actions.", PREY));
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Mail").isPresent(),
                "Share action 'Mail' was not found.");

        detailsPage.getShareBtn().click();
        detailsPage.clickOnCopyShareLink();
        detailsPage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        String actualUrl = searchPage.getClipboardContentBySearchInput().split("\\?")[0];
        String expectedUrl = R.TESTDATA.get("disney_prod_hulu_movie_prey_share_link");
        Assert.assertTrue(expectedUrl.contains(actualUrl), "Deeplink is not correct");

        //Kids
        searchPage.clickMoreTab();
        whoseWatchingPage.clickProfile(JUNIOR_PROFILE);
        Assert.assertTrue(homePage.isKidsHomePageOpen(), "Kids home page did not open");
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.searchForMedia(grootSeries);
        searchPage.getDynamicAccessibilityId(grootSeries).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertFalse(detailsPage.getShareBtn().isPresent(ONE_SEC_TIMEOUT),
                "Share button was found on kids profile.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73820"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkSeriesAndMovieNetworkAttribution() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        ArrayList<String> contentList = new ArrayList<>();
        contentList.add(ONLY_MURDERS_IN_THE_BUILDING);
        contentList.add("Palm Springs");
        contentList.add("Home Economics");
        contentList.add("Cruel Summer");
        contentList.add("Devs");

        IntStream.range(0, contentList.size()).forEach(i -> {
            homePage.clickSearchIcon();
            if (searchPage.getClearTextBtn().isPresent(SHORT_TIMEOUT)) {
                searchPage.clearText();
            }
            searchPage.searchForMedia(contentList.get(i));
            List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
            results.get(0).click();
            sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
            if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
                Assert.assertTrue(detailsPage.getHandsetNetworkAttributionImage().isPresent(), "Handset Network attribution image was not found on " + i + " series details page.");
            } else {
                Assert.assertTrue(detailsPage.getTabletNetworkAttributionImage().isPresent(), "Tablet Network attribution image was not found on " + i + " series details page.");
            }
        });
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75083"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyJuniorProfileNoHulu() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer =  initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(JUNIOR_PROFILE).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang()).avatarId(BABY_YODA).kidsModeEnabled(true).isStarOnboarded(true).build());
        setAppToHomeScreen(getAccount(), JUNIOR_PROFILE);

        //No upsell
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertFalse(moreMenu.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT),
                "Account option was available to a child account, upsell option possible");

        //Home
        moreMenu.clickHomeIcon();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.getKidsCarousels().forEach(element -> sa.assertFalse(element.getText().contains(HULU),
                String.format("%s contains %s", element.getText(), HULU)));
        sa.assertFalse(homePage.isHuluTileVisible(), "Hulu tile was found on Kids home.");
        sa.assertTrue(homePage.getStaticTextByLabelContains(HULU).isElementNotPresent(SHORT_TIMEOUT), "Hulu branding was found on Kids' Home page");

        //Search
        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.getStaticTextByLabelContains(HULU).isElementNotPresent(SHORT_TIMEOUT), "Hulu branding was found on Kids' Search page");

        //Hulu Original Movie
        searchPage.searchForMedia(PREY);
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(PREY), PREY + " 'no results found' message not found.");

        //Hulu Original Kids Series
        searchPage.clearText();
        searchPage.searchForMedia(THE_BRAVEST_KNIGHT);
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(THE_BRAVEST_KNIGHT), THE_BRAVEST_KNIGHT + " 'no results found' message not found.");

        //Details
        searchPage.clearText();
        searchPage.searchForMedia(BLUEY);
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getStaticTextByLabelContains(HULU).isElementNotPresent(SHORT_TIMEOUT), "Hulu branding was found on Kids' Detail page");

        //Ad badge
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.displayVideoController();
        sa.assertTrue(videoPlayer.isAdBadgeLabelNotPresent(),
                "Ad badge found on Kids profile video content.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74448"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluAdTierMovieSeriesNoDownloadButton() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        //Movie
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertFalse(detailsPage.isMovieDownloadButtonDisplayed(), "Movie download button is not displayed.");

        //Series
        detailsPage.clickSearchIcon();
        searchPage.clearText();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertFalse(detailsPage.getEpisodeToDownload("1", "1").isPresent(), "Season button 1 button is was found.");
        sa.assertFalse(detailsPage.getDownloadAllSeasonButton().isPresent(), "Download all season button was found.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74630"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluDetailPagesFeaturedAreaAVBadgesAdsAccount() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();

        sa.assertTrue(detailsPage.getStaticTextByLabelContains("HD").isPresent(), "`HD` video quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("5.1").isPresent(), "`5.1` audio quality is not found.");

        //Validate Dolby Vision present / not present on certain devices
        detailsPage.isDolbyVisionPresentOrNot(sa);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74586"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluDetailsPageRatings() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_teen_titans_go_deeplink"));
        detailsPage.verifyRatingsInDetailsFeaturedArea(TV_PG.getContentRating(), sa);
        detailsPage.validateRatingsInDetailsTab(TV_PG.getContentRating(), sa);

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_movie_bohemian_rhapsody_deeplink"));
        detailsPage.verifyRatingsInDetailsFeaturedArea(PG_13.getContentRating(), sa);
        detailsPage.validateRatingsInDetailsTab(PG_13.getContentRating(), sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76389"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluSeriesDetailsTabContentBadges() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        setAppToHomeScreen(getAccount());
        homePage.waitForHomePageToOpen();

        // Open a hulu series and validate badges in details tab
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDynamicAccessibilityId(ONLY_MURDERS_IN_THE_BUILDING).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickDetailsTab();
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            swipe(detailsPage.getEpisodeToDownload(), Direction.UP, 1, 900);
        }
        sa.assertTrue(detailsPage.getFormatDetailsText().isPresent(), "Details formats text section is not present");
        sa.assertTrue(detailsPage.getDolbyBadge().isPresent(), "Dolby badge is not present");
        sa.assertTrue(detailsPage.getUHDBadge().isPresent(), "4K badge is not present");
        sa.assertTrue(detailsPage.getHDRBadge().isPresent(), "HDR badge not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77868"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, US})
    public void verifyHulkUpsellStandaloneUserInEligibleFlow() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        int swipeCount = 5;

        DisneyAccount basicAccount = createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM);
        setAppToHomeScreen(basicAccount);

        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));

        //Verify user can play some Hulu content
        String titleAvailableToPlay = "Hulu Original Series, Select for details on this title.";
        homePage.getTypeCellLabelContains(titleAvailableToPlay).click();
        Assert.assertTrue(detailsPage.isDetailPageOpened(SHORT_TIMEOUT), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayOrContinue();
        videoPlayer.waitForVideoToStart();
        videoPlayer.skipPromoIfPresent();
        videoPlayer.verifyThreeIntegerVideoPlaying(sa);
        videoPlayer.clickBackButton();

        //Go back to the Hulu page
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.tapBackButton();

        //Swipe to the "Unlock to stream more collection"
        homePage.swipeTillCollectionTappable(CollectionConstant.Collection.UNLOCK_TO_STREAM_MORE_HULU,
                Direction.UP,
                swipeCount);

        homePage.getTypeCellLabelContains(AVAILABLE_WITH_HULU).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.getUpgradeNowButton().click();

        //Verify that user is on the ineligible interstitial screen
        sa.assertTrue(detailsPage.isOnlyAvailableWithHuluHeaderPresent(), "Ineligible Screen Header is not present");
        sa.assertTrue(detailsPage.isIneligibleScreenBodyPresent(), "Ineligible Screen Body is not present");
        sa.assertTrue(detailsPage.getCtaIneligibleScreen().isPresent(), "Ineligible Screen cta is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78017"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyExtrasTabForESPNContent() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_nfl_turning_point_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        swipe(detailsPage.getSuggestedTab(), 2);
        Assert.assertFalse(detailsPage.getSuggestedTab().isPresent(THREE_SEC_TIMEOUT), "Suggested Tab is present");
        Assert.assertFalse(detailsPage.getExtrasTab().isPresent(THREE_SEC_TIMEOUT), "Extras Tab is present");

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_movie_shohei_ohtani_beyond_the_dream_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        swipe(detailsPage.getSuggestedTab(), 2);
        Assert.assertTrue(detailsPage.getSuggestedTab().isPresent(), "Suggested Tab is not present");
        Assert.assertTrue(detailsPage.getExtrasTab().isPresent(), "Extras Tab is not present");
        detailsPage.clickExtrasTab();
        String extrasContentTitle = detailsPage.getFirstTitleLabel().getText();
        detailsPage.getFirstTitleLabel().click();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(extrasContentTitle),
                "Expected content title not playing");
    }

    private void validateShopPromoLabelHeaderAndSubHeader(SoftAssert sa, String titleName) {
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        searchPage.searchForMedia(titleName);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> detailsPage.isShopPromoLabelHeaderPresent());
        } catch (Exception e) {
            throw new SkipException(String.format(
                    "Skipping test, Shop Promo Label header was not found for: %s", titleName) + e);
        }
        sa.assertTrue(detailsPage.isShopPromoLabelSubHeaderPresent(),
                String.format("Shop Promo Label Sub-header was not found for: %s", titleName));
        sa.assertTrue(detailsPage.getShopOrPerksBtn().isPresent(THREE_SEC_TIMEOUT),
                String.format("Shop or Perks Tab was not found for: %s", titleName));
    }

    private void validateShopTabButton(SoftAssert sa, String titleName){
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        searchPage.searchForMedia(titleName);
        List<ExtendedWebElement>  results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Detail page did not open");
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> detailsPage.getShopOrPerksBtn().isPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Shop or Perks Tab was not found" + e);
        }
        detailsPage.clickShopoOrPerksTab();
        String shopOrPerksText = detailsPage.getShopOrPerksBtn().getAttribute(Attributes.NAME.getAttribute());
        sa.assertTrue(detailsPage.isTabSelected(shopOrPerksText),
                String.format("%s Tab was not found", shopOrPerksText));
    }
}
