package com.disney.qa.disney.web.commerce;

import java.lang.invoke.MethodHandles;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAccountBlockedPage extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='-title']") 
    private ExtendedWebElement notEligibleTitle;

    @FindBy(xpath = "//*[@data-testid='-button']")
    private ExtendedWebElement helpCenterButton;

    @FindBy(xpath = "//*[@data-testid='-secondary-button']") 
    private ExtendedWebElement helpCenterDismissButton;

    public DisneyPlusAccountBlockedPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifyPage() {
        LOGGER.info("Verifying Account Blocked page is loaded");
        return notEligibleTitle.isVisible();
    }

    public DisneyPlusAccountBlockedPage clickHelpCenter() {
        LOGGER.info("Clicking Help Center Button");
        helpCenterButton.click();
        return this;
    }

    public DisneyPlusAccountBlockedPage clickDismissButton() {
        LOGGER.info("Clicking Help Center Dismiss Button");
        helpCenterDismissButton.click();
        return this;
    }    
}
