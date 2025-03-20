package com.disney.qa.tests.disney.apple.tvos.regression.videoplayer;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVCommonPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVDetailsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVVideoPlayerPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.DETAILS_PAGE_NOT_DISPLAYED;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVVideoPlayerTest extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-120534"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULK, US})
    public void verifyServiceAttributionOnPlayBack() {
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        SoftAssert sa = new SoftAssert();
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());
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
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULK, US})
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
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.PROFILES, US})
    public void verifyPCONPlayback() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        String contentUnavailableError = "This title cannot be watched with current Parental Control settings";

        // Set lower rating
        List<String> ratingSystemValues = getUnifiedAccount().getProfile(DEFAULT_PROFILE).getAttributes()
                .getParentalControls().getMaturityRating().getRatingSystemValues();
        LOGGER.info("Lower rating: {}", ratingSystemValues.get(0));
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(), ratingSystemValues.get(0));

        logIn(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), "Home page did not open");
        launchDeeplink(R.TESTDATA.get("disney_prod_series_loki_first_episode_playback_deeplink"));

        Assert.assertFalse(videoPlayer.isOpened(),"Video player opened");
        Assert.assertTrue(detailsPage.getRatingRestrictionDetailMessage().isPresent(),
                "Restriction message was not displayed");
    }
}
