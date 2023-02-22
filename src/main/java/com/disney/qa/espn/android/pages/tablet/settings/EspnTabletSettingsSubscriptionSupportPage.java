package com.disney.qa.espn.android.pages.tablet.settings;

import com.disney.qa.espn.android.pages.phone.settings.EspnSettingsSubscriptionSupportPage;
import com.disney.qa.espn.android.pages.settings.EspnSettingsSubscriptionSupportPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletSettingsSubscriptionSupportPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnSettingsSubscriptionSupportPageBase.class)
public class EspnTabletSettingsSubscriptionSupportPage extends EspnSettingsSubscriptionSupportPage {

	public EspnTabletSettingsSubscriptionSupportPage(WebDriver driver) {
		super(driver);
	}
}

