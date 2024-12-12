package com.disney.qa.tests.disney.apple.ios.regression.downloads;

import com.disney.qa.api.disney.*;
import com.disney.qa.api.pojos.explore.*;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66668"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEmptyDownloadsUI() {
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);

        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(downloads.isDownloadHeaderPresent(), "Downloads header is not present");
        Assert.assertTrue(downloads.getEmptyDownloadImage().isPresent(), "Downloads Image is not present");
        Assert.assertTrue(downloads.isDownloadsEmptyHeaderPresent(), "Downloads empty header is not present");
        Assert.assertTrue(downloads.isDownloadsEmptyCopyPresent(), "Downloads empty copy is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66730"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadsProgressBarDisplayedOnContentContainsBookmark() {
        int latency = 20;
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
        String movieTitle = detailsPage.getMediaTitle();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_FIFTY);
        videoPlayer.waitForVideoToStart();
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
        String episodeTitle = detailsPage.getEpisodeContentTitle();
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_FIFTY);
        videoPlayer.waitForVideoToStart();
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
        sa.assertTrue(downloads.getStaticTextByLabelContains(episodeTitle).isPresent(),
                "Episode Title was not found");
        sa.assertTrue(downloads.isProgressbarBookmarkDisplayedOnDownloads(one, one),
                "Progress bar bookmark on series title not displayed");
        sa.assertTrue(downloads.isProgressBarIndicatingCorrectPosition(one, one, SCRUB_PERCENTAGE_FIFTY, latency),
                bookmarkErrorMessage);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66742"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifySeriesDownloadsInProgressText() {
        String one = "1";
        String two = "2";
        String three = "3";
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        swipePageTillElementPresent(detailsPage.getEpisodeToDownload(one, two), 2,
                detailsPage.getContentDetailsPage(), Direction.UP, 1200);

        //Download one episode
        detailsPage.getEpisodeToDownload(one, one).click();

        //Navigate to Download page
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(downloads.isDownloadInProgressTextPresent(),
                "Download text for one download was not as expected");
        downloads.clickHomeIcon();
        //Start download of 2 episodes
        detailsPage.getEpisodeToDownload(one, two).click();
        detailsPage.getEpisodeToDownload(one, three).click();

        //Navigate to Download page
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(downloads.isDownloadInProgressPluralTextPresent(),
                "Download text for multiple downloads was not as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66670"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMoviesDownloadsUI() {
        int polling = 10;
        int timeout = 300;
        String Deeplink = "disneyplus://www.disneyplus.com/browse/";

        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        SoftAssert sa = new SoftAssert();

        launchDeeplink(Deeplink+DisneyEntityIds.MARVELS.getEntityId());
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        String movieTitle = detailsPage.getMediaTitle();
        ExploreContent movieApiContent = getMovieApi(DisneyEntityIds.MARVELS.getEntityId(),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        
        //Start download
        detailsPage.getMovieDownloadButton().click();
        detailsPage.waitForMovieDownloadComplete(timeout, polling);
        detailsPage.clickDownloadsIcon();
        Assert.assertTrue(downloadsPage.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);

        //Verify movie asset metadata on downloads
        sa.assertTrue(downloadsPage.isDownloadHeaderPresent(),
                "Downloads header is not present on downloads screen");
        sa.assertTrue(downloadsPage.getEditButton().isPresent(),
                "Edit button is not present on downloads screen");
        sa.assertTrue(downloadsPage.getDownloadedAssetImage(movieTitle).isPresent(),
                "Downloaded movie asset image is not found");
        sa.assertTrue(downloadsPage.getStaticTextByLabelContains(movieTitle).isPresent(),
                String.format("Movie title '%s' is not found for downloaded asset", movieTitle));
        sa.assertTrue(downloadsPage.getSizeAndRuntime().isPresent(),
                "Downloaded movie asset size and runtime are not found for downloaded asset");
        sa.assertTrue(downloadsPage.getStaticTextByLabelContains(movieApiContent.getRating()).isPresent(),
                "Movie downloaded asset rating not found for the downloaded asset");
        sa.assertAll();
    }
}
