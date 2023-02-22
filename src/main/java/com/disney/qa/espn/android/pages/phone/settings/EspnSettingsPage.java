package com.disney.qa.espn.android.pages.phone.settings;

import com.disney.qa.espn.android.pages.settings.EspnSettingsPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnSettingsPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnSettingsPageBase.class)
public class EspnSettingsPage extends EspnSettingsPageBase {

	public EspnSettingsPage(WebDriver driver) {
		super(driver);
	}
}

