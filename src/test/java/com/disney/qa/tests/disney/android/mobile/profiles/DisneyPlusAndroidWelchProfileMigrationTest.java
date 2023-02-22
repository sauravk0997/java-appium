package com.disney.qa.tests.disney.android.mobile.profiles;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.responses.profile.items.ProfileAttributes;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class DisneyPlusAndroidWelchProfileMigrationTest extends BaseDisneyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String PRIMARY_PROFILE_NAME = "Profile";
    private static final String SECONDARY_PROFILE_NAME = "Secondary";
    private static final String TERTIARY_PROFILE_NAME = "Tertiary";
    private static final String CHILD_PROFILE_NAME = "Child";
    private static final String MATURITY_RATING_TV_14 = "TV-14";
    private static final String ADDITIONAL_PROFILE_DOB = "2000-01-01";
    private static final String CHILD_PROFILE_DOB = "2015-01-01";

    ThreadLocal<String> defaultRating = new ThreadLocal<>();
    ThreadLocal<String> maxRating = new ThreadLocal<>();
    List<String> ratingSystemValues = new ArrayList<>();

    private void testSetUp() {
        ratingSystemValues = getRefinedRatingSystemValues(DEFAULT_PROFILE);
        defaultRating.set(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DisneyPlusMoreMenuPageBase.EditProfileKeys.RATING_14.getKey()));
        maxRating.set(ratingSystemValues.get(ratingSystemValues.size() - 1));
    }

    private DisneyAccount createAccount() {
        CreateDisneyAccountRequest request = new CreateDisneyAccountRequest();
        request.addSku(DisneySkuParameters.DISNEY_IAP_GOOGLE_MONTHLY);
        request.setFirstName(PRIMARY_PROFILE_NAME);
        request.setCountry(languageUtils.get().getLocale());
        request.setLanguage(languageUtils.get().getUserLanguage());
        request.setStarOnboarded(false);
        
        return accountApi.get().createAccount(request);
    }

    private ProfileAttributes setProfileMaturityRating(String maturityRating) {
        ProfileAttributes profileAttributes = new ProfileAttributes();
        profileAttributes.setAdditionalProperty("maturityRating", maturityRating);
        return profileAttributes;
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69529"})
    @Test(description = "[Maturity] Profile Migration | Multi Profile | Profile selection to start migration", groups = {"Welch", "Profiles"})
    public void testMigrationStartsAfterProfileSelect() {
        testSetUp();
        login(createAccount(), false);
        Assert.assertTrue(initPage(DisneyPlusMaturityPageBase.class).isOpened(),
                "Expected - Star Onboarding to be present after profile select");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69526"})
    @Test(description = "[Maturity] Profile Migration | Multi Profile | Selecting Kids Profile", groups = {"Welch", "Profiles"})
    public void testChildProfileDoesNotMigrate() {
        testSetUp();
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        DisneyAccount account = createAccount();
        accountApi.get().addProfile(account, SECONDARY_PROFILE_NAME, null, languageUtils.get().getUserLanguage(), null, true, false);
        login(account, false);

        commonPageBase.clickOnGenericTextExactElement(SECONDARY_PROFILE_NAME);
        commonPageBase.dismissTravelingDialog();

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "Star Onboarding not skipped after Child Profile select");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69717"})
    @Test(description = "[Maturity] Profile migration | Star Introduction Overlay", groups = {"Welch", "Profiles"})
    public void testIntroductionOverlay() {
        testSetUp();
        SoftAssert sa = new SoftAssert();
        login(createAccount(), false);

        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);

        sa.assertEquals(maturityPageBase.getMigrationTitleText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_INTRO_OVERLAY_TITLE.getText()),
                "Profile Migration title text is incorrect");

        sa.assertEquals(maturityPageBase.getMigrationDescriptionText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_INTRO_OVERLAY_DESC.getText()),
                "Profile Migration title text is incorrect");

        sa.assertTrue(maturityPageBase.isTextViewStringDisplayed(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_CONTINUE.getText())),
                "Profile Migration continue button text is incorrect");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71668"})
    @Test(description = "[Maturity] Profile Migration | Update Maturity Rating - CONTINUE", groups = {"Welch", "Profiles"})
    public void testContinueButton() {
        testSetUp();
        SoftAssert sa = new SoftAssert();
        login(createAccount(), false);

        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        maturityPageBase.clickContinueButton();

        sa.assertTrue(maturityPageBase.isProfileInfoAvatarDisplayed(),
                "User profile avatar not displayed");

        sa.assertEquals(maturityPageBase.getProfileName(), PRIMARY_PROFILE_NAME,
                "Selected profile name not displayed correctly");

        sa.assertEquals(maturityPageBase.getProfileMigrationCatalogHeaderText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_TITLE.getText()),
                "Expected - Full Catalog header text to be correctly displayed");

        sa.assertEquals(maturityPageBase.getProfileMigrationCatalogSubHeaderText(), StringUtils.substringBefore(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.MATURITY_RATING_SUBTITLE.getText()), "$") + maxRating.get(),
                "Full Catalog sub-header not properly displayed");

        sa.assertTrue(maturityPageBase.isTextViewStringDisplayed(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_DESCRIPTION_VALUE_BULLET_ONE.getText())),
                "Maturity Setting description bullet one not displayed correctly");

        sa.assertTrue(maturityPageBase.isTextViewStringDisplayed(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_DESCRIPTION_VALUE_BULLET_TWO.getText())),
                "Maturity Setting description bullet two not displayed correctly");

        sa.assertTrue(maturityPageBase.isTextViewStringDisplayed(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText())),
                "'Full Catalog' button text not properly displayed");

        sa.assertTrue(maturityPageBase.isTextViewStringDisplayed(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_NOT_NOW.getText())),
                "'Not Now' button text not properly displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69519"})
    @Test(description = "[Maturity] Profile Migration | Update Maturity Rating - NOT NOW", groups = {"Welch", "Profiles"})
    public void testNotNowButton() {
        testSetUp();
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        SoftAssert sa = new SoftAssert();
        login(createAccount(), false);

        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStarNotNowBtn();

        sa.assertTrue(maturityPageBase.isNotNowCloseButtonPresent(),
                "Not Now button is not visible");

        sa.assertEquals(maturityPageBase.getNotNowHeaderText(), StringUtils.substringBefore(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.CONTINUE_WITHOUT_MATURE_TITLE.getText()), "$") + ratingSystemValues.get(ratingSystemValues.size() - 3),
                "Not Now header text incorrectly displayed");

        sa.assertEquals(maturityPageBase.getNotNowDescriptionBody(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.CONTINUE_WITHOUT_MATURE_DESC.getText()).replace("${current_rating_value_text}", ratingSystemValues.get(ratingSystemValues.size() - 3)),
                "Not Now description text incorrectly displayed");

        Assert.assertTrue(maturityPageBase.isStandardButtonPresent(),
                "Not Now continue button not visible");

        sa.assertTrue(maturityPageBase.isTextViewStringDisplayed(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_CONTINUE.getText())),
                "Not Now button text incorrectly displayed");

        maturityPageBase.clickNotNowCloseBtn();
        Assert.assertFalse(maturityPageBase.isNotNowCloseButtonPresent(),
                "Dialog not dismissed and Not Now button is visible");

        maturityPageBase.clickStarNotNowBtn();
        maturityPageBase.clickStandardButton();
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        dismissChromecastOverlay();

        maturityPageBase.dismissTravelingDialog();
        discoverPageBase.clickStandardButton();
        discoverPageBase.navigateToSettings();
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        moreMenuPageBase.openEditProfile();
        initPage(DisneyPlusLoginPageBase.class).getGenericAccessibilityidElement(languageUtils.get().getEditProfileString(PRIMARY_PROFILE_NAME)).click();

        sa.assertEquals(moreMenuPageBase.getProfileRatingText(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DisneyPlusMoreMenuPageBase.EditProfileKeys.CONTENT_RATING.getKey())),
                languageUtils.get().replaceValuePlaceholders(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.MATURITY_RATING_DESCRIPTION_VALUE.getText()),
                        ratingSystemValues.get(ratingSystemValues.size() - 3)), "Maturity rating description incorrectly displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69516"})
    @Test(description = "[Maturity] Profile Migration |Mature Content Password Authentication", groups = {"Welch", "Profiles"})
    public void testMatureContentPasswordAuthenticationScreen() {
        testSetUp();
        SoftAssert sa = new SoftAssert();
        AndroidUtilsExtended util = new AndroidUtilsExtended();
        DisneyAccount account = createAccount();

        login(account, false);

        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStandardButton();

        sa.assertTrue(maturityPageBase.isConfirmPasswordOpen(),
                "Confirm password screen was not opened");

        util.hideKeyboard();

        sa.assertTrue(maturityPageBase.isProfileInfoAvatarDisplayed(),
                "User profile avatar was not displayed");

        sa.assertEquals(maturityPageBase.getProfileName(), PRIMARY_PROFILE_NAME,
                "Selected profile name was displayed incorrectly");

        sa.assertEquals(maturityPageBase.getPasswordTitleText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_PASSWORD_TITLE.getText()),
                "Confirm Password title was not displayed");

        sa.assertEquals(maturityPageBase.getPasswordSubtitleText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_PASSWORD_COPY.getText())
                        .replace("${highest_rating_value_text}", maxRating.get()),
                "Confirm Password title was not displayed");

        sa.assertTrue(maturityPageBase.isEditTextFieldPresent() && maturityPageBase.getShowPwdToggle().isElementPresent(),
                "Password text entry was not displayed with visibility toggle");

        sa.assertEquals(maturityPageBase.getCaseSensitiveSubcopy().getText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, "case_sensitive"),
                "'(case sensitive)' subcopy text was not displayed");

        sa.assertTrue(maturityPageBase.isForgotPasswordLinkPresent(),
                "Forgot Password link was not displayed");

        sa.assertTrue(maturityPageBase.isStandardButtonPresent(),
                "Standard Confirm button was not displayed");

        maturityPageBase.clickForgotPasswordLink();

        boolean doesForgotPwdOpen = maturityPageBase.getForgotPwdHeader().isElementPresent();
        if(doesForgotPwdOpen) {
            LOGGER.info("Keyboard: " + util.isKeyboardShown());
            util.hideKeyboard();
            util.pressBack();
        }

        sa.assertTrue(doesForgotPwdOpen,
                "Forgot Password link did not open the Forgot Password entry page");

        maturityPageBase.editTextByClass.type("fakePassword");

        String passwordEntryText = maturityPageBase.editTextByClass.getText();
        if(passwordEntryText.isEmpty()) {
            Assert.fail("Password Text cannot be entered. User cannot proceed with Maturity Migration.");
        }

        sa.assertTrue(passwordEntryText.equals("••••••••••••"),
                "User could not enter Password text");

        maturityPageBase.getShowPwdToggle().click();

        sa.assertTrue(maturityPageBase.editTextByClass.getText().equals("fakePassword"),
                "User could not toggle password visibility");

        util.hideKeyboard();
        maturityPageBase.clickStandardButton();

        sa.assertEquals(maturityPageBase.getPasswordErrorText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, "invalidcredentials"),
                "Incorrect Password error was not displayed");

        maturityPageBase.editTextByClass.type(account.getUserPass());
        maturityPageBase.clickStandardButton();

        sa.assertTrue(maturityPageBase.isPinPageOpen(),
                "User was not directed to PIN entry after entering correct password");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69532"})
    @Test(description = "[Maturity] Profile Migration | Optionally Set Profile PIN - CREATE PROFILE PIN (Elements)", groups = {"Welch", "Profiles"})
    public void testProfilePinPageElements() {
        testSetUp();
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        SoftAssert sa = new SoftAssert();
        DisneyAccount account = createAccount();

        login(account, false);
        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStandardButton();
        maturityPageBase.editTextByClass.type(account.getUserPass());
        androidUtils.get().hideKeyboard();
        maturityPageBase.clickStandardButton();

        Assert.assertTrue(maturityPageBase.isPinPageOpen(),
                "Pin page not displayed");

        sa.assertTrue(maturityPageBase.isProfileInfoAvatarDisplayed(),
                "Profile avatar not displayed");

        sa.assertEquals(maturityPageBase.getProfileName(), PRIMARY_PROFILE_NAME,
                "Profile name not displayed");

        sa.assertTrue(maturityPageBase.isProfileRatingImageVisible(),
                "Profile rating image to be visible");

        sa.assertEquals(maturityPageBase.getMigrationPinTitleText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.SECURE_PROFILE_PIN_TITLE.getText()),
                "Secure pin title not displayed");

        sa.assertEquals(maturityPageBase.getMigrationSubtitle(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.SECURE_PROFILE_PIN_MATURITY_RATING.getText())
                        .replace("${highest_rating_value_text}", maxRating.get()),
                "Migration pin subtitle not displayed");

        sa.assertEquals(maturityPageBase.getPinHeaderText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.SECURE_PROFILE_PIN_ACTION.getText()),
                "Migration pin text is not displayed");

        sa.assertEquals(maturityPageBase.getCreatePinButtonText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_CREATE_PIN.getText()),
                "Create pin button text is not displayed");

        sa.assertTrue(maturityPageBase.isTextViewStringDisplayed(
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.BTN_NOT_NOW.getText())),
                "Not now button text is not visible");

        maturityPageBase.clickPositiveActionButton();
        maturityPageBase.enterProfilePin("1");
        maturityPageBase.clickPositiveActionButton();

        if(maturityPageBase.isEditTextFieldPresent()) {
            sa.fail("Password should not be prompted after submitting a PIN value");
            maturityPageBase.editTextByClass.type(account.getUserPass());
            androidUtils.get().hideKeyboard();
            maturityPageBase.clickStandardButton();
        }

        sa.assertEquals(
                maturityPageBase.getPinErrorText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.PROFILE_PIN_MISSING.getText()),
                "Profile pin error is not displayed");

        maturityPageBase.enterProfilePin("1234");

        sa.assertFalse(maturityPageBase.isPinErrorTextVisible(),
                "Pin error is visible");

        maturityPageBase.clickPositiveActionButton();

        initPage(DisneyPlusDiscoverPageBase.class).isOpened();

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71669"})
    @Test(description = "[Maturity] Profile Migration | Optionally Set Profile PIN - CREATE PROFILE PIN (Submit PIN - 1 Profile)", groups = {"Welch", "Profiles"})
    public void testPinSubmitOneProfile() {
        testSetUp();
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);

        AndroidUtilsExtended util = new AndroidUtilsExtended();
        DisneyAccount account = createAccount();
        login(account, false);
        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStandardButton();
        maturityPageBase.editTextByClass.type(account.getUserPass());
        util.hideKeyboard();
        maturityPageBase.clickStandardButton();

        Assert.assertTrue(maturityPageBase.isPinPageOpen(),
                "PIN Page did not load after password submit.");

        maturityPageBase.clickPositiveActionButton();
        maturityPageBase.enterProfilePin("1234");
        maturityPageBase.clickPositiveActionButton();
        maturityPageBase.dismissTravelingDialog();

        if(maturityPageBase.isEditTextFieldPresent()) {
            maturityPageBase.editTextByClass.type(account.getUserPass());
            util.hideKeyboard();
            maturityPageBase.clickStandardButton();
        }

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "Expected - App to land on Discover after submitting a PIN with only 1 adult profile");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71670"})
    @Test(description = "[Maturity] Profile Migration | Optionally Set Profile PIN - CREATE PROFILE PIN (Submit PIN - Multiple Profiles)", groups = {"Welch", "Profiles"})
    public void testPinSubmitMultiProfile() {
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);

        testSetUp();
        DisneyAccount account = createAccount();
        accountApi.get().addProfile(account, SECONDARY_PROFILE_NAME, ADDITIONAL_PROFILE_DOB,
                languageUtils.get().getUserLanguage(), null, false, false);
        account.getProfile(SECONDARY_PROFILE_NAME).setAttributes(setProfileMaturityRating(MATURITY_RATING_TV_14));
        login(account, true);
        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStandardButton();
        maturityPageBase.editTextByClass.type(account.getUserPass());
        androidUtils.get().hideKeyboard();
        maturityPageBase.clickStandardButton();

        Assert.assertTrue(maturityPageBase.isPinPageOpen(),
                "PIN Page did not load after password submit.");

        maturityPageBase.clickPositiveActionButton();
        maturityPageBase.enterProfilePin("1234");
        maturityPageBase.clickPositiveActionButton();

        Assert.assertTrue(maturityPageBase.isOtherProfilesMaturityPageOpen(),
                "App did not land on 'Update Other Profiles' after setting PIN");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71671"})
    @Test(description = "[Maturity] Profile Migration | Optionally Set Profile PIN - CREATE PROFILE PIN (Not Now - 1 Profile)", groups = {"Welch", "Profiles"})
    public void testPinRemindMeOneProfile() {
        testSetUp();
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        AndroidUtilsExtended util = new AndroidUtilsExtended();
        DisneyAccount account = createAccount();
        login(account, false);

        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStandardButton();
        maturityPageBase.editTextByClass.type(account.getUserPass());
        util.hideKeyboard();
        maturityPageBase.clickStandardButton();

        Assert.assertTrue(maturityPageBase.isPinPageOpen(),
                "PIN Page did not load after password submit.");

        maturityPageBase.clickPinNotNowButton();
        maturityPageBase.dismissTravelingDialog();

        Assert.assertTrue(initPage(DisneyPlusDiscoverPageBase.class).isOpened(),
                "Expected - Add Profiles banner to be displayed after landing on Discover with no other profiles");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71672"})
    @Test(description = "[Maturity] Profile Migration | Optionally Set Profile PIN - CREATE PROFILE PIN (Not Now - Multiple Profile)", groups = {"Welch", "Profiles"})
    public void testPinRemindMeMultiProfile() {
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);

        testSetUp();
        DisneyAccount account = createAccount();
        accountApi.get().addProfile(account, SECONDARY_PROFILE_NAME, ADDITIONAL_PROFILE_DOB,
                languageUtils.get().getUserLanguage(), null, false, false);
        account.getProfile(SECONDARY_PROFILE_NAME).setAttributes(setProfileMaturityRating(MATURITY_RATING_TV_14));
        login(account, true);
        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStandardButton();
        maturityPageBase.editTextByClass.type(account.getUserPass());
        androidUtils.get().hideKeyboard();
        maturityPageBase.clickStandardButton();

        Assert.assertTrue(maturityPageBase.isPinPageOpen(),
                "PIN Page did not load after password submit.");

        maturityPageBase.clickPinNotNowButton();

        Assert.assertTrue(maturityPageBase.isOtherProfilesMaturityPageOpen(),
                "App did not land on 'Update Other Profiles' after setting PIN");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71673"})
    @Test(description = "[Maturity] Profile Migration | Multi Profile | Update maturity ratings for other profiles - UI Check", groups = {"Welch", "Profiles"})
    public void testUpdateMaturityForOtherProfilesDisplay() {
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        SoftAssert sa = new SoftAssert();

        testSetUp();
        DisneyAccount account = createAccount();
        accountApi.get().addProfile(
                account, SECONDARY_PROFILE_NAME, ADDITIONAL_PROFILE_DOB, languageUtils.get().getUserLanguage(),
                null, false, false);
        accountApi.get().addProfile(
                account, TERTIARY_PROFILE_NAME, ADDITIONAL_PROFILE_DOB, languageUtils.get().getUserLanguage(),
                null, false, false);
        accountApi.get().addProfile(
                account, CHILD_PROFILE_NAME, CHILD_PROFILE_DOB, languageUtils.get().getUserLanguage(),
                null, true, false);
        account.getProfile(SECONDARY_PROFILE_NAME).setAttributes(setProfileMaturityRating(MATURITY_RATING_TV_14));
        account.getProfile(TERTIARY_PROFILE_NAME).setAttributes(setProfileMaturityRating(MATURITY_RATING_TV_14));
        account.getProfile(CHILD_PROFILE_NAME).setAttributes(setProfileMaturityRating(MATURITY_RATING_TV_14));
        login(account, true);
        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStandardButton();
        maturityPageBase.editTextByClass.type(account.getUserPass());
        androidUtils.get().hideKeyboard();
        maturityPageBase.clickStandardButton();
        maturityPageBase.clickPinNotNowButton();

        Assert.assertTrue(maturityPageBase.isOtherProfilesMaturityPageOpen(),
                "App did not land on 'Update Other Profiles' after setting PIN");

        sa.assertEquals(
                maturityPageBase.getMaturityRatingsTitleText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.UPDATE_OTHERS_TITLE.getText()),
                "Update Profiles header text is improperly displayed");

        sa.assertEquals(
                maturityPageBase.getMigrationSubtitle(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.UPDATE_OTHERS_DESC.getText()).replace(
                        "${highest_rating_value_text}", maxRating.get()),
                "Update Profiles sub header is improperly displayed");

        sa.assertTrue(maturityPageBase.isStandardButtonPresent(),
                "Continue button is not displayed");

        String setToRating =
                languageUtils.get().replaceValuePlaceholders(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.WELCH,
                                DictionaryKeys.UPDATE_OTHERS_SET_TO.getText()), maxRating.get());

        sa.assertEquals(maturityPageBase.getMaturityRatingsSetToText(), setToRating,
                "Update Profiles 'Set To' text is improperly displayed");

        sa.assertTrue(maturityPageBase.isProfileInfoAvatarDisplayed(),
                "Selected profile avatar is not visible");

        sa.assertEquals(maturityPageBase.getAdditionalAvatarsCount(), 2,
                "Secondary profile avatars are not displaying correctly");

        sa.assertTrue(
                maturityPageBase.isAdditionalProfileNameDisplayed(SECONDARY_PROFILE_NAME) &&
                        maturityPageBase.isAdditionalProfileNameDisplayed(TERTIARY_PROFILE_NAME),
                "All non-restricted profiles is not displayed");

        sa.assertTrue(
                maturityPageBase.isUpdateProfileTogglePresent(SECONDARY_PROFILE_NAME) &&
                        maturityPageBase.isUpdateProfileTogglePresent(TERTIARY_PROFILE_NAME),
                "All non-restricted profiles does not have a toggle switch present");

        sa.assertFalse(maturityPageBase.isAdditionalProfileNameDisplayed("Child"),
                "Restricted/Child accounts should not be displayed");

        sa.assertEquals(maturityPageBase.getProfileName(), PRIMARY_PROFILE_NAME,
                "Selected profile name is not properly displayed on PIN submission page");

        sa.assertTrue(maturityPageBase.isProfileRatingImageVisible(),
                "Profile rating image is not visible");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69513"})
    @Test(description = "[Maturity] Profile Migration | Multi Profile | Update maturity ratings for other profiles", groups = {"Welch", "Profiles"})
    public void testUpdateMaturityFunctions() {
        testSetUp();
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);

        DisneyAccount account = createAccount();
        SoftAssert sa = new SoftAssert();

        accountApi.get().addProfile(account, SECONDARY_PROFILE_NAME, null, "en", null, false, false);
        accountApi.get().addProfile(account, TERTIARY_PROFILE_NAME, null, "en", null, false, false);
        login(account, true);

        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStandardButton();
        maturityPageBase.editTextByClass.type(disneyAccount.get().getUserPass());
        androidUtils.get().hideKeyboard();
        maturityPageBase.clickStandardButton();
        maturityPageBase.clickPinNotNowButton();
        maturityPageBase.clickProfileRatingToggle(SECONDARY_PROFILE_NAME);
        maturityPageBase.clickStandardButton();
        maturityPageBase.dismissTravelingDialog();
        dismissChromecastOverlay();
        maturityPageBase.navigateToSettings();
        moreMenuPageBase.openEditProfile();
        moreMenuPageBase.getGenericAccessibilityidElement(languageUtils.get().getEditProfileString(SECONDARY_PROFILE_NAME)).click();

        sa.assertEquals(moreMenuPageBase.getProfileRatingText(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DisneyPlusMoreMenuPageBase.EditProfileKeys.CONTENT_RATING.getKey())),
                languageUtils.get().replaceValuePlaceholders(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DisneyPlusMoreMenuPageBase.EditProfileKeys.MATURITY_RATING_DESCRIPTION.getKey()),
                        ratingSystemValues.get(ratingSystemValues.size() - 1)), "Rating description incorrect");

        androidUtils.get().pressBack();
        moreMenuPageBase.getGenericAccessibilityidElement(languageUtils.get().getEditProfileString(TERTIARY_PROFILE_NAME)).click();

        sa.assertEquals(moreMenuPageBase.getProfileRatingText(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DisneyPlusMoreMenuPageBase.EditProfileKeys.CONTENT_RATING.getKey())),
                languageUtils.get().replaceValuePlaceholders(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DisneyPlusMoreMenuPageBase.EditProfileKeys.MATURITY_RATING_DESCRIPTION.getKey()),
                        ratingSystemValues.get(ratingSystemValues.size() - 3)), "Rating 14 description incorrect");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69515"})
    @Test(description = "[Maturity] Profile Migration | Homepage Maturity Rating Confirmation", groups = {"Welch", "Profiles"})
    public void testHomepageDefaultRatingBanner() {
        testSetUp();
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);

        SoftAssert sa = new SoftAssert();

        login(createAccount(), false);
        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStarNotNowBtn();
        maturityPageBase.clickStandardButton();
        androidUtils.get().hideKeyboard();

        maturityPageBase.dismissTravelingDialog();

        sa.assertTrue(maturityPageBase.isProfileInfoAvatarDisplayed(),
                "Profile avatar not displayed");

        sa.assertEquals(maturityPageBase.getProfileName(), PRIMARY_PROFILE_NAME,
                "Profile name display is incorrect");

        sa.assertTrue(maturityPageBase.isProfileRatingImageVisible(),
                "Profile default rating image not displayed");

        sa.assertEquals(maturityPageBase.getDefaultRatingHeader(), languageUtils.get()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_BANNER_HEADER.getText())
                        .replace("${current_rating_value_text}",  ratingSystemValues.get(ratingSystemValues.size() - 3)),
                "Default rating header is improperly displayed");

        sa.assertEquals(maturityPageBase.getDefaultRatingBody(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_BANNER_SUB.getText()),
                "Default rating body is improperly displayed");

        sa.assertTrue(maturityPageBase.isTextViewStringDisplayed(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_GOT_IT.getText())),
                "Default rating button text is improperly displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69518"})
    @Test(description = "[Maturity] Profile Migration | Single profile | Homepage Add Profile Banner", groups = {"Welch", "Profiles"})
    public void testAddProfilesBannerPopup() {
        testSetUp();
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        login(createAccount(), false);
        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStandardButton();
        maturityPageBase.editTextByClass.type(disneyAccount.get().getUserPass());
        androidUtils.get().hideKeyboard();
        maturityPageBase.clickStandardButton();
        maturityPageBase.clickPinNotNowButton();
        maturityPageBase.dismissTravelingDialog();

        Assert.assertTrue(discoverPageBase.isAddProfilesBannerVisible(),
                "Add profiles banner not displayed");

        sa.assertEquals(discoverPageBase.getAddProfileHeaderText(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.ADD_PROFILE_HEADER.getText()),
                "Add Profiles header is improperly displayed");

        sa.assertEquals(discoverPageBase.getAddProfileSubHeaderTeaxt(), languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.ADD_PRPFILE_SUB.getText()),
                "Add profiles sub header not displayed");

        sa.assertTrue(commonPageBase.genericTextElement.format(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_ADD_PROFILE.getText())).isElementPresent(),
                "Add profiles confirm button text is improperly displayed");

        sa.assertTrue(discoverPageBase.isAddProfilesCloseBtnPresent(),
                "Add profiles banner not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69531"})
    @Test(description = "[Maturity] Profile Migration | Decision made for profile | Skip Update Maturity Screen", groups = {"Welch", "Profiles"})
    public void testMigrationWithPriorSetting() {
        testSetUp();
        DisneyAccount account = createAccount();
        accountApi.get().editContentRatingProfileSetting(account, languageUtils.get().getRatingSystem(), maxRating.get());
        login(account, false);

        Assert.assertTrue(initPage(DisneyPlusMaturityPageBase.class).isPinPageOpen(),
                "Expected - User to land on PIN setting with maturity setting already applied");
    }
}
