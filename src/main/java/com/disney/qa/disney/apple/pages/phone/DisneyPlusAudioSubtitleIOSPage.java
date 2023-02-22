package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAudioSubtitleIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusAudioSubtitleIOSPageBase.class)
public class DisneyPlusAudioSubtitleIOSPage extends DisneyPlusAudioSubtitleIOSPageBase {
    public DisneyPlusAudioSubtitleIOSPage(WebDriver driver) {
        super(driver);
    }
}
