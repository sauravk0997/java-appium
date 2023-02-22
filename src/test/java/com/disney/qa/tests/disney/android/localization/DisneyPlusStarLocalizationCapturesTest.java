package com.disney.qa.tests.disney.android.localization;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.*;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;

public class DisneyPlusStarLocalizationCapturesTest extends DisneyLocalizationBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    DisneyPlusLoginPageBase loginPageBase;
    DisneyPlusDiscoverPageBase discoverPageBase;
    DisneyPlusMoreMenuPageBase moreMenuPageBase;
    DisneyPlusMaturityPageBase maturityPageBase;

    @BeforeMethod(alwaysRun = true)
    public void testSetup() throws IOException, JSONException, URISyntaxException {
        setJarvisOverrides();
        initiateProxy(languageUtils.get().getCountryName());
    }

    private void resetStarStatus() throws JSONException, URISyntaxException {
        accountApi.get().patchStarOnboardingStatus(disneyAccount.get(), false);
    }

    @Test(dataProviderClass = DisneyLocalizationBase.class, dataProvider = "tuidGenerator", groups = {"Maturity"})
    public void captureWelchScreenshots(String TUID) throws JSONException, URISyntaxException {
        setDirectories("Maturity");
        count.set(1);
        resetStarStatus();
        androidUtils.get().clearAppCache();
        loginPageBase = initPage(DisneyPlusLoginPageBase.class);
        discoverPageBase = initPage(DisneyPlusDiscoverPageBase.class);
        moreMenuPageBase = initPage(DisneyPlusMoreMenuPageBase.class);
        maturityPageBase = initPage(DisneyPlusMaturityPageBase.class);

        LOGGER.info("Capturing group set: Skip Setup");
        executeSkipSetupCaptures();

        resetStarStatus();
        LOGGER.info("Capturing group set: Setup w/o Profiles");
        executeSetupWithoutProfilesSetupCaptures();
        accountApi.get().addProfile(disneyAccount.get(), "SecondProfileNameLong", languageUtils.get().getUserLanguage(), null, false);

        resetStarStatus();
        LOGGER.info("Capturing group set: Setup w/ Profiles");
        executeSetupWithProfilesSetupCaptures();

        LOGGER.info("Capturing group set: New Profile setup");
        executeAddProfileSetupCaptures();

        UniversalUtils.archiveAndUploadsScreenshots(baseDirectory.get(), pathToZip.get());
    }

    private void executeSkipSetupCaptures(){
        /*
         * Skipping STAR Setup
         *  1. "Now on Disney+ STAR" screen
         *  2. "Access the full Star catalog" screen
         *  3. "Your maturity rating will be xx" screen
         *  14. "Your maturity rating is set to xxx" message
         */

        login(disneyAccount.get(), false);
        maturityPageBase.isOpened();
        getScreenshots("NowOnDisney+STAR");

        maturityPageBase.clickContinueButton();
        getScreenshots("AccessFullStarCatalog");

        maturityPageBase.clickStarNotNowBtn();
        getScreenshots("MaturityRatingWillBe");

        discoverPageBase.clickStandardButton();
        discoverPageBase.isOpened();
        getScreenshots("MaturityRatingIsNow");
        discoverPageBase.clickConfirmIfPresent();
    }

    private void executeSetupWithoutProfilesSetupCaptures(){
        /*
         * STAR setup with 1 profile + add profile flow
         *  4. "Confirm with Password" screen
         *  5. "Confirm with Password" screen with error message
         *  6. (Forgot password)"Check your email box" screen
         *  7. "Check your email box" screen with error message (invalid code)
         *  10. PIN setting screen with "You have access to the full Star catalog" message.
         *  13. "Set maturity ratings for other profiles" screen
         */

        androidUtils.get().clearAppCache();
        login(disneyAccount.get(), false);
        maturityPageBase.clickContinueButton();
        maturityPageBase.clickStandardButton();
        getScreenshots("ConfirmWithPassword");

        loginPageBase.logInWithPassword("wrongpassword");
        pause(3);
        getScreenshots("PasswordError");

        maturityPageBase.clickForgotPasswordLink();
        maturityPageBase.waitForLoading();
        getScreenshots("ForgotPassword");

        new AndroidTVUtils(getDriver()).sendInput("123456");
        loginPageBase.clickStandardButton();
        loginPageBase.waitForLoading();
        getScreenshots("ForgotPasswordWithError");
        maturityPageBase.clickBackButton();
        loginPageBase.logInWithPassword(disneyAccount.get().getUserPass());
        maturityPageBase.waitForLoading();
        getScreenshots("PinSetting");
        maturityPageBase.clickPinNotNowButton();
        discoverPageBase.isOpened();
        getScreenshots("SharingWithOthers");
    }

    private void executeSetupWithProfilesSetupCaptures(){
        /*
         * STAR Setup with multiple profiles
         *  11. PIN setting screen with error message (empty PIN)
         *  12. Flash message (PIN is set)
         *  13. "Set maturity ratings for other profiles" screen
         */

        androidUtils.get().clearAppCache();
        login(disneyAccount.get(), true);
        maturityPageBase.clickStandardButton();
        maturityPageBase.waitForLoading();
        maturityPageBase.createProfilePin("5");
        maturityPageBase.getPinErrorText();
        getScreenshots("PinSettingWithError");

        maturityPageBase.createProfilePin("1234");
        maturityPageBase.waitForLoading();
        getScreenshots("PinSettingSuccess");

        maturityPageBase.waitForLoading();
        maturityPageBase.clickOnGenericTextExactElement(disneyAccount.get().getProfiles().get(0).getProfileName());
        maturityPageBase.enterProfilePin("1234");
        dismissChromecastOverlay();
    }

    public void executeAddProfileSetupCaptures(){
        /*
         * STAR Setup with multiple profiles
         *  1. "Access the full catalog" screen
         *  2. "Confirm with password" screen
         *  3. "Secure your profile" screen
         *  4. "Do you want to skip this step?"
         */

        discoverPageBase.navigateToPage(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DisneyPlusCommonPageBase.MenuItem.MORE.getText()));
        moreMenuPageBase.clickOnContentDescExact(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, "create_profile_add_profile"));
        moreMenuPageBase.clickFirstAvailableProfilePoster();
        pause(3);
        moreMenuPageBase.editTextByClass.type("Localized");
        moreMenuPageBase.clickActionButton();
        pause(3);
        getScreenshots("AddProfileAccessFullCatalog");

        moreMenuPageBase.clickStandardButton();
        pause(3);
        getScreenshots("AddProfilePinSetting");

        androidUtils.get().pressBack();
        pause(3);
        getScreenshots("AddProfileSkipStepConfirm");
    }

}
