package com.disney.qa.tests.disney.web.appex.profile.editProfile;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.parentalcontrols.DisneyPlusSetProfilePinPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAddProfilePage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusJuniorModePage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusEditProfilePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.AccountUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase.getDictionary;

public class DisneyPlusEditProfileTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();
    protected static final ThreadLocal<JsonNode> dictionary = new ThreadLocal<>();

    @BeforeMethod
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
        dictionary.set(getFullDictionary());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18201"})
    @Test(description = "Design Review - Edit Main Profile Page", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyEditDefaultProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editProfilePage
                .getActiveProfile();
        editProfilePage
                .clickOnEditProfile();
        editProfilePage
                .selectProfileName(ProfileConstant.MAIN_TEST);
        softAssert
                .assertTrue(editProfilePage.isLogoPresent(),
                        "Expected 'logo' to be present.");
        softAssert
                .assertTrue(editProfilePage.isProfileNameFieldPresent(),
                        "Expected 'Profile Name' field to be present");
        softAssert
                .assertTrue(editProfilePage.isTextPresentInParaElement("Autoplay"),
                        "Expected 'Autoplay' to be present");
        softAssert
                .assertTrue(editProfilePage.isTextPresentInDivElement("Autoplay"),
                        "Expected 'Autoplay Metadata' to be present");
        softAssert
                .assertTrue(editProfilePage.isTextPresent("Edit Profile"),
                        "Expected 'Edit Profile' to be present");
        softAssert
                .assertTrue(editProfilePage.getTextFromParaElement(ProfileConstant.PRIMARY_PROFILE).isElementPresent(),
                        "Expected 'Primary Profile Explainer' to be present");
        softAssert
                .assertTrue(editProfilePage.isTextPresent("Background Video"),
                        "Expected 'Background Video' to be present");
        softAssert
                .assertTrue(editProfilePage.isBackgroundVideoExplainerPresent(),
                        "Expected 'Background video explainer' to be present.");
        softAssert
                .assertTrue(editProfilePage.isTextPresent("App Language"),
                        "Expected 'App Language' to be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18203"})
    @Test(description = "Design Review - Non-default Profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyNonDefaultProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editProfilePage
                .getActiveProfile();
        editProfilePage
                .clickOnAddProfileFromAccount()
                .selectAvatarForNewProfile();
        editProfilePage
                .nameOfNewProfile(ProfileConstant.DARTH_VADER);
        editProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        editProfilePage
                .selectProfileName(ProfileConstant.DARTH_VADER_INCOMPLETE);
        pause(2);//need to wait or else the test bypasses the next step
        editProfilePage
                .clickOnEditProfile();
        editProfilePage
                .selectProfileName(ProfileConstant.DARTH_VADER_INCOMPLETE);

        softAssert
                .assertTrue(editProfilePage.isLogoPresent(),
                        "Expected 'logo' to be present.");
        softAssert
                .assertTrue(editProfilePage.isTextPresentInParaElement("Autoplay"),
                        "Expected 'Autoplay' to be present");
        softAssert
                .assertTrue(editProfilePage.isTextPresentInDivElement("Autoplay"),
                        "Expected 'Autoplay Metadata' to be present");
        softAssert
                .assertTrue(editProfilePage.isTextPresent("Edit Profile"),
                        "Expected 'Edit Profile' to be present");
        softAssert
                .assertTrue(editProfilePage.isTextPresent("Background Video"),
                        "Expected 'Background Video' to be present");
        softAssert
                .assertTrue(editProfilePage.isBackgroundVideoExplainerPresent(),
                        "Expected 'Background Explainer' to be present");
        softAssert
                .assertTrue(editProfilePage.isJuniorModeLabelPresent(),
                        "Expected 'Junior Mode label' to be present");
        softAssert
                .assertTrue(editProfilePage.isTextPresentInDivElement(WebConstant.CURATED_CONTENT),
                "Expected 'Kids Profile Explainer' to be present");
        softAssert
                .assertTrue(editProfilePage.isTextPresent("App Language"),
                        "Expected 'App Language' to be present");
        softAssert
                .assertTrue(editProfilePage.isDeleteProfileBtnPresent(),
                        "Expected 'Delete Profile' button to be present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18205", "XWEBQAS-31252", "XWEBQAS-31382"})
    @Test(description = "Verify Change Avatar", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyChangeAvatar() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());
        DisneyPlusAddProfilePage disneyPlusAddProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editProfilePage
                .getActiveProfile();
        editProfilePage
                .clickOnAddProfileFromAccount();
        editProfilePage
                .selectAvatarForNewProfile();

        String avatarName = disneyPlusAddProfilePage.getSelectedAvatarName();

        editProfilePage
                .nameOfNewProfile(ProfileConstant.MICKEY);
        editProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton()
                .waitForSeconds(5);

        disneyPlusSetProfilePinPage
                .clickOnCancelPinBtn()
                .waitForSeconds(5);
        editProfilePage
                .clickOnEditProfileFromPage()
                .clickOnPrimaryProfileBtn();
        softAssert
                .assertTrue(editProfilePage.isTextPresentInParaElement(WebConstant.JUNIOR_MODE), "Expected to see junior mode not available for primary profile label");

        editProfilePage
                .nameOfEditProfile(ProfileConstant.DARTH_VADER);
        editProfilePage
                .clickOnGenderBtn()
                .waitForSeconds(1);
        softAssert
                .assertTrue(editProfilePage.isUpdateConfirmationMsgVisible(), "Expected to see a flash notification message for profile name update");

        editProfilePage
                .clickOnGenderDropdown()
                .selectGender();
        editProfilePage
                .clickOnEditGenderSaveBtn()
                .waitForSeconds(1);
        softAssert
                .assertTrue(editProfilePage.isUpdateConfirmationMsgVisible(), "Expected to see a flash notification message for gender update");

        editProfilePage
                .clickOnAvatarImage();
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.SELECT_AVATAR), "Expected to be on select-avatar page");
        softAssert
                .assertTrue(editProfilePage.isAvatarVisible(), "Expected to see current avatar in the select-avatar page");
        softAssert
                .assertTrue(disneyPlusAddProfilePage.verifyUsedAvatarIsNotVisible(avatarName), "Expected to see that the used avatar is not available in select-avatar page");
        softAssert
                .assertTrue(disneyPlusAddProfilePage.verifySelectAvatarPageVerticalScroll(), "Expected to see that select-avatar page is scrollable vertically to view all avatars");
        softAssert
                .assertTrue(disneyPlusAddProfilePage.verifySelectAvatarPageHorizontalScroll(), "Expected to see that select-avatar page is scrollable horizontally to view all avatars");

        String avatar = disneyPlusAddProfilePage.getAvatarName(0);

        disneyPlusAddProfilePage
                .selectAvatarForProfile();
        waitForSeconds(1);
        softAssert
                .assertTrue(disneyPlusAddProfilePage.getSelectedAvatarName().equalsIgnoreCase(avatar), "Expected to see that the avatar selected in select-avatar page is showing up in edit-profile page");
        softAssert
                .assertTrue(editProfilePage.getEditProfileDOBValue().equals(editProfilePage.getEditProfileDOBValue()), "Expected to see that the DOB value does not change after avatar update");//Bug-WEB-5930
        softAssert
                .assertTrue(editProfilePage.getGender().equals(editProfilePage.getGender()), "Expected to see that the updated Gender value does not change after avatar update");//Bug-WEB-5930
        softAssert
                .assertTrue(editProfilePage.getEditProfileNameFieldValue().equals(ProfileConstant.DARTH_VADER), "Expected to see that the updated profile name value does not change after avatar update");//Bug-WEB-5930

        editProfilePage
                .clickOnEditProfileDoneBtn();
        editProfilePage
                .getActiveProfile();
        editProfilePage
                .clickOnEditProfile();
        waitForSeconds(3);
        softAssert
                .assertTrue(editProfilePage.getPrimaryProfileName().equals(ProfileConstant.DARTH_VADER_INCOMPLETE), "Expected to see edit-profiles page displays updated profile name");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18209"})
    @Test(description = "Test app language switch", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyAppLang() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editProfilePage.getActiveProfile();
        editProfilePage.clickOnEditProfile();
        editProfilePage.selectProfileName(ProfileConstant.MAIN_TEST);

        softAssert.assertTrue(editProfilePage.isLangDisplayedLocalized(locale),
                "Expected - Language should be displayed in localized version");

        editProfilePage.getDropdown();

        softAssert.assertTrue(editProfilePage.verifyDropdownItemsPresent(editProfilePage.getAppLangDropdownList()),
                "Expected 'App Lang Dropdown Items' to be in order, please check logs for missing items");

        editProfilePage.pickRandomLanguage().clickOnEditProfileDoneButton();
        disneyAccount.set(getAccountApi().createAccount("Yearly", editProfilePage.getRandomLocale(), editProfilePage.getRandomLanguage(), SUB_VERSION_V1));
        editProfilePage.getFullDictionary(account.getProfileLang());
        editProfilePage.clickOnEditProfilesDoneButton();
        editProfilePage.selectProfileName(ProfileConstant.MAIN_TEST);

        softAssert.assertTrue(new DisneyPlusBaseProfileViewsPage(getDriver()).isOpened(),
                "Expected 'Base profile view' page to be opened");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31250", "XWEBQAS-31243"})
    @Test(description = "Verify DOB field is not editable", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US"})
    public void verifyDOBNotEditable() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editProfilePage
                .getActiveProfile();
        editProfilePage
                .clickOnEditProfile();
        editProfilePage
                .selectProfileName(ProfileConstant.MAIN_TEST);

        softAssert
                .assertTrue(editProfilePage.verifyDOBFieldNotEditable(), "Expected that the DOB field and its value is disabled");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31248", "XWEBQAS-31251", "XWEBQAS-31351"})
    @Test(description = "Verify Edit gender page", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US"})
    public void verifyEditGender() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editProfilePage
                .getActiveProfile();
        editProfilePage
                .clickOnEditProfile();
        editProfilePage
                .selectProfileName(ProfileConstant.MAIN_TEST)
                .clickOnGenderBtn();
        editProfilePage.verifyUrlText(softAssert, PageUrl.GENDER_IDENTITY);

        String gender = editProfilePage.getEditGenderDropdownValue();

        editProfilePage
                .clickOnEditGenderCancelBtn();
        softAssert
                .assertTrue(editProfilePage.getGender().equalsIgnoreCase(gender), "Expected to see gender value same as edit gender page");

        editProfilePage
                .clickOnGenderBtn()
                .clickOnGenderDropdown();
        softAssert
                .assertTrue(editProfilePage.isGenderDropdownOptionsVisible(), "Expected to see all the 4 options in Gender dropdown");

        editProfilePage
                .clickOnGenderClearBtn();
        softAssert
                .assertTrue(editProfilePage.isGenderFieldRequiredErrorVisible(), "Expected to see error message when Gender dropdown is empty");

        editProfilePage
                .clickOnGenderDropdown()
                .selectGender();
        gender = editProfilePage.getEditGenderDropdownValue();
        editProfilePage
                .clickOnEditGenderSaveBtn();
        softAssert
                .assertTrue(editProfilePage.isUpdateConfirmationMsgVisible(), "Expected to see a confirmation message for successful update");
        softAssert
                .assertTrue(editProfilePage.getGender().equalsIgnoreCase(gender), "Expected to see that gender in edit profile page is same as new updated value");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31247", "XWEBQAS-31242"})
    @Test(description = "Update Profile modal does not show up for already authenticated users", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US"})
    public void verifyProfileModalAuthUser() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        String baseUrl = editProfilePage.getHomeUrl();

        editProfilePage.openURL(baseUrl.concat(PageUrl.COMPLETE_ACCOUNT_INFO));
        waitForSeconds(20);//Need to reduce the waitTime when the test is able to run successfully in Prod

        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(baseUrl.concat(PageUrl.DISNEYPLUS_HOME)), "Expected to see that page is redirected to Home page");

        editProfilePage.openURL(baseUrl.concat("/update-profile/5"));
        waitForSeconds(20);//Need to reduce the waitTime when the test is able to run successfully in Prod

        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(baseUrl.concat("/").concat(PageUrl.SELECT_PROFILE)), "Expected to see that page is redirected to select profile page");

        editProfilePage.openURL(baseUrl.concat(PageUrl.UPDATE_PROFILE_PRIMARY));
        waitForSeconds(10);//Need to reduce the waitTime when the test is able to run successfully in Prod

        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(baseUrl.concat(PageUrl.DISNEYPLUS_HOME)), "Expected to see that page stays on update profile page");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31246", "XWEBQAS-31243", "XWEBQAS-31255", "XWEBQAS-31242", "XWEBQAS-31233", "XWEBQAS-31354", "XWEBQAS-31376"})
    @Test(description = "Subscriber drops or re-loads date of birth screen", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US"})
    public void verifyDOBAndProfileModalDropAndReload() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());
        DisneyPlusCommercePage disneyPlusCommercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount(AccountUtils.createAccountRequest(locale, language, SUB_VERSION_V1, null, null)));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        String baseUrl = editProfilePage.getHomeUrl();

        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.COMPLETE_ACCOUNT_INFO), "Expected to see complete-account-info page upon login");
        softAssert
                .assertTrue(editProfilePage.isDisneyHelpLinkPresent(), "Expected to see Disney Help Centre link");

        editProfilePage
                .refresh(10);//Need to reduce this once the test is able to run successfully in Prod
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.COMPLETE_ACCOUNT_INFO), "Expected to see complete-account-info page after page refresh");

        editProfilePage
                .navigateBack();
        waitForSeconds(10);
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.COMPLETE_ACCOUNT_INFO), "Expected to stay on complete-account-info page after clicking on back button");
        softAssert
                .assertTrue(editProfilePage.verifyProfileLogoutBtnIsVisible(), "Expected to see Logout button in Complete-account-info page");
        
        editProfilePage
                .openURL(baseUrl.concat(PageUrl.COMPLETE_ACCOUNT_INFO));//Deeplink to verify for Mobile
        if (isMobile()) {
            softAssert
                    .assertTrue(disneyPlusCommercePage.isGetAppPagePresent(), "Expected to see get app page if its mobile");
            disneyPlusCommercePage
                    .verifyUrlText(softAssert, PageUrl.GET_APP_URL);
        }

        editProfilePage
                .clickOnProfileLogoutBtn()
                .waitForSeconds(3);

        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.COMPLETE_ACCOUNT_INFO), "Expected to see complete-account-info page upon login");

        editProfilePage
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB);
        editProfilePage
                .clickOnConfirmBtn()
                .waitForSeconds(5);//Need to reduce this once the test is able to run successfully in Prod
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.UPDATE_PROFILE), "Expected to see update-profile page after page refresh");

        editProfilePage
                .refresh(10);//Need to reduce this once the test is able to run successfully in Prod
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.UPDATE_PROFILE), "Expected to see update-profile page after page refresh");

        editProfilePage
                .navigateBack();
        waitForSeconds(5);
        editProfilePage
                .refresh(10);//Need to reduce this once the test is able to run successfully in Prod
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.UPDATE_PROFILE), "Expected to stay on same page after browser back button is clicked");

        editProfilePage
                .openURL(baseUrl.concat(PageUrl.UPDATE_PROFILE_PRIMARY));//Deeplink
        waitForSeconds(5);
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.UPDATE_PROFILE), "Expected to see update-profile page after deeplink");

        editProfilePage
                .deleteCookies();
        editProfilePage
                .openURL(baseUrl);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        waitForSeconds(10);
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.UPDATE_PROFILE), "Expected to stay on same page after re-login");
      
        editProfilePage
                .openURL(baseUrl.concat(PageUrl.DISNEYPLUS_HOME));//Deeplink
        waitForSeconds(10);
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.UPDATE_PROFILE), "Expected to stay on same page after deeplink");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31245", "XWEBQAS-31243", "XWEBQAS-31354", "XWEBQAS-31254", "XWEBQAS-31353", "XWEBQAS-31352"})
    @Test(description = "Subscriber completes date of birth screen and profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US", "ZW"})
    public void verifyPrimaryProfileUpdate() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount(AccountUtils.createAccountRequest(locale, language, SUB_VERSION_V1, null, null)));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB);

        String dateOfBirth = editProfilePage.getDOBValue();

        editProfilePage
                .clickOnConfirmBtn()
                .waitForSeconds(5);
        editProfilePage
                .verifyUrlText(softAssert, PageUrl.UPDATE_PROFILE);
        softAssert
                .assertTrue(editProfilePage.getDOBValue().equals(appExUtil.formatDate("MM / dd / yyyy", dateOfBirth, "MMMM d, yyyy")), "Expected to see that the DOB is retained from the add DOB page");

        editProfilePage
                .clickOnSaveButton()
                .waitForSeconds(3);
        softAssert
                .assertTrue(editProfilePage.isUpdateProfileGenderErrorVisible(), "Expected to see field required error message for missing gender");

        editProfilePage
                .clearProfileNameField()
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton()
                .waitForSeconds(3);
        softAssert
                .assertTrue(editProfilePage.getProfileNameErrorLabelValue().equals(WebConstant.FIELD_REQUIRED_ERROR_LABEL),
                        "Expected to see field required error message for missing profile name");

        String gender = editProfilePage.getProfileGenderValue();

        editProfilePage
                .enterProfileName(ProfileConstant.MAIN_TEST)
                .clickOnSaveButton()
                .waitForSeconds(5);//Need to reduce this once the test is able to run successfully in Prod
        editProfilePage
                .verifyUrlText(softAssert, PageUrl.DISNEYPLUS_HOME);
        editProfilePage
                .getActiveProfile();
        editProfilePage
                .clickOnEditProfile()
                .clickOnPrimaryProfileBtn();
        softAssert
                .assertTrue(editProfilePage.getGender().equalsIgnoreCase(gender), "Expected to see that the gender selection during profile update matches in edit profile page");
        softAssert
                .assertTrue(editProfilePage.getEditProfileDOBValue().equals(appExUtil.formatDate("MM / dd / yyyy", dateOfBirth, "MMMM d, yyyy")), "Expected to see that the date of birth specified in add-dob page matches with edit profile page");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31244", "XWEBQAS-31357"})
    @Test(description = "Verify negative flows in DOB page", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US"})
    public void verifyDOBNegativeFlows() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount(AccountUtils.createAccountRequest(locale, language, SUB_VERSION_V1, null, null)));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editProfilePage
                .clickOnConfirmBtn();
        softAssert
                .assertTrue(editProfilePage.getDOBInputErrorLabelValue().equals(WebConstant.INVALID_BIRTHDATE), "Expected to see error message displayed when invalid date of birth is specified");

        editProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.INELIGIBLE_DOB);
        editProfilePage
                .clickOnConfirmBtn();
        softAssert
                .assertTrue(editProfilePage.getDOBInputErrorLabelValue().equals(WebConstant.INVALID_BIRTHDATE), "Expected to see error message displayed when ineligible date of birth is specified");

        editProfilePage
                .refresh(20);
        editProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.INVALID_DOB);
        editProfilePage
                .clickOnConfirmBtn();
        softAssert
                .assertTrue(editProfilePage.isConfirmWithPasswordModalVisible(), "Expected to see Confirm with password modal");

        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        waitForSeconds(3);
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.ACCOUNT_BLOCKED), "Expected to see account-blocked page");
        softAssert
                .assertTrue(editProfilePage.getNotEligibleToUseServiceText().equals(WebConstant.NOT_ELIGIBLE_TO_USE_SERVICE), "Expected to see error message displayed when invalid date of birth is specified");

        editProfilePage
                .clickOnDismissBtn()
                .waitForSeconds(5);
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(editProfilePage.getHomeUrl()), "Expected to see base page is displayed");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31350", "XWEBQAS-31354", "XWEBQAS-31352"})
    @Test(description = "Verify DOB & Gender fields states and combinations", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US"})
    public void verifyDOBAndGenderCombinations() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.PROFILE, language, null, false);
        getAccountApi().addProfile(account, ProfileConstant.PROFILE1, language, null, false);

        String profileID = account.getProfiles().get(2).getProfileId();
        getAccountApi().patchProfileAge(account, "2001-01-01", profileID);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        editProfilePage
                .selectProfileName(ProfileConstant.PROFILE)
                .waitForSeconds(5);
        editProfilePage
                .navigateBack();
        waitForSeconds(5);
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.SELECT_PROFILE),
                        "Expected to see select-profile page");

        editProfilePage
                .clickOnEditProfileFromPage();
        editProfilePage
                .selectProfileName(ProfileConstant.PROFILE);
        softAssert
                .assertFalse(editProfilePage.isEditProfileDOBFieldPresent(), "Expected that the DOB field is not present");
        softAssert
                .assertFalse(editProfilePage.isEditProfileGenderFieldPresent(), "Expected that the Gender field is not present");

        editProfilePage
                .clickOnEditProfileDoneBtn()
                .selectProfileName(ProfileConstant.PROFILE1);
        softAssert
                .assertTrue(editProfilePage.isEditProfileDOBFieldValuePresent(), "Expected that the DOB field value is present");
        softAssert
                .assertTrue(editProfilePage.isEditProfileGenderFieldPresent(), "Expected that the Gender field is present");
        softAssert
                .assertFalse(editProfilePage.isEditProfileGenderFieldValuePresent(), "Expected that the Gender field value is not present");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31380", "XWEBQAS-31379"})
    @Test(description = "Verify negative flows for DOB for secondary profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US"})
    public void verifyDOBNegativeFlowsForSecondaryProfile() {
            SoftAssert softAssert = new SoftAssert();

            DisneyAccountApi accountApi = getAccountApi();
            DisneyAccount account = accountApi.createEntitledAccount(locale, language);
            account = accountApi.addFlex(account);
            disneyAccount.set(account);

            DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
            DisneyPlusJuniorModePage juniorModePage = new DisneyPlusJuniorModePage(getDriver());
            DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

            getAccountApi().addProfile(account, ProfileConstant.PROFILE, language, null, false);

            appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
            appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

            juniorModePage
                    .selectProfileToEditFromEditProfilePage(ProfileConstant.PROFILE);
            softAssert
                    .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.UPDATE_PROFILE), "Expected to see update profile page");

            editProfilePage
                    .navigateBack();
            waitForSeconds(5);
            softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.SELECT_PROFILE), "Expected to see select profile page");

            juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.PROFILE);
            waitForSeconds(3);
            editProfilePage
                    .clickOnDOBField()
                    .enterDOB(ProfileEligibility.INELIGIBLE_DOB)
                    .clickOnGenderDropdown()
                    .selectGender()
                    .clickOnSaveButton();
            softAssert
                    .assertTrue(editProfilePage.getDOBInputErrorLabelValue().equals(WebConstant.INVALID_BIRTHDATE), "Expected to see error message displayed when ineligible date of birth is specified");

            softAssert.assertAll();
        }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31247", "XWEBQAS-31242"})
    @Test(description = "Update Profile modal does not show up for secondary profiles with DOB & Gender", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US"})
    public void verifyProfileModalForAuthSecondaryProfiles() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusJuniorModePage juniorModePage = new DisneyPlusJuniorModePage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.PROFILE, language, null, false);

        String profileID = account.getProfiles().get(1).getProfileId();
        getAccountApi().patchProfileAge(account, "2001-01-01", profileID);
        getAccountApi().patchProfileGender(account, "Woman", profileID);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        String baseUrl = appExUtil.getHomeUrl();

        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.PROFILE);
        waitForSeconds(5);
        softAssert
                .assertTrue(appExUtil.getCurrentUrl().contains(baseUrl.concat(PageUrl.DISNEYPLUS_HOME)), "Expected to see that page is redirected to Home page");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31357"})
    @Test(description = "Verify minor profile block for main profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US"})
    public void verifyMinorBlockMainProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount(AccountUtils.createAccountRequest(locale, language, SUB_VERSION_V1, null, null)));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.INVALID_DOB);
        editProfilePage
                .clickOnConfirmBtn();
        waitForSeconds(5);
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.ACCOUNT_BLOCKED), "Expected to see account-blocked page");

        editProfilePage
                .clickOnHelpCentreBtn()
                .waitForSeconds(5);
        appExUtil
                .switchWindow();
        softAssert
                .assertTrue(editProfilePage.getCurrentUrl().contains(PageUrl.DISNEY_PLUS_HELP_CENTER), "Expected to see disney plus help page");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31377"})
    @Test(description = "Verify GroupWatch disabled for account with ads", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.GROUPWATCH, "US"})
    public void verifyGroupWatchNotAvailable() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount(STANDALONE_MONTHLY_ADS, locale, language, SUB_VERSION_V2_ORDER));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        editProfilePage
                .getActiveProfile();
        editProfilePage
                .clickOnEditProfile()
                .clickOnPrimaryProfileBtn();
        softAssert
                .assertTrue(editProfilePage.isGroupWatchToggleDisabled(), "Expected to see GroupWatch toggle disabled");

        softAssert.assertAll();
    }
}
