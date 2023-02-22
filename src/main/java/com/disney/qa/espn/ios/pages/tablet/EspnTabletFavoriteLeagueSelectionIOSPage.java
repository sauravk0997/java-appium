package com.disney.qa.espn.ios.pages.tablet;

import com.disney.qa.espn.ios.pages.common.EspnFavoriteLeagueSelectionIOSPageBase;
import com.disney.qa.espn.ios.pages.phone.EspnFavoriteLeagueSelectionIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnFavoriteLeagueSelectionIOSPageBase.class)
public class EspnTabletFavoriteLeagueSelectionIOSPage extends EspnFavoriteLeagueSelectionIOSPage {
    public EspnTabletFavoriteLeagueSelectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
