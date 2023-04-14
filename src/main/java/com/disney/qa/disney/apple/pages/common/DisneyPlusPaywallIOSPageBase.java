package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPaywallIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name CONTAINS \"monthly\"`]")
    private ExtendedWebElement monthlySkuBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name CONTAINS \"yearly\"`]")
    private ExtendedWebElement yearlySkuBtn;

    @ExtendedFindBy(accessibilityId = "restoreButton")
    public ExtendedWebElement restoreBtn;

    @ExtendedFindBy(accessibilityId = "alertAction:secondaryButton")
    private ExtendedWebElement alertResumeBtn;

    @ExtendedFindBy(accessibilityId = "alertAction:defaultButton")
    private ExtendedWebElement alertFinishLaterBtn;

    @FindBy(xpath = "//*[contains(@name, 'productButton')]")
    private ExtendedWebElement productPurchaseBtn;

    private ExtendedWebElement restartSubscriptionHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.RESTART_TITLE.getText()));

    private ExtendedWebElement restartSubscriptionSubHeader = getTextViewByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUBSCRIBE_EXPIRED_COPY.getText()));

    private ExtendedWebElement paywallCancelBtn = getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CANCEL_BTN_NORMAL.getText()));

    private ExtendedWebElement startStreamingText = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.BILLING_INFO_TITLE.getText()));

    private ExtendedWebElement cancelAnytimeText = getTextViewByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.PAYWALL_CANCEL_ANYTIME.getText()));

    private ExtendedWebElement annualSwitchHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.PAYWALL_SWITCH_ANNUAL_TITLE.getText()));

    private ExtendedWebElement alertFinishLaterHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.INTERRUPT_SUBSCRIPTION_TITLE.getText()));

    private ExtendedWebElement alertFinishLaterText = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.INTERRUPT_SUBSCRIPTION.getText()));

    @FindBy(xpath = "//*[@name='%s' or name ='%s']/following-sibling::XCUIElementTypeButton")
    private ExtendedWebElement purchasePlanBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'Subscribe'`]")
    private ExtendedWebElement overlaySubscribeBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeSecureTextField[`value == 'Password'`]")
    private ExtendedWebElement sandboxPasswordBox;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'Sign In'`]")
    protected ExtendedWebElement sandboxSigninButton;

    public DisneyPlusPaywallIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return yearlySkuBtn.isElementPresent();
    }

    public boolean isSwitchToAnnualHeaderDisplayed() {
        return annualSwitchHeader.isElementPresent();
    }

    public boolean isSwitchToAnnualCopyDisplayed() {
        return staticTypeTextViewValue.format(getDictionary()
                .replaceValuePlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.PAYWALL_SWITCH_ANNUAL_COPY.getText()), "---", "---"))
                .isElementPresent();
    }

    public void clickPaywallCancelButton() {
        restoreBtn.isElementPresent();
        paywallCancelBtn.click();
    }

    public boolean isPaywallCancelButtonDisplayed() {
        return paywallCancelBtn.isElementPresent();
    }

    public boolean isStartStreamingTextDisplayed() {
        return startStreamingText.isElementPresent();
    }

    public boolean isCancelAnytimeTextDisplayed() {
        return cancelAnytimeText.isElementPresent();
    }

    public boolean isRestartsSubscriptionHeaderDisplayed() {
        return restartSubscriptionHeader.isElementPresent();
    }

    public boolean isRestartsSubscriptionSubHeaderDisplayed() {
        return restartSubscriptionSubHeader.isElementPresent();
    }

    public boolean isMonthlySkuButtonPresent() {
        return monthlySkuBtn.isElementPresent();
    }

    public boolean isYearlySkuButtonPresent() {
        return yearlySkuBtn.isElementPresent();
    }

    public boolean isFinishLaterHeaderPresent() {
        return alertFinishLaterHeader.isElementPresent();
    }

    public boolean isFinishLaterTextPresent() {
        return alertFinishLaterText.isElementPresent();
    }

    public boolean isResumeButtonPresent() {
        return alertResumeBtn.isElementPresent();
    }

    public boolean isFinishLaterButtonPresent() {
        return alertFinishLaterBtn.isElementPresent();
    }

    public void clickBasicPlan() {
        purchasePlanBtn.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUB_SELECTOR_STANDALONE_ADS_CARD_TITLE.getText()),
                DictionaryKeys.SUB_SELECTOR_STANDALONE_ADS_CARD_TITLE.getText()).click();
    }

    public void clickPremiumPlan() {
        purchasePlanBtn.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUB_SELECTOR_STANDALONE_NO_ADS_CARD_TITLE.getText()),
                DictionaryKeys.SUB_SELECTOR_STANDALONE_NO_ADS_CARD_TITLE.getText()).click();
    }

    public void clickPurchaseButton() {
        productPurchaseBtn.click();
    }

    public void waitForSubscribeOverlay() {
        waitUntil(ExpectedConditions.visibilityOfElementLocated(overlaySubscribeBtn.getBy()), 30);
    }

    public void clickOverlaySubscribeButton() {
        overlaySubscribeBtn.click();
    }

    public void submitSandboxPassword(String password) {
        sandboxPasswordBox.type(password);
        sandboxSigninButton.click();
    }

    public void dismissPaywallErrorAlert() {
        if (isViewAlertPresent()) {
            alertFinishLaterBtn.click();
        }
    }
}
