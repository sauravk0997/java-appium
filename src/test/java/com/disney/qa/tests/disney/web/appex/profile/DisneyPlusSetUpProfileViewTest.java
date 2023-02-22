package com.disney.qa.tests.disney.web.appex.profile;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusSetUpProfileViewPage;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusSetUpProfileViewTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19223"})
    @Test(description = "Verify Profile dropdown elements", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyDesignProfileDropdown() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusSetUpProfileViewPage setUpProfileView = new DisneyPlusSetUpProfileViewPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        setUpProfileView.getActiveProfile();

        softAssert.assertTrue(setUpProfileView.getDropdownAccount().isElementPresent(),
                "Expected 'Edit' button to be present");
        softAssert.assertTrue(setUpProfileView.getAccountProfileEdit().isElementPresent(),
                "Expected 'Account' button to be present");
        softAssert.assertTrue(setUpProfileView.getAccountDropdownHelp().isElementPresent(),
                "Expected 'Help' button to be present");
        softAssert.assertTrue(setUpProfileView.getAccountLogout().isElementPresent(),
                "Expected 'Logout' button to be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19227"})
    @Test(description = "Verify Who's Watching - subsequent profile ", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyAddSubsequentProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusSetUpProfileViewPage setUpProfileView = new DisneyPlusSetUpProfileViewPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        setUpProfileView.getActiveProfile();
        setUpProfileView.clickOnAddProfileFromAccount();
        setUpProfileView.selectAvatarForProfile();
        setUpProfileView.addNewProfileName(ProfileConstant.DARTH_VADER);
        setUpProfileView
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        softAssert.assertTrue(setUpProfileView.isWhoIsWatchingPresent(),
                "Expected 'Who's watching?' to be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19229"})
    @Test(description = "Duplicate name error", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyDuplicateNameError() {
        SoftAssert softAssert = new SoftAssert();

        DisneyPlusSetUpProfileViewPage setUpProfileView = new DisneyPlusSetUpProfileViewPage(getDriver());

        verifyAddSubsequentProfile();
        setUpProfileView.clickOnEditProfileFromPage();
        setUpProfileView.selectProfile(ProfileConstant.MAIN_TEST);
        setUpProfileView.editProfileName(ProfileConstant.DARTH_VADER);

        softAssert.assertTrue(setUpProfileView.verifyDuplicateNameErrorMessage(),
                "Expected Duplicate Error message to be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19231"})
    @Test(description = "Max profiles allowed", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyMaxProfiles() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusSetUpProfileViewPage setUpProfileView = new DisneyPlusSetUpProfileViewPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        setUpProfileView.getActiveProfile();
        setUpProfileView.createProfile(ProfileConstant.MAX_PROFILES, ProfileConstant.getProfileNames());
        setUpProfileView.selectProfile(ProfileConstant.MAIN_TEST);

        softAssert.assertTrue(!setUpProfileView.isOpened(),
                "Expected 'Add profile' button should not be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19241"})
    @Test(description = "Done button - Multi profiles", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyDoneBtnMultipleProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyPlusSetUpProfileViewPage setUpProfileView = new DisneyPlusSetUpProfileViewPage(getDriver());

        verifyAddSubsequentProfile();
        setUpProfileView
                .clickOnEditProfileFromPage()
                .clickOnEditProfilesDoneButton();
        setUpProfileView
                .selectProfile(ProfileConstant.DARTH_VADER_INCOMPLETE);
        softAssert
                .assertTrue(setUpProfileView.isHomeMenuOptionPresent(), "Expected 'Home' menu to be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19243"})
    @Test(description = "Done button - Single profiles", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyDoneBtnSingleProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusSetUpProfileViewPage setUpProfileView = new DisneyPlusSetUpProfileViewPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        setUpProfileView
                .getActiveProfile();
        setUpProfileView
                .clickOnEditProfile()
                .clickOnEditProfilesDoneButton();
        setUpProfileView
                .selectProfile(ProfileConstant.MAIN_TEST);
        softAssert.assertTrue(setUpProfileView.isHomeMenuOptionPresent(), "Expected 'Home' menu to be present");

        softAssert.assertAll();
    }
}
