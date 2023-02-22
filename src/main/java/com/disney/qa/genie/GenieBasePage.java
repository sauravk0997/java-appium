package com.disney.qa.genie;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class GenieBasePage extends AbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[text()='%s']")
    private ExtendedWebElement genericTextEqualsElement;

    @FindBy(xpath = "//span[contains(text(), '%s')]")
    private ExtendedWebElement spanContainsText;

    @FindBy(id = "genie-logo")
    private ExtendedWebElement genieLogo;

    @FindBy(xpath = "//*[@data-testid='entitlement-save-btn']")
    private ExtendedWebElement saveButton;

    @FindBy(xpath = "//*[@data-testid='entitlement-cancel-btn']")
    private ExtendedWebElement cancelButton;

    @FindBy(xpath = "//*[@data-testid='name-field']")
    private ExtendedWebElement nameField;

    @FindBy(xpath = "//*[@data-testid='description-field']")
    private ExtendedWebElement descriptionField;

    @FindBy(xpath = "//div[@aria-colindex='1' and @data-rowindex='0']")
    private ExtendedWebElement newestEntitlement;

    @FindBy(xpath = "//*[@class='MuiChip-label']")
    private ExtendedWebElement nameSpaceDropdown;

    @FindBy(id = "emailInput")
    private ExtendedWebElement ssoEmailInput;

    @FindBy(xpath = "//input[@name='HomeRealmByEmail']")
    private ExtendedWebElement nextButton;

    @FindBy(id = "passwordInput")
    private ExtendedWebElement ssoPasswordInput;

    @FindBy(id = "submitButton")
    private ExtendedWebElement ssoSignInButton;

    @FindBy(xpath = "//*[contains(@class, 'MuiButton-containedPrimary')]")
    private ExtendedWebElement loginButton;

    @FindBy(id = "%s-field-helper-text")
    private ExtendedWebElement requiredFieldError;

    @FindBy(xpath = "//div[@data-rowindex='0']//div[@data-colindex='1']/div")
    private ExtendedWebElement firstItemOnFirstRow;

    @FindBy(xpath = "//*[@data-testid='change-history-date']")
    private List<ExtendedWebElement> editButtonForChangeHistory;

    private static final String CHANGE_HISTORY = "Change History";
    private static final String EDIT = " Edit";

    public GenieBasePage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getGenericTextEqualsElement(String text) {
        return genericTextEqualsElement.format(text);
    }

    public ExtendedWebElement getFirstItemOnFirstRow(){
        return firstItemOnFirstRow;
    }

    public ExtendedWebElement getSpanContainsText(String text) {
        return spanContainsText.format(text);
    }

    public ExtendedWebElement getGenieLogo() {
        return genieLogo;
    }

    public ExtendedWebElement getLoginButton() {
        return loginButton;
    }

    public ExtendedWebElement getNameSpaceMenuDropdown() {
        return nameSpaceDropdown;
    }

    public ExtendedWebElement getSaveButton() {
        return saveButton;
    }

    public ExtendedWebElement getCancelButton() {
        return cancelButton;
    }

    public ExtendedWebElement getNameField() {
        return nameField;
    }

    public ExtendedWebElement getDescriptionField() {
        return descriptionField;
    }

    public ExtendedWebElement getRequiredFieldError(String field) {
        return requiredFieldError.format(field);
    }

    public GenieBasePage open(WebDriver driver) {
        environmentSetUp(R.CONFIG.get("env"));
        return new GenieBasePage(driver);
    }

    public void environmentSetUp(String env) {
        switch (env) {
            case "DEV":
                getDriver().get(GenieParameter.DISNEY_GENIE_DEV_WEB.getValue());
                break;
            case "QA":
                getDriver().get(GenieParameter.DISNEY_GENIE_QA_WEB.getValue());
                break;
            default:
                LOGGER.info("Please provide valid environment...");
                break;
        }
    }

    public void clickOnLogin() {
        getLoginButton().click();
    }

    public void ssoLogin(String username, String password) {
        ssoEmailInput.type(username);
        nextButton.click();
        ssoPasswordInput.type(password);
        ssoSignInButton.click();
    }

    public void selectNameSpace(String text) {
        if (genericTextEqualsElement.format(text).isElementNotPresent(5)) {
            getNameSpaceMenuDropdown().click();
            genericTextEqualsElement.format(text).click();
        }
    }

    public void enterDescription(String description) {
        waitForJSToLoad(10);
        LOGGER.info("Enter new description: {}", description);
        getDescriptionField().type(description);
    }

    public void clickOnSaveButton() {
        LOGGER.info("Click on Save button to save New Entitlement");
        getSaveButton().click();
    }

    public void clickOnCancelButton() {
        LOGGER.info("Create New Entitlement");
        getCancelButton().click();
    }

    public void selectFirstItemOnFirstRow() {
        pause(10);
        LOGGER.info("Selecting first item on the first row");
        getFirstItemOnFirstRow().click();
        pause(SHORT_TIMEOUT);
    }

    public void clickOnChangeHistoryButton(){
        waitForJSToLoad(15);
        waitUntil(ExpectedConditions.elementToBeClickable(getSpanContainsText(CHANGE_HISTORY).getElement()), 10);
        getSpanContainsText(CHANGE_HISTORY).click();
        pause(5);
    }

    public void clickOnEditButtonForChangeHistory(){
        waitForJSToLoad(15);
        waitUntil(ExpectedConditions.elementToBeClickable(getGenericTextEqualsElement(EDIT).getElement()), 10);
        LOGGER.info("CLick on Edit button for change history");
        getGenericTextEqualsElement(EDIT).click();
        pause(SHORT_TIMEOUT);
    }

    public int getChangeHistorySize(){
        pause(SHORT_TIMEOUT);
        LOGGER.info("Change History table size: {}", editButtonForChangeHistory.size());
       return editButtonForChangeHistory.size();
    }

    public boolean verifyNewlyCreatedItem(String newItem) {
        LOGGER.info("Verify newly created item: {}", newItem);
        return getFirstItemOnFirstRow().getText().equals(newItem);
    }
}

