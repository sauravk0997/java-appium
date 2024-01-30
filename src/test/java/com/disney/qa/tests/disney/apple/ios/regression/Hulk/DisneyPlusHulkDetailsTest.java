package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.TimeConstant.SHORT_TIMEOUT;

public class DisneyPlusHulkDetailsTest extends DisneyBaseTest {
    private static final String BABY_YODA = "f11d21b5-f688-50a9-8b85-590d6ec26d0c";
    private static final String PREY = "Prey";
    private static final String ONLY_MURDERS_IN_THE_BUILDING = "Only Murders in the Building";
    private static final String THE_BRAVEST_KNIGHT = "The Bravest Knight";
    private static final String BLUEY = "Bluey";
    private static final String HULU = "Hulu";

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74543"})
    @Test(description = "On Junior Profile verify unavailable details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyJuniorProfileDetailsUnavailableState() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(getAccount(), JUNIOR_PROFILE, KIDS_DOB, getAccount().getProfileLang(), BABY_YODA, true, true);

        setAppToHomeScreen(getAccount(), JUNIOR_PROFILE);
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent() ||  homePage.getUnavailableContentErrorPreview().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");
        pause(2);
        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");
        homePage.clickSearchIcon();
        searchPage.searchForMedia("Mickey");
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74568"})
    @Test(description = "On Adult profile verify unavailable details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyAdultProfileDetailsUnavailableState() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.isOpened();
        launchDeeplink(true, R.TESTDATA.get("disney_prod_generic_unavailable_deeplink"), 10);
        homePage.clickOpenButton();

        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), "Unavailable content error not present.");
        sa.assertTrue(homePage.getUnavailableOkButton().isPresent(), "Unavailable content error button not present.");

