package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.RatingConstant.GERMANY;

public class DisneyPlusRalphProfileTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74028"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION }, enabled = false)
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
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION }, enabled = false)
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
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION }, enabled = false)
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
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION }, enabled = false)
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
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION }, enabled = false)
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
 //   @BeforeMethod(alwaysRun = false)
 //   @BeforeClass(alwaysRun = true)
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.RALPH_LOG_IN, TestGroup.PRE_CONFIGURATION })
    public void testRalphAddProfileJuniorModeDOBIsDisabled() {
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase chooseAvatar = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfile = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfile = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPageBase = initPage(DisneyPlusDOBCollectionPageBase.class);
        SoftAssert sa = new SoftAssert();
       // disableOneTrust();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_US_WEB_ADS_MONTHLY,
                GERMANY, getLocalizationUtils().getUserLanguage()));

        getAccountApi().overrideLocations(getAccount(), GERMANY);
        // Onboarding to application and accept one trust page if appears
        handleAlert(IOSUtils.AlertButtonCommand.ACCEPT);
      //  terminateApp(sessionBundles.get(DISNEY));
      //  launchApp(sessionBundles.get(DISNEY));
        setAppToHomeScreen(getAccount());
        if (oneTrustPage.isAllowAllButtonPresent()) {
            oneTrustPage.tapAcceptAllButton();
        }

        moreMenu.clickMoreTab();
        moreMenu.clickAddProfile();
        Assert.assertTrue(chooseAvatar.isOpened(), "Choose Avatar screen was not opened");
        addProfile.getCellsWithLabels().get(0).click();
        addProfile.enterProfileName(JUNIOR_PROFILE);
        addProfile.enterDOB(Person.U13.getMonth(), Person.U13.getDay(), Person.U13.getYear());

        // Validate Junior Mode toggle
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), "Off", "Junior Mode is not toggled OFF");
        addProfile.tapJuniorModeToggle();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), "On", "Profile is converted to General Audience");

        // Validate Content Rating and Birthdate are disabled
        sa.assertTrue(addProfile.isDateOfBirthFieldPresent(), "DOB field is not present");
        addProfile.getDynamicTextEntryFieldByName(addProfile.getBirthdateTextField()).click();
        Assert.assertFalse(editProfile.getTypeButtonByLabel("Done").isPresent(), "Date of birth is not disabled");
        Assert.assertTrue(addProfile.getChooseContentRating().isPresent(), "Choose content is not disabled");

        // Toggle Junior Mode OFF and validate content
        addProfile.tapJuniorModeToggle();
        sa.assertEquals(editProfile.getJuniorModeToggleValue(), "Off", "Junior Mode is not toggled OFF");
        Assert.assertTrue(addProfile.getValueFromDOB().isPresent(),
                "Date Of Birth field did not get empty after toggle Junior Mode OFF");
        Assert.assertTrue(addProfile.getChooseContentRating().isPresent(),
                "Choose Content Rating did not get empty after toggle Junior Mode OFF");
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
