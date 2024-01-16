package com.disney.qa.tests.disney.apple.qait;

import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


public class DisneyPlusTVSmokeTest extends DisneyPlusAppleTVBaseTest {

    @Test(groups = {TestGroup.QAIT})
    public void testTvWelcomeScreen() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        SoftAssert sa = new SoftAssert();
        sa.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        sa.assertAll();
    }
}
