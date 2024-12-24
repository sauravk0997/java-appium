package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.DisneyAbstractPage.TEN_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusMoreMenuWatchlistTest extends DisneyBaseTest {

    public void onboard() {
        setAppToHomeScreen(getAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68442"})
    @Test(groups = {TestGroup.WATCHLIST, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEmptyWatchlistDisplay() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        onboard();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(
                disneyPlusMoreMenuIOSPageBase.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isWatchlistHeaderDisplayed(),
                "'Watchlist' header was not properly displayed");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist text/logo was not properly displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68440"})
    @Test(groups = {TestGroup.WATCHLIST, TestGroup.PRE_CONFIGURATION, US})
    public void verifyPopulatedWatchlistDisplay() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        onboard();
        //Adding a Pixar item to Watchlist
        homePage.clickPixarTile();
        brandPage.isOpened();
        brandPage.clickFirstCarouselPoster();
        Assert.assertTrue(detailsPage.isDetailPageOpened(TEN_SEC_TIMEOUT));
        String firstTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();
        //Adding a Disney item to Watchlist
        detailsPage.getHomeNav().click();
        homePage.clickDisneyTile();
        brandPage.isOpened();
        brandPage.clickFirstCarouselPoster();
        Assert.assertTrue(detailsPage.isDetailPageOpened(TEN_SEC_TIMEOUT));
        String secondTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();
        //Adding a Marvel item to Watchlist
        detailsPage.getHomeNav().click();
        homePage.clickMarvelTile();
        brandPage.isOpened();
        brandPage.clickFirstCarouselPoster();
        Assert.assertTrue(detailsPage.isDetailPageOpened(TEN_SEC_TIMEOUT));
        String thirdTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();

        Assert.assertTrue(moreMenu.areWatchlistTitlesDisplayed(thirdTitle, secondTitle, firstTitle),
                "Titles were not added to the Watchlist");

        Assert.assertTrue(moreMenu.areWatchlistTitlesProperlyOrdered(thirdTitle, secondTitle, firstTitle),
                "Titles were not placed in the correct order");
    }

}
