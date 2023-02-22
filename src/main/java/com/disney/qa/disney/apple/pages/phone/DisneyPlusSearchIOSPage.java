package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusSearchIOSPageBase.class)
public class DisneyPlusSearchIOSPage extends DisneyPlusSearchIOSPageBase {

	public DisneyPlusSearchIOSPage(WebDriver driver) {
		super(driver);
	}
}
