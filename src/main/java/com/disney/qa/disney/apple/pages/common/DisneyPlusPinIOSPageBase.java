package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.Map;
import java.util.stream.IntStream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPinIOSPageBase extends DisneyPlusApplePageBase {
    public DisneyPlusPinIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "marketingCheckbox")
    private ExtendedWebElement pinCheckBox;

    @FindBy(id = "cancelBarButton")
    private ExtendedWebElement pinCancelButton;

    @ExtendedFindBy(accessibilityId = "pinInputTitle")
    private ExtendedWebElement pinInputTitle;

    @Override
    public boolean isOpened() {
        return pinInputTitle.isPresent();
    }

    public ExtendedWebElement getPinCheckBox() {
        return pinCheckBox;
    }

    public ExtendedWebElement getForgotPinButton() {
        return dynamicBtnFindByLabel.format(getDictionary()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.PROFILE_ENTRY_PIN_FORGOT_PIN.getText()));
    }

    public ExtendedWebElement getPinCancelButton() {
        return pinCancelButton;
    }

    public ExtendedWebElement getPinInputField() {
        return getTypeOtherContainsLabel(
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCESSIBILITY_PROFILEPIN_INPUT_EMPTY.getText()));
    }

    public ExtendedWebElement getProfilePinInvalidErrorMessage() {
        return getStaticTextByLabel(
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.PROFILE_PIN_INVALID.getText()));
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
        return staticTextByLabel.format(getDictionary()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.BTN_CANCEL_SET_PROFILE_ENTRY_PIN.getText()));
    }

    public ExtendedWebElement getSaveButton() {
        return staticTextByLabel.format(getDictionary()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.BTN_SET_PROFILE_ENTRY_PIN.getText()));
    }

    public ExtendedWebElement getAccountPasswordRequiredMessaging() {
        return getDynamicAccessibilityId(getDictionary()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                        DictionaryKeys.PCON_FORGOT_PIN_AUTH_PASSWORD_BODY.getText()));
    }

    public ExtendedWebElement getLimitAccessMessaging(String profileName) {
        String profilePinDescription = getDictionary().formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.SET_PROFILE_ENTRY_PIN_BODY.getText()), Map.of("profile_name", profileName));
        return getDynamicAccessibilityId(profilePinDescription);
    }
}
