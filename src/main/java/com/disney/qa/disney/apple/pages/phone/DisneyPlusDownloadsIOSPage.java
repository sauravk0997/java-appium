package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDownloadsIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusDownloadsIOSPageBase.class)
public class DisneyPlusDownloadsIOSPage extends DisneyPlusDownloadsIOSPageBase {

	public DisneyPlusDownloadsIOSPage(WebDriver driver) {
		super(driver);
	}
}
