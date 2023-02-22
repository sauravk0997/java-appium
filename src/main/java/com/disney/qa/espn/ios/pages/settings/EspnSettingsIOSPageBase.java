package com.disney.qa.espn.ios.pages.settings;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class EspnSettingsIOSPageBase extends EspnIOSPageBase {


    //Objects

    private static final String CUSTOMER_SUPP_CODE_BTN_ACCESSIBILITY_ID = "cell.settings.customerSupportCode";

    private static final String CLOSE_BTN_ACCESSIBILITY_ID = "button.close";

    @FindBy(name = "cell.settings.login")
    private ExtendedWebElement logInBtn;

    @FindBy(name = "cell.settings.login")
    private ExtendedWebElement logOutBtn;




    //Methods

    public EspnSettingsIOSPageBase(WebDriver driver) {
        super(driver);
    }



    public void clickLogoutBtn() {
        logOutBtn.click();
    }

    public void clickCloseBtn() {
        findExtendedWebElement(MobileBy.AccessibilityId(CLOSE_BTN_ACCESSIBILITY_ID)).click();
    }

    public void getCustomerSupportCode() {
      findExtendedWebElement(MobileBy.AccessibilityId(CUSTOMER_SUPP_CODE_BTN_ACCESSIBILITY_ID)).click();
    }

    public void getLogInPage() {
        logInBtn.click();
    }


}
