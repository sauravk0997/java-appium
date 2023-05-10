package com.disney.qa.tests.disney.apple.tvos.localization;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOrder;
import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.ZipUtils;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DisneyPlusAppleTVOnboardingLocalizationCaptures extends DisneyPlusAppleTVBaseTest {

    private final ThreadLocal<String> baseDirectory = new ThreadLocal<>();
    private final ThreadLocal<String> pathToZip = new ThreadLocal<>();
    private final DisneyAccountApi disneyAccountApi = new DisneyAccountApi(getApiConfiguration(DISNEY));

    @BeforeMethod(alwaysRun = true)
    public void proxySetUp() {
        boolean unpinDictionaries = Boolean.parseBoolean(R.CONFIG.get("custom_string"));
        boolean displayDictionaryKeys = Boolean.parseBoolean(R.CONFIG.get("custom_string5"));
        String globalizationVersion = R.CONFIG.get("custom_string4");
        if (unpinDictionaries || displayDictionaryKeys || (!globalizationVersion.isEmpty() && !globalizationVersion.equalsIgnoreCase("null"))) {
            installJarvis();
            setJarvisOverrides();
        }
        DisneyCountryData countryData = new DisneyCountryData();
        String country = (String) countryData.searchAndReturnCountryData(locale, "code", "country");
        initiateProxy(country);
        pause(10);
        clearAppCache();
    }

    @Test(description = "Onboarding Flow From Sign Up To Log Out Capture Screenshots", groups = {"Onboarding","Ariel"})
    public void captureFullOnboardingFlowFromSignUpToLogOut() {
        baseDirectory.set(String.format("Screenshots/%s/%s/", languageUtils.get().getCountryName(), languageUtils.get().getUserLanguage()));
        pathToZip.set(String.format("Ariel_Onboarding_Images_%s_%s_%s.zip", language.toUpperCase(), locale, getDate()));

        boolean ariel = languageUtils.get().getCountryName().equals("United States");
        boolean korea = languageUtils.get().getCountryName().equals("Korea");
        boolean logoutCheck = true;

        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().addDefaultEntitlement(true)
                .country(languageUtils.get().getLocale())
                .language(languageUtils.get().getUserLanguage())
                .gender(null)
                .build();
        DisneyAccount account = disneyAccountApi.createAccount(request);

        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage signUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLegalPage legalPage = new DisneyPlusAppleTVLegalPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVDOBCollectionPage dobCollectionPage = new DisneyPlusAppleTVDOBCollectionPage(getDriver());
        DisneyPlusAppleTVPaywallPage paywallPage = new DisneyPlusAppleTVPaywallPage(getDriver());
        DisneyPlusAppleTVCompleteSubscriptionPage completeSubscriptionPage = new DisneyPlusAppleTVCompleteSubscriptionPage(getDriver());
        DisneyPlusAppleTVUpdateProfilePage updateProfilePage = new DisneyPlusAppleTVUpdateProfilePage(getDriver());

        //Part 1: Standard onboarding screenshots (Welcome, Email, Legal, Password, Subscriber Agreement)
        welcomeScreenPage.isOpened();
        getScreenshots("1_WelcomePage", baseDirectory);

        welcomeScreenPage.clickSignUpButton();
        signUpPage.isOpened();
        getScreenshots("2_EnterEmail", baseDirectory);

        signUpPage.selectCheckBoxesForKr(korea);
        signUpPage.clickAgreeAndContinue();
        pause(1);
        getScreenshots("3_ErrorMessage", baseDirectory);

        signUpPage.clickViewAgreementAndPolicies();
        for (int i = 0; i < legalPage.getLegalTabs().size(); i++) {
            pause(1);
            getScreenshots(String.format("4-%s_LegalItem", i+1), baseDirectory);
            legalPage.moveDown(1, 1);
        }

        signUpPage.clickMenu();
        signUpPage.clickEmailButton();
        pause(1);
        getScreenshots("5-1_PreviousEmails", baseDirectory);
        loginPage.clickLocalizationEnterNewBtn();
        getScreenshots("5-2_SubmitEmailText", baseDirectory);

        loginPage.enterEmail(apiProvider.get().getUniqueUserEmail());
        loginPage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        loginPage.clickSelect();
        loginPage.clickContinueBtn();
        if (languageUtils.get().isSubscriberAgreementRequired()) {
            loginPage.isLocalizedPageWithPrimaryButtonOpened();
            getScreenshots("5-3_SubscriberAgreement", baseDirectory);
            loginPage.clickPrimaryButton();
        }
        passwordPage.isOpened();
        getScreenshots("6_CreatePassword", baseDirectory);

        passwordPage.clickPrimaryButton();
        getScreenshots("7_EmptyPasswordError", baseDirectory);

        passwordPage.clickPassword();
        passwordPage.enterPassword("a");
        passwordPage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        passwordPage.clickSelect();
        passwordPage.clickPrimaryButton();
        getScreenshots("8_ErrorMessage", baseDirectory);

        passwordPage.clickPassword();
        passwordPage.enterPassword("Local123");
        getScreenshots("9_WeakPasswordStrength", baseDirectory);

        passwordPage.enterPassword("Local123$");
        getScreenshots("10_MediumPasswordStrength", baseDirectory);

        passwordPage.enterPassword("Local123@!");
        getScreenshots("11_StrongPasswordStrength", baseDirectory);

        passwordPage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        passwordPage.clickSelect();
        passwordPage.clickPrimaryButton();

        //Part 2a: Ariel (DOB, Plan Selection)
        if(ariel) {
            dobCollectionPage.isOpened();
            getScreenshots("12-1_EnterBirthdate", baseDirectory);

            dobCollectionPage.clickPrimaryButton();
            getScreenshots("12-2_ErrorMessage", baseDirectory);

            dobCollectionPage.enterDOB("05/02/2020");
            dobCollectionPage.clickPrimaryButton();
            pause(2);
            dobCollectionPage.isLocalizedPageWithPrimaryButtonOpened();
            getScreenshots("13_NotEligible", baseDirectory);

            dobCollectionPage.clickSelect();
            String appName = Configuration.getMobileApp().toLowerCase();
            if(appName.contains("adhoc") && !appName.contains("non-iap")) {
                welcomeScreenPage.clickSignUpButton();
                signUpPage.clickEmailButton();
                loginPage.clickLocalizationEnterNewBtn();
                loginPage.enterEmail(apiProvider.get().getUniqueUserEmail());
                loginPage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
                loginPage.clickSelect();
                loginPage.clickContinueBtn();
                passwordPage.clickPassword();
                passwordPage.enterPassword("Local123@!");
                passwordPage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
                passwordPage.clickSelect();
                passwordPage.clickPrimaryButton();
                dobCollectionPage.enterDOB("05/02/1985");
                dobCollectionPage.clickPrimaryButton();

                getScreenshots("14_ChooseYourPlan", baseDirectory);

                paywallPage.clickBasicPlan();
                completeSubscriptionPage.isOpened();
                getScreenshots("15-1_PaywallBasic", baseDirectory);

                completeSubscriptionPage.clickMenu();
                paywallPage.clickPremiumPlan();
                completeSubscriptionPage.isOpened();
                getScreenshots("15-2_PaywallPremium", baseDirectory);

                paywallPage.clickMenuTimes(2, 1);
            } else {
                logoutCheck = false;
                LOGGER.warn("Non IAP build found. Skipping paywall images.");
            }
            //Part 2b: Legacy (International Paywall)
        } else {
            paywallPage.isOpened();
            paywallPage.dismissUnexpectedErrorAlert();
            getScreenshots("15-0_PaywallInternational", baseDirectory);
            paywallPage.clickMenuTimes(1, 1);
        }

        //Part 3: Post Paywall (Finish later, One Step Away, Logout)
        if(logoutCheck) {
            pause(1);
            getScreenshots("16_FinishLater", baseDirectory);

            paywallPage.clickSelect();
            pause(1);
            getScreenshots("17-1_OneStepAway", baseDirectory);

            paywallPage.moveDown(1, 1);
            paywallPage.clickSelect();
            pause(1);
            getScreenshots("17-2_LogOut", baseDirectory);
            paywallPage.clickSelect();
        }

        //Part 4: Ariel Continued (Post-Login profile settings)
        if(ariel) {
            logInWithoutHomeCheck(account);
            Instant timeout = Instant.now().plus(1, ChronoUnit.MINUTES);
            while (Instant.now().isBefore(timeout) && !updateProfilePage.isOpened()) {
                updateProfilePage.isOpened();
            }
            getScreenshots("18-1_UpdateProfile", baseDirectory);

            updateProfilePage.clickSaveProfileButton();
            pause(1);
            getScreenshots("18-2_ErrorMessage", baseDirectory);

            updateProfilePage.clickGenderDropDown();
            pause(1);
            getScreenshots("18-3_GenderMenu", baseDirectory);
        }
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(description = "Onboarding Flow Expired Account Capture Screenshots", groups = {"Onboarding"})
    public void captureFullOnboardingFlowToExpiredAccount() {
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSignUpPage disneyPlusAppleTVSignUpPage = new DisneyPlusAppleTVSignUpPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVRestartSubscriptionPage disneyPlusAppleTVRestartSubscriptionPage = new DisneyPlusAppleTVRestartSubscriptionPage(getDriver());

        DisneyAccount user = disneyAccountApi.createExpiredAccount(ENTITLEMENT_LOOKUP, languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), SUB_VERSION);
        baseDirectory.set("Screenshots-Onboarding-PartTwo/");
        pathToZip.set(String.format("Onboarding_Full_Expired_Account_%s_%s_%s.zip", locale, language, getDate()));

        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();

//        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(10); //handle initial load of app
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());

//        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(15);
        disneyPlusAppleTVSignUpPage.isRestartSubBtnPresent();
        getScreenshots("18-WelcomeBack", baseDirectory);

        disneyPlusAppleTVSignUpPage.clickRestartSubscription();
//        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        //Some countries have subscriber agreement requirement:
        if (languageUtils.get().isSubscriberAgreementRequired()) {
            disneyPlusApplePageBase.isLocalizedPageWithPrimaryButtonOpened();
            pause(5);
            disneyPlusApplePageBase.clickPrimaryButton();
        }

//        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(15);
        disneyPlusAppleTVRestartSubscriptionPage.isOpened();
        getScreenshots("19-RestartPaywall", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());

        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Onboarding flows Background Images Check", groups = {"BGImage"})
    public void backgroundImage() {
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().addDefaultEntitlement(true)
                .country(languageUtils.get().getLocale()).language(languageUtils.get().getUserLanguage()).build();
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
        pathToZip.set(String.format("Onboarding_Background_Images_%s_%s_%s.zip", language.toUpperCase(), locale, getDate()));

//        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();

        disneyPlusAppleTVWelcomeScreenPage.clickSignUpButton();
        disneyPlusAppleTVSignUpPage.isOpened();

        boolean isKr = locale.equalsIgnoreCase("kr");
        disneyPlusAppleTVSignUpPage.selectCheckBoxesForKr(isKr);
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(apiProvider.get().getUniqueUserEmail());
        if (languageUtils.get().isSubscriberAgreementRequired()) {
            if (!disneyPlusAppleTVPasswordPage.isPasswordEntryFieldPresent(15))
                disneyPlusAppleTVPasswordPage.clickSelect();
        }
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(account.getUserPass());
//        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVCompletePurchasePage.isOpened();
        disneyPlusAppleTVCompletePurchasePage.clickBack();
        if (disneyPlusAppleTVCompletePurchasePage.isAlertDefaultBtnPresent()) {
            disneyPlusAppleTVCompletePurchasePage.clickDefaultAlertBtn();
        }
//        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVCompletePurchasePage.isCustomButtonPresent();
        pause(5);
        getScreenshots(count++ + "-OneStepAwayScreen", baseDirectory);
        disneyPlusAppleTVCompletePurchasePage.clickLogoutButton();
        disneyPlusAppleTVWelcomeScreenPage.isViewAlertPresent();
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        getScreenshots(count++ + "-WelcomeScreen", baseDirectory);

        clearAppCache();

//        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.isOpened();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(account.getEmail());
        if (languageUtils.get().isSubscriberAgreementRequired()) {
            if (!disneyPlusAppleTVPasswordPage.isPasswordEntryFieldPresent(15))
                disneyPlusAppleTVPasswordPage.clickSelect();
        }
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(account.getUserPass());
//        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVRestartSubscriptionPage.isLocalizedPageWithPrimaryButtonOpened();
        pause(15);
        getScreenshots(count + "-WelcomeBack", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(description = "Basic IAP flow", enabled = false)
    public void capturePurchaseFlow() {
        if(false) {
            skipExecution("Test run is not against IAP compatible build.");
        }

        baseDirectory.set("Screenshots-CapturePurchaseFlow/");
        pathToZip.set(String.format("Capture_Purchase_%s_%s_%s.zip", language.toUpperCase(), locale, getDate()));
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
