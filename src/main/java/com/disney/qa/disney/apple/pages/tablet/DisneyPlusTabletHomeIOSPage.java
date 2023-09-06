package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusHomeIOSPage;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusHomeIOSPageBase.class)
public class DisneyPlusTabletHomeIOSPage extends DisneyPlusHomeIOSPage {

	public DisneyPlusTabletHomeIOSPage(WebDriver driver){
		super(driver);
	}
}
