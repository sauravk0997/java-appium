package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.api.client.requests.content.DMCEpisodesRequest;
import com.disney.qa.api.client.requests.content.DMCSeriesBundleRequest;
import com.disney.qa.api.client.requests.content.DMCVideoBundleRequest;
import com.disney.qa.api.client.responses.content.*;
import com.disney.qa.api.search.assets.StarMovies;
import com.disney.qa.api.search.assets.StarSeries;
import com.disney.qa.api.search.assets.StarSeasons;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.util.disney.ZebrunnerXrayLabels;
import io.appium.java_client.android.nativekey.AndroidKey;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVDetailsTests extends DisneyPlusAndroidTVBaseTest {

    private final String ONLY_MURDERS_SERIES_ID = StarSeries.ONLY_MURDERS.getEncodedSeriesId();
    private final String ONLY_MURDERS_S1_ID = StarSeasons.ONLY_MURDERS_S1.getSeasonId();
    private final String DEATH_ON_THE_NILE_MOVIE_ID = StarMovies.DEATH_ON_THE_NILE.getEncodedFamilyId();

    private static final String SEARCH_GLOBAL_NAV = String.valueOf(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SEARCH);
    private static final String WATCHLIST_GLOBAL_NAV = String.valueOf(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST);

    @Test(description = "Movies Details page layout with content watched")
    public void moviesDetailsContWatching() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-102958"));
        SoftAssert sa = new SoftAssert();
        NavHelper navHelper = new NavHelper(this.getCastedDriver());

        DMCVideoBundleRequest movieRequest = DMCVideoBundleRequest.builder()
                .language(language)
                .maturity("1850")
                .region(country)
                .encodedFamilyId(DEATH_ON_THE_NILE_MOVIE_ID).build();

        ContentMovie movieDetailsApi = getSearchApi().getMovie(movieRequest);

        // Login and navigate to a series Details Page
        login(entitledUser.get());
        getSearchApi().addToWatchlist(entitledUser.get(), WATCHLIST_REF_TYPE_MOVIES, StarMovies.DEATH_ON_THE_NILE.getProgramId());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.DPAD_LEFT);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible.");
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_DOWN, WATCHLIST_GLOBAL_NAV);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertFalse(disneyPlusAndroidTVWatchlistPageBase.get().isEmptyWatchlistIconPresent(), WATCHLIST_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressRight(1);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);

        // Get content view progress
        disneyPlusAndroidTVDetailsPageBase.get().getPlayButton().click();
        disneyPlusAndroidTVVideoPlayerPage.get().waitForVideoToPlay();
        pause(30);
        Assert.assertTrue(disneyPlusAndroidTVVideoPlayerPage.get().isInPlayback(), VIDEO_PLAYER_PAGE_LOAD_ERROR);
        pause(10);
        navHelper.keyUntilElementVisible(() -> disneyPlusAndroidTVDetailsPageBase.get().getDetailsBackground(), AndroidKey.BACK, 10, 1);

        // Check Cont Watching data
        String expectedDescription = movieDetailsApi.getBriefDescription();
        String expectedTitle = movieDetailsApi.getVideoTitle();

        sa.assertEquals(disneyPlusAndroidTVDetailsPageBase.get().getDescriptionText(), expectedDescription,
                "Episode description should appear correctly,");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc().contains(expectedTitle),
                String.format("Episode title [%s] should contain [%s]", disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc(),
                        expectedTitle));

        ContentPersonalizedProgramBundle moviesVideoBookmarkApi  = getSearchApi().getStarVideoBookmarks(entitledUser.get(), StarMovies.DEATH_ON_THE_NILE.getEncodedFamilyId());
        List<String> bookmarkContentIds = moviesVideoBookmarkApi.getContentIdsWithBookmarks();
        Assert.assertEquals(bookmarkContentIds.size(), 1, "Bookmark was not found.");
        int bookmarkPlayhead = moviesVideoBookmarkApi.getBookmarkPlayhead(bookmarkContentIds.get(0));
        sa.assertTrue(bookmarkPlayhead < 80, "Bookmark value should be less than 80 but found: " + bookmarkPlayhead);

        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isProgressBarPresent(), "Watch progress bar should be present,");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isContinueButtonPresent(), "Continue button should be present,");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isRestartButtonPresent(), "Restart button should be present,");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isWatchlistButtonPresent(), "Watchlist button should be present,");

        sa.assertAll();
    }

    @Test(description = "Verify the layout of the Movies Details Page")
    public void moviesDetailsPageLayout() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-102956"));
        SoftAssert sa = new SoftAssert();
        NavHelper navHelper = new NavHelper(this.getCastedDriver());

        DMCVideoBundleRequest movieRequest = DMCVideoBundleRequest.builder()
                .language(language)
                .maturity("1850")
                .region(country)
                .encodedFamilyId(DEATH_ON_THE_NILE_MOVIE_ID).build();

        ContentMovie movieDetailsApi = getSearchApi().getMovie(movieRequest);

        String expectedTitle = movieDetailsApi.getVideoTitle();
        String expectedDescription = movieDetailsApi.getBriefDescription();
        String expectedRating = movieDetailsApi.getContentRatingsValue().replace("+", "");
        String expectedGenres = movieDetailsApi.getContentGenres().toString().replace("[", "").replace("]","");
        String expectedYear = movieDetailsApi.getReleaseDate();

        // Login and navigate to a series Details Page
        login(entitledUser.get());
        getSearchApi().addToWatchlist(entitledUser.get(), WATCHLIST_REF_TYPE_MOVIES, StarMovies.DEATH_ON_THE_NILE.getProgramId());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(), GLOBAL_NAV_LOAD_ERROR);
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_DOWN, WATCHLIST_GLOBAL_NAV);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertFalse(disneyPlusAndroidTVWatchlistPageBase.get().isEmptyWatchlistIconPresent(), WATCHLIST_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);

        // Check content info comes back correctly on the Details Page
        String actualMetadataText = disneyPlusAndroidTVDetailsPageBase.get().getMetadataText();

        sa.assertEquals(disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc(), expectedTitle, "Title should appear as expected,");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getDescriptionText().contains(expectedDescription), String.format("Description [%s] should contain [%s] ",
                disneyPlusAndroidTVDetailsPageBase.get().getDescriptionText(), expectedDescription));
        sa.assertTrue(actualMetadataText.contains(expectedYear), String.format("Metadata set ([%s]) should contain year [%s]", actualMetadataText, expectedYear));
        sa.assertTrue(actualMetadataText.contains(expectedGenres), String.format("Metadata set ([%s]) should contain genres [%s]", actualMetadataText, expectedGenres));
        sa.assertTrue(actualMetadataText.contains(expectedRating), String.format("Metadata set ([%s]) should contain rating [%s]", actualMetadataText, expectedRating));

        sa.assertAll();
    }
    @Test(description = "Series Details page add content to Watchlist")
    public void seriesDetailsPageAddToWatchlist() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-100936"));
        SoftAssert sa = new SoftAssert();
        NavHelper navHelper = new NavHelper(this.getCastedDriver());

        // Login and navigate to a series Details Page
        login(entitledUser.get());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_UP, SEARCH_GLOBAL_NAV);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        disneyPlusAndroidTVSearchPage.get().typeInSearchBox("only");
        disneyPlusAndroidTVSearchPage.get().selectFirstSearchedItem();
        Assert.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);

        // Check content is not added to the Watchlist
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getWatchlistContentDesc().contains("add"),
                "Content should not be added to the Watchlist,");

        // Add content and check that the Watchlist button updates correctly
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDetailsPageBase.get().getWatchlistButton(), AndroidKey.DPAD_RIGHT);
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDetailsPageBase.get().getWatchlistButton(), AndroidKey.DPAD_CENTER, "remove");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getWatchlistContentDesc().contains("remove"),
                "Content should be added to the Watchlist,");

        sa.assertAll();
    }

    @Test(description = "Series Details page layout with content watched")
    public void seriesDetailsPageContentWatched() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-102959"));
        SoftAssert sa = new SoftAssert();
        NavHelper navHelper = new NavHelper(this.getCastedDriver());

        DMCSeriesBundleRequest seriesRequest = DMCSeriesBundleRequest.builder()
                .language(language)
                .maturity("1850")
                .region(country)
                .encodedSeriesId(ONLY_MURDERS_SERIES_ID).build();
        DMCEpisodesRequest episodesRequest = DMCEpisodesRequest.builder()
                .language(language)
                .maturity("1850")
                .region(country)
                .seasonId(ONLY_MURDERS_S1_ID).build();
        ContentSeries seriesDetailsApi = getSearchApi().getSeries(seriesRequest);
        ContentSeason seriesEpisodesTabApi = getSearchApi().getSeason(episodesRequest);

        // Login and navigate to a series Details Page
        login(entitledUser.get());
        getSearchApi().addToWatchlist(entitledUser.get(), WATCHLIST_REF_TYPE_SERIES, StarSeries.ONLY_MURDERS.getSeriesId());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.DPAD_LEFT);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),GLOBAL_NAV_LOAD_ERROR);
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_DOWN, WATCHLIST_GLOBAL_NAV);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertFalse(disneyPlusAndroidTVWatchlistPageBase.get().isEmptyWatchlistIconPresent(), WATCHLIST_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().pressRight(1);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);

        // Get content view progress
        disneyPlusAndroidTVDetailsPageBase.get().getPlayButton().click();
        disneyPlusAndroidTVVideoPlayerPage.get().waitForVideoToPlay();
        pause(30);
        Assert.assertTrue(disneyPlusAndroidTVVideoPlayerPage.get().isInPlayback(), VIDEO_PLAYER_PAGE_LOAD_ERROR);
        pause(10);
        navHelper.keyUntilElementVisible(() -> disneyPlusAndroidTVDetailsPageBase.get().getDetailsBackground(), AndroidKey.BACK, 10, 1);

        // Check Cont Watching data
        String expectedEpisodeDescription = seriesDetailsApi.getEpisodeDescription();
        String expectedEpisodeTitle = seriesEpisodesTabApi.getEpisodeTitles().get(0);

        sa.assertEquals(disneyPlusAndroidTVDetailsPageBase.get().getEpisodeContWatchingDescription(), expectedEpisodeDescription,
                "Episode description should appear as expected,");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getEpisodeContWatchingTitle().contains(expectedEpisodeTitle),
                String.format("Episode title [%s] should contain [%s]", disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc(),
                        expectedEpisodeTitle));
        ContentPersonalizedSeriesBundle seriesVideoBookmarkApi = getSearchApi().getStarSeriesBookmarks(entitledUser.get(), StarSeries.ONLY_MURDERS.getEncodedSeriesId());
        List<String> bookmarkContentIds = seriesVideoBookmarkApi.getBookmarks();
        Assert.assertEquals(bookmarkContentIds.size(), 1, "Bookmark was not found.");
        int bookmarkPlayhead = seriesVideoBookmarkApi.getBookmarkPlayhead(bookmarkContentIds.get(0));
        sa.assertTrue(bookmarkPlayhead < 80, "Bookmark value should be less than 80 but found: " + bookmarkPlayhead);

        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isProgressBarPresent(), "Watch progress bar should be present,");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isContinueButtonPresent(), "Continue button should be present,");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isRestartButtonPresent(), "Restart button should be present,");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isWatchlistButtonPresent(), "Watchlist button should be present,");

        sa.assertAll();
    }

    @Test(description = "Verify the layout of the Series Details Page")
    public void seriesDetailsPageLayout() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-102955"));
        SoftAssert sa = new SoftAssert();
        NavHelper navHelper = new NavHelper(this.getCastedDriver());

        DMCSeriesBundleRequest seriesRequest = DMCSeriesBundleRequest.builder()
                .language(language)
                .maturity("1850")
                .region(country)
                .encodedSeriesId(ONLY_MURDERS_SERIES_ID).build();
        ContentSeries seriesDetailsApi = getSearchApi().getSeries(seriesRequest);
        String expectedDescription = seriesDetailsApi.getSeriesDescription();
        String expectedRating = seriesDetailsApi.getSeriesRatingsValue().replace("+", "");
        String expectedGenres = "Mystery, Drama, Comedy, Crime"; // Replace with seriesDetailsApi.getGenres(getDisneyDictionary()); if possible
        String expectedYear = String.valueOf(seriesDetailsApi.getReleaseYear());

        // Login and navigate to a series Details Page
        login(entitledUser.get());
        getSearchApi().addToWatchlist(entitledUser.get(), WATCHLIST_REF_TYPE_SERIES, StarSeries.ONLY_MURDERS.getSeriesId());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(), GLOBAL_NAV_LOAD_ERROR);
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_DOWN, WATCHLIST_GLOBAL_NAV);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertFalse(disneyPlusAndroidTVWatchlistPageBase.get().isEmptyWatchlistIconPresent(), WATCHLIST_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);

        // Check content info comes back correctly on the Details Page
        String actualMetadataText = disneyPlusAndroidTVDetailsPageBase.get().getMetadataText();

        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc().contains(expectedDescription), String.format("Description [%s] should contain [%s] ",
                disneyPlusAndroidTVDetailsPageBase.get().getTitleImageContentDesc(), expectedDescription));
        sa.assertTrue(actualMetadataText.contains(expectedYear), String.format("Metadata set ([%s]) should contain year [%s]", actualMetadataText, expectedYear));
        sa.assertTrue(actualMetadataText.contains(expectedGenres), String.format("Metadata set ([%s]) should contain genres [%s]", actualMetadataText, expectedGenres));
        sa.assertTrue(actualMetadataText.contains(expectedRating), String.format("Metadata set ([%s]) should contain rating [%s]", actualMetadataText, expectedRating));

        sa.assertAll();
    }

    @Test(description = "Series Details page remove content from Watchlist")
    public void seriesDetailsPageRemoveFromWatchlist() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(STA, country, "XCDQA-100935"));
        SoftAssert sa = new SoftAssert();
        NavHelper navHelper = new NavHelper(this.getCastedDriver());

        // Login and navigate to a series Details Page
        login(entitledUser.get());
        getSearchApi().addToWatchlist(entitledUser.get(), WATCHLIST_REF_TYPE_SERIES, StarSeries.ONLY_MURDERS.getSeriesId());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible.");
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_DOWN, WATCHLIST_GLOBAL_NAV);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertFalse(disneyPlusAndroidTVWatchlistPageBase.get().isEmptyWatchlistIconPresent(), WATCHLIST_PAGE_LOAD_ERROR);
        disneyPlusAndroidTVCommonPage.get().selectFocusedElement();
        Assert.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().isOpened(), DETAILS_PAGE_LOAD_ERROR);

        // Check content is displayed as added to Watchlist
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getWatchlistContentDesc().contains("remove"),
                "Content should be added to the Watchlist already,");

        // Remove content and check that the Watchlist button updates correctly
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDetailsPageBase.get().getWatchlistButton(), AndroidKey.DPAD_RIGHT);
        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDetailsPageBase.get().getWatchlistButton(), AndroidKey.DPAD_CENTER, "add");
        sa.assertTrue(disneyPlusAndroidTVDetailsPageBase.get().getWatchlistContentDesc().contains("add"),
                "Content should be removed from the Watchlist,");

        sa.assertAll();
    }
}
