package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusLoginIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusLoginIOSPageBase.class)
public class DisneyPlusTabletLoginIOSPage extends DisneyPlusLoginIOSPage {

	public DisneyPlusTabletLoginIOSPage(WebDriver driver) {
		super(driver);
	}
}
