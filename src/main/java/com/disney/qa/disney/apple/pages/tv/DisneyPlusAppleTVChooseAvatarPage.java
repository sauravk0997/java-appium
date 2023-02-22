package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusChooseAvatarIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVChooseAvatarPage extends DisneyPlusChooseAvatarIOSPageBase {

    public DisneyPlusAppleTVChooseAvatarPage(WebDriver driver) { super(driver); }
}
