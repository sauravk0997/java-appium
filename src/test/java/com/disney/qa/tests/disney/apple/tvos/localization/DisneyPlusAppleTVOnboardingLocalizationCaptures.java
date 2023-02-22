package com.disney.qa.tests.disney.apple.tvos.localization;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOrder;
import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.ZipUtils;
import com.qaprosoft.carina.core.foundation.utils.DateUtils;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

public class DisneyPlusAppleTVOnboardingLocalizationCaptures extends DisneyPlusAppleTVBaseTest {

    private final ThreadLocal<String> baseDirectory = new ThreadLocal<>();
    private final ThreadLocal<String> pathToZip = new ThreadLocal<>();
    private final ThreadLocal<String> regionLanguage = new ThreadLocal<>();
    private final ThreadLocal<String> region = new ThreadLocal<>();
    private final DisneyContentApiChecker disneyContentApiChecker = new DisneyContentApiChecker();
    private final DisneyAccountApi disneyAccountApi = new DisneyAccountApi(DisneyPlusAppleTVBaseTest.PLATFORM, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DISNEY);

    @BeforeMethod(alwaysRun = true)
    public void proxySetUp() {
        R.CONFIG.put("capabilities.fullReset", "true");
        boolean unpinDictionaries = Boolean.parseBoolean(R.CONFIG.get("custom_string"));
        boolean displayDictionaryKeys = Boolean.parseBoolean(R.CONFIG.get("custom_string5"));
        String globalizationVersion = R.CONFIG.get("custom_string4");
        if (unpinDictionaries || displayDictionaryKeys || (!globalizationVersion.isEmpty() && !globalizationVersion.equalsIgnoreCase("null"))) {
            installJarvis();
            setJarvisOverrides();
        }
        DisneyCountryData countryData = new DisneyCountryData();
        regionLanguage.set(R.CONFIG.get("custom_string2"));
        region.set(R.CONFIG.get("custom_string3"));
        String country = (String) countryData.searchAndReturnCountryData(region.get(), "code", "country");
        setSystemLanguage(regionLanguage.get(), region.get());
        restartDriver(true);
        initiateProxy(country);
        regionLanguage.set(handleAppLanguage(regionLanguage.get()));
        pause(10);
        clearAppCache();
        languageUtils.set(new DisneyLocalizationUtils(region.get(), regionLanguage.get(), "apple-tv", "prod", PARTNER));
        DisneyMobileConfigApi configApi = new DisneyMobileConfigApi("tvos", "prod", PARTNER, new MobileUtilsExtended().getInstalledAppVersion());
        languageUtils.get().setDictionaries(configApi.getDictionaryVersions());
        DisneyPlusApplePageBase.setDictionary(languageUtils.get());
    }

