package com.disney.qa.disney.android.pages.phone;

import com.disney.qa.disney.android.pages.common.DisneyPlusSubscriberAgreementPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = DisneyPlusSubscriberAgreementPageBase.class)
public class DisneyPlusSubscriberAgreementPage extends DisneyPlusSubscriberAgreementPageBase {
    public DisneyPlusSubscriberAgreementPage(WebDriver driver) {
        super(driver);
    }
}
