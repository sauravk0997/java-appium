package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusWelcomeScreenTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62330", "XMOBQA-62332"})
    @Test(description = "Verify the Welcome screen elements in Portrait mode", groups = {"Onboarding"})
    public void verifyWelcomeScreenPortrait() {
        setGlobalVariables();
        handleAlert();
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isOpened(),
                "XMOBQA-62330/62332 - 'Welcome' screen was not displayed on launch/Sign Up button was not displayed as expected");

        sa.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isDisneyPlusLogoDisplayed(),
                "XMOBQA-62332 - Disney+ Logo was not displayed as expected");

        sa.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isMainTextDisplayed(),
                "XMOBQA-62332 - 'Marketing Copy' was not displayed as expected");

        sa.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isSubCtaPresent(),
                "XMOBQA-62332 - 'Sign Up CTA' text was not displayed as expected");

        sa.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isLogInButtonDisplayed(),
                "XMOBQA-62332 - 'Log In' button was not displayed as expected");

        sa.assertAll();
    }
}
