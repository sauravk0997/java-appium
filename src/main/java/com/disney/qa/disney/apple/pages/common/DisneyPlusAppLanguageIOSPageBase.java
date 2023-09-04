package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppLanguageIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "appLanguage")
    ExtendedWebElement appLanguageView;

    public DisneyPlusAppLanguageIOSPageBase(WebDriver driver) { super(driver); }

    @Override
    public boolean isOpened() { return appLanguageView.isElementPresent(); }
}
