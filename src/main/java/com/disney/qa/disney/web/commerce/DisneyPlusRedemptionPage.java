package com.disney.qa.disney.web.commerce;

import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.entities.WebConstant;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusRedemptionPage extends DisneyPlusCommercePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String IS_ZIPCODE_COUNTRY = "isZipCodeCountry";
    public static final String IS_CITY_COUNTRY = "isCityCountry";
    public static final String IS_CREDIT_DROPDOWN_COUNTRY = "isCreditDropdownCountry";
    public static final String IS_PROVINCE_DROPDOWN_COUNTRY = "isProvinceDropdownCountry";
    public static final String IS_ADDRESS_COUNTRY = "isAdressCountry";
    public DisneyPlusRedemptionPage(WebDriver driver) { super(driver); }

    // D23 Redemption Landing Page
    @FindBy(css = "#logo")
    private ExtendedWebElement logoD23DisneyPlus;

    @FindBy(xpath = "//p[contains(text(),'BECOME A FOUNDER')]")
    private ExtendedWebElement lblD23CodePageHeader;

    @FindBy(xpath = "//h3[contains(text(),'Enter Your Code')]")
    private ExtendedWebElement lblD23EnterCode;

    @FindBy(id = "redemption")
    private ExtendedWebElement redemptionFieldId;

    @FindBy(xpath = "//button[@value='submit']")
    private ExtendedWebElement submitBtn;

    @FindBy(xpath = "//button[@aria-label='CONTINUE']")
    private ExtendedWebElement continueBtn;

    @FindBy(xpath = "//h3[contains(text(),'Subscriber Agreement')]")
    private ExtendedWebElement subscriberAgreementHeader;

    //Error Validation
    @FindBy(css = ".input-error")
    private ExtendedWebElement inputError;

    // D23 Signup Email
    @FindBy(xpath = "//h3[contains(text(),'Enter your email')]")
    private ExtendedWebElement lblD23Email;

    @FindBy(css = "#email")
    private ExtendedWebElement fieldD23Email;

    @FindBy(xpath = "//span[@class='sc-iELTvK cEGaAd']")
    private ExtendedWebElement chkboxD23Offer;

    @FindBy(xpath = "//button[contains(text(),'Terms of Use')]")
    private ExtendedWebElement btnTermsOfUseEmailPage;

    @FindBy(xpath = "//button[contains(text(),'Privacy Policy')]")
    private ExtendedWebElement btnPrivacyPolicyEmailPage;

    @FindBy(xpath = "//span[contains(text(),'AGREE & CONTINUE')]")
    private ExtendedWebElement btnD23Agree;

    @FindBy(xpath = "//*[@data-testid='price-order-duration-copy']")
    private ExtendedWebElement subscriberHeaderOrderSummary;

    //Error Validation
    @FindBy(css = ".input-error")
    private ExtendedWebElement lblEmailBlank;

    // D23 Signup Password
    @FindBy(xpath = "//h3[contains(text(),'Enter a password')]")
    private ExtendedWebElement lblD23Password;

    @FindBy(css = "#password")
    private ExtendedWebElement fieldD23Password;

    @FindBy(xpath = "//button[contains (text(),'Continue')]")
    private ExtendedWebElement btnD23Continue;

    //D23 Billing Page

    @FindBy(id = "billing-card-name")
    private ExtendedWebElement billingCardNameId;

    @FindBy(xpath = "//span[contains(text(),'Credit Card')]")
    private ExtendedWebElement lblCreditCard;

    @FindBy(xpath = "//span[contains(text(),'PayPal')]")
    private ExtendedWebElement lblPayPal;

    @FindBy(id = "billing-card-number")
    private ExtendedWebElement billingCardNumberId;

    @FindBy(id = "billing-card-exp-date")
    private ExtendedWebElement billingCardExpDateId;

    @FindBy(id = "billing-card-pin")
    private ExtendedWebElement billingCardPin;

    @FindBy(id = "billing-card-CSC")
    private ExtendedWebElement billingCardCscId;

    @FindBy(xpath = "//*[@data-testid='zip-code-input']")
    private ExtendedWebElement billingZipCodeFieldId;

    @FindBy(id = "billing-city")
    private ExtendedWebElement billingCityFieldId;

    @FindBy(id = "savePayment")
    private ExtendedWebElement checkboxSavePaymentId;

    @FindBy(xpath = "//a[contains(text(),'Terms of Use')]")
    private ExtendedWebElement btnTermsOfUseBillingPage;

    @FindBy(id = "credit-submit-button")
    private ExtendedWebElement btnAgreeBillinPageId;

    @FindBy(xpath = "//div[contains(@class,'payment-success-overlay')]")
    private ExtendedWebElement btnPaymentAcceptOverlay;

    @FindBy(id = "CannotApplyPromotion-button")
    private ExtendedWebElement btnCannotApplyPromotion;

    @FindBy(xpath = "//*[@data-testid='zip-code-verified']")
    private ExtendedWebElement billingZipCodeFieldVerified;

    @FindBy(css = ".taxId-container [data-testid=input-error-label]")
    private ExtendedWebElement billingZipCodeTaxIdInputErrorLabel;

    @FindBy(xpath = "//*[@data-testid='credit-debit-dropdown-control']")
    private ExtendedWebElement cardSelectorDropdown;

    @FindBy(xpath = "//*[@data-testid='province-selector-dropdown']")
    private ExtendedWebElement provinceSelectorDropdown;

    @FindBy(xpath = "//*[@data-testid='dropdown-selector-indicator']")
    private ExtendedWebElement dropdownSelectorIndicator;

    @FindBy(id = "billing-address")
    private ExtendedWebElement billingAddressFieldId;

    @FindBy(xpath = "//*[@data-testid='year-selector-dropdown']")
    private ExtendedWebElement yearDropdown;

    @FindBy(xpath = "//*[@data-testid='month-selector-dropdown']")
    private ExtendedWebElement monthDropdown;

    @FindBy(xpath = "//*[@data-testid='day-selector-dropdown']")
    private ExtendedWebElement dayDropdown;

    @FindBy(xpath = "//*[@data-testid='date-dropdown-error-message']")
    private ExtendedWebElement dateDropdownErrorMessage;

    @FindBy(id = "reload-button")
    private ExtendedWebElement mercadoReloadButton;

    //Error Validation

    @FindBy(xpath = "//p[contains(text(),'Enter the name on your Card')]")
    private ExtendedWebElement lblNameOnCardBlank;

    @FindBy(xpath = "//p[contains(text(),'Enter a valid Credit Card Number')]")
    private ExtendedWebElement lblCardNoBlank;

    @FindBy(xpath = "//p[contains(text(),'Enter a valid expiration date')]")
    private ExtendedWebElement lblExpirationBlank;

    @FindBy(xpath = "//p[contains(text(),'Enter Security Code')]")
    private ExtendedWebElement lblSecurityCodeBlank;

    @FindBy(xpath = "//p[contains(text(),'Please enter your Zip Code')]")
    private ExtendedWebElement lblZipBlank;

    @FindBy(xpath = "//*[@role='alert']")
    private ExtendedWebElement lblPurchaseAlert;

    @FindBy(xpath = "//*[@role='alert' and contains(text(),'Please enter a valid postcode')]")
    private ExtendedWebElement lblPurchaseEnterValidZipcodeAlert;

    //D23 Confirmation Page
    @FindBy(xpath = "//h1[@class='sc-hXRMBi cDzxUk']")
    private ExtendedWebElement lblD23Congrats;

    //Not Eligible Page
    @FindBy(xpath = "//*[contains(text(),'You are not eligible for a free trial')]")
    private ExtendedWebElement lblPurchaseNotEligible;

    // *** Getter for Enter Redemption Code ***

    public ExtendedWebElement getDplusLogo() {
        return logoD23DisneyPlus;
    }

    public ExtendedWebElement getRedeemCodelbl() {
        return lblD23EnterCode;
    }

    public ExtendedWebElement getBecomeFounderlbl() {
        return lblD23CodePageHeader;
    }

