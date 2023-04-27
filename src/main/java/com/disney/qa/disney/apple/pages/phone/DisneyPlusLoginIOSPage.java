package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusLoginIOSPageBase.class)
public class DisneyPlusLoginIOSPage extends DisneyPlusLoginIOSPageBase {

	public DisneyPlusLoginIOSPage(WebDriver driver) {
		super(driver);
	}
}
