package com.disney.qa.common.google_play_store.android.pages.tablet;

import com.disney.qa.common.google_play_store.android.pages.common.GooglePlayHandler;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = GooglePlayHandler.class)
public class GooglePlayTabletHandler extends GooglePlayHandler {
    public GooglePlayTabletHandler(WebDriver driver){
        super(driver);
    }
}
