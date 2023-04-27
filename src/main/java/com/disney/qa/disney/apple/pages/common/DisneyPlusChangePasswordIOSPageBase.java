package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusChangePasswordIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "checkboxUncheckedNormal")
    private ExtendedWebElement logoutAllDevicesUnchecked;

    @ExtendedFindBy(accessibilityId = "checkboxCheckedNormal")
    private ExtendedWebElement logoutAllDevicesChecked;

    private ExtendedWebElement logoutAllDevicesTitle = xpathNameOrName.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.LOGOUT_ALL_DEVICES_TITLE.getText()),
            DictionaryKeys.LOGOUT_ALL_DEVICES_TITLE.getText());

    private ExtendedWebElement logoutAllDevicesPasswordCopy = xpathNameOrName.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.LOGOUT_ALL_DEVICES_PASSWORD_COPY.getText()),
            DictionaryKeys.LOGOUT_ALL_DEVICES_PASSWORD_COPY.getText());

    private ExtendedWebElement changePasswordCancelBtn = xpathNameOrName.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL,
                            DictionaryKeys.CANCEL_LABEL.getText()),
            DictionaryKeys.CANCEL_LABEL.getText());

    protected ExtendedWebElement newPasswordSaveBtn = xpathNameOrName.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.BTN_SAVE.getText()),
            DictionaryKeys.BTN_SAVE.getText());

    @FindBy(id = "labelErrorMessage")
    private ExtendedWebElement invalidPassword;

    public DisneyPlusChangePasswordIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return headlineHeader.isElementPresent();
    }

    public boolean isBackButtonPresent() {
        return getBackArrow().isElementPresent();
    }

    public boolean isLogoutAllDevicesChecked() {
        return logoutAllDevicesChecked.isElementPresent();
    }

    public boolean isLogoutAllDevicesUnchecked() {
        return logoutAllDevicesUnchecked.isElementPresent();
    }

    public void clickLogoutAllDevices() {
        getDynamicXpathContainsName("checkbox").click();
    }

    public boolean isLogoutAllDevicesTitlePresent() {
        return logoutAllDevicesTitle.isElementPresent();
    }

    public boolean isLogoutAllDevicesPasswordCopyDisplayed() {
        return logoutAllDevicesPasswordCopy.isElementPresent();
    }

    @Override
    public boolean isSaveBtnPresent() {
        return newPasswordSaveBtn.isElementPresent();
    }

    @Override
    public void clickSaveBtn() {
        newPasswordSaveBtn.click();
    }

    @Override
    public boolean isCancelBtnPresent() {
        return changePasswordCancelBtn.isElementPresent();
    }

    @Override
    public void clickCancelBtn() {
        changePasswordCancelBtn.click();
    }

    public boolean isInvalidPasswordErrorDisplayed() {
        return invalidPassword.isElementPresent();
    }

    public String getInvalidPasswordText() {
        return invalidPassword.getText();
    }

    public void enterNewPasswordValue(String value) {
        secureTextEntryField.type(value);
    }

    public void submitNewPasswordValue(String value) {
        enterNewPasswordValue(value);
        clickSaveBtn();
    }

    public boolean isPasswordDescriptionPresent(){
        String expectedString = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.AUTH_MINOR_PASSWORD.getText());
        LOGGER.info("Expected auth headline: {}", expectedString);
        return headlineSubtitle.getText().equalsIgnoreCase(expectedString);
    }
}
