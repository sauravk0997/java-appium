package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusMoreMenuWatchlistTest extends DisneyBaseTest {

    public void onboard() {
        setAppToHomeScreen(disneyAccount.get());
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62610"})
    @Test(description = "Verify populated Watchlist display", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyPopulatedWatchlistDisplay() {
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusBrandIOSPageBase disneyPlusBrandIOSPageBase = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase disneyPlusDetailsIOSPageBase = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase =  initPage(DisneyPlusHomeIOSPageBase.class);

        onboard();
        //Adding a Pixar item to Watchlist
        disneyPlusHomeIOSPageBase.clickPixarTile();
        disneyPlusBrandIOSPageBase.isOpened();
        disneyPlusBrandIOSPageBase.clickFirstCarouselPoster();
        String firstTitle = disneyPlusDetailsIOSPageBase.getMediaTitle();
        disneyPlusDetailsIOSPageBase.addToWatchlist();
        //Adding a Disney item to Watchlist
        disneyPlusDetailsIOSPageBase.getHomeNav().click();
        disneyPlusHomeIOSPageBase.clickDisneyTile();
        disneyPlusBrandIOSPageBase.isOpened();
        disneyPlusBrandIOSPageBase.clickFirstCarouselPoster();
        String secondTitle = disneyPlusDetailsIOSPageBase.getMediaTitle();
        disneyPlusDetailsIOSPageBase.addToWatchlist();
        //Adding a Marvel item to Watchlist
        disneyPlusDetailsIOSPageBase.getHomeNav().click();
        disneyPlusHomeIOSPageBase.clickMarvelTile();
        disneyPlusBrandIOSPageBase.isOpened();
        disneyPlusBrandIOSPageBase.clickFirstCarouselPoster();
        String thirdTitle = disneyPlusDetailsIOSPageBase.getMediaTitle();
        disneyPlusDetailsIOSPageBase.addToWatchlist();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        disneyPlusMoreMenuIOSPageBase.getDynamicCellByLabel(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.areWatchlistTitlesDisplayed(firstTitle, secondTitle, thirdTitle),
                "Titles were not added to the Watchlist");

        Assert.assertTrue(disneyPlusMoreMenuIOSPageBase.areWatchlistTitlesProperlyOrdered(thirdTitle, secondTitle, firstTitle),
                "Titles were not placed in the correct order");
    }
}
