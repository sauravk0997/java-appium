package com.disney.qa.espn.android.pages.phone.settings;

import com.disney.qa.espn.android.pages.settings.EspnSettingsSubscriptionSupportPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnSettingsSubscriptionSupportPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnSettingsSubscriptionSupportPageBase.class)
public class EspnSettingsSubscriptionSupportPage extends EspnSettingsSubscriptionSupportPageBase {

	public EspnSettingsSubscriptionSupportPage(WebDriver driver) {
		super(driver);
	}
}

