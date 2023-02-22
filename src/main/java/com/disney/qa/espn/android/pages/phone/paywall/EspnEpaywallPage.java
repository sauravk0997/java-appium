package com.disney.qa.espn.android.pages.phone.paywall;

import com.disney.qa.espn.android.pages.paywall.EspnEpaywallPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnEpaywallPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnEpaywallPageBase.class)
public class EspnEpaywallPage extends EspnEpaywallPageBase {

	public EspnEpaywallPage(WebDriver driver) {
		super(driver);
	}
}

