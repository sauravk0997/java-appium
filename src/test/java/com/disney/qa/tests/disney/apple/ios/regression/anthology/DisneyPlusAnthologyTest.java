package com.disney.qa.tests.disney.apple.ios.regression.anthology;

import static com.disney.qa.common.DisneyAbstractPage.*;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BASIC_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.explore.response.Visuals;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAnthologyTest extends DisneyBaseTest {

    private static final String DANCING_WITH_THE_STARS = "Dancing with the Stars";
    private static final String PLAY = "PLAY";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72640"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologySearch() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        String entityID = R.TESTDATA.get("disney_prod_series_dwts_entity_id");
        Visuals visualsResponse = getExploreAPIPageVisuals(entityID);
        Map<String, Object> exploreAPIData = getContentMetadataFromAPI(visualsResponse);
        String year = exploreAPIData.get(RELEASE_YEAR_DETAILS).toString();
        String rating = exploreAPIData.get(RATING).toString();
        String genre = visualsResponse.getMetastringParts().getGenres().getValues().get(0);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        homePage.clickSearchIcon();
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);

        searchPage.searchForMedia(DANCING_WITH_THE_STARS);
        sa.assertTrue(searchPage.getStaticTextByLabel(DANCING_WITH_THE_STARS).isPresent(),
                "Anthology Search Result Title is not displayed");
        if (Stream.of(year, rating, genre).noneMatch(Objects::isNull)) {
            sa.assertTrue(searchPage.getTypeCellNameContains(year).isPresent(),
                    "Anthology Search Result Year is not displayed");
            sa.assertTrue(searchPage.getTypeCellNameContains(genre).isPresent(),
                    "Anthology Search Result Genre is not displayed");
            sa.assertTrue(searchPage.getTypeCellNameContains(rating).isPresent(),
                    "Anthology Search Result Rating is not displayed");
        } else {
            throw new SkipException("Series Episodes Metadata is not available from api");
        }

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72728"})
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void testAnthologyAddRemoveFromWatchlist() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_detailpage_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        //Add to Watchlist
        String mediaTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();
        sa.assertTrue(detailsPage.getRemoveFromWatchListButton().isPresent(),
                "Remove From Watchlist button is not displayed");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        Assert.assertTrue(moreMenu.isOpened(), MORE_MENU_NOT_DISPLAYED);
        moreMenu.getDynamicCellByLabel(
                moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        Assert.assertTrue(watchlistPage.isWatchlistScreenDisplayed(), WATCHLIST_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(watchlistPage.isWatchlistTitlePresent(mediaTitle),
                "Media title was not added to the Watchlist");

        //Remove from Watchlist
        watchlistPage.tapWatchlistContent(mediaTitle);
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        detailsPage.clickRemoveFromWatchlistButton();
        detailsPage.waitForWatchlistButtonToAppear();
        sa.assertEquals(detailsPage.getWatchlistButtonText(),
                "Add the current title to your Watchlist", "Add To Watchlist Text is not displayed");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        Assert.assertTrue(moreMenu.isOpened(), MORE_MENU_NOT_DISPLAYED);
        moreMenu.getDynamicCellByLabel(
                moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        Assert.assertTrue(watchlistPage.isWatchlistScreenDisplayed(), WATCHLIST_PAGE_NOT_DISPLAYED);
        sa.assertTrue(watchlistPage.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist is not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72247"})
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void testAnthologyEpisodesTab() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String currentSeason = "Season 33";
        String downloadAll = "Download All";

        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_detailpage_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        detailsPage.swipePageTillElementTappable(detailsPage.getFirstDescriptionLabel(), 1, null, Direction.UP, 1);
        sa.assertTrue(detailsPage.getEpisodesTab().isEnabled(), "Episodes tab is not focused");
        sa.assertTrue(detailsPage.getSeasonSelectorButton().isPresent(), "Season Selector is not displayed");
        LOGGER.info("This is the Expected Season: {}", detailsPage.getSeasonSelectorButton().getAttribute("label").toString());
        sa.assertTrue(detailsPage.getSeasonSelectorButton().getAttribute("label").equals(currentSeason),
                "Expected Season not displayed");
        detailsPage.getSeasonSelectorButton().click();
        sa.assertTrue(detailsPage.isSeasonPickerListInReverseChronologicalOrder(),
                "Season Picker is not in Chronological order");
        detailsPage.getItemPickerClose().click();
        sa.assertTrue(detailsPage.getDownloadAllSeasonButton().isPresent(), "Download Down Arrow button not displayed");
        sa.assertTrue(detailsPage.getDownloadAllSeasonButton().getAttribute("label").equals(downloadAll),
                "Download All text is not displayed");
        sa.assertTrue(detailsPage.isEpisodeInReverseChronologicalOrder(), "Episodes are not in Chronological Order");
        sa.assertTrue(detailsPage.isContentImageViewPresent(), "Content Image View not found on Episode container");
        sa.assertTrue(detailsPage.getPlayIcon().isPresent(), "Play Icon not found on Episodes container");
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(), "Episode title was not found");
        sa.assertTrue(detailsPage.getFirstDescriptionLabel().isPresent(), "Episode description was not found");
        sa.assertTrue(detailsPage.isDurationTimeLabelPresent(), "Episode duration was not found");
        sa.assertTrue(detailsPage.isSeriesDownloadButtonPresent("33", "10"),
                "Series Download Button is not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-73780" })
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyDetailsUIElements() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();
        String entityID = R.TESTDATA.get("disney_prod_series_dwts_entity_id");
        Visuals visualsResponse = getExploreAPIPageVisuals(entityID);
        Map<String, Object> exploreAPIData = getContentMetadataFromAPI(visualsResponse);

        sa.assertTrue(details.getBackButton().isPresent(), "Close button not present");
        sa.assertTrue(details.getShareBtn().isPresent(), SHARE_BTN_NOT_DISPLAYED);
        sa.assertTrue(details.isHeroImagePresent(), "Banner image not present");
        sa.assertTrue(details.isLogoImageDisplayed(), "Details page logo image not present");
        sa.assertEquals(details.getMediaTitle(), exploreAPIData.get(CONTENT_TITLE), MEDIA_TITLE_NOT_DISPLAYED);

        if (exploreAPIData.containsKey(CONTENT_PROMO_TITLE)) {
            sa.assertEquals(details.getPromoLabelText(), exploreAPIData.get(CONTENT_PROMO_TITLE).toString(),
                    "Promo label mismatch");
        }
        if (exploreAPIData.containsKey(AUDIO_VIDEO_BADGE)) {
            sa.assertTrue(((List<String>) exploreAPIData.get(AUDIO_VIDEO_BADGE))
                            .containsAll(details.getAudioVideoFormatValue()),
                    "Expected Audio and video badge not displayed");
        }
        if (exploreAPIData.containsKey(RATING)) {
            sa.assertTrue(details.getStaticTextByLabelContains(exploreAPIData.get(RATING).toString()).isPresent(),
                    "Rating value is not present on details page featured area");
        }
        if (exploreAPIData.containsKey(RELEASE_YEAR_DETAILS)) {
            sa.assertTrue(details.getStaticTextByLabelContains(exploreAPIData.get(RELEASE_YEAR_DETAILS).toString())
                            .isPresent(), "Release year value is not present on details page featured area");
        }
        String metadataString = details.getMetaDataLabel().getText();
        getGenreMetadataLabels(visualsResponse).forEach(value -> sa.assertTrue(metadataString.contains(value),
                String.format("%s value was not present on Metadata label", value)));

        sa.assertTrue(details.isPlayButtonDisplayed(), "Play button not present");
        sa.assertTrue(details.isTrailerButtonDisplayed(), TRAILER_BTN_NOT_DISPLAYED);
        sa.assertTrue(details.isWatchlistButtonDisplayed(), WATCHLIST_BTN_NOT_DISPLAYED);
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Content Description not found.");

        sa.assertTrue(details.getEpisodesTab().isPresent(), EPISODE_TAB_NOT_DISPLAYED);
        sa.assertTrue(details.getSuggestedTab().isPresent(), SUGGESTED_TAB_NOT_DISPLAYED);
        sa.assertTrue(details.getExtrasTab().isPresent(), EXTRAS_TAB_NOT_DISPLAYED);
        sa.assertTrue(details.getDetailsTab().isPresent(), DETAILS_TAB_NOT_DISPLAYED);
        sa.assertTrue(details.metadataLabelCompareDetailsTab(0, details.getReleaseDate(), 1),
                "Metadata label date year not found and does not match details tab year.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-73789" })
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyEpisodeDownload() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String Play = "Play";
        String one = "1";

        setAppToHomeScreen(getUnifiedAccount());

        //validate DWTS Episode Download
        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_detailpage_deeplink"));
        Assert.assertTrue(details.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        String mediaTitle = details.getMediaTitle();
        String seasonString = details.getSeasonSelectorButton().getText();
        String seasonNumber = seasonString.split(" ")[1];
        swipePageTillElementPresent(details.getEpisodeToDownload(seasonNumber, one), 2,
                null, Direction.UP, 1200);
        details.getEpisodeToDownload(seasonNumber, one).click();
        details.waitForOneEpisodeDownloadToComplete(THREE_HUNDRED_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);
        //Navigate to Download page
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_NOT_DISPLAYED);

        sa.assertTrue(mediaTitle.equalsIgnoreCase(downloads.getTypeOtherByLabel(DANCING_WITH_THE_STARS).getText()),
                DANCING_WITH_THE_STARS + " titles are not the same.");
        sa.assertTrue(downloads.getStaticTextByLabelContains("1 Episode").isPresent(), "1 episode was not found.");

        //Play downloaded episode
        downloads.getDynamicIosClassChainElementTypeImage(DANCING_WITH_THE_STARS).click();
        downloads.getTypeButtonContainsLabel(Play).click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.clickBackButton();
        sa.assertTrue(downloads.getProgressBar().isPresent(), "Progress bar not found.");

        //Remove Download
        downloads.clickEditButton();
        downloads.clickUncheckedCheckbox();
        sa.assertTrue(downloads.isCheckedCheckboxPresent(), "Checked checkbox is not found.");
        sa.assertTrue(downloads.getStaticTextByLabelContains("1 Selected").isPresent(), "1 Select is not found");
        downloads.clickDeleteDownloadButton();
        sa.assertTrue(downloads.isDownloadsEmptyHeaderPresent(), "Download was not removed, empty header not present.");

        //validate DWTS Season Download
        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_detailpage_deeplink"));
        Assert.assertTrue(details.waitForDetailsPageToOpen(), DETAILS_PAGE_NOT_DISPLAYED);
        String mediaTitleDWTS = details.getMediaTitle();
        details.downloadAllOfSeason();
        details.clickAlertConfirm();
        details.waitForOneEpisodeDownloadToComplete(THREE_HUNDRED_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloads.isOpened(), DOWNLOADS_PAGE_NOT_DISPLAYED);
        sa.assertTrue(mediaTitleDWTS.equalsIgnoreCase(downloads.getTypeOtherByLabel(DANCING_WITH_THE_STARS).getText()),
                DANCING_WITH_THE_STARS + " titles are not the same.");

        //Play downloaded episode
        downloads.getDynamicIosClassChainElementTypeImage(DANCING_WITH_THE_STARS).click();
        downloads.getTypeButtonContainsLabel(Play).click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-73802" })
    @Test(description = "Verify Anthology Series - VOD Progress", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyVODProgress() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, " + PLAY + " label not found, currently live content playing. " + e);
        }

        details.clickPlayButton();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.scrubToPlaybackPercentage(20);
        videoPlayer.waitForVideoToStart();

        videoPlayer.clickBackButton();
        details.waitForDetailsPageToOpen();
        sa.assertTrue(details.isContinueButtonPresent(), "Continue button was not found.");
        sa.assertTrue(details.getProgressBar().isPresent(), "Progress found not found.");
        sa.assertTrue(details.getContinueWatchingTimeRemaining().isPresent(TEN_SEC_TIMEOUT),
                "Continue watching time remaining is not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-72249" })
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyDetailsTab() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        searchAndOpenDWTSDetails();

        String mediaTitle = details.getMediaTitle();
        swipePageTillElementPresent(details.getDetailsTab(), 1, null, Direction.UP, 1000);
        sa.assertTrue(details.getDetailsTab().isPresent(), DETAILS_TAB_NOT_DISPLAYED);

        details.clickDetailsTab();
        swipePageTillElementPresent(details.getFormats(), 3, null, Direction.UP, 600);
        sa.assertTrue(details.getDetailsTabTitle().contains(mediaTitle), "Details tab title does not match media title");
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Details Tab description not present");
        sa.assertTrue(details.isReleaseDateDisplayed(), "Detail Tab rating is not present");
        sa.assertTrue(details.isRatingPresent(), "Details tab rating is not present");
        sa.assertTrue(details.isGenreDisplayed(), "Details Tab genre is not present");
        sa.assertTrue(details.areFormatsDisplayed(), "Details Tab formats are not present");
        sa.assertTrue(details.areActorsDisplayed(), "Details tab starring actors not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-72253" })
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologySuggestedTab() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isSuggestedTabPresent(), "Suggested tab was not found.");
        details.compareSuggestedTitleToMediaTitle(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-72252" })
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyFeaturedVOD() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, play button not found, currently live content playing", e);
        }

        Visuals visualsResponse = getExploreAPIPageVisuals(DisneyEntityIds.DANCING_WITH_THE_STARS.getEntityId());
        Map<String, Object> exploreAPIData = getContentMetadataFromAPI(visualsResponse);

        if (!exploreAPIData.containsKey(AUDIO_VIDEO_BADGE)) {
            throw new SkipException("Failed to fetch Audio/Visual badges for anthology series through Explore API");
        }

        if (!exploreAPIData.containsKey(RATING)) {
            throw new SkipException("Failed to fetch Rating for anthology series through Explore API");
        }

        sa.assertTrue(details.isLogoImageDisplayed(), "Logo image is not present");
        sa.assertTrue(details.isHeroImagePresent(), "Hero image is not present");
        ((List<String>) exploreAPIData.get(AUDIO_VIDEO_BADGE)).forEach(badge ->
                sa.assertTrue(details.getStaticTextByLabelContains(badge).isPresent(),
                        String.format("Audio/Visual badge '%s' is not present", badge)));
        sa.assertTrue(details.getStaticTextByLabelContains(exploreAPIData.get(RATING).toString()).isPresent(),
                String.format("%s rating was not present", exploreAPIData.get(RATING).toString()));
        sa.assertTrue(details.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(details.isWatchlistButtonDisplayed(), "Watchlist button is not displayed.");
        sa.assertTrue(details.isPlayButtonDisplayed(), "Play button is not found.");
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Content description is not found");

        details.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();
        sa.assertTrue(details.isContinueButtonPresent(), "Continue button is not present after exiting playback");
        sa.assertTrue(details.isProgressBarPresent(), "Progress bar is not present after exiting playback");
        sa.assertTrue(details.getTypeButtonContainsLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.
                ResourceKeys.APPLICATION, DictionaryKeys.BTN_DETAILS_RESTART.getText())).isPresent(),
                "Restart button is not displayed");
        sa.assertTrue(details.isProgressBarPresent(), "Progress bar is not present after exiting playback");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-73782" })
    @Test(description = "Verify Anthology Series - Trailer", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyTrailer() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isTrailerButtonDisplayed(), "Trailer button was not found.");

        details.getTrailerButton().click();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);

        videoPlayer.waitForTrailerToEnd(75, 5);
        sa.assertTrue(details.isOpened(), "After trailer ended, not returned to Details page.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74155"})
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyDeepLinkDetailPage() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_detailpage_deeplink"));
        Assert.assertTrue(details.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(details.getMediaTitle().equals(DANCING_WITH_THE_STARS),
                "Media title of detail page does not match " + DANCING_WITH_THE_STARS);

        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(DANCING_WITH_THE_STARS),
                "Content title doesn't match with the anthology title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73972"})
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyDownloadForAdTierUser() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());


        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_detailpage_deeplink"));
        Assert.assertTrue(details.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);

        swipe(details.getSeasonSelectorButton());
        Assert.assertFalse(details.getDownloadAllSeasonButton().isPresent(),
                "Download all season button displayed for ad tier user");
        Assert.assertFalse(details.getEpisodeToDownload().isPresent(),
                "Episode Download button is displayed for ad tier user");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73790"})
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPlaybackOfDownloadedEpisodeOnAnthologyDetailsPage() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        String seasonNumber = "32";
        String episodeNumber = "10";

        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_detailpage_deeplink"));
        detailsPage.getSeasonSelectorButton().click();
        detailsPage.getStaticTextByLabel("Season " + seasonNumber).click();
        ExtendedWebElement episodeToDownload = detailsPage.getEpisodeToDownload(seasonNumber, episodeNumber);
        detailsPage.swipePageTillElementPresent(episodeToDownload, 10,
                detailsPage.getContentDetailsPage(), Direction.UP, 1500);
        episodeToDownload.click();
        detailsPage.waitForOneEpisodeDownloadToComplete(ONE_HUNDRED_TWENTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT);
        String episodeTitle = detailsPage.getEpisodeCellTitle(seasonNumber, episodeNumber);
        detailsPage.getEpisodeCell(seasonNumber, episodeNumber).click();

        Assert.assertTrue(videoPlayer.isOpened(),
                "Video player did not open after choosing a downloaded episode");
        videoPlayer.waitForVideoToStart();
        String playerSubtitle = videoPlayer.getSubTitleLabel();
        Assert.assertTrue(playerSubtitle.contains(episodeTitle),
                "Video player title does not match with expected title: " + episodeTitle);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72675"})
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologySeriesPromotionLabel() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        String promoLabelHeader = getExploreAPIPageVisuals(R.TESTDATA.get("disney_prod_series_dwts_entity_id"))
                .getPromoLabel().getHeader();
        if (promoLabelHeader == null) {
            throw new SkipException("No promo label header found for the anthology content in Content API");
        }

        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_detailpage_deeplink"));
        Assert.assertEquals(detailsPage.getPromoLabelText(), promoLabelHeader,
                "API promo label header and UI promo label header text did not match");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72727"})
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyDetailsPageFeatureAreaDownloadsSupportVOD() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        String seasonLabel = "Season 32";
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_detailpage_deeplink"));
        detailsPage.getSeasonSelectorButton().click();
        detailsPage.getStaticTextByLabel(seasonLabel).click();
        swipe(detailsPage.getFirstEpisodeDownloadButton(), 5);
        Assert.assertTrue(detailsPage.getFirstEpisodeDownloadButton().isElementPresent(),
                "First episode download button was not present");

        detailsPage.getFirstEpisodeDownloadButton().click();
        downloadsPage.waitForDownloadToStart();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        Assert.assertTrue(downloadsPage.isDownloadInProgressTextPresent(),
                "Download start/in-progress text not found");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73971"})
    @Test(groups = {TestGroup.ANTHOLOGY, TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyMaturityRatingRestrictionErrorMessage() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        //set lower rating
        List<String> ratingSystemValues = getUnifiedAccount().getProfile(DEFAULT_PROFILE).getAttributes()
                .getParentalControls().getMaturityRating().getRatingSystemValues();
        if(ratingSystemValues.isEmpty()) {
            throw new IllegalArgumentException("Unable to get the rating system values for default profile");
        }

        getUnifiedAccountApi().editContentRatingProfileSetting(getUnifiedAccount(),
                getLocalizationUtils().getRatingSystem(),
                ratingSystemValues.get(0));

        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_detailpage_deeplink"));
        sa.assertTrue(details.isHeroImagePresent(), "Title background image is not present");
        sa.assertTrue(details.isLogoImageDisplayed(), MEDIA_TITLE_NOT_DISPLAYED);
        sa.assertTrue(details.isMetaDataLabelDisplayed(), "Metadata is not present");
        Assert.assertTrue(details.getParentalControlIcon().isPresent(), "Parental Control icon is not present");
        Assert.assertTrue(details.getRatingRestrictionDetailMessage().isPresent(),
                "Rating restriction detail message is not present");
        sa.assertFalse(details.isContentDescriptionDisplayed(), "Content description is present");
        sa.assertTrue(details.getTrailerButton().isElementNotPresent(ONE_SEC_TIMEOUT), "Trailer button is present");
        sa.assertTrue(details.getPlayButton().isElementNotPresent(ONE_SEC_TIMEOUT), "Play button is present");
        sa.assertTrue(details.getWatchlistButton().isElementNotPresent(ONE_SEC_TIMEOUT),
                "Add to watchlist button is present");
        sa.assertTrue(details.getTabBar().isElementNotPresent(ONE_SEC_TIMEOUT), "Tab container is present");
        sa.assertAll();
    }

    private void searchAndOpenDWTSDetails() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        homePage.clickSearchIcon();
        search.searchForMedia(DANCING_WITH_THE_STARS);
        search.waitForPresenceOfAnElement(search.getDisplayedTitles().get(0));
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
    }
}

