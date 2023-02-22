package com.disney.qa.tests.common.android.fakegps;

import com.disney.qa.tests.BaseMobileTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;

/**
 * Start Fake GPS
 */


public class FakeGPSUsage extends BaseMobileTest {

    public static final String LOCATION_TO_USE = "New York";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private AndroidService androidService = AndroidService.getInstance();

    public String origin_app;

    @BeforeSuite(alwaysRun = true)
    public void setConfig() throws Throwable {
        LOGGER.info("Start Fake GPS app here...");
        origin_app = R.CONFIG.get("mobile_app");
        LOGGER.info("Origin app is : " + origin_app);

        androidService = AndroidService.getInstance();

        try{
            getDriver();
        } catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            androidService.executeAdbCommand("shell pm grant com.foxsports.videogo.internal android.permission.ACCESS_FINE_LOCATION");
            restartDriver(true);
        }
        if(getDriver().findElements(By.id("com.android.packageinstaller:id/permission_allow_button")).size()>0){
            LOGGER.info("Found Allow Button and click it.");
            getDriver().findElement(By.id("com.android.packageinstaller:id/permission_allow_button")).click();
        }

    }

    @AfterSuite(alwaysRun = true)
    public void returnConfig() throws Throwable {
        //LOGGER.info("Origin app return back : " + origin_app);
        //R.CONFIG.put("mobile_app", origin_app);
    }


    @Test(description = "Start Fake GPS")
    public void startFakeGPS() {

        androidService.setFakeGPSLocation(LOCATION_TO_USE);
    }

    @Test(description = "Stop Fake GPS")
    public void stopFakeGPS() {

        androidService.stopFakeGPS();
    }

    @Deprecated
    //Local resource loader example
    protected String getResourceFilePath(String resourceFileName) {
        String filePath = "";
        try {
            filePath = URLDecoder.decode(getClass().getClassLoader().getResource(resourceFileName).getFile(), "utf-8");
            LOGGER.info("Path1: " + filePath);
            String filePath2 = URLDecoder.decode(getClass().getClassLoader().getSystemResource(resourceFileName).getFile(), "utf-8");
            LOGGER.info("Path2: " + filePath2);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        if (filePath.contains(":")) {
            LOGGER.info("Windows execution. Return with substring.");
            filePath=filePath.substring(1);
        } else {
            if (filePath.startsWith("/")) {
                LOGGER.info("Path is correct: " + filePath);
            } else filePath = "/" + filePath;
        }
        return filePath;
    }

}
