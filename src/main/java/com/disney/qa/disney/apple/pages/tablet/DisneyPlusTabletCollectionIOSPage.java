package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusCollectionIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusCollectionIOSPageBase.class)
public class DisneyPlusTabletCollectionIOSPage extends DisneyPlusCollectionIOSPageBase{
    public DisneyPlusTabletCollectionIOSPage(WebDriver driver) { super(driver); }
}
