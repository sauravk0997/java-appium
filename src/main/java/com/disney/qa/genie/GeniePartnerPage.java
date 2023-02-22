package com.disney.qa.genie;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class GeniePartnerPage extends GenieBasePage{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='navigation-page-partners']")
    private ExtendedWebElement navigateToPartner;

    @FindBy(xpath = "//*[@data-testid='partner-save-btn']")
    private ExtendedWebElement partnerSaveButton;

    @FindBy(xpath = "//*[@placeholder='Enter Provider Name']")
    private ExtendedWebElement providerNameField;

    @FindBy(xpath = "//*[@data-testid='sourceProviders.0.displayName-field']")
    private ExtendedWebElement providerDisplayNameField;

    @FindBy(xpath = "//*[@placeholder='Select a Country']")
    private ExtendedWebElement partnerCountryDropdown;

    public GeniePartnerPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getNavigateToPartner() {
        return navigateToPartner;
    }

    public void navigateToPartner() {
        LOGGER.info("Navigate to Partner");
        getNavigateToPartner().click();
    }

    public void clickOnCreateNewPartner() {
        LOGGER.info("Click on Create New Partner button");
        getGenericTextEqualsElement("Create New Partner").click();
    }

    public String createNewPartnerName(String partnerName) {
        LOGGER.info("Enter new partner name: {}", partnerName);
        getNameField().type(partnerName);
        return partnerName;
    }

    public String enterProviderNameForPartner(String providerName){
        LOGGER.info("Enter provider name for partner creation: {}", providerName);
        providerNameField.type(providerName);
        return providerName;
    }

    public void enterProviderDisplayName(String providerDisplayName){
        waitForJSToLoad(10);
        providerDisplayNameField.type(providerDisplayName);
    }

    public void clickPartnerSaveButton() {
        LOGGER.info("Click on save button for partner creation");
        partnerSaveButton.click();
    }

    public void selectCountryForPartner(String selectCountry){
        waitUntil(ExpectedConditions.elementToBeSelected(partnerCountryDropdown.getElement()), 5);
        partnerCountryDropdown.click();
        LOGGER.info("Select country: {}", selectCountry);
        getGenericTextEqualsElement(selectCountry).click();
    }
}
