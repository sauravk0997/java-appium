package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.JarvisIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = JarvisIOSPageBase.class)
public class JarvisTabletIOSPageBase extends JarvisIOSPageBase {
    public JarvisTabletIOSPageBase(WebDriver driver) {
        super(driver);
    }
}
