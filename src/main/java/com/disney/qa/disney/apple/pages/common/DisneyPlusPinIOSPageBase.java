package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.stream.IntStream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPinIOSPageBase extends DisneyPlusApplePageBase {
    public DisneyPlusPinIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "marketingCheckbox")
    private ExtendedWebElement pinCheckBox;

    @ExtendedFindBy(accessibilityId = "cancelBarButton")
    private ExtendedWebElement pinCancelButton;

    @ExtendedFindBy(accessibilityId = "pinInputTitle")
    private ExtendedWebElement pinInputTitle;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"profilePin\"`]/XCUIElementTypeOther[2]")
    private ExtendedWebElement pinInputField;


    @Override
    public boolean isOpened() {
        return pinInputTitle.isPresent();
    }

    public ExtendedWebElement getPinCheckBox() {
        return pinCheckBox;
    }

    public ExtendedWebElement getForgotPinButton() {
        return dynamicBtnFindByLabel.format(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.PROFILE_ENTRY_PIN_FORGOT_PIN.getText()));
    }

    public ExtendedWebElement getPinCancelButton() {
        return pinCancelButton;
    }

    public ExtendedWebElement getPinInputField() {
        return getTypeOtherContainsLabel(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESSIBILITY_PROFILEPIN_INPUT_EMPTY.getText()));
    }

    public ExtendedWebElement getProfilePinInvalidErrorMessage() {
        return getStaticTextByLabel(
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.PROFILE_PIN_INVALID.getText()));
    }

    public void clickProfilePin() {
        getPinInputField().click();
    }

    public void enterProfilePin(String pin) {
        getPinInputField().type(pin);
    }

    public void clearPin() {
        IntStream.range(0, 4).forEach(i -> getKeyboardDelete().click());
    }

    public ExtendedWebElement getCancelButton() {
        return getDynamicAccessibilityId(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.BTN_CANCEL_SET_PROFILE_ENTRY_PIN.getText()));
    }

    public ExtendedWebElement getSaveButton() {
        return getDynamicAccessibilityId(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.BTN_SET_PROFILE_ENTRY_PIN.getText()));
    }

    public ExtendedWebElement getR21SetPinButton() {
        return getDynamicAccessibilityId(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.R21_CREATE_PIN_SET_PIN.getText()));
    }

    public ExtendedWebElement getAccountPasswordRequiredMessaging() {
        return getDynamicAccessibilityId(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.PCON_FORGOT_PIN_AUTH_PASSWORD_BODY.getText()));
    }

    public ExtendedWebElement getLimitAccessMessaging(String profileName) {
        String profilePinDescription = getLocalizationUtils().formatPlaceholderString(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.SET_PROFILE_ENTRY_PIN_BODY.getText()), Map.of("profile_name", profileName));
        return getDynamicAccessibilityId(profilePinDescription);
    }

    public ExtendedWebElement getProfilePinMissingErrorMessage() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.SDK_ERROR_PROFILE_PIN_MISSING.getText()));
    }

    public boolean isPinFieldNumberPresent(String number) {
        return getTypeOtherByLabel(number).isPresent();
    }

    public boolean isR21PinPageOpen() {
        return getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                DictionaryKeys.R21_CREATE_PIN_CREATE_PIN.getText())).isPresent();
    }

    public boolean isR21PinPageModalHeaderDisplayed() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                DictionaryKeys.R21_MUST_CREATE_PIN_MODAL_HEADER.getText())).isPresent();
    }

    public boolean isR21PinPageModalMessageDisplayed() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                DictionaryKeys.R21_MUST_CREATE_PIN_MODAL_MESSAGE.getText())).isPresent();
    }

    public void enterPin(String pin) {
        pinInputField.type(pin);
    }

    public boolean isContinueButtonOnCancelModalDisplayed() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                DictionaryKeys.R21_MUST_CREATE_PIN_MODAL_CONTINUE_BUTTON.getText())).isPresent();
    }

    public boolean isNotNowButtonOnCancelModalDisplayed() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                DictionaryKeys.R21_MUST_CREATE_PIN_MODAL_NOT_NOW_BUTTON.getText())).isPresent();
    }
}
