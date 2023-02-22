package com.disney.qa.genie;

import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeniePromotionPage extends GenieBasePage{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='navigation-page-promotions']")
    private ExtendedWebElement navigateToPromotion;

    @FindBy(xpath = "//*[text()='Create New Promotion']")
    private ExtendedWebElement createNewPromotionButton;

    @FindBy(xpath = "//*[@data-testid='code-field']")
    private ExtendedWebElement promotionCode;

    @FindBy(xpath = "//*[@placeholder='Select Campaign']")
    private ExtendedWebElement partnershipId;

    @FindBy(xpath = "//*[@data-testid='offerId-field']//input")
    private ExtendedWebElement associatedOffer;

    @FindBy(xpath = "//*[@data-testid='startDate-field']")
    private ExtendedWebElement startDateField;

    @FindBy(xpath = "//*[@data-testid='endDate-field']")
    private ExtendedWebElement endDateField;

    @FindBy(xpath = "//*[@data-testid='promotion-save-btn']")
    private ExtendedWebElement promoSaveButton;

    @FindBy(xpath = "//*[@data-testid='country-field']//input")
    private ExtendedWebElement associatedCountryField;

    @FindBy(xpath = "//*[@data-testid='skuKey-field']//input")
    private ExtendedWebElement associatedSkuField;

    @FindBy(xpath = "//*[@data-testid='partner-field']//input")
    private ExtendedWebElement promotionalPartnerName;

    @FindBy (xpath = "//div[@data-rowindex='0']//div[@data-colindex='1']//p")
    private ExtendedWebElement newlyCreatedPromotionName;

    @FindBy (xpath = "//*[@data-testid='promotion-save-btn']")
    private ExtendedWebElement promotionSaveButton;

    @FindBy (xpath = "//*[@data-testid='promotion-edit-btn']")
    private ExtendedWebElement promotionEditButton;

    protected static final String BROWSER = R.CONFIG.get("browser");

    public GeniePromotionPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getNavigateToPromotion() {
        return navigateToPromotion;
    }

    public void navigateToPromotion() {
        LOGGER.info("Navigate to Partner");
        getNavigateToPromotion().click();
    }

    public void clickOnCreateNewPromotion() {
        LOGGER.info("Click on Create New promotion button");
        createNewPromotionButton.click();
    }

    public void enterStartDate(){
        pause(SHORT_TIMEOUT);// Needed for calendar to load
        startDateField.type(retrieveCurrentTime());
        pause(SHORT_TIMEOUT);
        startDateField.sendKeys(Keys.ENTER);
    }

    public void enterEndDate(){
        pause(SHORT_TIMEOUT);// Needed for calendar to load
        endDateField.type(retrieveCurrentTime());
        pause(SHORT_TIMEOUT);
        endDateField.sendKeys(Keys.ARROW_DOWN);
        endDateField.sendKeys(Keys.ENTER);
    }

    public String enterPromotionCodeName(String promotionCodeName){
        waitForJSToLoad(10);
        LOGGER.info("Enter promotion code name to create new promotion");
        promotionCode.type(promotionCodeName);
        return promotionCodeName;
    }

    public void enterPartnershipIdName(){
        waitForJSToLoad(10);
        partnershipId.type("Automation_Partner");
        partnershipId.sendKeys(Keys.ARROW_DOWN);
        partnershipId.sendKeys(Keys.ENTER);
    }

    public void selectAssociatedOffer() {
        waitForJSToLoad(10);
        LOGGER.info("Select associated offer from dropdown");
        associatedOffer.click();
        associatedOffer.sendKeys(Keys.ARROW_DOWN);
        pause(2);
        associatedOffer.sendKeys(Keys.ENTER);
    }

    public void selectAssociatedCountry(String countryName){
        LOGGER.info("Select assocaited country to create new promotion");
        associatedCountryField.type(countryName);
        associatedCountryField.sendKeys(Keys.ARROW_DOWN);
        associatedCountryField.sendKeys(Keys.ENTER);
    }

    public void clickOnPromoSaveButton(){
        LOGGER.info("Click on save button to create new promotion");
        promoSaveButton.click();
    }

    public void selectAssociatedSku(){
        waitForJSToLoad(15);
        LOGGER.info("Select SKU for associated offer to be created");
        associatedSkuField.click();
        pause(2);
        associatedSkuField.sendKeys(Keys.ARROW_DOWN);
        associatedSkuField.sendKeys(Keys.ENTER);
    }

    public void enterPromotionalPartnerName() {
        waitForJSToLoad(10);
        promotionalPartnerName.click();
        promotionalPartnerName.sendKeys(Keys.ARROW_DOWN);
        promotionalPartnerName.sendKeys(Keys.ENTER);
        pause(5);
}

    public boolean verifyNewlyAddedPromotionName(String newPromotionName) {
        LOGGER.info("Verify New Promotion name: {}", newPromotionName);
        return newlyCreatedPromotionName.getText().equalsIgnoreCase(newPromotionName);
    }

    public void selectNewlyCreatedPromotion(){
        LOGGER.info("Selecting newly created Promotion");
        waitForJSToLoad(10);
        newlyCreatedPromotionName.click();
    }

    public void clickOnPromotionSaveButton() {
        LOGGER.info("Click on Save button to save");
        waitUntil(ExpectedConditions.elementToBeClickable(promotionSaveButton.getElement()), 10);
        promotionSaveButton.click();
    }

    public void clickOnPromotionEditButtonForChangeHistory(){
        waitForJSToLoad(15);
        waitUntil(ExpectedConditions.elementToBeClickable(promotionEditButton.getElement()), 10);
        LOGGER.info("CLick on Edit button for change history");
        promotionEditButton.click();
        pause(SHORT_TIMEOUT);
    }

    public String retrieveCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm a");
        Date currentDate = new Date();
        String formattedCurrentDate= dateFormat.format(currentDate);

        LOGGER.info("Current date and time is {}", formattedCurrentDate);

        return formattedCurrentDate;
    }
}
