package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import java.lang.invoke.MethodHandles;

/*
 * Email and password login pages
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLoginIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public enum emailFormats {
        ONECHAR,
        NOTOPLEVELDOMAIN,
        NOEMAIL,
        INVALIDEMAIL
    }

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
        return getTextEntryField().format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HINT.getText())).isPresent();
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
        return getTextEntryField().format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HINT.getText())).getText();
    }

    public void fillOutEmailField(String email) {
        textEntryField.type(email);
    }

    public void submitEmail(String userEmailAddress) {
        //To hide the keyboard, passing \n at the end of username value
        fillOutEmailField(userEmailAddress + "\n");
        Assert.assertTrue(waitUntil(ExpectedConditions.invisibilityOfElementLocated(continueButton.getBy()), DELAY));
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

    public String getEmailFormat(DisneyPlusLoginIOSPageBase.emailFormats emailFormats) {
        switch (emailFormats) {
            case oneChar:
                return "a";
            case noTopLevelDomain:
                return "emailWithoutTLD@gmail";
            case noEmail:
                return "";
            case invalidEmail:
                return "notAnEmail";
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' the email format is not valid one", emailFormats));
        }
    }
}