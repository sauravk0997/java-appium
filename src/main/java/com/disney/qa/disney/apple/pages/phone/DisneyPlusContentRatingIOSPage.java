package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusContentRatingIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusContentRatingIOSPageBase.class)
public class DisneyPlusContentRatingIOSPage extends DisneyPlusContentRatingIOSPageBase {
    public DisneyPlusContentRatingIOSPage(WebDriver driver) {
        super(driver);
    }
}
