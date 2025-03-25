package com.disney.qa.tests.disney.apple.tvos.regression.watchlist;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVDetailsPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVSearchPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWatchListPage;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.appletv.IRemoteControllerAppleTV;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.IConstantHelper.WATCHLIST_PAGE_NOT_DISPLAYED;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusAppleTVWatchlistTest extends DisneyPlusAppleTVBaseTest {
    private static final String WATCHLIST_NOT_OPEN = "Watchlist page did not open";
    private static final String DETAILS_NOT_OPEN = "Details page did not open";
    private static final String WATCHLIST_BUTTON_NOT_PRESENT = "Details page watchlist button not present";

    private List<DisneyEntityIds> seriesTitles() {
        return new ArrayList<>(Arrays.asList(
                DisneyEntityIds.DANCING_WITH_THE_STARS,
                DisneyEntityIds.SKELETON_CREW));
    }

    private List<DisneyEntityIds> movieTitles() {
        return new ArrayList<>(Arrays.asList(
                DisneyEntityIds.LUCA,
                DisneyEntityIds.IRONMAN));
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-102674"})
    @Test(groups = {TestGroup.WATCHLIST, TestGroup.SMOKE, US})
    public void verifyNoWatchlistAppearance() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWatchListPage watchlistPage = new DisneyPlusAppleTVWatchListPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());

        Assert.assertTrue(watchlistPage.isWatchlistScreenDisplayed(), WATCHLIST_PAGE_NOT_DISPLAYED);
        Assert.assertTrue(watchlistPage.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist text/logo was not properly displayed");
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
                        DisneyEntityIds.WANDA_VISION,
                        DisneyEntityIds.LOKI,
                        DisneyEntityIds.SKELETON_CREW,
                        DisneyEntityIds.INCREDIBLES_2));

        List<String> infoBlockList = new ArrayList<>();
        titles.forEach(title ->
                infoBlockList.add(getWatchlistInfoBlock(title.getEntityId())));
        IntStream.range(0, titles.size()).forEach(i -> getWatchlistApi().addContentToWatchlist(getUnifiedAccount().getAccountId(),
                getUnifiedAccount().getAccountToken(),getUnifiedAccount().getProfileId(), infoBlockList.get(i)));

        logIn(getUnifiedAccount());
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.WATCHLIST.getText());
        int watchlistItems = watchListPage.getNumberOfItemsByCell();
        sa.assertTrue(titles.size() == watchlistItems, "Number of added items did not match.");
        sa.assertTrue(watchListPage.isContentShownCertainNumberPerRow(0, 3),
                "Watchlist items are not arranged 4 per row");

        IntStream.range(0, titles.size()).forEach(title -> {
            String name = titles.get(title).getTitle();
            sa.assertTrue(watchListPage.getTypeCellLabelContains(name).isElementPresent(SHORT_TIMEOUT),
                    String.format("%s Title was not found in Watchlist", name));
            watchListPage.getTypeCellLabelContains(name).click();
            Assert.assertTrue(detailsPage.isOpened(), DETAILS_NOT_OPEN);
            detailsPage.clickWatchlistButton();
            Assert.assertTrue(detailsPage.isWatchlistButtonDisplayed(), WATCHLIST_BUTTON_NOT_PRESENT);
            watchListPage.clickMenuTimes(1, 2);
            Assert.assertFalse(watchListPage.getTypeCellLabelContains(name).isElementPresent(SHORT_TIMEOUT),
                    String.format("%s Title was not removed from Watchlist", name));
        });
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64991"})
    @Test(groups = {TestGroup.WATCHLIST, US})
    public void verifyWatchlistAddRemoveSeriesContent() {
        validateAddRemoveWatchlistContent(seriesTitles());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-64740"})
    @Test(groups = {TestGroup.WATCHLIST, US})
    public void verifyWatchlistAddRemoveMovieContent() {
        validateAddRemoveWatchlistContent(movieTitles());
    }

    private void validateAddRemoveWatchlistContent(List<DisneyEntityIds> titles) {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVWatchListPage watchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());

        logIn(getUnifiedAccount());
        homePage.openGlobalNavAndSelectOneMenu(DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH.getText());
        searchPage.waitForPresenceOfAnElement(searchPage.getSearchField());
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
        Assert.assertEquals(titles.get(1).getTitle(), firstItem,
                String.format("Newly added %s content is not the first item in Watchlist but found: %s",
                        titles.get(0).getTitle(), firstItem));
        watchListPage.getTypeCellLabelContains(titles.get(0).getTitle()).click();
        Assert.assertTrue(detailsPage.isOpened(), DETAILS_NOT_OPEN);
        detailsPage.clickWatchlistButton();
        Assert.assertTrue(detailsPage.isWatchlistButtonDisplayed(), WATCHLIST_BUTTON_NOT_PRESENT);
        watchListPage.clickMenuTimes(1, 2);
        Assert.assertTrue(watchListPage.getTypeCellLabelContains(titles.get(1).getTitle()).isElementPresent(SHORT_TIMEOUT),
                String.format("%s content is not present in Watchlist",
                        titles.get(1).getTitle()));
        Assert.assertFalse(watchListPage.getTypeCellLabelContains(
                        titles.get(0).getTitle()).isElementPresent(SHORT_TIMEOUT),
                String.format("Removed %s content is present in Watchlist",
                        titles.get(0).getTitle()));
    }
}
