package com.disney.qa.tests.disney.apple.tvos.regression.alice;

import com.disney.alice.AliceDriver;
import com.disney.alice.labels.AliceLabels;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAppleTVAlice extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89335"})
    @Test(groups = {"Smoke"})
    public void testAppLaunch() {
        selectAppleUpdateLaterAndDismissAppTracking();
        SoftAssert sa = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());

        sa.assertTrue(welcomeScreenPage.isOpened(), "Welcome screen did not launch");
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, AliceLabels.DISNEY_LOGO.getText());
        sa.assertAll();
    }
}
