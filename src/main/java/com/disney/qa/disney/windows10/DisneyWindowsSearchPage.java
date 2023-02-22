package com.disney.qa.disney.windows10;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsSearchPage extends DisneyWindowsCommonPage {
    @ExtendedFindBy(accessibilityId = "SearchTextBox")
    private ExtendedWebElement searchTextBox;

    public DisneyWindowsSearchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return searchTextBox.isElementPresent();
    }

    public boolean isSearchBoxPresent() {
        return searchTextBox.isElementPresent();
    }
}
