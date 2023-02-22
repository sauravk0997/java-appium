package com.disney.qa.tests.disney.android.tv.helpertests;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.utility.navhelper.NavHelper;
import com.disney.qa.tests.disney.android.tv.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import io.appium.java_client.android.nativekey.AndroidKey;

// Smoke tests for the utility helper NavHelper.

public class NavHelperSmokeTests extends DisneyPlusAndroidTVBaseTest {

    @Test()
    public void keyUntilVisibleTest() {
        SoftAssert sa = new SoftAssert();
        login(entitledUser.get());
        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementVisible(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible.");
    }

    @Test()
    public void keyUntilDescChangeTest() {
        SoftAssert sa = new SoftAssert();
        AndroidTVUtils utils = new AndroidTVUtils(getDriver());

        login(entitledUser.get());
        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementVisible(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem().isVisible(),"Nav bar should be visible.");

        navHelper.keyUntilElementDescChanged(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_DOWN);
        sa.assertFalse(utils.isFocused(disneyPlusAndroidTVDiscoverPage.get().getNavHome()), "Nav bar HOME should NOT be focused");

        navHelper.keyUntilElementDescChanged(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_UP);
        sa.assertTrue(utils.isFocused(disneyPlusAndroidTVDiscoverPage.get().getNavHome()), "Nav bar HOME should be focused");
    }

    @Test()
    public void keyUntilDescContainsTest() {
        SoftAssert sa = new SoftAssert();
        AndroidTVUtils utils = new AndroidTVUtils(getDriver());

        login(entitledUser.get());
        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementVisible(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);
        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem().isVisible(),"Nav bar should be visible.");

        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_DOWN, "Settings");
        sa.assertFalse(utils.isFocused(disneyPlusAndroidTVDiscoverPage.get().getNavHome()), "Nav bar HOME should NOT be focused");

        navHelper.keyUntilElementDescContains(() -> disneyPlusAndroidTVDiscoverPage.get().getFocusedNavItem(), AndroidKey.DPAD_UP, "Home");
        sa.assertTrue(utils.isFocused(disneyPlusAndroidTVDiscoverPage.get().getNavHome()), "Nav bar HOME should be focused");
    }

    @Test()
    public void keyUntilVisibleOverrideTest() {
        SoftAssert sa = new SoftAssert();
        login(entitledUser.get());
        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementVisible(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK, 60, 2);

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible.");
    }

    @Test()
    public void keyUntilFocusTest() {
        AndroidTVUtils utils = new AndroidTVUtils(getDriver());

        SoftAssert sa = new SoftAssert();
        login(entitledUser.get());
        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible.");
        sa.assertTrue(utils.isFocused(disneyPlusAndroidTVDiscoverPage.get().getNavHome()), "Nav bar HOME should be focused");

        navHelper.press(AndroidKey.DPAD_DOWN);

        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.DPAD_UP);
        sa.assertTrue(utils.isFocused(disneyPlusAndroidTVDiscoverPage.get().getNavHome()), "Nav bar HOME should be focused");
    }

    @Test()
    public void keyUntilFocusOverrideTest() {
        AndroidTVUtils utils = new AndroidTVUtils(getDriver());

        SoftAssert sa = new SoftAssert();
        login(entitledUser.get());
        NavHelper navHelper = new NavHelper(this.getCastedDriver());
        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.BACK);

        sa.assertTrue(disneyPlusAndroidTVDiscoverPage.get().getNavHome().isVisible(),"Nav bar should be visible.");
        sa.assertTrue(utils.isFocused(disneyPlusAndroidTVDiscoverPage.get().getNavHome()), "Nav bar HOME should be focused");

        navHelper.press(AndroidKey.DPAD_DOWN);

        navHelper.keyUntilElementFocused(() -> disneyPlusAndroidTVDiscoverPage.get().getNavHome(), AndroidKey.DPAD_UP, 60 ,2);
        sa.assertTrue(utils.isFocused(disneyPlusAndroidTVDiscoverPage.get().getNavHome()), "Nav bar HOME should be focused");
    }
}
