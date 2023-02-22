package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusChooseAvatarIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusChooseAvatarIOSPageBase.class)
public class DisneyPlusTabletChooseAvatarIOSPage extends DisneyPlusChooseAvatarIOSPageBase {

    public DisneyPlusTabletChooseAvatarIOSPage(WebDriver driver) {
        super(driver);
    }
}
