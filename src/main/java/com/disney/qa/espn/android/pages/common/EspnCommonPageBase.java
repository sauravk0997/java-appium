package com.disney.qa.espn.android.pages.common;

import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.espn.android.pages.home.EspnHomePageBase;
import com.disney.qa.espn.android.pages.media.EspnVideoPageBase;
import com.disney.qa.espn.android.pages.watch.EspnWatchPageBase;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.utils.resources.L10N;
import com.qaprosoft.carina.core.foundation.webdriver.DriverHelper;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.MobileDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * ESPN - Common page
 *
 * @author bzayats
 */
public abstract class EspnCommonPageBase extends DisneyAbstractPage implements IMobileUtils {
    protected static final int MINIMAL_TIMEOUT = 1;

    //Toolbar
    @FindBy(xpath = "//*[contains(@resource-id,'oolbar')]/android.widget.ImageButton")
    protected ExtendedWebElement menu;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/xToolbarTitleTextView']")
    private ExtendedWebElement title;

    @FindBy(id = "com.espn.score_center:id/toolbar_logo")
    private ExtendedWebElement toolbarLogo;

    //Footer bar
    @FindBy(xpath = "//*[contains(@resource-id,'Label') and @text='%s']")
    private ExtendedWebElement footerDynamicTab;

    //Enable location services page
    @FindBy(xpath = "//android.widget.Button[@resource-id='com.android.packageinstaller:id/permission_allow_button']")
    private ExtendedWebElement enableLocationServicesBtn;

    //webviews
    @FindBy(xpath = "//*[contains(@text, '%s')]")
    private ExtendedWebElement webViewDynamicText;

    @FindBy(xpath = "//android.widget.ImageButton[contains(@content-desc, 'Navigate')]")
    private ExtendedWebElement navigateUpButton;

    @FindBy(id = "android:id/button1")
    private ExtendedWebElement messagesOkBtn;

    //alerts
    @FindBy(id = "com.android.packageinstaller:id/perm_desc_root")
    private ExtendedWebElement accessToPhotosMediaFilesAlert;

    @FindBy(id = "com.android.packageinstaller:id/permission_allow_button")
    private ExtendedWebElement alertAllowBtn;

    @FindBy(id = "com.android.packageinstaller:id/permission_deny_button")
    private ExtendedWebElement alertDenyBtn;

    @FindBy(id = "com.android.packageinstaller:id/permission_message")
    private ExtendedWebElement accessToDevicesLocationAlert;

    //Search
    @FindBy(xpath = "//*[@content-desc='Search']")
    private ExtendedWebElement toolBarSearchBtn;

    @FindBy(id = "search_src_text")
    private ExtendedWebElement searchInputField;

    @FindBy(id = "com.espn.score_center:id/sports_list_header_text")
    private ExtendedWebElement searchResultsListHeaderText;

    @FindBy(xpath = "//*[contains(@resource-id,'watch_cell_header_text') and @text='%s']")
    private ExtendedWebElement searchResultsListItemDynamicTitleText;

