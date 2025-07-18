package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.requests.*;
import com.disney.qa.api.client.responses.profile.Profile;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.common.constant.*;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.CollectionConstant.Collection.ESPN_LEAGUES;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.*;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_PG;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusDetailsTest extends DisneyBaseTest {

    private static final String THE_LION_KINGS_TIMON_AND_PUUMBA = "The Lion King Timon Pumbaa";
    private static final String THE_ARISTOCATS = "The aristocats";
    private static final String TV_Y7 = "TV-Y7";
    private static final String SPIDERMAN_THREE = "Spider-Man™ 3";
    private static final double PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT = 40;
    private static final String THE_BRAVEST_KNIGHT = "The Bravest Knight";
    private static final String BLUEY = "Bluey";
    private static final String AVAILABLE_WITH_HULU = "Available with Hulu Subscription";
    private static final String UNLOCK_HULU_ON_DISNEY = "Unlock Hulu on Disney+";
    private static final String AVAILABLE_WITH_ESPN_SUBSCRIPTION = "Available with ESPN+ Subscription";
    public static final String UPCOMING = "Upcoming";
    public static final String LIVE = "LIVE";
    private static final String ESPN_CONTENT = "NFL 2025 Winter Classic";
    public static final String NEGATIVE_STEREOTYPE_ADVISORY_DESCRIPTION = "This program is presented as originally " +
            "created and may contain stereotypes or negative depictions.";
    private static final String RATING_RESTRICTION_DETAIL_MESSAGE_NOT_DISPLAYED = "Rating Restriction Detail Message " +
            "is not displayed";
    private static final String PARENTAL_CONTROL_ICON_NOT_DISPLAYED = "Parental Control icon is not displayed";
    private static final String EXTRAS_TAB_DISPLAYED = "Extras tab is displayed";
    private static final String SUGGESTED_TAB_DISPLAYED = "Suggested tab is displayed";
    private static final String DETAILS_TAB_DISPLAYED = "Details tab is displayed";
    private static final String EPISODES_TAB_DISPLAYED = "Episodes tab is displayed";
    private static final String WATCHLIST_BUTTON_DISPLAYED = "Watchlist CTA is displayed";
    private static final String TRAILER_BUTTON_DISPLAYED = "Trailer CTA displayed";
    private static final String PLAY_BUTTON_DISPLAYED = "Play CTA found.";
    private static final String METADATA_NOT_DISPLAYED = "Metadata label is not displayed";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71130"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyIMAXEnhancedBadges() {
        String filterValue = "IMAX Enhanced";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.clickMoviesTab();
        Assert.assertTrue(searchPage.getStaticTextByLabel("Movies").isPresent(), "Movies Content Page is not displayed");
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            searchPage.clickContentPageFilterDropDown();
            searchPage.waitForLoaderToDisappear(SHORT_TIMEOUT);
            searchPage.swipeItemPicker(Direction.UP);
            searchPage.waitForLoaderToDisappear(SHORT_TIMEOUT);
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
        setAppToHomeScreen(getUnifiedAccount());

        ExploreContent seriesApiContent = getSeriesApi(R.TESTDATA.get("disney_prod_lion_king_timon_and_pumbaa_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);

        //series
        home.clickSearchIcon();
        search.searchForMedia(THE_LION_KINGS_TIMON_AND_PUUMBA);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        sa.assertTrue(details.isContentDetailsPagePresent(),
                "Details tab was not found on details page");
        details.clickDetailsTab();
        String contentAdvisoryUI = details.getTypeOtherContainsLabel(NEGATIVE_STEREOTYPE_ADVISORY_DESCRIPTION).getText();
        sa.assertTrue(contentAdvisoryUI.contains(details.retrieveContentAdvisory(seriesApiContent)),
                "Content Advisory Description not as expected");

        //movie
        home.clickSearchIcon();
        search.clearText();
        search.searchForMedia(THE_ARISTOCATS);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        sa.assertTrue(details.isContentDetailsPagePresent(),
                "Details tab was not found on details page");
        details.clickDetailsTab();
        sa.assertTrue(contentAdvisoryUI.contains(details.retrieveContentAdvisory(seriesApiContent)),
                "Content Advisory Description not as expected");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66841"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMaturityRatingRestrictionOnDetailPage() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(TV_Y7)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());
        Profile profile = getUnifiedAccount().getProfile(TV_Y7);

        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                profile.getProfileId(),
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystem(),
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues().get(1));

        setAppToHomeScreen(getUnifiedAccount(), profile.getProfileName());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        // Movies
        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_deeplink"));

        sa.assertFalse(detailsPage.getExtrasTab().isPresent(SHORT_TIMEOUT), EXTRAS_TAB_DISPLAYED);
        sa.assertFalse(detailsPage.getSuggestedTab().isPresent(SHORT_TIMEOUT), SUGGESTED_TAB_DISPLAYED);
        sa.assertFalse(detailsPage.getDetailsTab().isPresent(SHORT_TIMEOUT), DETAILS_TAB_DISPLAYED);
        sa.assertFalse(detailsPage.getWatchlistButton().isPresent(SHORT_TIMEOUT), WATCHLIST_BUTTON_DISPLAYED);
        sa.assertFalse(detailsPage.getTrailerButton().isPresent(SHORT_TIMEOUT), TRAILER_BUTTON_DISPLAYED);
        sa.assertFalse(detailsPage.getPlayButton().isPresent(SHORT_TIMEOUT), PLAY_BUTTON_DISPLAYED);

        sa.assertTrue(detailsPage.getParentalControlIcon().isPresent(), PARENTAL_CONTROL_ICON_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getRatingRestrictionDetailMessage().isPresent(),
                RATING_RESTRICTION_DETAIL_MESSAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), METADATA_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getMediaTitle().contains("The Avengers"), MEDIA_TITLE_NOT_DISPLAYED);

        // Series
        launchDeeplink(R.TESTDATA.get("disney_prod_dr_ks_exotic_animal_deeplink"));

        sa.assertFalse(detailsPage.getExtrasTab().isPresent(SHORT_TIMEOUT), EXTRAS_TAB_DISPLAYED);
        sa.assertFalse(detailsPage.getSuggestedTab().isPresent(SHORT_TIMEOUT), SUGGESTED_TAB_DISPLAYED);
        sa.assertFalse(detailsPage.getDetailsTab().isPresent(SHORT_TIMEOUT), DETAILS_TAB_DISPLAYED);
        sa.assertFalse(detailsPage.getEpisodesTab().isPresent(SHORT_TIMEOUT), EPISODES_TAB_DISPLAYED);
        sa.assertFalse(detailsPage.getWatchlistButton().isPresent(SHORT_TIMEOUT), WATCHLIST_BUTTON_DISPLAYED);
        sa.assertFalse(detailsPage.getTrailerButton().isPresent(SHORT_TIMEOUT), TRAILER_BUTTON_DISPLAYED);
        sa.assertFalse(detailsPage.getPlayButton().isPresent(SHORT_TIMEOUT), PLAY_BUTTON_DISPLAYED);

        sa.assertTrue(detailsPage.getParentalControlIcon().isPresent(), PARENTAL_CONTROL_ICON_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getRatingRestrictionDetailMessage().isPresent(),
                RATING_RESTRICTION_DETAIL_MESSAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), METADATA_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getMediaTitle().contains("Dr. K's Exotic Animal ER"), MEDIA_TITLE_NOT_DISPLAYED);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71128"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyIMAXEnhancedVersionTab() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String widescreenDescription = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_VERSIONS_DESCRIPTION_OV_WIDESCREEN.getText());
        String imaxEnhancedDescription = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DETAILS_VERSIONS_DESCRIPTION_IMAX_ENHANCED.getText());

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_lightyear_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        String title = detailsPage.getMediaTitle();
        detailsPage.clickVersionsTab();
        Assert.assertTrue(detailsPage.getVersionTab().isPresent(), "Versions Tab was not found");

        //Validate Both Titles
        scrollDown();
        sa.assertEquals(detailsPage.getFirstTitleLabel().getText(), title, "Widescreen Title is not displayed");
        sa.assertEquals(detailsPage.getSecondTitleLabel().getText(), "IMAX Enhanced - " + title,
                "IMAX Title is not displayed");
        //Validate Both Images and Play Icon
        sa.assertTrue(detailsPage.isFirstImageInVersionTabPresent(), "Widescreen Image is not displayed");
        sa.assertTrue(detailsPage.isSecondImageInVersionTabPresent(), "IMAX Image is not displayed");
        sa.assertTrue(detailsPage.isFirstPlayIconPresentInVersionTabPresent(), "Widescreen Play Icon is not displayed");
        sa.assertTrue(detailsPage.isSecondPlayIconPresentInVersionTabPresent(), "IMAX Play Icon is not displayed");
        //Validate Both Descriptions
        sa.assertEquals(detailsPage.getFirstDescriptionLabel().getText(), widescreenDescription,
                "Widescreen Description was not found");
        sa.assertEquals(detailsPage.getSecondDescriptionLabel().getText(), imaxEnhancedDescription,
                "IMAX Description was not found");
        //Validate Video Duration from API
        ExploreContent exploreMovieContent = getMovieApi(R.TESTDATA.get("disney_prod_lightyear_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        if (exploreMovieContent != null) {
            int durationAPI = exploreMovieContent.getDurationMs();
            String durationInHoursMinutes = detailsPage.getHourMinFormatForDuration(durationAPI);
            sa.assertEquals(detailsPage.getFirstDurationLabel().getText(), durationInHoursMinutes,
                    "Widescreen Duration is not as expected");
            sa.assertEquals(detailsPage.getSecondDurationLabel().getText(), durationInHoursMinutes,
                    "IMAX Duration is not as expected");
        } else {
            throw new SkipException("Not able to get the Movies collection data from the API");
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72725"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyShopPromoLabelInFeatureAreaOfDetailPage() {
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());

        //Verify Shop Promo for Series
        launchDeeplink(R.TESTDATA.get("disney_prod_series_disney_parks_sunrise_series_deeplink"));
        validateShopPromoLabelHeaderAndSubHeader(sa);

        //Verify Shop Promo for Movie
        launchDeeplink(R.TESTDATA.get("disney_prod_zootropolis_deeplink"));
        validateShopPromoLabelHeaderAndSubHeader(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72730"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyShopTabInDetailsPage() {
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());

        //verify Shop Tab button for movies
        launchDeeplink(R.TESTDATA.get("disney_prod_zootropolis_deeplink"));
        validateShopTabButton(sa);
        validateShopTabContainer(sa);

        //Verify Shop tab button for series
        launchDeeplink(R.TESTDATA.get("disney_prod_series_disney_parks_sunrise_series_deeplink"));
        validateShopTabButton(sa);
        validateShopTabContainer(sa);
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

        setAppToHomeScreen(getUnifiedAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        homePage.getSearchNav().click();
        searchPage.searchForMedia(SPIDERMAN_THREE);
        searchPage.getDynamicAccessibilityId(SPIDERMAN_THREE).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayerPage.isOpened(), "Video player is not opened");
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.clickPauseButton();
        videoPlayerPage.scrubToPlaybackPercentage(PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT);
        videoPlayerPage.clickPlayButton();
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
        Assert.assertTrue(videoPlayerPage.isOpened(), "Video player Page is not opened");
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.clickPauseButton();
        videoPlayerPage.scrubToPlaybackPercentage(95);
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear();
        videoPlayerPage.clickPlayButton();
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
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_bluey_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        String contentTitle = detailsPage.getContentTitle();
        swipeInContainer(detailsPage.getContentDetailsPage(), Direction.UP, 500);
        detailsPage.waitForPresenceOfAnElement(detailsPage.getStaticTextByLabel(contentTitle));
        Assert.assertTrue(detailsPage.getStaticTextByLabel(contentTitle).isPresent(),
                "Content title is not found in navigation bar");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78024"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUpsellPromptScreenForEspnContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.getSearchBar().click();
        searchPage.searchForMedia(ESPN_CONTENT);
        searchPage.getDynamicAccessibilityId(ESPN_CONTENT).click();

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getUnlockButton().isPresent(), UNLOCK_BUTTON_NOT_DISPLAYED);
        detailsPage.getUnlockButton().click();

        Assert.assertTrue(detailsPage.isOnlyAvailableWithESPNHeaderPresent(),
                "Ineligible Screen Header is not present");
        Assert.assertTrue(detailsPage.isIneligibleScreenBodyPresent(),
                "Ineligible Screen Body is not present");
        Assert.assertTrue(detailsPage.getCtaIneligibleScreen().isPresent(),
                "Ineligible Screen cta is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77920"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUpsellDetailPageForEspnAndHuluContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.getSearchBar().click();
        searchPage.searchForMedia(ESPN_CONTENT);
        searchPage.getDynamicAccessibilityId(ESPN_CONTENT).click();

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getUnlockButton().isPresent(), UNLOCK_BUTTON_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getStaticTextByLabel(AVAILABLE_WITH_ESPN_SUBSCRIPTION).isPresent(),
                AVAILABLE_WITH_ESPN_SUBSCRIPTION + " upsell message not displayed");

        detailsPage.getBackArrow().click();
        searchPage.getClearTextBtn().clickIfPresent();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDynamicAccessibilityId(ONLY_MURDERS_IN_THE_BUILDING).click();

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getUnlockButton().isPresent(), UNLOCK_BUTTON_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getStaticTextByLabel(UNLOCK_HULU_ON_DISNEY).isPresent(),
                UNLOCK_HULU_ON_DISNEY + " upsell message not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77990"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEspnHubSportPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusEspnIOSPageBase espnPage = initPage(DisneyPlusEspnIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String leagues = "Leagues";

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        homePage.swipeToOriginalBrandRow();
        homePage.clickEspnTile();
        Assert.assertTrue(espnPage.isOpened(), ESPN_PAGE_IS_NOT_DISPLAYED);

        String espnSportCollectionId = CollectionConstant.getCollectionName(CollectionConstant.Collection.ESPN_SPORTS);
        ExtendedWebElement sportsContainer = homePage.getCollection(espnSportCollectionId);
        swipePageTillElementPresent(sportsContainer, 5,
                homePage.getBrandLandingView(), Direction.UP, 1000);

        // Get first sport and validate page
        sa.assertTrue(sportsContainer.isPresent(), "Sports container was not found");
        String sportTitle = getContainerTitlesFromApi(espnSportCollectionId, 5).get(0);
        if(!sportTitle.isEmpty()) {
            espnPage.getCellElementFromContainer(CollectionConstant.Collection.ESPN_SPORTS, sportTitle).click();
            sa.assertTrue(espnPage.isPageTitlePresent(sportTitle), "Sport title was not found");
            sa.assertTrue(homePage.getBackButton().isPresent(), "Back button is not present");
            sa.assertTrue(homePage.getStaticTextByLabelContains(leagues).isPresent(), "Leagues container is not present");
        } else {
            throw new IllegalArgumentException("No containers titles found for Sports");
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77989"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEspnHubLeaguePage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusEspnIOSPageBase espnPage = initPage(DisneyPlusEspnIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        homePage.clickEspnTile();
        Assert.assertTrue(espnPage.isOpened(), ESPN_PAGE_IS_NOT_DISPLAYED);

        String espnLeagueTitle = CollectionConstant.getCollectionTitle(ESPN_LEAGUES);

        swipePageTillElementPresent(homePage.getStaticTextByLabel(espnLeagueTitle), 5,
                homePage.getBrandLandingView(), Direction.UP, 1000);
        Assert.assertTrue(homePage.getStaticTextByLabel(espnLeagueTitle).isPresent(),
                "ESPN League title is not present");

        ExtendedWebElement leagueContainer =
                homePage.getCollection(CollectionConstant.getCollectionName(ESPN_LEAGUES));
        swipePageTillElementPresent(leagueContainer, 2,
                homePage.getBrandLandingView(), Direction.UP, 1000);

        // Get first league and validate page
        String leagueName = getContainerTitlesFromApi(CollectionConstant.getCollectionName(ESPN_LEAGUES), 5).get(0);
        LOGGER.info("leagueName:{}", leagueName);
        if(!leagueName.isEmpty()) {
            espnPage.getCellElementFromContainer(CollectionConstant.Collection.ESPN_LEAGUES, leagueName).click();
            sa.assertTrue(espnPage.getHeroImage().isPresent(), "Hero Image is not found");
            sa.assertTrue(espnPage.getLogoImage().isPresent(), "Logo Image is not found");
            sa.assertTrue(espnPage.getLogoImage().getText().equals(leagueName),
                    "Logo Image label is not as expected");
            LOGGER.info("league text:{}", espnPage.getLogoImage().getText());
            sa.assertTrue(homePage.getBackButton().isPresent(), "Back button is not present");
        } else {
            throw new SkipException("No containers titles found for league");
        }
        sa.assertAll();
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73820"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluSeriesAndMovieServiceAttribution() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        ArrayList<String> contentList = new ArrayList<>();
        contentList.add(ONLY_MURDERS_IN_THE_BUILDING);
        contentList.add(BILL_BURR);

        IntStream.range(0, contentList.size()).forEach(i -> {
            homePage.clickSearchIcon();
            if (searchPage.getClearTextBtn().isPresent(SHORT_TIMEOUT)) {
                searchPage.clearText();
            }
            searchPage.searchForMedia(contentList.get(i));
            List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
            results.get(0).click();
            detailsPage.isOpened();
            Assert.assertTrue(detailsPage.getServiceAttribution().isPresent(),
                    "Hulu Detail Page Service attribution was not found");
        });
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72248"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluExtrasTab() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluShare() {
        String grootSeries = "I Am Groot";
        String mailLabel = "Mail";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoseWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), DEFAULT_PROFILE);

        //Adult
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
        searchPage.searchForMedia(PREY);
        searchPage.getDynamicAccessibilityId(PREY).click();
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not found");
        detailsPage.getShareBtn().click();
        sa.assertTrue(detailsPage.getTypeOtherByLabel(PREY).isPresent(),
                String.format("'%s' title was not found on share actions", PREY));
        sa.assertTrue(detailsPage.getStaticTextByLabelContains(mailLabel).isPresent(),
                "Share action 'Mail' was not found");

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
                "Share button was found on kids profile");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73820"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluSeriesAndMovieNetworkAttribution() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

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
            detailsPage.waitForPresenceOfAnElement(detailsPage.getNetworkAttributionLogo());
            Assert.assertTrue(detailsPage.getNetworkAttributionLogo().isPresent(),
                    "Network attribution logo was not found on " + i + " series details page");
        });
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75083"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyJuniorProfileNoHulu() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer =  initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());
        setAppToHomeScreen(getUnifiedAccount(), KIDS_PROFILE);
        Assert.assertTrue(homePage.isKidsHomePageOpen(), HOME_PAGE_NOT_DISPLAYED);

        //No upsell
        homePage.clickMoreTab();
        sa.assertTrue(moreMenu.isMenuOptionNotPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT),
                "Account option was available to a child account, upsell option possible");

        //Home
        moreMenu.clickHomeIcon();
        Assert.assertTrue(homePage.isKidsHomePageOpen(), HOME_PAGE_NOT_DISPLAYED);
        homePage.getKidsCarousels().forEach(element -> sa.assertFalse(element.getText().contains(HULU),
                String.format("%s contains %s", element.getText(), HULU)));
        sa.assertFalse(homePage.getBrandCell(HULU).isPresent(ONE_SEC_TIMEOUT), "Hulu tile was found on Kids home.");
        sa.assertTrue(homePage.getStaticTextByLabelContains(HULU).isElementNotPresent(SHORT_TIMEOUT), "Hulu branding was found on Kids' Home page");

        //Search
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
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
        searchPage.getDynamicAccessibilityId(BLUEY).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getStaticTextByLabelContains(HULU).isElementNotPresent(SHORT_TIMEOUT), "Hulu branding was found on Kids' Detail page");

        //Ad badge
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.displayVideoController();
        sa.assertTrue(videoPlayer.isAdBadgeLabelNotPresent(),
                "Ad badge found on Kids profile video content.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74448"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluAdTierMovieSeriesNoDownloadButton() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_BASIC)));
        setAppToHomeScreen(getUnifiedAccount());

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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluDetailPagesFeaturedAreaAVBadgesAdsAccount() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluDetailsPageRatings() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_teen_titans_go_deeplink"));
        detailsPage.verifyRatingsInDetailsFeaturedArea(TV_PG.getContentRating(), sa);
        detailsPage.validateRatingsInDetailsTab(TV_PG.getContentRating(), sa);

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_movie_grimcutty_detailspage_deeplink"));
        detailsPage.verifyRatingsInDetailsFeaturedArea(RatingConstant.Rating.RESTRICTED.getContentRating(), sa);
        detailsPage.validateRatingsInDetailsTab(RatingConstant.Rating.RESTRICTED.getContentRating(), sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76389"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluSeriesDetailsTabContentBadges() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

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
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, US})
    public void verifyHuluUpsellStandaloneUserInEligibleFlow() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        int swipeCount = 10;

        setAppToHomeScreen(getUnifiedAccount());
        homePage.swipeToOriginalBrandRow();
        homePage.clickOnBrandCell(brandPage.getBrand(DisneyPlusBrandIOSPageBase.Brand.HULU));

        //Swipe to the "Unlock to stream more collection" and select first Upsell title
        homePage.swipeTillCollectionTappable(CollectionConstant.Collection.UNLOCK_TO_STREAM_MORE_HULU,
                Direction.UP,
                swipeCount);
        homePage.getTypeCellLabelContains(AVAILABLE_WITH_HULU).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.getUnlockButton().click();

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
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_the_last_dance_deeplink"));
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78054"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEntitledDetailPageForEspnContent() {
        int swipeCount = 5;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModal = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        DisneyPlusEspnIOSPageBase espnPage = initPage(DisneyPlusEspnIOSPageBase.class);
        CollectionConstant.Collection espnLiveAndUpcomingCollection =
                CollectionConstant.Collection.ESPN_PLUS_LIVE_AND_UPCOMING;

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickEspnTile();

        Assert.assertTrue(espnPage.isOpened(), ESPN_PAGE_IS_NOT_DISPLAYED);
        collectionPage.swipeTillCollectionTappable(espnLiveAndUpcomingCollection,
                Direction.UP, swipeCount);
        Assert.assertTrue(collectionPage.isCollectionPresent(espnLiveAndUpcomingCollection),
                "ESPN+ Live and Upcoming Container not found");

        String airingBadge = collectionPage.getAiringBadgeOfFirstCellElementFromCollection(CollectionConstant
                .getCollectionName(espnLiveAndUpcomingCollection)).getText();

        collectionPage.getFirstCellFromCollection(CollectionConstant
                .getCollectionName(espnLiveAndUpcomingCollection)).click();

        if (airingBadge.equals(UPCOMING)) {
            Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
            Assert.assertTrue(detailsPage.isWatchlistButtonDisplayed(),
                    "Watchlist button is not displayed");
            Assert.assertTrue(detailsPage.getAiringBadgeLabel().getText().contains(UPCOMING),
                    "Upcoming badge not displayed on detail page");
        } else {
            Assert.assertTrue(liveEventModal.isOpened(), "Live event modal is not open");
            liveEventModal.getDetailsButton().click();
            Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
            Assert.assertTrue(detailsPage.isWatchButtonPresent(), "Watch button not displayed");
            Assert.assertTrue(detailsPage.getAiringBadgeLabel().getAttribute(Attributes.LABEL.getAttribute()).contains(LIVE),
                    "Live badge not displayed on detail page");
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78066"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, JP})
    public void verifyESPNUnavailableDetailsPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM)));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_nfl_turning_point_deeplink"));

        Assert.assertTrue(homePage.isViewAlertPresent(),
                "Alert was not present");
        Assert.assertTrue(homePage.getContentUnavailableErrorMessageElement().isElementPresent(),
                "Content Unavailable error message was not present");
        Assert.assertTrue(homePage.getOkButton().isElementPresent(),
                "OK button text was not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78064"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNUnavailableDetailsPagePCONError() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        Profile secondaryProfile = getUnifiedAccount().getProfile(SECONDARY_PROFILE);
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                secondaryProfile.getProfileId(),
                secondaryProfile.getAttributes().getParentalControls().getMaturityRating().getRatingSystem(),
                secondaryProfile.getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues().get(1));

        setAppToHomeScreen(getUnifiedAccount(), SECONDARY_PROFILE);
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_nhl_replay_deeplink"));

        Assert.assertTrue(detailsPage.getEspnPlusGenericErrorText().isElementPresent(),
                "Inline generic error message was not present");
        Assert.assertFalse(detailsPage.getDetailsTab().isElementPresent(FIVE_SEC_TIMEOUT),
                "Details tab was present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78420"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluNetworkAttributionInDetailsPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_only_murders_in_the_building_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getNetworkAttributionLogo().isPresent(),
                "Network Attribution logo is not present");
        String currentNetworkAttribution = detailsPage.getNetworkAttributionValue();
        Assert.assertTrue(currentNetworkAttribution.contains(HULU),
                String.format("Current network attribution '%s' didn't contain '%s'",
                        currentNetworkAttribution, HULU));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77682"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNNetworkAttributionInDetailsPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);
        CollectionConstant.Collection espnLiveAndUpcomingCollection =
                CollectionConstant.Collection.ESPN_EXPLORE_MORE;

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        //verify network attribution on VOD
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_the_last_dance_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        verifyNetworkAttributionAndPlacement(ESPN_PLUS);
        detailsPage.clickCloseButton();

        //verify network attribution on live content
        homePage.clickEspnTile();
        collectionPage.swipeTillCollectionTappable(espnLiveAndUpcomingCollection,
                Direction.UP, 5);
        Assert.assertTrue(collectionPage.isCollectionPresent(espnLiveAndUpcomingCollection),
                "ESPN+ More with Container not found");
        collectionPage.getFirstCellFromCollection(CollectionConstant
                .getCollectionName(espnLiveAndUpcomingCollection)).click();
        //D+ basic subscription will not allow live events
        verifyNetworkAttributionAndPlacement(ESPN_PLUS);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77679"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNUpsellDetailsPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        // Update to Api metadata validations when QP-4303 is resolved
        String description = "Watch all your favorite sports content anytime, anywhere.";
        String genre = "Sports";
        String releaseYear = "2022";
        String duration = "32m";

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        //Open an ESPN+ content title
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_perfect_number_one_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        // Validate metadata
        sa.assertTrue(detailsPage.getMetaDataLabel().isPresent(), "Metadata badging information is not present");
        sa.assertTrue(detailsPage.getMetaDataLabel().getText().contains(releaseYear),
                "Release year is not present");
        sa.assertTrue(detailsPage.getMetaDataLabel().getText().contains(genre),
                "Genre is not present");
        sa.assertTrue(detailsPage.getMetaDataLabel().getText().contains(duration),
                "Duration is not present");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains(description).isPresent(),
                "Description is not present");
        sa.assertTrue(detailsPage.getUnlockButton().isPresent(), UNLOCK_BUTTON_NOT_DISPLAYED);
        sa.assertFalse(detailsPage.getPlayButton().isPresent(), "Play Button is present");
        sa.assertFalse(detailsPage.getWatchlistButton().isPresent(), "Watchlist Button is present");

        detailsPage.getUnlockButton().click();
        sa.assertTrue(detailsPage.isIneligibleScreenBodyPresent(), "Ineligible Screen Body is not present");
        sa.assertTrue(detailsPage.getCtaIneligibleScreen().isPresent(), "Ineligible Screen cta is not present");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-82729"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNElementAttributionInDetailsAndPlayer() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_BASIC)));
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        //Open an entitlement-gated ESPN+ content
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_long_gone_deeplink"));

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getStaticTextByLabelContains(ESPN_SUBSCRIPTION_MESSAGE).isPresent(),
                ENTITLEMENT_ATTRIBUTION_IS_NOT_PRESENT);

        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getStaticTextByLabelContains(ESPN_SUBSCRIPTION_MESSAGE).isPresent(),
                "Element attribution is not present on video player");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77675"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEpisodesAndSuggestedTabForESPNContent() {
        String seasonNumber = "1";
        String episodeNumber = "1";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        //Go to details page for ESPN series with upsell badge and suggested content present
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_man_in_the_arena_deeplink"));
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);

        ExtendedWebElement firstEpisode = detailsPage.getEpisodeCell(seasonNumber, episodeNumber);
        detailsPage.swipePageTillElementPresent(firstEpisode, 1, null, Direction.UP, 1500);
        Assert.assertTrue(detailsPage.getEpisodesTab().isPresent(), EPISODE_TAB_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.isUpsellBadgeDisplayedForEpisode(seasonNumber, episodeNumber),
                "Upsell badge is not displayed for episode");
        Assert.assertTrue(detailsPage.getSuggestedTab().isPresent(), SUGGESTED_TAB_NOT_DISPLAYED);
        detailsPage.clickSuggestedTab();
        detailsPage.getFirstSuggestedContent().click();
        detailsPage.clickPlayButton(TEN_SEC_TIMEOUT);
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);

        //Go to details page for ESPN series for which there is no suggested content
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_the_last_dance_deeplink"));
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getEpisodesTab().isPresent(), EPISODE_TAB_NOT_DISPLAYED);
        Assert.assertFalse(detailsPage.getSuggestedTab().isPresent(ONE_SEC_TIMEOUT),
                "Suggested tab is displayed even if there is no suggested content");
    }

    private void validateShopPromoLabelHeaderAndSubHeader(SoftAssert sa) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        String titleName = detailsPage.getLogoImage().getText();
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

    private void validateShopTabButton(SoftAssert sa){
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        Assert.assertTrue(detailsPage.isOpened(), "DETAILS_PAGE_NOT_DISPLAYED");
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> detailsPage.getShopOrPerksBtn().isPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Shop or Perks Tab was not found" + e);
        }
        detailsPage.clickShopOrPerksTab();
        String shopOrPerksText = detailsPage.getShopOrPerksBtn().getAttribute(Attributes.NAME.getAttribute());
        sa.assertTrue(detailsPage.isTabSelected(shopOrPerksText),
                String.format("%s Tab was not found", shopOrPerksText));
    }

    private void validateShopTabContainer(SoftAssert sa){
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        sa.assertTrue(detailsPage.isShopTabBackgroundImagePresent(), "Shop tab background image is not present");
        sa.assertTrue(detailsPage.getShopTabHeadingText().isPresent(), "Shop Tab heading is not present");
        sa.assertTrue(detailsPage.getShopTabSubHeadingText().isPresent(), "Shop Tab sub-heading is not present");
        sa.assertTrue(detailsPage.getShopTabLink().isPresent(), "Shop Tab link is not present");
    }

    private void verifyNetworkAttributionAndPlacement(String network) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        Assert.assertTrue(detailsPage.getNetworkAttributionLogo().isPresent(),
                "ESPN Network Attribution is not present on details page");
        String currentNetworkAttribution = detailsPage.getNetworkAttributionValue();
        Assert.assertTrue(currentNetworkAttribution.contains(network),
                String.format("Current network attribution '%s' didn't contain '%s'",
                        currentNetworkAttribution, network));
        Assert.assertTrue(verifyElementIsOnRightSide(detailsPage.getNetworkAttributionLogo()),
                "Network Attribution logo is not on the right side of the details page");
    }
}
