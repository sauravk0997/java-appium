package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusHomeIOSPageBase.class)
public class DisneyPlusHomeIOSPage extends DisneyPlusHomeIOSPageBase {

	public DisneyPlusHomeIOSPage(WebDriver driver) {
		super(driver);
	}
}
