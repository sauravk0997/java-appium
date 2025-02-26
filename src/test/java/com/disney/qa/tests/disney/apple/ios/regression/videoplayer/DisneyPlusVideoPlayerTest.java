package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.ONE_HUNDRED_TWENTY_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusVideoPlayerTest extends DisneyBaseTest {

    private static final int SPLIT_TIME = 15;
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player didn't open";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page didn't open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77674"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadOfESPNContent() {
        String seasonNumber = "1";
        String episodeNumber = "1";
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB));
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_in_the_arena_serena_williams_deeplink"));
        detailsPage.waitForDetailsPageToOpen();
        swipe(detailsPage.getEpisodeToDownload(seasonNumber, episodeNumber), Direction.UP, 1, 900);
        detailsPage.getEpisodeToDownload(seasonNumber, episodeNumber).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(ONE_HUNDRED_TWENTY_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);
        String episodeTitle = detailsPage.getEpisodeCellTitle(seasonNumber, episodeNumber);
        detailsPage.getFirstEpisodeDownloadCompleteButton().click();
        detailsPage.getDownloadModalPlayButton().click();

        Assert.assertTrue(videoPlayer.isOpened(),
                "Video player did not open after choosing a downloaded episode");
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.getSubTitleLabel().contains(episodeTitle),
                "Video player title does not match with expected title: " + episodeTitle);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77676"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNPlusEntitlementAttribution() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB));
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_nfl_turning_point_deeplink"));
        detailsPage.waitForDetailsPageToOpen();
        Assert.assertTrue(detailsPage.getESPNPlusEntitlementAttributionText().isElementPresent(),
                "ESPN+ entitlement attribution is not present on Details page");

        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.getServiceAttributionLabel().isElementPresent(),
                "ESPN+ entitlement attribution is not present on the video player");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77615"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.EODPLUS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNContent() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB));
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_in_the_arena_serena_williams_deeplink"));
        detailsPage.waitForDetailsPageToOpen();

        String contentTitle = detailsPage.getContentTitle();

        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), "Video player did not open");
        Assert.assertEquals(videoPlayer.getTitleLabel(), contentTitle,
                "Expected content did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74451"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluVideoPlayerNetworkWatermark() {
        String network = "FX";
        String networkTitle = "Pose";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(networkTitle);
        searchPage.getDisplayedTitles().get(0).click();

        detailsPage.waitForPresenceOfAnElement(detailsPage.getPlayButton());
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton().isOpened();

        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.waitForVideoToStart();

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network), String.format("Network (%s) Watermark logo is not present", network));
        int maxDelay = videoPlayer.getRemainingTimeThreeIntegers() / 100;
        videoPlayer.scrubToPlaybackPercentage(50);

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network), String.format("Network (%s) Watermark logo is not present", network));
        pause(maxDelay);
        sa.assertTrue(videoPlayer.isNetworkWatermarkIsNotLogoPresent(network), String.format("Network (%s) Watermark logo is present", network));

        videoPlayer.clickBackButton();

        detailsPage.waitForPresenceOfAnElement(detailsPage.getContinueButton());
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        detailsPage.clickOnHuluContinueButton();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network), String.format("Network (%s) Watermark logo is not present", network));

        videoPlayer.tapAudioSubtitleMenu();
        sa.assertTrue(subtitlePage.isOpened(), "Subtitle menu didn't open");

        subtitlePage.chooseSubtitlesLanguage("English");
        subtitlePage.tapCloseButton();

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network), String.format("Network (%s) Watermark logo is not present", network));

        videoPlayer.tapForwardButton(2);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network), String.format("Network (%s) Watermark logo is not present", network));

        videoPlayer.tapRewindButton(2);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network), String.format("Network (%s) Watermark logo is not present", network));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74454"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluVideoPlayerNetworkWatermarkUserInterruptedSkipFFRW() {
        String network = "FX";
        String networkTitle = "Pose";
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
        searchPage.searchForMedia(networkTitle);
        searchPage.getDisplayedTitles().get(0).click();

        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton().isOpened();

        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        videoPlayer.skipPromoIfPresent();

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network),
                String.format("Network (%s) Watermark logo is not present", network));
        int maxDelay = videoPlayer.getRemainingTimeThreeIntegers() / 100;

        videoPlayer.tapPlayerScreen(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.FAST_FORWARD, 2);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network),
                String.format("Network (%s) Watermark logo is not present after forward the video", network));
        pause(SPLIT_TIME);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network),
                String.format("Network (%s) Watermark logo is not present after forward the video", network));
        pause(maxDelay);
        sa.assertTrue(videoPlayer.isNetworkWatermarkIsNotLogoPresent(network),
                String.format("Network (%s) Watermark logo is present after forward the video", network));

        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Video player was not closed");
        detailsPage.clickContinueButton();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

        videoPlayer.tapPlayerScreen(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.REWIND, 2);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network),
                String.format("Network (%s) Watermark logo is not present after forward the video", network));
        pause(SPLIT_TIME);
        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network),
                String.format("Network (%s) Watermark logo is not present after forward the video", network));
        pause(maxDelay);
        sa.assertTrue(videoPlayer.isNetworkWatermarkIsNotLogoPresent(network),
                String.format("Network (%s) Watermark logo is present after rewind the video", network));
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