// *** Getter for Enter Email ***

    public ExtendedWebElement getEnterEmaillbl() {
        return lblD23Email;
    }

// *** Getter for Enter Password ***

    public ExtendedWebElement getEnterPasswordlbl() {
        return lblD23Password;
    }

// *** Getter for Enter Billing Information ***

    public ExtendedWebElement getCreditCardIcon() {
        return lblCreditCard;
    }

    public ExtendedWebElement getPayPalIcon() {
        return lblPayPal;
    }

    public ExtendedWebElement getInputError() {
        waitFor(inputError);
        return inputError;
    }

    public ExtendedWebElement getSubmitAgreeBtn() {
        return btnAgreeBillinPageId;
    }

    public ExtendedWebElement getSubmitBtn() {
        waitFor(submitBtn);
        return submitBtn;
    }

    public ExtendedWebElement getContinueBtn() {
        waitFor(continueBtn);
        return continueBtn;
    }

    public ExtendedWebElement getSubscriberAgreementHeader() {
        waitFor(subscriberAgreementHeader);
        return subscriberAgreementHeader;
    }

    public ExtendedWebElement getSubscriberHeaderOrderSummary() {
        waitFor(subscriberHeaderOrderSummary);
        return subscriberHeaderOrderSummary;
    }

    public ExtendedWebElement getBtnCannotApplyPromotion() {
        waitFor(btnCannotApplyPromotion);
        return btnCannotApplyPromotion;
    }

    public ExtendedWebElement getBillingZipCodeFieldVerified() {
        waitFor(billingZipCodeFieldVerified, DELAY);
        return billingZipCodeFieldVerified;
    }

    public ExtendedWebElement getBillingCardCscId() {
        waitFor(billingCardCscId);
        return billingCardCscId;
    }

    public ExtendedWebElement getBillingCardNameId() {
        waitFor(billingCardNameId);
        return billingCardNameId;
    }

    public ExtendedWebElement getMercadoReloadButton() {
        return mercadoReloadButton;
    }

