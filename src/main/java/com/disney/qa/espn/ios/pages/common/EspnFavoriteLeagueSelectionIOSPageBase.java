package com.disney.qa.espn.ios.pages.common;


import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class EspnFavoriteLeagueSelectionIOSPageBase extends EspnIOSPageBase {


    //Objects

    private static final String SKIP_BTN_ACCESSIBILITY_ID = "button.finish";

    @FindBy(xpath = "//XCUIElementTypeOther[@name='Tap your favorite leagues']")
    private ExtendedWebElement favoriteLeaguePageTitle;

    @FindBy(xpath = "//XCUIElementTypeCollectionView/XCUIElementTypeCell")
    private List<ExtendedWebElement> favoriteLeagueLise;

    @FindBy(name = "%s")
    private ExtendedWebElement dynamicLeagueBtn;



    //Methods

    public EspnFavoriteLeagueSelectionIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public EspnFavoriteTeamsSelectionIOSPageBase getFavoriteTeamsSelectionPage() {
        findExtendedWebElement(MobileBy.AccessibilityId(SKIP_BTN_ACCESSIBILITY_ID)).click();
        return initPage(EspnFavoriteTeamsSelectionIOSPageBase.class);
    }
}