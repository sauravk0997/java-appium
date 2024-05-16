package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusChangeEmailIOSPageBase extends DisneyPlusApplePageBase{

    private ExtendedWebElement title = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CHANGE_EMAIL_TITLE.getText()));

    private ExtendedWebElement newEmailHeader = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EMAIL_NEW_HEADER.getText()));

    @ExtendedFindBy(accessibilityId = "disneyAuthCheckboxUnchecked")
    private ExtendedWebElement logoutAllDevicesUnchecked;

    @ExtendedFindBy(accessibilityId = "disneyAuthCheckboxChecked")
    private ExtendedWebElement logoutAllDevicesChecked;

    @ExtendedFindBy(accessibilityId = "Cancel")
    private ExtendedWebElement cancelButton;

    private ExtendedWebElement logoutAllDevicesTitle = staticTextByLabel.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.LOGOUT_ALL_DEVICES_TITLE.getText()),
            DictionaryKeys.LOGOUT_ALL_DEVICES_TITLE.getText());

    private ExtendedWebElement logoutAllDevicesEmailCopy = staticTextByLabel.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.LOGOUT_ALL_DEVICES_EMAIL_COPY.getText()),
            DictionaryKeys.LOGOUT_ALL_DEVICES_EMAIL_COPY.getText());

    @FindBy(id = "labelErrorMessage")
    private ExtendedWebElement invalidEmail;

    public DisneyPlusChangeEmailIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getDynamicAccessibilityId("Choose your new MyDisney email").isElementPresent();
    }

    public boolean isCurrentEmailShown(String email) {
        return getStaticTextByLabelContains(email).isElementPresent();
    }

    public boolean isNewEmailHeaderPresent() {
        return getStaticTextByLabelContains("Choose an email").isElementPresent();
    }

    public void enterNewEmailAddress(String value) {
        textEntryField.type(value);
    }

    public void submitNewEmailAddress(String value) {
        enterNewEmailAddress(value + "\n");
        getSaveAndContinueBtn().click();
    }

    public boolean isLogoutAllDevicesChecked() {
        return logoutAllDevicesChecked.isElementPresent();
    }

    public boolean isLogoutAllDevicesUnchecked() {
        return logoutAllDevicesUnchecked.isElementPresent();
    }

    public void clickLogoutAllDevices() {
        logoutAllDevicesUnchecked.click();
    }

    public boolean isLogoutAllDevicesTitlePresent() {
        return logoutAllDevicesTitle.isElementPresent();
    }

    public boolean isLogoutAllDevicesEmailCopyDisplayed() {
        return logoutAllDevicesEmailCopy.isElementPresent();
    }

    public boolean isInvalidEmailErrorDisplayed() {
        return getStaticTextByLabelContains("Sorry, we are having trouble creating your account").isPresent();
    }

    public String getInvalidEmailText() {
        return invalidEmail.getText();
    }

    @Override
    public void clickCancelBtn() {
        cancelButton.click();
    }
}
