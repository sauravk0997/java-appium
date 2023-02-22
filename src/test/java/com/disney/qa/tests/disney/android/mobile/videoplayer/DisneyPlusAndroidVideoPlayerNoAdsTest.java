package com.disney.qa.tests.disney.android.mobile.videoplayer;

import com.disney.qa.api.client.responses.content.ContentMovie;
import com.disney.qa.api.client.responses.content.ContentSeason;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.disney.android.pages.common.DisneyPlusVideoPageBase.*;

public class DisneyPlusAndroidVideoPlayerNoAdsTest extends BaseDisneyTest {
    private static final String MOVIE_CHIP_N_DALE = "/chip-n-dale-rescue-rangers/9WluNFNFwNHY";
    private static final String MOVIE_CHIP_N_DALE_ID = "9WluNFNFwNHY";
    private static final String MOVIE_DUMBO = "/dumbo/3lAXKmGUOnjh";

    private static final String SERIES_DUCK_TALES_S1_SEASON_ID =  "48d7cda1-fbb2-495b-b4dd-c43f7759870d";
    private static final String SERIES_TIMON_AND_PUMBAA_S1_SEASON_ID = "457dc91f-9a4a-44ba-8a43-29a22f9f5901";
    private static final String SERIES_DUCK_TALES_S1_E1_DEEPLINK = "/ducktales/tc6CG7H7lhCE";
    private static final String SERIES_AMPHIBIA = "/amphibia/4jsQ0zDkUTeN";
    private static final String SERIES_BAD_BATCH = "/the-bad-batch/4gMliqFxxqXC";
    private static final String SERIES_TIMON_AND_PUMBAA = "/timon-pumbaa/4quDoLoWikHX";
    private static final String ITALIAN_AUDIO_LANGUAGE = "Italiano";
    private static final String REGEX_HH_MM_SS = "[0-9]:[0-9]\\d:[0-9]\\d";
    private static final String REGEX_MM_SS = "[1-9][0-9]:[0-9][0-9]";
    private static final String REGEX_SS = "[0-9]:[0-9][0-9]";

    public static final String DISNEY_PROD_SERIES_DEEPLINK = "disney_prod_series_landing_page_deeplink";
    public static final String DISNEY_PROD_MOVIES_DEEPLINK = "disney_prod_movies_landing_page_deeplink";

    public static final String REMAINING_TIME_BEFORE_FORWARD_TAP = "Remaining time before forward tap ";
    public static final String REMAINING_TIME_AFTER_FORWARD_TAP = "Remaining time after forward tap ";
    public static final String REMAINING_TIME_BEFORE_REWIND_TAP = "Remaining time before rewind tap ";
    public static final String REMAINING_TIME_AFTER_REWIND_TAP = "Remaining time after rewind tap ";

    public static final int SINGLE_TAP = 1;
    public static final int DOUBLE_TAP = 2;
    public static final int MULTI_TAP = 4;

    private void testSetup(Boolean autoPlay) {
        if(!autoPlay) {
            try {
                accountApi.get().patchProfileAutoplayStatus(disneyAccount.get(), false);
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("Something went wrong with patching the account");
            }
        }

        login(disneyAccount.get(), false);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67847"})
    @Test(description = "Test Check Changing Preferred Audio Language", groups = {"Video Player"})
    public void testChangePreferredAudioInterface() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1677"));

        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(true);

        androidUtils.get().launchWithDeeplinkAddress(
                R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_BAD_BATCH);

        mediaPageBase.startPlayback();

        Assert.assertFalse(
                videoPageBase.checkForPlaybackConnectionErrors(),
                "Connection error occurred after starting playback");

        videoPageBase.selectAudioOption(ITALIAN_AUDIO_LANGUAGE);

        sa.assertTrue(videoPageBase.isAudioOptionEnabled(ITALIAN_AUDIO_LANGUAGE),
                "Audio Option was not enabled");

        androidUtils.get().pressBack();

        Assert.assertTrue(videoPageBase.isOpened(),
                "Video Player did not remain opened after setting Subtitles");

        sa.assertTrue(videoPageBase.isAudioOptionEnabled(ITALIAN_AUDIO_LANGUAGE),
                "Audio Option was not retained after reopening Audio and Subtitles menu");

        androidUtils.get().pressBack();
        videoPageBase.closeVideo();
        mediaPageBase.clickContinuePlayButton();

        sa.assertTrue(videoPageBase.isAudioOptionEnabled(ITALIAN_AUDIO_LANGUAGE),
                "Audio Option was not retained after reopening the video");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67869"})
    @Test(description = "Test Check Changing Preferred Subtitle Language", groups = {"Video Player"})
    public void testChangePreferredSubtitleInterface() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1678"));

        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        String subtitleLanguage = "English [CC]";
        String subtitleOptionOff =
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                        DictionaryKeys.SUBTITLES_OPTIONS_OFF.getText());

