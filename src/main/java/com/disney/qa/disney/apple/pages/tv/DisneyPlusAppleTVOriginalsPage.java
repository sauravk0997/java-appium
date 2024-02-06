package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOriginalsIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusOriginalsIOSPageBase.class)
public class DisneyPlusAppleTVOriginalsPage extends DisneyPlusOriginalsIOSPageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`name == 'Originals'`]")
    private ExtendedWebElement originalText;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == 'PLAY'`]")
    private ExtendedWebElement playButton;

    @ExtendedFindBy(accessibilityId = "Featured")
    private ExtendedWebElement featured;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[$name == 'PLAY'$]/**/XCUIElementTypeButton[-1]")
    private ExtendedWebElement addBtn;

    public DisneyPlusAppleTVOriginalsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOriginalTextPresent() {
        return originalText.isPresent();
    }

    public boolean isPlayBtnPresent() {
        return playButton.isPresent();
    }

    public DisneyPlusAppleTVOriginalsPage clickAddBtn() {
        addBtn.click();
        return this;
    }

    public String getAddBtnStatus() {
        return addBtn.getAttribute("label");
    }

    @Override
    public boolean isOpened() {
        boolean isOpened = DisneyPlusAppleTVCommonPage.isProd() ? featured.isElementPresent() : getDynamicAccessibilityId("Star+_Grid").isElementPresent();
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return isOpened;
    }
}
