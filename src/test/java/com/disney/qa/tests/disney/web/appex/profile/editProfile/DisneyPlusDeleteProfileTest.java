package com.disney.qa.tests.disney.web.appex.profile.editProfile;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusDeleteProfilePage;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusDeleteProfileTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    private static final ThreadLocal<DisneyApiProvider> apiProvider = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        apiProvider.set(new DisneyApiProvider());
        disneyAccount.set(new DisneyAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19147"})
    @Test(description = "Cancel delete Profile Modal", groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyCancelDeleteProfile() {
        SoftAssert softAssert = cancelProfile(getDriver());

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19145"})
    @Test(description = "verify delete Profile",  groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyDeleteProfile() {
        SoftAssert softAssert = cancelProfile(getDriver());

        DisneyPlusDeleteProfilePage deleteProfilePage = new DisneyPlusDeleteProfilePage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        deleteProfilePage.clickOnDeleteButtonOnEditPage();
        commercePage.clickModalPrimaryBtn();
        deleteProfilePage.clickOnEditProfilesDoneButton();

        softAssert.assertFalse(deleteProfilePage.getSelectedProfileOnEditPage(1).isElementPresent(),
                "Expected secondary profile on Edit page not to be present");

        deleteProfilePage.selectProfileName(ProfileConstant.MAIN_TEST);
        deleteProfilePage.getActiveProfile();

        softAssert.assertFalse(deleteProfilePage.getSelectedDropdownProfile(0).isElementPresent(),
                "Expected secondary profile on dropdown not be present");

        softAssert.assertAll();
    }

    private SoftAssert cancelProfile(WebDriver drv) {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusDeleteProfilePage deleteProfilePage = new DisneyPlusDeleteProfilePage(drv);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.PROFILE1, language, null, true);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        deleteProfilePage.selectProfileName(ProfileConstant.MAIN_TEST);
        deleteProfilePage.getActiveProfile();

        softAssert.assertTrue(deleteProfilePage.isDropdownProfileFirstPresent(),
                "Expected secondary profile from dropdown to be present");

        deleteProfilePage.clickOnEditProfile();
        deleteProfilePage.selectProfileName(ProfileConstant.PROFILE1);
        deleteProfilePage.clickOnDeleteButtonOnEditPage();

        softAssert.assertTrue(commercePage.getModalPrimaryBtn().isElementPresent(),
                "Expected 'Delete' button to be present");

        commercePage.clickModalSecondaryBtn();

        softAssert.assertTrue(deleteProfilePage.getDeleteProfileButton().isElementPresent(), "Expected 'Delete Profile' button to be present on page");

        return softAssert;
    }
}
