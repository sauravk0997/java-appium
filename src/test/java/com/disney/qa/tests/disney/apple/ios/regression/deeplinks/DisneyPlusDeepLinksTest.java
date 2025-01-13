package com.disney.qa.tests.disney.apple.ios.regression.deeplinks;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;

import static com.disney.qa.common.DisneyAbstractPage.THREE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.ONLY_MURDERS_IN_THE_BUILDING;

public class DisneyPlusDeepLinksTest extends DisneyBaseTest {

    private static final String CONTENT_UNAVAILABLE_ERROR = "Content Unavailable Error not displayed";
    private static final String CONTENT_UNAVAILABLE_OK_ERROR = "Content Unavailable Error OK cta is not displayed";
    private static final String HOME_PAGE_NOT_DISPLAYED = "Home page not displayed";
    private static final String WATCHLIST_IS_EMPTY_ERROR = "Your watchlist is empty";
    private static final String WATCHLIST_DEEP_LINK_ERROR = "Watchlist Page did not open via Deep Link";
    private static final String VIDEO_PLAYER_DID_NOT_OPEN = "Video player did not open";

    @DataProvider(name = "watchlistDeepLinks")
    public Object[][] watchlistDeepLinks() {
        return new Object[][]{{R.TESTDATA.get("disney_prod_watchlist_deeplink_2")},
                {R.TESTDATA.get("disney_prod_watchlist_deeplink_language")}
        };
    }

    @DataProvider(name = "huluNetworkDeepLinks")
    public Object[][] huluNetworkDeepLinks() {
        return new Object[][]{{R.TESTDATA.get("disney_prod_hulu_abc_network_deeplink")},
                {R.TESTDATA.get("disney_prod_hulu_abc_network_language_deeplink")}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67520"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHomeDeeplink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_home_deeplink"));
        Assert.assertTrue(homePage.isOpened(), "Home page did not open via deeplink");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67547"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkWatchlist() {
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        String legacyWatchlistDeepLink = R.TESTDATA.get("disney_prod_watchlist_deeplink_legacy");
        launchDeeplink(legacyWatchlistDeepLink);
        Assert.assertTrue(watchlistPage.getStaticTextByLabelContains(WATCHLIST_IS_EMPTY_ERROR).isPresent(), WATCHLIST_DEEP_LINK_ERROR);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74588"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US}, dataProvider = "watchlistDeepLinks")
    public void verifyDeepLinkNewURLStructureWatchlistAuthenticatedUser(String deepLink) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        launchDeeplink(deepLink);
        sa.assertTrue(watchlistPage.getStaticTextByLabelContains(WATCHLIST_IS_EMPTY_ERROR).isPresent(),
                WATCHLIST_DEEP_LINK_ERROR);

        terminateApp(BuildType.ENTERPRISE.getDisneyBundle());
        launchDeeplink(deepLink);
        homePage.handleSystemAlert(AlertButtonCommand.DISMISS, 1);
        sa.assertTrue(watchlistPage.getStaticTextByLabelContains(WATCHLIST_IS_EMPTY_ERROR).isPresent(),
                WATCHLIST_DEEP_LINK_ERROR);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75123"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureWatchlistUnauthenticatedUser() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        launchDeeplink(R.TESTDATA.get("disney_prod_watchlist_deeplink_2"));

