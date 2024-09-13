package com.disney.qa.tests.disney.apple.ios.regression.update;

import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.appcenter.AppCenterManager;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DisneyPlusVersionUpgradeTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67617"})
    @Test(groups = {TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE})
    public void verifyAppUpgrade() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = new DisneyPlusVideoPlayerIOSPageBase(getDriver());
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);

        String APP_LATEST_VERSION =  "latest";

        setAppToHomeScreen(getAccount());
        // Terminate app to update application
        terminateApp(sessionBundles.get(DISNEY));
        // Install latest application
        installApp(AppCenterManager.getInstance()
                .getAppInfo(String.format("appcenter://Disney-Prod-Enterprise/ios/enterprise/%s", APP_LATEST_VERSION))
                .getDirectLink());
        startApp(sessionBundles.get(DISNEY));

        // Verify the edit profile option of user
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getAccount().getFirstName());
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
        Assert.assertTrue(videoPlayer.isOpened(), "Video player Page did not opened");
    }
}
