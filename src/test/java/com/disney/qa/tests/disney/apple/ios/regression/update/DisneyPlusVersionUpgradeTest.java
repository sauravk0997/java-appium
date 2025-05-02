package com.disney.qa.tests.disney.apple.ios.regression.update;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.appcenter.AppCenterManager;
import org.testng.annotations.Listeners;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.disney.qa.common.constant.IConstantHelper.*;

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
        String appPreviousFCVersion =  R.TESTDATA.get("disney_app_previous_fc_version");

        // Install previous FC Version and log in
        installAppCenterApp(appPreviousFCVersion);
        terminateApp(sessionBundles.get(DISNEY));
        launchApp(sessionBundles.get(DISNEY));
        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        moreMenu.clickMoreTab();
        // Assert that version installed it is the previous FC Version
        Assert.assertTrue(moreMenu.isAppVersionDisplayed(), "App Version was not displayed");
        Assert.assertEquals(moreMenu.getAppVersion(), appPreviousFCVersion, "Version is not the previous expected");

        // Terminate app and upgrade application to current version
        terminateApp(sessionBundles.get(DISNEY));
        installApp(TEST_FAIRY_URL.get()); // temp solution until TestFairy integration is complete
        startApp(sessionBundles.get(DISNEY));
        //Handle ATT Modal
        handleGenericPopup(5,1);
        moreMenu.clickMoreTab();
        // Verify version is current FC Version
        Assert.assertTrue(moreMenu.isAppVersionDisplayed(), "App Version was not displayed");
        Assert.assertEquals(moreMenu.getAppVersion(), formatAppVersion(TEST_FAIRY_APP_VERSION.get()),
                "Version is not the current expected");
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

    private void installTestFairyApp(String url) {
        installApp(url);
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