// *** Boolean Getters

    public boolean isBillingCardNameIdPresent() {
        return getBillingCardNameId().isElementPresent();
    }

    public boolean isSubscriberAgreementHeaderPresent() {
        return getSubscriberAgreementHeader().isElementPresent();
    }

    public boolean isSubscriberHeaderOrderSummaryPresent() {
        return getSubscriberHeaderOrderSummary().isElementPresent();
    }

    public boolean isInputErrorPresent() {
        return getInputError().isElementPresent();
    }

    public boolean isBtnCannotApplyPromotionPresent() {
        return getBtnCannotApplyPromotion().isElementPresent();
    }

    public boolean isBillingZipCodeFieldVerifiedPresent() {
        return getBillingZipCodeFieldVerified().isElementPresent();
    }

    public boolean isBillingZipCodeTaxIdInputErrorLabelPresent() {
        return billingZipCodeTaxIdInputErrorLabel.isElementPresent(SHORT_TIMEOUT);
    }

// *** Getter for Confirmation Page ***

    public ExtendedWebElement getCongratslbl() {
        return lblD23Congrats;
    }

    public ExtendedWebElement getPaymentAcceptOverlay() {
        return btnPaymentAcceptOverlay;
    }

// *** Click Methods

    public void clickSubmitBtn() {
        getSubmitBtn().click();
    }

    public void clickContinueBtn() {
        getContinueBtn().click();
    }

    // *** Expected text for validation messages ***
    private String msgCodeBlank = "Enter your code";
    private String msgEmailBlank = "Please enter a valid email address";
    private String msgPasswordBlank = "Enter a password";
    private String msgNameOnCardBlank = "Enter the name on your Card";
    private String msgCardNoBlank = "Enter a valid Credit Card Number";
    private String msgExpirationBlank = "Enter a valid expiration date";
    private String msgSecurityCodeBlank = "Enter Security Code";
    private String msgZipBlank = "Please enter your Zip Code";

    // Setup QA Env
    public void environmentSetUp(String env) {

        switch (env) {
            case "PROD":
                LOGGER.info("Switching to PROD env and verifying");
                urlSwitch(DisneyWebParameters.DISNEY_QA_WEB_D23URL.getValue(), DisneyWebParameters.DISNEY_PROD_WEB_D23URL.getValue());
                break;
            case "QA":
                LOGGER.info("Switching to QA env and verifying");
                urlSwitch(DisneyWebParameters.DISNEY_PROD_WEB_D23URL.getValue(), DisneyWebParameters.DISNEY_QA_WEB_D23URL.getValue());
                break;
            default:
                Assert.fail("Supposed to be switching to QA or Prod env, but none of the cases match.");
                break;
        }
    }

    // Not Eligible for Free Trial Page - No longer in use (for now)
    public void skipTrialIfPresent() {

        if (lblPurchaseNotEligible.isElementPresent()) {
            LOGGER.info("Payment Method not eligible for free trial...skipping ahead");
            btnAgreeBillinPageId.click();
        } else {
            LOGGER.info("Payment Method Eligible for free trial / no free trial offered");
        }

    }

    // Enter Redemption Code Page

    public void verifyD23RedemptionCodeValidationError() {

        SoftAssert sa = new SoftAssert();
        LOGGER.info("Verifying U.S. D23 Redemption flow Code validation message");
        submitBtn.click();
        verifyError(sa, msgCodeBlank, inputError);
        sa.assertAll();
        LOGGER.info("Verified U.S. D23 Redemption flow Code validation message");
    }

    public void enterRedemptionCode(String redemptionPassword) {
        LOGGER.info("Entering redemption code {}", redemptionPassword);
        redemptionFieldId.click(Long.valueOf(LONG_TIMEOUT) * 2);
        redemptionFieldId.type(redemptionPassword);
        submitBtn.click();
    }

    // Enter Email Address Page

    public void verifyD23EmailValidationError() {

        SoftAssert sa = new SoftAssert();
        LOGGER.info("Verifying U.S. D23 Redemption flow Email validation message");
        submitBtn.click();
        verifyError(sa, msgEmailBlank, lblEmailBlank);
        sa.assertAll();
        LOGGER.info("Verified U.S. D23 Redemption flow Email validation messages");
    }

    public void enterD23EmailAddress(String email) {

        waitForPageToFinishLoading();
        fieldD23Email.type(email);
        chkboxD23Offer.isElementPresent(HALF_TIMEOUT);
        chkboxD23Offer.click();
        waitFor(submitBtn);
        submitBtn.click();
    }

    // Enter Password Page
    public void verifyD23PasswordValidationError() {

        SoftAssert sa = new SoftAssert();
        LOGGER.info("Verifying U.S. D23 Redemption flow Password validation message");
        submitBtn.click();
        verifyError(sa, msgPasswordBlank, lblEmailBlank);
        sa.assertAll();
        LOGGER.info("Verified U.S. D23 Redemption flow Password validation messages");
    }


    public void enterD23Password(String password) {

        waitForPageToFinishLoading();
        fieldD23Password.type(password);
        waitFor(btnD23Continue);
        btnD23Continue.click();
    }

    // Enter Billing Info Page
    public void verifyD23BillingPageValidationErrors() {

        SoftAssert sa = new SoftAssert();
        LOGGER.info("Verifying U.S. D23 Redemption flow validation messages when leaving form blank and submitting...");
        btnAgreeBillinPageId.click();
        verifyError(sa, msgNameOnCardBlank, lblNameOnCardBlank);
        verifyError(sa, msgCardNoBlank, lblCardNoBlank);
        verifyError(sa, msgExpirationBlank, lblExpirationBlank);
        verifyError(sa, msgSecurityCodeBlank, lblSecurityCodeBlank);
        verifyError(sa, msgZipBlank, lblZipBlank);
        sa.assertAll();
        LOGGER.info("Verified All U.S. D23 Redemption flow validation messages");
    }

    /**
     * @param name
     * @param creditCard
     * @param expiration
     * @param cvv
     * @param zipOrTaxId
     * @param locale
     */

    public void enterPurchaseFlowBillingInfo(String name, String creditCard, String expiration, String cvv,
            String zipOrTaxId, String locale) {
        refreshIfCreditRadioIconIdNotPresent(locale);
        fillBillingCardInformation(name, creditCard, expiration, cvv, locale);
        clickCreditCardSelectorDropdown(locale);
        fillBillingCityFieldId(locale);
        fillAddressCountries(locale);
        clickDropDownCountries(locale);
        fillZipCodeField(locale, zipOrTaxId);
        clickCheckBoxSavePaymentId();
        handleDateOfBirthOnBilling(locale);
        handleOptInCheckBoxesOnBilling(locale);
        DisneyPlusBillingPage billingPage = new DisneyPlusBillingPage(getDriver());
        if (billingPage.isDobFieldVisible()) {
            billingPage.enterBillingDob(WebConstant.ADULT_DOB);
        }
    }

    public void refreshIfCreditRadioIconIdNotPresent(String locale) {
        boolean isCreditCardOnlyCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_CREDITCARD_ONLY_COUNTRY);
        if (!isCreditCardOnlyCountry && isCreditRadioLabelPresent()) {
            LOGGER.info("Attempting to click credit card radio icon");
            clickCreditCardRadioIconById();
        }
    }

    public void fillBillingCardInformation(String name, String creditCard, String expiration, String cvv, String locale) {
        boolean isCardPinCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_CARD_PIN_COUNTRY);
        //getBillingCardNameId().clickByJs();
        getBillingCardNameId().type(name);
        billingCardNumberId.type(creditCard);
        billingCardExpDateId.type(expiration);
        if (isCardPinCountry) {
            billingCardPin.type(cvv);
        } else {
            billingCardCscId.type(cvv);
        }
    }

    public void clickCreditCardSelectorDropdown(String locale) {
        boolean isCreditDropdownCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_CREDIT_DROPDOWN_COUNTRY);
        if (isCreditDropdownCountry) {
            waitFor(cardSelectorDropdown);
            cardSelectorDropdown.click();
            getPaymentMethodCreditCard().click();
        }
    }

    public void fillBillingCityFieldId(String locale) {
        boolean isCityCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_CITY_COUNTRY );
        if (isCityCountry) {
            billingCityFieldId.clickByJs();
            billingCityFieldId.type("City Here");
        }
    }

    public void fillAddressCountries(String locale) {
        boolean isAddressCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_ADDRESS_COUNTRY );
        if (isAddressCountry) {
            billingAddressFieldId.click();
            billingAddressFieldId.type("Address Here");
        }
    }

    public void clickDropDownCountries(String locale) {
        boolean isProvinceDropdownCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_PROVINCE_DROPDOWN_COUNTRY);
        if (isProvinceDropdownCountry) {
            pause(SHORT_TIMEOUT); // Pausing here for provinces to load in certain conditions.
            if (!waitUntil(ExpectedConditions.elementToBeClickable(dropdownSelectorIndicator.getElement()), EXPLICIT_TIMEOUT)) {
                LOGGER.error("Dropdown Selector is not clickable!");
            }
            if (dropdownSelectorIndicator.isElementPresent(HALF_TIMEOUT)) {
                waitFor(dropdownSelectorIndicator);
                dropdownSelectorIndicator.click();
                getProviceDropdownMenu().click();
            } else {
                waitFor(provinceSelectorDropdown);
                provinceSelectorDropdown.click();
                getFirstProvinceOptionMobile().click();
            }
        }
    }

    public void fillZipCodeField(String locale, String zipOrTaxId) {
        SoftAssert sa = new SoftAssert();
        boolean isTaxIdCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_TAXID_COUNTRY);
        boolean isZipCodeCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_ZIPCODE_COUNTRY);
        if (isZipCodeCountry) {
            // TODO: https://jira.disneystreaming.com/browse/QAA-10218
            // Until WEB-2093 is looked at clicking then changing focus resolves the input validation
            billingZipCodeFieldId.clickByJs();
            if (!waitUntil(ExpectedConditions.elementToBeClickable(billingZipCodeFieldId.getElement()), EXPLICIT_TIMEOUT)) {
                LOGGER.error("billingZipCodeFieldId is not clickable!");
            }
            billingZipCodeFieldId.sendKeys(Keys.TAB);
            billingZipCodeFieldId.clickByJs();
            billingZipCodeFieldId.type(zipOrTaxId);
            // Wait for zip code dropdown flash or disappear
            pause(ONE_SEC_TIMEOUT);
            if (getZipCodeDropdownItem().isElementPresent(SHORT_TIMEOUT)) {
                LOGGER.info("Attempting to select zip code");
                getZipCodeDropdownItem().clickByJs();
            }

            // Wait for zip code input validation to complete
            pause(SHORT_TIMEOUT);
            sa.assertTrue(isBillingZipCodeFieldVerifiedPresent(), "Zip code input failed to validate");
        } else if (isTaxIdCountry){
            billingTaxIdFieldId.clickByJs();
            billingTaxIdFieldId.type(zipOrTaxId);
            // Send a tab key to perform tax ID input validation
            billingTaxIdFieldId.sendKeys(Keys.TAB);
        }
        sa.assertFalse(isBillingZipCodeTaxIdInputErrorLabelPresent(), "Zip code/tax ID input error message is present");
        sa.assertAll("Zip code/tax ID input");
    }

    public void clickCheckBoxSavePaymentId() {
        if (checkboxSavePaymentId.isElementPresent(ONE_SEC_TIMEOUT)) {
            LOGGER.info("Clicking Save Payment Input");
            checkboxSavePaymentId.click();
        }
    }

    public void handleStoredPaymemt(String cvv) {
        getBillingCardCscId().type(cvv);
        clickCreditSubmitBtnByIdIfPresent();
        clickStoredPaymentSubmitBtnIfPresent();
        clickStoredPaymentReviewBtnIfPresent();
    }

    public String searchAndReturnZipTaxId(String locale) {
        DisneyCountryData countryData = new DisneyCountryData();
        String zip = null;
        boolean isTaxIdCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_TAXID_COUNTRY);
        boolean isZipCodeCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_ZIPCODE_COUNTRY);
        if (isZipCodeCountry) {
            zip = (String) countryData.searchAndReturnCountryData(locale, "code", "zip");
        } else if (isTaxIdCountry) {
            zip = (String) countryData.searchAndReturnCountryData(locale, "code", "taxid");
        }
        return zip;
    }

    public void handleMercadoPago(String locale) {
        clickDropDownCountries(locale);
        fillBillingCityFieldId(locale);
        fillAddressCountries(locale);
        fillZipCodeField(locale, searchAndReturnZipTaxId(locale));
        clickMercadoSubmitBtn();
    }

    public void reloadMercadoPaymentPage() {
        if (IS_DISNEY && mercadoReloadButton.isElementPresent()) {
            mercadoReloadButton.click();
        }
    }

        public String getCardHolderErrorName(String locale) {
        DisneyCountryData countryData = new DisneyCountryData();

        String errorName = (String) countryData.searchAndReturnCountryData(locale, "code", "cardHolderErrorName");
        if (errorName == null) {
            errorName = "REFUSED41";
        }

        return errorName;
    }

    public LocalDate generateRandomDateWithBillingAge(String locale) {
        DisneyCountryData countryData = new DisneyCountryData();
        String billingMinimumAge = (String) countryData.searchAndReturnCountryData(locale, "code", "billingMinimumAge");
        LocalDate date = LocalDate.now().minusYears(Integer.parseInt(billingMinimumAge));
        return date;
    }

    public void handleDateOfBirthOnBilling(String locale) {
        boolean isBillingDOBCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_BILLING_DOB_COUNTRY);

        if (isBillingDOBCountry) {
            enterDateOfBirth(generateRandomDateWithBillingAge(locale));
        }
    }

    public void enterDateOfBirth(LocalDate date) {
        yearDropdown.type(Integer.toString(date.getYear()) + Keys.ENTER);
        monthDropdown.type(Integer.toString(date.getMonthValue()) + Keys.ENTER);
        if (!isDayDisabled())
            dayDropdown.type(Integer.toString(date.getDayOfMonth()) + Keys.ENTER);
    }

    public boolean isDateDropdownErrorMessagePresent() {
        return dateDropdownErrorMessage.isElementPresent();
    }

    public boolean isDayDisabled() {
        return dayDropdown.getAttribute("disabled")!= null;
    }

}

