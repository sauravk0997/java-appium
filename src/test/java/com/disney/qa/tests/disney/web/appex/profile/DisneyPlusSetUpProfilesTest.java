package com.disney.qa.tests.disney.web.appex.profile;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusSetUpProfilesPage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusSetUpProfilesTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @QTestCases(id = "43603")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18221"})
    @Test(description = "Junior toggle off in Profile creation flow", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyJuniorToggleOff() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusSetUpProfilesPage setUpProfile = new DisneyPlusSetUpProfilesPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        setUpProfile
                .getActiveProfile();
        setUpProfile
                .clickOnAddProfileFromAccount();
        setUpProfile
                .selectAvatarForNewProfile();
        setUpProfile
                .nameOfNewProfile(ProfileConstant.PROFILE1);
        softAssert
                .assertTrue(setUpProfile.getJuniorToggleStatus("false"), "Expected junior Mode Toggled Off.");

        setUpProfile
                .clickOnSaveButton();

        softAssert.assertAll();
    }

    @Test(description = "Created Profile Junior toggle off by Default ", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyJuniorModeToggleOffDefault() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusSetUpProfilesPage setUpProfile = new DisneyPlusSetUpProfilesPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        setUpProfile
                .getActiveProfile();
        setUpProfile
                .clickOnAddProfileFromAccount()
                .selectAvatarForNewProfile();
        setUpProfile
                .nameOfNewProfile(ProfileConstant.DARTH_VADER);
        setUpProfile
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        setUpProfile
                .selectProfile(ProfileConstant.DARTH_VADER_INCOMPLETE);
        pause(5);
        setUpProfile
                .getActiveProfile();
        setUpProfile
                .clickOnEditProfile();
        setUpProfile
                .selectProfile(ProfileConstant.DARTH_VADER_INCOMPLETE);
        softAssert
                .assertTrue(setUpProfile.getJuniorToggleStatus("false"), "Expected junior Mode Toggled Off.");

        setUpProfile
                .clickOnEditProfileDoneButton();

        softAssert.assertAll();
    }

    @Test(description = "Verify Done Button", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyDone() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusSetUpProfilesPage setUpProfile = new DisneyPlusSetUpProfilesPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        setUpProfile
                .getActiveProfile();
        setUpProfile
                .clickOnAddProfileFromAccount()
                .selectAvatarForNewProfile();
        setUpProfile
                .nameOfNewProfile(ProfileConstant.PROFILE1);
        setUpProfile
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        setUpProfile
                .clickOnEditProfileFromPage()
                .clickOnEditProfilesDoneButton();
        setUpProfile
                .verifyUrlText(softAssert, PageUrl.SELECT_PROFILE);

        softAssert.assertAll();
    }
}
