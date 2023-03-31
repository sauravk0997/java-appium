package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.responses.profile.DisneyProfile;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOrder;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.common.web.VerifyEmail;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.JSONException;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DisneyPlusMoreMenuAccountSettingsTest extends DisneyBaseTest {

    private static final String EMAIL_SUBJECT = "Your one-time passcode";
    private static final String NEW_PASSWORD = "TestPass1234!";
    private static final String MONTHLY = "Monthly";
    private static final String YEARLY = "Yearly";
    private static final String DISNEY_URL = "disneyplus.com";
    private static final String VERIZON_URL = "verizon.com";
    private static final String TELMEX_URL = "telmex.com";
    private static final String O2_URL = "o2.com";
    private static final String CANAL_URL = "client.canalplus.com";
    private static final String GOOGLE_URL = "accounts.google.com";
    private static final String HULU_URL = "auth.hulu.com";
    private static final String ROKU_URL = "my.roku.com";
    private static final String SKY_URL = "skyid.sky.com";
    private static final String AMAZON_URL = "amazon.com";
    private static final String MERCADOLIBRE_URL = "mercadolibre.com";
    private static final String MERCADOLIBRE_BR_URL = "mercadolivre.com";
    private static final String CABLEVISION_URL = "telecom.com.ar";
    private static final String TELECOM_TIM_URL = "tim.it";
    private static final String MOVISTAR_URL = "movistar.es";
    private static final String BRADESCO_URL = "banco.bradesco";
    private static final String BRADESCO_NEXT_URL = "next.me";
    private static final String DETELEKOM_URL = "telekom.de";
    ThreadLocal<VerifyEmail> verifyEmail = new ThreadLocal<>();

    @BeforeMethod
    public void handleAlert() {
        super.handleAlert();
    }

    //Monthly and Yearly direct billing entitlements
    private void setDisneyEntitlements() {
        List<DisneyEntitlement> disneyEntitlements = new LinkedList<>();
        disneyEntitlements.add(new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), MONTHLY), SUBSCRIPTION_V2_ORDER));
        disneyEntitlements.add(new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), YEARLY), SUBSCRIPTION_V2_ORDER));
    }

    public void setAppToAccountSettings() {
        setAppToHomeScreen(disneyAccount.get(), disneyAccount.get().getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61547"})
    @Test(description = "Verify the Account submenu display elements are present", groups = {"More Menu"})
    public void verifyAccountDisplay() {
        setGlobalVariables();
        setAppToAccountSettings();
        SoftAssert sa = new SoftAssert();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        sa.assertTrue(disneyPlusAccountIOSPageBase.getDynamicAccessibilityId(disneyAccount.get().getEmail()).isPresent(),
                "User Email address was not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isChangeLinkPresent(disneyAccount.get().getEmail())
                        && disneyPlusAccountIOSPageBase.isChangeLinkActive(disneyAccount.get().getEmail()),
                "Change Email link was not displayed and enabled");

        sa.assertTrue(disneyPlusAccountIOSPageBase.getDynamicAccessibilityId(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.HIDDEN_PASSWORD.getText())).isPresent(),
                "User Password (hidden) was not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isChangeLinkPresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.HIDDEN_PASSWORD.getText())),
                "Change Password link was not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.getStaticTextByLabel(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE.getText())).isPresent(),
                "Billing Details (Subscriptions) header not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.getStaticTextByLabel(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ACCOUNT_SETTINGS_HEADER.getText())).isPresent(),
                "Settings header not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isRestrictProfilesContainerPresent(),
                "Restrict Profile Creation container was not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isEditProfilesLinkPresent(),
                "Edit Profiles link was not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isEditProfilesTextPresent(),
                "Edit Profiles text was not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isPrivacyChoicesLinkPresent(),
                "Privacy Choices link was not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61571", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for D+ Premium displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_DisneyPlus() {
        setGlobalVariables();
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isDisneyPlusPremiumSubscriptionPresent(),
                "D+ Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openDisneyPlusPremiumWebView();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(DISNEY_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61573", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for D+ Bundle displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_DisneyBundle() {
        setGlobalVariables();
        disneyAccount.set(disneyAccountApi.get().createAccount("Disney+, Hulu, and ESPN+", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUBSCRIPTION_V1));

        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        setAppToAccountSettings();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isBamtechBundleSubscriptionMessagePresent(),
                "D+ Bundle Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openBamtechBundleWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(DISNEY_URL),
                "Webview did not open to the expected url");
    }

    public DisneyAccount createAccountWithSku(DisneySkuParameters sku, String country, String language) {
        CreateDisneyAccountRequest request = new CreateDisneyAccountRequest();
        request.addSku(sku);
        request.setCountry(country);
        request.setLanguage(language);
        return disneyAccountApi.get().createAccount(request);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61575", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Hulu Bundle displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_HuluBundle() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        setAppToAccountSettings();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isHuluBundleSubscriptionMessagePresent(),
                "Hulu Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openHuluBundleWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(HULU_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61577", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Google Bundle displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_GooglePlay() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_GOOGLE_YEARLY, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isGoogleSubscriptionMessagePresent(),
                "Google Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openGoogleWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(GOOGLE_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61579", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Roku displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_Roku() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_ROKU_YEARLY, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isRokuSubscriptionMessagePresent(),
                "Roku Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openRokuWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(ROKU_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61581", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Amazon Bundle displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_Amazon() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_AMAZON_YEARLY, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isAmazonSubscriptionMessagePresent(),
                "Amazon Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openAmazonWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(AMAZON_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61583", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Verizon displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_Verizon() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_PROMO_BUNDLE_12MONTH, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isVerizonSubscriptionMessagePresent(),
                "Verizon Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openVerizonWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(VERIZON_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61585", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for O2 displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_O2() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        setAppToAccountSettings();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isO2SubscriptionMessagePresent(),
                "O2 Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openO2SubcriptionWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(GOOGLE_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61587", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Telecom TIM displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_TelecomTIM() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_TIM_IT_STANDALONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isTelecomTIMSubscriptionMessagePresent(),
                "Telecom TIM Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openTIMWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(TELECOM_TIM_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61589", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Movistar displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_Movistar() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_EXTERNAL_MOVISTAR_STANDALONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isMovistarSubscriptionMessagePresent(),
                "Moviestar Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openMovistarWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(MOVISTAR_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61591", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Deutsche Telekom displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_DeutscheTelekom() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_EXTERNAL_DETELEKOM_STANDALONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isDeutscheTelekomSubscriptionMessagePresent(),
                "Deutsche Telekom Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openDeTelekomWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(DETELEKOM_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61593", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Sky is displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_Sky() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_EXTERNAL_SKYUK_STANDALONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isSkySubscriptionMessagePresent(),
                "Sky Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openSkyWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(SKY_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61609", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description Telmex is displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_Telmex() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_TELMEX_MX_STANDALONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        setAppToAccountSettings();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isTelmexSubscriptionMessagePresent(),
                "Telmex Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openTelmexWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(TELMEX_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61610", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Bradesco is displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_Bradesco() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_BRADESCO_BANK_BR_STANDALONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isBradescoSubscriptionMessagePresent(),
                "Bradesco Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openBradescoWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(BRADESCO_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61611", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Bradesco NEXT is displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_BradescoNext() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_BRADESCO_NEXT_BR_STANDALONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isBradescoNextSubscriptionMessagePresent(),
                "Bradesco NEXT Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openBradescoNextWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(BRADESCO_NEXT_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61612", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Telefonica Vivo is displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_TelefonicaVivo() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_VIVO_BR_STANDALONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isTelefonicaVivoSubscriptionMessagePresent(),
                "Telefonica Vivo Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openVivoWebview();

        if (disneyPlusAccountIOSPageBase.continueButtonPresent()) {
            disneyPlusAccountIOSPageBase.getTypeButtonByLabel("Continue").clickIfPresent();
            disneyPlusAccountIOSPageBase.getTypeButtonByLabel("Turn On Personalized Ads").clickIfPresent();
            disneyPlusAccountIOSPageBase.getTypeButtonByLabel("Allow While Using App").clickIfPresent();
        }
        //Expecting vivo app in app store
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isTelefonicaAppPresentInAppStore(),
                "App didn't redirect to the Vivo app in app store");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61613", "XMOBQA-66500", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Mercado Libre displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_MercadoLibre() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_MERCADOLIBRE_MX_STANDALONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isMercadoLibreSubscriptionMessagePresent(),
                "Mercado Libre Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openMercadoLibreWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(MERCADOLIBRE_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66499", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Cablevision is displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_Cablevision() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_TELECOM_AR_STANDAONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isCablevisionSubscriptionMessagePresent(),
                "Cablevision/Telecom Argentina SA Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openCablevisionWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        //Use wait instead of instant check to account for multiple redirects in browser.
        disneyPlusAccountIOSPageBase.waitUntilWebviewUrlContains(CABLEVISION_URL);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66502", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Mercado Libre Brazil is displayed", groups = {"More Menu"})
    public void verifySubscriptionDetails_MercadoLibreBrazil() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_MERCADOLIBRE_BR_STANDALONE, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isMercadoLibreBrazilSubscriptionMessagePresent(),
                "Mercado Libre Brazil Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openMercadoLibreBrazilWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(MERCADOLIBRE_BR_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62564", "XMOBQA-62566", "XMOBQA-62568", "XMOBQA-62570"})
    @Test(description = "Verify the 'Unverified email badge is displayed in the More Menu and Account submenu", groups = {"More Menu"})
    public void verifyUnverifiedAccountFunctions() throws URISyntaxException {
        setGlobalVariables();
        SoftAssert sa = new SoftAssert();
        //Builds a DisneyAccount object with existing credentials that are already configured for test needs
        DisneyProfile profile = new DisneyProfile();
        profile.setProfileName("Profile");
        DisneyAccount externalAccount = DisneyAccount.builder()
                .email("dssproducttest+o2_z77mhgfgtezqn6mm@gmail.com")
                .accountId("3167b40f-4066-485b-99f7-110bb891b4e1")
                .userPass("Mandalorian11!")
                .profiles(List.of(profile))
                .build();
        disneyAccountApi.get().entitleAccount(externalAccount, disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE), "V1");
        disneyAccount.set(externalAccount);

        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);

        setAppToHomeScreen(disneyAccount.get(), disneyAccount.get().getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isAccountUnverifiedBadgeDisplayed(),
                "XMOBQA-62564 - Unverified Account badge was not displayed");

        disneyPlusMoreMenuIOSPageBase.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);

        sa.assertTrue(disneyPlusAccountIOSPageBase.isVerifyAccountHeaderPresent(),
                "XMOBQA-62566 - Verify Account header was not displayed");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isVerifyAccountLinkPresent(),
                "XMOBQA-62566 - Verify Account link was not present");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isChangeLinkPresent(disneyAccount.get().getEmail())
                        && !disneyPlusAccountIOSPageBase.isChangeLinkActive(disneyAccount.get().getEmail()),
                "XMOBQA-62570 - Change Email link was not displayed and disabled");

        disneyPlusAccountIOSPageBase.clickVerifyAccountLink();

        sa.assertTrue(disneyPlusOneTimePasscodeIOSPageBase.isOpened(),
                "XMOBQA-62568 -  OTP Page was not opened");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61557", "XMOBQA-61559", "XMOBQA-61565", "XMOBQA-61561"})
    @Test(description = "Verify the UI elements for the Change Password screen from Account Settings", groups = {"More Menu"})
    public void testChangePasswordUI() {
        setGlobalVariables();
        SoftAssert sa = new SoftAssert();
        verifyEmail.set(new VerifyEmail());
        Date startTime = verifyEmail.get().getStartTime();
        disneyAccount.set(disneyAccountApi.get().createAccountForOTP(languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangePasswordIOSPageBase disneyPlusChangePasswordIOSPageBase = new DisneyPlusChangePasswordIOSPageBase(getDriver());

        disneyPlusAccountIOSPageBase.clickChangeLink(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.HIDDEN_PASSWORD.getText()));
        String otp = verifyEmail.get().getDisneyOTP(disneyAccount.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);

        Assert.assertTrue(disneyPlusOneTimePasscodeIOSPageBase.isOpened(),
                "XMOBQA-61559 - OTP entry page was not opened");

        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValue(otp);

        Assert.assertTrue(disneyPlusChangePasswordIOSPageBase.isOpened(),
                "XMOBQA-61559 - 'Change Password' screen was not opened");

        sa.assertTrue(disneyPlusChangePasswordIOSPageBase.isHeadlineSubtitlePresent(),
                "XMOBQA-61559 - 'Change Password' subtitle was not displayed");

        sa.assertTrue(disneyPlusChangePasswordIOSPageBase.isLogoutAllDevicesTitlePresent(),
                "XMOBQA-61559 - 'Logout All Devices' title was not displayed");

        sa.assertTrue(disneyPlusChangePasswordIOSPageBase.isSaveBtnPresent(),
                "XMOBQA-61559 - 'Save' button was not displayed");

        sa.assertTrue(disneyPlusChangePasswordIOSPageBase.isCancelBtnPresent(),
                "XMOBQA-61559 - 'Cancel' button was not displayed");

        Assert.assertTrue(disneyPlusChangePasswordIOSPageBase.isLogoutAllDevicesUnchecked(),
                "XMOBQA-61559 - 'Logout All Devices' was not unchecked by default");

        disneyPlusChangePasswordIOSPageBase.clickLogoutAllDevices();

        sa.assertTrue(disneyPlusChangePasswordIOSPageBase.isLogoutAllDevicesChecked(),
                "XMOBQA-61559 - 'Logout All Devices' was not checked");

        sa.assertTrue(disneyPlusChangePasswordIOSPageBase.isLogoutAllDevicesPasswordCopyDisplayed(),
                "XMOBQA-61559 - 'Logout All Devices' password text was not displayed");

        disneyPlusChangePasswordIOSPageBase.submitNewPasswordValue("invalid");

        sa.assertTrue(disneyPlusChangePasswordIOSPageBase.isInvalidPasswordErrorDisplayed(),
                "XMOBQA-61565 - 'Invalid Password' error was not displayed");

        disneyPlusChangePasswordIOSPageBase.clickCancelBtn();

        sa.assertTrue(disneyPlusAccountIOSPageBase.isOpened(),
                "XMOBQA-61561 - User was not directed back to 'Account Settings' after cancelling the password change");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61563"})
    @Test(description = "Verify the password save functionality flow without Logout checked", groups = {"More Menu"})
    public void testChangePasswordWithoutLogout() {
        setGlobalVariables();
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangePasswordIOSPageBase disneyPlusChangePasswordIOSPageBase = new DisneyPlusChangePasswordIOSPageBase(getDriver());

        verifyEmail.set(new VerifyEmail());
        Date startTime = verifyEmail.get().getStartTime();
        disneyAccount.set(disneyAccountApi.get().createAccountForOTP(languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        disneyPlusAccountIOSPageBase.clickChangeLink(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.HIDDEN_PASSWORD.getText()));
        String otp = verifyEmail.get().getDisneyOTP(disneyAccount.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);
        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValue(otp);
        disneyPlusChangePasswordIOSPageBase.submitNewPasswordValue(NEW_PASSWORD);

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isOpened(),
                "User was not directed back to 'Account Settings' after changing their password");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61599"})
    @Test(description = "Verify the password save functionality flow with Logout checked", groups = {"More Menu"})
    public void testChangePasswordWithLogout() {
        setGlobalVariables();
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangePasswordIOSPageBase disneyPlusChangePasswordIOSPageBase = new DisneyPlusChangePasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        verifyEmail.set(new VerifyEmail());
        Date startTime = verifyEmail.get().getStartTime();
        disneyAccount.set(disneyAccountApi.get().createAccountForOTP(languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        disneyPlusAccountIOSPageBase.clickChangeLink(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.HIDDEN_PASSWORD.getText()));
        String otp = verifyEmail.get().getDisneyOTP(disneyAccount.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);
        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValue(otp);
        disneyPlusChangePasswordIOSPageBase.clickLogoutAllDevices();

        disneyPlusChangePasswordIOSPageBase.submitNewPasswordValue(NEW_PASSWORD);

        Assert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isOpened(),
                "User was logged out after changing their password");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61551", "XMOBQA-61553", "XMOBQA-61555"})
    @Test(description = "Verify the UI elements for the Change Password screen from Account Settings", groups = {"More Menu"})
    public void testChangeEmailUI() {
        setGlobalVariables();
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangeEmailIOSPageBase disneyPlusChangeEmailIOSPageBase = new DisneyPlusChangeEmailIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        verifyEmail.set(new VerifyEmail());
        Date startTime = verifyEmail.get().getStartTime();
        disneyAccount.set(disneyAccountApi.get().createAccountForOTP(languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        disneyPlusAccountIOSPageBase.clickChangeLink(disneyAccount.get().getEmail());
        String otp = verifyEmail.get().getDisneyOTP(disneyAccount.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);

        Assert.assertTrue(disneyPlusOneTimePasscodeIOSPageBase.isOpened(),
                "XMOBQA-61553 - OTP entry page was not opened");

        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValue(otp);

        Assert.assertTrue(disneyPlusChangeEmailIOSPageBase.isOpened(),
                "XMOBQA-61551 - 'Change Email' screen was not opened");

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isHeadlineSubtitlePresent(),
                "XMOBQA-61551 - 'Change Email' subtitle was not displayed");

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isCurrentEmailShown(disneyAccount.get().getEmail()),
                "XMOBQA-61551 - 'Change Email' display of user email was not shown");

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isNewEmailHeaderPresent(),
                "XMOBQA-61551 - 'Change Email' text entry header was not displayed");

        Assert.assertTrue(disneyPlusChangeEmailIOSPageBase.isLogoutAllDevicesUnchecked(),
                "XMOBQA-61551 - 'Change Email' device logout checkbox was not unchecked by default");

        disneyPlusChangeEmailIOSPageBase.clickLogoutAllDevices();

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isLogoutAllDevicesChecked(),
                "XMOBQA-61551 - 'Logout All Devices' was not checked");

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isLogoutAllDevicesEmailCopyDisplayed(),
                "XMOBQA-61551 - 'Logout All Devices' password text was not displayed");

        disneyPlusChangeEmailIOSPageBase.submitNewEmailAddress("invalid");

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isInvalidEmailErrorDisplayed(),
                "XMOBQA-61551 - 'Invalid Email' error was not displayed");

        disneyPlusChangeEmailIOSPageBase.clickCancelBtn();

        sa.assertTrue(disneyPlusAccountIOSPageBase.isOpened(),
                "XMOBQA-61555 - User was not returned to the Account Settings page after cancelling new email submission");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61553"})
    @Test(description = "Verify the user is returned to Welcome after submitting new Email with Logout checked", groups = {"More Menu"})
    public void testChangeEmailWithoutLogout() {
        setGlobalVariables();
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangeEmailIOSPageBase disneyPlusChangeEmailIOSPageBase = new DisneyPlusChangeEmailIOSPageBase(getDriver());

        verifyEmail.set(new VerifyEmail());
        Date startTime = verifyEmail.get().getStartTime();
        disneyAccount.set(disneyAccountApi.get().createAccountForOTP(languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        disneyPlusAccountIOSPageBase.clickChangeLink(disneyAccount.get().getEmail());
        String otp = verifyEmail.get().getDisneyOTP(disneyAccount.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);
        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValue(otp);
        String newEmail = generateGmailAccount();

        disneyPlusChangeEmailIOSPageBase.submitNewEmailAddress(newEmail);

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isOpened(),
                "User was not returned to Account Settings after submitting the new email");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isChangeLinkPresent(newEmail),
                "The User's new email address was not displayed as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61601", "XMOBQA-61553"})
    @Test(description = "Verify the user is returned to Welcome after submitting new Email with Logout checked", groups = {"More Menu"})
    public void testChangeEmailWithLogout() {
        setGlobalVariables();
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangeEmailIOSPageBase disneyPlusChangeEmailIOSPageBase = new DisneyPlusChangeEmailIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = new DisneyPlusHomeIOSPageBase(getDriver());

        verifyEmail.set(new VerifyEmail());
        Date startTime = verifyEmail.get().getStartTime();
        disneyAccount.set(disneyAccountApi.get().createAccountForOTP(languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        disneyPlusAccountIOSPageBase.clickChangeLink(disneyAccount.get().getEmail());
        String otp = verifyEmail.get().getDisneyOTP(disneyAccount.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);
        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValue(otp);
        disneyPlusChangeEmailIOSPageBase.clickLogoutAllDevices();
        String newEmail = generateGmailAccount();
        disneyAccount.get().setEmail(newEmail);

        disneyPlusChangeEmailIOSPageBase.submitNewEmailAddress(newEmail);

        Assert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isOpened(),
                "User was not logged out and returned to the Welcome screen after submitting the new email");

        setAppToHomeScreen(disneyAccount.get(), disneyAccount.get().getProfiles().get(0).getProfileName());

        Assert.assertTrue(disneyPlusHomeIOSPageBase.isOpened(),
                "User was not able to log in successfully with the new email");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61597"})
    @Test(description = "Verify the UI of the 'Logout of all devices'", groups = {"More Menu"})
    public void testLogoutOfAllDevicesUI() {
        setGlobalVariables();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusLogOutOfDevicesIOSPageBase disneyPlusLogOutOfDevicesIOSPageBase = new DisneyPlusLogOutOfDevicesIOSPageBase(getDriver());

        SoftAssert sa = new SoftAssert();
        setAppToAccountSettings();
        disneyPlusAccountIOSPageBase.clickLogOutOfAllDevices();

        Assert.assertTrue(disneyPlusLogOutOfDevicesIOSPageBase.isOpened(),
                "'Log out of all accounts' screen did not open");

        sa.assertTrue(disneyPlusLogOutOfDevicesIOSPageBase.isSubtitleDisplayed(),
                "Subtitle was not displayed");

        sa.assertTrue(disneyPlusLogOutOfDevicesIOSPageBase.isPasswordTextEntryPresent(),
                "Password entry field was not displayed");

        sa.assertTrue(disneyPlusLogOutOfDevicesIOSPageBase.isForgotPasswordLinkDisplayed(),
                "'Forgot Password?' link was not displayed");

        sa.assertTrue(disneyPlusLogOutOfDevicesIOSPageBase.isPrimaryButtonPresent(),
                "'Log Out' button was not displayed");

        sa.assertAll();
    }

    //TODO: Refactor to use 2 drivers to cover XMOBQA-61603
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61599"})
    @Test(description = "Verify the UI of the 'Logout of all devices'", groups = {"More Menu"})
    public void testLogoutOfAllDevicesForgotPasswordFunctions() {
        setGlobalVariables();
        verifyEmail.set(new VerifyEmail());
        Date startTime = verifyEmail.get().getStartTime();
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusLogOutOfDevicesIOSPageBase disneyPlusLogOutOfDevicesIOSPageBase = new DisneyPlusLogOutOfDevicesIOSPageBase(getDriver());
        DisneyPlusChangePasswordIOSPageBase disneyPlusChangePasswordIOSPageBase = new DisneyPlusChangePasswordIOSPageBase(getDriver());

        disneyAccount.set(disneyAccountApi.get().createAccountForOTP(languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        disneyPlusAccountIOSPageBase.clickLogOutOfAllDevices();
        disneyPlusLogOutOfDevicesIOSPageBase.clickForgotPasswordLink();

        Assert.assertTrue(disneyPlusOneTimePasscodeIOSPageBase.isOpened(),
                "OTP Page was not opened");

        String otp = verifyEmail.get().getDisneyOTP(disneyAccount.get().getEmail(), EmailApi.getOtpAccountPassword(), EMAIL_SUBJECT, startTime);
        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValue(otp);

        Assert.assertTrue(disneyPlusChangePasswordIOSPageBase.isOpened(),
                "Change Password screen did not open after submitting OTP");

        disneyPlusChangePasswordIOSPageBase.submitNewPasswordValue(NEW_PASSWORD);

        Assert.assertTrue(disneyPlusLogOutOfDevicesIOSPageBase.isOpened(),
                "User was not returned to 'Log out of all devices' after submitting new password");

        disneyPlusLogOutOfDevicesIOSPageBase.submitPasswordAndLogout(NEW_PASSWORD);

        Assert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isOpened(),
                "User was not returned to the Welcome screen upon logout");
    }



    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61604"})
    @Test(description = "Verify Subscription section header displays correctly", groups = {"More Menu"})
    public void verifySubscriptionsSectionHeader() throws JSONException, URISyntaxException {
        setGlobalVariables();
        SoftAssert sa = new SoftAssert();
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        sa.assertTrue(disneyPlusAccountIOSPageBase.isSingleSubHeaderPresent(),
                "Single Subscription header text was not displayed");

        disneyAccountApi.get().entitleAccount(disneyAccount.get(), DisneySkuParameters.DISNEY_IAP_ROKU_YEARLY, SUBSCRIPTION_V1);
        logout();
        setAppToAccountSettings();

        sa.assertTrue(disneyPlusAccountIOSPageBase.isStackedSubHeaderPresent(),
                "Stacked Subscription header text was not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61607"})
    @Test(description = "Verify monthly subscription details for Apple subscribers", groups = {"More Menu"})
    public void verifyAccountMonthlyToAnnualDisplays_Apple() {
        setGlobalVariables();
        SoftAssert sa = new SoftAssert();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusPaywallIOSPageBase disneyPlusPaywallIOSPageBase = new DisneyPlusPaywallIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.areSwitchToAnnualElementsDisplayed(),
                "Switch to Annual description and/or CTA was not displayed");

        disneyPlusAccountIOSPageBase.clickSwitchToAnnualButton();

        Assert.assertTrue(disneyPlusPaywallIOSPageBase.isOpened(),
                "User was not directed to the paywall after clicking Switch CTA");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isSwitchToAnnualHeaderDisplayed(),
                "'Switch to Annual' header was not shown");

        sa.assertTrue(disneyPlusPaywallIOSPageBase.isSwitchToAnnualCopyDisplayed(),
                "'Switch to Annual' copy was not shown");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61607"})
    @Test(description = "Verify monthly subscription details for Google subscribers", groups = {"More Menu"})
    public void verifyAccountMonthlyToAnnualDisplays_Google() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_GOOGLE_MONTHLY, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.areSwitchToAnnualElementsDisplayed(),
                "Switch to Annual description and/or CTA was not displayed");

        disneyPlusAccountIOSPageBase.clickSwitchToAnnualButton();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(GOOGLE_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61607"})
    @Test(description = "Verify Monthly to Annual option is not present for Amazon Monthly subscribers", groups = {"More Menu"})
    public void verifyAccountMonthlyToAnnualDisplays_Amazon() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_AMAZON_MONTHLY, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertFalse(disneyPlusAccountIOSPageBase.areSwitchToAnnualElementsDisplayed(),
                "Switch to Annual description and/or CTA was displayed unexpectedly");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61607"})
    @Test(description = "Verify Monthly to Annual option is not present for Roku Monthly subscribers", groups = {"More Menu"})
    public void verifyAccountMonthlyToAnnualDisplays_Roku() {
        setGlobalVariables();
        disneyAccount.set(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_ROKU_MONTHLY, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertFalse(disneyPlusAccountIOSPageBase.areSwitchToAnnualElementsDisplayed(),
                "Switch to Annual description and/or CTA was displayed unexpectedly");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62826"})
    @Test(description = "Verify Direct Paused Billing display and navigation", groups = {"More Menu"}, enabled = false)
    public void verifyPausedSubscription_VerizonStandalone() {
        setGlobalVariables();
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());

        List<DisneyEntitlement> directBillingEntitlements = new LinkedList<>();
        directBillingEntitlements.add(new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), MONTHLY), SUBSCRIPTION_V2_ORDER));
        directBillingEntitlements.add(new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), YEARLY), SUBSCRIPTION_V2_ORDER));

        directBillingEntitlements.forEach(direct -> {
            List<DisneyEntitlement> disneyEntitlements = Arrays.asList(direct, new DisneyEntitlement(disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE), SUBSCRIPTION_V1));
            CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().entitlements(disneyEntitlements).country(languageUtils.get().getLocale()).language(languageUtils.get().getUserLanguage()).build();
            disneyAccount.set(disneyAccountApi.get().createAccount(createDisneyAccountRequest));
            sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE, VERIZON_URL));
        });

        sa.get().assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62826"})
    @Test(description = "Verify Direct Paused Billing display and navigation", groups = {"More Menu"}, enabled = false)
    public void verifyPausedSubscription_Canal() {
        setGlobalVariables();
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());

        List<DisneyEntitlement> directBillingEntitlements = new LinkedList<>();
        directBillingEntitlements.add(new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), MONTHLY), SUBSCRIPTION_V2_ORDER));
        directBillingEntitlements.add(new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), YEARLY), SUBSCRIPTION_V2_ORDER));

        directBillingEntitlements.forEach(direct -> {
            List<DisneyEntitlement> disneyEntitlements = Arrays.asList(direct, new DisneyEntitlement(disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE), SUBSCRIPTION_V1));
            CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().entitlements(disneyEntitlements).country(languageUtils.get().getLocale()).language(languageUtils.get().getUserLanguage()).build();
            disneyAccount.set(disneyAccountApi.get().createAccount(createDisneyAccountRequest));
            sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE, CANAL_URL));
        });

        sa.get().assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62826"})
    @Test(description = "Verify Direct Paused Billing display and navigation", groups = {"More Menu"}, enabled = false)
    public void verifyPausedSubscription_O2() {
        setGlobalVariables();
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());

        List<DisneyEntitlement> directBillingEntitlements = new LinkedList<>();
        directBillingEntitlements.add(new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), MONTHLY), SUBSCRIPTION_V2_ORDER));
        directBillingEntitlements.add(new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), YEARLY), SUBSCRIPTION_V2_ORDER));

        directBillingEntitlements.forEach(direct -> {
            List<DisneyEntitlement> disneyEntitlements = Arrays.asList(direct, new DisneyEntitlement(disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE), SUBSCRIPTION_V1));
            CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().entitlements(disneyEntitlements).country(languageUtils.get().getLocale()).language(languageUtils.get().getUserLanguage()).build();
            disneyAccount.set(disneyAccountApi.get().createAccount(createDisneyAccountRequest));
            sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE, O2_URL));
        });

        sa.get().assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62826"})
    @Test(description = "Verify Direct Paused Billing display and navigation", groups = {"More Menu"}, enabled = false)
    public void verifyPausedSubscription_TelMex() {
        setGlobalVariables();
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());

        List<DisneyEntitlement> directBillingEntitlements = new LinkedList<>();
        directBillingEntitlements.add(new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), MONTHLY), SUBSCRIPTION_V2_ORDER));
        directBillingEntitlements.add(new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), YEARLY), SUBSCRIPTION_V2_ORDER));

        directBillingEntitlements.forEach(direct -> {
            List<DisneyEntitlement> disneyEntitlements = Arrays.asList(direct, new DisneyEntitlement(disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_PARTNER_TELMEX_MX_STANDALONE), SUBSCRIPTION_V1));
            CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().entitlements(disneyEntitlements).country(languageUtils.get().getLocale()).language(languageUtils.get().getUserLanguage()).build();
            disneyAccount.set(disneyAccountApi.get().createAccount(createDisneyAccountRequest));
            sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_PARTNER_TELMEX_MX_STANDALONE, TELMEX_URL));
        });

        sa.get().assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62827", "XMOBQA-69488"})
    @Test(description = "Verify an unpaused direct billing subscription updates the Subscriptions list correctly", groups = {"More Menu"})
    public void verifyUnpausedSubscription() {
        setGlobalVariables();
        SoftAssert sa = new SoftAssert();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        List<DisneyEntitlement> disneyEntitlements = Arrays.asList(
                new DisneyEntitlement(disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), YEARLY), SUBSCRIPTION_V2_ORDER),
                new DisneyEntitlement(disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_PARTNER_TELMEX_MX_STANDALONE), SUBSCRIPTION_V1));
        CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().entitlements(disneyEntitlements).country(languageUtils.get().getLocale()).language(languageUtils.get().getUserLanguage()).build();
        disneyAccount.set(disneyAccountApi.get().createAccount(createDisneyAccountRequest));
        setAppToAccountSettings();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isStackedSubHeaderPresent()
                        && disneyPlusAccountIOSPageBase.isDirectBillingPausedSubscriptionDisplayed(),
                "Required 'Paused Subscription' state was not applied to the account. Unpaused entitlement check will be invalid.");

        logout();

        disneyAccountApi.get().revokeSku(disneyAccount.get(), disneyEntitlements.get(1).getOffer());

        setAppToAccountSettings();

        sa.assertTrue(disneyPlusAccountIOSPageBase.isSingleSubHeaderPresent(),
                "Subscriptions header text was not displayed correctly");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isBamtechSubscriptionMessagePresent(),
                "Disney+ Direct billing subscription was not displayed correctly");

        sa.assertFalse(disneyPlusAccountIOSPageBase.isTelmexSubscriptionMessagePresent(),
                "Revoked partner subscription was displayed unexpectedly");

        disneyPlusAccountIOSPageBase.openBamtechWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(DISNEY_URL),
                "Webview did not open to the expected url");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66501"})
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {"More Menu"}, enabled = false)
    public void verifyIAPBillingHoldWithPartnerSub() {
        setGlobalVariables();
        SoftAssert sa = new SoftAssert();
        String firstName = "Test";
        String lastName = "User";
        List<DisneyOrder> billingOrder = new LinkedList<>();
        List<DisneySkuParameters> iapOnHold = Arrays.asList(DisneySkuParameters.DISNEY_IAP_GOOGLE_MONTHLY, DisneySkuParameters.DISNEY_IAP_GOOGLE_YEARLY,
                DisneySkuParameters.DISNEY_IAP_AMAZON_MONTHLY, DisneySkuParameters.DISNEY_IAP_AMAZON_YEARLY,
                DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY, DisneySkuParameters.DISNEY_IAP_APPLE_YEARLY,
                DisneySkuParameters.DISNEY_IAP_ROKU_MONTHLY, DisneySkuParameters.DISNEY_IAP_ROKU_YEARLY);
        List<DisneySkuParameters> activeProviders = Arrays.asList(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE, DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE,
                DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE, DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE);

        iapOnHold.forEach(iap -> activeProviders.forEach(provider -> {
            LOGGER.info("Entitlement combo: {}/{}", iap, provider);
            billingOrder.add(DisneyOrder.SET_BILLING_HOLD);
            CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(generateGmailAccount())
                    .language(languageUtils.get().getUserLanguage())
                    .country(languageUtils.get().getLocale()).build();

            createDisneyAccountRequest.addSku(iap);
            createDisneyAccountRequest.setOrderSettings(billingOrder);
            disneyAccount.get().getOrderSettings().clear();

            try {
                disneyAccountApi.get().entitleAccount(
                        disneyAccount.get(),
                        disneyAccountApi.get().fetchOffer(provider),
                        SUBSCRIPTION_V1);

                performIapHoldProviderSubscriptionCheck(sa, provider, iap);
            } catch (URISyntaxException | JSONException e) {
                e.printStackTrace();
                LOGGER.info("Something went wrong with generating the account with entitlements {}/{}", iap, provider);
            }
        }));

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66503"})
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {"More Menu"}, enabled = false)
    public void verifyBillingHoldWithPartnerSub_VerizonStandalone() throws JSONException, URISyntaxException {
        setGlobalVariables();
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());

        DisneyAccount monthly = disneyAccountApi.get().createAccountWithBillingHold(
                disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), MONTHLY).getOfferId(),
                languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        monthly.getOrderSettings().clear();
        disneyAccountApi.get().entitleAccount(monthly, disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE), SUBSCRIPTION_V1);
        disneyAccount.set(monthly);

        sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE, VERIZON_URL));

        DisneyAccount yearly = disneyAccountApi.get().createAccountWithBillingHold(
                disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), YEARLY).getOfferId(),
                languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        yearly.getOrderSettings().clear();
        disneyAccountApi.get().entitleAccount(yearly, disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE), SUBSCRIPTION_V1);
        disneyAccount.set(yearly);

        sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE, VERIZON_URL));

        sa.get().assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66503"})
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {"More Menu"}, enabled = false)
    public void verifyBillingHoldWithPartnerSub_VerizonBundle() throws JSONException, URISyntaxException {
        setGlobalVariables();
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());

        DisneyAccount monthly = disneyAccountApi.get().createAccountWithBillingHold(
                disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), MONTHLY).getOfferId(),
                languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        monthly.getOrderSettings().clear();
        disneyAccountApi.get().entitleAccount(monthly, disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE), SUBSCRIPTION_V1);
        disneyAccount.set(monthly);

        sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE, VERIZON_URL));

        DisneyAccount yearly = disneyAccountApi.get().createAccountWithBillingHold(
                disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), YEARLY).getOfferId(),
                languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        yearly.getOrderSettings().clear();
        disneyAccountApi.get().entitleAccount(yearly, disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE), SUBSCRIPTION_V1);
        disneyAccount.set(yearly);

        sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE, VERIZON_URL));

        sa.get().assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66503"})
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {"More Menu"}, enabled = false)
    public void verifyBillingHoldWithPartnerSub_Canal() throws JSONException, URISyntaxException {
        setGlobalVariables();
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());
        List<DisneyOrder> billingOrder = new LinkedList<>();
        billingOrder.add(DisneyOrder.SET_BILLING_HOLD);
        DisneyAccount monthly = disneyAccountApi.get().createAccountWithBillingHold(
                disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), MONTHLY).getOfferId(),
                languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        monthly.getOrderSettings().clear();
        disneyAccountApi.get().entitleAccount(monthly, disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE), SUBSCRIPTION_V1);
        disneyAccount.set(monthly);

        sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE, CANAL_URL));

        DisneyAccount yearly = disneyAccountApi.get().createAccountWithBillingHold(
                disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), YEARLY).getOfferId(),
                languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        yearly.getOrderSettings().clear();
        disneyAccountApi.get().entitleAccount(yearly, disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE), SUBSCRIPTION_V1);
        disneyAccount.set(yearly);

        sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE, CANAL_URL));

        sa.get().assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66503"})
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {"More Menu"}, enabled = false)
    public void verifyBillingHoldWithPartnerSub_O2() throws JSONException, URISyntaxException {
        setGlobalVariables();
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());

        DisneyAccount monthly = disneyAccountApi.get().createAccountWithBillingHold(
                disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), MONTHLY).getOfferId(),
                languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        monthly.getOrderSettings().clear();
        disneyAccountApi.get().entitleAccount(monthly, disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE), SUBSCRIPTION_V1);
        disneyAccount.set(monthly);

        sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE, O2_URL));

        DisneyAccount yearly = disneyAccountApi.get().createAccountWithBillingHold(
                disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), YEARLY).getOfferId(),
                languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        yearly.getOrderSettings().clear();
        disneyAccountApi.get().entitleAccount(yearly, disneyAccountApi.get().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE), SUBSCRIPTION_V1);
        disneyAccount.set(yearly);

        sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE, O2_URL));

        sa.get().assertAll();
    }


    //Helper methods

    /**
     * Performs the Paused Entitlement check by checking for the passed URL value for the given provider.
     * Checks both the Partner and Direct subscription tab navigation.
     *
     * @param url - The expected URL (fragment, whole path is not required) when opening the webview
     * @return - The soft assertion in the test for which it was called.
     */
    private SoftAssert performPausedEntitlementCheck(DisneySkuParameters sku, String url) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        setAppToAccountSettings();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isStackedSubHeaderPresent(),
                "Stacked Subscription header text was not displayed. Unable to verify Paused status.");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isDirectBillingPausedSubscriptionDisplayed(),
                "Direct Billing 'Paused' subscription was not displayed");

        disneyPlusAccountIOSPageBase.openBillingProvider(sku);

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                String.format("Browser webview did not open on navigation for the following SKU: %s", sku));

        sa.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(url),
                String.format("Webview did not open to the expected URL: %s", url));

        relaunch();
        disneyPlusAccountIOSPageBase.clickPausedDirectBillingContainer();

        sa.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(DISNEY_URL),
                String.format("Webview did not open to the expected URL: %s", DISNEY_URL));

        relaunch();
        logout();
        return sa;
    }

    /**
     * Performs the Held Entitlement check by checking for the the presence of the active provider and the lack of
     * display for the held IAP provider.
     */
    private void performIapHoldProviderSubscriptionCheck(SoftAssert sa, DisneySkuParameters activeProvider, DisneySkuParameters heldIapService) {
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        try {
            setAppToAccountSettings();

            sa.assertTrue(disneyPlusAccountIOSPageBase.isSingleSubHeaderPresent(),
                    "Single Subscription header text was not displayed");

            sa.assertFalse(disneyPlusAccountIOSPageBase.isBillingProviderCellPresent(heldIapService, 5),
                    "Held IAP subscription cell was displayed unexpectedly for sku: " + heldIapService);

            sa.assertTrue(disneyPlusAccountIOSPageBase.isBillingProviderCellPresent(activeProvider),
                    "Active Partner cell was not displayed for sku: " + activeProvider);

            logout();
        } catch (NoSuchElementException e) {
            LOGGER.error("There was a navigation error. Iteration was not completed");
            sa.fail(String.format("Something went wrong with navigation for the account with entitlement combo: %s/%s", activeProvider, heldIapService));
            e.printStackTrace();
            restart();
        }
    }
}