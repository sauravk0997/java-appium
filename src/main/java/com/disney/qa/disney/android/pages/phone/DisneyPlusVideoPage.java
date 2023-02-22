package com.disney.qa.disney.android.pages.phone;


import com.disney.qa.disney.android.pages.common.DisneyPlusVideoPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusVideoPageBase.class)
public class DisneyPlusVideoPage  extends DisneyPlusVideoPageBase{

    public DisneyPlusVideoPage(WebDriver driver){
        super(driver);
    }
}
