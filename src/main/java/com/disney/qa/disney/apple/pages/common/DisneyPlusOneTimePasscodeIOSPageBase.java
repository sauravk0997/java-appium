package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.MY_DISNEY_OTP_INCORRECT_ERROR;
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

    private ExtendedWebElement resendButton = xpathNameOrName.format(getLocalizationUtils()
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
        getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CONTINUE_BTN.getText())).click();
    }

    public void enterOtpValueDismissKeys(String value) {
        otpInputField.type(value);
        dismissKeyboardForPhone();
        getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CONTINUE_BTN.getText())).click();
    }

    public void enterOtp(String value) {
        otpInputField.type(value);
    }

    public boolean isOtpIncorrectErrorPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                        MY_DISNEY_OTP_INCORRECT_ERROR.getText()))
                .isPresent();
    }

    @Override
    public void clickCancelBtn() {
        getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_CANCEL_BTN.getText())).click();
    }
}
