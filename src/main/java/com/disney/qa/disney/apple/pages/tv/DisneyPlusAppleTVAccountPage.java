package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVAccountPage extends DisneyPlusApplePageBase {
    public DisneyPlusAppleTVAccountPage(WebDriver driver) {super(driver);}

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == 'changeEmailCell'`]")
    private ExtendedWebElement changeEmailBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == 'changePasswordCell'`]")
    private ExtendedWebElement changePasswordBtn;

    @Override
    public boolean isOpened() {
        boolean isPresent = changeEmailBtn.isElementPresent() && changePasswordBtn.isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isPresent;
    }

    public void clickChangeEmailBtn() {
        changeEmailBtn.click();
    }

    public void clickChangePasswordBtn() {
        changePasswordBtn.click();
    }

}
