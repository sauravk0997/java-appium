package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusSearchIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusSearchIOSPageBase.class)
public class DisneyPlusTabletSearchIOSPage extends DisneyPlusSearchIOSPage {

	public DisneyPlusTabletSearchIOSPage(WebDriver driver){
		super(driver);
	}
}
