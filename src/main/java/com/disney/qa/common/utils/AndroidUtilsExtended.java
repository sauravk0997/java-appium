package com.disney.qa.common.utils;

import java.lang.invoke.MethodHandles;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.qaprosoft.carina.core.foundation.utils.android.IAndroidUtils;
import com.qaprosoft.carina.core.foundation.utils.common.CommonUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;

/**
 * Keep this file for possible future Android Utils extending
 */

public class AndroidUtilsExtended extends MobileUtilsExtended implements IAndroidUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    //Samsung OS has a specific ID on their clear-all button. Others use generic tag.
    public boolean closeAppStack(AppiumDriver<?> driver){
        LOGGER.info("Closing app via system 'Clear All' button.");
        AndroidService.getInstance().pressKeyboardKey(AndroidKey.APP_SWITCH);
        boolean closed = useSystemCloseAppsButton(driver);
            if(!closed) {
                closed = setAppsToDefaultLeft() || useSystemCloseAppsButton(driver);
            }
            if(!closed) {
                closed = setAppsToDefaultTop() || useSystemCloseAppsButton(driver);
            }
        return closed;
    }

    private boolean useSystemCloseAppsButton(AppiumDriver<?> driver){
        try {
            //Thread sleep gives the device time to display the apps. Navigation ensures the required button is visible.
            Thread.sleep(1000);
            driver.findElementByClassName("android.widget.Button").click();
            return true;
        } catch (NoSuchElementException e1) {
            LOGGER.error("Button class not found. Checking for ui button ID...");
            try {
                driver.findElementById("com.android.systemui:id/button").click();
                return true;
            } catch (Exception e2) {
                LOGGER.error("Google button not found. Might be too many apps open. Swiping apps and trying again...");
                return false;
            }
        } catch (InterruptedException t){
            LOGGER.info("ERROR - Thread interrupted: " + t);
            Thread.currentThread().interrupt();
            return false;
        }
    }

    private boolean setAppsToDefaultLeft(){
        try {
            wideSwipeRight(5, 1000);
        } catch (InvalidElementStateException e){
            LOGGER.info("All possible swipes completed.");
            return true;
        }
        return false;
    }

    private boolean setAppsToDefaultTop(){
        try {
            swipeDown(5, 1000);
        } catch (InvalidElementStateException e){
            LOGGER.info("All possible swipes completed.");
            return true;
        }
        return false;
    }

    public void backgroundApp(){
        AndroidService.getInstance().pressKeyboardKey(AndroidKey.HOME);
    }

    public String getDeviceModel(){
        AndroidService androidService = AndroidService.getInstance();
        return StringUtils.substringAfter(androidService.executeAdbCommand("shell getprop | grep 'ro.product.model'"), "ro.product.model: ");
    }

    public boolean isCarrierConnectionAvailable(){
        AndroidService androidService = AndroidService.getInstance();
        boolean status = ((AndroidDriver<?>)this.castDriver()).getConnection().isDataEnabled();
        boolean linkProperties = false;

        String linkProp = androidService.executeAdbCommand("shell dumpsys telephony.registry | grep mPreciseDataConnectionState");
        LOGGER.info("PROP:  " + linkProp);
        if(!linkProp.isEmpty()) {
            linkProperties = !StringUtils.substringBetween(linkProp, "APN: ", " ").equals("null");
        }
        LOGGER.info("STATUS ENABLED: " + status);
        LOGGER.info("CARRIER AVAILABLE: " + linkProperties);
        return ((AndroidDriver<?>)this.castDriver()).getConnection().isDataEnabled() && linkProperties;
    }

    public void launchAppThroughStartActivity(String appPackage, String appActivity){
        AndroidService androidService = AndroidService.getInstance();
        androidService.executeShell("am start -n " + appPackage + "/" + appActivity);
    }

    public void openAppSystemMenu(String appName){
        AndroidService androidService = AndroidService.getInstance();
        androidService.executeAdbCommand("shell am start -a android.settings.APPLICATION_SETTINGS");

        ExtendedWebElement appItem = new ExtendedWebElement(By.xpath(String.format("//*[contains(@text, '%s')]", appName)), "notifications", getDriver(), getDriver());
        swipe(appItem);

        appItem.click();
    }


    /*
     * 'element' is how Android OSs observed from version 8+ display system level alert controls
     * 'element2' is how Android OSs observed from version 7- display system level alert controls.
     * They were essentially reversed when Android went from 7 to 8, as well as changed the toggle text.
     */
    public void toggleAppNotifications(String appName, boolean setValue){
        openAppSystemMenu(appName);

        WebDriver driver = getDriver();
        ExtendedWebElement element = new ExtendedWebElement(By.xpath("//*[contains(@text, 'Notifications') or contains(@text, 'notifications')]"), "notifications", driver, driver);
        element.click();

        element = new ExtendedWebElement(By.xpath("//*[@resource-id='com.android.settings:id/switch_text']/following-sibling::android.widget.Switch"), "toggle", driver, driver);
        ExtendedWebElement element2 = new ExtendedWebElement(By.xpath("//*[@resource-id='android:id/title' and @text='Block']/../following-sibling::*/*/*[@class='android.widget.Switch']"), "toggle", driver, driver);
        if(element.isElementPresent(3) && Boolean.valueOf(element.getAttribute("checked")) != setValue){
            element.click();
        } else if(element2.isElementPresent(3) && Boolean.valueOf(element2.getAttribute("checked")) == setValue) {
            element2.click();
        } else {
            LOGGER.info("MISSING ITEM");
        }
    }

    public void resetAndRelaunch(String appPackage, String appActivity){
        clearAppCache(appPackage);
        AndroidService.getInstance().executeAdbCommand(String.format("shell am start -n %s/%s", appPackage, appActivity));
    }

    public void clickPushNotification(String alertBody){
        AndroidService.getInstance().expandStatusBar();
        WebDriver driver = getDriver();
        ExtendedWebElement alert = new ExtendedWebElement(By.xpath(String.format("//*[@text='%s']", alertBody)), "alert", driver, driver);
        alert.click();
    }

    //TODO: Additional element IDs should be migrated to Core as it does not contain all valid Android possibilities for modern devices
    public void clearNotifications(){
        AndroidService.getInstance().expandStatusBar();
        WebDriver driver = getDriver();
        ExtendedWebElement notificationStack = new ExtendedWebElement(By.id("com.android.systemui:id/notification_stack_scroller"), "tray", driver, driver);
        ExtendedWebElement clearAllButton = new ExtendedWebElement(By.xpath("//*[@resource-id='com.android.systemui:id/clear_all' or @resource-id='com.android.systemui:id/clear_all_button' or @resource-id='com.android.systemui:id/dismiss_text']"), "button", driver, driver);
        
        try{
            swipe(clearAllButton, notificationStack);
            if(clearAllButton.getAttribute("enabled").equals("true")) {
                LOGGER.info("Clicking 'Clear All' button...");
                clearAllButton.click();
            } else {
                LOGGER.info("'Clear All' Button is present but disabled, meaning any alerts displayed are not closable. Collapsing tray...");
                pressBack();
            }
        } catch (AssertionError e){
            LOGGER.info("Device tray closed by swiping which means no notifications were present. Proceeding with test.");
        }
    }

    /**
     * Waits for the WiFi connection on the device to actually connect before moving on
     */
    public void waitForWifiToStabilize() {
        LOGGER.info("Verifying WiFI connectivity...");
        int timer = 0;
        while(!isWiFiConnected() && timer < 60) {
            CommonUtils.pause(1);
            timer++;
        }
    }

    //TODO: Look into implementing into Carina directly
    public boolean isWiFiConnected() {
        return !new AndroidService().executeAdbCommand("shell ip -f inet addr show | grep wlan0 | grep inet").isEmpty();
    }

    public void setPortraitCapability() {
        LOGGER.info("Setting orientation to PORTRAIT as default via capabilities...");
        R.CONFIG.put("capabilities.orientation", "PORTRAIT");
        setOrientation(AndroidUtilsExtended.Orientations.PORTRAIT);
    }

    public enum Orientations {
        PORTRAIT(0),
        LANDSCAPE(1);

        Integer orientation;

        Orientations(Integer orientation){
            this.orientation = orientation;
        }

    }

    public void setOrientation(Orientations val){
        Boolean success;
        int attempts = 0;
        String command = String.format("shell settings put system user_rotation %s", val.orientation);
        AndroidService androidService = AndroidService.getInstance();
        do {
            attempts++;
            androidService.executeAdbCommand(command);
            success = androidService.executeAdbCommand("shell dumpsys input | grep 'SurfaceOrientation'").contains("SurfaceOrientation: " + val.orientation);
        } while (!success && attempts < 3);
    }

    public void reopenApp(String appName) {
        ExtendedWebElement element = new ExtendedWebElement(By.xpath("//*[(@content-desc=\"" + appName + "\")]"),
                appName, getDriver(), getDriver());
        IAndroidUtils.UTILS_LOGGER.info("tap on {}", element.getName());
        Point point = element.getLocation();
        Dimension size = element.getSize();
        this.tap(point.getX() + size.getWidth() / 2, point.getY() + size.getHeight() / 2, 0);
    }

}
