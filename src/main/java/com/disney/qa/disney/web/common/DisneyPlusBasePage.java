package com.disney.qa.disney.web.common;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.disney.DisneyHttpHeaders;
import com.disney.qa.api.disney.DisneyPlusOverrideKeys;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneySubscriptions;
import com.disney.qa.api.utils.DisneyContentApiChecker;
import com.disney.qa.carina.GeoedgeProxyServer;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.DisneyCountryData;
import com.disney.qa.disney.DisneyProductData;
import com.disney.qa.disney.web.DisneyWebKeys;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.star.StarPlusParameters;
import com.disney.util.disney.DisneyGlobalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.carina.browsermobproxy.ProxyPool;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.common.CommonUtils;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.registrar.CurrentTest;
import exceptions.DisneyAccountNotFoundException;
import net.lightbody.bmp.BrowserMobProxy;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

import static com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase.getDictionary;

public class DisneyPlusBasePage extends DisneyAbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusBasePage(WebDriver driver) {
        super(driver);
        util = new SeleniumUtils(driver);
    }

    protected SeleniumUtils util;
    protected DisneyGlobalUtils disneyGlobalUtils = new DisneyGlobalUtils();
    public static final boolean IS_STAR = "STA".equalsIgnoreCase(DisneyGlobalUtils.getProject());
    public static final boolean IS_DISNEY = "DIS".equalsIgnoreCase(DisneyGlobalUtils.getProject());
    public static final boolean IS_DGI = "DGI".equalsIgnoreCase(DisneyGlobalUtils.getProject());
    public static final boolean IS_ANA01 = "ANA01".equalsIgnoreCase(DisneyGlobalUtils.getProject());


    protected JavascriptExecutor js;
    protected DisneyContentApiChecker apiChecker = new DisneyContentApiChecker(WEB_PLATFORM, ENVIRONMENT, PARTNER);
    protected DisneyAccountApi accountApi = getAccountApi();
    private DisneyCountryData countryData = new DisneyCountryData();

    protected static final ThreadLocal<DisneyApiProvider> disneyApiProvider = new ThreadLocal<>();

    public static final String LOCALE_TEXT = "locale";
    public static final String ENVIRONMENT = R.CONFIG.get("env");
    public static final String BRANCH = R.CONFIG.get("app_version");
    public static final String LOCALE = R.CONFIG.get(LOCALE_TEXT);
    public static final String WEB_PLATFORM = "browser";
    public static final String PREVIEW = "preview";
    public static final String PARTNER = getProjectApiName();
    public static final String SUB_VERSION_V2_ORDER = "V2-ORDER";
    public static final String BROWSER = R.CONFIG.get(WEB_PLATFORM);

    public static final boolean DPLUS_SPECIAL_OFFER_ENABLED = IS_DISNEY && Boolean.parseBoolean(DisneyWebParameters.DISNEY_WEB_SPECIAL_OFFER_ACTIVATED.getValue());
    public static final String DISNEY_STAGING_VALUE = "true";
    public static final String DISNEY_WEAPONX_DISABLE = "/?disable-weaponx=true";
    public static final String BAMTECH_CANONBALL_PREVIEW_VALUE = "3Br5QesdzePvQEH";
    public static final String BAMTECH_CDN_BYPASS_VALUE = "21ea40fe-bdb5-4426-b134-66f98acb2b68";
    public static final String IS_BUNDLE_COUNTRY = "isBundleCountry";
    public static final String IS_COMBO_COUNTRY = "isComboCountry";
    public static final String IS_GEOEDGE_UNSUPPORTED_REGION = "isGeoEdgeUnsupportedRegion";
    public static final String IS_GEOEDGE_SUPPORTED_REGION_WITH_ISSUES = "isGeoEdgeSupportedRegionWithIssues";
    public static final String IS_ONEID_TRUSTED_REGION = "isOneIdTrustedRegion";
    public static final String IS_MATURE_CONTENT_ONBOARDING_COUNTRY = "isMatureContentOnboardingCountry";
    public static final String IS_EMAIL_CHECKBOX_OPTIN_COUNTRY = "isEmailCheckBoxOptInCountry";
    public static final String IS_BILLING_CHECKBOX_OPTIN_COUNTRY = "isBillingCheckBoxOptInCountry";
    public static final String IS_BILLING_DOB_COUNTRY = "isBillingDOBCountry";
    public static final String IS_TAXID_COUNTRY = "isTaxIdCountry";
    public static final String IS_PAYPAL_QA_COUNTRY = "isPaypalQACountry";
    public static final String IS_MEGABUNDLE_COUNTRY = "isMegaBundleCountry";
    public static final String IS_EXCLUDED_FORCED_SA_COUNTRY = "isExcludedForcedSACountry";
    private boolean isMegaBundleTest = false;
    private boolean isIgnoreCookie = false;

    @FindBy(xpath = "(//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s')])[1]")
    private ExtendedWebElement dPlusIgnoreCaseTextElement;

    @FindBy(xpath = "//%s[%s='%s']")
    private ExtendedWebElement dPlusGenericTempElement;

    @FindBy(xpath = "//%s[contains(%s,'%s')]")
    private ExtendedWebElement dPlusGenericContainsElement;

    @FindBy(xpath = "//*[contains(text(),'%s')]")
    private ExtendedWebElement dPlusGenericAllContainsTextElement;

    @FindBy(xpath = "(//*[contains(text(),'%s')])[1]")
    private ExtendedWebElement dPlusGenericAllContainsTextElementFirstIndex;

    @FindBy(xpath = "//*[text()='%s']")
    private ExtendedWebElement dPlusGenericAllTextEqualsElement;

    @FindBy(xpath = "//*[@role='alert']")
    private ExtendedWebElement dPlusAlertElement;

    //Homepage Elements

    @FindBy (xpath = "//h1[contains(text(),'502 Bad Gateway')]")
    private ExtendedWebElement badGatewayHeader;

    @FindBy(id = "cta-target")
    private ExtendedWebElement dPlusCtaTargetId;

    @FindBy(xpath = "//*[@data-testid='standalone_cta_hero']")
    private ExtendedWebElement dPlusBaseSignupStandaloneHeroId;

    @FindBy(xpath = "//*[@data-testid='2p_bundle_cta']")
    private ExtendedWebElement dPlusBaseSignUp2pBundleCta;

    @FindBy(xpath = "//*[@data-testid='special_offers_hero']")
    private ExtendedWebElement dPlusBaseSignupSpecialOfferHeroCta;

    @FindBy(xpath = "//*[@data-testid='special_offers_weaponXElement']")
    private ExtendedWebElement dPlusBaseSignupSpecialOfferXCta;

    @FindBy(xpath = "//*[@data-testid='bundle_cta_hero']")
    private ExtendedWebElement dPlusBaseBundleHeroCta;

    @FindBy(xpath = "//*[@data-testid='sash_cta_section']")
    private ExtendedWebElement dPlusSASHBundleHeroCta;

    @FindBy(xpath = "//*[@data-testid='noah_cta' and contains(@data-analytics-name, 'Desktop')]")
    private ExtendedWebElement dPlusBaseBundleCta;

    @FindBy(xpath = "//*[contains(@data-testid, 'view_all_plans')]")
    private ExtendedWebElement landingPageHeroAnchorLink;

    @FindBy(xpath = "//*[@id='plan-selector']")
    private ExtendedWebElement mlpPlanSelectorSection;

    @FindBy(xpath = "//*[@id='tab-DisneyBundle']")
    private ExtendedWebElement tabDisneyBundlePlanCards;


    @FindBy(xpath = "//*[@data-analytics-name='Sign Up Now']")
    private ExtendedWebElement dPlusBaseSecondaryCtaEuBtn;

    @FindBy(xpath = "//*[contains(@data-analytics-name, 'Save')]")
    private ExtendedWebElement dPlusBaseAnnualCtaEuBtn;

    @FindBy(xpath = "//*[@data-testid='flash-notification-container']")
    private ExtendedWebElement dPlusBaseFlashNotificationContainer;

    @FindBy(xpath = "//*[contains(@data-testid,'welch-onboarding-banner-button')]")
    private ExtendedWebElement contentOnboardingBannerButton;

    @FindBy(xpath = "//*[@data-testid='welch-onboarding-banner-closeIcon']")
    private ExtendedWebElement matureContentOnboardingBannerCloseButton;

    @FindBy(xpath = "//*[@data-testid='mlp_link']")
    private ExtendedWebElement newDplusStandaloneCta;

    @FindBy(xpath = "//*[@data-testid='bundle_cta_hero']")
    private ExtendedWebElement mlpComboCTAHero;

    @FindBy(xpath = "//*[@data-testid='megabundle_cta_hero']")
    private ExtendedWebElement mlpMegaBundleCTAHero;

    @FindBy(xpath = "//*[@data-testid='standalone_cta_monthly']")
    private ExtendedWebElement mlpStandaloneCTA;

    @FindBy(xpath = "//*[@data-testid='bundle_cta']")
    private ExtendedWebElement mlpComboCTA;

    @FindBy(xpath = "//*[@data-testid='megabundle_cta']")
    private ExtendedWebElement mlpMegaBundleCTA;

    @FindBy(xpath = "//*[@data-testid='standalone_cta_monthly_hero']")
    private ExtendedWebElement mlpStandaloneCTAMonthlyHeroLink;

    @FindBy(xpath = "//*[@data-testid='standalone_cta_annual_hero']")
    private ExtendedWebElement mlpStandaloneCTAAnnualHeroLink;

    @FindBy(xpath = "//*[@data-testid='bundle_cta_weaponXElement']")
    private ExtendedWebElement dPlusBaseBundleSignupSpecialOfferHeroCta;

    //Login Flow Elements

    @FindBy(xpath = "//section[contains(@class,'hero')]/div/ul/li/picture")
    private ExtendedWebElement dPlusBaseLogoId;

    @FindBy(id = "logo")
    private ExtendedWebElement dPlusBaseMainAppLogoId;

    @FindBy(xpath = "//section[contains(@class,'hero')]/div/picture")
    private ExtendedWebElement sPlusBaseLogoId;

    @FindBy(id = "onetrust-accept-btn-handler")
    private ExtendedWebElement dPlusBaseAcceptCookiesBtn;

    @FindBy(id = "consent-accept")
    private ExtendedWebElement dPlusBaseAcceptCookiesConsentBtn;

    @FindBy(xpath = "//*[@data-testid='mlp_link_header']")
    private ExtendedWebElement dPlusBasePreviewEuLoginId;

    @FindBy(xpath = "//*[@data-testid='log_in_header']")
    private ExtendedWebElement dPlusBaseLoginBtn;

    @FindBy(xpath = "//button[contains(text(),'START FREE TRIAL')]")
    private ExtendedWebElement dPlusBaseTrialBtn;

    @FindBy(id = "email")
    private ExtendedWebElement dPlusBaseEmailFieldId;

    @FindBy(xpath = "//button[contains(text(),'CONTINUE')]")
    private ExtendedWebElement dPlusBaseContinueBtn;

    @FindBy(id = "password")
    private ExtendedWebElement dPlusBasePasswordFieldId;

    @FindBy(xpath = "//*[@data-testid='login-password-forgot-password']")
    private ExtendedWebElement dPlusBaseForgotPasswordId;

    @FindBy(name = "dssLoginSubmit")
    private ExtendedWebElement dPlusBaseLoginFlowSubmit;

    @FindBy(xpath = "//*[@data-testid='password-continue-login']")
    private ExtendedWebElement dPlusBasePasswordContinueLoginBtn;

    @FindBy(xpath = "//button[@data-test-id='marketing-page-logout']")
    private ExtendedWebElement dPlusBaseMarketingPageLogOutBtn;

    @FindBy(xpath = "//*[@value='submit']")
    private ExtendedWebElement dPlusBaseGenericSubmitValueBtn;

    @FindBy(xpath = "//*[@type='submit']")
    private ExtendedWebElement dPlusBaseGenericSubmitTypeBtn;

    @FindBy(xpath = "//*[@data-testid='subscriber-agreement-continue']")
    private ExtendedWebElement dPlusBaseSubscriberAgreementContinue;

    @FindBy(xpath = "//*[@data-testid='review-subscription-credit-submit-button']")
    private ExtendedWebElement dPlusBaseReviewSubscriptionCreditCardCta;

    @FindBy(xpath = "//*[contains(@data-testid,'continue-button')]")
    private ExtendedWebElement signUpLoginContinueBtn;

    @FindBy(xpath = "//*[@data-testid='modal-primary-button']")
    private ExtendedWebElement modalPrimaryBtn;

    @FindBy(xpath = "//*[@data-gv2-name='mlp_link']")
    private ExtendedWebElement mlpLink;

    //Footer Elements

    @FindBy(id = "footer")
    protected ExtendedWebElement dPlusBaseFooter;

    //Beta Login Elements

    @FindBy(id = "inputUser")
    private ExtendedWebElement dPlusBaseBetaEmailFld;

    @FindBy(id = "inputPassword")
    private ExtendedWebElement dPlusBaseBetaPasswordFld;

    @FindBy(xpath = "//select[@name='redirectUrl']")
    private ExtendedWebElement dPlusBaseEnvironmentList;

    @FindBy(xpath = "//button[@type='submit']")
    private ExtendedWebElement dPlusBaseSubmitBtn;

    public boolean isMegaBundleTest() {
        return isMegaBundleTest;
    }

    public void setMegaBundleTest(boolean isMegaBundleTest) {
        this.isMegaBundleTest = isMegaBundleTest;
    }

    public void setIgnoreCookie(boolean isIgnoreCookie) {
        if (IS_DISNEY) {
            this.isIgnoreCookie = isIgnoreCookie;
        }
    }

    //Click Methods

    //Clicks all base cta sign up elements without bundle
    public void clickDplusSignUpBaseBtn() {
        getDplusBaseSignupStandaloneHeroIdBtn().click();
    }

    public void clickMlpStandaloneCTA() {
        getMlpStandaloneCTA().clickByJs();
    }

    public void clickMlpComboCTA() {
        getMlpComboCTA().clickByJs();
    }

    public void clickMlpMegaBundleCTA() {
        getMlpMegaBundleCTA().clickByJs();
    }

    public void clickSpecialOfferBundleCTA() {
        dPlusBaseBundleSignupSpecialOfferHeroCta.clickIfPresent();
    }

    public void handleStandaloneCtasOnOlp() {
        if (DPLUS_SPECIAL_OFFER_ENABLED) {
            dPlusBaseSignupSpecialOfferHeroCta.clickIfPresent();
            dPlusBaseSignupStandaloneHeroId.clickIfPresent(1);
        } else {
            clickMlpStandaloneCTA();
            waitFor(signUpLoginContinueBtn,2);
        }
    }

    public void handleBundleCtaOnOlp() {
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);
        if (isComboCountry && !isMegaBundleTest()) {
            clickMlpComboCTA();
        } else if (isMegaBundleTest()) {
            clickMlpMegaBundleCTA();
        } else {
            clickDplusAnnualBndleBtn();
        }
    }

    //Clicks all annual/bundle sign up elements
    public void clickDplusAnnualBndleBtn() {
        boolean isBundleCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_BUNDLE_COUNTRY);
        boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_COMBO_COUNTRY);
        if (isBundleCountry || isComboCountry) {
            pause(2); //Needed to add the btn attributes
            if (isBundleCountry) {
                navigateTo(getHomeUrl().concat("/welcome/disney-hulu-espn-bundle"));
                dPlusSASHBundleHeroCta.clickByJs(2);
            } else {
                dPlusBaseBundleHeroCta.click(2);
            }
        } else {
            handleStandaloneCtasOnOlp();
        }
    }

    public void clickDplusBaseLoginBtn() {
        getDplusBaseLoginBtn().click();
        waitFor(signUpLoginContinueBtn, 2);
    }

    public void clickDplus2pBundleCtaBtn() {
        if (getdPlusBaseSignUp2pBundleCtaBtn().isClickable(10)) {
            getdPlusBaseSignUp2pBundleCtaBtn().scrollTo();
            getdPlusBaseSignUp2pBundleCtaBtn().click();
        }
    }

    public void clickDplusSubscriberAgreementContinue() {
        getDplusSubscriberAgreementContinue().clickByJs();
    }

    public void clickDplusSubscriberAgreementContinueIfPresent() {
        if (getDplusSubscriberAgreementContinue().isElementPresent(2)) {
            getDplusSubscriberAgreementContinue().clickByJs();
        }
    }

    public void clickDplusReviewSubscriptionCreditCardCta() {
        waitFor(dPlusBaseReviewSubscriptionCreditCardCta, 5);
        getDplusReviewSubscriptionCreditCardCta().click();
    }

    //Element can be used for both `continue` and `login` elements during flow
    public void clickDplusBaseLoginFlowBtn() {
        getDplusBaseLoginFlowBtn().clickByJs();
    }

    public void clickLoginBtn() {
        getPasswordContinueLoginBtn().click();
    }

    public void clickDplusBaseLogoId() {
        dPlusBaseMainAppLogoId.click();
    }

    public void clickPreviewEuLoginId() {
        dPlusBasePreviewEuLoginId.click();
    }

    public void clickDplusBasePreviewEuLoginId(boolean skipLoginClick) {
        if (isDplusBasePreviewEuLoginIdPresent()) {
            clickPreviewEuLoginId();
            waitForPageToFinishLoading();
        } else if (skipLoginClick) {
            openURL(getHomeUrl().concat("/login"));
        } else {
            clickDplusBaseLoginBtn();
            waitFor(signUpLoginContinueBtn); //Need this to ensure login page is fully loaded
        }
    }

    public void clickMatureContentOnboardingBannerButton(String locale) {
        if (checkMatureContentOnboardingCountries(locale)) {
            if (matureContentOnboardingBannerCloseButton.isElementPresent()) {
                matureContentOnboardingBannerCloseButton.sendKeys(Keys.ENTER);
            } else {
                if (contentOnboardingBannerButton.isPresent(5)) {
                    contentOnboardingBannerButton.clickByJs();
                }
            }
        }
    }

    public boolean checkMatureContentOnboardingCountries(String locale) {
        return !IS_STAR && disneyGlobalUtils.getBooleanFromCountries(locale, IS_MATURE_CONTENT_ONBOARDING_COUNTRY);
    }

    //Dictionary Getters

    public ExtendedWebElement getDictionaryContainsAllElement(JsonNode dictionary, String key) {
        return getdPlusGenericAllTextContainsElement(apiChecker.getDictionaryItemValue(dictionary, key));
    }

    /**
     * @param characterCount - Pass an integer to specify how many characters will be included for the start of an contains text() element. This
     *                       handles partial string matches that populate with a formatter when app is loaded.
     */

    public ExtendedWebElement getDictionaryContainsAllElementIndexGeneral(JsonNode dictionary, String key, int characterCount) {
        return getdPlusGenericAllTextContainsElement(apiChecker.getDictionaryItemValue(dictionary, key).substring(0, characterCount));
    }

    public ExtendedWebElement getDictionaryContainsIgnoreCaseElement(JsonNode dictionary, String key) {
        return getdPlusIgnoreCaseTextElement(apiChecker.getDictionaryItemValue(dictionary, key).toLowerCase());
    }

    public ExtendedWebElement getDictionaryElement(JsonNode dictionary, String key) {
        return getdPlusGenericAllTextEqualsElement(apiChecker.getDictionaryItemValue(dictionary, key));
    }

    public ExtendedWebElement getDictionaryElements(JsonNode dictionary, String key) {
        return getdPlusGenericAllTextEqualsElement(getDictionary().getDictionaryItemValue(dictionary, key));
    }

    public ExtendedWebElement getBtnEmphasizedCtaKey(JsonNode paywallDictionary, int characterCount) {
        return getDictionaryContainsAllElementIndexGeneral(paywallDictionary, DisneyWebKeys.BUNDLE_EMPHASIZED_CTA.getText(), characterCount);
    }

    //Click Dictionary

    public void waitForDictionaryAndClick(JsonNode dictionary, String dictionaryKey) {
        getdPlusGenericAllTextEqualsElement(apiChecker.getDictionaryItemValue(dictionary, dictionaryKey)).click();
    }

    public void waitForDictionaryAndClickIfPresent(JsonNode dictionary, String dictionaryKey) {
        getdPlusGenericAllTextEqualsElement(apiChecker.getDictionaryItemValue(dictionary, dictionaryKey)).clickIfPresent();
    }

    public void clickDplusTrialSignUpBtn(JsonNode paywallDictionary) {
        waitForDictionaryAndClick(paywallDictionary, DisneyWebKeys.WELCOME_SIGNUP_BTN.getText());
    }

    public void clickDplusContinueBtnKey(JsonNode dictionary, int poll, int timeout) {
        waitForElementPollingAndClick(getDictionaryContainsIgnoreCaseElement(dictionary, DisneyWebKeys.CONTINUE_BTN.getText()), poll, timeout);
    }

    public void clickBtnEmphasizedCtaKey(JsonNode paywallDictionary, int characterCount) {
        getBtnEmphasizedCtaKey(paywallDictionary, characterCount).click();
    }

    //Booleans

    public boolean isGeoedgeUnsupportedRegion(String countryCode) {
        boolean isGeoEdgeUnsupportedRegion = disneyGlobalUtils.getBooleanFromCountries(countryCode, IS_GEOEDGE_UNSUPPORTED_REGION);
        boolean isGeoEdgeSupportedRegionWithIssues = disneyGlobalUtils.getBooleanFromCountries(countryCode, IS_GEOEDGE_SUPPORTED_REGION_WITH_ISSUES);
        return isGeoEdgeSupportedRegionWithIssues || isGeoEdgeUnsupportedRegion;
    }

    public boolean isMarketingLogOutBtnPresent() {
        return dPlusBaseMarketingPageLogOutBtn.isElementPresent(5);
    }

    public boolean isDplusBasePreviewEuLoginIdPresent() {
        return dPlusBasePreviewEuLoginId.isElementPresent(5);
    }

    public boolean isCtaTargetIdPresent() {
        return dPlusCtaTargetId.isElementPresent();
    }

    public boolean isBtnEmphasizedCtaKeyPresent(JsonNode paywallDictionary, int characterCount) {
        return getBtnEmphasizedCtaKey(paywallDictionary, characterCount).isElementPresent();
    }

    public boolean isSuperBundlePresent() {
        return dPlusBaseBundleCta.isElementPresent();
    }

    public boolean isDplusAlertElementPresent() {
        return getdPlusAlertElement().isElementPresent();
    }

    public boolean isDplusBaseLogoIdPresent() {
        return getDplusBaseLogoId().isElementPresent();
    }

    public boolean isDplusBaseLoginBtnPresent(int timeout) {
        return getDplusBaseLoginBtn().isElementPresent(timeout);
    }

    public boolean isDplusBaseEmailFieldIdPresent() {
        return dPlusBaseEmailFieldId.isElementPresent();
    }

    public boolean isSignUpLoginContinueBtnPresent() {
        waitFor(signUpLoginContinueBtn);
        return getSignUpLoginContinueBtn().isElementPresent(2);
    }

    public boolean isMlpLinkBtnPresent() {
        waitFor(mlpLink);
        return mlpLink.isElementPresent();
    }

    public boolean isFooterPresent() {
        waitFor(dPlusBaseFooter);
        return dPlusBaseFooter.isElementPresent();
    }

    public boolean isDplusBaseFlashNotificationContainerPresent() {
        return getdPlusBaseFlashNotificationContainer().isElementPresent();
    }

    public boolean isSignupCtasPresent() {
        if (DPLUS_SPECIAL_OFFER_ENABLED) {
            return dPlusBaseSignupSpecialOfferHeroCta.isElementPresent();
        } else if (IS_STAR) {
            return verifySignUpCTAHero() && verifySignUpCTAHeroLinks() && verifySignUpCTAs();
        } else {
            return landingPageHeroAnchorLink.isElementPresent();
        }
    }

    public boolean verifySignUpCTAHero() {
        LOGGER.info("Verify signup Hero CTAs are present");
        return mlpComboCTAHero.isElementPresent() && mlpMegaBundleCTAHero.isElementPresent();
    }

    public boolean verifySignUpCTAHeroLinks() {
        LOGGER.info("Verify signup Hero Link CTAs are present");
        return mlpStandaloneCTAMonthlyHeroLink.isElementPresent() && mlpStandaloneCTAAnnualHeroLink.isElementPresent();
    }

    public boolean verifySignUpCTAs() {
        LOGGER.info("Verify signup 3-up CTAs are present");
        return mlpStandaloneCTA.isElementPresent() && mlpComboCTA.isElementPresent() && mlpMegaBundleCTA.isElementPresent();
    }

    public boolean isDplus2pBundleCtaPresent(int timeout) {
        return getdPlusBaseSignUp2pBundleCtaBtn().isElementPresent(timeout);
    }

    //ExtendedWebElement Getters

    public ExtendedWebElement getdPlusGenericTempElement(String a, String b, String c) {
        return dPlusGenericTempElement.format(a, b, c);
    }

    public ExtendedWebElement getdPlusIgnoreCaseTextElement(String a) {
        return dPlusIgnoreCaseTextElement.format(a);
    }

    public ExtendedWebElement getdPlusGenericContainsTempElement(String a, String b, String c) {
        return dPlusGenericContainsElement.format(a, b, c);
    }

    public ExtendedWebElement getdPlusGenericAllTextContainsElement(String text) {
        return dPlusGenericAllContainsTextElement.format(text);
    }

    public ExtendedWebElement getdPlusGenericAllTextEqualsElement(String text) {
        return dPlusGenericAllTextEqualsElement.format(text);
    }

    public ExtendedWebElement getdPlusAlertElement() {
        return dPlusAlertElement;
    }

    public ExtendedWebElement getDplusBaseLogoId() {
        return dPlusBaseMainAppLogoId;
    }

    public ExtendedWebElement getBaseLogo() {
        if (IS_STAR) {
            return sPlusBaseLogoId;
        } else {
            return dPlusBaseLogoId;
        }
    }

    public ExtendedWebElement getDplusSubscriberAgreementContinue() {
        return dPlusBaseSubscriberAgreementContinue;
    }

    public ExtendedWebElement getDplusReviewSubscriptionCreditCardCta() {
        return dPlusBaseReviewSubscriptionCreditCardCta;
    }

    public ExtendedWebElement getdPlusBasePreviewEuLoginId() {
        return dPlusBasePreviewEuLoginId;
    }

    public ExtendedWebElement getDplusBaseLoginFlowBtn() {
        return dPlusBaseLoginFlowSubmit;
    }

    public ExtendedWebElement getPasswordContinueLoginBtn() {
        return dPlusBasePasswordContinueLoginBtn;
    }

    public ExtendedWebElement getDplusBaseTrialBtn() {
        return dPlusBaseTrialBtn;
    }

    public ExtendedWebElement getDplusBaseGenericContinueTextBtn() {
        return dPlusBaseContinueBtn;
    }

    public ExtendedWebElement getDplusBaseGenericSubmitValueBtn() {
        return dPlusBaseGenericSubmitValueBtn;
    }

    public ExtendedWebElement getdPlusBaseGenericSubmitTypeBtn() {
        return dPlusBaseGenericSubmitTypeBtn;
    }

    public ExtendedWebElement getSignUpLoginContinueBtn() {
        return signUpLoginContinueBtn;
    }

    public ExtendedWebElement getdPlusBaseFlashNotificationContainer() {
        return dPlusBaseFlashNotificationContainer;
    }

    public DisneyPlusBasePage typeDplusBaseEmailFieldId(String email) {
        isDplusBaseEmailFieldIdPresent();
        analyticPause();
        dPlusBaseEmailFieldId.type(email);
        return this;
    }

    public ExtendedWebElement getDplusBasePasswordFieldId() {
        return dPlusBasePasswordFieldId;
    }

    public ExtendedWebElement getDplusBaseEmailFieldId() {
        return dPlusBaseEmailFieldId;
    }

    public ExtendedWebElement getdPlusBaseForgotPasswordId() {
        return dPlusBaseForgotPasswordId;
    }

    public ExtendedWebElement getDplusBaseLoginBtn() {
        return dPlusBaseLoginBtn;
    }

    public ExtendedWebElement getDplusBaseSignupStandaloneHeroIdBtn() {
        return dPlusBaseSignupStandaloneHeroId;
    }

    public ExtendedWebElement getdPlusBaseSignUp2pBundleCtaBtn() {
        return dPlusBaseSignUp2pBundleCta;
    }

    public String getDplusBasePasswordAtribute(String attribute) {
        return getDplusBasePasswordFieldId().getAttribute(attribute);
    }

    public void typeDplusBasePasswordFieldId(String password) {
        getDplusBasePasswordFieldId().isElementPresent();
        getDplusBasePasswordFieldId().type(password);
        pause(1);
    }

    public void clickSignUpLoginContinueBtn() {
        getSignUpLoginContinueBtn().click();
    }

    public void clickModalPrimaryBtn() {
        modalPrimaryBtn.click();
    }

    public void clickMlpLink() {
        mlpLink.click();
    }

    public void clickDisneyBundleTab() {
        tabDisneyBundlePlanCards.click();
    }

    public ExtendedWebElement getMlpStandaloneCTA() {
        return mlpStandaloneCTA;
    }

    public ExtendedWebElement getMlpComboCTA() {
        return mlpComboCTA;
    }

    public ExtendedWebElement getMlpMegaBundleCTA() {
        return mlpMegaBundleCTA;
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    public void waitForPageToFinishLoading() {
        util.waitUntilDOMready();
    }

    // Delete Cookies, Open & Go to URL based on Environments
    public void pageSetUp() {
        deleteCookies();

        if (ENVIRONMENT.equalsIgnoreCase("PROD")) {
            setPageAbsoluteURL(DisneyWebParameters.DISNEY_PROD_WEB_D23URL.getValue());
        } else if (ENVIRONMENT.equalsIgnoreCase("QA")) {
            setPageAbsoluteURL(DisneyWebParameters.DISNEY_QA_WEB_D23URL.getValue());
        }
        open();
    }

    protected static final String getProjectApiName() {
        String project = DisneyGlobalUtils.getProject();
        return project.equalsIgnoreCase("DIS") ? "disney" : "star";
    }

    public static String getEnvironmentType(String environment) {

        String currentEnvironment = environment.toUpperCase();

        if (currentEnvironment.contains("PROD") ||
                currentEnvironment.contains("HOTFIX")
        ) {
            return "PROD";
        } else if (currentEnvironment.contains("QA") ||
                currentEnvironment.contains("DEV") ||
                currentEnvironment.contains("DEV-NEXT") ||
                currentEnvironment.contains("LOCAL")
        ) {
            return "QA";
        } else if (currentEnvironment.contains("BETA")) {
            return "BETA";
        } else {
            return "PROD";
        }
    }

    public static String getBackendService(String environment) {

        String currentEnvironment = getEnvironmentType(environment);

        if (currentEnvironment.contains("BETA")) {
            return "PROD";
        } else {
            return currentEnvironment;
        }
    }

    public Map<String, String> getHeaders(String country) {
        Map<String, String> headers = new HashMap<>();
        String countryCode = convertFullCountryNameToCountryCode(country);
        DisneyProductData productData = new DisneyProductData();
        boolean productHasLaunched = productData.searchAndReturnProductData("hasLaunched").equalsIgnoreCase("true");
        boolean countryHasNotLaunched = disneyGlobalUtils.getBooleanFromCountries(countryCode, "hasNotLaunched");

        // Adds the X-BAMTech-wpnx-disable header if value in config is true
        if (R.CONFIG.getBoolean("disable_weapon_x")) {
            headers.put(DisneyHttpHeaders.BAMTECH_WPNX_DISABLE, "true");
        }

        switch (getCurrentEnvironment()) {
            case "PROD":
                headers.put(DisneyHttpHeaders.BAMTECH_IS_TEST, "true");
                headers.put(DisneyHttpHeaders.DISNEY_STAGING, DISNEY_STAGING_VALUE);
                if ((countryHasNotLaunched || !productHasLaunched) && BRANCH.equalsIgnoreCase("LIVE")) {
                    headers.put(DisneyHttpHeaders.BAMTECH_CDN_BYPASS, BAMTECH_CDN_BYPASS_VALUE);
                }
                break;
            case "BETA":
                headers.put(DisneyHttpHeaders.BAMTECH_IS_TEST, "true");
                break;
            case "QA":
                headers.put(DisneyHttpHeaders.BAMTECH_IS_TEST, "true");
                break;
            case "PREVIEW":
                headers.put(DisneyHttpHeaders.BAMTECH_IS_TEST, "true");
                if (R.CONFIG.getBoolean("custom_string2")) {
                    headers.put(DisneyHttpHeaders.BAMTECH_CANONBALL_PREVIEW, BAMTECH_CANONBALL_PREVIEW_VALUE);
                }
                break;
            default:
                CurrentTest.revertRegistration();
                throw new SkipException(String.format("Invalid environment parameter passed for navigating home: %s", getCurrentEnvironment()));
        }

        if (IS_DISNEY || IS_DGI || IS_ANA01) {
            headers.put(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY);
        } else if (IS_STAR) {
            headers.put(DisneyHttpHeaders.BAMTECH_VPN_OVERRIDE, DisneyPlusOverrideKeys.OVERRIDE_KEY_STAR);
            headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);
            headers.put(DisneyHttpHeaders.BAMTECH_DSS_PHYSICAL_COUNTRY_OVERRIDE, countryCode);
            headers.put(DisneyHttpHeaders.BAMTECH_GEO_OVERRIDE, countryCode);
            headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
            headers.put(DisneyHttpHeaders.BAMTECH_PARTNER, "star");
        }

        if ((countryHasNotLaunched || !productHasLaunched)) {
            if (IS_DISNEY || IS_DGI || IS_ANA01) {
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
            } else if (IS_STAR) {
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_STAR);
            }
            headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_OVERRIDE_KEY);
            headers.put(DisneyHttpHeaders.BAMTECH_CANONBALL_PREVIEW, BAMTECH_CANONBALL_PREVIEW_VALUE);
        }
        if (isGeoedgeUnsupportedRegion(countryCode) || BRANCH.equalsIgnoreCase("LOCAL")) {
            LOGGER.info(String.format("Passing country code override headers, %s (%s) not supported", country, countryCode));
            headers.put(DisneyHttpHeaders.BAMTECH_DSS_PHYSICAL_COUNTRY_OVERRIDE, countryCode);
            if (IS_STAR) {
                headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);
                headers.put(DisneyHttpHeaders.BAMTECH_GEO_OVERRIDE, countryCode);
                headers.put(DisneyHttpHeaders.BAMTECH_OVERRIDE_SUPPORTED_LOCATION, DisneyPlusOverrideKeys.SUPPORTED_LOCATION_STAR);
                headers.put(DisneyHttpHeaders.BAMTECH_AKA_USER_GEO_OVERRIDE, countryCode);
                headers.put(DisneyHttpHeaders.BAMTECH_PARTNER, "star");
            }
        } else if (!countryCode.equals("US") && !isGeoedgeUnsupportedRegion(countryCode)) {
            headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);
        }

            if (R.CONFIG.getBoolean("capabilities.dictionaryKeys")) {
                headers.put(DisneyHttpHeaders.DISNEY_DICTIONARY, "keys_no_sign");
            }

            if (R.CONFIG.getBoolean("capabilities.localization")) {
                headers.put(DisneyHttpHeaders.BAMTECH_GEO_ALLOW, DisneyPlusOverrideKeys.GEO_ALLOW_KEY);
                headers.put(DisneyHttpHeaders.PREVIEW_ENVIRONMENT, DisneyPlusOverrideKeys.PREVIEW_KEY);
            }
            return headers;
        }


    public BrowserMobProxy environmentSetupLocationOverride(String env, String country) {
        BrowserMobProxy proxy;
        Map<String, String> headers = getHeaders(country);
        String countryCode = convertFullCountryNameToCountryCode(country);

        if (isGeoedgeUnsupportedRegion(countryCode) || BRANCH.equalsIgnoreCase("LOCAL")) {
            proxy = ProxyPool.getProxy();
        } else if (!countryCode.equals("US") && !isGeoedgeUnsupportedRegion(countryCode) && !IS_STAR) {
            GeoedgeProxyServer geoedgeProxyFreshInstance = new GeoedgeProxyServer();
            ProxyPool.registerProxy(geoedgeProxyFreshInstance.getGeoedgeProxy(country));
            int registeredPort = geoedgeProxyFreshInstance.getProxyPortForThread();
            proxy = ProxyPool.getProxy();
            proxy.start(registeredPort);
        } else {
            proxy = ProxyPool.getProxy();
        }
        proxy.addHeaders(headers);
        LOGGER.info("Headers being used: {}", proxy.getAllHeaders());
        navigateMainStage();
        //checkForFailedProxy();
        //waitForPageToFinishLoading();
        cookieCatcher();
        return proxy;
    }

    public void navigateToMainPage() {
        navigateMainStage();
        cookieCatcher();
    }

    public void removeTestHeader(BrowserMobProxy proxy) {
        try {
            proxy.removeHeader(DisneyHttpHeaders.BAMTECH_IS_TEST);
        } catch (Exception e) {
            LOGGER.error("Exception encountered", e);
        }
    }

    public void addCookiesForStage(String cookieDomain) {
        String cookiePath = "/";
        Date today = new Date();
        Date cookieExpires = new Date(today.getTime() + (1000 * 60 * 60 * 24));

        //To DO - Temp solution for WEB-5659 Remove when it's resolved
        if(!isIgnoreCookie) {
            addCookie(new Cookie("AkamaiId", DisneyWebParameters.DISNEY_WEB_AKAMAI_ID.getValue(), cookieDomain, cookiePath, cookieExpires));
            addCookie(new Cookie("cloudFrontPolicy", DisneyWebParameters.DISNEY_WEB_CLOUDFRONT_POLICY.getValue(), cookieDomain, cookiePath, cookieExpires));
            addCookie(new Cookie("cloudFrontSignature", DisneyWebParameters.DISNEY_WEB_CLOUDFRONT_SIGNATURE.getValue(), cookieDomain, cookiePath, cookieExpires));
            addCookie(new Cookie("cloudFrontKeyPairId", DisneyWebParameters.DISNEY_WEB_CLOUDFRONT_KEYPAIR_ID.getValue(), cookieDomain, cookiePath, cookieExpires));
        }
        addCookie(new Cookie("dss_a", "123456789", cookieDomain, cookiePath, cookieExpires));
        addCookie(new Cookie("dss_branch", BRANCH.toLowerCase(), cookieDomain, cookiePath, cookieExpires));
        addCookie(new Cookie("dss_backend_services", ENVIRONMENT.toLowerCase(), cookieDomain, cookiePath, cookieExpires));
        addCookie(new Cookie("dss_z", "123456789", cookieDomain, cookiePath, cookieExpires));

        LOGGER.debug("Cookies: {}", driver.manage().getCookies());
    }

    //Navigates to either Main or Stage depending on environment parameters
    public void navigateMainStage() {
        if (BRANCH.equalsIgnoreCase("LOCAL")) {
            navigateToLocal();
        } else if (!BRANCH.equalsIgnoreCase("LIVE")) {
            navigateToStage();
            // obligatory check that loaded domain is valid otherwise cookies can't be added
            String cookieDomain = IS_STAR ? ".starplus.com" : ".disneyplus.com";
            addCookiesForStage(cookieDomain);
            navigateToStage();
        } else {
            navigateToMain();
        }
    }

    public void navigateToPreviewStage() {
        if (BRANCH.equalsIgnoreCase("LOCAL")) {
            navigateToLocal();
        } else if (!BRANCH.equalsIgnoreCase("LIVE")) {
            navigateToPreview();
            // obligatory check that loaded domain is valid otherwise cookies can't be added
            String cookieDomain = IS_STAR ? ".starplus.com" : ".disneyplus.com";
            addCookiesForStage(cookieDomain);
            navigateToPreview();
        }
    }

    public String getStageProjectUrl() {
        String stageProjectUrl;
        if (IS_STAR) {
            stageProjectUrl = StarPlusParameters.STAR_PLUS_STAGE_WEB.getValue();
        } else {
            stageProjectUrl = DisneyWebParameters.STAGE_WEB_DISNEY_PLUS.getValue();
        }

        if (DPLUS_SPECIAL_OFFER_ENABLED) {
            stageProjectUrl = stageProjectUrl + DISNEY_WEAPONX_DISABLE;
        }

        return stageProjectUrl;
    }

    public void navigateToMain() {
        String environmentUrl;
        if (IS_STAR) {
            environmentUrl = StarPlusParameters.STAR_PROD_WEB.getValue();
        } else {
            environmentUrl = DisneyWebParameters.DISNEY_PROD_WEB.getValue();
        }

        if (DPLUS_SPECIAL_OFFER_ENABLED) {
            environmentUrl = environmentUrl + DISNEY_WEAPONX_DISABLE;
        }

        navigateTo(environmentUrl);
    }

    public void navigateToPreview() {
        String environmentUrl;
        if (IS_STAR) {
            environmentUrl = StarPlusParameters.STAR_PREVIEW_WEB.getValue();
        } else {
            environmentUrl = DisneyWebParameters.PREVIEW_STAGE_WEB_DISNEY_PLUS.getValue();
        }

        if (DPLUS_SPECIAL_OFFER_ENABLED) {
            environmentUrl = environmentUrl + DISNEY_WEAPONX_DISABLE;
        }

        navigateTo(environmentUrl);
    }

    public void navigateToLocal() {
        navigateTo(getLocalUrl());
    }

    public void navigateToStage() {
        navigateTo(getStageProjectUrl());
    }

    public void navigateToUrl() { navigateTo(getHomeUrl().concat(R.CONFIG.get("custom_string")));
    }

    public String getDustService() {
        String dustService = null;
        if (IS_DISNEY) {
            switch (getCurrentEnvironment()) {
                case "PROD": case "PREVIEW":
                    dustService = DisneyWebParameters.DISNEY_EDGE_DUST_PROD.getValue();
                    break;
                case "QA":
                    dustService = DisneyWebParameters.DISNEY_EDGE_DUST_QA.getValue();
                    break;
                default:
                    throw new SkipException(String.format("Invalid Dust Environment: %s", getCurrentEnvironment()));
            }

            if (IS_STAR) {
                switch (getCurrentEnvironment()) {
                    case "PROD": case "PREVIEW":
                        dustService = StarPlusParameters.STAR_EDGE_DUST_PROD.getValue();
                        break;
                    case "QA":
                        dustService = StarPlusParameters.STAR_EDGE_DUST_QA.getValue();
                        break;
                    default:
                        throw new SkipException(String.format("Invalid Dust Environment: %s", getCurrentEnvironment()));
                }
            }
        }
        return dustService;
    }

    public String getLocalUrl() {
        String url;
        if (IS_STAR) {
            url = StarPlusParameters.STAR_LOCAL_WEB.getValue();
        } else {
            url = DisneyWebParameters.DISNEY_LOCAL_WEB.getValue();
        }

        if (DPLUS_SPECIAL_OFFER_ENABLED) {
            url = url + DISNEY_WEAPONX_DISABLE;
        }

        return url;
    }

    private void navigateTo(String url) {
        openURL(url);
        //TODO: return to the code below after fixing https://github.com/zebrunner/carina/issues/1639
        // LogicUtils: investigate if we can use $ignore placeholder to ignore any levels
        if (!isUrlAsExpected(url + "/$ignore/$ignore/$ignore", 10) && url.contains(PREVIEW)) {
            openURL(url); //forcibly reopen url
            if (!isUrlAsExpected(url + "/$ignore/$ignore/$ignore", 10) && url.contains(PREVIEW)) {
                Assert.fail("Unable to proceed as url is not loaded correctly! url: " + url);
            }
        }

        if (badGatewayHeader.isElementPresent(1)) {
            LOGGER.error("502 Bad Gateway detected!");
            Assert.fail("502 Bad Gateway detected!");
        }

    }

    //Returns current environment
    public static String getCurrentEnvironment() {
        return R.CONFIG.get("env");
    }

    //Returns Home URL for environments depending on branch,backend service, and host
    public String getHomeUrl() {
        String url = "";
        if (BRANCH.equalsIgnoreCase("LOCAL")) {
            url = getLocalUrl();
        } else if (!BRANCH.equalsIgnoreCase("LIVE")) {
            url = getStageProjectUrl();
        } else if (IS_STAR) {
            url = StarPlusParameters.STAR_PROD_WEB.getValue();
        } else {
            url = DisneyWebParameters.DISNEY_PROD_WEB.getValue();
        }

        if (DPLUS_SPECIAL_OFFER_ENABLED) {
            url = url.replace(DISNEY_WEAPONX_DISABLE, "");
        }
        return url;
    }

    //Clicks Accept Cookies Prompt if present
    public void cookieCatcher() {
        LOGGER.info("starting cookieCatcher...");
        boolean isOneIdTrustedRegion = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_ONEID_TRUSTED_REGION);
        if (getPageURL().contains(PREVIEW)) {
            LOGGER.info("Preview environment, no need to check for cookie prompt");
        } else if (isOneIdTrustedRegion) {
            LOGGER.info("Pausing 5 seconds to allow cookie prompt to populate");
            pause(5);
            dPlusBaseAcceptCookiesBtn.clickIfPresent(2);
            dPlusBaseAcceptCookiesConsentBtn.clickIfPresent(2);
        } else {
            //fluentWaitFor(dPlusBaseLogoId, 5, 20);
            LOGGER.info(String.format("No Cookie Prompt present in %s region, skipping check...", getLocale()));
        }

        LOGGER.info("finished cookieCatcher.");
    }

    //Clicks Accept Cookies Prompt if present
    public void cookieCatcherAfterLogin() {
        boolean isOneIdTrustedRegion = disneyGlobalUtils.getBooleanFromCountries(getLocale(), IS_ONEID_TRUSTED_REGION);
        if (isOneIdTrustedRegion) {
            LOGGER.info("Attempting to click cookie prompt");
            dPlusBaseAcceptCookiesBtn.clickIfPresent(1);
            dPlusBaseAcceptCookiesConsentBtn.clickIfPresent(1);
        }
    }

    //Assertion to verify if current url contains desired text/
    public void verifyUrlText(SoftAssert softAssert, String expectedUrlText) {
        LOGGER.info("Verify URL contains '" + expectedUrlText + "'.");
        String url = getCurrentUrl();
        softAssert.assertTrue(url.contains(expectedUrlText),
                String.format("URL (%s) does not contain specified text (%s)", url, expectedUrlText));
    }

    public void printAllAccountInformation(DisneyAccount account) {
        LOGGER.info("Account ID: {}", account.getAccountId());
        LOGGER.info("Device ID: {}", account.getDeviceId());
        LOGGER.info("Email: {}", account.getEmail());
        LOGGER.info("First Name: {}", account.getFirstName());
        LOGGER.info("Last Name: {}", account.getLastName());
        LOGGER.info("Profile ID: {}", account.getProfileId());
        LOGGER.info("Session ID: {}", account.getSessionId());
        LOGGER.info("User Name: {}", account.getUserName());
        LOGGER.info("IdentityPoint ID: {}", account.getIdentityPointId());
        LOGGER.info("Subscriptions: {}", account.getSubscriptions().size());
    }

    /**
     * Login Flow Method with login button click
     *
     * @param email Disney+ User Email
     * @param password Disney+ User Password
     */
    public void dBaseUniversalLogin(String email, String password) {
        if (!BRANCH.equalsIgnoreCase("LOCAL")) {
            clickDplusBaseLoginBtn();
            waitFor(signUpLoginContinueBtn); //Need this to ensure login page is fully loaded
        }
        typeDplusBaseEmailFieldId(email);
        clickSignUpLoginContinueBtn();
        typeDplusBasePasswordFieldId(password);
        clickDplusBaseLoginFlowBtn();
        cookieCatcherAfterLogin();
        clickMatureContentOnboardingBannerButton(getLocale());
    }

    public void enterLoginFlow(String userName, String password) {
        typeDplusBaseEmailFieldId(userName);
        clickSignUpLoginContinueBtn();
        typeDplusBasePasswordFieldId(password);
        clickDplusBaseLoginFlowBtn();
        clickMatureContentOnboardingBannerButton(getLocale());
    }

    public void startLoginWithPartnerAccount(String userName) {
        if (!BRANCH.equalsIgnoreCase("LOCAL")) {
            clickDplusBaseLoginBtn();
        }
        typeDplusBaseEmailFieldId(userName);
        clickSignUpLoginContinueBtn();
        clickModalPrimaryBtn();
        clickSignUpLoginContinueBtn();
    }

    public void finishLoginWithPartnerAccount(String password, String locale) {
        typeDplusBasePasswordFieldId(password);
        clickDplusBaseLoginFlowBtn();
        cookieCatcherAfterLogin();
        clickMatureContentOnboardingBannerButton(locale);
    }

    //Fluent wait for Element and Click
    public void waitForElementPollingAndClick(ExtendedWebElement element, int poll, int timeOut) {

        try {
            FluentWait<WebDriver> wait = new FluentWait<>(getDriver());
            wait.pollingEvery(Duration.ofSeconds(poll));
            wait.withTimeout(Duration.ofSeconds(timeOut));
            wait.ignoring(NoSuchElementException.class);
            wait.ignoring(TimeoutException.class);
            wait.ignoring(WebDriverException.class);

            Function<WebDriver, Boolean> waitCheck = arg0 -> {
                waitFor(element);
                element.scrollTo();
                return element.isElementPresent();
            };

            wait.until(waitCheck);
            element.click();

        } catch (TimeoutException e) {
            LOGGER.error("Timeout Exception encountered", e);
        }
    }

    private void checkForFailedProxy() {
        String pageSource = getPageSource();

        List<String> failedProxyMessages = new ArrayList<>();
        failedProxyMessages.add("ERR_CONNECTION_TIMED_OUT");
        failedProxyMessages.add("ERR_TIMED_OUT");
        failedProxyMessages.add("ERR_TUNNEL_CONNECTION_FAILED");

        for (String failedProxy : failedProxyMessages) {
            if (pageSource.contains(failedProxy)) {
                CurrentTest.revertRegistration();
                throw new SkipException(String.format("Proxy has returned '%s'", failedProxy));
            }
        }
    }

    //Convert Full Country Name to Country Code
    public String convertFullCountryNameToCountryCode(String country) {
        return countryData.searchAndReturnCountryData(country, "country", "code");
    }

    //Retrieve Locale in Config
    public String getLocale() {
        return R.CONFIG.get(LOCALE_TEXT);
    }

    //Update Locale in Config
    public void updateLocale(String country) {
        R.CONFIG.put(LOCALE_TEXT, country);
    }

    //Resize Browser Size
    public void resizeBrowser(int width, int height) {
        util.setBrowserSize(width, height);
    }

    // Delete Cookies
    public void deleteCookies() {
        getDriver().manage().deleteAllCookies();
    }

    //Captures full page screenshot and posts to zebrunner test artifacts
    public void captureFullScreenshotArtifact(String comment) {
        Screenshot.capture(getDriver(), comment, true);
    }

    // Create unique email
    public String createUniqueCredentials(String userName) {
        String emailAddressBase = userName.substring(0, userName.indexOf('@'));
        return emailAddressBase + "+new+" + retrieveCurrentTime() + createRandomAlphabeticString(15) + "@disneyplustesting.com";
    }

    //Create unique password
    public String createUniquePassword() {
        return "new_" + retrieveCurrentTime() + "_" + createRandomAlphabeticString(20);

    }

    //Listens for substring and returns full value of URL
    public String getFullUrl(int pause, String substringUrl) {
        String urlGrabbed = "";
        for (int x = 0; x <= pause; x++) {
            pause(1);
            if (getCurrentUrl().contains(substringUrl)) {
                urlGrabbed = getCurrentUrl();
                break;
            }
        }
        return urlGrabbed;
    }

    //Listens for substring and returns full value of URL
    public void listenToUrl(SoftAssert sa, int pause, String substringUrlOne, String substringUrlTwo) {
        boolean substringOneFound = false;
        boolean substringTwoFound = false;

        for (int x = 0; x <= pause * 2; x++) {
            if (x != 0) {
                pause(0.25);
            }

            if (!substringOneFound && getCurrentUrl().contains(substringUrlOne)) {
                substringOneFound = true;
            } else if (!substringTwoFound && getCurrentUrl().contains(substringUrlTwo)) {
                substringTwoFound = true;
            }
        }

        sa.assertTrue(substringOneFound,
                String.format("substringOneFound: %s, substringUrlOne: %s: ", substringOneFound, substringUrlOne));

        sa.assertTrue(substringTwoFound,
                String.format("substringTwoFound: %s, substringUrlTwo: %s: ", substringTwoFound, substringUrlTwo));

    }

    // Retrieve current time for unique email generation
    public String retrieveCurrentTime() {
        Calendar cal = Calendar.getInstance();
        return new SimpleDateFormat("MMddHHmmss").format(cal.getTime());
    }

    //Create and return a randomized string
    public String createRandomAlphabeticString(int characterCount) {
        return RandomStringUtils.randomAlphabetic(characterCount);
    }

    // Switch from PROD to QA
    public void urlSwitch(String departure, String destination) {
        String url = getCurrentUrl();
        url = url.replace(departure, destination);
        openURL(url);
    }

    // Wait for element to load
    public void waitFor(ExtendedWebElement ele) {
        waitFor(ele, DELAY / 2);
    }

    //Wait for with timeout
    public void waitFor(ExtendedWebElement ele, long timeout) {
        if (!waitUntil(ExpectedConditions.presenceOfElementLocated(ele.getBy()), timeout)) {
            LOGGER.error("Waiting for '" + ele.getNameWithLocator() + "' was unsuccessful!");
            Screenshot.capture(getDriver(), "Waiting for '" + ele.getNameWithLocator() + "' was unsuccessful!");
        }
    }

    // Error Verification
    public void verifyError(SoftAssert sa, String strMessage, ExtendedWebElement webElementLabel) {
        sa.assertTrue(strMessage.equalsIgnoreCase(webElementLabel.getText()),
                "Expected (" + strMessage + ") but page shows (" + webElementLabel.getText() + ")");
    }

    //Assertion Url Contains
    public void assertUrlContains(SoftAssert sa, String text) {
        sa.assertTrue(getCurrentUrl().contains(text), String.format("%s not present on: %s", text, getCurrentUrl()));
    }

    public void assertUrlContainsWebOrMobile(SoftAssert sa, String webUrlAssert, String mobileUrlAssert, boolean isMobile) {
        if (isMobile) {
            sa.assertTrue(getCurrentUrl().contains(mobileUrlAssert), String.format("%s not present on: %s", mobileUrlAssert, getCurrentUrl()));
        } else {
            sa.assertTrue(getCurrentUrl().contains(webUrlAssert), String.format("%s not present on: %s", webUrlAssert, getCurrentUrl()));
        }
    }

    public void assertUrlNotContains(SoftAssert sa, String text) {
        sa.assertFalse(getCurrentUrl().contains(text), String.format("%s is present on: %s", text, getCurrentUrl()));
    }

    //Assertion String format message
    public String assertionUrlString(String actual, String expected) {
        return String.format("Expected URL Redirect to contain: %s, Actual: %s", expected, actual);
    }

    public JsonNode getFullDictionary(String lang) {
        DisneyApiProvider apiProvider = new DisneyApiProvider();
        return apiProvider.getFullDictionaryBody(lang);
    }

    //Retrieve e-mail credentials for account. Handles QA/Beta/Prod login.
    public String getEnvBasedAccount(String qaAccount, String prodAccount) {
        switch (getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT)) {
            case "QA":
                return qaAccount;
            case "PROD":
            case "BETA":
                return prodAccount;
            default:
                CurrentTest.revertRegistration();
                throw new SkipException("Invalid environment passed for retrieving account");
        }
    }

    /**
     * Method to set account object for UI generated accounts as V2-ORDER for test teardown order cancellation
     * @param email
     * @param password
     * @return
     */

    public DisneyAccount setAndGetUIGeneratedAccount(String email, String password) {
        String id = "";
        try {
            id = getAccountApi().readAccount(email, password).getAccountId();
        } catch (DisneyAccountNotFoundException e) {
            e.getMessage();
        }
        DisneyAccount account;
        List<DisneySubscriptions> subs = new ArrayList<>();
        DisneySubscriptions sub = new DisneySubscriptions();
        sub.setSubscriptionVersion(SUB_VERSION_V2_ORDER);
        subs.add(sub);
        account = DisneyAccount.builder().subscriptions(subs).accountId(id).build();
        return account;
    }

    protected DisneyAccountApi getAccountApi() {
        String env = getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT);
        String project = getProjectApiName();
        return new DisneyAccountApi(WEB_PLATFORM, env, project);
    }

    public void analyticPause(){
        if (R.CONFIG.getBoolean("enable_hora_validation")){
            pause(5);
        }
    }

    public void waitForSeconds(long secs) {
        LOGGER.info("Waiting for : {} seconds" , secs);
        CommonUtils.pause(secs);
    }

    public String getCreditCardNumber(String env, String locale, boolean isFreeTrialFraud) {

        String qaCreditCard = countryData.searchAndReturnCountryData(locale, WebConstant.CODE, WebConstant.CC);

        if (isFreeTrialFraud) {
            qaCreditCard = countryData.searchAndReturnCountryData(locale, WebConstant.CODE, WebConstant.FTF_CC);
        }

        if (env.equalsIgnoreCase(WebConstant.PROD) || env.equalsIgnoreCase(WebConstant.BETA)) {
            String prodCreditCard;
            if (isFreeTrialFraud) {
                prodCreditCard = countryData.searchAndReturnCountryData(locale, WebConstant.CODE, WebConstant.PROD_FTF_CC);
            } else {
                prodCreditCard = countryData.searchAndReturnCountryData(locale, WebConstant.CODE, WebConstant.PROD_CC);
            }
            if (prodCreditCard != null) {
                return prodCreditCard;
            }
        }

        return qaCreditCard;
    }

    public String getCreditCardName(String locale) {
        boolean isEuCountry = disneyGlobalUtils.getBooleanFromCountries(locale, WebConstant.IS_EU_COUNTRY);
        if (isEuCountry) {
            return WebConstant.EU_NAME_ON_CARD;
        } else {
            return WebConstant.NAME_ON_CARD;
        }
    }

    public String collectNewTabLandingPageURL() {
        ArrayList<String> tabs = new ArrayList(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(tabs.size()-1));
        waitForPageToFinishLoading();
        String actualContent = new SeleniumUtils(getDriver()).getCurrentUrl();
        getDriver().close();
        getDriver().switchTo().window(tabs.get(0));
        return actualContent;
    }
}
