package com.disney.qa.espn.ios.pages.watch;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class EspnWatchVideoPlayerIOSPageBase extends EspnIOSPageBase {


    private static final String SLIDER_PREDICATE_LOC  = "type == 'XCUIElementTypeSlider'";
    private static final String WATCH_BUTTON_PREDICATE_LOC  = "name == 'Watch' AND type == 'XCUIElementTypeButton'";



    //Objects

    @FindBy(xpath = "//XCUIElementTypeApplication[@name='ESPN']/XCUIElementTypeWindow[1]/XCUIElementTypeOther" +
            "/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther" +
            "/XCUIElementTypeOther[2]/XCUIElementTypeStaticText")
    private ExtendedWebElement elapsedTime;







    //Methods
    public EspnWatchVideoPlayerIOSPageBase(WebDriver driver) {
        super(driver);
    }

    public void watchVideo(int time) {
        pause(time);
    }

    public void pauseVideo() {

        IOSUtils iosUtils = new IOSUtils();

        iosUtils.screenPress( 2,4);

        iosUtils.screenPress( 2,4);

        int maxAttempt = 10;

        int i = 0;

        while (!getDriver().findElement(MobileBy.iOSNsPredicateString(SLIDER_PREDICATE_LOC)).isDisplayed()
                && i <= 10) {

            LOGGER.info("Video didn't pause. Trying again.." + "\n" + "Attempts remaining: " + maxAttempt);

            iosUtils.screenPress( 2,4);

            iosUtils.screenPress( 2,4);

            i++;

            if(i == 10 && !getDriver().findElement(MobileBy.iOSNsPredicateString(SLIDER_PREDICATE_LOC)).isDisplayed()) {
                LOGGER.error("Failed to pause video");
                break;
            }

            if(getDriver().findElement(MobileBy.iOSNsPredicateString(SLIDER_PREDICATE_LOC)).isDisplayed()) {
                LOGGER.info("Paused video successfully");
            }
        }
    }

    @Override
    public boolean isOpened() {
        return getDriver().findElement(MobileBy.iOSNsPredicateString(WATCH_BUTTON_PREDICATE_LOC)).isDisplayed();
    }



}
