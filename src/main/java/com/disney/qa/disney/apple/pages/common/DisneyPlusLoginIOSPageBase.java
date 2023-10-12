package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/*
 * Email and password login pages
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLoginIOSPageBase extends DisneyPlusApplePageBase {

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

    public void submitEmail(String userEmailAddress) {
        fillOutEmailField(userEmailAddress);
        UniversalUtils.captureAndUpload(getCastedDriver());
        clickPrimaryButtonByCoordinates();
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
        UniversalUtils.captureAndUpload(getCastedDriver());
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