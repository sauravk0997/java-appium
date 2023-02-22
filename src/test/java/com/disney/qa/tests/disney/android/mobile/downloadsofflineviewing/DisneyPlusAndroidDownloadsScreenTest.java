package com.disney.qa.tests.disney.android.mobile.downloadsofflineviewing;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

import static com.disney.qa.tests.disney.android.mobile.videoplayer.DisneyPlusAndroidVideoPlayerNoAdsTest.DISNEY_PROD_MOVIES_DEEPLINK;
import static com.disney.qa.tests.disney.android.mobile.videoplayer.DisneyPlusAndroidVideoPlayerNoAdsTest.DISNEY_PROD_SERIES_DEEPLINK;

public class DisneyPlusAndroidDownloadsScreenTest extends BaseDisneyTest {

    private static final String THE_SIMPSONS = "The Simpsons";
    private static final String INNER_WORKINGS = "Inner Workings";
    private static final String SERIES_THE_SIMPSONS_DEEPLINK = "/the-simpsons/3ZoBZ52QHb4x";
    private static final String MOVIE_DIARY_OF_A_WIMPY_KID_DEEPLINK = "/diary-of-a-wimpy-kid/3W4BZbeErSgN";
    private static final String MOVIE_DIARY_OF_A_WIMPY_KID_DURATION = "59m";

    @AfterMethod(alwaysRun = true)
    private void tearDown() {
        clearAppCache();
    }

