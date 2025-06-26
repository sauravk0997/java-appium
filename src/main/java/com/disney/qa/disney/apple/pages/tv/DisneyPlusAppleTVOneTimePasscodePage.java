package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusOneTimePasscodeIOSPageBase.class)
public class DisneyPlusAppleTVOneTimePasscodePage extends DisneyPlusOneTimePasscodeIOSPageBase {

    public DisneyPlusAppleTVOneTimePasscodePage (WebDriver driver) {
        super(driver);
    }

    private static final String ONE_TIME_PASSCODE = "oneTimePasscode";

    @Override
    public boolean isOpened() {
        return getOneTimePasscode().isPresent();
    }

    public void enterOTPCode(String otp) {
        char[] otpArray = otp.toCharArray();
        for (char otpChar : otpArray) {
            dynamicBtnFindByLabel.format(otpChar).click();
        }
    }

    public void clickLoginWithPassword() {
        moveDown(3, 1);
        clickSelect();
    }

    public ExtendedWebElement getOneTimePasscode() {
        return getTypeOtherByName(ONE_TIME_PASSCODE);
    }
}
