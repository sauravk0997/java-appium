package com.disney.qa.tests.disney.apple.tvos.localization;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;
import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.client.responses.profile.Profile;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.TestGroup;
import com.disney.util.ZipUtils;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.config.Configuration;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.common.constant.DisneyUnifiedOfferPlan.DISNEY_PLUS_PREMIUM;
import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.PROFILE;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;

public class DisneyPlusAppleTVSubscriberLocalizationCaptures extends DisneyPlusAppleTVBaseTest {

    private static final String ADULT_PROFILE_NAME = "Adult";
    private static final String KIDS_PROFILE_NAME = "Kids";
    private static final String THE_SIMPSONS = "The Simpsons";
    private static final String SHANG_CHI = "Shang-Chi";
    private static final String WINNIE_THE_POOH = "Winnie the Pooh";

    @BeforeMethod(alwaysRun = true)
    public void proxySetUp() {
        boolean unpinDictionaries = Configuration.get(DisneyConfiguration.Parameter.UNPIN_DICTIONARIES, Boolean.class)
                .orElse(false);
        boolean displayDictionaryKeys = Boolean.parseBoolean(R.CONFIG.get("custom_string5"));
        String globalizationVersion = R.CONFIG.get("custom_string4");
        if (unpinDictionaries || displayDictionaryKeys || (!globalizationVersion.isEmpty() && !globalizationVersion.equalsIgnoreCase("null"))) {
            installJarvis();
            setJarvisOverrides();
        }
        clearAppCache();
    }

    @Test(description = "Subscriber Flow of Login", groups = { TestGroup.SUBSCRIBER_LOGIN_FLOW, TestGroup.SUB_UI, TestGroup.PROXY, US })
    public void captureLoginFlow() {
        DisneyPlusApplePageBase applePage = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        DisneyAccount user = getAccountApi().createAccount(ENTITLEMENT_LOOKUP, getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage(), SUB_VERSION);
        String baseDirectory = "LoginScreenshots/";

        //Apple TV S1.1
        loginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
        //        applePage.dismissUnexpectedErrorAlert();
        pause(10); //handle initial load of app
        welcomePage.isOpened();
        getScreenshots("1-WelcomePage", baseDirectory);

        //Apple TV S1.2
        welcomePage.clickLogInButton();
        loginPage.isOpened();
        loginPage.clickContinueBtn();
        loginPage.isOpened();
        getScreenshots("2-LoginPageBlankEmailError", baseDirectory);

        //Apple TV S1.3
        loginPage.moveUp(1, 1);
        loginPage.proceedToLocalizedPasswordScreen("email_does_not_exist@abcde.com");
        applePage.isActionAlertTitlePresent();
        getScreenshots("3-CouldNotFindAccountError", baseDirectory);

        //Apple TV S1.4
        loginPage.clickSelect();
        loginPage.isOpened();
        getScreenshots("4-UnknownEmailError", baseDirectory);

        //Apple TV S1.5
        loginPage.clickEmailField();
        loginPage.clickLocalizationEnterNewBtn();
        loginPage.enterEmail(user.getEmail());
        loginPage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        loginPage.clickSelect();
        loginPage.clickContinueBtn();
        passwordPage.isOpened();
        passwordPage.clickPrimaryButton();
        getScreenshots("5-EnterCurrentPasswordError", baseDirectory);

        //Apple TV S1.6
        passwordPage.moveUp(2, 1);
        passwordPage.clickSelect();
        passwordPage.enterPassword("aYbZ12@34");
        passwordPage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        passwordPage.clickSelect();
        passwordPage.clickPrimaryButton();
        passwordPage.isOpened();
        getScreenshots("6-IncorrectPasswordError", baseDirectory);

        //Apple TV S1.7
        passwordPage.clickHavingTroubleLogginInBtn();
        oneTimePasscodePage.isOpened();
        getScreenshots("7-CheckEmailSixDigitCode", baseDirectory);

        //Apple TV S1.8
        oneTimePasscodePage.clickSelect();
        oneTimePasscodePage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        oneTimePasscodePage.clickSelect();
        applePage.isHeadlineHeaderPresent();
        getScreenshots("8-PasscodeError", baseDirectory);

        //Apple TV S1.9
        oneTimePasscodePage.isOpened();
        oneTimePasscodePage.moveDown(1, 1);
        oneTimePasscodePage.clickSelect();
        applePage.isActionAlertTitlePresent();
        getScreenshots("9-NewEmailSent", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory,
                String.format("%s_%s_%s_%s.zip", "Sub_UI_Log_In", getLanguage().toUpperCase(), getCountry(), getDate()));
    }

