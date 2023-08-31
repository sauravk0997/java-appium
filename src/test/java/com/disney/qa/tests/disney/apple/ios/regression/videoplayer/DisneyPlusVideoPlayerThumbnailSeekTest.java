package com.disney.qa.tests.disney.apple.ios.regression.videoplayer;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDownloadsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class DisneyPlusVideoPlayerThumbnailSeekTest extends DisneyBaseTest {

    protected static final String  SHORT_SERIES = "Bluey";

    //Disabling these tests since we are not able to verify that thumbnail is present on screen as we seek along
    //These tests were working before, we will evaluate after the carina upgrade again.
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61937"})
    @Test(description = "Verify Thumbnail Seek Functionality", groups = {"Video Player"}, enabled = false)
    @Maintainer("gkrishna1")
    public void verifyThumbnailSeek() {
        initialSetup();
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase =  initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(disneyAccount.get());
        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusSearchIOSPageBase.clickMoviesTab();
        List<ExtendedWebElement> movies = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        movies.get(1).click();
        pause(5);
        disneyPlusDetailsIOSPageBase.clickPlayButton().isOpened();
        disneyPlusVideoPlayerIOSPageBase.seekOnPlayer();
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isThumbnailViewPresent(),"thumbnail view on player is not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61939"})
    @Test(description = "Verify Thumbnail Seek Functionality for downloaded asset", groups = {"Video Player"}, enabled = false)
    @Maintainer("gkrishna1")
    public void verifyThumbnailSeekForDownloadedAsset() {
        initialSetup();
        DisneyPlusVideoPlayerIOSPageBase disneyPlusVideoPlayerIOSPageBase = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusSearchIOSPageBase disneyPlusSearchIOSPageBase = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase =  initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase disneyPlusDownloadsIOSPageBase = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(disneyAccount.get());
        disneyPlusHomeIOSPageBase.clickSearchIcon();
        disneyPlusHomeIOSPageBase.getSearchNav().click();
        disneyPlusSearchIOSPageBase.searchForMedia("Bluey");
        List<ExtendedWebElement> results = disneyPlusSearchIOSPageBase.getDisplayedTitles();
        results.get(0).click();

        disneyPlusDetailsIOSPageBase.startDownload();
        disneyPlusDetailsIOSPageBase.waitForSeriesDownloadToComplete();
        disneyPlusHomeIOSPageBase.clickDownloadsIcon();
        disneyPlusDownloadsIOSPageBase.tapDownloadedAssetFromListView(SHORT_SERIES);
        disneyPlusDownloadsIOSPageBase.tapDownloadedAsset(SHORT_SERIES);
        disneyPlusVideoPlayerIOSPageBase.isOpened();
        disneyPlusVideoPlayerIOSPageBase.seekOnPlayer();
        sa.assertTrue(disneyPlusVideoPlayerIOSPageBase.isThumbnailViewPresent(),"thumbnail view on player for downloaded content is not present");
        sa.assertAll();
    }
}
