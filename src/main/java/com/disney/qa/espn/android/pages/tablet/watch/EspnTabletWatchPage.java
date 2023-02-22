package com.disney.qa.espn.android.pages.tablet.watch;

import com.disney.qa.espn.android.pages.phone.watch.EspnWatchPage;
import com.disney.qa.espn.android.pages.watch.EspnWatchPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletWatchPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnWatchPageBase.class)
public class EspnTabletWatchPage extends EspnWatchPage {

	public EspnTabletWatchPage(WebDriver driver) {
		super(driver);
	}
}

