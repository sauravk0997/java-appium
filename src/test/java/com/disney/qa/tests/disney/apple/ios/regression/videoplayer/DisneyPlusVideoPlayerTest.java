package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.ONE_HUNDRED_TWENTY_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusVideoPlayerTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77674"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadOfESPNContent() {
        String seasonNumber = "1";
        String episodeNumber = "1";
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE));
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_in_the_arena_serena_williams_deeplink"));
        detailsPage.waitForDetailsPageToOpen();
        swipe(detailsPage.getEpisodeToDownload(seasonNumber, episodeNumber), Direction.UP, 1, 900);
        detailsPage.getEpisodeToDownload(seasonNumber, episodeNumber).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(ONE_HUNDRED_TWENTY_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);
        String episodeTitle = detailsPage.getEpisodeCellTitle(seasonNumber, episodeNumber);
        detailsPage.getFirstEpisodeDownloadCompleteButton().click();
        detailsPage.getDownloadModalPlayButton().click();

        Assert.assertTrue(videoPlayer.isOpened(),
                "Video player did not open after choosing a downloaded episode");
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.getSubTitleLabel().contains(episodeTitle),
                "Video player title does not match with expected title: " + episodeTitle);
    }
}