    public EspnCommonPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return title.isElementPresent(DriverHelper.EXPLICIT_TIMEOUT / 2);
    }

    // Leagues
    public enum Leagues {
        NA_LCS("NA LCS"),
        WORLD_CHAMPIONSHIP("WORLD CHAMPIONSHIP"),
        EU_LCS("EU LCS"),
        ALL_STAR_EVENT("All STAR EVENT"),
        NA_SCOUTING_GROUNDS("NA SCOUTING GROUNDS"),
        INTERNATIONAL_WILDCARD("INTERNATIONAL WILDCARD"),
        DEFAULT("NA LCS");

        private String title;

        Leagues(String title) {
            this.title = title;
        }

        public String getText() {
            String title = this.title;
            if (title.contains("{L10N:")) {
                String key = title.replace("{L10N:", "").replace("}", "");
                title = L10N.getText(key);
            }
            return title;
        }
    }

    // Leagues
    public enum FooterTabs {
        HOME_TAB("Home"),
        SCORES_TAB("Watch"),
        WATCH_TAB("Watch"),
        ESPN_TAB("ESPN+"),
        MORE_OPTION("More");

        private String footerTab;

        FooterTabs(String tab) {
            this.footerTab = tab;
        }

        public String getText() {
            String tab = this.footerTab;
            if (tab.contains("{L10N:")) {
                String key = tab.replace("{L10N:", "").replace("}", "");
                tab = L10N.getText(key);
            }
            return tab;
        }
    }


    /**
     * @param - String > content title to search for
     **/
    public boolean launchContentForQE(String content) {
        boolean res = false;

        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);
        AndroidUtilsExtended util = new AndroidUtilsExtended();

        toolBarSearchBtn.click();
        searchInputField.type(content);
        util.pressBack();
        if (util.swipe(searchResultsListItemDynamicTitleText.format(content))) {
            searchResultsListItemDynamicTitleText.format(content).click();
            if (videoPageBase.isMediaPlayerPresentQE(60L)) {
                videoPageBase.waitTillProgressBarNotPresent();
                pause(60); //waiting for playback
                clickNavUpBtn();
                res = true;
            }
        }

        return res;
    }

    /**
     * @param - index represents dynamic index of desired tab, 1 for HOME, 2 for Schedule, 3 for OnDemand, 4 for Standings
     **/
    public boolean isFooterTabEnabled(String tab) {

        return footerDynamicTab.format(tab).isElementPresent();
    }

    public String getPageHeaderText(){

        return title.getText();
    }

    /**
     * clickNavUp
     */
    public void clickNavUpBtn() {
        LOGGER.info("Try to click NavUp button.");
        if (!navigateUpButton.clickIfPresent(SHORT_TIMEOUT)) {
            LOGGER.error("There is no NavUp button. Try to use phone back navigation.");
            getDriver().navigate().back();
        }
    }

    /**
     * Navigation > open desired page
     *
     * @return desired page
     */
    public <T> T navigateToPage(String desiredPage) {
        switch (desiredPage) {
            case "Home":
                if (footerDynamicTab.format(FooterTabs.HOME_TAB.getText()).isElementPresent()) {
                    footerDynamicTab.format(FooterTabs.HOME_TAB.getText()).clickIfPresent();
                }

                return (T) initPage(EspnHomePageBase.class);

            case "Watch":
                if (footerDynamicTab.format(FooterTabs.WATCH_TAB.getText()).isElementPresent()) {
                    footerDynamicTab.format(FooterTabs.WATCH_TAB.getText()).clickIfPresent();
                }

                return (T) initPage(EspnWatchPageBase.class);

            case "ESPN+":
                if (footerDynamicTab.format(FooterTabs.ESPN_TAB.getText()).isElementPresent()) {
                    footerDynamicTab.format(FooterTabs.ESPN_TAB.getText()).clickIfPresent();
                }

                return (T) initPage(EspnWatchPageBase.class);

            default:
                LOGGER.info("Please provide valid option for the desired page..");

        }

        return null;

    }

    public boolean handleAccessToMediaAlert(boolean allow) {
        boolean res = false;

        if (accessToPhotosMediaFilesAlert.isElementPresent(DELAY)) {
            if (allow) {
                alertAllowBtn.clickIfPresent(SHORT_TIMEOUT);
                res = true;
            } else {
                alertDenyBtn.clickIfPresent(SHORT_TIMEOUT);
            }
        }

        return res;
    }

    public boolean handleLocationAlert(boolean allow) {
        boolean res = false;

        if (accessToDevicesLocationAlert.isElementPresent(DELAY)) {
            if (allow) {
                alertAllowBtn.clickIfPresent(SHORT_TIMEOUT);
                res = true;
            } else {
                alertDenyBtn.clickIfPresent(SHORT_TIMEOUT);
            }
        }

        return res;
    }

    /** Any sub-page webView > check for presence of text **/
    public boolean isWebViewTextPresent(String text){

        if (webViewDynamicText.format(text).isElementPresent()){
            return true;
        }else{
            return webViewDynamicText.format(text.toUpperCase()).isElementPresent();
        }
    }

    /** click coords **/
    public void clickCoords(double xCoor, double yCoor){
        Dimension scrSize;
        scrSize = getDriver().manage().window().getSize();

        int x = (int) (scrSize.width / xCoor);
        int y = (int) (scrSize.height / yCoor);

        LOGGER.info("Clicking desired coords.. ");
        tap(x, y);
    }
    /** Overload to get around Appium stream interruption error in Core **/
    public void clickCoords(Dimension scrSize, double xCoor, double yCoor){
        int x = (int) (scrSize.width / xCoor);
        int y = (int) (scrSize.height / yCoor);

        tap(x, y);
    }

    /** drag to coords **/
    public void dragToCoords(int xEndPos){
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);

        int startX = videoPageBase.getMediaPlayerSeekBar().getElement().getLocation().getX();
        int startY = videoPageBase.getMediaPlayerSeekBar().getElement().getLocation().getY();
        int endX = xEndPos + startX;

		TouchAction<?> touchAction = new TouchAction((MobileDriver<?>) getDriver());
		PointOption<?> startPoint = PointOption.point(startX, startY);
		PointOption<?> endPoint = PointOption.point(endX, startY);

        LongPressOptions options = LongPressOptions.longPressOptions().withPosition(startPoint);
        touchAction.longPress(options).moveTo(endPoint).release().perform();
    }

    /** waits until element is NOT present **/
    public void waitUntilNotPresent(ExtendedWebElement ele){
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(
                ele.getBy()), 20);
    }

    /** waits until element is visible **/
    public void waitUntilVisible(ExtendedWebElement ele){
        waitUntil(ExpectedConditions.visibilityOfElementLocated(
                ele.getBy()), 20);
    }

    /** ESPN > get Navigate back btn **/
    public ExtendedWebElement getNavigateUpButton() {
        return navigateUpButton;
    }
}
