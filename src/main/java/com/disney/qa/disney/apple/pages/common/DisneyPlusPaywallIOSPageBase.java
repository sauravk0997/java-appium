package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPaywallIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name CONTAINS \"monthly\"`]")
    private ExtendedWebElement monthlySkuBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name CONTAINS \"yearly\"`]")
    private ExtendedWebElement yearlySkuBtn;

    @ExtendedFindBy(accessibilityId = "restoreButton")
    public ExtendedWebElement restoreBtn;

    @ExtendedFindBy(accessibilityId = "titleLabel")
    private ExtendedWebElement chooseYourPlanHeader;

    @ExtendedFindBy(accessibilityId = "subtitleLabel")
    private ExtendedWebElement chooseYourPlanSubHeader;

    @ExtendedFindBy(accessibilityId = "cardTitleLabel_%s")
    private ExtendedWebElement planCardTitleLabel;

    @ExtendedFindBy(accessibilityId = "cardSubTitleLabel_%s")
    private ExtendedWebElement planCardSubTitleLabel;

    @ExtendedFindBy(accessibilityId = "priceLabel_%s")
    private ExtendedWebElement priceLabel;

    @ExtendedFindBy(accessibilityId = "selectButton_%s")
    public ExtendedWebElement selectBtn;

    @ExtendedFindBy(accessibilityId = "upNextContentFooterLabel")
    private ExtendedWebElement footerLabel;

    @ExtendedFindBy(accessibilityId = "alertAction:secondaryButton")
    private ExtendedWebElement alertResumeBtn;

    @ExtendedFindBy(accessibilityId = "alertAction:defaultButton")
    private ExtendedWebElement alertFinishLaterBtn;

    @FindBy(xpath = "//*[contains(@name, 'productButton')]")
    private ExtendedWebElement productPurchaseBtn;

    @FindBy(xpath = "//*[contains(@name, 'Basic')]")
    private ExtendedWebElement BasicPurchaseBtn;

    @FindBy(xpath = "//*[@name='%s' or name ='%s']/following-sibling::XCUIElementTypeButton")
    private ExtendedWebElement purchasePlanBtn;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'Subscribe'`]")
    private ExtendedWebElement overlaySubscribeBtn;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeStaticText[`label == \"You’re currently subscribed to this.\"`]")
    private ExtendedWebElement subscribedText;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeSecureTextField")
    private ExtendedWebElement sandboxPasswordBox;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeTextField")
    private ExtendedWebElement sandboxIdBox;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'Sign In'`]")
    protected ExtendedWebElement sandboxSigninButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`label == 'OK'`]")
    protected ExtendedWebElement sandboxOkButton;
    private ExtendedWebElement restartSubscriptionHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.RESTART_TITLE.getText()));

    private ExtendedWebElement restartSubscriptionSubHeader = getTextViewByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUBSCRIBE_EXPIRED_COPY.getText()));

    private ExtendedWebElement paywallCancelBtn = getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CANCEL_BTN_NORMAL.getText()));

    private ExtendedWebElement startStreamingText = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.BILLING_INFO_TITLE.getText()));

    private ExtendedWebElement cancelAnytimeText = getTextViewByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.PAYWALL_CANCEL_ANYTIME.getText()));

    private ExtendedWebElement annualSwitchHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.PAYWALL_SWITCH_ANNUAL_TITLE.getText()));

    private ExtendedWebElement alertFinishLaterHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.INTERRUPT_SUBSCRIPTION_TITLE.getText()));

    private ExtendedWebElement alertFinishLaterText = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.INTERRUPT_SUBSCRIPTION.getText()));


    public DisneyPlusPaywallIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return restoreBtn.isPresent();
    }

    public boolean isSwitchToAnnualHeaderDisplayed() {
        return annualSwitchHeader.isElementPresent();
    }

    public boolean isSwitchToAnnualCopyDisplayed() {
        return staticTypeTextViewValue.format(getDictionary()
                        .formatPlaceholderString(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.PAYWALL_SWITCH_ANNUAL_PRICE.getText()), Map.of("PRICE", "---", "DURATION", "---")))
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

    public enum PlanType {
        BASIC,
        BUNDLE_TRIO_BASIC,
        BUNDLE_TRIO_PREMIUM,
        LEGACY_BUNDLE,
        PREMIUM_MONTHLY,
        PREMIUM_YEARLY;
    }

    public String getPlanName(PlanType planType) {
        switch (planType) {
            case BASIC:
                return "Disney+ Basic";
            case BUNDLE_TRIO_BASIC:
                return getDictionary()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_BAMTECH_ADS_BUNDLE.getText());
            case BUNDLE_TRIO_PREMIUM:
                return getDictionary()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_BAMTECH_NOADS_BUNDLE.getText());
            case LEGACY_BUNDLE:
                return getDictionary()
                        .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_BAMTECH_HYBRID_BUNDLE.getText());
            case PREMIUM_MONTHLY:
                return "Disney+ Premium";
            case PREMIUM_YEARLY:
                return "Disney+ Premium";

            default:
                throw new IllegalArgumentException(
                        String.format("'%s' Plan type is not a valid option", planType));
        }
    }

    public boolean verifyPlanCardFor(PlanType planType) {
        return planCardTitleLabel.format(getPlanName(planType)).isElementPresent() &&
                planCardSubTitleLabel.format(getPlanName(planType)).isElementPresent() &&
                //priceLabel.format(getPlanName(planType)).isElementPresent() &&
                selectBtn.format(getPlanName(planType)).isElementPresent();
    }

    public ExtendedWebElement getSelectButtonFor(PlanType planName) {
        return selectBtn.format(getPlanName(planName));
    }

    public boolean isChooseYourPlanHeaderPresent() {
        String chooseYourPlanHeaderText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUB_SELECTOR_TITLE.getText());
        return chooseYourPlanHeader.getText().toLowerCase().contains(chooseYourPlanHeaderText.toLowerCase());
    }

    public boolean isChooseYourPlanSubHeaderPresent() {
        String subHeaderLabel = getDictionary().
                getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUB_SELECTOR_SUBCOPY.getText()).trim();
        return chooseYourPlanSubHeader.getText().contains(subHeaderLabel.replace(".",""));
    }

    public boolean isFooterLabelPresent() {
        String footerLabelText = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUB_SELECTOR_CANCEL_ANYTIME_DISCLAIMER.getText());
        return staticTextLabelContains.format(footerLabelText).isPresent();
    }

    public void tapFinishLaterButton() {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        alertFinishLaterBtn.click();
    }

    public void clickBasicPlan() {
        purchasePlanBtn.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUB_SELECTOR_STANDALONE_ADS_CARD_TITLE.getText()),
                DictionaryKeys.SUB_SELECTOR_STANDALONE_ADS_CARD_TITLE.getText()).click();
    }
    public void clickBasicPlanButton(){
        BasicPurchaseBtn.click();
    }

    public void clickPremiumPlan() {
        purchasePlanBtn.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUB_SELECTOR_STANDALONE_NO_ADS_CARD_TITLE.getText()),
                DictionaryKeys.SUB_SELECTOR_STANDALONE_NO_ADS_CARD_TITLE.getText()).click();
    }

    public void clickPurchaseButton() {
        productPurchaseBtn.click();
    }

    public void clickPurchaseButton(PlanType planType) {
        if (planType.equals(PlanType.PREMIUM_MONTHLY)) {
            dynamicBtnFindByName.format("productButton-com.disney.monthly.premium.apple").click();
        } else if (planType.equals(PlanType.PREMIUM_YEARLY)) {
            dynamicBtnFindByName.format("productButton-com.disney.yearly.premium.apple").click();
        } else {
            productPurchaseBtn.click();
        }
    }

    public boolean isPurchaseButtonPresent() {
        return productPurchaseBtn.isElementPresent();
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
    public void fillSandboxId(String id,String password) {
        if (sandboxIdBox.isPresent()) {
            sandboxIdBox.type(id);
            sandboxPasswordBox.type(password);
            sandboxOkButton.click();
        }
    }

    public void dismissPaywallErrorAlert() {
        if (isViewAlertPresent()) {
            alertFinishLaterBtn.click();
        }
    }

    public boolean isStartStreamingTextPresent(){
        return staticTextByLabel.format("Start streaming today").isPresent();
    }

    public void clickBundleSelectButton() {
        dynamicBtnFindByNameContains.format("selectButton").click();
    }

    public void clickPremiumYearlyRowButton() {
            getDynamicRowButtonLabel(getDictionary()
                    .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.PLAN_SWITCH_IAP_ANNUAL.getText()),2).click();
    }
}
