package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusHulkVideoPlayerTest extends DisneyBaseTest {

    static final String NETWORK = "FX";
    static final String NETWORK_CONTENT = "Pose";
    static final int SPLIT_TIME = 15;
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player didn't open";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page didn't open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74451"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluVideoPlayerNetworkWatermark() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(NETWORK_CONTENT);
        searchPage.getDisplayedTitles().get(0).click();

        detailsPage.waitForPresenceOfAnElement(detailsPage.getPlayButton());
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton().isOpened();

        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK), String.format("Network (%s) Watermark logo is not present", NETWORK));
        int maxDelay = videoPlayer.getRemainingTimeThreeIntegers() / 100;
        videoPlayer.scrubToPlaybackPercentage(50);

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK), String.format("Network (%s) Watermark logo is not present", NETWORK));
        pause(maxDelay);
        sa.assertTrue(videoPlayer.isNetworkWatermarkIsNotLogoPresent(NETWORK), String.format("Network (%s) Watermark logo is present", NETWORK));

        videoPlayer.clickBackButton();

        detailsPage.waitForPresenceOfAnElement(detailsPage.getContinueButton());
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        detailsPage.clickOnHuluContinueButton();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK), String.format("Network (%s) Watermark logo is not present", NETWORK));

        videoPlayer.tapAudioSubtitleMenu();
        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");

        subtitlePage.chooseSubtitlesLanguage("English");
        subtitlePage.tapCloseButton();

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK), String.format("Network (%s) Watermark logo is not present", NETWORK));

        videoPlayer.tapForwardButton(2);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK), String.format("Network (%s) Watermark logo is not present", NETWORK));

        videoPlayer.tapRewindButton(2);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK), String.format("Network (%s) Watermark logo is not present", NETWORK));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74454"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluVideoPlayerNetworkWatermarkUserInterruptedSkipFFRW() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAccount(createAccountWithSku(
                DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(NETWORK_CONTENT);
        searchPage.getDisplayedTitles().get(0).click();

        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton().isOpened();

        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.skipPromoIfPresent();

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK),
                String.format("Network (%s) Watermark logo is not present", NETWORK));
        int maxDelay = videoPlayer.getRemainingTimeThreeIntegers() / 100;

        videoPlayer.tapPlayerScreen(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.FAST_FORWARD, 2);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK),
                String.format("Network (%s) Watermark logo is not present after forward the video", NETWORK));
        pause(SPLIT_TIME);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK),
                String.format("Network (%s) Watermark logo is not present after forward the video", NETWORK));
        pause(maxDelay);
        sa.assertTrue(videoPlayer.isNetworkWatermarkIsNotLogoPresent(NETWORK),
                String.format("Network (%s) Watermark logo is present after forward the video", NETWORK));

        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not closed");
        detailsPage.clickContinueButton();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

        videoPlayer.tapPlayerScreen(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.REWIND, 2);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK),
                String.format("Network (%s) Watermark logo is not present after forward the video", NETWORK));
        pause(SPLIT_TIME);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(NETWORK),
                String.format("Network (%s) Watermark logo is not present after forward the video", NETWORK));
        pause(maxDelay);
        sa.assertTrue(videoPlayer.isNetworkWatermarkIsNotLogoPresent(NETWORK),
                String.format("Network (%s) Watermark logo is present after rewind the video", NETWORK));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74452"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluVideoPlayerNetworkWatermarkAutoInterrupted() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        String contentNetwork = "CBS";

        setAccount(createAccountWithSku(
                DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_survivor_episode_playback"));
        Assert.assertTrue(videoPlayer.getSkipRecapButton().isPresent(),
                "Skip Recap button is not present");
        Assert.assertFalse(videoPlayer.getNetworkWatermarkLogo(contentNetwork).isElementPresent(1),
                String.format("Network (%s) Watermark logo is present at the same time that Skip Recap button",
                        contentNetwork));
        videoPlayer.getSkipRecapButton().click();

        Assert.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(contentNetwork),
                String.format("Network (%s) Watermark logo is not present after skipping recap", contentNetwork));
        Assert.assertTrue(videoPlayer.getContentRatingInfoView().isPresent(),
                "Content rating info view is not present after skipping recap");
    }
}
