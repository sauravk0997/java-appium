package com.disney.qa.common.jarvis.android;

import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = JarvisHandset.class)
public class JarvisTablet extends JarvisHandset {
    public JarvisTablet(WebDriver driver) {
        super(driver);
    }
}
