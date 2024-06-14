package com.disney.qa.tests.disney.apple.tvos.regression.watchlist;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.disney.DisneyContentIds;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.pojos.ApiConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.api.watchlist.WatchlistApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWatchListPage;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.apache.commons.collections4.set.ListOrderedSet;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DisneyPlusAppleTVWatchlistTest extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89594"})
    @Test(description = "No Watchlist Items", groups = {"Watchlist", "Smoke"}, enabled = false)
    public void noWatchlistAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyOffer offer = new DisneyOffer();
        DisneyAccount entitledUser = getAccountApi().createAccount(offer, getCountry(), getLanguage(), SUB_VERSION);
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());

        logInTemp(entitledUser);

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
    public void watchlistAppearance() {
        SoftAssert sa = new SoftAssert();
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        List<DisneyEntityIds> titles = new ArrayList<>();
        titles.add(DisneyEntityIds.END_GAME);
        titles.add(DisneyEntityIds.INCREDIBLES2);
        titles.add(DisneyEntityIds.LUCA);
        titles.add(DisneyEntityIds.WANDA_VISION);
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        ApiConfiguration configuration = ApiConfiguration.builder().partner(DISNEY).environment(PROD).platform(APPLE).build();
        WatchlistApi watchlistApi = new WatchlistApi(configuration);
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        IntStream.range(0, titles.size()).forEach(i -> watchlistApi.addContentToWatchlist(getAccount(), getAccount().getProfileId(), titles.get(i).getEntityId()));
        logInTemp(getAccount());

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

        watchlistApi.addContentToWatchlist(getAccount(), getAccount().getProfileId(), DisneyEntityIds.SOUL.getEntityId());
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.HOME.getText());
        sa.assertTrue(disneyPlusAppleTVHomePage.isOpened(), "Home page is not open after login");
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page is not open");
        String firstItem = disneyPlusAppleTVWatchListPage.getContentItems(0).get(0);
        sa.assertTrue(firstItem.equals(DisneyContentIds.SOUL.getTitle()), String.format("Newly added Soul content is not the first item in Watchlist but found: %s", firstItem));
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89598"})
    @Test(description = "Verify Removing Content from Watchlist", groups = {"Watchlist"})
    public void removeWatchlistContent() {
        SoftAssert sa = new SoftAssert();
        ListOrderedSet<DisneyEntityIds> titles = new ListOrderedSet<>();
        titles.add(DisneyEntityIds.LUCA);
        titles.add(DisneyEntityIds.IRONMAN);
        DisneyBaseTest disneyBaseTest = new DisneyBaseTest();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());
        setAccount(disneyBaseTest.createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        ApiConfiguration configuration = ApiConfiguration.builder().partner(DISNEY).environment(PROD).platform(APPLE).build();
        WatchlistApi watchlistApi = new WatchlistApi(configuration);
        IntStream.range(0, titles.size()).forEach(i -> watchlistApi.addContentToWatchlist(getAccount(), getAccount().getProfileId(), titles.get(i).getEntityId()));

        logInTemp(getAccount());
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isOpened(), "Watchlist page is not open");
        //debug
        pause(5);
        System.out.println(getDriver().getPageSource());

        homePage.getDynamicCellByLabel(titles.get(0).getTitle()).click();
        sa.assertTrue(disneyPlusAppleTVWatchListPage.isRemoveWatchlistBtnOpen(), "remove watchlist button is not present");
        disneyPlusAppleTVWatchListPage.clickRemoveWatchlistBtn();
        titles.remove(0);
        disneyPlusAppleTVWatchListPage.clickMenuTimes(1, 1);
        String firstItemAfterRemoval = disneyPlusAppleTVWatchListPage.getContentItems(0).get(0);
        sa.assertTrue(firstItemAfterRemoval.equals(titles.get(0).getTitle()), "Removed Luca content is present in Watchlist");
        sa.assertAll();
    }
}
