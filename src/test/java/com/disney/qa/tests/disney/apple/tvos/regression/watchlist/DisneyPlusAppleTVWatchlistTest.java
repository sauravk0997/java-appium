package com.disney.qa.tests.disney.apple.tvos.regression.watchlist;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyContentIds;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWatchListPage;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DisneyPlusAppleTVWatchlistTest extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89594"})
    @Test(description = "No Watchlist Items", groups = {"Watchlist", "Smoke"})
    public void noWatchlistAppearance() {
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
    @Test(description = "Watchlist Items Present - UI check", groups = {"Watchlist"}, enabled = false)
    public void watchlistAppearance() {
        SoftAssert sa = new SoftAssert();
        List<DisneyContentIds> titles = new ArrayList<>();
        titles.add(DisneyContentIds.END_GAME);
        titles.add(DisneyContentIds.INCREDIBLES2);
        titles.add(DisneyContentIds.LUCA);
        titles.add(DisneyContentIds.WANDA_VISION);
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        IntStream.range(0, titles.size()).forEach(i -> getSearchApi().addToWatchlist(entitledUser, titles.get(i).getContentType(), titles.get(i).getContentId()));

        logInTemp(entitledUser);

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page did not launch");
        int watchlistItems = disneyPlusAppleTVWatchListPage.getNumberOfItemsByCell();

        sa.assertTrue(titles.size() == watchlistItems, "Number of added items did not match.");
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page is not open");
        IntStream.range(0, titles.size()).forEach(i -> {
            String title = titles.get(i).getTitle();
            sa.assertTrue(disneyPlusAppleTVWatchListPage.getDynamicCellByLabel(title).isElementPresent(), String.format("%s not found", title));
        });
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isContentShownCertainNumberPerRow(0, 3), "watchlist items are not arranged 4 per row");

        getSearchApi().addToWatchlist(entitledUser, DisneyContentIds.SOUL.getContentType(), DisneyContentIds.SOUL.getContentId());
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText());
        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page is not open after login");
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page is not open");
        String firstItem = disneyPlusAppleTVWatchListPage.getContentItems(0).get(0);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        sa.assertTrue(firstItem.equals(DisneyContentIds.SOUL.getTitle()), String.format("Newly added Soul content is not the first item in Watchlist but found: %s", firstItem));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89598"})
    @Test(description = "Verify Removing Content from Watchlist", groups = {"Watchlist"}, enabled = false)
    public void removeWatchlistContent() {
        SoftAssert sa = new SoftAssert();
        ListOrderedSet<DisneyContentIds> titles = new ListOrderedSet<>();
        titles.add(DisneyContentIds.LUCA);
        titles.add(DisneyContentIds.WANDA_VISION);
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());

        IntStream.range(0, titles.size()).forEach(i -> getSearchApi().addToWatchlist(entitledUser, titles.get(i).getContentType(), titles.get(i).getContentId()));

        logInTemp(entitledUser);

        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page is not open");
        disneyPlusAppleTVHomePage.getDynamicCellByLabel(titles.get(0).getTitle()).click();
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isRemoveWatchlistBtnOpen(), "remove watchlist button is not present");
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        disneyPlusAppleTVWatchListPage.clickRemoveWatchlistBtn();
        titles.remove(0);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        disneyPlusAppleTVWatchListPage.clickMenuTimes(1, 1);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        String firstItemAfterRemoval = disneyPlusAppleTVWatchListPage.getContentItems(0).get(0);
        sa.assertTrue(firstItemAfterRemoval.equals(titles.get(0).getTitle()), "Removed Luca content is present in Watchlist");
        sa.assertAll();
    }
}
