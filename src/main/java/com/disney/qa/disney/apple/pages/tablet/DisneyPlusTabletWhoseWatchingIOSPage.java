package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWhoseWatchingIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusWhoseWatchingIOSPageBase.class)
public class DisneyPlusTabletWhoseWatchingIOSPage extends DisneyPlusPasswordIOSPageBase {

	public DisneyPlusTabletWhoseWatchingIOSPage(WebDriver driver){
		super(driver);
	}
}
