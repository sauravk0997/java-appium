package com.disney.qa.espn.web;

import com.disney.qa.common.web.SeleniumUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.gui.AbstractPage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.List;




public class EspnBasePage extends AbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected static final int LONG_TIMEOUT = 60;
    private String espnPlusMenuOption = "ESPN+";
    private static final String SKIP_EXCEPTION = "Fight Night URL not available! Skipping this test case";

    @FindBy(linkText = "ESPN+")
    private ExtendedWebElement espnPlusMenu;

    @FindBy(css = "header[id='global-header']")
    private ExtendedWebElement homepageHeader;

    @FindBy(css = "div.watch-home")
    private ExtendedWebElement homePageLoad;

    //Events Player
    @FindBy(css = "span[class='see-all']")
    private List<ExtendedWebElement> seeAllUpcomingEventsButton;

    @FindBy(css = "div[id='tabLive']>p>span")
    private ExtendedWebElement noLiveEventIndicator;

    @FindBy(css = "a[data-status='live']")
    private ExtendedWebElement liveNowEventsButton;

    @FindBy(css = "td.schedule__competitors")
    private ExtendedWebElement liveEventsPresent;

    @FindBy(css = "td[class='schedule__competitors']>div>a")
    private List<ExtendedWebElement> liveEventVideo;

    @FindBy(css = "a[href=\"/watch/collections/5525/event-replays\"]")
    private ExtendedWebElement recentReplaysSeeAll;

    @FindBy(css = "a[href='/watch/collections/7145/latest-episodes")
    private ExtendedWebElement latestEpisodesAll;

    @FindBy(css = "article[class='collection_item collection_item--one']")
    private List<ExtendedWebElement> openLatestEpisode;

    @FindBy(css = "article[class='collection_item collection_item--one']")
    private List<ExtendedWebElement> openRecentReplay;

    @FindBy(css = "div[class='watchBroadcast__item']")
    private List<ExtendedWebElement> replayBroadcast;

    @FindBy(css = "img[alt='espn+']")
    private ExtendedWebElement ePlusIdentifier;

    @FindBy(css = "div[class='filters']>span:nth-child(2)>div>button")
    private ExtendedWebElement eventNetworkDropdown;

    @FindBy(xpath = "//li[@class='CarouselSlide relative pointer CarouselSlide--active']")
    private List<ExtendedWebElement> watchWebRedesignrecentReplays;

    @FindBy(xpath = "a[href='/espnplus/collections/5525/event-replays#partitionDtc/true']")
    private List<ExtendedWebElement> eventReplay;

    //Video Player Elements
    //** need to be in iFrame for player controls
    @FindBy(css = "iframe[class='embed-player__iframe']")
    private ExtendedWebElement iframeVideoPlayer;

    @FindBy(css = "div[class='play-pause-btn media-icon']>i")
    private ExtendedWebElement videoPlayButton;

    @FindBy(css = "div[class='play-pause-btn media-icon']>i")
    private ExtendedWebElement videoPauseButton;

    @FindBy(css = "div[class='controls-left-horizontal']>div:nth-child(2)>i")
    private ExtendedWebElement videoPlayPauseStopButton;

    @FindBy(css = "div[class='media-icon volume-control__btn']>i")
    private ExtendedWebElement videoVolumnControl;

    @FindBy(css = "div.volume-control__slider-handle")
    private ExtendedWebElement volumeSlider;

    @FindBy(css = "div.volume-control__slider-range")
    private ExtendedWebElement volumeRange;

    @FindBy(css = "i.media-icon.media-icon--share-video")
    private ExtendedWebElement videoShareButton;

    @FindBy(css = "i.media-icon.media-icon--closed-captioning")
    private ExtendedWebElement videoClosedCaptionButton;

    @FindBy(css = "i.media-icon.media-icon--settings")
    private ExtendedWebElement videoGearSettingsButton;

    @FindBy(css = "li.menu-item.auto-play.option--enabled")
    private ExtendedWebElement videoGearSettingsAutoPlay;

    @FindBy(css = "li.menu-item.closed-caption")
    private ExtendedWebElement videoGearSettingsCC;

    @FindBy(css = "li.menu-item.closed-caption-settings")
    private ExtendedWebElement videoGearSettingsCcSettings;

    @FindBy(css = "i.media-icon.media-icon--fullscreen")
    private ExtendedWebElement videoEnterFullScreenButton;

    @FindBy(css = "i.media-icon.media-icon--exit-fullscreen")
    private ExtendedWebElement videoExitFullScreenButton;

    @FindBy(css = "div.video-controls__scrubber.js-video-controls_scrubber")
    private ExtendedWebElement videoScrubberBar;

    @FindBy(css = "div.video-scrubber__progress.js-video-scrubber__progress")
    private ExtendedWebElement videoScrubberProgress;

    @FindBy(css = "div.video-scrubber__handle.js-video-scrubber__handle")
    private ExtendedWebElement videoScrubberHandle;

    @FindBy(css = "span.time-display-label__current")
    private ExtendedWebElement videoCurrentRunTime;

    @FindBy(css = "span.time-display-label__duration")
    private ExtendedWebElement videoDurationTime;

    @FindBy(xpath = "//div[@class='WatchListingsVideo WatchListingsVideo--VideoContainer']")
    private ExtendedWebElement videoPlayer;

    @FindBy(css = "div.feed-picker-btn.media-icon")
    private ExtendedWebElement videoBroadcastSelectButton;

    @FindBy(css = "ul[class='feed-list']>li:nth-child(2)")
    private ExtendedWebElement videoBroadcastFeedOption;

    @FindBy(css = "li[class='feed-list-item  ']")
    private ExtendedWebElement videoLiveBroadcastFeedOption;

    @FindBy(css = "li[class='feed-list-item selected ']")
    private ExtendedWebElement videoLiveSelectedFeed;

    @FindBy(css = "ul[class='feed-list']>li:nth-child(2)>i")
    private ExtendedWebElement videoSelectedFeed;

    @FindBy(css = "div.live-btn.media-icon")
    private ExtendedWebElement videoLiveIcon;

    @FindBy(css = "div[class='playback__title']")
    private ExtendedWebElement videoTitle;

    //Share Links for Video Player
    @FindBy(css = "a.social-share-link.media-icon.media-icon--facebook")
    private ExtendedWebElement videoFacebookShareLink;

    @FindBy(css = "a.social-share-link.media-icon.media-icon--twitter")
    private ExtendedWebElement videoTwitterShareLink;

    @FindBy(css = "a.social-share-link.media-icon.media-icon--email")
    private ExtendedWebElement videoEmailShareLink;

    @FindBy(css = "button.media-icon.media-icon--cancel")
    private ExtendedWebElement videoExitShare;

    @FindBy(css = "a[id='direct-link-copy']")
    private ExtendedWebElement videoCopyShareLink;

    //Marketing Landing Page
    @FindBy(css = "a[id='subscriptions-link']")
    private ExtendedWebElement landingPageMySubscriptions;

    @FindBy(css = "button[class='exit-button button']")
    private ExtendedWebElement landingPagePopupButton;

    @FindBy(css = "div[class='language-toggle-btn']")
    private ExtendedWebElement landingPageLanguageToggle;

    @FindBy(css = "div[class='language-selection-item']")
    private ExtendedWebElement landingPageLanguageSelect;

    @FindBy(css = "div[class='footer-content-links']")
    private ExtendedWebElement landingPageFooterLinks;

    @FindBy(css = "header[class='header header-sticky']")
    private ExtendedWebElement landingPageStickyHeader;

    @FindBy(css = "a[href='https://secure.web.plus.espn.com/billing/purchase/ESPN_PURCHASE_CMPGN/ESPN_PPV254BNDL_VOCHR/ufcppv254bndlupg?locale=en_US']")
    private ExtendedWebElement ufcPpvStickyHeaderCta;

    @FindBy(css = "a[class='link button-0 cta-button btn-solid btn-yellow btn-xs wysiwyg  ']")
    private ExtendedWebElement landingPageSignUpButton;

    @FindBy(css = "a[class='link button-0 cta-button btn-solid btn-yellow btn-xs wysiwyg ']")
    private ExtendedWebElement landingPagePpvBuyButton;

    @FindBy(css = "img[alt='ESPN+']")
    private ExtendedWebElement espnPlusLogo;

    @FindBy(css = "img[class='upsell-brand-logo']")
    private ExtendedWebElement mlbLandingLogo;

    @FindBy(css = "img[src='https://espn-cannonball-cdn.bamgrid.com/assets/originals/UFC_PPV_254_Logo-3.png']")
    private ExtendedWebElement upsellLogo;

    @FindBy(css = "div[class='footer-content-links']")
    private ExtendedWebElement footerHyperLink;

    @FindBy(css = "div[id='sticky-content-add-on']>div")
    private ExtendedWebElement mlbLandingPageFreeTrial;

    @FindBy(css = "div[class='headings wysiwyg']>h1>span:nth-child(3)")
    private ExtendedWebElement mlbEntHeadingText;

    @FindBy(xpath = "//*[@id='hero-1']/div[2]/div/div[4]/p/span[1]")
    private ExtendedWebElement mlbEntBodyText;

    // Paywall
    @FindBy(xpath = "//div[@class='cta-button-container']")
    private ExtendedWebElement paywallSignUp;

    @FindBy(xpath = "//div[@class='log-in btn center']")
    private ExtendedWebElement paywallLogin;

    @FindBy(css = "div.paywall")
    private ExtendedWebElement paywallMlbTvDisplay;

    @FindBy(css = "iframe[class='embed-player__iframe']")
    private ExtendedWebElement iframePaywall;

    @FindBy(css = "h1.section-header_title")
    private List<ExtendedWebElement> mlbTvSectionHeaders;

    @FindBy(css = "figure[class='collection_item_media']")
    private List<ExtendedWebElement> mlbTvAsset;

    @FindBy(css = "div[class='btn-close icon-font-before icon-close-solid-before']")
    private ExtendedWebElement paywallXButton;

    @FindBy(xpath = "//span[@class='fine-print']")
    private ExtendedWebElement contentPaywall;


    @FindBy (xpath= "//div[@class='Image__Wrapper aspect-ratio--child']" )
    private List<ExtendedWebElement> mLBTVTile;

    @FindBy (xpath= "//ul[@class='StreamingCard__Meta']")
    private ExtendedWebElement mLBTVButton;

    @FindBy (css ="div[class='paywall-brand-logo']")
    private ExtendedWebElement mLBPaywallLogo;

    @FindBy (xpath = "//div[@class='cta-button-container']")
    private ExtendedWebElement mLBSignUp;

    @FindBy( css= "span[class='fine-print']")
    private ExtendedWebElement mLBPrice;




    public EspnBasePage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getHomePageLoad() {
        return homePageLoad;
    }

    //Events Getters
    public ExtendedWebElement getOpenRecentReplay(int i) {
        return openRecentReplay.get(i);
    }

    public ExtendedWebElement geteventReplay(int i) {return eventReplay.get(i);}

    public ExtendedWebElement getOpenLatestEpisode(int i) {
        return openLatestEpisode.get(i);
    }

    public ExtendedWebElement getLiveEventSelect(int l) {
        return liveEventVideo.get(l);
    }

    public ExtendedWebElement getSeeAllLiveEvents(int s) {
        return seeAllUpcomingEventsButton.get(s);
    }


    //Video Player Getters
    public ExtendedWebElement getVideoPlayer() {
        return videoPlayer;
    }

    public ExtendedWebElement getVideoPlayButton() {
        return videoPlayButton;
    }

    public ExtendedWebElement getVideoPauseButton() {
        return videoPauseButton;
    }

    public ExtendedWebElement getVideoPlayPauseStopButton() {
        return videoPlayPauseStopButton;
    }

    public ExtendedWebElement getVideoVolumnControl() {
        return videoVolumnControl;
    }

    public ExtendedWebElement getVideoFacebookShareLink() {
        return videoFacebookShareLink;
    }

    public ExtendedWebElement getVideoTwitterShareLink() {
        return videoTwitterShareLink;
    }

    public ExtendedWebElement getVideoEmailShareLink() {
        return videoEmailShareLink;
    }

    public ExtendedWebElement getVideoGearSettingsAutoPlay() {
        return videoGearSettingsAutoPlay;
    }

    public ExtendedWebElement getVideoExitFullScreenButton() {
        return videoExitFullScreenButton;
    }

    public ExtendedWebElement getVideoEnterFullScreenButton() {
        return videoEnterFullScreenButton;
    }

    public ExtendedWebElement getVideoBroadcastFeedOption() {
        return videoBroadcastFeedOption;
    }

    public ExtendedWebElement getLiveVideoSelectedFeed() {
        return videoLiveSelectedFeed;
    }

    public ExtendedWebElement getVideoSelectedFeed() {
        return videoSelectedFeed;
    }

    public ExtendedWebElement getVideoScrubberProgress() {
        return videoScrubberProgress;
    }

    public ExtendedWebElement getVolumeRange() {
        return volumeRange;
    }

    public ExtendedWebElement getVideoTitle() {
        return videoTitle;
    }

    //Landing Page Getters
    public ExtendedWebElement getEspnPlusLogo() {
        return espnPlusLogo;
    }

    public ExtendedWebElement getMlbLandingLogo() {
        return mlbLandingLogo;
    }

    public ExtendedWebElement getLandingPageMySubscriptions() {
        return landingPageMySubscriptions;
    }

    public ExtendedWebElement getLandingPagePopupButton() { return landingPagePopupButton;}

    public ExtendedWebElement getLandingPageFooterLinks() {
        return landingPageFooterLinks;
    }

    public ExtendedWebElement getLandingPageStickyHeader() {
        return landingPageStickyHeader;
    }

    public ExtendedWebElement getUfcPpvStickyHeaderCta() {
        return ufcPpvStickyHeaderCta;
    }

    public ExtendedWebElement getLandingPageSignUpButton() {
        return landingPageSignUpButton;
    }

    public ExtendedWebElement getLandingPagePpvBuyButton() {
        return landingPagePpvBuyButton;
    }

    public ExtendedWebElement getFooterHyperLink(int f) {
        return footerHyperLink;
    }

    public ExtendedWebElement getMlbEntHeadingText() {
        return mlbEntHeadingText;
    }

    public ExtendedWebElement getMlbEntBodyText() {
        return mlbEntBodyText;
    }



    //Paywall Getters
    public ExtendedWebElement getPaywallSignUp () {
        return paywallSignUp;
    }

    public ExtendedWebElement getPaywallLogin () {
        return paywallLogin;
    }

    public ExtendedWebElement getPaywallMlbTvDisplay () {
        return paywallMlbTvDisplay;
    }

    public ExtendedWebElement getMlbTVSectionHeader (int c) {
        return mlbTvSectionHeaders.get(c);
    }

    public ExtendedWebElement getReplayBroadcast (int g) {
        return replayBroadcast.get(g);
    }

    public ExtendedWebElement getMlbTvAsset (int p) {
        return mlbTvAsset.get(p);
    }

    public ExtendedWebElement getUpsellLogo () { return upsellLogo; }
    public ExtendedWebElement getContentPaywall () {return contentPaywall;}
    public ExtendedWebElement getmLBPaywallLogo () {return mLBPaywallLogo;}
    public ExtendedWebElement getmLBTVTile(int o) {return mLBTVTile.get(o);}
    public ExtendedWebElement getmLBTVButton() {return mLBTVButton;}
    public ExtendedWebElement getmLBSignUp() {return mLBSignUp;}
    public ExtendedWebElement getmLBPrice() {return mLBPrice;}



    //Methods
    public void skipTestExecution () {
        if (getCurrentUrl().contains("/fight-night") || (getCurrentUrl().contains("/ufc"))){
            LOGGER.info("Fight Night or UFC MLP is found");
        }
        else
        {
            throw new SkipException(SKIP_EXCEPTION);
        }
    }

    public void skipPPVTestExecution () {
        if (getCurrentUrl().contains("/ufc/ppv")){
            LOGGER.info("UFC PPV MLP is found");
        }
        else
        {
            throw new SkipException(SKIP_EXCEPTION);
        }
    }

    public void closePopUp () {
        if (landingPagePopupButton.isElementPresent()){
            PAGEFACTORY_LOGGER.info("PopUp displays");
            landingPagePopupButton.click();
        }
        else{
            LOGGER.warn("No PopUp displayed");
        }
    }

    public void selectDropDownOption(ExtendedWebElement dropDownFilter, String item) {
        dropDownFilter.click();
        pause(8);
        findExtendedWebElement(By.xpath("//a[text()='"+item+"']")).click();
        pause(3);
    }

    public void waitForPageToFinishLoading() {
        SeleniumUtils util = new SeleniumUtils(getDriver());
        util.waitUntilDOMready();
    }

    public void waitFor(ExtendedWebElement ele){
        waitUntil(ExpectedConditions.elementToBeClickable(ele.getBy()), (long) LONG_TIMEOUT * 2);
    }

    public void switchLanguages () {
        landingPageLanguageToggle.click();
        landingPageLanguageSelect.click();
    }

    public void scrollDownLandingPage () {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(0,250)", "");
    }

    public String getCurrentUrl (){
        return getDriver().getCurrentUrl();
    }

    public void openVodAsset () {
        pause(7);
        for(int i = 1; i<watchWebRedesignrecentReplays.size(); i++){
            if(watchWebRedesignrecentReplays.get(i).isClickable()){
                watchWebRedesignrecentReplays.get(i).click();
                break;

            }
        }
    }

    public void openMlbAsset () {
        for(int i = 1; i <mLBTVTile.size(); i++){
            if(mLBTVTile.get(i).isClickable()){
                mLBTVTile.get(i).click();
                break;
            }
        }
        mLBTVButton.click();
        getDriver().switchTo().frame(iframePaywall.getElement());
        pause(3);
    }

    public void enterVideoIframe() {
        getDriver().switchTo().frame(iframeVideoPlayer.getElement());
    }

    public void videoPlayPauseStopPlayer(){
        videoPlayPauseStopButton.hover();
        videoPlayPauseStopButton.click();
        waitFor(videoPlayPauseStopButton);
    }

    public void openLiveAsset (String espnNetwork) {
        pause(10);
        getSeeAllLiveEvents(0).click();
        liveNowEventsButton.click();
        selectDropDownOption(eventNetworkDropdown, espnNetwork);
    }

    public void selectLiveAsset(String mlbNetwork) {
        if(liveEventsPresent.isElementPresent()){
            getLiveEventSelect(0).click();
        }
        else if (noLiveEventIndicator.isElementPresent()){
            selectDropDownOption(eventNetworkDropdown, mlbNetwork);
            if (liveEventsPresent.isElementPresent()) {
                pause(10);
                getLiveEventSelect(0).click();
            }
            else {
                Assert.assertTrue(liveEventsPresent.isElementPresent(), "NO LIVE EVENTS!");
            }
        }
    }

    public void volumeControl () {
        videoVolumnControl.hover();
        videoVolumnControl.click();
    }

    public void videoShareLink() {
        videoShareButton.click();
    }

    public void exitVideoShare () {
        videoExitShare.click();
    }

    public void viewBroadcastOptions () {
        SoftAssert sa = new SoftAssert();
        if (videoBroadcastSelectButton.isElementPresent()){
            videoBroadcastSelectButton.click();
            videoBroadcastFeedOption.click();
            waitForPageToFinishLoading();
            waitFor(videoBroadcastSelectButton);
            videoBroadcastSelectButton.click();
            waitFor(videoSelectedFeed);
            sa.assertTrue(getVideoSelectedFeed().isElementPresent(), "Broadcast Feed did NOT switch");
            sa.assertAll();
        }
        else{
            LOGGER.warn("No Broadcast button");
        }
    }

    public void viewLiveBroadcastOptions () {
        SoftAssert sa = new SoftAssert();
        if (videoBroadcastSelectButton.isElementPresent()){
            videoBroadcastSelectButton.click();
            videoLiveBroadcastFeedOption.click();
            waitForPageToFinishLoading();
            waitFor(videoBroadcastSelectButton);
            videoBroadcastSelectButton.click();
            waitFor(videoLiveSelectedFeed);
            sa.assertTrue(getLiveVideoSelectedFeed().isElementPresent(), "Broadcast Feed did NOT switch");
            sa.assertAll();
        }
        else{
            LOGGER.warn("No Broadcast button");
        }
    }

    public void closedCaptionPresent (){
        if (videoClosedCaptionButton.isElementPresent()){
            videoGearSettingsCC.isElementPresent();
            videoGearSettingsCcSettings.isElementPresent();
        }
        else {
            LOGGER.warn("No CC for this feed");
        }
    }

    public void openVideoSettings () {
        videoGearSettingsButton.click();
    }

    public void videoEnterFullScreen () {
        videoGearSettingsButton.hover();
        videoEnterFullScreenButton.click();
    }

    public void videoExitFullScreen () {
        videoGearSettingsButton.hover();
        videoExitFullScreenButton.click();
        videoEnterFullScreenButton.hover();
    }

    public void adjustVolumeSlider () {
        LOGGER.info("Volume before adjustment: " + volumeRange.getAttribute("style"));
        if (volumeRange.getAttribute("style").equals("width: 0%;")) {
            videoVolumnControl.click();
            LOGGER.info("Volume before adjustment: " + volumeRange.getAttribute("style"));
            slide(volumeSlider, 70, 10);
            LOGGER.info("Volume after adjustment: " + volumeRange.getAttribute("style"));
        }
        else {
            slide(volumeSlider, 70, 10);
            LOGGER.info("Volume after adjustment: " + volumeRange.getAttribute("style"));
        }
    }

    public void scrubberSeekVideo () {
        LOGGER.info("Video before adjustment: " + videoScrubberProgress.getAttribute("style"));
        slide(videoScrubberHandle, 0, 50);
        pause(10);
        LOGGER.info("Video after adjustment: " + videoScrubberProgress.getAttribute("style"));
    }

    public void scrubberSeekLiveVideo () {
        SoftAssert sa = new SoftAssert();
        if (videoScrubberHandle.isElementPresent()) {
            LOGGER.info("Video before adjustment: " + videoScrubberProgress.getAttribute("style"));
            dragAndDrop(videoScrubberHandle, videoScrubberProgress);
            pause(10);
            LOGGER.info("Video after adjustment: " + videoScrubberProgress.getAttribute("style"));
            sa.assertTrue(getVideoScrubberProgress().getAttribute("style").contains("width: 13") ||
                            getVideoScrubberProgress().getAttribute("style").contains("width: 11") ||
                            getVideoScrubberProgress().getAttribute("style").contains("width: 12"),
                    "Scrubber NOT adjusted");
            videoLiveIcon.click();
            LOGGER.info("Video after Live adjustment: " + videoScrubberProgress.getAttribute("style"));
            pause(5);
        }
        else {
            LOGGER.warn("This is a Live DVR Event");
        }
        sa.assertAll();
    }

    public void openPaywallAsset () {
        recentReplaysSeeAll.click();
        getOpenRecentReplay(0).click();
        paywallXButton.click();
        getOpenRecentReplay(0).click();
        getDriver().switchTo().frame(iframePaywall.getElement());
    }

    public void espnPlusWebProxyCapture(){
        pause(5);
        getSeeAllLiveEvents(0).click();
        selectDropDownOption(eventNetworkDropdown, espnPlusMenuOption);

        liveNowEventsButton.click();
        if(liveEventsPresent.isElementPresent()){
            pause(10);
            getLiveEventSelect(0).click();
            pause(LONG_TIMEOUT);
        }
        else if (noLiveEventIndicator.isElementPresent(5)){
            espnPlusMenu.click();
            recentReplaysSeeAll.click();
            pause(10);
            getOpenRecentReplay(0).hover();
            getOpenRecentReplay(0).click();
            pause(LONG_TIMEOUT);
        }
    }

    public void iframePaywallExit () {
        getDriver().switchTo().defaultContent();
    }

    public void openPaywallLogin(){
        getMlbTvAsset(0).click();
    }

    public void iframePaywallEnter () {
        pause(3);
        getDriver().switchTo().frame(iframePaywall.getElement());

    }

}
