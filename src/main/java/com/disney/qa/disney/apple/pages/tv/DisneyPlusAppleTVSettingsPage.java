package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusHomeIOSPageBase.class)
public class DisneyPlusAppleTVSettingsPage extends DisneyPlusMoreMenuIOSPageBase {
    public DisneyPlusAppleTVSettingsPage(WebDriver driver) {super(driver);}

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == 'accountTab'`]")
    private ExtendedWebElement accountBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == 'logOutAllDevicesCell'`]")
    private ExtendedWebElement  logOutAllDevicesBtn;

    public void clickAccountBtn() { accountBtn.click(); }

    public void clickLogOutAllDevicesBtn() { logOutAllDevicesBtn.click(); }

    @ExtendedFindBy(accessibilityId = "accountView")
    ExtendedWebElement accountView;

    @Override
    public boolean isOpened() { return accountView.isElementPresent(); }
}
