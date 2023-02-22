package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusMoreMenuIOSPageBase.class)
public class DisneyPlusMoreMenuIOSPage extends DisneyPlusMoreMenuIOSPageBase {

	public DisneyPlusMoreMenuIOSPage(WebDriver driver) {
		super(driver);
	}
}
