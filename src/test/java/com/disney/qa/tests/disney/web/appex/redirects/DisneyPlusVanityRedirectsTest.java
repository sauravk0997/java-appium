package com.disney.qa.tests.disney.web.appex.redirects;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.Test;

public class DisneyPlusVanityRedirectsTest extends DisneyPlusBaseTest {

    private static final String DISNEY_BUNDLE_TEXT = "welcome/disney";
    private static final String HELP_TEXT = "help.disneyplus";
    private static final String TARGET = "welcome/target";

    //Jira: https://jira.disneystreaming.com/browse/QAA-6507
    @Test(description = "Disney Bundle", groups = {"US", TestGroup.DISNEY_SMOKE,TestGroup.DISNEY_APPEX,TestGroup.STAR_APPEX}, enabled = false)
    public void vanityTestDisneyBundle() {
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1519"));
        SoftAssert softAssert = new SoftAssert();

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        disneyPlusBasePage.getCastedDriver().get(disneyPlusBasePage.getHomeUrl().concat("/disneybundle"));

        disneyPlusBasePage.verifyUrlText(softAssert, DISNEY_BUNDLE_TEXT);

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    //to re-enable after Disney+ day
    @Test(enabled = false,
            description = "Target", groups = {"US",TestGroup.DISNEY_SMOKE,TestGroup.DISNEY_APPEX,TestGroup.STAR_APPEX})
    public void vanityTestTarget() {
        skipTestByEnv(QA);
        
        SoftAssert softAssert = new SoftAssert();

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        disneyPlusBasePage.getCastedDriver().get(disneyPlusBasePage.getHomeUrl().concat("/target"));

        disneyPlusBasePage.verifyUrlText(softAssert, TARGET);

        softAssert.assertAll();
    }

    //Jira: https://jira.disneystreaming.com/browse/QAA-6507
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18891"})
    @Test(description = "Help", groups = {"US",TestGroup.DISNEY_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX})
    public void vanityTestHelp() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1520"));
        SoftAssert softAssert = new SoftAssert();

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        disneyPlusBasePage.getCastedDriver().get(disneyPlusBasePage.getHomeUrl().concat("/help"));

        disneyPlusBasePage.verifyUrlText(softAssert, HELP_TEXT);

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }
}
