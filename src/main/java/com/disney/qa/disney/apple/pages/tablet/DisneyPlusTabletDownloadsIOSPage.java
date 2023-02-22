package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDownloadsIOSPageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusDownloadsIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusDownloadsIOSPageBase.class)
public class DisneyPlusTabletDownloadsIOSPage extends DisneyPlusDownloadsIOSPage {

	public DisneyPlusTabletDownloadsIOSPage(WebDriver driver){
		super(driver);
	}
}
