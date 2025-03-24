package com.disney.qa.tests.disney.apple.tvos.regression.details;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.alice.AliceUtilities;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.explore.response.Set;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.*;

import static com.disney.alice.labels.AliceLabels.DESCRIPTION;
import static com.disney.qa.api.disney.DisneyEntityIds.END_GAME;
import static com.disney.qa.common.DisneyAbstractPage.ONE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.CollectionConstant.getCollectionName;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY;
import static com.disney.qa.common.constant.IConstantHelper.DETAILS_PAGE_NOT_DISPLAYED;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.ONLY_MURDERS_IN_THE_BUILDING;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.PREY;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVDetailsScreenTests extends DisneyPlusAppleTVBaseTest {
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String HOME_PAGE_ERROR_MESSAGE = "Home page did not open";
    private static final String SEARCH_PAGE_ERROR_MESSAGE = "Search page did not open";
    private static final String DETAILS_PAGE_ERROR_MESSAGE = "Details page did not open";
    private static final String WATCHLIST_SCREEN_ERROR_MESSAGE = "Watchlist page did not open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-66642"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SMOKE, US})
    public void verifyMovieDetailsPageAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());

        getWatchlistApi().addContentToWatchlist(getUnifiedAccount().getAccountId(), getUnifiedAccount().getAccountToken(),
                getUnifiedAccount().getProfileId(),
                getWatchlistInfoBlock(DisneyEntityIds.END_GAME.getEntityId()));

        ExploreContent movieApiContent = getMovieApi(END_GAME.getEntityId(), DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String description = movieApiContent.getDescription().getBrief();
        String ratingsValue = movieApiContent.getRating();
        List<String> tabs = Stream.of("SUGGESTED", "EXTRAS", "DETAILS").collect(Collectors.toList());
        logIn(getUnifiedAccount());
        homePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        sa.assertTrue(watchListPage.isOpened(), WATCHLIST_SCREEN_ERROR_MESSAGE);

        watchListPage.clickSelect();
        sa.assertTrue(detailsPage.isOpened(), "Movies details page did not launch");
        sa.assertTrue(detailsPage.isLogoImageDisplayed(), "Logo isn't present in its expected position");
        sa.assertTrue(detailsPage.isContentSummaryView(), "Content summary view is not displayed.");
        sa.assertTrue(detailsPage.isBriefDescriptionPresent(description), "description is not present");
        sa.assertTrue(detailsPage.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(detailsPage.isPlayButtonDisplayed(), "Play button isn't present in its expected position");
        sa.assertTrue(detailsPage.isTrailerButtonDisplayed(), "Trailer button isn't present in its expected position");
        sa.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Watchlist button isn't present in its expected position");

        AliceUtilities aliceUtilities = new AliceUtilities(getDriver());
        aliceUtilities.isUltronTextPresent("HD 5.1 CC", DESCRIPTION.getText());
        aliceUtilities.isUltronTextPresent(ratingsValue, DESCRIPTION.getText());
        tabs.forEach(item -> sa.assertTrue(detailsPage.getDynamicRowButtonLabel(item, 1).isPresent(SHORT_TIMEOUT),
                "The following tab isn't present " + item));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64740"})
    @Test(groups = {TestGroup.DETAILS_PAGE, US})
    public void addAndRemoveAssetFromWatchlist() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());

        sa.assertTrue(disneyPlusAppleTVSearchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVSearchPage.typeInSearchField("endgame");
        disneyPlusAppleTVSearchPage.clickSearchResult(END_GAME.getTitle());

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVDetailsPage.clickWatchlistButton();

        disneyPlusAppleTVDetailsPage.clickMenuTimes(2, 1);
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), WATCHLIST_SCREEN_ERROR_MESSAGE);
        sa.assertTrue(disneyPlusAppleTVWatchListPage.getTypeCellLabelContains(END_GAME.getTitle()).isElementPresent(), "The following asset was not found in watchlist " + END_GAME.getTitle());

        disneyPlusAppleTVWatchListPage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVDetailsPage.clickWatchlistButton();

        sa.assertEquals(disneyPlusAppleTVDetailsPage.getWatchlistButtonText(), "Add the current title to your Watchlist");

        disneyPlusAppleTVDetailsPage.clickMenuTimes(1, 1);
        collapseGlobalNav();

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), WATCHLIST_SCREEN_ERROR_MESSAGE);

        sa.assertFalse(disneyPlusAppleTVWatchListPage.getDynamicCellByLabel(END_GAME.getTitle()).isElementPresent(), "The following asset was  found in watchlist " + END_GAME.getTitle());

        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90976"})
    @Test(groups = {TestGroup.DETAILS_PAGE, TestGroup.SMOKE, US})
    public void trailerCompletionTakesUserToDetailsPage() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage disneyPlusAppleTVVideoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();

        logIn(getUnifiedAccount());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());

        sa.assertTrue(disneyPlusAppleTVSearchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVSearchPage.typeInSearchField("endgame");
        disneyPlusAppleTVSearchPage.clickSearchResult(END_GAME.getTitle());

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), DETAILS_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVDetailsPage.getTrailerButton().click();
        sa.assertTrue(disneyPlusAppleTVVideoPlayerPage.isOpened(), "Video player page did not launch");

        disneyPlusAppleTVVideoPlayerPage.waitUntilDetailsPageIsLoadedFromTrailer(200, 20);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-112611"})
    @Test(groups = {TestGroup.SEARCH, TestGroup.HULK, US})
    public void verifyNetworkAttributionWithBundleUserAccount() {
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());
        SoftAssert sa = new SoftAssert();
        verifyServiceAttribution(ONLY_MURDERS_IN_THE_BUILDING, sa);
        terminateApp(sessionBundles.get(DISNEY));
        launchApp(sessionBundles.get(DISNEY));
        verifyServiceAttribution(PREY, sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102798"})
    @Test(groups = {TestGroup.VIDEO_PLAYER, TestGroup.HULK, US})
    public void verifyLiveEventAppearance() {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY)));
        logIn(getUnifiedAccount());

        homePage.waitForHomePageToOpen();
        homePage.moveDownFromHeroTileToBrandTile();

        // Navigate to a live event
        Set espnLiveEvent =
               getExploreAPISet(getCollectionName(CollectionConstant.Collection.ESPN_PLUS_LIVE_AND_UPCOMING), 50);

        if (espnLiveEvent == null) {
            throw new SkipException("Skipping test, no live events are available");
        }

        LOGGER.info("items title: {}", espnLiveEvent.getItems().get(0).getVisuals().getTitle());
        navigateToShelf(detailsPage.getTypeCellLabelContains(espnLiveEvent.getItems().get(0).getVisuals().getTitle()));
        homePage.moveDown(1, 1);
        detailsPage.getTypeCellLabelContains(espnLiveEvent.getItems().get(0).getVisuals().getTitle()).click();
        detailsPage.getDetailsButton().click();
        // Validate logo and play button
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_PAGE_NOT_DISPLAYED);
        // Validate other UI elements
        Assert.assertTrue(detailsPage.getMetaDataLabel().isPresent(), "Metadata text is not present");
        Assert.assertTrue(detailsPage.getAiringBadgeLabel().isPresent(), "Badge label is not present");
        Assert.assertTrue(detailsPage.getExtrasTabTitle().isPresent(), "Title is not present");
        Assert.assertTrue(detailsPage.isContentDescriptionDisplayed(), "Description is not present");
        Assert.assertTrue(detailsPage.getWatchlistButton().isPresent(), "Watchlist button is not present");
        Assert.assertTrue(detailsPage.getBackgroundImage().isPresent(), "Background image is not present");
    }


    public void navigateToShelf(ExtendedWebElement element) {
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        int count = 10;
        while (count > 0) {
            detailsPage.moveDown(1, 1);
            if (element.isPresent(ONE_SEC_TIMEOUT)) {
                count = 0;
            } else {
                count--;
            }
        }
    }

    private void verifyServiceAttribution(String content, SoftAssert sa) {
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());

        Assert.assertTrue(home.isOpened(), HOME_PAGE_ERROR_MESSAGE);
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);
        searchPage.clearSearchBar();
        searchPage.typeInSearchField(content);
        searchPage.clickSearchResult(content);
        sa.assertTrue(detailsPage.getServiceAttribution().isPresent(),
                "Service attribution was not found on Hulu series detail page");
    }
}
