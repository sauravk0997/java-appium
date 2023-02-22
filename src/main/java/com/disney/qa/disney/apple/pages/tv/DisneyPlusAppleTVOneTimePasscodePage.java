package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusOneTimePasscodeIOSPageBase.class)
public class DisneyPlusAppleTVOneTimePasscodePage extends DisneyPlusOneTimePasscodeIOSPageBase {

    public DisneyPlusAppleTVOneTimePasscodePage (WebDriver driver) {
        super(driver);
    }

    //TODO: QAA-11329 - Move OneTimePasscode identifiers and elements from ForgotPasswordPage
}
