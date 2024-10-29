package com.disney.qa.tests.disney.apple.ios.downloads;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDownloadsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusDownloadsTest extends DisneyBaseTest {

    //Test constants
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page didn't open";
    private static final String DOWNLOADS_PAGE_DID_NOT_OPEN = "Downloads page did not open";
    private static final double SCRUB_PERCENTAGE_FIFTY = 50;

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66730"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadsProgressBarDisplayedOnContentContainsBookmark() {
        int latency = 10;
        int pollingInSeconds = 5;
        int timeoutInSeconds = 120;
        String zero = "0";
        String one = "1";
        String two = "2";
        String bookmarkErrorMessage = "Bookmark not indicating correct position";
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton().waitForVideoToStart().isOpened();
        String movieTitle = videoPlayer.getTitleLabel();
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_FIFTY);
        videoPlayer.clickBackButton();
        detailsPage.startDownload();
        detailsPage.waitForMovieDownloadComplete(timeoutInSeconds, pollingInSeconds);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        sa.assertTrue(downloads.getStaticTextByLabel(movieTitle).isPresent(),
                "Movie Title was not found");
        sa.assertTrue(downloads.isProgressbarBookmarkDisplayedOnDownloads(zero, zero),
                "Progress bar bookmark on movie title not displayed");
        sa.assertTrue(downloads.isProgressBarIndicatingCorrectPosition(zero, zero, SCRUB_PERCENTAGE_FIFTY, latency),
                bookmarkErrorMessage);

        //Series
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        detailsPage.clickPlayButton().waitForVideoToStart().isOpened();
        String episodeTitle = videoPlayer.getSubTitleLabel();
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_FIFTY);
        videoPlayer.clickBackButton();
        swipePageTillElementPresent(detailsPage.getEpisodeToDownload(one, two), 2,
                detailsPage.getContentDetailsPage(), Direction.UP, 1200);

        //Download episode
        detailsPage.getEpisodeToDownload(one, one).click();
        detailsPage.getEpisodeToDownload(one, two).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(timeoutInSeconds, pollingInSeconds);
        //Navigate to Download page
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        downloads.clickSeriesMoreInfoButton();
        sa.assertTrue(downloads.getStaticTextByLabel(episodeTitle).isPresent(),
                "Episode Title was not found");
        sa.assertTrue(downloads.isProgressbarBookmarkDisplayedOnDownloads(one, one),
                "Progress bar bookmark on series title not displayed");
        sa.assertTrue(downloads.isProgressBarIndicatingCorrectPosition(one, one, SCRUB_PERCENTAGE_FIFTY, latency),
                bookmarkErrorMessage);

        sa.assertAll();
    }
}
