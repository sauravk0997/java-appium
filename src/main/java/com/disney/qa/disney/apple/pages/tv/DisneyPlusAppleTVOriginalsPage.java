package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOriginalsIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
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
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isOpened;
    }
}
