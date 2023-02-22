package com.disney.qa.tests.disney.android.mobile.deeplinks;

import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.requests.content.SetRequest;
import com.disney.qa.api.client.responses.content.*;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.api.search.sets.DisneyCollection;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.disney.util.disney.ZebrunnerXrayLabels;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.*;

public class DisneyPlusAndroidDeeplinkTest extends BaseDisneyTest {
    private static final String DISNEY_MOVIE_CARS_TITLE = "Cars";
    private static final String DISNEY_SERIES_AVENGERS_ASSEMBLE = "Avengers Assemble";
    private static final String SEARCH_URL = R.TESTDATA.get("disney_prod_search_deeplink");
    private static final String DOWNLOADS_URL = R.TESTDATA.get("disney_prod_downloads_deeplink");
    private static final String DOWNLOAD_EPISODE_PLACEHOLDERS = "${S} • ${E}";
    private static final String GENERIC_COLLECTION = "collection";
    private static final String FRANCHISE_COLLECTION = "franchise";
    private static final String EDITORIAL_COLLECTION = "editorial";
    private static final String RESTRICTED_PROFILE_NAME = "Restricted";
    private static final String OVER18 = "03311985";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67520"})
    @Test(description = "Deep Linking - Home", groups = {"Deeplinks"})
    public void testCheckDeeplinkToDiscover(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1605"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        String basicAddress = R.TESTDATA.get("disney_prod_discover_deeplink");
        String homeAddress = R.TESTDATA.get("disney_prod_home_deeplink");
        androidUtils.get().launchWithDeeplinkAddress(basicAddress);
        sa.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                String.format("Expected - App to open on Discover for deeplink address: ", basicAddress));

