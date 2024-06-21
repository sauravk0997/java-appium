package com.disney.qa.tests.disney.apple.tvos.regression.anthology;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.api.disney.DisneyContentIds.DANCING_WITH_THE_STARS;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST;

public class DisneyPlusAppleTVAnthologyTest extends DisneyPlusAppleTVBaseTest {

    //Test constants
    private static final String UPCOMING = "UPCOMING";
    private static final String LIVE = "LIVE";
    private static final String PLAY = "PLAY";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106662"})
    @Test(description = "Verify Anthology Series - Watchlist", groups = {"Anthology"})
    public void verifyAnthologyWatchlist() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchList = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage search = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();
        details.addToWatchlist();
        details.clickMenuTimes(1,1);
        pause(1); //from transition to search bar
        search.clickMenuTimes(1,1);
        home.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        sa.assertTrue(watchList.areWatchlistTitlesDisplayed(DANCING_WITH_THE_STARS.getTitle()), "Dancing With The Stars was not added to watchlist.");

        watchList.getDynamicCellByLabel(DANCING_WITH_THE_STARS.getTitle()).click();
        sa.assertTrue(details.isOpened(), DANCING_WITH_THE_STARS.getTitle() + " details page did not load.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106657"})
    @Test(description = "Verify Anthology Series - Search", groups = {"Anthology"})
    public void verifyAnthologySearch() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage search = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        home.isOpened();
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        search.isOpened();
        search.typeInSearchField(DANCING_WITH_THE_STARS.getTitle());
        sa.assertTrue(search.getDynamicCellByLabel(DANCING_WITH_THE_STARS.getTitle()).isElementPresent(), "Dancing with the Stars title not found in search results.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105996"})
    @Test(description = "Verify Anthology Series - Upcoming Badge and Metadata", groups = {"Anthology"})
    public void verifyAnthologyUpcomingBadgeAndMetadata() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isStaticTextLabelPresent(UPCOMING));
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ UPCOMING + " label not found. " + e);
        }

        sa.assertTrue(details.isOpened(), "Details page did not open.");
        sa.assertTrue(details.getAiringBadgeLabel().isElementPresent(), "Airing badge label is not displayed.");
        sa.assertTrue(details.getUpcomingDateTime().isElementPresent(), "Upcoming Date and Time was not found.");
        sa.assertTrue(details.getUpcomingTodayBadge().isElementPresent() || details.getUpcomingBadge().isElementPresent(),
                "Upcoming Today / Upcoming badge is not present");
        sa.assertTrue(details.getAiringBadgeLabel().isElementPresent(), "Upcoming airing Badge is not present.");
        sa.assertTrue(details.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(details.isWatchlistButtonDisplayed(), "Watchlist button is not displayed.");
        sa.assertTrue(details.isLogoImageDisplayed(), "Logo image is not displayed.");
        sa.assertTrue(details.isHeroImagePresent(), "Hero image is not displayed.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105994"})
    @Test(description = "Verify Anthology Series - Live Badge and Airing Indicator", groups = {"Anthology"})
    public void verifyAnthologyLiveBadge() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.doesAiringBadgeContainLive());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ LIVE + " label not found, no live content airing. " + e);
        }

        details.validateLiveProgress(sa);
        sa.assertTrue(details.isProgressBarPresent(), "Progress bar is not found.");
        sa.assertTrue(details.doesAiringBadgeContainLive(), "Airing badge does not contain 'live' on Details Page");

        details.clickWatchButton();
        sa.assertTrue(liveEventModal.doesAiringBadgeContainLive(), "Airing badge does not contain 'live' on Live Event Modal");
        liveEventModal.validateLiveProgress(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105997"})
    @Test(description = "Verify Anthology Series - Live Playback", groups = {"Anthology"})
    public void verifyAnthologyLivePlayback() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found, no live content playing. " + e);
        }

        details.clickWatchButton();
        details.getStaticTextByLabelContains("WATCH").click();
        liveEventModal.clickWatchLiveButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.pauseAndPlayVideo();
        sa.assertTrue(videoPlayer.isWatchingLivePresent(), "Watching live lightning bolt is not present.");
        sa.assertTrue(videoPlayer.isOpened(), "Live video is not playing");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-110033"})
    @Test(description = "Verify Anthology Series - Ended, Compare episode number", groups = {"Anthology"})
    public void verifyAnthologyEnded() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found, no live content playing. " + e);
        }

        Assert.assertFalse(details.compareEpisodeNum(), "Episode number are the same");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-110034"})
    @Test(description = "Verify Anthology Series - Title, Description, Date", groups = {"Anthology"})
    public void verifyAnthologyTitleDescriptionDate() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.getLogoImage().isPresent(), DANCING_WITH_THE_STARS.getTitle() + "logo image was not found.");
        sa.assertTrue(details.doesMetadataYearContainDetailsTabYear(), "Metadata label date year not found and does not match details tab year.");
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Content Description not found.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-110036"})
    @Test(description = "Verify Anthology Series - VOD Progress", groups = {"Anthology"})
    public void verifyAnthologyVODProgress() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ PLAY + " label not found, currently live content playing. " + e);
        }

        details.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open after clicking play button.");
        videoPlayer.clickMenuTimes(1,1);
        sa.assertTrue(details.isOpened(), "Details page did not open.");
        details.clickContinueButton();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open after clicking continue button.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105988", "XCDQA-110055"})
    @Test(description = "Verify Anthology Series - Details Tab", groups = {"Anthology"})
    public void verifyAnthologyDetailsTab() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();

        String mediaTitle = details.getMediaTitle();
        sa.assertTrue(details.isOpened(), "Details page did not open.");
        sa.assertTrue(details.getDynamicRowButtonLabel("DETAILS", 1).isElementPresent(), "Details tab is not found.");

        details.moveDown(1,1);
        details.moveRight(3,1);
        details.isFocused(details.getDetailsTab());
        sa.assertTrue(details.getDetailsTabTitle().contains(mediaTitle), "Details tab title does not match media title.");
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Details tab content description is not present.");
        sa.assertTrue(details.isReleaseDateDisplayed(), "Release date is not present.");
        sa.assertTrue(details.isGenreDisplayed(), "Genre is not present.");
        sa.assertTrue(details.isSeasonRatingPresent(), "Season rating is not present.");
        sa.assertTrue(details.areFormatsDisplayed(), "Formats are not present.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105998", "XCDQA-110054"})
    @Test(description = "Verify Anthology Series - Suggested Tab", groups = {"Anthology"})
    public void verifyAnthologySuggestedTab() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isSuggestedTabPresent(), "Suggested tab was not found.");
        details.compareSuggestedTitleToDetailsTabTitle(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105989", "XCDQA-110046"})
    @Test(description = "Verify Anthology Series - Extras Tab", groups = {"Anthology"})
    public void verifyAnthologyExtrasTab() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isExtrasTabPresent(), "Extras tab was not found.");
        details.compareExtrasTabToPlayerTitle(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105993"})
    @Test(description = "Verify Anthology Series - Featured VOD", groups = {"Anthology"})
    public void verifyAnthologyFeaturedVOD() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, play button not found, currently live content playing. " + e);
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
        details.clickMenuTimes(1,1);
        details.isOpened();
        sa.assertTrue(details.doesContinueButtonExist(), "Continue button not displayed after exiting playback.");
        sa.assertTrue(details.isProgressBarPresent(), "Progress bar is not present after exiting playback.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-109938"})
    @Test(description = "Verify Anthology Series - Trailer", groups = {"Anthology"})
    public void verifyAnthologyTrailer() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isTrailerButtonDisplayed(), "Trailer button was not found.");

        details.getTrailerButton().click();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open.");

        videoPlayer.waitForTvosTrailerToEnd(75, 5);
        sa.assertTrue(details.isOpened(), "After trailer completed, did not return to details page.");
        sa.assertTrue(details.isFocused(details.getTrailerButton()), "Trailer button is not focused on.");
        sa.assertAll();
    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106001"})
    @Test(description = "Verify Anthology Series - Live Modal", groups = {"Anthology"})
    public void verifyAnthologyLiveModal() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

        logInTemp(entitledUser);
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found, no live content playing. " + e);
        }

        details.clickWatchButton();
        sa.assertTrue(liveEventModal.isTitleLabelPresent(), "Title label not found.");
        sa.assertTrue(liveEventModal.isSubheadLineLabelPresent(), "Subhead line label is not present.");
        sa.assertTrue(liveEventModal.isThumbnailViewPresent(), "Thumbnail view is not present.");
        sa.assertTrue(liveEventModal.getWatchLiveButton().isPresent(), "Watch live button is not present.");
        sa.assertTrue(liveEventModal.getWatchFromStartButton().isPresent(), "Watch from start button is not present.");

        videoPlayer.compareWatchLiveToWatchFromStartTimeRemaining(sa);
        sa.assertAll();
    }

    private void searchAndOpenDWTSDetails() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        homePage.isOpened();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.isOpened();
        searchPage.typeInSearchField(DANCING_WITH_THE_STARS.getTitle());
        searchPage.clickSearchResult(DANCING_WITH_THE_STARS.getTitle());
        detailsPage.isOpened();
    }
}
