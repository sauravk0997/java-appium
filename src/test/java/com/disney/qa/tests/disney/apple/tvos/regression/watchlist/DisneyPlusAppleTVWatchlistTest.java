package com.disney.qa.tests.disney.apple.tvos.regression.watchlist;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVDetailsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWatchListPage;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

    public class DisneyPlusAppleTVWatchlistTest extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89594"})
    @Test(description = "No Watchlist Items", groups = {"Watchlist", "Smoke"})
    public void verifyNoWatchlistAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        logInTemp(getAccount());

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page is not open");

        sa.assertTrue(disneyPlusAppleTVWatchListPage.isAIDElementPresentWithScreenshot(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WATCHLIST_COPY.getText())), "Empty watchlist text is not present");
        String subtext = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WATCHLIST_COPY.getText())
                + ". " +getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WATCHLIST_SUBCOPY.getText());
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isAIDElementPresentWithScreenshot(subtext), "Empty watchlist subtext is not present");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89592", "XCDQA-89596"})
    @Test(description = "Watchlist Items Present - UI check", groups = {"Watchlist"})
    public void verifyWatchlistAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        List<DisneyEntityIds> titles =
                new ArrayList<>(Arrays.asList(
                        DisneyEntityIds.END_GAME,
                        DisneyEntityIds.INCREDIBLES_2,
                        DisneyEntityIds.LUCA,
                        DisneyEntityIds.WANDA_VISION));
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        IntStream.range(0, titles.size()).forEach(i -> getWatchlistApi().addContentToWatchlist(getAccount(), getAccount().getProfileId(), titles.get(i).getEntityId()));
        logInTemp(getAccount());

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        Assert.assertTrue(watchListPage.isOpened(), "Watchlist page did not launch");
        int watchlistItems = watchListPage.getNumberOfItemsByCell();

        sa.assertTrue(titles.size() == watchlistItems, "Number of added items did not match.");
        Assert.assertTrue(watchListPage.isOpened(), "Watchlist page is not open");
        IntStream.range(0, titles.size()).forEach(i -> {
            String title = titles.get(i).getTitle();
            sa.assertTrue(watchListPage.getTypeCellLabelContains(title).isElementPresent(), String.format("%s not found", title));
        });
        sa.assertTrue(watchListPage.isContentShownCertainNumberPerRow(0, 3), "watchlist items are not arranged 4 per row");

        getWatchlistApi().addContentToWatchlist(getAccount(), getAccount().getProfileId(), DisneyEntityIds.SOUL.getEntityId());
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText());
        Assert.assertTrue(homePage.isOpened(), "Home page is not open");

        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        Assert.assertTrue(watchListPage.isOpened(), "Watchlist page is not open");

        String firstItem = watchListPage.getContentItems(0).get(0).split(",")[0];
        Assert.assertEquals(DisneyEntityIds.SOUL.getTitle(), firstItem, String.format("Newly added Soul content is not the first item in Watchlist but found: %s", firstItem));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89598"})
    @Test(description = "Verify Removing Content from Watchlist", groups = {"Watchlist"})
    public void verifyRemoveWatchlistContent() {
        List<DisneyEntityIds> titles =
                new ArrayList<>(Arrays.asList(
                        DisneyEntityIds.LUCA,
                        DisneyEntityIds.IRONMAN));
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        IntStream.range(0, titles.size()).forEach(i -> getWatchlistApi().addContentToWatchlist(getAccount(), getAccount().getProfileId(), titles.get(i).getEntityId()));

        logInTemp(getAccount());
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        Assert.assertTrue(watchListPage.isOpened(), "Watchlist page is not open");

        watchListPage.getTypeCellLabelContains(titles.get(0).getTitle()).click();
        Assert.assertTrue(detailsPage.isOpened(), "Details page did not open.");

        detailsPage.clickWatchlistButton();
        watchListPage.clickMenuTimes(1, 1);
        Assert.assertTrue(watchListPage.getTypeCellLabelContains(DisneyEntityIds.IRONMAN.getTitle()).isElementPresent(),
                "Ironman content is not present in Watchlist");
        Assert.assertFalse(watchListPage.getTypeCellLabelContains(DisneyEntityIds.LUCA.getTitle()).isElementPresent(),
                "Removed Luca content is present in Watchlist");
    }
}
