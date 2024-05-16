package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVerifyAgeIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "verifyAge")
    private ExtendedWebElement verifyAge;

    @ExtendedFindBy(accessibilityId = "verifyMaturity")
    private ExtendedWebElement acceptMaturityButton;

    @ExtendedFindBy(accessibilityId = "underAgeButton")
    private ExtendedWebElement declineMaturityButton;

    //FUNCTIONS
    public DisneyPlusVerifyAgeIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return verifyAge.isPresent();
    }

    public void clickIAm21PlusButton() {
        acceptMaturityButton.click();
    }
}