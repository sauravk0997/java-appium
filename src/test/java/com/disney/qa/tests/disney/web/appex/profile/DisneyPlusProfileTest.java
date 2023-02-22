package com.disney.qa.tests.disney.web.appex.profile;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusDefaultProfilePage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusEditProfilePage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusJuniorModePage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.parentalcontrols.DisneyPlusSetProfilePinPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusCreateProfilePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.AccountUtils;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;

public class DisneyPlusProfileTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18625"})
    @Test(description = "Create Profile", priority = 1, groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, TestGroup.PROFILES,
            TestGroup.ARIEL_APPEX, "US", "MX", "JP"})
    public void verifyCreateProfileFlow() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1515"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusCreateProfilePage createProfilePage = new DisneyPlusCreateProfilePage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        disneyAccount.set(account);
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        createProfilePage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        createProfilePage
                .selectAvatarForProfile();
        analyticPause();
        createProfilePage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        createProfilePage
                .addProfileName(ProfileConstant.PROFILE1);
        createProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        createProfilePage
                .verifyNumberOfProfileForNewProfileCreation(softAssert, PageUrl.SELECT_PROFILE);

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    /* Temporary addition for S+ until logic is implemented to have one test for D+ & S+ */
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18625"})
    @Test(description = "Create Profile for MX", priority = 1, groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, TestGroup.PROFILES,
            "US", "MX", "JP"})
    public void verifyCreateProfileFlowMX() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1515"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusCreateProfilePage createProfilePage = new DisneyPlusCreateProfilePage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        disneyAccount.set(account);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        createProfilePage.clickOnAddProfileFromAccount();
        createProfilePage.verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        createProfilePage.selectAvatarForProfile();
        createProfilePage.verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        createProfilePage.addProfileName(ProfileConstant.PROFILE1);
        createProfilePage.clickOnSaveButton();

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());
        createProfilePage.verifyNumberOfProfileForNewProfileCreation(softAssert, PageUrl.SELECT_PROFILE);
        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-27711"})
    @Test(description = "Verify Junior Mode", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, TestGroup.PROFILES,
            "US", "MX", "JP"})
    public void verifyJuniorToggle() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1516"));
        skipTestByProjectLocale(locale, new ArrayList<>(Arrays.asList("MX")), EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusJuniorModePage juniorModePage = new DisneyPlusJuniorModePage(getDriver());
        DisneyPlusCreateProfilePage createProfilePage = new DisneyPlusCreateProfilePage(getDriver());

        accountApi.addProfile(account, ProfileConstant.PROFILE1, language, null, false);
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        juniorModePage
                .isEditProfileFromPagePresent();
        juniorModePage
                .clickOnEditProfileFromPage();
        createProfilePage
                .selectProfile(ProfileConstant.PROFILE1);
        softAssert
                .assertTrue(juniorModePage.verifyJuniorToggleStatus("false"), "Expected 'Kids Toggled off' by default");

        juniorModePage
                .clickOnEditProfileDoneButton();
        juniorModePage
                .selectProfileToEditFromEditProfilePage(account.getFirstName());
        softAssert
                .assertFalse(juniorModePage.verifyJuniorProfileOptionUnavailable(), "Expected 'Kid's Profile' to not exist");

        juniorModePage
                .clickOnEditProfileDoneButton();
        createProfilePage
                .clickOnSecondaryProfileOnEditPage();
        juniorModePage
                .clickOnJuniorModeToggle()
                .clickOnEditProfileDoneButton();
        juniorModePage
                .clickOnEditProfilesDoneButton()
                .clickOnEditProfileFromPage();
        pause(3);
        createProfilePage
                .selectProfile(ProfileConstant.PROFILE1);

        softAssert.assertTrue(juniorModePage.verifyJuniorToggleStatus("true"), "Expected 'Kids Toggled on'");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-21887", "XWEBQAS-31380", "XWEBQAS-31235", "XWEBQAS-31383", "XWEBQAS-31382"})
    @Test(description = "Verify Junior Profile Set", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.STAR_APPEX, TestGroup.DISNEY_SMOKE, TestGroup.PROFILES,
            TestGroup.ARIEL_APPEX, "US", "MX", "JP"})
    public void verifyJuniorProfileSet() {
        skipTestByEnv(QA);
        
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1517"));
        skipTestByProjectLocale(locale, new ArrayList<>(Arrays.asList("MX")), EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusJuniorModePage juniorModePage = new DisneyPlusJuniorModePage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());
        DisneyPlusEditProfilePage editProfilePage = new DisneyPlusEditProfilePage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.JUNIOR, language, null, true);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.JUNIOR);
        softAssert
                .assertTrue(juniorModePage.getCurrentUrl().contains(PageUrl.UPDATE_PROFILE), "Expected to see update-profile page");
        softAssert
                .assertTrue(editProfilePage.isGenderDropdownEnabled(),
                        "Expected to see gender dropdown as enabled before DOB is specified");

        juniorModePage
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_THIRTEEN_DOB);
        softAssert
                .assertTrue(editProfilePage.isGenderDropdownDisabled(),
                        "Expected to see gender dropdown as disabled after DOB is specified");

        juniorModePage
                .clickOnSaveButton();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        softAssert
                .assertTrue(disneyPlusSetProfilePinPage.isContinueButtonPresent(),
                        "Expected to see password continue button");

        disneyPlusSetProfilePinPage
                .clickOnContinue();
        waitForSeconds(1);
        softAssert
                .assertTrue(disneyPlusSetProfilePinPage.isAgreeBtnPresent(),
                        "Expected to see agree button on the consent form");
        juniorModePage
                .clickOnAgreeBtn();
        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.JUNIOR);
        analyticPause();
        softAssert
                .assertTrue(juniorModePage.verifyKidProfileTile(0, ProfileConstant.JUNIOR),
                        "Expected Kids Tile present");
        analyticPause();

        juniorModePage
                .getActiveProfile();
        juniorModePage
                .clickOnExitJuniorMode()
                .clickOnEditProfileFromPage();
        editProfilePage
                .selectProfileName(ProfileConstant.JUNIOR);
        softAssert
                .assertFalse(editProfilePage.isEditProfileGenderFieldPresent(), "Expected to not see Gender field for kids profile");
        softAssert
                .assertTrue(juniorModePage.isTextPresentInDivElement(WebConstant.JUNIOR_MODE), "Expected to see kid proof exit message for junior mode");

        juniorModePage
                .clickOnKidProofExitToggle()
                .clickOnLearnMoreLink();
        juniorModePage
                .switchWindow();
        waitForSeconds(5);
        softAssert
                .assertTrue(juniorModePage.getCurrentUrl().contains(PageUrl.DISNEY_PLUS_HELP_CENTER), "Expected to see page for Disney Plus help centre");

        juniorModePage
                .switchWindow();
        juniorModePage
                .clickOnJuniorModeToggle();
        waitForSeconds(1);
        softAssert
                .assertTrue(juniorModePage.isTurnOffJuniorModeTextPresent(WebConstant.JUNIOR_MODE), "Expected to see turn off junior mode text on password pop up");

        disneyPlusSetProfilePinPage
                .clickOnPasswordCancelBtn();
        editProfilePage
                .clickOnEditProfileDoneBtn()
                .clickOnEditProfilesDoneButton();
        editProfilePage
                .selectProfileName(ProfileConstant.JUNIOR);
        editProfilePage
                .getActiveProfile();
        juniorModePage
                .clickOnExitJuniorMode();
        softAssert
                .assertTrue(juniorModePage.isTextPresentInParaElement(WebConstant.JUNIOR_MODE), "Expected to see kid proof exit label with text Junior Mode");

        analyticPause();
        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    //jira: https://jira.disneystreaming.com/browse/QAA-7837
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-27299"})
    @Test(description = "Verify Set Profile Pin", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.DISNEY_SMOKE, TestGroup.PROFILES,
            TestGroup.ARIEL_APPEX, "US", "MX", "JP"})
    public void verifySetProfilePin() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1518"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusSetProfilePinPage profilePinPage = new DisneyPlusSetProfilePinPage(getDriver());
        DisneyPlusJuniorModePage juniorModePage = new DisneyPlusJuniorModePage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.PROFILE1, language, null, true);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        analyticPause();

        profilePinPage.isEditProfileFromPagePresent();

        analyticPause();

        profilePinPage.clickOnEditProfileFromPage();
        profilePinPage.verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);

        juniorModePage.selectProfileToEditFromEditProfilePage(ProfileConstant.PROFILE1);
        profilePinPage.clickOnProfilePin();
        profilePinPage.enterPassword(account.getUserPass());
        profilePinPage.clickOnContinue();
        pause(5); //ensure loading for below Url verification
        profilePinPage.verifyUrlText(softAssert, PageUrl.MANAGE_PIN);
        profilePinPage.clickOnCheckboxToLimitAccess();
        String pin = profilePinPage.enterProfilePin(0, "4567");
        profilePinPage.clickSavePinButton();

        softAssert.assertTrue(profilePinPage.isEnterPinErrorPresent(), "Enter pin input error not present ");

        profilePinPage.clickOnEditProfileDoneButton();

        softAssert.assertTrue(profilePinPage.isLockForProfilePresent(), "Lock not present under edited profile, profile pin was not set");

        profilePinPage.clickOnEditProfilesDoneButton();
        juniorModePage.selectProfileToEditFromEditProfilePage(ProfileConstant.PROFILE1);
        profilePinPage.enterProfilePin(0, pin);

        analyticPause();
        juniorModePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();

        softAssert.assertTrue(profilePinPage.getActiveProfile().getText().equals(ProfileConstant.PROFILE1), "Expected profile to be new profile");

        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    /* Temporary addition for S+ until logic is implemented to have one test for D+ & S+ */
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-27299"})
    @Test(description = "Verify Set Profile Pin for MX", groups = {TestGroup.STAR_SMOKE, TestGroup.DISNEY_APPEX, TestGroup.DISNEY_SMOKE, TestGroup.PROFILES,
            "US", "MX", "JP"})
    public void verifySetProfilePinMX() {
        skipTestByEnv(QA);

        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1518"));
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusSetProfilePinPage profilePinPage = new DisneyPlusSetProfilePinPage(getDriver());
        DisneyPlusJuniorModePage juniorModePage = new DisneyPlusJuniorModePage(getDriver());

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        accountApi.addProfile(account, ProfileConstant.PROFILE1, language, null, false);
        disneyAccount.set(account);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        analyticPause();

        profilePinPage
                .isEditProfileFromPagePresent();
        analyticPause();
        profilePinPage
                .clickOnEditProfileFromPage();
        profilePinPage
                .verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);
        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.PROFILE1);
        profilePinPage
                .clickOnProfilePin();
        profilePinPage
                .enterPassword(account.getUserPass());
        profilePinPage
                .clickOnContinue();
        pause(5); //ensure loading for below Url verification
        profilePinPage
                .verifyUrlText(softAssert, PageUrl.MANAGE_PIN);
        profilePinPage
                .clickOnCheckboxToLimitAccess();
        String pin = profilePinPage.enterProfilePin(0, "4567");
        profilePinPage
                .clickSavePinButton();
        softAssert
                .assertTrue(profilePinPage.isEnterPinErrorPresent(), "Enter pin input error not present ");
        profilePinPage
                .clickOnEditProfileDoneButton();
        softAssert
                .assertTrue(profilePinPage.isLockForProfilePresent(), "Lock not present under edited profile, profile pin was not set");
        profilePinPage
                .clickOnEditProfilesDoneButton();
        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.PROFILE1);
        profilePinPage
                .enterProfilePin(0, pin);
        analyticPause();
        softAssert.assertTrue(profilePinPage.getActiveProfile().getText().equals(ProfileConstant.PROFILE1), "Expected profile to be new profile");
        // Sends the logs to the validator and then adds the results to the soft assert
        checkAssertions(softAssert);
    }

    @Test(description = "verify Adult Profile shows DOB page", priority = 1, groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "US"})
    public void verifyProfileLoginDOB() {
        skipTestByEnv(QA);

        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusDefaultProfilePage profilePage = new DisneyPlusDefaultProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount(AccountUtils.createAccountRequest(locale, language, SUB_VERSION_V1, null, null)));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profilePage.verifyUrlText(softAssert, PageUrl.COMPLETE_ACCOUNT_INFO);

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-21887"})
    @Test(description = "Verify character category tiles in Junior Mode", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, "US", "MX"})
    public void verifyCharacterCategoryTiles() {
        skipTestByEnv(QA);
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusJuniorModePage juniorModePage = new DisneyPlusJuniorModePage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.JUNIOR, language, null, true);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.JUNIOR);
        juniorModePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_THIRTEEN_DOB)
                .clickOnSaveButton();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        waitForSeconds(1);
        juniorModePage
                .clickOnAgreeBtn();
        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.JUNIOR);
        softAssert
                .assertTrue(juniorModePage.getCharacterCategoryTilesCount() == 6, "Expected to see 6 character category tiles");
        softAssert
                .assertTrue(juniorModePage.verifyCharacterCategoryTileName(), "Expected to see all character category tiles");
        softAssert
                .assertTrue(juniorModePage.verifyTilesCollectionPageURL(), "Expected to see corresponding character category tile name in url");

        softAssert.assertAll();
    }

}
