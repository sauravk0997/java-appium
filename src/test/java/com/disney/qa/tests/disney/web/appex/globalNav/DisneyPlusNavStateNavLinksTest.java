package com.disney.qa.tests.disney.web.appex.globalNav;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBaseNavPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusCreateProfilePage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusNavStateNavLinksTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyPlusBaseNavPage>  dNavPage = new ThreadLocal<>();
    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    private static final ThreadLocal<DisneyPlusCreateProfilePage> dProfilePage = new ThreadLocal<>();
    private static final ThreadLocal<DisneyPlusHomePageSearchPage> dSearchPage = new ThreadLocal<>();

    private String searchUrl = "/search";
    private String watchlistUrl = "/watchlist";
    private String originalsUrl = "/originals";
    private String seriesUrl = "/series";
    private String moviesUrl = "/movies";

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        dNavPage.set(new DisneyPlusBaseNavPage(getDriver()));
        dProfilePage.set(new DisneyPlusCreateProfilePage(getDriver()));
        dSearchPage.set(new DisneyPlusHomePageSearchPage(getDriver()));
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20446"})
    @Test(description = "Disney+ logo links to Home View", groups = {"US", "GB", "ES", "CA" , "NL" , TestGroup.DISNEY_APPEX})
    public void globalNavLinksLogoHomeView() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());


        dSearchPage.get().clickOnSearchBar();

        String searchUrlGrab = dNavPage.get().getCurrentUrl();

        sa.assertTrue(searchUrlGrab.contains(searchUrl),
                dNavPage.get().navPageUrlAssertTrue(searchUrlGrab, searchUrl));

        dNavPage.get().getDplusBaseLogoId().click();

        String urlAfterLogoClick = dNavPage.get().getCurrentUrl();

        sa.assertFalse(urlAfterLogoClick.contains(searchUrl),
                dNavPage.get().navPageUrlAssertFalse(urlAfterLogoClick, "Logo", "/" +
                        " Expected Navigation to home after clicking on Logo"));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20448"})
    @Test(description = "'HOME' links to Home view", groups = {"US", "GB", "ES", "CA" , "NL" , TestGroup.DISNEY_APPEX})
    public void globalNavLinksHomeLinksHomeView() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        dSearchPage.get().clickOnSearchBar();

        String searchUrlGrab = dNavPage.get().getCurrentUrl();

        sa.assertTrue(searchUrlGrab.contains(searchUrl),
                dNavPage.get().navPageUrlAssertTrue(searchUrlGrab, searchUrl));

        dSearchPage.get().clickNavHomeBtn();

        String urlAfterLogoClick = dNavPage.get().getCurrentUrl();

        sa.assertFalse(urlAfterLogoClick.contains(searchUrl),
                dNavPage.get().navPageUrlAssertFalse(urlAfterLogoClick, "Home", "/" +
                        " Expected Navigation to home after clicking on Home Button"));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20450"})
    @Test(description = "'SEARCH' links to Search/Explore view", groups = {"US", "GB", "ES", "CA" , "NL" , TestGroup.DISNEY_APPEX})
    public void globalNavLinksSearchLinksSearchView() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        dSearchPage.get().clickOnSearchBar();

        String urlAfterSearchClick = dNavPage.get().getCurrentUrl();

        sa.assertTrue(urlAfterSearchClick.contains(searchUrl),
                dNavPage.get().navPageUrlAssertFalse(urlAfterSearchClick, "Search", searchUrl +
                        " Expected Navigation to search after clicking on search Button"));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20452"})
    @Test(description = "'WATCHLIST' links to Watchlist view", groups = {"US", "GB", "ES", "CA" , "NL", TestGroup.DISNEY_APPEX})
    public void globalNavLinksWatchlistLinksWatchlistView() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        dNavPage.get().clickNavWatchListBtn();

        String urlAfterWatchListClick = dNavPage.get().getCurrentUrl();

        sa.assertTrue(urlAfterWatchListClick.contains(watchlistUrl),
                dNavPage.get().navPageUrlAssertFalse(urlAfterWatchListClick, "Watchlist", watchlistUrl +
                        " Expected Navigation to watchlist after clicking on Home Button"));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20454"})
    @Test(description = "Global Nav View - Nav Links - 'MORE' expands to display Originals/Movies/Series", groups = {"US", "GB", "ES", "CA" , "NL" ,TestGroup.DISNEY_APPEX})
    public void globalNavLinksMoreExpands() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        dNavPage.get().resizeBrowser(400, 400);

        dNavPage.get().hoverMoreMenuIcon();

        sa.assertTrue(dNavPage.get().getDplusBaseNavMoreOriginalsBtn().isElementPresent(),
                "More Originals Label not Present after mouse hover SubNav Expansion");
        sa.assertTrue(dNavPage.get().getDplusBaseNavMoreSeriesBtn().isElementPresent(),
                "More Series Label not Present after mouse hover SubNav Expansion");
        sa.assertTrue(dNavPage.get().getDplusBaseNavMoreMoviesBtn().isElementPresent(),
                "More Movies Label not Present after mouse hover SubNav Expansion");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20456"})
    @Test(description = "Global Nav View - Nav Links - 'ORIGINALS' links to Originals view", groups = {"US", "GB", "ES", "CA" , "NL" , TestGroup.DISNEY_APPEX})
    public void globalNavOriginalsLinksOriginalsView() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        dNavPage.get().clickNavOriginalsBtn();

        String urlAfterOriginalsClick = dNavPage.get().getCurrentUrl();

        sa.assertTrue(urlAfterOriginalsClick.contains(originalsUrl),
                dNavPage.get().navPageUrlAssertFalse(urlAfterOriginalsClick, "Originals", originalsUrl +
                        " Navigation to originals after clicking on originals Button"));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20458"})
    @Test(description = "'MOVIES' links to Movies view", groups = {"US", "GB", "ES", "CA" , "NL" , TestGroup.DISNEY_APPEX})
    public void globalNavMoviesLinksMoviesView() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        dNavPage.get().clickNavMoviesBtn();

        String movieUrlGrab = dNavPage.get().getCurrentUrl();

        sa.assertTrue(movieUrlGrab.contains(moviesUrl),
                dNavPage.get().navPageUrlAssertFalse(movieUrlGrab, "Movies", moviesUrl +
                        " Expected Navigation to movies after clicking on movies Button"));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20460"})
    @Test(description = "'SERIES' links to Series view", groups = {"US", "GB", "ES", "CA" , "NL" , TestGroup.DISNEY_APPEX})
    public void globalNavSeriesLinksSeriesView() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        dNavPage.get().clickNavSeriesBtn();

        String seriesUrlGrab = dNavPage.get().getCurrentUrl();

        sa.assertTrue(seriesUrlGrab.contains(seriesUrl),
                dNavPage.get().navPageUrlAssertFalse(seriesUrlGrab, "Series", seriesUrl +
                        " Expected Navigation to series after clicking on series Button"));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20462"})
    @Test(description = "'ACCOUNT' expands to display menu items", groups = {"US", "GB", "ES", "CA" , "NL", TestGroup.DISNEY_APPEX})
    public void globalNavAccountExpandsMenuItems() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        dProfilePage.get().hoverOnProfile();

        String isAccountExpanded =  dProfilePage.get().getAccountAttribute("aria-expanded");

        boolean accountStatus = Boolean.parseBoolean(isAccountExpanded);

        sa.assertTrue(accountStatus, "Account isn't expanded after hovering over Account Icon");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20464"})
    @Test(description = "Global Nav View - Nav Links - Design Review - 1440px+", groups = {"US", "GB", "ES", "CA" , "NL", TestGroup.DISNEY_APPEX})
    public void globalNavDesignReview1440() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        String browserDimensions = "1440 x 1440";

        dNavPage.get().resizeBrowser(1456, 1456);

        sa.assertTrue(dNavPage.get().getDplusBaseLogoId().isVisible(),
                "Disney Logo not present on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuHomeIsVisible(),
                "Disney Home label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuSearchIsVisible(),
                "Disney Search label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuWatchListIsVisible(),
                "Disney Watchlist label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuOriginalsIsVisible(),
                "Disney Originals label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuSeriesIsVisible(),
                "Disney Series label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuMoviesIsVisible(),
                "Disney Movies label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dProfilePage.get().isProfileBtnVisible(),
                "Disney Profile button not visible on Nav Page, browser size: " + browserDimensions);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20466"})
    @Test(description = "Design Review - 1024 - 1439px", groups = {"US", "GB", "ES", "CA" , "NL", TestGroup.DISNEY_APPEX})
    public void globalNavDesignReview10241439() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        String browserDimensions = "1024 x 1439";

        dNavPage.get().resizeBrowser(1040, 1455);

        sa.assertTrue(dNavPage.get().getDplusBaseLogoId().isVisible(),
                "Disney Logo not present on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuHomeIsVisible(),
                "Disney Home label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuSearchIsVisible(),
                "Disney Search label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuWatchListIsVisible(),
                "Disney Watchlist label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuOriginalsIsVisible(),
                "Disney Originals label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuSeriesIsVisible(),
                "Disney Series label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().isNavMenuMoviesIsVisible(),
                "Disney Movies label not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue( dProfilePage.get().isProfileBtnVisible(),
                "Disney Profile button not visible on Nav Page, browser size: " + browserDimensions);

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20468"})
    @Test(description = "Design Review - 766 - 1023px", groups = {"US", "GB", "ES", "CA" , "NL" ,TestGroup.DISNEY_APPEX})
    public void globalNavDesignReview7661023() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        String browserDimensions = "766 x 1023";
        dNavPage.get().resizeBrowser(772, 1039);

        sa.assertTrue(dNavPage.get().getDplusBaseLogoId().isVisible(),
                "Disney Logo not present on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getDplusBaseNavHomeLogoBtn().isVisible(),
                "Disney Home Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getDplusBaseNavSearchLogoBtn().isVisible(),
               "Disney Search Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getDplusBaseNavWatchlistLogoBtn().isVisible(),
                "Disney Watchlist Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getNavViewMoreMenuIcn().isVisible(),
                "Disney More Logo not visible on Nav Page, browser size: " + browserDimensions);

        dNavPage.get().getNavViewMoreMenuIcn().hover();

        sa.assertTrue(dNavPage.get().getDplusBaseNavMoreOriginalsBtn().isVisible(),
                "Disney Subnav Originals Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getDplusBaseNavMoreMoviesBtn().isVisible(),
                "Disney Subnav Movies Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getDplusBaseNavMoreSeriesBtn().isVisible(),
                "Disney Subnav Series Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue( dProfilePage.get().isProfileBtnVisible(),
                "Disney Profile button not visible on Nav Page, browser size: " + browserDimensions);


        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20470"})
    @Test(description = "Design Review - 480 - 765px", groups = {"US", "GB", "ES", "CA" , "NL", TestGroup.DISNEY_APPEX})
    public void globalNavDesignReview480765() throws MalformedURLException, JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));

        setOverride(getAccountApi().overrideLocations(disneyAccount.get(), locale));

        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        String browserDimensions = "480 x 765";

        dNavPage.get().resizeBrowser(496, 781);

        sa.assertTrue(dNavPage.get().getDplusBaseLogoId().isVisible(),
                "Disney Logo not present on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getDplusBaseNavHomeLogoBtn().isVisible(),
                "Disney Home Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getDplusBaseNavSearchLogoBtn().isVisible(),
                "Disney Search Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getDplusBaseNavWatchlistLogoBtn().isVisible(),
                "Disney Watchlist Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getNavViewMoreMenuIcn().isVisible(),
                "Disney More Logo not visible on Nav Page, browser size: " + browserDimensions);

        dNavPage.get().getNavViewMoreMenuIcn().hover();

        sa.assertTrue(dNavPage.get().getDplusBaseNavMoreOriginalsBtn().isVisible(),
                "Disney Subnav Originals Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getDplusBaseNavMoreMoviesBtn().isVisible(),
                "Disney Subnav Movies Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue(dNavPage.get().getDplusBaseNavMoreSeriesBtn().isVisible(),
                "Disney Subnav Series Logo not visible on Nav Page, browser size: " + browserDimensions);
        sa.assertTrue( dProfilePage.get().isProfileBtnVisible(),
                "Disney Profile button not visible on Nav Page, browser size: " + browserDimensions);

        sa.assertAll();
    }
}