        testSetup(true);

        androidUtils.get().launchWithDeeplinkAddress(
                R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_AMPHIBIA);

        mediaPageBase.startPlayback();

        Assert.assertFalse(
                videoPageBase.checkForPlaybackConnectionErrors(),
                "Connection error occurred after starting playback");

        videoPageBase.selectSubtitleOption(subtitleLanguage);

        sa.assertTrue(videoPageBase.isSubtitleOptionEnabled(subtitleLanguage),
                "Subtitle Option was not enabled");

        androidUtils.get().pressBack();

        Assert.assertTrue(videoPageBase.isOpened(),
                "Video Player did not remain opened after setting Subtitles");

        Assert.assertTrue(videoPageBase.isSubtitleOptionEnabled(subtitleLanguage),
                "Subtitle Option was not retained after reopening Audio and Subtitles menu");

        androidUtils.get().pressBack();
        videoPageBase.closeVideo();
        mediaPageBase.startPlayback();

        Assert.assertFalse(
                videoPageBase.checkForPlaybackConnectionErrors(),
                "Connection error occurred after starting playback");

        sa.assertTrue(videoPageBase.isSubtitleOptionEnabled(subtitleLanguage),
                "Subtitle Option was not retained after reopening the video");

        videoPageBase.selectSubtitleOption(subtitleOptionOff);

        sa.assertTrue(videoPageBase.isSubtitleOptionEnabled(subtitleOptionOff),
                "Subtitle disabling was not performed");

        androidUtils.get().pressBack();

        Assert.assertTrue(videoPageBase.isOpened(),
                "Video Player did not remain opened after closing disabling Subtitles");

        sa.assertTrue(videoPageBase.isSubtitleOptionEnabled(subtitleOptionOff),
                "Subtitle disabling was not retained after reopening Audio and Subtitles menu");

        androidUtils.get().pressBack();
        videoPageBase.closeVideo();
        mediaPageBase.clickContinuePlayButton();

        sa.assertFalse(videoPageBase.isSubtitleOptionEnabled(subtitleLanguage),
                "Subtitle disabling was not retained after reopening the video");

