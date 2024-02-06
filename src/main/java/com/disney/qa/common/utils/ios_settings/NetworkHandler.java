package com.disney.qa.common.utils.ios_settings;

import com.disney.qa.common.utils.IOSUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Duration;

public  class NetworkHandler implements IOSUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final By wifiSwitch = By.xpath("//XCUIElementTypeSwitch[@name='Wi-Fi']");
    private final By wifi = AppiumBy.accessibilityId("WIFI");
    private static final String SELECTED_WIFI = "//XCUIElementTypeStaticText[@name=\"%s\"]/following-sibling::XCUIElementTypeOther/XCUIElementTypeImage";

    public void getWifiPage() {
        terminateApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        launchApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        WebDriver driver = getDriver();
        driver.findElement(wifi).click();
    }

    public IOSUtils.ButtonStatus getBtnStatus(int maxAttempts) {
        do {
            try {
                int buttonValue = Integer.parseInt(getDriver().findElement(wifiSwitch).getAttribute(IOSUtils.Attributes.VALUE.getAttribute()));
                if (buttonValue == 1) {
                    return IOSUtils.ButtonStatus.ON;
                } else if (buttonValue == 0) {
                    return IOSUtils.ButtonStatus.OFF;
                }
            } catch (NoSuchElementException | NumberFormatException | StaleElementReferenceException e) {
                LOGGER.debug("Button status couldn't be fetched due to:\n{}\nMax attempts remaining: {}", e, +maxAttempts);
            }
        } while (maxAttempts-- > 0);
        return IOSUtils.ButtonStatus.INVALID;
    }

    public boolean checkIfWiFiSelected(String wifiName, int maxAttempts, int explicitWait) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(explicitWait));
        do {
            try {
                if ("checkmark".equalsIgnoreCase(wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath(String.format(SELECTED_WIFI, wifiName)))).getAttribute(IOSUtils.Attributes.NAME.getAttribute()))) {
                    return true;
                }
            } catch (Exception e) {
                if (getBtnStatus(3) == IOSUtils.ButtonStatus.OFF) {
                    LOGGER.info("Wifi switch is turned off, needs to be toggled on..");
                    break;
                } else if (getBtnStatus(3) == IOSUtils.ButtonStatus.ON) {
                    LOGGER.info("Different wifi network selected, need to pick correct wifi");
                    break;
                } else {
                    LOGGER.debug("Unable to verify 'checkmark' for '{}', due to: {}" +
                            "\n Re-verifying, max attempts remaining: {}", wifiName, e, maxAttempts);
                }
            }
        } while (maxAttempts-- > 0);
        return false;
    }

    public boolean toggleWiFiButtonOnAndOff(String defaultWifiName, int maxAttempts) {
        LOGGER.debug("Attempting to connect to '{}' by toggling wifi button ON and OFF", defaultWifiName);
        toggleWifiBtn("OFF", maxAttempts, IOSUtils.ButtonStatus.OFF);
        LOGGER.debug("Wifi switch turned OFF, trying to turn ON now");
        return toggleWifiBtn("ON", maxAttempts, IOSUtils.ButtonStatus.ON);
    }

    public boolean toggleWifiBtn(String buttonOnOff, int maxAttempts, IOSUtils.ButtonStatus buttonStatus) {
        do {
            LOGGER.info("Trying to switch {} wifi", buttonOnOff);
            try {
                getDriver().findElement(wifiSwitch).click();
                if (getBtnStatus(maxAttempts).equals(buttonStatus)) {
                    LOGGER.info("Successfully turned {} wifi", buttonOnOff);
                    if (IOSUtils.ButtonStatus.OFF.toString().equalsIgnoreCase(buttonOnOff)) {
                        break;
                    } else if (IOSUtils.ButtonStatus.ON.toString().equalsIgnoreCase(buttonOnOff))
                        return true;
                } else {
                    LOGGER.info("Failed to turn {} wifi, retrying..", buttonOnOff);
                }
            } catch (WebDriverException e) {
                LOGGER.debug("Unable to turn {} wifi due to:\n{}\nRetrying, max attempts remaining: {}", buttonOnOff, e, maxAttempts);
            }
        } while (maxAttempts-- > 0 && !getBtnStatus(maxAttempts).equals(buttonStatus));
        return false;
    }

    public boolean selectWifi(String wifiName, int maxAttempts, int explicitWait) {
        while (!checkIfWiFiSelected(wifiName, maxAttempts, 10) && maxAttempts-- > 0) {
            LOGGER.info("{} is not set as current wifi/locator not found", wifiName);
            try {
                WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(explicitWait));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id(wifiName))).click();
                break; //TODO may need to replace with a check
            } catch (Exception e) {
                LOGGER.debug("{} wifi could not be selected due to:\n{}" +
                        "\nTrying again, max attempts remaining: {}", wifiName, e, maxAttempts);
            }
        }
        LOGGER.info("{} is set as current wifi", wifiName);
        return true;
    }

    public void toggleWifi(IOSUtils.ButtonStatus status) {
        getWifiPage();
        toggleWifiBtn(status.toString(), 3, status);
        terminateApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
    }

}