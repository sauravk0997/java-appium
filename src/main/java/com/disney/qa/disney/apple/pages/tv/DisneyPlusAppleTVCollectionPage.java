package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusCollectionIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusCollectionIOSPageBase.class)
public class DisneyPlusAppleTVCollectionPage extends DisneyPlusCollectionIOSPageBase {

    public DisneyPlusAppleTVCollectionPage(WebDriver driver) { super(driver); }
}
