package com.disney.qa.genie;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class GenieEntitlementPage  extends GenieBasePage{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='navigation-page-entitlements']")
    private ExtendedWebElement navigateToEntitlementPage;

    @FindBy(xpath = "//*[text()='Create New Entitlement']")
    private ExtendedWebElement createNewEntitlementButton;

    @FindBy(xpath = "//*[@data-field='name']")
    private List<ExtendedWebElement> entitlementID;

    @FindBy(xpath = "//div[@aria-colindex='1' and @data-rowindex='0']")
    private ExtendedWebElement newestEntitlement;

    @FindBy(xpath = "//*[@data-testid='internal-field']")
    private ExtendedWebElement internalFieldDropdown;

    @FindBy(xpath = "//*[contains(@class,'MuiMenu-list')]/li")
    private List<ExtendedWebElement> internalType;

    @FindBy(xpath = "//*[@data-testid='category-field']")
    private ExtendedWebElement entitlementCategoryFieldDropdown;

    @FindBy(xpath = "//*[@data-value='%s']")
    private ExtendedWebElement entitlementCategoriesType;

    public GenieEntitlementPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getCreateNewEntitlementButton() {
        return createNewEntitlementButton;
    }

    public ExtendedWebElement getNavigateToEntitlementPage() {
        return navigateToEntitlementPage;
    }

    public ExtendedWebElement getInternalType(int selectInternalType) {
        return internalType.get(selectInternalType);
    }

    public ExtendedWebElement getEntitlementCategoryType(String entitlementCategoryType) {
        return entitlementCategoriesType.format(entitlementCategoryType);
    }

    public void navigateToEntitlementsPage() {
        LOGGER.info("Navigate to entitlements page");
        getNavigateToEntitlementPage().click();
    }

    public void clickOnCreateNewEntitlement() {
        LOGGER.info("Create New Entitlement");
        getGenericTextEqualsElement("Create New Entitlement").click();
    }

    public ExtendedWebElement getEntitlementId(int index) {
        return entitlementID.get(index);
    }

    public String createNewEntitlementName(String entitlementName) {
        LOGGER.info("Enter new entitlement name to create");
        getNameField().type(entitlementName);
        return entitlementName;
    }

    public boolean verifyNewlyAddedEntitlementId(String newEntitlementName) {
        LOGGER.info("Verify New Entitlement Id: {}", newEntitlementName);
        return newestEntitlement.getAttribute("data-value").equals(newEntitlementName);
    }

    public void selectInternalType(int internalType) {
        internalFieldDropdown.click();
        LOGGER.info("Select internal type: {}", internalType);
        getInternalType(internalType).click();
    }

    public String selectEntitlementCategoryType(String entitlementCategoryType) {
        waitUntil(ExpectedConditions.elementToBeSelected(entitlementCategoryFieldDropdown.getElement()), 10);
        entitlementCategoryFieldDropdown.click();
        LOGGER.info("Select Entitlement category type: {}", entitlementCategoryType);
        getEntitlementCategoryType(entitlementCategoryType).click();
        return entitlementCategoryType;
    }

}
