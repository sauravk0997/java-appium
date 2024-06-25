package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVerifyAgeIOSPageBase extends DisneyPlusApplePageBase {

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "verifyAge")
    private ExtendedWebElement verifyAge;

    @ExtendedFindBy(accessibilityId = "verifyMaturity")
    private ExtendedWebElement acceptMaturityButton;

    @ExtendedFindBy(accessibilityId = "underAgeButton")
    private ExtendedWebElement declineMaturityButton;

    @ExtendedFindBy(accessibilityId = "cancelBarButton")
    private ExtendedWebElement cancelButton;

    //FUNCTIONS
    public DisneyPlusVerifyAgeIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return verifyAge.isPresent();
    }

    public void clickIAm21PlusButton() {
        waitForPresenceOfAnElement(acceptMaturityButton);
        acceptMaturityButton.click();
    }

    public void clickCancelButton() {
        cancelButton.click();
    }
}