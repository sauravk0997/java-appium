package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusOneTimePasscodeIOSPage;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusOneTimePasscodeIOSPageBase.class)
public class DisneyPlusTabletOneTimePasscodeIOSPage extends DisneyPlusOneTimePasscodeIOSPage {

    public DisneyPlusTabletOneTimePasscodeIOSPage (WebDriver driver) {
        super(driver);
    }
}
