package com.disney.qa.tests.disney.apple.tvos.regression.anthology;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.Maintainer;
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

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106662"})
    @Test(description = "Verify Anthology Series - Watchlist", groups = {"Anthology"})
    public void verifyAnthologyWatchlist() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchList = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage search = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
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

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106657"})
    @Test(description = "Verify Anthology Series - Search", groups = {"Anthology"})
    public void verifyAnthologySearch() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage search = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
        home.isOpened();
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        search.isOpened();
        search.typeInSearchField(DANCING_WITH_THE_STARS.getTitle());
        sa.assertTrue(search.getDynamicCellByLabel(DANCING_WITH_THE_STARS.getTitle()).isElementPresent(), "Dancing with the Stars title not found in search results.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105996"})
    @Test(description = "Verify Anthology Series - Upcoming Badge and Metadata", groups = {"Anthology"})
    public void verifyAnthologyUpcomingBadgeAndMetadata() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> details.isStaticTextLabelPresent(UPCOMING));
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
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
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> details.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
        }

        sa.assertTrue(details.getLiveNowBadge().isElementPresent(), "Live Now badge was not found.");
        sa.assertTrue(details.getLiveProgress().isElementPresent() || details.getLiveProgressTime().isElementPresent(),
                "Live progress indicator not found.");
        sa.assertTrue(details.doesAiringBadgeContainLive(), "Airing badge does not contain 'live' on Details Page");

        details.clickWatchButton();
        sa.assertTrue(liveEventModal.doesAiringBadgeContainLive(), "Airing badge does not contain 'live' on Live Event Modal");
        liveEventModal.clickDetailsButton();
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105997"})
    @Test(description = "Verify Anthology Series - Live Playback", groups = {"Anthology"})
    public void verifyAnthologyLivePlayback() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVLiveEventModalPage liveEventModal = new DisneyPlusAppleTVLiveEventModalPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> details.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
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
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
        searchAndOpenDWTSDetails();
        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> details.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
        }

        Assert.assertFalse(details.compareEpisodeNum(), "Episode number are the same");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-110034"})
    @Test(description = "Verify Anthology Series - Title, Description, Date", groups = {"Anthology"})
    public void verifyAnthologyTitleDescriptionDate() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
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
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
        searchAndOpenDWTSDetails();

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-106679"})
    @Test(description = "Verify Anthology Series - No Group Watch During Live Event", groups = {"Anthology"})
    public void verifyAnthologyNoGroupWatchLive() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> details.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found. " + e);
        }

        Assert.assertFalse(details.isGroupWatchButtonDisplayed(), "Group watch was found during live event.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-110153"})
    @Test(description = "Verify Anthology Series - No Group Watch VOD", groups = {"Anthology"})
    public void verifyAnthologyNoGroupWatchVOD() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getCastedDriver(), 15, 1).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, play button not found. " + e);
        }

        Assert.assertFalse(details.isGroupWatchButtonDisplayed(), "Group watch was found during VOD state.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-105988", "XCDQA-110055"})
    @Test(description = "Verify Anthology Series - Details Tab", groups = {"Anthology"})
    public void verifyAnthologyDetailsTab() {
        DisneyPlusAppleTVDetailsPage details = new DisneyPlusAppleTVDetailsPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
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
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);


        logIn(entitledUser);
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
        DisneyAccount entitledUser = disneyAccountApi.createAccount(offer, country, language, SUB_VERSION);

        logIn(entitledUser);
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isExtrasTabPresent(), "Extras tab was not found.");
        details.compareExtrasTabToPlayerTitle(sa);
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
