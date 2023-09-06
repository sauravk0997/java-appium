package com.disney.qa.disney.apple.pages.phone;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusWelcomeScreenIOSPageBase.class)
public class DisneyPlusWelcomeScreenIOSPage extends DisneyPlusWelcomeScreenIOSPageBase {

	public DisneyPlusWelcomeScreenIOSPage(WebDriver driver) {
		super(driver);
	}
}
