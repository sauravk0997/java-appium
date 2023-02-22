package com.disney.qa.espn.android.pages.watch;

import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.espn.android.pages.common.EspnCommonPageBase;
import com.disney.qa.espn.android.pages.common.EspnPageBase;
import com.disney.qa.espn.android.pages.media.EspnVideoPageBase;
import com.disney.qa.espn.android.pages.paywall.EspnEpaywallPageBase;
import com.disney.qa.espn.android.pages.settings.EspnSettingsPageBase;
import com.qaprosoft.carina.core.foundation.utils.android.IAndroidUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.SkipException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ESPN - Home page
 *
 * @author bzayats
 */
public abstract class EspnWatchPageBase extends EspnPageBase implements IAndroidUtils {

    //ToolBar > Setting
    @FindBy(id = "com.espn.score_center:id/menu_settings")
    private ExtendedWebElement toolBarSettingsBtn;

    @FindBy(xpath = "//*[@content-desc='Search']")
    private ExtendedWebElement toolBarSearchBtn;

    @FindBy(xpath = "//*[@content-desc='Schedule']")
    private ExtendedWebElement toolBarScheduleBtn;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/clubhouse_toolbar']/android.widget.ImageView")
    private ExtendedWebElement toolBarLogoImage;

    @FindBy(xpath = "//*[@content-desc='More options")
    private ExtendedWebElement toolBarOverFlowMenu;

    @FindBy(xpath = "//*[@content-desc='Schedule")
    private ExtendedWebElement toolBarOverFlowMenuScheduleBtn;

    @FindBy(xpath = "//*[@content-desc='Schedule")
    private ExtendedWebElement toolBarOverFlowMenuSettingsBtn;

    //Watch Tabs
    @FindBy(xpath = "//*[@class='android.support.v7.app.ActionBar$Tab']//*[contains(@text, '%s')]")
    private ExtendedWebElement watchDynamicTab;

