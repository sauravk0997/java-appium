package com.disney.qa.tests.disney.web.appex.profile;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusProfilePickerPage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusProfilePickerTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @Test(description = "Verify Profile Picker Elements are displayed", groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyProfilePickerElements() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusProfilePickerPage profilePickerPage = new DisneyPlusProfilePickerPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profilePickerPage.getActiveProfile();
        profilePickerPage.clickOnEditProfile();
        profilePickerPage.verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);
        profilePickerPage.clickOnEditProfilesDoneButton();
        profilePickerPage.verifyUrlText(softAssert, PageUrl.SELECT_PROFILE);

        softAssert.assertTrue(profilePickerPage.isLogoPresent(), "Expected 'logo' to be present.");
        softAssert.assertTrue(profilePickerPage.isEditProfileFromPagePresent(),
                "Expected 'Edit Profile' button to be present.");

        softAssert.assertAll();
    }

    @Test(description = "Verify Profile Selection", groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyProfileSelection() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusProfilePickerPage profilePickerPage = new DisneyPlusProfilePickerPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profilePickerPage.getActiveProfile();
        profilePickerPage.clickOnEditProfile();
        profilePickerPage.verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);
        profilePickerPage.clickOnEditProfilesDoneButton();
        profilePickerPage.verifyUrlText(softAssert, PageUrl.SELECT_PROFILE);
        profilePickerPage.verifyProfileSelection(ProfileConstant.MAIN_TEST);

        softAssert.assertTrue(profilePickerPage.isHomeMenuOptionPresent(), "Expected 'Home' menu to be present.");

        softAssert.assertAll();
    }

    @Test(description = "Verify 'Edit Profile' Button", groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyEditProfileButton() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusProfilePickerPage profilePickerPage = new DisneyPlusProfilePickerPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        profilePickerPage.clickOnEditProfile();
        profilePickerPage.verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);
        profilePickerPage.clickOnEditProfilesDoneButton();

        softAssert.assertTrue(profilePickerPage.isEditProfileFromPagePresent(),
                "Expected 'Edit Profile' button to be present.");

        softAssert.assertAll();
    }
}
