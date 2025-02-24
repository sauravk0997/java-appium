package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.disney.DisneyEntityIds;
import com.disney.qa.api.pojos.UnifiedEntitlement;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase.MoreMenu;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static com.disney.qa.common.DisneyAbstractPage.TEN_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.UNIFIED_ORDER;
import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusMoreMenuWatchlistTest extends DisneyBaseTest {

    private static final String GRIMCUTTY = "Grimcutty";
    private static final String WANDA_VISION = "WandaVision";

    public void onboard() {
        setAppToHomeScreen(getAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68442"})
    @Test(groups = {TestGroup.WATCHLIST, TestGroup.PRE_CONFIGURATION, US})
    public void verifyEmptyWatchlistDisplay() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = new DisneyPlusMoreMenuIOSPageBase(getDriver());
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        onboard();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickMenuOption(MoreMenu.WATCHLIST);

        Assert.assertTrue(watchlistPage.isWatchlistScreenDisplayed(), "'Watchlist' page was not displayed");

        Assert.assertTrue(moreMenu.isWatchlistEmptyBackgroundDisplayed(),
                "Empty Watchlist text/logo was not properly displayed");
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74876"})
    @Test(groups = {TestGroup.WATCHLIST, TestGroup.HULK, TestGroup.PRE_CONFIGURATION, US})
    public void verifyExpiredHuluWatchlistDisplay() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);

        String disneyPremiumPlan = "Disney+ Premium - 159.99 USD - Yearly";
        String trioBasicPlan = "Disney Bundle Trio Premium - 26.99 USD - Monthly";

        //Create account with Disney Bundle plan
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(trioBasicPlan)));
        loginToHome(getUnifiedAccount());

        //Add disney content to watchlist
        getWatchlistApi().addContentToWatchlist(getUnifiedAccount().getAccountId(),
                getUnifiedAccount().getAccountToken(),
                getUnifiedAccount().getProfileId(),
                getWatchlistInfoBlockForUnifiedAccount(DisneyEntityIds.WANDA_VISION.getEntityId()));

        //Add hulu content to watchlist
        getWatchlistApi().addContentToWatchlist(getUnifiedAccount().getAccountId(),
                getUnifiedAccount().getAccountToken(),
                getUnifiedAccount().getProfileId(),
                getWatchlistInfoBlockForUnifiedAccount(R.TESTDATA.get("disney_prod_hulu_movie_grimcutty_entity_id")));

        // Verify content on Watchlist
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        Assert.assertTrue(moreMenu.areWatchlistTitlesDisplayed(GRIMCUTTY, WANDA_VISION),
                "Titles were not added to the Watchlist");

        // Revoke HULU subscription
        getUnifiedSubscriptionApi().revokeSubscription(getUnifiedAccount(),
                getUnifiedAccount().getAgreement(0).getAgreementId());

        //Entitle account with D+
        UnifiedEntitlement disneyEntitlements = UnifiedEntitlement.builder()
                .unifiedOffer(getUnifiedOffer(disneyPremiumPlan)).subVersion(UNIFIED_ORDER).build();
        try {
            getUnifiedSubscriptionApi().entitleAccount(getUnifiedAccount(), Arrays.asList(disneyEntitlements));
        } catch (MalformedURLException | URISyntaxException | InterruptedException e) {
            Assert.fail("Failed to entitle the account with new entitlement");
        }

        // Terminate app and relaunch
        terminateApp(sessionBundles.get(DISNEY));
        startApp(sessionBundles.get(DISNEY));

        // Verify content on watchlist after revoke HULU entitlement
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.getDynamicCellByLabel(moreMenu.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        Assert.assertFalse(moreMenu.getTypeCellLabelContains(GRIMCUTTY).isPresent(SHORT_TIMEOUT),
                "Hulu title was present in the Watchlist");
        Assert.assertTrue(moreMenu.getTypeCellLabelContains(WANDA_VISION).isPresent(),
                "Disney Plus title was not present in the Watchlist");
    }

}
