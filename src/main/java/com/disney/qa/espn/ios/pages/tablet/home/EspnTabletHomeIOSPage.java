package com.disney.qa.espn.ios.pages.tablet.home;

import com.disney.qa.espn.ios.pages.home.EspnHomeIOSPageBase;
import com.disney.qa.espn.ios.pages.phone.home.EspnHomeIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.annotations.Predicate;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnHomeIOSPageBase.class)
public class EspnTabletHomeIOSPage extends EspnHomeIOSPage {

    //Objects

    @FindBy(xpath = "type == 'XCUIElementTypeNavigationBar' AND name == 'SCiPadClubhouseView'")
    @Predicate
    private ExtendedWebElement homeLogo;


    //Methods

    public EspnTabletHomeIOSPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return homeLogo.isElementPresent(DELAY);
    }

}
