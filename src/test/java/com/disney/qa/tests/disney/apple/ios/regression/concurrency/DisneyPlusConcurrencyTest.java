package com.disney.qa.tests.disney.apple.ios.regression.concurrency;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusConcurrencyTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68652"})
    @Test(groups = {TestGroup.CONCURRENCY, TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyConcurrency() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        loginIn(System.getenv("accountEmail"), System.getenv("accountPassword"));

        homePage.waitForHomePageToOpen();
        sa.assertTrue(homePage.isOpened(), "Home page did not open");
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_deadpool_rated_r_deeplink"));
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open");

    }

    public void loginIn(String email, String password) {
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);

        welcomePage.clickLogInButton();
        loginPage.submitEmail(email);
        passwordPage.submitPasswordForLogin(password);
        homePage.waitForHomePageToOpen();
    }


}
