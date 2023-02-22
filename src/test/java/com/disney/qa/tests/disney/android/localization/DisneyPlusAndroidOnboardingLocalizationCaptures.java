package com.disney.qa.tests.disney.android.localization;

import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusLoginPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusPaywallPageBase;
import com.disney.qa.disney.android.pages.common.DisneyPlusWelcomePageBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DisneyPlusAndroidOnboardingLocalizationCaptures extends DisneyLocalizationBase {

    @BeforeMethod(alwaysRun = true)
    private void setup() {
        setJarvisOverrides();
        disneyAccount.set(accountApi.get().createExpiredAccount("Yearly", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage(), "V1"));
        clearAppCache();
        verifyConnectionIsGood();
    }

    @Test(dataProvider = "tuidGenerator", groups = {"Onboarding - Backgrounds"})
    public void captureSignupAndExpiredAccountBackgrounds(String TUID) {
        setDirectories("Onboarding_Backgrounds");
        count.set(1);

        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        welcomePageBase.isOpened();
        boolean iapAvailable = false;
        if(!isFireTablet()) {
            iapAvailable = welcomePageBase.isPurchaseAvailable(languageUtils.get().getNoPurchaseSubtextValues(languageUtils.get().isSelectedLanguageSupported()));
        }
        welcomePageBase.proceedToSignUp();
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        loginPageBase.waitForLoading();

        checkSignupLoads(loginPageBase, welcomePageBase);

        loginPageBase.registerNewEmail(generateNewUserEmail());
        loginPageBase.clickConfirmIfPresent();
        loginPageBase.registerPassword();
        DisneyPlusPaywallPageBase paywallPageBase = initPage(DisneyPlusPaywallPageBase.class);
        paywallPageBase.waitForLoading();

        checkPaywallLoads(paywallPageBase, loginPageBase);

        if (iapAvailable) {
            loginPageBase.clickActionButton();
            paywallPageBase.clickConfirmButton();
        }
        pause(3);
        getScreenshots("OneStepAway");

        paywallPageBase.clickLogout();
        paywallPageBase.clickConfirmButton();

        initPage(DisneyPlusWelcomePageBase.class).isOpened();
        getScreenshots("WelcomePage");

        login(disneyAccount.get(), false);
        initPage(DisneyPlusPaywallPageBase.class).waitForLoading();
        getScreenshots("WelcomeBack");

        UniversalUtils.archiveAndUploadsScreenshots(baseDirectory.get(), pathToZip.get());
    }

    @Test(dataProvider = "tuidGenerator", groups = {"Onboarding - Full"})
    public void getOnboardingAndRenewSubscriptionBackgrounds(String TUID) {
        setDirectories("Onboarding_Full");
        count.set(0);
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        welcomePageBase.isOpened();
        boolean iapAvailable = false;
        if(!isFireTablet()) {
            iapAvailable = welcomePageBase.isPurchaseAvailable(languageUtils.get().getNoPurchaseSubtextValues(languageUtils.get().isSelectedLanguageSupported()));
        }
        pause(3);
        getScreenshots("LandingPage");
        welcomePageBase.proceedToSignUp();
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        loginPageBase.waitForLoading();

        checkSignupLoads(loginPageBase, welcomePageBase);

        pause(3);
        androidUtils.get().hideKeyboard();
        getScreenshots("EnterEmail");

        //TODO Add hyperlink navigation and captures
        //Do Privacy Policy
        if (!languageUtils.get().isSubscriberAgreementRequired()) {
            //Do Subscriber Agreement
        }
        count.set(4);
        loginPageBase.registerNewEmail("");
        loginPageBase.waitForLoading();
        getScreenshots("Error1");
        loginPageBase.getEditTextByClass().type(generateNewUserEmail());
        androidUtils.get().hideKeyboard();
        loginPageBase.clickAgreeAndContinueButton();

        if (languageUtils.get().isSubscriberAgreementRequired()) {
            loginPageBase.waitForLoading();
            getScreenshots("1SubscriberAgreement");
            loginPageBase.clickStandardButton();
            count.set(5);
        }

        loginPageBase.waitForLoading();
        getScreenshots("CreatePW");
        loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        loginPageBase.registerPassword("");
        loginPageBase.waitForLoading();
        getScreenshots("Error2");
        loginPageBase.getEditTextByClass().type("123");
        pause(1);
        getScreenshots("Error3");
        loginPageBase.getEditTextByClass().type("abcd1234");
        pause(1);
        getScreenshots("Fair");
        loginPageBase.getEditTextByClass().type("abcd123456");
        pause(1);
        getScreenshots("Good");
        loginPageBase.getEditTextByClass().type("abcd123456!");
        pause(1);
        getScreenshots("Great");
        loginPageBase.clickAgreeAndContinueButton();
        DisneyPlusPaywallPageBase paywallPageBase = initPage(DisneyPlusPaywallPageBase.class);
        paywallPageBase.waitForLoading();
        pause(3);

        checkPaywallLoads(paywallPageBase, loginPageBase);

        if (iapAvailable) {
            getScreenshots("Paywall");
            paywallPageBase.clickActionButton();
            pause(1);
            getScreenshots("FinishLater");
            paywallPageBase.clickConfirmButton();
        }

        count.set(12);
        paywallPageBase.waitForLoading();
        pause(3);
        getScreenshots("1OneStepAway");
        paywallPageBase.clickLogout();
        pause(1);
        getScreenshots("LogOut");
        paywallPageBase.clickConfirmButton();
        login(disneyAccount.get(), false);
        paywallPageBase.waitForLoading();
        getScreenshots("WelcomeBack");

        if (iapAvailable) {
            paywallPageBase.clickStandardButton();
            paywallPageBase.clickStandardButton();
            paywallPageBase.waitForLoading();
            pause(3);
            getScreenshots("RestartPaywall");
        }

        UniversalUtils.archiveAndUploadsScreenshots(baseDirectory.get(), pathToZip.get());
    }

    private void checkSignupLoads(DisneyPlusLoginPageBase loginPageBase, DisneyPlusWelcomePageBase welcomePageBase) {
        if (!loginPageBase.isSignUpScreenDisplayed()) {
            int tries = 0;
            boolean success;
            do {
                tries++;
                loginPageBase.clickStandardButton();
                welcomePageBase.proceedToSignUp();
                loginPageBase.waitForLoading();
                success = loginPageBase.isOpened();
            } while (!success && tries < 3);

            if (!success) {
                Assert.fail("The network appears to be unstable. Unable to proceed past Signup.");
            }
        }
    }

    private void checkPaywallLoads(DisneyPlusPaywallPageBase paywallPageBase, DisneyPlusLoginPageBase loginPageBase) {
        if (!paywallPageBase.isOpened()) {
            int tries = 0;
            boolean success;
            do {
                tries++;
                paywallPageBase.clickStandardButton();
                loginPageBase.registerNewEmail(generateNewUserEmail());
                loginPageBase.clickConfirmIfPresent();
                loginPageBase.registerPassword();
                paywallPageBase.waitForLoading();
                success = paywallPageBase.isOpened();
            } while (!success && tries < 3);

            if (!success) {
                Assert.fail("The network appears to be unstable. Unable to proceed past paywall.");
            }
        }
    }
}
