package com.disney.qa.tests.disney.android.tv;

import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.disney.qa.disney.android.pages.tv.globalnav.DisneyPlusAndroidTVProfilePageBase;
import com.disney.util.disney.ZebrunnerXrayLabels;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.stream.IntStream;

import static com.disney.qa.tests.disney.DisneyPlusBaseTest.*;

public class DisneyPlusAndroidTVAppEntryMultipleProfilesTest extends DisneyPlusAndroidTVBaseTest {

    private static final String PROFILE_NAME = "auto";

    @BeforeMethod
    public void testSetup() {
        IntStream.range(0, 3).forEach(i -> {
            disneyAccountApi.get().addProfile(entitledUser.get(), PROFILE_NAME + i, language, null, false);
        });
    }

    @Test
    public void AppRestartToMultipleProfile(){
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66564"));
        SoftAssert sa = new SoftAssert();

        loginWithoutHomeCheck(entitledUser.get());

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);
        closeApp();

        pause(15);
        launchTVapp(APP_PACKAGE_DISNEY, APP_LAUNCH_ACTIVITY);

        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(), PROFILE_PAGE_LOAD_ERROR);

        sa.assertAll();
    }

    //TODO:Figure out the best course of action for this, needs to take into account container restart, connection of the device to the jenkins agent
    @Test(enabled = false)
    public void AppEntryColdStart() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-66560", "XCDQA-66562"));
        SoftAssert sa = new SoftAssert();

        closeApp();
        pause(15);
        String pageBeforeAppLaunch = disneyPlusAndroidTVCommonPage.get().getCurrentPageLayout();
        launchTVapp(APP_PACKAGE_DISNEY, APP_LAUNCH_ACTIVITY);

        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(entitledUser.get().getEmail());
        disneyPlusAndroidTVLoginPage.get().logInWithPassword(entitledUser.get().getUserPass());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(),
                "did not land on multiple profiles page after initial login");

        WebDriver driver = new AndroidTVUtils(getDriver()).rebootAndroidTv();
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage1 = new DisneyPlusAndroidTVCommonPage(driver);
        DisneyPlusAndroidTVProfilePageBase disneyPlusAndroidTVProfilePageBase1 = initPage(driver, DisneyPlusAndroidTVProfilePageBase.class);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase1.isOpened(),
                "did not land on multiple profiles page after restarting Android TV");

        closeApp();
        launchTVapp(APP_PACKAGE_DISNEY, APP_LAUNCH_ACTIVITY);
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase1.isOpened(),
                "did not land on multiple profiles page after restarting Disney app");

        new AndroidTVUtils(driver).pressBack();
        pause(15);
        String pageAfterAppExit = disneyPlusAndroidTVCommonPage1.getCurrentPageLayout();
        sa.assertEquals(pageBeforeAppLaunch, pageAfterAppExit, "App is not exited to original state after pressing back");

        sa.assertAll();
    }

    @Test(enabled = false)
    public void AppEntryColdStart2Hrs() {
        setPartnerZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, country, "XCDQA-88029"));
        SoftAssert sa = new SoftAssert();
        login(entitledUser.get());
        sa.assertTrue(disneyPlusAndroidTVProfilePageBase.get().isOpened(),
                "did not land on multiple profiles page after initial login");

        closeApp();
        pause(7200);
        launchTVapp(APP_PACKAGE_DISNEY, APP_LAUNCH_ACTIVITY);
        sa.assertTrue(initPage(DisneyPlusAndroidTVProfilePageBase.class).isOpened(),
                "did not land on multiple profiles page after restarting Disney app after 2 hrs");
        sa.assertAll();
    }
}