        closeAppForRelaunch();
        androidUtils.get().launchWithDeeplinkAddress(homeAddress);
        sa.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                String.format("Expected - App to open on Discover for deeplink address: ", homeAddress));

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67522"})
    @Test(description = "Deep Linking - Downloads", groups = {"Deeplinks"})
    public void testCheckDeeplinkToDownloads(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1606"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        androidUtils.get().launchWithDeeplinkAddress(DOWNLOADS_URL);
        Assert.assertTrue(initPage(DisneyPlusDownloadsPageBase.class).isOpened(),
                "Expected - App to land on Downloads");
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67523"})
    @Test(description = "Add to Downloads & Verify in Downloads tab", groups = {"Deeplinks"})
    public void testCheckDeeplinkAddToDownloads(){
        setHoraZebrunnerLabels(
                new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1607"));

        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        String emptyDownloadErrorMessage = "Download screen is not empty";
        SoftAssert sa = new SoftAssert();
        int swipeDownCount = 5;

        internalSetup();

        String promptText =
                languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.DOWNLOAD_SEASON_NUMBER_BTN.getText())
                        .replace("${seasonNumber}", "1");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_add_season_deeplink"));

        Assert.assertTrue(mediaPageBase.isTextElementPresent(promptText),
                "Download confirmation for series not displayed");

        mediaPageBase.clickOnGenericTextElement(promptText);
        mediaPageBase.swipeDownOnScreen(swipeDownCount);
        verifyDownload(mediaPageBase.getMediaTitle(), DownloadTypes.SERIES, sa);

        Assert.assertTrue(mediaPageBase.isDownloadScreenEmpty(), emptyDownloadErrorMessage);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_add_movie_download_deeplink"));
        verifyDownload(mediaPageBase.getMediaTitle(), DownloadTypes.MOVIE, sa);

        Assert.assertTrue(mediaPageBase.isDownloadScreenEmpty(), emptyDownloadErrorMessage);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_add_episode_deeplink"));
        Assert.assertTrue(mediaPageBase.isMediaTabLayoutDisplayed(),"Media tab layout not displayed");
        mediaPageBase.swipeDownOnScreen(swipeDownCount);
        verifyDownload(mediaPageBase.getMediaTitle(), DownloadTypes.EPISODE, sa);

        Assert.assertTrue(mediaPageBase.isDownloadScreenEmpty(), emptyDownloadErrorMessage);

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67525"})
    @Test(description = "Deep Linking - Search", groups = {"Deeplinks"})
    public void testCheckDeeplinkToSearch(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1608"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        androidUtils.get().launchWithDeeplinkAddress(SEARCH_URL);
        Assert.assertTrue(initPage(DisneyPlusSearchPageBase.class).isOpened(),
                "Expected - App to land on Search");
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67527"})
    @Test(description = "Deep Linking - Movie Detail Page", groups = {"Deeplinks"})
    public void testCheckDeeplinkToMovieDetails() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1609"));
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);
        initPage(DisneyPlusDiscoverPageBase.class).isOpened();

        mediaPageBase.verifyAvailableContentTabsDeepLink(
                mediaPageBase.availableMovieTabsFromResponse(
                        DISNEY_MOVIE_CARS_TITLE,
                        disneyAccount.get(),
                        searchApi.get()),
                R.TESTDATA.get("disney_prod_movie_detail_deeplink"),
                androidUtils.get(),
                mediaPageBase,
                languageUtils.get(),
                sa);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67529"})
    @Test(description = "Deep Linking - Series Detail Page", groups = {"Deeplinks"})
    public void testCheckDeeplinkToSeriesDetails(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1610"));
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);
        initPage(DisneyPlusDiscoverPageBase.class).isOpened();

        mediaPageBase.verifyAvailableContentTabsDeepLink(
                mediaPageBase.availableSeriesTabsFromResponse(
                        DISNEY_SERIES_AVENGERS_ASSEMBLE,
                        disneyAccount.get(),
                        searchApi.get(),
                        languageUtils.get()),
                R.TESTDATA.get("disney_prod_series_detail_deeplink"),
                androidUtils.get(),
                mediaPageBase,
                languageUtils.get(),
                sa);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67531"})
    @Test(description = "Deep Linking - Movies Video Player", groups = {"Deeplinks"})
    public void testCheckDeeplinkToVideoPlayer(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1611"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_video_player_movie_deeplink"));
        ContentMovie movie = searchApi.get().getMovie("6aM2a8mZATiu", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        String localizedTitle = movie.getVideoTitle();

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);

        Assert.assertTrue(videoPageBase.isOpened(),
                "Expected - Video Player to open on launch");

        Assert.assertEquals(videoPageBase.getActiveMediaTitle(), localizedTitle,
                "Expected - Video player to open correct movie");
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67533"})
    @Test(description = "Deep Linking - Series Episode Video Player", groups = {"Deeplinks"})
    public void testCheckDeeplinkToSeriesEpisodeVideo(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1612"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_video_player_episode_deeplink"));
        ContentSeries series = searchApi.get().getSeries("1Vl0AKTYhC6U", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        ContentSeason season = searchApi.get().getSeason("7f9d22f7-0bef-42cc-9880-caa781793b7d", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        String seriesTitle = series.getSeriesTitle();
        String episodeTitle = season.getEpisodeTitles().get(13);

        DisneyPlusVideoPageBase videoPageBase = initPage(DisneyPlusVideoPageBase.class);

        Assert.assertTrue(videoPageBase.isOpened(),
                "Expected - Video Player to open on launch");

        sa.assertTrue(videoPageBase.getActiveMediaTitle().contains(episodeTitle),
                String.format("Expected - Video player title '%s' to be displayed", episodeTitle));

        sa.assertEquals(videoPageBase.getActiveMediaSubtitle(), seriesTitle,
                String.format("Expected - Video player subtitle (series title) '%s' to be displayed", seriesTitle));

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67535"})
    @Test(description = "Deep Linking - Content Type Landing Page", groups = {"Deeplinks"})
    public void testCheckDeeplinkToContentLandingPages(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1613"));
        internalSetup();
        SoftAssert sa = new SoftAssert();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_series_landing_deeplink"));
        sa.assertEquals(initPage(DisneyPlusSearchPageBase.class).getLandingPageText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusSearchPageBase.ScreenTitles.SERIES.getText()),
                "Expected - App to open on Series landing page");

        closeAppForRelaunch();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_movies_landing_deeplink"));
        sa.assertEquals(initPage(DisneyPlusSearchPageBase.class).getLandingPageText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusSearchPageBase.ScreenTitles.MOVIES.getText()),
                "Expected - App to open on Movies landing page");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67537"})
    @Test(description = "Deep Linking - Brand Page", groups = {"Deeplinks"})
    public void testCheckDeeplinkToBrandPage(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1614"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusBrandPageBase brandPageBase = initPage(DisneyPlusBrandPageBase.class);
        ContentCollection collection = searchApi.get().getCollectionByContentClassAndSlugValue(languageUtils.get().getLocale(), false, languageUtils.get().getUserLanguage(), "home", "home");
        List<String> brands = collection.getStringValues(collection.getJsonNode(), "$..set..key");
        brands.forEach(brand -> {
            brand = brand.replace("brand-", "");
            androidUtils.get().launchWithDeeplinkAddress(String.format("%s/brand/%s", R.TESTDATA.get("disney_prod_discover_deeplink"), brand));
            Assert.assertTrue(brandPageBase.isOpened(),
                    "Expected - Brand page deeplink to launch to brand page view");

            brand = brand.replace("-", " ");

            Assert.assertTrue(brandPageBase.getBrandLogoDesc().toLowerCase().equals(brand),
                    "Expected - Brand page deeplink to launch to correct brand page");

            closeAppForRelaunch();
        });
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67539"})
    @Test(description = "Deep Linking - Originals", groups = {"Deeplinks"})
    public void testCheckDeeplinkToOriginalsPage(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1615"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_originals_deeplink"));

        sa.assertTrue(initPage(DisneyPlusSearchPageBase.class).isOriginalsHeaderPresent(),
                "Expected - App to launch to Originals page");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67541"})
    @Test(description = "Deep Linking - Editorial & Collection Pages", groups = {"Deeplinks"})
    public void testCheckDeeplinkToEditorialAndCollections(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1616"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        ContentSet exploreSetCollections = getSearchCollections();
        List<DisneyCollection> collectionsInSet = exploreSetCollections.getCollectionsInSet();
        List<String> editorials = new LinkedList<>();
        List<String> generalCollections = new LinkedList<>();

        collectionsInSet.forEach(collection -> {
            if(collection.getContentType().equals(EDITORIAL_COLLECTION)){
                editorials.add(collection.getSlug());
            } else if(collection.getContentType().equals(GENERIC_COLLECTION)) {
                generalCollections.add(collection.getSlug());
            }
        });

        DisneyPlusEditorialPageBase editorialPageBase = initPage(DisneyPlusEditorialPageBase.class);

        boolean performTest = false;
        if(!editorials.isEmpty()) {
            performTest = true;
            String editorialName = editorials.get(0);
            String firstMediaItem = getMediaForValidation(editorialName, EDITORIAL_COLLECTION);

            androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_discover_deeplink") + "/" + EDITORIAL_COLLECTION + "/" + editorialName);

            sa.assertTrue(editorialPageBase.isOpened(),
                    "Expected - App to launch into an Editorial page view");

            sa.assertTrue(editorialPageBase.isMediaItemDisplayed(firstMediaItem),
                    "Expected - Franchise page to launch with correct media.");
        }

        closeAppForRelaunch();

        if(!generalCollections.isEmpty()) {
            performTest = true;
            String collectionName = generalCollections.get(0);
            String firstMediaItem = getMediaForValidation(collectionName, GENERIC_COLLECTION);

            androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_discover_deeplink") + "/" + GENERIC_COLLECTION + "/" + collectionName);

            sa.assertTrue(editorialPageBase.isOpened(),
                    "Expected - App to launch into an Editorial page view");

            sa.assertTrue(editorialPageBase.isMediaItemDisplayed(firstMediaItem),
                    "Expected - Franchise page to launch with correct media.");
        }

        if(performTest) {
            checkAssertions(sa);
        } else {
            skipExecution("Skipping test. No Editorial or Collection content is presently being hosted to test against");
        }

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67543"})
    @Test(description = "Deep Linking - Franchise Page", groups = {"Deeplinks"})
    public void testCheckDeeplinkToFranchisePage(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1617"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        ContentSet exploreSetCollections = getSearchCollections();
        List<DisneyCollection> collectionsInSet = exploreSetCollections.getCollectionsInSet();
        List<String> franchises = new LinkedList<>();
        //Adds all found franchise type slugs to the collections list
        collectionsInSet.forEach(collection -> {
            if (collection.getContentType().equals(FRANCHISE_COLLECTION)) {
                franchises.add(collection.getSlug());
            }
        });

        if(!franchises.isEmpty()) {
            String franchiseName = franchises.get(0);
            String firstMediaItem = getMediaForValidation(franchiseName, FRANCHISE_COLLECTION);

            androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_discover_deeplink") + "/" + FRANCHISE_COLLECTION + "/" + franchiseName);
            DisneyPlusEditorialPageBase editorialPageBase = initPage(DisneyPlusEditorialPageBase.class);

            sa.assertTrue(editorialPageBase.isOpened(),
                    "Expected - App to launch into an Franchise page view");

            sa.assertTrue(editorialPageBase.isMediaItemDisplayed(firstMediaItem),
                    "Expected - Franchise page to launch with correct media.");

            checkAssertions(sa);
        } else {
            skipExecution("Skipping test. No Franchise content is presently being hosted to test against");
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67545"})
    @Test(description = "Deep Linking - Profiles Pages", groups = {"Deeplinks"})
    public void testCheckDeeplinkToProfilesPages() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1618"));

        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "Discovery screen is not displayed");

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_edit_profiles_deeplink"));
        sa.assertTrue(commonPageBase.isGenericTextPresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.WhosWatchingKeys.EDIT_PROFILES.getKey()).toUpperCase()),
                "Edit profile screen not displayed");

        closeAppForRelaunch();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_setup_profiles_deeplink"));
        sa.assertTrue(moreMenuPageBase.isOpened(),
                "More menu screen not displayed");

        closeAppForRelaunch();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_select_profiles_deeplink"));
        sa.assertTrue(commonPageBase.isGenericTextPresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.WhosWatchingKeys.WHOS_WATCHING_TITLE.getKey())),
                "Profile screen not displayed");

        closeAppForRelaunch();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_add_profiles_deeplink"));
        sa.assertTrue(moreMenuPageBase.isGenericTextPresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.AddProfileKeys.CHOOSE_AVATAR.getKey())),
                "Choose avatar screen not displayed");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67547"})
    @Test(description = "Deep Linking - Watchlist Page", groups = {"Deeplinks"})
    public void testCheckDeeplinkIntoWatchlistPage(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1619"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_watchlist_deeplink"));

        Assert.assertTrue(initPage(DisneyPlusWatchlistPageBase.class).isOpened(),
                "Expected - App to launch into the 'Watchlist' page");
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67549"})
    @Test(description = "Deep Linking - Defer to Welcome Page if logged out", groups = {"Deeplinks"})
    public void testCheckDeferredDeeplink(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1620"));
        SoftAssert sa = new SoftAssert();
        androidUtils.get().clearAppCache();
        androidUtils.get().launchWithDeeplinkAddress(SEARCH_URL);

        sa.assertTrue(initPage(DisneyPlusWelcomePageBase.class).isOpened(),
                "Expected - App to direct user to Welcome Page");

        login(disneyAccount.get(), false);

        sa.assertTrue(initPage(DisneyPlusSearchPageBase.class).isOpened(),
                "Expected - App to land on Search when login is complete");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67553"})
    @Test(description = "Add to Watchlist & Verify in Watchlist page", groups = {"Deeplinks"})
    public void testCheckDeeplinkAddToWatchlist(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1685"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_add_movie_to_watchlist_deeplink"));

        sa.assertTrue(mediaPageBase.isOpened(),
                "Expected - App to launch into a Movie media page with a watchlist URL");
        String movieTitle = mediaPageBase.getMediaTitle();
        closeAppForRelaunch();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_add_series_to_watchlist_deeplink"));
        sa.assertTrue(mediaPageBase.isOpened(),
                "Expected - App to launch into a Series media page with a watchlist URL");
        String seriesTitle = mediaPageBase.getMediaTitle();
            closeAppForRelaunch();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_watchlist_deeplink"));

        DisneyPlusWatchlistPageBase watchlistPage = initPage(DisneyPlusWatchlistPageBase.class);

        sa.assertFalse(watchlistPage.isMediaPresent(movieTitle),
                "Expected - 'Moana' to be added to Watchlist");

        sa.assertFalse(watchlistPage.isMediaPresent(seriesTitle),
                "Expected - 'Mickey Mouse (Shorts)' to be added to Watchlist");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67555"})
    @Test(description = "Deep Linking - App Settings", groups = {"Deeplinks"})
    public void testCheckDeeplinkToAppSettings(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1621"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_settings_deeplink"));
        sa.assertTrue(commonPageBase.isTextElementPresent("App Settings") && commonPageBase.isGenericBackButtonPresent(),
                "Expected - App to open to 'App Settings' sub menu");
        closeAppForRelaunch();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_cellular_data_deeplink"));
        sa.assertTrue(commonPageBase.isTextElementPresent("Cellular Data Usage") && commonPageBase.isGenericBackButtonPresent(),
                "Expected - App to open to 'Cellular Data Usage' sub menu");
        closeAppForRelaunch();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_download_quality_deeplink"));
        sa.assertTrue(initPage(DisneyPlusMoreMenuPageBase.class).areDownloadQualityOptionsPresent(
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.DownloadQualityItems.HIGH.getKey()),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.DownloadQualityItems.MEDIUM.getKey()),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.DownloadQualityItems.STANDARD.getKey()))
                        && commonPageBase.isGenericBackButtonPresent(),
                "Expected - App to open to 'Download Quality' sub-menu and all options are available");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67557"})
    @Test(description = "Deep Linking - Account", groups = {"Deeplinks"})
    public void testCheckDeeplinkToAccount(){
        internalSetup();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_account_deeplink"));

        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        Assert.assertTrue(commonPageBase.isTextElementPresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusMoreMenuPageBase.MenuList.ACCOUNT.getText()))
                        && commonPageBase.isGenericBackButtonPresent(),
                "Expected - App to open to Account sub menu");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67559"})
    @Test(description = "Deep Linking - Account/Change Email", groups = {"Deeplinks"})
    public void testCheckDeeplinkToChangeEmail(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1622"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_change_email_deeplink"));
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);

        Assert.assertTrue(commonPageBase.getForgotPwdHeader().isElementPresent(),
                "App did not successfully open 'One Time Password' input page");

        Assert.assertEquals(commonPageBase.getForgotPwdInputTotal(), 6,
                "'One Time Password' input did not have the expected 6 digits");
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67561"})
    @Test(description = "Deep Linking - Account/Change Password", groups = {"Deeplinks"})
    public void testCheckDeeplinkToChangePassword(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1623"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_change_password_deeplink"));
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);

        Assert.assertTrue(commonPageBase.getForgotPwdHeader().isElementPresent(),
                "App did not successfully open 'One Time Password' input page");

        Assert.assertEquals(commonPageBase.getForgotPwdInputTotal(), 6,
                "'One Time Password' input did not have the expected 6 digits");
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67562"})
    @Test(description = "Deep Linking - Legal Page and Legal Documents", groups = {"Deeplinks"})
    public void testCheckDeeplinkToLegalPages() throws Exception {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1624"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        boolean legalTabisOpened;
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_legal_deeplink"));
        sa.assertTrue(commonPageBase.isTextElementPresent("Legal") && commonPageBase.isGenericBackButtonPresent(),
                "Expected - App to open to parent Legal sub-menu");

        Map<String, String> legalItems = languageUtils.get().getLegalDocuments();
        legalItems.keySet().forEach(legalItem -> sa.assertTrue(commonPageBase.isTextElementPresent(legalItem),
                String.format("Legal item '%s' was not present.", legalItem)));

        closeAppForRelaunch();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_privacy_deeplink"));
        legalTabisOpened = moreMenuPageBase.isLegalContentViewVisible();
        sa.assertTrue(legalTabisOpened,
                "Expected - A Legal subsection to be expanded after launching with '/privacy-policy'");

        if(legalTabisOpened) {
            sa.assertTrue(commonPageBase.isTextSnippetPresent("SCOPE OF THIS POLICY"),
                    "Expected - Expanded legal subsection to be 'Privacy Policy'");
        }
        closeAppForRelaunch();

        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_terms_deeplink"));
        legalTabisOpened = moreMenuPageBase.isLegalContentViewVisible();
        sa.assertTrue(legalTabisOpened,
                "Expected - A Legal subsection to be expanded after launching with '/terms-of-use'");

        if(legalTabisOpened) {
            sa.assertTrue(commonPageBase.isTextSnippetPresent("Disney+ and ESPN+ Subscriber Agreement"),
                    "Expected - Expanded legal subsection to be 'Terms of Use'");
        }

        checkAssertions(sa);
    }

    //TODO: Test is disabled until expected results are provided by Mobile QA
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67564"})
    @Test(description = "Deep Linking - Help", groups = {"Deeplinks"}, enabled = false)
    public void testCheckDeeplinkToHelp(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1625"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_help_deeplink"));

        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);

        sa.assertTrue(moreMenuPageBase.isWebviewClosePresent(),
                "Expected - Webview close button (X) to be present");

        String helpUrl = "help.disneyplus.com";

        sa.assertTrue(moreMenuPageBase.doesWebviewOpenCorrectAddress("help.disneyplus.com"),
                "Expected 'Help' to launch correct webview page");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67572"})
    @Test(description = "Deep Linking - All Deeplinks foreground Disney+ if Disney+ is Backgrounded", groups = {"Deeplinks"})
    public void testCheckDeeplinkForegroundsApp(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1626"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        activityAndPackageLaunch();

        androidUtils.get().backgroundApp();
        pause(1);

        androidUtils.get().launchWithDeeplinkAddress(SEARCH_URL);
        Assert.assertTrue(initPage(DisneyPlusSearchPageBase.class).isOpened(),
                "Expected - App to launch to 'Search' page");
        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67576"})
    @Test(description = "Deep Linking - App open vs closed", groups = {"Deeplinks"})
    public void testCheckDeeplinkOpenVsClosed(){
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1627"));
        internalSetup();
        SoftAssert sa = new SoftAssert();
        AppiumDriver driver = (AppiumDriver) getCastedDriver();
        androidUtils.get().closeAppStack(driver);
        pause(1);

        androidUtils.get().launchWithDeeplinkAddress(DOWNLOADS_URL);
        sa.assertTrue(initPage(DisneyPlusDownloadsPageBase.class).isOpened(),
                "Expected - App to open to 'Downloads' page after fresh launch");

        androidUtils.get().backgroundApp();
        pause(1);

        androidUtils.get().launchWithDeeplinkAddress(SEARCH_URL);
        sa.assertTrue(initPage(DisneyPlusSearchPageBase.class).isOpened(),
                "Expected - App to foreground on 'Search' page after foregrounding");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67570"})
    @Test(description = "Deep Linking - User taps deeplink to content blocked due to Parental Controls", groups = {"Deeplinks"})
    public void testCheckDeeplinkToRestrictedContent() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1628"));

        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(disneyAccount.get(), false);

        Assert.assertTrue(discoverPageBase.isOpened(),"Discover page did not open");

        moreMenuPageBase.navigateToPage(
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));

        moreMenuPageBase.addArielNewProfileFromMoreMenu(
                RESTRICTED_PROFILE_NAME,
                false,
                true,
                OVER18,
                loginPageBase,
                disneyAccount.get(),
                androidUtils.get());

        moreMenuPageBase.changeProfile(RESTRICTED_PROFILE_NAME);

        Assert.assertTrue(discoverPageBase.isOpened(), "Discover page did not open");

        androidUtils.get().backgroundApp();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_child_restricted_media_deeplink"));

        sa.assertTrue(discoverPageBase.isErrorDialogPresent(),
                "Error dialog not displayed");

        sa.assertEquals(discoverPageBase.getErrorDialogText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION, "error_medianotallowed_parental-control"));

        commonPageBase.clickOnGenericTextElement("OK");

        sa.assertTrue(discoverPageBase.isOpened(),
                "Discover screen not displayed");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71633"})
    @Test(description = "Deep Linking - User is taken to the Login page when accessed directly via url", groups = {"Deeplinks"})
    public void testCheckDeeplinkToLoginPage() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1629"));

        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCommonPageBase disneyPlusCommonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        closeAppForRelaunch();
        androidUtils.get().launchWithDeeplinkAddress(R.TESTDATA.get("disney_prod_login_deeplink"));

        Assert.assertTrue(loginPageBase.isOpened(),
                "Log in screen not displayed");

        sa.assertEquals(loginPageBase.getEmailTitleText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, "log_in_title"),
                "Login title is displayed incorrectly");

        sa.assertTrue(loginPageBase.isLogoDisplayed(),
                "Disney logo is not displayed");

        sa.assertTrue(disneyPlusCommonPageBase.isTextViewStringDisplayed(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_CONTINUE.getText())),
                "Continue button not displayed");

        checkAssertions(sa);
    }

    public void internalSetup(){
        login(disneyAccount.get(), false);
        initPage(DisneyPlusDiscoverPageBase.class).isOpened();
        closeAppForRelaunch();
    }

    private void verifyDownload(String title, DownloadTypes type, SoftAssert sa) {
        DisneyPlusDownloadsPageBase downloadsPageBase = initPage(DisneyPlusDownloadsPageBase.class);

        closeAppForRelaunch();
        androidUtils.get().launchWithDeeplinkAddress(DOWNLOADS_URL);

        sa.assertTrue(downloadsPageBase.isTextElementPresent(title),
                String.format("Expected - Download listing for '%s' to be added to Downloads", title));

        switch (type) {
            case SERIES:
                sa.assertTrue(downloadsPageBase.isSeasonDownloadCorrect(title,
                                StringUtils.substringAfter(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, "size_episodes_placeholder"), DOWNLOAD_EPISODE_PLACEHOLDERS)),
                        "Expected - Multiple items to be downloaded for media item '" + title + "'");
                break;
            case EPISODE:
                sa.assertTrue(downloadsPageBase.isSingleEpisodeDownloadCorrect(title,
                                StringUtils.substringAfter(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, "size_episode_placeholder"), DOWNLOAD_EPISODE_PLACEHOLDERS)),
                        "Expected - Only 1 download to be registered for media item '" + title + "'");
                break;
        }
        removeDownloads();
    }

    private enum DownloadTypes {
        MOVIE,
        EPISODE,
        SERIES;
    }

    private String getMediaForValidation (String slug, String contentClass) {
        CollectionRequest collectionRequest = CollectionRequest.builder()
                .slug(slug)
                .contentClass(contentClass)
                .account(disneyAccount.get())
                .build();
        ContentCollection collection = searchApi.get().getCollection(collectionRequest);

        String firstCarouselId = collection.getCollectionSetsInfo().get(0).getRefId();
        ContentSet firstCarouselContent = searchApi.get().getCuratedSet(firstCarouselId, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage());
        return firstCarouselContent.getStringValues(firstCarouselContent.getJsonNode(), "$..[?(@.type=='DmcSeries' || @.type=='DmcVideo')]..text..full..content").get(0);
    }

    private ContentSet getSearchCollections() {
        CollectionRequest collectionRequest = CollectionRequest.builder()
                .slug(DisneyStandardCollection.EXPLORE.getSlug())
                .contentClass(DisneyStandardCollection.EXPLORE.getContentClass())
                .account(disneyAccount.get())
                .build();

        DisneyCollectionSet set = searchApi.get().getCollection(collectionRequest).getCollectionSetByName("Explore");
        SetRequest setRequest = SetRequest.builder()
                .setId(set.getRefId())
                .refType(set.getRefType())
                .build();

        return searchApi.get().getSet(setRequest);
    }
}
