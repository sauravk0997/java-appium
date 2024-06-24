package com.disney.qa.tests.disney.apple.ios.regression.deeplinks;

import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWatchlistIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.ONLY_MURDERS_IN_THE_BUILDING;

public class DisneyPlusDeepLinksTest extends DisneyBaseTest {

    @DataProvider(name = "watchlistDeepLinks")
    public Object[][] watchlistDeepLinks() {
        return new Object[][]{{R.TESTDATA.get("disney_prod_watchlist_deeplink_2")},
                {R.TESTDATA.get("disney_prod_watchlist_deeplink_language")}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67520"})
    @Test(description = "Home - Deeplink", groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION})
    public void verifyHomeDeeplink() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_home_deeplink"), 10);
        homePage.clickOpenButton();
        Assert.assertTrue(homePage.isOpened(), "Home page did not open via deeplink.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67547"})
    @Test(description = "Deep Link - Legacy Watchlist URL", groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION})
    public void verifyDeepLinkWatchlist() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWatchlistIOSPageBase watchlistPage = initPage(DisneyPlusWatchlistIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        setAppToHomeScreen(getAccount());
        String legacyWatchlistDeepLink = R.TESTDATA.get("disney_prod_watchlist_deeplink_legacy");
        launchDeeplink(true, legacyWatchlistDeepLink, 10);
        homePage.clickOpenButton();
        sa.assertTrue(watchlistPage.getStaticTextByLabelContains("Your watchlist is empty").isPresent(), "Watchlist page did not open via deeplink.");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74588"})
    @Test(description = "Deep Link - New URL Structure - Watchlist - Authenticated", groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION}, dataProvider = "watchlistDeepLinks")
    public void verifyDeepLinkNewURLStructureWatchlistAuthenticatedUser(String deepLink) {
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
    @Test(description = "Deep Link - New URL Structure - Watchlist - Un-authenticated", groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION})
    public void verifyDeepLinkNewURLStructureWatchlistUnauthenticatedUser() {
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67527"})
    @Test(description = "Movies Details - Deeplink", groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION})
    public void verifyMovieDetailsDeepLink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_movie_detail_deeplink"), 10);
        detailsPage.clickOpenButton();
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains("Cars"),
                "Cars Movie Details page did not open via deeplink.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67529"})
    @Test(description = "Series Details - Deeplink", groups = {TestGroup.DEEPLINKS, TestGroup.PRE_CONFIGURATION})
    public void verifySeriesDetailsDeepLink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_series_detail_deeplink"), 10);
        detailsPage.clickOpenButton();
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains("Avengers Assemble"),
                "Avengers Assemble Details page did not open via deeplink.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74858"})
    @Test(description = "Deep Links - New URL Structure - Hulu Series Detail Page", groups = {TestGroup.DETAILS_PAGE, TestGroup.PRE_CONFIGURATION})
    public void verifyHuluSeriesDetailDeepLink() {
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_HULU_NO_ADS_ESPN_WEB, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToHomeScreen(getAccount());
        launchDeeplink(true, R.TESTDATA.get("disney_prod_hulk_series_details_deeplink"), 10);
        detailsPage.clickOpenButton();
        detailsPage.isOpened();
        Assert.assertTrue(detailsPage.getMediaTitle().contains(ONLY_MURDERS_IN_THE_BUILDING),
                "Only Murders In The Building - Hulu Series Details Page did not open via deeplink.");
    }
}
