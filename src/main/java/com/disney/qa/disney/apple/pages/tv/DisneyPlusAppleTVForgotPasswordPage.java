package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.CHECK_EMAIL_COPY;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.CHECK_EMAIL_TITLE;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.EMAIL_CODE_TITLE;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.RESEND_EMAIL_COPY;
import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.RESEND_EMAIL_COPY_2;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusOneTimePasscodeIOSPageBase.class)
public class DisneyPlusAppleTVForgotPasswordPage extends DisneyPlusOneTimePasscodeIOSPageBase {

    private static final String EMAIL_PLACEHOLDER = "{user_email}";

    private static final String TEXT_FIELD_INPUT_CODE = "textFieldInputCode";

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField[`name == \"textFieldInputCode\"`]")
    private ExtendedWebElement otpInputCodeField;

    @ExtendedFindBy(accessibilityId = "buttonResend")
    private ExtendedWebElement resendButton;

    @Override
    public boolean isOpened() {
        boolean isPresent = headlineHeader.getText().equals(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CHECK_EMAIL_TITLE.getText()));
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public DisneyPlusAppleTVForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getForgotPasswordExpectedScreenTexts(String email) {
        var expectedList = new ArrayList<String>();

        Stream.of(CHECK_EMAIL_TITLE, CHECK_EMAIL_COPY, RESEND_EMAIL_COPY, RESEND_EMAIL_COPY_2).collect(Collectors.toList())
                .forEach(item -> {
                    if (item == CHECK_EMAIL_COPY) {
                        expectedList.add(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, item.getText()).replace(EMAIL_PLACEHOLDER, email));
                    } else if (item == RESEND_EMAIL_COPY_2) {
                        expectedList.add(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, item.getText()).toUpperCase());
                    } else {
                        expectedList.add(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, item.getText()));
                    }
                });
        return expectedList;
    }

    public void clickOnOtpField() {
        getDynamicTextEntryFieldByName(TEXT_FIELD_INPUT_CODE).click();
    }

    public boolean isNumericKeyboardOpen() {
        boolean isPresent = isAIDElementPresentWithScreenshot(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, EMAIL_CODE_TITLE.getText()));
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public String getOTPCode(String code) {
        String text = staticTypeTextViewValue.format(code).getText();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return text;
    }

    public void enterOTP(String otp) {
        getDynamicTextEntryFieldByName(TEXT_FIELD_INPUT_CODE).type(otp);
        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public void enterOTPLocalized(String otp) {
        getDynamicTextEntryFieldByName(TEXT_FIELD_INPUT_CODE).type(otp);
        moveDown(1,1);
        clickSelect();
    }

    public String getOTPText() {
        return otpInputCodeField.getText();
    }

    public void clickResend() {
        resendButton.click();
    }

    public String getCheckYourEmailScreenTitle() {
        return getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EMAIL_RESEND_TITLE.getText());
    }

    public String getOTPErrorMessage() {
        return getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_PASSCODE.getText());
    }
}