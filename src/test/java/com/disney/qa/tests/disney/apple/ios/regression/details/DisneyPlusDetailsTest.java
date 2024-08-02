package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.api.client.responses.profile.DisneyProfile;
import com.disney.qa.disney.apple.pages.common.DisneyPlusUpNextIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;
import static com.disney.qa.api.disney.DisneyEntityIds.IMAX_ENHANCED_SET;

public class DisneyPlusDetailsTest extends DisneyBaseTest {

    private static final String THE_LION_KINGS_TIMON_AND_PUUMBA = "The Lion King Timon Pumbaa";
    private static final String HIGH_SCHOOL_MUSICAL = "High School Musical: The Musical: The Series";
    private static final String HOCUS_POCUS = "Hocus Pocus";
    private static final String THE_ARISTOCATS = "The aristocats";
    private static final String TV_Y7 = "TV-Y7";
    private static final String SPIDERMAN_THREE = "SpiderMan 3";
    private static final String AHSOKA = "Ahsoka";
    private static final String SHOP = "Shop";
    private static final double PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT = 50;
    private static final String SHOP_TAB_SERIES = "Bluey";

    @DataProvider(name = "disneyPlanTypes")
    public Object[][] disneyWebPlanTypes() {
        return new Object[][]{{"Disney+ With Ads, Hulu with Ads, and ESPN+"},
                {"Disney+, Hulu No Ads, and ESPN+"}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68448","XMOBQA-71632"})
    @Test(description = "Series/Movies Detail Page > User taps add to watchlist", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE})
    public void verifyAddSeriesAndMovieToWatchlist() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        //search movies
        homePage.clickSearchIcon();
        searchPage.searchForMedia(HOCUS_POCUS);
        List<ExtendedWebElement> movies = searchPage.getDisplayedTitles();
        movies.get(0).click();
        String firstMovieTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);

        //search series
        searchPage.clearText();
        searchPage.searchForMedia(HIGH_SCHOOL_MUSICAL);
        List<ExtendedWebElement> series = searchPage.getDisplayedTitles();
        series.get(0).click();
        String firstSeriesTitle = initPage(DisneyPlusDetailsIOSPageBase.class).getMediaTitle();
        detailsPage.addToWatchlist();

