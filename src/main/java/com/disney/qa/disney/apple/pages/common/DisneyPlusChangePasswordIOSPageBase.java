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

    private final ExtendedWebElement logoutAllDevicesTitle = staticTextByLabel.format(getLocalizationUtils()
            .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                    DictionaryKeys.MY_DISNEY_LOGOUT_ALL_CHECKBOX.getText()));

    private final ExtendedWebElement logoutAllDevicesPasswordCopy = staticTextByLabel.format(getLocalizationUtils()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.LOGOUT_ALL_DEVICES_PASSWORD_COPY.getText()),
            DictionaryKeys.LOGOUT_ALL_DEVICES_PASSWORD_COPY.getText());

    private final ExtendedWebElement changePasswordCancelBtn = staticTextByLabel.format(getLocalizationUtils()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL,
                            DictionaryKeys.CANCEL_LABEL.getText()),
            DictionaryKeys.CANCEL_LABEL.getText());

    protected ExtendedWebElement newPasswordSaveBtn = staticTextByLabel.format(getLocalizationUtils()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            DictionaryKeys.BTN_SAVE.getText()),
            DictionaryKeys.BTN_SAVE.getText());

    private final ExtendedWebElement saveAndContinueBtn = staticTextByLabel.format(getLocalizationUtils()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                            DictionaryKeys.MY_DISNEY_SAVE_CONTINUE_BTN.getText()),
            DictionaryKeys.MY_DISNEY_SAVE_CONTINUE_BTN.getText());

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
        String cancelButton = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.CANCEL_LABEL.getText());
        dynamicBtnFindByLabel.format(cancelButton).click();
    }

    public boolean isInvalidPasswordErrorDisplayed() {
        return labelError.isPresent();
    }

    public void submitNewPasswordValue(String value) {
        enterLogInPassword(value);
        clickSaveBtn();
    }

    public boolean isPasswordDescriptionPresent() {
        String expectedString = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.AUTH_MINOR_PASSWORD.getText());
        LOGGER.info("Expected auth headline: {}", expectedString);
        waitForPresenceOfAnElement(headlineSubtitle);
        return headlineSubtitle.getText().equalsIgnoreCase(expectedString);
    }

    public boolean isSaveAndContinueBtnPresent() {
        return saveAndContinueBtn.isPresent();
    }

    public void clickHeader() {
        headlineHeader.click();
    }

    public boolean isPasswordSubtitlePresent() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.
                        ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_CHANGE_PASSWORD_BODY.getText())).isElementPresent();
    }
}
