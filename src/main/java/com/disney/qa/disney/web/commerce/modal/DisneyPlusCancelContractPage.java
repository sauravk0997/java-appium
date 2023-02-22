package com.disney.qa.disney.web.commerce.modal;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCancelContractPage extends DisneyPlusBasePage {

private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

@FindBy (xpath = "//div[@data-testid = 'cancel-contract']")
private ExtendedWebElement cancelContractTitle;

@FindBy(id = "email")
private ExtendedWebElement emailField;

@FindBy(id = "subId")
private ExtendedWebElement subscriptionIDField;

@FindBy (xpath = "//button[@data-testid = 'complete-cancellation-button']")
private ExtendedWebElement completeCancellationButton;

@FindBy (xpath = "//button[text()='KEEP SUBSCRIPTION']")
private ExtendedWebElement keepSubscriptionButton;

public DisneyPlusCancelContractPage(WebDriver driver) {
    super(driver);
}

public boolean verifyPage() {
    LOGGER.info("Verify Cancel Contract page is loaded");
    return cancelContractTitle.isVisible(LONG_TIMEOUT);
}

public DisneyPlusCancelContractPage enterEmail(String email) {
    LOGGER.info("Entering email to cancel subscription");
    emailField.type(email);
    return this;
}

public DisneyPlusCancelContractPage enterSubscriptionID(String subscriptionID) {
    LOGGER.info("Entering subscriptionID to cancel subscription");
    subscriptionIDField.type(subscriptionID);
    return this;
}

public DisneyPlusCancelContractPage clickKeepSubscriptionButton() {
    LOGGER.info("Clicking keep subscription button");
    keepSubscriptionButton.click();
    return this;
}

public DisneyPlusCancelContractPage clickCancelSubscriptionButton() {
    LOGGER.info("Clicking cancel subscription button");
    completeCancellationButton.click();
    return this;
}
    
}