        //titles added to watchlist
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(moreMenu.areWatchlistTitlesDisplayed(firstSeriesTitle,firstMovieTitle), "Titles were not added to the Watchlist");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71130"})
    @Test(description = "Details Page - IMAX Enhanced - Badges", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyIMAXEnhancedBadges() {
        String filterValue = "IMAX Enhanced";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");
        searchPage.clickMoviesTab();
        if(R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            searchPage.clickContentPageFilterDropDown();
            swipe(searchPage.getStaticTextByLabel(filterValue));
            searchPage.getStaticTextByLabel(filterValue).click();
        }else{
            searchPage.getTypeButtonByLabel(filterValue).click();
        }
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page was not opened");
        sa.assertTrue(detailsPage.isImaxEnhancedPresentInMediaFeaturesRow(), "IMAX Enhanced was not found in media features row");
        sa.assertTrue(detailsPage.isImaxEnhancedPresentBeforeQualityDetailsInFeturesRow(), "IMAX Enhanced was not found before video or audio quality details in media featured rows");

        detailsPage.clickDetailsTab();
        scrollDown();
        sa.assertTrue(detailsPage.areFormatsDisplayed(), "Formats in details tab not found");
        sa.assertTrue(detailsPage.isImaxEnhancedPresentsInFormats(), "IMAX Enhanced was not found in details tab formats");
        sa.assertTrue(detailsPage.isImaxEnhancedPresentBeforeQualityDetailsInFormats(), "IMAX Enhanced was not found before video or audio quality details in details tab formats");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68648"})
    @Test(description = "Series/Movies Detail Page > Negative Stereotype Advisory Expansion", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
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
        sa.assertTrue(details.isContentDetailsPagePresent(), "Details tab was not found on details page");
        details.clickDetailsTab();
        sa.assertTrue(details.isNegativeStereotypeAdvisoryLabelPresent(), "Negative Stereotype Advisory text was not found on details page");

        //movie
        home.clickSearchIcon();
        search.clearText();
        search.searchForMedia(THE_ARISTOCATS);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        sa.assertTrue(details.isContentDetailsPagePresent(), "Details tab was not found on details page");
        details.clickDetailsTab();
        sa.assertTrue(details.isNegativeStereotypeAdvisoryLabelPresent(), "Negative Stereotype Advisory text was not found on details page");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66841"})
    @Test(description = "Maturity Rating Restriction on Detail Page", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyMaturityRatingRestrictionOnDetailPage() {
        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(TV_Y7).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang()).avatarId(RAYA).kidsModeEnabled(false).isStarOnboarded(true).build());
        DisneyProfile profile = getAccount().getProfile(TV_Y7);
        getAccountApi().editContentRatingProfileSetting(getAccount(), getAccountApi().getDisneyProfiles(getAccount()).get(1).getProfileId(),
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystem(),
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues().get(1));

        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(1).getProfileName());

        // Movies
        launchDeeplink(true, R.TESTDATA.get("disney_prod_avengers_end_game_deeplink"), 10);
        detailsPage.clickOpenButton();

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
        launchDeeplink(true, R.TESTDATA.get("disney_prod_dr_ks_exotic_animal_deeplink"), 10);
        detailsPage.clickOpenButton();

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
    @Test(description = "Details Page - IMAX Enhanced - Versions Tab", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyIMAXEnhancedVersionTab() throws URISyntaxException, JsonProcessingException {
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
            ExploreContent exploreMovieContent = getDisneyApiMovie(entityID);
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
    @Test(description = "Details Page - ShopDisney - Feature Area of Details Page", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyShopPromoLabelInFeatureAreaOfDetailPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");

        //Verify Shop Promo for Series
        validateShopPromoLabelHeaderAndSubHeader(sa, SHOP_TAB_SERIES);

        //Verify Shop Promo for Movie
        detailsPage.getBackArrow().click();
        validateShopPromoLabelHeaderAndSubHeader(sa, SPIDERMAN_THREE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72730"})
    @Test(description = "Details Page - ShopDisney - Shop Tab - Primary Profile (Ad Tier & Non Ad Tier)", dataProvider = "disneyPlanTypes", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
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
    @Test(description = "Details Page - Bookmarks - Visual Progress Bar - Update after user watches content", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
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
        Assert.assertTrue(detailsPage.clickPlayButton().isOpened(), "Video player is not opened");
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.scrubToPlaybackPercentage(PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT);
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.clickPauseButton();
        String durationTime = videoPlayerPage.getRemainingTimeInStringWithHourAndMinutes();
        videoPlayerPage.clickBackButton();

        detailsPage.waitForPresenceOfAnElement(detailsPage.getProgressBar());
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button not present after exiting playback");
        sa.assertTrue(detailsPage.isProgressBarPresent(), "Progress bar is not present after exiting playback");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().isPresent(), "Continue watching - time remaining is not present");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().getText().contains(durationTime), "Correct remaining time is not reflecting in progress bar");

        detailsPage.clickContinueButton();
        sa.assertTrue(videoPlayerPage.isOpened(), "Video player Page is not opened");
        videoPlayerPage.scrubToPlaybackPercentage(99);
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear();
        videoPlayerPage.clickPauseButton();
        videoPlayerPage.clickBackButton();
        detailsPage.waitForPresenceOfAnElement(detailsPage.getPlayButton());
        sa.assertFalse(detailsPage.isContinueButtonPresent(), "Continue button on detail page is present after completing playback");
        sa.assertFalse(detailsPage.isProgressBarPresent(), "Progress bar on detail page is present after completing playback");
        sa.assertAll();
    }

    private void validateShopPromoLabelHeaderAndSubHeader(SoftAssert sa, String titleName){
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        searchPage.searchForMedia(titleName);
        List<ExtendedWebElement>  results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Detail page did not open");
        sa.assertTrue(detailsPage.isShopPromoLabelHeaderPresent(), "Shop Promo Label header was not found");
        sa.assertTrue(detailsPage.isShopPromoLabelSubHeaderPresent(), "Shop Promo Label Sub-header was not found");
        sa.assertTrue(detailsPage.getShopBtn().isPresent(), "Shop Tab was not found");
    }

    private void validateShopTabButton(SoftAssert sa, String titleName){
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        searchPage.searchForMedia(titleName);
        List<ExtendedWebElement>  results = searchPage.getDisplayedTitles();
        results.get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Detail page did not open");
        sa.assertTrue(detailsPage.getShopBtn().isPresent(), "Shop Tab was not found");
        detailsPage.clickShopTab();
        sa.assertTrue(detailsPage.isTabSelected(SHOP.toUpperCase()), "Shop tab is not focused");
    }

    private void navigateToIMAXEnhancedDetailPageFromDeeplink(String tabName) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        String deeplinkFormat = "disneyplus://www.disneyplus.com/movies/doctor-strange-in-the-multiverse-of-madness/27EiqSW4jIyH/";
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));
        if(tabName.equalsIgnoreCase("suggested")){
            tabName = "related";
        }
        launchDeeplink(true, deeplinkFormat + tabName.toLowerCase(), 10);
        homePage.clickOpenButton();
    }

    protected ArrayList<String> getTabname() {
        ArrayList<String> contentList = new ArrayList<>();
        contentList.add("Suggested");
        contentList.add("Extras");
        contentList.add("Versions");
        contentList.add("Details");
        return contentList;
    }
}
