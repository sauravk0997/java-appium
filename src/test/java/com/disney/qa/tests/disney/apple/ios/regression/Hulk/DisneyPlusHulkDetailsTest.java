package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.pojos.*;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.IntStream;


import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.common.constant.RatingConstant.Rating.PG_13;
import static com.disney.qa.common.constant.RatingConstant.Rating.TV_PG;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;

public class DisneyPlusHulkDetailsTest extends DisneyBaseTest {
    private static final String PREY = "Prey";
    private static final String THE_BRAVEST_KNIGHT = "The Bravest Knight";
    private static final String BLUEY = "Bluey";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67891"})
    @Test(description = "Hulk Movie Details: Verify Details Tab Metadata", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkMovieDetailsTab() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.isOpened();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab was not found");

        detailsPage.clickDetailsTab();
        scrollDown();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Tablet")) {
            detailsPage.swipeTillActorsElementPresent();;
        }
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Detail Tab description not present");
        sa.assertTrue(detailsPage.isReleaseDateDisplayed(), "Detail Tab rating not present");
        sa.assertTrue(detailsPage.isGenreDisplayed(), "Detail Tab genre is not present");
        sa.assertTrue(detailsPage.isDurationDisplayed(), "Detail Tab duration is not present");
        sa.assertTrue(detailsPage.areFormatsDisplayed(), "Detail Tab formats not present");
        sa.assertTrue(detailsPage.isCreatorDirectorDisplayed(), "Detail Tab Creator not present");
        sa.assertTrue(detailsPage.areActorsDisplayed(), "Details Tab actors not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73825"})
    @Test(description = "Hulk Movie Details: Verify Tabs are visible", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkDetailsTabs() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.isOpened();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();

        //validate episodes tab
        sa.assertTrue(detailsPage.getEpisodesTab().isPresent(), "Episodes tab not present on Details page");
        detailsPage.getEpisodesTab().click();
        sa.assertTrue(detailsPage.getSeasonSelectorButton().isPresent(), "Season selector button not found on Episodes tab");

        //validate details tab
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab not present");
        detailsPage.clickDetailsTab();
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Detail Tab description not present on Details tab");

        //validate suggested tab
        sa.assertTrue(detailsPage.getSuggestedTab().isPresent(), "Suggest tab not present");
        detailsPage.compareSuggestedTitleToMediaTitle(sa);

        //validate extras tab
        sa.assertTrue(detailsPage.getExtrasTab().isPresent(), "Extras tab not present on Details page");
        detailsPage.clickExtrasTab();
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(), "Extras first title not present.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73820"})
    @Test(description = "Hulk Series & Movie Details - verify included with hulu subscription service attribution", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkSeriesAndMovieServiceAttribution() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        IntStream.range(0, getHuluMedia().size()).forEach(i -> {
            homePage.clickSearchIcon();
            if (searchPage.getClearTextBtn().isPresent(SHORT_TIMEOUT)) {
                searchPage.clearText();
            }
            searchPage.searchForMedia(getHuluMedia().get(i));
            List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
            results.get(0).click();
            detailsPage.isOpened();
            Assert.assertTrue(detailsPage.getServiceAttribution().isPresent(), "Service attribution was not found on Hulu series detail page.");
        });
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75021"})
    @Test(description = "Hulu Series Details Page - Restart Button", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void verifySeriesDetailsPageRestartButton() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.getPlayIcon().click();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(50);
        pause(5);
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.getRestartButton().isPresent(), "Restart button is not displayed on details page");
        detailsPage.getRestartButton().click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.getCurrentPositionOnPlayer() < 50, "video didn't start from the beginnning");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75020"})
    @Test(description = "Hulu Movies Details Page - Restart Button", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMovieDetailsPageRestartButton() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickPlayButton().isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(50);
        pause(5);
        videoPlayer.clickBackButton();
        waitUntil(ExpectedConditions.visibilityOfElementLocated(detailsPage.getRestartButton().getBy()), SHORT_TIMEOUT);
        sa.assertTrue(detailsPage.getRestartButton().isPresent(), "Restart button is not displayed on details page");
        detailsPage.getRestartButton().click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.getCurrentPositionOnPlayer() < 50, "video didn't start from the beginning");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72248"})
    @Test(description = "Hulk Details verify extras tab", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkExtrasTab() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.isOpened();
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        sa.assertTrue(detailsPage.isExtrasTabPresent(), "Extras tab was not found.");

        detailsPage.clickExtrasTab();
        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
            detailsPage.swipeUp(1500);
        }
        sa.assertTrue(detailsPage.isTabSelected(detailsPage.getExtrasTab().getAttribute(Attributes.NAME.getAttribute())),
                "EXTRAS Tab is not selected");
        sa.assertTrue(detailsPage.getPlayIcon().isPresent(), "Extras tab play icon was not found");
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(), "First extras title was not found");
        sa.assertTrue(detailsPage.getFirstDescriptionLabel().isPresent(), "First extras description was not found");
        sa.assertTrue(detailsPage.getFirstDurationLabel().isPresent(), "First extras runtime was not found");

        detailsPage.getPlayIcon().click();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.fluentWait(getDriver(), 60, 5, "Time remaining not found").until(it -> videoPlayer.getRemainingTime() <= 130);
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickSuggestedTab();
        detailsPage.waitForPresenceOfAnElement(detailsPage.getExtrasTab());
        detailsPage.clickExtrasTab();
        sa.assertTrue(detailsPage.isProgressBarPresent(), "Duration not displayed on extras trailer.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74254"})
    @Test(description = "Hulk Details verify share on adult and kids profile", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkShare() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoseWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(JUNIOR_PROFILE).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang()).avatarId(BABY_YODA).kidsModeEnabled(true).isStarOnboarded(true).build());
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        //Adult
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not found.");
        detailsPage.getShareBtn().click();
        sa.assertTrue(detailsPage.getTypeOtherByLabel("Prey | Disney+").isPresent(), "'Prey | Disney+' title was not found on share actions.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Messages").isPresent(), "Share action 'Messages' was not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Mail").isPresent(), "Share action 'Mail' was not found.");

        if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Tablet")) {
            detailsPage.clickHomeIcon();
        } else {
            detailsPage.getTypeButtonByLabel("Close").click();
        }

        //Kids
        homePage.clickMoreTab();
        whoseWatchingPage.clickProfile(JUNIOR_PROFILE);
        sa.assertTrue(homePage.isKidsHomePageOpen(), "Kids home page did not open");
        homePage.clickSearchIcon();
        searchPage.searchForMedia("I Am Groot");
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertFalse(detailsPage.getShareBtn().isPresent(), "Share button was found on kids profile.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73820"})
    @Test(description = "Hulk Network Attribution on various series/movie details pages - different networks", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkSeriesAndMovieNetworkAttribution() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        IntStream.range(0, getMedia().size()).forEach(i -> {
            homePage.clickSearchIcon();
            if (searchPage.getClearTextBtn().isPresent(SHORT_TIMEOUT)) {
                searchPage.clearText();
            }
            searchPage.searchForMedia(getMedia().get(i));
            List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
            results.get(0).click();
            sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
            if (DisneyConfiguration.getDeviceType().equalsIgnoreCase("Phone")) {
                Assert.assertTrue(detailsPage.getHandsetNetworkAttributionImage().isPresent(), "Handset Network attribution image was not found on " + i + " series details page.");
            } else {
                Assert.assertTrue(detailsPage.getTabletNetworkAttributionImage().isPresent(), "Tablet Network attribution image was not found on " + i + " series details page.");
            }
        });
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74845"})
    @Test(description = "Hulk Base UI - Movies - all attributes on base ui of movie details page", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHulkBaseUIMovies() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();

        validateBaseUI(sa, PREY);
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(1, detailsPage.getDuration(), 1),
                "Duration from metadata label does not match duration from details tab.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75083"})
    @Test(description = "Hulk Junior Mode - No Hulu content found", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyJuniorProfileNoHulu() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer =  initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(JUNIOR_PROFILE).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang()).avatarId(BABY_YODA).kidsModeEnabled(true).isStarOnboarded(true).build());
        setAppToHomeScreen(getAccount(), JUNIOR_PROFILE);

        //No upsell
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        sa.assertFalse(moreMenu.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT),
                "Account option was available to a child account, upsell option possible");

        //Home
        moreMenu.clickHomeIcon();
        homePage.isOpened();
        homePage.getKidsCarousels().forEach(element -> sa.assertFalse(element.getText().contains(HULU),
                String.format("%s contains %s", element.getText(), HULU)));
        sa.assertFalse(homePage.isHuluTileVisible(), "Hulu tile was found on Kids home.");
        sa.assertTrue(homePage.getStaticTextByLabelContains(HULU).isElementNotPresent(SHORT_TIMEOUT), "Hulu branding was found on Kids' Home page");

        //Search
        homePage.clickSearchIcon();
        sa.assertTrue(searchPage.getStaticTextByLabelContains(HULU).isElementNotPresent(SHORT_TIMEOUT), "Hulu branding was found on Kids' Search page");

        //Hulu Original Movie
        searchPage.searchForMedia(PREY);
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(PREY), PREY + " 'no results found' message not found.");

        //Hulu Original Kids Series
        searchPage.clearText();
        searchPage.searchForMedia(THE_BRAVEST_KNIGHT);
        sa.assertTrue(searchPage.isNoResultsFoundMessagePresent(THE_BRAVEST_KNIGHT), THE_BRAVEST_KNIGHT + " 'no results found' message not found.");

        //Details
        searchPage.clearText();
        searchPage.searchForMedia(BLUEY);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        sa.assertTrue(detailsPage.getStaticTextByLabelContains(HULU).isElementNotPresent(SHORT_TIMEOUT), "Hulu branding was found on Kids' Detail page");

        //Ad badge
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.displayVideoController();
        sa.assertFalse(videoPlayer.isAdBadgeLabelPresent(), "Ad badge found on Kids profile video content.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74450"})
    @Test(description = "Hulu Movie Download - download metadata and playable", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluMovieDownloadAsset() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        detailsPage.startDownload();
        detailsPage.waitForMovieDownloadComplete(350, 20);
        detailsPage.clickDownloadsIcon();
        downloadsPage.isOpened();

        //Downloaded movie asset metadata
        sa.assertTrue(downloadsPage.getStaticTextByLabelContains(PREY).isPresent(), PREY + " title was not found on downloads tab.");
        sa.assertTrue(downloadsPage.getDownloadedAssetImage(PREY).isPresent(), "Downloaded movie asset image was not found.");
        sa.assertTrue(downloadsPage.getSizeAndRuntime().isPresent(), "Downloaded movie asset size and runtime are not found.");
        sa.assertTrue(downloadsPage.getRating().getText().toLowerCase().contains("r"), "Movie downloaded asset rating not found.");

        //Playback of downloaded movie asset
        downloadsPage.tapDownloadedAsset(PREY);
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not launch.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74448"})
    @Test(description = "Hulu Ad Tier Movie and Series Details - No download buttons", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluAdTierMovieSeriesNoDownloadButton() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        //Movie
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        sa.assertFalse(detailsPage.isMovieDownloadButtonDisplayed(), "Movie download button is not displayed.");

        //Series
        detailsPage.clickSearchIcon();
        searchPage.clearText();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.isOpened();
        sa.assertFalse(detailsPage.getEpisodeToDownload("1", "1").isPresent(), "Season button 1 button is was found.");
        sa.assertFalse(detailsPage.getDownloadAllSeasonButton().isPresent(), "Download all season button was found.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74630"})
    @Test(description = "Hulu Detail Pages - Featured Area - Validate A/V Badges on details page for Ads Account", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluDetailPagesFeaturedAreaAVBadgesAdsAccount() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();

        sa.assertTrue(detailsPage.getStaticTextByLabelContains("HD").isPresent(), "`HD` video quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("5.1").isPresent(), "`5.1` audio quality is not found.");

        //Validate Dolby Vision present / not present on certain devices
        detailsPage.isDolbyVisionPresentOrNot(sa);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74630"})
    @Test(description = "Hulu Detail Pages - Featured Area - Validate A/V Badges on details page for No Ads Account", groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluDetailPagesFeaturedAreaAVBadgesNoAdsAccount() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.clickSearchIcon();
        searchPage.searchForMedia(ONLY_MURDERS_IN_THE_BUILDING);
        searchPage.getDisplayedTitles().get(0).click();

        sa.assertTrue(detailsPage.getStaticTextByLabelContains("HD").isPresent(), "`HD` video quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("5.1").isPresent(), "`5.1` audio quality is not found.");
        //Validate Dolby Vision present / not present on certain devices
        detailsPage.isDolbyVisionPresentOrNot(sa);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74876"})
    @Test(groups = {TestGroup.WATCHLIST, TestGroup.PRE_CONFIGURATION, US})
    public void verifyExpiredHuluWatchlistDisplay() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        String GRIMCUTTY = "Grimcutty";
        String WANDA_VISION = "WandaVision";
        String disneyPremiumPlan = "Disney+ Premium - 159.99 USD - Yearly";
        String trioBasicPlan = "Disney Bundle Trio Premium - 26.99 USD - Monthly";

        //Create account with Disney Bundle plan
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(trioBasicPlan)));
        loginToHome(getUnifiedAccount());

        //Add disney content to watchlist
        getWatchlistApi().addContentToWatchlist(getUnifiedAccount().getAccountId(),
                getUnifiedAccount().getAccountToken(),
                getUnifiedAccount().getProfileId(),
                getWatchlistInfoBlockForUnifiedAccount(DisneyEntityIds.WANDA_VISION.getEntityId()));

        //Add hulu content to watchlist
        getWatchlistApi().addContentToWatchlist(getUnifiedAccount().getAccountId(),
                getUnifiedAccount().getAccountToken(),
                getUnifiedAccount().getProfileId(),
                getWatchlistInfoBlockForUnifiedAccount(R.TESTDATA.get("disney_prod_hulu_movie_grimcutty_entity_id")));

        // Verify content on Watchlist
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        Assert.assertTrue(moreMenu.areWatchlistTitlesDisplayed(GRIMCUTTY, WANDA_VISION),
                "Titles were not added to the Watchlist");

        // Revoke HULU subscription
        getUnifiedSubscriptionApi().revokeSubscription(getUnifiedAccount(),
                getUnifiedAccount().getAgreement(0).getAgreementId());

        //Entitle account with D+
        UnifiedEntitlement disneyEntitlements = UnifiedEntitlement.builder()
                .unifiedOffer(getUnifiedOffer(disneyPremiumPlan)).subVersion(UNIFIED_ORDER).build();
        try {
            getUnifiedSubscriptionApi().entitleAccount(getUnifiedAccount(), Arrays.asList(disneyEntitlements));
        } catch (MalformedURLException | URISyntaxException | InterruptedException e) {
            Assert.fail("Failed to entitle the account with new entitlement");
        }

        // Terminate app and relaunch
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));

        // Verify content on watchlist after revoke HULU entitlement
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        Assert.assertFalse(moreMenu.getTypeCellLabelContains(GRIMCUTTY).isPresent(SHORT_TIMEOUT),
                "Hulu title was present in the Watchlist");
        Assert.assertTrue(moreMenu.getTypeCellLabelContains(WANDA_VISION).isPresent(),
                "Disney Plus title was not present in the Watchlist");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74586"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluDetailsPageRatings() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_teen_titans_go_deeplink"));
        detailsPage.verifyRatingsInDetailsFeaturedArea(TV_PG.getContentRating(), sa);
        detailsPage.validateRatingsInDetailsTab(TV_PG.getContentRating(), sa);

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_movie_bohemian_rhapsody_deeplink"));
        detailsPage.verifyRatingsInDetailsFeaturedArea(PG_13.getContentRating(), sa);
        detailsPage.validateRatingsInDetailsTab(PG_13.getContentRating(), sa);
    }

    protected ArrayList<String> getMedia() {
        ArrayList<String> contentList = new ArrayList<>();
        contentList.add(ONLY_MURDERS_IN_THE_BUILDING);
        contentList.add("Palm Springs");
        contentList.add("Home Economics");
        contentList.add("Cruel Summer");
        contentList.add("Devs");
        return contentList;
    }

    protected ArrayList<String> getHuluMedia() {
        ArrayList<String> contentList = new ArrayList<>();
        contentList.add(ONLY_MURDERS_IN_THE_BUILDING);
        contentList.add(PREY);
        return contentList;
    }

    private void validateBaseUI(SoftAssert sa, String mediaTitle) {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        detailsPage.isOpened();

        //media features - audio, video, accessibility
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("HD").isPresent(), "`HD` video quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("5.1").isPresent(), "`5.1` audio quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Subtitles / CC").isPresent(), "`Subtitles / CC` accessibility badge not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Audio Description").isPresent(), "`Audio Description` accessibility badge is not found.");

        //Validate Dolby Vision present / not present on certain devices
        detailsPage.isDolbyVisionPresentOrNot(sa);

        //back button, share button, title, description
        sa.assertTrue(detailsPage.getBackButton().isPresent(), "Back button is not found.");
        sa.assertTrue(detailsPage.getShareBtn().isPresent(), "Share button not found.");
        sa.assertTrue(detailsPage.getMediaTitle().contains(mediaTitle), "Prey media title not found.");
        sa.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Content Description not found.");

        //Release date, duration, genres, rating
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(0, detailsPage.getReleaseDate(), 1),
                "Release date from metadata label does not match release date from details tab.");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(2, detailsPage.getGenre(), 1),
                "Genre Thriller from metadata label does not match Genre Thriller from details tab.");
        sa.assertTrue(detailsPage.metadataLabelCompareDetailsTab(3, detailsPage.getGenre(), 2),
                "Genre Drama from metadata label does not match Genre Drama from details tab.");
        sa.assertTrue(detailsPage.getRating().isPresent(), "Rating not found.");

        //CTAs
        sa.assertTrue(detailsPage.getPlayButton().isPresent(), "Play CTA not found.");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Watchlist CTA not found.");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Trailer CTA not found.");

        //Restart
        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        videoPlayer.scrubToPlaybackPercentage(50);
        videoPlayer.clickBackButton();
        detailsPage.isOpened();
        sa.assertTrue(detailsPage.getRestartButton().isPresent(), "Restart button was not found.");

        //Tabs
        sa.assertTrue(detailsPage.isSuggestedTabPresent(), "Suggested tab not found.");
        sa.assertTrue(detailsPage.isExtrasTabPresent(), "Extras tab not found");
        sa.assertTrue(detailsPage.getDetailsTab().isPresent(), "Details tab not found");
    }
}
