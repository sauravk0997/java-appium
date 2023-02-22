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

public class EspnMarketingLandingUfcPageTest extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    Dimension d = new Dimension(1400, 900);

    String env = Configuration.get(Configuration.Parameter.ENV);

    EspnLoginPage page = null;
    EspnBasePage basePage = null;

    @BeforeTest
    public void beforeTests(){
        basePage = new EspnBasePage(getDriver());
        page = new EspnLoginPage(getDriver());
        getDriver().manage().window().setSize(d);
        page.redirectUrl(env, "UFC");
        page.waitForPageToFinishLoading();
        basePage.skipTestExecution();
        page.closePopUp();
    }

    @Test(description = "Load /UFC Marketing Landing Page", priority = 1, groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "69264")
    public void testLoadUfcMarketingLandingPage () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        LOGGER.info("Current URL " + espnBasePage.getCurrentUrl());
        sa.assertTrue(espnBasePage.getCurrentUrl().contains("ufc") || espnBasePage.getCurrentUrl().contains("fight-night"), "Not correct URL");
        sa.assertTrue(espnBasePage.getEspnPlusLogo().isElementPresent(), "UFC Landing Page did NOT load");

        sa.assertAll();
    }

    @Test(description = "Language Toggle on /UFC", priority = 2, groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "69266")
    public void testLanguageUfcToggle () {
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

    @Test(description = "Verify Footer Elements on /UFC", priority = 3, groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "69267")
    public void testVerifyUfcFooterElements () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        sa.assertTrue(espnBasePage.getLandingPageFooterLinks().isElementPresent(), "Footer links did NOT load");
        sa.assertTrue(espnBasePage.getFooterHyperLink(0).getText().contains("Support & FAQ"),
                "Footer links are NOT present in container");

        sa.assertAll();
    }

    @Test(description = "Verify Sticky Header on /UFC", priority = 4, groups = {TestGroup.ESPN_APPEX})
    @QTestCases(id = "69268")
    public void testVerifyUfcStickyHeader () {
        SoftAssert sa = new SoftAssert();
        EspnBasePage espnBasePage = new EspnBasePage(getDriver());

        espnBasePage.scrollDownLandingPage();
        sa.assertTrue(espnBasePage.getLandingPageStickyHeader().isElementPresent(), "Sticky Header NOT present on scroll");

        sa.assertAll();
    }
}
