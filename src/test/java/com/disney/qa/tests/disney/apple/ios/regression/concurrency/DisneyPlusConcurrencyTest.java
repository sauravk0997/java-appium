package com.disney.qa.tests.disney.apple.ios.regression.concurrency;

import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusConcurrencyTest extends DisneyBaseTest {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68652"})
    @Test(groups = {TestGroup.CONCURRENCY, TestGroup.VIDEO_PLAYER, TestGroup.PRE_CONFIGURATION, US})
    public void verifyConcurrency() {
        DisneyPlusVideoPlayerIOSPageBase videoPlayer = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
      //  setAppToHomeScreen(getAccount());
/*
        String email = getAccount().getEmail();
        String password = getAccount().getUserPass();
        LOGGER.info("* email** " + email);
        LOGGER.info("* password ** " + password);
        */
        String email = System.getenv("accountEmail");
        String password = System.getenv("accountPassword");

        welcomePage.clickLogInButton();
        loginPage.submitEmail(email);
        passwordPage.submitPasswordForLogin(password);
    //    loginPage.submitEmail(getAccount().getEmail());
       // loginPage.submitEmail("qaittestguid+173402350387805e8@gsuite.disneyplustesting.com");
     //   passwordPage.submitPasswordForLogin(getAccount().getUserPass());
       // passwordPage.submitPasswordForLogin("M1ck3yM0us3#");


        //  passwordPage.clickPrimaryButton();
       // pause(5);
        homePage.waitForHomePageToOpen();
        sa.assertTrue(homePage.isOpened(), "Home page did not open");
        launchDeeplink(R.TESTDATA.get("disney_prod_movie_deadpool_rated_r_deeplink"));
        sa.assertTrue(videoPlayer.isOpened(), "Video player did not open");

    }
}
