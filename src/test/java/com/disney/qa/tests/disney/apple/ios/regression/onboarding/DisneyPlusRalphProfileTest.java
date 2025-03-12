package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.api.client.requests.*;
import com.disney.qa.api.dictionary.*;
import com.disney.qa.api.offer.pojos.*;
import com.disney.qa.api.pojos.*;
import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;
import java.util.Map;

import static com.disney.qa.common.DisneyAbstractPage.FIVE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.common.constant.RatingConstant.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.RAYA;

public class DisneyPlusRalphProfileTest extends DisneyBaseTest {

    private static final int[] AGE_VALUES_GERMANY = {5, 11, 15, 17};
    private static final int[] AGE_VALUES_CANADA = {0, 5, 8, 11, 13, 15, 17, 18};
    private static final int[] AGE_VALUES_EMEA = {5, 8, 11, 13, 15, 17, 18};
    private static final String CODE = "code";
    private static final String RATING_VALUES = "ratingValues";
    private static final String RECOMMENDED_RATING_ERROR_MESSAGE = "Recommended rating is not present";
    public static final String DOB_PAGE_NOT_DISPLAYED = "DOB Collection Page is not displayed";

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
        sa.assertTrue(oneTrustPage.isAllowAllButtonPresent(), "Accept all button is not present on one trust banner");
        sa.assertTrue(oneTrustPage.isCustomizedChoicesButtonPresent(), "Customized choices button is not present on one trust banner");
        sa.assertTrue(oneTrustPage.isListOfVendorsLinkPresent(), "List of vendors link is not present on one trust banner");
        oneTrustPage.tapAcceptAllButton();
        sa.assertTrue(welcomePage.isOpened(), "Welcome page is not opened");

