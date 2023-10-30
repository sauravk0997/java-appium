package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusRalphProfileTest extends DisneyBaseTest {

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74240"})
    @Test(description = "Conditional OneTrust Initialization-CTA-AcceptAll", groups = {"Ralph-Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testConditionalOneTrustInitializationAcceptAll() {
        setupForRalph();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
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
        loginPage.submitEmail(disneyAccount.get().getEmail());
        passwordPage.submitPasswordForLogin(disneyAccount.get().getUserPass());
        passwordPage.clickPrimaryButton();
        pause(5);
        homePage.isOpened();
        pause(3);
        sa.assertFalse(oneTrustPage.isOpened(), "One trust page is present after login");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74758"})
    @Test(description = "Conditional OneTrust Initialization-CTA-RejectAll", groups = {"Ralph-Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testConditionalOneTrustInitializationCustomizeChoice() {
        setupForRalph();
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(oneTrustPage.isOpened(), "One trust page is not opened");
        sa.assertTrue(oneTrustPage.isCustomizedChoicesButtonPresent(),"Customized choices button is not present on one trust banner");
        oneTrustPage.tapCustomizedChoices();
        sa.assertTrue(oneTrustPage.isPrivacyPreferenceCenterOpen(), "Privacy Preference page is not opened");
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74757"})
    @Test(description = "Conditional OneTrust Initialization-CTA-RejectAll", groups = {"Ralph-Onboarding", TestGroup.PRE_CONFIGURATION })
    public void testConditionalOneTrustInitializationRejectAll() {
        setupForRalph();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusOneTrustConsentBannerIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustConsentBannerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(oneTrustPage.isOpened(), "One trust page is not opened");
        sa.assertTrue(oneTrustPage.isRejectAllButtonPresent(), "Reject all button is not present on one trust banner");
        oneTrustPage.tapRejectAllButton();
        sa.assertFalse(oneTrustPage.isOpened(), "One trust page is present, reject all button was not actioned");
        sa.assertTrue(welcomePage.isOpened(), "Welcome page is not opened");
        welcomePage.clickLogInButton();
        loginPage.submitEmail(disneyAccount.get().getEmail());
        passwordPage.submitPasswordForLogin(disneyAccount.get().getUserPass());
        passwordPage.clickPrimaryButton();
        pause(5);
        homePage.isOpened();
        pause(3);
        sa.assertFalse(oneTrustPage.isOpened(), "One trust page is present after login");
        sa.assertAll();
        sa.assertAll();
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74136"})
    @Test(description = "Suppress Gender field on Edit Profile for all jurisdictions", groups = {"Ralph-Onboarding", TestGroup.PRE_CONFIGURATION })
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
        loginPage.submitEmail(disneyAccount.get().getEmail());
        passwordPage.submitPasswordForLogin(disneyAccount.get().getUserPass());
        passwordPage.clickPrimaryButton();
        pause(5);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(DEFAULT_PROFILE);
        // verify that Gender field is not present
        Assert.assertFalse(editProfilePage.isGenderButtonPresent(), "Gender Field is shown on edit profile page");
    }

    @Maintainer("gkrishna1")
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74136"})
    @Test(description = "Suppress Gender field on Edit Profile for all jurisdictions", groups = {"Ralph-Onboarding", TestGroup.PRE_CONFIGURATION })
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
                .setDisneyAccount(disneyAccount.get())
                .setProfileName(SECONDARY_PROFILE)
                .setDateOfBirth("1995-01-01")
                .setAvatarId(R.TESTDATA.get("disney_darth_maul_avatar_id"))
                .setGender(null)
                .setKidsModeEnabled(false)
                .setStarOnboarded(false)
                .setCountry(languageUtils.get().getLocale())
                .setLanguage(languageUtils.get().getUserLanguage());
        disneyAccountApi.get().addProfile(createDisneyProfileRequest);

        oneTrustPage.tapRejectAllButton();
        loginPage.dismissAppTrackingPopUp();
        welcomePage.clickLogInButton();
        loginPage.submitEmail(disneyAccount.get().getEmail());
        passwordPage.submitPasswordForLogin(disneyAccount.get().getUserPass());
        passwordPage.clickPrimaryButton();
        pause(5);
        whoIsWatching.clickProfile(SECONDARY_PROFILE);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(SECONDARY_PROFILE);
        // verify that Gender field is not present
        Assert.assertFalse(editProfilePage.isGenderButtonPresent(), "Gender Field is shown on edit profile page");
    }

    private void  setupForRalph(String... DOB) {
        String locale = languageUtils.get().getLocale();
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();

        createDisneyAccountRequest
                .setGender(null)
                .setCountry(languageUtils.get().getLocale())
                .setLanguage(languageUtils.get().getUserLanguage());
        // Depending on the test scenario we need to set the DOB to
        // 1. certain age or
        // 2. set to null - to trigger the data collection flow
        // 3. Don't add the DOB param in test to set the default DOB from the account api
        if (null!= DOB && DOB.length > 0) {
            createDisneyAccountRequest.setDateOfBirth(DOB[0]);
        } else if(DOB == null) {
            createDisneyAccountRequest.setDateOfBirth(null);
        }
        DisneyOffer disneyOffer = disneyAccountApi.get().lookupOfferToUse(locale, "Disney Plus Standard W Ads Monthly - DE - Web");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V2").build();
        createDisneyAccountRequest.addEntitlement(entitlement);
        DisneyAccount testAccount = disneyAccountApi.get().createAccount(createDisneyAccountRequest);
        disneyAccountApi.get().addFlex(testAccount);
        disneyAccount.set(testAccount);
        handleAlert();
    }
}
