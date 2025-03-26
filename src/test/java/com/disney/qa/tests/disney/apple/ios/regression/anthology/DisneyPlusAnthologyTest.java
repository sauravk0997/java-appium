package com.disney.qa.tests.disney.apple.ios.regression.anthology;

import static com.disney.qa.common.DisneyAbstractPage.*;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BASIC_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.MEDIA_TITLE_NOT_DISPLAYED;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.fluentWaitNoMessage;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.TestGroup;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
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

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAnthologyTest extends DisneyBaseTest {

    //Test constants
    private static final String DANCING_WITH_THE_STARS = "Dancing with the Stars";
    private static final String PLAY = "PLAY";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page did not open";
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player did not open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72640"})
    @Test(description = "Verify Anthology Series - Search", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAnthologySearch() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(DANCING_WITH_THE_STARS);
        String[] firstDisplayTitle = searchPage.getDisplayedTitles().get(0).getText().split(",");
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.isOpened(), DANCING_WITH_THE_STARS + " "+DETAILS_PAGE_DID_NOT_OPEN);
        sa.assertTrue(firstDisplayTitle[0].equalsIgnoreCase(detailsPage.getMediaTitle()),
                "Search result title does not match Details page media title.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-72728" })
    @Test(description = "Verify Anthology Series - Watchlist", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAnthologyWatchlist() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();
        String mediaTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        sa.assertTrue(watchlistPage.areWatchlistTitlesDisplayed(mediaTitle), "Media title was not added.");
        moreMenu.getDynamicCellByLabel(mediaTitle).click();
        sa.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-72247" })
    @Test(description = "Verify Anthology Series - Ended, Compare episode number", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAnthologyEnded() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> detailsPage.isWatchButtonPresent());
        } catch (Exception e) {
            throw new SkipException("Skipping test, Watch button not found, no live content playing." + e);
        }

        Assert.assertFalse(detailsPage.compareEpisodeNum(), "Expected: Current episode number does not match new episode number.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-73780" })
    @Test(description = "Verify Anthology Series - Title, Description, Date", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologyTitleDescriptionDate() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.getMediaTitle().equalsIgnoreCase(DANCING_WITH_THE_STARS),
                "Media title of logo image does not match " + DANCING_WITH_THE_STARS);
        sa.assertTrue(details.metadataLabelCompareDetailsTab(0, details.getReleaseDate(), 1),
                "Metadata label date year not found and does not match details tab year.");
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Content Description not found.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-73789" })
    @Test(description = "Verify Anthology Series - Episode Download", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAnthologyEpisodeDownload() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloads = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        //Download episode
        details.isOpened();
        String mediaTitle = details.getMediaTitle();
        details.startDownload();
        sa.assertTrue(details.isSeriesDownloadButtonPresent(), "Series download button not found.");

        //Wait for download to complete and validate titles same.
        details.waitForSeriesDownloadToComplete(180, 9);
        details.clickDownloadsIcon();
        sa.assertTrue(downloads.isOpened(), "Downloads page was not opened.");
        sa.assertTrue(mediaTitle.equalsIgnoreCase(downloads.getTypeOtherByLabel(DANCING_WITH_THE_STARS).getText()),
                DANCING_WITH_THE_STARS + " titles are not the same.");
        sa.assertTrue(downloads.getStaticTextByLabelContains("1 Episode").isPresent(), "1 episode was not found.");

        //Play downloaded episode
        downloads.getDynamicIosClassChainElementTypeImage(DANCING_WITH_THE_STARS).click();
        downloads.getTypeButtonContainsLabel("Play").click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

        videoPlayer.clickBackButton();
        sa.assertTrue(downloads.getProgressBar().isPresent(), "Progress bar not found.");

        //Remove Download
        downloads.clickEditButton();
        downloads.clickUncheckedCheckbox();
        sa.assertTrue(downloads.isCheckedCheckboxPresent(), "Checked checkbox is not found.");
        sa.assertTrue(downloads.getStaticTextByLabelContains("1 Selected").isPresent(), "1 Select is not found");
        downloads.clickDeleteDownloadButton();
        sa.assertTrue(downloads.isDownloadsEmptyHeaderPresent(), "Download was not removed, empty header not present.");
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
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
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
    @Test(description = "Verify Anthology Series - Details Tab", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAnthologyDetailsTab() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        String mediaTitle = details.getMediaTitle();

        if ("Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE))) {
            swipeInContainer(null, Direction.UP, 1000);
        }
        sa.assertTrue(details.getDetailsTab().isPresent(), "Details tab is not found.");

        String selectorSeason = details.getSeasonSelector();
        details.clickDetailsTab();
        swipePageTillElementPresent(details.getFormats(), 3, null, IMobileUtils.Direction.UP, 600);
        sa.assertTrue(details.getDetailsTabTitle().contains(mediaTitle), "Details tab title does not match media title.");
        sa.assertTrue(details.isContentDescriptionDisplayed(), "Details Tab description not present");
        sa.assertTrue(details.isReleaseDateDisplayed(), "Detail Tab rating is not present");
        sa.assertTrue(details.getDetailsTabSeasonRating().contains(selectorSeason),
                "Details Tab season rating does not contain season selector text.");
        sa.assertTrue(details.isGenreDisplayed(), "Details Tab genre is not present.");
        sa.assertTrue(details.areFormatsDisplayed(), "Details Tab formats are not present.");
        sa.assertTrue(details.areActorsDisplayed(), "Details tab starring actors not present.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-72253" })
    @Test(description = "Verify Anthology Series - Suggested Tab", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAnthologySuggestedTab() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isSuggestedTabPresent(), "Suggested tab was not found.");
        details.compareSuggestedTitleToMediaTitle(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-72248" })
    @Test(description = "Verify Anthology Series - Extras Tab", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAnthologyExtrasTab() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        sa.assertTrue(details.isExtrasTabPresent(), "Extras tab was not found.");
        details.compareExtrasTabToPlayerTitle(sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = { "XMOBQA-72252" })
    @Test(description = "Verify Anthology Series - Featured VOD", groups = {TestGroup.ANTHOLOGY, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifyAnthologyFeaturedVOD() {
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        searchAndOpenDWTSDetails();

        try {
            fluentWaitNoMessage(getDriver(), 15, 2).until(it -> details.isPlayButtonDisplayed());
        } catch (Exception e) {
            throw new SkipException("Skipping test, play button not found, currently live content playing." + e);
        }

        sa.assertTrue(details.isLogoImageDisplayed(), "Logo image is not present.");
        sa.assertTrue(details.isHeroImagePresent(), "Hero image is not present.");
        sa.assertTrue(details.getStaticTextByLabelContains("TV-PG").isPresent(), "TV-MA rating was not found.");
        sa.assertTrue(details.getStaticTextByLabelContains("HD").isPresent(), "HD was not found.");
        sa.assertTrue(details.getStaticTextByLabelContains("5.1").isPresent(), "5.1 was not found.");
        sa.assertTrue(details.getStaticTextByLabelContains("Subtitles for the Deaf and Hearing Impaired").isPresent(),
                "Subtitles advisory was not found.");
        sa.assertTrue(details.getStaticTextByLabelContains("Audio Descriptions").isPresent(), "Audio description advisory was not found.");
        sa.assertTrue(details.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(details.isWatchlistButtonDisplayed(), "Watchlist button is not displayed.");
        sa.assertTrue(details.isPlayButtonDisplayed(), "Play button is not found.");

        details.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.clickBackButton();
        sa.assertTrue(details.isContinueButtonPresent(), "Continue button is not present after exiting playback.");
        sa.assertTrue(details.isProgressBarPresent(), "Progress bar is not present after exiting playback.");
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
        sa.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);

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
        Assert.assertTrue(details.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);
        Assert.assertTrue(details.getMediaTitle().equals(DANCING_WITH_THE_STARS),
                "Media title of detail page does not match " + DANCING_WITH_THE_STARS);

        launchDeeplink(R.TESTDATA.get("disney_prod_series_dwts_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
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
        Assert.assertTrue(details.isOpened(), DETAILS_PAGE_DID_NOT_OPEN);

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
        detailsPage.waitForOneEpisodeDownloadToComplete(ONE_HUNDRED_TWENTY_SEC_TIMEOUT, FIVE_SEC_TIMEOUT);
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
        search.getDisplayedTitles().get(0).click();
        details.isOpened();
    }
}

