package com.disney.qa.tests.disney.web.appex.homepage;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.headermenu.DisneyPlusMoviesPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageCarouselPage;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusBaseDetailsPage;
import com.disney.qa.disney.web.appex.media.DisneyPlusVideoPlayerPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;

public class DisneyPlusHomePageCarouselTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    //These test are part of this ticket: https://jira.bamtechmedia.com/browse/QAA-5875
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18494", "XWEBQAS-18496", "XWEBQAS-18501"})
    @Test(description = "Verify Main Carousel", groups = {"US", "MX", TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE})
    public void verifyMainCarousel() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1503"));
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, new ArrayList<>(Arrays.asList("MX")), EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        disneyAccount.set(account);
        accountApi.addFlex(account);

        DisneyPlusHomePageCarouselPage homepageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        homepageCarouselPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        homepageCarouselPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.isHomeMenuOptionPresent();
        pause(10); // pause time for proper loading
        homepageCarouselPage.clickThroughTopCarousel(softAssert, homepageCarouselPage.getDynamicCarouselRightArrow(0));
        pause(5); // pause time for proper loading
        homepageCarouselPage.clickThroughTopCarousel(softAssert, homepageCarouselPage.getDynamicCarouselLeftArrow(0));

        //Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19003", "XWEBQAS-19005", "XWEBQAS-27590", "XWEBQAS-27593", "XWEBQAS-27595",
            "XWEBQAS-30884", "XWEBQAS-30883", "XWEBQAS-30882", "XWEBQAS-30881", "XWEBQAS-30880"})
    @Test(description = "Verify Brand Tiles", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, "US", "MX", "JP"})
    public void verifyBrandTiles() {
        skipTestByEnv(QA);
        
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1504"));
        skipTestByProjectLocale(locale, new ArrayList<>(Arrays.asList("MX")), EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        disneyAccount.set(account);
        analyticPause();
        DisneyPlusHomePageCarouselPage homepageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        homepageCarouselPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        analyticPause();
        homepageCarouselPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        analyticPause();
        homepageCarouselPage.verifyBrandTiles(softAssert, "Disney", "disney");
        homepageCarouselPage.verifyBrandTiles(softAssert, "Pixar", "pixar");
        homepageCarouselPage.verifyBrandTiles(softAssert, "Marvel", "marvel");
        homepageCarouselPage.verifyBrandTiles(softAssert, "Star Wars", "star-wars");
        homepageCarouselPage.verifyBrandTiles(softAssert, "National Geographic", "national-geographic");
        if (!locale.equalsIgnoreCase("US")) {
            homepageCarouselPage.verifyBrandTiles(softAssert, "Star", "star");
        }
        analyticPause();
        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-21692"})
    @Test(description = "Verify Beneath Carousel", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, "US", "MX", "JP"})
    public void verifyBeneathCarousel() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1505"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusHomePageCarouselPage homepageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        homepageCarouselPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        homepageCarouselPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.isHomeMenuOptionPresent();
        pause(10); // pause time for proper loading
        homepageCarouselPage.clickThroughButtonCarousel(softAssert, homepageCarouselPage.getDynamicCarouselRightArrow(1), 1);
        pause(5); // pause time for proper loading
        homepageCarouselPage.clickThroughButtonCarousel(softAssert, homepageCarouselPage.getDynamicCarouselLeftArrow(1), 1);

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18464", "XWEBQAS-18466"})
    @Test(description = "Verify Continue Watching", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, "US", "MX", "JP"})
    public void verifyContinueWatching() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1506"));
        SoftAssert sa = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyPlusHomePageCarouselPage homepageCarouselPage = new DisneyPlusHomePageCarouselPage(getDriver());
        homepageCarouselPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        homepageCarouselPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusMoviesPage moviesPage = new DisneyPlusMoviesPage(getDriver());
        moviesPage.clickOnMovieMenuOption();
        analyticPause();
        DisneyPlusHomePageSearchPage homepageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        homepageSearchPage.clickOnSearchBar();
        homepageSearchPage.enterMovieOnSearchBar();
        analyticPause();
        DisneyPlusBaseDetailsPage detailsPage = new DisneyPlusBaseDetailsPage(getDriver());
        detailsPage.clickDetailsPagePlayBtn();

        pause(25);//need to let the video play in order for it to be added on 'continue watching' on homepage

        DisneyPlusVideoPlayerPage videoPlayerPage = new DisneyPlusVideoPlayerPage(getDriver());
        String key = videoPlayerPage.getUrlContentKey();
        videoPlayerPage.hoverVideoPlayer();
        videoPlayerPage.clickVideoPlayerBackArrow();
        analyticPause();
        homepageCarouselPage.clickOnHomeMenuOption();

        sa.assertTrue(homepageCarouselPage.isContentTileByKeyPresent(key),
                String.format("Continue watching tile not present for key: %s", key));

        analyticPause();
        homepageCarouselPage.clickContentTileByKey(key);
        pause(7);

        String resumeKey = videoPlayerPage.getUrlContentKey();

        sa.assertEquals(key, resumeKey,
                String.format("Continue watching content not equal to content desired: original key: %s, continue watching key: %s", key, resumeKey));

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(sa);
    }
}
