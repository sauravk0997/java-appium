package com.disney.qa.common.google_play_store.android.pages.phone;

import com.disney.qa.common.google_play_store.android.pages.common.GooglePlayHandler;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = GooglePlayHandler.class)
public class GooglePlayHandsetHandler extends GooglePlayHandler{
    public GooglePlayHandsetHandler(WebDriver driver){
        super(driver);
    }
}
