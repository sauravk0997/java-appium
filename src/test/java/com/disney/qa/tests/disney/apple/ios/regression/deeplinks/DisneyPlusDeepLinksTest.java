package com.disney.qa.tests.disney.apple.ios.regression.deeplinks;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.client.requests.CreateUnifiedAccountProfileRequest;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.explore.request.ExploreSearchRequest;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.disney.qa.api.disney.DisneyEntityIds.*;
import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.*;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase.Brand;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusDeepLinksTest extends DisneyBaseTest {
    private static final String COMMON_DISNEY_PLUS_WEB_VIEW_URL_TEXT = "disneyplus.com";
    private static final String FEATURED_FILTER_LABEL = "Featured";

    private static final String CONTENT_UNAVAILABLE_ERROR = "Content Unavailable Error not displayed";
    private static final String CONTENT_UNAVAILABLE_OK_ERROR = "Content Unavailable Error OK cta is not displayed";
    private static final String HULU_PAGE_NOT_DISPLAYED = "Hulu Page is not displayed";
    private static final String NETWORK_LOGO_IMAGE_NOT_DISPLAYED = "Network Logo Image is not displayed";
    private static final String WATCHLIST_IS_EMPTY_ERROR = "You haven't added anything yet";
    private static final String WATCHLIST_DEEP_LINK_ERROR = "Watchlist Page did not open via Deep Link";
    private static final String WEBVIEW_DID_NOT_OPEN = "Deeplink did not redirect to webview";

    @DataProvider(name = "watchlistDeepLinks")
    public Object[][] watchlistDeepLinks() {
        return new Object[][]{{R.TESTDATA.get("disney_prod_watchlist_deeplink_2")},
                {R.TESTDATA.get("disney_prod_watchlist_deeplink_language")}
        };
    }

    @DataProvider(name = "huluUnavailableDeepLinks")
    public Object[][] huluUnavailableDeepLinks() {
        return new Object[][]{{R.TESTDATA.get("disney_prod_hulu_unavailable_deeplink")},
                {R.TESTDATA.get("disney_prod_hulu_unavailable_language_deeplink")}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67520"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHomeDeeplink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_home_deeplink"));
        Assert.assertTrue(homePage.isOpened(), "Home page did not open via deeplink");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67547"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkWatchlist() {
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());
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

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

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
        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));

        launchDeeplink(R.TESTDATA.get("disney_prod_watchlist_deeplink_2"));

        handleAlert();
        login(getUnifiedAccount());
        handleGenericPopup(5, 1);
        watchlistPage.waitForPresenceOfAnElement(watchlistPage.getStaticTextByLabelContains(WATCHLIST_IS_EMPTY_ERROR));
        sa.assertTrue(watchlistPage.getStaticTextByLabelContains(WATCHLIST_IS_EMPTY_ERROR).isPresent(), WATCHLIST_DEEP_LINK_ERROR);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67527"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMovieDetailsDeepLink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_deeplink"));
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains("Cars"),
                "Cars Movie Details page did not open via deeplink.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67529"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifySeriesDetailsDeepLink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_deeplink"));
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains("Avengers Assemble"),
                "Avengers Assemble Details page did not open via deeplink.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74858"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluSeriesDetailDeepLink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_series_details_deeplink"));
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains(ONLY_MURDERS_IN_THE_BUILDING),
                "Only Murders In The Building - Hulu Series Details Page did not open via deeplink.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72029"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMissingMovieDetailsDeepLink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());
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
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_missing_series_detail_deeplink"));
        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), CONTENT_UNAVAILABLE_ERROR);
        Assert.assertTrue(homePage.getUnavailableOkButton().isPresent(), CONTENT_UNAVAILABLE_OK_ERROR);
        homePage.getUnavailableOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67551"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToMissingContentCollectionn() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_missing_collection_deeplink"));
        Assert.assertTrue(homePage.getUnavailableContentError().isPresent(), CONTENT_UNAVAILABLE_ERROR);
        Assert.assertTrue(homePage.getUnavailableOkButton().isPresent(), CONTENT_UNAVAILABLE_OK_ERROR);
        homePage.getUnavailableOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74590"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureHuluNetworkPage() {
        String abcNetwork = "ABC";
        String abcNetworkDeepLink = R.TESTDATA.get("disney_prod_hulu_abc_network_language_deeplink");
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(abcNetworkDeepLink);
        Assert.assertTrue(homePage.isNetworkLogoImageVisible(abcNetwork), NETWORK_LOGO_IMAGE_NOT_DISPLAYED);

        huluPage.waitForPresenceOfAnElement(homePage.getNetworkLogoImage(abcNetwork));
        // Get Network logo by deeplink access
        BufferedImage networkLogoImageSelected = getElementImage(homePage.getNetworkLogoImage(abcNetwork));
        homePage.clickHomeIcon();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        homePage.swipeToOriginalBrandRow();
        homePage.tapHuluBrandTile();
        Assert.assertTrue(huluPage.isOpened(), HULU_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(huluPage.isStudiosAndNetworkPresent(), STUDIOS_AND_NETWORKS_NOT_DISPLAYED);

        huluPage.clickOnNetworkLogo(abcNetwork);
        Assert.assertTrue(homePage.isNetworkLogoImageVisible(abcNetwork), NETWORK_LOGO_IMAGE_NOT_DISPLAYED);

        huluPage.waitForPresenceOfAnElement(homePage.getNetworkLogoImage(abcNetwork));
        // Get Network logo by app navigation
        BufferedImage networkLogoImage = getElementImage(homePage.getNetworkLogoImage(abcNetwork));
        Assert.assertTrue(areImagesTheSame(networkLogoImageSelected, networkLogoImage, 10),
                "The user doesn't land on the given " + abcNetwork + " network page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74856"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void testDeeplinkMovieDetailsPageContentUnavailable() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

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
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(R.TESTDATA.get("disney_darth_maul_avatar_id"))
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        ExploreSearchRequest exploreSearchRequest = getHuluExploreSearchRequest()
                .setEntityId(HULU_PAGE.getEntityId())
                .setKidsMode(true)
                .setCountryCode(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage())
                .setLimit(30)
                .setUnifiedAccount(getUnifiedAccount())
                .setProfileId(getUnifiedAccount().getProfileId());

        String errorMessage = getExploreAPIResponseOrErrorMsg(exploreSearchRequest);

        handleSystemAlert(AlertButtonCommand.DISMISS, 1);
        welcomePage.clickLogInButton();
        login(getUnifiedAccount());
        handleSystemAlert(AlertButtonCommand.DISMISS, 1);
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_hub"));
        Assert.assertTrue(homePage.getStaticTextByLabelContains(errorMessage).isPresent(), CONTENT_UNAVAILABLE_ERROR);
        Assert.assertTrue(homePage.getUnavailableOkButton().isPresent(), CONTENT_UNAVAILABLE_OK_ERROR);

        homePage.getUnavailableOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61169"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyCloseButtonForDeepLinkingContentSeries() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_debug_video_player_episode_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isDetailPageOpened(TEN_SEC_TIMEOUT), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.clickCloseButton().isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68456"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyCloseButtonForDeepLinkingContentMovie() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_debug_video_player_movie_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.clickBackButton();
        Assert.assertTrue(detailsPage.isDetailPageOpened(TEN_SEC_TIMEOUT), DETAILS_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(detailsPage.clickCloseButton().isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75302"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyHuluSeriesVideoPlayerDeepLink() {
        int seasonNumber = 0;
        int episodeNumber = 0;
        String episodeTitle, episodeDeeplinkId;
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        setAppToHomeScreen(getUnifiedAccount());

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

        if (episodeTitle == null || episodeDeeplinkId == null) {
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
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_trailer_playback_dancing_with_the_stars_deeplink"));
        videoPlayer.waitForVideoToStart();

        videoPlayer.waitForTrailerToEnd(60, 5);
        Assert.assertFalse(upNextPage.getUpNextImageView().isElementPresent(THREE_SEC_TIMEOUT),
                "Up Next view was present");

        Assert.assertTrue(detailsPage.waitForDetailsPageToOpen(),
                "User was not redirected to Details Page");

        detailsPage.getBackButton().click();
        Assert.assertTrue(homePage.isOpened(),
                "User was not redirected to Home Page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72028"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyMissingVideoDeepLink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getUnifiedAccount());

        launchDeeplink(R.TESTDATA.get("disney_prod_missing_video_content_deeplink"));
        sa.assertTrue(homePage.getUnavailableContentError().isPresent(), CONTENT_UNAVAILABLE_ERROR);
        Assert.assertTrue(homePage.getUnavailableOkButton().isPresent(), CONTENT_UNAVAILABLE_OK_ERROR);
        homePage.getUnavailableOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74587"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureHuluVideoPlayback() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        ExploreContent movieAPIContent = getMovieApi(R.TESTDATA.get("disney_prod_hulu_movie_prey_entity_id"),
                DisneyPlusBrandIOSPageBase.Brand.HULU);
        String movieTitle = movieAPIContent.getTitle();

        if (movieTitle == null) {
            throw new SkipException("Skipping test, failed to get title from the api");
        }

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_movie_prey_playback_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(movieTitle),
                "Video player deeplink's title doesn't match with api title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75301"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureDisneyPlusMovieVideoPlayer() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        ExploreContent movieAPI = getMovieApi(DisneyEntityIds.IRONMAN.getEntityId(),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String movieTitle = movieAPI.getTitle();

        if (movieTitle == null) {
            throw new SkipException("Skipping test, title from API was not found");
        }

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_ironman_playback_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(movieTitle),
                "Video player deeplink title does not match API title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75329"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureDisneyPlusSeriesVideoPlayer() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        ExploreContent seriesApi = getSeriesApi(R.TESTDATA.get("disney_prod_series_me_and_mickey_entity"),
                DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String seriesTitle = seriesApi.getTitle();
        if (seriesTitle == null) {
            throw new SkipException("Skipping test, title from API was not found");
        }
        launchDeeplink(R.TESTDATA.get("disney_prod_series_me_and_mickey_1st_episode_playback_deeplink"));
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        Assert.assertTrue(videoPlayer.getTitleLabel().equals(seriesTitle),
                "Video player deeplink title does not match API title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74589"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureHuluBrandLanding() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_brand_deeplink"));
        Assert.assertTrue(huluPage.isHuluBrandImageExpanded(), "Hulu brand page did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74855"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureSeriesContentUnavailable() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_content_unavailable_entity_id"));
        Assert.assertTrue(homePage.isUnavailableContentErrorPopUpMessageIsPresent(), CONTENT_UNAVAILABLE_ERROR);
        homePage.getOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75028"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.HULU, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkNewURLStructureHuluJuniorMode() {
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        String contentUnavailableError = "content-unavailable";

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount());
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

        setAppToHomeScreen(getUnifiedAccount());

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
        Assert.assertTrue(commonPage.isWebviewOpen(), WEBVIEW_DID_NOT_OPEN);
        Assert.assertTrue(commonPage.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HEADER.getText())).isPresent(),
                "Sign Up webview was not opened");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67580"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAccountSubscriptionDeeplink() {
        DisneyPlusApplePageBase commonPage = initPage(DisneyPlusApplePageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_account_subscription"));
        Assert.assertTrue(commonPage.isWebviewOpen(), WEBVIEW_DID_NOT_OPEN);
        Assert.assertTrue(commonPage.getWebviewUrl().contains(COMMON_DISNEY_PLUS_WEB_VIEW_URL_TEXT),
                "Webview did not open common Disney+ URL: " + COMMON_DISNEY_PLUS_WEB_VIEW_URL_TEXT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67578"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAccountCancelSubscriptionDeeplink() {
        DisneyPlusApplePageBase commonPage = initPage(DisneyPlusApplePageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_account_cancel_subscription"));
        Assert.assertTrue(commonPage.isWebviewOpen(), WEBVIEW_DID_NOT_OPEN);
        Assert.assertTrue(commonPage.getWebviewUrl().contains(COMMON_DISNEY_PLUS_WEB_VIEW_URL_TEXT),
                "Webview did not open common Disney+ URL: " + COMMON_DISNEY_PLUS_WEB_VIEW_URL_TEXT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75209"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US}, dataProvider = "huluUnavailableDeepLinks")
    public void verifyHuluDeepLinkNewURLStructureNotEntitledHulu(String deepLink) {
        String abcNetworkEntityId = "entity-d8ea2b7d-d87d-4e5b-bfee-719a39e95129";

        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BASIC_MONTHLY)));

        ExploreSearchRequest exploreSearchRequest = getDisneyExploreSearchRequest()
                .setEntityId(abcNetworkEntityId)
                .setCountryCode(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage())
                .setUnifiedAccount(getUnifiedAccount())
                .setProfileId(getUnifiedAccount().getProfileId());

        String errorMessage = getExploreAPIResponseOrErrorMsg(exploreSearchRequest);
        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        launchDeeplink(deepLink);

        Assert.assertTrue(detailsPage.getStaticTextByLabel(errorMessage).isPresent(), CONTENT_UNAVAILABLE_ERROR);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-77699"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.ESPN, TestGroup.PRE_CONFIGURATION, US})
    public void verifyUnavailableContentPopUpForESPNContentJuniorProfile() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .language(getUnifiedAccount().getProfileLang())
                .avatarId(null)
                .dateOfBirth(U18_DOB)
                .build());

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .language(getUnifiedAccount().getProfileLang())
                .avatarId(null)
                .kidsModeEnabled(true)
                .dateOfBirth(KIDS_DOB)
                .build());

        loginToHome(getUnifiedAccount(), SECONDARY_PROFILE);
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_in_the_arena_serena_williams_deeplink"));
        Assert.assertTrue(detailsPage.getRatingRestrictionDetailMessage().isPresent(),
                "Parental Control Message not found");

        homePage.clickMoreTab();
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_espn_series_in_the_arena_serena_williams_deeplink"));
        Assert.assertTrue(homePage.getParentalControlMediaNotAllowedErrorPopUpMessage().isPresent(),
                "Parental Control media not allowed error message not found");
        Assert.assertTrue(homePage.getOkButton().isPresent(), "CTA button not found");
        homePage.getOkButton().click();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67541"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToCollectionPages() {
        String waltDisneyCollectionPageTitle = "Walt Disney Animation Studios";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_collection_walt_disney_animation_studios"));
        collectionPage.waitForCollectionPageToOpen(waltDisneyCollectionPageTitle);
        Assert.assertTrue(collectionPage.isOpened(waltDisneyCollectionPageTitle),
                String.format("Expected editorial/franchise collection page '%s' did not open",
                        waltDisneyCollectionPageTitle));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67543"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToFranchisePages() {
        String toyStoryCollectionPageTitle = "Toy Story";
        String theAvengersCollectionPageTitle = "Marvel's Avengers";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_toy_story_collection"));
        collectionPage.waitForCollectionPageToOpen(toyStoryCollectionPageTitle);
        Assert.assertTrue(collectionPage.isOpened(toyStoryCollectionPageTitle),
                String.format("Expected editorial/franchise collection page '%s' did not open",
                        toyStoryCollectionPageTitle));

        launchDeeplink(R.TESTDATA.get("disney_prod_the_avengers_collection"));
        collectionPage.waitForCollectionPageToOpen(escapeSingleQuotes(theAvengersCollectionPageTitle));
        Assert.assertTrue(collectionPage.isOpened(escapeSingleQuotes(theAvengersCollectionPageTitle)),
                String.format("Expected editorial/franchise collection page '%s' did not open",
                        theAvengersCollectionPageTitle));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67549"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeferredDeeplink() {
        DisneyPlusHuluIOSPageBase huluPage = initPage(DisneyPlusHuluIOSPageBase.class);

        launchDeeplink(R.TESTDATA.get("disney_prod_hulu_brand_deeplink"));
        handleAlert();
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        login(getUnifiedAccount());
        handleGenericPopup(5, 1);
        Assert.assertTrue(huluPage.isOpened(), HULU_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67539"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToOriginals() {
        String originalsPageTitle = "Originals";
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusCollectionIOSPageBase collectionPage = initPage(DisneyPlusCollectionIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_originals_deeplink"));
        Assert.assertTrue(collectionPage.isOpened(originalsPageTitle), ORIGINALS_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67572"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkDisneyForeground() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        // Background Disney app
        launchApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        // Open any deeplink should bring back Disney app to the foreground
        launchDeeplink(R.TESTDATA.get("disney_prod_search_deeplink"));
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67576"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkOpenVsClosed() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
        terminateApp(sessionBundles.get(DISNEY));

        launchDeeplink(R.TESTDATA.get("disney_prod_watchlist_deeplink_2"));
        Assert.assertTrue(welcomePage.getAppLoadingView().isPresent(),
                "Screen splash was not present");
        Assert.assertTrue(watchlistPage.isWatchlistScreenDisplayed(), WATCHLIST_PAGE_NOT_DISPLAYED);

        launchApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        launchDeeplink(R.TESTDATA.get("disney_prod_search_deeplink"));
        Assert.assertFalse(welcomePage.getAppLoadingView().isElementPresent(THREE_SEC_TIMEOUT),
                "Screen splash was present");
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67564"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToHelp() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_help_webview_deeplink"));
        Assert.assertTrue(moreMenuPage.isHelpWebviewOpen(), "Deeplink did not redirect to help webview");
    }

    // There is a bug related IOS-15924
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67562"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToLegal() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyplusLegalIOSPageBase legalPage = initPage(DisneyplusLegalIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        Set<String> legalItems = getLocalizationUtils().getLegalHeaders();
        List<String> legalHeaders = new ArrayList<>(legalItems);
        List<String> legalDeepLinks = getLegalDeepLinks();

        launchDeeplink(R.TESTDATA.get("disney_prod_legal_deeplink"));
        sa.assertTrue(legalPage.isOpened(), "Legal page did not open");

        // Validate each legal deeplink and sub header
        for (int i = 0; i < legalHeaders.size(); i++) {
            launchDeeplink(legalDeepLinks.get(i));
            sa.assertTrue(legalPage.getLegalHeader(legalHeaders.get(i)).isPresent(THREE_SEC_TIMEOUT),
                    "Legal header expected " + legalHeaders.get(i) + "is not present");
        }
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67585"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToRestartSubscription() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusApplePageBase commonPage = initPage(DisneyPlusApplePageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_restart_subscription_deeplink"));
        Assert.assertTrue(commonPage.isWebviewOpen(), WEBVIEW_DID_NOT_OPEN);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67570"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToKidsModeBlockedContent() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount(), KIDS_PROFILE);

        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_detail_loki_deeplink"));
        Assert.assertTrue(homePage.getParentalControlMediaNotAllowedErrorPopUpMessage().isPresent(),
                "Parental Control media not allowed error message not found");
        Assert.assertTrue(homePage.getDismissCTAButtonPresent().isPresent(), "OK CTA is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67531"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToMoviePlayback() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_ironman_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        Assert.assertEquals(videoPlayer.getTitleLabel(), IRONMAN.getTitle(),
                "Video Player title doesn't match expected title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67533"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToSeriesPlayback() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);

        ExploreContent seriesApiContent = getSeriesApi(LOKI.getEntityId(), DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String firstEpisodeTitle;
        try {
            firstEpisodeTitle = seriesApiContent.getSeasons().get(0).getItems().get(0)
                    .getVisuals().getEpisodeTitle();
        } catch (Exception e) {
            throw new SkipException("Unable to fetch first episode title from Explore API", e);
        }

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_series_loki_first_episode_playback_deeplink"));
        Assert.assertTrue(videoPlayer.isOpened(), VIDEO_PLAYER_NOT_DISPLAYED);
        videoPlayer.waitForVideoToStart();
        Assert.assertEquals(videoPlayer.getTitleLabel(), LOKI.getTitle(),
                "Video Player title doesn't match expected title");
        Assert.assertTrue(videoPlayer.getSubTitleLabel().contains(firstEpisodeTitle),
                "Video Player subtitle doesn't contains expected episode title");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67525"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToSearchPage() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_search_deeplink"));
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67535"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinksToMoviesAndSeriesLandingPages() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMediaCollectionIOSPageBase mediaCollectionPage = initPage(DisneyPlusMediaCollectionIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        launchDeeplink(R.TESTDATA.get("disney_prod_movies_landing_page_deeplink"));
        Assert.assertTrue(mediaCollectionPage.getMoviesHeader().isPresent(),
                "Movies landing page is not opened");
        Assert.assertEquals(mediaCollectionPage.getSelectedCategoryFilterName(), FEATURED_FILTER_LABEL,
                String.format("Movies filter was not set to '%s' by default", FEATURED_FILTER_LABEL));

        launchDeeplink(R.TESTDATA.get("disney_prod_series_landing_page_deeplink"));
        Assert.assertTrue(mediaCollectionPage.getSeriesHeader().isPresent(),
                "Series landing page is not opened");
        Assert.assertEquals(mediaCollectionPage.getSelectedCategoryFilterName(), FEATURED_FILTER_LABEL,
                String.format("Series filter was not set to '%s' by default", FEATURED_FILTER_LABEL));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67537"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyDeepLinkToBrandsForUS() {
        List<Brand> brands = Arrays.asList(
                Brand.DISNEY,
                Brand.PIXAR,
                Brand.MARVEL,
                Brand.STAR_WARS,
                Brand.NATIONAL_GEOGRAPHIC,
                Brand.HULU,
                Brand.ESPN
        );
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();

        validateBrandsDeepLinks(brands);
    }

    //Below TC is failing due to bug https://jira.disney.com/browse/IOS-15919
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67557"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, US})
    public void verifyAccountDeeplink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        setAppToHomeScreen(getUnifiedAccount());
        homePage.waitForHomePageToOpen();
        launchDeeplink(R.TESTDATA.get("disney_prod_account_deeplink"));
        accountPage.waitForAccountPageToOpen();
        Assert.assertTrue(accountPage.isOpened(), ACCOUNT_PAGE_NOT_DISPLAYED);
        accountPage.clickNavBackBtn();
        Assert.assertTrue(moreMenu.isOpened(), MORE_MENU_NOT_DISPLAYED);

        launchDeeplink(R.TESTDATA.get("disney_prod_commerce_deeplink"));
        accountPage.waitForAccountPageToOpen();
        Assert.assertTrue(accountPage.isOpened(), ACCOUNT_PAGE_NOT_DISPLAYED);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-82850"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, LATAM_ANZ})
    public void verifyDeepLinkToBrandsForLATAMAndANZ() {
        List<Brand> brands = Arrays.asList(
                Brand.DISNEY,
                Brand.PIXAR,
                Brand.MARVEL,
                Brand.STAR_WARS,
                Brand.NATIONAL_GEOGRAPHIC,
                Brand.STAR
        );
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM_MONTHLY,
                        getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();
        homePage.waitForHomePageToOpen();

        validateBrandsDeepLinks(brands);

        // Separate validation for ESPN brand, since the page doesn't have the same elements as the rests of brands
        String firstEspnCollectionId;
        try {
            firstEspnCollectionId = getDisneyAPIPage(ESPN.getEntityId()).get(0).getId();
        } catch (Exception e) {
            throw new SkipException(
                    "Skipping test, failed to get first ESPN collection ID from Explore API", e);
        }
        launchDeeplink(brandPage.getBrandDeepLink(Brand.ESPN));
        Assert.assertTrue(brandPage.getCollection(firstEspnCollectionId).isPresent(),
                String.format("Brand screen for '%s' is not displayed", Brand.ESPN));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-82851"})
    @Test(groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION, EMEA_CA})
    public void verifyDeepLinkToBrandsForEMEAAndCanada() {
        List<Brand> brands = Arrays.asList(
                Brand.DISNEY,
                Brand.PIXAR,
                Brand.MARVEL,
                Brand.STAR_WARS,
                Brand.NATIONAL_GEOGRAPHIC,
                Brand.STAR
        );
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(DISNEY_PLUS_PREMIUM,
                        getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();
        homePage.waitForHomePageToOpen();

        validateBrandsDeepLinks(brands);
    }

    public void validateBrandsDeepLinks(List<Brand> brands) {
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        for (Brand brand : brands) {
            launchDeeplink(brandPage.getBrandDeepLink(brand));
            String brandString = brandPage.getBrand(brand);
            Assert.assertTrue(brandPage.isOpened(), "Brand page is not open");
            Assert.assertTrue(brandPage.isBrandScreenDisplayed(brandString),
                    String.format("Brand screen for '%s' is not displayed", brandString));
        }
    }

    public List<String> getLegalDeepLinks() {
        List<String> legalDeepLinks = new ArrayList<>();
        legalDeepLinks.add(R.TESTDATA.get("disney_prod_terms_deeplink"));
        legalDeepLinks.add(R.TESTDATA.get("disney_prod_subscriber_agreement_deeplink"));
        legalDeepLinks.add(R.TESTDATA.get("disney_prod_privacy_deeplink"));
        legalDeepLinks.add(R.TESTDATA.get("disney_prod_us_privacy_rights_deeplink"));
        legalDeepLinks.add(R.TESTDATA.get("disney_prod_us_legal_dnsmi_deeplink"));
        return legalDeepLinks;
    }
}
