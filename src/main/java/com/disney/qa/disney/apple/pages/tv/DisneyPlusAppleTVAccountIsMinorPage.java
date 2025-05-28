package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountIsMinorIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusAccountIsMinorIOSPageBase.class)
public class DisneyPlusAppleTVAccountIsMinorPage extends DisneyPlusAccountIsMinorIOSPageBase {

    public DisneyPlusAppleTVAccountIsMinorPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getHeaderTextElement().isPresent();
    }
}
