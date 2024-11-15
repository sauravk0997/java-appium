package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusCollectionIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusCollectionIOSPageBase.class)
public class DisneyPlusCollectionIOSPage extends DisneyPlusCollectionIOSPageBase {
    public DisneyPlusCollectionIOSPage(WebDriver driver) { super(driver); }
}
