package com.disney.qa.tests.disney.web.commerce;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.web.VerifyEmail;
import com.disney.qa.disney.web.commerce.DisneyPlusCancelSuccessPage;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
 import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.commerce.DisneyPlusSubscriptionDetailsPage;
import com.disney.qa.disney.web.commerce.modal.DisneyPlusCancelContractPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAccountPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;

public class DisneyPlusCommerceCancelSubscriptionTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<com.disney.qa.api.pojos.DisneyAccount>();
    ThreadLocal<VerifyEmail> verifyEmail = new ThreadLocal<>();

    private String completePurchase = "complete-purchase";
    private String paymentType = "credit";

    @BeforeMethod (alwaysRun = true)
    public void setupTest() {
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Base", groups = {"MX", TestGroup.STAR_COMMERCE})
    public void monthly() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().country(locale).isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);
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

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);
        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();
        accountPage.clickSubscriptionDetailsCancelSubscriptionBtn();

        accountPage.clickAndReturnRandomSurveyResponse();

        accountPage.clickCancelSubmitBtn();
        accountPage.clickCompleteCancellationBtn();

        sa.assertTrue(accountPage.isCancellationConfirmationBlurbPresent(),
                "Cancellation Confirmation Blurb not present after cancellation");

        sa.assertTrue(accountPage.isChangeYourMindBodyPresent(),
                "Restart Subscription Banner not present after cancellation");

        accountPage.clickCancellationConfirmationGoHomeBtn();

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        sa.assertTrue(accountPage.isRestartSubscriptionBtnPresent(),
                String.format("Restart Subscription btn not present on: %s", accountPage.getCurrentUrl()));

        sa.assertAll();
    }

    @Test(description = "Annual", groups = {"MX", TestGroup.STAR_COMMERCE})
    public void annual() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, DISNEY_PLUS_SKIP_COUNTRIES);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().country(locale).isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.dBaseUniversalLogin(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);
        commercePage.clickYearlyRadioBtnById();
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

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);
        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        accountPage.clickSubscriptionDetailsCancelSubscriptionBtn();

        accountPage.clickAndReturnRandomSurveyResponse();

        accountPage.clickCancelSubmitBtn();

        accountPage.clickCompleteCancellationBtn();

        sa.assertTrue(accountPage.isCancellationConfirmationBlurbPresent(),
                "Cancellation Confirmation Blurb not present after cancellation");

        sa.assertTrue(accountPage.isChangeYourMindBodyPresent(),
                "Restart Subscription Banner not present after cancellation");

        accountPage.clickCancellationConfirmationGoHomeBtn();

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        sa.assertTrue(accountPage.isRestartSubscriptionBtnPresent(),
                String.format("Restart Subscription btn not present on: %s", accountPage.getCurrentUrl()));

        sa.assertAll();
    }

    @Test(description = "Combo+", groups = {"MX", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void comboPlusCancel() throws URISyntaxException, JSONException, IOException, MalformedURLException {
        skipTestByEnv(QA);
        skipTestByProjectLocale(locale, STAR_PLUS_SKIP_COUNTRIES, EMPTY_SKIP_COUNTRIES_LIST);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = true;
        boolean isRedemption = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));
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

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        accountPage.clickSubscriptionDetailsCancelSubscriptionBtn();

        accountPage.clickAndReturnRandomSurveyResponse();

        accountPage.clickCancelSubmitBtn();

        accountPage.clickCompleteCancellationBtn();

        sa.assertTrue(accountPage.isCancellationConfirmationBlurbPresent(),
                "Cancellation Confirmation Blurb not present after cancellation");

        sa.assertTrue(accountPage.isChangeYourMindBodyPresent(),
                "Restart Subscription Banner not present after cancellation");

        accountPage.clickCancellationConfirmationGoHomeBtn();

        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        sa.assertTrue(accountPage.isRestartSubscriptionBtnPresent(),
                String.format("Restart Subscription btn not present on: %s", accountPage.getCurrentUrl()));

        sa.assertAll();
    }

    @Test(description = "Shows In Dropdown", groups = {"AR", TestGroup.DISNEY_COMMERCE, TestGroup.STAR_COMMERCE})
    public void showsInDropdown() throws URISyntaxException, IOException, JSONException, MalformedURLException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);
        DisneyAccountApi accountApi = getAccountApi();
        DisneyAccount account = accountApi.createAccount(locale, language);
        accountApi.overrideLocations(account, locale);
        disneyAccount.set(account);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.dBaseUniversalLogin(account.getEmail(), account.getUserPass());
        commercePage.clickCompleteAndRestartPurchaseBaseCta(locale, completePurchase, isBundle);
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

        commercePage.clickCreditSubmitBtnById();
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);
        if (!isMobile()) {
            commercePage.clickStartStreamingBtnKey();
        }
        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnCancelSubscriptionDropdown(isMobile());
        sa.assertTrue(accountPage.getCancellationSurvey().isElementPresent(), "Cancel survey not present");

        sa.assertAll();
    }

    @Test(description = "Cancel Subscription - Unauth user", groups = {"DE", TestGroup.DISNEY_COMMERCE})
    public void cancelSubUnauthUser() throws MalformedURLException, JSONException, IOException, URISyntaxException, InterruptedException, NoSuchAlgorithmException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, DE, "XWEBQAP-53260", "XWEBQAP-53261"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        boolean isActivation = false;
        String paymentType = "credit";
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().country(locale).isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, isBundle, isActivation, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, false, disneyAccount, proxy));
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
        redemptionPage.clickCreditSubmitBtnById();
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);
        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), false, false);

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(getDriver());
        String subscriptionID = subscriptionDetailsPage.getSubscriptionID();
        accountPage.clickOnLogoutWebOrMobile(false);

        userPage.clickCancelSubscriptionFooter();

        DisneyPlusCancelContractPage cancelContractPage = new DisneyPlusCancelContractPage(getDriver());
        Assert.assertTrue(cancelContractPage.verifyPage(), "Cancel Contract page didn't load");
        
        cancelContractPage.enterEmail(disneyAccount.get().getEmail());
        cancelContractPage.enterSubscriptionID(subscriptionID);
        cancelContractPage.clickCancelSubscriptionButton();

        DisneyPlusCancelSuccessPage cancelSuccessPage = new DisneyPlusCancelSuccessPage(getDriver());
        sa.assertTrue(cancelSuccessPage.isCancelSuccessTitleVisible(), "Cancel success title is not visible");
        sa.assertTrue(cancelSuccessPage.cancelConfirmationBlurbText().equals(WebConstant.DE_CANCEL_CONFORMATION_COPY), "Cancel confirmation copy doesn't match");

        sa.assertAll();
    }

    @Test(description = "Keep Subscription - Unauth user", groups = {"DE", TestGroup.DISNEY_COMMERCE})
    public void keepSubUnauthUser() throws MalformedURLException, JSONException, IOException, URISyntaxException, InterruptedException, NoSuchAlgorithmException {
        skipTestByEnv(QA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, DE, "XWEBQAP-53263"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        boolean isActivation = false;
        String paymentType = "credit";
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        commercePage.setIgnoreCookie(true);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().country(locale).isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, isBundle, isActivation, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, false, disneyAccount, proxy));
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
        redemptionPage.clickCreditSubmitBtnById();
        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);
        commercePage.assertPurchaseWithSuccessOverlay(sa, locale, isMobile(), false, false);

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickInternalD2CSubscriptionCtaContains();

        DisneyPlusSubscriptionDetailsPage subscriptionDetailsPage = new DisneyPlusSubscriptionDetailsPage(getDriver());
        String subscriptionID = subscriptionDetailsPage.getSubscriptionID();
        sa.assertFalse(subscriptionDetailsPage.getSubscriptionID().isEmpty(), "Subscription ID is empty");
        
        subscriptionDetailsPage.clickSubscriptionIDInfoButton();
        sa.assertTrue(subscriptionDetailsPage.isSubscriptionInfoModalVisible(),"Subscription ID Info Modal is not visible");
        subscriptionDetailsPage.clickSubscriptionInfoModalCloseButton();

        accountPage.clickOnLogoutWebOrMobile(false);

        userPage.clickCancelSubscriptionFooter();

        DisneyPlusCancelContractPage cancelContractPage = new DisneyPlusCancelContractPage(getDriver());
        Assert.assertTrue(cancelContractPage.verifyPage(), "Cancel Contract page didn't load");
        
        cancelContractPage.enterEmail(disneyAccount.get().getEmail());
        cancelContractPage.enterSubscriptionID(subscriptionID);
        cancelContractPage.clickKeepSubscriptionButton();

        sa.assertTrue(userPage.isDplusBaseLoginBtnPresent(SHORT_TIMEOUT));

        sa.assertAll();
    }


    @Test(description = "Cancel Subscription - Auth user", groups = {"DE", TestGroup.DISNEY_COMMERCE})
    public void cancelSubAuthUser() throws MalformedURLException, JSONException, IOException, URISyntaxException, InterruptedException {
        skipTestByEnv(QA);

        String[] disneyPlusUsXrayId = new String[] {"XWEBQAP-52104", "XWEBQAP-52162", "XWEBQAP-52160", "XWEBQAP-52166"};
        setZebrunnerXrayLabels(new ZebrunnerXrayLabels("DIS", "US", disneyPlusUsXrayId));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        boolean isRedemption = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isRedemption, language, disneyAccount, proxy));
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

        redemptionPage.clickCreditSubmitBtnById();
        pause(HALF_TIMEOUT);

        commercePage.clickBtnReviewSubscriptionCta(locale, paymentType);
        commercePage.clickChallengeFormSubmitBtn(locale);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        DisneyPlusAccountPage accountPage = new DisneyPlusAccountPage(getDriver());
        accountPage.clickOnAccountDropdown(isMobile());
        accountPage.clickOnCancelSubscriptionFooter();
        accountPage.clickOnCancelSurveyModalLink();

        sa.assertTrue(accountPage.isCancellationSurveyModalPresent(),
                String.format("Cancellation Survey Modal not present on: %s", accountPage.getCurrentUrl()));

        accountPage.clickOnCancelSurveyModalOtherButton();
        accountPage.clickOnCancellationSurveyModalSubmitButton();
        commercePage.clickCancelBtn();

        sa.assertTrue(accountPage.isRestartSubscriptionBtnPresent(),
                String.format("Restart Subscription btn not present on: %s", accountPage.getCurrentUrl()));

        sa.assertAll();
    }
}
