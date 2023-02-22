package com.disney.qa.espn.android.pages.tablet.espn;

import com.disney.qa.espn.android.pages.espn.EspnPlusPageBase;
import com.disney.qa.espn.android.pages.phone.espn.EspnPlusPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletPlusPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnPlusPageBase.class)
public class EspnTabletPlusPage extends EspnPlusPage {

	public EspnTabletPlusPage(WebDriver driver) {
		super(driver);
	}
}

