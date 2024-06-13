package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Date;

import static com.disney.qa.common.constant.RatingConstant.Rating.R21;
import static com.disney.qa.common.constant.RatingConstant.SINGAPORE;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.INVALID_CREDENTIALS_ERROR;

public class DisneyPlusNonUSRatingR21Test extends DisneyPlusRatingsBase {

    private static final String PASSWORD_PAGE_ERROR_MESSAGE = "Password page should open";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69769"})
    @Test(description = "R21: Edit Profile - Maturity Ratings Slider - R21 Extra Copy", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21MaturityRatingSliderCopy() {
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_YEARLY_PREMIUM, SINGAPORE, SINGAPORE_LANG));
        getAccountApi().overrideLocations(getAccount(), SINGAPORE);
        initialSetup();
        handleAlert();
        setAppToHomeScreen(getAccount());
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(getAccount().getFirstName());
        swipe(editProfile.getMaturityRatingLabel(), Direction.UP, 2, 500);
        editProfile.getMaturityRatingCell().click();
        editProfile.enterPassword(getAccount());
        Assert.assertTrue(editProfile.isR21MaturitySliderPresent(), "Maturity Rating slider description for R21 is not present");
    }
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74415"})
    @Test(description = "R21: Create PIN - Enter Password - Invalid Input", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINInvalidPasswordError() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String incorrectPasswordError = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, INVALID_CREDENTIALS_ERROR.getText());
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        Assert.assertTrue(passwordPage.isOpened(), PASSWORD_PAGE_ERROR_MESSAGE);
        passwordPage.enterPasswordNoAccount(INVALID_PASSWORD);
        sa.assertEquals(passwordPage.getErrorMessageString().replaceAll("\"", "'"), incorrectPasswordError.replaceAll("’|‘","'"), "'We couldn't log you in' error message did not display for wrong password entered.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69767"})
    @Test(description = "R21 - Create Pin - Enter Password - Valid Input directs user to Enter Date Of Birth Screen", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINDOBScreen() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        Assert.assertTrue(passwordPage.isOpened(), PASSWORD_PAGE_ERROR_MESSAGE);
        passwordPage.enterPassword(getAccount());
        sa.assertTrue(verifyAgeDOBPage.isOpened(), "Enter your birthdate page not opened");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74416"})
    @Test(description = "R21 - Create Pin - Enter Password - Forgot Password Flow", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINForgetPassword() {
        String NEW_PASSWORD = "TestPass1234!";
        Date startTime = getEmailApi().getStartTime();
        ratingsSetupForOTPAccount(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusChangePasswordIOSPageBase changePasswordPage = new DisneyPlusChangePasswordIOSPageBase(getDriver());
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        Assert.assertTrue(passwordPage.isOpened(), PASSWORD_PAGE_ERROR_MESSAGE);
        passwordPage.clickR21ForgotPasswordLink();
        Assert.assertTrue(oneTimePasscodePage.isOpened(), "OTP Page was not opened");
        String otp = getEmailApi().getDisneyOTP(getAccount().getEmail(), startTime);
        oneTimePasscodePage.enterOtp(otp);
        oneTimePasscodePage.clickPrimaryButton();
        Assert.assertTrue(changePasswordPage.isOpened(),
                "Change Password screen did not open after submitting OTP");
        changePasswordPage.submitNewPasswordValue(NEW_PASSWORD);
        Assert.assertTrue(verifyAgeDOBPage.isOpened(), "Enter your birthdate page not opened");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69894"})
    @Test(description = "R21 - Create Pin - Verify Age - Select Back Button on Verify Age Screen", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINBackButtonOnVerifyAgeScreen() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        launchR21Content();
        Assert.assertTrue(verifyAgePage.isOpened(), "Verify Age page was not opened");
        verifyAgePage.clickCancelButton();
        Assert.assertTrue(detailsPage.isOpened(SHORT_TIMEOUT), "Details page was not opened");
    }

    public void launchR21Content() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(contentTitle);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickPlayButton();
        Assert.assertTrue(verifyAgePage.isOpened(), "Verify your age page should open");
    }

}