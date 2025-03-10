package com.disney.qa.tests.disney.apple.tvos.videoplayer;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVDetailsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVSearchPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVVideoPlayerPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.IConstantHelper.VIDEO_PLAYER_NOT_DISPLAYED;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;

public class DisneyPlusAppleTVVideoPlayerControlTest extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-120534"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULK, US})
    public void verifyServiceAttributionOnPlayBack() {
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage()));

        logInTemp(getAccount());
        searchAndOpenVideoPlayer(ONLY_MURDERS_IN_THE_BUILDING);
        videoPlayer.skipPromoIfPresent();
        sa.assertFalse(videoPlayer.getSeekbar().isPresent(ONE_SEC_TIMEOUT),
                "player controls were displayed when video started");
        sa.assertTrue(videoPlayer.isTitleLabelDisplayed(),
                "Video player title wasn't visible when video started");
        sa.assertTrue(videoPlayer.isServiceAttributionLabelVisible(),
                "service attribution wasn't visible when video started");
        sa.assertTrue(videoPlayer.isServiceAttributionLabelVisibleWithControls(),
                "service attribution wasn't visible along with controls");
        sa.assertTrue(videoPlayer.isTitleLabelDisplayed(),
                "Video player title wasn't visible along with controls");
        sa.assertTrue(videoPlayer.isSubTitleLabelDisplayed(),
                "Video player meta data title wasn't visible along with controls");
        sa.assertTrue(videoPlayer.isSeekbarVisible(),
                "player controls were not displayed when playback activated");
        sa.assertTrue(videoPlayer.getServiceAttributionLabel().getText().equals(HULU_SERVICE_ATTRIBUTION_MESSAGE),
                "Expected Hulu Service Attribution not displayed");
        sa.assertAll();
    }

    private void searchAndOpenVideoPlayer(String content) {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        homePage.isOpened();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        searchPage.isOpened();
        searchPage.typeInSearchField(content);
        searchPage.clickSearchResult(content);
        detailsPage.isOpened();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
    }
}
