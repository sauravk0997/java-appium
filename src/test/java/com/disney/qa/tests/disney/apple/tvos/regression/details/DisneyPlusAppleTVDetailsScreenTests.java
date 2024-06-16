package com.disney.qa.tests.disney.apple.tvos.regression.details;

import com.disney.alice.AliceAssertion;
import com.disney.alice.AliceDriver;
import com.disney.alice.AliceUtilities;
import com.disney.qa.api.client.responses.content.ContentMovie;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.alice.labels.AliceLabels.DESCRIPTION;
import static com.disney.qa.api.disney.DisneyContentIds.END_GAME;
import static com.disney.qa.api.disney.DisneyContentIds.END_GAME_QA;
import static com.disney.qa.api.search.assets.DisneyMovies.AVENGERS_ENDGAME_PROD;
import static com.disney.qa.api.search.assets.DisneyMovies.AVENGERS_ENDGAME_QA;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST;

public class DisneyPlusAppleTVDetailsScreenTests extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90964", "XCDQA-107758", "XCDQA-90972", "XCDQA-90974"})
    @Test(description = "Verify movie details screen appearance", groups = {"Details", "Smoke"}, enabled = false)
    public void movieDetailsPageAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        getSearchApi().addMovieToWatchlist(entitledUser, IS_PROD ? END_GAME.getContentId() : END_GAME_QA.getContentId());
        ContentMovie contentMovie = getSearchApi().getMovie(IS_PROD ? AVENGERS_ENDGAME_PROD.getEncodedFamilyId() : AVENGERS_ENDGAME_QA.getEncodedFamilyId(), getCountry(), getLanguage());
        String briefDescription = contentMovie.getBriefDescription();
        String ratingsValue = contentMovie.getContentRatingsValue();
        List<String> tabs = Stream.of("SUGGESTED", "EXTRAS", "DETAILS").collect(Collectors.toList());
        if (!IS_PROD) {
            tabs.remove("EXTRAS");
        }

        logInTemp(entitledUser);
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page did not launch");

        disneyPlusAppleTVWatchListPage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), "Movies details page did not launch");
        sa.assertTrue(disneyPlusAppleTVDetailsPage.isLogoImageDisplayed(), "Logo isn't present in its expected position");
        sa.assertTrue(disneyPlusAppleTVDetailsPage.isContentSummaryView(), "Content summary view is not displayed.");
        sa.assertTrue(disneyPlusAppleTVDetailsPage.isBriefDescriptionPresent(briefDescription), "Brief description is not present");
        sa.assertTrue(disneyPlusAppleTVDetailsPage.isMetaDataLabelDisplayed(), "Metadata label is not displayed.");
        sa.assertTrue(disneyPlusAppleTVDetailsPage.isPlayButtonDisplayed(), "Play button isn't present in its expected position");
        sa.assertTrue(disneyPlusAppleTVDetailsPage.isTrailerButtonDisplayed(), "Trailer button isn't present in its expected position");
        sa.assertTrue(disneyPlusAppleTVDetailsPage.isWatchlistButtonDisplayed(), "Watchlist button isn't present in its expected position");

        new AliceUtilities(getDriver()).isUltronTextPresent("HD 5.1 CC", DESCRIPTION.getText());
        AliceAssertion aliceAssertion = aliceDriver.screenshotAndRecognize();
        aliceAssertion.assertLabelContainsCaption(sa, ratingsValue, DESCRIPTION.getText());
        tabs.forEach(item -> sa.assertTrue(disneyPlusAppleTVDetailsPage.getDynamicRowButtonLabel(item, 1).isElementPresent(),
                "The following tab isn't present " + item));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90968", "XCDQA-90970"})
    @Test(description = "Verify that add/remove button changes and the asset is properly added to watchlist", groups = {"Details"})
    public void addAndRemoveAssetFromWatchlist() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        SoftAssert sa = new SoftAssert();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        logInTemp(getAccount());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());

        sa.assertTrue(disneyPlusAppleTVSearchPage.isOpened(), "Search page didn't launch");

        disneyPlusAppleTVSearchPage.typeInSearchField("endgame");
        disneyPlusAppleTVSearchPage.clickSearchResult(END_GAME.getTitle());

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), "Details page did not launch");

        disneyPlusAppleTVDetailsPage.clickWatchlistButton();

        disneyPlusAppleTVDetailsPage.clickMenuTimes(2, 1);
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page did not launch");
        sa.assertTrue(disneyPlusAppleTVWatchListPage.getTypeCellLabelContains(END_GAME.getTitle()).isElementPresent(), "The following asset was not found in watchlist " + END_GAME.getTitle());

        disneyPlusAppleTVWatchListPage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), "Details page did not launch");

        disneyPlusAppleTVDetailsPage.clickWatchlistButton();

        sa.assertEquals(disneyPlusAppleTVDetailsPage.getWatchlistButtonText(), "Add the current title to your Watchlist");

        disneyPlusAppleTVDetailsPage.clickMenuTimes(1, 1);
        collapseGlobalNav();

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page did not launch");

        sa.assertFalse(disneyPlusAppleTVWatchListPage.getDynamicCellByLabel(END_GAME.getTitle()).isElementPresent(), "The following asset was  found in watchlist " + END_GAME.getTitle());

        sa.assertAll();

    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90976"})
    @Test(description = "Verify after playing trailer user is taken back to details page", groups = {"Details"})
    public void trailerCompletionTakesUserToDetailsPage() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage disneyPlusAppleTVVideoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        SoftAssert sa = new SoftAssert();
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        logInTemp(getAccount());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());

        sa.assertTrue(disneyPlusAppleTVSearchPage.isOpened(), "Search page didn't launch");

        disneyPlusAppleTVSearchPage.typeInSearchField("endgame");
        disneyPlusAppleTVSearchPage.clickSearchResult(END_GAME.getTitle());

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), "Details page did not launch");

        disneyPlusAppleTVDetailsPage.getTrailerButton().click();
        sa.assertTrue(disneyPlusAppleTVVideoPlayerPage.isOpened(), "Video player page did not launch");

        disneyPlusAppleTVVideoPlayerPage.waitUntilDetailsPageIsLoadedFromTrailer(200, 20);

        sa.assertAll();
    }
}
