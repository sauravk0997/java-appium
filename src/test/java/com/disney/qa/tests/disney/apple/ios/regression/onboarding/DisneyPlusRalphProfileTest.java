package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.DisneyGlobalUtils;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.RatingConstant.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;

public class DisneyPlusRalphProfileTest extends DisneyBaseTest {

    private static final int THIRTEEN_YEARS_AGE = 13;
    private static final int[] AGE_VALUES_GERMANY = {5, 11, 15, 17};
    private static final int[] AGE_VALUES_CANADA = {0, 5, 8, 11, 13, 15, 17, 18};
    private static final int[] AGE_VALUES_EMEA = {5, 8, 11, 13, 15, 17, 18};
    private static final String RATING_VALUES = "ratingValues";
    private static final String RECOMMENDED_RATING_ERROR_MESSAGE = "Recommended rating is not present";
    private static final String CODE = "code";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74028"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void testConditionalOneTrustInitializationAcceptAll() {
        setupForRalph();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        sa.assertTrue(oneTrustPage.isOpened(), "One trust page is not opened");
        sa.assertTrue(oneTrustPage.isRejectAllButtonPresent(), "Reject all button is not present on one trust banner");
        sa.assertTrue(oneTrustPage.isAllowAllButtonPresent(),"Accept all button is not present on one trust banner");
        sa.assertTrue(oneTrustPage.isCustomizedChoicesButtonPresent(),"Customized choices button is not present on one trust banner");
        sa.assertTrue(oneTrustPage.isListOfVendorsLinkPresent(),"List of vendors link is not present on one trust banner");
        oneTrustPage.tapAcceptAllButton();
        sa.assertTrue(welcomePage.isOpened(), "Welcome page is not opened");

