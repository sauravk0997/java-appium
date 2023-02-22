package com.disney.qa.disney.web.appex.parentalcontrols;

import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSetProfilePinPage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='managePIN']")
    private ExtendedWebElement profilePin;

    @FindBy(id = "password")
    private ExtendedWebElement password;

    @FindBy(xpath = "//*[@data-testid='password-continue-login']")
    private ExtendedWebElement passwordContinue;

    @FindBy(id = "checkbox")
    private ExtendedWebElement limitaccessCheckbox;

    @FindBy(xpath = "//*[@data-testid='set-pin-button']")
    private ExtendedWebElement setPinButton;

    @FindBy(xpath = "//*[@data-testid='cancel-button']")
    private ExtendedWebElement cancelPinButton;

    @FindBy(xpath = "//*[@data-testid='digit-%s']")
    private ExtendedWebElement profilePinBox;

    @FindBy(xpath = "//*[contains(@class,'input-error')]")
    private ExtendedWebElement inputErrorForPin;

    @FindBy(xpath = "//*[@aria-label='lock']")
    private ExtendedWebElement lock;

    @FindBy(xpath = "//*[@data-testid='password-cancel-login']")
    private ExtendedWebElement passwordCancelBtn;

    public DisneyPlusSetProfilePinPage(WebDriver driver) {
        super(driver);
    }

    public void clickOnProfilePin(){
        profilePin.click();
    }

    public void clickOnContinue(){
        passwordContinue.click();
    }

    public void enterPassword(String pass){
        password.type(pass);
    }

    public void clickOnCheckboxToLimitAccess(){
        limitaccessCheckbox.click();
    }

    public void clickSavePinButton(){
        setPinButton.check();
    }

    public String enterProfilePin(int digitBox, String pinNumber){
        LOGGER.info("Remove 'readonly' attribute for pin digit box: {}", digitBox);
        trigger("document.getElementById('digit-0').removeAttribute('readonly');");
        profilePinBox.format(digitBox).click();
        LOGGER.info("Enter pin number");
        profilePinBox.format(digitBox).type(pinNumber);
        return pinNumber;
    }

    public boolean isEnterPinErrorPresent(){
        return inputErrorForPin.isElementNotPresent(5);
    }

    public boolean isLockForProfilePresent(){
        return lock.isElementPresent();
    }

    public DisneyPlusSetProfilePinPage clickOnCancelPinBtn() {
        LOGGER.info("Click on profile pin cancel button");
        cancelPinButton.click();
        return this;
    }

    public boolean isContinueButtonPresent() {
        LOGGER.info("Verify Continue button is present");
        return passwordContinue.isElementPresent();
    }

    public DisneyPlusSetProfilePinPage clickOnPasswordCancelBtn() {
        LOGGER.info("Click on password cancel button");
        passwordCancelBtn.click();
        return this;
    }

    public boolean isProfilePinCancelButtonPresent() {
        LOGGER.info("Verify if profile pin cancel button present");
        return cancelPinButton.isElementPresent();
    }
}