        androidUtils.get().pressBack();

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68462"})
    @Test(description = "Test Check Negative Stereotype Advisory Playback Interstitial (on a Movie)", groups = {"Video Player"})
    public void testNegativeStereotypeDisplayMovies() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1679"));

        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);

        Assert.assertTrue(discoverPageBase.isOpened(),
                "Discovery screen not displayed");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_DUMBO);

        Assert.assertTrue(mediaPageBase.isOpened(),
                "Details screen not displayed");

        String title = mediaPageBase.getMediaTitle();
        mediaPageBase.startPlayback();

        sa.assertEquals(
                videoPageBase.getContentAdvisoryText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                        DictionaryKeys.DETAILS_NEGATIVE_STEREOTYPE_ADVISORY.getText()),
                "Content Advisory text is displayed incorrectly");

        commonPageBase.swipeUpOnScreen(1);

        sa.assertEquals(
                videoPageBase.getContentAdvisoryCountdownText().replaceAll("\\d", "").trim(),
                languageUtils.get().getValueBeforePlaceholder(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.DETAILS_HERITAGE_COUNTDOWN.getText())),
                "Content advisory countdown text not displayed");

        Assert.assertFalse(
                videoPageBase.checkForPlaybackConnectionErrors(),
                "Connection error occurred after starting playback");

        sa.assertEquals(
                videoPageBase.getActiveMediaTitle(),
                title,
                "Video Playback did not begin after the warning timer concluded");

        videoPageBase.closeVideo();

        sa.assertTrue(mediaPageBase.getMediaTitle().equals(title),
                "User was not returned to the correct Media Details page after closing the video");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71665"})
    @Test(description = "Test Negative Stereotype Advisory Interstitial - Series episode", groups = {"Video Player"})
    public void testNegativeStereotypeDisplaySeries() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1680"));

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        ContentSeason season =
                searchApi.get().getSeason(SERIES_TIMON_AND_PUMBAA_S1_SEASON_ID,
                        languageUtils.get().getLocale(),
                        languageUtils.get().getUserLanguage());

        testSetup(true);

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "App did not land on Discover page");

        androidUtils.get().launchWithDeeplinkAddress(
                R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_TIMON_AND_PUMBAA);

        Assert.assertTrue(initPage(DisneyPlusMediaPageBase.class).isOpened(),
                "App did not launch into media page with a watchlist URL");

        String title = mediaPageBase.getMediaTitle();
        mediaPageBase.playDesiredMedia(season.getEpisodeDescriptions().get(0));

        String displayedAdvisoryText =
                videoPageBase.getContentAdvisoryText()
                        .replace("\n", "")
                        .replace("  ", " ");

        commonPageBase.swipeUpOnScreen(1);

        String displayedAdvisoryCountdown =
                videoPageBase.getContentAdvisoryCountdownText().replaceAll("\\d", "").trim();

        sa.assertEquals(
                displayedAdvisoryText,
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                        DictionaryKeys.DETAILS_NEGATIVE_STEREOTYPE_ADVISORY.getText()),
                "Content Advisory text is displayed incorrectly");

        sa.assertEquals(
                displayedAdvisoryCountdown,
                languageUtils.get().getValueBeforePlaceholder(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.DETAILS_HERITAGE_COUNTDOWN.getText())),
                "Content Advisory Countdown text was not displayed properly");

        sa.assertFalse(
                videoPageBase.verifySeriesMetadata(
                        videoPageBase.getActiveMediaTitle(), season.getEpisodeTitles().get(0)).containsValue(false),
                "Metadata format is not in the expected format");

        videoPageBase.closeVideo();
        androidUtils.get().swipeDown(3, 250);

        sa.assertTrue(mediaPageBase.getMediaTitle().equals(title),
                "User was not returned to the correct Media Details page after closing the video");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66505"})
    @Test(description = "Test video player controls on movie content", groups = {"Video Player"})
    @Maintainer("akorwar")
    public void testVideoPlayerControlsOnMovies() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1681"));

        SoftAssert sa = new SoftAssert();
        ContentMovie movie = searchApi.get().getMovie(MOVIE_CHIP_N_DALE_ID, languageUtils.get().getLocale(),languageUtils.get().getUserLanguage());

        testVideoPlayerControls(DISNEY_PROD_MOVIES_DEEPLINK, MOVIE_CHIP_N_DALE, movie.getVideoTitle(), false);

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72539"})
    @Test(description = "Test video player controls on series content", groups = {"Video Player"})
    @Maintainer("akorwar")
    public void testVideoPlayerControlsOnSeries() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1682"));

        SoftAssert sa = new SoftAssert();
        ContentSeason season = searchApi.get().getSeason(SERIES_DUCK_TALES_S1_SEASON_ID, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());

        testVideoPlayerControls(DISNEY_PROD_SERIES_DEEPLINK, SERIES_DUCK_TALES_S1_E1_DEEPLINK, season.getEpisodeTitles().get(0), true);

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66509"})
    @Test(description = "Video Player Controls - Bring Up and Dismiss Controls", groups = {"Video Player"})
    @Maintainer("akorwar")
    public void testVideoPlayerControlsDismiss() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1683"));

        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "App did not land on Discover page");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_CHIP_N_DALE);

        Assert.assertTrue(mediaPageBase.isOpened(),
                "App did not launch into media page with a watchlist URL");

        mediaPageBase.startPlayback();

        Assert.assertFalse(
                videoPageBase.checkForPlaybackConnectionErrors(),
                "Connection error occurred after starting playback");

        Assert.assertTrue(videoPageBase.isInPlayback(),
                "Error in starting playback");

        videoPageBase.displayControlOverlay();

        sa.assertTrue(videoPageBase.isPlayPauseButtonPresent(),
                "Video player controls are not displayed after tapping the player screen");

        videoPageBase.waitForDisplayControlOverlayToDisappear();

        sa.assertFalse(videoPageBase.isPlayPauseButtonPresent(),
                "Video player controls are not automatically dismissed");

        videoPageBase.tapOnPlayPauseButton();

        sa.assertTrue(videoPageBase.isPlayPauseButtonPresent(),
                "Video player controls not displayed in paused state");

        videoPageBase.tapOutsidePlayerControls();

        sa.assertFalse(videoPageBase.isPlayPauseButtonPresent(),
                "Video player controls are not dismissed on tapping outside of player controls");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66529"})
    @Test(description = "Video Player Controls - Verify Rewind and Forward functions", groups = {"Video Player"})
    @Maintainer("akorwar")
    public void testRewindAndForwardButtons() {
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1684"));

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(false);

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "App did not land on Discover page");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_CHIP_N_DALE);

        Assert.assertTrue(initPage(DisneyPlusMediaPageBase.class).isOpened(),
                "App did not launch into media page with a watchlist URL");

        mediaPageBase.startPlayback();

        Assert.assertFalse(
                videoPageBase.checkForPlaybackConnectionErrors(),
                "Connection error occurred after starting playback");

        videoPageBase.waitForVideoToOpenAndBuffer();

        videoPageBase.waitForDisplayControlOverlayToDisappear();

        videoPageBase.tapOnPlayPauseButton();

        int remainingTimeInPauseMode = videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());
        videoPageBase.clickJumpForwardButton();

        int remainingTimeAfterFwdTapInPauseMode =
                videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        sa.assertTrue(remainingTimeInPauseMode > remainingTimeAfterFwdTapInPauseMode,
                "Remaining time in pause mode before fwd tap " + remainingTimeInPauseMode +
                        " is not greater than remaining time after fwd tap " + remainingTimeAfterFwdTapInPauseMode);

        int remainingTimeBeforeRwdTapInPauseMode =
                videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        videoPageBase.clickJumpBackwardButton();

        int remainingTimeAfterRwdTapInPauseMode =
                videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        sa.assertTrue(remainingTimeAfterRwdTapInPauseMode > remainingTimeBeforeRwdTapInPauseMode,
                "Remaining time in pause mode after tapping rewind " + remainingTimeAfterRwdTapInPauseMode +
                        " is not greater than remaining time after rwd tap " + remainingTimeBeforeRwdTapInPauseMode);

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71946"})
    @Test(description = "Video Player Controls - Double tap to RW /FW", groups = {"Video Player"})
    @Maintainer("akorwar")
    public void testDoubleTapOnVideoScreenToRwdAndFwd() {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "App did not land on Discover page");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_CHIP_N_DALE);

        Assert.assertTrue(initPage(DisneyPlusMediaPageBase.class).isOpened(),
                "App did not launch into media page with a watchlist URL");

        mediaPageBase.startPlayback();

        Assert.assertFalse(
                videoPageBase.checkForPlaybackConnectionErrors(),
                "Connection error occurred after starting playback");

        videoPageBase.waitForVideoToOpenAndBuffer();
        videoPageBase.waitForDisplayControlOverlayToDisappear();
        videoPageBase.tapOnPlayPauseButton();

        int remainingTimeBeforeFwdDoubleTap = videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        videoPageBase.tapOnPlayerScreen(PlayerControls.TAP, SINGLE_TAP);
        videoPageBase.tapOnPlayerScreen(PlayerControls.FAST_FORWARD, DOUBLE_TAP);
        videoPageBase.waitForVideoBuffering();

        int remainingTimeAfterFwdDoubleTap = videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        sa.assertTrue(
                videoPageBase.verifyForwardTapAction(
                        remainingTimeBeforeFwdDoubleTap, remainingTimeAfterFwdDoubleTap),
                REMAINING_TIME_BEFORE_FORWARD_TAP + remainingTimeBeforeFwdDoubleTap +
                        " is less than " + REMAINING_TIME_AFTER_FORWARD_TAP + remainingTimeAfterFwdDoubleTap);

        int remainingTimeBeforeRwdDoubleTap = videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        videoPageBase.tapOnPlayerScreen(PlayerControls.TAP, SINGLE_TAP);
        videoPageBase.tapOnPlayerScreen(PlayerControls.REWIND, DOUBLE_TAP);
        videoPageBase.waitForVideoBuffering();

        int remainingTimeAfterRwdDoubleTap = videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        sa.assertTrue(
                videoPageBase.verifyRewindTapAction(
                        remainingTimeBeforeRwdDoubleTap, remainingTimeAfterRwdDoubleTap),
                REMAINING_TIME_AFTER_REWIND_TAP + remainingTimeAfterRwdDoubleTap +
                        " is less than " + REMAINING_TIME_BEFORE_REWIND_TAP + remainingTimeBeforeRwdDoubleTap);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71947"})
    @Test(description = "Video Player Controls - RW & FW - Tap Multiple Times", groups = {"VideoPlayer"})
    @Maintainer("akorwar")
    public void testMultipleTapsToRwdAndFwd() {
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "App did not land on Discover page");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_CHIP_N_DALE);

        Assert.assertTrue(initPage(DisneyPlusMediaPageBase.class).isOpened(),
                "App did not launch into media page with a watchlist URL");

        mediaPageBase.startPlayback();

        Assert.assertFalse(
                videoPageBase.checkForPlaybackConnectionErrors(),
                "Connection error occurred after starting playback");

        videoPageBase.waitForVideoToOpenAndBuffer();
        videoPageBase.waitForDisplayControlOverlayToDisappear();
        videoPageBase.tapOnPlayPauseButton();

        int remainingTimeBeforeFwdDoubleTap = videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        videoPageBase.tapOnRwdOrFwdButton(PlayerControls.FAST_FORWARD, MULTI_TAP);

        int remainingTimeAfterFwdDoubleTap = videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        sa.assertTrue(
                videoPageBase.verifyForwardTapAction(
                        remainingTimeBeforeFwdDoubleTap, remainingTimeAfterFwdDoubleTap),
                REMAINING_TIME_BEFORE_FORWARD_TAP + remainingTimeBeforeFwdDoubleTap +
                        " is less than " + REMAINING_TIME_AFTER_FORWARD_TAP + remainingTimeAfterFwdDoubleTap);

        int remainingTimeBeforeRwdDoubleTap = videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        videoPageBase.tapOnRwdOrFwdButton(PlayerControls.REWIND, MULTI_TAP);

        int remainingTimeAfterRwdDoubleTap = videoPageBase.getRemainingTimeInSeconds(videoPageBase.getRemainingTime());

        sa.assertTrue(videoPageBase.verifyRewindTapAction(
                        remainingTimeBeforeRwdDoubleTap, remainingTimeAfterRwdDoubleTap),
                REMAINING_TIME_AFTER_REWIND_TAP + remainingTimeAfterRwdDoubleTap +
                        " is less than " + REMAINING_TIME_BEFORE_REWIND_TAP + remainingTimeBeforeRwdDoubleTap);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66517"})
    @Test(description = "Video Player Controls - Displaying the Duration", groups = {"VideoPlayer"})
    public void testRemainingTimeDurationDisplay() {
        login(disneyAccount.get(), false);

        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        SoftAssert sa = new SoftAssert();

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "App did not land on Discover page");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_CHIP_N_DALE);

        Assert.assertTrue(mediaPageBase.isOpened(),
                "App did not launch into media page with a watchlist URL");

        mediaPageBase.startPlayback();

        Assert.assertFalse(videoPageBase.checkForPlaybackConnectionErrors(), "Connection error occurred after starting playback");

        videoPageBase.waitForVideoToOpenAndBuffer();
        videoPageBase.waitForDisplayControlOverlayToDisappear();

        sa.assertTrue(videoPageBase.verifyRemainingTimePattern(videoPageBase.getRemainingTime(), REGEX_HH_MM_SS));

        videoPageBase.scrubToAbsolutePercentage(0.75);

        sa.assertTrue(videoPageBase.verifyRemainingTimePattern(videoPageBase.getRemainingTime(), REGEX_MM_SS));

        videoPageBase.scrubToAbsolutePercentage(0.97);
        videoPageBase.dismissPlayNextEpisodeOverlay();
        videoPageBase.displayControlOverlay();

        sa.assertTrue(videoPageBase.verifyRemainingTimePattern(videoPageBase.getRemainingTime(), REGEX_SS));

        sa.assertAll();
    }

    private void testVideoPlayerControls(String deeplink, String contentSubdirectory, String searchApiMetadata, boolean isSeries) {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(true);

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "App did not land on Discover page");

        boolean isChromecastEnabled = discoverPageBase.isChromecastAvailable();

        new AndroidUtilsExtended().launchWithDeeplinkAddress(R.TESTDATA.get(deeplink) + contentSubdirectory);

        Assert.assertTrue(initPage(DisneyPlusMediaPageBase.class).isOpened(),
                "App did not launch into media page with a watchlist URL");

        mediaPageBase.startPlayback();

        Assert.assertFalse(
                videoPageBase.checkForPlaybackConnectionErrors(),
                "Connection error occurred after starting playback");

        videoPageBase.waitForVideoToOpenAndBuffer();

        String contentTitle = videoPageBase.getActiveMediaTitle();

        sa.assertTrue(videoPageBase.isBackButtonPresent(),
                "Video back button does not exist on the player screen");

        sa.assertTrue(videoPageBase.isPlayPauseButtonPresent(),
                "Play/Pause button does not exist on the player screen");

        sa.assertTrue(videoPageBase.isJumpBackwardsButtonPresent(),
                "Jump backward button not displayed on player screen");

        sa.assertTrue(videoPageBase.isJumpForwardButtonPresent(),
                "Jump forward button not displayed on player screen");

        sa.assertTrue(videoPageBase.isSeekBarPresent(),
                "Seek bar is not displayed on player screen");

        sa.assertTrue(videoPageBase.isRemainingTimePresent(),
                "Remaining time not displayed on the player screen");

        if (isSeries) {
            sa.assertFalse(
                    videoPageBase
                            .verifySeriesMetadata(contentTitle, searchApiMetadata).containsValue(false),
                    "Metadata format is not in the expected format");
        } else {
            sa.assertTrue(contentTitle.equals(searchApiMetadata),
                    "Movie metadata does not match Search API value");
        }

        if(isChromecastEnabled){
            sa.assertTrue(videoPageBase.isChromecastIconPresent(),
                    "Expected - Chromecast button to be visible");
        } else {
            sa.assertFalse(videoPageBase.isChromecastIconPresent(),
                    "Expected - Chromecast button to be hidden");
        }

        videoPageBase.closeVideo();

        checkAssertions(sa);
    }
}
