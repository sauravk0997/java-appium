package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.util.TestGroup;
import org.openqa.selenium.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusAddProfileIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusCreatePasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDOBCollectionPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPaywallIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSignUpIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.agent.core.annotation.TestLabel;
import com.zebrunner.carina.utils.R;

import java.lang.invoke.MethodHandles;

public class DisneyPlusIAPStandardPurchaseTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73696"})
    @Test(description = "Standard purchase with a new account for Basic sku", groups = {"Ariel-Purchase", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyStandardPurchase_Basic() {
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
        initPage(DisneyPlusCreatePasswordIOSPageBase.class).submitPasswordValue(getAccount().getUserPass());
        if (!dobCollectionPage.isOpened()) {
            dobCollectionPage.getTypeButtonByLabel("done").clickIfPresent();
            initPage(DisneyPlusCreatePasswordIOSPageBase.class).tapSignUpButton();
        }
        setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();
        //Purchase plan
        paywallIOSPageBase.waitForPresenceOfAnElement(paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.BASIC));
        paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.BASIC).click(SHORT_TIMEOUT);
        paywallIOSPageBase.isOpened();
        paywallIOSPageBase.clickPurchaseButton(DisneyPlusPaywallIOSPageBase.PlanType.BASIC);
        paywallIOSPageBase.waitForSubscribeOverlay();
        paywallIOSPageBase.clickOverlaySubscribeButton();
        try {
            paywallIOSPageBase.submitSandboxPassword(R.TESTDATA.getDecrypted("sandbox_pw"));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
        paywallIOSPageBase.clickOkBtn();
        paywallIOSPageBase.dismissNotificationsPopUp();
        //Create profile
        addProfilePage.createProfileForNewUser(DEFAULT_PROFILE);
        //More thrills and drama continue button
        if (addProfilePage.getTypeButtonByLabel("CONTINUE").isPresent()) {
            clickElementAtLocation(addProfilePage.getTypeButtonByLabel("CONTINUE"), 50, 50);
        }
        //Not now button
        addProfilePage.clickSecondaryButtonByCoordinates();
        pause(3);
        addProfilePage.clickMoreTab();
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        accountPage.isSingleSubHeaderPresent();
        sa.assertTrue(accountPage.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.BASIC), "plan name on account page is not displayed as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73697"})
    @Test(description = "Standard purchase with a new account for Premium monthly sku", groups = {"Ariel-Purchase", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyStandardPurchase_Premium_Monthly() {
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
        initPage(DisneyPlusCreatePasswordIOSPageBase.class).submitPasswordValue(getAccount().getUserPass());
        if (!dobCollectionPage.isOpened()) {
            dobCollectionPage.getTypeButtonByLabel("done").clickIfPresent();
            initPage(DisneyPlusCreatePasswordIOSPageBase.class).tapSignUpButton();
        }
        setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();
        pause(3);
        //Purchase plan
        paywallIOSPageBase.waitForPresenceOfAnElement(paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY));
        paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY).click(SHORT_TIMEOUT);
        paywallIOSPageBase.isOpened();
        paywallIOSPageBase.clickPurchaseButton(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY);
        paywallIOSPageBase.waitForSubscribeOverlay();
        paywallIOSPageBase.clickOverlaySubscribeButton();
        try {
            paywallIOSPageBase.submitSandboxPassword(R.TESTDATA.getDecrypted("sandbox_pw"));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
        paywallIOSPageBase.clickOkBtn();
        paywallIOSPageBase.dismissNotificationsPopUp();
        //Create profile
        addProfilePage.createProfileForNewUser(DEFAULT_PROFILE);
        //More thrills and drama continue button
        if (addProfilePage.getTypeButtonByLabel("CONTINUE").isPresent()) {
            clickElementAtLocation(addProfilePage.getTypeButtonByLabel("CONTINUE"), 50, 50);
        }
        //Not now button
        addProfilePage.clickSecondaryButtonByCoordinates();
        pause(3);
        addProfilePage.clickMoreTab();
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        accountPage.isSingleSubHeaderPresent();
        sa.assertTrue(accountPage.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY), "plan name on account page is not displayed as expected");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72741"})
    @Test(description = "Standard purchase with a new account for Premium yearly sku", groups = {"Ariel-Purchase", TestGroup.PRE_CONFIGURATION }, enabled = false)
    public void verifyStandardPurchase_Premium_Yearly() {
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
        initPage(DisneyPlusCreatePasswordIOSPageBase.class).submitPasswordValue(getAccount().getUserPass());
        if (!dobCollectionPage.isOpened()) {
            dobCollectionPage.getTypeButtonByLabel("done").clickIfPresent();
            initPage(DisneyPlusCreatePasswordIOSPageBase.class).tapSignUpButton();
        }
        setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();
        //Purchase plan
        paywallIOSPageBase.waitForPresenceOfAnElement(paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY));
        paywallIOSPageBase.getSelectButtonFor(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY).click(SHORT_TIMEOUT);
        paywallIOSPageBase.isOpened();
        paywallIOSPageBase.clickPurchaseButton(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY);
        paywallIOSPageBase.waitForSubscribeOverlay();
        paywallIOSPageBase.clickOverlaySubscribeButton();
        try {
            paywallIOSPageBase.submitSandboxPassword(R.TESTDATA.getDecrypted("sandbox_pw"));
        } catch (NoSuchElementException nse) {
            LOGGER.info("Sandbox password was not prompted. Device may have it cached from a prior test run.");
        }
        paywallIOSPageBase.clickOkBtn();
        pause(3);
        paywallIOSPageBase.dismissNotificationsPopUp();
        //Create profile
        addProfilePage.createProfileForNewUser(DEFAULT_PROFILE);
        //More thrills and drama continue button
        if (addProfilePage.getTypeButtonByLabel("CONTINUE").isPresent()) {
            clickElementAtLocation(addProfilePage.getTypeButtonByLabel("CONTINUE"), 50, 50);
        }
        //Not now button
        addProfilePage.clickSecondaryButtonByCoordinates();
        pause(3);
        addProfilePage.clickMoreTab();
        initPage(DisneyPlusMoreMenuIOSPageBase.class).clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        accountPage.isSingleSubHeaderPresent();
        sa.assertTrue(accountPage.isPlanNameDisplayed(DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY), "plan name on account page is not displayed as expected");
        sa.assertAll();
    }
}
