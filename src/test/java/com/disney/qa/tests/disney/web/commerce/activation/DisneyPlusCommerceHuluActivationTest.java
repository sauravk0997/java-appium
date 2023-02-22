package com.disney.qa.tests.disney.web.commerce.activation;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneyApiCommon;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.disney.web.commerce.DisneyPlusCommercePage;
import com.disney.qa.disney.web.commerce.DisneyPlusRedemptionPage;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.WebConstant;
import com.disney.qa.disney.web.appex.profileviews.DisneyPlusAddProfilePage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusConsentCollectionPage;
import com.disney.qa.disney.web.appex.userflows.DisneyPlusUserPage;
import com.disney.qa.tests.disney.DisneyPlusBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.disney.ZebrunnerXrayLabels;
import net.lightbody.bmp.BrowserMobProxy;
import org.json.JSONException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public class DisneyPlusCommerceHuluActivationTest extends DisneyPlusBaseTest {

    ThreadLocal<DisneyAccount> disneyAccount = new ThreadLocal<DisneyAccount>();

    private final String disneyActivationTokenUrl = DisneyWebParameters.DISNEY_QA_ACTIVATION_TOKEN_URL.getValue();
    private final String disneyHuluActivationUrl = DisneyWebParameters.DISNEY_INCOMING_ACTIVATION_URL.getValue();
    private String provider = "hulu";

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(){
        disneyAccount.set(new DisneyAccount());
    }

    @AfterMethod (alwaysRun = true)
    public void testTeardown(ITestResult result) {
        testCleanup(result.isSuccess(), disneyAccount.get());
        disneyAccount.remove();
    }

    @Test(description = "Incoming Activation - Hulu", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void incomingActivationCreditCardProfileHulu() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52112"));

        SoftAssert sa = new SoftAssert();
        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyAccountApi accountApi = getAccountApi();
        CreateDisneyAccountRequest accountRequest = CreateDisneyAccountRequest.builder().isStarOnboarded(false).build();
        disneyAccount.set(accountApi.createAccount(accountRequest));

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        String token = commercePage.getJwtActivationToken(disneyActivationTokenUrl, account.getEmail(), provider);

        commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.openURL(commercePage.getActivationUrl(disneyHuluActivationUrl, token, provider));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        sa.assertAll();
    }

    @Test(description = "Incoming Activation - Hulu - Account created on D+", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void incomingActivationCreditCardActivatedHulu() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        setZebrunnerXrayLabels(new ZebrunnerXrayLabels(DIS, US, "XWEBQAP-52113"));

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        boolean isActivation = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        disneyAccount.get().setEmail(DisneyApiCommon.getUniqueEmail());
        disneyAccount.get().setUserPass(DisneyApiCommon.getCommonPass());

        commercePage.setIgnoreCookie(true);
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, isBundle, isActivation, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, false, disneyAccount, proxy));

        String token = commercePage.getJwtActivationToken(disneyActivationTokenUrl, disneyAccount.get().getEmail(), provider);
        commercePage.openURL(commercePage.getActivationUrl(disneyHuluActivationUrl, token, provider));

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        redemptionPage.enterLoginFlow(disneyAccount.get().getEmail(), disneyAccount.get().getUserPass());
        
        DisneyPlusConsentCollectionPage consentCollectionPage = new DisneyPlusConsentCollectionPage(getDriver());
        consentCollectionPage.verifyPage();
        consentCollectionPage.enterConsentDob(WebConstant.ADULT_DOB);

        if (!isMobile()) {
            commercePage.clickStartStreamingBtnKey();
            DisneyPlusAddProfilePage addProfilePage = new DisneyPlusAddProfilePage(getDriver());
            addProfilePage.updateProfile();
        }
        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, true, isMobile());

        sa.assertAll();
    }

    @Test(description = "Incoming Activation - Hulu - Attempt D+ Superbundle Purchase With Same Email", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void incomingActivationSuperbundleSameEmail() throws MalformedURLException, JSONException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean isActivation = true;
        boolean isBundle = true;

        disneyAccount.get().setEmail(DisneyWebParameters.DISNEY_QA_WEB_SUPERBUNDLE_USER.getValue());
        disneyAccount.get().setUserPass(DisneyWebParameters.DISNEY_QA_WEB_SUPERBUNDLE_PASS.getDecryptedValue());

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, isBundle, isActivation, disneyAccount.get().getEmail(), disneyAccount.get().getUserPass(), language, true, disneyAccount, proxy));

        sa.assertTrue(userPage.isIneligibleHuluSubscriberHeaderPresent(),
                "Hulu Block Title Key not Present for Activation");
        sa.assertAll();
    }


    @Test(description = "Outgoing Activation - Hulu - Credit Card", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void outgoingActivationCreditCard() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        boolean isActivation = true;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.clickDplusAnnualBndleBtn();

        commercePage.assertLogo(sa, commercePage.isDplusBaseLogoIdPresent());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isActivation, language, disneyAccount, proxy));
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

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        if (isMobile()) {
            sa.assertTrue(commercePage.isGetAppBundlePagePresent());
        }
        sa.assertAll();
    }

    @Test(description = "Outgoing Activation - Hulu - Credit Card - From Account Profile", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void outgoingActivationCreditCardProfile() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(QA);

        SoftAssert sa = new SoftAssert();
        boolean isBundle = false;
        boolean isActivation = true;
        String paymentType = "credit";

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        commercePage.clickDplusAnnualBndleBtn();
        commercePage.assertLogo(sa, commercePage.isDplusBaseLogoIdPresent());

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isActivation, language, disneyAccount, proxy));
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

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        if (isMobile()) {
            sa.assertTrue(commercePage.isGetAppBundlePagePresent());
        }
        sa.assertAll();
    }

    @Test(description = "Outgoing Activation - Hulu - PayPal", groups = {"US", TestGroup.DISNEY_COMMERCE})
    public void outgoingActivationPayPal() throws MalformedURLException, JSONException, URISyntaxException {
        skipPayPalTestByProduct();

        SoftAssert sa = new SoftAssert();
        boolean isActivation = true;
        boolean isBundle = true;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);

        commercePage.clickDplusAnnualBndleBtn();

        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpGeneratedEmailPassword(locale, isBundle, isActivation, language, disneyAccount, proxy));

        commercePage.clickPayPalRadioIconById(locale);

        DisneyPlusRedemptionPage redemptionPage = new DisneyPlusRedemptionPage(getDriver());
        commercePage.handleTaxIdBillingFields(locale, redemptionPage.searchAndReturnZipTaxId(locale));

        boolean shouldSkip = commercePage.handleFullPayPal(locale, proxy);
        skipPayPalTest(shouldSkip);

        commercePage.checkAndHandleMatureContentOnboardingFlowOrSuccessOverlay(sa, locale, false, false, isMobile());

        if (isMobile()) {
            sa.assertTrue(commercePage.isGetAppBundlePagePresent());
        }
        sa.assertAll();
    }

    //Double billed is no longer in use for Hulu (DMGZWEB-17988)
    @Test( description = "Outgoing Activation - Hulu - Double Billed", groups = {"US"} , enabled = false)
    public void incomingActivationHuluDoubleBilled() throws MalformedURLException, JSONException, IOException, URISyntaxException {
        skipTestByEnv(PROD);
        skipTestByEnv(BETA);

        SoftAssert sa = new SoftAssert();
        boolean isActivation = false;
        boolean isBundle = false;

        String data = countryData.searchAndReturnCountryData(locale, CODE, COUNTRY);

        DisneyPlusCommercePage commercePage = new DisneyPlusCommercePage(getDriver());
        String email = DisneyApiCommon.getUniqueEmail();
        String password = DisneyApiCommon.getCommonPass();

        BrowserMobProxy proxy = commercePage.environmentSetupLocationOverride(DisneyPlusBasePage.ENVIRONMENT, data);
        DisneyPlusUserPage userPage = new DisneyPlusUserPage(getDriver());
        setOverride(userPage.signUpEmailPassword(locale, isBundle, isActivation, email, password, language, false, disneyAccount, proxy));
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
        commercePage.clickChallengeFormSubmitBtn(locale);
        userPage.isAccountDropdownIdPresent();

        String token = commercePage.getJwtActivationToken(disneyActivationTokenUrl, email, provider);

        commercePage.openURL(commercePage.getHomeUrl());
        commercePage.openURL(commercePage.getActivationUrl(disneyHuluActivationUrl, token, provider));
        redemptionPage.enterLoginFlow(email, password);

        sa.assertTrue(commercePage.isDoubleBilledHuluKeyPresent(),
                String.format("Double billed key not present on: %s", commercePage.getCurrentUrl()));

        sa.assertAll();
    }
}
