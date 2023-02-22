package com.disney.qa.espn.android.pages.tablet.authentication;

import com.disney.qa.espn.android.pages.authentication.EspnFirstTimeLaunchPageBase;
import com.disney.qa.espn.android.pages.phone.authentication.EspnFirstTimeLaunchPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletFirstTimeLaunchPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnFirstTimeLaunchPageBase.class)
public class EspnTabletFirstTimeLaunchPage extends EspnFirstTimeLaunchPage {

	public EspnTabletFirstTimeLaunchPage(WebDriver driver) {
		super(driver);
	}
}

