package com.disney.qa.espn.android.pages.phone.common;

import com.disney.qa.espn.android.pages.common.EspnCommonPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnCommonPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnCommonPageBase.class)
public class EspnCommonPage extends EspnCommonPageBase {

	public EspnCommonPage(WebDriver driver) {
		super(driver);
	}
}

