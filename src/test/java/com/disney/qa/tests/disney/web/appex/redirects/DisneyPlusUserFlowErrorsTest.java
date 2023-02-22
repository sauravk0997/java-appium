package com.disney.qa.tests.disney.web.appex.redirects;

import com.disney.qa.api.dgi.validationservices.hora.HoraValidator;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.disney.pojo.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusUserFlowErrorsTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> accountApi = new ThreadLocal<>();
    private static final ThreadLocal<DisneyApiProvider> apiProvider = new ThreadLocal<>();

    private static final String DISNEY_BUNDLE_TEXT = "welcome/disney";
    private static final String HELP_TEXT = "help.disneyplus";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        apiProvider.set(new DisneyApiProvider());
        accountApi.set(new DisneyAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18635"})
    @Test(description = "Design Review", groups = {TestGroup.DISNEY_APPEX})
    public void emailDesignReview() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusUserPage page = new DisneyPlusUserPage(getDriver());
        page.open(getDriver());
        page.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));


        page.handleStandaloneCtasOnOlp();

        sa.assertTrue(page.isEmailFieldPresent());

        page.typeEmailFieldId("invalidEmail");
        page.clickOnSignupContinueButton();

        sa.assertTrue(page.isInvalidEmailMsgPresent(),
                "Invalid Email Message not present after entering `invalidEmail` in Email Field");

        checkAssertions(sa);
    }

    //Jira: https://jira.disneystreaming.com/browse/QAA-6507
    @Test(description = "Disney Bundle", groups = {TestGroup.DISNEY_APPEX, "US"})
    public void vanityTestDisneyBundle() {
        SoftAssert softAssert = new SoftAssert();

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        disneyPlusBasePage.getCastedDriver().get(disneyPlusBasePage.getHomeUrl().concat("/disneybundle"));

        disneyPlusBasePage.verifyUrlText(softAssert, DISNEY_BUNDLE_TEXT);

        checkAssertions(softAssert);
    }

    //Jira: https://jira.disneystreaming.com/browse/QAA-6507
    @Test(description = "Help", groups = {TestGroup.DISNEY_APPEX, "AG","AI","AR","AT","AU","AW","BB","BE","BL","BM","BO","BQ","BR","BS","BZ","CA","CH","CL","CO","CR","CW","DE","DK","DM","DO","EC","ES","FI","FK","FR","GB","GD","GF","GG","GL","GP","GS","GT","GY","HN","HT","IE","IM","IS","IT","JE","JM","JP","KR","KN","KY","LC","LU","MC","MF","MQ","MS","MU","MX","NC","NI","NL","NO","NZ","PA","PE","PR","PT","PY","RE","SE","SR","SG","SV","TC","TT","US","UY","VC","VE","VG","VI","WF","YT"})
    public void vanityTestHelp() {
        SoftAssert softAssert = new SoftAssert();

        DisneyPlusBasePage disneyPlusBasePage = new DisneyPlusBasePage(getDriver());
        disneyPlusBasePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        disneyPlusBasePage.getCastedDriver().get(disneyPlusBasePage.getHomeUrl().concat("/help"));

        disneyPlusBasePage.verifyUrlText(softAssert, HELP_TEXT);

        checkAssertions(softAssert);
    }
}