    @Test(description = "Onboarding Flow From Sign Up To Log Out Capture Screenshots", groups = {"Onboarding"})
    public void captureFullOnboardingFlowFromSignUpToLogOut() {
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVLegalPage disneyPlusAppleTVLegalPage = new DisneyPlusAppleTVLegalPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVPaywallPage disneyPlusAppleTVPaywallPage = new DisneyPlusAppleTVPaywallPage(getDriver());
        DisneyPlusAppleTVFinishLaterPage disneyPlusAppleTVFinishLaterPage = new DisneyPlusAppleTVFinishLaterPage(getDriver());
        DisneyPlusAppleTVCompleteSubscriptionPage disneyPlusAppleTVCompleteSubscriptionPage = new DisneyPlusAppleTVCompleteSubscriptionPage(getDriver());

        baseDirectory.set("Screenshots/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "Onboarding", DateUtils.now()));

        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();

        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(10); //handle initial load of app
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        getScreenshots("1_Landing_page", baseDirectory);

        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        pause(5);
        disneyPlusAppleTVSignUpPage.isLocalizedPageWithPrimaryButtonOpened();
        getScreenshots("2_Enter_email", baseDirectory);

        disneyPlusAppleTVSignUpPage.moveDown(2, 1);
        disneyPlusAppleTVSignUpPage.clickSelect();
        pause(5);
        disneyPlusAppleTVLegalPage.isOpened();
        getScreenshots("3_Privacy_Policy", baseDirectory);

        disneyPlusAppleTVLegalPage.clickMenuTimes(1, 1);
        disneyPlusAppleTVSignUpPage.moveUp(2, 1);
        disneyPlusAppleTVLoginPage.clickEmailField();
        disneyPlusAppleTVLoginPage.clickLocalizationEnterNewBtn();
        pause(5);
        getScreenshots("4_Enter_your_email", baseDirectory);

        disneyPlusAppleTVLoginPage.keyPressTimes(disneyPlusAppleTVLoginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        disneyPlusAppleTVLoginPage.clickSelect();
        disneyPlusAppleTVLoginPage.clickContinueBtn();
        disneyPlusAppleTVLoginPage.isLocalizedPageWithPrimaryButtonOpened();
        pause(5); //loading next page
        getScreenshots("5_Error_1", baseDirectory);
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(disneyContentApiChecker.getUniqueUserEmail());
        pause(5); //loading next page

        //Some countries have subscriber agreement requirement:
        if (languageUtils.get().isSubscriberAgreementRequired()) {
            disneyPlusApplePageBase.isLocalizedPageWithPrimaryButtonOpened();
            getScreenshots("6_SA", baseDirectory);
            disneyPlusApplePageBase.clickPrimaryButton();
        }
        pause(5); //loading next page
        getScreenshots("7_Create_PW", baseDirectory);

        disneyPlusAppleTVPasswordPage.moveDown(1, 1);
        disneyPlusAppleTVPasswordPage.clickSignUp();
        pause(5);
        getScreenshots("8_Error_2", baseDirectory);

        disneyPlusAppleTVPasswordPage.moveUp(1, 1);
        disneyPlusAppleTVPasswordPage.clickPassword();
        pause(5); //loading next page
        disneyPlusAppleTVPasswordPage.isLocalizedPageWithPrimaryButtonOpened();
        getScreenshots("9_Create_a_password", baseDirectory);

        disneyPlusAppleTVPasswordPage.enterPassword("123");
        pause(2);
        getScreenshots("10_Error_3", baseDirectory);

        disneyPlusAppleTVPasswordPage.enterPassword("local123");
        pause(2);
        getScreenshots("11_Fair", baseDirectory);

        disneyPlusAppleTVPasswordPage.enterPassword("local1234");
        pause(2);
        getScreenshots("12_Good", baseDirectory);

        disneyPlusAppleTVPasswordPage.enterPassword("local123b456@");
        pause(2);
        getScreenshots("13_Excellent", baseDirectory);

        disneyPlusAppleTVLoginPage.keyPressTimes(disneyPlusAppleTVLoginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        disneyPlusAppleTVSignUpPage.clickSelect();
        disneyPlusAppleTVSignUpPage.clickPrimaryButton();

        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(2);
        disneyPlusAppleTVPaywallPage.isOpened();
        getScreenshots("14_Paywall", baseDirectory);

        disneyPlusAppleTVPaywallPage.clickMenuTimes(1, 1);
        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(2);
        disneyPlusAppleTVFinishLaterPage.isOpened();
        getScreenshots("15_Finish_later", baseDirectory);

        disneyPlusAppleTVPaywallPage.clickAlertConfirm(); //click finish later button
        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        pause(2);
        disneyPlusAppleTVCompleteSubscriptionPage.isCompleteSubBtnFocused();
        getScreenshots("16_One_Step_Away", baseDirectory);

        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVCompleteSubscriptionPage.moveDown(1, 1);
        disneyPlusAppleTVCompleteSubscriptionPage.clickLogoutButtonIfHasFocus();
        pause(2);
        getScreenshots("17_Log out", baseDirectory);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());

        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Onboarding Flow Expired Account Capture Screenshots", groups = {"Onboarding"})
    public void captureFullOnboardingFlowToExpiredAccount() {
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVRestartSubscriptionPage disneyPlusAppleTVRestartSubscriptionPage = new DisneyPlusAppleTVRestartSubscriptionPage(getDriver());

        DisneyAccount user = disneyAccountApi.createExpiredAccount(ENTITLEMENT_LOOKUP, region.get(), regionLanguage.get(), SUB_VERSION);
        baseDirectory.set("Screenshots/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "Onboarding", DateUtils.now()));

        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();

        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(10); //handle initial load of app
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());

        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(5);
        disneyPlusAppleTVSignUpPage.isRestartSubBtnPresent();
        getScreenshots("18_Welcome back", baseDirectory);

        disneyPlusAppleTVSignUpPage.clickRestartSubscription();
        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        //Some countries have subscriber agreement requirement:
        if (languageUtils.get().isSubscriberAgreementRequired()) {
            disneyPlusApplePageBase.isLocalizedPageWithPrimaryButtonOpened();
            pause(5);
            disneyPlusApplePageBase.clickPrimaryButton();
        }

        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(5);
        disneyPlusAppleTVRestartSubscriptionPage.isOpened();
        getScreenshots("19_Restart_paywall", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());

        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Onboarding flows Background Images Check", groups = {"BGImage"})
    public void backgroundImage() {
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().addDefaultEntitlement(true).country(region.get()).language(regionLanguage.get()).build();
        request.addOrderSetting(DisneyOrder.SET_EXPIRED);
        DisneyAccount account = disneyAccountApi.createAccount(request);
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVCompletePurchasePage disneyPlusAppleTVCompletePurchasePage = new DisneyPlusAppleTVCompletePurchasePage(getDriver());
        DisneyPlusAppleTVRestartSubscriptionPage disneyPlusAppleTVRestartSubscriptionPage = new DisneyPlusAppleTVRestartSubscriptionPage(getDriver());
        int count = 1;
        baseDirectory.set("Screenshots-BGImage/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "BGImage", UUID.randomUUID()));

        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();

        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        disneyPlusAppleTVSignUpPage.isOpened();

        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(apiProvider.get().getUniqueUserEmail());
        if (languageUtils.get().isSubscriberAgreementRequired()) {
            if (!disneyPlusAppleTVPasswordPage.isPasswordEntryFieldPresent(15))
                disneyPlusAppleTVPasswordPage.clickSelect();
        }
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(account.getUserPass());
        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVCompletePurchasePage.isOpened();
        disneyPlusAppleTVCompletePurchasePage.clickBack();
        if (disneyPlusAppleTVCompletePurchasePage.isAlertDefaultBtnPresent()) {
            disneyPlusAppleTVCompletePurchasePage.clickDefaultAlertBtn();
        }
        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVCompletePurchasePage.isCustomButtonPresent();
        pause(5);
        getScreenshots(count++ + "-One_Step_Away_Screen", baseDirectory);
        disneyPlusAppleTVCompletePurchasePage.clickLogoutButton();
        disneyPlusAppleTVWelcomeScreenPage.isViewAlertPresent();
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        getScreenshots(count++ + "-Welcome_Screen", baseDirectory);

        clearAppCache();

        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.isOpened();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(account.getEmail());
        if (languageUtils.get().isSubscriberAgreementRequired()) {
            if (!disneyPlusAppleTVPasswordPage.isPasswordEntryFieldPresent(15))
                disneyPlusAppleTVPasswordPage.clickSelect();
        }
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(account.getUserPass());
        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVRestartSubscriptionPage.isLocalizedPageWithPrimaryButtonOpened();
        pause(5);
        getScreenshots(count + "-Welcome_Back", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(description = "Basic IAP flow", enabled = false)
    public void capturePurchaseFlow() {
        if(false) {
            skipExecution("Test run is not against IAP compatible build.");
        }

        int count = 1;
        baseDirectory.set("Screenshots-BGImage/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "BGImage", UUID.randomUUID()));
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVPaywallPage disneyPlusAppleTVPaywallPage = new DisneyPlusAppleTVPaywallPage(getDriver());

        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(apiProvider.get().getUniqueUserEmail());
        if (languageUtils.get().isSubscriberAgreementRequired()) {
            if (!disneyPlusAppleTVPasswordPage.isPasswordEntryFieldPresent(15))
                disneyPlusAppleTVPasswordPage.clickSelect();
        }
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(R.TESTDATA.getDecrypted("disney_qa_web_d23password"));
        if(languageUtils.get().getCountryName().equals("United States")) {
            LOGGER.info("DOB entry required.");
            StringBuilder dob = new StringBuilder()
                    .append(DisneyBaseTest.Person.ADULT.getMonth().getNum())
                    .append(DisneyBaseTest.Person.ADULT.getDay(true))
                    .append(DisneyBaseTest.Person.ADULT.getYear());
            disneyPlusAppleTVSignUpPage.enterDateOfBirth(dob.toString());
            disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
            disneyPlusAppleTVPaywallPage.clickBasicPlan();
            disneyPlusAppleTVPaywallPage.clickPurchaseButton();
            try {
                disneyPlusAppleTVPaywallPage.submitSandboxPassword("G0Disney!");
            } catch (NoSuchElementException nse) {
                LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
                disneyPlusAppleTVPaywallPage.clickSelect();
            }
            disneyPlusAppleTVPaywallPage.acceptConfirmationAlert();
            Assert.assertTrue(disneyPlusAppleTVPaywallPage.confirmSubscription(),
                    "IAP did not complete successfully. Retry count exceeded.");
            disneyPlusAppleTVPaywallPage.acceptConfirmationAlert();
            LOGGER.info("derr");
        }

    }


//    @Test(description = "Onboarding Flow Expired Account Capture Screenshots", groups = {"onboarding"})
//    public void captureFullOnboardingFlowToExpiredAccount() {

    //  captureFullOnboardingFlowEndToEnd crashes at logOut while proxied. Commented out just in case in the future possible.
//    @Test(description = "Onboarding Flow End To End Capture Screenshots", groups = {"Localization"})
//    public void captureFullOnboardingFlowEndToEnd() {
//        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
//        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
//        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
//        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
//        DisneyPlusAppleTVLegalPage disneyPlusAppleTVLegalPage = new DisneyPlusAppleTVLegalPage(getDriver());
//        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
//        DisneyPlusAppleTVPaywallPage disneyPlusAppleTVPaywallPage = new DisneyPlusAppleTVPaywallPage(getDriver());
//        DisneyPlusAppleTVCompleteSubscriptionPage disneyPlusAppleTVCompleteSubscriptionPage = new DisneyPlusAppleTVCompleteSubscriptionPage(getDriver());
//
//        DisneyAccount user = disneyAccountApi.createExpiredAccount(ENTITLEMENT_LOOKUP, region.get(), regionLanguage.get(), SUB_VERSION);
//        baseDirectory.set(String.format("Screenshots/"));
//        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "Onboarding", DateUtils.now()));
//
//        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
//        pause(15); //wait for page to fully load
//        disneyPlusAppleTVWelcomeScreenPage.isOpened();
//        getScreenshots("1_Landing_page", baseDirectory);
//
//        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
//        pause(5); //wait for page to fully load
//        disneyPlusAppleTVSignUpPage.isOpened();
//        getScreenshots("2_Enter_email", baseDirectory);
//
//        disneyPlusAppleTVSignUpPage.isOpened();
//
//        disneyPlusAppleTVSignUpPage.moveDown(2,1);
//        disneyPlusAppleTVSignUpPage.clickSelect();
//        disneyPlusAppleTVLegalPage.isOpened();
//        getScreenshots("3_Privacy_Policy", baseDirectory);
//
//        disneyPlusAppleTVLegalPage.clickBack();
//        disneyPlusAppleTVSignUpPage.moveUp(2,1);
//        disneyPlusAppleTVLoginPage.clickEmailField();
//        disneyPlusAppleTVLoginPage.clickLocalizationEnterNewBtn();
//        getScreenshots("4_Enter_your_email", baseDirectory);
//
//        disneyPlusAppleTVLoginPage.keyPressTimes(disneyPlusAppleTVLoginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
//
//        disneyPlusAppleTVLoginPage.clickSelect();
//        disneyPlusAppleTVLoginPage.clickContinueBtn();
//        disneyPlusAppleTVLoginPage.isOpened();
//        getScreenshots("5_Error_1", baseDirectory);
//        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(disneyContentApiChecker.get().getUniqueUserEmail());
//
//        //Some countries have subscriber agreement requirement:
//        if(languageUtils.get().isSubscriberAgreementRequired() ) {
//            disneyPlusApplePageBase.isLocalizedPageWithPrimaryButtonOpened();
//            pause(1);
//            getScreenshots("6_SA", baseDirectory);
//            disneyPlusApplePageBase.clickPrimaryButton();
//        }
//        pause(1);
//        getScreenshots("7_Create PW", baseDirectory);
//        disneyPlusAppleTVPasswordPage.moveDown(1,1);
//        disneyPlusAppleTVPasswordPage.clickSignUp();
//        pause(1);
//        getScreenshots("8_Error_2", baseDirectory);
//        disneyPlusAppleTVPasswordPage.clickUp();
//        disneyPlusAppleTVPasswordPage.clickPassword();
//
//        pause(1);
//        disneyPlusAppleTVPasswordPage.isOpened();
//        getScreenshots("9_Create_a_password", baseDirectory);
//        disneyPlusAppleTVPasswordPage.enterPassword("123");
//        pause(1);
//        getScreenshots("10_Error_3", baseDirectory);
//        disneyPlusAppleTVPasswordPage.enterPassword("local123");
//        pause(1);
//        getScreenshots("11_Fair", baseDirectory);
//        disneyPlusAppleTVPasswordPage.enterPassword("local1234");
//        pause(1);
//        getScreenshots("12_Good", baseDirectory);
//        disneyPlusAppleTVPasswordPage.enterPassword("local123b456@");
//        pause(1);
//        getScreenshots("12_Excellent", baseDirectory);
//
//        disneyPlusAppleTVLoginPage.keyPressTimes(disneyPlusAppleTVLoginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
//        disneyPlusAppleTVSignUpPage.clickSelect();
//        disneyPlusAppleTVSignUpPage.clickPrimaryButton();
//        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
//
//        disneyPlusAppleTVPaywallPage.isOpened();
//        getScreenshots("14_Paywall", baseDirectory);
//
//        disneyPlusAppleTVPaywallPage.clickMenuTimes(1,1);
//        disneyPlusAppleTVPaywallPage.isOpened();
//        getScreenshots("15_Finish_later", baseDirectory);
//
//        disneyPlusAppleTVPaywallPage.clickAlertConfirm(); //click finish later button
//        disneyPlusAppleTVSignUpPage.isOpened();
//        getScreenshots("16_One_Step_Away", baseDirectory);
//
//        disneyPlusAppleTVCompleteSubscriptionPage.isOpened();
//        disneyPlusAppleTVCompleteSubscriptionPage.moveDown(1,1);
//        disneyPlusAppleTVCompleteSubscriptionPage.clickLogOutBtn();
//        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
//        getScreenshots("17_Log_out", baseDirectory);
//        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
//
//        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
//        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());
//
//        disneyPlusAppleTVWelcomeScreenPage.isOpened();
//        getScreenshots("18_Welcome_back", baseDirectory);
//
//        disneyPlusAppleTVSignUpPage.isOpened();
//        disneyPlusAppleTVSignUpPage.clickSelect();
//        disneyPlusAppleTVSignUpPage.clickAgreeAndContinue();
//        getScreenshots("19_Restart_paywall", baseDirectory);
//
//        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
//    }

}
