package com.disney.qa.espn.android.pages.tablet.paywall;

import com.disney.qa.espn.android.pages.paywall.EspnEpaywallPageBase;
import com.disney.qa.espn.android.pages.phone.paywall.EspnEpaywallPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletEpaywallPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnEpaywallPageBase.class)
public class EspnTabletEpaywallPage extends EspnEpaywallPage {

	public EspnTabletEpaywallPage(WebDriver driver) {
		super(driver);
	}
}

