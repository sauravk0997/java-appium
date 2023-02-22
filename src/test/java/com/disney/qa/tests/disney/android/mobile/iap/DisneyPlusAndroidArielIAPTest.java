package com.disney.qa.tests.disney.android.mobile.iap;

import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.common.google_play_store.android.pages.common.GooglePlayHandler;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.disney.android.pages.common.*;
import com.disney.qa.disney.android.pages.phone.DisneyPlusCompleteProfilePage;
import com.disney.qa.disney.web.DisneyWebParameters;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.fasterxml.jackson.databind.JsonNode;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

//TODO: Refactor JsonNode usage to reference languageUtils objects
public class DisneyPlusAndroidArielIAPTest extends BaseDisneyTest {

    private static final String OVER18 = "03311999";
    private static final String NEW_PASSWORD = "G0D1sn3yQ@";

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-71676"})
    @Test(description = "Onboarding flow with Existing Unentitled D+ account", groups = {"IAP"})
    public void testMonthlyPurchaseFlowWithPriorAccount(){
        DisneyPlusCompletePurchasePageBase completePurchasePageBase = initPage(DisneyPlusCompletePurchasePageBase.class);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusPlanSelectPageBase planSelectPageBase = initPage(DisneyPlusPlanSelectPageBase.class);
        DisneyPlusPaywallPageBase paywallPageBase = initPage(DisneyPlusPaywallPageBase.class);
        GooglePlayHandler googlePlayHandler = initPage(GooglePlayHandler.class);

        iapSetup();
        verifyPurchaseOptionAvailable(new DisneyApiProvider().getFullDictionaryBody(languageUtils.get().getUserLanguage()));
        login(accountApi.get().createAccount(languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()), false);
        completePurchasePageBase.clickCompletePurchaseBtn();
        planSelectPageBase.isOpened();
        planSelectPageBase.clickPlanSelect(0);
        paywallPageBase.clickOnGenericTextElement(getPaywallMonthlyButtonText());
        googlePlayHandler.approvePurchase();
        googlePlayHandler.clickNowNowBtnIfPresent();

        Assert.assertTrue(discoverPageBase.isOpened(),
                "Discover screen not displayed");
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67067"})
    @Test(description = "Onboarding flow with D+ account signup", groups = {"IAP"})
    public void testMonthlyPurchaseFlowWithSignup(){
        DisneyPlusDOBCollectionPageBase dobCollectionPageBase = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusPlanSelectPageBase planSelectPageBase = initPage(DisneyPlusPlanSelectPageBase.class);
        DisneyPlusCompleteProfilePageBase completeProfilePageBase = initPage(DisneyPlusCompleteProfilePageBase.class);

        iapSetup();
        SoftAssert sa = new SoftAssert();
        JsonNode appDictionary = new DisneyApiProvider().getFullDictionaryBody(languageUtils.get().getUserLanguage());
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);

        verifyPurchaseOptionAvailable(appDictionary);
        welcomePageBase.proceedToSignUp();

        DisneyPlusPaywallPageBase paywallPageBase = initPage(DisneyPlusLoginPageBase.class)
                .registerNewEmail(generateNewUserEmail())
                .registerPassword();

        dobCollectionPageBase.submitDOBValue(OVER18);
        planSelectPageBase.isOpened();
        planSelectPageBase.clickPlanSelect(0);
        paywallPageBase.clickOnGenericTextElement(getPaywallMonthlyButtonText());

        GooglePlayHandler googlePlayHandler = initPage(GooglePlayHandler.class);
        googlePlayHandler.approvePurchase();
        googlePlayHandler.clickNowNowBtnIfPresent();

        completeProfilePageBase.clickGenderSelector();
        completeProfilePageBase.selectFirstGender();
        completeProfilePageBase.swipeUpOnScreen(1);
        completeProfilePageBase.clickSave();

        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        Assert.assertTrue(maturityPageBase.isOpened(),
                "User was not directed to Profile Migration after purchase");

        maturityPageBase.clickStarNotNowBtn();
        maturityPageBase.clickStandardButton();
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        Assert.assertTrue(discoverPageBase.isAddProfilesBannerVisible(),
                "User was not prompted to add additional profiles to their account on Home page");

        discoverPageBase.dismissAddProfilesPopup();
        sa.assertTrue(discoverPageBase.isOpened(),
                "Expected - User to land on Discover after purchase");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67335"})
    @Test(description = "Restore Flow", groups = {"IAP"})
    public void testRestoreFlow(){
        iapSetup();
        SoftAssert sa = new SoftAssert();
        JsonNode fullDictionary = new DisneyContentApiChecker().getFullDictionaryBody(languageUtils.get().getUserLanguage());
        AndroidUtilsExtended util = new AndroidUtilsExtended();

        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusLoginPageBase loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        DisneyPlusCreatePasswordPageBase createPasswordPageBase = initPage(DisneyPlusCreatePasswordPageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPageBase = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusPlanSelectPageBase planSelectPageBase = initPage(DisneyPlusPlanSelectPageBase.class);
        DisneyPlusCompleteProfilePage completeProfilePageBase = initPage(DisneyPlusCompleteProfilePage.class);
        DisneyPlusDiscoverPageBase discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        DisneyPlusMaturityPageBase maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);
        DisneyPlusSearchPageBase searchPageBase = initPage(DisneyPlusSearchPageBase.class);
        GooglePlayHandler googlePlayHandler = initPage(GooglePlayHandler.class);

        LOGGER.info("Performing initial registration and purchase...");

        welcomePageBase.proceedToSignUp();

        DisneyPlusPaywallPageBase paywallPageBase = initPage(DisneyPlusLoginPageBase.class)
                .registerNewEmail(generateNewUserEmail())
                .registerPassword();

        dobCollectionPageBase.submitDOBValue(OVER18);
        planSelectPageBase.isOpened();
        planSelectPageBase.clickPlanSelect(0);
        paywallPageBase.clickOnGenericTextElement(getPaywallMonthlyButtonText());
        googlePlayHandler.approvePurchase();
        googlePlayHandler.clickNowNowBtnIfPresent();

        completeProfilePageBase.isOpened();
        completeProfilePageBase.clickGenderSelector();
        completeProfilePageBase.selectFirstGender();
        completeProfilePageBase.swipeUpOnScreen(1);
        completeProfilePageBase.clickSave();

        Assert.assertTrue(maturityPageBase.isOpened(),
                "User was not directed to Profile Migration after purchase");

        maturityPageBase.clickStarNotNowBtn();
        maturityPageBase.clickStandardButton();

        discoverPageBase.dismissAddProfilesPopup();

        LOGGER.info("Performing Restore with second Disney+ Account registration...");
        androidUtils.get().clearAppCache();
        activityAndPackageLaunch();

        String secondUser = generateNewUserEmail();
        welcomePageBase.proceedToSignUp();
        loginPageBase.registerNewEmail(secondUser);
        createPasswordPageBase.submitNewPassword(NEW_PASSWORD);
        dobCollectionPageBase.isDOBTitleDisplayed();
        dobCollectionPageBase.submitDOBValue(OVER18);
        planSelectPageBase.isOpened();
        planSelectPageBase.clickPlanSelect(0);
        paywallPageBase.clickRestorePurchaseBtn();

        boolean linkErrorOccurred = maturityPageBase.isErrorPresent();
        sa.assertTrue(linkErrorOccurred, "2nd Registration did not throw Error Code 30");

        paywallPageBase.clickPositiveBtn();
        completeProfilePageBase.isOpened();
        completeProfilePageBase.clickGenderSelector();
        completeProfilePageBase.selectFirstGender();
        completeProfilePageBase.swipeUpOnScreen(1);
        completeProfilePageBase.clickSave();

        Assert.assertTrue(maturityPageBase.isOpened(),
                "User was not directed to Profile Migration after purchase");

        maturityPageBase.clickStarNotNowBtn();
        maturityPageBase.clickStandardButton();
        discoverPageBase.dismissAddProfilesPopup();
        Assert.assertTrue(discoverPageBase.isOpened(),
                "Expected - Second Account to be allowed access to the app");

        dismissChromecastOverlay();
        discoverPageBase.navigateToPage(new DisneyContentApiChecker().getDictionaryItemValue(fullDictionary, DisneyPlusCommonPageBase.MenuItem.SEARCH.getText()));
        searchPageBase.searchForMedia("Soul");
        searchPageBase.openFirstSearchResultItem();
        initPage(DisneyPlusMediaPageBase.class).startPlayback();
        sa.assertTrue(initPage(DisneyPlusVideoPageBase.class).isOpened(),
                "Second Account was not allowed media playback during logged in session.");

        util.clearAppCache();
        activityAndPackageLaunch();
        LOGGER.info("Logging in with second Disney+ account after clearing app data...");
        manualLogin(secondUser, NEW_PASSWORD);

        sa.assertTrue(initPage(DisneyPlusCompletePurchasePageBase.class).isOpened(),
                "Expected - Paywall to be displayed after entering Second Account credentials after clearing data");

        sa.assertAll();
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-67331"})
    @Test(description = "Restore Flow - Nothing to Restore Check", groups = {"IAP"})
    public void testRestoreWithoutEntitlement(){
        iapSetup();
        SoftAssert sa = new SoftAssert();
        DisneyContentApiChecker apiChecker = new DisneyContentApiChecker();
        JsonNode fullDictionary = apiChecker.getFullDictionaryBody(languageUtils.get().getUserLanguage());
        DisneyPlusWelcomePageBase welcomePageBase = initPage(DisneyPlusWelcomePageBase.class);
        DisneyPlusDOBCollectionPageBase dobCollectionPageBase = initPage(DisneyPlusDOBCollectionPageBase.class);
        DisneyPlusPlanSelectPageBase planSelectPageBase = initPage(DisneyPlusPlanSelectPageBase.class);

        verifyPurchaseOptionAvailable(fullDictionary);
        welcomePageBase.proceedToSignUp();

        DisneyPlusPaywallPageBase paywallPageBase = initPage(DisneyPlusLoginPageBase.class)
                .registerNewEmail(generateNewUserEmail())
                .registerPassword();

        dobCollectionPageBase.isDOBTitleDisplayed();
        dobCollectionPageBase.submitDOBValue(OVER18);
        planSelectPageBase.isOpened();
        planSelectPageBase.clickPlanSelect(0);
        paywallPageBase.clickRestorePurchaseBtn();

        boolean isErrorPresent = paywallPageBase.isPaywallErrorDisplayed();
        sa.assertTrue(isErrorPresent,
                "Expected - Restore Purchase error to be displayed");

        if(isErrorPresent){
            sa.assertEquals(paywallPageBase.getErrorText(),
                    apiChecker.getDictionaryItemValue(fullDictionary, "error_purchase_restore_fail_expired"),
                    "Expected - Error text to be displayed correctly");

            JsonNode paywallDictionary = apiChecker.getDictionaryBody(languageUtils.get().getUserLanguage(), "application");
            sa.assertEquals(paywallPageBase.getConfirmButtonText(),
                    apiChecker.getDictionaryItemValue(paywallDictionary, "btn_dismiss").toUpperCase(),
                    "Expected - Correct 'Dismiss' text to be displayed");

            paywallPageBase.clickConfirmButton();

            sa.assertTrue(paywallPageBase.isOpened(),
                    "Expected - Dismissal to return user to Sku options page");
        }

        sa.assertAll();
    }
}
