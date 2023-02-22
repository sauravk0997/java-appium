package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusContentRatingIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusContentRatingIOSPageBase.class)
public class DisneyPlusAppleTVContentRatingPage extends DisneyPlusContentRatingIOSPageBase {
    public DisneyPlusAppleTVContentRatingPage(WebDriver driver) {
        super(driver);
    }
}
