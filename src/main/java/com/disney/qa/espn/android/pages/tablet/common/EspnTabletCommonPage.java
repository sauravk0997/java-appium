package com.disney.qa.espn.android.pages.tablet.common;

import com.disney.qa.espn.android.pages.common.EspnCommonPageBase;
import com.disney.qa.espn.android.pages.phone.common.EspnCommonPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletCommonPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnCommonPageBase.class)
public class EspnTabletCommonPage extends EspnCommonPage {

	public EspnTabletCommonPage(WebDriver driver) {
		super(driver);
	}
}

