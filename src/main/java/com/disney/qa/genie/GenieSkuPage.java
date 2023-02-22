package com.disney.qa.genie;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class GenieSkuPage extends GenieBasePage{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='navigation-page-skus']")
    private ExtendedWebElement navigateToSku;

    @FindBy(xpath = "//*[@data-testid='entitlements-field']//p")
    private ExtendedWebElement entitlementReqField;

    @FindBy(xpath = "//*[@data-testid='type-field']")
    private ExtendedWebElement productTypeDropdown;

    @FindBy(xpath = "//*[@data-testid='sku-save-btn']")
    private ExtendedWebElement skuSaveButton;

    @FindBy(xpath = "//*[@data-testid='key-field']")
    private ExtendedWebElement skuKeyField;

    @FindBy(xpath = "//*[@data-testid='product-field']")
    private ExtendedWebElement skuProductDropdown;

    @FindBy(xpath = "//*[@data-testid='platform-field']")
    private ExtendedWebElement skuPlatformDropdown;

    @FindBy(xpath = "//*[@data-testid='associatedCountries-field']")
    private ExtendedWebElement skuCountryDropdown;

    @FindBy(xpath = "//*[@data-testid='%s-form-grid-item']//p")
    private ExtendedWebElement requiredFieldError;

    @FindBy(xpath = "//*[@data-testid='%s-error']")
    private ExtendedWebElement newRequiredFieldError;

    @FindBy(xpath = "//*[@data-value='%s']")
    private ExtendedWebElement platformTypes;

    @FindBy(xpath = "//*[@data-option-index='%s']")
    private ExtendedWebElement productsOptions;

    @FindBy (xpath = "//div[@data-rowindex='0']//div[@data-colindex='1']/p")
    private ExtendedWebElement newestSkuName;

    public GenieSkuPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getNavigateToSku() {
        return navigateToSku;
    }

    public ExtendedWebElement getRequiredFieldErrorMsg(String fieldName) {
        return requiredFieldError.format(fieldName);
    }

    public ExtendedWebElement getNewRequiredFieldErrorMsg(String fieldName) {
        return newRequiredFieldError.format(fieldName);
    }

    public ExtendedWebElement getPlatformType(String platformType) {
        return platformTypes.format(platformType);
    }

    public ExtendedWebElement getProductType(String productType) {
        return productsOptions.format(productType);
    }

    public void navigateToSkus() {
        LOGGER.info("Navigate to SKUs");
        getNavigateToSku().click();
    }

    public void clickOnCreateNewSku() {
        LOGGER.info("Create New SKU");
        getGenericTextEqualsElement("Create New SKU").click();
    }

    public void navigateToCreateNewSkuButton(){
        navigateToSkus();
        pause(15);
        clickOnCreateNewSku();
    }

    public void clickOnSkuSaveButton() {
        LOGGER.info("Click on Save button to save");
        waitUntil(ExpectedConditions.elementToBeClickable(skuSaveButton.getElement()), 10);
        skuSaveButton.click();
    }

    public String createSkuName(String skuName) {
        LOGGER.info("Enter new entitlement name to create");
        getNameField().type(skuName);
        return skuName;
    }

    public void createSkuDescription(String skuDescription){
        getDescriptionField().type(skuDescription);
    }

    public void createSkuKey(String skuKey){
        if(!skuKeyField.toString().isEmpty()) {
            skuKeyField.doubleClick();
            skuKeyField.sendKeys(Keys.DELETE);
        }
        skuKeyField.type(skuKey);
    }

    public void selectProduct(String productSelection){
        waitUntil(ExpectedConditions.elementToBeSelected(skuProductDropdown.getElement()), 5);
        skuProductDropdown.click();
        waitUntil(ExpectedConditions.elementToBeSelected(getProductType(productSelection).getElement()), 5);
        getProductType(productSelection).click();
    }

    public void selectPlatformType(String platformTypeName) {
        skuPlatformDropdown.click();
        LOGGER.info("Select Product type: {}", platformTypeName);
        waitUntil(ExpectedConditions.elementToBeSelected(getPlatformType(platformTypeName).getElement()), 5);
        getPlatformType(platformTypeName).click();
    }

    public void selectCountry(String selectCountry){
        waitUntil(ExpectedConditions.elementToBeSelected(skuCountryDropdown.getElement()), 5);
        skuCountryDropdown.click();
        LOGGER.info("Select country: {}", selectCountry);
        getGenericTextEqualsElement(selectCountry).click();
    }

    public boolean verifyNewlyAddedSkuName(String newSkuName) {
        LOGGER.info("Verify New SKU name: {}", newSkuName);
        return newestSkuName.getText().equals(newSkuName);
    }

    public void selectNewlyCreatedSku(){
        LOGGER.info("Selecting newly created Sku");
        waitForJSToLoad(10);
        newestSkuName.click();
    }

}
