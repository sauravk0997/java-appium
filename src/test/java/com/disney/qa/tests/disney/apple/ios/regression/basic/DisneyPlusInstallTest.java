package com.disney.qa.tests.disney.apple.ios.regression.basic;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusInstallTest extends DisneyBaseTest {
    ThreadLocal<String> oldAppVersion = new ThreadLocal<>();
    private static final String KIDS_SHORT_SERIES = "Bluey";
    private static final String ADULTS_SHORT_MOVIE = "Purl";
    private static final String KIDS = "KIDS";
    private static final String TEST = "Test";
    private static final String MOVIES = "Movies";
    private static final String SERIES = "Series";
    private static final String ORIGINALS = "Originals";
    private static final String KIDS_DOB = "2018-01-01";

    //TODO: Refactor this test to support AdHoc builds
    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72882"})
    @Test(description = "Old app to new app installation with login validation", groups = {"Install"})
    public void testOldAppToNewAppInstallBVT() {
        initialSetup();
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originals = initPage(DisneyPlusOriginalsIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        oldAppVersion.set(R.CONFIG.get("custom_string2"));
        disneyAccountApi.get().addProfile(disneyAccount.get(), KIDS_PROFILE, KIDS_DOB, disneyAccount.get().getProfileLang(), null, true, true);

        //install old app
        removeApp(buildType.getDisneyBundle());
        installOldApp(oldAppVersion.get());
        relaunch();
        setAppToHomeScreen(disneyAccount.get());
        sa.assertTrue(whoIsWatching.isOpened(), "Who Is Watching Page not displayed");

        whoIsWatching.clickProfile(TEST);
        sa.assertTrue(homePage.isOpened(), "Home screen not displayed");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isProfileSwitchDisplayed(TEST), TEST + " Profile not found on Profile Switch display.");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenuPage.getAppVersionText().contains(oldAppVersion.get()),
                "Current app version found does not match expected old app version");

        //install new app
        installLatestApp();
        relaunch();
        sa.assertTrue(homePage.isOpened(), "Home screen not displayed");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isProfileSwitchDisplayed(TEST), TEST + " Profile not found on Profile Switch display.");

        homePage.clickSearchIcon();
        sa.assertTrue(search.isOpened(), "Search screen not displayed");

        search.getTypeCellLabelContains(ORIGINALS).click();
        sa.assertTrue(originals.isOriginalPageLoadPresent(), "Original screen not display");

        search.getBackArrow().click();
        search.getTypeCellLabelContains(MOVIES).click();
        sa.assertTrue(search.getStaticTextByLabel(MOVIES).isElementPresent(), "Movies screen not displayed");

        search.getBackArrow().click();
        search.getTypeCellLabelContains(SERIES).click();
        sa.assertTrue(search.getStaticTextByLabel(SERIES).isElementPresent(), "Series screen not displayed");

        search.getBackArrow().click();
        search.searchForMedia(KIDS_SHORT_SERIES);
        search.getDisplayedTitles().get(0).click();
        sa.assertTrue(details.isOpened(), "Series detail screen not displayed");

        details.addToWatchlist();
        details.startDownload();
        details.waitForSeriesDownloadToComplete();
        details.clickPlayButton().isOpened();
        sa.assertTrue(videoPlayer.isOpened(), "Video player not displayed");

        videoPlayer.clickBackButton();
        details.dismissNotificationsPopUp(); //notifications pop-up appears after exit player
        homePage.clickDownloadsIcon();
        sa.assertTrue(downloads.isDownloadsDisplayed(), "Download is not displayed.");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isOpened(), "More Menu is not displayed");

        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(moreMenu.areWatchlistTitlesDisplayed(KIDS_SHORT_SERIES),
                "Short Series not found on Watchlist");

        moreMenu.getBackArrow().click();
        whoIsWatching.clickProfile(KIDS);
        pause(1); //to handle transition
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isProfileSwitchDisplayed(KIDS), KIDS + " Profile not found on Profile Switch display.");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertFalse(moreMenuPage.getAppVersionText().contains(oldAppVersion.get()),
                "Old app version and new app version are the same");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73477"})
    @Test(description = "Old app with kids download to new app installation", groups = {"Install"})
    public void testOldAppToNewAppInstallDownloadKids() {
        initialSetup();
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        oldAppVersion.set(R.CONFIG.get("custom_string2"));
        disneyAccountApi.get().addProfile(disneyAccount.get(), KIDS_PROFILE, KIDS_DOB, disneyAccount.get().getProfileLang(), null, true, true);

        //install old app
        removeApp(buildType.getDisneyBundle());
        installOldApp(oldAppVersion.get());
        relaunch();
        setAppToHomeScreen(disneyAccount.get());
        sa.assertTrue(whoIsWatching.isOpened(), "Who Is Watching Page not displayed");

        whoIsWatching.clickProfile(TEST);
        sa.assertTrue(homePage.isOpened(), "Home screen not displayed");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isProfileSwitchDisplayed(TEST), TEST + " Profile not found on Profile Switch display.");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenuPage.getAppVersionText().contains(oldAppVersion.get()),
                "Current app version found does not match expected old app version");

        homePage.clickSearchIcon();
        search.searchForMedia(KIDS_SHORT_SERIES);
        search.getDisplayedTitles().get(0).click();
        sa.assertTrue(details.isOpened(), "Series detail screen not displayed");

        details.startDownload();
        details.waitForSeriesDownloadToComplete();
        homePage.clickDownloadsIcon();
        sa.assertTrue(downloads.isDownloadsDisplayed(), "Download is not displayed.");

        downloads.tapDownloadedAssetFromListView(KIDS_SHORT_SERIES);
        downloads.tapDownloadedAsset(KIDS_SHORT_SERIES);
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player not displayed");
        videoPlayer.clickBackButton();

        //install new app
        installLatestApp();
        relaunch();
        sa.assertTrue(homePage.isOpened(), "Home screen not displayed");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isProfileSwitchDisplayed(KIDS), KIDS + " Profile not found on Profile Switch display.");

        homePage.clickDownloadsIcon();
        sa.assertTrue(downloads.isDownloadsDisplayed(), "Download is not displayed on Kids profile.");

        downloads.tapDownloadedAssetFromListView(KIDS_SHORT_SERIES);
        downloads.tapDownloadedAsset(KIDS_SHORT_SERIES);
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player not displayed");

        videoPlayer.clickBackButton();
        details.dismissNotificationsPopUp(); //notifications pop-up appears after exit player
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isOpened(), "More Menu is not displayed");
        sa.assertFalse(moreMenuPage.getAppVersionText().contains(oldAppVersion.get()),
                "Old app version and new app version are the same");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73476"})
    @Test(description = "Old app with adult download to new app install", groups = {"Install"})
    public void testOldAppToNewAppInstallDownloadAdult() {
        initialSetup();
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        disneyAccountApi.get().addProfile(disneyAccount.get(), KIDS_PROFILE, KIDS_DOB, disneyAccount.get().getProfileLang(), null, true, true);

        oldAppVersion.set(R.CONFIG.get("custom_string2"));
        String appCenterAppName = R.CONFIG.get("capabilities.app");
        boolean isEnterpriseBuild = appCenterAppName.contains("Enterprise");

        //install old app
        removeApp(buildType.getDisneyBundle());
        installOldApp(oldAppVersion.get());
        relaunch();
        setAppToHomeScreen(disneyAccount.get());
        sa.assertTrue(whoIsWatching.isOpened(), "Who Is Watching Page not displayed");

        whoIsWatching.clickProfile(TEST);
        sa.assertTrue(homePage.isOpened(), "Home screen not displayed");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isProfileSwitchDisplayed(TEST), TEST + " Profile not found on Profile Switch display.");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenuPage.getAppVersionText().contains(oldAppVersion.get()),
                "Current app version found does not match expected old app version");

        homePage.clickSearchIcon();
        search.searchForMedia(ADULTS_SHORT_MOVIE);
        search.getDisplayedTitles().get(0).click();
        sa.assertTrue(details.isOpened(), "Series detail screen not displayed");

        details.startDownload();
        details.waitForMovieDownloadComplete();
        homePage.clickDownloadsIcon();
        sa.assertTrue(downloads.isDownloadsDisplayed(), "Download is not displayed.");

        downloads.tapDownloadedAssetFromListView(ADULTS_SHORT_MOVIE);
        downloads.tapDownloadedAsset(ADULTS_SHORT_MOVIE);
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player not displayed");
        videoPlayer.clickBackButton();

        //install new app
        installLatestApp();
        relaunch();
        sa.assertTrue(homePage.isOpened(), "Home screen not displayed");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isProfileSwitchDisplayed(TEST), TEST + " Profile not found on Profile Switch display.");

        homePage.clickDownloadsIcon();
        sa.assertTrue(downloads.isDownloadsDisplayed(), "Download is not displayed.");

        downloads.tapDownloadedAssetFromListView(ADULTS_SHORT_MOVIE);
        downloads.tapDownloadedAsset(ADULTS_SHORT_MOVIE);
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player not displayed");

        videoPlayer.clickBackButton();
        details.dismissNotificationsPopUp(); //notifications pop-up appears after exit player
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isOpened(), "More Menu is not displayed");
        sa.assertFalse(moreMenuPage.getAppVersionText().contains(oldAppVersion.get()),
                "Old app version and new app version are the same");

        whoIsWatching.clickProfile(KIDS);
        sa.assertTrue(homePage.isOpened(), "Home screen not displayed");

        homePage.clickDownloadsIcon();
        sa.assertTrue(downloads.isDownloadsEmptyHeaderPresent(), "Non-kids content found on kids downloads list");
        sa.assertAll();
    }
}
