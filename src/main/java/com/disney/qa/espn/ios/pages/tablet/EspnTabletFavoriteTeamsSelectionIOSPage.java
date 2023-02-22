package com.disney.qa.espn.ios.pages.tablet;

import com.disney.qa.espn.ios.pages.common.EspnFavoriteTeamsSelectionIOSPageBase;
import com.disney.qa.espn.ios.pages.phone.EspnFavoriteTeamsSelectionIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnFavoriteTeamsSelectionIOSPageBase.class)
public class EspnTabletFavoriteTeamsSelectionIOSPage extends EspnFavoriteTeamsSelectionIOSPage {
    public EspnTabletFavoriteTeamsSelectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
