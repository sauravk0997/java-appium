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

import java.lang.invoke.MethodHandles;
import java.util.Map;

/*
 * Email and password login pages
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLoginIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private String getDictionaryItem(DisneyDictionaryApi.ResourceKeys dictionary, DictionaryKeys key) {
        boolean isSupported = getDictionary().getSupportedLangs().contains(getDictionary().getUserLanguage());
        return getDictionary().getDictionaryItem(dictionary, key.getText(), isSupported);
    }

    @ExtendedFindBy(accessibilityId = "signUpSwap")
    protected ExtendedWebElement signUpButton;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeImage")
    private ExtendedWebElement dPlusLogo;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView[$type='XCUIElementTypeTextField'$]/XCUIElementTypeOther/**/XCUIElementTypeImage[2]")
    private ExtendedWebElement myDisneyLogo;

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
        Assert.assertTrue(waitUntil(ExpectedConditions.invisibilityOfElementLocated(continueButton.getBy()), DELAY), "Continue button was present after 10 sec on 'enter email' page");
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

    public boolean isLearnMoreSubTextDisplayed() {
        String learnMoreText = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CHANGE_EMAIL_SUCCESS_BODY.getText()), Map.of("link_1" , "and more"));
        return getDynamicAccessibilityId(learnMoreText).isElementPresent();
    }

    public boolean isMyDisneyLogoDisplayed() {
        return myDisneyLogo.isPresent();
    }
}