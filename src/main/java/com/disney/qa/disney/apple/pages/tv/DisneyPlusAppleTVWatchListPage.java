package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.phone.DisneyPlusMoreMenuIOSPage;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusMoreMenuIOSPage.class)
public class DisneyPlusAppleTVWatchListPage extends DisneyPlusMoreMenuIOSPage {

    // TODO: Watchlist accessibility id requested: TVOS-4227
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"placeholder accessibility title label\"`]")
    private ExtendedWebElement watchlistCollectionView;

    @ExtendedFindBy(accessibilityId = "watchlistButton")
    private ExtendedWebElement removeWatchlistBtn;

    @ExtendedFindBy(accessibilityId = "Add the current title to your Watchlist")
    private ExtendedWebElement addWatchlistBtn;

    public DisneyPlusAppleTVWatchListPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = watchlistCollectionView.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public boolean isRemoveWatchlistBtnOpen() {
        return removeWatchlistBtn.isElementPresent();
    }

    public void clickRemoveWatchlistBtn() {
        removeWatchlistBtn.click();
    }
}
