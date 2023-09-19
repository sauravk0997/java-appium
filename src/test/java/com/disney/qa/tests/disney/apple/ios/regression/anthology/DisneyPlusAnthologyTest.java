package com.disney.qa.tests.disney.apple.ios.regression.anthology;

import static com.disney.qa.common.utils.IOSUtils.DEVICE_TYPE;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;

import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDownloadsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLiveEventModalIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;

public class DisneyPlusAnthologyTest extends DisneyBaseTest {

    //Test constants
    private static final String UPCOMING = "UPCOMING";
    private static final String DANCING_WITH_THE_STARS = "Dancing with the Stars";
    private static final String LIVE = "LIVE";
    private static final String PLAY = "PLAY";
    private static final String WATCH_LIVE = "Watch Live";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72640"})
    @Test(description = "Verify Anthology Series - Search", groups = {"Anthology"})
    public void verifyAnthologySearch() {
        initialSetup();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DANCING_WITH_THE_STARS);
        String[] firstDisplayTitle = searchPage.getDisplayedTitles().get(0).getText().split(",");
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DANCING_WITH_THE_STARS + " details page did not open.");
        sa.assertTrue(firstDisplayTitle[0].equalsIgnoreCase(detailsPage.getMediaTitle()), "Search result title does not match Details page media title.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72646"})
    @Test(description = "Verify Anthology Series - Watchlist", groups = {"Anthology"})
    public void verifyAnthologyWatchlist() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();
        String mediaTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        sa.assertTrue(moreMenu.areWatchlistTitlesDisplayed(mediaTitle), "Media title was not added.");
        moreMenu.getDynamicCellByLabel(mediaTitle).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72301"})
    @Test(description = "Verify Anthology Series - Upcoming Badge and Metadata", groups = {"Anthology"})
    public void verifyAnthologyUpcomingBadgeAndMetadata() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> detailsPage.isStaticTextLabelPresent(UPCOMING));
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ UPCOMING + " label not found. " + e);
        }

        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertTrue(detailsPage.getAiringBadgeLabel().isElementPresent(), "Airing badge label is not displayed.");
        sa.assertTrue(detailsPage.getUpcomingDateTime().isElementPresent(), "Upcoming Date and Time was not found.");
        sa.assertTrue(detailsPage.getUpcomingTodayBadge().isElementPresent() || detailsPage.getUpcomingBadge().isElementPresent(),
                "Upcoming Today / Upcoming badge is not present");
        sa.assertTrue(detailsPage.getAiringBadgeLabel().isElementPresent(), "Upcoming airing Badge is not present.");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Watchlist button is not displayed.");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Logo image is not displayed.");
        sa.assertTrue(detailsPage.isHeroImagePresent(), "Hero image is not displayed.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72300"})
    @Test(description = "Verify Anthology Series - Live Badge and Airing Indicator", groups = {"Anthology"})
    public void verifyAnthologyLiveBadge() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModal = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.doesAiringBadgeContainLive());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ LIVE + " label not found, no live content airing. " + e);
        }

        details.validateLiveProgress(sa);
        sa.assertTrue(details.isProgressBarPresent(), "Progress bar is not found.");
        sa.assertTrue(details.doesAiringBadgeContainLive(), "Airing badge does not contain live badge on Details Page");

        details.clickWatchButton();
        liveEventModal.isOpened();
        sa.assertTrue(liveEventModal.doesAiringBadgeContainLive(), "Airing badge does not contain Live badge on Live Event Modal");
        liveEventModal.validateLiveProgress(sa);
        liveEventModal.clickThumbnailView();
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72299"})
    @Test(description = "Verify Anthology Series - Live Playback", groups = {"Anthology"})
    public void verifyAnthologyLivePlayback() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModal = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.doesAiringBadgeContainLive());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ LIVE + " label not found, no live content playing. " + e);
        }

        details.clickWatchButton();
        liveEventModal.isOpened();
        liveEventModal.getWatchLiveButton().click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");
        sa.assertTrue(videoPlayer.isYouAreLiveButtonPresent(), "'You are live' button was not found");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73876"})
    @Test(description = "Verify Anthology Series - Ended, Compare episode number", groups = {"Anthology"})
    public void verifyAnthologyEnded() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> detailsPage.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found, no live content playing." + e);
        }

        Assert.assertFalse(detailsPage.compareEpisodeNum(), "Expected: Current episode number does not match new episode number.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73873"})
    @Test(description = "Verify Anthology Series - Title, Description, Date", groups = {"Anthology"})
    public void verifyAnthologyTitleDescriptionDate() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.getMediaTitle().equalsIgnoreCase(DANCING_WITH_THE_STARS),
                "Media title of logo image does not match " + DANCING_WITH_THE_STARS);
        sa.assertTrue(details.doesMetadataYearContainDetailsTabYear(), "Metadata label date year not found and does not match details tab year.");
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Content Description not found.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73874"})
    @Test(description = "Verify Anthology Series - Episode Download", groups = {"Anthology"})
    public void verifyAnthologyEpisodeDownload() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        //Download episode
        details.isOpened();
        String mediaTitle = details.getMediaTitle();
        details.startDownload();
        sa.assertTrue(details.isSeriesDownloadButtonPresent(), "Series download button not found.");

        //Wait for download to complete and validate titles same.
        details.waitForLongSeriesDownloadToComplete(180, 9);
        details.clickDownloadsIcon();
        sa.assertTrue(downloads.isOpened(), "Downloads page was not opened.");
        sa.assertTrue(mediaTitle.equalsIgnoreCase(downloads.getTypeOtherByLabel(DANCING_WITH_THE_STARS).getText()),
                DANCING_WITH_THE_STARS + " titles are not the same.");
        sa.assertTrue(downloads.getStaticTextByLabelContains("1 Episode").isPresent(), "1 episode was not found.");

        //Play downloaded episode
        downloads.getDynamicIosClassChainElementTypeImage(DANCING_WITH_THE_STARS).click();
        downloads.getTypeButtonContainsLabel("Play").click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not launch.");

        videoPlayer.clickBackButton();
        sa.assertTrue(downloads.getProgressBar().isPresent(), "Progress bar not found.");

        //Remove Download
        downloads.clickEditButton();
        downloads.clickUncheckedCheckbox();
        sa.assertTrue(downloads.isCheckedCheckboxPresent(), "Checked checkbox is not found.");
        sa.assertTrue(downloads.getStaticTextByLabelContains("1 Selected").isPresent(), "1 Select is not found");
        downloads.clickDeleteDownloadButton();
        sa.assertTrue(downloads.isDownloadsEmptyHeaderPresent(), "Download was not removed, empty header not present.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73875"})
    @Test(description = "Verify Anthology Series - VOD Progress", groups = {"Anthology"})
    public void verifyAnthologyVODProgress() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ PLAY + " label not found, currently live content playing. " + e);
        }

        details.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), "Video Player did not launch.");

        videoPlayer.clickBackButton();
        sa.assertTrue(details.isContinueButtonPresent(), "Continue button was not found.");
        sa.assertTrue(details.getProgressBar().isPresent(), "Progress found not found.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73881", "XMOBQA-72290"})
    @Test(description = "Verify Anthology Series - Details Tab", groups = {"Anthology"})
    public void verifyAnthologyDetailsTab() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        String mediaTitle = details.getMediaTitle();

        if ("Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            new IOSUtils().swipeInContainer(null, Direction.UP, 1000);
        }
        sa.assertTrue(details.getDetailsTab().isPresent(), "Details tab is not found.");

        String selectorSeason = details.getSeasonSelector();
        details.clickDetailsTab();
        new IOSUtils().swipePageTillElementPresent(details.getFormats(), 3, null, IMobileUtils.Direction.UP, 600);
        sa.assertTrue(details.getDetailsTabTitle().contains(mediaTitle), "Details tab title does not match media title.");
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Details Tab description not present");
        sa.assertTrue(details.isReleaseDateDisplayed(), "Detail Tab rating is not present");
        sa.assertTrue(details.getDetailsTabSeasonRating().contains(selectorSeason),
                "Details Tab season rating does not contain season selector text.");
        sa.assertTrue(details.isGenreDisplayed(), "Details Tab genre is not present.");
        sa.assertTrue(details.areFormatsDisplayed(), "Details Tab formats are not present.");
        sa.assertTrue(details.areActorsDisplayed(), "Details tab starring actors not present.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73872", "XMOBQA-72293"})
    @Test(description = "Verify Anthology Series - Suggested Tab", groups = {"Anthology"})
    public void verifyAnthologySuggestedTab() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isSuggestedTabPresent(), "Suggested tab was not found.");
        details.compareSuggestedTitleToMediaTitle(sa);
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72291", "XMOBQA-73871"})
    @Test(description = "Verify Anthology Series - Extras Tab", groups = {"Anthology"})
    public void verifyAnthologyExtrasTab() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isExtrasTabPresent(), "Extras tab was not found.");
        details.compareExtrasTabToPlayerTitle(sa);
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72654"})
    @Test(description = "Verify Anthology Series - No Group Watch During Live Event", groups = {"Anthology"})
    public void verifyAnthologyNoGroupWatchLive() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found, no live content playing. " + e);
        }

        Assert.assertFalse(details.isGroupWatchButtonDisplayed(), "Group Watch was found during live event.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73936"})
    @Test(description = "Verify Anthology Series - No Group Watch VOD", groups = {"Anthology"})
    public void verifyAnthologyNoGroupWatchVOD() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, play button not found, currently live content playing." + e);
        }

        Assert.assertFalse(details.isGroupWatchButtonDisplayed(), "Group Watch was found during VOD state.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72298"})
    @Test(description = "Verify Anthology Series - Featured VOD", groups = {"Anthology"})
    public void verifyAnthologyFeaturedVOD() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, play button not found, currently live content playing." + e);
        }

        sa.assertTrue(details.isLogoImageDisplayed(), "Logo image is not present.");
        sa.assertTrue(details.isHeroImagePresent(), "Hero image is not present.");
        sa.assertTrue(details.getStaticTextByLabelContains("TV-PG").isPresent(), "TV-MA rating was not found.");
        sa.assertTrue(details.getStaticTextByLabelContains("HD").isPresent(), "HD was not found.");
        sa.assertTrue(details.getStaticTextByLabelContains("5.1").isPresent(), "5.1 was not found.");
        sa.assertTrue(details.getStaticTextByLabelContains("Subtitles for the Deaf and Hearing Impaired").isPresent(), "Subtitles advisory was not found.");
        sa.assertTrue(details.getStaticTextByLabelContains("Audio Descriptions").isPresent(), "Audio description advisory was not found.");
        sa.assertTrue(details.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(details.isWatchlistButtonDisplayed(), "Watchlist button is not displayed.");
        sa.assertTrue(details.isPlayButtonDisplayed(), "Play button is not found.");

        details.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();
        sa.assertTrue(details.isContinueButtonPresent(), "Continue button is not present after exiting playback.");
        sa.assertTrue(details.isProgressBarPresent(), "Progress bar is not present after exiting playback.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73863"})
    @Test(description = "Verify Anthology Series - Trailer", groups = {"Anthology"})
    public void verifyAnthologyTrailer() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isTrailerButtonDisplayed(), "Trailer button was not found.");

        details.getTrailerButton().click();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not launch.");

        videoPlayer.waitForTrailerToEnd(75, 5);
        sa.assertTrue(details.isOpened(), "After trailer ended, not returned to Details page.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72297"})
    @Test(description = "Verify Anthology Series - Live Modal", groups = {"Anthology"})
    public void verifyAnthologyLiveModal() {
        initialSetup();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusLiveEventModalIOSPageBase liveEventModal = initPage(DisneyPlusLiveEventModalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 2).until(it -> details.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found, no live content playing. " + e);
        }

        details.clickWatchButton();
        liveEventModal.isOpened();
        sa.assertTrue(liveEventModal.isTitleLabelPresent(), "Title label not found.");
        sa.assertTrue(liveEventModal.isSubtitleLabelPresent(), "Subtitle label is not present.");
        sa.assertTrue(liveEventModal.isThumbnailViewPresent(), "Thumbnail view is not present.");
        sa.assertTrue(liveEventModal.getWatchLiveButton().isPresent(), "Watch live button is not present.");
        sa.assertTrue(liveEventModal.getWatchFromStartButton().isPresent(), "Watch from start button is not present.");

        videoPlayer.compareWatchLiveToWatchFromStartTimeRemaining(sa);

        sa.assertAll();
    }

    private void searchAndOpenDWTSDetails() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        homePage.clickSearchIcon();
        search.searchForMedia(DANCING_WITH_THE_STARS);
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
    }
}

