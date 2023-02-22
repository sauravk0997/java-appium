package com.disney.qa.espn.android.pages.phone.authentication;

import com.disney.qa.espn.android.pages.authentication.EspnFirstTimeLaunchPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnFirstTimeLaunchPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnFirstTimeLaunchPageBase.class)
public class EspnFirstTimeLaunchPage extends EspnFirstTimeLaunchPageBase {

	public EspnFirstTimeLaunchPage(WebDriver driver) {
		super(driver);
	}
}

