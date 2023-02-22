package com.disney.qa.espn.android.pages.tablet.settings;

import com.disney.qa.espn.android.pages.phone.settings.EspnSettingsPage;
import com.disney.qa.espn.android.pages.settings.EspnSettingsPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletSettingsPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnSettingsPageBase.class)
public class EspnTabletSettingsPage extends EspnSettingsPage {

	public EspnTabletSettingsPage(WebDriver driver) {
		super(driver);
	}
}

