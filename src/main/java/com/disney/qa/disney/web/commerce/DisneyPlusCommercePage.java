package com.disney.qa.disney.web.commerce;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.web.VerifyEmail;
import com.disney.qa.disney.DisneyCountryData;
import com.disney.qa.disney.DisneyProductData;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.common.DisneyPlusBaseProfileViewsPage;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.entities.PlanCardTypes;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.util.FileUtils;
import com.disney.util.ProxyUtils;
import com.disney.util.disney.DisneyGlobalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.registrar.CurrentTest;
import net.lightbody.bmp.BrowserMobProxy;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.NoSuchElementException;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusCommercePage extends DisneyPlusBasePage {

    ThreadLocal<DisneyCountryData> dCountry = new ThreadLocal<>();
    DisneyApiCommon disneyApiCommon = new DisneyApiCommon();
    private static final String EMAIL_SUBJECT = "Your one-time passcode";

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String IS_EU_COUNTRY = "isEuCountry";
    public static final String IS_BUNDLE_COUNTRY = "isBundleCountry";
    public static final String IS_CREDITCARD_ONLY_COUNTRY = "isCreditCardOnlyCountry";
    public static final String IS_REVIEW_SUBSCRIPTION_COUNTRY = "isReviewSubscriptionCountry";
    public static final String IS_SUBSCRIBER_AGREEMENT_COUNTRY = "isSubscriberAgreementCountry";
    public static final String IS_STARZ_COUNTRY = "isStarzCountry";
    public static final String NAME_ON_CARD = "WebQA Automation";
    public static final String EXP = "12/25";
    public static final String FTF_EXP = "12/24";
    public static final String GENERIC_PASS = DisneyApiCommon.getCommonPass();
    public static final String MASTERCARD = DisneyWebParameters.MASTERCARD_CREDIT_CARD.getValue();
    public static final String CREDIT_CARD_CVV = DisneyWebParameters.MASTERCARD_CVV.getValue();
    public static final String QA_EA_URL = "/movies/toy-story/1Ye1nzUgtF7d";
    public static final String CRUELLA_EA_URL = "/movies/cruella/2GJTZuO8I01c";
    public static final String BLACK_WIDOW_EA_URL = "/movies/black-widow/3VfTap90rwZC";
    public static final String JUNGLE_CRUISE_EA_URL = "/movies/jungle-cruise/73QMeTY6Dray";
    public static final String IS_CARD_PIN_COUNTRY = "isCardPinCountry";
    protected DateTime cruellaStartDate = DateTime.parse(disneyApiCommon.formatDateForQuery("2021-05-14T00:00:00.595-04:00"));
    protected DateTime cruellaEndDate = DateTime.parse(disneyApiCommon.formatDateForQuery("2021-07-30T00:00:00.595-04:00"));
    protected DateTime blackWidowStartDate =  DateTime.parse(disneyApiCommon.formatDateForQuery("2021-07-09T00:00:00.595-04:00"));
    protected DateTime blackWidowEndDate =  DateTime.parse(disneyApiCommon.formatDateForQuery("2021-09-10T00:00:00.595-04:00"));
    protected DateTime jungleCruiseStartDate =  DateTime.parse(disneyApiCommon.formatDateForQuery("2021-07-16T00:00:00.595-04:00"));
    protected DateTime jungleCruiseEndDate =  DateTime.parse(disneyApiCommon.formatDateForQuery("2021-10-01T00:00:00.595-04:00"));
    protected boolean isCruellaOnProd = disneyGlobalUtils.isDateWithinRange(cruellaStartDate, cruellaEndDate);
    protected boolean isBlackWidowOnProd = disneyGlobalUtils.isDateWithinRange(blackWidowStartDate, blackWidowEndDate);
    protected boolean isJungleCruiseOnProd = disneyGlobalUtils.isDateWithinRange(jungleCruiseStartDate, jungleCruiseEndDate);
    public static final boolean DISABLE_TANDEM_FLOW = true;
    private static final String GET_APP_URL = "get-app";

    public DisneyPlusCommercePage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "(//button[text()='Privacy Policy'])[1]")
    private ExtendedWebElement privacyPolicy1;

    @FindBy(xpath = "(//button[text()='Privacy Policy'])[2]")
    private ExtendedWebElement privacyPolicy2;

    @FindBy(xpath = "(//button[text()='Subscriber Agreement'])[1]")
    private ExtendedWebElement subscriberAgreement;

    @FindBy(xpath = "(//button[text()='UK & EU Privacy Rights'])[1]")
    private ExtendedWebElement euPrivacyAgreement;

    @FindBy(xpath = "//*[contains(@class,'terms-text')]")
    private ExtendedWebElement termsText;

    @FindBy(className = "brand-image")
    private ExtendedWebElement brandImage;

    @FindBy(xpath = "//*[@data-testid='ad-blurb']")
    private ExtendedWebElement adBlurb;

    @FindBy(xpath = "//input[contains(@id,'MONTH')]")
    private ExtendedWebElement monthlyRadioBtnId;

    @FindBy(xpath = "//input[contains(@id,'YEAR')]")
    private ExtendedWebElement yearlyRadioBtnId;

    @FindBy(xpath = "//*[@data-testid='toggle-year']")
    private ExtendedWebElement yearlyRadioParentBtn;

    @FindBy(xpath = "(//*[@name='subscribeOptions'])")
    private ExtendedWebElement subscribeOptions;

    @FindBy(xpath = "//*[@data-testid='bundle-upsell-button']")
    private ExtendedWebElement bundleUpsellToggle;

    @FindBy(id = "credit-submit-button")
    private ExtendedWebElement creditSubmitBtnId;

    @FindBy(xpath = "//label[@aria-labelledby='credit-radio-icon']")
    private ExtendedWebElement creditRadioLabel;

    @FindBy(id = "credit-radio-icon")
    private ExtendedWebElement creditRadioIconId;

    @FindBy(xpath = "//label[@aria-labelledby='credit-radio-icon']")
    private ExtendedWebElement creditRadioIconAria;

    @FindBy(id = "paypal-radio-icon")
    private ExtendedWebElement paypalRadioIconId;

    @FindBy(xpath = "//*[@data-testid='paypal-radio-icon']")
    private ExtendedWebElement paypalRadioIconTestId;

    @FindBy(xpath = "//*[@data-testid='mercado-radio-icon']")
    private ExtendedWebElement mercadoRadioIconId;

    @FindBy(xpath = "//*[@data-testid='klarna-radio-icon']")
    private ExtendedWebElement klarnaRadioIconId;

    @FindBy(id = "paypal-submit-button")
    private ExtendedWebElement paypalSubmitBtnById;

    @FindBy(id = "challenge-form-submit")
    private ExtendedWebElement challengeFormSubmitBtnById;

    @FindBy(xpath = "//input[@type='submit' and @value ='OK']")
    private ExtendedWebElement challengeFormSubmitBtn2ById;

    @FindBy(xpath = "//*[@data-testid='paypal-review-button']")
    private ExtendedWebElement paypalReviewSubscriptionCta;

    @FindBy(xpath = "//*[@data-testid='mercado-review-button']")
    private ExtendedWebElement mercadoReviewBtn;

    @FindBy(xpath = "//*[@data-testid='mercado-submit-button']")
    private ExtendedWebElement mercadoSubmitBtn;

    @FindBy(xpath = "//div[@role='button' and contains(@class,'paypal')]")
    private ExtendedWebElement paypalGenericBtn;

    @FindBy(id = "klarna-submit-button")
    private ExtendedWebElement klarnaSubmitBtnId;

    @FindBy(id = "ideal-submit-button")
    private ExtendedWebElement idealSubmitBtnId;

    @FindBy(id = "payment-block-button")
    private ExtendedWebElement paymentBlockedBtnId;

    @FindBy(xpath = "//*[@data-testid='speed-bump-continue']")
    private ExtendedWebElement speedBumpContinue;

    @FindBy(xpath = "//*[@data-testid='ineligible-title']")
    private ExtendedWebElement ineligibleHuluSubscriberHeader;

    @FindBy(xpath = "//*[@data-testid='ineligible-button']")
    private ExtendedWebElement ineligibleHuluSubscriberButton;

    @FindBy(xpath = "//*[@data-testid='redirect-title']")
    private ExtendedWebElement redirectHuluSubscriberHeader;

    @FindBy(xpath = "//*[@data-testid='redirect-button']")
    private ExtendedWebElement redirectHuluSubscriberButton;

    @FindBy(xpath = "//button[@class='button button--continue disabled']")
    private ExtendedWebElement createAccountHuluContinueButton;

    @FindBy(xpath = "//*[@aria-label='alert']")
    private ExtendedWebElement errorProcessing;

    @FindBy(xpath = "//*[@role='alert']")
    private ExtendedWebElement errorOnAccount;

    @FindBy(xpath = "//*[@data-testid='account-hold-update-payment-button']")
    private ExtendedWebElement accountHoldUpdatePaymentCta;

    @FindBy(xpath = "//*[@data-testid='cancel-contract']")
    private ExtendedWebElement cancelContract;

    @FindBy(id = "get-app_index")
    private ExtendedWebElement getAppPage;

    @FindBy(xpath = "//*[@data-testid='get-app-content-ea']")
    private ExtendedWebElement getAppEAPage;

    @FindBy(xpath = "//*[@data-testid='get-app-content']")
    private ExtendedWebElement getAppBundlePage;

    @FindBy(xpath = "//*[@data-testid='modal-primary-button']")
    private ExtendedWebElement modalPrimaryBtn;

    @FindBy(xpath = "//*[@data-testid='modal-secondary-button']")
    private ExtendedWebElement modalSecondaryBtn;

    @FindBy(xpath = "//*[@data-testid='modal-close-button']")
    private ExtendedWebElement modalCloseBtn;

    @FindBy(xpath = "//*[@data-testid='bundle-offer-card']")
    private ExtendedWebElement bundleOfferCard;

    @FindBy(xpath = "//*[@data-testid='bundle-offer-card-downgrade']")
    private ExtendedWebElement bundleOfferCardDowngrade;

    @FindBy(xpath = "//*[@data-testid='bundle-upgrade-card']")
    private ExtendedWebElement bundleUpgradeCard;

    @FindBy(xpath = "//*[@data-testid='bundle-upgrade-card-link']")
    private ExtendedWebElement bundleUpgradeCardLink;

    @FindBy(xpath = "//*[@data-testid='bundle-billing-subcopy']")
    private ExtendedWebElement comboPlusBillingSubcopy;

    @FindBy(xpath = "//*[@data-testid='bundle-billing-subcopy-cta']")
    private ExtendedWebElement comboPlusBillingSubcopyCta;

    @FindBy(xpath = "//*[@data-testid='bundle-popover-copy']")
    private ExtendedWebElement comboPlusDisneyBillingSubcopy;

    @FindBy(xpath = "//*[@data-testid='bundle-popover-copy-link']")
    private ExtendedWebElement comboPlusDisneyBillingSubcopyCta;

    @FindBy(xpath = "//*[@data-testid='docomo-continue-button']")
    private ExtendedWebElement docomoContinueButton;

    @FindBy(xpath = "//*[@data-testid='CheckBoxOptIn-electronic_payment_consent']")
    private ExtendedWebElement electronicPaymentConsentOptInCheckBox;

    @FindBy(xpath = "//*[@data-testid='CheckBoxOptIn-third_party_provision_consent']")
    private ExtendedWebElement thirdPartyProvisionConsentOptInCheckBox;

    @FindBy(xpath = "//*[@data-testid='checkbox-opt-in-error']")
    private ExtendedWebElement optInErrorMessage;

    //Billing Page

    @FindBy(id = "row-list")
    private ExtendedWebElement billingRowListById;

    @FindBy(xpath = "//*[@data-testid='change-payment-option']")
    private ExtendedWebElement changePaymentCta;

    @FindBy(xpath = "(//div[@role='radio'])[2]")
    private ExtendedWebElement secondaryPaymentRadioBtn;

    @FindBy(xpath = "//*[@data-testid='stored-payment-submit-button']")
    private ExtendedWebElement storedPaymentSubmitBtn;

    @FindBy(xpath = "//*[@data-testid='stored-payment-review-button']")
    private ExtendedWebElement storedPaymentReviewBtn;

    @FindBy(id = "react-select-zip-code-dropdown-option-0-0")
    private ExtendedWebElement zipCodeDropdownItem;

    @FindBy(xpath = "//*[@data-testid='credit-debit-dropdown-menu']")
    private ExtendedWebElement paymentMethodDropdownMenu;

    @FindBy(xpath = "//*[@data-testid='province-dropdown-option-0']")
    private ExtendedWebElement provinceFirstOptionMobile;

    @FindBy(xpath = "//*[@data-testid='province-dropdown-menu']")
    private ExtendedWebElement proviceDropdownMenu;

    @FindBy(id = "billing-taxId")
    protected ExtendedWebElement billingTaxIdFieldId;

    @FindBy(id = "annual")
    protected ExtendedWebElement annualOptInCheckbox;

    @FindBy(xpath = "//*[@data-testid='billing-error-banner']")
    private ExtendedWebElement billingErrorBanner;

    @FindBy(xpath = "//*[@data-testid='billing-shell']")
    private ExtendedWebElement billingPage;

    @FindBy(id = "upgrade_index")
    private ExtendedWebElement upgradeIndex;

    @FindBy(xpath = "//*[@data-gv2elementkey='cvv_info']")
    private ExtendedWebElement cvvInfo;

    @FindBy(xpath = "//*[@data-testid='input-error-label']")
    private ExtendedWebElement inputErrorLabel;

    //Complete Purchase Page

    @FindBy(xpath = "//*[@data-testid='complete-purchase-cta']")
    private ExtendedWebElement completePurchaseCta;

    @FindBy(xpath = "//*[@data-testid='special-offer-complete-purchase-cta']")
    private ExtendedWebElement offerCompletePurchaseCta;

    @FindBy(xpath = "//*[@data-testid='restart-subscription-cta']")
    private ExtendedWebElement restartSubscriptionCta;

    @FindBy(xpath = "//*[@data-testid='regular-sign-up']")
    private ExtendedWebElement signUpNowBtn;

    @FindBy(xpath = "//*[@data-testid='standalone-buy-link']")
    private ExtendedWebElement signUpStandaloneLink;

    @FindBy(xpath = "//*[@data-testid='superbundle-buy-button']")
    private ExtendedWebElement superBuyNowBtn;

    @FindBy(xpath = "//*[@data-testid='combo-buy-button']")
    private ExtendedWebElement comboBuyNowBtn;

    @FindBy(xpath = "//*[@data-testid='megabundle-buy-button']")
    private ExtendedWebElement megaBuyNowBtn;

    @FindBy(xpath = "//*[@data-testid='payment-processing-spinner']")
    private ExtendedWebElement paymentProcessingSpinner;

    @FindBy(xpath = "//*[@data-testid='payment-processing-text']")
    private ExtendedWebElement paymentProcessingText;

    @FindBy(xpath = "//*[@data-testid='hulu-double-billed-title']")
    private ExtendedWebElement huluDoubleBilledTitle;

    @FindBy(xpath = "//*[@data-testid='review-subscription']")
    private ExtendedWebElement reviewSubscriptionPage;

    //License Plate

    @FindBy(xpath = "//*[@data-testid='license-plate-input-%s']")
    private ExtendedWebElement licensePlateInputFormat;

    @FindBy(xpath = "//*[@data-testid='digit-%s']")
    private ExtendedWebElement otpInputFormat;

    @FindBy(xpath = "//*[@data-testid='license-plate-submit']")
    private ExtendedWebElement licensePlateSubmit;

    @FindBy (xpath = "//*[@data-gv2elementkey='enterPasscode']")
    private ExtendedWebElement otpEnterPasscodeField;

    @FindBy(xpath = "//*[@data-testid='enter-passcode-submit-button']")
    private ExtendedWebElement enterPasscodeSubmitButton;

    @FindBy(xpath = "//*[@data-testid='cancel-btn']")
    private ExtendedWebElement cancelBtn;

    //Delete account page
    @FindBy(xpath = "//*[@data-testid='default-profile-delete-account-cta']")
    private ExtendedWebElement continueDeleteAccountCta;

    //delete account modal
    @FindBy(xpath = "//*[@data-testid='delete-account-modal-confirmation-cta']")
    private ExtendedWebElement deleteAccountConfirmBtn;

    //delete account success page
    @FindBy(xpath = "//*[@data-testid='delete-account-success-cta']")
    private ExtendedWebElement deleteAccountSuccessBtn;

    //Success Page

    @FindBy(xpath = "//*[@data-testid='onboarding-success-overlay-NU']")
    private ExtendedWebElement purchaseSuccessNewUser;

    @FindBy(xpath = "//*[@data-testid='purchase-success-confirm-button']")
    private ExtendedWebElement purchaseSuccessConfirmBtn;

    @FindBy(xpath = "//*[@data-testid='purchase-success-redirect-button']")
    private ExtendedWebElement purchaseSuccessRedirectBtn;

    @FindBy(xpath = "//*[@data-testid='crossSellingCTA']")
    private ExtendedWebElement purchaseSuccessRedirectMobileBtn;

    @FindBy(xpath = "//*[@data-testid='onboarding-success-overlay-PartnerSuccess']")
    private ExtendedWebElement purchaseSuccessPartnerSuccess;

    @FindBy(xpath = "//*[@data-testid='onboarding-success-overlay-PartnerStarSuccess']")
    private ExtendedWebElement purchaseSuccessPartnerStarSuccess;

    @FindBy(xpath = "//*[@data-testid='onboarding-success-overlay-NUVerizon']")
    private ExtendedWebElement purchaseSuccessNewUserVerizon;

    @FindBy(xpath = "//*[@data-testid='onboarding-success-overlay-SBDisney']")
    private ExtendedWebElement purchaseSuccessSuperBundle;

    @FindBy(xpath = "//*[@data-testid='onboarding-success-overlay-MegaBundleSuccess']")
    private ExtendedWebElement purchaseSuccessMegaBundle;

    @FindBy(xpath = "//*[@data-testid='onboarding-success-overlay-ComboPlusSuccess']")
    private ExtendedWebElement purchaseSuccessComboPlusSuccess;

    @FindBy(xpath = "//*[@data-testid='onboarding-success-overlay-SBHulu']")
    private ExtendedWebElement purchaseSuccessSuperbundleIncoming;

    @FindBy(xpath = "//*[@aria-label='Hulu']")
    private ExtendedWebElement huluOutgoingLink;

    //Paypal Sandbox Elements

    @FindBy(css = "p.paypal-logo paypal-logo-long")
    private ExtendedWebElement paypalLogo;

    @FindBy(id = "email")
    private ExtendedWebElement paypalEmailFldById;

    @FindBy(id = "password")
    private ExtendedWebElement paypalPasswordFldById;

    @FindBy(id = "btnLogin")
    private ExtendedWebElement paypalLoginBtnById;

    @FindBy(id = "btnNext")
    private ExtendedWebElement paypalNextBtnById;

    @FindBy(id = "b07bfe8df73ce97d3532e9ecc59cca80")
    private ExtendedWebElement paypalPurchaseChaseRadioBtnById;

    @FindBy(id = "8629b2789d4ab2f5a0410c3d1a328f20")
    private ExtendedWebElement paypalPurchaseBankPlatinumRewardsRadioBtnById;

    @FindBy(id = "button")
    private ExtendedWebElement paypalPurchaseContinueBtnById;

    @FindBy(id = "payment-submit-btn")
    private ExtendedWebElement paypalSubmitPayBtnById;

    @FindBy(id = "acceptAllButton")
    private ExtendedWebElement paypalAcceptAllCookiesBtn;

    @FindBy(xpath = "//*[@data-test-id='continueButton']")
    private ExtendedWebElement paypalPurchaseContinueBtn;

    @FindBy(xpath = "//span[@class='cardMask']")
    private ExtendedWebElement paypalCardMask;

    //WordPay Sandbox Elements

    @FindBy(id = "billing-ideal-bank")
    private ExtendedWebElement idealBankSelectorId;

    @FindBy(id = "react-select-image-dropdown-option-0")
    private ExtendedWebElement idealReactIndex0;

    @FindBy(xpath = "//*[@data-testid='selection-option-0']")
    private ExtendedWebElement idealReactMobileIndex0;

    @FindBy(id = "op-Auth")
    private ExtendedWebElement wordPayAuthorizeBtnById;

    @FindBy(id = "op-Reject")
    private ExtendedWebElement wordPayRejectBtnById;

    @FindBy(id = "jsEnabled")
    private ExtendedWebElement wordPayContinueBtnById;

    @FindBy(xpath = "//select[@name='status']")
    private ExtendedWebElement wordPayOutcomeDropDownlist;

    @FindBy(xpath = "//option[@value='REFUSED']")
    private ExtendedWebElement wordPayOutcomeRefused;

    @FindBy(xpath = "//option[@value='AUTHORISED']")
    private ExtendedWebElement wordPayOutcomeAuthorised;

    @FindBy(id = "csc-popover-container")
    private ExtendedWebElement cscPopoverContainer;

    @FindBy(xpath = "//*[@aria-label='close']")
    private ExtendedWebElement cscPopoverContainerClose;

    @FindBy(xpath = "//*[@data-gv2elementkey='learnMore']")
    private ExtendedWebElement learnMore;

    //Early Access
    @FindBy(xpath = "//*[@data-testid='details-signup-early-access-cta']")
    private ExtendedWebElement premierAccessCta;

    @FindBy(xpath = "//*[@data-testid='ea-standalone-offer-charge']")
    private ExtendedWebElement eaStandalonePrice;

    @FindBy(xpath = "//*[@data-testid='ea-offer-charge']")
    private ExtendedWebElement eaOfferPrice;

    @FindBy(xpath = "//*[@data-testid='details-ea-access-confirmation']")
    private ExtendedWebElement eaConfirmation;

    @FindBy(xpath = "//*[@data-testid='play-button']")
    private ExtendedWebElement playBtn;

    //3DS2
    @FindBy(css = "iframe[id='challengeFrame']")
    private ExtendedWebElement threeDsTwoIframe;

    //Star
    @FindBy(xpath = "//*[@data-testid='decision-intro-button']")
    private ExtendedWebElement decisionIntroButton;

    @FindBy(xpath = "//*[@data-testid='maturity-rating-button']")
    private ExtendedWebElement maturityRatingContinueBtn;

    @FindBy(xpath = "//*[@data-testid='password-continue-login']")
    private ExtendedWebElement passwordContinueLoginBtn;

    @FindBy(xpath = "//*[@data-testid='password-cancel-login']")
    private ExtendedWebElement passwordCancelLoginBtn;

    @FindBy(xpath = "//*[@data-testid='cancel-button']")
    private ExtendedWebElement cancelButton;

    @FindBy(xpath = "//*[@data-testid='set-pin-button']")
    private ExtendedWebElement createProfilePinButton;

    @FindBy(css = "[data-testid='create-pin-cancel-button']")
    private ExtendedWebElement notNowButton;

    @FindBy(id = "account-dropdown")
    private ExtendedWebElement accountDropdownId;

    @FindBy(xpath = "//*[@data-testid='bundle-success-cta']")
    private ExtendedWebElement bundleSuccessContinueButton;

    //Get Elements

    public ExtendedWebElement getPrivacyPolicy1() {
        return privacyPolicy1;
    }

    public ExtendedWebElement getPrivacyPolicy2() {
        return privacyPolicy2;
    }

    public ExtendedWebElement getSubscriberAgreement() {
        return subscriberAgreement;
    }

    public ExtendedWebElement getZipCodeDropdownItem() {
        return zipCodeDropdownItem;
    }

    public ExtendedWebElement getPaymentMethodCreditCard() {
        return paymentMethodDropdownMenu;
    }

    public ExtendedWebElement getFirstProvinceOptionMobile() {
        return provinceFirstOptionMobile;
    }

    public ExtendedWebElement getProviceDropdownMenu() {
        return proviceDropdownMenu;
    }

    public ExtendedWebElement getAnnualOptInCheckbox() {
        return annualOptInCheckbox;
    }

    public ExtendedWebElement getLicensePlateInputFormat(String index) {
        return licensePlateInputFormat.format(index);
    }

    public ExtendedWebElement getOtpInputFormat(String index) {
        return otpInputFormat.format(index);
    }

    public ExtendedWebElement getOtpEnterPasscodeField() {
        return otpEnterPasscodeField;
    }

    public ExtendedWebElement getEnterPasscodeSubmitButton() {
        return enterPasscodeSubmitButton;
    }

    public ExtendedWebElement getCancelBtn() {
        return cancelBtn;
    }

    public ExtendedWebElement getModalPrimaryBtn() {
        return modalPrimaryBtn;
    }

    public ExtendedWebElement getDeleteAccountConfirmBtn() {
        return deleteAccountConfirmBtn;
    }

    public ExtendedWebElement getDeleteAccountSuccessBtn() {
        return deleteAccountSuccessBtn;
    }

    @Override
    public ExtendedWebElement getPasswordContinueLoginBtn() {
        return passwordContinueLoginBtn;
    }

    //Element Booleans

    public boolean isAdBlurbPresent() {
        return adBlurb.isElementPresent();
    }

    public boolean isEuPrivacyAgreementPresent() {
        return euPrivacyAgreement.isElementPresent();
    }

    public boolean isTermsTextPresent() {
        return termsText.isElementPresent();
    }

    public boolean isBrandImagePresent() {
        return brandImage.isElementPresent();
    }

    public boolean isRowListByIdPresent() {
        return billingRowListById.isElementPresent();
    }

    public boolean isMonthlyRadioBtnByIdPresent() {
        if (!DPLUS_SPECIAL_OFFER_ENABLED) {
            return monthlyRadioBtnId.isPresent();
        }
        return true;
    }

    public boolean isCreditCardRadioBtnByIdChecked() {
        return creditRadioIconId.isChecked();
    }

    public boolean isCreditRadioBtnAriaChecked() {
        return creditRadioIconAria.isChecked();
    }

    public boolean isMonthlyRadioBtnByIdChecked() {
        if (subscribeOptions.isElementPresent(2)) {
            return monthlyRadioBtnId.isChecked();
        }
        return true;
    }

    public boolean isYearlyRadioBtnByIdPresent() {
        if (subscribeOptions.isElementPresent(2)) {
            return yearlyRadioBtnId.isPresent();
        }
        return true;
    }

    public boolean isYearlyRadioBtnByIdChecked() {
        if (subscribeOptions.isElementPresent(2)) {
            return yearlyRadioBtnId.isChecked();
        }
        return true;
    }

    public boolean isCompletePurchaseCtaPresent() {
        if (DPLUS_SPECIAL_OFFER_ENABLED) {
            return offerCompletePurchaseCta.isElementPresent();
        } else {
            return completePurchaseCta.isElementPresent();
        }
    }

    public boolean isPayPalRadioIconByIdPresent() {
        return paypalRadioIconId.isPresent();
    }

    public boolean isPayPalRadioIconByTestIdChecked() {
        return paypalRadioIconTestId.isChecked();
    }

    public boolean isGenericPayPalBtnPresent() {
        return paypalGenericBtn.isElementPresent();
    }

    public boolean isKlarnaRadioIconIdPresent() {
        return klarnaRadioIconId.isElementPresent();
    }

    public boolean isHuluOutgoingLinkPresent() {
        return huluOutgoingLink.isElementPresent();
    }

    public boolean isPurchaseSuccessConfirmBtnPresent() {
        return purchaseSuccessConfirmBtn.isElementPresent();
    }

    public boolean isPurchaseSuccessRedirectBtnPresent() {
        return purchaseSuccessRedirectBtn.isElementPresent();
    }

    public boolean isPurchaseSuccessNewUser() {
        return purchaseSuccessNewUser.isElementPresent();
    }

    public boolean isPurchaseSuccessPartnerSuccess() {
        return purchaseSuccessPartnerSuccess.isElementPresent();
    }

    public boolean isPurchaseSuccessPartnerStarSuccess() {
        return purchaseSuccessPartnerStarSuccess.isElementPresent();
    }

    public boolean isPurchaseSuccessNewUserVerizon() {
        return purchaseSuccessNewUserVerizon.isElementPresent();
    }

    public boolean isPurchaseSuccessSuperBundle() {
        return purchaseSuccessSuperBundle.isElementPresent();
    }
    public boolean isPurchaseSuccessMegaBundle() {
        return purchaseSuccessMegaBundle.isElementPresent();
    }
    public boolean isPurchaseSuccessComboPlusSuccess() {
        return purchaseSuccessComboPlusSuccess.isElementPresent();
    }

    public boolean isPurchaseSuccessSuperbundleIncoming() {
        return purchaseSuccessSuperbundleIncoming.isElementPresent();
    }

    public boolean isPaymentProcessingSpinnerPresent() {
        return paymentProcessingSpinner.isElementPresent();
    }

    public void isAccountHoldPaymentCtaPresent() {
        accountHoldUpdatePaymentCta.isElementPresent();
    }

    public boolean isCancelContractPresent() {
        return cancelContract.isElementPresent();
    }

    public boolean isGetAppPagePresent() {
        return getAppPage.isElementPresent();
    }

    public boolean isGetAppEAPagePresent() {
        if (!DISABLE_TANDEM_FLOW) {
            return getAppEAPage.isElementPresent();
        } else {
            return isGetAppPagePresent();
        }
    }

    public boolean isGetAppBundlePagePresent() {
        return getAppBundlePage.isElementPresent();
    }

    public boolean isDoubleBilledHuluKeyPresent() {
        return huluDoubleBilledTitle.isElementPresent();
    }

    public boolean isReviewSubscriptionPagePresent() {
        return reviewSubscriptionPage.isElementPresent();
    }

    public boolean isEAStandalonePricePresent() {
        if (!DISABLE_TANDEM_FLOW) {
            return eaStandalonePrice.isElementPresent();
        } else {
            return true;
        }
    }

    public boolean isEAOfferPricePresent() {
        if (!DISABLE_TANDEM_FLOW) {
            return eaOfferPrice.isElementPresent();
        } else {
            return true;
        }
    }

    public boolean isEAConfirmationPresent() {
        return eaConfirmation.isElementPresent() || playBtn.isElementPresent();
    }

    public boolean isUSCountry(String locale) {
        return disneyGlobalUtils.getBooleanFromCountries(locale, IS_BUNDLE_COUNTRY);
    }

    public boolean isRegularSignUpBtnPresent() {
        return signUpNowBtn.isElementPresent();
    }

    public boolean isBundleOfferCardPresent() {
        return bundleOfferCard.isElementPresent();
    }

    public boolean isComboPlusSubcopyPresent() {
        return comboPlusBillingSubcopy.isElementPresent();
    }

    public boolean isBundleUpgradeCardPresent() {
        return bundleUpgradeCard.isElementPresent();
    }

    public boolean isComboPlusSubcopyCtaPresent() {
        return comboPlusBillingSubcopyCta.isElementPresent();
    }

    public boolean isComboPlusDisneySubcopyCtaPresent() {
        return comboPlusDisneyBillingSubcopyCta.isElementPresent();
    }

    public boolean isComboPlusDisneySubcopyPresent() {
        return comboPlusDisneyBillingSubcopy.isElementPresent();
    }

    public boolean isBundleUpgradeCardNotPresent() {
        return bundleUpgradeCard.isElementNotPresent(SHORT_TIMEOUT);
    }

    public boolean isBundleOfferCardDowngradePresent() {
        return bundleOfferCardDowngrade.isElementPresent();
    }

    public boolean isModalPrimaryBtnPresent() {
        return getModalPrimaryBtn().isElementPresent();
    }

    public boolean isPayPalSubmitBtnByIdPresent() {
        return paypalSubmitBtnById.isElementPresent();
    }

    public boolean isChallengeFormSubmitBtnByIdPresent() {
        return challengeFormSubmitBtnById.isElementPresent();
    }

    public boolean isChallengeFormSubmitBtn2ByIdPresent() {
        return challengeFormSubmitBtn2ById.isElementPresent();
    }

    public boolean isThreeDsTwoIframePresent() {
        return threeDsTwoIframe.isElementPresent();
    }

    public boolean isAccountDropdownIdPresent() {
        return accountDropdownId.isElementPresent();
    }

    public boolean isPremierAccessCtaPresent() {
        return premierAccessCta.isElementPresent();
    }

    public boolean isBillingPagePresent() {
        return billingPage.isElementPresent();
    }

    public boolean isUpgradePagePresent() {
        return upgradeIndex.isElementPresent();
    }

    public boolean isCvvInfoPresent() {
        return cvvInfo.isElementPresent();
    }

    public boolean isInputErrorLabelPresent() {
        return inputErrorLabel.isElementPresent();
    }

    public boolean isOverlayCloseBtnPresent() {
        return cscPopoverContainerClose.isElementPresent();
    }

    public boolean isBillingOrUpgradePagePresent() {
        return IS_STAR ? isBillingPagePresent() : isUpgradePagePresent();
    }

    public boolean isChangePaymentCtaPresent() {
        return changePaymentCta.isElementPresent();
    }

    public boolean isSecondaryPaymentRadioBtnPresent() {
        return secondaryPaymentRadioBtn.isElementPresent();
    }

    public boolean isStoredPaymentSubmitBtnPresent() {
        return storedPaymentSubmitBtn.isElementPresent();
    }

    public boolean isDocomoContinueButton() {
        return docomoContinueButton.isElementPresent();
    }

    //Assertions
    public void assertBillingPageElements(SoftAssert sa, Boolean monthlyRadioBtn, Boolean yearlyRadioBtn, boolean checkMonthly) {
        if (subscribeOptions.isElementPresent(2)) {
            if (checkMonthly) {
                sa.assertTrue(monthlyRadioBtn, "Monthly subscription radio button is not checked");
                sa.assertFalse(yearlyRadioBtn, "Yearly subscription radio button is checked");
            } else {
                sa.assertFalse(monthlyRadioBtn, "Monthly subscription radio button is not checked");
                sa.assertTrue(yearlyRadioBtn, "Yearly subscription radio button is checked");
            }
        }
    }

    public void assertSuperbundlePageElements(SoftAssert sa, Boolean assertionOne, Boolean assertionTwo, boolean checkUrl, String urlToAssert) {
        sa.assertTrue(assertionOne, "Bundle offer card does not exist");
        sa.assertTrue(assertionTwo, "Bundle upgrade card does not exist");

        if (checkUrl) {
            assertUrlContains(sa, urlToAssert);
        }
    }

    public void assertEAElements(SoftAssert sa, Boolean priceOne, Boolean priceTwo) {
        if (!DISABLE_TANDEM_FLOW) {
            sa.assertTrue(priceOne, "Price one not found");
            sa.assertTrue(priceTwo, "Price two not found");
        }
    }

    public void assertLogo(SoftAssert sa, Boolean logo) {
            sa.assertTrue(logo, "logo not present on page");
    }

    public void assertMobileUserGetApp(SoftAssert sa, String locale, boolean checkStarz) {
        if (checkStarz) {
            adBlurbAssert(sa, locale);
        }
        isGetAppPagePresent();
        assertUrlContains(sa, GET_APP_URL);
    }

    public void assertUserSelectProfilePage(SoftAssert sa, String locale, String webUrlAssert, boolean isMobile) {
        if (isMobile) {
            DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
            DisneyPlusBaseProfileViewsPage profileViewsPage = new DisneyPlusBaseProfileViewsPage(getDriver());
            assertMobileUserGetApp(sa, locale, false);
            accountPage.clickOnAccountDropdown(isMobile);
            profileViewsPage.getActiveProfile();
            sa.assertTrue(profileViewsPage.isDropdownProfileFirstPresent());
        } else {
            assertUrlContains(sa, webUrlAssert);
        }
    }

    public void assertPurchaseWithSuccessOverlay(SoftAssert sa, String locale, boolean isMobile, boolean isBundle, boolean isPartner) {
        LOGGER.info("Attempting to verify assertPurchaseWithSuccessOverlay");
        if (isMobile) {
            isGetAppPagePresent();
            assertUrlContains(sa, GET_APP_URL);
            adBlurbAssert(sa, locale);
        } else {
            if (checkMatureContentOnboardingCountries(locale)) {
                handleMatureContentOnboardingFlow(false);
                clickMatureContentOnboardingBannerButton(locale);
                sa.assertTrue(isAccountDropdownIdPresent(), "Account dropdown not present");
            } else {
                handleSuccessOverlayByType(sa, locale, isBundle, isPartner);
            }
        }
    }

    public void handleSuccessOverlayByType(SoftAssert sa, String locale, boolean isBundle, boolean isPartner) {
        isPurchaseSuccessConfirmBtnPresent();
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);

        if (isBundle) {
            if (isComboCountry && !isMegaBundleTest()) {
                sa.assertTrue(isPurchaseSuccessComboPlusSuccess(), "Onboarding Success Overlay comboplus is not Present on Success Screen after Purchase");
            } else if (isMegaBundleTest()) {
                sa.assertTrue(isPurchaseSuccessMegaBundle(), "Onboarding Success Overlay megabundle is not Present on Success Screen after Purchase");
            } else  {
                sa.assertTrue(isPurchaseSuccessSuperBundle(), "Onboarding Success Overlay superbundle is not Present on Success Screen after Purchase");
            }
        } else if (isPartner) {
            if (IS_STAR) {
                sa.assertTrue(isPurchaseSuccessPartnerStarSuccess(), "Onboarding Success Overlay star partner is not Present on Success Screen after Purchase");
            } else {
                sa.assertTrue(isPurchaseSuccessPartnerSuccess(), "Onboarding Success Overlay partner is not Present on Success Screen after Purchase");
            }
        } else {
            sa.assertTrue(isPurchaseSuccessNewUser(), "Onboarding Success Overlay is not Present on Success Screen after Purchase");
        }
        sa.assertTrue(isPurchaseSuccessConfirmBtnPresent(), "Confirm Button not present on Success Screen after Purchase");
        adBlurbAssert(sa, locale);
    }

    public void assertSuccessOverlay(SoftAssert sa, Boolean successTitle, Boolean successButton, boolean checkStarz, String locale, boolean isMobile) {
        LOGGER.info("Attempting to verify assertSuccessOverlay");
        if (isMobile) {
            isGetAppPagePresent();
            assertUrlContains(sa, GET_APP_URL);
        } else {
                sa.assertTrue(successTitle, "Onboarding Success Overlay is not Present on Success Screen after Purchase");
                sa.assertTrue(successButton, "Confirm Button not present on Success Screen after Purchase");
        }
            if (checkStarz) {
                adBlurbAssert(sa, locale);
            }
        }

    public void assertSuccessWithoutOverlay(SoftAssert sa, Boolean accountDropdown) {
        String currentUrl = getCurrentUrl();

        sa.assertTrue(accountDropdown, "Account dropdown not present after purchase");
        sa.assertTrue(currentUrl.contains("home"), "Home URL not present after purchase, Actual: " + currentUrl);
    }

    public void assertSuccessWithoutOverlay(SoftAssert sa, Boolean accountDropdown, boolean isMobile) {
        if (isMobile) {
            isGetAppPagePresent();
            assertUrlContains(sa, GET_APP_URL);
        } else {
            assertSuccessWithoutOverlay(sa, accountDropdown);
        }
    }

    public void assertSuccessWithoutOverlay(SoftAssert sa, Boolean accountDropdown, String url) {
        sa.assertTrue(accountDropdown, "Account dropdown not present after purchase");

        String currentUrl = getCurrentUrl();

        sa.assertTrue(currentUrl.contains(url), url + " not present after purchase, Actual: " + currentUrl);
    }


    public void assertSuccessWithoutOverlay(SoftAssert sa, Boolean accountDropdown, String url, boolean isMobile) {

    String currentUrl = getCurrentUrl();
        if (isMobile) {
            isGetAppPagePresent();
            assertUrlContains(sa, GET_APP_URL);
        } else {
            sa.assertTrue(accountDropdown, "Account dropdown not present after purchase");
            sa.assertTrue(currentUrl.contains(url), url + " not present after purchase, Actual: " + currentUrl);
        }
    }

    public void assertPaymentProccesing(SoftAssert sa, boolean isFail, Boolean paymentProcessingCopy, Boolean formErrorCopy) {
        if (isFail) {
            sa.assertFalse(paymentProcessingCopy,
                    "Payment processing element is present after WordPay Transaction");
            sa.assertTrue(formErrorCopy,
                    "FormError Notify User Payment Element not present after WordPay Transaction");
        } else {
            sa.assertTrue(paymentProcessingCopy,
                    "Payment processing element is present after WordPay Transaction");
        }
    }

    public void assertCreditCardPaymentBlocked(SoftAssert sa) {
        sa.assertTrue(isPaymentBlockedBtnIdPresent(), "Change Payment Button not present after blocked transaction");

        String currentUrl = getCurrentUrl();

        sa.assertTrue(currentUrl.contains("payment-blocked"), "URL does not contain 'payment-blocked', Actual: " + currentUrl);
    }

    public void assertBundleUpgradePurchaseSuccess(SoftAssert sa) {
        LOGGER.info("Attempting to verify assertBundleUpgradePurchaseSuccess");
            DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
            accountPage.isBundleSuccessPage();
            assertUrlContains(sa, "bundle-success");
        }

    public void assertPlansPurchase(SoftAssert sa, String locale, boolean isMobile, boolean isBundle) {
        if (isMobile) {
            isGetAppPagePresent();
            assertUrlContains(sa, GET_APP_URL);
            adBlurbAssert(sa, locale);
        } else {
            if (isBundle) {
                sa.assertTrue(isPurchaseSuccessConfirmBtnPresent(), "Confirm Button not present on Success Screen after Purchase");
                sa.assertTrue(isPurchaseSuccessSuperBundle(), "Onboarding Success Overlay superbundle is not Present on Success Screen after Purchase");
                clickStartStreamingBtnKey();
            }
            handleMatureContentOnboardingFlow(false);
            clickMatureContentOnboardingBannerButton(locale);
            sa.assertTrue(isAccountDropdownIdPresent(), "Account dropdown not present");
        }
    }

    public boolean isBillingErrorBannerPresent() {
        return billingErrorBanner.isElementPresent();
    }

    public boolean isCreditRadioIconByIdPresent() {
        return creditRadioIconId.isElementPresent();
    }

    public boolean isCreditRadioLabelPresent() {
        return creditRadioLabel.isElementPresent();
    }

    public boolean isCreditRadioIconAriaPresent() {
        return creditRadioIconAria.isElementPresent();
    }

    public boolean isMercadoRadioIconIdPresent() {
        return mercadoRadioIconId.isPresent();
    }

    public boolean isMercadoPagoIconPresent() {
        return mercadoRadioIconId.isElementPresent();
    }

    public boolean isCreditSubmitBtnIdPresent() {
        return creditSubmitBtnId.isElementPresent(HALF_TIMEOUT);
    }

    public boolean isPaymentBlockedBtnIdPresent() {
        return paymentBlockedBtnId.isElementPresent();
    }

    public boolean isSpeedBumpContinuePresent() {
        return speedBumpContinue.isElementPresent();
    }

    public boolean isErrorProcessingPresent() {
        return errorProcessing.isElementPresent();
    }

    public boolean isErrorOnAccountPresent() {
        return errorOnAccount.isElementPresent();
    }

    public boolean isIneligibleHuluSubscriberHeaderPresent() {
        return ineligibleHuluSubscriberHeader.isElementPresent();
    }

    public boolean isRedirectHuluSubscriberHeaderPresent() {
        return redirectHuluSubscriberHeader.isElementPresent();
    }

    public boolean isRedirectHuluSubscriberButtonPresent() {
        return redirectHuluSubscriberButton.isElementPresent();
    }

    public boolean isCreateAccountHuluContinueButtonPresent() {
        return createAccountHuluContinueButton.isElementPresent();
    }

    public boolean isGenericValueSubmitBtnPresent() {
        return getDplusBaseGenericSubmitValueBtn().isElementPresent();
    }

    public boolean isCreditSubmitBtnIdClickable() {
        return creditSubmitBtnId.isClickable();
    }

    public boolean isCreditSubmitBtnIdVisible() {
        return creditSubmitBtnId.isVisible();
    }

    public boolean isMaturityRatingContinueBtnVisible() {
        LOGGER.info("Is maturity rating continue button visible");
        return maturityRatingContinueBtn.isVisible();
    }

    public boolean isOptInErrorMessagePresent() {
        return optInErrorMessage.isElementPresent();
    }

    public boolean isCscPopoverContainerPresent() {
        return cscPopoverContainer.isElementPresent();
    }

    public boolean isComboPlusUpsellPresent() {
        return bundleUpsellToggle.isElementPresent();
    }

    //Clicks

    public void clickGenericPayPalBtn() {
        paypalGenericBtn.click();
    }

    public boolean clickPremierAccessCta() {
        pause(DELAY); // Needed for extra padding time
        if (isPremierAccessCtaPresent()) {
            premierAccessCta.click();
        } else {
            LOGGER.info("Premier access cta not found, refreshing!");
            refresh();
            pause(DELAY); // Needed for extra padding time
            if (isPremierAccessCtaPresent()) {
                premierAccessCta.click();
            } else {
                return false;
            }
        }
        return true;
    }

    public void clickGenericContinueTextBtn() {
        getDplusBaseGenericContinueTextBtn().click();
    }

    public void clickGenericValueSubmitBtn() {
        getDplusBaseGenericSubmitValueBtn().click();
    }

    public void clickChallengeFormSubmitBtn(String locale) {
        boolean isEuCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_EU_COUNTRY);
        if (isEuCountry && isThreeDsTwoIframePresent()) {
            getDriver().switchTo().frame(threeDsTwoIframe.getElement());
            waitFor(challengeFormSubmitBtnById);
            challengeFormSubmitBtnById.clickIfPresent();
            challengeFormSubmitBtn2ById.clickIfPresent(SHORT_TIMEOUT);
            getDriver().switchTo().defaultContent();
        } else {
            if (ENVIRONMENT.equalsIgnoreCase("QA")) {
                isPurchaseSuccessConfirmBtnPresent(); // Needed sometimes in QA
            }
        }
    }

    public void clickGenericTypeSubmitBtn() {
        getdPlusBaseGenericSubmitTypeBtn().click();
    }

    public void clickPayPalRadioIconById(String locale) {
        boolean isCreditCardOnlyCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_CREDITCARD_ONLY_COUNTRY);
        if (isCreditCardOnlyCountry) {
            CurrentTest.revertRegistration();
            throw new SkipException("Skipping test due to PayPal not being available");
        }

        isPayPalRadioIconByIdPresent();
        pause(ONE_SEC_TIMEOUT); // Allow extra 1 seconds for page to load
        paypalRadioIconTestId.click();
    }

    public void clickCompletePurchaseCta() {
        if (DPLUS_SPECIAL_OFFER_ENABLED) {
            offerCompletePurchaseCta.click();
        } else {
            completePurchaseCta.click();
        }
    }

    public void clickRestartSubscriptionCta() {
        if (DPLUS_SPECIAL_OFFER_ENABLED) {
            offerCompletePurchaseCta.click();
        } else {
            completePurchaseCta.click();
        }
    }

    public void clickCompleteAndRestartPurchaseSignUpNowBtn(String locale) {
        boolean isMegaBundleCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_MEGABUNDLE_COUNTRY);
        if (isMegaBundleCountry && signUpStandaloneLink.isElementPresent(SHORT_TIMEOUT)) {
            signUpStandaloneLink.clickByJs();
        } else {
            signUpNowBtn.clickByJs();
            if (locale.equalsIgnoreCase(WebConstant.US)) {
                DisneyPlusPlansPage plansPage = new DisneyPlusPlansPage(getDriver());
                plansPage.clickPlanCTA(PlanCardTypes.PlanSelectCard.DISNEY_PLUS_WITH_ADS);
            }
        }
    }

    public void clickSignUpNowBtn() {
        signUpNowBtn.clickByJs();
    }

    public void clickCompletePurchaseSuperBundleBuyNowBtn() {
        superBuyNowBtn.clickByJs();
    }

    public void clickCompletePurchaseComboBuyNowBtn() {
        comboBuyNowBtn.clickByJs();
    }

    public void clickCompletePurchaseMegaBuyNowBtn() {
        megaBuyNowBtn.clickByJs();
    }

    public void clickCreditCardRadioIconById() {
        creditRadioIconId.clickByJs();
    }

    public void clickCreditRadioIconAria() {
        creditRadioIconAria.clickByJs();
    }

    public void clickMercadoRadioIconId() {
        mercadoRadioIconId.click();
    }

    public void clickMonthlyRadioBtnById() {
        monthlyRadioBtnId.isElementPresent();
        monthlyRadioBtnId.clickByJs();
    }

    public void clickYearlyRadioBtnById() {
        yearlyRadioParentBtn.click();
    }

    public void clickBundleUpsellToggle() {
        bundleUpsellToggle.click();
    }

    public void clickSpeedBumpContinue() {
        speedBumpContinue.click();
    }

    public void clickWorldPayBtnById() {
        waitFor(wordPayAuthorizeBtnById,10);
        wordPayAuthorizeBtnById.click();
    }

    public void clickWorldPayRejectBtnById() {
        wordPayRejectBtnById.click();
    }

    public void clickWorldPayContinueBtnById() {
        waitFor(wordPayContinueBtnById);
        wordPayContinueBtnById.hover();
        wordPayContinueBtnById.click();
    }

    public void clickWordPayOutcomeDropDownList() {
        wordPayOutcomeDropDownlist.click();
    }

    public void selectWordPayOutcomeDropDownListRefused() {
        wordPayOutcomeDropDownlist.select(" Refused ");
    }

    public void clickWordPayOutcomeRefused() {
        wordPayOutcomeRefused.click();
    }

    public void clickWordPayOutcomeAuthorised() {
        wordPayOutcomeAuthorised.click();
    }

    public void clickCloseCscPopoverContainer() {
        cscPopoverContainerClose.click();
    }

    public void clickLearnMore() {
        learnMore.click();
    }

    //Element loads additional element that throws off ID, adding a pause to compensate.
    public void clickPayPalSubmitBtnById() {
        LOGGER.info("Attempting to Click PayPal Submit in 8 seconds on Billing");
        pause(HALF_TIMEOUT);
        paypalSubmitBtnById.scrollTo();
        pause(SHORT_TIMEOUT);
        paypalSubmitBtnById.click();
    }

    public void clickIdealBankSelectorId() {
        waitFor(idealBankSelectorId);
        idealBankSelectorId.click();
    }

    public void clickIdealBankIndex0ById() {
        waitFor(idealReactIndex0);
        idealReactIndex0.click();
    }

    public void clickIdealReactMobileIndex0ById() {
        waitFor(idealReactMobileIndex0);
        idealReactMobileIndex0.click();
    }

    public void clickPaypalReviewSubscriptionCta() {
        paypalReviewSubscriptionCta.click();
    }

    public void clickPaypalAcceptAllCookiesBtn() {
        pause(SHORT_TIMEOUT);
        LOGGER.info("Attempting to click PayPal Cookie Banner");
        paypalAcceptAllCookiesBtn.clickIfPresent(SHORT_TIMEOUT);
    }

    public void clickCvvInfo() { cvvInfo.click(); }

    public void clickPaypalSubmitPayBtnById() {
        paypalSubmitPayBtnById.clickByJs();
    }

    public void clickPayPalPurchaseContinueBtnById() {
        paypalPurchaseContinueBtnById.clickIfPresent();
    }

    public void clickPayPalPurchaseContinueBtn() {
        paypalPurchaseContinueBtn.click();
    }

    public void clickHuluOutgoingLink() {
        huluOutgoingLink.click();
    }

    public void clickStartStreamingBtnKey() {
        purchaseSuccessConfirmBtn.click();
    }

    public void clickBackToShopDisneyBtn(Boolean isMobile) {
        if (isMobile) {
            purchaseSuccessRedirectMobileBtn.click();
        } else {
            purchaseSuccessRedirectBtn.click();
        }
    }

    public void clickAccountHoldUpdatePaymentCta() {
        accountHoldUpdatePaymentCta.click();
    }

    public void clickChangePaymentCta() {
        changePaymentCta.clickByJs();
    }

    public void clickSecondaryPaymentRadioBtn() {
        secondaryPaymentRadioBtn.click();
    }

    public void clickChangePaymentCtaIfPresent() {
        if (changePaymentCta.isElementPresent(SHORT_TIMEOUT)) {
            changePaymentCta.clickByJs();
        }
    }

    public void clickStoredPaymentSubmitBtn() {
        storedPaymentSubmitBtn.clickByJs();
    }

    public void clickStoredPaymentSubmitBtnIfPresent() {
        if (storedPaymentSubmitBtn.isPresent(ONE_SEC_TIMEOUT)) {
            storedPaymentSubmitBtn.clickByJs();
        }
    }

    public void clickStoredPaymentReviewBtnIfPresent() {
        storedPaymentReviewBtn.clickIfPresent(ONE_SEC_TIMEOUT);
    }

    public void clickBundleUpgradeCardLink() {
        bundleUpgradeCardLink.clickByJs();
    }

    public void clickIneligibleHuluSubscriberButton() {
        ineligibleHuluSubscriberButton.click();
    }

    public void clickMaturityRatingContinueBtn() {
        maturityRatingContinueBtn.clickByJs();
    }

    public void clickDecisionIntroButton() {
        if (decisionIntroButton.isPresent()) {
            decisionIntroButton.clickByJs();
        }
    }

    public void clickPasswordContinueLoginBtn() {
        getPasswordContinueLoginBtn().click();
    }

    public void clickPasswordCancelLoginBtn(){
        waitForPageToFinishLoading();
        passwordCancelLoginBtn.clickIfPresent();
    }

    public void clickCancelButton() {
        cancelButton.clickByJs(DELAY);
    }

    public void clickCreatePinNotNowButton() {
        if (notNowButton.isPresent(SHORT_TIMEOUT)) {
            LOGGER.info("Clicking not now on the 'You have access to the full catalog! overlay'");
            notNowButton.click();
        }
    }

    public void clickMercadoSubmitBtn() {
        pause(SHORT_TIMEOUT); // Needed to load mercado stuff
        mercadoReviewBtn.clickIfPresent(SHORT_TIMEOUT);
        mercadoSubmitBtn.clickIfPresent(SHORT_TIMEOUT);
    }

    public void clickLegalCopyLinksAssert(SoftAssert sa, ExtendedWebElement e, String assertUrl) {

        e.click();
        waitForPageToFinishLoading();

        String urlAssert = getCurrentUrl();

        sa.assertTrue(urlAssert.contains(assertUrl),
                assertionUrlString(getCurrentUrl(), assertUrl));

        navigateBack();

    }

    public void clickCreditSubmitBtnById() {
        waitFor(creditSubmitBtnId);
        //creditSubmitBtnId.hover();
        creditSubmitBtnId.clickByJs();
    }

    public void clickCreditSubmitBtnIfPresent() {
        if(creditSubmitBtnId.isElementPresent(ONE_SEC_TIMEOUT)) {
            creditSubmitBtnId.clickByJs();
        }
    }

    public void handleOptInCheckBoxesOnBilling(String locale) {
        boolean isBillingCheckBoxOptInCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_BILLING_CHECKBOX_OPTIN_COUNTRY);

        if (isBillingCheckBoxOptInCountry) {
            clickCheckBoxElectronicPaymentConsent();
            clickCheckBoxThirdPartyProvisionConsent();
        }
    }

    public void clickCheckBoxThirdPartyProvisionConsent() {
        thirdPartyProvisionConsentOptInCheckBox.click();
    }

    public void clickCheckBoxElectronicPaymentConsent() {
        electronicPaymentConsentOptInCheckBox.click();
    }

    public void clickCreditSubmitBtnByIdIfPresent() {
        if (creditSubmitBtnId.isPresent(ONE_SEC_TIMEOUT)) {
            creditSubmitBtnId.clickByJs();
        }
    }

    public void clickSubscriberAgreementContinueCta(String locale) {
        boolean isSubscriberAgreementCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_SUBSCRIBER_AGREEMENT_COUNTRY);
        if (isSubscriberAgreementCountry) {
            clickDplusSubscriberAgreementContinue();
        }
    }

    public void clickSubscriberAgreementContinueCtaOnUpgrade(String locale) {
        DisneyProductData productData = new DisneyProductData();
        boolean hasSubscriberAgreementForBundleUpgrade = productData.searchAndReturnProductData("hasSubscriberAgreementForBundleUpgrade").equalsIgnoreCase("true");
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_COMBO_COUNTRY);

        if (hasSubscriberAgreementForBundleUpgrade && isComboCountry) {
            clickSubscriberAgreementContinueCta(locale);
        }
    }

    public void clickSubscriberAgreementContinueCtaIfPresent(String locale) {
        boolean isSubscriberAgreementCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_SUBSCRIBER_AGREEMENT_COUNTRY);
        if (isSubscriberAgreementCountry) {
            clickDplusSubscriberAgreementContinueIfPresent();
        }
    }

    public void clickBtnReviewSubscriptionCta(String locale, String paymentType) {
        boolean isReviewSubscriptionCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_REVIEW_SUBSCRIPTION_COUNTRY);
        if (isReviewSubscriptionCountry) {
            LOGGER.info("Review subscription country");
            if (paymentType.equalsIgnoreCase("paypal")) {
                clickPaypalReviewSubscriptionCta();
            } else if (paymentType.equalsIgnoreCase("mercado")) {
                getDplusReviewSubscriptionCreditCardCta().clickIfPresent(ONE_SEC_TIMEOUT);
            } else {
                clickDplusReviewSubscriptionCreditCardCta();
            }
        } else {
            LOGGER.info("Review subscription not present");
        }
    }

    public void checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(SoftAssert sa, String locale, boolean selectNotNow, boolean shouldNotClickStartStreaming, boolean isMobile) {
        if (isMobile) {
            isGetAppPagePresent();
            assertUrlContains(sa, GET_APP_URL);
            adBlurbAssert(sa, locale);
        } else {
            if (checkMatureContentOnboardingCountries(locale)) {
                handleMatureContentOnboardingFlow(selectNotNow);
                clickMatureContentOnboardingBannerButton(locale);
            } else if (!shouldNotClickStartStreaming) {
                clickStartStreamingBtnKey();
            }
        }
    }

    public void handleMatureContentOnboardingFlow(boolean selectNotNow) {
        LOGGER.info("Entering Content Onboarding country");
        if ("DIS".equalsIgnoreCase(DisneyGlobalUtils.getProject()) || IS_ANA01) {
            clickDecisionIntroButton();
            if (!selectNotNow) {
                clickMaturityRatingContinueBtn();
                analyticPause();
                // TODO: Remove once second-time password prompt on content-onboarding page bug is fixed (WEB-1588)
                clickPasswordCancelLoginBtn();
                clickCancelButton();
                clickCreatePinNotNowButton();
            } else {
                if (cancelButton.isPresent()) {
                    LOGGER.info("Not setting up star");
                    clickCancelButton();
                }
            }
            pause(HALF_TIMEOUT); // Added to see what happens after since test closes to quick
        }
    }

    public void clickCompleteAndRestartPurchaseBaseCta(String locale, String type, boolean isBundle) {
        LOGGER.info("Attempting to Click Complete Purchase or Restart Purchase CTA");
        pause(HALF_TIMEOUT);
        boolean isBundleCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_BUNDLE_COUNTRY);
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_COMBO_COUNTRY);
        boolean isMegaBundleCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_MEGABUNDLE_COUNTRY);
        boolean isExcludedForcedSACountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_EXCLUDED_FORCED_SA_COUNTRY);

        if (isBundleCountry) {
            if (isBundle) {
                clickCompletePurchaseSuperBundleBuyNowBtn();
            } else {
                clickCompleteAndRestartPurchaseSignUpNowBtn(locale);
            }
        } else if (isComboCountry && !isMegaBundleTest()) {
            if (isBundle) {
            clickCompletePurchaseComboBuyNowBtn();
        } else {
            clickCompleteAndRestartPurchaseSignUpNowBtn(locale);
            }
        } else if (isMegaBundleCountry){
            clickCompletePurchaseMegaBuyNowBtn();
        } else if (type.equals("restart-subscription")) {
            clickRestartSubscriptionCta();
        } else {
            clickCompletePurchaseCta();
        }
        if (!isExcludedForcedSACountry) {
            clickSubscriberAgreementContinueCta(locale);
        }
    }

    public void handleSubmitOnPaypal() {
        clickPaypalAcceptAllCookiesBtn();
        LOGGER.info("Waiting 10 seconds to click PayPal submit");

        pause(DELAY);

        LOGGER.info("Attempting to Click PayPal Submit");
        if (paypalSubmitPayBtnById.isElementPresent(SHORT_TIMEOUT)) {
            LOGGER.info("1st PayPal submit present. clicking....");

            clickPaypalSubmitPayBtnById();
        } else if (paypalPurchaseContinueBtn.isElementPresent(SHORT_TIMEOUT)) {
            LOGGER.info("2nd PayPal submit present. clicking....");

            clickPayPalPurchaseContinueBtn();
        } else {
            LOGGER.info("3rd PayPal submit clicking....");

            clickPayPalPurchaseContinueBtnById();
            clickPayPalPurchaseContinueBtn();
        }

        pause(ONE_SEC_TIMEOUT);
    }

    public void clickContinueDeleteAccountCta() {
        LOGGER.info("Click continue delete account cta");
        continueDeleteAccountCta.click();
    }

    public void clickKlarnaSubmitBtnById() {
        klarnaSubmitBtnId.click();
    }

    public void clickKlarnaSubmitBtnIfPresent() {
        klarnaSubmitBtnId.clickIfPresent(ONE_SEC_TIMEOUT);
    }

    public void clickModalPrimaryBtn() {
        getModalPrimaryBtn().clickByJs();
    }

    public void clickIdealSubmitBtnById() {
        idealSubmitBtnId.click();
    }

    public void clickPaymentBlockedButton() {
        paymentBlockedBtnId.click();
    }

    public void clickKlarnaRadioIconId() {
        klarnaRadioIconId.click();
    }

    public void clickLicensePlateSubmit() {
        licensePlateSubmit.click();
    }

    public void clickEnterPasscodeSubmitBt() {
        getEnterPasscodeSubmitButton().click();
    }

    public void clickCancelBtn() {
        getCancelBtn().click();
    }

    public void clickDeleteAccountConfirmBtn() {
        LOGGER.info("Click delete account confirm button");
        getDeleteAccountConfirmBtn().click();
    }

    public void clickDeleteAccountSuccessBtn() {
        LOGGER.info("Click delete account success button");
        getDeleteAccountSuccessBtn().click();
    }

    public void clickModalSecondaryBtn() {
        modalSecondaryBtn.click();
    }

    public void clickModalCloseBtn() {
        modalCloseBtn.click();
    }

    public void clickDocomoContinueButton() {
        docomoContinueButton.click();
    }

    //Retrieve Activation Token from Bamtech Activation

    public String getJwtActivationToken(String url, String email, String provider) {

        url = String.format(url, provider, DisneyApiCommon.getUniqueEmail(), PARTNER);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JsonNode> guidUrl = restTemplate.getForEntity(url, JsonNode.class);
        String token = guidUrl.getBody().findPath("activationJwt").asText();

        LOGGER.info("Token: {}. Email: {}", token, email);

        return token;
    }

    //Retrieve Url with Token for Activation

    public String getActivationUrl(String url, String token, String provider) {
        String activationUrl = String.format(url, getHomeUrl(), token, provider);

        LOGGER.info("Activation Url: " + activationUrl);

        return activationUrl;
    }

    public String getDeeplinkUrl(String locale) {
        DisneyOffer offer = accountApi.lookupOfferToUse(locale, "Monthly");
        String deeplinkUrl = String.format(DisneyWebParameters.DISNEY_WEB_DEEPLINK_URL.getValue(), offer.getPromotionGroup(), offer.getPromotionCode(), offer.getSku());
        LOGGER.info("Deeplink Url {}", deeplinkUrl);
        return deeplinkUrl;
    }

    //Retrieve PreOrder Url

    public String getPreOrderUrl(String locale) {
        String url = "https://qa-web-preorder.disneyplus.com/billing/DISNEY_EU_PRESALE_2020_CMPGN/";

        switch (locale.toUpperCase()) {
            case "DK": url = url.concat("DISNEY_DK_PRESALE_2020_VOCHR/c0040ace7e5d055d9f6f1360858004f0_disney"); break;
            case "FI": url = url.concat("DISNEY_FI_PRESALE_2020_VOCHR/8c080b0c4b19e657c2c32b03aab653c5_disney"); break;
            case "NO": url = url.concat("DISNEY_NO_PRESALE_2020_VOCHR/213f7b309efb06927af47572df3009a3_disney"); break;
            case "SE": url = url.concat("DISNEY_SE_PRESALE_2020_VOCHR/6f1d5d0608b2d3a0f34a9c958832822a_disney"); break;
            case "BE": url = url.concat("DISNEY_BE_PRESALE_2020_VOCHR/3760383234e14146780523984b24be91_disney"); break;
            case "PT": url = url.concat("DISNEY_PT_PRESALE_2020_VOCHR/fbad7415647f9badb5b2ce5cfeacb94b_disney"); break;
            default: url = ""; Assert.fail(String.format("No valid pre-order region selected, region: %s", locale)); break;
        }

        return url;
    }

    //Retrieve LATAM Url

    public String getLatamUrl(String locale) {
        String latamUrl = DisneyWebParameters.DISNEY_QA_WEB_LATAM_URL.getValue();
        String presaleYear = "DISNEY_LATAM_PRESALE_2020_CMPGN";
        String voucherYear = "DISNEY_BR_PRESALE_2020_VOCHR";
        String regionVoucher = "disney_plus_yearly_%s_web_c7097ea";

        switch (locale.toUpperCase()) {
            case "BR":
                latamUrl = String.format(latamUrl, presaleYear, voucherYear, String.format(regionVoucher, locale.toLowerCase()), "disney_plus_yearly_br_web_c7097ea");
                break;
            default:
                latamUrl = "";
                Assert.fail(String.format("No valid latam region selected, region: %s", locale));
                break;
        }
        LOGGER.info(String.format("LATAM URL: %s", latamUrl));
        return latamUrl;
    }

    public String getBlockedCreditCardNumber(String locale) {
        DisneyCountryData countryData = new DisneyCountryData();
        String creditCard;
        String creditCardType = ENVIRONMENT.equalsIgnoreCase("QA") ? "cc" : "prod-cc";

        switch(locale.toLowerCase()) {
            case "us": case "pr": case "vi":
                creditCard = countryData.searchAndReturnCountryData("CA", "code", creditCardType); break;
            case "ca": case "au": case "nz":
                creditCard = MASTERCARD; break;
            case "fr": case "de": case "nl":
                creditCard = countryData.searchAndReturnCountryData("PR", "code", creditCardType); break;
            default:
                LOGGER.info(String.format("No Compatible Locale: %s, defaulting to US Mastercard", locale)); creditCard = MASTERCARD; break;
        }
        LOGGER.info(String.format("Credit Card returned for %s region: %s", locale, creditCard));
        return creditCard;

    }

    public String getInvalidCreditCard(String env, String locale, boolean isFreeTrialFraud) {

        if (!locale.equals("US")) {
            locale = "US";
        } else if (locale.equals("MX")){
            locale = "US";
        } else {
            locale = "MX";
        }

        return getCreditCardNumber(env, locale, isFreeTrialFraud);
    }

    // Handles PayPal Transaction Windows
    public boolean paypalTransactionHandler(String locale) {
        dCountry.set(new DisneyCountryData());
        LOGGER.info("Waiting 10 seconds for PayPal to load");
        pause(DELAY);
        util.switchWindow();
        LOGGER.info("Inserting PayPal Info");
        if (!paypalEmailFldById.isElementPresent(DELAY)) {
            return true;
        }

        paypalEmailFldById.click();
        paypalEmailFldById.type(dCountry.get().searchAndReturnCountryData(locale, "code", "paypalEmail"));
        paypalNextBtnById.click();
        paypalPasswordFldById.isElementPresent();
        paypalPasswordFldById.type(dCountry.get().searchAndReturnCountryData(locale, "code", "paypalPass"));
        paypalLoginBtnById.sendKeys(Keys.ENTER);

        return false;
    }

    public boolean handleFullPayPal(String locale, BrowserMobProxy proxy) {
        DisneyProductData productData = new DisneyProductData();
        boolean shouldSkip = false;
        boolean isTrunkQA = BRANCH.equalsIgnoreCase("TRUNK") && ENVIRONMENT.equalsIgnoreCase("QA");
        boolean isTrunkNotQA = BRANCH.equalsIgnoreCase("TRUNK") && !ENVIRONMENT.equalsIgnoreCase("QA");
        boolean usePaypalMock = productData.searchAndReturnProductData("usePaypalMock").equalsIgnoreCase("true") && !isTrunkQA;
        boolean isPayPalBlocked = !getLocale().equals(locale) && !ENVIRONMENT.equalsIgnoreCase("QA") && !isTrunkQA;
        boolean isLive = BRANCH.equalsIgnoreCase("LIVE");
        boolean isTaxIdCountry = disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_TAXID_COUNTRY);
        boolean isPaypalQACountry = disneyGlobalUtils.getBooleanFromCountries(locale, DisneyPlusBasePage.IS_PAYPAL_QA_COUNTRY);
        boolean isQA = ENVIRONMENT.equalsIgnoreCase("QA");

        if (isLive || isTrunkNotQA) {
            shouldSkip = true;
        } else if (!isPaypalQACountry && !isQA && (usePaypalMock || isPayPalBlocked)) {
            isPayPalSubmitBtnByIdPresent();
            pause(HALF_TIMEOUT); // Need time for PayPal to load
            paypalSubmitBtnById.scrollTo();
            pause(HALF_TIMEOUT); // Need time for PayPal to load
            js = (JavascriptExecutor) getDriver();
            if (isTaxIdCountry) {
                js.executeScript("window.paypal.onClick()");
                pause(SHORT_TIMEOUT); // Need time in between calls
            }
            js.executeScript("window.paypal.createOrder()");
            pause(HALF_TIMEOUT); // Need time in between calls
            js.executeScript("window.paypal.onApprove()");
        } else {
            removeTestHeader(proxy);
            clickPayPalSubmitBtnById();
            shouldSkip = paypalTransactionHandler(locale);
            if (shouldSkip) {
                return shouldSkip;
            }
            handleSubmitOnPaypal();
            switchWindow();
        }

        return shouldSkip;
    }

    public void handleTaxIdBillingFields(String locale, String zipOrTaxId) {
        boolean isTaxIdCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_TAXID_COUNTRY);
        if (isTaxIdCountry) {
            billingTaxIdFieldId.clickByJs();
            billingTaxIdFieldId.type(zipOrTaxId);
            pause(HALF_TIMEOUT);
            billingTaxIdFieldId.sendKeys(Keys.TAB);
        }
    }

    //Handles Blocked Paypal Country Logic for paypalTransactionHandler
    public String getBlockedPaypalCreditAccount(String locale) {

        String country;
        switch(locale.toLowerCase()) {
            case "us": case "pr": case "vi":
                country = "CA"; break;
            case "ca": case "au": case "nz": default:
                country = "US"; break;
        }
        return country;
    }


    //Handle WorldPay Transaction

    public void handleWorldPayTransaction(String paymentType, boolean isFail) {
        LOGGER.info("Attempting to Run Ideal/Klarna Payment");
        if (paymentType.equals("ideal") && !isFail) {
            selectAndSubmitIdealBank();
            clickWorldPayBtnById();
        } else if (paymentType.equals("ideal") && isFail) {
            selectAndSubmitIdealBank();
            clickWorldPayRejectBtnById();
        } else if (paymentType.equals("klarna") && !isFail) {
            clickKlarnaSubmitBtnById();
            clickCreditSubmitBtnIfPresent();
            clickKlarnaSubmitBtnIfPresent();
            clickWordPayOutcomeAuthorised();
            clickWorldPayContinueBtnById();
        } else if (paymentType.equals("klarna") && isFail) {
            clickKlarnaSubmitBtnById();
            clickCreditSubmitBtnIfPresent();
            clickKlarnaSubmitBtnIfPresent();
            clickWordPayOutcomeDropDownList();
            clickWordPayOutcomeRefused();
            clickWorldPayContinueBtnById();
        }
    }

    //Go To Complete Purchase or Restart Subscription
    public void goToCompleteOrRestartPurchase(SoftAssert sa, String expectedUrl) {
        isCreditSubmitBtnIdPresent();
        getDriver().navigate().back();
        if (modalPrimaryBtn.isElementNotPresent(2)) {
            getDriver().navigate().back();
        }
        clickModalPrimaryBtn();
        isMarketingLogOutBtnPresent();
        assertUrlContains(sa, expectedUrl);
    }

    //Flexible SoftAssertions for regions
    public void adBlurbAssert(SoftAssert sa, String locale) {
        boolean isStarzCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_STARZ_COUNTRY);
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);
        if (isStarzCountry && !IS_STAR && !isComboCountry) {
            sa.assertTrue(isAdBlurbPresent(), "Ad blurb not Present");
        } else {
            LOGGER.info(String.format("Country (%s) does not contain ad blurb promotion, skipping assertion...", locale));
        }
    }

    //Generates license plate code and auto-fills input field
    public void generateAndEnterLicensePlateInputField(DisneyAccount account) throws JSONException, IOException, URISyntaxException, InterruptedException {
        DisneyAccountApi accountApi = getAccountApi();
        String licensePlateCode = accountApi.retrieveLicensePlateCode(account);

        LOGGER.info(String.format("Submitting %s in license plate field", licensePlateCode));
        try {
            for (int x = 0; x <= 7; x++) {
                String licenseIndex = Integer.toString(x);
                getLicensePlateInputFormat(licenseIndex).type(Character.toString(licensePlateCode.charAt(x+1)));
            }
        } catch (NoSuchElementException e) {
            LOGGER.info(e.getMessage());
        }

    }

    public void generateAndEnterOtp(String email, ThreadLocal<VerifyEmail> verifyEmail, Date startTime) throws JSONException, IOException, URISyntaxException, InterruptedException {
        String otp = verifyEmail.get().getDisneyOTP(email, EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);

        LOGGER.info(String.format("Submitting %s in license plate field", otp));

        try {
            UniversalUtils util = new UniversalUtils();

            for (int x = 0; x <= 5; x++) {
                String otpIndex = Integer.toString(x);
                getOtpInputFormat(otpIndex).sendKeys(util.sendKeysNumpadConverter(Character.toString(otp.charAt(x))));
                pause(ONE_SEC_TIMEOUT);
            }
        } catch (NoSuchElementException e) {
            LOGGER.info(e.getMessage());
        }
    }

    public void override3DS2Data(BrowserMobProxy proxy, String locale) throws IOException {
        boolean isEuCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_EU_COUNTRY);
        if (isEuCountry && getEnvironmentType(ENVIRONMENT).equals("BETA") || getEnvironmentType(ENVIRONMENT).equals("PROD")) {

            String worldpayDdc = R.TESTDATA.get("centiline_api_v1_collect_url");
            FileReader ddc = new FileReader(new FileUtils().getResourceFilePath("disney/web/mocks/3ds2/ddc.html"));
            String ddcFileText = "";
            try (BufferedReader ddcBufferedReader = new BufferedReader(ddc)) {
                for (String line1 = ddcBufferedReader.readLine(); line1!=null; line1 = ddcBufferedReader.readLine()) {
                    ddcFileText = String.format("%1$s %2$s", ddcFileText, line1);
                }
            } catch (IOException e) {
                throw new IOException(e);
            }

            Document ddcDocument = Jsoup.parse(ddcFileText);

            ddc.close();

            new ProxyUtils(proxy).rewriteHttpResponseBody(worldpayDdc, ddcDocument.toString());

            String worldpayChallenge = R.TESTDATA.get("centiline_api_v2_stepup_url");
            FileReader challenge = new FileReader(new FileUtils().getResourceFilePath("disney/web/mocks/3ds2/challenge.html"));
            String challengeFileText = "";
            try (BufferedReader challengeBufferedReader = new BufferedReader(challenge)) {
                for (String line2 = challengeBufferedReader.readLine(); line2!=null; line2 = challengeBufferedReader.readLine()) {
                    challengeFileText = String.format("%1$s %2$s", challengeFileText, line2);
                }
            } catch (IOException e) {
                throw new IOException(e);
            }

            Document challengeDocument = Jsoup.parse(challengeFileText);

            challenge.close();

            new ProxyUtils(proxy).rewriteHttpResponseBody(worldpayChallenge, challengeDocument.toString());
        }
    }

    //Handle EA URL + Sign up cta click
    public boolean handleEAUrl() {
        setUiLoadedMarker(premierAccessCta);
        if (isCruellaOnProd) {
            openURL(getHomeUrl().concat(CRUELLA_EA_URL));
        } else if (isBlackWidowOnProd) {
            openURL(getHomeUrl().concat(BLACK_WIDOW_EA_URL));
        } else if (isJungleCruiseOnProd) {
            openURL(getHomeUrl().concat(JUNGLE_CRUISE_EA_URL));
        } else {
            // Only happens on QA
            openURL(getHomeUrl().concat(QA_EA_URL));
        }
        return clickPremierAccessCta();
    }

    //Get EA SKUs
    public DisneyOffer getEaSkus(String locale) {
        if (isCruellaOnProd) {
            return accountApi.lookupOfferToUse(locale, String.format("Cruella - %s - Web", locale.toUpperCase()));
        } else if (isBlackWidowOnProd) {
            return accountApi.lookupOfferToUse(locale, String.format("Black Widow - %s - Web", locale.toUpperCase()));
        } else if (isJungleCruiseOnProd) {
            return accountApi.lookupOfferToUse(locale, String.format("Jungle Cruise - %s - Web", locale.toUpperCase()));
        } else {
            // Only happens on QA
            return accountApi.fetchOffer(DisneySkuParameters.DISNEY_D2C_EA_TOY_STORY);
        }
    }

    public boolean isEaPurchaseSuccessConfirmBtn() {
        if (!DISABLE_TANDEM_FLOW) {
            return purchaseSuccessConfirmBtn.getAttribute("kind").equals("earlyAccess");
        } else {
            return isPurchaseSuccessNewUser();
        }
    }

    public boolean getBundleCardTest() {
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);
        boolean bundleCardTest;

        if (isComboCountry) {
            bundleCardTest = isComboPlusSubcopyPresent();
        } else {
            bundleCardTest = isBundleUpgradeCardPresent();
        }

        return bundleCardTest;
    }

    public boolean getBundleOfferCardTest() {
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);
        boolean bundleOfferCardTest;

        if (isComboCountry) {
            bundleOfferCardTest = isComboPlusSubcopyCtaPresent();
        } else {
            bundleOfferCardTest = isBundleOfferCardPresent();
        }

        return bundleOfferCardTest;
    }

    public void selectAndSubmitIdealBank() {
        clickIdealBankSelectorId();
        if (idealReactIndex0.isElementPresent(HALF_TIMEOUT)) {
            clickIdealBankIndex0ById();
        } else {
            clickIdealReactMobileIndex0ById();
        }
        pause(SHORT_TIMEOUT);
        clickIdealSubmitBtnById();
    }

    public void checkMyServices(SoftAssert sa, DisneyPlusAccountPage accountPage) {
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);
        if (IS_DISNEY) {
            if (!isComboCountry) {
                sa.assertTrue(accountPage.isMyServicesEspnPlusPresent(), "ESPN not present");
                sa.assertTrue(accountPage.isMyServicesHuluPresent(), "Hulu not present");
            } else {
                sa.assertTrue(accountPage.isMyServicesStarPresent(), "Star not present");
            }
        } else {
            sa.assertTrue(accountPage.isMyServicesDisneyPresent(), "Disney not present");
        }
    }

    public void reloadPageForLocationOverride(String locale) {
        if (isGeoedgeUnsupportedRegion(locale)) {
            getDriver().navigate().back();
            clickModalPrimaryBtn();
            getDriver().navigate().refresh();
        }
    }

    public void clickBundleSuccessContinueButton() {
        bundleSuccessContinueButton.click();
    }

}
