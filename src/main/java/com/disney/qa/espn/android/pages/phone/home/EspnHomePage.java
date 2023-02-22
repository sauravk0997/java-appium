package com.disney.qa.espn.android.pages.phone.home;

import com.disney.qa.espn.android.pages.home.EspnHomePageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnHomePage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnHomePageBase.class)
public class EspnHomePage extends EspnHomePageBase {

	public EspnHomePage(WebDriver driver) {
		super(driver);
	}
}

