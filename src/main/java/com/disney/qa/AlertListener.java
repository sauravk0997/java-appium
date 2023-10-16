package com.disney.qa;

import com.zebrunner.carina.webdriver.IDriverPool;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Objects;

public class AlertListener implements WebDriverListener, IDriverPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Integer closedAlerts = 0;
    private boolean isTrackingPopUpClosed = false;
    private final WebDriver driver;

    public AlertListener(WebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "driver parameter should contain value.");
    }

    @Override
    public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args) {
        closeTrackingPopUp();
        closeAlert();
    }

    @Override
    public void beforeAnyWebElementCall(WebElement element, Method method, Object[] args) {
        closeTrackingPopUp();
        closeAlert();
    }

    @Override
    public void beforeAnyNavigationCall(WebDriver.Navigation navigation, Method method, Object[] args) {
        closeTrackingPopUp();
        closeAlert();
    }

    public void closeAlert() {
        if (closedAlerts < 2) {
            try {
                Alert alert = driver.switchTo().alert();
                alert.dismiss();
                closedAlerts++;
            } catch (NoAlertPresentException e) {
                //do nothing
            }
        }
    }

    public void closeTrackingPopUp() {
        if (!isTrackingPopUpClosed) {
            try {
                driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`label == \"Ask App Not to Track\"`]"))
                        .click();
                isTrackingPopUpClosed = true;
                LOGGER.info("Tracking popUp closed");
            } catch (NoSuchElementException e) {
                //do nothing
            } catch (Exception e) {
                LOGGER.error("Error when try to close Tracking PopUp: {}", e.getMessage(), e);
            }
        }
    }
}
