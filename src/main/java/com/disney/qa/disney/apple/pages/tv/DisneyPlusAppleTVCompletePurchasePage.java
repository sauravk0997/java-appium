package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusCompletePurchaseIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusCompletePurchaseIOSPageBase.class)
public class DisneyPlusAppleTVCompletePurchasePage extends DisneyPlusCompletePurchaseIOSPageBase {

    @ExtendedFindBy(accessibilityId = "restoreButton")
    private ExtendedWebElement restorePurchaseBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"Complete Subscription\"`]")
    private ExtendedWebElement completeSubscriptionBtn;

    @ExtendedFindBy(accessibilityId = "productButton-com.disney.monthly.disneyplus2021.apple")
    private ExtendedWebElement monthlyBtn;

    @ExtendedFindBy(accessibilityId = "productButton-com.disney.yearly.disneyplus2021.apple")
    private ExtendedWebElement yearlyBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == \"LOG OUT\"`]")
    private ExtendedWebElement logOutAlertBtn;

    public DisneyPlusAppleTVCompletePurchasePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = restorePurchaseBtn.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }

    public boolean isMonthlyBtnPresent() {
        return DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).equalsIgnoreCase("prod")
                ? monthlyBtn.isElementPresent() : getDynamicAccessibilityId("productButton-com.disney.monthly.dplusdaytest.apple").isElementPresent();
    }

    public boolean isYearlyBtnPresent() {
        return yearlyBtn.isElementPresent();
    }

    public boolean isCompleteSubscriptionBtnPresent() {
        boolean isPresent = completeSubscriptionBtn.isElementPresent();
        UniversalUtils.captureAndUpload(getCastedDriver());
        return isPresent;
    }
}
