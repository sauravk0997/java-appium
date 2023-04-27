package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusWatchlistIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusWatchlistIOSPageBase.class)
public class DisneyPlusTabletWatchlistIOSPage extends DisneyPlusWatchlistIOSPageBase {

	public DisneyPlusTabletWatchlistIOSPage(WebDriver driver) {
		super(driver);
	}
}
