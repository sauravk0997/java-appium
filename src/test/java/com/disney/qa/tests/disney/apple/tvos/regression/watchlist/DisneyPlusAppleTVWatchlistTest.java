package com.disney.qa.tests.disney.apple.tvos.regression.watchlist;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVDetailsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVSearchPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWatchListPage;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.appletv.IRemoteControllerAppleTV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusAppleTVWatchlistTest extends DisneyPlusAppleTVBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String WATCHLIST_NOT_OPEN = "Watchlist page did not open";
    private static final String DETAILS_NOT_OPEN = "Details page did not open";
    private static final String WATCHLIST_BUTTON_NOT_PRESENT = "Details page watchlist button not present";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102674"})
    @Test(groups = {TestGroup.WATCHLIST, TestGroup.SMOKE, US})
    public void verifyNoWatchlistAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        logInTemp(getAccount());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), WATCHLIST_NOT_OPEN);

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isAIDElementPresentWithScreenshot(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WATCHLIST_COPY.getText())), "Empty watchlist text is not present");
        String subtext = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WATCHLIST_COPY.getText())
                + ". " +getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WATCHLIST_SUBCOPY.getText());
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isAIDElementPresentWithScreenshot(subtext), "Empty watchlist subtext is not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67728"})
    @Test(groups = {TestGroup.WATCHLIST, US})
    public void verifyWatchlistRefresh() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        List<DisneyEntityIds> titles =
                new ArrayList<>(Arrays.asList(
                        DisneyEntityIds.AHSOKA,
                        DisneyEntityIds.INCREDIBLES_2,
                        DisneyEntityIds.SKELETON_CREW,
                        DisneyEntityIds.END_GAME));

        List<String> infoBlockList = new ArrayList<>();
        titles.forEach(title ->
                infoBlockList.add(getWatchlistInfoBlock(title.getEntityId())));
        IntStream.range(0, titles.size()).forEach(i -> getWatchlistApi().addContentToWatchlist(getAccount().getAccountId(),
                getAccount().getAccountToken(),getAccount().getProfileId(), infoBlockList.get(i)));

        logIn(getAccount());
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        int watchlistItems = watchListPage.getNumberOfItemsByCell();
        sa.assertTrue(titles.size() == watchlistItems, "Number of added items did not match.");
        sa.assertTrue(watchListPage.isContentShownCertainNumberPerRow(0, 3),
                "Watchlist items are not arranged 4 per row");

        IntStream.range(0, titles.size()).forEach(title -> {
            String name = titles.get(title).getTitle();
            sa.assertTrue(watchListPage.getTypeCellLabelContains(name).isElementPresent(SHORT_TIMEOUT),
                    String.format("%s not found", name));
            watchListPage.getTypeCellLabelContains(name).click();
            Assert.assertTrue(detailsPage.isOpened(), DETAILS_NOT_OPEN);
            detailsPage.clickWatchlistButton();
            Assert.assertTrue(detailsPage.isWatchlistButtonDisplayed(), WATCHLIST_BUTTON_NOT_PRESENT);
            watchListPage.clickMenuTimes(1, 2);
        });
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64991"})
    @Test(groups = {TestGroup.WATCHLIST, US})
    public void verifyWatchlistAddRemoveSeriesContent() {
        SoftAssert sa = new SoftAssert();
        List<DisneyEntityIds> titles =
                new ArrayList<>(Arrays.asList(
                        DisneyEntityIds.DANCING_WITH_THE_STARS,
                        DisneyEntityIds.SKELETON_CREW));

        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());

        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        logIn(getAccount());
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH.getText());
        Assert.assertTrue(searchPage.isOpened(), "Search did not open");

        IntStream.range(0, titles.size()).forEach(i -> {
            searchPage.typeInSearchField(titles.get(i).getTitle());
            searchPage.clickSearchResult(titles.get(i).getTitle());
//            Assert.assertTrue(detailsPage.isOpened(), DETAILS_NOT_OPEN);
            detailsPage.clickWatchlistButton();
            detailsPage.clickMenuTimes(2, 2);
            searchPage.moveDown(4, 1);
            searchPage.keyPressTimes(IRemoteControllerAppleTV::clickSelect, 1, 5);
        });

        detailsPage.clickMenuTimes(3, 2);
        homePage.navigateToOneGlobalNavMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        homePage.clickSelect();
        Assert.assertTrue(watchListPage.isOpened(), WATCHLIST_NOT_OPEN);
        String firstItem = watchListPage.getContentItems(0).get(0).split(",")[0];
        Assert.assertEquals(DisneyEntityIds.SKELETON_CREW.getTitle(), firstItem,
                String.format("Newly added Soul content is not the first item in Watchlist but found: %s", firstItem));
        watchListPage.getTypeCellLabelContains(titles.get(0).getTitle()).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_NOT_OPEN);
        detailsPage.clickWatchlistButton();
        Assert.assertTrue(detailsPage.isWatchlistButtonDisplayed(), WATCHLIST_BUTTON_NOT_PRESENT);
        watchListPage.clickMenuTimes(1, 2);
        Assert.assertTrue(watchListPage.getTypeCellLabelContains(DisneyEntityIds.SKELETON_CREW.getTitle()).isElementPresent(),
                "Skeleton Crew content is not present in Watchlist");
        Assert.assertFalse(watchListPage.getTypeCellLabelContains(
                DisneyEntityIds.DANCING_WITH_THE_STARS.getTitle()).isElementPresent(),
                "Removed Dancing With The Stars content is present in Watchlist");
        sa.assertAll();
    }

   @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64740"})
    @Test(groups = {TestGroup.WATCHLIST, US})
    public void verifyWatchlistAddRemoveMovieContent() {
        List<DisneyEntityIds> titles =
                new ArrayList<>(Arrays.asList(
                        DisneyEntityIds.LUCA,
                        DisneyEntityIds.IRONMAN));
       SoftAssert sa = new SoftAssert();
       DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
       DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
       DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
       DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
       DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
       setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
               getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

       logIn(getAccount());
       homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH.getText());
       Assert.assertTrue(searchPage.isOpened(), "Search did not open");

       IntStream.range(0, titles.size()).forEach(i -> {
           searchPage.typeInSearchField(titles.get(i).getTitle());
           searchPage.clickSearchResult(titles.get(i).getTitle());
            Assert.assertTrue(detailsPage.isOpened(), DETAILS_NOT_OPEN);
           detailsPage.clickWatchlistButton();
           detailsPage.clickMenuTimes(2, 2);
           searchPage.moveDown(4, 1);
           searchPage.keyPressTimes(IRemoteControllerAppleTV::clickSelect, 1, 5);
       });

       detailsPage.clickMenuTimes(3, 2);
       homePage.navigateToOneGlobalNavMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
       homePage.clickSelect();
       Assert.assertTrue(watchListPage.isOpened(), WATCHLIST_NOT_OPEN);
       String firstItem = watchListPage.getContentItems(0).get(0).split(",")[0];
       Assert.assertEquals(DisneyEntityIds.IRONMAN.getTitle(), firstItem,
               String.format("Newly added Soul content is not the first item in Watchlist but found: %s", firstItem));
       watchListPage.getTypeCellLabelContains(titles.get(0).getTitle()).click();
       Assert.assertTrue(detailsPage.isOpened(), DETAILS_NOT_OPEN);
       detailsPage.clickWatchlistButton();
       Assert.assertTrue(detailsPage.isWatchlistButtonDisplayed(), WATCHLIST_BUTTON_NOT_PRESENT);
       watchListPage.clickMenuTimes(1, 2);
       Assert.assertTrue(watchListPage.getTypeCellLabelContains(DisneyEntityIds.IRONMAN.getTitle()).isElementPresent(),
               "Ironman content is not present in Watchlist");
       Assert.assertFalse(watchListPage.getTypeCellLabelContains(
                       DisneyEntityIds.LUCA.getTitle()).isElementPresent(),
               "Removed Luca content is present in Watchlist");
       sa.assertAll();
    }
}
