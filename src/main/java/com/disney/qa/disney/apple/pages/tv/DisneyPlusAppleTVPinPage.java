package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusPinIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.*;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusPinIOSPageBase.class)
public class DisneyPlusAppleTVPinPage extends DisneyPlusPinIOSPageBase {
    public DisneyPlusAppleTVPinPage(WebDriver driver) {
        super(driver);
    }

    public void enterProfilePin(String otp) {
        char[] otpArray = otp.toCharArray();
        for (char otpChar : otpArray) {
            dynamicBtnFindByLabel.format(otpChar).click();
        }
    }
}
