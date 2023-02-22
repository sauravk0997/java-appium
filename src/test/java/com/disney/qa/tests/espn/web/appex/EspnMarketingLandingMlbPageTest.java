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

public class EspnMarketingLandingMlbPageTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    Dimension d = new Dimension(1400, 900);

    String env = Configuration.get(Configuration.Parameter.ENV);

    EspnLoginPage page = null;

    @BeforeTest
    public void beforeTests(){
        page = new EspnLoginPage(getDriver());
        getDriver().manage().window().setSize(d);
        page.redirectUrl(env, "MLB");
        page.waitForPageToFinishLoading();
        page.closePopUp();
    }

    @Test(description = "Load /MLB Marketing Landing Page", priority = 1,  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9191")
    public void testLoadMlbMarketingLandingPage () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        LOGGER.info("Current URL " + espnBasePage.getCurrentUrl());
        sa.assertTrue(espnBasePage.getCurrentUrl().contains("mlb"), "Not correct URL");
        sa.assertTrue(espnBasePage.getMlbLandingLogo().isElementPresent(), "MLB Landing Page did NOT load");

        sa.assertAll();
    }

    @Test(description = "Language Toggle on /MLB", priority = 2,  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9193")
    public void testLanguageMlbToggle () {
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

    @Test(description = "Verify Footer Elements on /MLB", priority = 3,  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9194")
    public void testVerifyMlbFooterElements () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        sa.assertTrue(espnBasePage.getLandingPageFooterLinks().isElementPresent(), "Footer links did NOT load");
        sa.assertTrue(espnBasePage.getFooterHyperLink(0).getText().contains("Terms Of Use"),
                "Footer links are NOT present in container");

        sa.assertAll();
    }

    @Test(description = "Verify Sticky Header on /MLB", priority = 4,  groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "9195")
    public void testVerifyMlbStickyHeader () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.scrollDownLandingPage();
        sa.assertTrue(espnBasePage.getLandingPageStickyHeader().isElementPresent(), "Sticky Header NOT present on scroll");

        sa.assertAll();
    }

}
