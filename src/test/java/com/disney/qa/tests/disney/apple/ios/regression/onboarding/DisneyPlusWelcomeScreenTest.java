package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.dmed.productivity.jocasta.JocastaCarinaAdapter;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.ScreenOrientation;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;

@Listeners(JocastaCarinaAdapter.class)
public class DisneyPlusWelcomeScreenTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67821"})
    @Test(groups = {TestGroup.ONBOARDING, TestGroup.SMOKE, TestGroup.PRE_CONFIGURATION, US})
    public void verifyWelcomeScreen() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);

        setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.PORTRAIT, ScreenOrientation.LANDSCAPE);
        Assert.assertTrue(welcomeScreen.isOpened(), "Welcome Screen was not displayed on launch");
        sa.assertTrue(welcomeScreen.isMainTextDisplayed(), "Marketing Copy was not displayed as expected");
        sa.assertTrue(welcomeScreen.isSubCopyDirectTextPresent(), "Sub copy is not displayed as expected");
        sa.assertTrue(welcomeScreen.isLogInButtonDisplayed(), "Log In button was not displayed as expected");

        setToNewOrientation(DeviceType.Type.IOS_TABLET, ScreenOrientation.LANDSCAPE, ScreenOrientation.PORTRAIT);
        Assert.assertTrue(welcomeScreen.isOpened(), "Welcome Screen was not displayed on launch");
        sa.assertTrue(welcomeScreen.isMainTextDisplayed(), "Marketing Copy was not displayed as expected");
        sa.assertTrue(welcomeScreen.isSubCopyDirectTextPresent(), "Sub copy is not displayed as expected");
        sa.assertTrue(welcomeScreen.isLogInButtonDisplayed(), "Log In button was not displayed as expected");
        sa.assertAll();
    }
}
