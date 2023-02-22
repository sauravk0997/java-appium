package com.disney.qa.tests.disney.web.appex.profile;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.parentalcontrols.DisneyPlusSetProfilePinPage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusAutoplayPage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusEditProfilePage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusJuniorModePage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAddProfilePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusSetUpProfileViewPage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAddProfileTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal();

    private static final String BROWSER = R.CONFIG.get("browser");

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18185", "XWEBQAS-18627", "XWEBQAS-31378", "XWEBQAS-31380", "XWEBQAS-31350"})
    @Test(description = "Add second Profile from Menu dropdown on Home page ", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyAddSecondProfileFromMenuDropdown() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        addProfilePage
                .getActiveProfile();
        addProfilePage
                .clickOnAddProfileFromAccount();
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        addProfilePage
                .selectAvatarForProfile();
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        addProfilePage
                .clickOnSaveButton();
        softAssert
                .assertTrue(addProfilePage.getProfileNameErrorLabelValue().equals(WebConstant.FIELD_REQUIRED_ERROR_LABEL),
                        "Expected to see error message displayed when profile name is empty");
        softAssert
                .assertTrue(editProfilePage.getDOBInputErrorLabelValue().equals(WebConstant.ENTER_VALID_DOB),
                        "Expected to see error message displayed when date of birth is empty");
        softAssert
                .assertTrue(addProfilePage.isGenderRequiredErrorVisible(),
                        "Expected to see error message when Gender dropdown is empty");

        addProfilePage
                .addProfileName(ProfileConstant.PROFILE1);
        editProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender();

        String gender = editProfilePage.getProfileGenderValue();
        String dob = editProfilePage.getDOBValue();

        editProfilePage
                .clickOnSaveButton();
        commercePage
                .checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());
        addProfilePage
                .selectProfile(ProfileConstant.PROFILE1);
        softAssert
                .assertTrue(addProfilePage.verifyProfileSelectedMatchOnHomePage().equalsIgnoreCase(ProfileConstant.PROFILE1),
                "Expected selected profile name to match on Homepage");

        addProfilePage
                .getActiveProfile();
        addProfilePage
                .clickOnEditProfile();
        editProfilePage
                .selectProfileName(ProfileConstant.PROFILE1);
        softAssert
                .assertTrue(editProfilePage.getEditProfileDOBValue().equals(appExUtil.formatDate("MM / dd / yyyy", dob, "MMMM d, yyyy")), "Expected to see correct DOB is displayed");
        softAssert
                .assertTrue(editProfilePage.getGender().equals(gender), "Expected to see correct gender is displayed");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18183", "XWEBQAS-30879"})
    @Test(description = "Verify Add Profile From Who Is Watching Page", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyAddSecondProfileFromWhoIsWatchingPage() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        addProfilePage
                .getActiveProfile();
        addProfilePage
                .clickOnEditProfile();
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);
        addProfilePage
                .clickOnAddProfileButtonOnPage();
        addProfilePage
                .selectAvatarForProfile();
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        addProfilePage
                .addProfileName(ProfileConstant.PROFILE1);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();
        commercePage
                .checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());
        addProfilePage
                .selectProfile(ProfileConstant.PROFILE1);
        softAssert
                .assertTrue(addProfilePage.verifyProfileSelectedMatchOnHomePage().equalsIgnoreCase(ProfileConstant.PROFILE1),
                String.format("Expected profile name: %s", addProfilePage.verifyProfileSelectedMatchOnHomePage()));

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18187, XWEBQAS-31380"})
    @Test(description = "Verify Duplicate Name Error - New Profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyDuplicateProfileNameError() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusSetUpProfileViewPage profileViewPage = new DisneyPlusSetUpProfileViewPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        addProfilePage
                .clickOnAddProfileFromAccount();
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        addProfilePage
                .selectAvatarForProfile();
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        addProfilePage
                .addProfileName(ProfileConstant.MAIN_TEST);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();
        softAssert.assertTrue(profileViewPage.verifyDuplicateNameErrorMessage(), "Expected Duplicate Name Error Message to be present");

        addProfilePage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        addProfilePage
                .addProfileName(ProfileConstant.PROFILE1);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();
        commercePage
                .checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());
        softAssert
                .assertTrue(addProfilePage.isEditProfileFromPagePresent(),
                "Expected 'Edit Profile' button to be present.");

        softAssert.assertAll();
    }

    @QTestCases(id = "57047")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20194"})
    @Test(description = "Verify Add Profile Design Review", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyDesignReview() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        addProfilePage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        softAssert
                .assertTrue(addProfilePage.verifyChooseAvatarTextPresent(), "Expected 'Choose Icon' text to be present");
        softAssert
                .assertTrue(addProfilePage.getSkipButton().isElementPresent(), "Expected 'Skip' text to be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20196"})
    @Test(description = "Verify Avatar Selection", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyAvatarSelection() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        addProfilePage
                .getActiveProfile();
        addProfilePage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        softAssert
                .assertTrue(addProfilePage.verifyCorrectIconDisplayed(), "Expected correct Icon to display on page");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20198"})
    @Test(description = "Verify Avatar Selection Must Be Unique", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifySelectionIconUnique() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        addProfilePage
                .clickOnAddProfileFromAccount();
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        addProfilePage
                .selectAvatarForProfile();
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        addProfilePage
                .addProfileName(ProfileConstant.PROFILE1);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();
        commercePage
                .checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());
        addProfilePage
                .clickOnPrimaryProfileOnEditPage();
        softAssert
                .assertTrue(addProfilePage.isHomeMenuOptionPresent(), "Expected 'Home' menu to be present on page");

        addProfilePage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        pause(5);//needed for loading for non-US regions
        softAssert
                .assertTrue(addProfilePage.verifySelectedIconDoesNotExistOnPage(ProfileConstant.PROFILE1),
                "Expected selected avatar not to reappear on page");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-20200"})
    @Test(description = "Verify Skip on Avatar Page", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyAvatarSkip() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        addProfilePage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        addProfilePage
                .clickOnSkip();
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31380", "XWEBQAS-31384", "XWEBQAS-31382"})
    @Test(description = "Add second Profile for under eighteen DOB ", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyAddSecondaryProfileForUnderEighteen() {
        SoftAssert softAssert = new SoftAssert();
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        SeleniumUtils util = new SeleniumUtils(getDriver());
        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());
        DisneyPlusAutoplayPage disneyPlusAutoplayPage = new DisneyPlusAutoplayPage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());
        DisneyPlusJuniorModePage juniorModePage = new DisneyPlusJuniorModePage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.PROFILE, language, null, true);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        addProfilePage
                .selectProfile(ProfileConstant.PROFILE);
        editProfilePage
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_EIGHTEEN_DOB);
        softAssert
                .assertTrue(editProfilePage.isGenderDropdownDisabled(),
                        "Expected to see gender dropdown as disabled after DOB is specified");

        editProfilePage
                .clickOnSaveButton();
        softAssert
                .assertTrue(addProfilePage.verifyProfileSelectedMatchOnHomePage().equalsIgnoreCase(ProfileConstant.PROFILE),
                        "Expected selected profile name to match on Homepage");

        disneyPlusAutoplayPage
                .getActiveProfile();
        disneyPlusAutoplayPage
                .exitJuniorProfile()
                .clickOnEditProfileFromPage();
        disneyPlusAutoplayPage
                .selectProfileToEdit(ProfileConstant.PROFILE);
        waitForSeconds(2);
        if(BROWSER.equalsIgnoreCase("firefox")) {
            util.scrollToBottom();
            waitForSeconds(3);
        }
        softAssert
                .assertTrue(editProfilePage.getContentRatingHoverMessage().contains(WebConstant.JUNIOR_MODE),
                        "Expected to see content rating message on hover when junior mode is enabled");
        softAssert
                .assertTrue(editProfilePage.getGroupWatchHoverMessage().contains(WebConstant.JUNIOR_MODE),
                        "Expected to see group watch message on hover when junior mode is enabled");

        juniorModePage
                .clickOnJuniorModeToggle();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        waitForSeconds(10);
        if(BROWSER.equalsIgnoreCase("firefox")) {
            util.scrollToBottom();
            waitForSeconds(3);
        }
        softAssert
                .assertTrue(juniorModePage.getKidProofExitHoverMessage().contains(WebConstant.JUNIOR_MODE),
                        "Expected to see kid proof exit hover message");

        juniorModePage
                .clickOnEditProfileDoneButton();
        addProfilePage
                .clickOnAddProfileButtonOnPage();
        addProfilePage
                .selectAvatarForProfile();
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        addProfilePage
                .addProfileName(ProfileConstant.PROFILE1);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_EIGHTEEN_DOB)
                .clickOnSaveButton();
        commercePage
                .checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());
        addProfilePage
                .clickOnEditProfileFromPage();
        editProfilePage
                .selectProfileName(ProfileConstant.PROFILE1);
        softAssert
                .assertFalse(editProfilePage.isEditProfileGenderFieldPresent(), "Expected to not see Gender field");

        editProfilePage
                .clickOnEditProfileDoneButton();
        editProfilePage
                .clickOnEditProfilesDoneButton();
        addProfilePage
                .selectProfile(ProfileConstant.PROFILE1);
        softAssert
                .assertTrue(addProfilePage.verifyProfileSelectedMatchOnHomePage().equalsIgnoreCase(ProfileConstant.PROFILE1),
                        "Expected selected profile name to match on Homepage");

        softAssert.assertAll();
    }
}
