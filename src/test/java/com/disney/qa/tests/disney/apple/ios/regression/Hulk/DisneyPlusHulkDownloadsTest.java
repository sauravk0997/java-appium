package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.ONLY_MURDERS_IN_THE_BUILDING;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.PREY;

public class DisneyPlusHulkDownloadsTest extends DisneyBaseTest {
    private static final String SET = "set";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75169", "XMOBQA-74449"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyHuluSeriesPremiumDownloadActions() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        //Movie download button
        homePage.isOpened();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        sa.assertTrue(detailsPage.getMovieDownloadButton().isPresent(),
                "Movie download button was not found.");

        //Episode download buttons
        detailsPage.clickSearchIcon();
        searchPage.clearText();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            swipeUp(2500);
        }
        String season1NumberOfEpisodeDownloads = String.valueOf(getEpisodeDownloadsOfSeason());
        LOGGER.info("Season 1 Total number of episode downloads: " + season1NumberOfEpisodeDownloads);
        sa.assertTrue(season1NumberOfEpisodeDownloads.equalsIgnoreCase("10"),
                "Season 1 total number of episode downloads does not equal expected total of '10'");

        //Download all of season
        swipePageTillElementTappable(detailsPage.getDownloadAllSeasonButton(), 2, null, Direction.DOWN, 900);
        detailsPage.getDownloadAllSeasonButton().click();
        detailsPage.clickDownloadSeasonAlertButton();
        detailsPage.waitForTwoOrMoreHuluEpisodeDownloadsToComplete(250, 25);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        downloads.isOpened();
        sa.assertTrue(downloads.getStaticTextByLabelContains("10 Episodes").isPresent(), "10 episode downloads were not found.");
        downloads.clickSeriesMoreInfoButton();
        sa.assertTrue(downloads.getStaticTextByLabelContains("Season 1").isPresent(), "Season 1 was not downloaded.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74562"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluSeriesDeleteAndPlay() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        int polling = 5;
        int episodeTimeout = 120;
        int shortEpisodeTimeout = 20;
        int percentageForNextTitle = 97;
        String one = "1";
        String two = "2";
        String videoPlayerNotOpen = "Video player did not open";
        String detailsNotOpen = "Details page did not open";
        String deleteButtonNotOpen = "Delete and Play button did not appear";

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        homePage.waitForHomePageToOpen();

        // Online + next episode is downloaded
        launchDeeplink(R.TESTDATA.get("hulu_prod_series_drifters_deeplink"));
        sa.assertTrue(detailsPage.isOpened(), detailsNotOpen);
        detailsPage.getEpisodeToDownload(one,one).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(shortEpisodeTimeout, polling);
        detailsPage.getEpisodeToDownload(one,two).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(shortEpisodeTimeout, polling);
        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), videoPlayerNotOpen);
        videoPlayer.scrubToPlaybackPercentage(percentageForNextTitle);
        sa.assertTrue(videoPlayer.waitForDeleteAndPlayButton(),
                deleteButtonNotOpen);
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), detailsNotOpen);
        detailsPage.clickCloseButton();

        // Online + next episode is NOT downloaded
        launchDeeplink(R.TESTDATA.get("disney_prod_hulk_series_details_deeplink"));
        sa.assertTrue(detailsPage.isOpened(), detailsNotOpen);
        detailsPage.getEpisodeToDownload(one,one).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(episodeTimeout, polling);
        detailsPage.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), videoPlayerNotOpen);
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(percentageForNextTitle);
        sa.assertTrue(videoPlayer.waitForDeleteAndPlayButton(),
                deleteButtonNotOpen);
        sa.assertAll();
    }


    private Integer getEpisodeDownloadsOfSeason() {
        Map<List<String>, List<String>> params = new HashMap<>();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            int count = 3;
            while (count > 0) {
                params.put(Collections.singletonList(SET + count), detailsPage.findAllEpisodeDownloadButtonsInCurrentView());
                swipeUp(600);
                count --;
            }
            List<String> allEpisodeDownloadButtons = new ArrayList<>();
            allEpisodeDownloadButtons.addAll(params.get(Collections.singletonList(SET + "1")));
            allEpisodeDownloadButtons.addAll(params.get(Collections.singletonList(SET + "2")));
            allEpisodeDownloadButtons.addAll(params.get(Collections.singletonList(SET + "3")));
            List<String> allEpisodeDownloadButtonsNoDupes = allEpisodeDownloadButtons.stream().distinct().collect(Collectors.toList());
            return allEpisodeDownloadButtonsNoDupes.size();
        } else {
            int count = 2;
            while (count > 0) {
                params.put(Collections.singletonList(SET + count), detailsPage.findAllEpisodeDownloadButtonsInCurrentView());
                swipeUp(1200);
                count --;
            }
            List<String> allEpisodeDownloadButtons = new ArrayList<>();
            allEpisodeDownloadButtons.addAll(params.get(Collections.singletonList(SET + "1")));
            allEpisodeDownloadButtons.addAll(params.get(Collections.singletonList(SET + "2")));
            List<String> allEpisodeDownloadButtonsNoDupes = allEpisodeDownloadButtons.stream().distinct().collect(Collectors.toList());
            return allEpisodeDownloadButtonsNoDupes.size();
        }
    }
}
