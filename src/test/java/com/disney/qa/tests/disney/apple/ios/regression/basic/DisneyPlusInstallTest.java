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
    private static final String  SHORT_SERIES = "Bluey";
    private static final String KIDS = "Kids";
    private static final String TEST = "Test";
    private static final String MOVIES = "Movies";
    private static final String SERIES = "Series";
    private static final String ORIGINALS = "Originals";

    //TODO: Refactor this test to support AdHoc builds
    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72882"})
    @Test(description = "Old app to new app installation with login validation", groups = {"Install"})
    public void testOldAppToNewAppInstallWithLogin() {
        setGlobalVariables();
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
        String appCenterAppName = R.CONFIG.get("capabilities.app");
        boolean isEnterpriseBuild = appCenterAppName.contains("Enterprise");

        disneyAccountApi.get().addProfile(disneyAccount.get(), KIDS, disneyAccount.get().getProfileLang(), null, true);

        //install old app
        removeApp(buildType.getDisneyBundle());
        installOldApp(isEnterpriseBuild, oldAppVersion.get());
        startApp(buildType.getDisneyBundle());
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
        installLatestApp(isEnterpriseBuild);
        startApp(buildType.getDisneyBundle());
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
        search.searchForMedia(SHORT_SERIES);
        search.getDisplayedTitles().get(0).click();
        sa.assertTrue(details.isOpened(), "Series detail screen not displayed");

        details.addToWatchlist();
        details.startDownload();
        details.waitForDownloadToComplete();
        details.clickPlayButton().isOpened();
        sa.assertTrue(videoPlayer.isOpened(), "Video player not displayed");

        videoPlayer.clickBackButton();
        details.dismissNotificationsPopUp(); //notifications pop-up appears after exit player
        homePage.clickDownloadsIcon();
        sa.assertTrue(downloads.isDownloadsDisplayed(), "Download is not displayed.");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertTrue(moreMenu.isOpened(), "More Menu is not displayed");

        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(moreMenu.areWatchlistTitlesDisplayed(SHORT_SERIES),
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
}
