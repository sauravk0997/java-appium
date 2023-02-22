package com.disney.qa.espn.android.pages.watch;

import com.disney.qa.espn.android.pages.common.EspnCommonPageBase;
import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * ESPN - Watch > See All Page
 *
 * @author bzayats
 */
public abstract class EspnWatchSeeAllPageBase extends EspnPageBase {

    //ToolBar > Setting
    @FindBy(id = "com.espn.score_center:id/xToolbarTitleTextView")
    private ExtendedWebElement toolBarTitle;

    @FindBy(id = "com.espn.score_center:id/menu_settings")
    private ExtendedWebElement toolBarSettingsBtn;

    @FindBy(xpath = "//*[@content-desc='Search']")
    private ExtendedWebElement toolBarSearchBtn;

    @FindBy(xpath = "//*[@content-desc='Schedule']")
    private ExtendedWebElement toolBarScheduleBtn;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/clubhouse_toolbar']/android.widget.ImageView")
    private ExtendedWebElement toolBarLogoImage;

    //Content Scrollable Container
    @FindBy(id = "com.espn.score_center:id/recycler_view")
    private ExtendedWebElement contentScrollableContainer;

    @FindBy(id = "com.espn.score_center:id/root")
    private ExtendedWebElement contentCell;

    //Content Cell Container elements
    @FindBy(id = "com.espn.score_center:id/image_view")
    private ExtendedWebElement contentCellImage;

    @FindBy(id = "com.espn.score_center:id/xBugTextView")
    private ExtendedWebElement contentCellVideoDurationTimeText;

    @FindBy(id = "com.espn.score_center:id/header_text_view")
    private ExtendedWebElement contentCellScheduleAndTitleText;

    @FindBy(id = "com.espn.score_center:id/subheader_text_view")
    private ExtendedWebElement contentCellChannelAndSubscriptionText;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/header_text_view' and @text='%s']")
    private ExtendedWebElement contentCellDynamicScheduleAndTitleText;

    public EspnWatchSeeAllPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return initPage(EspnCommonPageBase.class).isOpened();
    }

    /** ESPN > WATCH > check for Schedule Btn on 'See All' page **/
    public boolean isScheduleBtnPresent(){

        return toolBarScheduleBtn.isElementPresent();
    }

}

