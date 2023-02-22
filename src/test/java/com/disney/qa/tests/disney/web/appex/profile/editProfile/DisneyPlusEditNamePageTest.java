package com.disney.qa.tests.disney.web.appex.profile.editProfile;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusEditNamePage;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusEditNamePageTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @Test(description = "Design Review for Profile Listing/Switcher Page",  groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyDefaultProfileListing() {
        SoftAssert softAssert = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditNamePage editNamePage = new DisneyPlusEditNamePage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editNamePage.getActiveProfile();
        editNamePage.clickOnEditProfile();

        softAssert.assertTrue(editNamePage.getEditProfilesDoneButton().isElementPresent(), "Expected 'Done' button to be present on page");
        softAssert.assertTrue(editNamePage.isLogoPresent(), "Expected 'logo' to be present.");
        softAssert.assertTrue(editNamePage.getEditProfilesDoneButton().isElementPresent(),
                "Expected 'Done' button to be present on page");
        softAssert.assertTrue(editNamePage.isSelectAProfileToEditTextPresent(),
                "Expected 'Select a profile to edit' to be present on page");
        softAssert.assertTrue(editNamePage.verifyAvatarNamesOnProfileSelectionPage(ProfileConstant.getAvatarNames()),
                "Expected avatar names to be present on page");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18112"})
    @Test(description = "Verify Duplicate Profile Error- Edit Name",  groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyDuplicateNameError() {
        SoftAssert softAssert = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        getAccountApi().addProfile(disneyAccount.get(), ProfileConstant.PROFILE1, language, null, false);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditNamePage editNamePage = new DisneyPlusEditNamePage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editNamePage.selectProfileName(ProfileConstant.MAIN_TEST);
        editNamePage.getActiveProfile();
        editNamePage.clickOnEditProfile();
        editNamePage.selectProfileName(ProfileConstant.PROFILE1);
        editNamePage.editProfileName(ProfileConstant.MAIN_TEST);

        softAssert.assertTrue(editNamePage.verifyDuplicateNameErrorMessage(),"Expected Duplicate Error message to be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18106","XWEBQAS-18627"})
    @Test(description = "Verify Edit Profile Name",  groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyEditName() {
        SoftAssert softAssert = new SoftAssert();

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditNamePage editNamePage = new DisneyPlusEditNamePage(getDriver());

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editNamePage.getActiveProfile();
        editNamePage.clickOnEditProfile();
        editNamePage.selectProfileName(ProfileConstant.MAIN_TEST);
        editNamePage.editProfileName(ProfileConstant.MICKEY);
        editNamePage
                .clickOnEditProfileDoneButton();
        editNamePage
                .clickOnEditProfilesDoneButton();
        editNamePage.selectProfileName(ProfileConstant.MICKEY);

        softAssert.assertTrue(editNamePage.verifyProfileSelectedMatchOnHomePage().equalsIgnoreCase(ProfileConstant.MICKEY),
                "Expected selected profile name to match on Homepage");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18110"})
    @Test(description = "Verify Error on Name with only spaces",  groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyErrorOnNameWithOnlySpaces() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditNamePage editNamePage = new DisneyPlusEditNamePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        getAccountApi().addProfile(disneyAccount.get(), ProfileConstant.PROFILE1, language, null, false);
        editNamePage.clickOnLogout();

        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editNamePage.selectProfileName(ProfileConstant.MAIN_TEST);
        editNamePage.getActiveProfile();
        editNamePage.clickOnEditProfile();
        editNamePage.selectProfileName(ProfileConstant.PROFILE1);
        editNamePage.editProfileNameWithOnlySpace();

        softAssert.assertTrue(editNamePage.verifyOnlySpaceErrorMessage(), "Expected Only space Error message to be present");

        softAssert.assertAll();
    }
}
