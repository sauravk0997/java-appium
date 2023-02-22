package com.disney.qa.common.jarvis.apple;

import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = JarvisHandset.class)
public class JarvisTablet extends JarvisHandset {

    public JarvisTablet(WebDriver driver) {
        super(driver);
    }
}
