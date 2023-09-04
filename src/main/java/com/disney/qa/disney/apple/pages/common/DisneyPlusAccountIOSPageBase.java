package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAccountIOSPageBase extends DisneyPlusApplePageBase{

    private static final String CONTAINER_TEXT = "%s, %s ";

    @FindBy(xpath = "//XCUIElementTypeStaticText[@label='%s']/../following-sibling::*/XCUIElementTypeButton")
    private ExtendedWebElement changeLink;

    private ExtendedWebElement accountDetailsSection = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_ACCOUNT.getText()));

    private ExtendedWebElement singleSubscriptionHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE.getText()));

    private ExtendedWebElement stackedSubscriptionHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_STACKED.getText()));

    private ExtendedWebElement switchToAnnualDescription = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SWITCH_ANNUAL_DESCRIPTION.getText()));

    private ExtendedWebElement switchToAnnualBtn = getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SWITCH_ANNUAL_CTA.getText()));

    private ExtendedWebElement directBillingYearlyPausedContainer = getDynamicCellByLabel(String.format(CONTAINER_TEXT, "Disney+ Premium Annual", getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SETTINGS_PAUSED.getText())));
    private ExtendedWebElement directBillingMonthlyPausedContainer = getDynamicCellByLabel(String.format(CONTAINER_TEXT, "Disney+ Basic Monthly", getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SETTINGS_PAUSED.getText())));
    private ExtendedWebElement disneyPlusPremiumSubscription = getStaticTextByLabelContains(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.DISNEYPLUS_PREMIUM.getText()));

    private ExtendedWebElement o2Subscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_O2.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_O2.getText())));

    private ExtendedWebElement timSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_TELECOM_TIM.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_TELECOM_TIM.getText())));

    private ExtendedWebElement movistarSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_MOVISTAR.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_MOVISTAR.getText())));

    private ExtendedWebElement deTelekomSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_DEUTSCHE_TELEKOM.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_DEUTSCHE_TELEKOM.getText())));

    private ExtendedWebElement bamtechSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_BAMTECH.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.SUBSCRIPTIONS_MESSAGE.getText())));

    private ExtendedWebElement bamtechBundleSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.SUBSCRIPTIONS_TITLE_BAMTECH_BUNDLE.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.SUBSCRIPTIONS_BUNDLE_MESSAGE.getText())));

    private ExtendedWebElement huluBundleSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_HULU_BUNDLE.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_HULU_BUNDLE.getText())));

    private ExtendedWebElement googleSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_GOOGLE.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_GOOGLE.getText())));

    private ExtendedWebElement rokuSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_ROKU.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_ROKU.getText())));

    private ExtendedWebElement amazonSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_AMAZON.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_AMAZON.getText())));

    private ExtendedWebElement verizonSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_VERIZON.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_VERIZON.getText())));

    private ExtendedWebElement verizonBundleSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_VERIZON_BUNDLE.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_VERIZON_BUNDLE.getText())));

    private ExtendedWebElement skySubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_SKY.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_SKY.getText())));

    private ExtendedWebElement telmexSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_TELMEX.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_TELMEX.getText())));

    private ExtendedWebElement bradescoSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_BRADESCO.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_BRADESCO.getText())));

    private ExtendedWebElement bradescoNextSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_BRADESCO_NEXT.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_BRADESCO_NEXT.getText())));

    private ExtendedWebElement telefonicaVivoSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,DictionaryKeys.SUBSCRIPTIONS_TITLE_TELEFONICA_VIVO.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,DictionaryKeys.SUBSCRIPTIONS_MESSAGE_TELEFONICA_VIVO.getText())));

    private ExtendedWebElement mercadolibreSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_MERCADOLIBRE.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_MERCADOLIBRE.getText())));

    private ExtendedWebElement cablevisionSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_CABLEVISION.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_CABLEVISION.getText())));

    private ExtendedWebElement mercardoLibreBrazilSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_MERCADOLIBRE_BR.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_MERCADOLIBRE_BR.getText())));

    private ExtendedWebElement canalSubscription = getDynamicCellByLabel(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_CANAL.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_CANAL.getText())));

    private ExtendedWebElement restrictProfileCreationContainer = getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.RESTRICT_PROFILE_CREATION_TITLE.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.RESTRICT_PROFILE_CREATION_DESCRIPTION.getText())));

    private ExtendedWebElement verifyAccountHeader = getDynamicXpath(
            String.format("//XCUIElementTypeStaticText[@name='%s']/../preceding-sibling::XCUIElementTypeImage",
                    getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VERIFY_ACCOUNT_HEADER.getText())));

    private ExtendedWebElement verifyAccountLink = getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VERIFY_ACCOUNT_CTA.getText()));


    public DisneyPlusAccountIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return accountDetailsSection.isElementPresent();
    }

    public boolean isChangeLinkPresent(String text) {
        return changeLink.format(text).isPresent();
    }

    public boolean isChangeLinkActive(String text) {
        return changeLink.format(text).getAttribute("enabled").equals("true");
    }

    public boolean isSingleSubHeaderPresent() {
        return singleSubscriptionHeader.isElementPresent();
    }

    public boolean isStackedSubHeaderPresent() {
        return stackedSubscriptionHeader.isElementPresent();
    }

    public void clickChangeLink(String text) {
        pause(1);
        changeLink.format(text).click();
    }

    public boolean isDisneyPlusPremiumSubscriptionPresent() { return disneyPlusPremiumSubscription.isPresent(); }

    public void openDisneyPlusPremiumWebView() { disneyPlusPremiumSubscription.click(); }

    public boolean isO2SubscriptionMessagePresent() {
        return o2Subscription.isPresent();
    }

    public void openO2SubcriptionWebview() {
        o2Subscription.click();
    }

    public boolean isTelecomTIMSubscriptionMessagePresent() {
        return timSubscription.isPresent();
    }

    public void openTIMWebview() {
        timSubscription.click();
    }

    public boolean isMovistarSubscriptionMessagePresent() {
        return movistarSubscription.isPresent();
    }

    public void openMovistarWebview() {
        movistarSubscription.click();
    }

    public boolean isDeutscheTelekomSubscriptionMessagePresent() {
        return deTelekomSubscription.isPresent();
    }

    public void openDeTelekomWebview() {
        deTelekomSubscription.click();
    }

    public boolean isBamtechSubscriptionMessagePresent() {
        return bamtechSubscription.isPresent();
    }

    public void openBamtechWebview() {
        bamtechSubscription.click();
    }

    public boolean isBamtechBundleSubscriptionMessagePresent() {
        return bamtechBundleSubscription.isPresent();
    }

    public void openBamtechBundleWebview() {
        bamtechBundleSubscription.click();
    }

    public boolean isHuluBundleSubscriptionMessagePresent() {
        return huluBundleSubscription.isPresent();
    }

    public void openHuluBundleWebview() {
        huluBundleSubscription.click();
    }

    public boolean isGoogleSubscriptionMessagePresent() {
        return googleSubscription.isPresent();
    }

    public void openGoogleWebview() {
        googleSubscription.click();
    }

    public boolean isRokuSubscriptionMessagePresent() {
        return rokuSubscription.isPresent();
    }

    public void openRokuWebview() {
        rokuSubscription.click();
    }

    public boolean isAmazonSubscriptionMessagePresent() {
        return amazonSubscription.isPresent();
    }

    public void openAmazonWebview() {
        amazonSubscription.click();
    }

    public boolean isVerizonSubscriptionMessagePresent() {
        return verizonSubscription.isPresent();
    }

    public void openVerizonWebview() {
        verizonSubscription.click();
    }

    public boolean isSkySubscriptionMessagePresent() {
        return skySubscription.isPresent();
    }

    public void openSkyWebview() {
        skySubscription.click();
    }

    public boolean isTelmexSubscriptionMessagePresent() {
        return telmexSubscription.isPresent();
    }

    public void openTelmexWebview() {
        telmexSubscription.click();
    }

    public boolean isBradescoSubscriptionMessagePresent() {
        return bradescoSubscription.isPresent();
    }

    public void openBradescoWebview() {
        bradescoSubscription.click();
    }

    public boolean isBradescoNextSubscriptionMessagePresent() {
        return bradescoNextSubscription.isPresent();
    }

    public void openBradescoNextWebview() {
        bradescoNextSubscription.click();
    }

    public boolean isTelefonicaVivoSubscriptionMessagePresent() {
        return telefonicaVivoSubscription.isPresent();
    }

    public boolean isTelefonicaAppPresentInAppStore() {
        return getStaticTextByLabel("Vivo").isElementPresent();
    }

    public void openVivoWebview() {
        telefonicaVivoSubscription.click();
    }

    public boolean isMercadoLibreSubscriptionMessagePresent() {
        return mercadolibreSubscription.isPresent();
    }

    public void openMercadoLibreWebview() {
        mercadolibreSubscription.click();
    }

    public boolean isCablevisionSubscriptionMessagePresent() {
        return cablevisionSubscription.isPresent();
    }

    public void openCablevisionWebview() {
        cablevisionSubscription.click();
    }

    public boolean isMercadoLibreBrazilSubscriptionMessagePresent() {
        return mercardoLibreBrazilSubscription.isPresent();
    }

    public void openMercadoLibreBrazilWebview() {
        mercardoLibreBrazilSubscription.click();
    }

    public boolean isCanalSubscriptionMessagePresent() {
        return canalSubscription.isPresent();
    }

    public void openCanalWebview() {
        canalSubscription.click();
    }

    public boolean areSwitchToAnnualElementsDisplayed() {
        return switchToAnnualDescription.isElementPresent() && switchToAnnualBtn.isElementPresent();
    }

    public void clickSwitchToAnnualButton() {
        switchToAnnualBtn.click();
    }

    public boolean isRestrictProfilesContainerPresent() {
        return restrictProfileCreationContainer.isElementPresent();
    }

    public boolean isEditProfilesLinkPresent() {
        String dictValOfEditProfile = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCOUNT_EDIT_PROFILE_LINK.getText());
        //To manage parental controls for profiles on your account, visit [Edit Profiles](https://www.disneyplus.com/edit-profiles) and select a Profile.
        //Extracting the link text which is inside the '[]'
        String expectedHyperLinkText = dictValOfEditProfile.substring(dictValOfEditProfile.indexOf('[')+1,dictValOfEditProfile.indexOf(']'));
        return customHyperlinkByLabel.format(expectedHyperLinkText).isElementPresent();
    }

    public boolean isEditProfilesTextPresent() {
        String dictValOfEditProfile = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.ACCOUNT_EDIT_PROFILE_LINK.getText());
        //Removing the square bracket, rounded bracket and the link inside it to match the label displayed on the screen.
        String editProfileText = dictValOfEditProfile.replaceAll("\\([^()]*\\)", "").replaceAll("[\\[\\]]","");
        return textViewByLabel.format(editProfileText).isElementPresent();
    }

    public boolean isPrivacyChoicesLinkPresent() {
        return customHyperlinkByLabel.format(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.PRIVACY_CHOICES_LINK.getText())).isElementPresent();
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

    public boolean isRestrictProfileCreationEnabled() {
        return restrictProfileCreationContainer.getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equalsIgnoreCase(IOSUtils.ButtonStatus.ON.toString());
    }

    public void toggleRestrictProfileCreation(IOSUtils.ButtonStatus status) {
        if(!restrictProfileCreationContainer.getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equalsIgnoreCase(status.toString())) {
            new IOSUtils().clickElementAtLocation(restrictProfileCreationContainer, 35, 90);
        }
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
                return verizonSubscription;
            case DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE:
            case DISNEY_EXTERNAL_VERIZON_PROMO_BUNDLE_12MONTH:
            case DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE_PROMO:
            case DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE_UPGRADE:
                return verizonBundleSubscription;
            case DISNEY_IAP_GOOGLE_MONTHLY:
            case DISNEY_IAP_GOOGLE_YEARLY:
                return googleSubscription;
            case DISNEY_IAP_APPLE_MONTHLY:
            case DISNEY_IAP_APPLE_YEARLY:
                return bamtechSubscription;
            case DISNEY_PARTNER_BRADESCO_BANK_BR_STANDALONE:
                return bradescoSubscription;
            case DISNEY_PARTNER_BRADESCO_NEXT_BR_STANDALONE:
                return bradescoNextSubscription;
            case DISNEY_EXTERNAL_DETELEKOM_STANDALONE:
                return deTelekomSubscription;
            case DISNEY_PARTNER_TELECOM_AR_STANDAONE:
                return cablevisionSubscription;
            case DISNEY_PARTNER_TIM_IT_STANDALONE:
                return timSubscription;
            case DISNEY_PARTNER_TELMEX_MX_STANDALONE:
                return telmexSubscription;
            case DISNEY_PARTNER_VIVO_BR_BUNDLE:
            case DISNEY_PARTNER_VIVO_BR_STANDALONE:
            case DISNEY_PARTNER_VIVO_BR_3MONTH_INTRO_PROMO:
            case DISNEY_PARTNER_VIVO_BR_3MONTH_PROMO:
            case DISNEY_PARTNER_VIVO_BR_BUNDLE_3MONTH_PROMO:
                return telefonicaVivoSubscription;
            case DISNEY_IAP_ROKU_YEARLY:
            case DISNEY_IAP_ROKU_MONTHLY:
                return rokuSubscription;
            case HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH:
            case HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_SASH:
            case HULU_EXTERNAL_HULU_SUPER_BUNDLE_NOAH:
            case HULU_EXTERNAL_HULU_SUPER_BUNDLE_SASH:
                return huluBundleSubscription;
            case DISNEY_PARTNER_MERCADOLIBRE_MX_STANDALONE:
                return mercadolibreSubscription;
            case DISNEY_PARTNER_MERCADOLIBRE_BR_STANDALONE:
                return mercardoLibreBrazilSubscription;
            case DISNEY_IAP_AMAZON_MONTHLY:
            case DISNEY_IAP_AMAZON_YEARLY:
                return amazonSubscription;
            case DISNEY_EXTERNAL_O2_BUNDLE:
            case DISNEY_EXTERNAL_O2_PROMO_BUNDLE_3MONTH:
            case DISNEY_PARTNER_O2_DE_STANDALONE:
                return o2Subscription;
            case DISNEY_EXTERNAL_MOVISTAR_STANDALONE:
                return movistarSubscription;
            case DISNEY_EXTERNAL_SKYUK_STANDALONE:
                return skySubscription;
            case DISNEY_EXTERNAL_CANAL_BUNDLE:
                return canalSubscription;
            default:
                return bamtechBundleSubscription;
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
        UniversalUtils.captureAndUpload(getCastedDriver());
        switch (planName) {
            case BASIC:
                return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_BAMTECH_ADS.getText())).isPresent();
            case PREMIUM_MONTHLY:
            case PREMIUM_YEARLY:
                return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_APPLE.getText())).isPresent();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' Plan type is not a valid option", planName));
        }
    }

    public boolean isSubscriptionChangeFlashMessagePresent() {
        return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SWITCH_ANNUAL_FLASH_MESSAGE.getText())).isPresent();
    }

    public void clickChangeBamtechBasicPlan() {
        getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_BAMTECH_ADS.getText())).click();
    }

    public void clickChangePremiumMonthlyPlan() {
        getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_APPLE.getText())).click();
    }

    public boolean isWebPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType planName) {
        DisneyPlusPaywallIOSPageBase paywallPage = initPage(DisneyPlusPaywallIOSPageBase.class);
        //Currently, all the web plans are monthly
        String expectedPlanName = (paywallPage.getPlanName(planName) + " " + (getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.SUBSCRIPTION_MONTHLY.getText())));
        UniversalUtils.captureAndUpload(getCastedDriver());
        return paywallPage.getStaticTextByLabel(expectedPlanName).isPresent();
    }
}
