package com.disney.qa.tests.disney.web.commerce.licenseplate;


import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceLicensePlateUnauthenticated extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();

    private static final String startUrl ="/start";
    private static final String beginUrl = "/begin";

    @BeforeMethod(alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "New User", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void newUser() throws IOException, JSONException, URISyntaxException, IOException, InterruptedException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        DisneyAccount account = new DisneyAccount();
        String email = DisneyApiCommon.getUniqueEmail();
        String password = DisneyApiCommon.getCommonPass();
        account.setEmail(email);
        account.setUserPass(password);
        disneyAccount.set(account);
        String env = DisneyPlusBasePage.getBackendService(DisneyPlusBasePage.ENVIRONMENT);

        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat(startUrl));

        commercePage.generateAndEnterLicensePlateInputField(account);
        commercePage.clickLicensePlateSubmit();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        userPage.getDssLoginWrapperId().isElementPresent();
        commercePage.assertUrlContains(sa, "sign-up");
        setOverride(userPage.signUpEmailPassword(locale, false, true, email, password, language, true, disneyAccount, proxy));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.isBillingCardNameIdPresent();

        commercePage.assertUrlContains(sa, "billing");

        if (!env.equals("PROD")) {
                commercePage.override3DS2Data(proxy, locale);
                String creditCard = commercePage.getCreditCardNumber(env, locale, false);

                redemptionPage.enterPurchaseFlowBillingInfo(
                        commercePage.getCreditCardName(locale),
                        creditCard,
                        countryData.searchAndReturnCountryData(locale, CODE, EXP),
                        countryData.searchAndReturnCountryData(locale, CODE, CVV),
                        redemptionPage.searchAndReturnZipTaxId(locale),
                        locale
                );

                commercePage.clickCreditSubmitBtnById();
                commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
                commercePage.clickChallengeFormSubmitBtn(locale);

                sa.assertTrue(commercePage.isDplusBaseFlashNotificationContainerPresent(),
                        "Flash Notification Container not present after transaction");

        }

        sa.assertAll();
    }

    @Test(description = "Existing Entitled User", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void existingEntitled() throws IOException, JSONException, URISyntaxException, InterruptedException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat(beginUrl));
        commercePage.generateAndEnterLicensePlateInputField(account);
        commercePage.clickLicensePlateSubmit();

        commercePage.isDplusBaseEmailFieldIdPresent();
        commercePage.assertUrlContains(sa, "login");

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, false, true, account.getEmail(), account.getUserPass(), language, true, disneyAccount, proxy));

        sa.assertTrue(commercePage.isDplusBaseFlashNotificationContainerPresent(),
                "Flash Notification Container not present after transaction");

        sa.assertAll();
    }

    @Test(description = "Existing User w/ Expired Subscription", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void existingExpired() throws IOException, JSONException, URISyntaxException, IOException, InterruptedException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String type = "restart-subscription";
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createExpiredAccount(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat(beginUrl));
        commercePage.generateAndEnterLicensePlateInputField(disneyAccount.get());
        commercePage.clickLicensePlateSubmit();

        commercePage.isDplusBaseEmailFieldIdPresent();
        commercePage.assertUrlContains(sa, "login");

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, false, true, account.getEmail(), account.getUserPass(), language, true, disneyAccount, proxy));

        commercePage.isRegularSignUpBtnPresent();
        commercePage.assertUrlContains(sa, type);
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, type, isBundle);
        commercePage.override3DS2Data(proxy, locale);
        commercePage.clickChangePaymentCtaIfPresent();

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
                commercePage.getCreditCardName(locale),
                creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                redemptionPage.searchAndReturnZipTaxId(locale),
                locale
        );

        commercePage.assertUrlContains(sa, "billing");

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        sa.assertTrue(commercePage.isDplusBaseFlashNotificationContainerPresent(),
                "Flash Notification Container not present after transaction");

        sa.assertAll();
    }

    @Test(description = "Existing User w/ Account Hold", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void existingAccountHold() throws IOException, JSONException, URISyntaxException, IOException, InterruptedException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccountWithBillingHold(YEARLY, locale, language, SUB_VERSION_V2_ORDER);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat(beginUrl));
        commercePage.generateAndEnterLicensePlateInputField(disneyAccount.get());
        commercePage.clickLicensePlateSubmit();

        commercePage.isDplusBaseEmailFieldIdPresent();
        commercePage.assertUrlContains(sa, "login");

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, false, true, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, true, disneyAccount, proxy));

        commercePage.isAccountHoldPaymentCtaPresent();
        commercePage.assertUrlContains(sa, "account-hold");

        commercePage.clickAccountHoldUpdatePaymentCta();
        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.isBillingCardNameIdPresent();

        commercePage.assertUrlContains(sa, "change-payment-info");
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        redemptionPage.enterPurchaseFlowBillingInfo(
                commercePage.getCreditCardName(locale),
                creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                redemptionPage.searchAndReturnZipTaxId(locale),
                locale
        );

        redemptionPage.clickCreditSubmitBtnById();
        commercePage.clickChallengeFormSubmitBtn(locale);

        sa.assertTrue(commercePage.isDplusBaseFlashNotificationContainerPresent(),
                "Flash Notification Container not present after transaction");

        sa.assertAll();
    }

    @Test(description = "Existing Abandoned User", groups = {"US", "MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void existingAbandonedUser() throws JSONException, URISyntaxException, IOException, InterruptedException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String pageType = "complete-purchase";
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getHomeUrl().concat(startUrl));
        commercePage.generateAndEnterLicensePlateInputField(account);
        commercePage.clickLicensePlateSubmit();

        commercePage.isDplusBaseEmailFieldIdPresent();
        commercePage.assertUrlContains(sa, "sign-up");

        commercePage.enterLoginFlow(account.getEmail(), account.getUserPass());

        if (!locale.equalsIgnoreCase(WebConstant.US)) {
            commercePage.isCompletePurchaseCtaPresent();
            commercePage.assertUrlContains(sa, pageType);
            commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, pageType, isBundle);
        }
        commercePage.override3DS2Data(proxy, locale);

        String creditCard = commercePage.getCreditCardNumber(DisneyPlusBasePage.getEnvironmentType(DisneyPlusBasePage.ENVIRONMENT), locale, false);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterPurchaseFlowBillingInfo(
                commercePage.getCreditCardName(locale),
                creditCard,
                countryData.searchAndReturnCountryData(locale, CODE, EXP),
                countryData.searchAndReturnCountryData(locale, CODE, CVV),
                redemptionPage.searchAndReturnZipTaxId(locale),
                locale
        );

        commercePage.assertUrlContains(sa, "billing");

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        sa.assertTrue(commercePage.isDplusBaseFlashNotificationContainerPresent(),
                "Flash Notification Container not present after transaction");

        sa.assertAll();
    }
}
