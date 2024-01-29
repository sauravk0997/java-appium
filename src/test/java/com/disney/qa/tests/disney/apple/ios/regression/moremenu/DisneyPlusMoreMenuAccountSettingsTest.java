package com.disney.qa.tests.disney.apple.ios.regression.moremenu;

import com.disney.qa.api.account.CancellationReasons;
import com.disney.qa.api.account.PatchType;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.responses.profile.DisneyProfile;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.pojos.DisneyOrder;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.json.JSONException;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DisneyPlusMoreMenuAccountSettingsTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
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

    @BeforeMethod
    public void handleAlert() {
        super.handleAlert();
    }
    @DataProvider(name = "disneyPlanTypes")
    public Object[][] disneyPlanTypes() {
        return new Object[][]{
                {MONTHLY}, {YEARLY}
        };
    }

    public void setAppToAccountSettings() {
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }

    public void setAppToAccountSettings(DisneyAccount account) {
        setAppToHomeScreen(account, account.getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61547"})
    @Test(description = "Verify the Account submenu display elements are present", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyAccountDisplay() {
        DisneyAccount accountV2 = createV2Account();
        setAppToAccountSettings(accountV2);
        SoftAssert sa = new SoftAssert();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = initPage(DisneyPlusAccountIOSPageBase.class);
        pause(3);
        sa.assertTrue(disneyPlusAccountIOSPageBase.getStaticTextByLabel(accountV2.getEmail()).isPresent(),
                "User Email address was not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isChangeLinkPresent(accountV2.getEmail())
                        && disneyPlusAccountIOSPageBase.isChangeLinkActive(accountV2.getEmail()),
                "Change Email link was not displayed and enabled");

        sa.assertTrue(disneyPlusAccountIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.HIDDEN_PASSWORD.getText())).isPresent(),
                "User Password (hidden) was not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isChangeLinkPresent(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.HIDDEN_PASSWORD.getText())),
                "Change Password link was not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.SUBSCRIPTIONS_TITLE.getText())).isPresent(),
                "Billing Details (Subscriptions) header not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.getStaticTextByLabel(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.ACCOUNT_SETTINGS_HEADER.getText())).isPresent(),
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
    @Test(description = "Verify that the correct description for D+ Premium displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifySubscriptionDetails_DisneyPlus() {
        DisneyAccount accountV2 = createV2Account();
        setAppToAccountSettings(accountV2);
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
    @Test(description = "Verify that the correct description for D+ Bundle displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifySubscriptionDetails_DisneyBundle() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_VERIFIED_HULU_ESPN_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        setAppToAccountSettings();
            SoftAssert sa  = new SoftAssert();
        sa.assertTrue(disneyPlusAccountIOSPageBase.isBamtechBundleMonthlySubscriptionTitlePresent(),
                "D+ Bundle Subscription title was not displayed");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isBamtechBundleSubscriptionMessagePresent(),
                "D+ Bundle Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openBamtechBundleWebview();

        sa.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        sa.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(DISNEY_URL),
                "Webview did not open to the expected url");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61575", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Hulu Bundle displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_HuluBundle() {
        setAccount(createAccountWithSku(DisneySkuParameters.HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
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
    @Test(description = "Verify that the correct description for Google Bundle displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_GooglePlay() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_GOOGLE_YEARLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isGoogleSubscriptionTitlePresent(),
                "Google Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isGoogleSubscriptionMessagePresent(),
                "Google Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openGoogleWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(GOOGLE_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61579", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Roku displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_Roku() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_ROKU_YEARLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isRokuSubscriptionTitlePresent(),
                "Roku Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isRokuSubscriptionMessagePresent(),
                "Roku Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openRokuWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(ROKU_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61581", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Amazon Bundle displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_Amazon() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_AMAZON_YEARLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isAmazonSubscriptionTitlePresent(),
                "Amazon Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isAmazonSubscriptionMessagePresent(),
                "Amazon Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openAmazonWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(AMAZON_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61583", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Verizon displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_Verizon() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_PROMO_BUNDLE_12MONTH, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isVerizonSubscriptionTitlePresent(),
                "Verizon Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isVerizonSubscriptionMessagePresent(),
                "Verizon Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openVerizonWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(VERIZON_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61585", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for O2 displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_O2() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        setAppToAccountSettings();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isO2SubscriptionTitlePresent(),
                "O2 Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isO2SubscriptionMessagePresent(),
                "O2 Subscription message was not displayed");

        //Due to existing IOS-2649 bug, commenting out below steps for now
        /*disneyPlusAccountIOSPageBase.openO2SubscriptionWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(GOOGLE_URL),
                "Webview did not open to the expected url");*/
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61587", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Telecom TIM displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_TelecomTIM() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_TIM_IT_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isTelecomTIMSubscriptionTitlePresent(),
                "Telecom TIM Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isTelecomTIMSubscriptionMessagePresent(),
                "Telecom TIM Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openTIMWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(TELECOM_TIM_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61589", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Movistar displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_Movistar() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_EXTERNAL_MOVISTAR_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isMovistarSubscriptionTitlePresent(),
                "Moviestar Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isMovistarSubscriptionMessagePresent(),
                "Moviestar Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openMovistarWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(MOVISTAR_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61591", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Deutsche Telekom displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_DeutscheTelekom() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_EXTERNAL_DETELEKOM_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isDeutscheTelekomSubscriptionTitlePresent(),
                "Deutsche Telekom Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isDeutscheTelekomSubscriptionMessagePresent(),
                "Deutsche Telekom Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openDeTelekomWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(DETELEKOM_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61593", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Sky is displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_Sky() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_EXTERNAL_SKYUK_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isSkySubscriptionTitlePresent(),
                "Sky Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isSkySubscriptionMessagePresent(),
                "Sky Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openSkyWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(SKY_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61609", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description Telmex is displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_Telmex() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_TELMEX_MX_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        setAppToAccountSettings();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isTelmexSubscriptionTitlePresent(),
                "Telmex Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isTelmexSubscriptionMessagePresent(),
                "Telmex Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openTelmexWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(TELMEX_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61610", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Bradesco is displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_Bradesco() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_BRADESCO_BANK_BR_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isBradescoSubscriptionTitlePresent(),
                "Bradesco Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isBradescoSubscriptionMessagePresent(),
                "Bradesco Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openBradescoWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(BRADESCO_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61611", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Bradesco NEXT is displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_BradescoNext() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_BRADESCO_NEXT_BR_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isBradescoNextSubscriptionTitlePresent(),
                "Bradesco NEXT Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isBradescoNextSubscriptionMessagePresent(),
                "Bradesco NEXT Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openBradescoNextWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(BRADESCO_NEXT_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61612", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Telefonica Vivo is displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_TelefonicaVivo() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_VIVO_BR_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isTelefonicaVivoSubscriptionMessagePresent(),
                "Telefonica Vivo Subscription title was not displayed");
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
    @Test(description = "Verify that the correct description for Mercado Libre displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_MercadoLibre() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_MERCADOLIBRE_MX_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isMercadolibreSubscriptionTitlePresent(),
                "Mercado Libre Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isMercadoLibreSubscriptionMessagePresent(),
                "Mercado Libre Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openMercadoLibreWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(MERCADOLIBRE_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66499", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Cablevision is displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_Cablevision() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_TELECOM_AR_STANDAONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isCablevisionSubscriptionTitlePresent(),
                "Cablevision/Telecom Argentina SA Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isCablevisionSubscriptionMessagePresent(),
                "Cablevision/Telecom Argentina SA Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openCablevisionWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        //Use wait instead of instant check to account for multiple redirects in browser.
        disneyPlusAccountIOSPageBase.waitUntilWebviewUrlContains(CABLEVISION_URL);
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66502", "XMOBQA-61569"})
    @Test(description = "Verify that the correct description for Mercado Libre Brazil is displayed", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionDetails_MercadoLibreBrazil() {
        SoftAssert sa = new SoftAssert();
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_PARTNER_MERCADOLIBRE_BR_STANDALONE, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        sa.assertTrue(disneyPlusAccountIOSPageBase.isMercadolibreMonthlySubscriptionTitlePresent(),
                "Mercado Libre Brazil Subscription title was not displayed");
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isMercadoLibreBrazilSubscriptionMessagePresent(),
                "Mercado Libre Brazil Subscription message was not displayed");

        disneyPlusAccountIOSPageBase.openMercadoLibreBrazilWebview();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                "Browser webview did not open");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(MERCADOLIBRE_BR_URL),
                "Webview did not open to the expected url");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62564", "XMOBQA-62566", "XMOBQA-62568", "XMOBQA-62570"})
    @Test(description = "Verify the 'Unverified email badge is displayed in the More Menu and Account submenu", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyUnverifiedAccountFunctions() throws URISyntaxException, InterruptedException {
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
        getAccountApi().entitleAccount(externalAccount, DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE, "V1");
        getAccountApi().patchAccountVerified(externalAccount, false, PatchType.ACCOUNT);
        setAccount(externalAccount);

        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase disneyPlusMoreMenuIOSPageBase = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusOneTrustIOSPageBase oneTrustPage = initPage(DisneyPlusOneTrustIOSPageBase.class);

        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        if (homePage.getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.FOOTER_MANAGE_PREFERENCE.getText())).isPresent()) {
            sa.assertTrue(oneTrustPage.isOpened(), "One trust page did not open.");
            oneTrustPage.tapCloseButton();
        }
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);

        sa.assertTrue(disneyPlusMoreMenuIOSPageBase.isAccountUnverifiedBadgeDisplayed(),
                "XMOBQA-62564 - Unverified Account badge was not displayed");

        disneyPlusMoreMenuIOSPageBase.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);

        sa.assertTrue(disneyPlusAccountIOSPageBase.isVerifyAccountHeaderPresent(),
                "XMOBQA-62566 - Verify Account header was not displayed");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isVerifyAccountLinkPresent(),
                "XMOBQA-62566 - Verify Account link was not present");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isChangeLinkPresent(getAccount().getEmail()),
                "XMOBQA-62570 - Change Email link was not displayed");

        sa.assertFalse(disneyPlusAccountIOSPageBase.isChangeLinkActive(getAccount().getEmail()),
                "XMOBQA-62570 - Change Email link was not disabled");

        disneyPlusAccountIOSPageBase.clickVerifyAccountLink();

        sa.assertTrue(disneyPlusOneTimePasscodeIOSPageBase.isOpened(),
                "XMOBQA-62568 -  OTP Page was not opened");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61557", "XMOBQA-61559", "XMOBQA-61565", "XMOBQA-61561"})
    @Test(description = "Verify the UI elements for the Change Password screen from Account Settings", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void testChangePasswordUI() {
        SoftAssert sa = new SoftAssert();
        Date startTime = getEmailApi().getStartTime();
        setAccount(getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase accountPage = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangePasswordIOSPageBase changePasswordPage = new DisneyPlusChangePasswordIOSPageBase(getDriver());

        accountPage.clickChangePasswordCell();
        String otp = getEmailApi().getDisneyOTP(getAccount().getEmail(), startTime);

        sa.assertTrue(oneTimePasscodePage.isOpened(),
                "XMOBQA-61559 - OTP entry page was not opened");

        oneTimePasscodePage.enterOtpValue(otp);

        sa.assertTrue(changePasswordPage.isOpened(),
                "XMOBQA-61559 - 'Change Password' screen was not opened");

        sa.assertTrue(changePasswordPage.isHeadlineSubtitlePresent(),
                "XMOBQA-61559 - 'Change Password' subtitle was not displayed");

        sa.assertTrue(changePasswordPage.isLogoutAllDevicesTitlePresent(),
                "XMOBQA-61559 - 'Logout All Devices' title was not displayed");

        sa.assertTrue(changePasswordPage.isSaveBtnPresent(),
                "XMOBQA-61559 - 'Save' button was not displayed");

        sa.assertTrue(changePasswordPage.isCancelBtnPresent(),
                "XMOBQA-61559 - 'Cancel' button was not displayed");

        sa.assertTrue(changePasswordPage.isLogoutAllDevicesUnchecked(),
                "XMOBQA-61559 - 'Logout All Devices' was not unchecked by default");

        changePasswordPage.clickLogoutAllDevices();

        sa.assertTrue(changePasswordPage.isLogoutAllDevicesChecked(),
                "XMOBQA-61559 - 'Logout All Devices' was not checked");

        sa.assertTrue(changePasswordPage.isLogoutAllDevicesPasswordCopyDisplayed(),
                "XMOBQA-61559 - 'Logout All Devices' password text was not displayed");

        changePasswordPage.submitNewPasswordValue("invalid");

        sa.assertTrue(changePasswordPage.isInvalidPasswordErrorDisplayed(),
                "XMOBQA-61565 - 'Invalid Password' error was not displayed");

        changePasswordPage.clickCancelBtn();

        sa.assertTrue(accountPage.isOpened(),
                "XMOBQA-61561 - User was not directed back to 'Account Settings' after cancelling the password change");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61563"})
    @Test(description = "Verify the password save functionality flow without Logout checked", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void testChangePasswordWithoutLogout() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase accountPage = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangePasswordIOSPageBase changePasswordPage = new DisneyPlusChangePasswordIOSPageBase(getDriver());

        setAccount(getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        Date startTime = getEmailApi().getStartTime();
        accountPage.clickChangePasswordCell();
        String otp = getEmailApi().getDisneyOTP(getAccount().getEmail(), startTime);
        oneTimePasscodePage.enterOtpValue(otp);
        changePasswordPage.submitNewPasswordValue(NEW_PASSWORD);

        Assert.assertTrue(accountPage.isOpened(),
                "User was not directed back to 'Account Settings' after changing their password");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61599"})
    @Test(description = "Verify the password save functionality flow with Logout checked", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void testChangePasswordWithLogout() {
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase accountPage = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangePasswordIOSPageBase changePasswordPage = new DisneyPlusChangePasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());

        setAccount(getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        Date startTime = getEmailApi().getStartTime();
        accountPage.clickChangePasswordCell();
        String otp = getEmailApi().getDisneyOTP(getAccount().getEmail(), startTime);
        oneTimePasscodePage.enterOtpValue(otp);
        changePasswordPage.clickLogoutAllDevices();
        changePasswordPage.submitNewPasswordValue(NEW_PASSWORD);

        Assert.assertTrue(welcomeScreen.isOpened(),
                "User was logged out after changing their password");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61551", "XMOBQA-61553", "XMOBQA-61555"})
    @Test(description = "Verify the UI elements for the Change Password screen from Account Settings", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void testChangeEmailUI() {
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangeEmailIOSPageBase disneyPlusChangeEmailIOSPageBase = new DisneyPlusChangeEmailIOSPageBase(getDriver());
        SoftAssert sa = new SoftAssert();
        Date startTime = getEmailApi().getStartTime();
        setAccount(getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        disneyPlusAccountIOSPageBase.clickChangeLink(getAccount().getEmail());
        String otp = getEmailApi().getDisneyOTP(getAccount().getEmail(), startTime);

        Assert.assertTrue(disneyPlusOneTimePasscodeIOSPageBase.isOpened(),
                "XMOBQA-61553 - OTP entry page was not opened");

        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValueDismissKeys(otp);

        Assert.assertTrue(disneyPlusChangeEmailIOSPageBase.isOpened(),
                "XMOBQA-61551 - 'Change Email' screen was not opened");

        hideKeyboard();

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isHeadlineSubtitlePresent(),
                "XMOBQA-61551 - 'Change Email' subtitle was not displayed");

        sa.assertTrue(disneyPlusChangeEmailIOSPageBase.isCurrentEmailShown(getAccount().getEmail()),
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
    @Test(description = "Verify the user is returned to Welcome after submitting new Email with Logout checked", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void testChangeEmailWithoutLogout() {
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangeEmailIOSPageBase disneyPlusChangeEmailIOSPageBase = new DisneyPlusChangeEmailIOSPageBase(getDriver());

        Date startTime = getEmailApi().getStartTime();
        setAccount(getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        disneyPlusAccountIOSPageBase.clickChangeLink(getAccount().getEmail());
        String otp = getEmailApi().getDisneyOTP(getAccount().getEmail(), startTime);
        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValueDismissKeys(otp);
        String newEmail = generateGmailAccount();

        disneyPlusChangeEmailIOSPageBase.submitNewEmailAddress(newEmail);

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isOpened(),
                "User was not returned to Account Settings after submitting the new email");

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isChangeLinkPresent(newEmail),
                "The User's new email address was not displayed as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61601", "XMOBQA-61553"})
    @Test(description = "Verify the user is returned to Welcome after submitting new Email with Logout checked", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void testChangeEmailWithLogout() {
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusChangeEmailIOSPageBase disneyPlusChangeEmailIOSPageBase = new DisneyPlusChangeEmailIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusHomeIOSPageBase disneyPlusHomeIOSPageBase = new DisneyPlusHomeIOSPageBase(getDriver());

        Date startTime = getEmailApi().getStartTime();
        setAccount(getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        disneyPlusAccountIOSPageBase.clickChangeLink(getAccount().getEmail());
        String otp = getEmailApi().getDisneyOTP(getAccount().getEmail(), startTime);
        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValueDismissKeys(otp);

        Assert.assertTrue(disneyPlusChangeEmailIOSPageBase.isOpened(),
                "'Change Email' screen was not opened");

        hideKeyboard();

        disneyPlusChangeEmailIOSPageBase.clickLogoutAllDevices();
        String newEmail = generateGmailAccount();
        getAccount().setEmail(newEmail);

        disneyPlusChangeEmailIOSPageBase.submitNewEmailAddress(newEmail);

        Assert.assertTrue(disneyPlusWelcomeScreenIOSPageBase.isOpened(),
                "User was not logged out and returned to the Welcome screen after submitting the new email");

        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        Assert.assertTrue(disneyPlusHomeIOSPageBase.isOpened(),
                "User was not able to log in successfully with the new email");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61597"})
    @Test(description = "Verify the UI of the 'Logout of all devices'", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void testLogoutOfAllDevicesUI() {
        DisneyPlusAccountIOSPageBase accountPage = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusLogOutOfDevicesIOSPageBase logOutOfDevicesPage = new DisneyPlusLogOutOfDevicesIOSPageBase(getDriver());

        SoftAssert sa = new SoftAssert();
        setAppToAccountSettings();
        accountPage.clickLogOutOfAllDevices();

        Assert.assertTrue(logOutOfDevicesPage.isOpened(),
                "'Log out of all accounts' screen did not open");

        sa.assertTrue(logOutOfDevicesPage.isSubtitleDisplayed(),
                "Subtitle was not displayed");

        sa.assertTrue(logOutOfDevicesPage.isPasswordTextEntryPresent(),
                "Password entry field was not displayed");

        sa.assertTrue(logOutOfDevicesPage.isForgotPasswordLinkDisplayed(),
                "'Forgot Password?' link was not displayed");

        sa.assertTrue(logOutOfDevicesPage.isPrimaryButtonPresent(),
                "'Log Out' button was not displayed");

        sa.assertAll();
    }

    //TODO: Refactor to use 2 drivers to cover XMOBQA-61603
    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61599"})
    @Test(description = "Verify the UI of the 'Logout of all devices'", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void testLogoutOfAllDevicesForgotPasswordFunctions() {
        SoftAssert sa = new SoftAssert();
        Date startTime = getEmailApi().getStartTime();
        DisneyPlusOneTimePasscodeIOSPageBase oneTimePasscodePage = new DisneyPlusOneTimePasscodeIOSPageBase(getDriver());
        DisneyPlusAccountIOSPageBase accountPage = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreenPage = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusLogOutOfDevicesIOSPageBase logOutOfDevicePage = new DisneyPlusLogOutOfDevicesIOSPageBase(getDriver());
        DisneyPlusChangePasswordIOSPageBase changePasswordPage = new DisneyPlusChangePasswordIOSPageBase(getDriver());

        setAccount(getAccountApi().createAccountForOTP(getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        accountPage.clickLogOutOfAllDevices();
        logOutOfDevicePage.clickForgotPasswordLink();
        sa.assertTrue(oneTimePasscodePage.isOpened(),
                "OTP Page was not opened");

        String otp = getEmailApi().getDisneyOTP(getAccount().getEmail(), startTime);
        oneTimePasscodePage.enterOtpValue(otp);
        sa.assertTrue(changePasswordPage.isOpened(),
                "Change Password screen did not open after submitting OTP");

        changePasswordPage.enterNewPasswordValue(NEW_PASSWORD);
        changePasswordPage.clickLogoutAllDevices();
        changePasswordPage.clickSaveBtn();
        sa.assertTrue(changePasswordPage.isLogOutOfThisDeviceMessagePresent(),
                "`You're now being logged out of this device` message is not present");
        sa.assertTrue(welcomeScreenPage.isOpened(),
                "User was not returned to the Welcome screen upon logout");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61604"})
    @Test(description = "Verify Subscription section header displays correctly", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifySubscriptionsSectionHeader() throws JSONException, URISyntaxException {
        SoftAssert sa = new SoftAssert();
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        sa.assertTrue(disneyPlusAccountIOSPageBase.isSingleSubHeaderPresent(),
                "Single Subscription header text was not displayed");

        getAccountApi().entitleAccount(getAccount(), DisneySkuParameters.DISNEY_IAP_ROKU_YEARLY, SUBSCRIPTION_V1);
        logout();
        setAppToAccountSettings();

        sa.assertTrue(disneyPlusAccountIOSPageBase.isStackedSubHeaderPresent(),
                "Stacked Subscription header text was not displayed");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61607"})
    @Test(description = "Verify Monthly to Annual option is not present for Amazon Monthly subscribers", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyAccountMonthlyToAnnualDisplays_Amazon() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_AMAZON_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertFalse(disneyPlusAccountIOSPageBase.areSwitchToAnnualElementsDisplayed(),
                "Switch to Annual description and/or CTA was displayed unexpectedly");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-61607"})
    @Test(description = "Verify Monthly to Annual option is not present for Roku Monthly subscribers", groups = {"More Menu", TestGroup.PRE_CONFIGURATION})
    public void verifyAccountMonthlyToAnnualDisplays_Roku() {
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_ROKU_MONTHLY, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));
        setAppToAccountSettings();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());

        Assert.assertFalse(disneyPlusAccountIOSPageBase.areSwitchToAnnualElementsDisplayed(),
                "Switch to Annual description and/or CTA was displayed unexpectedly");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73683"})
    @Test(description = "Verify Direct Paused Billing display and navigation", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, dataProvider = "disneyPlanTypes", enabled = false)
    public void verifyPausedSubscription_VerizonStandalone(String planType) {
        SoftAssert sa = new SoftAssert();
        DisneyEntitlement directBillingEntitlement = new DisneyEntitlement(getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), planType), SUBSCRIPTION_V1);
        List<DisneyEntitlement> disneyEntitlements = Arrays.asList(directBillingEntitlement, new DisneyEntitlement(getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE), SUBSCRIPTION_V1));
        CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().entitlements(disneyEntitlements).country(getLocalizationUtils().getLocale()).language(getLocalizationUtils().getUserLanguage()).build();
        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));
        DisneyPlusPaywallIOSPageBase.PlanType planName = directBillingEntitlement.getOffer().getPeriod().contains("MONTH") ? DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY: DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY;
        performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE, VERIZON_URL, planName, sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73684"})
    @Test(description = "Verify Direct Paused Billing display and navigation", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, dataProvider = "disneyPlanTypes", enabled = false)
    public void verifyPausedSubscription_Canal(String planType) {
        SoftAssert sa = new SoftAssert();
        DisneyEntitlement directBillingEntitlement = new DisneyEntitlement(getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), planType), SUBSCRIPTION_V1);
            List<DisneyEntitlement> disneyEntitlement = Arrays.asList(directBillingEntitlement, new DisneyEntitlement(getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE), SUBSCRIPTION_V3));
            CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().entitlements(disneyEntitlement).country(getLocalizationUtils().getLocale()).language(getLocalizationUtils().getUserLanguage()).build();
            setAccount(getAccountApi().createAccount(createDisneyAccountRequest));
            DisneyPlusPaywallIOSPageBase.PlanType planName = directBillingEntitlement.getOffer().getPeriod().contains("MONTH") ? DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY: DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY;
            performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE, CANAL_URL, planName, sa);
            sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73685"})
    @Test(description = "Verify Direct Paused Billing display and navigation", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, dataProvider = "disneyPlanTypes", enabled = false)
    public void verifyPausedSubscription_O2(String planType) {
        SoftAssert sa = new SoftAssert();
        DisneyEntitlement directBillingEntitlement = new DisneyEntitlement(getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), planType), SUBSCRIPTION_V1);
            List<DisneyEntitlement> disneyEntitlements = Arrays.asList(directBillingEntitlement, new DisneyEntitlement(getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE), SUBSCRIPTION_V1));
            CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().entitlements(disneyEntitlements).country(getLocalizationUtils().getLocale()).language(getLocalizationUtils().getUserLanguage()).build();
            setAccount(getAccountApi().createAccount(createDisneyAccountRequest));
        DisneyPlusPaywallIOSPageBase.PlanType planName = directBillingEntitlement.getOffer().getPeriod().contains("MONTH") ? DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY: DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY;
        performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE, O2_URL, planName, sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62826"})
    @Test(description = "Verify Direct Paused Billing display and navigation", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, dataProvider = "disneyPlanTypes", enabled = false)
    public void verifyPausedSubscription_TelMex(String planType) {
        SoftAssert sa = new SoftAssert();
        DisneyEntitlement  directBillingEntitlement = new DisneyEntitlement(getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), planType), SUBSCRIPTION_V1);
        List<DisneyEntitlement> disneyEntitlements = Arrays.asList(directBillingEntitlement, new DisneyEntitlement(getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_PARTNER_TELMEX_MX_STANDALONE), SUBSCRIPTION_V3));
        CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().entitlements(disneyEntitlements).country(getLocalizationUtils().getLocale()).language(getLocalizationUtils().getUserLanguage()).build();
        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));
        DisneyPlusPaywallIOSPageBase.PlanType planName = directBillingEntitlement.getOffer().getPeriod().contains("MONTH") ? DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY: DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY;
        performPausedEntitlementCheck(DisneySkuParameters.DISNEY_PARTNER_TELMEX_MX_STANDALONE, TELMEX_URL, planName, sa);
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62827", "XMOBQA-69488"})
    @Test(description = "Verify an unpaused direct billing subscription updates the Subscriptions list correctly", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyUnpausedSubscription() throws JSONException, URISyntaxException{
        SoftAssert sa = new SoftAssert();
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        List<DisneyEntitlement> disneyEntitlements = Arrays.asList(
                new DisneyEntitlement(getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), YEARLY), SUBSCRIPTION_V1),
                new DisneyEntitlement(getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_PARTNER_TELMEX_MX_STANDALONE), SUBSCRIPTION_V3));
        CreateDisneyAccountRequest createDisneyAccountRequest = CreateDisneyAccountRequest.builder().entitlements(disneyEntitlements).country(getLocalizationUtils().getLocale()).language(getLocalizationUtils().getUserLanguage()).build();
        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));
        disneyPlusAccountIOSPageBase.keepSessionAlive(1, welcomePage.getSignupButton());
        setAppToAccountSettings();

        Assert.assertTrue(disneyPlusAccountIOSPageBase.isStackedSubHeaderPresent()
                        && disneyPlusAccountIOSPageBase.isDirectBillingPausedSubscriptionDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY),
                "Required 'Paused Subscription' state was not applied to the account. Unpaused entitlement check will be invalid.");

        logout();

        getAccountApi().revokeSkuV3(getAccount(), disneyEntitlements.get(1).getOffer(), CancellationReasons.VOLUNTARY_CANCEL);

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
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyIAPBillingHoldWithPartnerSub() {
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
                    .language(getLocalizationUtils().getUserLanguage())
                    .country(getLocalizationUtils().getLocale()).build();

            createDisneyAccountRequest.addSku(iap);
            createDisneyAccountRequest.setOrderSettings(billingOrder);
            getAccount().getOrderSettings().clear();

            try {
                getAccountApi().entitleAccount(
                        getAccount(),
                        getAccountApi().fetchOffer(provider),
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
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyBillingHoldWithPartnerSub_VerizonStandalone() throws JSONException, URISyntaxException {
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());

        DisneyAccount monthly = getAccountApi().createAccountWithBillingHold(
                getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), MONTHLY).getOfferId(),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        monthly.getOrderSettings().clear();
        getAccountApi().entitleAccount(monthly, getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE), SUBSCRIPTION_V1);
        setAccount(monthly);

       // sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE, VERIZON_URL));

        DisneyAccount yearly = getAccountApi().createAccountWithBillingHold(
                getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), YEARLY).getOfferId(),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        yearly.getOrderSettings().clear();
        getAccountApi().entitleAccount(yearly, getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE), SUBSCRIPTION_V1);
        setAccount(yearly);

        //sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE, VERIZON_URL));

        sa.get().assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66503"})
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyBillingHoldWithPartnerSub_VerizonBundle() throws JSONException, URISyntaxException {
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());

        DisneyAccount monthly = getAccountApi().createAccountWithBillingHold(
                getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), MONTHLY).getOfferId(),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        monthly.getOrderSettings().clear();
        getAccountApi().entitleAccount(monthly, getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE), SUBSCRIPTION_V1);
        setAccount(monthly);

       // sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE, VERIZON_URL));

        DisneyAccount yearly = getAccountApi().createAccountWithBillingHold(
                getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), YEARLY).getOfferId(),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        yearly.getOrderSettings().clear();
        getAccountApi().entitleAccount(yearly, getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE), SUBSCRIPTION_V1);
        setAccount(yearly);

        //sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE, VERIZON_URL));

        sa.get().assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66503"})
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyBillingHoldWithPartnerSub_Canal() throws JSONException, URISyntaxException {
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());
        List<DisneyOrder> billingOrder = new LinkedList<>();
        billingOrder.add(DisneyOrder.SET_BILLING_HOLD);
        DisneyAccount monthly = getAccountApi().createAccountWithBillingHold(
                getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), MONTHLY).getOfferId(),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        monthly.getOrderSettings().clear();
        getAccountApi().entitleAccount(monthly, getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE), SUBSCRIPTION_V1);
        setAccount(monthly);

       // sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE, CANAL_URL));

        DisneyAccount yearly = getAccountApi().createAccountWithBillingHold(
                getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), YEARLY).getOfferId(),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        yearly.getOrderSettings().clear();
        getAccountApi().entitleAccount(yearly, getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE), SUBSCRIPTION_V1);
        setAccount(yearly);

        //sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_CANAL_BUNDLE, CANAL_URL));

        sa.get().assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66503"})
    @Test(description = "User in IAP D+ Hold who gets Partner Subscription does not see Hold UX", groups = {"More Menu", TestGroup.PRE_CONFIGURATION}, enabled = false)
    public void verifyBillingHoldWithPartnerSub_O2() throws JSONException, URISyntaxException {
        AtomicReference<SoftAssert> sa = new AtomicReference<>(new SoftAssert());

        DisneyAccount monthly = getAccountApi().createAccountWithBillingHold(
                getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), MONTHLY).getOfferId(),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        monthly.getOrderSettings().clear();
        getAccountApi().entitleAccount(monthly, getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE), SUBSCRIPTION_V1);
        setAccount(monthly);

        //sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE, O2_URL));

        DisneyAccount yearly = getAccountApi().createAccountWithBillingHold(
                getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), YEARLY).getOfferId(),
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2_ORDER);
        yearly.getOrderSettings().clear();
        getAccountApi().entitleAccount(yearly, getAccountApi().fetchOffer(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE), SUBSCRIPTION_V1);
        setAccount(yearly);

       // sa.set(performPausedEntitlementCheck(DisneySkuParameters.DISNEY_EXTERNAL_O2_BUNDLE, O2_URL));

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
    private SoftAssert performPausedEntitlementCheck(DisneySkuParameters sku, String url, DisneyPlusPaywallIOSPageBase.PlanType planName, SoftAssert sa) {
        DisneyPlusAccountIOSPageBase disneyPlusAccountIOSPageBase = new DisneyPlusAccountIOSPageBase(getDriver());
        setAppToAccountSettings();
        if (!disneyPlusAccountIOSPageBase.isStackedSubHeaderPresent()) {
            logout();
            setAppToAccountSettings();
        }
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isStackedSubHeaderPresent(),
                "Stacked Subscription header text was not displayed. Unable to verify Paused status.");

        sa.assertTrue(disneyPlusAccountIOSPageBase.isDirectBillingPausedSubscriptionDisplayed(planName),
                "Direct Billing 'Paused' subscription was not displayed");

        disneyPlusAccountIOSPageBase.openBillingProvider(sku);
        pause(4);
        Assert.assertTrue(disneyPlusAccountIOSPageBase.isWebviewOpen(),
                String.format("Browser webview did not open on navigation for the following SKU: %s", sku));
        pause(2);
        sa.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(url),
                String.format("Webview did not open to the expected URL: %s", url));

        relaunch();
        disneyPlusAccountIOSPageBase.clickPausedDirectBillingContainer(planName);
        sa.assertTrue(disneyPlusAccountIOSPageBase.getWebviewUrl().contains(DISNEY_URL),
                String.format("Webview did not open to the expected URL: %s", DISNEY_URL));
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
