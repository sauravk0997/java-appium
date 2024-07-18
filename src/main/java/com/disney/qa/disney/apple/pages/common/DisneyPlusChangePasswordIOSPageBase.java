package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusChangePasswordIOSPageBase extends DisneyPlusPasswordIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExtendedFindBy(accessibilityId = "checkboxUncheckedNormal")
    private ExtendedWebElement logoutAllDevicesUnchecked;

    @ExtendedFindBy(accessibilityId = "checkboxCheckedNormal")
    private ExtendedWebElement logoutAllDevicesChecked;

    @FindBy(id = "labelErrorMessage")
    private ExtendedWebElement invalidPassword;

    private ExtendedWebElement logoutAllDevicesTitle = staticTextByLabel.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.LOGOUT_ALL_DEVICES_TITLE.getText()),
            DictionaryKeys.LOGOUT_ALL_DEVICES_TITLE.getText());

    private ExtendedWebElement logoutAllDevicesPasswordCopy = staticTextByLabel.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.LOGOUT_ALL_DEVICES_PASSWORD_COPY.getText()),
            DictionaryKeys.LOGOUT_ALL_DEVICES_PASSWORD_COPY.getText());

    private ExtendedWebElement changePasswordCancelBtn = staticTextByLabel.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL,
                            DictionaryKeys.CANCEL_LABEL.getText()),
            DictionaryKeys.CANCEL_LABEL.getText());

    protected ExtendedWebElement newPasswordSaveBtn = staticTextByLabel.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.BTN_SAVE.getText()),
            DictionaryKeys.BTN_SAVE.getText());

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
        return logoutAllDevicesChecked.isPresent();
    }

    public boolean isLogoutAllDevicesUnchecked() {
        return logoutAllDevicesUnchecked.isPresent();
    }

    public void clickLogoutAllDevices() {
        logoutAllDevicesUnchecked.click();
    }

    public boolean isLogoutAllDevicesTitlePresent() {
        return logoutAllDevicesTitle.isPresent();
    }

    public boolean isLogoutAllDevicesPasswordCopyDisplayed() {
        return logoutAllDevicesPasswordCopy.isPresent();
    }

    @Override
    public boolean isSaveBtnPresent() {
        return newPasswordSaveBtn.isPresent();
    }

    @Override
    public void clickSaveBtn() {
        primaryButton.click();
    }

    @Override
    public boolean isCancelBtnPresent() {
        return changePasswordCancelBtn.isPresent();
    }

    //Need to remove below method once we replace it from all steps with clickCancelButton method
    @Override
    public void clickCancelBtn() {
        changePasswordCancelBtn.click();
    }

    public void clickCancelButton() {
        String cancelButton = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.CANCEL_LABEL.getText());
        dynamicBtnFindByLabel.format(cancelButton).click();
    }

    public boolean isInvalidPasswordErrorDisplayed() {
        return invalidPassword.isPresent();
    }

    public void submitNewPasswordValue(String value) {
        enterLogInPassword(value);
        clickSaveBtn();
    }

    public boolean isPasswordDescriptionPresent() {
        String expectedString = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.AUTH_MINOR_PASSWORD.getText());
        LOGGER.info("Expected auth headline: {}", expectedString);
        waitForPresenceOfAnElement(headlineSubtitle);
        return headlineSubtitle.getText().equalsIgnoreCase(expectedString);
    }
}
