package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.util.TestGroup;
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
    private static final String PIN_PAGE_DID_NOT_OPEN = "R21 pin page did not open";
    private static final String DETAILS_PAGE_DID_NOT_OPEN = "Details page did not open";
    private static final String HOME_PAGE_DID_NOT_OPEN = "Home page did not open";
    private static final String MUST_CREATE_PIN_POPUP_ERROR_MESSAGE = "'Must Create pin' popup header/message is not displayed";
    private static final String MUST_CREATE_PIN_POPUP_SUBHEADER_ERROR_MESSAGE = "Manage your PIN message is not displayed";
    private static final String MUST_BE_21_YEAR_OLDER_MODAL_ERROR_MESSAGE = "'You Must be 21 year older' modal/popup is not displayed";
    private static final String MUST_VERIFY_YOUR_AGE_MODAL_ERROR_MESSAGE = "'You must verify your age' modal/popup is not displayed";
    private static final String BROWSE_OTHER_TITLE_ERROR_MESSAGE = "Browse other titles button not displayed on modal";
    private static final String DOB_INVALID_BIRTHDATE_ERROR_MESSAGE = "Invalid birthdate error did not display";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69769"})
    @Test(description = "R21: Edit Profile - Maturity Ratings Slider - R21 Extra Copy", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
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
    @Test(description = "R21: Create PIN - Enter Password - Invalid Input", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
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
    @Test(description = "R21 - Create Pin - Enter Password - Valid Input directs user to Enter Date Of Birth Screen", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
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
    @Test(description = "R21 - Create Pin - Enter Password - Forgot Password Flow", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
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
    @Test(description = "R21 - Create Pin - Verify Age - Select Back Button on Verify Age Screen", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21CreatePINBackButtonOnVerifyAgeScreen() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        launchR21Content();
        verifyAgePage.clickCancelButton();
        Assert.assertTrue(detailsPage.isOpened(SHORT_TIMEOUT), DETAILS_PAGE_DID_NOT_OPEN);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74656"})
    @Test(description = "R21 - Create Pin - Enter Date of Birth - Select Back Button on Enter Your Birthday Screen", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
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
        sa.assertTrue(verifyAgeDOBPage.isR21VerifyYourAgeModalDisplayed(), MUST_VERIFY_YOUR_AGE_MODAL_ERROR_MESSAGE);
        verifyAgeDOBPage.clickSystemAlertSecondaryBtn();
        sa.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);

        //Verify Not Now button on alert
        verifyAgePage.clickCancelButton();
        sa.assertTrue(verifyAgeDOBPage.isR21VerifyYourAgeModalDisplayed(), MUST_VERIFY_YOUR_AGE_MODAL_ERROR_MESSAGE);
        verifyAgeDOBPage.clickDefaultAlertBtn();
        sa.assertTrue(detailsPage.isOpened(SHORT_TIMEOUT), DETAILS_PAGE_DID_NOT_OPEN);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69777"})
    @Test(description = "R21 - Playback - Video Player - Play Another R21 Content", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
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
        Assert.assertTrue(pinPage.isR21PinPageOpen(), PIN_PAGE_DID_NOT_OPEN);

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
    @Test(description = "R21: Create PIN - Enter Date of Birth - Error Modal when DOB is Not 21+", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21CreatePINNot21ErrorModalOnDOBScreen() {
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
        sa.assertTrue(verifyAgePage.isR21MustBe21YearOlderModalDisplayed(), MUST_BE_21_YEAR_OLDER_MODAL_ERROR_MESSAGE);
        sa.assertTrue(verifyAgePage.isBrowseOtherTitlesButtonDisplayed(), BROWSE_OTHER_TITLE_ERROR_MESSAGE);
        verifyAgePage.clickDefaultAlertBtn();
        homePage.waitForHomePageToOpen();
        sa.assertTrue(homePage.isOpened(), HOME_PAGE_DID_NOT_OPEN);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69860"})
    @Test(description = "R21: Create PIN - Enter Date of Birth - Missing Or Incorrect", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
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
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_DID_NOT_OPEN);
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
    @Test(description = "R21 - Create Pin - Date of Birth Format", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
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
    @Test(description = "R21: Create PIN - Verify Age - Not 21+ Error Modal", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21CreatePINNot21ErrorModalOnAgeScreen() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        launchR21Content();
        verifyAgePage.clickNoButton();
        Assert.assertTrue(verifyAgePage.isR21MustBe21YearOlderModalDisplayed(), MUST_BE_21_YEAR_OLDER_MODAL_ERROR_MESSAGE);
        Assert.assertTrue(verifyAgePage.isBrowseOtherTitlesButtonDisplayed(), BROWSE_OTHER_TITLE_ERROR_MESSAGE);

        //Clicking Cancel button to validate that user can not dismiss the modal by clicking outside the bounds
        verifyAgePage.clickCancelButton();
        Assert.assertTrue(verifyAgePage.isR21MustBe21YearOlderModalDisplayed(), MUST_BE_21_YEAR_OLDER_MODAL_ERROR_MESSAGE);

        //Click "Browse other titles" to return back to Home
        verifyAgePage.clickDefaultAlertBtn();
        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_DID_NOT_OPEN);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69766"})
    @Test(description = "R21 - Create Pin - Set Pin - Select Cancel", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
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
        Assert.assertTrue(pinPage.isR21PinPageOpen(), PIN_PAGE_DID_NOT_OPEN);
        verifyAgePage.clickCancelButton();
        sa.assertTrue(pinPage.isR21PinPageModalHeaderDisplayed(), MUST_CREATE_PIN_POPUP_ERROR_MESSAGE);
        sa.assertTrue(pinPage.isR21PinPageModalMessageDisplayed(), MUST_CREATE_PIN_POPUP_SUBHEADER_ERROR_MESSAGE);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69768"})
    @Test(description = "R21: Create PIN - Verify Age - I Am 21+", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21CreatePINPasswordScreen() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        Assert.assertTrue(passwordPage.isOpened(), PASSWORD_PAGE_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69776"})
    @Test(description = "R21 - Create Pin - Set PIN - Error Modal", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21CreatePINCancelModalUIOnPINScreen() {
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
        Assert.assertTrue(pinPage.isR21PinPageOpen(), PIN_PAGE_DID_NOT_OPEN);
        pinPage.clickCancelButton();
        sa.assertTrue(pinPage.isR21PinPageModalHeaderDisplayed(), MUST_CREATE_PIN_POPUP_ERROR_MESSAGE);
        sa.assertTrue(pinPage.isR21PinPageModalMessageDisplayed(), MUST_CREATE_PIN_POPUP_SUBHEADER_ERROR_MESSAGE);
        sa.assertTrue(pinPage.isContinueButtonOnCancelModalDisplayed(), "Continue button on Must Create Pin modal is not displayed");
        sa.assertTrue(pinPage.isNotNowButtonOnCancelModalDisplayed(), "Not now button on Must Create Pin modal is not displayed");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74657"})
    @Test(description = "R21 - Create Pin - Set PIN - Select Back Button on Set PIN Screen", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21CreatePINCancelModalButtonOnPINScreen() {
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
        Assert.assertTrue(pinPage.isR21PinPageOpen(), PIN_PAGE_DID_NOT_OPEN);

        //Verify Continue button on alert
        pinPage.clickCancelButton();
        sa.assertTrue(pinPage.isR21PinPageModalHeaderDisplayed(), MUST_CREATE_PIN_POPUP_ERROR_MESSAGE);
        verifyAgeDOBPage.clickSystemAlertSecondaryBtn();
        sa.assertTrue(pinPage.isR21PinPageOpen(), PIN_PAGE_DID_NOT_OPEN);

        //Verify Not Now button on alert
        pinPage.clickCancelButton();
        sa.assertTrue(pinPage.isR21PinPageModalMessageDisplayed(), MUST_CREATE_PIN_POPUP_SUBHEADER_ERROR_MESSAGE);
        verifyAgeDOBPage.clickDefaultAlertBtn();
        sa.assertTrue(detailsPage.isOpened(SHORT_TIMEOUT), DETAILS_PAGE_DID_NOT_OPEN);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74659"})
    @Test(description = "R21 - User Has PIN - Verify Age - Not 21+ Error Modal", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21HasPINNot21ErrorMessage() {
        ratingsSetupWithPIN(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        navigateToHomePageForPinUser();
        launchR21Content();
        verifyAgePage.clickNoButton();
        sa.assertTrue(verifyAgePage.isR21MustBe21YearOlderModalDisplayed(), MUST_BE_21_YEAR_OLDER_MODAL_ERROR_MESSAGE);
        sa.assertTrue(verifyAgePage.isBrowseOtherTitlesButtonDisplayed(), BROWSE_OTHER_TITLE_ERROR_MESSAGE);

        //Tap above the alert to validate that user can not dismiss the modal by clicking outside the bounds
        verifyAgePage.tapAboveElement(verifyAgePage.getSystemAlert());
        Assert.assertTrue(verifyAgePage.isR21MustBe21YearOlderModalDisplayed(), MUST_BE_21_YEAR_OLDER_MODAL_ERROR_MESSAGE);

        //Click "Browse other titles" to return back to Home
        verifyAgePage.clickDefaultAlertBtn();
        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_DID_NOT_OPEN);
        sa.assertAll();
    }
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69944"})
    @Test(description = "R21 - Create Pin - Enter Date of Birth - Inline Error if Date is Illogical", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21CreatePINErrorMessageForInvalidDOB() {
        ratingsSetup(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        passwordPage.enterPassword(getAccount());
        Assert.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);
        verifyAgeDOBPage.enterDOB(Person.OLDERTHAN200.getMonth(), Person.OLDERTHAN200.getDay(), Person.OLDERTHAN200.getYear());
        verifyAgeDOBPage.clickVerifyAgeButton();
        Assert.assertTrue(verifyAgeDOBPage.isR21InvalidBirthdateErrorMessageDisplayed(), DOB_INVALID_BIRTHDATE_ERROR_MESSAGE);
        Assert.assertTrue(verifyAgePage.isR21MustBe21YearOlderModalDisplayed(), MUST_BE_21_YEAR_OLDER_MODAL_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74746"})
    @Test(description = "R21 - User Has Pin - Verify Age - Select Back Button on Verify Age Screen", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21HasPINBackButtonOnVerifyAgeScreen() {
        ratingsSetupWithPIN(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        navigateToHomePageForPinUser();
        launchR21Content();
        verifyAgePage.clickCancelButton();
        Assert.assertTrue(detailsPage.isOpened(SHORT_TIMEOUT), DETAILS_PAGE_DID_NOT_OPEN);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74750"})
    @Test(description = "R21 - User Has Pin - Enter Date of Birth - DOB Date Format for Supported UI Languages", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21HasPINDateOfBirthFormat() {
        ratingsSetupWithPIN(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        String r21Format = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.R21_BIRTHDAY_FORMAT.getText()).toUpperCase();
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        navigateToHomePageForPinUser();
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        Assert.assertTrue(passwordPage.isOpened(), PASSWORD_PAGE_ERROR_MESSAGE);
        passwordPage.enterPassword(getAccount());
        Assert.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);
        verifyAgeDOBPage.waitForPresenceOfAnElement(verifyAgeDOBPage.getClearTextBtn());
        verifyAgeDOBPage.pressByElement(verifyAgeDOBPage.getClearTextBtn(), 1);
        Assert.assertTrue(verifyAgeDOBPage.getTextFieldValue(r21Format).isPresent(), "R21 birthday format is not present.");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74752"})
    @Test(description = "R21 - User Has Pin - Enter Date of Birth - Inline Error if Date is Illogical", groups = {TestGroup.NON_US_RATINGS, TestGroup.R21})
    public void verifyR21HasPINErrorMessageForInvalidDOB() {
        ratingsSetupWithPIN(R21.getContentRating(), SINGAPORE_LANG, SINGAPORE);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusVerifyAgeDOBCollectionIOSPageBase verifyAgeDOBPage = initPage(DisneyPlusVerifyAgeDOBCollectionIOSPageBase.class);
        navigateToHomePageForPinUser();
        launchR21Content();
        verifyAgePage.clickIAm21PlusButton();
        passwordPage.enterPassword(getAccount());
        Assert.assertTrue(verifyAgeDOBPage.isOpened(), DOB_PAGE_ERROR_MESSAGE);
        verifyAgeDOBPage.enterDOB(Person.OLDERTHAN200.getMonth(), Person.OLDERTHAN200.getDay(), Person.OLDERTHAN200.getYear());
        verifyAgeDOBPage.clickVerifyAgeButton();
        Assert.assertTrue(verifyAgeDOBPage.isR21InvalidBirthdateErrorMessageDisplayed(), DOB_INVALID_BIRTHDATE_ERROR_MESSAGE);
        Assert.assertTrue(verifyAgePage.isR21MustBe21YearOlderModalDisplayed(), MUST_BE_21_YEAR_OLDER_MODAL_ERROR_MESSAGE);
    }

    private void launchR21Content() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVerifyAgeIOSPageBase verifyAgePage = initPage(DisneyPlusVerifyAgeIOSPageBase.class);
        homePage.clickSearchIcon();
        searchPage.searchForMedia(contentTitle);
        searchPage.getDisplayedTitles().get(0).click();
        detailsPage.clickPlayButton(SHORT_TIMEOUT);
        Assert.assertTrue(verifyAgePage.isOpened(), "'Verify your age' page should open");
    }

    private void navigateToHomePageForPinUser() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusPinIOSPageBase pinPage = initPage(DisneyPlusPinIOSPageBase.class);
        Assert.assertTrue(whoIsWatching.isOpened(), "Who is watching page was not open");
        whoIsWatching.getStaticTextByLabel(DEFAULT_PROFILE).click();
        pinPage.enterPin(PROFILE_PIN);
        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.isOpened(), "After entering profile pin, home page did not open.");
    }
}