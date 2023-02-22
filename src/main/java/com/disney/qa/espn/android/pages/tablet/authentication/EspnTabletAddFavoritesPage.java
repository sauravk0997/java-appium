package com.disney.qa.espn.android.pages.tablet.authentication;

import com.disney.qa.espn.android.pages.authentication.EspnAddFavoritesPageBase;
import com.disney.qa.espn.android.pages.phone.authentication.EspnAddFavoritesPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType.Type;
import org.openqa.selenium.WebDriver;

/**
 * ESPN Tablet EspnTabletAddFavoritesPage
 *
 * @author bzayats
 */
@DeviceType(pageType = Type.ANDROID_TABLET, parentClass = EspnAddFavoritesPageBase.class)
public class EspnTabletAddFavoritesPage extends EspnAddFavoritesPage {

	public EspnTabletAddFavoritesPage(WebDriver driver) {
		super(driver);
	}
}

