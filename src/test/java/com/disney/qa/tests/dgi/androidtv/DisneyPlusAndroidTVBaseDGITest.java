package com.disney.qa.tests.dgi.androidtv;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.tests.disney.android.tv.DisneyPlusAndroidTVBaseTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.testng.Assert;

@SuppressWarnings("squid:S2187")
public class DisneyPlusAndroidTVBaseDGITest extends DisneyPlusAndroidTVBaseTest {
    protected ThreadLocal<AndroidTVUtils> androidTVUtils = new ThreadLocal<>();

    protected String[] events = R.CONFIG.get("custom_string").split(",");

    public void loginSlowly(DisneyAccount disneyAccount){
        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), "Welcome page did not launch");
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().isLoginPageOpened();
        androidTVUtils.get().sendInput(disneyAccount.getEmail());
        androidTVUtils.get().pressTab();
        androidTVUtils.get().hideKeyboardIfPresent();
        androidTVUtils.get().pressSelect();
        disneyPlusAndroidTVLoginPage.get().isLoginPageOpened();
        disneyPlusAndroidTVLoginPage.get().enterPassword(disneyAccount.getUserPass());
        androidTVUtils.get().hideKeyboardIfPresent();
        androidTVUtils.get().keyPressTimes(AndroidTVUtils::pressTab,2,2);
        androidTVUtils.get().pressSelect();
    }
}