package com.disney.qa.tests.disney.apple.tvos.regression.details;

import com.disney.alice.AliceUtilities;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.pojos.explore.ExploreContent;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.alice.labels.AliceLabels.DESCRIPTION;
import static com.disney.qa.api.disney.DisneyEntityIds.END_GAME;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST;

public class DisneyPlusAppleTVDetailsScreenTests extends DisneyPlusAppleTVBaseTest {

    private static final String SEARCH_PAGE_ERROR_MESSAGE = "Search page did not open";
    private static final String DETAIL_PAGE_ERROR_MESSAGE = "Detail page did not open";
    private static final String WATCHLIST_SCREEN_ERROR_MESSAGE = "Watchlist page did not open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90964", "XCDQA-107758", "XCDQA-90972", "XCDQA-90974"})
    @Test(description = "Verify movie details screen appearance", groups = {TestGroup.DETAILS_PAGE, TestGroup.SMOKE,
            US})
    public void verifyMovieDetailsPageAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        getWatchlistApi().addContentToWatchlist(getAccount().getAccountId(), getAccount().getAccountToken(),
                getAccount().getProfileId(),
                getWatchlistInfoBlock(DisneyEntityIds.END_GAME.getEntityId()));

        ExploreContent movieApiContent = getMovieApi(END_GAME.getEntityId(), DisneyPlusBrandIOSPageBase.Brand.DISNEY);
        String description = movieApiContent.getDescription().getBrief();
        String ratingsValue = movieApiContent.getRating();
        List<String> tabs = Stream.of("SUGGESTED", "EXTRAS", "DETAILS").collect(Collectors.toList());
        logInTemp(getAccount());
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-90968", "XCDQA-90970"})
    @Test(groups = {TestGroup.DETAILS_PAGE, US})
    public void addAndRemoveAssetFromWatchlist() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        SoftAssert sa = new SoftAssert();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        logInTemp(getAccount());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());

        sa.assertTrue(disneyPlusAppleTVSearchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVSearchPage.typeInSearchField("endgame");
        disneyPlusAppleTVSearchPage.clickSearchResult(END_GAME.getTitle());

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), DETAIL_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVDetailsPage.clickWatchlistButton();

        disneyPlusAppleTVDetailsPage.clickMenuTimes(2, 1);
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(WATCHLIST.getText());

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), WATCHLIST_SCREEN_ERROR_MESSAGE);
        sa.assertTrue(disneyPlusAppleTVWatchListPage.getTypeCellLabelContains(END_GAME.getTitle()).isElementPresent(), "The following asset was not found in watchlist " + END_GAME.getTitle());

        disneyPlusAppleTVWatchListPage.clickSelect();

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), DETAIL_PAGE_ERROR_MESSAGE);

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
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        logInTemp(getAccount());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());

        sa.assertTrue(disneyPlusAppleTVSearchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVSearchPage.typeInSearchField("endgame");
        disneyPlusAppleTVSearchPage.clickSearchResult(END_GAME.getTitle());

        sa.assertTrue(disneyPlusAppleTVDetailsPage.isOpened(), DETAIL_PAGE_ERROR_MESSAGE);

        disneyPlusAppleTVDetailsPage.getTrailerButton().click();
        sa.assertTrue(disneyPlusAppleTVVideoPlayerPage.isOpened(), "Video player page did not launch");

        disneyPlusAppleTVVideoPlayerPage.waitUntilDetailsPageIsLoadedFromTrailer(200, 20);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-122531"})
    @Test(groups = {TestGroup.HULU_HUB, US})
    public void verifyHuluLicenseAttributeForStandAloneUser() {
        String contentTitle = "Shogun";
        String licenseAttributionText = "Provided by Hulu";
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVHomePage home = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayer = new DisneyPlusAppleTVVideoPlayerPage(getDriver());

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM));
        logIn(getAccount());

        Assert.assertTrue(home.isOpened(), "Home page did not open");
        home.moveDownFromHeroTileToBrandTile();
        home.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), SEARCH_PAGE_ERROR_MESSAGE);
        searchPage.typeInSearchField(contentTitle);
        searchPage.clickSearchResult(contentTitle);
        Assert.assertTrue(detailsPage.isOpened(), DETAIL_PAGE_ERROR_MESSAGE);
        Assert.assertTrue(detailsPage.getStaticTextByLabel(licenseAttributionText).isPresent(),
                licenseAttributionText + "License Attribute text is not displayed on details page");

        detailsPage.clickPlayButton();
        videoPlayer.waitForVideoToStart();
        Assert.assertTrue(videoPlayer.getTitleLabel().contains(licenseAttributionText),
                licenseAttributionText + "License Attribute text is not displayed on video player");
    }
}
