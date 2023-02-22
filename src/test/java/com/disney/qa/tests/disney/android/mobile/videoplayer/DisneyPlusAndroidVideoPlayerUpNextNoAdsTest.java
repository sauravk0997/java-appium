package com.disney.qa.tests.disney.android.mobile.videoplayer;

import com.disney.qa.api.client.responses.content.ContentSeason;
import com.disney.qa.api.client.responses.content.ContentSeries;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.jarvis.android.JarvisAndroidBase;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class DisneyPlusAndroidVideoPlayerUpNextNoAdsTest extends BaseDisneyTest {

    public static final String DISNEY_PROD_SERIES_DEEPLINK = "disney_prod_series_landing_page_deeplink";
    public static final String DISNEY_PROD_MOVIES_DEEPLINK = "disney_prod_movies_landing_page_deeplink";

    private static final String CONTENT_ID_DUCKTALES =  "48d7cda1-fbb2-495b-b4dd-c43f7759870d";
    private static final String CONTENT_ID_OWL_HOUSE = "68779ba4-f115-439e-9d03-189e08fa2ec2";
    private static final String CONTENT_ID_WANDAVISION = "421f0840-aae6-4eeb-b4b3-e764387502ed";

    private static final String SERIES_DUCKTALES = "/ducktales/tc6CG7H7lhCE";
    private static final String SERIES_GRAVITY_FALLS = "/gravity-falls/HZxayxzMJqed";
    private static final String SERIES_THE_OWL_HOUSE = "/the-owl-house/4cOTrEy0YyaX";
    private static final String SERIES_WANDAVISION = "/wandavision/4SrN28ZjDLwH";

    private static final String MOVIE_TURNING_RED = "/turning-red/4mFPCXJi7N2m";

    private void testSetup(Boolean autoPlay) {
        if (!autoPlay) {
            try {
                accountApi.get().patchProfileAutoplayStatus(disneyAccount.get(), false);
            } catch (Exception e) {
                e.printStackTrace();
                Assert.fail("Something went wrong with patching the account");
            }
        }

        login(disneyAccount.get(), false);
    }

    public void verifyUpNextUI(DisneyPlusVideoPageBase videoPageBase, SoftAssert sa, String upNextTitle, String upNextText) {
        sa.assertTrue(videoPageBase.isUpNextPresent(), "Up next is not present.");
        sa.assertTrue(videoPageBase.isUpNextBackButtonElementPresent(), "Up next back button is not present.");

        sa.assertTrue(videoPageBase.isUpNextTitlePresent(), "Up next title is not present.");
        sa.assertEquals(videoPageBase.getUpNextTitleText(), upNextTitle);

        sa.assertTrue(videoPageBase.isUpNextHeaderPresent(), "Up next header is not present.");
        sa.assertEquals(videoPageBase.getUpNextHeaderText(), upNextText, "Up next text did not match.");

        sa.assertTrue(videoPageBase.isTitleTextSwitcherPresent(), "See all episodes button is not preset.");

        sa.assertTrue(videoPageBase.isUpNextImagePresent(), "Up next image is not present.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67654"})
    @Test(description = "Test Check Up Next - Autoplay ON", groups = {"Video Player"})
    public void testAutoPlayEnabledFunctionality() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1674"));
        testSetup(true);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        initPage(DisneyPlusMediaPageBase.class).startPlayback();
        videoPageBase.playVideo(120, true, 30, 85);

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67656"})
    @Test(description = "Test Check Up Next - Autoplay OFF / Click to Play", groups = {"Video Player"})
    public void testAutoPlayDisabledFunctionality() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1675"));

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(false);

        ContentSeason season = searchApi.get().getSeason(CONTENT_ID_OWL_HOUSE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        String upNextTitlePlaceHolder = languageUtils
                .get()
                .replaceValuePlaceholders(languageUtils
                                .get()
                                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                        DictionaryKeys.SEASON_EPISODE_TITLE_PLACEHOLDER.getText()),
                        "1", "2", season.getEpisodeTitles().get(1));
        String upNextText = languageUtils
                .get()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.POSTPLAY_UPNEXT.getText()).toUpperCase();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_THE_OWL_HOUSE);
        String mediaTitle = mediaPageBase.getMediaTitle();
        initPage(DisneyPlusMediaPageBase.class).startPlayback();
        videoPageBase.waitForVideoBuffering();
        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");
        verifyUpNextUI(videoPageBase, sa, upNextTitlePlaceHolder, upNextText);

        Assert.assertTrue(videoPageBase.isUpNextBackgroundImageVisible(),
                "Background image did not display.");

        videoPageBase.clickUpNextBackButton();

        mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        sa.assertEquals(mediaPageBase.getMediaTitle(), mediaTitle,
                "User did not return to the same media details page after closing the video player");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67646"})
    @Test(description = "Test Check Up Next - Autoplay ON UI elements - User Taps Play Icon", groups = {"Video Player"})
    public void testAutoPlayEnabledUI() {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);

        SoftAssert sa = new SoftAssert();

        testSetup(true);

        ContentSeason season = searchApi.get().getSeason(CONTENT_ID_DUCKTALES, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        String upNextTitlePlaceHolder = languageUtils
                .get()
                .replaceValuePlaceholders(languageUtils
                                .get()
                                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                        DictionaryKeys.SEASON_EPISODE_TITLE_PLACEHOLDER.getText()),
                        "1", "2",  season.getEpisodeTitles().get(1));
        String upNextText = languageUtils
                .get()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.POSTPLAY_UPNEXT.getText()).toUpperCase();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        mediaPageBase.startPlayback();
        videoPageBase.waitForVideoBuffering();
        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isUpNextContainerVisible(), "Up next container did not display.");
        verifyUpNextUI(videoPageBase, sa, upNextTitlePlaceHolder, upNextText);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67648"})
    @Test(description = "Test Check Up Next - Autoplay ON - User Taps Play Icon", groups = {"Video Player"})
    public void testAutoPlayPlayButton() {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(true);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);

        mediaPageBase.startPlayback();
        videoPageBase.waitForVideoBuffering();
        String contentTitle = videoPageBase.getActiveMediaTitle();
        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");

        Assert.assertTrue(videoPageBase.isUpNextProgressImageClickable(),
                "Up Next progress image is not clickable");

        videoPageBase.clickPlayNextEpisodeBtn();
        videoPageBase.waitForVideoBuffering();
        sa.assertFalse(contentTitle.equals(videoPageBase.getActiveMediaTitle()),
                "Tapped on play button but episode did not change.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67640"})
    @Test(description = "Test Check Up next - Autoplay ON - Dismiss Up Next Overlay before Timer", groups = {"Video Player"})
    public void testDismissAutoPlayOverlay() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1676"));

        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(true);

        Assert.assertTrue(discoverPageBase.isOpened(),
                "Discover page is not displayed");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_GRAVITY_FALLS);
        mediaPageBase.startPlayback();
        videoPageBase.waitForVideoBuffering();
        videoPageBase.scrubToPercentage(0.85);

        sa.assertFalse(videoPageBase.isLoadSpinnerDisplayed(),
                "Video player auto play initiated and loading spinner displayed");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67652"})
    @Test(description = "Test Check Up Next - User Taps See All Episodes", groups = {"Video Player"})
    public void testAutoPlaySeeAllEpisodesButton() {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(true);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        String mediaTitle = mediaPageBase.getMediaTitle();
        mediaPageBase.startPlayback();
        videoPageBase.waitForVideoBuffering();
        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");
        Assert.assertTrue(videoPageBase.isTitleTextSwitcherPresent(),
                "See all episodes button is not present.");

        videoPageBase.clickTitleTextSwitcherTextView();
        mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        sa.assertEquals(mediaPageBase.getMediaTitle(), mediaTitle,
                "Did not navigate back to media page after clicking see all episodes button.");

        sa.assertTrue(mediaPageBase.isStandardButtonContainerDisplayed(), "Play button is not displayed.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67664"})
    @Test(description = "Test Check Up Next - Autoplay ON - Next Episode is Downloaded - wifi off", groups = {"Video Player"})
    public void testUpNextEpisodeIsDownloadedOffline() {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusDownloadsPageBase downloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(true);

        ContentSeason season = searchApi.get().getSeason(CONTENT_ID_DUCKTALES, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        String localizedContentDescription = languageUtils
                .get()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOAD_SERIES_EPISODE.getText());

        String localizedSeason = languageUtils
                .get()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText());

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        String mediaTitle = mediaPageBase.getMediaTitle();

        localizedSeason = languageUtils
                .get()
                .replaceValuePlaceholders(localizedSeason, "1");

        mediaPageBase.downloadEpisodeByContentDescription(languageUtils
                .get()
                .replaceValuePlaceholders(localizedContentDescription, "1", "1", season.getEpisodeTitles().get(0)), localizedSeason);

        mediaPageBase.downloadEpisodeByContentDescription(languageUtils
                        .get()
                        .replaceValuePlaceholders(localizedContentDescription, "1", "2", season.getEpisodeTitles().get(1)),
                localizedSeason);

        mediaPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText()));

        downloadsPageBase.waitForDownload(mediaTitle);

        androidUtils.get().disableWifi();

        downloadsPageBase.openDownloadedMedia();
        downloadsPageBase.clickMediaDownloadThumbnail();
        videoPageBase.waitForVideoBuffering();
        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");
        sa.assertFalse(androidUtils.get().isWifiEnabled(), "Wifi was not disabled");

        androidUtils.get().enableWifi();
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72685"})
    @Test(description = "Test Check Up Next - Autoplay ON - Next Episode is Downloaded - wifi on", groups = {"Video Player"})
    public void testUpNextEpisodeIsDownloadedWifiOn() {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusDownloadsPageBase downloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(true);

        ContentSeason season = searchApi.get().getSeason(CONTENT_ID_DUCKTALES, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        String localizedContentDescription = languageUtils
                .get()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOAD_SERIES_EPISODE.getText());

        String localizedSeason = languageUtils
                .get()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText());

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        String mediaTitle = mediaPageBase.getMediaTitle();

        localizedSeason = languageUtils
                .get()
                .replaceValuePlaceholders(localizedSeason, "1");

        mediaPageBase.downloadEpisodeByContentDescription(languageUtils
                .get()
                .replaceValuePlaceholders(localizedContentDescription, "1", "1", season.getEpisodeTitles().get(0)), localizedSeason);

        mediaPageBase.downloadEpisodeByContentDescription(languageUtils
                        .get()
                        .replaceValuePlaceholders(localizedContentDescription, "1", "2", season.getEpisodeTitles().get(1)),
                localizedSeason);

        mediaPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText()));

        downloadsPageBase.waitForDownload(mediaTitle);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);


        mediaPageBase.startPlayback();
        videoPageBase.waitForVideoBuffering();
        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");

        sa.assertTrue(androidUtils.get().isWifiEnabled());

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67672"})
    @Test(description = "Test Check Up Next - Autoplay ON - Only Autoplay Episode-to-Episode - movie is up next", groups = {"Video Player"})
    public void testUpNextMovieDoesNotAutoplay() {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);

        SoftAssert sa = new SoftAssert();

        testSetup(true);
        initPage(DisneyPlusDiscoverPageBase.class).isOpened();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_TURNING_RED);
        String initialMediaTitle = mediaPageBase.getMediaTitle();
        mediaPageBase.startPlayback();
        videoPageBase.waitForVideoBuffering();
        videoPageBase.scrubToUpNext(0.9, 0.025);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");

        sa.assertNotNull(searchApi.get().getMovie(videoPageBase.getUpNextTitleText(), disneyAccount.get()));

        videoPageBase.isPlayNextEpisodeBtnPresent();
        videoPageBase.dismissPlayNextEpisodeOverlay();
        sa.assertEquals(videoPageBase.getActiveMediaTitle(), initialMediaTitle,
                "User did not return to the same media details page after closing the video player");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67676"})
    @Test(description = "Test Check Up Next - Autoplay ON - Behavior when App is Backgrounded", groups = {"Video Player"})
    public void testUpNextBackgroundedBehaviorAutoplayOn() {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(true);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        Assert.assertTrue(mediaPageBase.waitUntilMediaPageLoads(), "Media page did not load");
        mediaPageBase.startPlayback();
        Assert.assertTrue(videoPageBase.waitForVideoToPlay(), "Video did not begin playback");
        String initialEpisodeTitle = videoPageBase.getActiveMediaTitle();

        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");
        androidUtils.get().backgroundApp();
        androidUtils.get().displayRecentApps();

        new AndroidUtilsExtended().reopenApp(APP_NAME);

        Assert.expectThrows(TimeoutException.class, videoPageBase::isPlayNextEpisodeBtnNotPresent);

        videoPageBase.clickVideoPlayer();
        String currentEpisodeTitle = videoPageBase.getActiveMediaTitle();

        sa.assertEquals(currentEpisodeTitle, initialEpisodeTitle,
                "Current and initial episode titles do not match. Autoplay occurred.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72610"})
    @Test(description = "Test Check Up Next - Autoplay OFF - Behavior when App is Backgrounded", groups = {"Video Player"})
    public void testUpNextBackgroundedBehaviorAutoplayOff() {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);

        testSetup(false);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_DUCKTALES);
        Assert.assertTrue(mediaPageBase.waitUntilMediaPageLoads(), "Media page did not load");
        mediaPageBase.startPlayback();
        Assert.assertTrue(videoPageBase.waitForVideoToPlay(), "Video did not begin playback");

        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");
        androidUtils.get().backgroundApp();
        androidUtils.get().displayRecentApps();

        new AndroidUtilsExtended().reopenApp(APP_NAME);

        Assert.expectThrows(TimeoutException.class, videoPageBase::isPlayNextEpisodeBtnNotPresent);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67684"})
    @Test(description = "Test Check Up Next - Content Rating Overlay", groups = {"Video Player"})
    public void testUpNextContentRatingOverlay() {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(true);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_GRAVITY_FALLS);
        mediaPageBase.startPlayback();
        videoPageBase.waitForVideoBuffering();
        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isUpNextContainerVisible(), "Up next container did not display.");
        videoPageBase.clickPlayNextEpisodeBtn();
        sa.assertTrue(videoPageBase.waitForContentRatingOverlay(), "Content advisory overlay did not display.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67678"})
    @Test(description = "Test Check Up Next - New Series", groups = {"Video Player"})
    public void testUpNextNewSeries() {
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetup(false);

        String localizedSeason = languageUtils
                .get()
                .replaceValuePlaceholders(languageUtils
                        .get()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()),
                        "2");

        ContentSeason season = searchApi.get().getSeason("11c79d97-e9c4-41aa-bf7f-e58e74e14ad2", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        String localizedContentDescription = languageUtils
                .get()
                .replaceValuePlaceholders(languageUtils
                        .get()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.CONTENT_TILE_EPISODE.getText()),
                        "21", season.getEpisodeTitles().get(20));

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_GRAVITY_FALLS);
        Assert.assertTrue(mediaPageBase.waitUntilMediaPageLoads(), "Media page did not load");

        mediaPageBase.playEpisodeByContentDescription(localizedContentDescription, localizedSeason);
        videoPageBase.waitForVideoBuffering();
        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");

        ContentSeries upNextSeries = mediaPageBase.getSeriesObject(videoPageBase.getUpNextTitleText(),
                searchApi.get(),disneyAccount.get(), languageUtils.get());

        sa.assertNotNull(upNextSeries, "Up next did not return a series.");

        videoPageBase.clickTitleTextSwitcherTextView();
        Assert.assertTrue(mediaPageBase.waitUntilMediaPageLoads(), "Media page did not load");
        String currentTitle = mediaPageBase.getMediaTitle();

        sa.assertEquals(currentTitle, upNextSeries.getSeriesTitle(),
                "Clicked see details but was not taken to the right series.");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_GRAVITY_FALLS);
        String initialMediaTitle = mediaPageBase.getMediaTitle();

        mediaPageBase.playEpisodeByContentDescription(localizedContentDescription, localizedSeason);
        videoPageBase.waitForVideoBuffering();
        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");

        upNextSeries = mediaPageBase.getSeriesObject(videoPageBase.getUpNextTitleText(),
                searchApi.get(),disneyAccount.get(), languageUtils.get());

        sa.assertNotNull(upNextSeries, "Up next did not return a series.");

        videoPageBase.clickPlayNextEpisodeBtn();

        currentTitle = videoPageBase.getActiveMediaTitle();

        sa.assertNotEquals(currentTitle, initialMediaTitle,
                "Clicked play next episode button but the show did not change.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67682"})
    @Test(description = "Test Check Up Next - Sneak Peek", groups = {"Video Player"})
    public void testUpNextSneakPeek() {
        DisneyAccount testAccount = disneyAccount.get();
        accountApi.get().addFlex(testAccount);
        List<String> ratingSystemValues = disneyAccount.get().getProfile(DEFAULT_PROFILE).getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues();
        accountApi.get().editContentRatingProfileSetting(testAccount, languageUtils.get().getRatingSystem(), ratingSystemValues.get(ratingSystemValues.size() - 1));

        //travel back in time with delorean
        JarvisAndroidBase jarvis = initPage(JarvisAndroidBase.class);
        JarvisAndroidBase.Delorean.TIME_CIRCUITS.setOverrideValue("2021-01-21T12:04:00Z");
        jarvis.activateOverrides();
        pause(3);
        clearAppCache();
        testSetup(true);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        SoftAssert sa = new SoftAssert();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_WANDAVISION);
        Assert.assertTrue(mediaPageBase.waitUntilMediaPageLoads(), "Media page did not load");

        ContentSeason season = searchApi.get().getSeason(CONTENT_ID_WANDAVISION, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        mediaPageBase.playEpisodeByContentDescription(languageUtils
                .get()
                .replaceValuePlaceholders(languageUtils
                                .get()
                                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.CONTENT_TILE_EPISODE.getText()),
                        "2", season.getEpisodeTitles().get(1)), languageUtils
                .get()
                .replaceValuePlaceholders(languageUtils
                                .get()
                                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()),
                        "1"));
        Assert.assertTrue(videoPageBase.waitForVideoToPlay(), "Video did not begin playback");

        videoPageBase.scrubToPercentage(0.85);

        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");

        sa.assertTrue(videoPageBase.isUpNextHeaderPresent(), "Up next header is not present.");
        sa.assertEquals(videoPageBase.getUpNextHeaderText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.POSTPLAY_SNEAK_PEEK.getText()).toUpperCase(), "Up next text did not match.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67678"})
    @Test(description = "Test Check Up Next - Autoplay ON - Convert to Click to Play after 3 hours of Consecutive Viewing",
            groups = {"Video Player"})
    public void testUpNextAutoplayDisablesAfterThreeHours() {
        //Set the autoplay jarvis setting to 0 to mimic watching for 3 hours
        JarvisAndroidBase jarvis = initPage(JarvisAndroidBase.class);
        JarvisAndroidBase.UpNext.MAX_CONSECUTIVE_MINUTES_AUTOPLAY.setOverrideValue(0);
        jarvis.activateOverrides();
        pause(3);
        clearAppCache();
        testSetup(true);
        SoftAssert sa = new SoftAssert();
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_GRAVITY_FALLS);
        Assert.assertTrue(mediaPageBase.waitUntilMediaPageLoads(), "Media page did not load");

        mediaPageBase.startPlayback();
        Assert.assertTrue(videoPageBase.isOpened(), "Video did not begin playback");
        videoPageBase.scrubToPercentage(0.85);
        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");
        Assert.assertTrue(videoPageBase.isUpNextBackgroundImageVisible(),
                "Background image did not display.");

        //check that the next video is also CTP
        videoPageBase.clickPlayNextEpisodeBtn();
        Assert.assertTrue(videoPageBase.isOpened(), "Video did not begin playback");
        videoPageBase.scrubToPercentage(0.85);
        Assert.assertTrue(videoPageBase.isPlayNextEpisodeBtnPresent(), "Up next container did not display.");
        Assert.assertTrue(videoPageBase.isUpNextBackgroundImageVisible(),
                "Background image did not display.");

        sa.assertAll();
    }

}
