package com.disney.qa.common.utils.ios_settings;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.utils.IOSUtils;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IOSSettingsMenuBase extends DisneyAbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label CONTAINS \"Apple ID:\"`]")
    private ExtendedWebElement appleIDCell;

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

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeSecureTextField")
    private ExtendedWebElement sandboxPasswordBox;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'Sign In'`]")
    protected ExtendedWebElement sandboxSigninButton;

    @FindBy(id = "Subscriptions")
    protected ExtendedWebElement subscriptionsButton;

    @FindBy(id = "Retry")
    protected ExtendedWebElement retryButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label CONTAINS \"13.99 ✓\"`]")
    private ExtendedWebElement premiumMonthlyPriceCheckmark;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label CONTAINS \"139.99 ✓\"`]")
    private ExtendedWebElement premiumYearlyPriceCheckmark;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label CONTAINS \"7.99 ✓\"`]")
    private ExtendedWebElement basicMonthlyPriceCheckmark;

    public IOSSettingsMenuBase(WebDriver driver) {
        super(driver);
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
        launchApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        if(!isOpened()) {
            terminateApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
            launchApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        }
    }

    public String getDeviceSandBoxAppleID() {
        launchSettings();
        swipeInContainerTillElementIsPresent(settingsContainer, appStoreTab, 3, Direction.UP);
        appStoreTab.click();
        swipe(sandboxAccount);
        return appleIDCell.getText().split(":")[1];
    }

    public void cancelActiveEntitlement(String appName) {
        boolean waitForExpiryTime = false;
        int appSubButtonIndex = 9999;
        List<ExtendedWebElement> appSubButtons = new LinkedList<>();
        launchSettings();
        swipeInContainerTillElementIsPresent(settingsContainer, appStoreTab, 3, Direction.UP);
        appStoreTab.click();
        manageSandboxAcct();

        if(subscriptionsButton.isElementPresent()) {
            subscriptionsButton.click();
        }

        if (cancelSubscriptionBtn.isElementPresent()) {
            cancelActiveSubscription();
            doneBtn.click();
            waitForExpiryTime = true;
        }

        if(waitForExpiryTime) {
            waitForEntitlementExpiration(appSubButtons, appName, appSubButtonIndex);
        }

        terminateApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
    }

    public void navigateToManageSubscription() {
        launchSettings();
        swipeInContainerTillElementIsPresent(settingsContainer, appStoreTab, 3, Direction.UP);
        appStoreTab.click();
        manageSandboxAcct();
        if(subscriptionsButton.isElementPresent()) {
            subscriptionsButton.click();
            retryButton.clickIfPresent(5);
            scrollDown();
        }
    }

    protected void manageSandboxAcct() {
        swipe(sandboxAccount);
        sandboxAccount.click();
        manageButton.click();
        try {
            submitSandboxPassword(R.TESTDATA.getDecrypted("sandbox_pw"));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
    }

    protected void cancelActiveSubscription() {
        cancelSubscriptionBtn.click();
        acceptAlert();
        LOGGER.info("Active Subscription cancelled.");
    }

    protected void waitForEntitlementExpiration(List<ExtendedWebElement> appSubButtons, String appName, int appSubButtonIndex) {
        LOGGER.info("Refreshing page until expired indicator shows for up to 4 minutes...");
        Instant expireTime = Instant.now().plus(4, ChronoUnit.MINUTES);
        if(appSubButtons.isEmpty()) {
            sandboxAccount.click();
            manageButton.click();
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

    public boolean isPremiumMonthlyPriceCheckmarkPresent() {
        return premiumMonthlyPriceCheckmark.isElementPresent();
    }

    public boolean isPremiumYearlyPriceCheckmarkPresent() {
        return premiumYearlyPriceCheckmark.isElementPresent();
    }

    public boolean isBasicMonthlyPriceCheckmarkPresent() {
        return basicMonthlyPriceCheckmark.isElementPresent();
    }
}
