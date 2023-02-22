package com.disney.qa.tests.disney.web.localization;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.web.SeleniumUtils;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBaseNavPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class DisneyPlusLocalizationTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<>();

    private static final String locale = R.CONFIG.get("locale");
    private static final String completePurchase = "complete-purchase";
    private static final boolean isBundle = false;

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod(alwaysRun = true)
    public void testTeardown(ITestResult result) throws IOException {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Finish Later", groups = {"AD", "AG", "AI", "AR", "AT", "AU", "AW", "BB", "BE", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CH", "CL", "CO", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "ES", "FI", "FK", "FR", "GB", "GD", "GF", "GG", "GL", "GP", "GR", "GS", "GT", "GY", "HK", "HN", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KR", "KN", "KY", "LC", "LI", "LU", "MC", "MF", "MQ", "MS", "MU", "MX", "NC", "NI", "NL", "NO", "NZ", "PA", "PE", "PL", "PR", "PT", "PY", "RE", "RO", "SE", "SG", "SK", "SR", "SV", "TC", "TR", "TT", "TW", "US", "UY", "VC", "VE", "VG", "VI", "WF", "YT"})
    public void finishLater() throws URISyntaxException, IOException, JSONException {
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.captureFullScreenshotArtifact("Home");

        commercePage.openURL(commercePage.getHomeUrl().concat("/login"));

        if (!DisneyPlusBasePage.BRANCH.equalsIgnoreCase("LOCAL")) {
            commercePage.waitFor(commercePage.getSignUpLoginContinueBtn());
        }
        commercePage.typeDplusBaseEmailFieldId(account.getEmail());
        commercePage.clickSignUpLoginContinueBtn();
        commercePage.typeDplusBasePasswordFieldId(account.getUserPass());
        commercePage.clickDplusBaseLoginFlowBtn();
        commercePage.cookieCatcherAfterLogin();
        commercePage.clickMatureContentOnboardingBannerButton(locale);
        commercePage.isCompletePurchaseCtaPresent();

        Screenshot.capture(getDriver(), "Complete_Subscription", true);

        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);
        commercePage.isCreditRadioIconByIdPresent();

        Screenshot.capture(getDriver(), "Billing", true);

        commercePage.navigateBack();
        commercePage.isModalPrimaryBtnPresent();

        Screenshot.capture(getDriver(), "Finish_Later", true);
    }

    @Test(description = "Restart Subscription", groups = {"AD", "AG", "AI", "AR", "AT", "AU", "AW", "BB", "BE", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CH", "CL", "CO", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "ES", "FI", "FK", "FR", "GB", "GD", "GF", "GG", "GL", "GP", "GR", "GS", "GT", "GY", "HK", "HN", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KR", "KN", "KY", "LC", "LI", "LU", "MC", "MF", "MQ", "MS", "MU", "MX", "NC", "NI", "NL", "NO", "NZ", "PA", "PE", "PL", "PR", "PT", "PY", "RE", "RO", "SE", "SG", "SK", "SR", "SV", "TC", "TR", "TT", "TW", "US", "UY", "VC", "VE", "VG", "VI", "WF", "YT"})
    public void restart() throws URISyntaxException, IOException, JSONException {
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        setOverride(accountApi.overrideLocations(account, locale));
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isCompletePurchaseCtaPresent();
        commercePage.captureFullScreenshotArtifact("Restart_Subscription");
    }

    @Test(description = "Billing Page", groups = {"AD", "AG", "AI", "AR", "AT", "AU", "AW", "BB", "BE", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CH", "CL", "CO", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "ES", "FI", "FK", "FR", "GB", "GD", "GF", "GG", "GL", "GP", "GR", "GS", "GT", "GY", "HK", "HN", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KR", "KN", "KY", "LC", "LI", "LU", "MC", "MF", "MQ", "MS", "MU", "MX", "NC", "NI", "NL", "NO", "NZ", "PA", "PE", "PL", "PR", "PT", "PY", "RE", "RO", "SE", "SG", "SK", "SR", "SV", "TC", "TR", "TT", "TW", "US", "UY", "VC", "VE", "VG", "VI", "WF", "YT"})
    public void billingPage() throws URISyntaxException, IOException, JSONException {
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);
        boolean isRedemption = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));


        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));

        commercePage.isBillingPagePresent();
        commercePage.captureFullScreenshotArtifact("Billing_Page_Monthly");

        commercePage.clickCvvInfo();
        commercePage.isCscPopoverContainerPresent();
        commercePage.isCvvInfoPresent();
        pause(3);
        commercePage.captureFullScreenshotArtifact("CSC_Popover");

        commercePage.clickLearnMore();
        commercePage.isCvvInfoPresent();

        commercePage.captureFullScreenshotArtifact("Learn_More");

        if(!locale.equalsIgnoreCase("US")) {
            commercePage.clickYearlyRadioBtnById();
            commercePage.isYearlyRadioBtnByIdChecked();
            commercePage.captureFullScreenshotArtifact("Billing_Page_Annual");
        }

        commercePage.clickCreditSubmitBtnById();
        commercePage.isInputErrorLabelPresent();

        commercePage.captureFullScreenshotArtifact("Billing_Page_Input_Errors");

        commercePage.clickPayPalRadioIconById(locale);
        commercePage.isPayPalRadioIconByTestIdChecked();

        commercePage.captureFullScreenshotArtifact("Paypal");

        commercePage.navigateBack();
        commercePage.isModalPrimaryBtnPresent();

        commercePage.captureFullScreenshotArtifact("Finish_Later");

        commercePage.clickModalSecondaryBtn();
        commercePage.clickCreditCardRadioIconById();
        commercePage.clickCreditRadioIconAria();
        commercePage.clickCreditRadioIconAria();

        String creditCard = commercePage.getInvalidCreditCard(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
                commercePage.getCreditCardName(locale),
                creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                redemptionPage.searchAndReturnZipTaxId(locale),
                locale
        );

        commercePage.captureFullScreenshotArtifact("Billing_Page_Invalid_Credit_Card");

        commercePage.clickCreditSubmitBtnById();

        if (commercePage.isReviewSubscriptionPagePresent()) {
            pause(10); //Allows time for javascript to finish loading for Order & Pay Button
            commercePage.clickCreditSubmitBtnById();
        }

        commercePage.isPaymentBlockedBtnIdPresent();
        commercePage.captureFullScreenshotArtifact("Billing_Page_Unable_To_Subscribe");
        commercePage.clickPaymentBlockedButton();

        if (locale.equalsIgnoreCase("US")) {
            creditCard = "5460 8357 8210 5440";
        } else {
            creditCard = "5364 8155 8780 4709";
        }

        redemptionPage.enterPurchaseFlowBillingInfo(
                redemptionPage.getCardHolderErrorName(locale),
                creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                redemptionPage.searchAndReturnZipTaxId(locale),
                locale
        );

        commercePage.clickCreditSubmitBtnById();
        commercePage.isErrorProcessingPresent();

        commercePage.captureFullScreenshotArtifact("Billing_Page_Error_Processing");
    }

    @Test(description = "Manage Preferences", groups = {"AD", "AG", "AI", "AR", "AT", "AU", "AW", "BB", "BE", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CH", "CL", "CO", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "ES", "FI", "FK", "FR", "GB", "GD", "GF", "GG", "GL", "GP", "GR", "GS", "GT", "GY", "HK", "HN", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KR", "KN", "KY", "LC", "LI", "LU", "MC", "MF", "MQ", "MS", "MU", "MX", "NC", "NI", "NL", "NO", "NZ", "PA", "PE", "PL", "PR", "PT", "PY", "RE", "RO", "SE", "SG", "SK", "SR", "SV", "TC", "TR", "TT", "TW", "US", "UY", "VC", "VE", "VG", "VI", "WF", "YT"})
    public void managePreferences() throws URISyntaxException, IOException, JSONException {
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createEntitledAccount(locale, language);
        setOverride(accountApi.overrideLocations(account, locale));
        disneyAccount.set(account);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        commercePage.captureFullScreenshotArtifact("Start_Streaming");

        DisneyPlusBaseNavPage navPage = new DisneyPlusBaseNavPage(getDriver());

        navPage.clickNavWatchListLocalizationBtn();
        userPage.isFooterIdPresent();

        commercePage.captureFullScreenshotArtifact("Footer");

        userPage.clickPrivacyPolicy();

        //Need to pause because no data-testid attributes
        pause(5);

        commercePage.captureFullScreenshotArtifact("Legal");
    }

    @Test(description = "Lapsed Account", groups = {"AD", "AG", "AI", "AR", "AT", "AU", "AW", "BB", "BE", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CH", "CL", "CO", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "ES", "FI", "FK", "FR", "GB", "GD", "GF", "GG", "GL", "GP", "GR", "GS", "GT", "GY", "HK", "HN", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KR", "KN", "KY", "LC", "LI", "LU", "MC", "MF", "MQ", "MS", "MU", "MX", "NC", "NI", "NL", "NO", "NZ", "PA", "PE", "PL", "PR", "PT", "PY", "RE", "RO", "SE", "SG", "SK", "SR", "SV", "TC", "TR", "TT", "TW", "US", "UY", "VC", "VE", "VG", "VI", "WF", "YT"})
    public void lapsedAccount() throws URISyntaxException, IOException, JSONException {
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        String expectedUrl = "restart-subscription";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createExpiredAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        setOverride(accountApi.overrideLocations(account, locale));
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.isCompletePurchaseCtaPresent();

        commercePage.captureFullScreenshotArtifact("Restart_Subscription");

        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, expectedUrl, isBundle);

        if (commercePage.getDplusSubscriberAgreementContinue().isElementPresent()) {
            commercePage.captureFullScreenshotArtifact("Subscriber_Agreement");
            commercePage.clickSubscriberAgreementContinueCta(locale);
        }

        commercePage.isBillingPagePresent();

        commercePage.captureFullScreenshotArtifact("Payment_1");

        commercePage.clickChangePaymentCtaIfPresent();

        if(!commercePage.isCreditCardRadioBtnByIdChecked()) {
            if(commercePage.isSecondaryPaymentRadioBtnPresent())
                commercePage.clickSecondaryPaymentRadioBtn();
            commercePage.isMonthlyRadioBtnByIdPresent();
            commercePage.captureFullScreenshotArtifact("Payment_2");
        }

        commercePage.navigateBack();
        commercePage.isModalPrimaryBtnPresent();

        commercePage.captureFullScreenshotArtifact("Finish_Later");

        userPage.isCompletePurchaseCtaPresent();

        commercePage.captureFullScreenshotArtifact("Restart_Subscription_2");
    }

    @Test(description = "BG Image Check", groups = {"AD", "AG", "AI", "AR", "AT", "AU", "AW", "BB", "BE", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CH", "CL", "CO", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "ES", "FI", "FK", "FR", "GB", "GD", "GF", "GG", "GL", "GP", "GR", "GS", "GT", "GY", "HK", "HN", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KR", "KN", "KY", "LC", "LI", "LU", "MC", "MF", "MQ", "MS", "MU", "MX", "NC", "NI", "NL", "NO", "NZ", "PA", "PE", "PL", "PR", "PT", "PY", "RE", "RO", "SE", "SG", "SK", "SR", "SV", "TC", "TR", "TT", "TW", "US", "UY", "VC", "VE", "VG", "VI", "WF", "YT"})
    public void bgImage() throws MalformedURLException, URISyntaxException {
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        DisneyAccountApi accountApi = new DisneyAccountApi(WEB_PLATFORM, getEnvironmentOnly(), getProjectApiName());
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        disneyAccount.set(account);
        commercePage.isFooterPresent();

        commercePage.captureFullScreenshotArtifact("Welcome_Page");

        SeleniumUtils util = new SeleniumUtils(getDriver());
        util.scrolltoTop();

        commercePage.clickDplusBaseLoginBtn();
        commercePage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        commercePage.clickSignUpLoginContinueBtn();
        commercePage.getModalPrimaryBtn().clickIfPresent(5);
        commercePage.getSignUpLoginContinueBtn().clickIfPresent(5);
        commercePage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());
        commercePage.clickDplusBaseLoginFlowBtn();

        commercePage.isCompletePurchaseCtaPresent();
        commercePage.captureFullScreenshotArtifact("Welcome_Back");

        commercePage.getDriver().manage().deleteAllCookies();
        restartDriver();

        commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyAccount lapsedAccount = accountApi.createExpiredAccount(YEARLY,locale, language, SUB_VERSION_V1);
        disneyAccount.set(lapsedAccount);

        commercePage.clickDplusBaseLoginBtn();
        commercePage.typeDplusBaseEmailFieldId(disneyAccount.get().getEmail());
        commercePage.clickSignUpLoginContinueBtn();
        commercePage.getModalPrimaryBtn().clickIfPresent(5);
        commercePage.getSignUpLoginContinueBtn().clickIfPresent(5);
        commercePage.typeDplusBasePasswordFieldId(disneyAccount.get().getUserPass());
        commercePage.clickDplusBaseLoginFlowBtn();

        commercePage.isCompletePurchaseCtaPresent();
        commercePage.captureFullScreenshotArtifact("Welcome_Back");
    }

    @Test(description = "Landing Page - Splash", groups = {"AD", "AG", "AI", "AR", "AT", "AU", "AW", "BB", "BE", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CH", "CL", "CO", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "ES", "FI", "FK", "FR", "GB", "GD", "GF", "GG", "GL", "GP", "GR", "GS", "GT", "GY", "HK", "HN", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KR", "KN", "KY", "LC", "LI", "LU", "MC", "MF", "MQ", "MS", "MU", "MX", "NC", "NI", "NL", "NO", "NZ", "PA", "PE", "PL", "PR", "PT", "PY", "RE", "RO", "SE", "SG", "SK", "SR", "SV", "TC", "TR", "TT", "TW", "US", "UY", "VC", "VE", "VG", "VI", "WF", "YT"})
    public void landingPageSplash() throws MalformedURLException, URISyntaxException{
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        String screenshotName = "";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.navigateToPreviewStage();

        String screenshot = String.format("WEBS1_Landing_Page_Splash" + "_%s", screenshotName, R.CONFIG.get("lang"));
        commercePage.captureFullScreenshotArtifact(screenshot);

    }

    @Test(description = "Landing Page - MLP", groups = {"AD", "AG", "AI", "AR", "AT", "AU", "AW", "BB", "BE", "BL", "BM", "BO", "BQ", "BR", "BS", "BZ", "CA", "CH", "CL", "CO", "CR", "CW", "CZ", "DE", "DK", "DM", "DO", "EC", "ES", "FI", "FK", "FR", "GB", "GD", "GF", "GG", "GL", "GP", "GR", "GS", "GT", "GY", "HK", "HN", "HT", "HU", "IE", "IM", "IS", "IT", "JE", "JM", "JP", "KR", "KN", "KY", "LC", "LI", "LU", "MC", "MF", "MQ", "MS", "MU", "MX", "NC", "NI", "NL", "NO", "NZ", "PA", "PE", "PL", "PR", "PT", "PY", "RE", "RO", "SE", "SG", "SK", "SR", "SV", "TC", "TR", "TT", "TW", "US", "UY", "VC", "VE", "VG", "VI", "WF", "YT"})
    public void landingPageMlp() throws MalformedURLException, URISyntaxException{
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        String screenshotName = "";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        String screenshot = String.format("WEBS1_Landing_Page_MLP" + "_%s", screenshotName, R.CONFIG.get("lang"));
        commercePage.captureFullScreenshotArtifact(screenshot);

    }
}
