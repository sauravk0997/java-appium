package com.disney.qa.tests.espn.web.appex;

import com.disney.qa.espn.web.EspnBasePage;
import com.disney.qa.espn.web.EspnLoginPage;
import com.disney.qa.espn.web.EspnWebParameters;
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


public class EspnMarketingLandingDefaultPageTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    Dimension d = new Dimension(1400, 900);

    private String url = EspnWebParameters.ESPN_WEB_PROD_LANDING_URL.getValue();
    String env = Configuration.get(Configuration.Parameter.ENV);

    EspnLoginPage page = null;

    @BeforeTest
    public void beforeTests(){
        page = new EspnLoginPage(getDriver());
        getDriver().manage().window().setSize(d);
        getDriver().get(url);
        page.environmentSetUp(env);
        page.waitForPageToFinishLoading();
        page.closePopUp();
    }

    @Test(description = "Load Marketing Landing Page", priority = 1,  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9185")
    public void testLoadMarketingLandingPage () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        LOGGER.info("Current URL " + espnBasePage.getCurrentUrl());
        sa.assertTrue(espnBasePage.getEspnPlusLogo().isElementPresent(), "Landing Page did NOT load");

        sa.assertAll();
    }

    @Test(description = "Language Toggle", priority = 2,  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9187")
    public void testLanguageToggle () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.switchLanguages();
        page.waitForPageToFinishLoading();
        page.closePopUp();
        sa.assertTrue(espnBasePage.getCurrentUrl().contains("/es/"), "URL does NOT reflect spanish");
        espnBasePage.switchLanguages();
        page.waitForPageToFinishLoading();
        page.closePopUp();
        sa.assertFalse(espnBasePage.getCurrentUrl().contains("/es/"), "URL does NOT reflect english");
        sa.assertAll();

    }

    @Test(description = "Verify Footer Elements", priority = 3 ,  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9188")
    public void testVerifyFooterElements () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());
        sa.assertTrue(espnBasePage.getLandingPageFooterLinks().isElementPresent(), "Footer links did NOT load");
        sa.assertTrue(espnBasePage.getFooterHyperLink(0).getText().contains("Support & FAQ"),
                "Footer links are NOT present in container");

        sa.assertAll();
    }

    @Test(description = "Verify Sticky Header", priority = 4 , groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9189")
    public void testVerifyStickyHeader () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());
        espnBasePage.scrollDownLandingPage();
        sa.assertTrue(espnBasePage.getLandingPageStickyHeader().isElementPresent(), "Sticky Header NOT present on scroll");

        sa.assertAll();
    }
}
