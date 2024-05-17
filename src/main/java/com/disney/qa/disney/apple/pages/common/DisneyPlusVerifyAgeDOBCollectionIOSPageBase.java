package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusVerifyAgeDOBCollectionIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "birthdateEntryView")
    private ExtendedWebElement verifyAgeDOBPage;

    @ExtendedFindBy(accessibilityId = "verifyAgeButton")
    private ExtendedWebElement verifyAgeButton;

    //FUNCTIONS
    public DisneyPlusVerifyAgeDOBCollectionIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return verifyAgeDOBPage.isPresent();
    }

    public void clickVerifyAgeButton() {
        verifyAgeButton.click();
    }

}