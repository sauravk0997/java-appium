package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusUpdateProfileIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusUpdateProfileIOSPageBase.class)
public class DisneyPlusAppleTVUpdateProfilePage extends DisneyPlusUpdateProfileIOSPageBase {
    public DisneyPlusAppleTVUpdateProfilePage(WebDriver driver) { super(driver); }
}
