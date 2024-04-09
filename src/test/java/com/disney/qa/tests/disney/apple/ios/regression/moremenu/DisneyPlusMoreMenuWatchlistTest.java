package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusMoreMenuWatchlistTest extends DisneyBaseTest {

    public void onboard() {
        setAppToHomeScreen(getAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62612"})
    @Test(description = "Verify empty Watchlist display", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyEmptyWatchlistDisplay() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        onboard();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isWatchlistHeaderDisplayed(),
                "'Watchlist' header was not properly displayed");

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist text/logo was not properly displayed");

        sa.assertAll();
    }

    @Maintainer("csolmaz")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62610"})
    @Test(description = "Verify populated Watchlist display", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyPopulatedWatchlistDisplay() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        onboard();
        //Adding a Pixar item to Watchlist
        homePage.clickPixarTile();
        brandPage.isOpened();
        brandPage.clickFirstCarouselPoster();
        String firstTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();
        //Adding a Disney item to Watchlist
        detailsPage.getHomeNav().click();
        homePage.clickDisneyTile();
        brandPage.isOpened();
        brandPage.clickFirstCarouselPoster();
        String secondTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();
        //Adding a Marvel item to Watchlist
        detailsPage.getHomeNav().click();
        homePage.clickMarvelTile();
        brandPage.isOpened();
        brandPage.clickFirstCarouselPoster();
        String thirdTitle = detailsPage.getMediaTitle();
        detailsPage.addToWatchlist();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();

        Assert.assertTrue(moreMenu.areWatchlistTitlesDisplayed(firstTitle, secondTitle, thirdTitle),
                "Titles were not added to the Watchlist");

        Assert.assertTrue(moreMenu.areWatchlistTitlesProperlyOrdered(thirdTitle, secondTitle, firstTitle),
                "Titles were not placed in the correct order");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62294"})
    @Test(description = "Verify Watchlist Deeplink", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyWatchlistDeeplink() {
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_watchlist_deeplink_2"), 10);
        watchlistPage.clickOpenButton();
        pause(2);
        Assert.assertTrue(watchlistPage.getStaticTextByLabelContains("Your watchlist is empty").isPresent(), "Watchlist page did not open via deeplink.");
    }
}
