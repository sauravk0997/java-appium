package com.disney.qa.tests.disney.web.appex.profile;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.web.appex.DisneyAppExUtil;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusCreateProfilePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.ProfileConstant;
import com.disney.qa.disney.web.entities.ProfileEligibility;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusCreateProfileTest extends DisneyPlusBaseTest {

    private static final ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        disneyAccount.set(new DisneyAccount());
    }

    @Test(description = "Create Adult Profile", priority = 1, groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX,
            "AG", "AI", "AR", "AT", "AU", "AW", "BB", "BE", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CH", "CL", "CO", "CR", "CW", "DE", "DK", "DM", "DO", "EC", "ES", "FI", "FK", "FR", "GB", "GD", "GF", "GG", "GL", "GP", "GS", "GT", "GY", "HN", "HT", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KR", "KN", "KY", "LC", "LU", "MC", "MF", "MQ", "MS", "MU", "MX", "NC", "NI", "NL", "NO", "NZ", "PA", "PE", "PR", "PT", "PY", "RE", "SE", "SG", "SR", "SV", "TC", "TT", "US", "UY", "VC", "VE", "VG", "VI", "WF", "YT"})
    public void verifyCreateProfileFlow() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusCreateProfilePage createProfilePage = new DisneyPlusCreateProfilePage(getDriver());
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        createProfilePage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        createProfilePage
                .clickOnSkip();
        createProfilePage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        createProfilePage
                .addProfileName(ProfileConstant.PROFILE1);
        createProfilePage
                .clickOnPrimarySplashProfileBtn();
        createProfilePage
                .selectAvatarForProfile();
        softAssert
                .assertFalse(createProfilePage.getProfileNameValue().isBlank(), "Expected to see that the profile name still exists after avatar is selected");
        createProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.ELIGIBLE_DOB)
                .clickOnGenderDropdown()
                .selectGender()
                .clickOnSaveButton();
        commercePage
                .checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());
        createProfilePage
                .verifyNumberOfProfileForNewProfileCreation(softAssert, PageUrl.SELECT_PROFILE);

        softAssert.assertAll();
    }

    @Test(description = "Create Junior Profile", groups = {TestGroup.DISNEY_APPEX, TestGroup.PROFILES, TestGroup.ARIEL_APPEX})
    public void verifyCreateJuniorProfileFlow() {
        SoftAssert softAssert = new SoftAssert();

        DisneyAppExUtil appExUtil = new DisneyAppExUtil(getDriver());
        DisneyPlusCreateProfilePage createProfilePage = new DisneyPlusCreateProfilePage(getDriver());

        disneyAccount.set(getAccountApi().createAccount("Yearly", locale, language, SUB_VERSION_V1));
        appExUtil.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, countryData.searchAndReturnCountryData(locale, CODE, COUNTRY));
        appExUtil.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        createProfilePage
                .getActiveProfile();
        createProfilePage
                .clickOnAddProfileFromAccount()
                .verifyUrlText(softAssert, PageUrl.SELECT_AVATAR);
        createProfilePage
                .selectAvatarForProfile();
        createProfilePage
                .verifyUrlText(softAssert, PageUrl.ADD_PROFILE);
        createProfilePage
                .addProfileName(ProfileConstant.JUNIOR);
        createProfilePage
                .clickOnDOBField()
                .enterDOB(ProfileEligibility.UNDER_EIGHTEEN_DOB);
        createProfilePage
                .clickJuniorToggleBtn();
        createProfilePage
                .clickOnSaveButton();

        //commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(softAssert, locale, false, true, isMobile());

        createProfilePage
                .verifyNumberOfProfileForNewProfileCreation(softAssert, "select-profile");

        softAssert.assertAll();
    }
}
