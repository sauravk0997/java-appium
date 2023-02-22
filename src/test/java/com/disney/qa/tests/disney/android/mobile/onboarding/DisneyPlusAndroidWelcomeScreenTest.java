package com.disney.qa.tests.disney.android.mobile.onboarding;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusLoginPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.utils.AndroidUtilsExtended.Orientations.LANDSCAPE;

public class DisneyPlusAndroidWelcomeScreenTest extends BaseDisneyTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67821"})
    @Test(description = "Verify Welcome Screen UI", groups = {"Onboarding"})
    @Maintainer("akorwar")
    public void testWelcomeScreen() {
        verifyWelcomeScreenUIElements();
        if (isAndroidTablet()) {
            androidUtils.get().setOrientation(LANDSCAPE);
            verifyWelcomeScreenUIElements();
        }
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67829"})
    @Test(description = "Service Unavailable Welcome Screen", groups = {"Onboarding"})
    @Maintainer("ckim1")
    public void testServiceUnavailableWelcomeScreen() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        SoftAssert sa = new SoftAssert();

        forceUnsupportedLocationJarvis();

        sa.assertTrue(welcomePageBase.isInvalidRegionLinkVisible(),
                "Invalid region link is not displayed");

        sa.assertTrue(welcomePageBase.isServiceUnavailableTitleVisible(),
                "Service unavailable title text is not displayed");

        sa.assertTrue(welcomePageBase.isServiceUnavailableBodyVisible(),
                "Service unavailable body text is not displayed");

        sa.assertTrue(welcomePageBase.isInvalidRegionLoginButtonVisible(),
                "Invalid region log in button is not displayed");

        welcomePageBase.clickInvalidLoginButton();

        sa.assertTrue(loginPageBase.isEmailTitleDisplayed(),
                "Email title is not displayed");

        sa.assertTrue(loginPageBase.isStandardButtonContainerDisplayed(),
                "Log in button container is not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67262"})
    @Test(description = "Do not Show Sign Up on Log In when Service Unavailable", groups = {"Onboarding"})
    @Maintainer("bwatson")
    public void testNoSignUpOnLogInServiceUnavailable() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCommonPageBase commonPageBase = initPage(DisneyPlusCommonPageBase.class);
        SoftAssert sa = new SoftAssert();

        forceUnsupportedLocationJarvis();

        sa.assertTrue(welcomePageBase.isInvalidRegionLinkVisible(),
                "Invalid region link not displayed");

        sa.assertTrue(welcomePageBase.isInvalidRegionLoginButtonVisible(),
                "Invalid region log in button not displayed");

        welcomePageBase.clickInvalidLoginButton();
        loginPageBase.proceedToPasswordMode(generateNewUserEmail());

        sa.assertFalse(
                commonPageBase.isErrorDialogPresent(),
                "Sign Up Prompt is shown during unknown email login");

        Assert.assertEquals(
                loginPageBase.getInputErrorText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.LOGIN_INVALID_EMAIL_ERROR.getText()),
                "Unknown Email Error Text does not match dictionary key");

        loginPageBase.proceedToPasswordMode("");

        sa.assertFalse(
                commonPageBase.isErrorDialogPresent(),
                "Sign Up Prompt is shown during invalid email login");

        Assert.assertEquals(
                loginPageBase.getInputErrorText(),
                languageUtils.get().getDictionaryItem(
                        DisneyDictionaryApi.ResourceKeys.APPLICATION,
                        DictionaryKeys.INVALID_EMAIL_ERROR.getText()),
                "Null Email Error Text does not match dictionary key");

        sa.assertAll();
    }

    private void verifyWelcomeScreenUIElements() {
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        SoftAssert sa = new SoftAssert();

        sa.assertTrue(welcomePageBase.isDisneyPlusLogoVisible(), "Disney Plus logo not displayed");

        sa.assertTrue(welcomePageBase.isWelcomeTextVisible(), "Welcome description not displayed");

        sa.assertTrue(welcomePageBase.isWelcomeBrandLogosVisible(), "Brand logos not displayed");

        sa.assertTrue(welcomePageBase.isSignUpButtonPresent(), "Sign up button not displayed");

        sa.assertTrue(welcomePageBase.isLoginButtonPresent(), "Login button not displayed");

        sa.assertAll();
    }
}