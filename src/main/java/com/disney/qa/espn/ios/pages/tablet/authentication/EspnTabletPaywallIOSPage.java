package com.disney.qa.espn.ios.pages.tablet.authentication;

import com.disney.qa.espn.ios.pages.authentication.EspnPaywallIOSPageBase;
import com.disney.qa.espn.ios.pages.phone.authentication.EspnPaywallIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnPaywallIOSPageBase.class)
public class EspnTabletPaywallIOSPage extends EspnPaywallIOSPage {


    //Objects

    @FindBy(id = "dismissButton")
    private ExtendedWebElement closeBtn;



    //Methods

    public EspnTabletPaywallIOSPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void dismissPaywall() {
        closeBtn.click(DELAY);
    }
}
