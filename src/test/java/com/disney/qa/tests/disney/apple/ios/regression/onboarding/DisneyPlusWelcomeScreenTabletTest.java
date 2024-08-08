package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.zebrunner.carina.utils.factory.DeviceType;

import com.disney.util.TestGroup;
import org.openqa.selenium.ScreenOrientation;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.zebrunner.agent.core.annotation.TestLabel;

public class DisneyPlusWelcomeScreenTabletTest extends DisneyPlusWelcomeScreenTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67821"})
    @Test(description = "Verify the Welcome screen elements in Landscape mode (Tablet only)", groups = {TestGroup.ONBOARDING, TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyWelcomeScreenLandscape() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.PORTRAIT, ScreenOrientation.LANDSCAPE);


        sa.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isOpened(),
                "XMOBQA-62330/62333 - 'Welcome' screen was not displayed on launch/Sign Up button was not displayed as expected");

//        sa.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isDisneyPlusLogoDisplayed(), //Disney image logo identifier is missing
//                "XMOBQA-62333 - Disney+ Logo was not displayed as expected");

        sa.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isMainTextDisplayed(),
                "XMOBQA-62333 - 'Marketing Copy' was not displayed as expected");

        swipePageTillElementPresent(disneyPlusWelcomeScreenIOSPageBase.getSubCopyDirectText(), 5, null, Direction.UP, 1);

        sa.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isSubCopyDirectTextPresent(),
                "XMOBQA-62333 - 'Sign Up CTA' text was not displayed as expected");

        sa.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isLogInButtonDisplayed(),
                "XMOBQA-62333 - 'Log In' button was not displayed as expected");

        setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        sa.assertAll();
    }
}
