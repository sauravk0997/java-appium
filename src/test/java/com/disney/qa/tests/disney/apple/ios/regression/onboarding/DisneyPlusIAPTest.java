package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.appstoreconnect.AppStoreConnectApi;
import com.disney.qa.api.pojos.sandbox.SandboxAccount;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.common.utils.ios_settings.IOSSettingsMenuBase;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.NoSuchElementException;

import java.util.LinkedList;
import java.util.List;

import static com.disney.qa.common.constant.TimeConstant.SHORT_TIMEOUT;

public class DisneyPlusIAPTest extends DisneyBaseTest {

    public static final List<SandboxAccount> accountsList = new LinkedList() {
        { new AppStoreConnectApi().getSandboxAccounts(SANDBOX_ACCOUNT_PREFIX);}
    };

    @DataProvider(name = "disneyPlanTypes")
    public Object[][] disneyPlanTypes() {
        return new Object[][]{{DisneyPlusPaywallIOSPageBase.PlanType.BASIC},
                {DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_MONTHLY},
                {DisneyPlusPaywallIOSPageBase.PlanType.PREMIUM_YEARLY}
                };
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72376"})
    @Maintainer("gkrishna1")
    @Test(description = "Standard purchase with a new account on all SKUs", dataProvider = "disneyPlanTypes", groups = {"Ariel-IAP"})
    public void verifyIAPDisneyPlanCards(DisneyPlusPaywallIOSPageBase.PlanType planType) {
        initialSetup();
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
        initPage(DisneyPlusCreatePasswordIOSPageBase.class).submitPasswordValue(disneyAccount.get().getUserPass());
        sa.assertTrue(dobCollectionPage.isOpened(), "DOB collection page didn't open after signing up");
        iosUtils.get().setBirthDate(Person.ADULT.getMonth().getText(), Person.ADULT.getDay(), Person.ADULT.getYear());
        signUpIOSPageBase.clickAgreeAndContinue();
        sa.assertTrue(paywallIOSPageBase.isChooseYourPlanHeaderPresent(), "Choose your plan card 'title' is not as expected");
        sa.assertTrue(paywallIOSPageBase.isChooseYourPlanSubHeaderPresent(), "Choose your plan card 'subtitle' is not as expected");
        sa.assertTrue(paywallIOSPageBase.isFooterLabelPresent(), "Choose your plan card 'footer label' is not as expected");
        sa.assertTrue(paywallIOSPageBase.verifyPlanCardFor(planType), "Plan card UI is not as expected");
        paywallIOSPageBase.getSelectButtonFor(planType).click();
        paywallIOSPageBase.isOpened();
        sa.assertTrue(paywallIOSPageBase.isPurchaseButtonPresent(), "user was not taken to the billing cycle screen");
        paywallIOSPageBase.tapBackButton();
        paywallIOSPageBase.tapBackButton();
        paywallIOSPageBase.tapFinishLaterButton();
        sa.assertTrue(initPage(DisneyPlusCompleteSubscriptionIOSPageBase.class).getCompleteSubscriptionButton().isPresent(), "Complete subscription  page is not shown after clicking finish later alert");
        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-72376"})
    @Maintainer("gkrishna1")
    @Test(description = "Standard purchase with a new account on all SKUs", dataProvider = "disneyPlanTypes", groups = {"Ariel-IAP"})
    public void verifyStandardPurchase(DisneyPlusPaywallIOSPageBase.PlanType planType) {

        initialSetup();
        if (buildType != BuildType.IAP) {
            skipExecution("Test run is not against IAP compatible build.");
        }
        String appleID = initPage(IOSSettingsMenuBase.class).getDeviceSandBoxAppleID();
        clearDSSSandboxAccountFor(appleID);
        relaunch();
        DisneyPlusSignUpIOSPageBase signUpIOSPageBase = initPage(DisneyPlusSignUpIOSPageBase.class);
        DisneyPlusPaywallIOSPageBase paywallIOSPageBase = initPage(DisneyPlusPaywallIOSPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPage = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        SoftAssert sa = new SoftAssert();

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
        paywallIOSPageBase.waitForPresenceOfAnElement(paywallIOSPageBase.getSelectButtonFor(planType));
        paywallIOSPageBase.getSelectButtonFor(planType).click(SHORT_TIMEOUT);
        paywallIOSPageBase.isOpened();
        paywallIOSPageBase.clickPurchaseButton(planType);
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
        sa.assertTrue(accountPage.isPlanNameDisplayed(planType), "plan name on account page is not displayed as expected");
        sa.assertAll();
    }
}
