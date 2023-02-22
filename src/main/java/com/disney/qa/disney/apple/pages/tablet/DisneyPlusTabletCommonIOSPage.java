package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusCommonIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusTabletCommonIOSPage extends DisneyPlusCommonIOSPage {

	public DisneyPlusTabletCommonIOSPage(WebDriver driver) {
		super(driver);
	}
}
