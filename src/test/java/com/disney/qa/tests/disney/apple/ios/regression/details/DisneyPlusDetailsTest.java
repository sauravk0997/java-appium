package com.disney.qa.tests.disney.apple.ios.regression.details;

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
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;

public class DisneyPlusDetailsTest extends DisneyBaseTest {

    private static final String THE_LION_KINGS_TIMON_AND_PUUMBA = "The Lion King Timon Pumbaa";
    private static final String HIGH_SCHOOL_MUSICAL = "High School Musical: The Musical: The Series";
    private static final String HOCUS_POCUS = "Hocus Pocus";
    private static final String DUMBO = "Dumbo";
    private static final String THE_ARISTOCATS = "The aristocats";
    private static final String TV_Y7 = "TV-Y7";
    private static final String SPIDERMAN_THREE = "SpiderMan 3";
    private static final String ASHOKA = "Ashoka";
    private static final String SHOP = "Shop";
    private static final double PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT = 50;

    @DataProvider(name = "disneyPlanTypes")
    public Object[][] disneyWebPlanTypes() {
        return new Object[][]{{"Disney+ With Ads, Hulu with Ads, and ESPN+"},
                {"Disney+, Hulu No Ads, and ESPN+"}
        };
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61847"})
    @Test(description = "Series/Movies Detail Page > User taps add to watchlist", groups = {"Details", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71125"})
    @Test(description = "Details Page - IMAX Enhanced - Badges", groups = {"Details", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("mparra5")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62360"})
    @Test(description = "Series/Movies Detail Page > Negative Stereotype Advisory Expansion", groups = {"Details", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("mparra5")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61309"})
    @Test(description = "Maturity Rating Restriction on Detail Page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyMaturityRatingRestrictionOnDetailPage() {
        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(getAccount(), TV_Y7, KIDS_DOB, getAccount().getProfileLang(), RAYA, false, true);
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

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71123"})
    @Test(description = "Details Page - IMAX Enhanced - Versions Tab", groups = {"Details", TestGroup.PRE_CONFIGURATION})
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
        if(R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            searchPage.clickContentPageFilterDropDown();
            swipe(searchPage.getStaticTextByLabel(filterValue));
            searchPage.getStaticTextByLabel(filterValue).click();
        }else{
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
        int duration = getSearchApi().getMovie(title, getAccount()).getContentDuration();
        long hours = TimeUnit.MILLISECONDS.toHours(duration) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        String durationTime = String.format("%dh %dm",hours, minutes);
        sa.assertTrue(detailsPage.getMovieNameAndDurationFromIMAXEnhancedHeader().equals(title+ " "+ durationTime), "Content name and duration was not found in IMAX Enhanced Header");
        sa.assertTrue(detailsPage.getMovieNameAndDurationFromIMAXEnhancedHeader().endsWith(durationTime), "Duration details not found at the end of IMAX Enhanced Header");
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72032"})
    @Test(description = "Details Page - IMAX Enhanced - Deeplink to Details Screen", groups = {"Details", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void navigatIMAXEnhancedDetailsPagefromDeeplink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        IntStream.range(0, getTabname().size()).forEach(i -> {
            navigateToIMAXEnhancedDetailPageFromDeeplink(getTabname().get(i));
            detailsPage.dismissNotificationsPopUp();
            Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
            sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
            sa.assertTrue(detailsPage.isImaxEnhancedPromoLabelPresent(), "IMAX Enhanced Promo Label was not found");
            sa.assertTrue(detailsPage.isImaxEnhancedPresentInMediaFeaturesRow(),"IMAX Enhanced Badge was not found in media features row");
            if(R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
                swipeUp(1500);
            }
            sa.assertTrue(detailsPage.isTabSelected(getTabname().get(i).toUpperCase()),getTabname().get(i) + "Tab was not selected");
        });
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72725"})
    @Test(description = "Details Page - ShopDisney - Feature Area of Details Page", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifyShopPromoLabelInFeatureAreaOfDetailPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), "Search page did not open");

        //Verify Shop Promo for Series
        vailidateShopPromoLabelHeaderAndSubHeader(sa, ASHOKA);

        //Verify Shop Promo for Movie
        detailsPage.getBackArrow().click();
        vailidateShopPromoLabelHeaderAndSubHeader(sa, SPIDERMAN_THREE);
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72730"})
    @Test(description = "Details Page - ShopDisney - Shop Tab - Primary Profile (Ad Tier & Non Ad Tier)", dataProvider = "disneyPlanTypes", groups = {"Details", TestGroup.PRE_CONFIGURATION})
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
        validateShopTabButton(sa, ASHOKA);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62519"})
    @Test(description = "Details Page - Bookmarks - Visual Progress Bar - Update after user watches content", groups = {"Video Player", TestGroup.PRE_CONFIGURATION })
    @Maintainer("hpatel7")
    public void verifyProgressBarForAfterUserWatchesContent() {
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
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        detailsPage.clickPlayButton().isOpened();
        videoPlayerPage.clickPauseButton();
        videoPlayerPage.scrubToPlaybackPercentage(PLAYER_PERCENTAGE_FOR_EXTRA_UP_NEXT);
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.clickPlayButton();
        videoPlayerPage.clickPauseButton();

        int remainingTimeInMinutes = videoPlayerPage.getRemainingTime();
        videoPlayerPage.clickBackButton();
        long hours = remainingTimeInMinutes/60;
        long minutes = remainingTimeInMinutes %  60;
        String durationTime = String.format("%dh %dm",hours, minutes);

        sa.assertTrue(detailsPage.isOpened(), "Detail Page did not open");
        sa.assertTrue(detailsPage.isContinueButtonPresent(), "Continue button not present after exiting playback");
        sa.assertTrue(detailsPage.isProgressBarPresent(), "Progress bar is not present after exiting playback");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().isPresent(), "Continue watching time remaining is not present");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().getText().contains(durationTime), "Correct remaining time is not reflecting in progress bar");

        detailsPage.clickContinueButton();
        sa.assertTrue(videoPlayerPage.isOpened(), "Video player Page is not opened");
        videoPlayerPage.scrubToPlaybackPercentage(99);
        disneyPlusUpNextIOSPageBase.waitForUpNextUIToAppear();
        videoPlayerPage.clickPauseButton();
        videoPlayerPage.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Detail Page did not open");
        sa.assertFalse(detailsPage.isContinueButtonPresent(), "Continue button present after completing playback");
        sa.assertFalse(detailsPage.isProgressBarPresent(), "Progress bar is present after completing playback");
        sa.assertAll();
    }

    private void vailidateShopPromoLabelHeaderAndSubHeader(SoftAssert sa, String titleName){
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
