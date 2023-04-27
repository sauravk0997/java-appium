package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusPaywallIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;


@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusPaywallIOSPageBase.class)
public class DisneyPlusAppleTVPaywallPage extends DisneyPlusPaywallIOSPageBase {
    public DisneyPlusAppleTVPaywallPage(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name CONTAINS \"yearly\"`]")
    private ExtendedWebElement yearlySkuBtn;

    @ExtendedFindBy(accessibilityId = "paywallSubscribeStart")
    private ExtendedWebElement paywallSubscribeStart;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeSecureTextField")
    protected ExtendedWebElement sandboxPasswordBox;

    @Override
    public boolean isOpened() { return paywallSubscribeStart.isElementPresent(); }

    @Override
    public void clickBasicPlan() {
        clickLeft();
        pause(2);
        clickSelect();
    }

    @Override
    public void submitSandboxPassword(String password) {
        sandboxPasswordBox.type(password);
        keyPressTimes(getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        clickRight();
        pause(2);
        clickSelect();
    }

    public boolean confirmSubscription() {
        int retries = 0;
        boolean success = getTypeButtonByLabel("RETRY").isElementNotPresent(30);
        if(!success) {
            do {
                LOGGER.info("Error popup was found. Clicking retry...");
                retries++;
                clickSelect();
                success = getTypeButtonByLabel("CONTINUE").isElementNotPresent(30);
            } while (!success && retries < 5);
        }

        if(success) {
            clickSelect();
            return true;
        } else {
            return false;
        }
    }

    public void acceptConfirmationAlert() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(getTypeButtonByLabel("OK").getBy()), 30);
        clickSelect();
    }
}
