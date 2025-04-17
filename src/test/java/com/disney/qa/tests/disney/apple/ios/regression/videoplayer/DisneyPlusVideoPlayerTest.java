package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.ONE_HUNDRED_TWENTY_SEC_TIMEOUT;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusVideoPlayerTest extends DisneyBaseTest {

    private static final int SPLIT_TIME = 15;
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player did not open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77674"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadOfESPNContent() {
        String seasonNumber = "1";
        String episodeNumber = "1";
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

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

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_the_last_dance_deeplink"));
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

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_in_the_arena_serena_williams_deeplink"));
        detailsPage.waitForDetailsPageToOpen();

        String contentTitle = detailsPage.getContentTitle();

        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        Assert.assertEquals(videoPlayer.getTitleLabel(), contentTitle,
                "Expected content did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74451"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluVideoPlayerNetworkWatermark() {
        String network = "FX";
        String networkTitle = "Pose";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusAudioSubtitleIOSPageBase subtitlePage = initPage(DisneyPlusAudioSubtitleIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(networkTitle);
        searchPage.getDisplayedTitles().get(0).click();

        detailsPage.waitForPresenceOfAnElement(detailsPage.getPlayButton());
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();

        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network), String.format("Network (%s) Watermark logo is not present", network));
        int maxDelay = videoPlayer.getRemainingTimeThreeIntegers() / 100;
        videoPlayer.scrubToPlaybackPercentage(50);

        sa.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(network), String.format("Network (%s) Watermark logo is not present", network));
        pause(maxDelay);
        sa.assertTrue(videoPlayer.isNetworkWatermarkIsNotLogoPresent(network), String.format("Network (%s) Watermark logo is present", network));

        videoPlayer.clickBackButton();

        detailsPage.waitForPresenceOfAnElement(detailsPage.getContinueButton());
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        detailsPage.clickOnHuluContinueButton();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);

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
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluVideoPlayerNetworkWatermarkUserInterruptedSkipFFRW() {
        String network = "FX";
        String networkTitle = "Pose";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.clickSearchIcon();
        searchPage.searchForMedia(networkTitle);
        searchPage.getDisplayedTitles().get(0).click();

        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();

        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
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
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);

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
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluVideoPlayerNetworkWatermarkAutoInterrupted() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        String contentNetwork = "CBS";

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77740"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.EODPLUS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNAlternateBroadcastSelector() {
        openBroadcastMenu();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77687"})
    @Test(groups = {TestGroup.EODPLUS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEspnVODNetworkAttribution() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        String espn = "ESPN+";
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("espn_prod_survive_and_advance_documentary_playback"));

        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

        Assert.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(espn), "ESPN Network watermark is not present");
        // Validate right position of espn logo
        validateElementPositionAlignment(videoPlayer.getNetworkWatermarkLogo(espn), RIGHT_POSITION);
        // Validate bottom position of espn logo
        validateElementExpectedHeightPosition(videoPlayer.getNetworkWatermarkLogo(espn), BOTTOM);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77896"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.EODPLUS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNAlternateBroadcastSelectorFeedsOptions() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        openBroadcastMenu();
        Assert.assertTrue(broadcastsExpectedFeeds().containsAll(videoPlayer.getBroadcastTargetFeedOptionText()),
                "Target broadcasts feeds on UI are not as expected");
        verifyFeedOptionSelected();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77895"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.EODPLUS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyESPNAlternateBroadcastSelectorLanguageOptions() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        openBroadcastMenu();
        Assert.assertTrue(videoPlayer.getExpectedBroadcastLanguageOptions()
                        .containsAll(videoPlayer.getBroadcastLanguageOptionText()),
                "Target broadcasts language on UI are not as expected");
        verifyFeedOptionSelected();
    }

    public void openBroadcastMenu() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.waitForHomePageToOpen();

        //NHL collection
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_nhl_replay_deeplink"));

        detailsPage.waitForDetailsPageToOpen();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.displayVideoController();
        videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.BROADCAST_MENU).click();
        Assert.assertTrue(videoPlayer.getBroadcastCollectionView().isPresent(),
                "Broadcast Menu did not open on video player");
    }

    public void verifyFeedOptionSelected() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        String selectedMenuOption = videoPlayer.selectAndGetBroadcastFeedOption();
        LOGGER.info("Feed option selected value - " + selectedMenuOption);
        if (selectedMenuOption != null) {
            videoPlayer.waitForVideoToStart();
            videoPlayer.displayVideoController();
            videoPlayer.getElementFor(DisneyPlusVideoPlayerIOSPageBase.PlayerControl.BROADCAST_MENU).click();
            Assert.assertTrue(videoPlayer.isFeedOptionSelected(selectedMenuOption),
                    "Target feed/Language is not selected");
        } else {
            throw new SkipException("Only One target feed option available, hence skipping feed selection logic");
        }
    }

    private ArrayList<String> broadcastsExpectedFeeds() {
        ArrayList<String> broadcastsExpectedFeeds = new ArrayList<>();
        broadcastsExpectedFeeds.add("PRIMARY");
        broadcastsExpectedFeeds.add("NATIONAL");
        broadcastsExpectedFeeds.add("NATIONAL FEED");
        broadcastsExpectedFeeds.add("HOME");
        broadcastsExpectedFeeds.add("AWAY");
        return broadcastsExpectedFeeds;
    }
}
