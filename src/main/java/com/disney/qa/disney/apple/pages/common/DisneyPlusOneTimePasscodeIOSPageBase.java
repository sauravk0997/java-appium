package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.RESEND_EMAIL_COPY_2;

/*
 * Forgot Password Page
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOneTimePasscodeIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusOneTimePasscodeIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @FindBy(id = "oneTimePasscodeContainerInputView")
    protected ExtendedWebElement otpInputField;

    private ExtendedWebElement resendButton = xpathNameOrName.format(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                            RESEND_EMAIL_COPY_2.getText()),
            RESEND_EMAIL_COPY_2.getText());

    public void clickResend() {
        resendButton.click();
    }


    @Override
    public boolean isOpened() {
        return headlineHeader.isElementPresent();
    }

    public void enterOtpValue(String value) {
        otpInputField.type(value);
        getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_CONTINUE.getText())).click();
    }

    public void enterOtpValueDismissKeys(String value) {
        otpInputField.type(value);
        dismissKeyboardForPhone();
        getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_CONTINUE.getText())).click();
    }

}
