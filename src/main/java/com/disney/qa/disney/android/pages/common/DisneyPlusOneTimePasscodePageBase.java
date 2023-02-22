package com.disney.qa.disney.android.pages.common;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusOneTimePasscodePageBase extends DisneyPlusCommonPageBase {

    @FindBy(id = "forgotPwdLayout")
    private ExtendedWebElement otpLayout;

    @FindBy(id = "onboardingDisneyLogo")
    private ExtendedWebElement disneyLogo;

    @FindBy(id = "forgotPwdPinTitle")
    private ExtendedWebElement titleTxt;

    @FindBy(id = "forgotPwdPinMessageText")
    private ExtendedWebElement messageTxt;

    @FindBy(id = "disneyPinCode")
    private ExtendedWebElement pinCode;

    @FindBy(id = "resendEmailTextView")
    private ExtendedWebElement resendEmailTxt;

    @FindBy(id = "forgotPasswordResendEmailButton")
    private ExtendedWebElement resendBtn;

    @FindBy(id = "pinCodeErrorTextView")
    private ExtendedWebElement pinErrorText;

    public DisneyPlusOneTimePasscodePageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return otpLayout.isElementPresent();
    }

    public boolean isOnboardingDisneyLogoPresent() {
        return disneyLogo.isElementPresent();
    }

    public boolean isTitlePresent() {
        return titleTxt.isElementPresent();
    }

    public String getTitleText() {
        return getElementText(titleTxt);
    }

    public boolean isMessagePresent() {
        return messageTxt.isElementPresent();
    }

    public String getMessageText() {
        return getElementText(messageTxt);
    }

    public boolean isPinEntryFieldPresent() {
        return pinCode.isElementPresent();
    }

    public void clickPinEntry() {
        pinCode.click();
    }

    public boolean doesPinEntryActivateKeyboard() {
        try {
            pinCode.click();
        } catch (NoSuchElementException nse) {
            LOGGER.error("PIN entry field is not present!");
            return false;
        }
        return new AndroidUtilsExtended().isKeyboardShown();
    }

    public boolean isResendEmailTextPresent() {
        return resendEmailTxt.isElementPresent();
    }

    public String getResendEmailText() {
        return getElementText(resendEmailTxt);
    }

    public boolean isResendButtonPresent() {
        return resendBtn.isElementPresent();
    }

    public String getResendButtonText() {
        return getElementText(resendBtn);
    }

    public void clickResendButton() {
        resendBtn.click();
    }

    public boolean isPinErrorTextDisplayed() {
        return pinErrorText.isElementPresent();
    }

    public String getPinErrorText() {
        return getElementText(pinErrorText);
    }
}
