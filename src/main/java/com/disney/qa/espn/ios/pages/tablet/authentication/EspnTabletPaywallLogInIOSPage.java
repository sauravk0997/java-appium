package com.disney.qa.espn.ios.pages.tablet.authentication;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.espn.ios.pages.authentication.EspnPaywallLogInIOSPageBase;
import com.disney.qa.espn.ios.pages.phone.authentication.EspnPaywallLogInIOSPage;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.events.EventFiringWebDriver;


@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = EspnPaywallLogInIOSPageBase.class)
public class EspnTabletPaywallLogInIOSPage extends EspnPaywallLogInIOSPage {


    //Objects

    @FindBy(id = "//XCUIElementTypeButton[@name='Log In']")
    private ExtendedWebElement loginBtn;



    //Methods

    public EspnTabletPaywallLogInIOSPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void pressLogInField() {
        LOGGER.info("Using Tablet co-ordinates to find Log In field");
        new IOSUtils().screenPress(2, 4);
    }

    @Override
    public void pressPasswordField() {
        LOGGER.info("Using Tablet co-ordinates to find Password field");
        new IOSUtils().screenPress(2, 3);
    }

    @Override
    public void pressLogInBtn() {
        LOGGER.info("Using Tablet co-ordinates to find Log In button");
        TouchAction touchAction = new TouchAction((IOSDriver<?>) ((EventFiringWebDriver) getDriver()).getWrappedDriver());
        touchAction.longPress(new PointOption().withCoordinates(385, 377)).waitAction().release().perform();
    }

}
