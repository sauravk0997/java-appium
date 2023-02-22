package com.disney.qa.disney.web.commerce;

import java.lang.invoke.MethodHandles;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;
import org.slf4j.Logger;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusBillingPage extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='onboarding-stepper-3-of-3']")
    private ExtendedWebElement billingStep;

    @FindBy(xpath = "//*[@data-testid='toggle-month']")
    private ExtendedWebElement monthlyToggleButton;

    @FindBy(xpath = "//input[contains(@id,'MONTH')]")
    private ExtendedWebElement monthlyToggleCheckBox;

    @FindBy(xpath = "//*[@data-testid='toggle-year']")
    private ExtendedWebElement yearlyToggleButton;

    @FindBy(xpath = "//input[contains(@id,'YEAR')]")
    private ExtendedWebElement yearlyToggleCheckBox;

    @FindBy(xpath = "(//*[@name='subscribeOptions'])")
    private List<ExtendedWebElement> subscribeOptionsList;

    @FindBy(xpath = "//*[@data-testid='credit-radio-icon']")
    private ExtendedWebElement creditCardRadioButton;

    @FindBy(xpath = "//*[@data-testid='paypal-radio-icon']")
    private ExtendedWebElement paypalRadioButton;

    @FindBy(id = "billing-card-name")
    private ExtendedWebElement billingCardName;

    @FindBy(id = "billing-card-number")
    private ExtendedWebElement billingCardNumber;

    @FindBy(id = "billing-card-exp-date")
    private ExtendedWebElement billingCardExpDate;

    @FindBy(id = "billing-card-CSC")
    private ExtendedWebElement billingCardCvv;

    @FindBy(xpath = "//*[@data-testid='zip-code-input']") 
    private ExtendedWebElement billingZipCodeField;

    @FindBy(id = "react-select-zip-code-dropdown-option-0-0")
    private ExtendedWebElement zipCodeDropdownItem;

    @FindBy(xpath = "//*[@data-testid='zip-code-verified']")
    private ExtendedWebElement billingZipCodeFieldVerified;

    @FindBy(id = "dob_input")
    private ExtendedWebElement billingDobField;

    @FindBy(xpath = "//*[@data-testid='toggle-switch-savePayment']")
    private ExtendedWebElement savePymentToggleButton;

    @FindBy(id = "credit-submit-button")
    private ExtendedWebElement agreeAndSubButton;

    @FindBy(css = ".taxId-container [data-testid=input-error-label]")
    private ExtendedWebElement billingZipCodeTaxIdInputErrorLabel;

    @FindBy(xpath = "//*[@data-testid='dropdown-selector-indicator']")
    private ExtendedWebElement dropdownSelectorIndicator;

    @FindBy(xpath = "//a[contains(text(),'Terms of Use')]")
    private ExtendedWebElement billingPageTermsOfUse;

    @FindBy(xpath = "//*[@data-testid='paypal-submit-button']")
    private ExtendedWebElement paypalSubmitButton;

    @FindBy(xpath = "//*[@data-testid='change-payment-option']")
    private ExtendedWebElement changePaymentCta;

    @FindBy(xpath = "//*[@data-testid='bundle-offer-card']")
    private ExtendedWebElement bundleOfferCard;

    @FindBy(xpath = "//*[@data-testid='bundle-upgrade-card-link']")
    private ExtendedWebElement bundleOfferCardUpgradeLink;

    @FindBy(xpath = "//*[@data-testid='bundle-offer-card-downgrade']")
    private ExtendedWebElement bundleOfferCardDowngradeLink;

    @FindBy(xpath = "//*[@data-testid='mercado-radio-icon']")
    private ExtendedWebElement mercadoRadioIcon;

    public DisneyPlusBillingPage(WebDriver driver) {
        super(driver);
    }

    public boolean verifyPage() {
        LOGGER.info("Verify Billing page is loaded");
        return billingStep.isVisible();
    }

    public DisneyPlusBillingPage clickAnnualToggleButton() {
        LOGGER.info("Clicking annual toggle button");
        yearlyToggleButton.click();
        return this;
    }

    public DisneyPlusBillingPage clickChangePaymentButton() {
        LOGGER.info("Clicking change payment button");
        changePaymentCta.click();
        return this;
    }

    public int getSubscribeOptionsCount() {
        return subscribeOptionsList.size();
    }

    public String getBundleOfferCardDetails() {
       return bundleOfferCard.getText();
    }

    // Credit Card Payment

    public boolean isCreditCardIconVisible() {
        LOGGER.info("Is credit card icon visible");
        return creditCardRadioButton.isVisible(SHORT_TIMEOUT);
    }

    public boolean isCreditCardNameFieldVisible() {
        LOGGER.info("Is Credit card name field visible");
        return billingCardName.isVisible();
    }

    public boolean isCreditCardNumberFieldVisible() {
        LOGGER.info("Is Credit card number field visible");
        return billingCardNumber.isVisible();
    }

    public boolean isCreditCardExpirationDateFieldVisible() {
        LOGGER.info("Is expiration date field visible");
        return billingCardExpDate.isVisible();
    }

    public boolean isCreditCardCvvFieldVisible() {
        LOGGER.info("Is CVV field visible");
        return billingCardCvv.isVisible();
    }

    public boolean isZipCodeFieldVisible() {
        LOGGER.info("Is zip code field visible");
        return billingZipCodeField.isVisible();
    }

    public boolean isZipCodeDropdownItemVisible() {
        LOGGER.info("Is zip code drop down item visible");
        return zipCodeDropdownItem.isVisible(SHORT_TIMEOUT);
    }

    public boolean isBillingZipCodeFieldVerifiedPresent() {
        return billingZipCodeFieldVerified.isElementPresent(SHORT_TIMEOUT);
    }

    public boolean isDobFieldVisible() {
        LOGGER.info("Is DOB field visible");
        return billingDobField.isVisible();
    }

    public boolean isMonthlyToggleChecked() {
        LOGGER.info("Checking monthly toggle button is checked");
        return monthlyToggleCheckBox.isChecked();
    }

    public boolean isAnnualToggleChecked() {
        LOGGER.info("Checking annual toggle button is checked");
        return yearlyToggleCheckBox.isChecked();
    }

    public boolean isBundleOfferCardVisble() {
        LOGGER.info("Checking bundle offer card is visible");
        return bundleOfferCard.isVisible();
    }

    public boolean isBundleUpgradeLinkVisible() {
        LOGGER.info("Checking bundle upgrade link is visible");
        return bundleOfferCardUpgradeLink.isVisible();
    }

    public boolean isBundleDowngradeLinkVisible() {
        LOGGER.info("Checking bundle downgrade link is visible");
        return bundleOfferCardDowngradeLink.isVisible();
    }

    public DisneyPlusBillingPage clickCreditCardRadioBtn() {
        LOGGER.info("Click credit card radio button");
        creditCardRadioButton.click();
        return this;
    }

    public DisneyPlusBillingPage enterCreditCardName(String name) {
        LOGGER.info("Entering credit card name");
        isCreditCardNameFieldVisible();
        billingCardName.type(name);
        return this;
    }

    public DisneyPlusBillingPage enterCreditCardNumber(String number) {
        LOGGER.info("Entering credit card number");
        isCreditCardNumberFieldVisible();
        billingCardNumber.type(number);
        return this;
    }

    public DisneyPlusBillingPage enterCreditCardExpirationDate(String exp) {
        LOGGER.info("Entering credit card expiration date");
        isCreditCardExpirationDateFieldVisible();
        billingCardExpDate.type(exp);
        return this;
    }

    public DisneyPlusBillingPage enterCreditCardCvv(String cvv) {
        LOGGER.info("Entering credit card expiration date");
        isCreditCardCvvFieldVisible();
        billingCardCvv.type(cvv);
        return this;
    }

    public DisneyPlusBillingPage enterBillingZipCode(String zip) {
        LOGGER.info("Entering billing zip code");
        SoftAssert sa = new SoftAssert();

        isZipCodeFieldVisible();
        billingZipCodeField.clickByJs(); // TODO: QAA-10218 - Until WEB-2093 is looked at clicking then changing focus resolves the input validation
        if (!waitUntil(ExpectedConditions.elementToBeClickable(billingZipCodeField.getElement()), EXPLICIT_TIMEOUT)) {
            LOGGER.error("billingZipCodeFieldId is not clickable!");
        }
        billingZipCodeField.sendKeys(Keys.TAB);
        billingZipCodeField.clickByJs();
        billingZipCodeField.type(zip);
        // Wait for Zip code dropdown flash or disappear
        pause(ONE_SEC_TIMEOUT);
        if (isZipCodeDropdownItemVisible()) {
            LOGGER.info("Attempting to select zip code drop down item");
            zipCodeDropdownItem.clickByJs();
        }
        pause(SHORT_TIMEOUT); // Wait for zip code input validation to complete
        sa.assertTrue(isBillingZipCodeFieldVerifiedPresent(), "Zip code input failed to validate");

        return this;
    }

    public DisneyPlusBillingPage enterBillingDob(String dob) {
        billingDobField.type(dob);
        return this;
    }

    public DisneyPlusBillingPage clickAgreeAndSubscribeBtn() {
        LOGGER.info("Click credit card submit button");
        waitFor(agreeAndSubButton);
        agreeAndSubButton.clickByJs();
        return this;
    }

    private void fillCreditCardDetails(String name, String number, String exp, String cvv, String zip) {
        LOGGER.info("Filling credit card info for billing");
        enterCreditCardName(name);
        enterCreditCardNumber(number);
        enterCreditCardExpirationDate(exp);
        enterCreditCardCvv(cvv);
        enterBillingZipCode(zip);
    }

    public void billingCcWithDob(String name, String number, String exp, String cvv, String zip, String dob) {
        LOGGER.info("Billing with credit card for no dob on file users");
        fillCreditCardDetails(name, number, exp, cvv, zip);
        enterBillingDob(dob);
        clickAgreeAndSubscribeBtn();
    }

    public void billingCcWithoutDob(String name, String number, String exp, String cvv, String zip) {
        LOGGER.info("Billing with credit card for users having dob on file");
        fillCreditCardDetails(name, number, exp, cvv, zip);
        clickAgreeAndSubscribeBtn();
    }

    // Paypal payment

    public boolean isPaypalIconVisible() {
        LOGGER.info("Is paypal icon visible");
        return paypalRadioButton.isVisible(SHORT_TIMEOUT);
    }

    public DisneyPlusBillingPage clickPaypalRadioButton() {
        LOGGER.info("Click paypal radio button");
        paypalRadioButton.click();
        return this;
    }

    public DisneyPlusBillingPage clickPaypalSubmitButton() {
        LOGGER.info("Attempting to Click PayPal Submit on Billing");
        pause(HALF_TIMEOUT); // Need time for PayPal to load
        paypalSubmitButton.scrollTo();
        pause(HALF_TIMEOUT); // Need time for PayPal to load
        js = (JavascriptExecutor) getDriver();
        js.executeScript("window.paypal.createOrder()");
        pause(HALF_TIMEOUT); // Need time in between calls
        js.executeScript("window.paypal.onApprove()");
        return this;
    }

    public void billingWithPaypal() {
        clickPaypalRadioButton();
        clickPaypalSubmitButton();
    }

    //Mercado Pago Payment

    public boolean isMercadoPagoIconVisible() {
        LOGGER.info("Is maercado pago icon visible");
        return mercadoRadioIcon.isVisible(SHORT_TIMEOUT);
    }
}
