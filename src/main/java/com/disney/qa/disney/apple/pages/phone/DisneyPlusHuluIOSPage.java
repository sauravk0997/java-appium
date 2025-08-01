package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusHuluIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

    @SuppressWarnings("squid:MaximumInheritanceDepth")
    @DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusHuluIOSPageBase.class)
    public class DisneyPlusHuluIOSPage extends DisneyPlusHuluIOSPageBase {

        public DisneyPlusHuluIOSPage(WebDriver driver) {
            super(driver);
        }
    }

