package com.disney.qa.espn.android.pages.phone.espn;

import com.disney.qa.espn.android.pages.espn.EspnPlusPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnPlusPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnPlusPageBase.class)
public class EspnPlusPage extends EspnPlusPageBase {

	public EspnPlusPage(WebDriver driver) {
		super(driver);
	}
}

