package com.disney.qa.espn.android.pages.phone.watch;

import com.disney.qa.espn.android.pages.watch.EspnWatchPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnWatchPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnWatchPageBase.class)
public class EspnWatchPage extends EspnWatchPageBase {

	public EspnWatchPage(WebDriver driver) {
		super(driver);
	}
}

