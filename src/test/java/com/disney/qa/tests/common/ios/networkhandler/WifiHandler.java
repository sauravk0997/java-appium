package com.disney.qa.tests.common.ios.networkhandler;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.IOSUtils.SystemBundles;
import com.disney.qa.tests.BaseMobileTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;

public class WifiHandler extends BaseMobileTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static String WIFI_NETWORK = R.CONFIG.get("custom_string").replace("_"," ");

    @BeforeTest
    public void setUp() {
        int maxAttempts = 5;
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("automationName", "XCUITest");
        capabilities.setCapability("app", SystemBundles.SETTINGS.getBundleId());
        do {
            try {
                getDriver("default", capabilities);
                break;
            } catch (Exception e) {
                LOGGER.debug("Unable to initiate driver due to: " + e + "retrying, max attempts remaining: " + maxAttempts);
            }
        } while (maxAttempts-- > 0);
    }

    @Test
    public void revertWifiNetwork() {
        SoftAssert softAssert = new SoftAssert();
        IOSUtils.NetworkHandler networkHandler = new IOSUtils().new NetworkHandler();
        networkHandler.getWifiPage();
        if (networkHandler.checkIfWiFiSelected(WIFI_NETWORK, 2, 10)) {
            skipExecution(WIFI_NETWORK + " is preset network, no need to execute script");
        }
        softAssert.assertTrue(networkHandler.toggleWiFiButtonOnAndOff(WIFI_NETWORK, 5),
                "Error during toggle switch approach");
        softAssert.assertTrue(networkHandler.selectWifi(WIFI_NETWORK, 3 ,30), "Error during picking wifi approach");
        softAssert.assertAll();
    }

}
