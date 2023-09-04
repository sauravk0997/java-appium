package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.zebrunner.carina.utils.appletv.IRemoteControllerAppleTV;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusSearchIOSPageBase.class)
public class DisneyPlusAppleTVSearchPage extends DisneyPlusSearchIOSPageBase {

    @ExtendedFindBy(accessibilityId = "searchBar")
    private ExtendedWebElement searchField;

    public DisneyPlusAppleTVSearchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = searchField.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public void typeInSearchField(String text) {
        searchField.type(text);
    }

    public void clickSearchResult(String assetName) {
        if (keyboard.getSize().getWidth() > 1000) {
            LOGGER.info("Detected horizontal keyboard, clicking down to search results..");
            keyPressTimes(IRemoteControllerAppleTV::clickDown, 1, 1);
        } else {
            LOGGER.info("Detected vertical keyboard, clicking right to search results..");
            keyPressTimes(IRemoteControllerAppleTV::clickRight, 6, 1);
        }
        dynamicCellByLabel.format(assetName).click();
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
