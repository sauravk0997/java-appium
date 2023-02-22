package com.disney.qa.espn.android.pages.espn;

import com.disney.qa.espn.android.pages.watch.EspnWatchPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * ESPN - ESPN+ page
 *
 * @author bzayats
 */
public abstract class EspnPlusPageBase extends EspnWatchPageBase {

    //ToolBar > Setting
    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/clubhouse_toolbar']/android.widget.ImageView")
    private ExtendedWebElement toolBarLogoImage;

    public EspnPlusPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return toolBarLogoImage.isElementPresent();
    }
}

