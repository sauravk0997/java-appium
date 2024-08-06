package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusWelcomeScreenTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67821"})
    @Test(description = "Verify the Welcome screen elements in Portrait mode", groups = {TestGroup.ONBOARDING, TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyWelcomeScreenPortrait() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        Assert.assertTrue(welcomeScreen.isOpened(),
                "XMOBQA-62330/62332 - 'Welcome' screen was not displayed on launch/Sign Up button was not displayed as expected");

//        sa.assertTrue(welcomeScreen.isDisneyPlusLogoDisplayed(), //Disney image logo identifier is missing
//                "XMOBQA-62332 - Disney+ Logo was not displayed as expected");

        sa.assertTrue(welcomeScreen.isMainTextDisplayed(),
                "XMOBQA-62332 - 'Marketing Copy' was not displayed as expected");

        sa.assertTrue(welcomeScreen.isSignUpButtonDisplayed(),
                "XMOBQA-62332 - 'Sign Up CTA' text was not displayed as expected");
        sa.assertTrue(welcomeScreen.isSubCopyDirectTextPresent(),
                "XMOBQA-62332 - Sub copy is not displayed as expected");

        sa.assertTrue(welcomeScreen.isLogInButtonDisplayed(),
                "XMOBQA-62332 - 'Log In' button was not displayed as expected");

        sa.assertAll();
    }
}
