package com.disney.qa.tests.disney.apple.ios.regression.basic;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.utils.IOSUtils.DEVICE_TYPE;

public class DisneyPlusiOSBVT extends DisneyBaseTest {
    //Test constants
    private static final String  SHORT_SERIES = "Bluey";
    private static final String TEST = "Test";
    private static final String MOVIES = "Movies";
    private static final String SERIES = "Series";
    private static final String KIDS_DOB = "2018-01-01";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72582"})
    @Test(description = "iOS Basic Verification Test", groups = {"BVT"})
    public void iOSBVT() {
        setGlobalVariables();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originals = initPage(DisneyPlusOriginalsIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        IOSUtils utils = new IOSUtils();
        SoftAssert sa = new SoftAssert();
        disneyAccountApi.get().addProfile(disneyAccount.get(), KIDS_PROFILE, KIDS_DOB, disneyAccount.get().getProfileLang(), null, true, true);

        setAppToHomeScreen(disneyAccount.get());
        sa.assertTrue(whoIsWatching.isOpened());
        whoIsWatching.clickProfile(TEST);
        sa.assertTrue(homePage.isOpened(), "Home screen not displayed");

        homePage.clickSearchIcon();
        sa.assertTrue(search.isOpened(), "Search screen not displayed");

        search.clickOriginalsTab();
        pause(1); //handle transition to originals
        sa.assertTrue(originals.isOriginalPageLoadPresent(), "Original screen not display");

        search.getBackArrow().click();
        search.clickMoviesTab();
        sa.assertTrue(search.getStaticTextByLabel(MOVIES).isElementPresent(), "Movies screen not displayed");

        search.getBackArrow().click();
        search.clickSeriesTab();
        sa.assertTrue(search.getStaticTextByLabel(SERIES).isElementPresent(), "Series screen not displayed");

        search.getBackArrow().click();
        search.searchForMedia(SHORT_SERIES);
        search.getDisplayedTitles().get(0).click();
        sa.assertTrue(details.isOpened(), "Series detail screen not displayed");

        details.addToWatchlist();
        details.startDownload();
        details.waitForDownloadToComplete();
        details.clickPlayButton().isOpened();
        sa.assertTrue(videoPlayer.isOpened(), "Video player not displayed");

        videoPlayer.clickBackButton();
        homePage.clickDownloadsIcon();
        sa.assertTrue(downloads.isDownloadsDisplayed(), "Downloads is not displayed.");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isOpened(), "More Menu not displayed");

        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(moreMenu.areWatchlistTitlesDisplayed(SHORT_SERIES), "Short Series not found on Watchlist");

        moreMenu.getBackArrow().click();
        whoIsWatching.clickProfile(KIDS_PROFILE);
        pause(1); //to handle transition
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            LOGGER.info("Scrolling down to view all of 'Information and choices about your profile'");
            utils.scrollDown();
        }
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isExitKidsProfileButtonPresent(), "'Exit Kid's Profile' button not enabled.");
        sa.assertAll();
    }
}
