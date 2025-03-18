package com.disney.qa.tests.disney.apple.tvos.regression.videoplayer;

import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusEspnIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVCommonPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVDetailsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVVideoPlayerPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;

public class DisneyPlusAppleTVVideoPlayerTest extends DisneyPlusAppleTVBaseTest {

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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102801"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, US})
    public void verifyVODReplay() {
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerTVPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusEspnIOSPageBase espnPage = new DisneyPlusEspnIOSPageBase(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        String basketballTitle = "Basketball";
        String replays = "Replays";
      //  setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("espn_prod_replay_basketball_page"));

        Assert.assertTrue(espnPage.isSportTitlePresent(basketballTitle), "Title was not found");
        detailsPage.moveDown(2, 1);
        detailsPage.getStaticTextByLabel(replays);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();


    }
}
