package com.disney.qa.espn.ios.pages.settings;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class EspnSettingsCustomerSupportCodeIOSPageBase extends EspnIOSPageBase {


    //Objects

    private static final String CUSTOMER_SUPPORT_CODE_TITLE_PREDICATE_LOC =
            "name = 'ESPN+ Subscription Support' AND type = 'XCUIElementTypeNavigationBar'";

    private static final String BACK_BTN_ACCESSIBILITY_ID = "Settings";

    @FindBy(id = "Phone")
    private ExtendedWebElement phoneText;

    @FindBy(id = "Email")
    private ExtendedWebElement emailText;

    @FindBy(id = "Web Suport")
    private ExtendedWebElement webSupportText;

    @FindBy(name = "Customer Support Code")
    private ExtendedWebElement customerSupportCodeTitle;




    //Methods

    public EspnSettingsCustomerSupportCodeIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isCustomerSupportCodeTitlePresent() {
        return getDriver().findElement(MobileBy.iOSNsPredicateString(CUSTOMER_SUPPORT_CODE_TITLE_PREDICATE_LOC)).isDisplayed();
    }

    @Override
    public void getSettingsPage() {
        findExtendedWebElement(MobileBy.AccessibilityId(BACK_BTN_ACCESSIBILITY_ID)).click();
    }
}
