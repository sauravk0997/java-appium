package com.disney.qa.espn.android.pages.tablet.home;

import com.disney.qa.espn.android.pages.home.EspnHomePageBase;
import com.disney.qa.espn.android.pages.phone.home.EspnHomePage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletHomePage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnHomePageBase.class)
public class EspnTabletHomePage extends EspnHomePage {

	public EspnTabletHomePage(WebDriver driver) {
		super(driver);
	}
}

