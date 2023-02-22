package com.disney.qa.espn.android.pages.tablet.media;

import com.disney.qa.espn.android.pages.media.EspnVideoPageBase;
import com.disney.qa.espn.android.pages.phone.media.EspnVideoPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletVideoPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnVideoPageBase.class)
public class EspnTabletVideoPage extends EspnVideoPage {

	public EspnTabletVideoPage(WebDriver driver) {
		super(driver);
	}
}

