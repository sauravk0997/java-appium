package com.disney.qa.tests.disney.apple.ios.localization;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.pojos.DisneyOrder;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.ios_settings.IOSSettingsMenuBase;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.util.TestGroup;
import com.disney.util.disney.DisneyGlobalUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;

public class DisneyPlusAppleLocalizationCaptures extends DisneyPlusAppleLocalizationBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test(dataProvider = "tuidGenerator", description = "Capture Welcome, One Step Away, and Welcome Back screens", groups = {"Onboarding - Backgrounds",
            TestGroup.PRE_CONFIGURATION, TestGroup.PROXY })
    private void captureOnboardingBackgrounds(String TUID) {
        setup();
        setZipTestName("Onboarding_Backgrounds");
        boolean isArielRegion = getLocalizationUtils().getCountryName().equals("United States");
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusCompleteSubscriptionIOSPageBase completeSubscriptionIOSPageBase = initPage(DisneyPlusCompleteSubscriptionIOSPageBase.class);

        //Create an expired account
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(getLocalizationUtils().getLocale()).language(getLocalizationUtils().getUserLanguage()).build();
        List<DisneyOrder> orderList = new LinkedList<>();
        orderList.add(DisneyOrder.SET_EXPIRED);
        request.setOrderSettings(orderList);
        DisneyOffer disneyOffer = getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V1").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccount = getAccountApi().createAccount(request);

        //Dismiss system alert and take WelcomePage screenshot
        welcomeScreenIOSPageBase.waitUntil(ExpectedConditions.elementToBeClickable(welcomeScreenIOSPageBase.getSignUpButtonBy()), 30);
        getScreenshots("WelcomePage");

        //Navigate through Sign Up/Finish Later and take OneStepAway screenshot
        welcomeScreenIOSPageBase.clickSignUpButton();
        initPage(DisneyPlusSignUpIOSPageBase.class).submitEmailAddress(generateGmailAccount());
        pause(5);
        initPage(DisneyPlusSignUpIOSPageBase.class).clickAgreeAndContinueIfPresent();
        passwordPage.typePassword("1234AB!@");
        dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();
        pause(3);
        if (isArielRegion) {
            setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
            initPage(DisneyPlusSignUpIOSPageBase.class).clickPrimaryButton();
            passwordPage.clickAlertConfirm();
        } else {
            completeSubscriptionIOSPageBase.clickCancelBtn();
            completeSubscriptionIOSPageBase.clickAlertConfirm();
        }

        pause(2);
        getScreenshots("OneStepAway");
        completeSubscriptionIOSPageBase.clickCancelBtn();
        completeSubscriptionIOSPageBase.clickAlertConfirm();

        loginDismiss(testAccount);
        pause(5);
        getScreenshots("WelcomeBack");
    }

    @Test(dataProvider = "tuidGenerator", description = "Capture Welcome, One Step Away, and Welcome Back screens", groups = {"Onboarding - Full", TestGroup.PRE_CONFIGURATION, TestGroup.PROXY})
    public void captureFullOnboardingFlow(String TUID) {
        setup();
        setZipTestName("Onboarding_Full");
        boolean isArielRegion = getLocalizationUtils().getCountryName().equals("United States");

        DisneyPlusDOBCollectionPageBase dobPageBase = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusSignUpIOSPageBase signUpPage = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyplusLegalIOSPageBase legalPage = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallPage = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusCompleteSubscriptionIOSPageBase completeSubscriptionPage = initPage(DisneyPlusCompleteSubscriptionIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoseWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase avatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);

        getScreenshots("LandingPage");
        welcomePage.clickSignUpButton();
        pause(3);
        getScreenshots("EnterEmail");
        createPasswordPage.openPrivacyPolicyLink();
        pause(3);
        getScreenshots("PrivacyPolicy");
        legalPage.getBackArrow().click();

        /**
         * If the subscriber agreement is a part of onboarding, the hyperlink is not present in this location.
         * Increases count by 1 to keep the expected numbering
         */
        boolean checkboxRequired = new DisneyGlobalUtils().getBooleanFromCountries(getLocalizationUtils().getLocale(), "isEmailCheckBoxOptInCountry");

        if (!getLocalizationUtils().isSubscriberAgreementRequired() && !checkboxRequired) {
            createPasswordPage.openSubscriberAgreement();
            pause(3);
            getScreenshots("SubscriberAgreement");
            legalPage.getBackArrow().click();
        }

        if (checkboxRequired) {
            signUpPage.clickUncheckedBoxes();
        }

        signUpPage.submitEmailAddress("");
        pause(1);
        getScreenshots("Error1");
        signUpPage.submitEmailAddress(generateGmailAccount());
        pause(3);

        /**
         * If the subscriber agreement is a part of onboarding, it will be displayed at this point.
         * Resets the count to 5 (expected value) to keep the expected numbering
         */
        if (getLocalizationUtils().isSubscriberAgreementRequired()) {
            getScreenshots("1SubscriberAgreement");
            signUpPage.clickAgreeAndContinue();
            pause(3);
        }

        getScreenshots("CreatePW");
        createPasswordPage.submitPasswordValue("");
        pause(1);
        getScreenshots("Error2");
        createPasswordPage.submitPasswordValue("123");
        pause(1);
        getScreenshots("Error3");
        createPasswordPage.enterPasswordValue("fair1234");
        pause(1);
        getScreenshots("Fair");
        createPasswordPage.enterPasswordValue("fair12345");
        pause(1);
        getScreenshots("Good");
        createPasswordPage.enterPasswordValue("fair123456!");
        pause(1);
        getScreenshots("Great");
        createPasswordPage.enterPasswordValue(getAccount().getUserPass());
        dismissKeyboardForPhone();
        createPasswordPage.clickPrimaryButton();
        pause(3);

        if (isArielRegion) {
            getScreenshots("DateOfBirthPage");
            dobPageBase.enterDOB("01/01/3000");
            pause(2);
            getScreenshots("DateOfBirthError");
            dobPageBase.clickSystemAlertSecondaryBtn();
            welcomePage.clickSignUpButton();
            signUpPage.submitEmailAddress(generateGmailAccount());
            createPasswordPage.enterPasswordValue(getAccount().getUserPass());
            dismissKeyboardForPhone();
            createPasswordPage.clickPrimaryButton();
            dobPageBase.enterDOB("01/01/1980");
            paywallPage.isOpened();
        } else {
            paywallPage.clickCancelBtn();
        }

        getScreenshots("Paywall");
        paywallPage.clickAlertConfirm();
        pause(2);
        getScreenshots("OneStepAway");
        completeSubscriptionPage.getDismissButton().click();
        completeSubscriptionPage.waitUntil(
                ExpectedConditions.elementToBeClickable(completeSubscriptionPage.getSystemAlertDefaultBtn().getBy()),
                30);
        completeSubscriptionPage.clickAlertConfirm();
        loginDismiss(getAccount());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickAddProfile();
        avatarPage.clickSkipButton();
        hideKeyboard();
        pause(2);
        getScreenshots("AddProfilePage");

        if (isArielRegion) {
            pause(2);
            addProfilePage.clickGenderDropDown();
            getScreenshots("GenderOptions", false);
        }
    }

    @Test(dataProvider = "tuidGenerator", description = "Capture IAP related images", groups = {"Onboarding - IAP", TestGroup.PRE_CONFIGURATION, TestGroup.PROXY}, enabled = false)
    public void capturePurchaseFlow(String TUID) {
        setup();
        boolean isArielRegion = getLocalizationUtils().getCountryName().equals("United States");

        if (buildType != BuildType.IAP) {
            skipExecution("Test run is not against IAP compatible build.");
        }
        initPage(IOSSettingsMenuBase.class).cancelActiveEntitlement("Disney+");
        relaunch();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        DisneyPlusSignUpIOSPageBase signUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);

        if (new DisneyGlobalUtils().getBooleanFromCountries(getLocalizationUtils().getLocale(), "isEmailCheckBoxOptInCountry")) {
            LOGGER.info("Checkbox needed");
            signUpIOSPageBase.clickUncheckedBoxes();
        }

        signUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        signUpIOSPageBase.clickAgreeAndContinueIfPresent();
        initPage(DisneyPlusCreatePasswordIOSPageBase.class).submitPasswordValue(getAccount().getUserPass());

        if(isArielRegion) {
            setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
            signUpIOSPageBase.clickAgreeAndContinue();
        }

        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);

        if (isArielRegion) {
            pause(3);
            getScreenshots("PlanSelection");
            paywallIOSPageBase.clickBasicPlan();
        }

        paywallIOSPageBase.isOpened();
        getScreenshots("SkuSelection");
        paywallIOSPageBase.clickPurchaseButton();

        paywallIOSPageBase.waitForSubscribeOverlay();
        getScreenshots("SandboxSubscribe");
        paywallIOSPageBase.clickOverlaySubscribeButton();
        try {
            paywallIOSPageBase.submitSandboxPassword("G0Disney!");
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
        acceptAlert();
        acceptAlert();

        initPage(DisneyPlusWhoseWatchingIOSPageBase.class).isOpened();
        getScreenshots("ProfileSelect");
        UniversalUtils.archiveAndUploadsScreenshots(BASE_DIRECTORY.get(), PATH_TO_ZIP.get());
    }

    @Test(dataProvider = "tuidGenerator", description = "Capture Ariel onboarding images", groups = {"Onboarding - Ariel", TestGroup.PRE_CONFIGURATION, TestGroup.PROXY})
    public void captureArielOnboarding(String TUID) {
        setup();
        setZipTestName("Onboarding_Ariel");

        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusSignUpIOSPageBase signUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase createPasswordPage = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallPage = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);

        welcomePage.clickSignUpButton();

        if (new DisneyGlobalUtils().getBooleanFromCountries(getLocalizationUtils().getLocale(), "isEmailCheckBoxOptInCountry")) {
            LOGGER.info("Checkbox needed");
            signUpIOSPageBase.clickUncheckedBoxes();
        }

        //S1.1
        pause(2);
        getScreenshots("emailPage");

        //S1.2
        signUpIOSPageBase.submitEmailAddress("badEmail");
        pause(2);
        getScreenshots("badEmailError");

        //S1.3
        signUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        pause(2);
        getScreenshots("passwordPage");
        signUpIOSPageBase.clickAgreeAndContinueIfPresent();

        //S1.4 & S1.5
        createPasswordPage.submitPasswordValue("");
        pause(1);
        getScreenshots("emptyPasswordError");
        createPasswordPage.submitPasswordValue("123");
        pause(1);
        getScreenshots("badPasswordError");
        createPasswordPage.enterPasswordValue("fair1234");
        pause(1);
        getScreenshots("fair");
        createPasswordPage.enterPasswordValue("fair12345");
        pause(1);
        getScreenshots("good");
        createPasswordPage.enterPasswordValue("fair123456!");
        pause(1);
        getScreenshots("great");

        createPasswordPage.submitPasswordValue(getAccount().getUserPass());

        //S1.7
        pause(2);
        getScreenshots("birthdatePage");

        signUpIOSPageBase.clickPrimaryButton();
        getScreenshots("birthdatePageEmptyError");

        setBirthDate(Person.MINOR.getMonth().getText(), Person.MINOR.getDay(), Person.MINOR.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();
        pause(2);
        getScreenshots("minorError");

        signUpIOSPageBase.clickSystemAlertSecondaryBtn();

        //S1.8
        welcomePage.clickSignUpButton();
        signUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        createPasswordPage.submitPasswordValue(getAccount().getUserPass());
        setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();
        pause(2);
        getScreenshots("chooseYourPlanPage");

        //S1.9
        paywallPage.getDynamicRowButtonLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUB_SELECTOR_STANDALONE_ADS_CTA.getText()),1).click();
        pause(2);
        getScreenshots("basicPlan");

        paywallPage.getBackArrow().click();
        paywallPage.getDynamicRowButtonLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUB_SELECTOR_STANDALONE_NOADS_CTA.getText()),2).click();
        pause(2);
        getScreenshots("premiumPlan");

        //1.10
        paywallPage.clickCancelBtn();
        paywallPage.getSystemAlertDefaultBtn().click();
        pause(3);
        getScreenshots("oneStepAway");

        //1.11
        paywallPage.clickCancelBtn();
        paywallPage.getSystemAlertDefaultBtn().click();

        welcomePage.clickSignUpButton();
        signUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        signUpIOSPageBase.clickAgreeAndContinueIfPresent();
        initPage(DisneyPlusCreatePasswordIOSPageBase.class).submitPasswordValue(getAccount().getUserPass());

        setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();

        paywallPage.getDynamicRowButtonLabel(getLocalizationUtils()
                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.PAYWALL, DictionaryKeys.SUB_SELECTOR_STANDALONE_ADS_CTA.getText()),1).click();

        paywallPage.isOpened();
        paywallPage.clickPurchaseButton();

        paywallPage.waitForSubscribeOverlay();
        paywallPage.clickOverlaySubscribeButton();
        try {
            paywallPage.submitSandboxPassword("G0Disney!");
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }

        acceptAlert();
        acceptAlert();

        pause(3);
        getScreenshots("profilePage");

        swipe(addProfilePage.getSaveButton());
        addProfilePage.tapSaveButton();
        pause(2);
        getScreenshots("genderError");

        addProfilePage.clickGenderDropDown();
        getScreenshots("genderOptions");
    }
}