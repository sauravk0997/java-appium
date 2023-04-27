package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAudioSubtitleIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusAudioSubtitleIOSPageBase.class)
public class DisneyPlusTabletAudioSubtitleIOSPage extends DisneyPlusAudioSubtitleIOSPageBase {
    public DisneyPlusTabletAudioSubtitleIOSPage(WebDriver driver) {
        super(driver);
    }
}
