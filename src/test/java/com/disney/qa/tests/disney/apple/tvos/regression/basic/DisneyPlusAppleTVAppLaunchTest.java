package com.disney.qa.tests.disney.apple.tvos.regression.basic;

import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DisneyPlusAppleTVAppLaunchTest extends DisneyPlusAppleTVBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XCDQA-89335"})
    @Test(groups = {"Smoke"})
    public void launchApp() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        sa.assertAll();
    }
}
