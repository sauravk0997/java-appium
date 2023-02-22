package com.disney.qa.disney.android.pages.tablet;

import com.disney.qa.disney.android.pages.phone.DisneyPlusSubscriberAgreementPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TABLET, parentClass = DisneyPlusSubscriberAgreementPage.class)
public class DisneyPlusTabletSubscriberAgreementPage extends DisneyPlusSubscriberAgreementPage {
    public DisneyPlusTabletSubscriberAgreementPage(WebDriver driver) {
        super(driver);
    }
}