        homePage.getUnavailableOkButton().click();
        sa.assertTrue(homePage.isOpened(), "Home page not present");
        homePage.clickSearchIcon();
        searchPage.searchForMedia(PREY);
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74633"})
    @Test(description = "Hulk Movie Details: Verify Details Tab Metadata", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
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
        if (R.CONFIG.get("capabilities.deviceType").equalsIgnoreCase("Tablet")) {
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

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74830"})
    @Test(description = "Hulk Movie Details: Verify Tabs are visible", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74599"})
    @Test(description = "Hulk Series & Movie Details - verify included with hulu subscription service attribution", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkSeriesAndMovieServiceAttribution() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        IntStream.range(0, getHuluMedia().size()).forEach(i -> {
            homePage.clickSearchIcon();
            if (searchPage.getClearText().isPresent(SHORT_TIMEOUT)) {
                searchPage.clearText();
            }
            searchPage.searchForMedia(getHuluMedia().get(i));
            List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
            results.get(0).click();
            detailsPage.isOpened();
            Assert.assertTrue(detailsPage.getServiceAttribution().isPresent(), "Service attribution was not found on Hulu series detail page.");
        });
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75267"})
    @Test(description = "Hulu Series Details Page - Restart Button", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75281"})
    @Test(description = "Hulu Movies Details Page - Restart Button", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
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
        sa.assertTrue(detailsPage.getRestartButton().isPresent(), "Restart button is not displayed on details page");
        detailsPage.getRestartButton().click();
        videoPlayer.waitForVideoToStart();
        sa.assertTrue(videoPlayer.getCurrentPositionOnPlayer() < 50, "video didn't start from the beginnning");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73898"})
    @Test(description = "Hulk Details verify extras tab", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
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
        if (R.CONFIG.get("capabilities.deviceType").equalsIgnoreCase("Phone")) {
            detailsPage.swipeUp(1500);
        }
        sa.assertTrue(detailsPage.getPlayIcon().isPresent(), "Extras tab play icon was not found");
        sa.assertTrue(detailsPage.getFirstTitleLabel().isPresent(), "First extras title was not found");
        sa.assertTrue(detailsPage.getFirstDescriptionLabel().isPresent(), "First extras description was not found");
        sa.assertTrue(detailsPage.getFirstRunTimeLabel().isPresent(), "First extras runtime was not found");

        detailsPage.getPlayIcon().click();
        videoPlayer.isOpened();
        videoPlayer.waitForVideoToStart();
        videoPlayer.fluentWait(getDriver(), 60, 5, "Time remaining not found").until(it -> videoPlayer.getRemainingTime() <= 130);
        videoPlayer.clickBackButton();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickSuggestedTab();
        detailsPage.clickExtrasTab();
        sa.assertTrue(detailsPage.isProgressBarPresent(), "Duration not displayed on extras trailer.");
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74628"})
    @Test(description = "Hulk Details verify share on adult and kids profile", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkShare() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoseWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(getAccount(), JUNIOR_PROFILE, KIDS_DOB, getAccount().getProfileLang(), BABY_YODA, true, true);
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

        if (R.CONFIG.get("capabilities.deviceType").equalsIgnoreCase("Tablet")) {
            detailsPage.clickHomeIcon();
        } else {
            detailsPage.getTypeButtonByLabel("Close").click();
        }

        //Kids
        homePage.clickMoreTab();
        whoseWatchingPage.clickProfile(JUNIOR_PROFILE);
        homePage.clickSearchIcon();
        searchPage.searchForMedia("I Am Groot");
        searchPage.getDisplayedTitles().get(0).click();
        sa.assertFalse(detailsPage.getShareBtn().isPresent(), "Share button was found on kids profile.");
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74863", "XMOBQA-74548"})
    @Test(description = "Hulk Network Attribution on various series/movie details pages - different networks", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkSeriesAndMovieNetworkAttribution() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        IntStream.range(0, getMedia().size()).forEach(i -> {
            homePage.clickSearchIcon();
            if (searchPage.getClearText().isPresent(SHORT_TIMEOUT)) {
                searchPage.clearText();
            }
            searchPage.searchForMedia(getMedia().get(i));
            List<ExtendedWebElement> results = searchPage.getDisplayedTitles();
            results.get(0).click();
            sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
            if (R.CONFIG.get("capabilities.deviceType").equalsIgnoreCase("Phone")) {
                Assert.assertTrue(detailsPage.getHandsetNetworkAttributionImage().isPresent(), "Handset Network attribution image was not found on " + i + " series details page.");
            } else {
                Assert.assertTrue(detailsPage.getTabletNetworkAttributionImage().isPresent(), "Tablet Network attribution image was not found on " + i + " series details page.");
            }
        });
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74610"})
    @Test(description = "Hulk Base UI - Movies - all attributes on base ui of movie details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
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

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74933"})
    @Test(description = "Hulk Base UI - Series - all attributes on base ui of series details page", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkBaseUISeries() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_hulk_series_details_deeplink"), 10);
        detailsPage.clickOpenButton();
        detailsPage.isOpened();

        sa.assertTrue(detailsPage.doesOneOrMoreSeasonDisplayed(), "Season(s) not found.");
        validateBaseUI(sa, ONLY_MURDERS_IN_THE_BUILDING);
        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74916"})
    @Test(description = "Hulk Junior Mode - No Hulu content found", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyJuniorProfileNoHulu() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer =  initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(getAccount(), JUNIOR_PROFILE, KIDS_DOB, getAccount().getProfileLang(), BABY_YODA, true, true);
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

    protected ArrayList<String> getMedia() {
        ArrayList<String> contentList = new ArrayList<>();
        contentList.add(ONLY_MURDERS_IN_THE_BUILDING);
        contentList.add("Palm Springs");
        contentList.add("Home Economics");
        contentList.add("Cruel Summer");
        contentList.add("Praise Petey");
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
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Dolby Vision").isPresent(), "`Dolby Vision` video quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("5.1").isPresent(), "`5.1` audio quality is not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Subtitles / CC").isPresent(), "`Subtitles / CC` accessibility badge not found.");
        sa.assertTrue(detailsPage.getStaticTextByLabelContains("Audio Description").isPresent(), "`Audio Description` accessibility badge is not found.");

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
