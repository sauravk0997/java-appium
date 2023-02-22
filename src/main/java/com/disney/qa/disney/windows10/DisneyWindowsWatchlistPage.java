package com.disney.qa.disney.windows10;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsWatchlistPage extends DisneyWindowsCommonPage {
    @FindBy(name = "Your watchlist is empty")
    private ExtendedWebElement watchlistEmpty;
    @FindBy(name = "Watchlist")
    private ExtendedWebElement watchlistTitle;

    public DisneyWindowsWatchlistPage(WebDriver driver) {
        super(driver);
    }

    public boolean isWatchlistPresent() {
        return watchlistTitle.isElementPresent();
    }

    @Override
    public boolean isOpened() {
        return watchlistTitle.isElementPresent();
    }
}