    private void setup() {
        try {
            accountApi.get().setAccountDownloadLimit(disneyAccount.get(), 150);
            login(disneyAccount.get(), false);
            dismissChromecastOverlay();
        } catch (IOException ioe) {
            Assert.fail("An IO Exception occurred." + ioe);
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66668"})
    @Test(description = "Verify the UI of the downloads page when empty", groups = {"Downloads"})
    public void verifyEmptyDownloadsPageUI() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1630"));
        SoftAssert sa = new SoftAssert();
        setup();
        initPage(DisneyPlusCommonPageBase.class).navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText()));
        DisneyPlusDownloadsPageBase downloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);

        Assert.assertTrue(downloadsPageBase.isOpened(),
                "Downloads page was not opened.");

        sa.assertEquals(downloadsPageBase.getHeaderText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOADS_TITLE.getText()),
                "Downloads header was not displayed or is incorrect");

        sa.assertTrue(downloadsPageBase.isEmptyStateBackgroundImageDisplayed(),
                "Empty State download image was not displayed.");

        sa.assertEquals(downloadsPageBase.getEmptyStateTitle(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOADS_EMPTY_HEADER.getText()),
                "Downloads title was not displayed or is incorrect.");

        sa.assertEquals(downloadsPageBase.getEmptyStateDetails(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOADS_EMPTY_COPY.getText()),
                "Downloads empty-state description was not displayed or is incorrect.");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66670"})
    @Test(description = "Verify downloaded content UI on download screen", groups = {"Downloads"})
    public void verifyPopulatedDownloadsPageUI() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1631"));

        DisneyPlusMediaPageBase disneyPlusMediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusDownloadsPageBase disneyPlusDownloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);
        DisneyPlusDiscoverPageBase disneyPlusDiscoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusContentMetadataPageBase contentMetadataPageBase = initPage(DisneyPlusContentMetadataPageBase.class);

        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);

        Assert.assertTrue(disneyPlusDiscoverPageBase.isOpened(),
                "Discover page not displayed");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_DIARY_OF_A_WIMPY_KID_DEEPLINK);

        Assert.assertTrue(disneyPlusMediaPageBase.isOpened(),
                "Details page not displayed");

        String mediaTitle = disneyPlusMediaPageBase.getMediaTitle();
        disneyPlusMediaPageBase.beginDownload();
        disneyPlusMediaPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText()));

        Assert.assertTrue(disneyPlusDownloadsPageBase.isOpened(),
                "Download screen is not displayed");

        disneyPlusDownloadsPageBase.waitForDownload(mediaTitle);

        sa.assertEquals(disneyPlusDownloadsPageBase.getHeaderText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOADS_TITLE.getText()),
                "Downloads header is not displayed");

        sa.assertTrue(disneyPlusDownloadsPageBase.isTextElementPresent(mediaTitle),
                "Downloaded media title is not displayed");

        sa.assertEquals(disneyPlusDownloadsPageBase.getEditButtonText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EDIT.getText()),
                "Edit button is not displayed");

        sa.assertTrue(disneyPlusDownloadsPageBase.isDownloadsPlayButtonDisplayed(mediaTitle),
                "Play Button is not displayed");

        String metadata = disneyPlusDownloadsPageBase.getMetaData().format(mediaTitle).getText();
        PAGEFACTORY_LOGGER.info("Metadata: " + metadata);

        sa.assertEquals(disneyPlusDownloadsPageBase.getStandardMediaListingMetadataRating(mediaTitle),
                searchApi.get().getMovie(mediaTitle, disneyAccount.get()).getContentRatingsValue(),
                "Rating is not displayed");

        sa.assertTrue(metadata.contains("MB"),
                "Content size parameter 'MB' not displayed");

        sa.assertTrue(metadata.contains(MOVIE_DIARY_OF_A_WIMPY_KID_DURATION),
                "Media metadata runtime does not match UI content duration.");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66672"})
    @Test(description = "Verify the UI elements when Edit Mode is enabled and associated functions", groups = {"Downloads"})
    public void testEditModeUI() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1632"));

        DisneyPlusMediaPageBase disneyPlusMediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusDiscoverPageBase disneyPlusDiscoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusDownloadsPageBase disneyPlusDownloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);
        DisneyPlusContentMetadataPageBase contentMetadataPageBase = initPage(DisneyPlusContentMetadataPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);

        Assert.assertTrue(disneyPlusDiscoverPageBase.isOpened(),
                "Discover page not displayed");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_MOVIES_DEEPLINK) + MOVIE_DIARY_OF_A_WIMPY_KID_DEEPLINK);

        Assert.assertTrue(disneyPlusMediaPageBase.isOpened(),
                "Details page not displayed");

        String mediaTitle = disneyPlusMediaPageBase.getMediaTitle();
        disneyPlusMediaPageBase.beginDownload();
        disneyPlusMediaPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText()));

        Assert.assertTrue(disneyPlusDownloadsPageBase.isOpened(),
                "Download screen is not displayed");

        disneyPlusDownloadsPageBase.waitForDownload(mediaTitle);
        disneyPlusDownloadsPageBase.clickEditButton();

        sa.assertEquals(disneyPlusDownloadsPageBase.getCancelModeDescriptionText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SELECT_CONTENT_REMOVE.getText()),
                "Edit Mode description not displayed");

        sa.assertTrue(disneyPlusDownloadsPageBase.isSelectAllEnabled(),
                "Select all is not displayed");

        sa.assertTrue(disneyPlusDownloadsPageBase.isTextElementPresent(mediaTitle),
                "Downloaded content is not displayed");

        sa.assertTrue(disneyPlusDownloadsPageBase.isSelectItemCheckboxDisplayed(),
                "Checkbox is not displayed");

        sa.assertTrue(disneyPlusDownloadsPageBase.isMediaDownloadThumbnailDisplayed(),
                "Asset thumbnail was not displayed");

        String metadata = disneyPlusDownloadsPageBase.getMetaData().format(mediaTitle).getText();
        PAGEFACTORY_LOGGER.info("Metadata: " + metadata);

        sa.assertEquals(disneyPlusDownloadsPageBase.getStandardMediaListingMetadataRating(mediaTitle),
                searchApi.get().getMovie(mediaTitle, disneyAccount.get()).getContentRatingsValue(),
                "Rating displayed was not displayed");

        sa.assertTrue(metadata.contains("MB"),
                "Media file size parameter 'MB' not displayed");

        sa.assertTrue(metadata.contains(MOVIE_DIARY_OF_A_WIMPY_KID_DURATION),
                "Media metadata runtime does not match UI content duration.");

        disneyPlusDownloadsPageBase.clickSelectItemCheckbox();

        sa.assertTrue(disneyPlusDownloadsPageBase.isTrashButtonDisplayed(),
                "Trash Button was not displayed");

        sa.assertTrue(disneyPlusDownloadsPageBase.isDeselectItemCheckboxDisplayed(),
                "Check box was not changed to deselect");

        sa.assertTrue(disneyPlusDownloadsPageBase.isDeselectAllEnabled(),
                "Select all did not convert to deselect all");

        disneyPlusDownloadsPageBase.clickSelectAllButton();

        sa.assertTrue(disneyPlusDownloadsPageBase.isSelectAllEnabled(),
                "Select All was not set after clicking it in Deselect All mode.");

        sa.assertTrue(disneyPlusDownloadsPageBase.isSelectItemCheckboxDisplayed(),
                "Checkbox was not unchecked after clicking Deselect All.");

        disneyPlusDownloadsPageBase.clickCancelEditModeButton();

        sa.assertFalse(disneyPlusDownloadsPageBase.isSelectAllEnabled(),
                "Select All remained active after Cancel Edit Mode was clicked");

        sa.assertFalse(disneyPlusDownloadsPageBase.isTrashButtonDisplayed(),
                "Trash Button remained active after Cancel Edit Mode was clicked");

        sa.assertFalse(disneyPlusDownloadsPageBase.isSelectItemCheckboxDisplayed(),
                "Checkbox remained visible after Cancel Edit Mode was clicked.");

        disneyPlusDownloadsPageBase.deleteAllDownloads();

        sa.assertFalse(disneyPlusDownloadsPageBase.isMediaDownloadThumbnailDisplayed(),
                "Media item is visible");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66692"})
    @Test(description = "Verify the download icon and badge with 99 downloads in queue", groups = {"Downloads"})
    public void verifyDownloadBadgeExactlyNinetyNine() {
        DisneyPlusDiscoverPageBase disneyPlusDiscoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusMoreMenuPageBase disneyPlusMoreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusContentMetadataPageBase contentMetadataPageBase = initPage(DisneyPlusContentMetadataPageBase.class);
        SoftAssert sa = new SoftAssert();

        setup();

        Assert.assertTrue(disneyPlusDiscoverPageBase.isOpened(),
                "Discover screen not visible");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_download_quality_deeplink"));
        disneyPlusMoreMenuPageBase.selectDownloadQuality(DisneyPlusMoreMenuPageBase.DownloadQualityItems.MEDIUM);
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_THE_SIMPSONS_DEEPLINK);

        Assert.assertTrue(mediaPageBase.isOpened(),
                "Details screen not visible");

        mediaPageBase.downloadSeriesSeasonsOverTwentyEpisodes(
                100,
                false,
                THE_SIMPSONS,
                sa,
                languageUtils.get(),
                searchApi.get(),
                disneyAccount.get(),
                contentMetadataPageBase);

        Assert.assertTrue(mediaPageBase.isNinetyNineDownloadBadgeVisible(), "99 download badge not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72012"})
    @Test(description = "Verify the download icon and badge with over 99 downloads in queue", groups = {"Downloads"})
    public void verifyDownloadBadgeDisplayOverNinetyNine() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1633"));

        DisneyPlusDiscoverPageBase disneyPlusDiscoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusMoreMenuPageBase disneyPlusMoreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        DisneyPlusContentMetadataPageBase contentMetadataPageBase = initPage(DisneyPlusContentMetadataPageBase.class);
        SoftAssert sa = new SoftAssert();

        setup();

        Assert.assertTrue(disneyPlusDiscoverPageBase.isOpened(),
                "Discover screen not visible");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_download_quality_deeplink"));
        disneyPlusMoreMenuPageBase.selectDownloadQuality(DisneyPlusMoreMenuPageBase.DownloadQualityItems.HIGH);
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get(DISNEY_PROD_SERIES_DEEPLINK) + SERIES_THE_SIMPSONS_DEEPLINK);

        Assert.assertTrue(mediaPageBase.isOpened(),
                "Details screen not visible");

        mediaPageBase.downloadSeriesSeasonsOverTwentyEpisodes(
                100,
                true,
                THE_SIMPSONS,
                sa,
                languageUtils.get(),
                searchApi.get(),
                disneyAccount.get(),
                contentMetadataPageBase);

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66694"})
    @Test(description = "Verify the popup menu functions for Pause, Resume, Dismiss, and Remove for a download in progress", groups = {"Downloads"})
    public void verifyDownloadInProgressSubMenuFunctions() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1634"));
        SoftAssert sa = new SoftAssert();
        setup();
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        searchPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));
        searchPageBase.clickOnGenericTextElement(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusSearchPageBase.ScreenTitles.MOVIES.getText()));
        DisneyPlusMediaPageBase mediaPageBase = searchPageBase.openFirstCategoryItem();
        String mediaTitle = mediaPageBase.getMediaTitle();
        mediaPageBase.beginDownload();
        mediaPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText()));
        DisneyPlusDownloadsPageBase downloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);

        Assert.assertTrue(downloadsPageBase.isOpened(),
                "Downloads page did not open.");

        downloadsPageBase.clickDownloadStatus();
        downloadsPageBase.clickOnGenericTextElement(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_PAUSE_DOWNLOAD.getText()));
        downloadsPageBase.clickDownloadStatus();

        sa.assertEquals(downloadsPageBase.getMetaData().format(mediaTitle).getText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_PAUSED.getText()),
                "Download was not changed paused after clicking on Pause Download button.");

        new AndroidUtilsExtended().clickElementAtLocation(downloadsPageBase.getAppRootDisplay(), 50, 50);

        Assert.assertFalse(downloadsPageBase.isBottomSheetMenuDisplayed(),
                "Tapping outside of the sheet menu did not dismiss it.");

        downloadsPageBase.clickDownloadStatus();
        downloadsPageBase.clickOnGenericTextElement(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_RESUME_DOWNLOAD.getText()));
        downloadsPageBase.clickDownloadStatus();

        sa.assertTrue(downloadsPageBase.getMetaData().format(mediaTitle).getText().contains(languageUtils.get().getValueBeforePlaceholder(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_IN_PROGRESS_PERCENT.getText()))),
                "Download was not resumed after clicking on Resume Download button.");

        downloadsPageBase.clickOnGenericTextElement(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.REMOVE_DOWNLOAD_BTN.getText()));

        sa.assertTrue(downloadsPageBase.isEmptyStateBackgroundImageDisplayed(),
                "Download was not removed after clicking on Remove Download button.");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66706"})
    @Test(description = "Verify the popup menu functions for Play, Dismiss, and Remove for a completed download", groups = {"Downloads"})
    public void verifyDownloadCompleteSubMenuFunctions() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1635"));

        DisneyPlusDownloadsPageBase downloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);
        dismissChromecastOverlay();
        searchPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));

        sa.assertTrue(searchPageBase.isOpened(),
                "Search screen is not displayed");

        searchPageBase.clickSearchBar();

        sa.assertTrue(androidUtils.get().isKeyboardShown(),
                "Virtual keyboard not displayed");

        searchPageBase.searchForMedia(INNER_WORKINGS);
        searchPageBase.openSearchResult(INNER_WORKINGS);
        mediaPageBase.beginDownload();
        mediaPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText()));

        Assert.assertTrue(downloadsPageBase.isOpened(),
                "Downloads page did not open.");

        downloadsPageBase.waitForDownload(INNER_WORKINGS);
        downloadsPageBase.clickDownloadStatus();

        androidUtils.get().clickElementAtLocation(downloadsPageBase.getAppRootDisplay(), 50, 50);

        Assert.assertFalse(downloadsPageBase.isBottomSheetMenuDisplayed(),
                "Tapping outside of the sheet menu did not dismiss it.");

        downloadsPageBase.clickDownloadStatus();
        downloadsPageBase.clickOnGenericTextElement(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.PLAY.getText()));

        Assert.assertTrue(videoPageBase.isOpened(),
                "Video Player was not opened after clicking the Play button.");

        sa.assertEquals(videoPageBase.getActiveMediaTitle(), INNER_WORKINGS,
                "Video Player did not open the correct media after clicking the Play button.");

        videoPageBase.closeVideo();
        downloadsPageBase.clickDownloadStatus();
        downloadsPageBase.clickOnGenericTextElement(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.REMOVE_DOWNLOAD_BTN.getText()));

        sa.assertTrue(downloadsPageBase.isEmptyStateBackgroundImageDisplayed(),
                "Download was not removed after clicking on Remove Download button.");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66718"})
    @Test(description = "Verify the popup menu functions for Pause, Resume, Dismiss, and Remove for a download in progress", groups = {"Downloads"})
    public void verifyQueuedInProgressSubMenuFunctions() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1636"));
        SoftAssert sa = new SoftAssert();
        setup();
        AndroidUtilsExtended util = new AndroidUtilsExtended();
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        searchPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));
        searchPageBase.clickOnGenericTextElement(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusSearchPageBase.ScreenTitles.MOVIES.getText()));
        DisneyPlusMediaPageBase mediaPageBase = searchPageBase.openFirstCategoryItem();
        String mediaTitle = mediaPageBase.getMediaTitle();
        mediaPageBase.beginDownload();
        mediaPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText()));
        DisneyPlusDownloadsPageBase downloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);

        Assert.assertTrue(downloadsPageBase.isOpened(),
                "Downloads page did not open.");

        util.disableWifi();
        downloadsPageBase.clickDownloadStatus();

        sa.assertEquals(downloadsPageBase.getMetaData().format(mediaTitle).getText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_QUEUED.getText()),
                "Download was not changed Queued when interrupted.");

        new AndroidUtilsExtended().clickElementAtLocation(downloadsPageBase.getAppRootDisplay(), 50, 50);

        Assert.assertFalse(downloadsPageBase.isBottomSheetMenuDisplayed(),
                "Tapping outside of the sheet menu did not dismiss it.");

        downloadsPageBase.clickDownloadStatus();
        downloadsPageBase.clickOnGenericTextElement(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.REMOVE_DOWNLOAD_BTN.getText()));

        sa.assertTrue(downloadsPageBase.isEmptyStateBackgroundImageDisplayed(),
                "Download was not removed after clicking on Remove Download button.");

        util.enableWifi();
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66724"})
    @Test(description = "Verify download pauses with Wi-Fi Only Enabled, Download Resumes once user has Wi-Fi", groups = {"Downloads"})
    public void verifyWifiTogglingInteractions() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1637"));
        SoftAssert sa = new SoftAssert();
        setup();
        AndroidUtilsExtended util = new AndroidUtilsExtended();
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        searchPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));
        searchPageBase.clickOnGenericTextElement(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusSearchPageBase.ScreenTitles.MOVIES.getText()));
        DisneyPlusMediaPageBase mediaPageBase = searchPageBase.openFirstCategoryItem();
        mediaPageBase.beginDownload();
        mediaPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.DOWNLOADS.getText()));
        DisneyPlusDownloadsPageBase downloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);

        Assert.assertTrue(downloadsPageBase.isOpened(),
                "Downloads page did not open.");

        util.disableWifi();

        downloadsPageBase.clickDownloadStatus();

        sa.assertEquals(downloadsPageBase.getDownloadStatusText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_QUEUED.getText()),
                "Download status was not changed to 'Download is queued' after disabling WiFi");

        util.enableWifi();
        Assert.assertTrue(util.isWifiEnabled(), "Failed to enable Wi-Fi");

        downloadsPageBase.waitForDownloadInProgress();

        sa.assertTrue(downloadsPageBase.getDownloadStatusText().contains(languageUtils.get().getValueBeforePlaceholder(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_IN_PROGRESS_PERCENT.getText()))),
                "Download status was not changed to 'Download in progress' after enabling WiFi");

        checkAssertions(sa);
    }
}
