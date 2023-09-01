package com.disney.qa.tests.disney.apple.ios.regression.details;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class DisneyPlusDetailsTest extends DisneyBaseTest {

    private static final String SECRET_INVASION = "Secret Invasion";
    private static final String WORLDS_BEST = "World's Best";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61847"})
    @Test(description = "Series/Movies Detail Page > User taps add to watchlist", groups = {"Details"})
    public void verifyAddSeriesAndMovieToWatchlist() {
        initialSetup();
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());

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

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62616"})
    @Test(description = "Series/Movies Detail Page > User taps on Suggested tab", groups = {"Details"})
    public void verifyNavigateToSuggestedTab() {
        initialSetup();
        DisneyPlusHomeIOSPageBase home = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());

        //series
        home.clickSearchIcon();
        search.searchForMedia(SECRET_INVASION);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        sa.assertTrue(details.isSuggestedTabPresent(), "Suggested tab was not found on details page");
        details.compareSuggestedTitleToMediaTitle(sa);

        //movie
        home.clickSearchIcon();
        search.clearText();
        search.searchForMedia(WORLDS_BEST);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        sa.assertTrue(details.isSuggestedTabPresent(), "Suggested tab was not found on details page");
        details.compareSuggestedTitleToMediaTitle(sa);
        sa.assertAll();
    }
}
