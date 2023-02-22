package com.disney.qa.disney.windows10;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsOriginalsPage extends DisneyWindowsCommonPage {
    @ExtendedFindBy(accessibilityId = "OriginalsContainerLayoutListView")
    private ExtendedWebElement originalsContainer;

    public DisneyWindowsOriginalsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOriginalsPresent() {
        return originalsContainer.isElementPresent();
    }

    @Override
    public boolean isOpened() {
        return originalsContainer.isElementPresent();
    }
}
