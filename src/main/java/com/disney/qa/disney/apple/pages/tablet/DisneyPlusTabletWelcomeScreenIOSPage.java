package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusWelcomeScreenIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusWelcomeScreenIOSPageBase.class)
public class DisneyPlusTabletWelcomeScreenIOSPage extends DisneyPlusWelcomeScreenIOSPage {

	public DisneyPlusTabletWelcomeScreenIOSPage(WebDriver driver) {
		super(driver);
	}
}
