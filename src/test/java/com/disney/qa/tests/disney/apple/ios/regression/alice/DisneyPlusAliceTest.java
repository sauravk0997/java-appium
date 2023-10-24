package com.disney.qa.tests.disney.apple.ios.regression.alice;

import com.disney.alice.AliceDriver;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAliceTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62687"})
    @Test(description = "App Opens to Welcome Screen", groups = {"Smoke", TestGroup.PRE_CONFIGURATION})
    public void testAppLaunch() {
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreenPage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);

        welcomeScreenPage.isOpened();
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, "disney_logo");
    }
}
