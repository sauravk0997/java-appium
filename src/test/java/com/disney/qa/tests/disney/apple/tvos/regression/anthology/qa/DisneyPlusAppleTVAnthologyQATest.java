package com.disney.qa.tests.disney.apple.tvos.regression.anthology.qa;

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

public class DisneyPlusAppleTVAnthologyQATest extends DisneyPlusAppleTVBaseTest {

    //Test constants
    private static final String UPCOMING = "UPCOMING";
    private static final String LIVE = "LIVE";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105996"})
    @Test(description = "Verify Anthology Series - Upcoming Badge and Metadata", groups = {"Anthology"})
    public void verifyAnthologyUpcomingBadgeAndMetadata() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);

//        logIn(entitledUser);
        QALogin();
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isStaticTextLabelPresent(UPCOMING));
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
        }

        sa.assertTrue(details.isContentDetailsPagePresent(), "Details page did not open.");
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

        logIn(entitledUser);
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.doesAiringBadgeContainLive());
        } catch (Exception e) {
            throw new SkipException("Skipping test, "+ LIVE + " label not found, no live content airing. " + e);
        }

        details.validateLiveProgress(sa);
        sa.assertTrue(details.isProgressBarPresent(), "Progress bar is not found.");
        sa.assertTrue(details.doesAiringBadgeContainLive(), "Airing badge does not contain 'live' on Details Page");

        details.clickQAWatchButton();
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

//        logIn(entitledUser);
        QALogin();
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isQAWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found, no live content playing. " + e);
        }

        details.clickQAWatchButton();
        liveEventModal.getQAWatchLiveButton().click();
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

//        logIn(entitledUser);
        QALogin();
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isQAWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found, no live content playing. " + e);
        }

        Assert.assertFalse(details.compareEpisodeNum(), "Episode number are the same");
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

//        logIn(entitledUser);
        QALogin();
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isQAWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found, no live content playing. " + e);
        }

        details.clickQAWatchButton();
        sa.assertTrue(liveEventModal.isTitleLabelPresent(), "Title label not found.");
        sa.assertTrue(liveEventModal.isQASubtitleLabelPresent(), "Subhead line label is not present.");
        sa.assertTrue(liveEventModal.isQAThumbnailViewPresent(), "Thumbnail view is not present.");
        sa.assertTrue(liveEventModal.getQAWatchLiveButton().isPresent(), "Watch live button is not present.");
        sa.assertTrue(liveEventModal.getQAWatchFromStartButton().isPresent(), "Watch from start button is not present.");

        videoPlayer.compareQAWatchLiveToWatchFromStartTimeRemaining(sa);
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
        detailsPage.isContentDetailsPagePresent();
    }

    private void QALogin() {
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        welcomePage.isOpened();
        welcomePage.clickLogInButton();
        loginPage.isOpened();
        loginPage.proceedToLocalizedPasswordScreen("cristina.solmaz+43753@disneyplustesting.com");
        passwordPage.logInWithPasswordLocalized("G0Disney!");
    }
}
