package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAppLanguageIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusAppLanguageIOSPageBase.class)
public class DisneyPlusAppleTVAppLanguagePage extends DisneyPlusAppLanguageIOSPageBase {

    public DisneyPlusAppleTVAppLanguagePage(WebDriver driver) {
        super(driver);
    }
}
