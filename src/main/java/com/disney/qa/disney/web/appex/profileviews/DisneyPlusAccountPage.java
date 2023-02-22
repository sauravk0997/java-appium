package com.disney.qa.disney.web.appex.profileviews;

import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.DisneyProductData;
import com.disney.qa.disney.web.DisneyWebKeys;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.util.List;
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusAccountPage extends DisneyPlusBaseProfileViewsPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String FINISH_LATER = "FINISH LATER";
    public static final String IS_BUNDLE_COUNTRY = "isBundleCountry";

    @FindBy(xpath = "//h2[contains(text(),'Account')]")
    private ExtendedWebElement pageTitle;

    @FindBy(id = "id = %s")
    private ExtendedWebElement formattedId;

    @FindBy(xpath = "//*[@data-testid='dropdown-option-4-cancelSubscription']")
    private ExtendedWebElement cancelSubscriptionDropdown;

    @FindBy(xpath = "//*[@data-testid='cancel-subscription-button']")
    private ExtendedWebElement cancelSubscriptionButtonMobile;

    @FindBy(xpath = "//*[@data-testid='account-detail-item-wrapper-email']")
    private ExtendedWebElement emailChange;

    @FindBy(xpath = "//*[@data-testid='account-detail-item-wrapper-password']")
    private ExtendedWebElement passwordChange;

    @FindBy(xpath = "//*[text()='%s']")
    private ExtendedWebElement getText;

    @FindBy(xpath = "//*[@data-testid='cancellationSurvey']")
    private ExtendedWebElement cancellationSurvey;

    @FindBy(xpath = "//*[starts-with(@id, 'survey')]")
    private List <ExtendedWebElement> cancellationSurveylist;

    @FindBy(id = "surveyWatchedEverything")
    private ExtendedWebElement surveyWatchedEverything;

    @FindBy(id = "cancel_subscription_recommend-bundle")
    private ExtendedWebElement cancelSubscriptionRecommendBundle;

    @FindBy(xpath = "//*[@aria-label='arrowRight']")
    private ExtendedWebElement changePaymentOptionButton;

    @FindBy(xpath = "//*[contains(@data-testid,'internal-subscription-d2c')]")
    private ExtendedWebElement internalD2CSubscriptionContains;

    // Disney+ Basic (Monthly)
    @FindBy(xpath = "//*[@data-testid='internal-subscription-d2c-disney_plus_monthly_us_web_with_ads']")
    private ExtendedWebElement internalD2CStandaloneWithAdsCta;

    // Disney+ Premium (Monthly)
    @FindBy(xpath = "//*[@data-testid='internal-subscription-d2c-1999199999999910151999000_disney']")
    private ExtendedWebElement internalD2CSubscriptionCta;

    @FindBy(xpath = "//*[@data-testid='internal-subscription-d2c-1999199999999917051999000_disney']")
    private ExtendedWebElement internalSuperBundleD2CSubscriptionCta;

    @FindBy(xpath = "//*[@data-testid='internal-subscription-d2c-disney_with_ads_hulu_with_ads_and_espn']")
    private ExtendedWebElement internalBundleMonthlyD2CSubscriptionCta;

    @FindBy(xpath = "//*[@data-testid='internal-subscription-d2c-1999199999999910121999000_disney']")
    private ExtendedWebElement internalAnnualD2CSubscriptionCta;

    @FindBy(xpath = "//*[@data-testid='internal-subscription-nopay-1999199999999910121999000_disney']")
    private ExtendedWebElement internalMonthlyNopaySubscription;

    @FindBy(xpath = "//*[@data-testid='internal-subscription-d2c-disney_hulu_no_ads_and_espn_web_f4c9724']")
    private ExtendedWebElement internalD2CBundleMonthlyNoAdsCta;

    @FindBy(xpath = "//*[contains(@data-testid,'internal-subscription-nopay')]")
    private ExtendedWebElement internalNopaySubscription;

    @FindBy(xpath = "//*[@data-testid='external-subscription-VERIZON']")
    private ExtendedWebElement externalVerizonSubscription;

    @FindBy(xpath = "//*[@data-testid='external-subscription-APPLE']")
    private ExtendedWebElement externalAppleSubscription;

    @FindBy(xpath = "//*[@data-testid='external-subscription-BAMTECH']")
    private ExtendedWebElement externalBamtechSubscription;

    @FindBy(xpath = "//*[contains(@data-testid,'external-subscription')]")
    private ExtendedWebElement externalSubscription;

    @FindBy(xpath = "//*[@data-testid='subscription-details-cancel-subscription']")
    private ExtendedWebElement subscriptionDetailsCancelSubscriptionBtn;

    @FindBy(xpath = "//button[@data-testid='subscription-details-change-payment']")
    private ExtendedWebElement subscriptionDetailsChangePaymentBtn;

    @FindBy(xpath = "//*[@data-testid='subscription-details-paypalexpress']")
    private ExtendedWebElement lastPaymentPayPal;

    @FindBy(xpath = "//*[@data-testid='subscription-details-paymentcard']")
    private ExtendedWebElement lastPaymentCreditCard;

    @FindBy(xpath = "//*[@data-testid='subscription-details-klarnapayment']")
    private ExtendedWebElement lastPaymentKlarna;

    @FindBy(xpath = "//*[@data-testid='subscription-details-idealpayment']")
    private ExtendedWebElement lastPaymentIdeal;

    @FindBy(xpath = "//*[@data-testid='subscription-details-mercadopagopayment']")
    private ExtendedWebElement lastPaymentMercadoPrago;

    @FindBy(xpath = "//*[@data-testid='cancel-submit']")
    public ExtendedWebElement cancelSubmitBtn;

    @FindBy(id = "account_settings_index")
    private ExtendedWebElement accountSettingsIndex;

    @FindBy(xpath = "//*[@data-testid='survey-modal-link']")
    private ExtendedWebElement cancellationSurveyModalLink;

    @FindBy(xpath = "//*[@data-testid='take-survey-modal']")
    private ExtendedWebElement cancellationSurveyModal;

    @FindBy(xpath = "//*[@data-testid='Other']")
    private ExtendedWebElement cancellationSurveyModalOtherButton;

    @FindBy(xpath = "//*[@data-testid='survey-modal-submit']")
    private ExtendedWebElement cancellationSurveyModalSubmitButton;

    @FindBy(xpath = "//*[@data-testid='complete-cancellation-button']")
    private ExtendedWebElement completeCancellationBtn;

    @FindBy(xpath = "//*[contains(@data-testid,'cancellationConfirmationGoToHomeLink')]")
    private ExtendedWebElement cancellationConfirmationGoHomeBtn;

    @FindBy(xpath = "//*[@data-testid='restart-subscription-button']")
    private ExtendedWebElement restartSubscriptionBtn;

    @FindBy(xpath = "//*[@data-testid='subscription-canceled']")
    private ExtendedWebElement subscriptionCancelled;

    @FindBy(xpath = "//*[@data-testid='restart-modal-button']")
    private ExtendedWebElement restartModalBtn;

    @FindBy(xpath = "//*[contains(@data-testid,'cancellationConfirmationBlurb')]")
    private ExtendedWebElement cancellationConfirmationBlurb;

    @FindBy(xpath = "//*[contains(@data-testid,'restart-subscription-banner')]")
    private ExtendedWebElement restartSubscriptionBanner;

    @FindBy(xpath = "//*[@data-testid='change-your-mind-body']")
    private ExtendedWebElement changeYourMindBody;

    @FindBy(xpath = "//*[@data-testid='restart-legal-modal']")
    private ExtendedWebElement restartModal;

    @FindBy(xpath = "//*[@data-testid='upgrade-to-bundle-link']")
    private ExtendedWebElement upgradeToBundleCta;

    @FindBy(xpath = "//*[@data-testid='upgrade-to-bundle-card']")
    private ExtendedWebElement upgradeToBundleCard;

    @FindBy(xpath = "//*[@data-testid='upgrade-to-combo-bundle-link']")
    private ExtendedWebElement upgradeToComboCta;

    @FindBy(xpath = "//*[@data-testid='upgrade-to-mega-bundle-link']")
    private ExtendedWebElement upgradeToMegaBundleCta;

    @FindBy(xpath = "//*[@data-testid='upgrade-link']")
    private ExtendedWebElement changeSubscriptionUpgradeCta;

    @FindBy(xpath = "//*[@data-testid='upgrade-from-sash-to-noah-bundle-link']")
    private ExtendedWebElement upgradeSashToNoahCta;

    @FindBy(xpath = "//*[@data-testid='bundle-upgrade']")
    private ExtendedWebElement changeSubscriptionBundleUpgradeCta;

    @FindBy(xpath = "//*[@data-testid='combo-bundle-upgrade']")
    private ExtendedWebElement changeSubscriptionComboBundleUpgradeCta;

    @FindBy(xpath = "//*[@data-testid='annual-upgrade']")
    private ExtendedWebElement changeSubscriptionAnnualUpgradeCta;

    @FindBy(xpath = "//*[@data-testid='bundle-success']")
    private ExtendedWebElement bundleSuccessPage;

    @FindBy(xpath = "//*[@data-testid='upgrade-to-annual-card']")
    private ExtendedWebElement upgradeToAnnualCard;

    @FindBy(xpath = "//*[@data-testid='upgrade-to-annual-link']")
    private ExtendedWebElement upgradeToAnnualCta;

    @FindBy(xpath = "//*[@data-testid='billing-history-link']")
    private ExtendedWebElement billingHistoryCta;

    @FindBy(xpath = "//*[@data-testid='delete-account-link']")
    private ExtendedWebElement deleteAccountCta;

    @FindBy(xpath = "//*[@data-testid='enter-passcode-submit-button']")
    private ExtendedWebElement enterPasscodeSubmitButton;

    @FindBy(xpath = "//*[@data-testid='log-out-all-devices-button']")
    private ExtendedWebElement logOutAllDevicesButton;

    @FindBy(id = "currentPass")
    private ExtendedWebElement logOutAllDevicesCurrentPass;

    @FindBy(id = "continue")
    private ExtendedWebElement logOutAllDevicesContinue;

    @FindBy(xpath = "//*[@data-testid='last-payment-ft']")
    private ExtendedWebElement lastPaymentFreeTrial;

    @FindBy(xpath = "//*[@data-testid='last-payment-no-ft']")
    private ExtendedWebElement lastPaymentNoFreeTrial;

    @FindBy(xpath = "//*[@data-testid='myservices-link-bundle_activate_espnplus']")
    private ExtendedWebElement myServicesEspnPlus;

    @FindBy(xpath = "//*[@data-testid='myservices-link-bundle_activate_hulu']")
    private ExtendedWebElement myServicesHulu;

    @FindBy(xpath = "//*[@data-testid='myservices-link-disney_plus_home']")
    private ExtendedWebElement myServicesDisney;

    @FindBy(xpath = "//*[@data-testid='myservices-link-star_plus_home']")
    private ExtendedWebElement myServicesStar;

    @FindBy(xpath = "//*[@data-testid='crossed-out-label']")
    private ExtendedWebElement crossedOutLabel;

    @FindBy(xpath = "//*[@data-testid='paused-label']")
    private ExtendedWebElement pausedLabel;

    @FindBy(xpath = "//*[@data-testid='unpaused-subscription']")
    private ExtendedWebElement unpausedSubscription;

    @FindBy(xpath = "//*[@data-testid='paused-subscription']")
    private ExtendedWebElement pausedSubscription;

    @FindBy(xpath = "//*[@data-gv2elementkey='cancel_subscription']")
    private ExtendedWebElement cancelSubscriptionFooter;

    @FindBy(xpath = "//*[@data-testid='login-continue-button']")
    private ExtendedWebElement loginContinueButton;

    //Billing History
    @FindBy(xpath = "//*[@data-testid='invoice-row']")
    private ExtendedWebElement billingHistoryInvoiceRow;

    @FindBy(xpath = "//*[@data-testid='invoice-row'][2]")
    private ExtendedWebElement billingHistoryInvoiceRowTwo;

    @FindBy(xpath = "//*[contains(@data-testid,'invoice-link-/invoices/')]")
    private ExtendedWebElement billingHistoryInvoiceLink;

    @FindBy(xpath = "//*[@data-testid='link-button']")
    private ExtendedWebElement billingHistoryInvoiceLinkButton;

    @FindBy(xpath = "//*[contains(@data-testid,'invoice-details-title')]")
    private ExtendedWebElement billingHistoryInvoiceDetailsTitle;

    @FindBy(xpath = "//*[@data-testid='invoice-details-subscription']")
    private ExtendedWebElement billingHistoryInvoiceDetailsSubscription;

    @FindBy(xpath = "//*[@data-testid='invoice-details-subscription'][2]")
    private ExtendedWebElement billingHistoryInvoiceDetailsSubscriptionTwo;

    @FindBy(xpath = "//*[@data-testid='enter-passcode-cancel-button']")
    private ExtendedWebElement enterPasscodeCancelButton;

    @FindBy(xpath = "//*[@data-testid='log-out-all-devices-cancel-button']")
    private ExtendedWebElement logoutAllDevicesCancelButton;

    @FindBy(xpath = "//*[@data-testid='tw-einvoice-copy']")
    private ExtendedWebElement billingHistoryEInvoiceCopy;

    //Archived account
    @FindBy(xpath = "//*[@data-testid='account-archived-cta']")
    private ExtendedWebElement archivedAccountCta;

    @FindBy(xpath = "//*[@data-testid='account-archived-container']")
    private ExtendedWebElement archivedAccountContainer;

    @FindBy(xpath = "//*[@data-testid='toggle-switch-restrictProfileCreation']")
    private ExtendedWebElement restrictProfileCreationToggle;

    @FindBy(xpath = "//*[@data-testid='pending-downgrade-banner']")
    private ExtendedWebElement downgradePendingChangePrompt;

    public DisneyPlusAccountPage(WebDriver driver) {
        super(driver);
    }

    //Get Elements

    public ExtendedWebElement getCancelSubscriptionDropdown() {
        waitFor(cancelSubscriptionDropdown);
        return cancelSubscriptionDropdown;
    }

    public ExtendedWebElement getCancelSubscriptionButtonMobile() {
        waitFor(cancelSubscriptionButtonMobile);
        return cancelSubscriptionButtonMobile;
    }

    public ExtendedWebElement getCancellationSurvey() {
        waitFor(cancellationSurvey);
        return cancellationSurvey;
    }

    public ExtendedWebElement getEmailEdit() {
        return emailChange;
    }

    public ExtendedWebElement getPasswordEdit() {
        return passwordChange;
    }

    public ExtendedWebElement getPaymentChangeButton() {
        return changePaymentOptionButton;
    }

    public ExtendedWebElement getTextEquals(String text) {
        return getText.format(text);
    }

    public ExtendedWebElement getInternalD2CSubscriptionContains() {
        waitFor(internalD2CSubscriptionContains);
        return internalD2CSubscriptionContains;
    }

    public ExtendedWebElement getInternalD2CStandaloneWithAdsCta() {
        waitFor(internalD2CStandaloneWithAdsCta);
        return internalD2CStandaloneWithAdsCta;
    }

    public ExtendedWebElement getInternalD2CSubscriptionCta() {
        waitFor(internalD2CSubscriptionCta);
        return internalD2CSubscriptionCta;
    }

    public ExtendedWebElement getInternalSuperBundleD2CSubscriptionCta() {
        waitFor(internalSuperBundleD2CSubscriptionCta);
        return internalSuperBundleD2CSubscriptionCta;
    }

    public ExtendedWebElement getInternalBundleMonthlyD2CSubscriptionCta() {
        waitFor(internalBundleMonthlyD2CSubscriptionCta);
        return internalBundleMonthlyD2CSubscriptionCta;
    }

    public ExtendedWebElement getInternalAnnualD2CSubscriptionCta(){
        waitFor(internalAnnualD2CSubscriptionCta);
        return internalAnnualD2CSubscriptionCta;
    }

    public ExtendedWebElement getInternalMonthlyNopaySubscription(){
        waitFor(internalMonthlyNopaySubscription);
        return internalMonthlyNopaySubscription;
    }

    public ExtendedWebElement getInternalNopaySubscription(){
        waitFor(internalNopaySubscription);
        return internalNopaySubscription;
    }

    public ExtendedWebElement getInternalD2CBundleMonthlyNoAdsCta(){
        waitFor(internalD2CBundleMonthlyNoAdsCta);
        return internalD2CBundleMonthlyNoAdsCta;
    }

    public ExtendedWebElement getExternalVerizonSubscription(){
        waitFor(externalVerizonSubscription);
        return externalVerizonSubscription;
    }

    public ExtendedWebElement getExternalAppleSubscription(){
        waitFor(externalAppleSubscription);
        return externalAppleSubscription;
    }

    public ExtendedWebElement getExternalBamtechSubscription(){
        waitFor(externalBamtechSubscription);
        return externalBamtechSubscription;
    }

    public ExtendedWebElement getExternalSubscription(){
        waitFor(externalSubscription);
        return externalSubscription;
    }

    public ExtendedWebElement getSubscriptionDetailsCancelSubscriptionBtn() {
        waitFor(subscriptionDetailsCancelSubscriptionBtn);
        return subscriptionDetailsCancelSubscriptionBtn;
    }

    public ExtendedWebElement getSubscriptionDetailsChangePaymentBtn() {
        waitFor(subscriptionDetailsChangePaymentBtn);
        return subscriptionDetailsChangePaymentBtn;
    }

    public ExtendedWebElement getSubscriptionDetailsChangePaymentBtn(int wait) {
        waitFor(subscriptionDetailsChangePaymentBtn, wait);
        return subscriptionDetailsChangePaymentBtn;
    }

    public ExtendedWebElement getLastPaymentPayPal() {
        waitFor(lastPaymentPayPal);
        return lastPaymentPayPal;
    }

    public ExtendedWebElement getLastPaymentCreditCard() {
        waitFor(lastPaymentCreditCard);
        return lastPaymentCreditCard;
    }

    public ExtendedWebElement getCancelSubmitBtn() {
        waitFor(cancelSubmitBtn);
        return cancelSubmitBtn;
    }

    public ExtendedWebElement getCompleteCancellationBtn() {
        waitFor(completeCancellationBtn);
        return completeCancellationBtn;
    }

    public ExtendedWebElement getCancellationConfirmationGoHomeBtn() {
        waitFor(cancellationConfirmationGoHomeBtn);
        return cancellationConfirmationGoHomeBtn;
    }

    public ExtendedWebElement getRestartSubscriptionBtn() {
        waitFor(restartSubscriptionBtn);
        return restartSubscriptionBtn;
    }

    public ExtendedWebElement getRestartModalBtn() {
        waitFor(restartModalBtn);
        return restartModalBtn;
    }

    public ExtendedWebElement getCancellationConfirmationBlurb() {
        waitFor(cancellationConfirmationBlurb);
        return cancellationConfirmationBlurb;
    }

    public ExtendedWebElement getRestartSubscriptionBanner() {
        waitFor(restartSubscriptionBanner);
        return restartSubscriptionBanner;
    }

    public ExtendedWebElement getChangeYourMindBody() {
        waitFor(changeYourMindBody);
        return changeYourMindBody;
    }

    public ExtendedWebElement getRestartModal() {
        waitFor(restartModal);
        return restartModal;
    }

    public ExtendedWebElement getLastPaymentIdeal() {
        waitFor(lastPaymentIdeal);
        return lastPaymentIdeal;
    }

    public ExtendedWebElement getLastPaymentKlarna() {
        waitFor(lastPaymentKlarna);
        return lastPaymentKlarna;
    }

    public ExtendedWebElement getLastPaymentMercadoPrago() {
        waitFor(lastPaymentMercadoPrago);
        return lastPaymentMercadoPrago;
    }

    public ExtendedWebElement getUpgradeToBundleCta() {
        pause(4); // Need this since the cta exists but another call needs to run before we click it

        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);
        if (isComboCountry) {
            return upgradeToComboCta;
        } else {
            return upgradeToBundleCta;
        }
    }

    public ExtendedWebElement getUpgradeToMegaBundleCta() {
        waitFor(upgradeToMegaBundleCta);
        return upgradeToMegaBundleCta;
    }

    public ExtendedWebElement getUpgradeSashToNoahCta() {
        pause(4); // Need this since the cta exists but another call needs to run before we click it
        waitFor(upgradeSashToNoahCta);
        return upgradeSashToNoahCta;
    }

    public ExtendedWebElement getChangeSubscriptionBundleUpgradeCta() {
        waitFor(changeSubscriptionBundleUpgradeCta);
        return changeSubscriptionBundleUpgradeCta;
    }
    public ExtendedWebElement getChangeSubscriptionComboBundleUpgradeCta() {
        waitFor(changeSubscriptionComboBundleUpgradeCta);
        return changeSubscriptionComboBundleUpgradeCta;
    }

    public ExtendedWebElement getChangeSubscriptionAnnualUpgradeCta() {
        waitFor(changeSubscriptionAnnualUpgradeCta);
        return changeSubscriptionAnnualUpgradeCta;
    }

    public ExtendedWebElement getBundleSuccessPage() {
        waitFor(bundleSuccessPage);
        return bundleSuccessPage;
    }

    public ExtendedWebElement getUpgradeToAnnualCard() {
        waitFor(upgradeToAnnualCard);
        return upgradeToAnnualCard;
    }

    public ExtendedWebElement getUpgradeToAnnualCta() {
        waitFor(upgradeToAnnualCta);
        return upgradeToAnnualCta;
    }

    public ExtendedWebElement getBillingHistoryCta() {
        waitFor(billingHistoryCta);
        return billingHistoryCta;
    }

    public ExtendedWebElement getDeleteAccountCta() {
        waitFor(deleteAccountCta);
        return deleteAccountCta;
    }

    public ExtendedWebElement getEnterPasscodeSubmitButton() {
        waitFor(enterPasscodeSubmitButton);
        return enterPasscodeSubmitButton;
    }

    public ExtendedWebElement getLogOutAllDevicesButton() {
        waitFor(logOutAllDevicesButton);
        return logOutAllDevicesButton;
    }

    public ExtendedWebElement getLogOutAllDevicesCurrentPass() {
        waitFor(logOutAllDevicesCurrentPass);
        return logOutAllDevicesCurrentPass;
    }

    public ExtendedWebElement getLogOutAllDevicesContinue() {
        waitFor(logOutAllDevicesContinue);
        return logOutAllDevicesContinue;
    }

    public ExtendedWebElement getLastPaymentFreeTrial() {
        waitFor(lastPaymentFreeTrial);
        return lastPaymentFreeTrial;
    }

    public ExtendedWebElement getLastPaymentNoFreeTrial() {
        waitFor(lastPaymentNoFreeTrial);
        return lastPaymentNoFreeTrial;
    }

    public ExtendedWebElement getBillingHistoryInvoiceRow() {
        waitFor(billingHistoryInvoiceRow);
        return billingHistoryInvoiceRow;
    }

    public ExtendedWebElement getBillingHistoryInvoiceRowTwo() {
        waitFor(billingHistoryInvoiceRowTwo);
        return billingHistoryInvoiceRowTwo;
    }

    public ExtendedWebElement getBillingHistoryInvoiceLink() {
        waitFor(billingHistoryInvoiceLink);
        return billingHistoryInvoiceLink;
    }

    public ExtendedWebElement getBillingHistoryInvoiceDetailsTitle() {
        waitFor(billingHistoryInvoiceDetailsTitle);
        return billingHistoryInvoiceDetailsTitle;
    }

    public ExtendedWebElement getBillingHistoryInvoiceDetailsSubscription() {
        waitFor(billingHistoryInvoiceDetailsSubscription);
        return billingHistoryInvoiceDetailsSubscription;
    }

    public ExtendedWebElement getBillingHistoryInvoiceDetailsSubscriptionTwo() {
        waitFor(billingHistoryInvoiceDetailsSubscriptionTwo);
        return billingHistoryInvoiceDetailsSubscriptionTwo;
    }

    public ExtendedWebElement getChangeSubscriptionUpgradeCta() {
        pause(5); // Need this since the cta exists but another call needs to run before we click it
        waitFor(changeSubscriptionUpgradeCta);
        return changeSubscriptionUpgradeCta;
    }

    public ExtendedWebElement getSurveyWatchedEverything() {
        waitFor(surveyWatchedEverything);
        return surveyWatchedEverything;
    }

    public ExtendedWebElement getCancelSubscriptionRecommendBundle() {
        waitFor(cancelSubscriptionRecommendBundle);
        return cancelSubscriptionRecommendBundle;
    }

    public ExtendedWebElement getMyServicesEspnPlus() {
        waitFor(myServicesEspnPlus);
        return myServicesEspnPlus;
    }

    public ExtendedWebElement getMyServicesHulu() {
        waitFor(myServicesHulu);
        return myServicesHulu;
    }

    public ExtendedWebElement getMyServicesDisney() {
        waitFor(myServicesDisney);
        return myServicesDisney;
    }

    public ExtendedWebElement getMyServicesStar() {
        waitFor(myServicesStar);
        return myServicesStar;
    }

    public ExtendedWebElement getPausedLabel() {
        waitFor(pausedLabel);
        return pausedLabel;
    }

    public ExtendedWebElement getCrossedOutLabel() {
        waitFor(crossedOutLabel);
        return crossedOutLabel;
    }

    public ExtendedWebElement getPausedSubscription() {
        waitFor(pausedSubscription);
        return pausedSubscription;
    }

    public ExtendedWebElement getUnpausedSubscription() {
        waitFor(unpausedSubscription);
        return unpausedSubscription;
    }

    public void clickOnCancelSubscriptionDropdown(boolean isMobile) {
        if (isMobile) {
            getCancelSubscriptionButtonMobile().click();
        } else {
            getActiveProfile();
            getCancelSubscriptionDropdown().click();
        }
        LOGGER.info("Clicked on Cancel Subscription");
    }

    public ExtendedWebElement getCancelSubscriptionFooter() {
        waitFor(cancelSubscriptionFooter);
        return cancelSubscriptionFooter;
    }

    public ExtendedWebElement getCancellationSurveyModalLink() {
        waitFor(cancellationSurveyModalLink);
        return cancellationSurveyModalLink;
    }

    public ExtendedWebElement getCancellationSurveyModalOtherButton() {
        waitFor(cancellationSurveyModalOtherButton);
        return cancellationSurveyModalOtherButton;
    }

    public ExtendedWebElement getCancellationSurveyModalSubmitButton() {
        waitFor(cancellationSurveyModalSubmitButton);
        return cancellationSurveyModalSubmitButton;
    }

    public ExtendedWebElement getLoginContinueButton() {
        return loginContinueButton;
    }

    public ExtendedWebElement getBillingHistoryEInvoiceCopy() {
        waitFor(billingHistoryEInvoiceCopy);
        return billingHistoryEInvoiceCopy;
    }

    public ExtendedWebElement getArchivedAccountCta() {
        return archivedAccountCta;
    }

    public ExtendedWebElement getArchivedAccountContainer() {
        return archivedAccountContainer;
    }

    // Verify Methods
    public boolean verifyPage() {
        LOGGER.info("Verify Account page loaded");
        return pageTitle.isVisible(LONG_TIMEOUT);
    }

    public boolean isInternalD2CSubscriptionCtaVisible() {
        LOGGER.info("Is internal D2C Subscription CTA visible");
        return internalD2CSubscriptionCta.isVisible();
    }

    public boolean isInternalD2CStandaloneWithAdsCtaVisible() {
        LOGGER.info("Is internal D2C standalone with ads Subscription CTA visible");
        return internalD2CStandaloneWithAdsCta.isVisible();
    }

    public boolean isInternalD2CBundleMonthlyNoAdsCtaVisible() {
        LOGGER.info("Is internal D2C bundle monthly no ads subscription button visible");
        return internalD2CBundleMonthlyNoAdsCta.isVisible();
    }

    public boolean isBillingHistoryLinkVisible(){
        LOGGER.info("Is Billing history link visible");
        return billingHistoryCta.isVisible(30);
    }

    public boolean isUpgradeToBundleCardVisible(){
        LOGGER.info("Is upgrade to bundle card visible");
        return upgradeToBundleCard.isVisible();
    }

    public boolean isRestrictProfileCreationToggleVisible(){
        LOGGER.info("Is Restrict profile creation switch visible");
        return restrictProfileCreationToggle.isVisible();
    }

    public boolean isRestrictProfileCreationToggleEnabled() {
        return Boolean.valueOf(restrictProfileCreationToggle.getAttribute("aria-checked"));
    }

    // Click Elements
    public void clickOnEmailChange(int waitFor) {
        waitFor(getEmailEdit());
        getEmailEdit().click();
        LOGGER.info("Clicked on email change bar");
        pause(waitFor);
    }

    public void clickOnAccountDropdown(boolean isMobile) {
        if (isMobile) {
            getDropdownAccountMobile().click();
        } else {
            getActiveProfile();
            getDropdownAccount().clickByJs();
            LOGGER.info("Clicked on Account");
        }
    }

    public void clickOnAccountDropdownIfNotAccountPage(boolean isMobile) {
        String currentUrl = getCurrentUrl();

        if (currentUrl.contains("/home") || currentUrl.contains("/get-app"))
            clickOnAccountDropdown(isMobile);
    }

    public void clickOnPasscodeCancelButtonKey() {
        LOGGER.info("Cancel button is clicked");
        waitUntil(ExpectedConditions.elementToBeSelected(enterPasscodeCancelButton.getElement()), 10);
        enterPasscodeCancelButton.click();
    }

    public void clickOnlogoutAllDevicesCancelButtonKey() {
        LOGGER.info("Cancel button is clicked");
        waitUntil(ExpectedConditions.elementToBeSelected(logoutAllDevicesCancelButton.getElement()), 10);
        logoutAllDevicesCancelButton.click();
    }

    public void clickOnPasswordChange(int waitFor) {
        new SeleniumUtils(driver).waitUntilDOMready();
        getPasswordEdit().click();
        LOGGER.info("Clicked on password change bar");
        pause(waitFor);
    }

    public void clickToUpgradeBundleCard() {
        LOGGER.info("Click upgrade bundle card on Account page");
        upgradeToBundleCard.click();
    }

    public void clickBundleUpgradeButton(String locale, SoftAssert sa) {
        boolean isBundleCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_BUNDLE_COUNTRY);
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);

        if (isBundleCountry || isComboCountry) {
            waitFor(getUpgradeToBundleCta());
            if(getUpgradeToBundleCta().isElementNotPresent(5)){
                LOGGER.info("Refreshing page for 'Upgrade to Bundle' banner to load");
                refresh();
            }
            getUpgradeToBundleCta().click();
            LOGGER.info("Clicked on payment info bar");

            if(getLocale().equalsIgnoreCase("US")){
                verifyUrlText(sa, "change-subscription");
            }
            else {
                verifyUrlText(sa, "upgrade");
            }
        }
    }

    public void clickMegaBundleUpgradeButton(SoftAssert sa) {
        getUpgradeToMegaBundleCta().click();
        LOGGER.info("Clicked on Megabundle cta button");
        verifyUrlText(sa, "upgrade");
    }

    public void clickOnFinishLaterBtn() {
        if (getTextEquals(FINISH_LATER).isElementPresent()) {
            getTextEquals(FINISH_LATER).click();
            LOGGER.info("Clicked on 'Finish Later' button");
        }
    }

    public void clickBillingHistoryCta(JsonNode dictionary) {
        LOGGER.info("Clicking billing history");
        waitForDictionaryAndClick(dictionary, DisneyWebKeys.BILLING_HISTORY.getText());
    }

    public void clickInternalD2CSubscriptionCtaContains() {
        isAccountSettingsIndexPresent();
        getInternalD2CSubscriptionContains().clickByJs();
    }

    public void clickInternalD2CSubscriptionCtaContainsIfPresent() {
        isAccountSettingsIndexPresent();
        ExtendedWebElement el = getInternalD2CSubscriptionContains();
        if (el.isPresent(2)) {
            el.clickByJs();
        }
    }

    public void clickInternalD2CSubscriptionCtaContains(int timeout) {
        waitFor(accountSettingsIndex, timeout);
        getInternalD2CSubscriptionContains().click();
    }

    public void clickInternalD2CStandaloneWithAdsSubscriptionCta() {
        getInternalD2CStandaloneWithAdsCta().clickByJs();
    }

    public void clickInternalD2CSubscriptionCta() {
        getInternalD2CSubscriptionCta().clickByJs();
    }

    public void clickInternalSuperBundleD2CSubscriptionCta() {
        getInternalSuperBundleD2CSubscriptionCta().click();
    }

    public void clickInternalBundleMonthlyD2CSubscriptionCta() {
        getInternalBundleMonthlyD2CSubscriptionCta().click();
    }

    public void clickInternalAnnualD2CSubscriptionCta() {
        getInternalAnnualD2CSubscriptionCta().click();
    }

    public void clickInternalD2CBundleMonthlyNoAdsSubscriptionCta() {
        getInternalD2CBundleMonthlyNoAdsCta().click();
    }

    public void clickSubscriptionDetailsCancelSubscriptionBtn() {
        getSubscriptionDetailsCancelSubscriptionBtn().clickByJs();
    }

    public void clickSubscriptionDetailsChangePaymentBtn() {
        getSubscriptionDetailsChangePaymentBtn().clickByJs();
    }

    public void clickCancelSubmitBtn() {
        getCancelSubmitBtn().clickByJs();
    }

    public void clickCompleteCancellationBtn() {
        getCompleteCancellationBtn().clickByJs();
    }

    public void clickCancellationConfirmationGoHomeBtn() {
        getCancellationConfirmationGoHomeBtn().clickByJs();
    }

    public void clickRestartSubscriptionButton() {
        getRestartSubscriptionBtn().clickByJs();
    }

    public void clickRestartModalButton() {
        getRestartModalBtn().click();
    }

    public void clickUpgradeToBundleCta() {
        getUpgradeToBundleCta().clickByJs();
    }

    public void clickUpgradeSashToNoahCta() {
        getUpgradeSashToNoahCta().clickByJs();
    }

    public void clickChangeSubscriptionBundleUpgradeCta() {
        ExtendedWebElement el = getChangeSubscriptionBundleUpgradeCta();
        if (el.isPresent(2)) {
            el.clickByJs();
        }
    }

    public void clickChangeSubscriptionComboBundleUpgradeCta() {
        getChangeSubscriptionComboBundleUpgradeCta().isElementPresent(2);
        getChangeSubscriptionComboBundleUpgradeCta().clickByJs();
    }

    public void clickSwitchSubscriptionBundleUpgrade(String locale) {
      boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_COMBO_COUNTRY);
        if (isComboCountry) {
            clickChangeSubscriptionComboBundleUpgradeCta();
        } else {
            clickChangeSubscriptionBundleUpgradeCta();
        }
    }

    public void clickChangeSubscriptionAnnualUpgradeCta() {
        ExtendedWebElement el = getChangeSubscriptionAnnualUpgradeCta();
        if (el.isPresent(2)) {
            el.clickByJs();
        }
    }

    public void clickChangeSubscriptionUpgradeCta() {
        ExtendedWebElement el = getChangeSubscriptionUpgradeCta();
        if (el.isPresent(2)) {
            el.clickByJs();
        }
    }

    public void clickUpgradeToAnnualCta() {
        getUpgradeToAnnualCta().clickByJs();
    }

    public void clickLogOutAllDevicesButton() {
        new SeleniumUtils(driver).waitUntilDOMready();
        getLogOutAllDevicesButton().click();
        LOGGER.info("Clicked on log out all devices button");
    }

    public void clickLogOutAllDevicesContinue() {
        getLogOutAllDevicesContinue().click();
    }

    public void clickOnCancelSubscriptionFooter() {
        getCancelSubscriptionFooter().click();
    }

    public void clickOnCancelSurveyModalLink() {
        getCancellationSurveyModalLink().click();
    }

    public void clickOnCancelSurveyModalOtherButton() {
        getCancellationSurveyModalOtherButton().click();
    }

    public void clickOnCancellationSurveyModalSubmitButton() {
        getCancellationSurveyModalSubmitButton().click();
    }

    public void clickOnLoginContinueButton() {
        getLoginContinueButton().clickByJs();
    }

    public void clickBillingHistoryCta() {
        if (getBillingHistoryCta().isElementNotPresent(SHORT_TIMEOUT)) {
            pause(10);
            refresh();
        }
        getBillingHistoryCta().clickByJs();
    }

    public void clickDeleteAccountCta() {
        LOGGER.info("Click delete account cta");
        getDeleteAccountCta().click();
    }

    public void clickBillingHistoryInvoiceLink() {
        getBillingHistoryInvoiceLink();
        billingHistoryInvoiceLinkButton.click();
    }

    public void clickArchivedAccountCta() {
        getArchivedAccountCta().click();
    }

    //Booleans
    public boolean isRestartSubscriptionBannerPresent() {
        return getRestartSubscriptionBanner().isElementPresent(5);
    }

    public boolean isRestartSubscriptionBtnPresent() {
        DisneyProductData productData = new DisneyProductData();
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);

        if (!(IS_DISNEY && isComboCountry) && productData.searchAndReturnProductData("disableRestartSubscription").equalsIgnoreCase("false")) {
            return getRestartSubscriptionBtn().isElementPresent();
        }
        else {
            return isSubscriptionCancelledPresent();
        }
    }

    public boolean isSubscriptionCancelledPresent() {
        return subscriptionCancelled.isElementPresent();
    }

    public boolean isCancellationSurveyModalPresent() {
        return cancellationSurveyModal.isElementPresent();
    }

    public boolean isAccountSettingsIndexPresent() {
        return accountSettingsIndex.isElementPresent();
    }

    public boolean isChangeYourMindBodyPresent() {
        DisneyProductData productData = new DisneyProductData();
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);

        if (!(IS_DISNEY && isComboCountry) && productData.searchAndReturnProductData("disableRestartSubscription").equalsIgnoreCase("false")) {
            return getChangeYourMindBody().isElementPresent(5);
        } else {
            return true;
        }
    }

    public boolean isRestartModalPresent() {
        return getRestartModal().isElementPresent();
    }

    public boolean isSubscriptionDetailsCancelSubscriptionBtnPresent() {
        return getSubscriptionDetailsCancelSubscriptionBtn().isElementPresent();
    }

    public boolean isLastPaymentPayPal() {
        return getLastPaymentPayPal().isElementPresent();
    }

    public boolean isLastPaymentCreditCard() {
        return getLastPaymentCreditCard().isElementPresent();
    }

    public boolean isCancellationConfirmationBlurbPresent(){
        return getCancellationConfirmationBlurb().isElementPresent();
    }

    public boolean isLastPaymentIdeal() {
        return getLastPaymentIdeal().isElementPresent();
    }

    public boolean isLastPaymentKlarna() {
        return getLastPaymentKlarna().isElementPresent();
    }

    public boolean isLastPaymentMercadoPrago() {
        return getLastPaymentMercadoPrago().isElementPresent();
    }

    public boolean isBundleSuccessPage() {
        return getBundleSuccessPage().isElementPresent();
    }

    public boolean isUpgradeToAnnualCard() {
        return getUpgradeToAnnualCard().isElementPresent();
    }

    public boolean isUpgradeToAnnualCardNotPresent() {
        return getUpgradeToAnnualCard().isElementNotPresent(2);
    }

    public boolean isUpgradeToBundleCtaPresent() {
        return getUpgradeToBundleCta().isElementPresent();
    }

    public boolean isEnterPasscodeSubmitButtonPresent() {
        return getEnterPasscodeSubmitButton().isElementPresent();
    }

    public boolean isInternalMonthlyNopaySubscriptionPresent() {
        return getInternalMonthlyNopaySubscription().isElementPresent();
    }

    public boolean isInternalNopaySubscriptionPresent() {
        return getInternalNopaySubscription().isElementPresent();
    }

    public boolean isExternalVerizonSubscriptionPresent() {
        return getExternalVerizonSubscription().isElementPresent();
    }

    public boolean isExternalAppleSubscriptionPresent() {
        if (IS_STAR) {
            return isExternalSubscriptionPresent();
        } else {
            return getExternalAppleSubscription().isElementPresent();
        }
    }

    public boolean isExternalBamtechSubscriptionPresent() {
        return getExternalBamtechSubscription().isElementPresent();
    }

    public boolean isExternalSubscriptionPresent() {
        return getExternalSubscription().isElementPresent();
    }

    public boolean isMonthlyInternalD2CSubscriptionCtaPresent() {
        return getInternalD2CSubscriptionContains().isElementPresent();
    }

    public boolean isMonthlyInternalD2CSubscriptionCtaNotPresent() {
        return getInternalD2CSubscriptionContains().isElementNotPresent(5);
    }

    public boolean isBillingHistoryInvoiceRowPresent() {
        return getBillingHistoryInvoiceRow().isElementPresent();
    }

    public boolean isBillingHistoryInvoiceDetailsTitlePresent() {
        return getBillingHistoryInvoiceDetailsTitle().isElementPresent();
    }

    public boolean isBillingHistoryInvoiceDetailsSubscriptionPresent() {
        return getBillingHistoryInvoiceDetailsSubscription().isElementPresent();
    }

    public boolean isBillingHistoryInvoiceDetailsSubscriptionTwoPresent() {
        return getBillingHistoryInvoiceDetailsSubscriptionTwo().isElementPresent();
    }

    public boolean isBillingHistoryInvoiceRowTwoPresent() {
        return getBillingHistoryInvoiceRowTwo().isElementPresent();
    }

    public boolean isLastPaymentFreeTrialPresent() {
        return getLastPaymentFreeTrial().isElementPresent();
    }

    public boolean isLastPaymentNoFreeTrialPresent() {
        return getLastPaymentNoFreeTrial().isElementPresent();
    }

    public boolean isCancelSubscriptionRecommendBundlePresent(){
        return getCancelSubscriptionRecommendBundle().isElementPresent();
    }

    public boolean isMyServicesEspnPlusPresent(){
        return getMyServicesEspnPlus().isElementPresent();
    }

    public boolean isPausedLabelPresent(){
        return getPausedLabel().isElementPresent();
    }

    public boolean isCrossedOutLabelPresent(){
        return getCrossedOutLabel().isElementPresent();
    }

    public boolean isPausedSubscriptionPresent(){
        return getPausedSubscription().isElementPresent();
    }

    public boolean isUnpausedSubscriptionPresent(){
        return getUnpausedSubscription().isElementPresent();
    }

    public boolean isMyServicesHuluPresent(){
        return getMyServicesHulu().isElementPresent();
    }

    public boolean isMyServicesDisneyPresent(){
        return getMyServicesDisney().isElementPresent();
    }

    public boolean isMyServicesStarPresent(){
        return getMyServicesStar().isElementPresent();
    }

    public boolean isBillingHistoryEInvoiceCopyPresent() {
        return getBillingHistoryEInvoiceCopy().isElementPresent();
    }

    public boolean isArchivedAccountContainerPresent() {
        return getArchivedAccountContainer().isElementPresent(SHORT_TIMEOUT);
    }

    public boolean isClickSubscriptionDetailsChangePaymentBtnNotPresent() {
        return subscriptionDetailsChangePaymentBtn.isElementNotPresent(HALF_TIMEOUT);
    }

    //Assertions
    public void assertWatchedEverythingRecommend(String idAttribute,SoftAssert sa) {
        if (idAttribute.equalsIgnoreCase("surveyWatchedEverything")) {
            LOGGER.info(String.format("Asserting id: cancel_subscription_recommend-bundle due to %s radio button selection", idAttribute));
            sa.assertTrue(isCancelSubscriptionRecommendBundlePresent(),
                    String.format("Cancel Subscription Recommend Bundle not present after selecting, %s radio button", idAttribute));
        }
    }

    public String clickAndReturnRandomSurveyResponse() {
        while (cancellationSurveylist.isEmpty()) {
            pause(1);
        }
        int index = new SecureRandom().nextInt(cancellationSurveylist.size());
        ExtendedWebElement radio = cancellationSurveylist.get(index);
        String idAttribute = radio.getAttribute("id");
        ((JavascriptExecutor) driver).executeScript("arguments[0].checked = true", radio.getElement());
        LOGGER.info(String.format("Id: %s, clicked for survey response", radio.getAttribute("id")));
        return idAttribute;
    }

    public DisneyPlusAccountPage goToSwitchPlanFromSubscription(boolean isMobile) {
        clickOnAccountDropdown(isMobile);
        clickInternalD2CSubscriptionCtaContains();
        clickChangeSubscriptionUpgradeCta();
        return this;
    }
    public String getAccountPageSubscriptionDetails() {
        LOGGER.info("Getting subscription details shown in the account page");
        return internalD2CSubscriptionContains.getText();
    }

    public boolean isDisneyPlusAnnualPlanDisplayed() {
        waitForPageToFinishLoading();
        return internalAnnualD2CSubscriptionCta.isElementPresent();
    }

    public DisneyPlusAccountPage clickOnRestrictProfileCreationToggle() {
        LOGGER.info("Click on restrict profile creation toggle");
        restrictProfileCreationToggle.clickByJs();
        return this;
    }

    public boolean isDowngradePendingChangePromptVisible() {
        waitForPageToFinishLoading();
        return downgradePendingChangePrompt.isVisible();
    }
}
