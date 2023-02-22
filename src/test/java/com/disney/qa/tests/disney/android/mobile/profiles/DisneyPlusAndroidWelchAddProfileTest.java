package com.disney.qa.tests.disney.android.mobile.profiles;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.responses.profile.DisneyProfile;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.utils.DisneyContentApiChecker;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.jarvis.android.JarvisAndroidBase;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.disney.util.disney.ZebrunnerXrayLabels;
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
import java.util.Map;
import java.util.Objects;

public class DisneyPlusAndroidWelchAddProfileTest extends BaseDisneyTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String WELCH_PIN = "1234";
    private static final String TEST_PROFILE_NAME = "Test Profile";
    private static final int MAX_TITLES = 3;

    private List<String> ratingSystemValues;

    /**
     * Launches the app and signs in with the generated test account.
     * Dismisses chromecast overlay if it appears and initializes the pages.
     * Gets the max maturity rating image name for use if null.
     */
    private void testSetUp() {
        LOGGER.info("Setting up account...");
        CreateDisneyAccountRequest request = new CreateDisneyAccountRequest();
        JarvisAndroidBase jarvisAndroidBase = initPage(JarvisAndroidBase.class);

        request.addSku(DisneySkuParameters.DISNEY_IAP_GOOGLE_MONTHLY);
        request.addSku(DisneySkuParameters.DISNEY_D2C_FLEX_DATE);
        request.setCountry(languageUtils.get().getLocale());
        request.setLanguage(languageUtils.get().getUserLanguage());
        disneyAccount.set(accountApi.get().createAccount(request));
        DisneyProfile testProfile = disneyAccount.get().getProfile(DEFAULT_PROFILE);
        ratingSystemValues = getRefinedRatingSystemValues(DEFAULT_PROFILE);
        accountApi.get().editContentRatingProfileSetting(disneyAccount.get(),
                testProfile.getAttributes().getParentalControls().getMaturityRating().getRatingSystem(),
                ratingSystemValues.get(ratingSystemValues.size() - 1));
        activateRatingsJarvisOverrides(languageUtils.get().getLocale(), jarvisAndroidBase, true);

        login(disneyAccount.get(), false);

        LOGGER.info("Checking for Chromecast popup...");
        dismissChromecastOverlay();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69520"})
    @Test(description = "Update Maturity Rating - UI Element Checks", groups = {"Welch", "Profiles"})
    public void addProfileMaturityPageElementsTest() {
        testSetUp();
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1640"));

        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        SoftAssert sa = new SoftAssert();

        maturityPageBase.dismissTravelingDialog();
        addNewProfile(TEST_PROFILE_NAME);

        Assert.assertTrue(maturityPageBase.isMaturityRatingPageOpened(),
                "Maturity Setting page did not open after new profile creation");

        sa.assertTrue(maturityPageBase.isMaturitySettingProfileImageVisible(),
                "Profile Image not visible");

        sa.assertEquals(maturityPageBase.getMaturitySettingProfileName(), TEST_PROFILE_NAME,
                "Profile Name not displayed correctly");

        sa.assertEquals(maturityPageBase.getMaturitySettingHeaderText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_TITLE.getText()),
                "Maturity Settings title not properly displayed");

        sa.assertEquals(maturityPageBase.getMaturitySettingSubtitleText(), String.format("%s%s", StringUtils.substringBefore(
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_SUBTITLE.getText()), "$"),
                ratingSystemValues.get(ratingSystemValues.size() - 1).replace("+", ", ")),
                "Maturity Setting subtitle not displayed correctly");

        sa.assertTrue(maturityPageBase.isTextViewStringDisplayed(
                        languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_DESCRIPTION_VALUE_BULLET_ONE.getText())),
                "Maturity Setting description bullet one not displayed correctly");

        sa.assertTrue(maturityPageBase.isTextViewStringDisplayed(
                        languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_DESCRIPTION_VALUE_BULLET_TWO.getText())),
                "Maturity Setting description bullet two not displayed correctly");

        sa.assertEquals(maturityPageBase.getMaturityContinueButtonText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_FULL_CATALOG.getText()),
                "Maturity Setting 'FULL CATALOG' does not display correct text");

        sa.assertEquals(maturityPageBase.getMaturityNotNowButtonText(),
                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.BTN_NOT_NOW.getText()),
                "Maturity Setting not now does not display correct text");

        String notNowText = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH, DictionaryKeys.MATURITY_RATING_NOT_NOW_INFO.getText());
        notNowText = notNowText.replace(StringUtils.substringBetween(notNowText, "$", "}"), ratingSystemValues.get(ratingSystemValues.size() - 3))
                .replace("$", "")
                .replace("}", "");
        sa.assertEquals(maturityPageBase.getMaturitySettingsFooterText(), notNowText,
                "Maturity Setting footer text not displayed correctly");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69790"})
    @Test(description = "Update Maturity Rating - UPDATE NOW", groups = {"Welch", "Profiles"})
    public void maturityUpdateNowTest() {
        testSetUp();
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1641"));

        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        String testProfileName1 = "Update Now Show Password";
        String testProfileName2 = "Update Now Show PIN";
        String testProfileName3 = "Update Now Timeout";

        commonPageBase.dismissTravelingDialog();
        addNewProfile(testProfileName1);
        maturityPageBase.clickMaturityContinueButton();
        maturityPageBase.clickMaturityNotNowBtn(commonPageBase, languageUtils.get());
        moreMenuPageBase.clickOnGenericTextElement(testProfileName1);
        addNewProfile(testProfileName2);
        maturityPageBase.clickMaturityContinueButton();

        Assert.assertTrue(maturityPageBase.isPinPageOpen(),
                "Pin submission screen not displayed");

        maturityPageBase.clickPositiveActionButton();
        maturityPageBase.createProfilePin(WELCH_PIN);
        moreMenuPageBase.clickOnGenericTextElement(testProfileName1);
        addNewProfile(testProfileName3);
        maturityPageBase.isMaturityRatingPageOpened();
        closeAppForRelaunch();
        activityAndPackageLaunch();
        commonPageBase.clickStandardButton();
        maturityPageBase.clickFullCatalogBtn(commonPageBase,languageUtils.get());

        sa.assertTrue(maturityPageBase.isConfirmPasswordOpen(),
                "Password screen is not displayed");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69939"})
    @Test(description = "UPDATE NOW - Enter Password UI Element Checks", groups = {"Welch", "Profiles"})
    public void passwordEntryElementsTest() {
        testSetUp();
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1642"));

        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        commonPageBase.dismissTravelingDialog();
        addNewProfile(TEST_PROFILE_NAME);
        maturityPageBase.isMaturityRatingPageOpened();
        closeAppForRelaunch();
        activityAndPackageLaunch();
        commonPageBase.clickStandardButton();
        maturityPageBase.clickFullCatalogBtn(commonPageBase,languageUtils.get());

        Assert.assertTrue(maturityPageBase.isConfirmPasswordOpen(),
                "Password screen is not displayed");

        androidUtils.get().hideKeyboard();

        sa.assertTrue(maturityPageBase.isProfileInfoAvatarDisplayed(),
                "Profile avatar is not displayed");

        sa.assertTrue(
                maturityPageBase.getPasswordTitleText().equals(
                        languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                                DictionaryKeys.MATURITY_PASSWORD_TITLE.getText())),
                "Password header is not displayed");

        sa.assertTrue(
                maturityPageBase.getPasswordSubtitleText().equals(
                        StringUtils.substringBefore(
                                languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.WELCH,
                                        DictionaryKeys.MATURITY_RATING_PASSWORD_COPY.getText()), "$") +
                                ratingSystemValues.get(
                                        ratingSystemValues.size() - 1) + "."),
                "Profile password subtitle is not displayed");

        sa.assertTrue(maturityPageBase.isPasswordContinueButtonPresent(),
                "Password continue button is not displayed");

        sa.assertTrue(maturityPageBase.isForgotPasswordLinkPresent(),
                "Forgot password link is not displayed");

        sa.assertTrue(maturityPageBase.isGenericBackButtonPresent(),
                "Generic back Button is not displayed");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69791"})
    @Test(description = "Update Maturity Rating  - NOT NOW", groups = {"Welch", "Profiles"})
    public void maturityUpdateLaterTest() {
        testSetUp();

        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);

        discoverPageBase.dismissTravelingDialog();
        addNewProfile(TEST_PROFILE_NAME);
        maturityPageBase.clickMaturityNotNowButton();

        Assert.assertTrue(moreMenuPageBase.isGenericTextPresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.WhosWatchingKeys.WHOS_WATCHING_TITLE.getKey())),
                "Who's watching screen not displayed");

        Assert.assertTrue(moreMenuPageBase.isProfileVisible(TEST_PROFILE_NAME),
                "Profile name not displayed");

        Assert.assertFalse(moreMenuPageBase.isProfileLockVisible(TEST_PROFILE_NAME),
                "Pin lock is displayed");

        moreMenuPageBase.clickOnGenericTextElement(TEST_PROFILE_NAME);
        moreMenuPageBase.clickStandardButton();
        discoverPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.openEditProfile();
        moreMenuPageBase.clickOnGenericTextElement(TEST_PROFILE_NAME);

        Assert.assertEquals(moreMenuPageBase.getProfileRatingText(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DisneyPlusMoreMenuPageBase.EditProfileKeys.CONTENT_RATING.getKey())),
                languageUtils.get().replaceValuePlaceholders(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DisneyPlusMoreMenuPageBase.EditProfileKeys.MATURITY_RATING_DESCRIPTION.getKey()),
                        ratingSystemValues.get(ratingSystemValues.size() - 3)), "Rating 14 description incorrect");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69527"})
    @Test(description = "Optionally Set Profile PIN - UI Element Checks", groups = {"Welch", "Profiles"})
    public void setProfilePinElementsTest() {
        testSetUp();
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1643"));

        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        commonPageBase.dismissTravelingDialog();

        String ratingValues =
                ratingSystemValues.get(ratingSystemValues.size() - 1);
        List<String> maxRatedTitles = getMediaWithDesiredRating(ratingValues);
        String maxRatingImageName = getRatingImageName(maxRatedTitles, ratingValues);

        addNewProfile(TEST_PROFILE_NAME);
        maturityPageBase.clickFullCatalogBtn(commonPageBase,languageUtils.get());

        Assert.assertTrue(maturityPageBase.isPinPageOpen(),
                "Pin page is not displayed");

        sa.assertTrue(maturityPageBase.isProfileInfoAvatarDisplayed(),
                "Profile image is not displayed");

        sa.assertEquals(maturityPageBase.getProfileName(), TEST_PROFILE_NAME,
                "Profile name is not displayed");

        sa.assertEquals(maturityPageBase.getPinPageMaxRatingImageName(), maxRatingImageName,
                "Profile max rating image name not displayed");

        sa.assertTrue(maturityPageBase.isLockIconVisible(),
                "Lock icon graphic is not displayed");

        sa.assertEquals(
                maturityPageBase.getPinHeaderText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.SECURE_PROFILE_PIN_ACTION.getText()),
                "Pin page header text does not match dictionary key reference");

        sa.assertTrue(maturityPageBase.isPinNotNowButtonPresent(),
                "Pin not now button is not displayed");

        Assert.assertTrue(maturityPageBase.isCreatePinButtonPresent(),
                "Pin page confirmation button is not displayed");

        sa.assertFalse(maturityPageBase.isPinErrorTextVisible(),
                "Pin error message is displayed");

        maturityPageBase.clickPositiveActionButton();

        sa.assertEquals(
                maturityPageBase.getPinPageDescriptionText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.SECURE_PROFILE_PIN_DESCRIPTION.getText()),
                "Pin page description does not match dictionary key reference");

        sa.assertTrue(maturityPageBase.isPinInputDisplayCorrect(),
                "Pin input to display 4 boxes");

        maturityPageBase.editTextByClass.type(disneyAccount.get().getUserPass());
        maturityPageBase.clickStandardButton();

        sa.assertTrue(maturityPageBase.isPinErrorTextVisible(),
                "Pin error text is not displayed");

        sa.assertEquals(
                maturityPageBase.getPinErrorText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.WELCH,
                        DictionaryKeys.SECURE_PROFILE_PIN_ERROR.getText()),
                "Pin error does not match dictionary key reference");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69792"})
    @Test(description = "Optionally Set Profile PIN - SET PROFILE PIN", groups = {"Welch", "Profiles"})
    public void setProfilePinNowTest() {
        testSetUp();
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1644"));

        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        commonPageBase.dismissTravelingDialog();
        addNewProfile(TEST_PROFILE_NAME);
        maturityPageBase.isMaturityRatingPageOpened();
        closeAppForRelaunch();
        activityAndPackageLaunch();
        commonPageBase.clickStandardButton();
        maturityPageBase.clickFullCatalogBtn(commonPageBase,languageUtils.get());

        Assert.assertTrue(maturityPageBase.isConfirmPasswordOpen(),
                "Password input screen not displayed");

        maturityPageBase.getEditTextByClass().type(disneyAccount.get().getUserPass());
        androidUtils.get().hideKeyboard();
        maturityPageBase.clickPasswordContinueButton();
        maturityPageBase.clickPositiveActionButton();
        maturityPageBase.clickPositiveActionButton(); //enter empty pin submission

        sa.assertTrue(maturityPageBase.isPinErrorTextVisible(),
                "Pin error not displayed");

        sa.assertFalse(
                maturityPageBase.enterPinSequence(3).containsValue(true),
                "Pin error displayed during pin sequence: "
                        + maturityPageBase.enterPinSequence(3));

        maturityPageBase.clickPositiveActionButton();
        sa.assertTrue(maturityPageBase.isPinErrorTextVisible(),
                "Pin error not displayed from entry");

        maturityPageBase.createProfilePin(WELCH_PIN);

        Assert.assertTrue(
                moreMenuPageBase.isGenericTextPresent(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DisneyPlusCommonPageBase.WhosWatchingKeys.WHOS_WATCHING_TITLE.getKey())),
                "Who's watching screen not displayed");

        Assert.assertTrue(moreMenuPageBase.isProfileVisible(TEST_PROFILE_NAME),
                "New profile name not displayed");

        Assert.assertTrue(moreMenuPageBase.isProfileLockVisible(TEST_PROFILE_NAME),
                "Profile lock is not displayed");

        moreMenuPageBase.selectWhosWatchingProfile(TEST_PROFILE_NAME);

        Assert.assertTrue(maturityPageBase.isEnterPinTitleDisplayed(),
                "Enter pin text not displayed");

        maturityPageBase.enterProfilePin(WELCH_PIN);
        dismissChromecastOverlay();

        Assert.assertTrue(discoverPageBase.isOpened(),
                "Discover screen not displayed");

        discoverPageBase.getHeroCarousel().click();
        initPage(DisneyPlusMediaPageBase.class).startPlayback();

        Assert.assertTrue(initPage(DisneyPlusVideoPageBase.class).isOpened(),
                "Video player is not displayed");

        checkAssertions(sa);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-69793"})
    @Test(description = "Optionally Set Profile PIN - MAYBE LATER", groups = {"Welch", "Profiles"})
    public void setProfilePinLaterTest() {
        testSetUp();
        setHoraZebrunnerLabels(new ZebrunnerXrayLabels(ANALYTICS_PROJECT_KEY, ANALYTICS_COUNTRY_CODE, "XAQA-1645"));

        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusMoreMenuPageBase moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        commonPageBase.dismissTravelingDialog();
        addNewProfile(TEST_PROFILE_NAME);
        maturityPageBase.clickMaturityNotNowBtn(commonPageBase, languageUtils.get());

        Assert.assertTrue(
                moreMenuPageBase.isGenericTextPresent(
                        languageUtils.get().getDictionaryItem(
                                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DisneyPlusCommonPageBase.WhosWatchingKeys.WHOS_WATCHING_TITLE.getKey())),
                "Who's watching screen not displayed");

        sa.assertFalse(moreMenuPageBase.isProfileLockVisible(TEST_PROFILE_NAME),
                "Pin lock icon not displayed");

        discoverPageBase.selectWhosWatchingProfile(TEST_PROFILE_NAME);
        maturityPageBase.clickGotItBtn(commonPageBase,languageUtils.get());

        sa.assertTrue(discoverPageBase.isOpened(),
                "Discover screen not displayed");

        checkAssertions(sa);
    }

    private List<String> getMediaWithDesiredRating(String rating) {
        List<String> titles = new ArrayList<>();
        LOGGER.info("Scanning API for {} titles with desired rating '{}'", MAX_TITLES, rating);
        DisneyContentApiChecker apiChecker = new DisneyContentApiChecker(PLATFORM, ENV, PARTNER);
        for (int i = 0; i < MAX_TITLES; i++) {
            Map<String, String> item = apiChecker.findMediaByRating(rating, languageUtils.get().getUserLanguage(), languageUtils.get().getLocale(), "program", titles);
            if(item.get(rating) == null){
                item = apiChecker.findMediaByRating(rating, languageUtils.get().getUserLanguage(), languageUtils.get().getLocale(), "series", titles);
            }
            titles.add(item.get(rating));
        }
        titles.removeIf(Objects::isNull);

        return titles;
    }

    private String getRatingImageName(List<String> titles, String rating) {
        initPage(DisneyPlusDiscoverPageBase.class).navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        AndroidUtilsExtended utilsExtended = new AndroidUtilsExtended();

        for (String title : titles) {
            searchPageBase.searchForMedia(title);
            searchPageBase.openSearchResult(title);
            DisneyPlusMediaPageBase mediaPageBase = initPage(DisneyPlusMediaPageBase.class);
            if(mediaPageBase.isOpened() && mediaPageBase.getMediaTitle().equals(title)) {
                String mediaRating = mediaPageBase.getMediaDetailsRating(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, "nav_details"));

                utilsExtended.pressBack();
                if (mediaRating.equals(rating)) {
                    return mediaRating;
                }
            }
        }
        return null;
    }
}
