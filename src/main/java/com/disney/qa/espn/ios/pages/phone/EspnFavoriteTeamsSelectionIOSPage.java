package com.disney.qa.espn.ios.pages.phone;

import com.disney.qa.espn.ios.pages.common.EspnFavoriteTeamsSelectionIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = EspnFavoriteTeamsSelectionIOSPageBase.class)
public class EspnFavoriteTeamsSelectionIOSPage extends EspnFavoriteTeamsSelectionIOSPageBase {
    public EspnFavoriteTeamsSelectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
