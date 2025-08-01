package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.qa.api.client.requests.*;
import com.disney.qa.api.dictionary.*;
import com.disney.qa.api.offer.pojos.*;
import com.disney.qa.api.pojos.*;
import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.common.constant.DisneyUnifiedOfferPlan;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.disney.qa.common.DisneyAbstractPage.*;
import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.*;
import static com.disney.qa.common.constant.IConstantHelper.*;
import static com.disney.qa.common.constant.RatingConstant.*;
import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.BABY_YODA;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusRalphProfileTest extends DisneyBaseTest {

    private static final int[] AGE_VALUES_GERMANY = {5, 11, 15, 17};
    private static final int[] AGE_VALUES_CANADA = {0, 5, 8, 11, 13, 15, 17, 18};
    private static final int[] AGE_VALUES_EMEA = {5, 8, 11, 13, 15, 17, 18};
    private static final String CODE = "code";
    private static final String RATING_VALUES = "ratingValues";
    private static final String RECOMMENDED_RATING_ERROR_MESSAGE = "Recommended Rating is not present";
    private static final String DOB_FIELD_NOT_DISPLAYED = "DOB Field is not present";
    public static final String DOB_PAGE_NOT_DISPLAYED = "DOB Collection Page is not displayed";
    public static final String JUNIOR_MODE_NOT_TOGGLED_OFF = "Junior Mode is not toggled OFF";
    public static final String LEARN_MORE_LINK_NOT_DISPLAYED = "Learn More Link Text is not displayed";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73921"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, CA})
    public void testSuppressGenderOnDOBCollection() {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusEdnaDOBCollectionPageBase ednaDOBCollectionPage =
                initPage(DisneyPlusEdnaDOBCollectionPageBase.class);
        SoftAssert sa = new SoftAssert();
        String stepper = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.ONBOARDING_STEPPER.getText());
        String description = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_MISSING_INFO_BODY.getText());
        String updatedDescription = description
                .replace("{link_1}", "Privacy Policy")
                .replace("{link_2}", "Learn more");

        handleAlert();
        setupForRalph();
        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }
        handleGenericPopup(3, 1);
        welcomePage.clickLogInButton();
        loginPage.submitEmail(getUnifiedAccount().getEmail());
        passwordPage.submitPasswordForLogin(getUnifiedAccount().getUserPass());
        Assert.assertTrue(ednaDOBCollectionPage.isOpened(), DOB_PAGE_NOT_DISPLAYED);

        sa.assertTrue(loginPage.isDisneyLogoDisplayed(), "Disney Plus Logo is not displayed");
        sa.assertTrue(loginPage.isMyDisneyLogoDisplayed(), "My Disney Logo is not displayed");
        sa.assertTrue(loginPage.getStaticTextByLabel(getLocalizationUtils().formatPlaceholderString(
                        stepper, Map.of("current_step", 3, "total_steps", 5))).isElementPresent(),
                "Stepper text is not displayed");
        sa.assertTrue(ednaDOBCollectionPage.isEdnaDateOfBirthHeaderPresent(), "Header is not displayed");
        sa.assertTrue(ednaDOBCollectionPage.getStaticTextByNameContains(updatedDescription).isPresent(),
                "Description is not displayed");
        sa.assertTrue(ednaDOBCollectionPage.isEdnaBirthdateLabelDisplayed(), "Birthdate label is not displayed");
        sa.assertTrue(ednaDOBCollectionPage.getSaveAndContinueButton().isPresent(),
                "Save & Continue button is not displayed");
        sa.assertTrue(ednaDOBCollectionPage.isLogOutBtnDisplayed(), "Log Out button is not displayed");
        //Validate Gender Field is not displayed
        sa.assertFalse(addProfilePage.isGenderFieldTitlePresent(), "Gender Field Title is present");
        sa.assertFalse(addProfilePage.isGenderFieldPresent(), "Gender Field is displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74084"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, DE})
    public void testLoginAdTierSecondaryProfileCollectDOB() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollection = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfile = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String defaultRating = "12";
        String learnMoreContentRating = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.PCON,
                DictionaryKeys.RATING_SUBCOPY.getText());
        createAccountAndAddSecondaryProfile(GERMANY, getLocalizationUtils().getUserLanguage(), DISNEY_PLUS_STANDARD_WITH_ADS_DE);
        String updatedString = learnMoreContentRating.replace("{content_rating}", defaultRating);

        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        Assert.assertTrue(updateProfile.isOpened(), UPDATE_PROFILE_PAGE_NOT_DISPLAYED);
        sa.assertTrue(updateProfile.getUpdateProfileTitle().isPresent(), "DOB Collection Title is not displayed");
        sa.assertTrue(addProfile.isDateOfBirthFieldPresent(), DOB_FIELD_NOT_DISPLAYED);
        sa.assertFalse(addProfile.isGenderFieldPresent(), "Gender Field is displayed in Ralph Location");
        sa.assertTrue(dobCollection.getStaticTextViewValueContains(updatedString).isPresent(),
                "DOB collection screen learn more support link is not present");
        sa.assertTrue(updateProfile.isLearnMoreLinkTextPresent(), LEARN_MORE_LINK_NOT_DISPLAYED);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75282"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, DE})
    public void testRalphAddProfileJuniorModeDOBIsDisabled() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        String toggleOnDE = "An";
        String toggleOffDE = "Aus";
        SoftAssert sa = new SoftAssert();

        setAccount(getUnifiedAccountApi()
                .createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD_WITH_ADS_DE, GERMANY,
                getLocalizationUtils().getUserLanguage())));

        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), GERMANY);

        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();
        homePage.waitForHomePageToOpen();

        // Add profile
        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        Assert.assertTrue(chooseAvatar.isOpened(), "Choose Avatar screen was not opened");
        addProfile.getCellsWithLabels().get(0).click();
        addProfile.enterProfileName(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());
        String dobPlaceholder = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.DATE_OF_BIRTH_PLACEHOLDER.getText()).replace(".", "/");

        // Validate Junior Mode toggle and toggled it ON
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), toggleOffDE, "Junior Mode is not toggled OFF");
        addProfile.tapJuniorModeToggle();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), toggleOnDE, "Profile is converted to General Audience");

        // Validate Content Rating and Birthdate are disabled
        sa.assertTrue(addProfile.isDateOfBirthFieldPresent(), DOB_FIELD_NOT_DISPLAYED);
        addProfile.getDateOfBirthField().click();
        Assert.assertTrue(editProfile.getDoneButton().isElementNotPresent(THREE_SEC_TIMEOUT),
                "Date of birth is not disabled");
        Assert.assertTrue(addProfile.getChooseContentRating().isPresent(), "Choose content is not disabled");

        // Toggle Junior Mode OFF and validate content
        addProfile.tapJuniorModeToggle();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), toggleOffDE, "Junior Mode is not toggled OFF");
        Assert.assertEquals(addProfile.getValueFromDOB(), dobPlaceholder,
                "Date Of Birth field did not get empty after toggle Junior Mode OFF");
        Assert.assertTrue(addProfile.getChooseContentRating().isPresent(),
                "Choose Content Rating did not get empty after toggle Junior Mode OFF");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75283"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, DE})
    public void testRalphAddProfileJuniorModeDateOfBirth() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        String EXPECTED_RATING = "12";

        setAccount(getUnifiedAccountApi()
                .createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD_WITH_ADS_DE,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), DE);

        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(JUNIOR_PROFILE)
                .dateOfBirth(KIDS_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(false)
                .isStarOnboarded(true)
                .build());
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), GERMANY);

        setAppToHomeScreen(getUnifiedAccount());

        handleOneTrustPopUp();
        Assert.assertTrue(whoIsWatching.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatching.clickProfile(DEFAULT_PROFILE);
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(JUNIOR_PROFILE);
        Assert.assertTrue(editProfile.isEditTitleDisplayed(), EDIT_PROFILE_PAGE_NOT_DISPLAYED);
        swipeUp(2, 500);
        String toggleOffValue = getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.TEXT_OFF.getText());
        String toggleOnValue = getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.TEXT_ON.getText());
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), toggleOffValue, JUNIOR_MODE_NOT_TOGGLED_OFF);
        editProfile.toggleJuniorMode();
        editProfile.waitForUpdatedToastToDisappear();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), toggleOnValue,
                "Profile is converted to General Audience");
        swipeDown(2, 500);
        sa.assertTrue(editProfile.isDateFieldNotRequiredLabelPresent(),
                "Birthdate field did not change to 'Not Required'");
        swipeUp(2, 500);
        editProfile.toggleJuniorMode();
        passwordPage.enterPassword(getUnifiedAccount());
        editProfile.waitForUpdatedToastToDisappear();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), toggleOffValue, JUNIOR_MODE_NOT_TOGGLED_OFF);
        swipeUp(2, 500);
        sa.assertTrue(editProfile.verifyProfileSettingsMaturityRating(EXPECTED_RATING),
                "Profile rating is not as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74004"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, DE})
    public void testRalphContentSliderMaturityGermany() {
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRating = initPage(DisneyPlusContentRatingIOSPageBase.class);

        int under18Age = calculateAge(Person.AGE_17.getMonth(), Person.AGE_17);
        String recommendedContentRatingByAge = getLocalizationUtils()
                .formatPlaceholderString(contentRating.getRecommendedRating(),
                Map.of("content_rating", getRecommendedContentRating(GERMANY, under18Age, AGE_VALUES_GERMANY)));
        LOGGER.info("Recommended Content Rating: {}", recommendedContentRatingByAge);
        createAccountAndAddSecondaryProfile(GERMANY, getLocalizationUtils().getUserLanguage(), DISNEY_PLUS_STANDARD_WITH_ADS_DE);
        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        addProfile.enterDOB(Person.AGE_17.getMonth(), Person.AGE_17.getDay(), Person.AGE_17.getYear());
        updateProfilePage.tapSaveButton();
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
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

        createAccountAndAddSecondaryProfile(CANADA, getLocalizationUtils().getUserLanguage(), DISNEY_PLUS_STANDARD_WITH_ADS_CA);
        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        addProfile.enterDOB(Person.AGE_17.getMonth(), Person.AGE_17.getDay(), Person.AGE_17.getYear());
        updateProfilePage.tapSaveButton();
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
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
        createAccountAndAddSecondaryProfile(getLocalizationUtils().getLocale(), DE, DISNEY_PLUS_STANDARD_WITH_ADS_DE);
        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        addProfile.enterDOB(Person.MINOR.getMonth(), Person.MINOR.getDay(), Person.MINOR.getYear());
        updateProfilePage.tapSaveButton();
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
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
        int age = 59;

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(DISNEY_PLUS_STANDARD,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), CA);

        String recommendedContentRatingByAge = getLocalizationUtils().formatPlaceholderString(contentRating.getRecommendedRating(),
                Map.of("content_rating", getRecommendedContentRating(CANADA, age, AGE_VALUES_CANADA)));
        LOGGER.info("RecommendedContentRating {}", recommendedContentRatingByAge);
        handleAlert();
        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();
        homePage.waitForHomePageToOpen();
        Assert.assertTrue(homePage.isOpened(), HOME_PAGE_NOT_DISPLAYED);
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-76350"})
    @Test(groups = {TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, EMEA_LATAM})
    public void testEmeaEditProfilePrivacyData() {
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);

        DisneyUnifiedOfferPlan plan =
                getLocalizationUtils().getLocale().contains(GERMANY) ? DISNEY_PLUS_STANDARD_WITH_ADS_DE : DISNEY_PLUS_STANDARD;
        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(
                plan, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage())));

        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());

        // Create U18 profile
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
                .dateOfBirth(U18_DOB)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .isStarOnboarded(true)
                .build());

        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();

        Assert.assertTrue(whoIsWatching.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatching.clickEditProfile();
        // Validate Privacy & Data option is not present
        editProfilePage.clickEditModeProfile(SECONDARY_PROFILE);
        swipe(editProfilePage.getDeleteProfileButton(), Direction.UP, 10, 500);
        Assert.assertFalse(editProfilePage.getPrivacyAndDataTitleLabel().isPresent(THREE_SEC_TIMEOUT),
                "Privacy & Data section is present");
        Assert.assertFalse(editProfilePage.getDemographicTargetingToggleCell().isPresent(THREE_SEC_TIMEOUT),
                "Demographic Targeting toggle is present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74085"})
    @Test(groups = {TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION, CA})
    public void testRalphEditProfileAndSelectRatingDifferentFromRecommended() {
        DisneyPlusContentRatingIOSPageBase contentRating = initPage(DisneyPlusContentRatingIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatching = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusUpdateProfileIOSPageBase updateProfilePage = initPage(DisneyPlusUpdateProfileIOSPageBase.class);

        SoftAssert sa = new SoftAssert();
        String ratingByDefault = "TV-14";
        String ratingToChoose = "TV-Y7";
        int age = 59;

        createAccountAndAddSecondaryProfile(CA, ENGLISH_LANG, DISNEY_PLUS_STANDARD_WITH_ADS_CA);

        String recommendedContentRatingByAge = getLocalizationUtils()
                .formatPlaceholderString(contentRating.getRecommendedRating(),
                        Map.of("content_rating", getRecommendedContentRating(CA, age, AGE_VALUES_CANADA)));
        LOGGER.info("RecommendedContentRating {}", recommendedContentRatingByAge);

        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();
        Assert.assertTrue(whoIsWatching.isOpened(), WHOS_WATCHING_NOT_DISPLAYED);
        whoIsWatching.clickProfile(SECONDARY_PROFILE);

        Assert.assertTrue(updateProfilePage.isOpened(), UPDATE_PROFILE_PAGE_NOT_DISPLAYED);
        // Validate Update Profile UI
        sa.assertTrue(updateProfilePage.doesUpdateProfileTitleExist(), "Header profile is not present");
        sa.assertTrue(updateProfilePage.isCompleteProfileDescriptionPresent(), "Profile Description is not present");
        sa.assertTrue(updateProfilePage.isProfileNameFieldPresent(), "Profile Name field is not present");
        sa.assertTrue(editProfile.getDynamicCellByName(BABY_YODA).isPresent(), "Profile icon is not displayed");
        sa.assertTrue(editProfile.getBadgeIcon().isPresent(), "Pencil icon is not displayed");
        sa.assertTrue(updateProfilePage.isDateOfBirthFieldPresent(), DOB_FIELD_NOT_DISPLAYED);
        sa.assertTrue(contentRating.isContentRatingPresent(), "Content rating field is not present");
        sa.assertTrue(updateProfilePage.isLearnMoreLinkTextPresent(), LEARN_MORE_LINK_NOT_DISPLAYED);
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

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-78955"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.PRE_CONFIGURATION, TR})
    public void verifyTurkeyUserIsRequestedDOBForNewProfiles() {
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(
                getCreateUnifiedAccountRequest(
                        DISNEY_PLUS_STANDARD_YEARLY_TURKEY,
                        getLocalizationUtils().getLocale(),
                        getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());
        setAppToHomeScreen(getUnifiedAccount());
        handleOneTrustPopUp();
        homePage.waitForHomePageToOpen();

        homePage.clickMoreTab();
        moreMenu.clickAddProfile();
        Assert.assertTrue(chooseAvatarPage.isOpened(), CHOOSE_AVATAR_PAGE_NOT_DISPLAYED);
        chooseAvatarPage.clickSkipButton();
        addProfilePage.enterProfileName(SECONDARY_PROFILE);

        Assert.assertTrue(addProfilePage.isDateOfBirthFieldPresent(), "Date of Birth field is not present");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-75785"})
    @Test(groups = {TestGroup.PROFILES, TestGroup.PRE_CONFIGURATION, LATAM})
    public void verifyDemographicTargetingToggleIsNotDisplayedForJuniorModeProfiles() {
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoIsWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);

        setAccount(getUnifiedAccountApi().createAccount(getCreateUnifiedAccountRequest(
                DISNEY_PLUS_STANDARD,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(KIDS_PROFILE)
                .dateOfBirth(null)
                .language(getLocalizationUtils().getUserLanguage())
                .avatarId(BABY_YODA)
                .kidsModeEnabled(true)
                .isStarOnboarded(true)
                .build());
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), getLocalizationUtils().getLocale());

        setAppToHomeScreen(getUnifiedAccount());
        whoIsWatchingPage.clickEditProfile();
        editProfilePage.clickEditModeProfile(KIDS_PROFILE);

        // Validate Privacy & Data subsection and Demographic Targeting toggle elements are not visible
        swipe(editProfilePage.getDeleteProfileButton(), Direction.UP, 10, 500);
        Assert.assertFalse(editProfilePage.getPrivacyAndDataTitleLabel().isPresent(FIVE_SEC_TIMEOUT),
                "'Privacy & Data' subsection title is present");
        Assert.assertFalse(editProfilePage.getDemographicTargetingToggleCell().isPresent(THREE_SEC_TIMEOUT),
                "Demographic Targeting toggle title is present");
    }

    private void navigateToContentRating() {
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        moreMenu.clickMoreTab();
        moreMenu.clickEditProfilesBtn();
        editProfile.clickEditModeProfile(SECONDARY_PROFILE);
        swipe(editProfile.getMaturityRatingLabel(), Direction.UP, 2, 500);
        editProfile.getMaturityRatingCell().click();
        editProfile.enterPassword(getUnifiedAccount());
    }

    private void createAccountAndAddSecondaryProfile(String locale, String language, DisneyUnifiedOfferPlan planName) {
        // Create standard account with Ads subscription
        setAccount(getUnifiedAccountApi()
                .createAccount(getCreateUnifiedAccountRequest(planName,
                        locale, language)));
        getUnifiedAccountApi().overrideLocations(getUnifiedAccount(), locale);

        // Create secondary profile with no DOB and gender
        getUnifiedAccountApi().addProfile(CreateUnifiedAccountProfileRequest.builder()
                .unifiedAccount(getUnifiedAccount())
                .profileName(SECONDARY_PROFILE)
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
