package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusContentRatingIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusContentRatingIOSPageBase.class)
public class DisneyPlusTabletContentRatingIOSPage extends DisneyPlusContentRatingIOSPageBase {
    public DisneyPlusTabletContentRatingIOSPage(WebDriver driver) {
        super(driver);
    }
}
