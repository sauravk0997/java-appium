package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.zebrunner.carina.utils.appletv.IRemoteControllerAppleTV;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusSearchIOSPageBase.class)
public class DisneyPlusAppleTVSearchPage extends DisneyPlusSearchIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    @ExtendedFindBy(accessibilityId = "searchBar")
    private ExtendedWebElement searchField;

    public DisneyPlusAppleTVSearchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = searchField.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public void typeInSearchField(String text) {
        searchField.type(text);
    }

    public void clickSearchResult(String assetName) {
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        if (localizedKeyboard.getSize().getWidth() > 1000) {
            moveRight(1, 1);
        } else {
            moveDown(1, 1);
        }
        clickSelect();
    }

    public void clickLocalizedSearchResult(String assetName) {
        if (localizedKeyboard.getSize().getWidth() > 1000) {
            LOGGER.info("Detected horizontal keyboard, clicking down to search results..");
            keyPressTimes(IRemoteControllerAppleTV::clickDown, 1, 1);
        } else {
            LOGGER.info("Detected vertical keyboard, clicking right to search results..");
            keyPressTimes(IRemoteControllerAppleTV::clickRight, 6, 1);
        }
        getTypeCellLabelContains(assetName).clickIfPresent();
    }
}
