package com.disney.qa.tests.disney.apple.tvos.regression.watchlist;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVDetailsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWatchListPage;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
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

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page is not open");

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isAIDElementPresentWithScreenshot(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WATCHLIST_COPY.getText())), "Empty watchlist text is not present");
        String subtext = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WATCHLIST_COPY.getText())
                + ". " +getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WATCHLIST_SUBCOPY.getText());
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isAIDElementPresentWithScreenshot(subtext), "Empty watchlist subtext is not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67728", "XCDQA-64740", "XCDQA-64991"})
    @Test(groups = {TestGroup.WATCHLIST, US})
    public void verifyWatchlistAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        List<DisneyEntityIds> titles =
                new ArrayList<>(Arrays.asList(
                        DisneyEntityIds.END_GAME,
                        DisneyEntityIds.INCREDIBLES_2,
                        DisneyEntityIds.LUCA,
                        DisneyEntityIds.WANDA_VISION));

        List<String> infoBlockList = new ArrayList<>();
        titles.forEach(title ->
            infoBlockList.add(getWatchlistInfoBlock(title.getEntityId())));

        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());

        IntStream.range(0, titles.size()).forEach(i -> getWatchlistApi().addContentToWatchlist(getAccount().getAccountId(),
                getAccount().getAccountToken(),getAccount().getProfileId(), infoBlockList.get(i)));

        logInTemp(getAccount());

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        Assert.assertTrue(watchListPage.isOpened(), WATCHLIST_NOT_OPEN);
        int watchlistItems = watchListPage.getNumberOfItemsByCell();

        sa.assertTrue(titles.size() == watchlistItems, "Number of added items did not match.");
        Assert.assertTrue(watchListPage.isOpened(), WATCHLIST_NOT_OPEN);
        IntStream.range(0, titles.size()).forEach(i -> {
            String title = titles.get(i).getTitle();
            sa.assertTrue(watchListPage.getTypeCellLabelContains(title).isElementPresent(),
                    String.format("%s not found", title));
        });
        sa.assertTrue(watchListPage.isContentShownCertainNumberPerRow(0, 3),
                "Watchlist items are not arranged 4 per row");

        getWatchlistApi().addContentToWatchlist(getAccount().getAccountId(), getAccount().getAccountToken(),
                getAccount().getProfileId(),
                getWatchlistInfoBlock(DisneyEntityIds.SOUL.getEntityId()));

        watchListPage.moveLeft(1, 1);
        homePage.navigateToOneGlobalNavMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText());
        homePage.clickSelect();
        if (new DisneyPlusApplePageBase(getDriver()).isGlobalNavExpanded()) {
            homePage.clickSelect();
        }
        Assert.assertTrue(homePage.isOpened(), "Home page is not open");

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        Assert.assertTrue(watchListPage.isOpened(), WATCHLIST_NOT_OPEN);

        String firstItem = watchListPage.getContentItems(0).get(0).split(",")[0];
        Assert.assertEquals(DisneyEntityIds.SOUL.getTitle(), firstItem,
                String.format("Newly added Soul content is not the first item in Watchlist but found: %s", firstItem));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-67728"})
    @Test(groups = {TestGroup.WATCHLIST, US})
    public void verifyWatchlistRefresh() {


    }


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64991"})
    @Test(groups = {TestGroup.WATCHLIST, US})
    public void verifyWatchlistAddRemoveSeriesContent() {
        List<DisneyEntityIds> titles =
                new ArrayList<>(Arrays.asList(
                        DisneyEntityIds.DANCING_WITH_THE_STARS,
                        DisneyEntityIds.SKELETON_CREW));

        List<String> infoBlockList = new ArrayList<>();
        titles.forEach(title ->
                infoBlockList.add(getWatchlistInfoBlock(title.getEntityId())));

        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        IntStream.range(0, titles.size()).forEach(i -> getWatchlistApi().addContentToWatchlist(getAccount().getAccountId(),
                getAccount().getAccountToken(),getAccount().getProfileId(), infoBlockList.get(i)));

        logInTemp(getAccount());
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        Assert.assertTrue(watchListPage.isOpened(), "Watchlist page is not open");

        watchListPage.getTypeCellLabelContains(titles.get(0).getTitle()).click();
        Assert.assertTrue(detailsPage.isOpened(), "Details page did not open.");

        detailsPage.clickWatchlistButton();
        Assert.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Details page watchlist button not present");
        watchListPage.clickMenuTimes(1, 2);
        Assert.assertTrue(watchListPage.getTypeCellLabelContains(DisneyEntityIds.SKELETON_CREW.getTitle()).isElementPresent(),
                "Ironman content is not present in Watchlist");
        Assert.assertFalse(watchListPage.getTypeCellLabelContains(DisneyEntityIds.DANCING_WITH_THE_STARS.getTitle()).isElementPresent(),
                "Removed Luca content is present in Watchlist");
    }

   @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64740"})
    @Test(groups = {TestGroup.WATCHLIST, US})
    public void verifyWatchlistAddRemoveMovieContent() {
        List<DisneyEntityIds> titles =
                new ArrayList<>(Arrays.asList(
                        DisneyEntityIds.LUCA,
                        DisneyEntityIds.IRONMAN));

       List<String> infoBlockList = new ArrayList<>();
       titles.forEach(title ->
               infoBlockList.add(getWatchlistInfoBlock(title.getEntityId())));

        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

       IntStream.range(0, titles.size()).forEach(i -> getWatchlistApi().addContentToWatchlist(getAccount().getAccountId(),
               getAccount().getAccountToken(),getAccount().getProfileId(), infoBlockList.get(i)));

        logInTemp(getAccount());
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        Assert.assertTrue(watchListPage.isOpened(), "Watchlist page is not open");

        watchListPage.getTypeCellLabelContains(titles.get(0).getTitle()).click();
        Assert.assertTrue(detailsPage.isOpened(), "Details page did not open.");

        detailsPage.clickWatchlistButton();
        Assert.assertTrue(detailsPage.isWatchlistButtonDisplayed(), "Details page watchlist button not present");
        watchListPage.clickMenuTimes(1, 2);
        Assert.assertTrue(watchListPage.getTypeCellLabelContains(DisneyEntityIds.IRONMAN.getTitle()).isElementPresent(),
                "Ironman content is not present in Watchlist");
        Assert.assertFalse(watchListPage.getTypeCellLabelContains(DisneyEntityIds.LUCA.getTitle()).isElementPresent(),
                "Removed Luca content is present in Watchlist");
    }
}
