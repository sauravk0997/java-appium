package com.disney.qa.tests.disney.apple.tvos.regression.details;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.explore.response.*;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.common.constant.*;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.*;
import com.zebrunner.agent.core.annotation.*;
import com.zebrunner.carina.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.time.temporal.ValueRange;
import java.util.*;

import static com.disney.qa.api.disney.DisneyEntityIds.DAREDEVIL_BORN_AGAIN;
import static com.disney.qa.api.disney.DisneyEntityIds.LOKI;
import static com.disney.qa.common.DisneyAbstractPage.ONE_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.TEN_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.THREE_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.TWENTY_FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVDetailsSeriesTest extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SEARCH_PAGE_ERROR_MESSAGE = "Search page did not open";
    private static final String CONTENT_ERROR_MESSAGE = "Content is not found";
    private static final String SUGGESTED = "SUGGESTED";
    private static final long SCRUB_PERCENTAGE_FIFTY = 50;
    private static final long SCRUB_PERCENTAGE_SIXTY = 60;
    private static final long SCRUB_PERCENTAGE_HUNDRED = 100;

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64981"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifySeriesDetailsPageSuggestedTab() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        String series = LOKI.getTitle();

        logIn(getUnifiedAccount());

        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);
        searchPage.typeInSearchField(series);
        Assert.assertTrue(searchPage.getStaticTextByLabelContains(series).isPresent(), CONTENT_ERROR_MESSAGE);
        searchPage.getSearchResults(series).get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        detailsPage.moveDown(1, 1);
        detailsPage.moveRight(1, 1);
        Assert.assertTrue(detailsPage.isFocused(detailsPage.getSuggestedTab()), "Suggested tab was not focussed");
        detailsPage.moveDown(1, 1);

        ArrayList<Container> lokiPageDetails = getDisneyAPIPage(LOKI.getEntityId(), false);
        List<Item> list = lokiPageDetails.stream()
                .filter(container -> container.getVisuals().getName().equals(SUGGESTED))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("suggested container not present in API response"))
                .getItems().subList(0, 1);

        list.forEach(i -> {
            Assert.assertTrue(detailsPage.isFocused(detailsPage.getTypeCellLabelContains(i.getVisuals().getTitle())),
                    "Suggested title was not focussed");
            detailsPage.moveRight(1, 1);
        });
        detailsPage.clickSelect();
        detailsPage.waitForDetailsPageToOpen();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64975"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifySeriesDetailsExtraTab() {
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        logIn(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.moveDown(1, 1);
        // Navigate to extra tab and play first asset
        Assert.assertTrue(detailsPage.isExtrasTabPresent(), EXTRAS_TAB_NOT_DISPLAYED);
        detailsPage.moveRightUntilElementIsFocused(detailsPage.getExtrasTab(), 6);
        detailsPage.moveDown(1, 1);
        String extraTitle = detailsPage.getExtraEpisodeTitle();
        LOGGER.info("Extra title: {}", extraTitle);
        detailsPage.clickSelect();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        Assert.assertTrue(videoPlayer.getTitleLabel().contains(extraTitle),
                "Playback is not initiated for the extra content expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64899"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifySeriesDetailsVODBookmarkRefresh() {
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        String continueWatchingCollection = CollectionConstant
                .getCollectionName(CollectionConstant.Collection.CONTINUE_WATCHING);
        int maxCount = 20;
        String continueButton = "CONTINUE";

        logInWithoutHomeCheck(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        //Populate continue watching collection
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_daredevil_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();

        // Forward video and get remaining time
        commonPage.clickRight(4, 1, 1);
        videoPlayer.waitForVideoToStart();
        commonPage.clickDown(1);
        commonPage.clickSelect();
        String remainingTime = videoPlayer.getRemainingTimeInDetailsFormatString();
        LOGGER.info("remainingTime {}", remainingTime);

        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));

        //Navigate to continue watching collection
        homePage.waitForHomePageToOpen();
        commonPage.moveDownUntilCollectionContentIsFocused(continueWatchingCollection, maxCount);
        commonPage.clickSelect();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.getTypeButtonContainsLabel(continueButton).isPresent(),
                "Continue button was not present on details page");
        Assert.assertTrue(detailsPage.getContinueWatchingTimeRemaining().getText().contains(remainingTime),
                "Correct remaining time is not reflecting in progress bar on details page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64877"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifySeriesDetailsPageLanding() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.isFocused(detailsPage.getPlayButton()),
                "Play button was not focused when details page opened");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64962"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifyEpisodesTabBackButtonBehavior() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        //Move down to first episode and validate back button change focus to Episodes tab
        detailsPage.moveDown(2, 1);
        Assert.assertFalse(detailsPage.isFocused(detailsPage.getEpisodesTab()), "Episodes tab is focused");
        detailsPage.clickBack();
        Assert.assertTrue(detailsPage.isFocused(detailsPage.getEpisodesTab()), "Episodes tab is not focused");

        //Validate back button changes focus to Play/Continue CTA
        detailsPage.clickBack();
        Assert.assertTrue(detailsPage.isFocused(detailsPage.getPlayOrContinueButton()),
                "Play/Continue CTA is not focused");

        //Validate back button redirects to Home page
        detailsPage.clickBack();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64901"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifySeriesDetailsTabContent() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        SoftAssert sa = new SoftAssert();
        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_daredevil_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.moveDown(1, 1);
        detailsPage.moveRightUntilElementIsFocused(detailsPage.getDetailsTab(), 6);
        sa.assertTrue(detailsPage.isDetailsTabTitlePresent(), "Title is not present under Details Tab");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Description is not present under Details Tab");
        sa.assertTrue(detailsPage.isReleaseDateDisplayed(), "Release date is not present under Details Tab");
        sa.assertTrue(detailsPage.isGenreDisplayed(), "Genre is not present under Details Tab");
        sa.assertTrue(detailsPage.isRatingPresent(), "Rating is not present under Details Tab");
        sa.assertTrue(detailsPage.areFormatsDisplayed(), "Formats are not present under Details Tab");
        sa.assertTrue(detailsPage.isCreatorDirectorDisplayed(), "Creator is not present under Details Tab");
        sa.assertTrue(detailsPage.areActorsDisplayed(), "Actors are not present under Details Tab");
        sa.assertEquals(detailsPage.getQuantityOfActors(), 6, "Expected quantity of actors is incorrect under Details Tab");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64883"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifySeriesDetailsPageTrailerButton() {
        String trailer = "Trailer";
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(DEEPLINKURL.concat(DAREDEVIL_BORN_AGAIN.getEntityId()));
        Visuals visualsResponse = getExploreAPIPageVisuals(DAREDEVIL_BORN_AGAIN.getEntityId());

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.isTrailerButtonDisplayed(), TRAILER_BTN_NOT_DISPLAYED);
        detailsPage.getTrailerButton().click();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        String title = videoPlayer.getTitleLabel();
        Assert.assertTrue(title.contains(trailer) && title.contains(visualsResponse.getTitle()),
                "Expected Trailer not playing");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64881"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifySeriesDetailsPagePlayButtonBehavior() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        ExploreContent seriesApiContent = getSeriesApi(LOKI.getEntityId(), DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String firstEpisodeTitle;
        try {
            firstEpisodeTitle = seriesApiContent.getSeasons().get(0).getItems().get(0)
                    .getVisuals().getEpisodeTitle();
        } catch (Exception e) {
            throw new SkipException("Unable to fetch first episode title from Explore API", e);
        }

        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        commonPage.clickDown(2);
        Assert.assertTrue(videoPlayer.getSubtitleLabelElement().getText().contains(firstEpisodeTitle),
                "Video Player subtitle doesn't contains expected episode title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64887"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifySeriesDetailsPageContinueButtonBehavior() {
        int uiLatencyInSeconds = 10;
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        // Play an episode through deeplink, fast-forward a couple of times, pause playback and save remaining time
        launchDeeplink(R.TESTDATA.get("disney_prod_series_loki_first_episode_playback_deeplink"));
        videoPlayer.waitForVideoToStart(TEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT);
        commonPage.clickRight(3, 2, 1);
        videoPlayer.clickPlay();
        int remainingTimeAfterForward = videoPlayer.getRemainingTimeThreeIntegers();
        videoPlayer.waitForElementToDisappear(videoPlayer.getSeekbar(), TEN_SEC_TIMEOUT);

        // Restart app, move to 'Continue Watching' collection, select bookmarked series and resume episode playback
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));
        homePage.waitForHomePageToOpen();
        videoPlayer.moveDownUntilCollectionContentIsFocused(
                CollectionConstant.getCollectionName(CollectionConstant.Collection.CONTINUE_WATCHING), 20);
        videoPlayer.clickSelect();
        detailsPage.clickContinueButton();

        // Pause playback and validate elapsed time difference using a range to avoid problems due to latency
        videoPlayer.waitForVideoToStart(TEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT);
        videoPlayer.clickPlay();
        int remainingTimeAfterAppRelaunch = videoPlayer.getRemainingTimeThreeIntegers();
        ValueRange acceptableDeltaRange = ValueRange.of(0, uiLatencyInSeconds);
        Assert.assertTrue(
                acceptableDeltaRange.isValidIntValue(
                        Math.abs(remainingTimeAfterAppRelaunch - remainingTimeAfterForward)),
                String.format("The difference between the remaining time after fast-forwarding (%d seconds) and " +
                                "the remaining time after the relaunch and resume of content (%d seconds) " +
                                "is not between the expected range (%d-%d seconds)",
                        remainingTimeAfterForward, remainingTimeAfterAppRelaunch,
                        acceptableDeltaRange.getMinimum(), acceptableDeltaRange.getMaximum()));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-68346"})
    @Test(groups = {TestGroup.SERIES, TestGroup.SMOKE, US})
    public void verifySeriesUpNextPlayButtonBehavior() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVUpNextPage upNextPage = new DisneyPlusAppleTVUpNextPage(getDriver());
        String nextEpisodeTitle = "";
        logIn(getUnifiedAccount());

        // Get second episode title
        try {
            ExploreContent seriesApiContent =
                    getSeriesApi(R.TESTDATA.get("disney_prod_series_bluey_mini_episodes_entity"),
                            DisneyPlusBrandIOSPageBase.Brand.DISNEY);
            nextEpisodeTitle =
                    seriesApiContent.getSeasons().get(0).getItems().get(1).getVisuals().getEpisodeTitle();
        } catch (Exception e) {
            throw new SkipException("Skipping test, series title was not found" + e.getMessage());
        }

        // Play first episode
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_series_bluey_mini_episodes_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        // Scrub to the end and start next episode
        commonPage.clickRight(6, 1, 1);
        videoPlayer.waitForPresenceOfAnElement(upNextPage.getUpNextPlayButton());
        Assert.assertTrue(upNextPage.getUpNextPlayButton().isPresent(), "Up Next button is not present");
        upNextPage.getUpNextPlayButton().click();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickDown();
        Assert.assertTrue(videoPlayer.getStaticTextByLabelContains(nextEpisodeTitle).isPresent(),
                "Playback is not initiated for expected episode");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64885"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifyBookmarkSeriesDetailsAppearance() {
        String episodeTitle, episodeBriefDesc;
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        SoftAssert sa = new SoftAssert();
        logIn(getUnifiedAccount());

        String seriesDeeplink = R.TESTDATA.get("disney_prod_series_detail_loki_deeplink");
        ExploreContent seriesApiContent = getSeriesApi(R.TESTDATA.get("disney_prod_loki_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        try {
            Visuals seasonDetails = seriesApiContent.getSeasons().get(0).getItems().get(0).getVisuals();
            episodeTitle = seasonDetails.getEpisodeTitle();
            episodeBriefDesc = seasonDetails.getDescription().getBrief();
        } catch (Exception e) {
            throw new SkipException("Skipping test, Episode title and description not found" + e.getMessage());
        }

        //Populate continue watching collection
        launchDeeplink(seriesDeeplink);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();

        // Forward video and get remaining time
        commonPage.clickRight(4, 1, 1);
        videoPlayer.waitForVideoToStart();
        commonPage.clickPlay();
        String remainingTime = videoPlayer.getRemainingTimeInDetailsFormatString();
        LOGGER.info("remainingTime {}", remainingTime);
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));

        homePage.waitForHomePageToOpen();
        launchDeeplink(seriesDeeplink);
        detailsPage.waitForDetailsPageToOpen();
        sa.assertTrue(detailsPage.getStaticTextByLabel(
                        detailsPage.getEpisodeTitleWithSeasonAndEpisodeNumber(episodeTitle)).isPresent(),
                "Bookmark episode title not displayed");
        sa.assertTrue(detailsPage.getStaticTextByLabel(episodeBriefDesc).isPresent(),
                "Bookmark episode description not displayed");
        sa.assertTrue(detailsPage.getProgressBar().isPresent(), "Progress bar is not present");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().isPresent(),
                "Continue watching time remaining is not present");
        sa.assertTrue(detailsPage.getContinueWatchingTimeRemaining().getText().contains(remainingTime),
                "Correct remaining time is not reflecting in progress bar on details page");
        sa.assertTrue(detailsPage.isContinueButtonPresent(), CONTINUE_BTN_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.getRestartButton().isPresent(), RESTART_BTN_NOT_DISPLAYED);
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), WATCHLIST_BTN_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64956"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SERIES, US})
    public void verifyEpisodeTabProgressBarUpdates() {
        int latency = 20;
        String episodeTitle;
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVUpNextPage upNextPage = new DisneyPlusAppleTVUpNextPage(getDriver());

        ExploreContent seriesApiContent = getSeriesApi(R.TESTDATA.get("disney_prod_series_bluey_mini_episodes_entity"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        try {
            episodeTitle = seriesApiContent.getSeasons()
                    .get(0)
                    .getItems()
                    .get(0)
                    .getVisuals()
                    .getEpisodeTitle();
        } catch (Exception e) {
            throw new SkipException("Skipping test, Episode title not found" + e.getMessage());
        }
        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_series_bluey_mini_episodes_playback_deeplink"));
        videoPlayer.waitForVideoToStart();
        int remainingTime = videoPlayer.getRemainingTimeThreeIntegers();
        commonPage.clickRight(6, 2, 1);
        videoPlayer.waitForPresenceOfAnElement(upNextPage.getUpNextPlayButton());
        commonPage.clickRight(2, 1, 1);
        commonPage.clickSelect();
        commonPage.clickPlay();
        videoPlayer.waitUntilRemainingTimeLessThan(TWENTY_FIVE_SEC_TIMEOUT, THREE_SEC_TIMEOUT, (remainingTime / 2) - latency);
        videoPlayer.clickBack();
        detailsPage.waitForDetailsPageToOpen();
        Assert.assertTrue(detailsPage.getProgressContainer().isPresent(),
                "Progress container view is not present");
        commonPage.clickDown();
        detailsPage.isProgressBarIndicatingCorrectPositionOnEpisodeTab(episodeTitle, SCRUB_PERCENTAGE_FIFTY, latency);

        commonPage.clickSelect();
        videoPlayer.waitForVideoToStart();
        commonPage.clickRight(2, 2, 1);
        videoPlayer.clickBack();
        detailsPage.isProgressBarIndicatingCorrectPositionOnEpisodeTab(episodeTitle, SCRUB_PERCENTAGE_SIXTY, latency);

        commonPage.clickSelect();
        videoPlayer.waitForVideoToStart();
        commonPage.clickRight(2, 2, 1);
        videoPlayer.waitUntilRemainingTimeLessThan(TWENTY_FIVE_SEC_TIMEOUT, THREE_SEC_TIMEOUT, remainingTime - 10);
        videoPlayer.clickBack();
        detailsPage.isProgressBarIndicatingCorrectPositionOnEpisodeTab(episodeTitle, SCRUB_PERCENTAGE_HUNDRED, latency);
    }
}
