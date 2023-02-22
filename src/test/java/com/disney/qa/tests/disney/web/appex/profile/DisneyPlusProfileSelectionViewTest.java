package com.disney.qa.tests.disney.web.appex.profile;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusProfileSelectionViewPage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusProfileSelectionViewTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    private String[] avatarNames = {"Profile", "DARTH VADE..."};

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19199"})
    @Test(description = "Verify Design Review", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyDesignReview() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusProfileSelectionViewPage profileSelectionViewPage = new DisneyPlusProfileSelectionViewPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profileSelectionViewPage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        profileSelectionViewPage
                .selectAvatarForProfile();
        profileSelectionViewPage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        profileSelectionViewPage
                .addNewProfile(ProfileConstant.PROFILE1);
        profileSelectionViewPage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        softAssert
                .assertTrue(profileSelectionViewPage.isLogoPresent(), "Expected 'logo' to be present.");
        softAssert
                .assertTrue(profileSelectionViewPage.isEditProfileFromPagePresent(),
                "Expected 'Edit Profile' button to be present.");
        softAssert
                .assertTrue(profileSelectionViewPage.getAddProfileButtonOnPage().isElementPresent(),
                "Expected 'Add Profile' button to be present on 'Profile Selection' page.");
        softAssert
                .assertTrue(profileSelectionViewPage.verifyAvatarNamesOnProfileSelectionPage(ProfileConstant.getAvatarNames()),
                "Expected avatar names to be present on page");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19201"})
    @Test(description = "Verify New Session With Multiple Profiles", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyNewSessionWithMultipleProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusProfileSelectionViewPage profileSelectionViewPage = new DisneyPlusProfileSelectionViewPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profileSelectionViewPage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        profileSelectionViewPage
                .selectAvatarForProfile();
        profileSelectionViewPage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        profileSelectionViewPage
                .addNewProfile(ProfileConstant.PROFILE1);
        profileSelectionViewPage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        profileSelectionViewPage
                .selectProfile(ProfileConstant.MAIN_TEST);
        profileSelectionViewPage
                .getActiveProfile();
        profileSelectionViewPage
                .clickOnLogout();

        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        softAssert
                .assertTrue(profileSelectionViewPage.isLogoPresent(), "Expected 'logo' to be present.");
        softAssert
                .assertTrue(profileSelectionViewPage.isEditProfileFromPagePresent(),
                "Expected 'Edit Profile' button to be present.");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19207"})
    @Test(description = "Verify New Profile Opens Home View after Login", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifySelectedProfileOpenHomeView() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusProfileSelectionViewPage profileSelectionViewPage = new DisneyPlusProfileSelectionViewPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profileSelectionViewPage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        profileSelectionViewPage
                .selectAvatarForProfile();
        profileSelectionViewPage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        profileSelectionViewPage
                .addNewProfile(ProfileConstant.PROFILE1);
        profileSelectionViewPage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        profileSelectionViewPage
                .selectProfile(ProfileConstant.PROFILE1);
        profileSelectionViewPage
                .getActiveProfile();
        profileSelectionViewPage
                .clickOnLogout();

        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profileSelectionViewPage
                .selectProfile(ProfileConstant.PROFILE1);
        softAssert
                .assertTrue(profileSelectionViewPage.isHomeMenuOptionPresent(), "Expected 'Home' menu to be present");
        softAssert
                .assertTrue(profileSelectionViewPage.verifyProfileSelectedMatchOnHomePage().equalsIgnoreCase(ProfileConstant.PROFILE1),
                "Expected selected profile name to match on Homepage");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19209"})
    @Test(description = "Verify Edit Profile View", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyEditProfileView() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusProfileSelectionViewPage profileSelectionViewPage = new DisneyPlusProfileSelectionViewPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profileSelectionViewPage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        profileSelectionViewPage
                .selectAvatarForProfile();
        profileSelectionViewPage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        profileSelectionViewPage
                .addNewProfile(ProfileConstant.PROFILE1);
        profileSelectionViewPage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        profileSelectionViewPage
                .selectProfile(ProfileConstant.MAIN_TEST);
        profileSelectionViewPage
                .getActiveProfile();
        profileSelectionViewPage
                .clickOnLogout();

        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profileSelectionViewPage
                .clickOnEditProfileFromPage()
                .verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);
        softAssert
                .assertTrue(profileSelectionViewPage.getEditProfilesDoneButton().isElementPresent(), "Expected 'Done' button to be present.");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19211"})
    @Test(description = "Verify 'Add Profile' opens add profile view", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyAddProfileOpenAddProfileView() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusProfileSelectionViewPage profileSelectionViewPage = new DisneyPlusProfileSelectionViewPage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profileSelectionViewPage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        profileSelectionViewPage
                .selectAvatarForProfile();
        profileSelectionViewPage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        profileSelectionViewPage
                .addNewProfile(ProfileConstant.PROFILE1);
        profileSelectionViewPage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        profileSelectionViewPage
                .selectProfile(ProfileConstant.MAIN_TEST);
        profileSelectionViewPage
                .getActiveProfile();
        profileSelectionViewPage
                .clickOnLogout();

        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profileSelectionViewPage.clickOnAddProfileButtonOnPage();
        profileSelectionViewPage.verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);

        softAssert.assertAll();
    }
}
