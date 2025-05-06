package com.disney.qa.tests.disney.apple.ios.regression.downloads;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.config.DisneyConfiguration;
import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.api.client.requests.*;
import com.disney.qa.api.client.responses.profile.Profile;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.*;
import com.disney.qa.api.pojos.explore.*;
import com.disney.qa.common.constant.RatingConstant;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;
import java.util.stream.Collectors;

import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BASIC_MONTHLY;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusDownloadsTest extends DisneyBaseTest {

    //Test constants
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page didn't open";
    private static final String DOWNLOADS_PAGE_DID_NOT_OPEN = "Downloads page did not open";
    private static final double SCRUB_PERCENTAGE_FIFTY = 50;
    private static final String SET = "set";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66668"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEmptyDownloadsUI() {
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
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
        int timeoutInSeconds = 300;
        String zero = "0";
        String one = "1";
        String two = "2";
        String bookmarkErrorMessage = "Bookmark not indicating correct position";
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_DID_NOT_OPEN);
        String movieTitle = detailsPage.getMediaTitle();
        detailsPage.clickPlayButton();
        pauseScrubAndPlay(SCRUB_PERCENTAGE_FIFTY);
        videoPlayer.clickBackButton();
        detailsPage.waitForBookmarkToRefresh(SCRUB_PERCENTAGE_FIFTY, latency);
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
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(), DETAILS_PAGE_DID_NOT_OPEN);
        String episodeTitle = detailsPage.getEpisodeContentTitle();
        detailsPage.clickPlayButton();
        pauseScrubAndPlay(SCRUB_PERCENTAGE_FIFTY);
        videoPlayer.clickBackButton();
        detailsPage.waitForBookmarkToRefresh(SCRUB_PERCENTAGE_FIFTY, latency);
        swipePageTillElementPresent(detailsPage.getEpisodeToDownload(one, two), 2,
                detailsPage.getContentDetailsPage(), Direction.UP, 1200);

        //Download episode
        detailsPage.getEpisodeToDownload(one, one).click();
        detailsPage.waitForFirstEpisodeToCompleteDownload(timeoutInSeconds, pollingInSeconds);
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
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        swipePageTillElementPresent(detailsPage.getEpisodeToDownload(one, two), 2,
                detailsPage.getContentDetailsPage(), Direction.UP, 1200);

        //Download one episode
        detailsPage.getEpisodeToDownload(one, one).click();
        downloads.waitForDownloadToStart();
        //Navigate to Download page
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(downloads.isDownloadInProgressTextPresent(),
                "Download text for one download was not as expected");
        downloads.clickHomeIcon();
        //Start download of 2 episodes
        detailsPage.getEpisodeToDownload(one, two).click();
        detailsPage.getEpisodeToDownload(one, three).click();
        downloads.waitForMultipleDownloadsToStart();

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

        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        SoftAssert sa = new SoftAssert();

        launchDeeplink(DEEPLINKURL + DisneyEntityIds.MARVELS.getEntityId());
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66672"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.SMOKE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadsEditModeScreenUI() {
        int polling = 10;
        int timeout = 300;
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        SoftAssert sa = new SoftAssert();
        ExploreContent movieApiContent = getMovieApi(DisneyEntityIds.MARVELS.getEntityId(),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);

        launchDeeplink(DEEPLINKURL + DisneyEntityIds.MARVELS.getEntityId());
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        String movieTitle = detailsPage.getMediaTitle();
        detailsPage.getMovieDownloadButton().click();
        detailsPage.waitForMovieDownloadComplete(timeout, polling);
        detailsPage.clickDownloadsIcon();
        Assert.assertTrue(downloadsPage.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        downloadsPage.getEditButton().click();

        sa.assertTrue(downloadsPage.getCancelButton().isPresent(), "Cancel button not displayed");
        sa.assertTrue(downloadsPage.getSelectAllButton().isPresent(), "Select All button not displayed");
        sa.assertTrue(downloadsPage.getDownloadedAssetImage(movieTitle).isPresent(),
                "Downloaded movie asset image is not found");
        sa.assertTrue(downloadsPage.getStaticTextByLabelContains(movieTitle).isPresent(),
                String.format("Movie title '%s' is not found for downloaded asset", movieTitle));
        sa.assertTrue(downloadsPage.getStaticTextByLabelContains(getApiMovieRatingDetails(movieApiContent)).isPresent(),
                "Movie downloaded asset rating not found for the downloaded asset");
        sa.assertTrue(downloadsPage.getSizeAndRuntime().isPresent(),
                "Downloaded movie asset size and runtime are not found for downloaded asset");
        sa.assertTrue(downloadsPage.getUncheckedCheckbox().isPresent(), "Empty Checkbox is not displayed");
        sa.assertTrue(downloadsPage.isSelectContentToRemoveTextDisplayed(),
                "Select Content to remove text is not displayed");
        sa.assertTrue(downloadsPage.getTrashIcon().isPresent(), "Trash icon is not displayed");

        downloadsPage.clickUncheckedCheckbox();
        sa.assertTrue(downloadsPage.isCheckedCheckboxPresent(),
                "Checkbox is not selected after tapping unchecked checkbox");
        sa.assertTrue(downloadsPage.getDeSelectAllButton().isPresent(),
                "Deselect All button not displayed after tapping unchecked checkbox");

        downloadsPage.getCheckedCheckbox().click();
        sa.assertTrue(downloadsPage.getUncheckedCheckbox().isPresent(),
                "Checkbox was not unchecked after tapping checked checkbox");

        downloadsPage.getSelectAllButton().click();
        sa.assertTrue(downloadsPage.getUncheckedCheckbox().getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equals("1"),
                "Checkbox was not checked after clicking Select All");
        sa.assertTrue(downloadsPage.getDeSelectAllButton().isPresent(),
                "DeSelect All was not displayed after clicking Select All");

        downloadsPage.getDeSelectAllButton().click();
        sa.assertTrue(downloadsPage.getUncheckedCheckbox().isPresent(),
                "Checkbox was not unchecked after clicking Deselect All");
        sa.assertTrue(downloadsPage.getSelectAllButton().isPresent(),
                "Select All was not displayed after clicking Deselect All");

        downloadsPage.getCancelButton().click();
        sa.assertTrue(downloadsPage.getEditButton().isPresent(),
                "Edit button not displayed after exiting Edit mode");
        sa.assertFalse(downloadsPage.getTrashIcon().isPresent(), "Trash icon found after exiting Edit mode");

        downloadsPage.getEditButton().click();
        downloadsPage.getSelectAllButton().click();
        downloadsPage.clickDeleteDownloadButton();
        sa.assertTrue(downloadsPage.isDownloadsEmptyHeaderPresent(),
                "Download was not removed, empty header not present.");
        sa.assertFalse(downloadsPage.getStaticTextByLabelContains(movieTitle).isPresent(),
                String.format("Movie title '%s' is found after deleting content", movieTitle));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66694"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadsInProgressSubFunction() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        SoftAssert sa = new SoftAssert();

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        //Start download
        detailsPage.getMovieDownloadButton().click();
        downloadsPage.waitForDownloadToStart();
        detailsPage.clickDownloadsIcon();
        Assert.assertTrue(downloadsPage.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);

        sa.assertTrue(downloadsPage.getDownloadStopIcon().isPresent(), "Download not started");

        downloadsPage.getDownloadStopIcon().click();
        sa.assertTrue(downloadsPage.waitForPauseDownloadButton(), "Pause Download button not displayed on alert");
        downloadsPage.clickDefaultAlertBtn();
        sa.assertTrue(downloadsPage.getDownloadResumeIcon().isPresent(),
                "Download not pause after clicking pause download");
        downloadsPage.clickDownloadHeader();
        sa.assertTrue(downloadsPage.getDownloadResumeIcon().isPresent(),
                "Download resumed after clicking outside of container");

        downloadsPage.getDownloadResumeIcon().click();
        downloadsPage.clickDefaultAlertBtn();
        sa.assertTrue(downloadsPage.getDownloadStopIcon().isPresent(),
                "Download not resumed after clicking resume download");
        downloadsPage.clickDownloadHeader();
        sa.assertTrue(downloadsPage.getDownloadStopIcon().isPresent(),
                "Download stopped after clicking outside of container");

        downloadsPage.getDownloadStopIcon().click();
        downloadsPage.getSystemAlertDestructiveButton().click();
        downloadsPage.waitForDownloadEmptyHeader();
        sa.assertTrue(downloadsPage.isDownloadsEmptyHeaderPresent(),
                "Download was not removed after clicking on Remove");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66718"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadsInQueueSubFunction() {
        String one = "1";
        String four = "4";
        String five = "5";
        String fourthEpisodeTitle, fifthEpisodeTitle;
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        SoftAssert sa = new SoftAssert();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        ExploreContent seriesApiContent = getSeriesApi(
                R.TESTDATA.get("disney_prod_loki_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        try {
            fourthEpisodeTitle = seriesApiContent.getSeasons()
                    .get(0)
                    .getItems()
                    .get(3)
                    .getVisuals()
                    .getEpisodeTitle();

            fifthEpisodeTitle = seriesApiContent.getSeasons()
                    .get(0)
                    .getItems()
                    .get(4)
                    .getVisuals()
                    .getEpisodeTitle();
        } catch (Exception e) {
            throw new SkipException("Skipping test, titles were not found" + e.getMessage());
        }

        if (fourthEpisodeTitle == null || fifthEpisodeTitle == null) {
            throw new SkipException("Skipping test, failed to get episode titles from the api");
        }

        //Start download
        detailsPage.downloadAllOfSeason();
        detailsPage.clickAlertConfirm();
        downloadsPage.waitForDownloadToStart();
        downloadsPage.clickDownloadsIcon();
        Assert.assertTrue(downloadsPage.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        downloadsPage.clickSeriesMoreInfoButton();

        downloadsPage.getEpisodeDownloadButton(one, four).click();

        sa.assertTrue(detailsPage.isRemoveDownloadButtonDisplayed(), "Remove Download button not displayed on alert");
        sa.assertTrue(downloadsPage.isDownloadIsQueuedStatusDisplayed(),
                "Download is Queued status not displayed on alert");
        sa.assertTrue(downloadsPage.getStaticTextByLabel(fourthEpisodeTitle).isPresent(),
                "Episode title is not present on alert");
        sa.assertTrue(downloadsPage.isAlertDismissBtnPresent(), "Dismiss button not present on alert");

        downloadsPage.clickAlertDismissBtn();
        sa.assertTrue(downloadsPage.isEpisodeCellDisplayed(one, four),
                "episode is removed after dismissing alert");

        downloadsPage.getEpisodeDownloadButton(one, five).click();
        sa.assertTrue(downloadsPage.isDownloadIsQueuedStatusDisplayed(),
                "Download is Queued status not displayed on alert");
        downloadsPage.getSystemAlertDestructiveButton().click();
        sa.assertFalse(downloadsPage.getStaticTextByLabel(fifthEpisodeTitle).isPresent(),
                "episode is not removed after clicking remove download on alert");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67385"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMovieIconDownloads() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        // Launch movie details page
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        // Download movie and validate icons
        Assert.assertTrue(detailsPage.getMovieDownloadButton().isPresent(), "Download button is not present");
        detailsPage.getMovieDownloadButton().click();
        Assert.assertTrue(detailsPage.getDownloadStartedButton().isPresent(),
                "Download not started, icon has not changed to in progress");
        Assert.assertTrue(detailsPage.getDownloadsTabNotificationBadge().isPresent(),
                "Downloads tab footer has no elements in progress");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67381"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadIconOnSeriesEpisode() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        String one = "1";

        setAppToHomeScreen(getUnifiedAccount());
        // Launch series details page
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            swipe(detailsPage.getEpisodeToDownload(), Direction.UP, 1, 900);
        }

        // Download first episode and validate icons
        Assert.assertTrue(detailsPage.isSeriesDownloadButtonPresent(one, one),
                "Series download button is not present");
        detailsPage.getEpisodeToDownload(one, one).click();
        Assert.assertTrue(detailsPage.isStopOrPauseDownloadIconDisplayed(),
                "Download not started, icon has not changed to in progress");
        Assert.assertTrue(detailsPage.getDownloadsTabNotificationBadge().isPresent(),
                "Downloads tab footer has no elements in progress");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66734"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDescriptionOnDownloadAsset() {
        String description = null;
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(DEEPLINKURL + DisneyEntityIds.MARVELS.getEntityId());
        ExploreContent movieApiContent = getMovieApi(DisneyEntityIds.MARVELS.getEntityId(),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        try {
            description = movieApiContent.getDescription().getFull().split("\n")[0];
        } catch (Exception e) {
            Assert.fail(String.format("content Description not found: %s", e.getMessage()));
        }

        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        String movieTitle = detailsPage.getMediaTitle();

        //Start download
        detailsPage.getMovieDownloadButton().click();
        downloadsPage.waitForDownloadToStart();
        detailsPage.clickDownloadsIcon();
        Assert.assertTrue(downloadsPage.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);

        downloadsPage.getStaticTextByLabel(movieTitle).click();
        Assert.assertTrue(downloadsPage.getStaticTextByLabelContains(description)
                        .getAttribute(Attributes.VISIBLE.getAttribute())
                        .equals(TRUE),
                "Content description is not visible");

        downloadsPage.getStaticTextByLabel(movieTitle).click();
        Assert.assertTrue(downloadsPage.getStaticTextByLabelContains(description)
                        .getAttribute(Attributes.VISIBLE.getAttribute())
                        .equals(FALSE),
                "Content description is still visible");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72184"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadScreenUIForAdUser() {
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);

        Assert.assertTrue(downloadsPage.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(downloadsPage.isDownloadHeaderPresent(),
                "Downloads header is not present");
        Assert.assertTrue(downloadsPage.getEmptyDownloadImage().isPresent(),
                "Downloads Image is not present");
        Assert.assertTrue(downloadsPage.isAdTierDownloadTitleDisplayed(),
                "'Downloads not available' title is not displayed for Ad tier user");
        Assert.assertTrue(downloadsPage.isAdTierDownloadBodyTextDisplayed(),
                "'Downloads not available' description message is not displayed for Ad tier user");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69545"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadScreenHidesDownloadsThatExceedContentRating() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        String seasonNumber = "1";
        String episodeNumber = "1";

        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_content_timon_and_pumbaa_deeplink"));
        String firstSeriesName = detailsPage.getMediaTitle();
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            swipe(detailsPage.getEpisodeToDownload(), Direction.UP, 1, 900);
        }
        detailsPage.getEpisodeToDownload("1", "1").click();
        downloadsPage.waitForDownloadToStart();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_daredevil_deeplink"));
        String secondSeriesName = detailsPage.getMediaTitle();
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            swipe(detailsPage.getEpisodeToDownload(), Direction.UP, 1, 900);
        }
        detailsPage.getEpisodeToDownload(seasonNumber, episodeNumber).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(THREE_HUNDRED_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloadsPage.getDownloadAssetFromListView(firstSeriesName).isPresent(),
                firstSeriesName + " series title was not present");
        Assert.assertTrue(downloadsPage.getDownloadAssetFromListView(secondSeriesName).isPresent(),
                secondSeriesName + " series title was not present");

        //Set lower rating
        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(),
                RATING_TV14);

        terminateApp(sessionBundles.get(DISNEY));
        relaunch();

        homePage.waitForHomePageToOpen();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloadsPage.getDownloadAssetFromListView(firstSeriesName).isPresent(),
                firstSeriesName + " series title was not present");
        Assert.assertFalse(downloadsPage.getDownloadAssetFromListView(secondSeriesName).isPresent(THREE_SEC_TIMEOUT),
                secondSeriesName + " series title was present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75725"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyExpiredDownloadModalUI() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        jarvisEnableOfflineExpiredLicenseOverride();

        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_bluey_deeplink"));
        detailsPage.waitForDetailsPageToOpen();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase(PHONE)) {
            detailsPage.swipeUp(1500);
        }
        detailsPage.getFirstEpisodeDownloadButton().click();
        downloadsPage.waitForDownloadToStart();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        downloadsPage.clickSeriesMoreInfoButton();
        Assert.assertTrue(downloadsPage.getDownloadErrorButton().isElementPresent(SIXTY_SEC_TIMEOUT),
                "Download Error button (Expired Download CTA) was not present");

        downloadsPage.getDownloadErrorButton().click();
        sa.assertTrue(downloadsPage.getContentExpiredAlertTitle().isElementPresent(),
                "Content expired title was not present on Content expired alert");
        sa.assertTrue(downloadsPage.getRenewLicenseButton().isElementPresent(),
                "Renew license button was not present on Content expired alert");
        sa.assertTrue(downloadsPage.isAlertDismissBtnPresent(),
                "Cancel button was not present on Content expired alert");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75723"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRemovedExpiredDownload() {
        String seriesName = "Bluey";
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);

        jarvisEnableOfflineExpiredLicenseOverride();

        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_bluey_deeplink"));
        detailsPage.waitForDetailsPageToOpen();
        swipe(detailsPage.getFirstEpisodeDownloadButton(), Direction.UP, 1, 900);
        detailsPage.getFirstEpisodeDownloadButton().click();
        detailsPage.waitForFirstEpisodeToCompleteDownloadAndShowAsExpired(SIXTY_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        downloadsPage.clickSeriesMoreInfoButton();
        Assert.assertTrue(downloadsPage.getDownloadErrorButton().isElementPresent(),
                "Download Error button (Expired Download CTA) was not present");

        downloadsPage.clickEditButton();
        downloadsPage.clickUncheckedCheckbox();
        downloadsPage.clickDeleteDownloadButton();
        Assert.assertTrue(downloadsPage.isDownloadsEmptyHeaderPresent(), "Empty Downloads header is not present");
        Assert.assertFalse(downloadsPage.getStaticTextByLabelContains(seriesName).isPresent(FIVE_SEC_TIMEOUT),
                String.format("Title '%s' is present after deleting content", seriesName));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77923"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUnnumberedEpisodesOrderInDownloads() {
        String unnumberedSeries = "FX Short Films";
        String seasonOne = "1";
        int episodeOne = 1;
        int episodeTwo = 2;
        int episodeThree = 3;

        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        loginToHome(getUnifiedAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(unnumberedSeries);
        searchPage.getDynamicAccessibilityId(unnumberedSeries).click();

        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            swipe(detailsPage.getEpisodeToDownload(), Direction.UP, 1, 900);
        }

        //Start download in random order
        List<String> downloadedTitles = new ArrayList<>();
        detailsPage.getUnnumberedEpisodeToDownload(seasonOne, detailsPage.getEpisodeTitleLabel(episodeOne).getText()).click();
        downloadedTitles.add(detailsPage.getEpisodeTitleLabel(episodeOne).getText());

        detailsPage.getUnnumberedEpisodeToDownload(seasonOne, detailsPage.getEpisodeTitleLabel(episodeThree).getText()).click();
        downloadedTitles.add(detailsPage.getEpisodeTitleLabel(episodeThree).getText());

        detailsPage.getUnnumberedEpisodeToDownload(seasonOne, detailsPage.getEpisodeTitleLabel(episodeTwo).getText()).click();
        downloadedTitles.add(detailsPage.getEpisodeTitleLabel(episodeTwo).getText());

        // Wait for downloads to finish and navigate to the Downloads tab
        detailsPage.waitForElementToDisappear(detailsPage.getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.DOWNLOAD_IN_PROGRESS_PLURAL.getText())), SIXTY_SEC_TIMEOUT);

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        downloadsPage.getDownloadAssetFromListView(unnumberedSeries).click();

        //non-numbered episodes order will be based on recency
        for (int i = 0; i < downloadedTitles.size(); i++) {
            Assert.assertEquals(downloadedTitles.get(i),
                    downloadsPage.getEpisodeDownloadCellTitle(seasonOne, String.valueOf(i + 1)),
                    "Downloaded non-numbered episodes order is not based on recency at index:" + i);
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78059"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDownloadNumberedEpisodes() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        String titleEpisodesDownloads = "Play";
        String theSimpsonsSeries = "The Simpsons";
        String seasonOne = "Season 1";
        String firstSeason = "1";
        String episodeOneString = "1";
        String episodeTwoString = "2";
        String episodeThreeString = "3";
        int episodeOne = 1;
        int episodeTwo = 2;
        int episodeThree = 3;
        List<String> episodeTitleList = new ArrayList<>();

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_series_the_simpsons"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        String seriesName = detailsPage.getMediaTitle();
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            swipe(detailsPage.getEpisodeToDownload(), Direction.UP, 1, 1100);
        }

        // Download three episodes from season 1 and populate episodes details list
        detailsPage.getSeasonSelectorButton().click();
        detailsPage.getStaticTextByLabel(seasonOne).click();
        detailsPage.getEpisodeToDownload(firstSeason, episodeTwoString).click();
        detailsPage.getEpisodeToDownload(firstSeason, episodeOneString).click();
        detailsPage.getEpisodeToDownload(firstSeason, episodeThreeString).click();
        // Need some time to appropriate populate downloads and have episodes to compare after
        detailsPage.waitForFirstEpisodeToCompleteDownload(ONE_HUNDRED_TWENTY_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);

        // Get details episodes list
        episodeTitleList.add(detailsPage.getEpisodeTitleLabel(episodeOne).getText());
        episodeTitleList.add(detailsPage.getEpisodeTitleLabel(episodeTwo).getText());
        episodeTitleList.add(detailsPage.getEpisodeTitleLabel(episodeThree).getText());

        // Navigate to the Downloads title and wait for downloads to finish in case they have not
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        detailsPage.waitForElementToDisappear(
                detailsPage.getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_IN_PROGRESS_PLURAL.getText())), SIXTY_SEC_TIMEOUT);
        detailsPage.getStaticTextByLabelContains(theSimpsonsSeries).click();
        Assert.assertTrue(downloadsPage.getDownloadAssetFromListView(seriesName).isPresent(),
                seriesName + " series title was not present");
        // Get episodes list from Downloads UI
        List<String> episodeTitleListDownloads = getListEpisodes(titleEpisodesDownloads);

        // Get episodes list from Downloads UI and compare both lists
        if (!episodeTitleList.isEmpty()) {
            Assert.assertTrue(isDownloadsListOrdered(episodeTitleList, episodeTitleListDownloads),
                    "Numbered episodes are not ordered");
        } else {
            throw new IllegalArgumentException("Details or downloads list are empty");
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75169", "XMOBQA-74449"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyHuluSeriesPremiumDownloadActions() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        //Movie download button
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        sa.assertTrue(detailsPage.getMovieDownloadButton().isPresent(),
                "Movie download button was not found.");

        //Episode download buttons
        detailsPage.clickSearchIcon();
        searchPage.clearText();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            swipeUp(2500);
        }
        String season1NumberOfEpisodeDownloads = String.valueOf(getEpisodeDownloadsOfSeason());
        LOGGER.info("Season 1 Total number of episode downloads: {}", season1NumberOfEpisodeDownloads);
        sa.assertTrue(season1NumberOfEpisodeDownloads.equalsIgnoreCase("10"),
                "Season 1 total number of episode downloads does not equal expected total of '10'");

        //Download all of season
        swipePageTillElementTappable(detailsPage.getDownloadAllSeasonButton(), 2, null, Direction.DOWN, 900);
        detailsPage.getDownloadAllSeasonButton().click();
        detailsPage.clickDownloadSeasonAlertButton();
        detailsPage.waitForTwoOrMoreHuluEpisodeDownloadsToComplete(250, 25);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        sa.assertTrue(downloads.getStaticTextByLabelContains("10 Episodes").isPresent(), "10 episode downloads were not found.");
        downloads.clickSeriesMoreInfoButton();
        sa.assertTrue(downloads.getStaticTextByLabelContains("Season 1").isPresent(), "Season 1 was not downloaded.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74562"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluSeriesDeleteAndPlay() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        int polling = 5;
        int episodeTimeout = 120;
        int percentageForNextTitle = 97;
        String one = "1";
        String two = "2";
        String videoPlayerNotOpen = "Video player did not open";
        String detailsNotOpen = "Details page did not open";
        String deleteButtonNotOpen = "Delete and Play button did not appear";

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        homePage.waitForHomePageToOpen();

        // Online + next episode is downloaded
        launchDeeplink(R.TESTDATA.get("hulu_prod_series_futurama_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), detailsNotOpen);
        if (R.CONFIG.get(DEVICE_TYPE).equals(PHONE)) {
            swipe(detailsPage.getEpisodeToDownload(), Direction.UP, 1, 900);
        }
        detailsPage.getEpisodeToDownload(one,one).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(episodeTimeout, polling);
        detailsPage.getEpisodeToDownload(one,two).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(episodeTimeout, polling);
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), videoPlayerNotOpen);
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(percentageForNextTitle);
        sa.assertTrue(videoPlayer.waitForDeleteAndPlayButton(),
                deleteButtonNotOpen);
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isOpened(), detailsNotOpen);
        detailsPage.clickCloseButton();

        // Online + next episode is NOT downloaded
        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_details_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), detailsNotOpen);
        detailsPage.getEpisodeToDownload(one,one).click();
        detailsPage.waitForOneEpisodeDownloadToComplete(episodeTimeout, polling);
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), videoPlayerNotOpen);
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(percentageForNextTitle);
        sa.assertTrue(videoPlayer.waitForDeleteAndPlayButton(),
                deleteButtonNotOpen);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75082"})
    @Test( groups = {TestGroup.DOWNLOADS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyJuniorProfileHuluFilteredOutDownloads() {
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        validateHuluDownloadsNotOnLowerMaturityProfile(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74903"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPCONProfileHuluFilteredOutDownloads() {
        SoftAssert sa = new SoftAssert();
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(RatingConstant.Rating.PG_13.getContentRating())
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        Profile profile = getUnifiedAccountApi().getDisneyProfiles(getUnifiedAccount()).get(1);

        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                profile.getProfileId(),
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystem(),
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues().get(5));

        validateHuluDownloadsNotOnLowerMaturityProfile(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66748"})
    @Test(groups = {TestGroup.DOWNLOADS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyRenewedExpiredDownload() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);

        jarvisEnableOfflineExpiredLicenseOverride();

        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_bluey_deeplink"));
        detailsPage.waitForDetailsPageToOpen();
        swipe(detailsPage.getFirstEpisodeDownloadButton(), Direction.UP, 1, 900);
        detailsPage.getFirstEpisodeDownloadButton().click();
        detailsPage.waitForFirstEpisodeToCompleteDownloadAndShowAsExpired(SIXTY_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        downloadsPage.clickSeriesMoreInfoButton();
        Assert.assertTrue(downloadsPage.getDownloadErrorButton().isElementPresent(),
                "Download Error button (Expired Download CTA) was not present");

        downloadsPage.getDownloadErrorButton().click();
        downloadsPage.getRenewLicenseButton().click();
        Assert.assertTrue(downloadsPage.getDownloadCompleteButton().isElementPresent(),
                "Download Error button was not updated to Download Complete button");
        Assert.assertFalse(downloadsPage.getDownloadTitleLicenseExpiredText().isElementPresent(FIVE_SEC_TIMEOUT),
                "Expired text under downloaded title was present");
    }

    public List<String> getListEpisodes(String element) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        List<String> episodeTitleList = new ArrayList<>();

        List<WebElement> episodeListElement = getDriver().findElements(detailsPage.getTypeButtonByName(element).getBy());
        if (!episodeListElement.isEmpty()) {
            for (WebElement title : episodeListElement) {
                episodeTitleList.add(title.getText());
            }
        }
        return episodeTitleList;
    }

    public boolean isDownloadsListOrdered(List<String> detailPageList, List<String> downloadPageList) {
        for (int i = 0; i < downloadPageList.size(); i++) {
            LOGGER.info("details title: {}, downloads title: {}", detailPageList.get(i), downloadPageList.get(i));
            if (!downloadPageList.get(i).contains(detailPageList.get(i).substring(3))) {
                return false;
            }
        }
        return true;
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

    private void validateHuluDownloadsNotOnLowerMaturityProfile(SoftAssert sa) {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount(), getUnifiedAccount().getProfiles().get(0).getProfileName());

        //Download TV-MA content
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            swipeInContainer(null, Direction.UP, 2500);
        }
        detailsPage.getEpisodeToDownload("1", "1").click();
        detailsPage.waitForOneEpisodeDownloadToComplete(180, 6);
        searchPage.clickSearchIcon();
        searchPage.clearText();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        if (PHONE.equalsIgnoreCase(DisneyConfiguration.getDeviceType())) {
            swipeInContainer(null, Direction.UP, 2500);
        }
        detailsPage.getMovieDownloadButton().click();
        detailsPage.waitForMovieDownloadComplete(300, 25);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloadsPage.isOpened(), DOWNLOADS_PAGE_DID_NOT_OPEN);
        sa.assertTrue(downloadsPage.getDownloadAssetFromListView(ONLY_MURDERS_IN_THE_BUILDING).isPresent(),
                ONLY_MURDERS_IN_THE_BUILDING + "was not found present on Downloads screen.");
        sa.assertTrue(downloadsPage.getDownloadAssetFromListView(PREY).isPresent(),
                PREY + "was not found present on Downloads screen.");

        //Validate on lower maturity rating profile downloaded assets not visible
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        whoIsWatching.clickProfile(getUnifiedAccountApi().getDisneyProfiles(getUnifiedAccount()).get(1).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        sa.assertTrue(downloadsPage.getDownloadAssetFromListView(ONLY_MURDERS_IN_THE_BUILDING).isElementNotPresent(SHORT_TIMEOUT),
                ONLY_MURDERS_IN_THE_BUILDING +  " was found present on " + getUnifiedAccount().getProfiles().get(1) + " profile's Downloads screen.");
        sa.assertTrue(downloadsPage.getDownloadAssetFromListView(PREY).isElementNotPresent(SHORT_TIMEOUT),
                PREY + " was found present on " + getUnifiedAccount().getProfiles().get(1) + " profile's Downloads screen.");
    }

    private void pauseScrubAndPlay(double scrubPercentage) {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickPauseButton();
        videoPlayer.scrubToPlaybackPercentage(scrubPercentage);
        videoPlayer.clickPlayButton();
        videoPlayer.waitForVideoToStart();
    }

    @AfterMethod(alwaysRun = true)
    public void removeJarvisApp() {
        boolean isInstalled = isAppInstalled(sessionBundles.get(JarvisAppleBase.JARVIS));
        if(isInstalled){
            removeJarvis();
        }
    }
}
