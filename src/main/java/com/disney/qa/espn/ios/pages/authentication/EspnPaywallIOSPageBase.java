package com.disney.qa.espn.ios.pages.authentication;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class EspnPaywallIOSPageBase extends EspnIOSPageBase {

    //Objects

    private static final String LOG_IN_BTN_ACCESSIBILITY_ID = "loginButton";

    private static final String RESTORE_BTN_ACCESSIBILITY_ID = "RESTORE";

    private static final String CANCEL_BTN_ACCESSIBILITY_ID = "dismiss button";

    private static final String MONTHLY_BTN_ACCESSIBILITY_ID = "productButton-com.espn.monthly.espnplusbase.apple";

    @FindBy(xpath = "//XCUIElementTypeButton[contains(@name, 'monthly')]")
    private ExtendedWebElement monthlyBtn;

    @FindBy(name= "espn_plus_logo")
    private ExtendedWebElement espnLogo;

    @FindBy(id = "START MY 7-DAY FREE TRIAL")
    private ExtendedWebElement freeTrialBtn;

    @FindBy(id = "default_paywall_background")
    private ExtendedWebElement paywallBackgroundImage;

    @FindBy(id = "loginButton")
    private ExtendedWebElement logInBtn;





    //Methods

    public EspnPaywallIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getDriver().findElement(MobileBy.AccessibilityId(LOG_IN_BTN_ACCESSIBILITY_ID)).isDisplayed();
    }

    public boolean arePaywallBasicElementsPresent() {

//        if (isElementPresent(espnLogo, DELAY)) {     //Visibility turned off for ESPN Logo
//           return true;
//        } else {
//            LOGGER.error("ESPN Logo Missing!");
//        }

        if (getDriver().findElement(MobileBy.AccessibilityId(LOG_IN_BTN_ACCESSIBILITY_ID)).isDisplayed()) {
            return true;
        } else {
            LOGGER.error("Login Button Missing!");
        }

        if(getDriver().findElement(MobileBy.AccessibilityId(RESTORE_BTN_ACCESSIBILITY_ID)).isDisplayed()) {
            return true;
        } else {
            LOGGER.error("Restore Button Missing!");
        }

        if(getDriver().findElement(MobileBy.AccessibilityId(CANCEL_BTN_ACCESSIBILITY_ID)).isDisplayed()) {
            return true;
        } else {
            LOGGER.error("Cancel Button Missing!");
        }

        if(getDriver().findElement(MobileBy.AccessibilityId(MONTHLY_BTN_ACCESSIBILITY_ID)).isDisplayed()) {
            return true;
        } else {
            LOGGER.error("Monthly Button Missing!");
        }
        return false;
    }


    public void dismissPaywall() {
        findExtendedWebElement(MobileBy.AccessibilityId(CANCEL_BTN_ACCESSIBILITY_ID)).click();
    }

    public void waitForLoginPage() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(logInBtn.getBy()), 20);
    }

    public EspnPaywallLogInIOSPageBase getLogInPage() {
        findExtendedWebElement(MobileBy.AccessibilityId(LOG_IN_BTN_ACCESSIBILITY_ID)).click();
        waitForPaywall();
        return initPage(EspnPaywallLogInIOSPageBase.class);
    }

    public void waitForPaywall(){
        pause(2);
    }

}
