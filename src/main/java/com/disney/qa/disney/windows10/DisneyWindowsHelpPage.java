package com.disney.qa.disney.windows10;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsHelpPage extends DisneyWindowsCommonPage {
    @ExtendedFindBy(accessibilityId = "TitleTextBlock")
    private ExtendedWebElement helpTitle;
    @ExtendedFindBy(accessibilityId = "UrlTextBlock")
    private ExtendedWebElement urlText;
    @ExtendedFindBy(accessibilityId = "CopyTextBlock")
    private ExtendedWebElement faqText;

    public DisneyWindowsHelpPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return helpTitle.isElementPresent();
    }
}
