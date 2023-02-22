package com.disney.qa.espn.ios.pages.watch;

import com.disney.qa.api.espn.mobile.EspnIOSAcceptanceAPIcaller;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.espn.ios.pages.media.EspnVideoIOSPageBase;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.annotations.AccessibilityId;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.annotations.Predicate;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;


public class EspnWatchEspnPlusIOSPageBase extends EspnWatchIOSPageBase implements IMobileUtils {


    //Objects


    @FindBy(xpath = "//XCUIElementTypeStaticText[@name='%s']/following-sibling::XCUIElementTypeButton[@name='See All']")
    private ExtendedWebElement seeAllBtn;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name='%s']")
    private ExtendedWebElement carouselHeader;

    @FindBy(id = "ESPN+ Replays")
    private ExtendedWebElement espnReplaysTitle;

    @FindBy(xpath = "//XCUIElementTypeApplication[@name='ESPN']/XCUIElementTypeWindow[1]/XCUIElementTypeOther" +
            "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]" +
            "/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther" +
            "/XCUIElementTypeOther/XCUIElementTypeCollectionView/XCUIElementTypeCell[3]/XCUIElementTypeOther" +
            "/XCUIElementTypeCollectionView/XCUIElementTypeCell[1]/XCUIElementTypeOther/XCUIElementTypeOther[1]")
    private ExtendedWebElement firstReplay;

    @FindBy(xpath = "//XCUIElementTypeScrollView/XCUIElementTypeOther/XCUIElementTypeOther" +
            "/XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeOther/XCUIElementTypeCollectionView" +
            "/XCUIElementTypeCell/XCUIElementTypeOther")
    private ExtendedWebElement firstVideo;

    @FindBy(xpath = "//XCUIElementTypeCollectionView/XCUIElementTypeCell")
    private List<ExtendedWebElement> allElementsList;

    @FindBy(xpath = "//XCUIElementTypeStaticText")
    private ExtendedWebElement allElementsNames;

    @FindBy(name = "Live")
    @AccessibilityId
    private ExtendedWebElement liveIcon;

    @FindBy(name = "More Live")
    @AccessibilityId
    private ExtendedWebElement moreLiveBtn;

    @FindBy(name = "%s")
    @AccessibilityId
    private ExtendedWebElement dynamicTitleNames;

    @FindBy(xpath = "type == 'XCUIElementTypeStaticText' AND name == '%s'")
    @Predicate
    private ExtendedWebElement dynamicText;

    @FindBy(name = "customButton")
    @AccessibilityId
    private ExtendedWebElement freeTrialBtn;

    @FindBy(name = "Teams, leagues, players, and events")
    @AccessibilityId
    private ExtendedWebElement searchBar;


    //Methods

    public EspnWatchEspnPlusIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return "1".equals(findExtendedWebElement(MobileBy.AccessibilityId(ESPN_PLUS_TAB_ACCESSIBILITY_ID))
                .getAttribute("value"));
    }

    public boolean playLiveVideo(String asset, long playbackDuration) {

        boolean isStreamPresent = false;
        MobileUtilsExtended mobileUtil = new MobileUtilsExtended();

        searchBar.type(asset);
        mobileUtil.hideKeyboard();
        if (mobileUtil.swipe(dynamicTitleNames.format(asset))) {
            LOGGER.info("Asset Tile Found: " + asset);
            dynamicTitleNames.format(asset).click();
            if (initPage(EspnVideoIOSPageBase.class).isVideoStreamPresent(20L)) {
                LOGGER.info("Stream found playing stream for 60 seconds");
                pause(playbackDuration);
                isStreamPresent = true;
            }
        } else {
            LOGGER.info("Asset Not Found!!!");
        }
        return isStreamPresent;

    }

    public EspnWatchVideoPlayerIOSPageBase playFirstVideo() {
        firstVideo.click();
        return initPage(EspnWatchVideoPlayerIOSPageBase.class);
    }

    public void playFirstReplay() {
        swipe(espnReplaysTitle, Direction.UP, 10, 10);
        firstReplay.click();
    }

    /**
     * ESPN > WATCH > check section header title
     **/
    public boolean isWatchTabSectionHeaderPresent(String headerTitle) {
        return carouselHeader.format(headerTitle).isElementPresent(DELAY);
    }

    /**
     * ESPN > WATCH > check section headeNOTr title
     **/
    public boolean isWatchTabSectionHeaderNotPresent(String headerTitle) {
        ExtendedWebElement headerElement = carouselHeader.format(headerTitle);
        LOGGER.info("Header '" + headerElement + "' is not present");
        return headerElement.isElementPresent(DELAY);
    }

    public boolean isFirstSeeAllNotPresent() {
        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();
        return !seeAllBtn.format(espnIOSAcceptanceAPIcaller.getNoSelfTitles(WatchSubnavOptions.ESPNPLUS).get(1)).isElementPresent();
    }

    public boolean isFirstSeeAllPresent() {
        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();
        return seeAllBtn.format(espnIOSAcceptanceAPIcaller.getSelfTitles(WatchSubnavOptions.ESPNPLUS).get(1)).isElementPresent(DELAY);
    }

    public boolean scrollToFirstNoSelfTitle() {

        Configuration jsonPathJacksonConfiguration;

        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();

        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();

        String bucketName = espnIOSAcceptanceAPIcaller.getNoSelfTitles(WatchSubnavOptions.ESPNPLUS).get(1);

        LOGGER.info("Content names under '" + bucketName + "': " + JsonPath.using(jsonPathJacksonConfiguration)
                .parse(espnIOSAcceptanceAPIcaller.getResponseBasedOnSubnav(WatchSubnavOptions.ESPNPLUS))
                .read("$..[?(@.name == '" + bucketName + "')].contents[*].name"));

        return swipe(carouselHeader.format(bucketName));
    }

    public boolean scrollToFirstSelfTitle() {

        Configuration jsonPathJacksonConfiguration;

        EspnIOSAcceptanceAPIcaller espnIOSAcceptanceAPIcaller = new EspnIOSAcceptanceAPIcaller();

        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();

        String bucketName = espnIOSAcceptanceAPIcaller.getSelfTitles(WatchSubnavOptions.ESPNPLUS).get(1);

        LOGGER.info("Content names under '" + bucketName + "': " + JsonPath.using(jsonPathJacksonConfiguration)
                .parse(espnIOSAcceptanceAPIcaller.getResponseBasedOnSubnav(WatchSubnavOptions.ESPNPLUS))
                .read("$..[?(@.name == '" + bucketName + "')].contents[*].name"));

        return swipe(carouselHeader.format(bucketName));
    }


}
