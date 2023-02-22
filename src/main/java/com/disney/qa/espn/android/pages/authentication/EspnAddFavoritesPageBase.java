package com.disney.qa.espn.android.pages.authentication;

import com.disney.qa.espn.android.pages.common.EspnCommonPageBase;
import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * ESPN - 'Add Favorites' screen
 *
 * @author bzayats
 */
public abstract class EspnAddFavoritesPageBase extends EspnPageBase {
    @FindBy(id = "com.espn.score_center:id/xToolbarTitleTextView")
    private ExtendedWebElement favoritesPageTitle;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/item_text' and @text='%s']")
    private ExtendedWebElement itemDynamicName;

    @FindBy(id = "nextText")
    private ExtendedWebElement nextBtn;

    public EspnAddFavoritesPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened(){
        return favoritesPageTitle.isElementPresent();
    }

    /** ESPN - open Any more Favorites screen **/
    public EspnAddMoreFavoritesPageBase openAnyMoreFavoritesScreen(){
        nextBtn.clickIfPresent();

        return initPage(EspnAddMoreFavoritesPageBase.class);
    }

    /** ESPN - select favorites **/
    public void selectFavorites(String ... favorites){
        for (String str : favorites){
            itemDynamicName.format(str).clickIfPresent();
        }
    }

    /** ESPN - clear 'Save password to Autofill with Google?' OS dialog **/
    public boolean closeSavePasswordToAutofillWithGoogleDialog(){
        initPage(EspnCommonPageBase.class).clickCoords(1007.00, 1551.00);

        return nextBtn.isElementPresent(DELAY);
    }
}

