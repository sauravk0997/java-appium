package com.disney.qa.espn.ios.pages.common;


import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.annotations.AccessibilityId;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class EspnEditionIOSPageBase extends EspnIOSPageBase {


    //Objects

    @FindBy(name = "Next")
    @AccessibilityId
    private ExtendedWebElement nextBtn;




    //Methods

    public EspnEditionIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public EspnFavoriteLeagueSelectionIOSPageBase getFavoriteLeaguePage() {
        nextBtn.clickIfPresent(SHORT_TIMEOUT);
        return initPage(EspnFavoriteLeagueSelectionIOSPageBase.class);
    }

}
