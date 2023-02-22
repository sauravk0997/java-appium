package com.disney.qa.tests.dgi.mobile.android;

import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import com.disney.qa.tests.dgi.mobile.BaseMobileDgiTest;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/*
 * Class exists for any Android Mobile/TV specific methods that cannot be used on iOS
 * Presently empty due to there not being any that cannot be imported from the Disney+ Android framework
 */
public class BaseAndroidDgiTest extends BaseMobileDgiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    BaseDisneyTest baseDisneyTest = new BaseDisneyTest();

    protected static final String APP_PACKAGE = "com.disney.disneyplus";
    protected static final String APP_LAUNCH_ACTIVITY = "com.bamtechmedia.dominguez.main.MainActivity";
    protected static final String PLATFORM = "android";
    protected static final String ENV = R.CONFIG.get("capabilities.custom_env").toLowerCase();

    /**
     * Parameter is more for clarity than functionality, as calling proxy.newHar() will
     * wipe prior data recording anyway.
     *
     * @param startProxyRecording pass true when recording traffic for onboarding is required
     */
    public void internalSetup(boolean startProxyRecording){
        printUserFriendlyRetry();
        new BaseDisneyTest().setEnvironment();
        clearDataAndRelaunch();
        if(startProxyRecording){
            ProxyPool.getProxy().newHar();
        }
        resetOnLaunchHang();
    }

    //Resets package data for fresh install state and launches the application
    private void clearDataAndRelaunch(){
        new AndroidUtilsExtended().clearAppCache(APP_PACKAGE);
        AndroidService service = AndroidService.getInstance();
        service.openApp(String.format("%s/%s", APP_PACKAGE, APP_LAUNCH_ACTIVITY));
    }

    /**
     * Ensures a relaunch after reset loads the Welcome page. If not, driver closes all open apps in the stack
     * by the OS 'Close all apps' function.
     */
    private void resetOnLaunchHang(){
        try{
            new WebDriverWait(getDriver(), 30)
                    .until(ExpectedConditions.visibilityOfElementLocated(initPage(DisneyPlusWelcomePageBase.class).genericTextItemType.getBy()));
        } catch (TimeoutException e) {
            LOGGER.info("App relaunch hung on splash. Closing stack and restarting...");
            new AndroidUtilsExtended().closeAppStack((AppiumDriver) getCastedDriver());
            //Pause allows the device to close the applications fully before proceeding
            pause(3);
            AndroidService.getInstance().openApp(String.format("%s/%s", APP_PACKAGE, APP_LAUNCH_ACTIVITY));
        }
    }

    //Method proceeds through login using API generated D+ account data.
    public void mobileLogin(DisneyAccount account){
        baseDisneyTest.login(account, false);
    }
}
