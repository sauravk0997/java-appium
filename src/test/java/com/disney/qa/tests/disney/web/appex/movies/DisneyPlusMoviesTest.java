package com.disney.qa.tests.disney.web.appex.movies;

import java.lang.invoke.MethodHandles;
import java.util.List;

import com.disney.qa.common.web.SeleniumUtils;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseTilesPage;
import com.disney.qa.disney.web.appex.headermenu.DisneyPlusMoviesPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

public class DisneyPlusMoviesTest extends DisneyPlusBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31152","XWEBQAS-20458"})
    @Test(description = "Scroll/Filter", priority = 1, groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, "US", "MX", "JP"})
    public void verifyMoviesPageScroll() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1507"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusMoviesPage moviesPage = new DisneyPlusMoviesPage(getDriver());
        moviesPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        moviesPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        moviesPage.clickOnMovieMenuOption();

        DisneyPlusBaseTilesPage tilesPage = new DisneyPlusBaseTilesPage(getDriver());
        List<String> movies = tilesPage.getTileAssetAttributes(100, "alt");

        LOGGER.info(String.format("Size of content before scroll: %s", movies.size()));

        SeleniumUtils util = new SeleniumUtils(getDriver());
        util.scrollToBottom();

        List<String> moviesAfterScroll = tilesPage.getTileAssetAttributes(100, "alt");

        LOGGER.info(String.format("Size of tile content after scroll: %s", moviesAfterScroll.size()));

        softAssert.assertTrue(moviesAfterScroll.size() >= movies.size(),
                String.format("Movies failed to populate after page scroll, original size of tiles grabbed: %s, tiles grabbed afters scroll: %s", movies.size(), moviesAfterScroll.size()));

        moviesPage.clickOnMovieMenuOption();

        moviesPage.clickMovieFilter();
        softAssert.assertTrue(moviesPage.getMoviesFilterOptionDefault().isElementPresent(),
                "Check for default filter");

        moviesPage.selectMovieFilterOption();
        softAssert.assertTrue(moviesPage.isDisneyPlusMovieIndexPresent(1),
                "Expected data to be available after the filter change");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31154"})
    @Test(description = "Logged Out Details Page", priority = 2, groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, "US", "MX", "JP"})
    public void verifyLoggedOutDetailsPage() {
        skipTestByEnv(QA);
        
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1508"));

        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusMoviesPage moviesPage = new DisneyPlusMoviesPage(getDriver());
        moviesPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        moviesPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        analyticPause();
        moviesPage.clickOnMovieMenuOption();
        analyticPause();

        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        homepageSearchPage.clickOnSearchBar();
        analyticPause();
        homepageSearchPage.enterMovieOnSearchBar();
        analyticPause();
        String movieUrl = moviesPage.getCurrentUrl();
        moviesPage.getActiveProfile();
        moviesPage.clickOnLogout();
        analyticPause();
        moviesPage.openDetailsPageUrl(movieUrl);

        softAssert.assertTrue((moviesPage.getSignUpNowCta().isElementPresent()),
                "Sign up now element should be present");
        analyticPause();
        moviesPage.getDriver().navigate().refresh();
        analyticPause();
        softAssert.assertTrue((moviesPage.getSignUpNowCta().isElementPresent()),
                "Sign up now element should be present after refresh");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18302"})
    @Test(description = "Movie Details Page", priority = 3, groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, "US", "MX", "JP"})
    public void verifyMovieDetailPage() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1509"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusMoviesPage moviesPage = new DisneyPlusMoviesPage(getDriver());
        moviesPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        moviesPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        moviesPage.clickOnMovieMenuOption();
        analyticPause();
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        homepageSearchPage.clickOnSearchBar();
        homepageSearchPage.enterMovieOnSearchBar();
        pause(5); //avoid clicking 'QA build'

        SeleniumUtils util = new SeleniumUtils(getDriver());
        util.scrollToBottom();

        analyticPause();
        moviesPage.verifySuggestedTab();
        analyticPause();
        moviesPage.verifyDetailsTab();
        analyticPause();
        softAssert.assertTrue(moviesPage.getMovieDetailsMetaData().isElementPresent(),
                "Duration meta data should be present if the 'Details' tab is clicked");

        moviesPage.verifyExtrasTab();

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }
}

