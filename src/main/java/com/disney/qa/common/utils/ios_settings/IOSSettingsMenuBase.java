package com.disney.qa.common.utils.ios_settings;

import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.utils.IOSUtils;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class IOSSettingsMenuBase extends DisneyAbstractPage {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeNavigationBar[`name == 'Settings'`]")
    private ExtendedWebElement header;

    @FindBy(xpath = "//XCUIElementTypeNavigationBar[@name='Settings']/following-sibling::*")
    private ExtendedWebElement settingsContainer;

    @FindBy(id = "STORE")
    protected ExtendedWebElement appStoreTab;

    @FindBy(xpath = "//*[contains(@name, 'dsqaaiap')]")
    private ExtendedWebElement sandboxAccount;

    @FindBy(xpath = "//XCUIElementTypeButton[@name='Manage']")
    private ExtendedWebElement manageButton;

    @FindBy(xpath = "//*[contains(@name, '%s')]/../following-sibling::*/*[contains(@name, 'Expired')]")
    private ExtendedWebElement appExpiredNotation;

    @FindBy(id = "Cancel Subscription")
    protected ExtendedWebElement cancelSubscriptionBtn;

    @FindBy(id = "Done")
    protected ExtendedWebElement doneBtn;

    @FindBy(xpath = "//*[contains(@name, '%s') and(contains(@name, 'Expires') or contains(@name, 'Next'))]")
    private ExtendedWebElement multiSkuActiveSub;

    @FindBy(xpath = "//XCUIElementTypeButton[contains(@name, '%s')]")
    protected ExtendedWebElement appSubscriptionButton;

    @FindBy(id = "Back")
    protected ExtendedWebElement backBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeSecureTextField[`value == 'Password'`]")
    private ExtendedWebElement sandboxPasswordBox;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'Sign In'`]")
    protected ExtendedWebElement sandboxSigninButton;

    @FindBy(id = "Subscriptions")
    protected ExtendedWebElement subscriptionsButton;

    protected IOSUtils utils;

    public IOSSettingsMenuBase(WebDriver driver) {
        super(driver);
        utils = new IOSUtils();
    }

    public void submitSandboxPassword(String password) {
        sandboxPasswordBox.type(password);
        sandboxSigninButton.click();
    }

    @Override
    public boolean isOpened() {
        return header.isElementPresent();
    }

    public void launchSettings() {
        utils.launchApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        if(!isOpened()) {
            utils.terminateApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
            utils.launchApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        }
    }

    public void cancelActiveEntitlement(String appName) {
        boolean waitForExpiryTime = false;
        int appSubButtonIndex = 9999;
        List<ExtendedWebElement> appSubButtons = new LinkedList<>();
        launchSettings();
        utils.swipeInContainerTillElementIsPresent(settingsContainer, appStoreTab, 3, IMobileUtils.Direction.UP);
        appStoreTab.click();
        manageSandboxAcct();

        if(subscriptionsButton.isElementPresent()) {
            subscriptionsButton.click();
        }

        if(appSubscriptionButton.format(appName).isElementPresent()) {
            appSubButtons = findExtendedWebElements(appSubscriptionButton.format(appName).getBy());
            for (int i = 0, appSubButtonsSize = appSubButtons.size(); i < appSubButtonsSize; i++) {
                ExtendedWebElement app = appSubButtons.get(i);
                if (app.getName().contains("Next billing date:")) {
                    appSubButtonIndex = i;
                    app.click();
                    cancelActiveSubscription();
                    backBtn.click();
                    waitForExpiryTime = true;
                    break;
                }
            }
        } else if (cancelSubscriptionBtn.isElementPresent()) {
            cancelActiveSubscription();
            doneBtn.click();
            waitForExpiryTime = true;
        }

        if(waitForExpiryTime) {
            waitForEntitlementExpiration(appSubButtons, appName, appSubButtonIndex);
        }

        utils.terminateApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
    }

    protected void manageSandboxAcct() {
        utils.swipe(sandboxAccount);
        sandboxAccount.click();
        manageButton.click();
        try {
            submitSandboxPassword("G0Disney!");
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
    }

    protected void cancelActiveSubscription() {
        cancelSubscriptionBtn.click();
        utils.acceptAlert();
        LOGGER.info("Active Subscription cancelled.");
    }

    protected void waitForEntitlementExpiration(List<ExtendedWebElement> appSubButtons, String appName, int appSubButtonIndex) {
        LOGGER.info("Refreshing page until expired indicator shows for up to 4 minutes...");
        Instant expireTime = Instant.now().plus(4, ChronoUnit.MINUTES);
        if(appSubButtons.isEmpty()) {
            while (Instant.now().isBefore(expireTime) && !appExpiredNotation.format("Disney+").isElementPresent()) {
                doneBtn.click();
                manageSandboxAcct();
            }
        } else {
            while (Instant.now().isBefore(expireTime)) {
                appSubButtons = findExtendedWebElements(appSubscriptionButton.format(appName).getBy());
                if(!appSubButtons.get(appSubButtonIndex).getText().contains("Expired")) {
                    doneBtn.click();
                    manageSandboxAcct();
                } else {
                    break;
                }
            }
        }
    }
}
