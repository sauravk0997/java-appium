package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.*;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;

import java.awt.*;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAccountIOSPageBase extends DisneyPlusApplePageBase{

    private static final String CONTAINER_TEXT = "%s, %s ";
    private static final String MONTHLY = "Monthly";
    private static final String ANNUAL = "Annual";
    private static final String PREMIUM = "Premium";
    private static final String SUBSCRIPTION_MESSAGE = "Some account management features are only available " +
            "via the website. Create a Disney+ account and more at disneyplus.com/next";

    private ExtendedWebElement accountDetailsSection = getDynamicAccessibilityId(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_ACCOUNT.getText()));

    private ExtendedWebElement singleSubscriptionHeader = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE.getText()));

    private ExtendedWebElement stackedSubscriptionHeader = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_STACKED.getText()));

    private ExtendedWebElement switchToAnnualDescription = getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SWITCH_ANNUAL_DESCRIPTION.getText()));

    private ExtendedWebElement switchToAnnualBtn = getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SWITCH_ANNUAL_CTA.getText()));

    private ExtendedWebElement directBillingYearlyPausedContainer = getDynamicCellByLabel(String.format(CONTAINER_TEXT, "Disney+ Premium Annual", getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SETTINGS_PAUSED.getText())));
    private ExtendedWebElement directBillingMonthlyPausedContainer = getDynamicCellByLabel(String.format(CONTAINER_TEXT, "Disney+ Basic Monthly", getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SETTINGS_PAUSED.getText())));
    private ExtendedWebElement disneyPlusPremiumSubscription = getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.DISNEYPLUS_PREMIUM.getText()));

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == \"changeEmailCell\"`]/**/XCUIElementTypeButton")
    private ExtendedWebElement changeLink;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton/XCUIElementTypeImage[1]")
    private ExtendedWebElement editEmailButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton/XCUIElementTypeImage[2]")
    private ExtendedWebElement editPasswordButton;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == \"subscriptionChange\"`]/" +
            "**/XCUIElementTypeButton[2]")
    private ExtendedWebElement subscriptionMessage;
    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeToggle[2]")
    private ExtendedWebElement restrictedProfileToggle;
    @ExtendedFindBy(accessibilityId = "manageMyAccountCell")
    private ExtendedWebElement manageMyAcccountCell;
    @ExtendedFindBy(accessibilityId = "restrictProfileCreation")
    private ExtendedWebElement restrictProfileCreation;
    @ExtendedFindBy(accessibilityId = "subscriptionChange")
    private ExtendedWebElement subscriptionChange;
    @ExtendedFindBy(accessibilityId = "manageParentalControls")
    private ExtendedWebElement manageParentalControls;
    @ExtendedFindBy(accessibilityId = "manageDevices")
    private ExtendedWebElement manageDevices;

    private final ExtendedWebElement accessAndSecurityText =
            getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                    DictionaryKeys.ACCESS_SECURITY_HEADER.getText()));
    private final ExtendedWebElement manageDevicesText =
            getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.IDENTITY,
                    DictionaryKeys.DEVICE_MANAGEMENT_BUTTON_LABEL.getText()));

    public ExtendedWebElement getSubscriptionMessage() {
        return subscriptionMessage;
    }

    public ExtendedWebElement getManageMyAccountCell() {
        return manageMyAcccountCell;
    }

    public ExtendedWebElement getManageDevices() {
        return manageDevices;
    }

    public String getManageDevicesText() {
        return manageDevices.getText();
    }

    public boolean isSubscriptionMessageDisplayed() {
        return getSubscriptionMessage().getText().equals(SUBSCRIPTION_MESSAGE);
    }

    public boolean isMovistarSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_MOVISTAR.getText());
        return getStaticTextByLabel(title.concat(" " + MONTHLY)).isPresent();
    }

    public ExtendedWebElement getMovistarSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_MOVISTAR.getText()));
    }

    public boolean isDeutscheTelekomSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_DEUTSCHE_TELEKOM.getText());
        return getStaticTextByLabel(title.concat(" " + MONTHLY)).isPresent();
    }

    public ExtendedWebElement getDeTelekomSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_DEUTSCHE_TELEKOM.getText()));
    }

    public ExtendedWebElement getBamtechSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_BAMTECH.getText()),
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.SUBSCRIPTIONS_MESSAGE.getText())));
    }

    public ExtendedWebElement getBamtechBundleSubscriptionMessage() {
        String subscriptionMessage = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.UNIFIED_COMMERCE,
                DictionaryKeys.APPLE_LINK_OUT_SUBSCRIPTION_MGMT_LINK.getText());
        return getStaticTextByLabel(subscriptionMessage);
    }

    public ExtendedWebElement getMercardoLibreBrazilSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_MERCADOLIBRE_BR.getText()));
    }

    public boolean isMercadolibreMonthlySubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_MERCADOLIBRE_BR.getText());
        return getStaticTextByLabel(title.concat(" " + MONTHLY)).isPresent();
    }

    public boolean isGoogleSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_GOOGLE.getText());
        return getStaticTextByLabel(title.concat(" " + PREMIUM)).isPresent();
    }

    public ExtendedWebElement getGoogleSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_GOOGLE.getText()));
    }

    public boolean isRokuSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_ROKU.getText());
        return getStaticTextByLabel(title.concat(" " + ANNUAL)).isPresent();
    }

    public ExtendedWebElement getRokuSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_ROKU.getText()));
    }

    public boolean isAmazonSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_AMAZON.getText());
        return getStaticTextByLabel(title.concat(" " + ANNUAL)).isPresent();
    }

    public ExtendedWebElement getAmazonSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_AMAZON.getText()));
    }

    public boolean isVerizonSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_VERIZON.getText());
        return getStaticTextByLabel(title.concat(" " + MONTHLY)).isPresent();
    }

    public ExtendedWebElement getVerizonSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_VERIZON.getText()));
    }
    public ExtendedWebElement getVerizonBundleSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_VERIZON_BUNDLE.getText()),
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_VERIZON_BUNDLE.getText())));
    }

    public boolean isSkySubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_SKY.getText());
        return getStaticTextByLabel(title.concat(" " + MONTHLY)).isPresent();
    }

    public ExtendedWebElement getSkySubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_SKY.getText()));
    }

    public boolean isTelmexSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_TELMEX.getText());
        return getStaticTextByLabel(title.concat(" "+ MONTHLY)).isPresent();
    }

    public ExtendedWebElement getTelmexSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_TELMEX.getText()));
    }

    public boolean isBradescoSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_BRADESCO.getText());
        return getStaticTextByLabel(title.concat(" "+ MONTHLY)).isPresent();
    }
    public ExtendedWebElement getBradescoSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_BRADESCO.getText()));
    }

    public boolean isBradescoNextSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_BRADESCO_NEXT.getText());
        return getStaticTextByLabel(title.concat(" "+ MONTHLY)).isPresent();
    }

    public ExtendedWebElement getBradescoNextSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_BRADESCO_NEXT.getText()));
    }

    public boolean isTelefonicaVivoSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_TELEFONICA_VIVO.getText());
        return getStaticTextByLabel(title.concat(" "+ MONTHLY)).isPresent();
    }

    public ExtendedWebElement getTelefonicaVivoSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_TELEFONICA_VIVO.getText()));
    }

    public boolean isMercadolibreSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_MERCADOLIBRE.getText());
        return getStaticTextByLabel(title.concat(" "+ MONTHLY)).isPresent();
    }

    public ExtendedWebElement getMercadolibreSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_MERCADOLIBRE.getText()));
    }

    public boolean isCablevisionSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_CABLEVISION.getText());
        return getStaticTextByLabel(title.concat(" "+ MONTHLY)).isPresent();
    }

    public ExtendedWebElement getCablevisionSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_CABLEVISION.getText()));
    }

    public ExtendedWebElement getCanalSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_CANAL.getText()),
                getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_CANAL.getText())));
    }

    public ExtendedWebElement getRestrictProfileCreationContainer() {
        return restrictProfileCreation;
    }

    private ExtendedWebElement verifyAccountHeader = getDynamicXpath(
            String.format("//XCUIElementTypeStaticText[@name='%s']/../preceding-sibling::XCUIElementTypeImage",
                    getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VERIFY_ACCOUNT_HEADER.getText())));

    private ExtendedWebElement verifyAccountLink = getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VERIFY_ACCOUNT_CTA.getText()));


    public DisneyPlusAccountIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return accountDetailsSection.isElementPresent();
    }

    public void waitForAccountPageToOpen() {
        fluentWait(getDriver(), SIXTY_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Account page did not open")
                .until(it -> accountDetailsSection.isPresent(THREE_SEC_TIMEOUT));
    }

    public boolean isChangeLinkPresent(String text) {
        return getStaticTextByLabelContains(text).isPresent();
    }

    public boolean isChangeLinkActive(String text) {
        return changeLink.getAttribute("enabled").equals("true");
    }

    public boolean isSingleSubHeaderPresent() {
        return singleSubscriptionHeader.isElementPresent();
    }

    public boolean isStackedSubHeaderPresent() {
        return stackedSubscriptionHeader.isElementPresent();
    }

    public void clickChangeLink(String text) {
        pause(1);
        changeLink.click();
    }

    public boolean isDisneyPlusPremiumSubscriptionPresent() { return disneyPlusPremiumSubscription.isPresent(); }

    public void openDisneyPlusPremiumWebView() { disneyPlusPremiumSubscription.click(); }

    public boolean isO2SubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_O2.getText());
        return getStaticTextByLabel(title.concat(" "+ MONTHLY)).isPresent();
    }

    public ExtendedWebElement getO2Subscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_O2.getText()));
    }

    public boolean isO2SubscriptionMessagePresent() {
        return getO2Subscription().isPresent();
    }

    public void openO2SubscriptionWebview() {
        getO2Subscription().click();
    }

    public boolean isTelecomTIMSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_TELECOM_TIM.getText());
        return getStaticTextByLabel(title.concat(" "+ MONTHLY)).isPresent();
    }

    public ExtendedWebElement getTimSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_TELECOM_TIM.getText()));
    }

    public boolean isTelecomTIMSubscriptionMessagePresent() {
        return getTimSubscription().isPresent();
    }

    public void openTIMWebview() {
        getTimSubscription().click();
    }

    public boolean isMovistarSubscriptionMessagePresent() {
        return  getMovistarSubscription().isPresent();
    }

    public void openMovistarWebview() {
        getMovistarSubscription().click();
    }

    public boolean isDeutscheTelekomSubscriptionMessagePresent() {
        return getDeTelekomSubscription().isPresent();
    }

    public void openDeTelekomWebview() {
        getDeTelekomSubscription().click();
    }

    public boolean isBamtechSubscriptionMessagePresent() {
        return getBamtechSubscription().isPresent();
    }

    public void openBamtechWebview() {
        getBamtechSubscription().click();
    }

    public boolean isBamtechBundleSubscriptionMessagePresent() {
        return getBamtechBundleSubscriptionMessage().isPresent();
    }

    public boolean isMercadoLibreSubscriptionMessagePresent() {
        return getMercadolibreSubscription().isPresent();
    }

    public boolean isBamtechBundleSubscriptionTitlePresent() {
        String title = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS,
                DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_BAMTECH_HYBRID_BUNDLE.getText());
        return getTypeButtonByLabel(title).isPresent(TEN_SEC_TIMEOUT);
    }

    public void openBamtechBundleWebview() {
        getBamtechBundleSubscriptionMessage().click();
    }

    public ExtendedWebElement getHuluBundleSubscription() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_HULU_BUNDLE.getText()));
    }

    public boolean isHuluBundleSubscriptionMessagePresent() {
        return getHuluBundleSubscription().isPresent();
    }

    public void openHuluBundleWebview() {
        getHuluBundleSubscription().click();
    }

    public boolean isGoogleSubscriptionMessagePresent() {
        return getGoogleSubscription().isPresent();
    }

    public void openGoogleWebview() {
        getGoogleSubscription().click();
    }

    public boolean isRokuSubscriptionMessagePresent() {
        return getRokuSubscription().isPresent();
    }

    public void openRokuWebview() {
        getRokuSubscription().click();
    }

    public boolean isAmazonSubscriptionMessagePresent() {
        return getAmazonSubscription().isPresent();
    }

    public void openAmazonWebview() {
        getAmazonSubscription().click();
    }

    public boolean isVerizonSubscriptionMessagePresent() {
        return getVerizonSubscription().isPresent();
    }

    public void openVerizonWebview() {
        getVerizonSubscription().click();
    }

    public boolean isSkySubscriptionMessagePresent() {
        return getSkySubscription().isPresent();
    }

    public void openSkyWebview() {
        getSkySubscription().click();
    }

    public boolean isTelmexSubscriptionMessagePresent() {
        return getTelmexSubscription().isPresent();
    }

    public void openTelmexWebview() {
        getTelmexSubscription().click();
    }

    public boolean isBradescoSubscriptionMessagePresent() {
        return getBradescoSubscription().isPresent();
    }

    public void openBradescoWebview() {
        getBradescoSubscription().click();
    }

    public boolean isBradescoNextSubscriptionMessagePresent() {
        return getBradescoNextSubscription().isPresent();
    }

    public void openBradescoNextWebview() {
        getBradescoNextSubscription().click();
    }

    public boolean isTelefonicaVivoSubscriptionMessagePresent() {
        return getTelefonicaVivoSubscription().isPresent();
    }

    public boolean isTelefonicaAppPresentInAppStore() {
        return getStaticTextByLabel("Vivo").isElementPresent();
    }

    public void openVivoWebview() {
        getTelefonicaVivoSubscription().click();
    }


    public void openMercadoLibreWebview() {
        getMercadolibreSubscription().click();
    }

    public boolean isCablevisionSubscriptionMessagePresent() {
        return getCablevisionSubscription().isPresent();
    }

    public void openCablevisionWebview() {
        getCablevisionSubscription().click();
    }

    public boolean isMercadoLibreBrazilSubscriptionMessagePresent() {
        return getMercardoLibreBrazilSubscription().isPresent();
    }

    public void openMercadoLibreBrazilWebview() {
        getMercardoLibreBrazilSubscription().click();
    }

    public boolean isCanalSubscriptionMessagePresent() {
        return getCanalSubscription().isPresent();
    }

    public void openCanalWebview() {
        getCanalSubscription().click();
    }

    public boolean areSwitchToAnnualElementsDisplayed() {
        return switchToAnnualDescription.isElementPresent() && switchToAnnualBtn.isElementPresent();
    }

    public void clickSwitchToAnnualButton() {
        switchToAnnualBtn.click();
    }

    public boolean isRestrictProfilesContainerPresent() {
        return getRestrictProfileCreationContainer().isElementPresent();
    }

    public ExtendedWebElement getEditProfileLink() {
        return manageParentalControls;
    }

    public boolean isEditProfilesLinkPresent() {
        return getEditProfileLink().isElementPresent();
    }

    public void tapEditProfilesLink() {
        int heightValue = 60;
        ExtendedWebElement element = getEditProfileLink();
        Dimension dimension = element.getSize();
        Point location = element.getLocation();
        if (element.getSize().getHeight() > heightValue) {
            tap(location.getX() + 55 , location.getY() + dimension.getHeight()*2/3, 2);
        } else {
            element.click();
        }
    }

    public boolean isPrivacyChoicesLinkPresent() {
        return customHyperlinkByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PRIVACY_CHOICES_LINK.getText())).isElementPresent();
    }

    public boolean isVerifyAccountHeaderPresent() {
        return verifyAccountHeader.isPresent();
    }

    public boolean isVerifyAccountLinkPresent() {
        return verifyAccountLink.isPresent();
    }

    public void clickVerifyAccountLink() {
        verifyAccountLink.click();
    }

    public boolean isLogOutOfAllDevicesLinkPresent() {
        return getDynamicCellByName("logOutAllDevicesCell").isElementPresent();
    }

    public void clickLogOutOfAllDevices() {
        getDynamicCellByName("logOutAllDevicesCell").click();
    }

    public boolean isRestrictProfileCreationValueExpected(String expectedValue) {
        return getRestrictProfileCreationContainer()
                .getAttribute(Attributes.VALUE.getAttribute()).equals(expectedValue);
    }

    public void toggleRestrictProfileCreation() {
            restrictedProfileToggle.click();
    }

    public boolean isDirectBillingPausedSubscriptionDisplayed(DisneyPlusPaywallIOSPageBase.PlanType planName) {
        switch(planName) {
            case PREMIUM_MONTHLY:
                return directBillingMonthlyPausedContainer.isElementPresent();
            case PREMIUM_YEARLY:
                return directBillingYearlyPausedContainer.isElementPresent();
            default:
                return false;
        }
    }

    public void clickPausedDirectBillingContainer(DisneyPlusPaywallIOSPageBase.PlanType planName) {
        switch(planName) {
            case PREMIUM_MONTHLY:
                directBillingMonthlyPausedContainer.click();
                break;
            case PREMIUM_YEARLY:
                directBillingYearlyPausedContainer.click();
                break;
            default: throw new IllegalArgumentException(
                    String.format("Invalid plan name '%s'", planName));
        }
    }

    public ExtendedWebElement getBillingProviderCell(DisneySkuParameters sku) {
        switch (sku) {
            case DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE:
                return getVerizonSubscription();
            case DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE:
            case DISNEY_EXTERNAL_VERIZON_PROMO_BUNDLE_12MONTH:
            case DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE_PROMO:
            case DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE_UPGRADE:
                return getVerizonBundleSubscription();
            case DISNEY_IAP_GOOGLE_MONTHLY:
            case DISNEY_IAP_GOOGLE_YEARLY:
                return getGoogleSubscription();
            case DISNEY_IAP_APPLE_MONTHLY:
            case DISNEY_IAP_APPLE_YEARLY:
                return getBamtechSubscription();
            case DISNEY_PARTNER_BRADESCO_BANK_BR_STANDALONE:
                return getBradescoSubscription();
            case DISNEY_PARTNER_BRADESCO_NEXT_BR_STANDALONE:
                return getBradescoNextSubscription();
            case DISNEY_EXTERNAL_DETELEKOM_STANDALONE:
                return getDeTelekomSubscription();
            case DISNEY_PARTNER_TELECOM_AR_STANDAONE:
                return getCablevisionSubscription();
            case DISNEY_PARTNER_TIM_IT_STANDALONE:
                return getTimSubscription();
            case DISNEY_PARTNER_TELMEX_MX_STANDALONE:
                return getTelmexSubscription();
            case DISNEY_PARTNER_VIVO_BR_BUNDLE:
            case DISNEY_PARTNER_VIVO_BR_STANDALONE:
            case DISNEY_PARTNER_VIVO_BR_3MONTH_INTRO_PROMO:
            case DISNEY_PARTNER_VIVO_BR_3MONTH_PROMO:
            case DISNEY_PARTNER_VIVO_BR_BUNDLE_3MONTH_PROMO:
                return getTelefonicaVivoSubscription();
            case DISNEY_IAP_ROKU_YEARLY:
            case DISNEY_IAP_ROKU_MONTHLY:
                return getRokuSubscription();
            case HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH:
            case HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_SASH:
            case HULU_EXTERNAL_HULU_SUPER_BUNDLE_NOAH:
            case HULU_EXTERNAL_HULU_SUPER_BUNDLE_SASH:
                return getHuluBundleSubscription();
            case DISNEY_PARTNER_MERCADOLIBRE_MX_STANDALONE:
                return getMercadolibreSubscription();
            case DISNEY_PARTNER_MERCADOLIBRE_BR_STANDALONE:
                return getMercardoLibreBrazilSubscription();
            case DISNEY_IAP_AMAZON_MONTHLY:
            case DISNEY_IAP_AMAZON_YEARLY:
                return getAmazonSubscription();
            case DISNEY_EXTERNAL_O2_BUNDLE:
            case DISNEY_EXTERNAL_O2_PROMO_BUNDLE_3MONTH:
            case DISNEY_PARTNER_O2_DE_STANDALONE:
                return getO2Subscription();
            case DISNEY_EXTERNAL_MOVISTAR_STANDALONE:
                return getMovistarSubscription();
            case DISNEY_EXTERNAL_SKYUK_STANDALONE:
                return getSkySubscription();
            case DISNEY_EXTERNAL_CANAL_BUNDLE:
                return getCanalSubscription();
            default:
                return getBamtechBundleSubscriptionMessage();
        }
    }

    public boolean isBillingProviderCellPresent(DisneySkuParameters sku) {
        return isBillingProviderCellPresent(sku, 30);
    }

    public boolean isBillingProviderCellPresent(DisneySkuParameters sku, int timeout) {
        return getBillingProviderCell(sku).isPresent(timeout);
    }

    public void openBillingProvider(DisneySkuParameters sku) {
        getBillingProviderCell(sku).click();
    }

    public boolean isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType planName) {
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        switch (planName) {
            case BASIC:
                return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_BAMTECH_ADS.getText())).isPresent();
            case PREMIUM_MONTHLY:
            case PREMIUM_YEARLY:
                return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_APPLE.getText())).isPresent();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' Plan type is not a valid option", planName));
        }
    }

    public boolean isSubscriptionChangeFlashMessagePresent() {
        return getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SWITCH_ANNUAL_FLASH_MESSAGE.getText())).isPresent();
    }

    public void clickChangeBamtechBasicPlan() {
        getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_BAMTECH_ADS.getText())).click();
    }

    public void clickChangePremiumMonthlyPlan() {
        subscriptionChange.click();
    }

    public boolean isWebPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType planName) {
        DisneyPlusPaywallIOSPageBase paywallPage = initPage(DisneyPlusPaywallIOSPageBase.class);
        //Currently, all the web plans are monthly
        String expectedPlanName = (paywallPage.getPlanName(planName) + " " + (getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.SUBSCRIPTION_MONTHLY.getText())));
        Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE);
        return paywallPage.getStaticTextByLabel(expectedPlanName).isPresent();
    }

    public void tapEditEmailButton() {
        editEmailButton.click();
    }

    public boolean waitForManageMyDisneyAccountOverlayToOpen(UnifiedAccount account) {
        try {
            return fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Time out exception occurred")
                    .until(it -> getStaticTextByLabelContains(account.getEmail()).isPresent(THREE_SEC_TIMEOUT));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isSubscriptionCellPresent() {
        return subscriptionChange.isElementPresent();
    }

    public boolean isAccessAndSecurityTextPresent() {
        return accessAndSecurityText.isElementPresent();
    }

    public boolean isAccountManagementLinkPresent() {
        return getAccountManagementTextElement().isElementPresent();
    }

    public boolean isAccountManagementFAQWebViewDisplayed() {
        String acctMgmtFaqText = "Account Management FAQ";
        return staticTextByLabel.format(acctMgmtFaqText).isPresent();
    }

    public ExtendedWebElement getAccountManagementTextElement() {
        String dictValOfAccountManagement = getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.COMMUNICATION_SETTINGS.getText());
        String editProfileText = dictValOfAccountManagement.
                replaceAll("\\([^()]*\\)", "").replaceAll("[\\[\\]]","");
        return staticTextByLabel.format(editProfileText);
    }

    public boolean isAccountManagementTextPresent() {
        return getAccountManagementTextElement().isElementPresent();
    }

    public ExtendedWebElement getEditPasswordButton() {
        return editPasswordButton;
    }

    public void tapAccountManagementLink() {
        ExtendedWebElement element = getAccountManagementTextElement();
        int maxHeight = getDriver().manage().window().getSize().getHeight();
        int yCoordinate = element.getLocation().getY();
        if (maxHeight- yCoordinate < 150) {
            swipeUp(2, 1000);
        }
        element.click();
    }
}
