package com.disney.qa.disney.web.appex.userflows;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.entities.PageUrl;
import com.disney.qa.disney.web.entities.WebConstant;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusUserPage extends DisneyPlusCommercePage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//%s[%s]")
    private ExtendedWebElement masterUniversalElement;

    @FindBy(xpath = "//button[contains(text(),'%s')]")
    private ExtendedWebElement buttonContainsText;

    @FindBy(xpath = "//p[contains(text(),'%s')]")
    private ExtendedWebElement paragraphContainsText;

    @FindBy(xpath = "//*[contains(text(),'%s')]")
    private ExtendedWebElement allElementsContainsText;

    @FindBy(xpath = "//%s[contains(text(),'%s')]")
    private ExtendedWebElement nodeContainsText;

    @FindBy(id = "logo")
    private ExtendedWebElement disneyLogoId;

    @FindBy(xpath = "//img[@class='welcome-page__content__logo']")
    private ExtendedWebElement disneyLogo;

    @FindBy(id = "loginEmail")
    private ExtendedWebElement dssLoginWrapperId;

    @FindBy(name = "dssLoginSubmit")
    private ExtendedWebElement dssLoginSubmit;

    @FindBy(xpath = "//label[@aria-labelledby='credit-radio-icon']")
    private ExtendedWebElement dssCreditIcon;

    @FindBy(xpath = "//input[@id='password']//following-sibling::button")
    private ExtendedWebElement dssPasswordShowLabel;

    @FindBy(xpath = "//div[contains(text(),'hide')]")
    private ExtendedWebElement dssPasswordHideLabel;

    @FindBy(xpath = "//div[contains(text(),'6 characters')]")
    private ExtendedWebElement dssPasswordHelp;

    @FindBy(xpath = "//input[@id='password' and @type='password']")
    private  ExtendedWebElement encryptedPasswordInput;

    @FindBy(xpath = "//section[contains(@class,'hero')]/div/picture")
    private ExtendedWebElement brandWrap;

    @FindBy(xpath = "//*[@data-testid='hero-down-arrow']")
    private ExtendedWebElement foldArrow;

    @FindBy(id = "footer")
    private ExtendedWebElement footerId;

    @FindBy(xpath = "//h3[contains(text(),'Enter a password')]")
    private ExtendedWebElement enterPasswordCopy;

    @FindBy(xpath = "//h3[contains(text(),'Your email seems familiar')]")
    private ExtendedWebElement yourEmailSeemsFamiliarCopy;

    @FindBy(xpath = "//div[contains(@class,'input-error')]")
    private ExtendedWebElement passwordAlert;

    @FindBy(xpath = "//button//span[contains (text(),'Log In')]")
    private ExtendedWebElement signUpLoginBtn;

    @FindBy(xpath = "//h3[contains (text(), 'Enter your email')]")
    private ExtendedWebElement enterEmailHeader;

    @FindBy(xpath = "//input[@type='checkbox']")
    private ExtendedWebElement marketingOptInCheckBox;

    @FindBy(xpath = "//div[contains(text(),'receive updates')]")
    private ExtendedWebElement marketingEmailSubTxt;

    @FindBy(xpath = "//input[@type='checkbox']")
    private ExtendedWebElement marketingCheckBox;

    @FindBy(xpath = "//div[contains(text(),'Agree & Continue')]")
    private ExtendedWebElement legalAgreementTxt;

    @FindBy(xpath = "//*[@id='dssLogin']/div/div/div/button[2]")
    private ExtendedWebElement subscriberAgreementBtn;

    @FindBy(xpath = "//span[contains (text(),'Subscriber Agreement')]")
    private ExtendedWebElement subscriberAgreementBody;

    @FindBy(xpath = "//*[@id='dssLogin']/div/div/div/button[1]")
    private ExtendedWebElement privacyLink;

    @FindBy(xpath = "//span[contains (text(),'Privacy Policy')]")
    private ExtendedWebElement privacyBody;

    @FindBy(xpath = "//a[contains (text(),'Log In')]")
    private ExtendedWebElement signUploginLink;

    @FindBy(xpath = "//h1[contains (text(),'Legal')]")
    private ExtendedWebElement legalHeader;

    @FindBy(xpath = "//*[contains(@class, 'input-error')]")
    private ExtendedWebElement inputError;

    @FindBy(xpath = "//p[contains (text(),'Unknown email. Please check your spelling.')]")
    private ExtendedWebElement unknownEmailMessage;

    @FindBy(xpath = "//*[contains(@data-testid, 'onboarding-stepper')]")
    private ExtendedWebElement onboardingStepperStep1;

    @FindBy(xpath = "//*[@data-testid='signup-continue-button']")
    private ExtendedWebElement signupContinueButton;

    @FindBy(xpath = "//*[@data-testid='password-continue-login']")
    private ExtendedWebElement passwordContinueButton;

    @FindBy(xpath = "//*[@data-testid='CheckBoxOptIn-dplus-kr_sub_proxy']")
    private ExtendedWebElement optInCheckBox;

    @FindBy(xpath = "//*[@data-testid='CheckBoxOptIn-dplus-kr_thirdparty_proxy']")
    private ExtendedWebElement thirdPartyOptInCheckBox;

    @FindBy(xpath = "//*[@data-testid='checkbox-opt-in-error']")
    private ExtendedWebElement optInErrorMessage;

    @FindBy(xpath = "//*[@id='footer']//button[1]")
    private ExtendedWebElement privacyPolicy;

    @FindBy(xpath = "//*[@data-testid='cancel_subscription_footer']")
    private ExtendedWebElement cancelSubscriptionFooter;

    @FindBy(xpath = "//*[@data-testid='cannot-apply-promotion-button']")
    private ExtendedWebElement noPromotionButton;

    public DisneyPlusUserPage(WebDriver driver) {
        super(driver);
    }

    public DisneyPlusUserPage open(WebDriver driver) {
        getHomeUrl();
        waitForPageToFinishLoading();
        return new DisneyPlusUserPage(driver);
    }

    /**
     * Element Getters
     **/

    public ExtendedWebElement masterGenericElement(String body, String text) {
        return masterUniversalElement.format(body, text);
    }

    public ExtendedWebElement buttonContainsElement(String text) {
        return buttonContainsText.format(text);
    }

    public ExtendedWebElement paragraphContainsElement(String text) {
        return paragraphContainsText.format(text);
    }

    public ExtendedWebElement allElementsContainsText(String text) {
        return allElementsContainsText.format(text);
    }

    public ExtendedWebElement genericContainsElement(String body, String text) {
        return nodeContainsText.format(body, text);
    }

    public ExtendedWebElement getPasswordAlert() {
        return passwordAlert;
    }

    public ExtendedWebElement getDssLoginWrapperId() {
        return dssLoginWrapperId;
    }

    public ExtendedWebElement getSignupContinueButton() {
        return signupContinueButton;
    }

    public ExtendedWebElement getLegalCenterTitle() {
        return legalHeader;
    }

    public ExtendedWebElement getCancelSubscriptionFooter() {
        waitFor(cancelSubscriptionFooter);
        return cancelSubscriptionFooter;
    }

    /**
     * Element Boolean Assertions
     */

    public boolean verifyPage() {
        LOGGER.info("Verify User page loaded");
        return enterPasswordCopy.isVisible();
    }

    public boolean isDisneyLogoIdPresent() {
        return disneyLogoId.isElementPresent();
    }

    public boolean verifyLoginBtn() {
        return getDplusBaseLoginBtn().isPresent();
    }

    public boolean isFooterIdPresent() {
        return footerId.isElementPresent();
    }

    public boolean isEnterPasswordCopyPresent() {
        return enterPasswordCopy.isElementPresent();
    }

    public boolean isEmailSeemsFamiliarCopyVisible() {
        LOGGER.info("Is 'Your email seems familiar' copy visible");
        return yourEmailSeemsFamiliarCopy.isVisible();
    }

    public boolean isContentBrandsPresent() {
        return brandWrap.isElementPresent();
    }

    public boolean isFoldArrowPresent() {
        return foldArrow.isElementPresent();
    }

    public boolean isEmailFieldPresent() {
        return isDplusBaseEmailFieldIdPresent();
    }

    public boolean isSubscriberAgreementBtnPresent() {
        return subscriberAgreementBtn.isElementPresent();
    }

    public boolean isPrivacyLinkPresent() {
        return privacyLink.isElementPresent();
    }

    public boolean isPasswordFieldPresent() {
        return getDplusBasePasswordFieldId().isElementPresent();
    }

    public boolean isForgotPasswordIdPresent() {
        return getdPlusBaseForgotPasswordId().isElementPresent();
    }

    public boolean isPasswordSubmitBtnPresent() {
        return dssLoginSubmit.isElementPresent();
    }

    public boolean isCreditIconPresent(){
        return dssCreditIcon.isElementPresent();
    }

    public boolean isPasswordFieldShowLblPresent() {
        return dssPasswordShowLabel.isElementPresent();
    }

    public boolean isPasswordFieldHideLblPresent() {
        return dssPasswordHideLabel.isElementPresent();
    }

    public boolean isPasswordHelpPresent() {
        return dssPasswordHelp.isElementPresent();
    }

    public boolean isPasswordFieldEncrypted() {
        return encryptedPasswordInput.isElementPresent();
    }

    public boolean isInvalidEmailMsgPresent() {
        return inputError.isElementPresent();
    }

    public boolean isPasswordAlertPresent() {
        return passwordAlert.isElementPresent();
    }

    public boolean isSignUpMarketingOptInChecked() {
        return marketingCheckBox.isChecked();
    }

    public boolean isOptInErrorMessagePresent() {
        return optInErrorMessage.isPresent();
    }

    public boolean isPrivacyPolicyPresent() {
        return privacyPolicy.isElementPresent();
    }

    public boolean isNoPromoButtonPresent(long timeout) { return noPromotionButton.isElementPresent(timeout); }

    /**
     * End Element Boolean Assertions
     */

    /**
     * Element Interactions
     */

    public void clickOnSignupContinueButton(){
        LOGGER.info("Click on Continue button");
        getSignupContinueButton().click();
    }

    public String getInputErrorAttribute(String attribute) {
        return inputError.getAttribute(attribute);
    }

    public void clickDisneyLogoId() {
        disneyLogoId.click();
    }

    public void clickTrialBtn() {

        if(getDplusBaseTrialBtn().isElementNotPresent(5)) {
            LOGGER.info("Waited 5 seconds for free trial element to appear....");
        }

        getDplusBaseTrialBtn().click();
    }

    public void clickLoginBtn() {
        getDplusBaseLoginBtn().click();
    }

    public void clickLoginSubmit() {
        waitFor(dssLoginSubmit);
        dssLoginSubmit.click();
    }

    public void clickShowPasswordLbl() {
        dssPasswordShowLabel.click();
    }

    public void clickSubscriberAgreementBtn() {
        subscriberAgreementBtn.click();
    }

    public void clickPrivacyLink() {
        privacyLink.click();
    }

    public void typeEmailFieldId(String text) {
        typeDplusBaseEmailFieldId(text);
    }

    public void clickMarketingOptInCheckBox() {
        marketingOptInCheckBox.click();
    }

    public DisneyPlusUserPage enterPassword(String password) {
        encryptedPasswordInput.type(password);
        return this;
    }

    public DisneyPlusUserPage clickPasswordContinueButton() {
        passwordContinueButton.click();
        return this;
    }

    public void clickCheckBoxOptIn() {
        optInCheckBox.click();
    }

    public void clickCheckBoxThirdPartyProxy() {
        thirdPartyOptInCheckBox.click();
    }

    public ExtendedWebElement getOnboardingStepper() {
        LOGGER.info("Verify step 1 for onboarding stepper");
        return onboardingStepperStep1;
    }

    public void clickCancelSubscriptionFooter() {
        getCancelSubscriptionFooter().click();
    }

    public void handleCheckBoxesOnEnterEmail(String locale) {
        boolean isCheckBoxOptInCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_EMAIL_CHECKBOX_OPTIN_COUNTRY);

        if (isCheckBoxOptInCountry) {
            clickCheckBoxOptIn();
            clickCheckBoxThirdPartyProxy();
        }
    }

    public void clickPrivacyPolicy() {
        privacyPolicy.click();
    }

    public void clickNoPromotionButton(){
        LOGGER.info("Click on can't apply promotion button");
        noPromotionButton.click();
    }

    /**
     * End Element Interactions
     */

    /** Reusable Methods For Flows**/

    //Enter Sign up Enter Email and Password Flow
    public void signUpCreateEmail(String email, boolean isBundle, boolean isRedemptionOrActivation, ThreadLocal<DisneyAccount>  disneyAccount) {
        String locale = getLocale();
        if (isBundle) {
            boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_COMBO_COUNTRY);
            if (IS_DISNEY && isComboCountry && !isMegaBundleTest()) {
                openURL(getHomeUrl().concat(PageUrl.SIGNUP_TYPE_BUNDLE));
            } else if (isMegaBundleTest()) {
                openURL(getHomeUrl().concat(PageUrl.SIGN_UP_TYPE_LIONSGATEPLUSBUNDLE));
            } else {
                clickDplusAnnualBndleBtn();
            }
        } else if (!isRedemptionOrActivation) {
            handleStandaloneCtasOnOlp();
        }
        disneyAccount.set(setAndGetUIGeneratedAccount(email, GENERIC_PASS));
        typeDplusBaseEmailFieldId(email);
        handleCheckBoxesOnEnterEmail(locale);
        clickSignUpLoginContinueBtn();
    }

    public void deprecatedSignUpCreateEmail(String email, boolean isBundle, boolean isRedemptionOrActivation, ThreadLocal<DisneyAccount>  disneyAccount) {
        LOGGER.info("Deprecated Sign up and create email");
        String locale = getLocale();
        if (isBundle) {
            boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_COMBO_COUNTRY);
            if (IS_DISNEY && isComboCountry) {
                openURL(getHomeUrl().concat(PageUrl.SIGNUP_TYPE_BUNDLE));
            } else {
                clickDplusAnnualBndleBtn();
            }
        } else if (!isRedemptionOrActivation) {
            handleStandaloneCtasOnOlp();
        }
        disneyAccount.get().setEmail(email);
        typeDplusBaseEmailFieldId(email);
        handleCheckBoxesOnEnterEmail(locale);
        clickSignUpLoginContinueBtn();
    }

    public void signUpEmail(boolean isBundle, boolean isRedemptionOrActivation, ThreadLocal<DisneyAccount> disneyAccount) {
        String locale = getLocale();
        if (isBundle) {
            boolean isComboCountry = disneyGlobalUtils.getBooleanFromCountries(locale, IS_COMBO_COUNTRY);

            if (IS_DISNEY && isComboCountry && !isMegaBundleTest()) {
                openURL(getHomeUrl().concat(PageUrl.SIGNUP_TYPE_BUNDLE));
            } else if (isMegaBundleTest()) {
                openURL(getHomeUrl().concat(PageUrl.SIGN_UP_TYPE_LIONSGATEPLUSBUNDLE));
            } else {
                handleBundleCtaOnOlp();
            }
        } else if (!isRedemptionOrActivation) {
            handleStandaloneCtasOnOlp();
        }
        waitForPageToFinishLoading();
        typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());

        handleCheckBoxesOnEnterEmail(locale);
        clickSignUpLoginContinueBtn();
    }

    public Boolean createEmailAndLogin(String locale, boolean isBundle, boolean isRedemptionOrActivation, String language, ThreadLocal<DisneyAccount> disneyAccount, BrowserMobProxy proxy) throws URISyntaxException, MalformedURLException {
        if (isGeoedgeUnsupported(locale, proxy)) {
            return createEntitledAccountAndLogin(locale, language, isRedemptionOrActivation, disneyAccount);
        } else {
            deprecatedSignUpCreateEmail(DisneyApiCommon.getUniqueEmail(), isBundle, isRedemptionOrActivation, disneyAccount);
            clickSubscriberAgreementContinueCta(locale);
            disneyAccount.get().setUserPass(GENERIC_PASS);
            getDplusBasePasswordFieldId().type(GENERIC_PASS);
            clickLoginSubmit();

            return false;
        }
    }

    /**
     * Creates a randomly generated unique user e-mail for use
     * @param locale - Region
     * @param isBundle - Determines if flow is a bundle flow
     * @param isRedemptionOrActivation - Determines if flow contains either redemption or activation flow - set to true to skip login btn click
     */

    public Boolean signUpGeneratedEmailPassword(String locale, boolean isBundle, boolean isRedemptionOrActivation, String language, ThreadLocal<DisneyAccount> disneyAccount, BrowserMobProxy proxy) throws URISyntaxException, JSONException, MalformedURLException {
        if (isGeoedgeUnsupported(locale, proxy)) {
            return handleUnsupportedGeoedge(locale, language, isRedemptionOrActivation, isBundle, disneyAccount);
        } else {
            DisneyAccountApi accountApi = getAccountApi();
            CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder()
                    .country(locale).isStarOnboarded(false).build();
            disneyAccount.set(accountApi.createAccount(accountRequest));

            signUpCreateEmail(DisneyApiCommon.getUniqueEmail(), isBundle, isRedemptionOrActivation, disneyAccount);
            clickSubscriberAgreementContinueCta(locale);
            disneyAccount.get().setUserPass(GENERIC_PASS);
            getDplusBasePasswordFieldId().type(GENERIC_PASS);
            clickLoginSubmit();
            
            return false;
        }
    }
    public Boolean redirectToSignUpQsp(String redirectQSP) {
        openURL(getHomeUrl().concat(redirectQSP));
        waitForPageToFinishLoading();
        return false;
    }

    /**
     * Flow for signing up with e-mail and password as parameters
     */
    public Boolean signUpEmailPassword(String locale, boolean isBundle, boolean isRedemptionOrActivation, String email, String password, String language, boolean existingAccount, ThreadLocal<DisneyAccount> disneyAccount, BrowserMobProxy proxy) throws URISyntaxException, JSONException, MalformedURLException {
        if (isGeoedgeUnsupported(locale, proxy) && !existingAccount) {
            return handleUnsupportedGeoedge(locale, language, isRedemptionOrActivation, isBundle, email, disneyAccount);
        } else {
            signUpEmail(isBundle, isRedemptionOrActivation, disneyAccount);
            if (!existingAccount) {
                clickSubscriberAgreementContinueCta(locale);
            } else {
                clickSubscriberAgreementContinueCtaIfPresent(locale);
            }
            typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());

            clickLoginSubmit();
            return false;
        }
    }

    /**
     * Flow for signing up with e-mail activation as parameters without clicking complete subscription
     */
    public Boolean signUpEmailPasswordActivation(String locale, String language, boolean isRedemptionOrActivation, boolean existingAccount, ThreadLocal<DisneyAccount> disneyAccount, BrowserMobProxy proxy) throws URISyntaxException, MalformedURLException {
        if (isGeoedgeUnsupported(locale, proxy) && !existingAccount) {
            return handleUnsupportedGeoedgeActivation(locale, language, isRedemptionOrActivation, disneyAccount);
        }
        return false;
    }

    /**
     * If geoedge does not exist or if we are on the LOCAL enviorment
     */

    public Boolean handleUnsupportedGeoedge(String locale, String language, boolean isRedemptionOrActivation, boolean isBundle, ThreadLocal<DisneyAccount> disneyAccount) throws URISyntaxException, JSONException, MalformedURLException {
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder()
                .country(locale).language(language).isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));
        Boolean override = accountApi.overrideLocations(disneyAccount.get(), locale);

        if (!isRedemptionOrActivation) {
            dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
            clickCompleteAndRestartPurchaseBaseCta(locale,  WebConstant.COMPLETE_PURCHASE, isBundle);
        } else {
            enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        }
        disneyAccount.set(setAndGetUIGeneratedAccount(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass()));
        return override;
    }

    /**
     * Eventually combine this method with HandleUnsupportedGeoedge
    */
    public Boolean createEntitledAccountAndLogin(String locale, String language, boolean isRedemptionOrActivation, ThreadLocal<DisneyAccount> disneyAccount) throws URISyntaxException, MalformedURLException {
        LOGGER.info("Creating entitled account and logging in");
        DisneyAccountApi accountApi = getAccountApi();

        DisneyOffer offer = accountApi.lookupOfferToUse(locale, "Monthly");
        DisneyAccount account = accountApi.createAccount(offer, locale, language, SUB_VERSION_V2_ORDER);
        disneyAccount.set(account);
        Boolean override = accountApi.overrideLocations(account, locale);

        if (!isRedemptionOrActivation) {
            dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        }
        return override;
    }

    /**
     * If geoedge does not exist or if we are on the LOCAL enviorment and we are attempting to login/signup with an existing account
     */
    public Boolean handleUnsupportedGeoedge(String locale, String language, boolean isRedemptionOrActivation, boolean isBundle, String email, ThreadLocal<DisneyAccount> disneyAccount) throws URISyntaxException, JSONException, MalformedURLException {
        if (disneyAccount.get() == null) {
            DisneyAccountApi accountApi = getAccountApi();
            disneyAccount.set(accountApi.createAccount(locale, language));
        }
        Boolean override = accountApi.overrideLocations(disneyAccount.get(), locale);

        if (!isRedemptionOrActivation) {
            dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
            clickCompleteAndRestartPurchaseBaseCta(locale,  WebConstant.COMPLETE_PURCHASE, isBundle);
        } else {
            enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        }

        return override;
    }

    /**
     * If geoedge does not exist or if we are on the LOCAL enviorment and we are attempting to login/signup with an existing account without clicking complete subscription
     */
    public Boolean handleUnsupportedGeoedgeActivation(String locale, String language, boolean isRedemptionOrActivation, ThreadLocal<DisneyAccount> disneyAccount) throws URISyntaxException, MalformedURLException {
        DisneyAccountApi accountApi = getAccountApi();
        if (disneyAccount.get() == null) {
            CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder()
                    .country(locale).language(language).isStarOnboarded(false).build();
            disneyAccount.set(accountApi.createAccount(accountRequest));
        }
        Boolean override = accountApi.overrideLocations(disneyAccount.get(), locale);

        if (!isRedemptionOrActivation) {
            dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        } else {
            enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        }

        return override;
    }

    /**
     * If geoedge does not exist or if we are on the LOCAL enviorment
     */
    public boolean isGeoedgeUnsupported(String locale, BrowserMobProxy proxy) {
        boolean isTestAccountNeeded = getEnvironmentType(ENVIRONMENT).equals("BETA") || getEnvironmentType(ENVIRONMENT).equals("PROD");
        boolean isAccountGenerated = (BRANCH.equals("LOCAL") || isGeoedgeUnsupportedRegion(locale) || isTestAccountNeeded);

        if (!isAccountGenerated) {
            removeTestHeader(proxy);
        }

        return isAccountGenerated;
    }
}
