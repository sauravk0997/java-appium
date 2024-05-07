package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/*
 * Email and password login pages
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLoginIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExtendedFindBy(accessibilityId = "signUpSwap")
    protected ExtendedWebElement signUpButton;

    @FindBy(xpath = "//XCUIElementTypeButton[@name='buttonBack']/../following-sibling::*/*/XCUIElementTypeImage")
    private ExtendedWebElement dPlusLogo;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeAlert[`label == \"We couldn't find an account for that email\"`]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeScrollView[1]/XCUIElementTypeOther[1]")
    protected ExtendedWebElement noAccountAlert;

    @ExtendedFindBy(accessibilityId = "alertAction:secondaryButton")
    protected ExtendedWebElement alertSignUpBtn;

    @ExtendedFindBy(accessibilityId = "alertAction:defaultButton")
    protected ExtendedWebElement alertTryAgainBtn;
    
    public DisneyPlusLoginIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return headlineHeader.isElementPresent();
    }

    public boolean isBackArrowDisplayed() {
        return getBackArrow().isPresent();
    }

    public boolean isEmailFieldDisplayed() {
        return emailField.isPresent();
    }

    public boolean isSignUpButtonDisplayed() {
        return signUpButton.isPresent();
    }

    public boolean isDisneyLogoDisplayed() {
        return dPlusLogo.isPresent();
    }

    public boolean isLoginTextDisplayed() {
        return headlineHeader.isPresent();
    }

    public boolean isNewToDPlusTextDisplayed() {
        return getTextViewByName("signUpSwap").isElementPresent();
    }

    public String getEmailFieldText() {
        return emailField.getText();
    }

    public void fillOutEmailField(String email) {
        textEntryField.type(email);
    }

    public void clickContinueButton() {
        continueButton.click();
    }

    public void submitEmail(String userEmailAddress) {
        swipeInContainerTillElementIsPresent(null, continueButton, 1, Direction.UP);
        fillOutEmailField(userEmailAddress);
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        clickContinueButton();
        pause(3);
    }

    public String getErrorMessageString() {
        return labelError.getText();
    }

    public boolean isTryAgainAlertButtonDisplayed() {
        return getTryAgainAlertButton().isElementPresent();
    }

    public void clickAlertTryAgainButton() {
        getTryAgainAlertButton().click();
    }

    public boolean isSignUpAlertButtonDisplayed() {
        return getAlertSignUpButton().isElementPresent();
    }

    public void clickAlertSignUpButton() {
        getAlertSignUpButton().click();
    }

    public boolean isNoAccountAlertSubtextDisplayed() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        String text = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.LOGIN_NO_ACCOUNT_SUB_TEXT.getText());
        LOGGER.info("Expecting alert subtext: {}", text);
        return getDynamicAccessibilityId(text).isElementPresent();
    }

    private ExtendedWebElement getAlertSignUpButton() {
        return alertSignUpBtn;
    }

    private ExtendedWebElement getTryAgainAlertButton() {
        return alertTryAgainBtn;
    }
}