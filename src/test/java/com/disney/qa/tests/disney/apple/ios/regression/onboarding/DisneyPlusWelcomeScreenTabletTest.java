package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.zebrunner.carina.utils.factory.DeviceType;

import com.disney.util.TestGroup;
import org.openqa.selenium.ScreenOrientation;
import org.testng.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.zebrunner.agent.core.annotation.TestLabel;

public class DisneyPlusWelcomeScreenTabletTest extends DisneyPlusWelcomeScreenTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67821"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.PRE_CONFIGURATION, TestGroup.SMOKE})
    public void verifyWelcomeScreenLandscape() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.PORTRAIT, ScreenOrientation.LANDSCAPE);
        Assert.assertTrue(welcomeScreen.isOpened(), "'Welcome' screen was not displayed on launch");
        sa.assertTrue(welcomeScreen.isMainTextDisplayed(), "'Marketing Copy' was not displayed as expected");
        sa.assertTrue(welcomeScreen.isSignUpButtonDisplayed(), "'Sign Up CTA' text was not displayed as expected");
        sa.assertTrue(welcomeScreen.isSubCopyDirectTextPresent(), "Sub copy is not displayed as expected");
        sa.assertTrue(welcomeScreen.isLogInButtonDisplayed(), "'Log In' button was not displayed as expected");
        setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        sa.assertAll();
    }
}
