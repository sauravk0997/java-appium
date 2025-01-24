package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.JarvisIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = JarvisIOSPageBase.class)
public class JarvisIOSPage extends JarvisIOSPageBase {
    public JarvisIOSPage(WebDriver driver) {
        super(driver);
    }
}
