package com.disney.qa.disney.android.pages.tv.pages;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVLoginPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAndroidTVForgotPasswordPageBase extends DisneyPlusAndroidTVCommonPage {

    @FindBy(id = "forgotPasswordPinTitleText")
    private ExtendedWebElement forgotPasswordTitle;

    @FindBy(id = "forgotPwdPinMessageText")
    private ExtendedWebElement forgotPasswordBodyText;

    @FindBy(id = "forgotPasswordResendEmailText")
    private ExtendedWebElement resendEmailText;

    @FindBy(id = "forgotPasswordResendEmailButton")
    private ExtendedWebElement resendEmailBtn;

    @FindBy(id = "disneyPinCode")
    private ExtendedWebElement passcodeEntryField;

    @FindBy(id = "passwordResetRoot")
    private ExtendedWebElement resetPasswordTitle;

    @FindBy(id = "pinCodeEditText")
    private ExtendedWebElement pinCodeEditText;

    @FindBy(xpath = "//*[@resource-id = 'com.disney.disneyplus:id/disneyPinCode']/*[@class='android.widget.TextView']")
    private ExtendedWebElement pinCodeGhost;

    public DisneyPlusAndroidTVForgotPasswordPageBase(WebDriver driver) {
        super(driver);
    }

    public List<String> getPinCodeGhost() {
        return findExtendedWebElements(pinCodeGhost.getBy()).stream().map(ExtendedWebElement::getText).collect(Collectors.toList());
    }

    public void enterOtp(String otp) {
        // On FireTV the keypad obscures the DOM and elements are not discoverable.
        // So just blast keys. Works on vanilla AndroidTV too.
        getAndroidTVUtilsInstance().sendInput(otp);

        UniversalUtils.captureAndUpload(getCastedDriver());
    }

    public boolean isForgotPasswordTitlePresent(long timeout) {
        return forgotPasswordTitle.isElementPresent(timeout);
    }

    public void clickOkBtn() {
        tierTwoButtonOne.click();
    }

    public void clickResendEmailBtn() {
        resendEmailBtn.click();
    }

    public void moveToResendEmailBtnAndSelect() {
        if (AndroidTVUtils.isAmazon()){
            pressDown(1);
            if (getAndroidTVUtilsInstance().isFocused(pinCodeEditText)) {
                getAndroidTVUtilsInstance().pressEnter();
                pressRight(1);
            }
        } else {
            pressTabToMoveToTheNextField();
        }
        pressRight(1);
        selectFocusedElement();
    }

    public boolean isOTPScreenOpened() {
        boolean isPresent = forgotPasswordTitle.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public BufferedImage getOTPEntryFieldImage() {
        return new UniversalUtils().getElementImage(passcodeEntryField);
    }

    public List<String> forgotPasswordScreenTexts() {
        List<ExtendedWebElement> list = findExtendedWebElements(standardButton.getBy());
        List<String> textList = Stream.of(forgotPasswordTitle.getText(), forgotPasswordBodyText.getText().replaceAll("\\n", " "),
                resendEmailText.getText()).collect(Collectors.toList());
        list.forEach(item -> textList.add(String.valueOf(getTextViewInElement(item))));

        return textList;
    }

    public boolean isResetPasswordScreenOpened() {
        return resetPasswordTitle.isElementPresent();
    }

    public List<String> resendEmailTexts() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return Stream.of(tierTwoTitle.getText(), tierTwoSubtitle.getText(), tierTwoButtonOne.getText()).collect(Collectors.toList());
    }

    public enum ForgotPasswordItems {
        SCREEN_TITLE("check_email_title"),
        BODY_TEXT("check_email_copy"),
        RESEND_EMAIL_TEXT("resend_email_copy"),
        RESEND_EMAIL_BTN("btn_resend_email_code"),
        RESEND_EMAIL_TITLE("email_resend_title"),
        RESEND_EMAIL_SUBTITLE("email_resend_subtitle"),
        OK_BTN("btn_ok");

        private String forgotPasswordTexts;

        public static final List<String> screenTexts = Stream.of(SCREEN_TITLE.getText(), BODY_TEXT.getText(), RESEND_EMAIL_TEXT.getText(),
                DisneyPlusAndroidTVLoginPage.LoginItems.CONTINUE_BTN.getText(), RESEND_EMAIL_BTN.getText()).collect(Collectors.toList());

        public static final List<String> resendEmailScreenTexts = Stream.of(RESEND_EMAIL_TITLE.getText(),
                RESEND_EMAIL_SUBTITLE.getText(), OK_BTN.getText()).collect(Collectors.toList());

        ForgotPasswordItems(String forgotPasswordTexts) {
            this.forgotPasswordTexts = forgotPasswordTexts;
        }

        public String getText() {
            return this.forgotPasswordTexts;
        }
    }

    public boolean isResendEmailScreenOpen() {
        return tierTwoTitle.isElementPresent();
    }

    public String getInputText() {
        UniversalUtils.captureAndUpload(getCastedDriver());
        return pinCodeEditText.getText();
    }

    /**
     * Hides the numeric keyboard when landing on the OTP screen.
     * If the user previously entered a number then the keyboard requires a different key to be dismissed
     * @param keysEntered - Have numbers been input using the keyboard?
     */
    public void hideKeyboardOnOTPLanding(boolean keysEntered) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        pause(5);
        if (AndroidTVUtils.isAmazon()) {
            LOGGER.info("CLOSING KEYBOARD ON OTP SCREEN FOR AMAZON DEVICE");
            if (keysEntered) {
                androidTVUtils.pressEscape();
            } else {
                androidTVUtils.pressTab();
            }
        } else {
            androidTVUtils.hideKeyboardIfPresent();
        }
        UniversalUtils.captureAndUpload(getCastedDriver());
    }
}