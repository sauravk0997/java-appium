package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusOneTimePasscodeIOSPageBase.class)
public class DisneyPlusAppleTVForgotPasswordPage extends DisneyPlusOneTimePasscodeIOSPageBase {

    private static final String EMAIL_PLACEHOLDER = "{email}";

    private static final String TEXT_FIELD_INPUT_CODE = "textFieldInputCode";

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"oneTimePasscode\"`]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]")
    private ExtendedWebElement otpInputCodeField;

    @ExtendedFindBy(accessibilityId = "buttonResend")
    private ExtendedWebElement resendButton;

    private ExtendedWebElement checkEmailTitle = getStaticTextByLabel(getAppleTVLocalizationUtils().getDictionaryItem(
            DisneyDictionaryApi.ResourceKeys.APPLICATION, CHECK_EMAIL_TITLE.getText()));

    @Override
    public boolean isOpened() {
        return getOTPHeader().isPresent(5);
    }

    public ExtendedWebElement getOTPHeader() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_OTP_HEADER.getText()));
    }

    public ExtendedWebElement getCheckEmailTitle() {
        return checkEmailTitle;
    }

    public DisneyPlusAppleTVForgotPasswordPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getForgotPasswordExpectedScreenTexts(String email) {
        var expectedList = new ArrayList<String>();

        Stream.of(MY_DISNEY_OTP_HEADER, MY_DISNEY_OTP_BODY, MY_DISNEY_OTP_LOGIN_RESEND_BTN, MY_DISNEY_CONTINUE_BTN).collect(Collectors.toList())
                .forEach(item -> {
                    if (item == MY_DISNEY_OTP_BODY) {
                        expectedList.add(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, item.getText()).replace(EMAIL_PLACEHOLDER, email));
                    } else {
                        expectedList.add(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, item.getText()));
                    }
                });
        return expectedList;
    }

    public void clickOnOtpField() {
        otpInputCodeField.click();
    }

    public boolean isNumericKeyboardOpen() {
        boolean isPresent = isAIDElementPresentWithScreenshot(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, EMAIL_CODE_TITLE.getText()));
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public String getOTPCode(String code) {
        String text = staticTypeTextViewValue.format(code).getText();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return text;
    }

    public void enterOTP(String otp) {
        char[] otpArray = otp.toCharArray();
        for (char otpChar : otpArray) {
            dynamicBtnFindByLabel.format(otpChar).click();
        }
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
    }

    public void enterOTPLocalized(String otp) {
        getDynamicTextEntryFieldByName(TEXT_FIELD_INPUT_CODE).type(otp);
        moveDown(1,1);
        clickSelect();
    }

    public boolean isOTPPlaceholderPresent() {
        return otpInputCodeField.isPresent();
    }

    public void clickResend() {
        resendButton.click();
    }

    public String getCheckYourEmailScreenTitle() {
        return getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.EMAIL_RESEND_TITLE.getText());
    }

    public String getOTPErrorMessage() {
        return getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SDK_ERRORS, DictionaryKeys.INVALID_PASSCODE.getText());
    }

    public void clickContinueBtnOnOTPPage() {
        primaryButton.click();
    }

    public boolean isResentEmailHeaderPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_OTP_RESENT_HEADER.getText())).isPresent();
    }

    public boolean isResentEmailBodyPresent() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                DictionaryKeys.MY_DISNEY_OTP_RESENT_BODY.getText())).isPresent();
    }

    public void clickAgreeAndContinue() {
        getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY, DictionaryKeys.MY_DISNEY_CONTINUE_BTN.getText())).click();
    }
}