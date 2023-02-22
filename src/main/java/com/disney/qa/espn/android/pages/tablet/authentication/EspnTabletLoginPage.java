package com.disney.qa.espn.android.pages.tablet.authentication;

import com.disney.qa.espn.android.pages.authentication.EspnLoginPageBase;
import com.disney.qa.espn.android.pages.phone.authentication.EspnLoginPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletLoginPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnLoginPageBase.class)
public class EspnTabletLoginPage extends EspnLoginPage {

	public EspnTabletLoginPage(WebDriver driver) {
		super(driver);
	}
}

