package com.disney.qa.espn.android.pages.authentication;

import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.disney.qa.espn.android.pages.home.EspnHomePageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * ESPN - 'Add More Favorites' screen
 *
 * @author bzayats
 */
public abstract class EspnAddMoreFavoritesPageBase extends EspnPageBase {
    @FindBy(id = "com.espn.score_center:id/xToolbarTitleTextView")
    private ExtendedWebElement moreFavoritesPageTitle;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/league_name' and @text='%s']")
    private ExtendedWebElement leagueDynamicName;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/item_text' and @text='%s']")
    private ExtendedWebElement itemDynamicName;

    @FindBy(xpath = "//*[@resource-id='android:id/message' and contains(@text, 'customize the app!')]")
    private ExtendedWebElement addFavoritesCustomizeAlert;

    @FindBy(xpath = "//*[@resource-id='android:id/button1' and @text='OK' or @text='Ok']")
    private ExtendedWebElement addFavoritesCustomizeAlertOkBtn;

    @FindBy(id = "nextText")
    private ExtendedWebElement nextBtn;

    public EspnAddMoreFavoritesPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened(){
        return moreFavoritesPageTitle.isElementPresent();
    }

    /** ESPN - open HOME screen **/
    public EspnHomePageBase openHomeScreen(){
        nextBtn.clickIfPresent();
        clearCustomizeFavoritesAlert();

        return initPage(EspnHomePageBase.class);
    }

    /** ESPN - select favorites **/
    public void selectFavorites(String league, String ... favorites){
        leagueDynamicName.format(league).clickIfPresent();

        for (String str : favorites){
            itemDynamicName.format(str).clickIfPresent();
        }
    }

    /** ESPN - clear customize favorites alert **/
    public void clearCustomizeFavoritesAlert(){
        if (addFavoritesCustomizeAlert.isElementPresent(DELAY)){
            LOGGER.info("Customize alert found..");
            addFavoritesCustomizeAlertOkBtn.clickIfPresent();
        }
    }
}

