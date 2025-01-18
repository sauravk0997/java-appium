package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusOneTimePasscodeIOSPageBase.class)
public class DisneyPlusAppleTVOneTimePasscodePage extends DisneyPlusOneTimePasscodeIOSPageBase {

    public DisneyPlusAppleTVOneTimePasscodePage (WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getLoginButtonWithPassword().isPresent();
    }

    //TODO: QAA-16993 - Move OneTimePasscode identifiers and elements from ForgotPasswordPage

    public void enterOTPCode(String otp) {
        char[] otpArray = otp.toCharArray();
        for (char otpChar : otpArray) {
            dynamicBtnFindByLabel.format(otpChar).click();
        }
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
    }

    public void clickLoginWithPassword() {
        moveDown(3, 1);
        clickSelect();
    }
}
