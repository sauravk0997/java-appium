package com.disney.qa.tests.disney.apple.ios.regression.basic;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class DisneyPlusiOSBVT extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    //Test constants
    private static final String  SHORT_SERIES = "Bluey";
    private static final String TEST = "Test";
    private static final String MOVIES = "Movies";
    private static final String SERIES = "Series";
    private static final String KIDS_DOB = "2018-01-01";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72582"})
    @Test(description = "iOS Basic Verification Test", groups = {"BVT", TestGroup.PRE_CONFIGURATION})
    public void iOSBVT() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originals = initPage(DisneyPlusOriginalsIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        getAccountApi().addProfile(getAccount(), KIDS_PROFILE, KIDS_DOB, getAccount().getProfileLang(), null, true, true);

        setAppToHomeScreen(getAccount());
        sa.assertTrue(whoIsWatching.isOpened());
        whoIsWatching.clickProfile(TEST);
        sa.assertTrue(homePage.isOpened(), "Home screen not displayed");

        homePage.clickSearchIcon();
        sa.assertTrue(search.isOpened(), "Search screen not displayed");

        search.getDynamicCellByLabel("Originals").click();
        pause(1); //handle transition to originals
        sa.assertTrue(originals.isOriginalPageLoadPresent(), "Original screen not display");

        search.getBackArrow().click();
        search.getDynamicCellByLabel("Movies").click();
        sa.assertTrue(search.getStaticTextByLabel(MOVIES).isElementPresent(), "Movies screen not displayed");

        search.getBackArrow().click();
        search.getDynamicCellByLabel("Series").click();
        sa.assertTrue(search.getStaticTextByLabel(SERIES).isElementPresent(), "Series screen not displayed");

        search.getBackArrow().click();
        search.searchForMedia(SHORT_SERIES);
        search.getDisplayedTitles().get(0).click();
        sa.assertTrue(details.isOpened(), "Series detail screen not displayed");

        details.addToWatchlist();
        details.startDownload();
        details.waitForSeriesDownloadToComplete();
        homePage.clickDownloadsIcon();
        sa.assertTrue(downloads.isDownloadsDisplayed(), "Downloads is not displayed.");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isOpened(), "More Menu not displayed");

        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(moreMenu.areWatchlistTitlesDisplayed(SHORT_SERIES), "Short Series not found on Watchlist");

        moreMenu.clickBackArrowFromWatchlist();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        pause(1); //to handle transition
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            scrollDown();
        }
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isExitKidsProfileButtonPresent(), "'Exit Kid's Profile' button not enabled.");

        moreMenu.tapExitKidsProfileButton();
        whoIsWatching.clickProfile(TEST);
        homePage.isOpened();
        homePage.clickSearchIcon();
        search.searchForMedia(SHORT_SERIES);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
        details.clickPlayButton().isOpened();
        sa.assertTrue(videoPlayer.isOpened(), "Video player not displayed");
        sa.assertAll();
    }
}
