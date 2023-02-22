package com.disney.qa.espn.android.pages.phone.authentication;

import com.disney.qa.espn.android.pages.authentication.EspnLoginPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnLoginPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnLoginPageBase.class)
public class EspnLoginPage extends EspnLoginPageBase {

	public EspnLoginPage(WebDriver driver) {
		super(driver);
	}
}

