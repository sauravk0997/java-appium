package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import java.lang.invoke.MethodHandles;
import java.util.List;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.util.TestGroup;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.alice.AliceDriver;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.utils.ios_settings.IOSSettingsMenuBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;

public class DisneyPlusIAPSubscriptionTest extends DisneyBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String DOB_ADULT = "01/01/1983";
    private static final String DOB_CHILD = "01/01/2018";
    private static final String PRETTY_FREEKIN_SCARY = "Pretty Freekin Scary";
    //    private String genderPreferNotToSay = getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.GENDER_PREFER_TO_NOT_SAY.getText());
    @DataProvider(name = "disneyPlanTypes")
    public Object[][] disneyPlanTypes() {
        return new Object[][]{{DisneyPlusPaywallIOSPageBase.PlanType.BASIC},
                {DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY},
                {DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY}
        };
    }
    @DataProvider(name = "disneyPlanCards")
    public Object[][] disneyPlanCards() {
        return new Object[][]{{DisneyPlusPaywallIOSPageBase.PlanType.BASIC},
                {DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY}
        };
    }
    @DataProvider(name = "disneyWebPlanTypes")
    public Object[][] disneyWebPlanTypes() {
        return new Object[][]{{"Disney+ With Ads, Hulu with Ads, and ESPN+", DisneyPlusPaywallIOSPageBase.PlanType.BUNDLE_TRIO_BASIC},
                {"Disney+, Hulu No Ads, and ESPN+", DisneyPlusPaywallIOSPageBase.PlanType.BUNDLE_TRIO_PREMIUM},
                {"Disney Bundle Duo Basic", DisneyPlusPaywallIOSPageBase.PlanType.LEGACY_BUNDLE}
        };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72376"})
    @Test(description = "Standard purchase with a new account on all SKUs", dataProvider = "disneyPlanTypes", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyIAPDisneyPlanCards(DisneyPlusPaywallIOSPageBase.PlanType planType) {
        if (buildType != BuildType.IAP) {
            skipExecution("Test run is not against IAP compatible build.");
        }

        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        DisneyPlusSignUpIOSPageBase signUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        SoftAssert sa = new SoftAssert();
        signUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        signUpIOSPageBase.clickAgreeAndContinueIfPresent();
        initPage(DisneyPlusCreatePasswordIOSPageBase.class).submitPasswordValue(getAccount().getUserPass());
        sa.assertTrue(dobCollectionPage.isOpened(), "DOB collection page didn't open after signing up");
        setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();
        sa.assertTrue(paywallIOSPageBase.isChooseYourPlanHeaderPresent(), "Choose your plan card 'title' is not as expected");
        sa.assertTrue(paywallIOSPageBase.isChooseYourPlanSubHeaderPresent(), "Choose your plan card 'subtitle' is not as expected");
        sa.assertTrue(paywallIOSPageBase.isFooterLabelPresent(), "Choose your plan card 'footer label' is not as expected");
        sa.assertTrue(paywallIOSPageBase.verifyPlanCardFor(planType), "Plan card UI is not as expected");
        paywallIOSPageBase.getSelectButtonFor(planType).click();
        sa.assertTrue(paywallIOSPageBase.isOpened(), "paywall screen didn't load");
        sa.assertTrue(paywallIOSPageBase.isPurchaseButtonPresent(), "user was not taken to the billing cycle screen");
        paywallIOSPageBase.tapBackButton();
        paywallIOSPageBase.tapBackButton();
        paywallIOSPageBase.tapFinishLaterButton();
        sa.assertTrue(initPage(DisneyPlusCompleteSubscriptionIOSPageBase.class).getCompleteSubscriptionButton().isPresent(), "Complete subscription  page is not shown after clicking finish later alert");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62022"})
    @Test(description = "Sign Up - Paywall - User taps Cancel", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyPaywallCancel() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase disneyPlusPaywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);

        SoftAssert sa = new SoftAssert();

        disneyPlusWelcomeScreenIOSPageBase.clickSignUpButton();
        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusCreatePasswordIOSPageBase.submitPasswordValue("abcd123!@");
        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_ADULT);

        disneyPlusPaywallIOSPageBase.getBackArrow().click();
        sa.assertTrue(disneyPlusPaywallIOSPageBase.isFinishLaterHeaderPresent(),
                "Finish later header is not displayed.");
        sa.assertTrue(disneyPlusPaywallIOSPageBase.isFinishLaterTextPresent(),
                "Finish later text is not displayed.");
        sa.assertTrue(disneyPlusPaywallIOSPageBase.isResumeButtonPresent(),
                "RESUME button is not displayed.");
        sa.assertTrue(disneyPlusPaywallIOSPageBase.isFinishLaterButtonPresent(),
                "FINISH LATER button is not displayed.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62020"})
    @Test(description = "Sign Up - Verify Paywall UI", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyPaywallUI() {
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        SoftAssert sa = new SoftAssert();

        disneyPlusWelcomeScreenIOSPageBase.clickSignUpButton();
        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusSignUpIOSPageBase.clickUncheckedBoxes();
        disneyPlusSignUpIOSPageBase.clickAgreeAndContinue();
        disneyPlusSignUpIOSPageBase.clickAgreeAndContinue();
        disneyPlusCreatePasswordIOSPageBase.submitPasswordValue("abcd123!@");
        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_ADULT);
        paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY).click();

        sa.assertTrue(paywallIOSPageBase.isYearlySkuButtonPresent(),
                "Yearly SKU button is not displayed.");
        sa.assertTrue(paywallIOSPageBase.isMonthlySkuButtonPresent(),
                "Monthly SKU button is not displayed.");
        sa.assertTrue(paywallIOSPageBase.isPaywallCancelButtonDisplayed(),
                "Cancel button is not displayed.");
        sa.assertTrue(paywallIOSPageBase.isStartStreamingTextDisplayed(), "Start Streaming Text is not displayed.");

        sa.assertTrue(paywallIOSPageBase.isDisneyPlusPremiumTextDisplayed(),
                "Cancel anytime text is not displayed.");
        sa.assertTrue(paywallIOSPageBase.restoreBtn.isElementPresent(),
                "Restore Purchase button is not displayed.");
        aliceDriver.screenshotAndRecognize().isLabelPresent(sa, "disney_logo");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-62193"})
    @Test(description = "Log in - Verify Restart Subscription Paywall UI", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyRestartSubscriptionPaywallUI() {
        SoftAssert softAssert = new SoftAssert();
        AliceDriver aliceDriver = new AliceDriver(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase disneyPlusWelcomeScreenIOSPageBase = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusRestartSubscriptionIOSPageBase disneyPlusRestartSubscriptionIOSPageBase = initPage(DisneyPlusRestartSubscriptionIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallPage = initPage(DisneyPlusPaywallIOSPageBase.class);

        DisneyAccount expiredAccount = getAccountApi().createExpiredAccount("Yearly", "US", "en", "V1");
        disneyPlusWelcomeScreenIOSPageBase.clickLogInButton();
        login(expiredAccount);

        disneyPlusRestartSubscriptionIOSPageBase.clickRestartSubscriptionButton();
        paywallPage.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY).click();
        softAssert.assertTrue(paywallPage.isYearlySkuButtonPresent(),
                "Yearly SKU button is not displayed.");
        softAssert.assertTrue(paywallPage.isMonthlySkuButtonPresent(),
                "Monthly SKU button is not displayed.");
        softAssert.assertTrue(paywallPage.isPaywallCancelButtonDisplayed(),
                "Cancel button is not displayed.");
        softAssert.assertTrue(paywallPage.isRestartsSubscriptionHeaderDisplayed(),
                "Restart Subscription header is not displayed.");
        softAssert.assertTrue(paywallPage.isRestartsSubscriptionSubHeaderDisplayed(),
                "'You will be billed immediately. Restart anytime.' is not displayed. ");
        aliceDriver.screenshotAndRecognize().isLabelPresent(softAssert, "disney_logo");
        softAssert.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73539"})
    @Test(description = "Complete subscription for a returning user without a subscription, selects ads plan", dataProvider = "disneyPlanCards", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyReturningUserCompletesSubscription(DisneyPlusPaywallIOSPageBase.PlanType planName) {
        DisneyAccount nonActiveAccount = getAccountApi().createAccount("US", "en");
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusCompleteSubscriptionIOSPageBase CompleteSubsPage = initPage(DisneyPlusCompleteSubscriptionIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallPage = initPage(DisneyPlusPaywallIOSPageBase.class);
        welcomePage.dismissNotificationsPopUp();
        welcomePage.clickLogInButton();
        login(nonActiveAccount);

        // Complete subscription page verification
        sa.assertTrue(CompleteSubsPage.getHeroImage().isPresent(), "Hero image is not present");
        sa.assertTrue(CompleteSubsPage.getPrimaryText().isPresent(), "Primary text not present");
        sa.assertTrue(CompleteSubsPage.getSecondaryText().isPresent(), "Secondary text not present");
        sa.assertTrue(CompleteSubsPage.getCompleteSubscriptionButton().isPresent(), "Complete Subscription button is not present");
        //aliceDriver.screenshotAndRecognize().isLabelPresent(sa, "disney_logo");
        sa.assertTrue(welcomePage.isLogOutButtonDisplayed(), "Expected: 'Log out' button should be present");
        welcomePage.clickCompleteSubscriptionButton();

        // Verify choose your plan page
        sa.assertTrue(paywallPage.isChooseYourPlanHeaderPresent(), "Choose your plan card 'title' is not present");
        sa.assertTrue(paywallPage.isChooseYourPlanSubHeaderPresent(), "Choose your plan card 'subtitle' is not present");
        sa.assertTrue(paywallPage.isFooterLabelPresent(), "Choose your plan card 'footer label' is not present");
        sa.assertTrue(paywallPage.verifyPlanCardFor(planName), "Plan card UI is not as expected");
        sa.assertTrue(paywallPage.getSelectButtonFor(planName).isPresent(), "Select button is not present");

        //Choose plan
        paywallPage.getSelectButtonFor(planName).click();
        paywallPage.isStartStreamingTextPresent();
        //1. Test Resume and finish later actioned by Cancel button on billing cycle page
        paywallPage.clickCancelBtn();
        //Resume button on alert
        paywallPage.clickSystemAlertSecondaryBtn();
        pause(1);
        sa.assertTrue(paywallPage.isStartStreamingTextPresent(), "After resuming, billing cycle screen is not displayed");
        //Finish Later button on alert
        paywallPage.clickCancelBtn();
        paywallPage.clickDefaultAlertBtn();
        sa.assertTrue(CompleteSubsPage.getCompleteSubscriptionButton().isPresent(), "Complete subscription button not present after clicking 'finish later' on 'billing cycle screen'");
        //2. verify navigation for back button on billing cycle screen
        welcomePage.clickCompleteSubscriptionButton();
        paywallPage.getSelectButtonFor(planName).click();
        paywallPage.getBackArrow().click();
        sa.assertTrue(paywallPage.isChooseYourPlanHeaderPresent(), "Choose your plan card 'title' is not as expected");
        //3. Test Resume and finish later actioned by Cancel button on Choose your plan  page
        paywallPage.getBackArrow().click();
        //Resume button on alert
        paywallPage.clickSystemAlertSecondaryBtn();
        sa.assertTrue(paywallPage.isChooseYourPlanHeaderPresent(), "Choose your plan card 'title' is not as expected");
        paywallPage.getBackArrow().click();
        //Finish Later button on alert
        paywallPage.clickDefaultAlertBtn();
        sa.assertTrue(CompleteSubsPage.getCompleteSubscriptionButton().isPresent(), "Complete subscription button not present after clicking 'finish later' on 'Choose Your Plan' screen");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-66591"})
    @Test(description = "Verify valid password submissions and hide/show button", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyValidPasswordSubmissions() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusCreatePasswordIOSPageBase disneyPlusCreatePasswordIOSPageBase = initPage(DisneyPlusCreatePasswordIOSPageBase.class);
        DisneyPlusApplePageBase disneyPlusApplePageBase = initPage(DisneyPlusApplePageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        verifySignUpButtonNavigation();
        SoftAssert sa = new SoftAssert();

        disneyPlusSignUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        disneyPlusCreatePasswordIOSPageBase.enterPasswordValue("1234AB!@");
        disneyPlusCreatePasswordIOSPageBase.clickShowHidePassword();

        sa.assertEquals(disneyPlusCreatePasswordIOSPageBase.getPasswordEntryText(), "1234AB!@",
                "XMOBQA-62237 - Show/Hide Password did not un-hide the password value");

        disneyPlusCreatePasswordIOSPageBase.clickShowHidePassword();

        sa.assertEquals(disneyPlusCreatePasswordIOSPageBase.getPasswordEntryText(), "••••••••",
                "XMOBQA-62237 - Show/Hide Password did not hide the password value");

        disneyPlusApplePageBase.clickPrimaryButton();
        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_ADULT);

        paywallIOSPageBase.waitForPresenceOfAnElement(paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.BASIC));
        sa.assertTrue(paywallIOSPageBase.isChooseYourPlanHeaderPresent(), "XMOBQA-62241-Choose your plan card 'title' is not as expected");
        sa.assertTrue(paywallIOSPageBase.isChooseYourPlanSubHeaderPresent(), "XMOBQA-62241-Choose your plan card 'subtitle' is not as expected");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73609"})
    @Test(description = "Verify Web Offer/Plan Name", dataProvider = "disneyWebPlanTypes", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyWebOfferNames(String offerName, DisneyPlusPaywallIOSPageBase.PlanType planName) {
        setAccount(getAccountApi().createAccount(offerName, getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage(), SUBSCRIPTION_V2_ORDER));
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        SoftAssert sa = new SoftAssert();
        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        accountPage.isSingleSubHeaderPresent();
        sa.assertTrue(accountPage.isWebPlanNameDisplayed(planName), "plan name on account page is not displayed as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73647","XMOBQA-73648"})
    @Test(description = "When the returning user with expired subscription authenticates via login flow or sign up flow, selects ads plan", dataProvider = "disneyPlanCards", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyExpiredAccountSelectsSubscription(DisneyPlusPaywallIOSPageBase.PlanType planName) {
        SoftAssert sa = new SoftAssert();
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusRestartSubscriptionIOSPageBase restartSubs = initPage(DisneyPlusRestartSubscriptionIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallPage = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyAccount expired = getAccountApi().createExpiredAccount("Yearly", "US", "en", "V1");
        welcomePage.dismissNotificationsPopUp();
        welcomePage.clickLogInButton();
        login(expired);

        // Restart Subscription Page verification
        sa.assertTrue(restartSubs.getHeroImage().isPresent(), "hero not present");
        sa.assertTrue(restartSubs.getPrimaryText().isPresent(), "primary text not present");
        sa.assertTrue(restartSubs.getSecondaryText().isPresent(), "secondary text not present");
        sa.assertTrue(restartSubs.getRestartSubscriptionButton().isPresent(), "button not present");
        sa.assertTrue(welcomePage.isLogOutButtonDisplayed(), "Expected: 'Log out' button should be present");
        //aliceDriver.screenshotAndRecognize().isLabelPresent(sa, "disney_logo");
        restartSubs.getRestartSubscriptionButton().click();

        // Verify choose your plan page
        sa.assertTrue(paywallPage.isChooseYourPlanHeaderPresent(), "Choose your plan card 'title' is not present");
        sa.assertTrue(paywallPage.isChooseYourPlanSubHeaderPresent(), "Choose your plan card 'subtitle' is not present");
        sa.assertTrue(paywallPage.isFooterLabelPresent(), "Choose your plan card 'footer label' is not present");
        sa.assertTrue(paywallPage.verifyPlanCardFor(planName), "Plan card UI is not as expected");
        sa.assertTrue(paywallPage.getSelectButtonFor(planName).isPresent(), "Select button is not present");

        //Choose plan
        paywallPage.getSelectButtonFor(planName).click();
        //1. Test Resume and finish later actioned by Cancel button on billing cycle page
        paywallPage.clickCancelBtn();
        pause(2);
        //Resume button on alert
        paywallPage.clickSystemAlertSecondaryBtn();
        pause(5);
        sa.assertTrue(paywallPage.isRestartsSubscriptionHeaderDisplayed(), "Restart Subscription header is not displayed after selecting Resume on alert");
        //Finish Later button on alert
        paywallPage.clickCancelBtn();
        paywallPage.clickDefaultAlertBtn();
        pause(3);
        sa.assertTrue(restartSubs.getRestartSubscriptionButton().isPresent(), "Restart subscription button not present after clicking 'finish later' on 'billing cycle screen'");
        //2. verify navigation for back button on billing cycle screen
        welcomePage.clickCompleteSubscriptionButton();
        paywallPage.getSelectButtonFor(planName).click();
        paywallPage.getBackArrow().click();
        sa.assertTrue(paywallPage.isChooseYourPlanHeaderPresent(), "Choose your plan card 'title' is not as expected");
        //3. Test Resume and finish later actioned by Cancel button on Choose your plan  page
        paywallPage.getBackArrow().click();
        //Resume button on alert
        paywallPage.clickSystemAlertSecondaryBtn();
        sa.assertTrue(paywallPage.isChooseYourPlanHeaderPresent(), "Choose your plan card 'title' is not as expected");
        paywallPage.getBackArrow().click();
        //Finish Later button on alert
        paywallPage.clickDefaultAlertBtn();
        sa.assertTrue(restartSubs.getRestartSubscriptionButton().isPresent(), "Restart subscription button not present after clicking 'finish later' on 'Choose Your Plan' screen");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72389"})
    @Test(description = "Verify plan switch from basic monthly with ads to premium monthly with no ads", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyPlanSwitchBasicMonthlyToPremiumMonthly() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAccountIOSPageBase account = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywall = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusHomeIOSPageBase home = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase video = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        IOSSettingsMenuBase iosSettings = initPage(IOSSettingsMenuBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY_BASIC_22,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        //Validate ads in video player
        home.clickSearchIcon();
        search.searchForMedia(PRETTY_FREEKIN_SCARY);
        List<ExtendedWebElement> results = search.getDisplayedTitles();
        results.get(0).click();
        pause(3);
        details.clickPlayButton();
        sa.assertTrue(video.isAdBadgeLabelPresent(), "Ad badge label not present after video began");
        video.clickBackButton();

        //Account - Validate Basic Ads is displayed
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        sa.assertTrue(account.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.BASIC), "Basic Plan not displayed");

        //Switch to Premium Monthly
        account.clickChangeBamtechBasicPlan();
        paywall.clickBundleSelectButton();
        paywall.clickPurchaseButton(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY);
        paywall.clickOverlaySubscribeButton();
        pause(3);
        try {
            paywall.submitSandboxPassword(R.TESTDATA.getDecrypted("sandbox_pw"));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
        pause(5);
        acceptAlert();
        sa.assertTrue(account.isSubscriptionChangeFlashMessagePresent(), "Subscription change flash message did not appear");
        paywall.dismissNotificationsPopUp();
        pause(3);

        //Validate no ad badge in player after switch
        home.clickSearchIcon();
        if(details.getPlayButton().isPresent()) {
            details.clickPlayButton();
        } else {
            details.clickContinueButton();
        }
        sa.assertFalse(video.isAdBadgeLabelPresent(), "Ad badge label present after video began");
        video.clickBackButton();
        details.isOpened();

        //TODO: IOS-5556 - switch to Premium Monthly is not updated in Account Settings under Subscription.
//        sa.assertTrue(account.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY), "Premium Monthly plan type not displayed");

        //Validate in ios native settings the plan has been switched as alternative solution
        iosSettings.navigateToManageSubscription();
        pause(2);
        sa.assertTrue(iosSettings.isPremiumMonthlyPriceCheckmarkPresent(), "Premium Monthly Price with checkmark not displayed.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72782"})
    @Test(description = "Verify plan switch from premium monthly no ads to premium yearly with no ads", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyPlanSwitchPremiumMonthlyToPremiumYearly() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAccountIOSPageBase account = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywall = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusHomeIOSPageBase home = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase video = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        IOSSettingsMenuBase iosSettings = initPage(IOSSettingsMenuBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY_PREMIUM_22,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        //Validate ads in video player
        home.clickSearchIcon();
        search.searchForMedia(PRETTY_FREEKIN_SCARY);
        List<ExtendedWebElement> results = search.getDisplayedTitles();
        results.get(0).click();
        details.clickPlayButton();
        sa.assertFalse(video.isAdBadgeLabelPresent(), "Ad badge label not present after video began");
        video.clickBackButton();

        //Account - Validate Basic Ads is displayed
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        sa.assertTrue(account.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY), "Premium Monthly Plan is not displayed");

        //Switch to Premium Monthly
        account.clickChangePremiumMonthlyPlan();
        paywall.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY).click();
        paywall.clickPurchaseButton(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY);
        paywall.clickOverlaySubscribeButton();
        try {
            paywall.submitSandboxPassword(R.TESTDATA.getDecrypted("sandbox_pw"));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
        pause(10);
        acceptAlert();
        sa.assertTrue(account.isSubscriptionChangeFlashMessagePresent(), "Subscription change flash message did not appear");
        paywall.dismissNotificationsPopUp();

        //Validate no ad badge in player after switch
        pause(2);
        home.clickSearchIcon();
        if(details.getPlayButton().isPresent()) {
            details.clickPlayButton();
        } else {
            details.clickContinueButton();
        }
        sa.assertFalse(video.isAdBadgeLabelPresent(), "Ad badge label present after video began");
        video.clickBackButton();
        details.isOpened();

        //TODO: IOS-5556 - switch to Premium Yearly is not updated in Account Settings under Subscription.
//        sa.assertTrue(account.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY), "Premium Yearly plan type not displayed");

        //Validate in ios native settings the plan has been switched as alternative solution
        iosSettings.navigateToManageSubscription();
        sa.assertTrue(iosSettings.isPremiumYearlyPriceCheckmarkPresent(), "Premium Monthly Price with checkmark not displayed.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72362"})
    @Test(description = "Verify plan switch from basic monthly with ads to premium yearly with no ads", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyPlanSwitchBasicMonthlyToPremiumYearly() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusMoreMenuIOSPageBase moreMenu = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAccountIOSPageBase account = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywall = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusHomeIOSPageBase home = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase video = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusSearchIOSPageBase search = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase details = initPage(DisneyPlusDetailsIOSPageBase.class);
        IOSSettingsMenuBase iosSettings = initPage(IOSSettingsMenuBase.class);
        setAccount(createAccountWithSku(DisneySkuParameters.DISNEY_IAP_APPLE_MONTHLY_BASIC_22,
                getLocalizationUtils().getLocale(), getLocalizationUtils().getUserLanguage()));

        setAppToHomeScreen(getAccount(), getAccount().getProfiles().get(0).getProfileName());

        //Validate ads in video player
        home.clickSearchIcon();
        search.searchForMedia(PRETTY_FREEKIN_SCARY);
        List<ExtendedWebElement> results = search.getDisplayedTitles();
        results.get(0).click();
        details.clickPlayButton();
        sa.assertTrue(video.isAdBadgeLabelPresent(), "Ad badge label not present after video began");
        video.clickBackButton();

        //Account - Validate Basic Ads is displayed
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenu.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        sa.assertTrue(account.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.BASIC), "Basic Plan not displayed");

        //Switch to Premium Monthly
        account.clickChangeBamtechBasicPlan();
        paywall.clickBundleSelectButton();
        paywall.clickPurchaseButton(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY);
        paywall.clickOverlaySubscribeButton();
        try {
            paywall.submitSandboxPassword(R.TESTDATA.getDecrypted("sandbox_pw"));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
        pause(10);
        acceptAlert();
        sa.assertTrue(account.isSubscriptionChangeFlashMessagePresent(), "Subscription change flash message did not appear");
        paywall.dismissNotificationsPopUp();

        //Validate no ad badge in player after switch
        home.clickSearchIcon();
        if(details.getPlayButton().isPresent()) {
            details.clickPlayButton();
        } else {
            details.clickContinueButton();
        }
        sa.assertFalse(video.isAdBadgeLabelPresent(), "Ad badge label present after video began");
        video.clickBackButton();
        details.isOpened();

        //TODO: IOS-5556 - switch to Premium Yearly is not updated in Account Settings under Subscription.
//        sa.assertTrue(account.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY), "Premium Monthly plan type not displayed");

        //Validate in ios native settings the plan has been switched as alternative solution
        iosSettings.navigateToManageSubscription();
        sa.assertTrue(iosSettings.isPremiumYearlyPriceCheckmarkPresent(), "Premium Yearly Price with checkmark not displayed.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72385"})
    @Test(description = "Log in - Verify sign up - DOB Over 18", groups = {"Ariel-IAP", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void testSignUpDOBOver18() {
        if (buildType != BuildType.IAP) {
            skipExecution("Test run is not against IAP compatible build.");
        }
        SoftAssert sa = new SoftAssert();
        DisneyPlusDOBCollectionPageBase dobCollectionPage = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        DisneyPlusPaywallIOSPageBase paywallPage = new DisneyPlusPaywallIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());
        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        sa.assertTrue(welcomeScreen.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        welcomeScreen.clickCompleteSubscriptionButton();

        dobCollectionPage.isOpened();
        dobCollectionPage.enterDOB(DOB_ADULT);
        pause(5);
        sa.assertTrue(paywallPage.isChooseYourPlanHeaderPresent(), "Choose your plan card 'title' is not as expected");
        sa.assertTrue(paywallPage.isChooseYourPlanSubHeaderPresent(), "Choose your plan card 'subtitle' is not as expected");
        sa.assertAll();
    }

    private void verifySignUpButtonNavigation() {
        DisneyPlusSignUpIOSPageBase disneyPlusSignUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        Assert.assertTrue(disneyPlusSignUpIOSPageBase.isOpened(),
                "'Sign Up' did not open the email submission screen as expected");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72668"})
    @Test(description = "SUF – Password prompt when action grant expires", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void testPasswordPromptAfterActionGrantExpiresAdultDOB() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDOBCollectionPageBase dobCollectionPage = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        DisneyPlusPaywallIOSPageBase paywallPage = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        sa.assertTrue(welcomeScreen.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        welcomeScreen.clickCompleteSubscriptionButton();

        dobCollectionPage.isOpened();
        dobCollectionPage.keepSessionAlive(15, dobCollectionPage.getDateOfBirthHeader());
        pause(35);
        dobCollectionPage.enterDOB(DOB_ADULT);
        sa.assertTrue(passwordPage.isForgotPasswordLinkDisplayed(),
                "Forgot Password Link did not appear.");
        passwordPage.clickForgotPasswordLink();
        passwordPage.tapBackButton();
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        if(paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.BASIC).isPresent()){
            paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.BASIC).click();
            sa.assertTrue(paywallIOSPageBase.isOpened(), "paywall screen didn't load");
        }
        sa.assertTrue(paywallPage.isOpened(),
                "Paywall Page did not open.");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-74950"})
    @Test(description = "SUF – Password prompt when action grant expires", groups = {"Onboarding", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void testPasswordPromptAfterActionGrantExpiresU18DOB() {
        SoftAssert sa = new SoftAssert();
        DisneyPlusDOBCollectionPageBase dobCollectionPage = new DisneyPlusDOBCollectionPageBase(getDriver());
        DisneyPlusLoginIOSPageBase loginPage = new DisneyPlusLoginIOSPageBase(getDriver());
        DisneyPlusPasswordIOSPageBase passwordPage = new DisneyPlusPasswordIOSPageBase(getDriver());
        DisneyPlusWelcomeScreenIOSPageBase welcomeScreen = new DisneyPlusWelcomeScreenIOSPageBase(getDriver());
        CreateDisneyAccountRequest createDisneyAccountRequest = new CreateDisneyAccountRequest();
        DisneyPlusAccountIsMinorIOSPageBase accountIsMinorPage = new DisneyPlusAccountIsMinorIOSPageBase(getDriver());

        createDisneyAccountRequest
                .setDateOfBirth(null)
                .setGender(null)
                .setCountry(getLocalizationUtils().getLocale())
                .setLanguage(getLocalizationUtils().getUserLanguage());

        setAccount(getAccountApi().createAccount(createDisneyAccountRequest));

        welcomeScreen.clickLogInButton();
        loginPage.submitEmail(getAccount().getEmail());
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        sa.assertTrue(welcomeScreen.isCompleteSubscriptionButtonDisplayed(),
                "Complete Subscription Button did not appear.");
        welcomeScreen.clickCompleteSubscriptionButton();

        dobCollectionPage.isOpened();
        dobCollectionPage.keepSessionAlive(15, dobCollectionPage.getDateOfBirthHeader());
        pause(35);
        dobCollectionPage.enterDOB(DOB_CHILD);
        sa.assertTrue(passwordPage.isForgotPasswordLinkDisplayed(),
                "Forgot Password Link did not appear.");
        passwordPage.clickForgotPasswordLink();
        passwordPage.tapBackButton();
        passwordPage.submitPasswordForLogin(getAccount().getUserPass());
        sa.assertTrue(accountIsMinorPage.isOpened(),
                "Account Minor Page did not open.");
        sa.assertAll();
    }
}
