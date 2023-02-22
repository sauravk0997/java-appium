package com.disney.qa.tests.espn.ios;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.espn.ios.pages.authentication.EspnPaywallIOSPageBase;
import com.disney.qa.espn.ios.pages.authentication.EspnPaywallLogInIOSPageBase;
import com.disney.qa.espn.ios.pages.common.EspnFavoriteLeagueSelectionIOSPageBase;
import com.disney.qa.espn.ios.pages.common.EspnFavoriteTeamsSelectionIOSPageBase;
import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.disney.qa.espn.ios.pages.common.EspnLandingIOSPageBase;
import com.disney.qa.espn.ios.pages.watch.EspnWatchIOSPageBase;
import com.disney.qa.tests.BaseMobileTest;
import com.qaprosoft.carina.core.foundation.report.testrail.TestRailCases;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;


public class EspnPaywallAndAuthenticationAcceptanceIOSTest extends BaseMobileTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private static final String EXPECTED = "Expected: ";


    @TestRailCases(testCasesId = "1514519")
    @Test(description = "Location Prompt is displayed during First Launch")
    public void testFirstLaunchLocationPrompt() {

       SoftAssert softAssert = new SoftAssert();

       EspnIOSPageBase espnIOSPageBase = initPage(EspnIOSPageBase.class);

       if(espnIOSPageBase.isLocationPromptPresent()){
           softAssert.assertTrue(true);
           new IOSUtils().acceptAlert();
       } else {
           try {
               softAssert.fail(EXPECTED + " Location prompt to be present on first launch");
           } catch (AssertionError e){
               LOGGER.info("Location prompt missing", e);
           }
       }
       espnIOSPageBase.handleAlert("Notification");

       softAssert.assertAll();
    }

    @TestRailCases(testCasesId = "1514520")
    @Test(description = "Paywall displayed when trying to access Premium Content",
            dependsOnMethods = "testFirstLaunchLocationPrompt")
    public void testPaywallForPremiumContent() {

       SoftAssert softAssert = new SoftAssert();

       initPage(EspnLandingIOSPageBase.class).getEditionPage();

        EspnFavoriteLeagueSelectionIOSPageBase espnFavoriteLeagueSelectionIOSPageBase =
                initPage(EspnFavoriteLeagueSelectionIOSPageBase.class);

      espnFavoriteLeagueSelectionIOSPageBase.getFavoriteTeamsSelectionPage();

      initPage(EspnFavoriteTeamsSelectionIOSPageBase.class).getHomePage();

       EspnIOSPageBase espnIOSPageBase = initPage(EspnIOSPageBase.class);

       espnIOSPageBase.handleAlert("Customization");

       espnIOSPageBase.getWatchPageFirstTry(1);

        Screenshot.capture(getDriver(), "Watch Page");

       Screenshot.capture(getDriver(), "Paywall");

       softAssert.assertTrue(initPage(EspnPaywallIOSPageBase.class).isOpened(),
               EXPECTED + "Paywall to be displayed");

       softAssert.assertAll();

    }

    @TestRailCases(testCasesId = "1514521")
    @Test(description = "Verify Paywall Basics", dependsOnMethods = "testPaywallForPremiumContent")
    public void testPaywallBasics() {

       SoftAssert softAssert = new SoftAssert();

        EspnPaywallIOSPageBase espnPaywallIOSPageBase = initPage(EspnPaywallIOSPageBase.class);

        Screenshot.capture(getDriver(), "Paywall");

       softAssert.assertTrue(espnPaywallIOSPageBase.arePaywallBasicElementsPresent(),
               EXPECTED + "Basic elements to be displayed in Paywall");

       softAssert.assertAll();

    }

    @TestRailCases(testCasesId = "1514522")
    @Test(description = "User can Log In through Paywall", dependsOnMethods = "testPaywallBasics")
    public void testPaywallLogIn() {

        SoftAssert softAssert = new SoftAssert();

        initPage(EspnPaywallIOSPageBase.class).getLogInPage();

        initPage(EspnPaywallLogInIOSPageBase.class).login("QA");

        Screenshot.capture(getDriver(), "Watch page");

        softAssert.assertTrue(initPage(EspnWatchIOSPageBase.class).isOpened(),
                EXPECTED + "Watch title to be displayed");

        /**Second test rail case verification doesn't work in app
         (Test Rail Link: https://qa-reporting.us-east-1.bamgrid.net/testrail/index.php?/cases/view/1484541)
         **/

        softAssert.assertAll();

    }

    @TestRailCases(testCasesId = "1514523")
    @Test(description = "User taken to previous screen after Login", dependsOnMethods = "testPaywallLogIn")
    public void testPostLogin() {

        SoftAssert softAssert = new SoftAssert();

        Screenshot.capture(getDriver(), "Watch page");

        softAssert.assertTrue(initPage(EspnWatchIOSPageBase.class).isDefaultView(),
                EXPECTED + "Watch page to be displayed");

        softAssert.assertAll();

    }



}
