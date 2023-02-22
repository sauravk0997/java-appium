package com.disney.qa.espn.android.pages.phone.authentication;

import com.disney.qa.espn.android.pages.authentication.EspnAddMoreFavoritesPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnAddMoreFavoritesPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnAddMoreFavoritesPageBase.class)
public class EspnAddMoreFavoritesPage extends EspnAddMoreFavoritesPageBase {

	public EspnAddMoreFavoritesPage(WebDriver driver) {
		super(driver);
	}
}

