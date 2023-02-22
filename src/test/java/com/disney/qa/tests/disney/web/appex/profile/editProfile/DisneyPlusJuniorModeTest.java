package com.disney.qa.tests.disney.web.appex.profile.editProfile;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.homepage.DisneyPlusHomePageSearchPage;
import com.disney.qa.disney.web.appex.parentalcontrols.DisneyPlusSetProfilePinPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAddProfilePage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusJuniorModePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.*;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class DisneyPlusJuniorModeTest extends DisneyPlusBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        LOGGER.debug("Init JuniorModePage... ");
        apiProvider.set(new DisneyApiProvider());
        disneyAccount.set(new DisneyAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18221"})
    @Test(description = "Verify Junior Mode Toggle not present for Primary Profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyJuniorModeToggleNotPresentForPrimaryProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusJuniorModePage juniorModePage = new DisneyPlusJuniorModePage(getDriver());

        DisneyAccount disneyAccount = getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1);
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.getEmail(), disneyAccount.getUserPass());

        juniorModePage.getActiveProfile();
        juniorModePage.clickOnEditProfile();
        juniorModePage.verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);
        juniorModePage.selectProfileToEditFromEditProfilePage(ProfileConstant.MAIN_TEST);
        juniorModePage.verifyUrlText(softAssert, "edit-profile");

        softAssert.assertTrue(juniorModePage.getPrimaryProfileText().equals(ProfileConstant.MAIN_TEST),
                String.format("Expected primary profile name (%s) to match actual primary profile name on page (%s)",
                        ProfileConstant.MAIN_TEST, juniorModePage.getPrimaryProfileText()));
        softAssert.assertFalse(juniorModePage.verifyJuniorProfileOptionUnavailable(), "Expected 'Kid's Profile' to not exist");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-18219", "XWEBQAS-31382"})
    @Test(description = "Verify Junior Mode",
            groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, "AG", "AI", "AR", "AT", "AU", "AW", "BB", "BE", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CH", "CL", "CO", "CR", "CW", "DE", "DK", "DM", "DO", "EC", "ES", "FI", "FK", "FR", "GB", "GD", "GF", "GG", "GL", "GP", "GS", "GT", "GY", "HN", "HT", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KR", "KN", "KY", "LC", "LU", "MC", "MF", "MQ", "MS", "MU", "MX", "NC", "NI", "NL", "NO", "NZ", "PA", "PE", "PR", "PT", "PY", "RE", "SE", "SG", "SR", "SV", "TC", "TT", "US", "UY", "VC", "VE", "VG", "VI", "WF", "YT"})
    public void verifyJuniorModeToggle() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusJuniorModePage juniorModePage = new DisneyPlusJuniorModePage(getDriver());

        accountApi.addProfile(account, ProfileConstant.PROFILE, language, null, false);
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        juniorModePage
                .clickOnEditProfileFromPage();
        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.PROFILE);
        softAssert
                .assertTrue(juniorModePage.verifyJuniorToggleStatus("false"), "Expected 'Kids Toggled off' by default");

        juniorModePage
                .clickOnEditProfileDoneButton();
        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.MAIN_TEST);
        softAssert
                .assertFalse(juniorModePage.verifyJuniorProfileOptionUnavailable(), "Expected 'Kid's Profile' to not exist");

        juniorModePage
                .clickOnEditProfileDoneButton();
        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.PROFILE);
        juniorModePage
                .clickOnJuniorModeToggle()
                .clickOnEditProfileDoneButton();
        juniorModePage
                .clickOnEditProfilesDoneButton()
                .clickOnEditProfileFromPage();
        pause(5);
        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.PROFILE);
        softAssert
                .assertTrue(juniorModePage.verifyJuniorToggleStatus("true"), "Expected 'Kids Toggled on'");

        juniorModePage
                .clickOnJuniorModeToggle();


        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-27713", "XWEBQAS-31386"})
    @Test(description = "Verify adult content is not available in Junior profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "MX", "US"})
    public void verifyAdultContentInJuniorProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusHomePageSearchPage disneyPlusHomePageSearchPage = new DisneyPlusHomePageSearchPage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.JUNIOR, language, null, true);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        addProfilePage
                .selectProfile(ProfileConstant.JUNIOR);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_THIRTEEN_DOB)
                .clickOnSaveButton();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        addProfilePage
                .clickOnAgreeBtn();
        addProfilePage
                .selectProfile(ProfileConstant.JUNIOR);
        disneyPlusHomePageSearchPage
                .clickOnSearchBar()
                .searchFor(Contents.MANDALORIAN);
        softAssert
                .assertFalse(disneyPlusHomePageSearchPage.verifySearchListContainsTitle(Contents.MANDALORIAN), "Expected to not see adult content title");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31379", "XWEBQAS-31386"})
    @Test(description = "Verify minor authentication for existing profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "MX", "US"})
    public void verifyExistingProfileMinorAuthentication() {
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

        String baseUrl = juniorModePage.getHomeUrl();

        juniorModePage
                .selectProfileToEditFromEditProfilePage(ProfileConstant.JUNIOR);
        waitForSeconds(3);
        juniorModePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_THIRTEEN_DOB)
                .clickOnSaveButton();
        disneyPlusSetProfilePinPage
                .enterPassword("Wrong password");
        softAssert
                .assertTrue(disneyPlusSetProfilePinPage.isContinueButtonPresent(),
                        "Expected to see password continue button");
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        softAssert
                .assertFalse(disneyPlusSetProfilePinPage.isEnterPinErrorPresent(),
                        "Expected to see error message for wrong password");

        disneyPlusSetProfilePinPage
                .clickOnPasswordCancelBtn();
        juniorModePage
                .clickOnSaveButton();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        waitForSeconds(2);

        softAssert
                .assertTrue(juniorModePage.getCurrentUrl().contains(PageUrl.PARENTAL_CONSENT), "Expected to see parental consent page");

        juniorModePage
                .openURL(baseUrl.concat(PageUrl.DISNEYPLUS_HOME));
        waitForSeconds(20);
        softAssert
                .assertTrue(juniorModePage.getCurrentUrl().contains(PageUrl.PARENTAL_CONSENT), "Expected to stay on same page");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31381", "XWEBQAS-31382"})
    @Test(description = "Verify minor consent for add profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "MX", "US"})
    public void verifyAddProfileMinorConsent() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        String baseUrl = addProfilePage.getHomeUrl();

        addProfilePage
                .getActiveProfile();
        addProfilePage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        addProfilePage
                .selectAvatarForProfile();
        addProfilePage
                .addProfileName(ProfileConstant.PROFILE1);
        softAssert
                .assertTrue(addProfilePage.isJuniorModeVisible(), "Expected to see Junior Mode label displayed");

        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_THIRTEEN_DOB)
                .clickOnSaveButton();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        waitForSeconds(2);
        softAssert
                .assertTrue(addProfilePage.getCurrentUrl().contains(PageUrl.PARENTAL_CONSENT), "Expected to see parental consent page");
        softAssert
                .assertTrue(addProfilePage.isAgreeBtnPresent(), "Expected to see Agree CTA button");
        softAssert
                .assertTrue(addProfilePage.isDeclineBtnPresent(), "Expected to see Decline CTA button");

        addProfilePage
                .openURL(baseUrl.concat(PageUrl.DISNEYPLUS_HOME));
        waitForSeconds(20);
        softAssert
                .assertTrue(addProfilePage.getCurrentUrl().contains(PageUrl.DISNEYPLUS_HOME), "Expected to see home page");

        addProfilePage
                .getActiveProfile();
        softAssert
                .assertFalse(addProfilePage.isDropdownProfileFirstPresent(), "Expected to not see any other profiles");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31386", "XWEBQAS-31382"})
    @Test(description = "Verify decline minor consent", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "MX", "US"})
    public void verifyDeclineMinorConsent() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.JUNIOR, language, null, true);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        addProfilePage
                .selectProfile(ProfileConstant.JUNIOR);
        waitForSeconds(3);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_THIRTEEN_DOB)
                .clickOnSaveButton();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        waitForSeconds(2);
        addProfilePage
                .clickOnDeclineBtn();
        addProfilePage
                .selectProfile(ProfileConstant.JUNIOR);
        addProfilePage
                .getActiveProfile();
        softAssert
                .assertTrue(addProfilePage.isExitJuniorModePresent(), "Expected to see Exit Junior mode option");
        softAssert
                .assertTrue(addProfilePage.getExitJuniorModeDropdownLabel().contains("Exit Junior Mode"), "Expected to see Junior mode label");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31386"})
    @Test(description = "Verify minor consent for existing profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "MX", "US"})
    public void verifyExistingProfileMinorConsent() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        account = accountApi.addFlex(account);
        disneyAccount.set(account);

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());

        getAccountApi().addProfile(account, ProfileConstant.JUNIOR, language, null, true);

        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        String baseUrl = addProfilePage.getHomeUrl();

        addProfilePage
                .selectProfile(ProfileConstant.JUNIOR);
        waitForSeconds(3);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_THIRTEEN_DOB)
                .clickOnSaveButton();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        waitForSeconds(2);
        softAssert
                .assertTrue(addProfilePage.getCurrentUrl().contains(PageUrl.PARENTAL_CONSENT), "Expected to see parental consent page");
        softAssert
                .assertTrue(addProfilePage.isAgreeBtnPresent(), "Expected to see Agree CTA button");
        softAssert
                .assertTrue(addProfilePage.isDeclineBtnPresent(), "Expected to see Decline CTA button");

        addProfilePage
                .openURL(baseUrl.concat(PageUrl.DISNEYPLUS_HOME));
        waitForSeconds(20);//Probably can be reduced once the tests start running consistently in Prod
        softAssert
                .assertTrue(addProfilePage.getCurrentUrl().contains(PageUrl.PARENTAL_CONSENT), "Expected to see parental consent page after deeplink");

        addProfilePage
                .deleteCookies();
        addProfilePage
                .openURL(addProfilePage.getHomeUrl());
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        waitForSeconds(10);
        softAssert
                .assertTrue(addProfilePage.getCurrentUrl().contains(PageUrl.PARENTAL_CONSENT), "Expected to see parental consent page after re-login");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-31385"})
    @Test(description = "Verify minor authentication for add profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX, "MX", "US"})
    public void verifyAddProfileMinorAuthentication() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());
        DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        SeleniumUtils utils = new SeleniumUtils(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        String baseUrl = addProfilePage.getHomeUrl();

        accountPage
                .clickOnAccountDropdown(isMobile());
        utils.scrollToBottom();
        waitForSeconds(2);
        accountPage
                .clickOnRestrictProfileCreationToggle();
        disneyPlusSetProfilePinPage
                .enterPassword("Wrong password");
        softAssert
                .assertTrue(disneyPlusSetProfilePinPage.isContinueButtonPresent(),
                        "Expected to see password continue button");

        disneyPlusSetProfilePinPage
                .clickOnContinue();
        softAssert
                .assertFalse(disneyPlusSetProfilePinPage.isEnterPinErrorPresent(),
                        "Expected to see error message for wrong password");

        disneyPlusSetProfilePinPage
                .clickOnPasswordCancelBtn();
        accountPage
                .clickOnRestrictProfileCreationToggle();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        waitForSeconds(2);
        utils.scrolltoTop();
        waitForSeconds(2);
        accountPage
                .clickOnAddProfileFromAccount();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        waitForSeconds(2);
        addProfilePage
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        addProfilePage
                .selectAvatarForProfile();
        addProfilePage
                .addProfileName(ProfileConstant.PROFILE1);
        addProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_THIRTEEN_DOB)
                .clickOnSaveButton();
        softAssert
                .assertTrue(addProfilePage.getCurrentUrl().contains(PageUrl.PARENTAL_CONSENT), "Expected to see parental consent page");

        addProfilePage
                .openURL(baseUrl.concat(PageUrl.DISNEYPLUS_HOME));
        waitForSeconds(20);
        softAssert
                .assertTrue(addProfilePage.getCurrentUrl().contains(PageUrl.DISNEYPLUS_HOME), "Expected to see home page");

        addProfilePage
                .getActiveProfile();
        softAssert
                .assertFalse(addProfilePage.isDropdownProfileFirstPresent(), "Expected to not see any other profiles");

        softAssert.assertAll();
    }
}