        handleAlert();
        login(getAccount());
        watchlistPage.waitForPresenceOfAnElement(watchlistPage.getStaticTextByLabelContains(WATCHLIST_IS_EMPTY_ERROR));
        sa.assertTrue(watchlistPage.getStaticTextByLabelContains(WATCHLIST_IS_EMPTY_ERROR).isPresent(), WATCHLIST_DEEP_LINK_ERROR);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67527"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMovieDetailsDeepLink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_deeplink"));
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains("Cars"),
                "Cars Movie Details page did not open via deeplink.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67529"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifySeriesDetailsDeepLink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_deeplink"));
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains("Avengers Assemble"),
                "Avengers Assemble Details page did not open via deeplink.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74858"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluSeriesDetailDeepLink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_hulk_series_details_deeplink"));
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains(ONLY_MURDERS_IN_THE_BUILDING),
                "Only Murders In The Building - Hulu Series Details Page did not open via deeplink.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72029"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMissingMovieDetailsDeepLink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_missing_movie_detail_deeplink"));
        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), CONTENT_UNAVAILABLE_ERROR);
        Assert.assertTrue(homePage.getUnavailableOkButton().isPresent(), CONTENT_UNAVAILABLE_OK_ERROR);
        homePage.getUnavailableOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72030"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMissingSeriesDetailsDeepLink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_missing_series_detail_deeplink"));
        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), CONTENT_UNAVAILABLE_ERROR);
        Assert.assertTrue(homePage.getUnavailableOkButton().isPresent(), CONTENT_UNAVAILABLE_OK_ERROR);
        homePage.getUnavailableOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74590"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US}, dataProvider =
            "huluNetworkDeepLinks")
    public void verifyDeepLinkNewURLStructureHuluNetworkPage(String deepLink) {
        String network = "ABC";
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        launchDeeplink(deepLink);

        sa.assertTrue(homePage.isNetworkLogoImageVisible(network), "Network logo page are not present");

        huluPage.waitForPresenceOfAnElement(homePage.getNetworkLogoImage(network));
        // Get Network logo by deeplink access
        BufferedImage networkLogoImageSelected = getElementImage(homePage.getNetworkLogoImage(network));
        homePage.clickHomeIcon();
        homePage.tapHuluBrandTile();
        sa.assertTrue(huluPage.isStudiosAndNetworkPresent(), "Network and studios section are not present");

        huluPage.clickOnNetworkLogo(network);
        sa.assertTrue(homePage.isNetworkLogoImageVisible(network), "Network logo page are not present");

        huluPage.waitForPresenceOfAnElement(homePage.getNetworkLogoImage(network));
        // Get Network logo by app navigation
        BufferedImage networkLogoImage = getElementImage(homePage.getNetworkLogoImage(network));
        sa.assertTrue(areImagesTheSame(networkLogoImageSelected, networkLogoImage, 10),
                "The user doesn't land on the given " + network + " network page");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74856"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void testDeeplinkMovieDetailsPageContentUnavailable() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        setAppToHomeScreen(getAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_content_unavailable_entity"));
        Assert.assertTrue(homePage.getUnavailableContentError().isPresent(), CONTENT_UNAVAILABLE_ERROR);
        Assert.assertTrue(homePage.getUnavailableOkButton().isPresent(), CONTENT_UNAVAILABLE_OK_ERROR);

        homePage.getUnavailableOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74866"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void testDeeplinkJuniorModeHuluHubContentUnavailable() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount())
                .profileName(JUNIOR_PROFILE).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang())
                .avatarId(BABY_YODA).kidsModeEnabled(true).isStarOnboarded(true).build());

        setAppToHomeScreen(getAccount(), JUNIOR_PROFILE);
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_hub"));
        Assert.assertTrue(homePage.getUnavailableContentError().isPresent(), CONTENT_UNAVAILABLE_ERROR);
        Assert.assertTrue(homePage.getUnavailableOkButton().isPresent(), CONTENT_UNAVAILABLE_OK_ERROR);

        homePage.getUnavailableOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75302"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluSeriesVideoPlayerDeepLink() {
        int seasonNumber = 0;
        int episodeNumber = 0;
        String episodeTitle, episodeDeeplinkId;
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        ExploreContent seriesApiContent = getSeriesApi(
                R.TESTDATA.get("disney_prod_hulu_series_only_murders_in_the_building_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.HULU);
        try {
            episodeTitle = seriesApiContent.getSeasons()
                    .get(seasonNumber)
                    .getItems()
                    .get(episodeNumber)
                    .getVisuals()
                    .getEpisodeTitle();
            episodeDeeplinkId =
                    seriesApiContent.getSeasons().get(seasonNumber).getItems().get(episodeNumber).getId();
        } catch (Exception e) {
            throw new SkipException("Skipping test, titles or deeplinkID were not found" + e.getMessage());
        }

        if(episodeTitle == null || episodeDeeplinkId == null){
            throw new SkipException("Skipping test, failed to get titles or deeplinkID from the api");
        }

        String contentDeeplink = String.format("%s/%s",
                R.TESTDATA.get("disney_prod_content_playback_deeplink"),
                episodeDeeplinkId);

        launchDeeplink(contentDeeplink);
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.getSubTitleLabel().contains(episodeTitle),
                "Video player deeplink is not playing correct series episode");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68458"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyBehaviourWhenExtraContentEndsFromDeeplink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusUpNextIOSPageBase upNextPage = initPage(DisneyPlusUpNextIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_trailer_playback_dancing_with_the_stars_deeplink"));
        videoPlayer.waitForVideoToStart();

        videoPlayer.scrubToPlaybackPercentage(90);
        Assert.assertFalse(upNextPage.getUpNextImageView().isElementPresent(THREE_SEC_TIMEOUT),
                "Up Next view was present");

        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(),
                "User was not redirected to Details Page");

        detailsPage.getBackButton().click();
        Assert.assertTrue(homePage.isOpened(),
                "User was not redirected to Home Page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74587"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureHuluVideoPlayback() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        homePage.waitForHomePageToOpen();

        ExploreContent movieAPIContent = getMovieApi(R.TESTDATA.get("disney_prod_hulu_movie_prey_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.HULU);
        String movieTitle = movieAPIContent.getTitle();

        if(movieTitle == null){
            throw new SkipException("Skipping test, failed to get title from the api");
        }

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_movie_prey_playback_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(movieTitle),
                "Video player deeplink's title doesn't match with api title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75301"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureDisneyPlusMovieVideoPlayer() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        homePage.waitForHomePageToOpen();

        ExploreContent movieAPI = getMovieApi(DisneyEntityIds.IRONMAN.getEntityId(),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String movieTitle = movieAPI.getTitle();

        if(movieTitle == null) {
            throw new SkipException("Skipping test, title from API was not found");
        }

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_ironman_playback_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(movieTitle),
                "Video player deeplink title does not match API title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75329"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureDisneyPlusSeriesVideoPlayer() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        homePage.waitForHomePageToOpen();

        ExploreContent seriesApi = getSeriesApi(R.TESTDATA.get("disney_prod_series_me_and_mickey_entity"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String seriesTitle = seriesApi.getTitle();
        if(seriesTitle == null) {
            throw new SkipException("Skipping test, title from API was not found");
        }
        launchDeeplink(R.TESTDATA.get("disney_prod_series_me_and_mickey_1st_episode_playback_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_DID_NOT_OPEN);
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(seriesTitle),
                "Video player deeplink title does not match API title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74589"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureHuluBrandLanding() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAppToHomeScreen(getAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_brand_deeplink"));
        Assert.assertTrue(huluPage.isHuluBrandImageExpanded(), "Hulu brand page did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74855"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureSeriesContentUnavailable() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_content_unavailable_entity_id"));
        Assert.assertTrue(homePage.isUnavailableContentErrorPopUpMessageIsPresent(), CONTENT_UNAVAILABLE_ERROR);
        homePage.getOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75028"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureHuluJuniorMode() {
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        String contentUnavailableError = "content-unavailable";
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount())
                .profileName(JUNIOR_PROFILE).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang())
                .avatarId(BABY_YODA).kidsModeEnabled(true).isStarOnboarded(true).build());

        setAppToHomeScreen(getAccount());
        Assert.assertTrue(whoIsWatchingPage.isOpened(), "Who is watching screen did not open");
        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_brand_deeplink"));
        whoIsWatchingPage.clickProfile(JUNIOR_PROFILE);
        Assert.assertTrue(homePage.getStaticTextByLabelContains(contentUnavailableError).isPresent(), CONTENT_UNAVAILABLE_ERROR);
        homePage.getOkButton().click();
        Assert.assertTrue(homePage.isKidsHomePageOpen(), "Kids Home page is not open after dismissing error");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69522"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyStarBrandLandingPageNotAvailable() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAppToHomeScreen(getAccount());

        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_star_brand_deeplink"));
        Assert.assertTrue(homePage.getUnavailableContentError().isPresent(),
                "'Content not available' error modal was not present ");
        homePage.getUnavailableOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75744"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifySignUpDeeplink() {
        DisneyPlusApplePageBase commonPage = initPage(DisneyPlusApplePageBase.class);
        terminateApp(sessionBundles.get(DISNEY));
        launchDeeplink(R.TESTDATA.get("disney_prod_sign_up_deeplink"));
        Assert.assertTrue(commonPage.isWebviewOpen(), "Deeplink did not redirect to webview");
        Assert.assertTrue(commonPage.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HEADER.getText())).isPresent(),
                "Sign Up webview was not opened");
    }
}
