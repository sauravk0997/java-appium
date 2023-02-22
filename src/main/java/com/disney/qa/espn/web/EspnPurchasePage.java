package com.disney.qa.espn.web;


import com.disney.qa.common.web.SeleniumUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class EspnPurchasePage extends AbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int LONG_TIMEOUT = 60;
    private static final String DELETE_COOKIES_MESSAGE = "Delete Cookies";
    //Base page
    @FindBy(css = "div[class=\"guardrail\"]>div>a")
    private ExtendedWebElement purchaseButton;
    //sign up iframe
    @FindBy(css = "html[class='oneid-lightbox-open']")
    private ExtendedWebElement lightboxOpen;
    @FindBy(css = "div[id='disneyid-wrapper']>iframe")
    private ExtendedWebElement iframePaywall;
    //login page
    @FindBy(css = "input[type='email'][ng-model='vm.username']")
    private ExtendedWebElement emailField;
    @FindBy(css = "input[type='password'][ng-model='vm.password']")
    private ExtendedWebElement passwordField;
    @FindBy(css = "button[class='btn btn-primary btn-submit ng-isolate-scope'][ng-click='vm.submitLogin()']")
    private ExtendedWebElement loginButton;
    @FindBy(css = "a[class='btn btn-facebook ng-scope ng-isolate-scope'][ng-click='vm.facebook.login()']")
    private ExtendedWebElement facebookLoginButton;
    @FindBy(css = "a[class='btn btn-secondary ng-isolate-scope'][ng-click='vm.createAccount()']")
    private ExtendedWebElement signUpButton;
    @FindBy(css = "div[class='legal-msg-footer login']")
    private ExtendedWebElement loginFooter;
    //Update account page
    @FindBy(css = "input[name='firstName']")
    private ExtendedWebElement firstNameField;
    @FindBy(css = "input[name='lastName']")
    private ExtendedWebElement lastNameField;
    @FindBy(css = "button[type='submit']")
    private ExtendedWebElement updateAccountButton;
    //create account page
    @FindBy(css = "a[href='https://disneytermsofuse.com/english/']")
    private ExtendedWebElement termsOfUseLink;
    @FindBy(css = "a[href='https://privacy.thewaltdisneycompany.com/en/current-privacy-policy/']")
    private ExtendedWebElement privacyPolicyLink;
    @FindBy(css = "a[class='btn btn-email ng-isolate-scope'][ng-click='vm.emailSignUp()']")
    private ExtendedWebElement signUpWithEmailButton;
    @FindBy(css = "a[class='btn btn-facebook ng-scope ng-isolate-scope'][ng-click='vm.facebookSignUp()']")
    private ExtendedWebElement signUpWithFacebookButton;
    //create account with email
    @FindBy(css = "input[name='firstName'][ng-model='vm.model.firstName']")
    private ExtendedWebElement createAccountEamilFirstName;
    @FindBy(css = "input[name='lastName']")
    private ExtendedWebElement createAccountEmailLastName;
    @FindBy(css = "input[name='email'][ng-model='model.profile.email']")
    private ExtendedWebElement createAccountEmailAddress;
    @FindBy(css = "input[name='newPassword'][ng-model='vm.newPassword']")
    private ExtendedWebElement createAccountEmailPassword;
    @FindBy(css = "input[type='checkbox'][ng-change='vm.showPasswordClick()']")
    private ExtendedWebElement showPasswordCheckbox;
    @FindBy(css = "button[class='btn btn-primary ng-scope ng-isolate-scope']")
    private ExtendedWebElement signUpEmailButton;
    //espn+ base tier 7 day free trial purchase flow
    @FindBy(css = "input[id='monthlyPlanRadio']")
    private ExtendedWebElement monthlyRadioButton;
    @FindBy(css = "input[id='yearlyPlanRadio']")
    private ExtendedWebElement yearlyRadioButton;
    @FindBy(css = "li[class='credit-card-billing-toggle form-check form-check-inline']")
    private ExtendedWebElement creditCardRadioButton;
    @FindBy(css = "input[name='cardNumber']")
    private ExtendedWebElement credtCardNumberField;
    @FindBy(css = "input[id='cardOwner']")
    private ExtendedWebElement nameOnCardField;
    @FindBy(css = "select[id='cardExpMonth']")
    private ExtendedWebElement expirationMonth;
    @FindBy(css = "select[id='cardExpYear']")
    private ExtendedWebElement expirationYear;
    @FindBy(css = "input[name='cardCVV']")
    private ExtendedWebElement cardCVV;
    @FindBy(css = "input[id='zipCode']")
    private ExtendedWebElement zipCode;
    @FindBy(css = "ul[class='state-city-dropdown']>li")
    private List<ExtendedWebElement> stateCityDropdown;
    @FindBy(css = "//li[contains(text(),'SCHENECTADY, NY, US ')]")
    private ExtendedWebElement stateCityDropdownValue;
    @FindBy(css = "input[id='chkBox']")
    private ExtendedWebElement agreeCheckBox;
    @FindBy(css = "button[id='btnPurchase']")
    private ExtendedWebElement finalPurchaseButton;
    @FindBy(css = "span[class='badge badge-curved badge-secondary text-right product-price']")
    private ExtendedWebElement freeTrialPrice;
    @FindBy(css = "div[class='form-group relative']")
    private ExtendedWebElement legalFooter;
    @FindBy(css = "p[class='legal-msg']")
    private List<ExtendedWebElement> purchasePrice;
    @FindBy(css = "div[class=\"custom-billing-message link-account-info amount-info SFUIText-b\"] > span:nth-child(2)")
    private ExtendedWebElement tierPrice;
    @FindBy(css = "h1[class='card-title SFDisplay-b main-heading']")
    private ExtendedWebElement purchaseFlowHeader;
    @FindBy(css = "div[class='card-body success-msg-body']")
    private ExtendedWebElement purchaseSuccessHeader;
    @FindBy(css = "div[class='language-toggle-btn']")
    private ExtendedWebElement languageToggle;
    //PayPal Flow
    @FindBy(css = "li[class='paypal-billing-toggle form-check form-check-inline']")
    private ExtendedWebElement paypalRadioButton;
    @FindBy(css = "button[class='btn col-md-12 paypal-btn']")
    private ExtendedWebElement paypalPurchaseButton;
    @FindBy(css = "input[id='email']")
    private ExtendedWebElement paypalEmail;
    @FindBy(css = "button[id='btnNext']")
    private ExtendedWebElement paypalNextButton;
    @FindBy(css = "input[id='password']")
    private ExtendedWebElement paypalPassword;
    @FindBy(css = "button[id='btnLogin']")
    private ExtendedWebElement paypalLoginButton;
    @FindBy(css = "input[id='confirmButtonTop']")
    private ExtendedWebElement paypalAgreePayButton;
    //MLB Bundle
    @FindBy(css = "span[class='fine-print']")
    private ExtendedWebElement mlbMlpBundlePrice;
    @FindBy(css = "span[class='badge badge-curved badge-secondary text-right product-price']")
    private List<ExtendedWebElement> mlbBundlePrice;
    //MLB Add-on
    @FindBy(css = "span[class='badge badge-curved badge-secondary text-right product-price']")
    private ExtendedWebElement mlbAddOnPrice;
    @FindBy(css = "span[class='card-logo']")
    private ExtendedWebElement mlbCardLogo;
    //UFC
    @FindBy(css = "div[class='card card-block custom-width mx-auto espn-billing-container']")
    private List<ExtendedWebElement> ufcPpvPurchaseContainer;
    @FindBy(css = "a[class='cta-button non-decorated-link btn-sm btn-solid btn-gray']")
    private ExtendedWebElement ufcPurchaseButton;
    @FindBy(css = "span[class='badge badge-curved badge-secondary text-right product-price']")
    private ExtendedWebElement ufcPurchasePrice;
    @FindBy(css = "div[class='custom-billing-message link-account-info customer-messaging']")
    private ExtendedWebElement ufcBundlePrice;
    @FindBy(css = "span[class='add-plan-link']")
    private ExtendedWebElement addPlanLink;
    @FindBy(css = "label[for='monthlyPlanRadio']")
    private ExtendedWebElement ufcMonthlyToggle;
    @FindBy(css = "label[for='yearlyPlanRadio']")
    private ExtendedWebElement ufcYearlyToggle;
    @FindBy(css = "label[class='form-check-label']")
    private ExtendedWebElement agreementCheck;
    @FindBy(css = "button[class='btn espn-btn col-11']")
    private ExtendedWebElement addOnPurchaseButton;
    @FindBy(css = "h1[class='card-title text-center SFDisplay-b main-heading'")
    private ExtendedWebElement welcomeBackHeading;
    @FindBy(css = "a[class='btn col-md-6 espn-btn']")
    private ExtendedWebElement getStartedButton;
    //Sub-Management
    @FindBy(css = "a[class='ng-isolate-scope']")
    private ExtendedWebElement signInButton;
    @FindBy(css = "div[class='custom-width mx-auto link-to-mobile-device']")
    private ExtendedWebElement mobileDisclaimer;
    @FindBy(css = "div[style='background-image: url(\"https://secure.web.plus.espn.com/assets/images/espn-logo.png\");']")
    private ExtendedWebElement espnLogo;
    @FindBy(css = "div[style='background-image: url(\"https://static.web.plus.espn.com/espn/data/commerce/images/mlbtv_logo.png\");']")
    private ExtendedWebElement mlbLogo;
    //TODO: This element needs to be dynamically handled. Ideally, we'd want to fetch the image url from a config so it's automatically updated to the latest PPV.
    @FindBy(css = "div[style='background-image: url(\"https://static.web.plus.espn.com/espn/data/commerce/images/ufc-272-bw-pos-h@3x.png\");']")
    private ExtendedWebElement ufcLogo;
    @FindBy(css = "div[style='background-image: url(\"https://static.web.plus.espn.com/espn/data/commerce/images/bundle-full-color-01@3x.png\");']")
    private ExtendedWebElement superbundleLogo;
    @FindBy(css = "a[class='align-self-center subscription-link text-center']>span")
    private ExtendedWebElement manageButton;
    @FindBy(css = "button[class='btn col-md-12 espn-btn']>span")
    private ExtendedWebElement updatePaymentInfo;
    @FindBy(css = "div[class='button-container']>span")
    private ExtendedWebElement changeButton;
    @FindBy(css = "button[class='btn col-md-12 espn-btn']>span")
    private ExtendedWebElement saveButton;
    @FindBy(css = "div[ng-reflect-ng-class='success']")
    private ExtendedWebElement updatePaymentMsg;
    //Upgrade text element
    @FindBy(css = "div[class='custom-billing-message link-account-info customer-messaging']")
    private ExtendedWebElement monthlyToAnnualUpgradeMsg;
    //add on PPV on success page
    @FindBy(css = "span[class='cursor-p add-plan']")
    private ExtendedWebElement addonPPV;
    @FindBy(css = "span[class='plus-icon']")
    private ExtendedWebElement addPlusicon;
    @FindBy(css = "input[id='chkBox']")
    private ExtendedWebElement chkBox;
    @FindBy(css = "button[class='btn espn-btn col-11']")
    private ExtendedWebElement buyPPVaddon;

    public EspnPurchasePage(WebDriver driver) {
        super(driver);

    }

    public By getSaveButton() {
        return saveButton.getBy();
    }

    public By getAgreeCheckBoxBy() {
        return agreeCheckBox.getBy();
    }

    //Purchase page getters
    public ExtendedWebElement getStateCityDropdown(int cityDropDownIndex) {
        return stateCityDropdown.get(cityDropDownIndex);
    }

    public By getStateDropDownValue() {
        return stateCityDropdownValue.getBy();
    }

    //Select to buy add on
    public ExtendedWebElement getbutton() {
        return buyPPVaddon;
    }

    public ExtendedWebElement getchkBox() {
        return chkBox;
    }

    public ExtendedWebElement getAddPlusicon() {
        return addPlusicon;
    }

    public ExtendedWebElement getFreeTrialPrice() {
        return freeTrialPrice;
    }

    public ExtendedWebElement getPurchasePrice(int purchasePriceIndex) {
        return purchasePrice.get(purchasePriceIndex);
    }

    public ExtendedWebElement getTierPrice() {
        return tierPrice;
    }

    public ExtendedWebElement getMlbMlpBundlePrice() {
        return mlbMlpBundlePrice;
    }

    public ExtendedWebElement getMlbBundlePrice(int j) {
        return mlbBundlePrice.get(j);
    }

    public ExtendedWebElement getMlbAddOnPrice() {
        return mlbAddOnPrice;
    }

    public ExtendedWebElement getUFCPurchasePrice() {
        return ufcPurchasePrice;

    }

    public ExtendedWebElement getUFCBundlePrice() {
        return ufcBundlePrice;

    }

    public ExtendedWebElement getEspnLogo() {
        return espnLogo;

    }

    public ExtendedWebElement getMlbLogo() {
        return mlbLogo;

    }

    public ExtendedWebElement getSuperbundleLogo() {
        return superbundleLogo;

    }

    public ExtendedWebElement getUfcLogo() {
        return ufcLogo;

    }

    public ExtendedWebElement getPaywallBody(int index) {
        return ufcPpvPurchaseContainer.get(index);

    }

    public ExtendedWebElement getMonthlyToAnnualUpgradeMsg() {
        return monthlyToAnnualUpgradeMsg;

    }

    public ExtendedWebElement getMlbCardLogo() {
        return mlbCardLogo;

    }

    public ExtendedWebElement getChangeButton() {
        return changeButton;
    }

    public ExtendedWebElement getUpdatePaymentMsg() {
        return updatePaymentMsg;
    }

    // Post Sign In UFC PPV
    public ExtendedWebElement getWelcomeBackHeading() {
        return welcomeBackHeading;

    }

    public ExtendedWebElement getGetStartedButton() {
        return getStartedButton;

    }

    //Methods
    public By getManageButton() {
        return manageButton.getBy();
    }

    //Delete Cookies, Open & Go to URL
    public void pageSetUp(String link) {
        LOGGER.info(DELETE_COOKIES_MESSAGE);
        deleteCookies();
        setPageAbsoluteURL(link);
        open();

    }

    //Delete Cookies
    public void deleteCookies() {
        getDriver().manage().deleteAllCookies();

    }

    //Create unique email
    public String uniqueEmail(String email) {
        String emailAddressBase = email.substring(0, email.indexOf('@'));
        return emailAddressBase + "+new+" + retrieveCurrentTime() + "@gmail.com";
    }

    //Create unique yopmail
    public String uniqueYopmail(String email) {
        return email.substring(0, email.indexOf('@')) + retrieveCurrentTime() + "@yopmail.com";
    }

    //Get current URL
    public String getCurrentUrl() {
        return getDriver().getCurrentUrl();

    }

    //Click MLP CTA
    public void clickCTA() {
        purchaseButton.click();
        pause(10);
        environmentSetup("QA");
        pause(10);
    }

    //Click Buy Now
    public void buyNow() {
        purchaseButton.click();
    }

    //TODO:Remove waitFor,add it in a separate method and delete method - use signInWithUfcPpvEntitlements instead
    //Sign In
    public void signIn(String baseUser, String basePassword) {
        pause(5);
        getDriver().switchTo().frame(iframePaywall.getElement());
        emailField.type(baseUser);
        passwordField.type(basePassword);
        loginButton.click();
        getDriver().switchTo().defaultContent();
        pause(5);

    }

    //TODO: rename to signIn once TODO on current signIn is completed
    //Sign in without waiting for purchase button
    public void signInWithUfcPpvEntitlements(String baseUser, String basePassword) {
        waitFor(iframePaywall);
        getDriver().switchTo().frame(iframePaywall.getElement());
        emailField.type(baseUser);
        passwordField.type(basePassword);
        loginButton.click();
        getDriver().switchTo().defaultContent();

    }

    //Update my account
    public void updateAccount(String firstName, String lastName) {
        waitFor(iframePaywall);
        getDriver().switchTo().frame(iframePaywall.getElement());
        firstNameField.type(firstName);
        lastNameField.type(lastName);
        updateAccountButton.click();
        pause(5);

    }

    //Create new account
    public void createNewAccount(String firstName, String lastName, String baseEmailAddress, String password) {
        pause(5);
        getDriver().switchTo().frame(iframePaywall.getElement());
        signUpButton.click();
        createAccountEamilFirstName.type(firstName);
        createAccountEmailLastName.type(lastName);
        createAccountEmailAddress.type(baseEmailAddress);
        createAccountEmailPassword.type(password);
        pause(5);
        signUpEmailButton.click();
        pause(5);
        getDriver().switchTo().defaultContent();
        waitFor(finalPurchaseButton);

    }

    //Fill out base tier monthly billing
    public void purchaseFlow(String creditCard, String firstName, String lastName, String zip, String cvv) {
        pause(10);
        credtCardNumberField.type(creditCard);
        nameOnCardField.type(firstName + lastName);
        expirationMonth.select(3);
        expirationYear.select(9);
        cardCVV.type(cvv);
        zipCode.type(zip);
        pause(5);
        getStateCityDropdown(1).click();
        pause(5);
        agreeCheckBox.click();
        pause(5);
        finalPurchaseButton.click();
        waitFor(purchaseSuccessHeader);

    }

    public void paypalPurchaseFlow(String paypalUser, String paypalPass, String zip) {
        pause(10);
        paypalRadioButton.click();
        zipCode.type(zip);
        isCityListPresent();
        getStateCityDropdown(1).click();
        agreeCheckBox.click();
        pause(5);
        paypalPurchaseButton.click();
        waitFor(paypalNextButton);
        paypalEmail.type(paypalUser);
        paypalNextButton.click();
        paypalPassword.type(paypalPass);
        paypalLoginButton.click();
        waitFor(paypalAgreePayButton);
        paypalAgreePayButton.click();
        waitFor(purchaseSuccessHeader);

    }

    //Stored payment for back-to-back purchases
    public void storedPayment() {
        waitFor(agreeCheckBox);
        agreeCheckBox.click();
        finalPurchaseButton.click();
        waitFor(purchaseSuccessHeader);
    }

    //to use different payment method on purchase page
    public void changeButton(String creditCard, String firstName, String lastName, String zip, String cvv) {
        waitFor(getChangeButton());
        getChangeButton().click();
        credtCardNumberField.type(creditCard);
        nameOnCardField.type(firstName + lastName);
        expirationMonth.select(3);
        expirationYear.select(9);
        cardCVV.type(cvv);
        zipCode.type(zip);
        waitUntil(ExpectedConditions.elementToBeClickable(getStateDropDownValue()), 15);
        getStateCityDropdown(1).click();
        waitFor(agreeCheckBox);
        agreeCheckBox.click();
        waitFor(finalPurchaseButton);
        finalPurchaseButton.click();
        waitFor(purchaseSuccessHeader);
    }

    //Switch from monthly to annual purchase flow
    public void switchToAnnualPurchaseFlow() {
        yearlyRadioButton.click();
        pause(10);

    }

    //Waiting for State City dropdown
    public boolean isCityListPresent() {
        return !stateCityDropdown.isEmpty();

    }

    //Create new UFC Direct account
    public void createUFCDirectNewAccount(String firstName, String lastName, String baseEmailAddress, String password) {
        environmentSetup("QA");
        pause(10);
        waitFor(lightboxOpen);
        getDriver().switchTo().frame(iframePaywall.getElement());
        signUpButton.click();
        createAccountEamilFirstName.type(firstName);
        createAccountEmailLastName.type(lastName);
        createAccountEmailAddress.type(baseEmailAddress);
        createAccountEmailPassword.type(password);
        pause(5);
        signUpEmailButton.click();
        pause(5);
        getDriver().switchTo().defaultContent();
        waitFor(finalPurchaseButton);

    }

    //Switch to yearly E+ add-on
    public void ufcAddOnToggle() {
        ufcMonthlyToggle.click();
        pause(5);

    }

    //UFC Direct + E+ Add-on
    public void addOnESPNPlus() {
        addPlanLink.click();
        agreementCheck.click();
        addOnPurchaseButton.click();
        pause(5);

    }

    //Checking Sub-Management
    public void verifySubManagement(String baseEmailAddress, String password) {
        pause(10);
        getDriver().switchTo().frame(iframePaywall.getElement());
        signInButton.click();
        emailField.type(baseEmailAddress);
        passwordField.type(password);
        loginButton.click();
        getDriver().switchTo().defaultContent();
        pause(10);

    }

    public void verifySubPage() {
        pause(10);
        getDriver().switchTo().defaultContent();
        waitForPageToFinishLoading();
    }

    //Updating Payment method
    public void updatePayment(String creditCard, String firstName, String lastName, String zip, String cvv) {
        waitUntil(ExpectedConditions.elementToBeClickable(getManageButton()), 15);
        manageButton.click();
        updatePaymentInfo.click();
        changeButton.click();
        credtCardNumberField.type(creditCard);
        nameOnCardField.type(firstName + lastName);
        expirationMonth.select(3);
        expirationYear.select(9);
        cardCVV.type(cvv);
        zipCode.type(zip);
        waitUntil(ExpectedConditions.elementToBeClickable(getStateDropDownValue()), 1);
        getStateCityDropdown(1).click();
        waitUntil(ExpectedConditions.elementToBeClickable(getSaveButton()), 10);
        saveButton.click();
    }

    //Setup QA Env
    public void environmentSetup(String env) {

        switch (env) {
            case "PROD":
                LOGGER.info("Switching to PROD env and verifying");
                urlSwitch(EspnWebParameters.ESPN_WEB_QA_LANDING_URL.getValue(), EspnWebParameters.ESPN_WEB_PROD_LANDING_URL.getValue());
                break;
            case "QA":
                LOGGER.info("Switching to QA env and verifying");
                urlSwitch(EspnWebParameters.ESPN_WEB_PROD_SECURE_URL.getValue(), EspnWebParameters.ESPN_WEB_QA_SECURE_URL.getValue());
                break;
            default:
                Assert.fail("Supposed to be switching to QA or Prod env, but none of the cases match.");
                break;
        }

    }

    //Switch from PROD to QA
    public void urlSwitch(String departure, String destination) {

        String url = getDriver().getCurrentUrl();
        LOGGER.info("Switching from " + departure + " to " + destination);
        url = url.replace(departure, destination);
        getDriver().get(url);

    }

    public String retrieveCurrentTime() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("MMddHHmmss").format(cal.getTime());

    }

    public void waitForPageToFinishLoading() {
        SeleniumUtils util = new SeleniumUtils(getDriver());
        util.waitUntilDOMready();

    }

    public void waitFor(ExtendedWebElement ele) {
        waitUntil(ExpectedConditions.presenceOfElementLocated(ele.getBy()), (long) LONG_TIMEOUT * 2);

    }

    public String getValueOfCookieNamed(String name) {
        return driver.manage().getCookieNamed(name).getValue();

    }

    //Add on PPV from success page
    public void addOnPPV() {
        addPlusicon.click();
        agreeCheckBox.click();
        buyPPVaddon.click();
        waitFor(purchaseSuccessHeader);
    }
}