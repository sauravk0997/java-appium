package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.NoSuchElementException;


import static com.disney.qa.common.constant.TimeConstant.SHORT_TIMEOUT;

public class DisneyPlusIAPStandardPurchaseTest extends DisneyBaseTest {

//    public static final List<SandboxAccount> accountsList = new LinkedList() {
//        {
//            new AppStoreConnectApi().getSandboxAccounts(SANDBOX_ACCOUNT_PREFIX);
//        }
//    };


    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73696"})
    @Maintainer("gkrishna1")
    @Test(description = "Standard purchase with a new account for Basic sku", groups = {"Ariel-Purchase"})
    public void verifyStandardPurchase_Basic() {
        initialSetup();
        if (buildType != BuildType.IAP) {
            skipExecution("Test run is not against IAP compatible build.");
        }
        //String appleID = initPage(IOSSettingsMenuBase.class).getDeviceSandBoxAppleID();
        //clearDSSSandboxAccountFor(appleID);
        DisneyPlusSignUpIOSPageBase signUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        relaunch();
        signUpIOSPageBase.dismissNotificationsPopUp();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        signUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        signUpIOSPageBase.clickAgreeAndContinueIfPresent();
        initPage(DisneyPlusCreatePasswordIOSPageBase.class).submitPasswordValue(disneyAccount.get().getUserPass());
        if (!dobCollectionPage.isOpened()) {
            dobCollectionPage.getTypeButtonByLabel("done").clickIfPresent();
            initPage(DisneyPlusCreatePasswordIOSPageBase.class).tapSignUpButton();
        }
        iosUtils.get().setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();
        //Purchase plan
        paywallIOSPageBase.waitForPresenceOfAnElement(paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.BASIC));
        paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.BASIC).click(SHORT_TIMEOUT);
        paywallIOSPageBase.isOpened();
        paywallIOSPageBase.clickPurchaseButton(DisneyPlusPaywallIOSPageBase.PlanType.BASIC);
        paywallIOSPageBase.waitForSubscribeOverlay();
        paywallIOSPageBase.clickOverlaySubscribeButton();
        try {
            CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));
            paywallIOSPageBase.submitSandboxPassword(cryptoTool.decrypt(R.TESTDATA.get("sandbox_pw")));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
        iosUtils.get().acceptAlert();
        iosUtils.get().acceptAlert();
        paywallIOSPageBase.dismissNotificationsPopUp();
        //Create profile
        addProfilePage.createProfileForNewUser(DEFAULT_PROFILE);
        //More thrills and drama continue button
        if (addProfilePage.getTypeButtonByLabel("CONTINUE").isPresent()) {
            new MobileUtilsExtended().clickElementAtLocation(addProfilePage.getTypeButtonByLabel("CONTINUE"), 50, 50);
        }
        //Not now button
        addProfilePage.clickSecondaryButtonByCoordinates();
        addProfilePage.clickPrimaryButton();
        pause(3);
        addProfilePage.clickPrimaryButton();
        addProfilePage.clickMoreTab();
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        accountPage.isSingleSubHeaderPresent();
        sa.assertTrue(accountPage.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.BASIC), "plan name on account page is not displayed as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73697"})
    @Maintainer("gkrishna1")
    @Test(description = "Standard purchase with a new account for Premium monthly sku", groups = {"Ariel-Purchase"})
    public void verifyStandardPurchase_Premium_Monthly() {
        initialSetup();
        if (buildType != BuildType.IAP) {
            skipExecution("Test run is not against IAP compatible build.");
        }
        DisneyPlusSignUpIOSPageBase signUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        relaunch();
        signUpIOSPageBase.dismissNotificationsPopUp();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        signUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        signUpIOSPageBase.clickAgreeAndContinueIfPresent();
        initPage(DisneyPlusCreatePasswordIOSPageBase.class).submitPasswordValue(disneyAccount.get().getUserPass());
        if (!dobCollectionPage.isOpened()) {
            dobCollectionPage.getTypeButtonByLabel("done").clickIfPresent();
            initPage(DisneyPlusCreatePasswordIOSPageBase.class).tapSignUpButton();
        }
        iosUtils.get().setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();
        //Purchase plan
        paywallIOSPageBase.waitForPresenceOfAnElement(paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY));
        paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY).click(SHORT_TIMEOUT);
        paywallIOSPageBase.isOpened();
        paywallIOSPageBase.clickPurchaseButton(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY);
        paywallIOSPageBase.waitForSubscribeOverlay();
        paywallIOSPageBase.clickOverlaySubscribeButton();
        try {
            CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));
            paywallIOSPageBase.submitSandboxPassword(cryptoTool.decrypt(R.TESTDATA.get("sandbox_pw")));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
        iosUtils.get().acceptAlert();
        iosUtils.get().acceptAlert();
        paywallIOSPageBase.dismissNotificationsPopUp();
        //Create profile
        addProfilePage.createProfileForNewUser(DEFAULT_PROFILE);
        //More thrills and drama continue button
        if (addProfilePage.getTypeButtonByLabel("CONTINUE").isPresent()) {
            new MobileUtilsExtended().clickElementAtLocation(addProfilePage.getTypeButtonByLabel("CONTINUE"), 50, 50);
        }
        //Not now button
        addProfilePage.clickSecondaryButtonByCoordinates();
        addProfilePage.clickPrimaryButton();
        pause(3);
        addProfilePage.clickPrimaryButton();
        addProfilePage.clickMoreTab();
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        accountPage.isSingleSubHeaderPresent();
        sa.assertTrue(accountPage.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY), "plan name on account page is not displayed as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72741"})
    @Maintainer("gkrishna1")
    @Test(description = "Standard purchase with a new account for Premium yearly sku", groups = {"Ariel-Purchase"})
    public void verifyStandardPurchase_Premium_Yearly() {
        initialSetup();
        if (buildType != BuildType.IAP) {
            skipExecution("Test run is not against IAP compatible build.");
        }
        DisneyPlusSignUpIOSPageBase signUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

        relaunch();
        signUpIOSPageBase.dismissNotificationsPopUp();
        initPage(DisneyPlusWelcomeScreenIOSPageBase.class).clickSignUpButton();
        signUpIOSPageBase.submitEmailAddress(generateGmailAccount());
        signUpIOSPageBase.clickAgreeAndContinueIfPresent();
        initPage(DisneyPlusCreatePasswordIOSPageBase.class).submitPasswordValue(disneyAccount.get().getUserPass());
        if (!dobCollectionPage.isOpened()) {
            dobCollectionPage.getTypeButtonByLabel("done").clickIfPresent();
            initPage(DisneyPlusCreatePasswordIOSPageBase.class).tapSignUpButton();
        }
        iosUtils.get().setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();
        //Purchase plan
        paywallIOSPageBase.waitForPresenceOfAnElement(paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY));
        paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY).click(SHORT_TIMEOUT);
        paywallIOSPageBase.isOpened();
        paywallIOSPageBase.clickPurchaseButton(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY);
        paywallIOSPageBase.waitForSubscribeOverlay();
        paywallIOSPageBase.clickOverlaySubscribeButton();
        try {
            CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));
            paywallIOSPageBase.submitSandboxPassword(cryptoTool.decrypt(R.TESTDATA.get("sandbox_pw")));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
        iosUtils.get().acceptAlert();
        iosUtils.get().acceptAlert();
        pause(3);
        paywallIOSPageBase.dismissNotificationsPopUp();
        //Create profile
        addProfilePage.createProfileForNewUser(DEFAULT_PROFILE);
        //More thrills and drama continue button
        if (addProfilePage.getTypeButtonByLabel("CONTINUE").isPresent()) {
            new MobileUtilsExtended().clickElementAtLocation(addProfilePage.getTypeButtonByLabel("CONTINUE"), 50, 50);
        }
        //Not now button
        addProfilePage.clickSecondaryButtonByCoordinates();
        pause(1);
        addProfilePage.clickPrimaryButton();
        pause(3);
        addProfilePage.clickPrimaryButton();
        addProfilePage.clickMoreTab();
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        accountPage.isSingleSubHeaderPresent();
        sa.assertTrue(accountPage.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY), "plan name on account page is not displayed as expected");
        sa.assertAll();
    }
}
