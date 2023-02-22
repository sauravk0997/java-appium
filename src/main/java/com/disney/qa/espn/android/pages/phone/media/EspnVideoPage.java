package com.disney.qa.espn.android.pages.phone.media;

import com.disney.qa.espn.android.pages.media.EspnVideoPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnVideoPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnVideoPageBase.class)
public class EspnVideoPage extends EspnVideoPageBase {

	public EspnVideoPage(WebDriver driver) {
		super(driver);
	}
}