        welcomePage.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());
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
        sa.assertTrue(oneTrustPage.isCustomizedChoicesButtonPresent(), "Customized choices button is not present on one trust banner");
        oneTrustPage.tapCustomizedChoices();
        sa.assertTrue(oneTrustPage.isPrivacyPreferenceCenterOpen(), "Privacy Preference page is not opened");
        oneTrustPage.tapConfirmMyChoiceButton();
        sa.assertTrue(welcomePage.isOpened(), "Welcome page is not opened");
        welcomePage.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());
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
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());
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
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());
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

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(ADULT_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(RAYA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());

        oneTrustPage.tapRejectAllButton();
        loginPage.dismissAppTrackingPopUp();

        welcomePage.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());
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
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, DE})
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

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD, GERMANY,
                getLocalizationUtils().getUserLanguage())));

        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), GERMANY);

        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        setAppToHomeScreen(getUnifiedAccount());

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
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, DE})
    public void testRalphAddProfileJuniorModeDateOfBirth() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String EXPECTED_RATING = "12";

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), DE);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), GERMANY);

        setAppToHomeScreen(getUnifiedAccount());

        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        //Dismiss ATT Popup
        if (isAlertPresent()) {
            handleGenericPopup(5, 1);
        }
        Assert.assertTrue(whoIsWatching.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        //Dismiss ATT Popup
        if (isAlertPresent()) {
            handleGenericPopup(5, 1);
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
        passwordPage.enterPassword(getUnifiedAccount());
        editProfile.waitForUpdatedToastToDisappear();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), "Off", "Junior Mode is not toggled OFF");
        swipeUp(2, 500);
        sa.assertTrue(editProfile.verifyProfileSettingsMaturityRating(EXPECTED_RATING), "Profile rating is not as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74004"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, DE})
    public void testRalphContentSliderMaturityGermany() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRating = initPage(DisneyPlusContentRatingIOSPageBase.class);

        int under13Age = calculateAge(Person.U13.getMonth(), Person.U13);
        String recommendedContentRatingByAge = getLocalizationUtils().formatPlaceholderString(contentRating.getRecommendedRating(),
                Map.of("content_rating", getRecommendedContentRating(GERMANY, under13Age, AGE_VALUES_GERMANY)));
        LOGGER.info("Recommended Content Rating: {}", recommendedContentRatingByAge);

        jarvisDisableOneTrustBanner();

        createAccountAndAddSecondaryProfile(GERMANY, getLocalizationUtils().getUserLanguage());
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        setAppToHomeScreen(getUnifiedAccount());
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        updateProfilePage.tapSaveButton();
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        navigateToContentRating();
        swipe(contentRating.getRecommendedText(), Direction.UP, 2, 500);
        Assert.assertTrue(whoIsWatching.getStaticTextByLabelContains(recommendedContentRatingByAge).isPresent(),
                RECOMMENDED_RATING_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74273"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, CA})
    public void testRalphContentSliderMaturityCanada() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRating = initPage(DisneyPlusContentRatingIOSPageBase.class);

        int under18Age = calculateAge(Person.AGE_17.getMonth(), Person.AGE_17);
        String recommendedContentRatingByAge = getLocalizationUtils().formatPlaceholderString(contentRating.getRecommendedRating(),
                Map.of("content_rating", getRecommendedContentRating(CANADA, under18Age, AGE_VALUES_CANADA)));
        LOGGER.info("Recommended Content Rating: {}", recommendedContentRatingByAge);
        jarvisDisableOneTrustBanner();

        createAccountAndAddSecondaryProfile(CANADA, getLocalizationUtils().getUserLanguage());
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);

        setAppToHomeScreen(getUnifiedAccount());
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.AGE_17.getMonth(), Person.AGE_17.getDay(), Person.AGE_17.getYear());
        updateProfilePage.tapSaveButton();
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        navigateToContentRating();
        swipe(contentRating.getRecommendedText(), Direction.UP, 2, 500);
        Assert.assertTrue(whoIsWatching.getStaticTextByLabelContains(recommendedContentRatingByAge).isPresent(),
                RECOMMENDED_RATING_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74104"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, DE})
    public void testRalphContentSliderMaturityEMEA() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRating = initPage(DisneyPlusContentRatingIOSPageBase.class);

        int minorAge = calculateAge(Person.MINOR.getMonth(), Person.MINOR);
        String recommendedContentRatingByAge = getLocalizationUtils().formatPlaceholderString(contentRating.getRecommendedRating(),
                Map.of("content_rating", getRecommendedContentRating(UNITED_KINGDOM, minorAge, AGE_VALUES_EMEA)));
        LOGGER.info("Recommended Content Rating: {} ", recommendedContentRatingByAge);
        // Validation for 0 Rating because in screen appears AL Rating in slider
        if (recommendedContentRatingByAge.contains("0 (Recommended)")) {
            recommendedContentRatingByAge = "AL (Recommended)";
        }
        jarvisDisableOneTrustBanner();
        createAccountAndAddSecondaryProfile(getLocalizationUtils().getLocale(), DE);
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        setAppToHomeScreen(getUnifiedAccount());
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.MINOR.getMonth(), Person.MINOR.getDay(), Person.MINOR.getYear());
        updateProfilePage.tapSaveButton();
        whoIsWatching.clickProfile(JUNIOR_PROFILE);
        navigateToContentRating();
        swipe(contentRating.getRecommendedText(), Direction.UP, 2, 500);
        Assert.assertTrue(whoIsWatching.getStaticTextByLabelContains(recommendedContentRatingByAge).isPresent(),
                RECOMMENDED_RATING_ERROR_MESSAGE);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74308"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, CA})
    public void testRalphAddProfileSuggestMatureContentRating() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRating = initPage(DisneyPlusContentRatingIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        int age = 59;

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), CA);

        String recommendedContentRatingByAge = getLocalizationUtils().formatPlaceholderString(contentRating.getRecommendedRating(),
                Map.of("content_rating", getRecommendedContentRating(CANADA, age, AGE_VALUES_CANADA)));
        LOGGER.info("RecommendedContentRating {}", recommendedContentRatingByAge);
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
        setAppToHomeScreen(getUnifiedAccount());
        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        homePage.clickMoreTab();
        moreMenu.clickAddProfile();
        Assert.assertTrue(chooseAvatar.isOpened(), "Choose Avatar screen was not opened");
        addProfile.clickSkipBtn();
        addProfile.enterProfileName(SECONDARY_PROFILE);
        addProfile.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        Assert.assertTrue(contentRating.isContentRatingPresent(), "Content rating not displayed");
        Assert.assertTrue(addProfile.getStaticTextByLabelContains(recommendedContentRatingByAge).isPresent(),
                RECOMMENDED_RATING_ERROR_MESSAGE);
        Assert.assertTrue(addProfile.isContentRatingDropdownEnabled(recommendedContentRatingByAge),
                "Content rating dropdown is not enabled");
        addProfile.clickSaveProfileButton();
        Assert.assertTrue(moreMenu.getStaticTextByNameContains(SECONDARY_PROFILE).isPresent(),
                "New secondary user was not saved");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75290"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, CA})
    public void testRalphAddProfileValidateDropdownContentRating() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRating = initPage(DisneyPlusContentRatingIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        String ratingChoose = "TV-Y";
        int age = 59;

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(
                DISNEY_PLUS_STANDARD,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), CA);

        String recommendedContentRatingByAge = getLocalizationUtils().formatPlaceholderString(contentRating.getRecommendedRating(),
                Map.of("content_rating", getRecommendedContentRating(CANADA, age, AGE_VALUES_CANADA)));
        LOGGER.info("RecommendedContentRating {}", recommendedContentRatingByAge);

        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        setAppToHomeScreen(getUnifiedAccount());
        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        //Dismiss ATT Popup
        if (isAlertPresent()) {
            handleGenericPopup(5, 1);
        }
        homePage.clickMoreTab();
        moreMenu.clickAddProfile();
        Assert.assertTrue(chooseAvatar.isOpened(), "Choose Avatar screen was not opened");
        addProfile.clickSkipBtn();
        addProfile.enterProfileName(SECONDARY_PROFILE);
        addProfile.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        Assert.assertTrue(contentRating.isContentRatingPresent(), "Content rating not displayed");
        Assert.assertTrue(addProfile.getStaticTextByLabelContains(recommendedContentRatingByAge).isPresent(),
                RECOMMENDED_RATING_ERROR_MESSAGE);
        Assert.assertTrue(addProfile.isContentRatingDropdownEnabled(recommendedContentRatingByAge),
                "Content rating dropdown is not enabled");
        addProfile.getStaticTextByLabelContains(recommendedContentRatingByAge).click();
        validateRatingValuesInDropdown(getCountry());
        addProfile.getStaticTextByLabelContains(ratingChoose).click();
        addProfile.clickSaveProfileButton();
        Assert.assertTrue(moreMenu.getStaticTextByNameContains(SECONDARY_PROFILE).isPresent(),
                "New secondary user was not saved");
        // Review if the correct rating is selected after profile is saved
        homePage.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(SECONDARY_PROFILE);
        swipe(editProfile.getMaturityRatingLabel(), Direction.UP, 2, 500);
        Assert.assertTrue(editProfile.verifyProfileSettingsMaturityRating(ratingChoose), "Profile rating is not as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73829"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, DE})
    public void testRalphDOBScreenStandardDateFormat() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPage =
                initPage(DisneyPlusEdnaDOBCollectionPageBase.class);

        setupForRalph();
        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        handleGenericPopup(3, 1);
        welcomePage.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());
        Assert.assertTrue(ednaDOBCollectionPage.isOpened(), DOB_PAGE_NOT_DISPLAYED);
        passwordPage.getTextEntryField().click();
        Assert.assertTrue(passwordPage.getDynamicAccessibilityId(
                        getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.DATE_OF_BIRTH_PLACEHOLDER.getText())).isPresent(),
                "DOB format is not standard for the jurisdiction");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74085"})
    @Test(groups = {TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, CA})
    public void testRalphEditProfileAndSelectRatingDifferentFromRecommended() {
        DisneyPlusContentRatingIOSPageBase contentRating = initPage(DisneyPlusContentRatingIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);

        SoftAssert sa = new SoftAssert();
        String darthMaulAvatarId = R.TESTDATA.get("disney_darth_maul_avatar_id");
        String ratingByDefault = "TV-14";
        String ratingToChoose = "TV-Y7";
        int age = 59;

        createAccountAndAddSecondaryProfile(CA, ENGLISH_LANG);

        String recommendedContentRatingByAge = getLocalizationUtils()
                .formatPlaceholderString(contentRating.getRecommendedRating(),
                        Map.of("content_rating", getRecommendedContentRating(CA, age, AGE_VALUES_CANADA)));
        LOGGER.info("RecommendedContentRating {}", recommendedContentRatingByAge);

        setAppToHomeScreen(getUnifiedAccount());

        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        //Handle ATT popup after OneTrust popup
        handleGenericPopup(FIVE_SEC_TIMEOUT, 1);

        Assert.assertTrue(whoIsWatching.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatching.clickProfile(JUNIOR_PROFILE);

        Assert.assertTrue(updateProfilePage.isOpened(), UPDATE_PROFILE_PAGE_NOT_DISPLAYED);
        // Validate Update Profile UI
        sa.assertTrue(updateProfilePage.doesUpdateProfileTitleExist(), "Header profile is not present");
        sa.assertTrue(updateProfilePage.isCompleteProfileDescriptionPresent(), "Profile Description is not present");
        sa.assertTrue(updateProfilePage.isProfileNameFieldPresent(), "Profile Name field is not present");
        sa.assertTrue(editProfile.getDynamicCellByName(darthMaulAvatarId).isPresent(), "Profile icon is not displayed");
        sa.assertTrue(editProfile.getBadgeIcon().isPresent(), "Pencil icon is not displayed");
        sa.assertTrue(updateProfilePage.isDateOfBirthFieldPresent(), "DOB field is not present");
        sa.assertTrue(contentRating.isContentRatingPresent(), "Content rating field is not present");
        sa.assertTrue(updateProfilePage.isLearnMoreLinkTextPresent(), "Learn More link is not present3");
        sa.assertTrue(updateProfilePage.getSaveBtn().isPresent(), "Save button is not present");

        // Enter DOB and select a different rating to save
        editProfile.enterDOB(Person.ADULT.getMonth(), Person.ADULT.getDay(), Person.ADULT.getYear());
        editProfile.getStaticTextByLabelContains(ratingByDefault).click();
        sa.assertTrue(contentRating.isContentRatingPresent(), "Content rating not displayed");
        swipe(contentRating.getStaticTextByLabelContains(recommendedContentRatingByAge),
                updateProfilePage.getContentRatingContainer(), Direction.UP, 2);
        sa.assertTrue(contentRating.getStaticTextByLabelContains(recommendedContentRatingByAge).isPresent(),
                "Recommended rating by age is not present");

        editProfile.getStaticTextByLabelContains(ratingToChoose).click();

        updateProfilePage.tapSaveButton();
        sa.assertTrue(whoIsWatching.isOpened(), "Who is watching screen was not opened");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74027"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, DE})
    public void testCMPBannerUI() {
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage =
                initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), GERMANY);

        setAppToHomeScreen(getUnifiedAccount());

        Assert.assertTrue(oneTrustPage.isOpened(), "One trust page is not opened");

        sa.assertTrue(oneTrustPage.isBannerTitlePresent(),
                "Banner Title is not present on CMP OneTrust banner");
        sa.assertTrue(oneTrustPage.isBannerDescriptionsPresent(),
                "Banner Descriptions is not present on CMP OneTrust banner");
        sa.assertTrue(oneTrustPage.isBannerDPDTitlePresent(),
                "Banner DPD Title is not present on CMP OneTrust banner");
        sa.assertTrue(oneTrustPage.isBannerDPDDescriptionsPresent(),
                "Banner DPD Descriptions is not present on CMP OneTrust banner");
        sa.assertTrue(oneTrustPage.isListOfVendorsLinkPresent(),
                "List of all Partners (vendors) link is not present on CMP OneTrust banner");
        sa.assertTrue(oneTrustPage.isAllowAllButtonPresent(),
                "Accept All button is not present on CMP OneTrust banner");
        sa.assertTrue(oneTrustPage.isRejectAllButtonPresent(),
                "Reject All button is not present on CMP OneTrust banner");
        sa.assertTrue(oneTrustPage.isCustomizedChoicesButtonPresent(),
                "Customize Choices button is not present on CMP OneTrust banner");

        sa.assertAll();
    }

    private void navigateToContentRating() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(JUNIOR_PROFILE);
        swipe(editProfile.getMaturityRatingLabel(), Direction.UP, 2, 500);
        editProfile.getMaturityRatingCell().click();
        editProfile.enterPassword(getUnifiedAccount());
    }

    private void createAccountAndAddSecondaryProfile(String locale, String language) {
        // Create standard account with Ads subscription
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD, locale,
                language)));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), locale);

        // Create secondary profile with no DOB and gender
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(null)
                .gender(null)
                .language(language)
                .avatarId(BABY_YODA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());
    }

    private String getRecommendedContentRating(String locale, int age, int[] ageValues) {
        DisneyCountryData disneyCountryData = new DisneyCountryData();
        List<String> ratingValues = (List<String>) disneyCountryData.searchAndReturnCountryData(locale, CODE, RATING_VALUES);
        LOGGER.info("Ratings values {}", ratingValues);
        if (!ratingValues.isEmpty()) {
            for (int i = 0; i < ageValues.length; i++) {
                if (age <= ageValues[i]) {
                    return ratingValues.get(i);
                }
            }
            return ratingValues.get(ratingValues.size() - 1);
        } else {
            throw new NullPointerException("Ratings list not found for country specified");
        }
    }

    private void setupForRalph(String... DOB) {

        getDefaultCreateUnifiedAccountRequest()
                .setGender(null)
                .setPartner(Partner.DISNEY)
                .addEntitlement(UnifiedEntitlement.builder()
                        .unifiedOffer(getUnifiedOffer(DISNEY_PLUS_STANDARD))
                        .subVersion(UNIFIED_ORDER)
                        .build())
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());

        // Depending on the test scenario we need to set the DOB to
        // 1. certain age or
        // 2. set to null - to trigger the data collection flow
        // 3. Don't add the DOB param in test to set the default DOB from the account api
        if (null != DOB && DOB.length > 0) {
            getDefaultCreateUnifiedAccountRequest().setDateOfBirth(DOB[0]);
        } else if (DOB.length == 0) {
            getDefaultCreateUnifiedAccountRequest().setDateOfBirth(null);
        }
        setAccount(getUnifiedAccountApi().createAccount(getDefaultCreateUnifiedAccountRequest()));
        handleAlert();
    }

    private void validateRatingValuesInDropdown(String locale) {
        DisneyPlusContentRatingIOSPageBase contentRating = initPage(DisneyPlusContentRatingIOSPageBase.class);
        DisneyCountryData disneyCountryData = new DisneyCountryData();
        List<String> ratingValues = (List<String>) disneyCountryData.searchAndReturnCountryData(locale, CODE, RATING_VALUES);
        for (String item : ratingValues) {
            // Some rating values from country yaml contain + symbol instead of comma
            if (item.contains("+")) {
                item = item.replace("+", ", ");
            }
            Assert.assertTrue(contentRating.getStaticTextByLabelContains(item).isPresent(),
                    "Rating value not present: " + item);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void removeJarvisApp() {
        boolean isInstalled = isAppInstalled(sessionBundles.get(JarvisAppleBase.JARVIS));
        if (isInstalled) {
            removeJarvis();
        }
    }
}
