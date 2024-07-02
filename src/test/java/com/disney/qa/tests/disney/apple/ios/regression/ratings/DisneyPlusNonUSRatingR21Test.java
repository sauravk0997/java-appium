package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Date;
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.RatingConstant.Rating.R21;
import static com.disney.qa.common.constant.RatingConstant.SINGAPORE;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.getDictionary;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.INVALID_CREDENTIALS_ERROR;

public class DisneyPlusNonUSRatingR21Test extends DisneyPlusRatingsBase {

    private static final String PASSWORD_PAGE_ERROR_MESSAGE = "Password page should open";
    private static final String DOB_PAGE_ERROR_MESSAGE = "Enter your birthdate page should open";
    private static final String DOB_INVALID_BIRTHDATE_ERROR_MESSAGE = "Invalid birthdate error did not display";
    private static final String PIN_PAGE_ERROR_MESSAGE = "R21 pin page did not open";
    private static final String MUST_CREATE_PIN_POPUP_ERROR_MESSAGE = "Must Create pin' popup header is not displayed";


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
        sa.assertEquals(passwordPage.getErrorMessageString(), incorrectPasswordError, "'We couldn't log you in' error message did not display for wrong password entered.");
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
        sa.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);
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
        Assert.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69894"})
    @Test(description = "R21 - Create Pin - Verify Age - Select Back Button on Verify Age Screen", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINBackButtonOnVerifyAgeScreen() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        launchR21Content();
        verifyAgePage.clickCancelButton();
        Assert.assertTrue(detailsPage.isOpened(SHORT_TIMEOUT), "Details page was not opened");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74656"})
    @Test(description = "R21 - Create Pin - Enter Date of Birth - Select Back Button on Enter Your Birthday Screen", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINBackButtonOnDOBScreen() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        passwordPage.enterPassword(getAccount());
        sa.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);

        //Verify Continue button on alert
        verifyAgePage.clickCancelButton();
        sa.assertTrue(verifyAgeDOBPage.isBackModalDisplayed(), "Modal Back button/View alert not displayed");
        verifyAgeDOBPage.clickSystemAlertSecondaryBtn();
        sa.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);

        //Verify Not Now button on alert
        verifyAgePage.clickCancelButton();
        sa.assertTrue(verifyAgeDOBPage.isBackModalDisplayed(), "Modal Back button/View alert not displayed");
        verifyAgeDOBPage.clickDefaultAlertBtn();
        sa.assertTrue(detailsPage.isOpened(SHORT_TIMEOUT), "Details page was not opened");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69777"})
    @Test(description = "R21 - Playback - Video Player - Play Another R21 Content", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21VideoPlayerTwoContents() {
        ratingsSetup(SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusPinIOSPageBase pinPage = initPage(DisneyPlusPinIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);

        launchDeeplink(true, R.TESTDATA.get("disney_prod_r21_movie_out_deeplink"), 10);
        detailsPage.clickOpenButton();
        detailsPage.waitForPresenceOfAnElement(detailsPage.getPlayButton());
        detailsPage.clickPlayButton();
        verifyAgePage.clickIAm21PlusButton();
        Assert.assertTrue(passwordPage.isOpened(), PASSWORD_PAGE_ERROR_MESSAGE);

        passwordPage.enterPassword(getAccount());
        Assert.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);

        verifyAgeDOBPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        verifyAgeDOBPage.clickVerifyAgeButton();
        Assert.assertTrue(pinPage.isR21PinPageOpen(), "R21 pin page did not open.");

        IntStream.range(0, 4).forEach(i -> {
            pinPage.getTypeKey(String.valueOf(i)).click();
        });
        pressByElement(pinPage.getR21SetPinButton(), 1);
        Assert.assertTrue(videoPlayer.isOpened(), "Video did not begin to play for first R21 content.");

        videoPlayer.clickBackButton();
        launchDeeplink(true, R.TESTDATA.get("disney_prod_r21_movie_black_swan_deeplink"), 10);
        detailsPage.clickOpenButton();
        detailsPage.waitForPresenceOfAnElement(detailsPage.getPlayButton());
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), "Video did not begin to play for second R21 content.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69771"})
    @Test(description = "R21: Create PIN - Enter Date of Birth - Error Modal when DOB is Not 21+", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINNot21ErrorModalOnDOBScreen() {
        String modelErrorMessage = "Verify Age Modal/Alert should displayed";
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        passwordPage.enterPassword(getAccount());
        Assert.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);
        verifyAgeDOBPage.enterDOB(Person.U18.getMonth(), Person.U18.getDay(), Person.U18.getYear());
        verifyAgeDOBPage.clickVerifyAgeButton();
        sa.assertTrue(verifyAgePage.isAgeModalDisplayed(), modelErrorMessage);
        sa.assertTrue(verifyAgePage.isBrowseOtherTitlesButtonDisplayed(), "Browse other titles button not displyed on modal");
        verifyAgePage.clickDefaultAlertBtn();
        homePage.waitForHomePageToOpen();
        sa.assertTrue(homePage.isOpened(), "Home page did not open");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69860"})
    @Test(description = "R21: Create PIN - Enter Date of Birth - Missing Or Incorrect", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePinDateOfBirthMissingOrIncorrect() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);

        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        passwordPage.enterPassword(getAccount());
        verifyAgeDOBPage.clickVerifyAgeButton();
        Assert.assertTrue(verifyAgeDOBPage.isR21InvalidBirthdateErrorMessageDisplayed(), DOB_INVALID_BIRTHDATE_ERROR_MESSAGE);

        verifyAgeDOBPage.clickBrowseOtherTitlesButton();
        Assert.assertTrue(homePage.isOpened(), "Home page did not open.");
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        passwordPage.enterPassword(getAccount());
        verifyAgeDOBPage.waitForPresenceOfAnElement(verifyAgeDOBPage.getClearTextBtn());
        verifyAgeDOBPage.pressByElement(verifyAgeDOBPage.getClearTextBtn(), 1);
        verifyAgeDOBPage.enterDOB(Person.U18.getMonth(), Person.U18.getDay(), Person.U18.getYear());
        verifyAgeDOBPage.clickVerifyAgeButton();
        Assert.assertTrue(verifyAgeDOBPage.isR21InvalidBirthdateErrorMessageDisplayed(), DOB_INVALID_BIRTHDATE_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69893"})
    @Test(description = "R21 - Create Pin - Date of Birth Format", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINDateOfBirthFormat() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        String r21Format = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_BIRTHDAY_FORMAT.getText()).toUpperCase();
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        Assert.assertTrue(passwordPage.isOpened(), PASSWORD_PAGE_ERROR_MESSAGE);
        passwordPage.enterPassword(getAccount());
        Assert.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);

        verifyAgeDOBPage.waitForPresenceOfAnElement(verifyAgeDOBPage.getClearTextBtn());
        verifyAgeDOBPage.pressByElement(verifyAgeDOBPage.getClearTextBtn(), 1);
        Assert.assertTrue(verifyAgeDOBPage.getTextFieldValue(r21Format).isPresent(), "R21 birthday format is not present.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69770"})
    @Test(description = "R21: Create PIN - Verify Age - Not 21+ Error Modal", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINNot21ErrorModalOnAgeScreen() {
        String modelErrorMessage = "Verify Age Modal/Alert should be displayed";
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        launchR21Content();
        verifyAgePage.clickNoButton();
        Assert.assertTrue(verifyAgePage.isAgeModalDisplayed(), modelErrorMessage);
        Assert.assertTrue(verifyAgePage.isBrowseOtherTitlesButtonDisplayed(), "Browse other titles button not displyed on modal");

        //Clicking Cancel button to validate that user can not dismiss the modal by clicking outside the bounds
        verifyAgePage.clickCancelButton();
        Assert.assertTrue(verifyAgePage.isAgeModalDisplayed(), modelErrorMessage);

        //Click "Browse other titles" to return back to Home
        verifyAgePage.clickDefaultAlertBtn();
        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.isOpened(), "Home page did not open");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69766"})
    @Test(description = "R21 - Create Pin - Set Pin - Select Cancel", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINCancelButtonOnPinScreen() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        DisneyPlusPinIOSPageBase pinPage = initPage(DisneyPlusPinIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        passwordPage.enterPassword(getAccount());
        Assert.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);
        verifyAgeDOBPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        verifyAgeDOBPage.clickVerifyAgeButton();
        Assert.assertTrue(pinPage.isR21PinPageOpen(), "R21 pin page did not open.");
        verifyAgePage.clickCancelButton();
        sa.assertTrue(pinPage.isR21PinPageModalHeaderDisplayed(), MUST_CREATE_PIN_POPUP_ERROR_MESSAGE);
        sa.assertTrue(pinPage.isR21PinPageModalMessageDisplayed(), "Must Create pin' message is not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74657"})
    @Test(description = "R21 - Create Pin - Set PIN - Select Back Button on Set PIN Screen", groups = {"NonUS-Ratings", "R21"})
    public void verifyR21CreatePINCancelModalOnPINScreen() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusPinIOSPageBase pinPage = initPage(DisneyPlusPinIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        passwordPage.enterPassword(getAccount());
        Assert.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);
        verifyAgeDOBPage.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        verifyAgeDOBPage.clickVerifyAgeButton();
        Assert.assertTrue(pinPage.isR21PinPageOpen(), PIN_PAGE_ERROR_MESSAGE);

        //Verify Continue button on alert
        pinPage.clickCancelButton();
        sa.assertTrue(pinPage.isR21PinPageModalHeaderDisplayed(), MUST_CREATE_PIN_POPUP_ERROR_MESSAGE);
        verifyAgeDOBPage.clickSystemAlertSecondaryBtn();
        sa.assertTrue(pinPage.isR21PinPageOpen(), PIN_PAGE_ERROR_MESSAGE);

        //Verify Not Now button on alert
        pinPage.clickCancelButton();
        sa.assertTrue(pinPage.isR21PinPageModalMessageDisplayed(), MUST_CREATE_PIN_POPUP_ERROR_MESSAGE);
        verifyAgeDOBPage.clickDefaultAlertBtn();
        sa.assertTrue(detailsPage.isOpened(SHORT_TIMEOUT), "Details page was not opened");
        sa.assertAll();
    }

    public void launchR21Content() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(contentTitle);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickPlayButton(SHORT_TIMEOUT);
        Assert.assertTrue(verifyAgePage.isOpened(), "Verify your age page should open");
    }
}