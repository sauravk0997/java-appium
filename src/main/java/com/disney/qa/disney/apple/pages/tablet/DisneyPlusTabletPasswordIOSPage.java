package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusPasswordIOSPageBase.class)
public class DisneyPlusTabletPasswordIOSPage extends DisneyPlusPasswordIOSPageBase {

	public DisneyPlusTabletPasswordIOSPage(WebDriver driver){
		super(driver);
	}
}
