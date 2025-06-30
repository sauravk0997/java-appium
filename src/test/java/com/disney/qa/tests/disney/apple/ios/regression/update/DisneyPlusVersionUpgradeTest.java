package com.disney.qa.tests.disney.apple.ios.regression.update;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.api.client.requests.CreateUnifiedAccountProfileRequest;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.common.utils.helpers.IAPIHelper;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Listeners;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusVersionUpgradeTest extends DisneyBaseTest {

    String APP_URL = "appcenter://Disney-Prod-Enterprise/ios/enterprise/%s";
    private static final String FORCE_UPDATE_ERROR = "Force Update Error";

    /* This test case installs the previous FC build (appPreviousFCVersion) that was tested previous the current version
       and upgrades against the latest FC approved (appCurrentFCVersion) it is in the current FC XML
     */
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77606"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE, US})
    public void verifyAppUpgrade() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = new DisneyPlusVideoPlayerIOSPageBase(getDriver());
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        String appLatestRCAppStoreVersion =  R.TESTDATA.get("disney_app_latest_rc_app_store_version");

        // Install latest RC build used on App Store and log in
        installPreviousVersionTestFairyApp();
        terminateApp(sessionBundles.get(DISNEY));
        launchApp(sessionBundles.get(DISNEY));
        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        moreMenu.clickMoreTab();
        // Assert that version installed it is the latest RC build used on App Store
        Assert.assertTrue(moreMenu.isAppVersionDisplayed(), "App Version was not displayed");
        Assert.assertEquals(moreMenu.getAppVersion(), appLatestRCAppStoreVersion,
                "Current displayed version doesn't match with latest RC App Store version");

        // Terminate app and upgrade application to latest unreleased build
        terminateApp(sessionBundles.get(DISNEY));
        installApp(sessionBundles.get(APP));
        startApp(sessionBundles.get(DISNEY));
        //Handle ATT Modal
        handleGenericPopup(5,1);
        moreMenu.clickMoreTab();
        // Verify version corresponds to current latest unreleased build
        Assert.assertTrue(moreMenu.isAppVersionDisplayed(), "App Version was not displayed");
        Assert.assertEquals(moreMenu.getAppVersion(), formatAppVersion(IAPIHelper.TEST_FAIRY_APP_VERSION),
                "Current displayed version doesn't match with latest unreleased build version");
        // Verify edit profile option of user
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getUnifiedAccount().getFirstName());
        editProfile.toggleAutoplayButton("OFF");
        Assert.assertTrue(editProfile.isUpdatedToastPresent(), "'Updated' toast was not present");
        editProfile.waitForUpdatedToastToDisappear();
        editProfile.clickDoneBtn();

        // Verify navigation options and series play video
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DisneyEntityIds.WANDA_VISION.getTitle());
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), "Details page did not open");
        Assert.assertTrue(detailsPage.isSeriesDownloadButtonPresent("1","1"),
                "Series episode download button is not displayed");
        detailsPage.clickPlayButton(SHORT_TIMEOUT);
        Assert.assertTrue(videoPlayer.isOpened(), "Video player Page did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-83154"})
    @Test(groups = {TestGroup.UPGRADE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadANdProfilesAfterAppUpgrade() {
        int polling = 10;
        int timeout = 300;
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = new DisneyPlusVideoPlayerIOSPageBase(getDriver());

        // Install latest RC build used on App Store and log in
        installPreviousVersionTestFairyApp();
        terminateApp(sessionBundles.get(DISNEY));
        launchApp(sessionBundles.get(DISNEY));
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true).build());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true).build());
        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        moreMenu.clickMoreTab();
        Assert.assertTrue(moreMenu.isProfileSwitchDisplayed(DEFAULT_PROFILE),
                "Default profile is not displayed");
        Assert.assertTrue(moreMenu.isProfileSwitchDisplayed(SECONDARY_PROFILE),
                "Secondary profile is not displayed");
        Assert.assertTrue(moreMenu.isProfileSwitchDisplayed(KIDS_PROFILE),
                "Kids profile is not displayed");

        launchDeeplink(DEEPLINKURL + DisneyEntityIds.MARVELS.getEntityId());
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        String movieTitle = detailsPage.getMediaTitle();
        detailsPage.getMovieDownloadButton().click();
        detailsPage.waitForMovieDownloadComplete(timeout, polling);
        detailsPage.clickDownloadsIcon();
        Assert.assertTrue(downloadsPage.isOpened(), DOWNLOADS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(downloadsPage.getStaticTextByLabelContains(movieTitle).isPresent(),
                String.format("Movie title '%s' is not found for downloaded asset", movieTitle));

        // Terminate app and upgrade application to latest unreleased build
        terminateApp(sessionBundles.get(DISNEY));
        installApp(sessionBundles.get(APP));
        startApp(sessionBundles.get(DISNEY));
        handleGenericPopup(5, 1);
        moreMenu.clickMoreTab();
        Assert.assertTrue(moreMenu.isProfileSwitchDisplayed(DEFAULT_PROFILE),
                "Default profile is not displayed");
        Assert.assertTrue(moreMenu.isProfileSwitchDisplayed(SECONDARY_PROFILE),
                "Secondary profile is not displayed");
        Assert.assertTrue(moreMenu.isProfileSwitchDisplayed(KIDS_PROFILE),
                "Kids profile is not displayed");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloadsPage.isOpened(), DOWNLOADS_PAGE_NOT_DISPLAYED);

        Assert.assertTrue(downloadsPage.isOpened(),
                DOWNLOADS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(downloadsPage.getStaticTextByLabelContains(movieTitle).isPresent(),
                String.format("Movie title '%s' is not found for downloaded asset", movieTitle));

        downloadsPage.getDownloadedAssetImage(movieTitle).click();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);

        Assert.assertTrue(videoPlayer.getTitleLabel().equals(movieTitle),
                "Downloaded content is not playing on player");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67615"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.UPGRADE, US})
    public void verifyHardForcedUpdateWhileLoggedOut() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        AppStorePageBase appStorePageBase = initPage(AppStorePageBase.class);

        enableHardForceUpdateInJarvis();
        Assert.assertTrue(welcomePage.isForceAppUpdateTitlePresent(),
                FORCE_UPDATE_ERROR + " Title not found");
        Assert.assertTrue(welcomePage.isForceAppUpdateMessagePresent(),
                FORCE_UPDATE_ERROR + " message not found");
        Assert.assertTrue(welcomePage.isForceAppUpdateButtonPresent(),
                FORCE_UPDATE_ERROR + " button not found");

        welcomePage.clickForceUpdateTitle();
        Assert.assertTrue(welcomePage.isForceAppUpdateTitlePresent(),
                FORCE_UPDATE_ERROR + " Title not found after clicking on model");

        welcomePage.clickDefaultAlertBtn();

        appStorePageBase.dismissOnboardingScreenIfPresent();
        appStorePageBase.dismissPersonalisedAdsScreenIfPresent();
        Assert.assertTrue(appStorePageBase.isAppStoreAppOpen(), "AppStore App not open");
        Assert.assertTrue(appStorePageBase.getAppStoreAppScreenTitle().contains("Disney"),
                "AppStore is not opened to Disney+");

        relaunch();
        Assert.assertTrue(welcomePage.isForceAppUpdateTitlePresent(),
                FORCE_UPDATE_ERROR + " Title not found");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73777"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.UPGRADE, US})
    public void verifyHardForcedUpdateWhileLoggedIn() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        AppStorePageBase appStorePageBase = initPage(AppStorePageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), "Home page did not open");

        enableHardForceUpdateInJarvis();
        Assert.assertTrue(welcomePage.isForceAppUpdateTitlePresent(),
                FORCE_UPDATE_ERROR + " Title not found");
        Assert.assertTrue(welcomePage.isForceAppUpdateMessagePresent(),
                FORCE_UPDATE_ERROR + " message not found");
        Assert.assertTrue(welcomePage.isForceAppUpdateButtonPresent(),
                FORCE_UPDATE_ERROR + " button not found");

        welcomePage.clickForceUpdateTitle();
        Assert.assertTrue(welcomePage.isForceAppUpdateTitlePresent(),
                FORCE_UPDATE_ERROR + " Title not found after clicking on model");

        welcomePage.clickDefaultAlertBtn();

        appStorePageBase.dismissOnboardingScreenIfPresent();
        appStorePageBase.dismissPersonalisedAdsScreenIfPresent();
        Assert.assertTrue(appStorePageBase.isAppStoreAppOpen(), "AppStore App not open");
        Assert.assertTrue(appStorePageBase.getAppStoreAppScreenTitle().contains("Disney"),
                "AppStore is not opened to Disney+");

        relaunch();
        Assert.assertTrue(welcomePage.isForceAppUpdateTitlePresent(),
                FORCE_UPDATE_ERROR + " Title not found");
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72536"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.UPGRADE, US})
    public void verifySoftUpdate() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        String notNow = "NOT NOW";
        enableJarvisSoftUpdate();
        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(welcomePage.isForceAppUpdateTitlePresent(), "Soft update device banner is not present");
        Assert.assertTrue(welcomePage.isForceAppUpdateButtonPresent(), "Update button is not present");
        Assert.assertTrue(welcomePage.getTypeButtonContainsLabel(notNow).isPresent(), "Not now button is not present");
    }

    private void installPreviousVersionTestFairyApp() {
        String appPreviousFCVersionUrl =  R.CONFIG.get("test_fairy_latest_app_store_rc_url");
        if (appPreviousFCVersionUrl.isEmpty()) {
            throw new RuntimeException("TEST FAIRY CONFIG test_fairy_latest_app_store_rc_url IS MISSING!");
        }
        installApp(appPreviousFCVersionUrl);
    }

    private String formatAppVersion(String appVersion) {
        int index = appVersion.indexOf("-");
        return String.join("+", appVersion.substring(0, index), appVersion.substring(index + 1));
    }

    @AfterMethod(alwaysRun = true)
    public void removeJarvisApp(){
        boolean isInstalled = isAppInstalled(sessionBundles.get(JarvisAppleBase.JARVIS));
        if(isInstalled){
            removeJarvis();
        }
    }
}
