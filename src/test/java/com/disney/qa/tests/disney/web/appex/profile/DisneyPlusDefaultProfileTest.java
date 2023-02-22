package com.disney.qa.tests.disney.web.appex.profile;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusDefaultProfilePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusDefaultProfileTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeTestMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19133"})
    @Test(description = "Verify Default Profile Exists", groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyDefaultProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusDefaultProfilePage defaultProfilePage = new DisneyPlusDefaultProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        softAssert.assertTrue(defaultProfilePage.getActiveProfile().isElementPresent(),
                        "Expected Default Profile to exist");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19135"})
    @Test(description = "Verify 'Delete Profile' does not exist on Edit Default Profile", groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyDeleteDefaultProfile() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusDefaultProfilePage defaultProfilePage = new DisneyPlusDefaultProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        defaultProfilePage.getActiveProfile();
        defaultProfilePage.clickOnEditProfile();
        defaultProfilePage.verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);
        defaultProfilePage.clickOnPrimaryProfileOnEditPage();

        softAssert.assertFalse(defaultProfilePage.verifyDeleteProfileButton(),
                "Expected 'Delete Profile' text not present.");

        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XWEBQAS-19137"})
    @Test(description = "Verify Default Avatar is applied to New Profile after skipping new add", groups = {TestGroup.DISNEY_APPEX,TestGroup.PROFILES})
    public void verifyDefaultAvatarOnSkippingNewProfileAdd() {
        SoftAssert softAssert = new SoftAssert();

        DisneyPlusDefaultProfilePage defaultProfilePage = new DisneyPlusDefaultProfilePage(getDriver());

        verifyDeleteDefaultProfile();
        defaultProfilePage.clickOnEditProfileDoneButton();
        defaultProfilePage.clickOnAddProfileButtonOnPage();
        defaultProfilePage.verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        defaultProfilePage.clickOnSkip();
        defaultProfilePage.verifyUrlText(softAssert, PageUrl.ADD_PROFILE);

        softAssert.assertTrue(defaultProfilePage.verifyTextOnAddProfileTextBoxIsEmpty(), "Expected Profile text box to be empty");

        defaultProfilePage.clickOnCancelButton();
        defaultProfilePage.verifyUrlText(softAssert, PageUrl.EDIT_PROFILE);
        defaultProfilePage.clickOnEditProfilesDoneButton();
        defaultProfilePage.verifyUrlText(softAssert, PageUrl.SELECT_PROFILE);

        softAssert.assertAll();
    }
}
