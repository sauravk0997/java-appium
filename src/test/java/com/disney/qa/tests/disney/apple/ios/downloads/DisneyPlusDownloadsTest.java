package com.disney.qa.tests.disney.apple.ios.downloads;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDownloadsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class DisneyPlusDownloadsTest extends DisneyBaseTest {

    //Test constants
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page didn't open";
    private static final String DOWNLOADS_PAGE_DID_NOT_OPEN = "Downloads page did not open";
    private static final double SCRUB_PERCENTAGE_THIRTY = 30;
    private static final String LOKI = "Loki";
    private static final String SECRET_INVASION = "Secret Invasion";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66730"})
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION})
    public void verifyAnthologySearch() {
        int pollingInSeconds = 5;
        int timeoutInSeconds = 90;
        String zero = "0";
        String one = "1";
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());

        searchAndNavigateToDetailPage(LOKI);
        detailsPage.clickPlayButton().waitForVideoToStart().isOpened();
        String episodeTitle = videoPlayer.getSubTitleLabel();
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_THIRTY);
        videoPlayer.clickBackButton();

        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            swipeUp(2500);
        }
        //Download episode
        detailsPage.getEpisodeToDownload(one, one).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(timeoutInSeconds, pollingInSeconds);

        //Navigate to Download page
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        downloads.clickSeriesMoreInfoButton();
        sa.assertTrue(downloads.getStaticTextByLabel(episodeTitle).isPresent(), "Episode Title was not found");
        sa.assertTrue(downloads.isProgressbarDisplayedOnDownloads(one, one),
                "Progress bar on series title not displayed");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);
        detailsPage.clickCloseButton();
        searchAndNavigateToDetailPage(SECRET_INVASION);
        detailsPage.clickPlayButton().waitForVideoToStart().isOpened();
        String movieTitle = videoPlayer.getTitleLabel();
        videoPlayer.scrubToPlaybackPercentage(SCRUB_PERCENTAGE_THIRTY);
        videoPlayer.clickBackButton();

        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            swipeUp(2500);
        }
        detailsPage.startDownload();
        detailsPage.waitForMovieDownloadComplete(timeoutInSeconds, pollingInSeconds);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        sa.assertTrue(downloads.getStaticTextByLabel(movieTitle).isPresent(), "Movie Title was not found");
        sa.assertTrue(downloads.isProgressbarDisplayedOnDownloads(zero, one),
                "Progress bar on movie title not displayed");

        sa.assertAll();
    }

    private void searchAndNavigateToDetailPage(String contentTitle) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        homePage.clickSearchIcon();
        homePage.getSearchNav().click();
        searchPage.searchForMedia(contentTitle);
        searchPage.getDynamicAccessibilityId(contentTitle).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
    }
}
