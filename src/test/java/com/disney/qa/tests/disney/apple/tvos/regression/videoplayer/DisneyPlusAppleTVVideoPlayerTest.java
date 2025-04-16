package com.disney.qa.tests.disney.apple.tvos.regression.videoplayer;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.common.constant.CollectionConstant;
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
import java.util.List;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVVideoPlayerTest extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SPORT_PAGE_DID_NOT_OPEN = "Sport page did not open";
    private static final String NO_REPLAYS_FOUND = "No replay events found";

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
        sa.assertTrue(videoPlayer.isSeekbarVisible(),
                "player controls were not displayed when playback activated");
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
        sa.assertTrue(videoPlayer.isSeekbarVisible(),
                "player controls were not displayed when playback activated");
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
}
