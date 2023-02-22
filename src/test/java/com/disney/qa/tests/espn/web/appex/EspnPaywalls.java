package com.disney.qa.tests.espn.web.appex;

import com.disney.qa.espn.web.EspnBasePage;
import com.disney.qa.espn.web.EspnLoginPage;
import com.disney.qa.tests.BaseTest;
import com.disney.util.TestGroup;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import org.openqa.selenium.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class EspnPaywalls extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    Dimension d = new Dimension(1400, 900);

    String env = Configuration.get(Configuration.Parameter.ENV);

    EspnLoginPage page = null;

    @BeforeTest
    public void beforeTests(){
        page = new EspnLoginPage(getDriver());
        getDriver().manage().window().setSize(d);
        page.redirectUrl(env, "Video");
        page.waitForPageToFinishLoading();
    }

    @Test(description = "Default Paywall", groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9221")
    public void testDefaultPaywall () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());
        EspnLoginPage espnLoginPage = new EspnLoginPage(getDriver());

        espnLoginPage.checkLoginStatus();
        espnBasePage.openVodAsset();
        espnBasePage.iframePaywallEnter();
        sa.assertTrue(espnBasePage.getPaywallSignUp().isElementPresent(),"Default Paywall did NOT display");
        sa.assertTrue(espnBasePage.getPaywallLogin().isElementPresent(),"CTA Check is not done");
        sa.assertTrue(espnBasePage.getContentPaywall().isElementPresent(),"content check is not done");
        pause( 3);
        espnBasePage.iframePaywallExit();
        sa.assertAll();


    }

    @Test(description = "MLB.TV Paywall", groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9222")
    public void testMlbPaywall () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        page.redirectUrl(env, "TV");
        sa.assertTrue(page.getWatchEspnLogo().isElementPresent(), "Page did NOT load");
        espnBasePage.openMlbAsset();
        sa.assertTrue(espnBasePage.getmLBPaywallLogo().isElementPresent(), "MLB Paywall did NOT display");
        sa.assertTrue(espnBasePage.getmLBSignUp().isElementPresent(), "sign up button not present");
        sa.assertTrue(espnBasePage.getmLBPrice().isElementPresent(), "price is not 74.98$/yr");
        sa.assertAll();
    }
}