        welcomePage.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        passwordPage.clickPrimaryButton();
        pause(5);
        homePage.isOpened();
        pause(3);
        sa.assertFalse(oneTrustPage.isOpened(), "One trust page is present after login");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75154"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void testConditionalOneTrustInitializationCustomizeChoice() {
        setupForRalph();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        sa.assertTrue(oneTrustPage.isOpened(), "One trust page is not opened");
        sa.assertTrue(oneTrustPage.isCustomizedChoicesButtonPresent(),"Customized choices button is not present on one trust banner");
        oneTrustPage.tapCustomizedChoices();
        sa.assertTrue(oneTrustPage.isPrivacyPreferenceCenterOpen(), "Privacy Preference page is not opened");
        oneTrustPage.tapConfirmMyChoiceButton();
        sa.assertTrue(welcomePage.isOpened(), "Welcome page is not opened");
        welcomePage.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        passwordPage.clickPrimaryButton();
        homePage.waitForHomePageToOpen();
        homePage.isOpened();
        pause(3);
        sa.assertFalse(oneTrustPage.isOpened(), "One trust page is present after login");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75153"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void testConditionalOneTrustInitializationRejectAll() {
        setupForRalph();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        sa.assertTrue(oneTrustPage.isOpened(), "One trust page is not opened");
        sa.assertTrue(oneTrustPage.isRejectAllButtonPresent(), "Reject all button is not present on one trust banner");
        oneTrustPage.tapRejectAllButton();
        sa.assertFalse(oneTrustPage.isOpened(), "One trust page is present, reject all button was not actioned");
        sa.assertTrue(welcomePage.isOpened(), "Welcome page is not opened");
        welcomePage.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        passwordPage.clickPrimaryButton();
        pause(5);
        homePage.waitForHomePageToOpen();
        homePage.isOpened();
        pause(3);
        sa.assertFalse(oneTrustPage.isOpened(), "One trust page is present after login");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73921"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void testSuppressGenderOnEditProfileForSingleProfile() {
        setupForRalph();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);

        oneTrustPage.tapRejectAllButton();
        loginPage.dismissAppTrackingPopUp();

        welcomePage.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        passwordPage.clickPrimaryButton();
        pause(5);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(DEFAULT_PROFILE);
        // verify that Gender field is not present
        Assert.assertFalse(editProfilePage.isGenderButtonPresent(), "Gender Field is shown on edit profile page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74084"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, US}, enabled = false)
    public void testSuppressGenderOnEditProfileOnSecondaryProfile() {
        setupForRalph();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        CreateDisneyProfileRequest createDisneyProfileRequest = new CreateDisneyProfileRequest();

        createDisneyProfileRequest
                .setDisneyAccount(getAccount())
                .setProfileName(SECONDARY_PROFILE)
                .setDateOfBirth("1995-01-01")
                .setAvatarId(R.TESTDATA.get("disney_darth_maul_avatar_id"))
                .setGender(null)
                .setKidsModeEnabled(false)
                .setStarOnboarded(false)
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());
        getAccountApi().addProfile(createDisneyProfileRequest);

        oneTrustPage.tapRejectAllButton();
        loginPage.dismissAppTrackingPopUp();
        welcomePage.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        passwordPage.clickPrimaryButton();
        pause(5);
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(SECONDARY_PROFILE);
        // verify that Gender field is not present
        Assert.assertFalse(editProfilePage.isGenderButtonPresent(), "Gender Field is shown on edit profile page");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75282"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testRalphAddProfileJuniorModeDOBIsDisabled() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        String toggleOn = "On";
        String toggleOff = "Off";
        SoftAssert sa = new SoftAssert();
        // Disable one trust banner Jarvis config and set account
        jarvisDisableOneTrustBanner();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_ADS_MONTHLY,
                GERMANY, getLocalizationUtils().getUserLanguage()));
        getAccountApi().overrideLocations(getAccount(), GERMANY);

        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        setAppToHomeScreen(getAccount());
        // Add profile
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        Assert.assertTrue(chooseAvatar.isOpened(), "Choose Avatar screen was not opened");
        addProfile.getCellsWithLabels().get(0).click();
        addProfile.enterProfileName(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());

        // Validate Junior Mode toggle and toggled it ON
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), toggleOff, "Junior Mode is not toggled OFF");
        addProfile.tapJuniorModeToggle();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), toggleOn, "Profile is converted to General Audience");

        // Validate Content Rating and Birthdate are disabled
        sa.assertTrue(addProfile.isDateOfBirthFieldPresent(), "DOB field is not present");
        addProfile.getDynamicTextEntryFieldByName(addProfile.getBirthdateTextField()).click();
        Assert.assertFalse(editProfile.getTypeButtonByLabel("Done").isPresent(), "Date of birth is not disabled");
        Assert.assertTrue(addProfile.getChooseContentRating().isPresent(), "Choose content is not disabled");

        // Toggle Junior Mode OFF and validate content
        addProfile.tapJuniorModeToggle();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), toggleOff, "Junior Mode is not toggled OFF");
        Assert.assertTrue(addProfile.getValueFromDOB().isPresent(),
                "Date Of Birth field did not get empty after toggle Junior Mode OFF");
        Assert.assertTrue(addProfile.getChooseContentRating().isPresent(),
                "Choose Content Rating did not get empty after toggle Junior Mode OFF");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75283"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testRalphAddProfileJuniorModeDateOfBirth() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);

        SoftAssert sa =  new SoftAssert();
        String EXPECTED_RATING = "12";

        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_ADS_MONTHLY,
                GERMANY, getLocalizationUtils().getUserLanguage()));
        getAccountApi().overrideLocations(getAccount(), GERMANY);
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(getAccount())
                .profileName(JUNIOR_PROFILE).dateOfBirth(KIDS_DOB).language(getAccount().getProfileLang())
                .avatarId(BABY_YODA).kidsModeEnabled(false).isStarOnboarded(true).build());
        setAppToHomeScreen(getAccount());
        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }

        whoIsWatching.clickProfile(DEFAULT_PROFILE);
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(JUNIOR_PROFILE);
        swipeUp(2, 500);
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), "Off", "Junior Mode is not toggled OFF");
        editProfile.toggleJuniorMode();
        editProfile.waitForUpdatedToastToDisappear();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), "On",
                "Profile is converted to General Audience");
        swipeDown(2, 500);
        sa.assertTrue(editProfile.isDateFieldNotRequiredLabelPresent(),
                "Birthdate field did not change to 'Not Required'");
        swipeUp(2, 500);
        editProfile.toggleJuniorMode();
        passwordPage.enterPassword(getAccount());
        editProfile.waitForUpdatedToastToDisappear();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), "Off", "Junior Mode is not toggled OFF");
        swipeUp(2, 500);
        sa.assertTrue(editProfile.verifyProfileSettingsMaturityRating(EXPECTED_RATING), "Profile rating is not as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74004"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testRalphContentSliderMaturityGermany() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRating =   initPage(DisneyPlusContentRatingIOSPageBase.class);

        String recommendedContentRatingByAge = getLocalizationUtils().formatPlaceholderString(contentRating.getRecommendedRating(),
                Map.of("content_rating", getRecommendedContentRating(GERMANY, THIRTEEN_YEARS_AGE, AGE_VALUES_GERMANY)));
        LOGGER.info("RecommendedContentRating {} ", recommendedContentRatingByAge);
        jarvisDisableOneTrustBanner();
        createAccountAndAddSecondaryProfile(GERMANY, ENGLISH_LANG);
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        setAppToHomeScreen(getAccount());
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        updateProfilePage.tapSaveButton();
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        navigateToContentRating();
        swipeUp(2, 500);
        Assert.assertTrue(whoIsWatching.getStaticTextByLabelContains(recommendedContentRatingByAge).isPresent(),
                RECOMMENDED_RATING_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74273"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testRalphContentSliderMaturityCanada() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRating =   initPage(DisneyPlusContentRatingIOSPageBase.class);

        String recommendedContentRatingByAge = getLocalizationUtils().formatPlaceholderString(contentRating.getRecommendedRating(),
                Map.of("content_rating", getRecommendedContentRating(CANADA, 17, AGE_VALUES_CANADA)));
        LOGGER.info("RecommendedContentRating {} ", recommendedContentRatingByAge);
        jarvisDisableOneTrustBanner();
        createAccountAndAddSecondaryProfile(CANADA, ENGLISH_LANG);
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        setAppToHomeScreen(getAccount());
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.U18.getMonth(), Person.U18.getDay(), Person.U18.getYear());
        updateProfilePage.tapSaveButton();
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        navigateToContentRating();
        swipeUp(2, 500);
        Assert.assertTrue(whoIsWatching.getStaticTextByLabelContains(recommendedContentRatingByAge).isPresent(),
                RECOMMENDED_RATING_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74104"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, US})
    public void testRalphContentSliderMaturityEMEA() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRating =   initPage(DisneyPlusContentRatingIOSPageBase.class);

        String recommendedContentRatingByAge = getLocalizationUtils().formatPlaceholderString(contentRating.getRecommendedRating(),
                Map.of("content_rating", getRecommendedContentRating(UNITED_KINGDOM, 5, AGE_VALUES_EMEA)));
        LOGGER.info("RecommendedContentRating {} ", recommendedContentRatingByAge);
        // Validation for 0 Rating because in screen appears AL Rating in slider
        if(recommendedContentRatingByAge.contains("0 (Recommended)")) {
            recommendedContentRatingByAge = "AL (Recommended)";
        }
        jarvisDisableOneTrustBanner();
        createAccountAndAddSecondaryProfile(UNITED_KINGDOM, ENGLISH_LANG);
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        setAppToHomeScreen(getAccount());
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.MINOR.getMonth(), Person.MINOR.getDay(), Person.MINOR.getYear());
        updateProfilePage.tapSaveButton();
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        navigateToContentRating();
        swipeUp(2, 500);
        Assert.assertTrue(whoIsWatching.getStaticTextByLabelContains(recommendedContentRatingByAge).isPresent(),
                RECOMMENDED_RATING_ERROR_MESSAGE);
    }


    private void navigateToContentRating() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(JUNIOR_PROFILE);
        swipe(editProfile.getMaturityRatingLabel(), Direction.UP, 2, 500);
        editProfile.getMaturityRatingCell().click();
        editProfile.enterPassword(getAccount());
    }

    private void createAccountAndAddSecondaryProfile(String locale, String language) {
        String DARTH_MAUL = R.TESTDATA.get("disney_darth_maul_avatar_id");
        // Create standard account with Ads subscription
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_ADS_MONTHLY,
                locale, language));
        getAccountApi().overrideLocations(getAccount(), locale);
        // Create secondary profile with no DOB and gender
        getAccountApi().addProfile(
                CreateDisneyProfileRequest.builder().disneyAccount(getAccount()).profileName(JUNIOR_PROFILE)
                        .gender(null).language(getAccount().getProfileLang()).avatarId(DARTH_MAUL)
                        .dateOfBirth(null).kidsModeEnabled(false).isStarOnboarded(false).build());
    }

    private String getRecommendedContentRating(String locale, int age, int[] ageValues) {
        DisneyCountryData disneyCountryData = new DisneyCountryData();
        List<String> ratingValues = (List<String>)disneyCountryData.searchAndReturnCountryData(locale, CODE, RATING_VALUES);
        LOGGER.info("Ratings values {} ", ratingValues);
        for (int i = 0; i < ageValues.length; i++) {
            if (age <= ageValues[i]) {
                return ratingValues.get(i);
            }
        }
        return ratingValues.get(ratingValues.size() - 1);
    }

    private void  setupForRalph(String... DOB) {
        String locale = getLocalizationUtils().getLocale();
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();

        createDisneyAccountRequest
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());
        // Depending on the test scenario we need to set the DOB to
        // 1. certain age or
        // 2. set to null - to trigger the data collection flow
        // 3. Don't add the DOB param in test to set the default DOB from the account api
        if (null!= DOB && DOB.length > 0) {
            createDisneyAccountRequest.setDateOfBirth(DOB[0]);
        } else if(DOB == null) {
            createDisneyAccountRequest.setDateOfBirth(null);
        }
        DisneyOffer disneyOffer = getAccountApi().lookupOfferToUse(locale, "Disney Plus Standard W Ads Monthly - DE - Web");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V2").build();
        createDisneyAccountRequest.addEntitlement(entitlement);
        DisneyAccount testAccount = getAccountApi().createAccount(createDisneyAccountRequest);
        getAccountApi().addFlex(testAccount);
        setAccount(testAccount);
        handleAlert();
    }
}
