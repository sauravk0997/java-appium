package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPinIOSPageBase extends DisneyPlusApplePageBase {
    public DisneyPlusPinIOSPageBase(WebDriver driver) {
        super(driver);
    }

    private ExtendedWebElement pinInputField = dynamicRowOtherLabel.format( getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                    DictionaryKeys.ACCESSIBILITY_PROFILEPIN_INPUT_EMPTY.getText()),
            DictionaryKeys.ACCESSIBILITY_PROFILEPIN_INPUT_EMPTY.getText(),
            1);

    @FindBy(id = "marketingCheckbox")
    private ExtendedWebElement pinCheckBox;
    private ExtendedWebElement forgotPinButton = xpathNameOrName.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON,
                            DictionaryKeys.PROFILE_ENTRY_PIN_FORGOT_PIN.getText()),
            DictionaryKeys.PROFILE_ENTRY_PIN_FORGOT_PIN.getText());

    @FindBy(id = "cancelBarButton")
    private ExtendedWebElement pinCancelButton;

    @FindBy(id = "saveButton")
    private ExtendedWebElement pinSaveButton;

    public ExtendedWebElement getPinCheckBox() {
        return pinCheckBox;
    }

    public ExtendedWebElement getForgotPinButton() {
        return forgotPinButton;
    }

    public ExtendedWebElement getPinCancelButton() {
        return pinCancelButton;
    }

    public ExtendedWebElement getPinInputField() {
        return pinInputField;
    }

    public ExtendedWebElement getPinSaveButton() {
        return pinSaveButton;
    }
}
