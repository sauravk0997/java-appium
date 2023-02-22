package com.disney.qa.disney.web.appex.userflows;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusConsentCollectionPage extends DisneyPlusBasePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusConsentCollectionPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//*[@data-testid='consent-collection']")
    private ExtendedWebElement consentCollection;

    @FindBy(id = "dob_input")
    private ExtendedWebElement consentDob;

    @FindBy(xpath = "//*[@data-testid='confirm-dob']")
    private ExtendedWebElement confirmButton;

    public ExtendedWebElement getConfirmButton(){
       return confirmButton;
    }

    public void verifyPage() {
        LOGGER.info("Verify consent collection page");
        consentCollection.isVisible();
    }

    public void enterConsentDob(String dob) {
        LOGGER.info("Entering DOB and confirming");
        consentDob.type(dob);
        confirmButton.click();
    }
    
}
