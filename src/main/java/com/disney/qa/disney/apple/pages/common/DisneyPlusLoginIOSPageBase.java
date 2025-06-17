package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

/*
 * Email and password login pages
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLoginIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView/XCUIElementTypeOther[1]/XCUIElementTypeImage")
    private ExtendedWebElement dPlusLogo;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeScrollView[$type='XCUIElementTypeTextField'$]/XCUIElementTypeOther/**/XCUIElementTypeImage[2]")
    private ExtendedWebElement myDisneyLogo;

    public DisneyPlusLoginIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return headlineHeader.isElementPresent();
    }

    public boolean isEmailFieldDisplayed() {
        return getTextEntryField().format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HINT.getText())).isPresent();
    }

    public boolean isDisneyLogoDisplayed() {
        return dPlusLogo.isPresent();
    }

    public String getEmailFieldText() {
        return getTextEntryField().format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HINT.getText())).getText();
    }

    public void fillOutEmailField(String email) {
        textEntryField.type(email);
    }

    public void submitEmail(String userEmailAddress) {
        //To hide the keyboard, passing \n at the end of username value
        fillOutEmailField(userEmailAddress + "\n");
        Assert.assertTrue(waitUntil(ExpectedConditions.invisibilityOfElementLocated(continueButton.getBy()), TEN_SEC_TIMEOUT), "Continue button was present after 10 sec on 'enter email' page");
    }

    public boolean isMyDisneyLogoDisplayed() {
        return myDisneyLogo.isPresent();
    }

    public boolean isEnterEmailHeaderDisplayed() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_ENTER_EMAIL_HEADER.getText())).isPresent();
    }

    public boolean isEnterEmailBodyDisplayed() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_ENTER_EMAIL_BODY.getText())).isPresent();
    }

    public boolean isLearnMoreHeaderDisplayed() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_LEARN_MORE_HEADER.getText())).isPresent();
    }

    public ExtendedWebElement getTryAgainButton() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_CONTINUE_BTN.getText()));
    }
}
