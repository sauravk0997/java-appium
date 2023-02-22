package com.disney.qa.tests.disney.web.appex.globalNav;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.web.common.DisneyPlusBaseNavPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusNavStateHoverTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyPlusBaseNavPage>  dNavPage = new ThreadLocal<>();
    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        dNavPage.set(new DisneyPlusBaseNavPage(getDriver()));
        disneyAccount.set(new DisneyAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20442"})
    @Test(description = "Account Automatically Expands", groups = {"US", "GB", "ES", "CA" , "NL" , TestGroup.DISNEY_APPEX})
    public void globalNavHoverAccountExpands() {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));
        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        sa.assertTrue(dNavPage.get().accountDropdownStatus("false").isElementPresent(),
                "Account Menu is already visible before hover");

        dNavPage.get().getNavViewAccountDropdownBtn().hover();

        sa.assertTrue(dNavPage.get().accountDropdownStatus("true").isElementPresent(),
                "Account Menu did not drop down after hover");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20444"})
    @Test(description = "More automatically expands", groups = {"US", "GB", "ES", "CA" , "NL" ,TestGroup.DISNEY_APPEX})
    public void globalNavHoverMoreAutoExpands() {
        SoftAssert sa = new SoftAssert();

        dNavPage.get().environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, "V2"));
        dNavPage.get().dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        SeleniumUtils util = new SeleniumUtils(getDriver());

        util.setBrowserSize(400, 400);

        sa.assertTrue(dNavPage.get().getNavViewMoreMenuIcn().isVisible(),
                "More Item Icon not visible");

        sa.assertAll();
    }
}
