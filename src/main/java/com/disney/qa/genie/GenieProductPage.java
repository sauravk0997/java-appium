package com.disney.qa.genie;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class GenieProductPage extends GenieBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='navigation-page-products']")
    private ExtendedWebElement navigateToProduct;

    @FindBy(xpath = "//*[@data-testid='entitlements-field']//p")
    private ExtendedWebElement entitlementReqField;

    @FindBy(xpath = "//*[@data-testid='type-field']")
    private ExtendedWebElement productTypeDropdown;

    @FindBy(xpath = "//*[@data-value='%s']")
    private ExtendedWebElement productType;

    @FindBy(xpath = "//*[@data-testid='entitlements-field']//input")
    private ExtendedWebElement entitlementField;

    @FindBy(xpath = "//div[@data-rowindex='0']//div[@data-colindex='1']/div")
    private ExtendedWebElement newlyCreatedProduct;

    @FindBy(xpath = "//*[@data-testid='entitlements-value']/span")
    private List<ExtendedWebElement> newProductEntitlement;

    @FindBy(xpath = "//*[@type='datetime-local']")
    private ExtendedWebElement catalogField;

    @FindBy(xpath = "//*[@data-testid='product-save-btn']")
    private ExtendedWebElement productSaveButton;

    public GenieProductPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getNavigateToProduct() {
        return navigateToProduct;
    }

    public ExtendedWebElement getEntitlementReqField() {
        return entitlementReqField;
    }

    public ExtendedWebElement getProductType(String productTypeName) {
        return productType.format(productTypeName);
    }

    public void navigateToProducts() {
        LOGGER.info("Navigate to Products");
        getNavigateToProduct().click();
    }

    public void clickOnCreateNewProduct() {
        LOGGER.info("Create New Product");
        getGenericTextEqualsElement("Create New Product").click();
    }

    public void clickProductSaveButton() {
        productSaveButton.click();
    }

    public String createNewProductName(String productName) {
        LOGGER.info("Enter new entitlement name to create: {}", productName);
        getNameField().type(productName);
        return productName;
    }

    public String selectProductType(String productTypeName) {
        productTypeDropdown.click();
        LOGGER.info("Select Product type: {}", productTypeName);
        getProductType(productTypeName).click();
        return productTypeName;
    }

    public void selectEntitlement(String entitlementName){
        entitlementField.click();
        entitlementField.type(entitlementName);
        new WebDriverWait(getDriver(), 20).until(ExpectedConditions.visibilityOfElementLocated(entitlementField.getBy()));
        entitlementField.sendKeys(Keys.ARROW_DOWN);
        entitlementField.sendKeys(Keys.ENTER);
        entitlementField.sendKeys(Keys.ARROW_DOWN);
        entitlementField.sendKeys(Keys.ENTER);
    }

    public void selectEntitlement(){
        entitlementField.click();
        new WebDriverWait(getDriver(), 20).until(ExpectedConditions.visibilityOfElementLocated(entitlementField.getBy()));
        entitlementField.sendKeys(Keys.ARROW_DOWN);
        entitlementField.sendKeys(Keys.ENTER);
    }

    public boolean verifyNewlyCreatedProduct(String newProductName) {
        LOGGER.info("Verify New Product name: {}", newProductName);
        return newlyCreatedProduct.getText().equals(newProductName);
    }

    public void clickCatalogField(){
        catalogField.click();
    }

    public void clickOnNewlyCreatedProduct(){
        newlyCreatedProduct.click();
    }

    public boolean getNumberOfEntitlements(int numberOfEntitlements) {
        return newProductEntitlement.size() == numberOfEntitlements;
    }
}
