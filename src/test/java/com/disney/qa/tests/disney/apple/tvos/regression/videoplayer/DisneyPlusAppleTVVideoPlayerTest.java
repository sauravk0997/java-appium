package com.disney.qa.tests.disney.apple.tvos.regression.videoplayer;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.explore.response.Flag;
import com.disney.qa.api.explore.response.MetastringParts;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusEspnIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.List;

import static com.disney.qa.api.disney.DisneyEntityIds.BLUEY_MINISODES;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVVideoPlayerTest extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SPORT_PAGE_DID_NOT_OPEN = "Sport page did not open";
    private static final String NO_REPLAYS_FOUND = "No replay events found";
    private static final String PLAYER_CONTROLS_NOT_DISPLAYED =
            "Player controls were not displayed when playback activated";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-120534"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULU, US})
    public void verifyServiceAttributionOnPlayBack() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        SoftAssert sa = new SoftAssert();
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_only_murders_in_the_building_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.isServiceAttributionLabelVisible(),
                "service attribution wasn't visible when video started");
        sa.assertTrue(videoPlayer.isTitleLabelDisplayed(),
                "Video player title wasn't visible when video started");
        sa.assertFalse(videoPlayer.getSeekbar().isPresent(ONE_SEC_TIMEOUT),
                "player controls were displayed when video started");
        videoPlayer.waitForElementToDisappear(videoPlayer.getSeekbar(), FIVE_SEC_TIMEOUT);
        sa.assertTrue(videoPlayer.isServiceAttributionLabelVisibleWithControls(),
                "service attribution wasn't visible along with controls");
        commonPage.clickDown(2);
        sa.assertTrue(videoPlayer.isTitleLabelDisplayed(),
                "Video player title wasn't visible along with controls");
        sa.assertTrue(videoPlayer.isSubTitleLabelDisplayed(),
                "Video player meta data title wasn't visible along with controls");
        sa.assertTrue(videoPlayer.isSeekbarVisible(), PLAYER_CONTROLS_NOT_DISPLAYED);
        commonPage.clickDown(2);
        sa.assertTrue(videoPlayer.getServiceAttributionLabel().getText().equals(HULU_SERVICE_ATTRIBUTION_MESSAGE),
                "Expected Hulu Service Attribution not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-120546"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULU, US})
    public void verifyServiceAttributionOnPlayBackFromContinueWatching() {
        String continueWatchingCollection =
                CollectionConstant.getCollectionName(CollectionConstant.Collection.CONTINUE_WATCHING);
        int maxCount = 20;
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_only_murders_in_the_building_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        commonPage.clickRight(4);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickDown();
        videoPlayer.clickLeft();

        terminateApp(sessionBundles.get(DISNEY));
        relaunch();
        videoPlayer.moveDownUntilCollectionContentIsFocused(continueWatchingCollection, maxCount);
        videoPlayer.clickSelect();
        detailsPage.clickContinueButton();
        sa.assertTrue(videoPlayer.isServiceAttributionLabelVisible(),
                "service attribution wasn't visible when video started");
        sa.assertFalse(videoPlayer.getSeekbar().isPresent(ONE_SEC_TIMEOUT),
                "player controls were displayed when video started");
        sa.assertTrue(videoPlayer.isTitleLabelDisplayed(),
                "Video player title wasn't visible when video started");
        videoPlayer.waitForElementToDisappear(videoPlayer.getSeekbar(), FIVE_SEC_TIMEOUT);
        sa.assertTrue(videoPlayer.isServiceAttributionLabelVisibleWithControls(),
                "service attribution wasn't visible along with controls");
        commonPage.clickDown(2);
        sa.assertTrue(videoPlayer.isTitleLabelDisplayed(),
                "Video player title wasn't visible along with controls");
        sa.assertTrue(videoPlayer.isSubTitleLabelDisplayed(),
                "Video player meta data title wasn't visible along with controls");
        sa.assertTrue(videoPlayer.isSeekbarVisible(), PLAYER_CONTROLS_NOT_DISPLAYED);
        commonPage.clickDown(2);
        sa.assertTrue(videoPlayer.getServiceAttributionLabel().getText().equals(HULU_SERVICE_ATTRIBUTION_MESSAGE),
                "Expected Hulu Service Attribution not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-99575"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PROFILES, TestGroup.SMOKE, US})
    public void verifyPlayBackRatingRestrictionErrorMessage() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        // Set lower rating
        List<String> ratingSystemValues = getUnifiedAccount().getProfile(DEFAULT_PROFILE).getAttributes()
                .getParentalControls().getMaturityRating().getRatingSystemValues();
        LOGGER.info("Lower rating: {}", ratingSystemValues.get(0));
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(), ratingSystemValues.get(0));

        logIn(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        launchDeeplink(R.TESTDATA.get("disney_prod_series_loki_first_episode_playback_deeplink"));

        Assert.assertFalse(videoPlayer.isOpened(),"Playback initiated for the mature content");
        Assert.assertTrue(detailsPage.getRatingRestrictionDetailMessage().isPresent(),
                "Rating restriction message was not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-120559"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyTrailerAction() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_trailer_kardashians_deeplink"));
        detailsPage.waitForDetailsPageToOpen();

        detailsPage.getTrailerActionButton().click();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        commonPage.clickDown(1);
        int remainingTimeBeforeForward = videoPlayer.getRemainingTimeThreeIntegers();
        LOGGER.info("Remaining time before forward: {}", remainingTimeBeforeForward);
        commonPage.clickRight(2, 1, 1);
        commonPage.clickDown(1);
        int remainingTimeAfterForward = videoPlayer.getRemainingTimeThreeIntegers();
        LOGGER.info("Remaining time after forward: {}", remainingTimeAfterForward);
        Assert.assertTrue(remainingTimeBeforeForward > remainingTimeAfterForward, "Video did not progress");

        // Go back to details page and start trailer
        homePage.clickMenuTimes(1, 1);
        if (!detailsPage.isOpened()) {
            homePage.clickMenuTimes(1, 1);
        }

        detailsPage.getTrailerActionButton().click();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        commonPage.clickDown(1);
        int remainingTimeTrailerRestart = videoPlayer.getRemainingTimeThreeIntegers();
        LOGGER.info("Remaining time after trailer restart: {}", remainingTimeTrailerRestart);
        Assert.assertTrue(remainingTimeTrailerRestart > remainingTimeAfterForward,
                "Trailer did not start from the beginning");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102801"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyVODReplay() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusEspnIOSPageBase espnPage = new DisneyPlusEspnIOSPageBase(getDriver());
        DisneyPlusAppleTVBrandsPage brandPage = new DisneyPlusAppleTVBrandsPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        SoftAssert sa = new SoftAssert();
        String basketball = "Basketball";
        String espn = "ESPN+";
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.getBrandCell(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN))
                .isPresent(), "ESPN brand tile was not present on home page screen");
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.clickBrandTile(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN));

        Assert.assertTrue(brandPage.isBrandScreenDisplayed(brandPage.getBrand(DisneyPlusAppleTVBrandsPage.Brand.ESPN)),
                ESPN_PAGE_DID_NOT_OPEN);

        // Navigate to Sports and basketball sport
        homePage.moveDownUntilCollectionContentIsFocused(
                CollectionConstant.getCollectionName(CollectionConstant.Collection.ESPN_SPORTS), 10);
        homePage.moveRightUntilElementIsFocused(detailsPage.getTypeCellLabelContains(basketball), 30);
        detailsPage.getTypeCellLabelContains(basketball).click();
        Assert.assertTrue(espnPage.isSportTitlePresent(basketball),
                SPORT_PAGE_DID_NOT_OPEN);

        // Navigate to a Replay and validate playback
        CollectionConstant.Collection replaysCollection = CollectionConstant.Collection.SPORT_REPLAYS;
        homePage.moveDownUntilCollectionContentIsFocused(CollectionConstant.getCollectionName(replaysCollection), 10);
        String replayTitle = detailsPage.getAllCollectionCells(replaysCollection).get(0).getText();
        if (replayTitle == null) {
            throw new IndexOutOfBoundsException(NO_REPLAYS_FOUND);
        }
        LOGGER.info("Replay title  {}", replayTitle);
        detailsPage.getTypeCellLabelContains(replayTitle).click();
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        sa.assertTrue(replayTitle.contains(videoPlayer.getTitleLabel()),
                "Video title does not match with the expected");
        Assert.assertTrue(videoPlayer.isNetworkWatermarkLogoPresent(espn), "ESPN Network watermark is not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102802"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyVodReplayResume() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusEspnIOSPageBase espnPage = new DisneyPlusEspnIOSPageBase(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        SoftAssert sa = new SoftAssert();
        String basketball = "Basketball";
        String continueButton = "CONTINUE";
        String replayTitle = "";
        int latency = 60;

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_basketball_sport_deeplink"));
        Assert.assertTrue(espnPage.isSportTitlePresent(basketball),
                SPORT_PAGE_DID_NOT_OPEN);

        // Navigate to a Replay and validate playback
        CollectionConstant.Collection replaysCollection = CollectionConstant.Collection.SPORT_REPLAYS;
        homePage.moveDownUntilCollectionContentIsFocused(CollectionConstant.getCollectionName(replaysCollection), 10);
        List<ExtendedWebElement> collectionList = detailsPage.getAllCollectionCells(replaysCollection);
        if (collectionList.size() > 0) {
            replayTitle = collectionList.get(0).getText();
        }

        if (collectionList.size() == 0 || replayTitle == null || replayTitle.isEmpty()) {
            throw new SkipException(NO_REPLAYS_FOUND);
        }

        LOGGER.info("Replay title {}", replayTitle);
        detailsPage.getTypeCellLabelContains(replayTitle).click();
        detailsPage.waitForDetailsPageToOpen();
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        sa.assertTrue(replayTitle.contains(videoPlayer.getTitleLabel()),
                "Video title does not match with the expected");
        videoPlayer.waitForVideoToStart();
        // Forward video and get remaining time
        commonPage.clickRight(6, 1, 1);
        commonPage.clickDown(1);
        int remainingTime = videoPlayer.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTime {}", remainingTime);
        // Go back to details page and tap in Continue button
        homePage.clickMenuTimes(1, 1);
        if (!detailsPage.isOpened()) {
            homePage.clickMenuTimes(1, 1);
        }
        Assert.assertTrue(detailsPage.getTypeButtonContainsLabel(continueButton).isPresent(),
                "Continue button is not present");
        detailsPage.getTypeButtonContainsLabel(continueButton).click();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        // Get remaining time and validate if video restarted
        commonPage.clickDown(1);
        int remainingTimeAfterContinue = videoPlayer.getRemainingTimeThreeIntegers();
        LOGGER.info("remainingTimeAfterContinue {}", remainingTimeAfterContinue);

        int duration = remainingTime - remainingTimeAfterContinue;
        ValueRange range = ValueRange.of(0, latency);
        sa.assertTrue(range.isValidIntValue(duration),
                "Video did not restart from expected position");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112598"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifySameSeriesPostPlayUI() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVUpNextPage upNextPage = new DisneyPlusAppleTVUpNextPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        // Fetch series and second episode metadata relevant for Post Play UI validations
        MetastringParts seriesMetastringParts;
        MetastringParts secondEpisodeMetastringParts;
        try {
            seriesMetastringParts = getExploreAPIPageVisuals(BLUEY_MINISODES.getEntityId()).getMetastringParts();
            secondEpisodeMetastringParts = getSeriesApi(BLUEY_MINISODES.getEntityId(),
                    DisneyPlusBrandIOSPageBase.Brand.DISNEY).getSeasons().get(0).getItems().get(1)
                    .getVisuals().getMetastringParts();
        } catch (Exception e) {
            throw new SkipException("Skipping test. Episode/Series metastring parts not found using Explore API", e);
        }
        ArrayList<Flag> seriesAudioVisualFlags;
        String nextEpisodeRating, nextEpisodeFormattedRuntime, seriesReleaseYear;
        ArrayList<String> seriesGenres;
        try {
            nextEpisodeRating = secondEpisodeMetastringParts.getRatingInfo().getRating().getText();
            nextEpisodeFormattedRuntime = getFormattedDurationStringFromDurationInMs(
                    secondEpisodeMetastringParts.getRuntime().getRuntimeMs());
            seriesAudioVisualFlags = seriesMetastringParts.getAudioVisual().getFlags();
            seriesReleaseYear = seriesMetastringParts.getReleaseYearRange().getStartYear();
            seriesGenres = seriesMetastringParts.getGenres().getValues();
        } catch (Exception e) {
            throw new SkipException("Skipping test. Episode/Series metastring parts assignation failed", e);
        }

        // Play series' first episode
        launchDeeplink(R.TESTDATA.get("disney_prod_series_bluey_mini_episodes_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart(TEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT);

        // Scrub to the end and validate Post Play UI doesn't show Metastring element
        commonPage.clickRight(7, 1, 1);
        Assert.assertTrue(upNextPage.isOpened(), UP_NEXT_PAGE_NOT_DISPLAYED);
        Assert.assertFalse(upNextPage.getUpNextContentFooterLabel().isElementPresent(THREE_SEC_TIMEOUT),
                "Metastring element is present on Post Play UI");

        // Validate Metastring parts fetched from Explore API aren't visible as part of a text label element
        Assert.assertFalse(upNextPage.getStaticTextByLabelContains(nextEpisodeRating).isElementPresent(ONE_SEC_TIMEOUT),
                "Next episode rating is present on Post Play UI");
        for (Flag flag : seriesAudioVisualFlags) {
            Assert.assertFalse(upNextPage.getStaticTextByLabelContains(flag.getTts()).isElementPresent(ONE_SEC_TIMEOUT),
                    String.format("'%s' flag is present on Post Play UI", flag.getTts()));
        }
        Assert.assertFalse(upNextPage.getStaticTextByLabelContains(seriesReleaseYear).isElementPresent(ONE_SEC_TIMEOUT),
                "Series release year is present on Post Play UI");
        Assert.assertFalse(
                upNextPage.getStaticTextByLabelContains(nextEpisodeFormattedRuntime).isElementPresent(ONE_SEC_TIMEOUT),
                "Next episode runtime is present on Post Play UI");
        for (String seriesGenre : seriesGenres) {
            Assert.assertFalse(upNextPage.getStaticTextByLabelContains(seriesGenre).isElementPresent(ONE_SEC_TIMEOUT),
                    String.format("'%s' series genre is present on Post Play UI", seriesGenre));
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112957"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyPostPlayDismissLogic() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVUpNextPage upNextPage = new DisneyPlusAppleTVUpNextPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        // Play series' first episode
        launchDeeplink(R.TESTDATA.get("disney_prod_series_bluey_mini_episodes_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart(TEN_SEC_TIMEOUT, ONE_SEC_TIMEOUT);

        // Scrub to the end, validate Post Play UI is visible and select minimized video player
        commonPage.clickRight(8, 1, 1);
        Assert.assertTrue(upNextPage.waitForUpNextUIToAppear(), UP_NEXT_PAGE_NOT_DISPLAYED);
        upNextPage.moveRight(2, 1);
        upNextPage.clickSelect();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);

        // Validate player controls are still accessible
        videoPlayer.waitForElementToDisappear(videoPlayer.getContentRatingInfoView(), FIFTEEN_SEC_TIMEOUT);
        commonPage.clickDown(1);
        Assert.assertTrue(videoPlayer.isSeekbarVisible(), PLAYER_CONTROLS_NOT_DISPLAYED);

        // Fast-forward to the end of playback and validate Post Play UI is visible again after playback
        commonPage.clickRight(7, 1, 1);
        Assert.assertTrue(upNextPage.waitForUpNextUIToAppear(), UP_NEXT_PAGE_NOT_DISPLAYED);
    }
}