    //Content Section Header
    @FindBy(id = "com.espn.score_center:id/section_title_text_view")
    private ExtendedWebElement espnContentListSectionItemText;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/section_title_text_view']/../" +
            "*[@resource-id='com.espn.score_center:id/show_all_button']")
    private ExtendedWebElement espnContentListSectionItemTextShowAllBtn;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/section_title_text_view' and @text='%s']")
    private ExtendedWebElement espnContentListSectionItemDynamicText;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/section_title_text_view' and @text=\"%s\" or @text=\"%s\"]/../" +
            "*[@resource-id='com.espn.score_center:id/show_all_button']")
    private ExtendedWebElement espnContentListSectionItemDynamicTextShowAllBtn;

    //Section container elements
    @FindBy(id = "com.espn.score_center:id/xSmallCarouselRecyclerView")
    private ExtendedWebElement espnContentSectionHorizontalListContainer;

    @FindBy(id = "com.espn.score_center:id/image_view")
    private ExtendedWebElement espnContentListSectionItemImage;

    @FindBy(id = "com.espn.score_center:id/xBugTextView")
    private ExtendedWebElement espnContentListSectionItemBugText;

    @FindBy(id = "com.espn.score_center:id/header_text_view")
    private ExtendedWebElement espnContentListSectionItemHeaderText;

    @FindBy(id = "com.espn.score_center:id/subheader_text_view")
    private ExtendedWebElement espnContentListSectionItemSubHeaderText;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/subheader_text_view' and @text='%s']/..//" +
            "*[@resource-id='com.espn.score_center:id/image_view']")
    private ExtendedWebElement espnContentListSectionDynamicItemByPackage;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/header_text_view' and @text=\"%s\"]/..//" +
            "*[@resource-id='com.espn.score_center:id/image_view' or @resource-id='com.espn.score_center:id/image_container_card']")
    private ExtendedWebElement espnContentListSectionDynamicItemByContentText;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/header_text_view' and @text='%s']/..//" +
            "*[@resource-id='com.espn.score_center:id/bug_view']")
    private ExtendedWebElement espnContentListSectionDynamicItemByContentTextAndTypeIndicator;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/header_text_view' and @text='%s']/following-sibling::" +
            "*[@resource-id='com.espn.score_center:id/subheader_text_view']")
    private ExtendedWebElement espnContentListSectionDynamicItemByContentTextAndPackageName;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/section_title_text_view' and @text='%s' or @text='%s']/../following-sibling::" +
            "*[@resource-id='com.espn.score_center:id/xSmallCarouselRecyclerView']")
    private ExtendedWebElement espnContentListSectionDynamicItemCarousel;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/section_title_text_view' and @text='%s' or @text='%s']/../following-sibling::" +
            "*[@resource-id='com.espn.score_center:id/xSmallCarouselRecyclerView']//*[@resource-id='com.espn.score_center:id/header_text_view']")
    private ExtendedWebElement espnContentListSectionDynamicItemHeaderText;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/section_title_text_view' and @text='%s']/../following-sibling::" +
            "*[@resource-id='com.espn.score_center:id/xSmallCarouselRecyclerView']//*[@resource-id='com.espn.score_center:id/subheader_text_view']")
    private ExtendedWebElement espnContentListSectionDynamicItemSubHeaderText;

    //Media player + Top HeadLines
    @FindBy(id = "com.espn.score_center:id/recycler_view")
    private ExtendedWebElement watchScrollableContainer;

    @FindBy(id = "com.espn.score_center:id/video_frame")
    private ExtendedWebElement watchMediaPlayer;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/xBugTextView' and @text='LIVE']")
    private ExtendedWebElement videoFrameLIVEtext;

    //Scoreboard related paths, ONLY visible for specific events in the Hero Carousel. Data driven
    @FindBy(id = "com.espn.score_center:id/game_cell")
    private ExtendedWebElement scoreboardContainer;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/game_cell']/..//" +
            "*[@resource-id='com.espn.score_center:id/image_view' or @resource-id='com.espn.score_center:id/image_container_card']")
    private ExtendedWebElement espnContentImageContainerCardFromScoreboard;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/game_cell']/..//" +
            "*[@resource-id='com.espn.score_center:id/bug_view']")
    private ExtendedWebElement espnContentBugTextFromScoreboard;

    @FindBy(xpath = "//*[@resource-id='com.espn.score_center:id/game_cell']/following-sibling::" +
            "*[@resource-id='com.espn.score_center:id/subheader_text_view']")
    private ExtendedWebElement espnContentSubtextFromScoreboard;

    //MArketing ads
    @FindBy(xpath = "//*[@content-desc='marketing_web_view']")
    private ExtendedWebElement marketingWebView;

    @FindBy(xpath = "//*[@content-desc='close_button']")
    private ExtendedWebElement marketingWebViewCloseBtn;

    @FindBy(xpath = "//android.webkit.WebView")
    private ExtendedWebElement addWebView;

    public EspnWatchPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return initPage(EspnCommonPageBase.class).isOpened();
    }

    /** ESPN > watch tabs Enum **/
    public enum WatchTabs{
        FEATURED("FEATURED"),
        ORIGINALS("ORIGINALS"),
        DEFAULT("FEATURED");

        private String tab;

        WatchTabs(String tab){
            this.tab = tab;
        }

        public String getText(){

            return tab;
        }
    }

    /**
     * ESPN > swipe Watch page list container
     **/
    public void swipeWatchListContainer(int count, Direction direction) {
        for (int i = 0; i < count; ++i) {
            swipeInContainer(watchScrollableContainer, direction, 500);
        }
    }

    /** ESPN > WATCH > set watch tab **/
    public void setWatchTab(String tab) {
        setWatchTab(WatchTabs.valueOf(tab));
    }

    /** ESPN > WATCH > set watch tab **/
    public void setWatchTab(WatchTabs tab) {
        watchDynamicTab.format(tab.getText()).clickIfPresent();
    }

    /** ESPN > WATCH > check if tab is present **/
    public boolean isWatchTabPresent(WatchTabs tab) {

        return watchDynamicTab.format(tab.getText()).isElementPresent();
    }

    /** ESPN > WATCH > get tabs text **/
    public String getWatchTabText(WatchTabs tab) {

        return watchDynamicTab.format(tab.getText()).getText();
    }

    /** ESPN > WATCH > check if tab is selected **/
    public boolean isWatchTabSelected(String tab) {

        return watchDynamicTab.format(WatchTabs.valueOf(tab)).getAttribute("selected").equals("true");
    }

    /** ESPN > WATCH > launch video page **/
    public EspnSettingsPageBase openSettingsPage() {
        if (toolBarSettingsBtn.isElementPresent(5)) {
            toolBarSettingsBtn.clickIfPresent();
        } else {
            toolBarOverFlowMenu.click();
            toolBarOverFlowMenuSettingsBtn.click();
        }

        return initPage(EspnSettingsPageBase.class);
    }

    /** ESPN > WATCH > check for visible of section containers **/
    public boolean checkVisibleSectionContainers(){

        return findExtendedWebElements(espnContentListSectionItemText.getBy()).size() > 1;
    }

    /** ESPN > WATCH > verify all section container elements are present **/
    public boolean checkForSectionContainerElements(){
        List<Boolean> result = new ArrayList<>();
        result.add(espnContentListSectionItemText.isElementPresent());
        result.add(espnContentListSectionItemTextShowAllBtn.isElementPresent());

        return result.contains(false);
    }

    /** ESPN > WATCH > launch main video tile **/
    public EspnVideoPageBase launchMainVideoTile(){
        watchMediaPlayer.clickIfPresent();
        closeMarketingAdView();

        return initPage(EspnVideoPageBase.class);
    }

    /** ESPN > WATCH > check section header title **/
    public boolean isWatchTabSectionHeaderPresent(String sectionHeader, boolean useTextContains){

        if (useTextContains) {

            return scroll(sectionHeader, watchScrollableContainer, SelectorType.ID,
                    SelectorType.TEXT_CONTAINS).isElementPresent();
        }else{

            return scroll(sectionHeader, watchScrollableContainer).isElementPresent();
        }
    }

    /** ESPN > WATCH > check for Schedule Btn on 'See All' page **/
    public boolean isScheduleBtnPresent(){
        espnContentListSectionItemTextShowAllBtn.clickIfPresent();

        return initPage(EspnWatchSeeAllPageBase.class).isScheduleBtnPresent();
    }

    /** ESPN > WATCH > select first content found via ESPN package  **/
    public boolean launchContentItemBySubtitle(String contentTitle, boolean packageName){
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);
        EspnEpaywallPageBase epaywallPageBase = initPage(EspnEpaywallPageBase.class);

        boolean res = false;

        //TODO: Scroller is getting stuck in an infinite up-down loop. This should work as a temp fix until resolved.
        swipe(espnContentListSectionDynamicItemByContentText.format(contentTitle), Direction.UP);
        scroll(contentTitle, watchScrollableContainer).isElementPresent();

        selectSectionContent(contentTitle, packageName);
        closeMarketingAdView();

        //in case user has entitlements for this type of content, video playback should start
        if (videoPageBase.isOpened()){
            res = videoPageBase.isMediaPlayerPresent();
        }

        //in case user does NOT have entitlements for this type of content, E+ paywall should be presented
        if (epaywallPageBase.isOpened()){
            res = epaywallPageBase.isPaywallFreeTrialTextPresent();
        }

        return res;
    }

    /** ESPN > WATCH > verify 'replay' content retains playback position **/
    public boolean verifyPlaybackPositionRetainedForReplayContent(String contentTitle, boolean packageName){
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);

        String currentTime = " ";
        String newCurrentTime = " ";

        Dimension scrSize = getDriver().manage().window().getSize();

        scroll(contentTitle, watchScrollableContainer).isElementPresent();
        selectSectionContent(contentTitle, packageName);
        closeMarketingAdView();

        //in case user has entitlements for this type of content, video playback should start
        //Recon only activates after a minute has passed. Multiple pauses/interactions are required for this to prevent server timeout
        if (videoPageBase.isOpened()){
            pause(30); //waiting for playback
            videoPageBase.activateVideoPlayerControls(scrSize);
            //Needed to reach 60 seconds for recon to work properly
            pause(30);
            videoPageBase.activateVideoPlayerControls(scrSize);
            currentTime = videoPageBase.getMediaPlayerCurrentTime();
            LOGGER.info("Current Time: " + currentTime);
            clickNavUpBtn();
        }

        //re-opening same video
        swipeUp(5000);
        scroll(contentTitle, watchScrollableContainer).isElementPresent();
        selectSectionContent(contentTitle, packageName);
        closeMarketingAdView();

        videoPageBase = initPage(EspnVideoPageBase.class);

        if (videoPageBase.isOpened()){
            //not activating controls since we just need to obtain time from default presence of controls upon video launch
            //Pause is to allow time for video to actually load
            pause(2);
            newCurrentTime = videoPageBase.getMediaPlayerCurrentTime();
            LOGGER.info("New Current Time: " + newCurrentTime);
            clickNavUpBtn();
        }

        return videoPageBase.isTimeDurationWithinThreshold(5, currentTime, newCurrentTime);
    }

    /** ESPN > WATCH > verify 'LIVE' content **/
    public boolean verifyLiveContent(String contentTitle, boolean packageName){
        //TODO: understand why second content in the list is being clicked on
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);

        Dimension d = driver.manage().window().getSize();

        boolean res = false;

        //in case the event is blacked out, skip test
        if(espnContentListSectionItemBugText.getText().equals("BLACKOUT")){
            throw new SkipException("Skipping test as LIVE content is blacked out");
        }

        if(!scoreboardContainer.isElementPresent(DELAY)) {
            LOGGER.info("Scoreboard not displayed. Opening via contentTitle...");
            scroll(contentTitle, watchScrollableContainer).isElementPresent();
            selectSectionContent(contentTitle, packageName);
        }
        else {
            LOGGER.info("Scoreboard present. Clicking on element...");
            scoreboardContainer.click();
        }

        closeMarketingAdView();

        //in case user has entitlements for this type of content, video playback should start
        while(!videoPageBase.isOpened()){
            videoPageBase.activateVideoPlayerControls(d);
            LOGGER.info("Trying to force the video frame to be visible");
        }
        if (videoPageBase.isOpened()){
            //DO NOT delete commented out code > removing progressBar check for now due to DRM limitations
//            videoPageBase.waitTillProgressBarNotPresent();
            pause(20); //waiting for playback
//            res = videoPageBase.isLiveIndicatorPresent(d);
            res = videoPageBase.getMediaPlayerSeekBar().isElementPresent(DELAY);
            clickNavUpBtn();
        }

        return res;
    }

    /** ESPN > WATCH > verify 'LIVE' content **/
    public boolean verifyLiveContentForQE(String contentTitle, boolean packageName){
        EspnVideoPageBase videoPageBase = initPage(EspnVideoPageBase.class);

        boolean res = false;

        //in case the event is blacked out, skip test
        if(espnContentListSectionItemBugText.getText().equals("BLACKOUT")){
            throw new SkipException("Skipping test as LIVE content is blacked out");
        }

        if(!scoreboardContainer.isElementPresent(DELAY)) {
            LOGGER.info("Scoreboard not displayed. Opening via contentTitle...");
            scroll(contentTitle, watchScrollableContainer).isElementPresent();
            selectSectionContent(contentTitle, packageName);
        }
        else {
            LOGGER.info("Scoreboard present. Clicking on element...");
            scoreboardContainer.click();
        }
        closeMarketingAdView();

        if (videoPageBase.isOpened()){
            videoPageBase.waitTillProgressBarNotPresent();
            pause(60); //waiting for playback
            clickNavUpBtn();
            res = true;
        }

        return res;
    }

    /** ESPN > WATCH > verify content metadata **/
    public boolean verifyContentMetadata(String type, String contentTitle){
        List<Boolean> res = new LinkedList<>();

        switch (type){
            case "live":
                if(scoreboardContainer.isElementPresent(DELAY)){
                    res.add(espnContentImageContainerCardFromScoreboard.isElementPresent());
                    res.add(espnContentBugTextFromScoreboard.isElementPresent());
                    res.add(espnContentSubtextFromScoreboard.isElementPresent());
                }
                else {
                    res.add(espnContentListSectionDynamicItemByContentText.format(contentTitle).isElementPresent());
                    res.add(espnContentListSectionDynamicItemByContentTextAndPackageName.format(contentTitle).isElementPresent());
                    res.add(espnContentListSectionDynamicItemByContentTextAndTypeIndicator.format(contentTitle).isElementPresent());
                }
                break;

            case "vod":
                scroll(contentTitle, watchScrollableContainer).isElementPresent();
                res.add(espnContentListSectionDynamicItemByContentText.format(contentTitle).isElementPresent());
                res.add(espnContentListSectionDynamicItemByContentTextAndPackageName.format(contentTitle).isElementPresent());
                break;

            case "nonMetaData":
                res.add(espnContentListSectionDynamicItemHeaderText.format(contentTitle.toUpperCase(), contentTitle).isElementPresent());
                res.add(espnContentListSectionDynamicItemSubHeaderText.format(contentTitle.toUpperCase(), contentTitle).isElementPresent());
                break;

            default:
                LOGGER.info("Given content type provided not recognized, please provide correct type");
        }

        return res.contains(false);
    }

    /** ESPN > WATCH > select content based on text/packageName **/
    public void selectSectionContent(String contentTitle, boolean packageName){
        LOGGER.info("Content to use: " + contentTitle);
        if (packageName) {
            espnContentListSectionDynamicItemByPackage.format(contentTitle).clickIfPresent();
        }else{
            LOGGER.info("Clicking...");
            espnContentListSectionDynamicItemByContentText.format(contentTitle).click();
            LOGGER.info("Clicked");
        }
        //handling marketing add
        closeMarketingAdView();
    }

    /** ESPN > WATCH > ESPN+ > return visible section tile titles **/
    public List<String> getVisibleSectionTileTitles(String sectionHeaderTitle, Direction direction, int count, boolean swipeInCarousel){

        //TODO Need to try and figure out why it's not working with default scroll
        boolean isScrolled = scroll(sectionHeaderTitle, watchScrollableContainer, SelectorType.ID,
                SelectorType.TEXT_CONTAINS).isElementPresent();
        List<String> tileTitles = new LinkedList<>();

        if (isScrolled){
            if(!espnContentListSectionDynamicItemHeaderText.format(sectionHeaderTitle.toUpperCase(), sectionHeaderTitle).isElementPresent()){
                swipeUp(500);
            }
            tileTitles.add(espnContentListSectionDynamicItemHeaderText.format(sectionHeaderTitle.toUpperCase(), sectionHeaderTitle).getText());
            LOGGER.info("Found Title: " + tileTitles);
        }

        if (isScrolled && swipeInCarousel) {
            swipeInContainer(espnContentListSectionDynamicItemCarousel.format(sectionHeaderTitle.toUpperCase(), sectionHeaderTitle), direction, count,500);
        }

        return tileTitles;
    }

    /** ESPN > WATCH > ESPN+ > check for "See All" option for specified sectin title **/
    public boolean checkSeeAllBtnPresent(String sectionHeaderTitle){
        //TODO Need to try and figure out why it's not working with TEXT_CONTAINS
        AndroidUtilsExtended androidUtilsExtended = new AndroidUtilsExtended();
        androidUtilsExtended.scroll(sectionHeaderTitle, watchScrollableContainer, AndroidUtilsExtended.SelectorType.ID,
                AndroidUtilsExtended.SelectorType.TEXT_CONTAINS);

        return espnContentListSectionItemDynamicTextShowAllBtn.format(sectionHeaderTitle.toUpperCase(), sectionHeaderTitle).isElementPresent(DELAY);
    }

    /** ESPN > WATCH > ESPN+ > close marketing web viwe **/
    public void closeMarketingAdView(){
        if (addWebView.isElementPresent()){
//            marketingWebViewCloseBtn.click();
            new AndroidUtilsExtended().pressBack();
        }
    }
}

