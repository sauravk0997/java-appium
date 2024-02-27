package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
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

public class DisneyPlusDetailsTest extends DisneyBaseTest {

    private static final String THE_LION_KINGS_TIMON_AND_PUUMBA = "The Lion King Timon Pumbaa";
    private static final String DUMBO = "Dumbo";

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
}
