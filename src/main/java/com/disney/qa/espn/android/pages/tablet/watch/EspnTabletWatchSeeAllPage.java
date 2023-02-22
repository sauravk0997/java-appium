package com.disney.qa.espn.android.pages.tablet.watch;

import com.disney.qa.espn.android.pages.phone.watch.EspnWatchSeeAllPage;
import com.disney.qa.espn.android.pages.watch.EspnWatchSeeAllPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletWatchSeeAllPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnWatchSeeAllPageBase.class)
public class EspnTabletWatchSeeAllPage extends EspnWatchSeeAllPage {

	public EspnTabletWatchSeeAllPage(WebDriver driver) {
		super(driver);
	}
}

