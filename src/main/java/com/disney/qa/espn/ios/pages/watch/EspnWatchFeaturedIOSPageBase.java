package com.disney.qa.espn.ios.pages.watch;

import com.disney.qa.api.espn.mobile.EspnIOSAcceptanceAPIcaller;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.events.EventFiringWebDriver;


public class EspnWatchFeaturedIOSPageBase extends EspnWatchIOSPageBase implements IMobileUtils {

    //Objects

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name='%s']/following-sibling::XCUIElementTypeOther" +
            "/XCUIElementTypeStaticText[@name='%s']")
    private ExtendedWebElement metadata;

    @FindBy(id = "%s")
    private ExtendedWebElement title;


    //Methods

    public EspnWatchFeaturedIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return "1".equals(findExtendedWebElement(MobileBy.AccessibilityId(FEATURED_TAB_ACCESSIBILITY_ID))
                .getAttribute("value"));
    }

    public boolean isLiveMetadataPresent(int index) {
        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();
        return metadata.format(espnIOSAcceptanceAPIcaller.getLiveCarouselTitles(WatchSubnavOptions.FEATURED).get(index),
                espnIOSAcceptanceAPIcaller.getLiveNetworkAndLeague(WatchSubnavOptions.FEATURED).get(index)).isElementPresent(DELAY);
    }

    public boolean isVODMetadataNotPresent(int index) {
        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();
            return !metadata.format(espnIOSAcceptanceAPIcaller.getVODnames(WatchSubnavOptions.FEATURED).get(index),
                    espnIOSAcceptanceAPIcaller.getVODMetada(WatchSubnavOptions.FEATURED).get(index)).isElementPresent();
    }

    public boolean scrollToVODContent(int index, int minimumCarouselCount) {
        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();
        return swipe(title.format(espnIOSAcceptanceAPIcaller.getVODnames(WatchSubnavOptions.FEATURED, minimumCarouselCount)
                .get(index)),15);
    }

    public boolean noMetadataForNonPlayableAsset() {
        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();
        if (swipe(title.format(espnIOSAcceptanceAPIcaller.getFirstNonPlayableAssetTitle(WatchSubnavOptions.FEATURED)))) {
                return !title.format(title.format(espnIOSAcceptanceAPIcaller.getNonPlayableAssets(WatchSubnavOptions.FEATURED).get(0)))
                        .isElementPresent();
        }
        return false;
    }

    public int getVODCarouselX(int index) {
        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();
        return title.format(espnIOSAcceptanceAPIcaller.getVODnames(WatchSubnavOptions.FEATURED, 3).get(index)).getLocation().getX();
    }

    public int getVODCarouselY(int index) {
        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();
        return title.format(espnIOSAcceptanceAPIcaller.getVODnames(WatchSubnavOptions.FEATURED, 3).get(index)).getLocation().getY();
    }

    public void swipeVODCarousel(int maxSwipes) {

        int startContentIndex = 1;
        int endContentIndex = 0;

        TouchAction touchAction = new TouchAction((IOSDriver<?>) ((EventFiringWebDriver) getDriver()).getWrappedDriver());
        Dimension screenSize = getDriver().manage().window().getSize();

        LOGGER.info("Swiping across carousel | Swipes remaining: " + maxSwipes);
        LOGGER.info("Swiping from: (" + getVODCarouselX(endContentIndex) + ", " + getVODCarouselY(endContentIndex)
                + ") to (" + screenSize.width/12 + ", " + getVODCarouselY(endContentIndex) + ")");
        touchAction.longPress(new PointOption()
                .withCoordinates(getVODCarouselX(endContentIndex), getVODCarouselY(endContentIndex)))
                .waitAction()
                .moveTo(new PointOption().withCoordinates(screenSize.width/12, getVODCarouselY(endContentIndex)))
                .release()
                .perform();
        do {
            LOGGER.info("Swiping across carousel | Swipes remaining: " + (maxSwipes-1));
            LOGGER.debug("Swiping from: (" + getVODCarouselX(startContentIndex) + ", " + getVODCarouselY(startContentIndex)
                    + ") to (" + getVODCarouselX(endContentIndex) + ", " + getVODCarouselY(endContentIndex) + ")");

            touchAction.longPress(new PointOption()
                    .withCoordinates(getVODCarouselX(startContentIndex),getVODCarouselY(startContentIndex)))
                    .waitAction()
                    .moveTo(new PointOption().withCoordinates(screenSize.width/12, getVODCarouselY(endContentIndex)))
                    .release()
                    .perform();

            startContentIndex++;
            endContentIndex++;
            maxSwipes--;

            Screenshot.capture(getDriver(), "Swiping VOD Carousels");
        } while (maxSwipes-1 > 0);
    }

}
