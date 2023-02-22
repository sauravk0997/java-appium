package com.disney.qa.tests.disney.web.appex.account;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusProfileAccountManagementTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18231", "XWEBQAS-18967", "XWEBQAS-18227"})
    @Test (description = "Account Management Screens", groups = {"US", "MX", "JP" , TestGroup.DISNEY_APPEX ,TestGroup.STAR_APPEX, TestGroup.STAR_SMOKE,TestGroup.DISNEY_SMOKE})
    public void verifyAccountManagementScreens()  {
        skipTestByEnv(QA);
        
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1502"));
        
        DisneyAccountApi accountApi = getAccountApi();

        SoftAssert softAssert = new SoftAssert();

        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        disneyAccount.set(account);

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));

        accountPage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        accountPage.clickOnAccountDropdown(isMobile());
        analyticPause();
        accountPage.verifyUrlText(softAssert, "account");
        accountPage.clickOnEmailChange(5);
        accountPage.verifyUrlText(softAssert, "account/enter-passcode");
        accountPage.clickOnPasscodeCancelButtonKey();
        analyticPause();
        accountPage.clickOnPasswordChange(5);
        analyticPause();
        accountPage.verifyUrlText(softAssert, "account/enter-passcode");
        accountPage.clickOnPasscodeCancelButtonKey();
        analyticPause();
        accountPage.clickLogOutAllDevicesButton();
        analyticPause();
        accountPage.verifyUrlText(softAssert, "log-out-devices");
        accountPage.clickOnlogoutAllDevicesCancelButtonKey();
        analyticPause();
        accountPage.clickBundleUpgradeButton(locale, softAssert);
        if(PROJECT.equalsIgnoreCase(STA)) {
            accountPage.navigateBack();
            accountPage.clickOnFinishLaterBtn();
            softAssert.assertTrue(accountPage.isHomeMenuOptionPresent(), "Expected 'Home' menu to be present");
        }
        checkAssertions(softAssert);
    }
}
