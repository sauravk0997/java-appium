package com.disney.qa.disney.windows10;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsDetailsPage extends DisneyWindowsCommonPage {
    @ExtendedFindBy(accessibilityId = "PlayButton")
    private ExtendedWebElement playButton;
    @ExtendedFindBy(accessibilityId = "PlayTrailerButton")
    private ExtendedWebElement playTrailerButton;
    @ExtendedFindBy(accessibilityId = "WatchlistButton")
    private ExtendedWebElement watchlistButton;
    @ExtendedFindBy(accessibilityId = "GroupWatchRoomButton")
    private ExtendedWebElement groupWatchRoomButton;
    @ExtendedFindBy(accessibilityId = "DescriptionText")
    private ExtendedWebElement descriptionText;
    @ExtendedFindBy(accessibilityId = "AddedIconSelected")
    private ExtendedWebElement removeWatchlistButton;

    public DisneyWindowsDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return playButton.isElementPresent();
    }

    public void clickAddToWatchlist() {
        watchlistButton.click();
    }

    public boolean isAddWatchlistButtonPresent() {
        return watchlistButton.isElementPresent();
    }

    public boolean isRemoveWatchlistButtonPresent() {
        return removeWatchlistButton.isElementPresent();
    }

    public void removeFromWatchlist() {
        removeWatchlistButton.click();
    }

    public void clickPlay() {
        playButton.click();
        pause(5);
    }
}
