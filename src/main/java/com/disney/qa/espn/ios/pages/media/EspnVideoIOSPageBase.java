package com.disney.qa.espn.ios.pages.media;

import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.annotations.AccessibilityId;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class EspnVideoIOSPageBase extends EspnIOSPageBase {


    //Objects

    @FindBy(name = "player.frame")
    @AccessibilityId
    private ExtendedWebElement videoPlayerFrame;

    @FindBy(name = "player.video")
    @AccessibilityId
    private ExtendedWebElement videoPlayer;


    @FindBy(name = "player.close")
    @AccessibilityId
    private ExtendedWebElement closeBtn;

    @FindBy(name = "player.dock")
    @AccessibilityId
    private ExtendedWebElement dockBtn;

    @FindBy(name = "player.captions")
    @AccessibilityId
    private ExtendedWebElement captionsBtn;

    @FindBy(name = "player.airPlay")
    @AccessibilityId
    private ExtendedWebElement airplayBtn;

    @FindBy(name = "player.fullScreen")
    @AccessibilityId
    private ExtendedWebElement fullScreenBtn;

    @FindBy(name = "player.progressBar")
    @AccessibilityId
    private ExtendedWebElement progressBarBtn;

    @FindBy(name = "player.pause")
    @AccessibilityId
    private ExtendedWebElement pauseBtn;

    @FindBy(name = "LIVE")
    @AccessibilityId
    private ExtendedWebElement liveBtn;

    @FindBy(name = "player.title")
    @AccessibilityId
    private ExtendedWebElement playerTitle;

    @FindBy(name = "player.share")
    @AccessibilityId
    private ExtendedWebElement shareBtn;


    //Methods

    public EspnVideoIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public boolean isVideoStreamPresent(Long timeout) {
        return videoPlayerFrame.isElementPresent(timeout);
    }


}
