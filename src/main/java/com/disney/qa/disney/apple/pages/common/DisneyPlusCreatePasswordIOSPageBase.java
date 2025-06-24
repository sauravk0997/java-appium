package com.disney.qa.disney.apple.pages.common;

import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCreatePasswordIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusCreatePasswordIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return headlineHeader.isPresent();
    }
}
