package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWatchlistIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusHulkWatchlistTest extends DisneyBaseTest {

    @DataProvider(name = "huluWatchlistDeepLinks")
    public Object[][] huluWatchlistDeepLinks() {
        return new Object[][]{{R.TESTDATA.get("disney_prod_watchlist_deeplink_2")},
                {R.TESTDATA.get("disney_prod_watchlist_deeplink_language")},
                {R.TESTDATA.get("disney_prod_watchlist_deeplink_legacy")}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74588"})
    @Test(description = "New URL Structure - Hulu Hub - Watchlist", groups = {"Hulk", TestGroup.PRE_CONFIGURATION}, dataProvider = "huluWatchlistDeepLinks")
    public void verifyHulkDeepLinkNewURLStructureWatchlistAuthenticatedUser(String deepLink) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());

        launchDeeplink(true, deepLink, 10);
        homePage.clickOpenButton();

        sa.assertTrue(watchlistPage.getStaticTextByLabelContains("Your watchlist is empty").isPresent(), "Watchlist page did not open via deeplink.");

        terminateApp(BuildType.ENTERPRISE.getDisneyBundle());
        launchDeeplink(true, deepLink, 10);
        homePage.clickOpenButton();
        homePage.dismissAppTrackingPopUp(10);

        sa.assertTrue(watchlistPage.getStaticTextByLabelContains("Your watchlist is empty").isPresent(), "Watchlist page did not open via deeplink.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75123"})
    @Test(description = "New URL Structure - Hulu Hub - Watchlist - Log Out", groups = {"Hulk", TestGroup.PRE_CONFIGURATION})
    public void verifyHulkDeepLinkNewURLStructureWatchlistUnauthenticatedUser() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        launchDeeplink(true, R.TESTDATA.get("disney_prod_watchlist_deeplink_2"), 10);
        homePage.clickOpenButton();

        handleAlert();
        login(getAccount());
        pause(5);
        sa.assertTrue(watchlistPage.getStaticTextByLabelContains("Your watchlist is empty").isPresent(), "Watchlist page did not open via deeplink.");

        sa.assertAll();
    }
}
