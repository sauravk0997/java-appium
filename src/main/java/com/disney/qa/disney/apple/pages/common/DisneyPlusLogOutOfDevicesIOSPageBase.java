package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusLogOutOfDevicesIOSPageBase extends DisneyPlusApplePageBase{

    @FindBy(id = "headlineHeader")
    ExtendedWebElement header;

    @FindBy(id = "headlineSubtitle")
    ExtendedWebElement subTitle;

    ExtendedWebElement forgotPasswordLink = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FORGOT_PASSWORD.getText()));

    public DisneyPlusLogOutOfDevicesIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return header.isElementPresent();
    }

    public boolean isSubtitleDisplayed() {
        return subTitle.isElementPresent();
    }

    public boolean isPasswordTextEntryPresent() {
        return secureTextEntryField.isElementPresent();
    }

    public boolean isCancelButtonPresent() {
        return dismissBtn.isElementPresent();
    }

    public void clickCancelButton() {
        dismissBtn.click();
    }

    public boolean isForgotPasswordLinkDisplayed() {
        return forgotPasswordLink.isElementPresent();
    }

    public void clickForgotPasswordLink() {
        forgotPasswordLink.click();
    }

    public void submitPasswordAndLogout(String value) {
        secureTextEntryField.type(value);
        clickPrimaryButton();
    }
}
