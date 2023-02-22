package com.disney.qa.espn.ios.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class EspnFavoriteTeamsSelectionIOSPageBase extends EspnIOSPageBase {



    //Objects

    private static final String FINISH_BUTTON_ACCESSIBILITY_ID = "button.finish";

    @FindBy(xpath = "//XCUIElementTypeOther[@name='Tap your favorite teams']")
    private ExtendedWebElement favoriteTeamsPageTitle;

    @FindBy(name = "Tap your favorite leagues")
    private ExtendedWebElement backBtn;

    @FindBy(name = "Search")
    private ExtendedWebElement searchField;

    @FindBy(id = "OK")
    private ExtendedWebElement okBtn;





    //Methods

    public EspnFavoriteTeamsSelectionIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public void getHomePage() {
        findExtendedWebElement(MobileBy.AccessibilityId(FINISH_BUTTON_ACCESSIBILITY_ID)).click();
    }


}
