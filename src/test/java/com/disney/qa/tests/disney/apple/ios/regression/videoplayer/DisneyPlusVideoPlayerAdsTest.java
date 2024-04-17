package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase.PlayerControl;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class DisneyPlusVideoPlayerAdsTest extends DisneyBaseTest {

    //Test constants
    private static final String SPIDERMAN_THREE = "SpiderMan 3";
    private static final double PLAYER_PERCENTAGE_FOR_RANDOM_MOVING = 10;

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73630"})
    @Test(description = "VOD Player - Ads - Restart - Restart Button inactive during Pre-Roll Ad", groups = {"VideoPlayerAds", TestGroup.PRE_CONFIGURATION})
    public void verifyRestartButtonInActiveWhilePlayingAd() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        loginAndStartPlayback(SPIDERMAN_THREE);
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Ad is not playing");
        sa.assertTrue(videoPlayer.isElementPresent(PlayerControl.RESTART), "Restart button is not visible on ad player overlay");
        sa.assertTrue(videoPlayer.getRestartButtonStatus().equals(FALSE), "Restart button is clickable and not disabled on ad player overlay");
        videoPlayer.waitForAdToComplete(100, 5);
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(PLAYER_PERCENTAGE_FOR_RANDOM_MOVING);
        sa.assertTrue(videoPlayer.getRestartButtonStatus().equals(TRUE), "Restart button is not enabled on video player");
        videoPlayer.clickRestartButton();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isAdBadgeLabelPresent(), "Restart button not worked");
        sa.assertAll();
    }

    private void loginAndStartPlayback(String content) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(getAccountApi().createAccount(getAccountApi().lookupOfferToUse(getCountry(), BUNDLE_BASIC),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(content);
        List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
        results.get(0).click();
        detailsPage.clickPlayButton().waitForVideoToStart().isOpened();
    }
}
