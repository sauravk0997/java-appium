package com.disney.qa.espn.android.pages.phone.watch;

import com.disney.qa.espn.android.pages.watch.EspnWatchSeeAllPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnWatchSeeAllPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnWatchSeeAllPageBase.class)
public class EspnWatchSeeAllPage extends EspnWatchSeeAllPageBase {

	public EspnWatchSeeAllPage(WebDriver driver) {
		super(driver);
	}
}

