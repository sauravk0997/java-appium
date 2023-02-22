package com.disney.qa.common.jarvis.apple;

import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = JarvisAppleBase.class)
public class JarvisHandset extends JarvisAppleBase {

    public JarvisHandset(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return jarvisHeader.isElementPresent();
    }
}
