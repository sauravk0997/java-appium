package com.disney.qa.espn.ios.pages.watch;

import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.espn.ios.pages.common.EspnIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.annotations.Predicate;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;


public class EspnWatchIOSPageBase extends EspnIOSPageBase implements IMobileUtils {



    //Objects

    private static final String WATCH_TITLE_PREDICATE_LOC = "name == 'Watch' AND type == 'XCUIElementTypeOther'";

    private static final String WATCH_NAV_BAR_PREDICATE_LOC = "name == 'Watch' AND type == 'XCUIElementTypeNavigationBar'";

    public static final String FEATURED_TAB_ACCESSIBILITY_ID = "FEATURED";

    public static final String ESPN_PLUS_TAB_ACCESSIBILITY_ID = "ESPN+";

    public static final String ORIGINALS_TAB_ACCESSIBILITY_ID = "ORIGINALS";

    public static final String BACK_BUTTON_SEE_ALL = "type == 'XCUIElementTypeButton' AND name == 'Watch'";

    @FindBy(id = "FEATURED")
    private ExtendedWebElement featuredTab;

    @FindBy(xpath = "//XCUIElementTypeOther[@name='Watch']")
    private ExtendedWebElement watchTitle;

    @FindBy(id = "button.schedule")
    private ExtendedWebElement scheduleBtn;

    @FindBy(xpath = "type == 'XCUIElementTypeButton' AND name == 'ESPN+'")
    @Predicate
    private ExtendedWebElement espnPlusTabBtn;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name='%s']/following-sibling::XCUIElementTypeButton")
    private ExtendedWebElement dynamicSeeAllBtn;

    @FindBy(xpath = "//XCUIElementTypeOther[@name='%s']")
    private ExtendedWebElement dynamicSeeAllTitle;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name='%s']")
    private ExtendedWebElement dynamicSeeAllTitle2;



    //Methods

    public EspnWatchIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
           return findExtendedWebElement(MobileBy
                    .iOSNsPredicateString(WATCH_TITLE_PREDICATE_LOC)).isElementPresent();
    }

    public void getSeeAll(String carouselHeader) {
        dynamicSeeAllBtn.format(carouselHeader).click();
    }

    public boolean isSeeAllOpened(String carouselHeader) {
        if("30 for 30 Spotlight".equalsIgnoreCase(carouselHeader)){
            return dynamicSeeAllTitle2.format(carouselHeader.substring(0,9)).isElementPresent();
        }
        return dynamicSeeAllTitle.format(carouselHeader).isElementPresent();
    }

    public void getWatchPage() {
        findExtendedWebElement(MobileBy.iOSNsPredicateString(BACK_BUTTON_SEE_ALL)).click();
    }


    public boolean isScheduleBtnPresent(int timeout) {
        return scheduleBtn.isElementPresent(timeout);
    }

    public boolean isEspnPlusDefaultSubNav() {
        return "1".equals(findExtendedWebElement(MobileBy.AccessibilityId(ESPN_PLUS_TAB_ACCESSIBILITY_ID))
                .getAttribute("value"));
    }

    public boolean isDefaultView() {
        return findExtendedWebElement(MobileBy.iOSNsPredicateString(WATCH_NAV_BAR_PREDICATE_LOC)).isElementPresent();
    }

    public boolean areSubnavContentsPresent() {
        return (findExtendedWebElement(MobileBy.AccessibilityId(FEATURED_TAB_ACCESSIBILITY_ID)))
                .isElementPresent()
                && (findExtendedWebElement(MobileBy.AccessibilityId(ESPN_PLUS_TAB_ACCESSIBILITY_ID)))
                .isElementPresent()
                && (findExtendedWebElement(MobileBy.AccessibilityId(ORIGINALS_TAB_ACCESSIBILITY_ID)))
                .isElementPresent();
    }

    public void getSubnav(WatchSubnavOptions subnavOptions) {
        LOGGER.info("Searching for " + subnavOptions + " subnav");
        switch(subnavOptions) {
            case FEATURED:
                do {
                    findExtendedWebElement(MobileBy.AccessibilityId(FEATURED_TAB_ACCESSIBILITY_ID))
                            .clickIfPresent();
                } while (!initPage(EspnWatchFeaturedIOSPageBase.class).isOpened());
                break;
            case ESPNPLUS:
                do {
                    findExtendedWebElement(MobileBy.AccessibilityId(ESPN_PLUS_TAB_ACCESSIBILITY_ID))
                            .clickIfPresent();
                } while (!initPage(EspnWatchEspnPlusIOSPageBase.class).isOpened());
                break;
            case ORIGINALS:
                do {
                    findExtendedWebElement(MobileBy.AccessibilityId(ORIGINALS_TAB_ACCESSIBILITY_ID))
                            .clickIfPresent();
                } while (!initPage(EspnWatchOriginalsIOSPageBase.class).isOpened());
                break;
            default:
                LOGGER.info("Please use proper subnav options");
        }
    }

    public boolean scrollDownAndCheckSubnavNotPresent() {
        new IOSUtils().scrollDown();
        if (!espnPlusTabBtn.isElementPresent()) {
            Screenshot.capture(getDriver(), "Scroll Down Watch Page");
            LOGGER.info("PASS: Subnav option NOT present after scrolling down");
            return true;
        }
        return false;
    }

    public boolean scrollUpAndCheckSubnavPresent() {
        initPage(EspnWatchIOSPageBase.class).scrollToSubNavOptions();
        if(espnPlusTabBtn.isElementPresent()) {
            Screenshot.capture(getDriver(), "Scroll Up Watch Page");
            LOGGER.info("PASS: Subnav option present after scrolling up");
            return true;
        }
        return false;
    }

    public void scrollToSubNavOptions() {
        swipe(featuredTab, Direction.DOWN);
    }

}