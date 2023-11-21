package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.common.constant.CollectionConstant;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusHuluIOSPageBase extends DisneyPlusApplePageBase {

    public DisneyPlusHuluIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return isCollectionPresent(CollectionConstant.Collection.HULU_FEATURED_PREVIEW);
    }
}