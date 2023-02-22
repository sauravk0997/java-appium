package com.disney.qa.tests.disney.web.appex.profile.editProfile;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.appex.parentalcontrols.DisneyPlusSetProfilePinPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.editprofile.DisneyPlusAutoplayPage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAutoplayTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19469"})
    @Test(description = "Verify Adult profile Autoplay toggled on by default", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES})
    public void verifyAdultProfileAutoplayOn() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAutoplayPage autoPlayPage = new DisneyPlusAutoplayPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        autoPlayPage.getActiveProfile();
        autoPlayPage.clickOnEditProfile();
        autoPlayPage.selectProfileToEdit(ProfileConstant.MAIN_TEST);
        autoPlayPage.verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);

        softAssert.assertTrue(autoPlayPage.isAutoplayToggleOn(false), "Expected 'Autoplay Toggled On' by default");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19471"})
    @Test(description = "Verify Junior profile Autoplay toggle ", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyJuniorProfileToggle() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusAutoplayPage autoPlayPage = new DisneyPlusAutoplayPage(getDriver());
        DisneyPlusSetProfilePinPage disneyPlusSetProfilePinPage = new DisneyPlusSetProfilePinPage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        autoPlayPage
                .createJuniorProfile();
        disneyPlusSetProfilePinPage
                .enterPassword(disneyAccount.get().getUserPass());
        disneyPlusSetProfilePinPage
                .clickOnContinue();
        autoPlayPage
                .clickOnAgreeBtn();
        autoPlayPage
                .selectProfileForWhoIsWatching(ProfileConstant.JUNIOR);
        autoPlayPage
                .getActiveProfile();
        autoPlayPage
                .exitJuniorProfile()
                .clickOnEditProfileFromPage();
        autoPlayPage
                .selectProfileToEdit(ProfileConstant.JUNIOR);
        softAssert
                .assertFalse(autoPlayPage.isAutoplayToggleOn(false), "Expected 'Autoplay Toggled Off' by default for Kid's Profile");
        softAssert
                .assertTrue(autoPlayPage.verifyJuniorModeToggle("true"),
                        String.format("Expected Junior mode toggle on: '%s'", autoPlayPage.getJuniorModeToggleStatus("true").getAttribute("aria-pressed")));

        softAssert.assertAll();
    }
}
