package com.disney.qa.espn.android.pages.home;

import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.disney.qa.espn.android.pages.settings.EspnSettingsPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * ESPN - Home page
 *
 * @author bzayats
 */
public abstract class EspnHomePageBase extends EspnPageBase {

    //ToolBar > Setting
    @FindBy(id = "com.espn.score_center:id/menu_settings")
    private ExtendedWebElement toolBarSettingsBtn;

    @FindBy(xpath = "//*[@content-desc='Search']")
    private ExtendedWebElement toolBarSearchBtn;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/clubhouse_toolbar']//android.widget.ImageView")
    private ExtendedWebElement toolBarLogoImage;

    //Media player + Top HeadLines
    @FindBy(xpath = "//*[@class='android.support.v7.widget.RecyclerView']")
    private ExtendedWebElement homeScrollableContainer;

    @FindBy(id = "com.espn.score_center:id/exo_content_frame")
    private ExtendedWebElement homeMediaPlayer;

    @FindBy(id = "com.espn.score_center:id/xIconViewImage")
    private ExtendedWebElement homeMediaPlayerBottomSectionLogo;

    @FindBy(id = "com.espn.score_center:id/xArticleHeroTitleTextView")
    private ExtendedWebElement homeMediaPlayerBottomSectionText;

    @FindBy(id = "com.espn.score_center:id/xCommonHeaderTitleTextView")
    private ExtendedWebElement topHeadlineTextHeader;

    @FindBy(id = "com.espn.score_center:id/favorite_header_text")
    private ExtendedWebElement topHeadlinesListItemText;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/favorite_header_text'][%s]")
    private ExtendedWebElement topHeadlinesListDynamicItemTextByIndex;

    public EspnHomePageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return toolBarLogoImage.isElementPresent(DELAY);
    }

    /** ESPN > HOME > launch Settings page **/
    public EspnSettingsPageBase openSettingsPage() {
        toolBarSettingsBtn.clickIfPresent();

        return initPage(EspnSettingsPageBase.class);
    }
}

