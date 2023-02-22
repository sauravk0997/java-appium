package com.disney.qa.espn.android.pages.tablet.authentication;

import com.disney.qa.espn.android.pages.authentication.EspnAddMoreFavoritesPageBase;
import com.disney.qa.espn.android.pages.phone.authentication.EspnAddMoreFavoritesPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletAddMoreFavoritesPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnAddMoreFavoritesPageBase.class)
public class EspnTabletAddMoreFavoritesPage extends EspnAddMoreFavoritesPage {

	public EspnTabletAddMoreFavoritesPage(WebDriver driver) {
		super(driver);
	}
}

