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
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        String email = "accountEmail";
        String password =  "accountPassword";

        SoftAssert sa = new SoftAssert();

        loginIn(System.getenv(email), System.getenv(password));

        Assert.assertTrue(homePage.isOpened(), "Home page did not open");

        launchDeeplink(R.TESTDATA.get("disney_prod_movie_detail_dr_strange_deeplink"));
        Assert.assertTrue(detailsPage.isOpened(), "Details page did not open");
        detailsPage.clickPlayButton();
        Assert.assertTrue(videoPlayer.isOpened(), "Video player did not open");
        sa.assertTrue(videoPlayer.isConcurrencyTitleErrorPresent(),
                "Expected concurrency error title message is not present");
        sa.assertTrue(videoPlayer.isConcurrencyMessageErrorPresent(),
                "Expected concurrency error message is not present");
        sa.assertTrue(videoPlayer.isConcurrencyCTAButtonPresent(),
                "Expected concurrency error message is not present");
        videoPlayer.getCtaButtonDismiss().click();
        sa.assertTrue(detailsPage.isOpened(), "Details page did not open");
        sa.assertAll();
    }

    private void loginIn(String email, String password) {
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
