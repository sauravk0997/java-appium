package com.disney.qa.espn.ios.pages.phone;

import com.disney.qa.espn.ios.pages.common.EspnFavoriteLeagueSelectionIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnFavoriteLeagueSelectionIOSPageBase.class)
public class EspnFavoriteLeagueSelectionIOSPage extends EspnFavoriteLeagueSelectionIOSPageBase{
    public EspnFavoriteLeagueSelectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