    @Test(description = "Subscriber Flow of Global Nav, Search and Watchlist Screenshots", groups = { TestGroup.SUBSCRIBER_FLOW_NAV_SEARCH_WATCHLIST, TestGroup.SUB_UI, TestGroup.PROXY, US})
    public void captureSubscriberUIFlowGlobalNavSearchWatchlist() {
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());

        DisneyAccount user = getAccountApi().createAccount(ENTITLEMENT_LOOKUP, getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage(), SUB_VERSION);
        String baseDirectory = "globalNavSearch/";

        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
        //        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(10); //handle initial load of app
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());
        //        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        disneyPlusAppleTVHomePage.isOpened();
        if (!disneyPlusAppleTVHomePage.isGlobalNavExpanded()) {
            disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        }
        disneyPlusAppleTVHomePage.isGlobalNavExpanded();
        getScreenshots("1-GlobalNavigationBar", baseDirectory);

        disneyPlusAppleTVHomePage.clickSearchIcon();
        disneyPlusAppleTVSearchPage.isOpened();
        getScreenshots("2-Search", baseDirectory);

        disneyPlusAppleTVSearchPage.isOpened();
        disneyPlusAppleTVSearchPage.searchForMedia("```");
        pause(2); //load no results
        getScreenshots("3-SearchNoResults", baseDirectory);

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.isGlobalNavExpanded();
        disneyPlusAppleTVHomePage.clickWatchlistButton();
        disneyPlusAppleTVWatchListPage.isWatchlistEmptyBackgroundDisplayed();
        getScreenshots("4-WatchlistPage", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory,
                String.format("%s_%s_%s_%s.zip", "Sub_UI_Global_Nav_Search_Watchlist", getLanguage().toUpperCase(), getCountry(), getDate()));
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Subscriber Flow of Global Nav, Search and Watchlist Screenshots", groups = { TestGroup.SUBSCRIBER_FLOW_PROFILES, TestGroup.SUB_UI, TestGroup.PROXY, US})
    public void captureSubscriberUIFlowProfiles() throws URISyntaxException {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVEditProfilePage disneyPlusAppleTVEditProfilePage = new DisneyPlusAppleTVEditProfilePage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVChooseAvatarPage disneyPlusAppleTVChooseAvatarPage = new DisneyPlusAppleTVChooseAvatarPage(getDriver());
        DisneyPlusAppleTVAddProfilePage disneyPlusAppleTVAddProfilePage = new DisneyPlusAppleTVAddProfilePage(getDriver());
        DisneyPlusAppleTVSettingsPage disneyPlusAppleTVSettingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());
        DisneyPlusAppleTVAppLanguagePage disneyPlusAppleTVAppLanguagePage = new DisneyPlusAppleTVAppLanguagePage(getDriver());

        DisneyAccount user = getAccountApi().createAccount(ENTITLEMENT_LOOKUP, getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage(), SUB_VERSION);
        String baseDirectory = "Profile/";
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
        //        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(user).profileName(ADULT_PROFILE_NAME).language(getLocalizationUtils().getUserLanguage()).avatarId(null).kidsModeEnabled(false).dateOfBirth(null).build());

        pause(10); //handle initial load of app
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());
        //        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        user.setProfileId(getAccountApi().getProfiles(user).get(1).getProfileId());
        getAccountApi().patchStarOnboardingStatus(user, true);
        disneyPlusAppleTVWhoIsWatchingPage.getTypeCellLabelContains(ADULT_PROFILE_NAME).clickIfPresent();
        disneyPlusAppleTVHomePage.isOpened();

        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
        if (!disneyPlusAppleTVHomePage.isGlobalNavExpanded()) {
            disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        }
        disneyPlusAppleTVHomePage.clickProfileTab();
        disneyPlusAppleTVWhoIsWatchingPage.isOpened();
        getScreenshots("1-Profile", baseDirectory);

        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        disneyPlusAppleTVEditProfilePage.isOpened();
        getScreenshots("2-EditProfiles", baseDirectory);

        disneyPlusAppleTVEditProfilePage.getTypeCellLabelContains(ADULT_PROFILE_NAME).clickIfPresent();
        pause(5); //load Edit Adult Profile Screen
        disneyPlusAppleTVEditProfilePage.isOpened();
        disneyPlusAppleTVEditProfilePage.moveDown(2, 1);
        disneyPlusAppleTVEditProfilePage.isAutoplayToggleFocused();
        getScreenshots("3-EditProfileAutoplay", baseDirectory);

        disneyPlusAppleTVEditProfilePage.moveDown(1, 1);
        disneyPlusAppleTVEditProfilePage.isBackgroundVideoFocused();
        getScreenshots("4-EditProfileBackgroundVideo", baseDirectory);

        disneyPlusAppleTVEditProfilePage.moveDown(1, 1);
        disneyPlusAppleTVEditProfilePage.isAppLanguageCellFocused();
        pause(10);
        getScreenshots("5-EditProfileAppLanguage", baseDirectory);

        disneyPlusAppleTVEditProfilePage.clickAppLanguage();
        disneyPlusAppleTVAppLanguagePage.isOpened();
        getScreenshots("6-AppLanguage", baseDirectory);

        disneyPlusAppleTVAppLanguagePage.clickMenuTimes(1, 1);
        disneyPlusAppleTVEditProfilePage.moveDown(1, 1);
        disneyPlusAppleTVEditProfilePage.isParentalControlsCellFocused();
        disneyPlusAppleTVEditProfilePage.clickParentalControls();
        pause(10);
        getScreenshots("7-ParentalControls", baseDirectory);

        pause(30);
        disneyPlusAppleTVEditProfilePage.clickMenuTimes(2, 1);

        disneyPlusAppleTVWhoIsWatchingPage.clickAddProfile();
        pause(5); //load page fully
        disneyPlusAppleTVChooseAvatarPage.isOpened();
        getScreenshots("8-ChooseAvatar", baseDirectory);

        disneyPlusAppleTVAddProfilePage.clickSelectAvatarSkipBtn();
        disneyPlusAppleTVAddProfilePage.isOpened();
        disneyPlusAppleTVAddProfilePage.moveRight(1, 1);
        disneyPlusAppleTVAddProfilePage.clickSelect();
        pause(5);
        getScreenshots("9-NoProfileNameError", baseDirectory);

        disneyPlusAppleTVAddProfilePage.moveLeft(1, 1);
        disneyPlusAppleTVAddProfilePage.clickSelect();
        disneyPlusAppleTVAddProfilePage.enterProfileNameOnLocalizedKeyboard(ADULT_PROFILE_NAME);
        disneyPlusAppleTVAddProfilePage.clickSelect();
        disneyPlusAppleTVAddProfilePage.isOpened();
        getScreenshots("10-AddExistingProfileNameError", baseDirectory);

        disneyPlusAppleTVAddProfilePage.clickSelect();
        disneyPlusAppleTVAddProfilePage.enterProfileNameOnLocalizedKeyboard(KIDS_PROFILE_NAME);
        disneyPlusAppleTVAddProfilePage.clickSelect();
        disneyPlusAppleTVAddProfilePage.moveDown(2, 1);
        disneyPlusAppleTVAddProfilePage.clickSelect();
        disneyPlusAppleTVAddProfilePage.clickKidsOnToggleBtn();
        disneyPlusAppleTVAddProfilePage.moveRight(1, 1);
        disneyPlusAppleTVAddProfilePage.clickSaveProfileButton();
        disneyPlusAppleTVWhoIsWatchingPage.isOpened();
        pause(5); //load WhoIsWatchingPage
        getScreenshots("11-AddedKidsProfile", baseDirectory);

        disneyPlusAppleTVWhoIsWatchingPage.isOpened();
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();

        disneyPlusAppleTVWhoIsWatchingPage.moveRight(1, 1);
        disneyPlusAppleTVWhoIsWatchingPage.clickSelect();
        disneyPlusAppleTVEditProfilePage.clickDeleteButton();
        pause(5); //load delete view
        getScreenshots("12-DeleteProfile", baseDirectory);

        //        disneyPlusAppleTVEditProfilePage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVEditProfilePage.clickSaveProfileButton();
        disneyPlusAppleTVWhoIsWatchingPage.isOpened();
        disneyPlusAppleTVWhoIsWatchingPage.clickSelect();
        pause(15); //load kid's home
        disneyPlusAppleTVHomePage.isOpened();
        if (!disneyPlusAppleTVHomePage.isGlobalNavExpanded()) {
            disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
            disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        }
        disneyPlusAppleTVHomePage.clickSettingsTab();
        disneyPlusAppleTVSettingsPage.isOpened();
        getScreenshots("13-KidsSettings", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory,
                String.format("%s_%s_%s_%s.zip", "Sub_UI_Profile", getLanguage().toUpperCase(), getCountry(), getDate()));
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Account Settings Capture Screenshots", groups = { TestGroup.SUBSCRIBER_FLOW_ACCOUNT_SETTINGS, TestGroup.SUB_UI, TestGroup.PROXY, US})
    public void captureAccountSettings() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVSettingsPage disneyPlusAppleTVSettingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());
        DisneyPlusAppleTVForgotPasswordPage disneyPlusAppleTVForgotPasswordPage = new DisneyPlusAppleTVForgotPasswordPage(getDriver());
        DisneyPlusAppleTVChangeEmailPage disneyPlusAppleTVChangeEmailPage = new DisneyPlusAppleTVChangeEmailPage(getDriver());
        DisneyPlusAppleTVChangePasswordPage disneyPlusAppleTVChangePasswordPage = new DisneyPlusAppleTVChangePasswordPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVAccountPage disneyPlusAppleTVAccountPage = new DisneyPlusAppleTVAccountPage(getDriver());

        setAccount(getUnifiedAccountApi().createAccountForOTP(getCreateUnifiedAccountRequest(
                DISNEY_PLUS_PREMIUM,
                getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage())));
        String baseDirectory = "AccountSettingsScreens/";

        //        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(getUnifiedAccount().getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(getUnifiedAccount().getUserPass());
        //        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        disneyPlusAppleTVHomePage.isOpened();
        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.clickNavMenuSettings();

        disneyPlusAppleTVSettingsPage.moveRight(1, 1);
        disneyPlusAppleTVSettingsPage.clickAccountBtn();
        disneyPlusAppleTVAccountPage.isOpened();
        getScreenshots("1-AccountPage", baseDirectory);
        //Screenshot 1. Apple TV S4.1 From the left navigation panel > Settings, click on Account and review the page. - Take SS

        disneyPlusAppleTVAccountPage.clickChangeEmailBtn();
        disneyPlusAppleTVChangeEmailPage.isOpened();
        getScreenshots("1-OneTimePassword", baseDirectory);
        //Screenshot 2. Apple TV S4.2 Click on Change next to the email address and review the page.- Take SS

        String otp = getOTPFromApi(getUnifiedAccount());
        disneyPlusAppleTVForgotPasswordPage.clickOnOtpField();
        disneyPlusAppleTVForgotPasswordPage.enterOTPLocalized("000000");
        disneyPlusAppleTVChangeEmailPage.isOpened();
        getScreenshots("3-IncorrectOTP", baseDirectory);
        //Screenshot 3. Apple TV S4.3 Leave code empty and enter a wrong code and review the error message. (same error)- Take SS

        disneyPlusAppleTVForgotPasswordPage.clickResend();
        pause(5);
        getScreenshots("4-ResendEmailPopup", baseDirectory);
        //Screenshot 4. Apple TV S4.4 Now click on Resend email and review the pop up message.- Take SS

        disneyPlusAppleTVHomePage.clickMenu();
        disneyPlusAppleTVForgotPasswordPage.clickOnOtpField();
        disneyPlusAppleTVForgotPasswordPage.enterOTPLocalized(otp);
        pause(2);

        //Move down to select checkbox for "Log out of all devices" Appium seems to be unable to click on this element by any kind of identifier or xpath/iosclasschain
        disneyPlusAppleTVHomePage.moveDown(1, 1);
        disneyPlusAppleTVHomePage.clickSelect();
        pause(2);
        getScreenshots("5-LogOutAllDevices", baseDirectory);
        //Screenshot 5. Apple TV S4.5 Enter a correct code now and click Continue. Select Log out of devices and review the page.- Take SS

        disneyPlusAppleTVChangeEmailPage.clickSave();
        pause(2);
        getScreenshots("6-EmptyEmail", baseDirectory);
        //Screenshot 6. Apple TV S4.6 Leave the field empty, review error message.- Take SS

        disneyPlusAppleTVChangeEmailPage.clickEmailField();
        disneyPlusAppleTVLoginPage.clickLocalizationEnterNewBtn();
        disneyPlusAppleTVLoginPage.enterEmail("c");
        disneyPlusAppleTVLoginPage.keyPressTimes(disneyPlusApplePageBase.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        disneyPlusAppleTVLoginPage.clickSelect();
        disneyPlusAppleTVChangeEmailPage.clickSave();
        pause(2);
        getScreenshots("7-InvalidEmail", baseDirectory);
        //Screenshot 7. Apple TV S4.7 Enter invalid email address, review error message.- Take SS

        disneyPlusAppleTVHomePage.clickMenu();
        pause(1);
        disneyPlusAppleTVHomePage.clickMenu();

        disneyPlusAppleTVAccountPage.clickChangePasswordBtn();
        String otp2 = getOTPFromApi(getUnifiedAccount());
        pause(5);
        //Screenshot 8. Apple TV S4.8 Go back to the Account section, now click on Change next to Password. Follow the steps again for Check your email inbox flow (Same as S4.2 - S4.5).- Take SS
        getScreenshots("8-OneTimePassword", baseDirectory);

        disneyPlusAppleTVForgotPasswordPage.clickOnOtpField();
        disneyPlusAppleTVForgotPasswordPage.enterOTPLocalized(otp2);
        disneyPlusAppleTVChangePasswordPage.isOpened();
        getScreenshots("9-ChangePasswordPage", baseDirectory);
        //Screenshot 9. Apple TV S4.9 Review Create a password page. - Take SS (These two don't have a step in between so they are the same)

        disneyPlusAppleTVHomePage.moveDown(1, 1);
        disneyPlusAppleTVHomePage.clickSelect();
        disneyPlusAppleTVChangePasswordPage.clickSave();
        pause(2);
        getScreenshots("10-EmptyPassword", baseDirectory);
        //Screenshot 10. Apple TV S4.10 Enter no password and select "Log out of all device" checkbox. Review the error and additional text. - Take SS

        disneyPlusAppleTVChangePasswordPage.clickPasswordField();
        disneyPlusAppleTVChangePasswordPage.enterPassword("c");
        disneyPlusAppleTVForgotPasswordPage.keyPressTimes(disneyPlusApplePageBase.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        disneyPlusAppleTVForgotPasswordPage.clickSelect();
        disneyPlusAppleTVChangePasswordPage.clickSave();
        pause(2);
        getScreenshots("11-InvalidPassword", baseDirectory);
        //Screenshot 11. Apple TV S4.11 Enter an invalid password, such as "a", and click Continue. Review error message. - Take SS

        disneyPlusAppleTVChangePasswordPage.clickPasswordField();
        disneyPlusAppleTVChangePasswordPage.enterPassword("Abc12!");
        pause(2);
        getScreenshots("12-WeakPassword", baseDirectory);
        //Screenshot 12.1. Apple TV S4.12 Enter a random password and review the text changing (Do not save!!). Review Fair and Good. (shown in the screenshot here). - Take SS

        disneyPlusAppleTVHomePage.clickMenu();
        disneyPlusAppleTVChangePasswordPage.clickPasswordField();
        disneyPlusAppleTVChangePasswordPage.enterPassword("Abc12!qrs");
        pause(2);
        getScreenshots("13-FairPassword", baseDirectory);
        //Screenshot 12.2. Apple TV S4.12 Enter a random password and review the text changing (Do not save!!). Review Fair and Good. (shown in the screenshot here). - Take SS

        disneyPlusAppleTVHomePage.clickMenu();
        disneyPlusAppleTVChangePasswordPage.clickPasswordField();
        disneyPlusAppleTVChangePasswordPage.enterPassword("Abc12!qrsdeF");
        pause(2);
        getScreenshots("14-StrongPassword", baseDirectory);
        //Screenshot 12.3. Apple TV S4.12 Enter a random password and review the text changing (Do not save!!). Review Fair and Good. (shown in the screenshot here). - Take SS

        disneyPlusAppleTVHomePage.clickMenu();
        pause(1);
        disneyPlusAppleTVHomePage.clickMenu();
        pause(1);
        disneyPlusAppleTVHomePage.clickMenu();
        pause(1);
        disneyPlusAppleTVSettingsPage.clickLogOutAllDevicesBtn();
        disneyPlusAppleTVHomePage.clickPrimaryButton();
        pause(2);
        getScreenshots("15-LogoutAllDevicesEmptyPassword", baseDirectory);
        //Screenshot 13. Apple TV S4.13 Go back to Account Setting menu and now select Log out of all devices. Leave the field empty and select Log Out. Review error message. - Take SS

        disneyPlusAppleTVChangePasswordPage.clickPasswordField();
        disneyPlusAppleTVChangePasswordPage.enterPassword("a");
        disneyPlusAppleTVForgotPasswordPage.keyPressTimes(disneyPlusApplePageBase.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        disneyPlusAppleTVForgotPasswordPage.clickSelect();
        disneyPlusAppleTVHomePage.clickPrimaryButton();
        pause(2);
        getScreenshots("16-LogoutAllDevicesIncorrectPassword", baseDirectory);
        //Screenshot 14. Apple TV S4.14 Enter incorrect password and and select Log Out. Review error messages. - Take SS

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory,
                String.format("%s_%s_%s_%s.zip", "Sub_UI_Account_Settings", getLanguage().toUpperCase(), getCountry(), getDate()));
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = " Capture Subscriber UI Flows: PCON", groups = { TestGroup.SUBSCRIBER_FLOW_SETTINGS_PCON, TestGroup.SUB_UI, TestGroup.PROXY, US})
    public void captureSubscriberUIFlowSettingsPCON() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        String baseDirectory = "PCONSettings/";

        DisneyAccount entitledUser = getAccountApi().createAccount(ENTITLEMENT_LOOKUP, getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage(), SUB_VERSION);
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(entitledUser).profileName(KIDS_PROFILE_NAME).language(getLocalizationUtils().getUserLanguage()).avatarId(null).kidsModeEnabled(true).dateOfBirth(null).build());
        List<Profile> profiles = getAccountApi().getProfiles(entitledUser);
        entitledUser.setProfiles(profiles);

        //S6.5 Set the kid-proof exit in web or mobile.
        getAccountApi().editKidProofExitSettingForKidProfile(entitledUser, KIDS_PROFILE_NAME, true);

        //  S6.1 Set profile PIN for main profile
        try {
            getAccountApi().updateProfilePin(entitledUser, entitledUser.getProfileId(DEFAULT_PROFILE), "1234");
        } catch (Exception e) {
            throw new SkipException("Failed to update Profile pin: {}", e);
        }

        //S6.3 Set the Content rating to the lowest level
        List<Profile> profileList = getAccountApi().getProfiles(entitledUser);
        String system = profileList.get(0).getAttributes().getParentalControls().getMaturityRating().getRatingSystem();
        List<String> ratings = profileList.get(0).getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues();
        getAccountApi().editContentRatingProfileSetting(entitledUser, system, ratings.get(0));

        //        disneyPlusAppleTVHomePage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVHomePage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(entitledUser.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(entitledUser.getUserPass());
        //        disneyPlusAppleTVHomePage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWhoIsWatchingPage.getTypeCellLabelContains(DEFAULT_PROFILE).clickIfPresent();
        disneyPlusAppleTVHomePage.isOpened();
        pause(5);// wait for screen to load
        getScreenshots("1-EnterYourPinScreen", baseDirectory);

        //S6.2 Enter incorrect PIN. Review the error message.
        IntStream.range(0, 4).forEach(i ->
                disneyPlusAppleTVHomePage.clickSelect()
        );
        pause(5);
        getScreenshots("2-IncorrectPinErrorMessage", baseDirectory);

        //enter correct pin, this will enter "1234"
        disneyPlusAppleTVHomePage.clickRight();
        IntStream.range(0, 4).forEach(i -> {
            disneyPlusAppleTVHomePage.clickSelect();
            disneyPlusAppleTVHomePage.clickRight();

        });
        //S6.3  Go to Search menu and enter "avatar" in search field. Review the message on the bottom.
        disneyPlusAppleTVHomePage.isOpened();
        pause(5);// wait for screen to load
        //        disneyPlusAppleTVHomePage.dismissUnexpectedErrorAlert();

        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
        if (!disneyPlusAppleTVHomePage.isGlobalNavExpanded()) {
            disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        }
        disneyPlusAppleTVHomePage.clickSearchIcon();
        disneyPlusAppleTVSearchPage.isOpened();
        disneyPlusAppleTVSearchPage.typeInSearchField("Avatar");
        pause(3);
        getScreenshots("3-AvatarSearchOnLowestRatingProfile", baseDirectory);

        //S6.4 Go to Kids profile, search for "avatar", review different disclaimer.
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(PROFILE.getText());
        new DisneyPlusAppleTVWhoIsWatchingPage(getDriver()).isOpened();
        disneyPlusAppleTVHomePage.getTypeCellLabelContains(KIDS_PROFILE_NAME).clickIfPresent();
        //        disneyPlusAppleTVHomePage.dismissUnexpectedErrorAlert();
        if (!disneyPlusAppleTVHomePage.isGlobalNavExpanded()) {
            disneyPlusAppleTVHomePage.isOpened();
            disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
            disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        }
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(SEARCH.getText());
        disneyPlusAppleTVHomePage.clickSelect();
        disneyPlusAppleTVSearchPage.isOpened();
        disneyPlusAppleTVSearchPage.typeInSearchField("Avatar");
        pause(3);
        getScreenshots("4-AvatarSearchOnKidsProfile", baseDirectory);

        //S6.5 Go to kids profile in connected device. Review the kid-proof exit page.
        disneyPlusAppleTVHomePage.clickBack();
        disneyPlusAppleTVHomePage.clickLeft();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE.getText());
        disneyPlusAppleTVHomePage.clickSelect();
        pause(5);//wait for screen to load
        getScreenshots("5-KidProofExitScreen", baseDirectory);

        //S6.6 Enter the incorrect number, then review the error message.
        IntStream.range(0, 4).forEach(i -> disneyPlusAppleTVHomePage.clickSelect());
        getScreenshots("6-KidProofExitIncorrectNumberErrorMessage", baseDirectory);
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory,
                String.format("%s_%s_%s_%s.zip", "Sub_UI_PCON_Settings", getLanguage().toUpperCase(), getCountry(), getDate()));
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Capture Subscriber UI Flows: Settings Screenshots", groups = { TestGroup.SUBSCRIBER_FLOW_SETTINGS, TestGroup.SUB_UI, TestGroup.PROXY, US})
    public void captureSubscriberUIFlowSettings() {
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVSettingsPage disneyPlusAppleTVSettingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());
        DisneyPlusAppleTVLegalPage disneyPlusAppleTVLegalPage = new DisneyPlusAppleTVLegalPage(getDriver());

        DisneyAccount user = getAccountApi().createAccount(ENTITLEMENT_LOOKUP, getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage(), SUB_VERSION);
        String baseDirectory = "SettingsScreenshots/";
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
        //        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        disneyPlusAppleTVWelcomeScreenPage.waitForWelcomePageToLoad();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());
        //        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWhoIsWatchingPage.getTypeCellLabelContains(ADULT_PROFILE_NAME).clickIfPresent();
        disneyPlusAppleTVHomePage.isOpened();

        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
        if (!disneyPlusAppleTVHomePage.isGlobalNavExpanded()) {
            disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        }
        disneyPlusAppleTVHomePage.clickSettingsTab();

        disneyPlusAppleTVSettingsPage.isOpened();
        getScreenshots("1-Settings", baseDirectory);

        //Apple TV SS3.2 Click on App settings and review the page
        disneyPlusAppleTVSettingsPage.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS);
        disneyPlusAppleTVSettingsPage.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS);
        getScreenshots("2-AppSettings", baseDirectory);
        disneyPlusAppleTVSettingsPage.clickMenuTimes(1, 1);

        //Apple TV S3.3 Click on Help and Review the page
        disneyPlusAppleTVSettingsPage.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.HELP);
        getScreenshots("3-SettingsHelp", baseDirectory);
        disneyPlusApplePageBase.clickMenu();

        //Apple TV S3.4 Back to the settings page, click on Legal.
        disneyPlusAppleTVSettingsPage.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LEGAL);
        disneyPlusAppleTVLegalPage.isOpened();
        getScreenshots("4-SettingsLegal", baseDirectory);

        disneyPlusAppleTVLegalPage.isOpened();
        disneyPlusAppleTVLegalPage.getAllLegalSectionsScreenshot("5_Settings_Legal_", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory,
                String.format("%s_%s_%s_%s.zip", "Sub_UI_Settings", getLanguage().toUpperCase(), getCountry(), getDate()));
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Capture Subscriber UI Flows: Player Screenshots", groups = { TestGroup.SUBSCRIBER_FLOW_PLAYER, TestGroup.SUB_UI, TestGroup.PROXY, US})
    public void captureSubscriberUIFlowPlayer() {
        DisneyPlusApplePageBase applePageBase = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage whoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVSearchPage searchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVDetailsPage detailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        DisneyPlusAppleTVVideoPlayerPage videoPlayerPage = new DisneyPlusAppleTVVideoPlayerPage(getDriver());
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        DisneyPlusAppleTVUpNextPage upNextPage = new DisneyPlusAppleTVUpNextPage(getDriver());

        DisneyAccount user = getAccountApi().createAccount(ENTITLEMENT_LOOKUP, getLocalizationUtils().getLocale(),
                getLocalizationUtils().getUserLanguage(), SUB_VERSION);
        String baseDirectory = "PlayerScreenshots/";
        loginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
        //        applePageBase.dismissUnexpectedErrorAlert();

        welcomeScreenPage.waitForWelcomePageToLoad();
        welcomeScreenPage.moveDown(1, 1);
        welcomeScreenPage.clickSelect();
        loginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        passwordPage.logInWithPasswordLocalized(user.getUserPass());
        //        applePageBase.dismissUnexpectedErrorAlert();
        whoIsWatchingPage.getTypeCellLabelContains(ADULT_PROFILE_NAME).clickIfPresent();
        homePage.isOpened();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavWithClickingMenu();
        homePage.clickSearchIcon();
        searchPage.isOpened();
        searchPage.typeInSearchField(SHANG_CHI);
        searchPage.clickLocalizedSearchResult(SHANG_CHI);
        detailsPage.isOpened();
        getScreenshots("1-IMAXEnhancedMovieDetailsPage", baseDirectory);

        detailsPage.moveDown(1, 1);
        detailsPage.moveRight(1, 1);
        pause(1); //transition
        getScreenshots("2-DetailsPageIMAXTab", baseDirectory);

        detailsPage.moveUp(1, 1);
        detailsPage.clickSelect();
        videoPlayerPage.waitForVideoToStart();
        videoPlayerPage.clickSelect();
        pause(4); //wait for timeline options to appear
        videoPlayerPage.moveUp(1, 1);
        videoPlayerPage.moveRight(1, 1);
        videoPlayerPage.clickSelect();
        pause(1); //transition to video top nav
        videoPlayerPage.moveRight(1, 1);
        getScreenshots("3-VideoPlayerAudio", baseDirectory);

        videoPlayerPage.moveRight(2, 1);
        getScreenshots("4-VideoPlayerSubtitles", baseDirectory);

        videoPlayerPage.clickMenuTimes(3, 1);
        detailsPage.isOpened();
        getScreenshots("5-DetailsPageContinue", baseDirectory);

        detailsPage.clickMenuTimes(2, 1);
        homePage.openGlobalNavWithClickingMenu();
        homePage.isGlobalNavExpanded();
        homePage.clickSearchIcon();
        searchPage.isOpened();
        searchPage.moveUp(1, 1);
        searchPage.typeInSearchField(WINNIE_THE_POOH);
        pause(1);
        searchPage.moveDown(1, 1);
        searchPage.clickSelect();
        detailsPage.isOpened();
        detailsPage.clickSelect();
        videoPlayerPage.waitForVideoToStart();
        commonPage.goRightTillLocalizedEndPageAppears(6, 3, 40, upNextPage.upNextExtraActionButton);
        pause(5); // transition to because you watched
        getScreenshots("6-VideoPlayerBecauseYouWatched", baseDirectory);

        upNextPage.clickUpNextExtraActionButton();
        videoPlayerPage.moveRight(1, 1);
        videoPlayerPage.clickSelect();
        pause(2); //transition from video to details
        detailsPage.moveDown(1, 1);
        pause(1); //transition below detail fold
        detailsPage.moveRight(1, 1);
        pause(1); //transition to details tab
        getScreenshots("7-DetailsTab", baseDirectory);

        detailsPage.clickMenuTimes(2, 1);
        searchPage.isOpened();
        searchPage.moveUp(1, 1);
        searchPage.typeInSearchField(THE_SIMPSONS);
        pause(1);
        searchPage.moveDown(1, 1);
        searchPage.clickSelect();
        detailsPage.isOpened();
        getScreenshots("8-TheSimpsonsPlayEpisode", baseDirectory);

        detailsPage.moveDown(1, 1);
        getScreenshots("9-TheSimpsonsEpisodes", baseDirectory);

        detailsPage.moveDown(1, 1);
        detailsPage.clickSelect();
        videoPlayerPage.isSkipIntroButtonPresent();
        getScreenshots("10-TheSimpsonsS1E1VideoPlayerSkipIntro", baseDirectory);

        commonPage.goRightTillLocalizedEndPageAppears(2, 4, 40, upNextPage.upNextExtraActionButton);
        upNextPage.isUpNextExtraActionButtonPresent();
        getScreenshots("11-TheSimpsonsVideoPlayerPlayNextEpisode", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory,
                String.format("%s_%s_%s_%s.zip", "Sub_UI_Video_Player", getLanguage().toUpperCase(), getCountry(), getDate()));
        loginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }
}
