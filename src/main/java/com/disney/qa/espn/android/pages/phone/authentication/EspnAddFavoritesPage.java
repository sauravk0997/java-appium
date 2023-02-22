package com.disney.qa.espn.android.pages.phone.authentication;

import com.disney.qa.espn.android.pages.authentication.EspnAddFavoritesPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Handset EspnAddFavoritesPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_PHONE, parentClass = EspnAddFavoritesPageBase.class)
public class EspnAddFavoritesPage extends EspnAddFavoritesPageBase {

	public EspnAddFavoritesPage(WebDriver driver) {
		super(driver);
	}
}

