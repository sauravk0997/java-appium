package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAccountIOSPageBase extends DisneyPlusApplePageBase{

    private static final String CONTAINER_TEXT = "%s, %s ";

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`name == \"changeEmailCell\"`]/**/XCUIElementTypeButton")
    private ExtendedWebElement changeLink;

    private ExtendedWebElement accountDetailsSection = getDynamicAccessibilityId(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.NAV_ACCOUNT.getText()));

    private ExtendedWebElement singleSubscriptionHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE.getText()));

    private ExtendedWebElement stackedSubscriptionHeader = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_STACKED.getText()));

    private ExtendedWebElement switchToAnnualDescription = getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SWITCH_ANNUAL_DESCRIPTION.getText()));

    private ExtendedWebElement switchToAnnualBtn = getTypeButtonByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SWITCH_ANNUAL_CTA.getText()));

    private ExtendedWebElement directBillingYearlyPausedContainer = getDynamicCellByLabel(String.format(CONTAINER_TEXT, "Disney+ Premium Annual", getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SETTINGS_PAUSED.getText())));
    private ExtendedWebElement directBillingMonthlyPausedContainer = getDynamicCellByLabel(String.format(CONTAINER_TEXT, "Disney+ Basic Monthly", getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SETTINGS_PAUSED.getText())));
    private ExtendedWebElement disneyPlusPremiumSubscription = getStaticTextByLabelContains(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.DISNEYPLUS_PREMIUM.getText()));

    public ExtendedWebElement getMovistarSubscription() {
    return getDynamicAccessibilityId((String.format(CONTAINER_TEXT,
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_MOVISTAR.getText()),
            getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_MOVISTAR.getText()))));
    }

    public ExtendedWebElement getDeTelekomSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_DEUTSCHE_TELEKOM.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_DEUTSCHE_TELEKOM.getText())));
    }

    public ExtendedWebElement getBamtechSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_BAMTECH.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.SUBSCRIPTIONS_MESSAGE.getText())));
    }

    public ExtendedWebElement getBamtechBundleSubscriptionMessage() {
        return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.SUBSCRIPTIONS_BUNDLE_MESSAGE.getText()));
    }

    public ExtendedWebElement getGoogleSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_GOOGLE.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_GOOGLE.getText())));
    }
    public ExtendedWebElement getRokuSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_ROKU.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_ROKU.getText())));
    }
    public ExtendedWebElement getAmazonSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_AMAZON.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_AMAZON.getText())));
    }
    public ExtendedWebElement getVerizonSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_VERIZON.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_VERIZON.getText())));
    }
    public ExtendedWebElement getVerizonBundleSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_VERIZON_BUNDLE.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_VERIZON_BUNDLE.getText())));
    }
    public ExtendedWebElement getSkySubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_SKY.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_SKY.getText())));
    }

    public ExtendedWebElement getTelmexSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_TELMEX.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_TELMEX.getText())));
    }

    public ExtendedWebElement getBradescoSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_BRADESCO.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_BRADESCO.getText())));
    }
    public ExtendedWebElement getBradescoNextSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_BRADESCO_NEXT.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_BRADESCO_NEXT.getText())));
    }
    public ExtendedWebElement getTelefonicaVivoSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_TELEFONICA_VIVO.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_TELEFONICA_VIVO.getText())));
    }
    public ExtendedWebElement getMercadolibreSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_MERCADOLIBRE.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_MERCADOLIBRE.getText())));
    }

    public ExtendedWebElement getCablevisionSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_CABLEVISION.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_CABLEVISION.getText())));
    }

    public ExtendedWebElement getMercardoLibreBrazilSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_MERCADOLIBRE_BR.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_MERCADOLIBRE_BR.getText())));
    }
    public ExtendedWebElement getCanalSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_CANAL.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_CANAL.getText())));
    }

    public ExtendedWebElement getRestrictProfileCreationContainer() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.RESTRICT_PROFILE_CREATION_TITLE.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PCON, DictionaryKeys.RESTRICT_PROFILE_CREATION_DESCRIPTION.getText())));
    }

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
        return changeLink.isPresent();
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

    public ExtendedWebElement getO2Subscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_O2.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_O2.getText())));
    }

    public boolean isO2SubscriptionMessagePresent() {
        return getO2Subscription().isPresent();
    }

    public void openO2SubscriptionWebview() {
        getO2Subscription().click();
    }

    public ExtendedWebElement getTimSubscription() {
        return getDynamicAccessibilityId(String.format(CONTAINER_TEXT,
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE_TELECOM_TIM.getText()),
                getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_TELECOM_TIM.getText())));
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

    public boolean isBamtechBundleMonthlySubscriptionTitlePresent() {
        String title = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.SUBSCRIPTIONS, DictionaryKeys.ACCOUNT_SUBSCRIPTION_TITLE_BAMTECH_HYBRID_BUNDLE.getText());
        return getStaticTextByLabel(title.concat(" Monthly")).isPresent();
    }

    public void openBamtechBundleWebview() {
        getBamtechBundleSubscriptionMessage().click();
    }

    public ExtendedWebElement getHuluBundleSubscription() {
        return getStaticTextByLabel(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_MESSAGE_HULU_BUNDLE.getText()));
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

    public boolean isMercadoLibreSubscriptionMessagePresent() {
        return getMercadolibreSubscription().isPresent();
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
        return getRestrictProfileCreationContainer().getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equalsIgnoreCase(IOSUtils.ButtonStatus.ON.toString());
    }

    public void toggleRestrictProfileCreation(IOSUtils.ButtonStatus status) {
        if(!getRestrictProfileCreationContainer().getAttribute(IOSUtils.Attributes.VALUE.getAttribute()).equalsIgnoreCase(status.toString())) {
            new IOSUtils().clickElementAtLocation(getRestrictProfileCreationContainer(), 35, 90);
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
