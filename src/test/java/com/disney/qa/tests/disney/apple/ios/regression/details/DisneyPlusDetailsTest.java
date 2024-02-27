package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.api.client.responses.profile.DisneyProfile;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;

public class DisneyPlusDetailsTest extends DisneyBaseTest {

    private static final String THE_LION_KINGS_TIMON_AND_PUUMBA = "The Lion King Timon Pumbaa";
    private static final String DUMBO = "Dumbo";
    private static final String TV_Y7 = "TV-Y7";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61847"})
    @Test(description = "Series/Movies Detail Page > User taps add to watchlist", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifyAddSeriesAndMovieToWatchlist() {
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        //search movies
        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.clickMoviesTab();
        List<ExtendedWebElement> movies = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        movies.get(0).click();
        String firstMovieTitle = disneyPlusDetailsIOSPageBase.getMediaTitle();
        disneyPlusDetailsIOSPageBase.addToWatchlist();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);

        //search series
        disneyPlusSearchIOSPageBase.clickSeriesTab();
        List<ExtendedWebElement> series = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        series.get(2).click();
        String firstSeriesTitle = initPage(DisneyPlusDetailsIOSPageBase.class).getMediaTitle();
        disneyPlusDetailsIOSPageBase.addToWatchlist();

        //titles added to watchlist
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.areWatchlistTitlesDisplayed(firstSeriesTitle,firstMovieTitle), "Titles were not added to the Watchlist");
        sa.assertAll();
    }

    @Maintainer("hpatel7")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71124"})
    @Test(description = "Details Page - IMAX Enhanced - Promo Labels", groups = {"Details", TestGroup.PRE_CONFIGURATION})
    public void verifyIMAXEnhancedPromoLabels() {
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
        sa.assertTrue(detailsPage.isImaxEnhancedPromoLabelPresent(), "IMAX Enhanced Promo Label was not found");
        sa.assertTrue(detailsPage.isImaxEnhancedPromoSubHeaderPresent(), "IMAX Enhanced Promo sub header was not found");
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
        search.searchForMedia(DUMBO);
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
        getAccountApi().addProfile(getAccount(), TV_Y7, ADULT_DOB, getAccount().getProfileLang(), RAYA, false, true);
        DisneyProfile profile = getAccount().getProfile(TV_Y7);
        getAccountApi().editContentRatingProfileSetting(getAccount(), getAccountApi().getDisneyProfiles(getAccount()).get(1).getProfileId(),
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystem(),
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues().get(1));

        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(1).getProfileName());

        // Movies
        launchDeeplink(true, R.TESTDATA.get("disney_prod_avengers_end_game_deeplink"), 10);
        detailsPage.clickOpenButton();

        sa.assertTrue(detailsPage.getStaticTextByLabelContains("HD").isPresent(), "`HD` video quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("5.1").isPresent(), "`5.1` audio quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Subtitles for the Deaf and Hearing Impaired").isPresent(), "`Subtitles accessibility badge not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Audio Description").isPresent(), "`Audio Description` accessibility badge is not found.");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Metadata label is displayed.");
        sa.assertTrue(detailsPage.getMediaTitle().contains("The Avengers"), "Media title not found.");
        sa.assertTrue(detailsPage.getRatingRestrictionDetailMessage().isPresent(), "Rating Restriction Detail Message not found");

        sa.assertTrue(detailsPage.getBackButton().isPresent(), "Back button is not found.");
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button is not found.");
        sa.assertFalse(detailsPage.getContentDescription().isPresent(SHORT_TIMEOUT), "Content Description found.");

        sa.assertFalse(detailsPage.getPlayButton().isPresent(SHORT_TIMEOUT), "Play CTA found.");
        sa.assertFalse(detailsPage.getWatchlistButton().isPresent(SHORT_TIMEOUT), "Watchlist CTA found.");
        sa.assertFalse(detailsPage.getTrailerButton().isPresent(SHORT_TIMEOUT), "Trailer CTA found.");

        // Series
        launchDeeplink(true, R.TESTDATA.get("disney_prod_echo_deeplink"), 10);
        detailsPage.clickOpenButton();

        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Metadata label is displayed.");
        sa.assertTrue(detailsPage.getMediaTitle().contains("Echo"), "Media title not found.");
        sa.assertTrue(detailsPage.getRatingRestrictionDetailMessage().isPresent(), "Rating Restriction Detail Message not found");

        sa.assertTrue(detailsPage.getBackButton().isPresent(), "Back button is not found.");
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button is not found.");
        sa.assertFalse(detailsPage.getContentDescription().isPresent(SHORT_TIMEOUT), "Content Description found.");

        sa.assertFalse(detailsPage.getEpisodesTab().isPresent(SHORT_TIMEOUT), "Episodes CTA found.");
        sa.assertFalse(detailsPage.getPlayButton().isPresent(SHORT_TIMEOUT), "Play CTA found.");
        sa.assertFalse(detailsPage.getWatchlistButton().isPresent(SHORT_TIMEOUT), "Watchlist CTA found.");
        sa.assertFalse(detailsPage.getTrailerButton().isPresent(SHORT_TIMEOUT), "Trailer CTA found.");

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
}
