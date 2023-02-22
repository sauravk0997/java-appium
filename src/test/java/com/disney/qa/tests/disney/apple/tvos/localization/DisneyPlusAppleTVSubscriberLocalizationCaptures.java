package com.disney.qa.tests.disney.apple.tvos.localization;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.responses.profile.DisneyProfile;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.common.utils.MobileUtilsExtended;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.tvos.DisneyPlusAppleTVBaseTest;
import com.disney.util.ZipUtils;
import com.qaprosoft.carina.core.foundation.utils.DateUtils;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.PROFILE;
import static com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage.globalNavigationMenu.SEARCH;
import static com.disney.qa.tests.disney.apple.ios.DisneyBaseTest.DEFAULT_PROFILE;

public class DisneyPlusAppleTVSubscriberLocalizationCaptures extends DisneyPlusAppleTVBaseTest {

    private static final String ADULT_PROFILE_NAME = "Adult";
    private static final String KIDS_PROFILE_NAME = "Kids";
    private static final String THE_SIMPSONS = "The Simpsons";
    private static final String SHANG_CHI = "Shang-Chi";
    private static final String WINNIE_THE_POOH = "Winnie the Pooh";
    private final ThreadLocal<DisneyLocalizationUtils> languageUtils = new ThreadLocal<>();
    private final ThreadLocal<String> regionLanguage = new ThreadLocal<>();
    private final ThreadLocal<String> region = new ThreadLocal<>();
    private final ThreadLocal<String> baseDirectory = new ThreadLocal<>();
    private final ThreadLocal<String> pathToZip = new ThreadLocal<>();
    private final DisneyAccountApi disneyAccountApi = new DisneyAccountApi(DisneyPlusAppleTVBaseTest.PLATFORM, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), DISNEY);

    @BeforeMethod(alwaysRun = true)
    public void proxySetUp() {
        R.CONFIG.put("capabilities.fullReset", "true");
        boolean unpinDictionaries = Boolean.parseBoolean(R.CONFIG.get("custom_string"));
        boolean displayDictionaryKeys = Boolean.parseBoolean(R.CONFIG.get("custom_string5"));
        String globalizationVersion = R.CONFIG.get("custom_string4");
        if (unpinDictionaries || displayDictionaryKeys || (!globalizationVersion.isEmpty() && !globalizationVersion.equalsIgnoreCase("null"))) {
            installJarvis();
            setJarvisOverrides();
        }
        DisneyCountryData countryData = new DisneyCountryData();
        regionLanguage.set(R.CONFIG.get("custom_string2"));
        region.set(R.CONFIG.get("custom_string3"));
        String country = (String) countryData.searchAndReturnCountryData(region.get(), "code", "country");
        setSystemLanguage(regionLanguage.get(), region.get());
        restartDriver(true);
        initiateProxy(country);
        regionLanguage.set(handleAppLanguage(regionLanguage.get()));
        pause(10);
        clearAppCache();
        languageUtils.set(new DisneyLocalizationUtils(region.get(), regionLanguage.get(), "apple-tv", "prod", PARTNER));
        DisneyMobileConfigApi configApi = new DisneyMobileConfigApi("tvos", "prod", PARTNER, new MobileUtilsExtended().getInstalledAppVersion());
        languageUtils.get().setDictionaries(configApi.getDictionaryVersions());
        DisneyPlusApplePageBase.setDictionary(languageUtils.get());
    }

    @Test(description = "Subscriber Flow of Login", groups = {"SusbscriberLoginFlow", "SubUI"})
    public void captureLoginFlow() {
        DisneyPlusApplePageBase applePage = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage welcomePage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage loginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage passwordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage oneTimePasscodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());

        DisneyAccount user = disneyAccountApi.createAccount(ENTITLEMENT_LOOKUP, region.get(), regionLanguage.get(), SUB_VERSION);
        baseDirectory.set("Screenshots/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "Subscriber", DateUtils.now()));

        //Apple TV S1.1
        loginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
        applePage.dismissUnexpectedErrorAlert();
        pause(10); //handle initial load of app
        welcomePage.isOpened();
        getScreenshots("1_WelcomePage", baseDirectory);

        //Apple TV S1.2
        welcomePage.clickLogInButton();
        loginPage.isOpened();
        loginPage.clickContinueBtn();
        loginPage.isOpened();
        getScreenshots("2_LoginPage_BlankEmailError", baseDirectory);

        //Apple TV S1.3
        loginPage.moveUp(1,1);
        loginPage.proceedToLocalizedPasswordScreen("email_does_not_exist@abcde.com");
        applePage.isActionAlertTitlePresent();
        getScreenshots("3_CouldNotFindAccountError", baseDirectory);

        //Apple TV S1.4
        loginPage.clickSelect();
        loginPage.isOpened();
        getScreenshots("4_UnknownEmailError", baseDirectory);

        //Apple TV S1.5
        loginPage.clickEmailField();
        loginPage.clickLocalizationEnterNewBtn();
        loginPage.enterEmail(user.getEmail());
        loginPage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        loginPage.clickSelect();
        loginPage.clickContinueBtn();
        passwordPage.isOpened();
        passwordPage.clickPrimaryButton();
        getScreenshots("5_EnterCurrentPasswordError", baseDirectory);

        //Apple TV S1.6
        passwordPage.moveUp(2,1);
        passwordPage.clickSelect();
        passwordPage.enterPassword("aYbZ12@34");
        passwordPage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        passwordPage.clickSelect();
        passwordPage.clickPrimaryButton();
        passwordPage.isOpened();
        getScreenshots("6_IncorrectPasswordError", baseDirectory);

        //Apple TV S1.7
        passwordPage.clickForgotPasswordBtn();
        oneTimePasscodePage.isOpened();
        getScreenshots("7_CheckEmailSixDigitCode", baseDirectory);

        //Apple TV S1.8
        oneTimePasscodePage.clickSelect();
        oneTimePasscodePage.keyPressTimes(loginPage.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        oneTimePasscodePage.clickSelect();
        applePage.isHeadlineHeaderPresent();
        getScreenshots("8_PasscodeError", baseDirectory);

        //Apple TV S1.9
        oneTimePasscodePage.isOpened();
        oneTimePasscodePage.moveDown(1,1);
        oneTimePasscodePage.clickSelect();
        applePage.isActionAlertTitlePresent();
        getScreenshots("9_NewEmailSent", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(description = "Subscriber Flow of Global Nav, Search and Watchlist Screenshots", groups = {"SubscriberFlowNavSearchWatchlist", "SubUI"})
    public void captureSubscriberUIFlowGlobalNavSearchWatchlist() {
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVWatchListPage disneyPlusAppleTVWatchListPage = new DisneyPlusAppleTVWatchListPage(getDriver());

        DisneyAccount user = disneyAccountApi.createAccount(ENTITLEMENT_LOOKUP, region.get(), regionLanguage.get(), SUB_VERSION);
        baseDirectory.set("Screenshots/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "Subscriber", DateUtils.now()));

        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        pause(10); //handle initial load of app
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());
        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        if (!disneyPlusAppleTVHomePage.isGlobalNavExpanded()) {
            disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        }
        disneyPlusAppleTVHomePage.isGlobalNavExpanded();
        getScreenshots("1_GlobalNavigationBar", baseDirectory);

        disneyPlusAppleTVHomePage.clickSearchIcon();
        disneyPlusAppleTVSearchPage.isOpened();
        getScreenshots("2_Search", baseDirectory);

        disneyPlusAppleTVSearchPage.isOpened();
        disneyPlusAppleTVSearchPage.searchForMedia("```");
        pause(2); //load no results
        getScreenshots("3_SearchNoResults", baseDirectory);

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.isGlobalNavExpanded();
        disneyPlusAppleTVHomePage.clickWatchlistButton();
        disneyPlusAppleTVWatchListPage.isWatchlistEmptyBackgroundDisplayed();
        getScreenshots("4_WatchlistPage", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Subscriber Flow of Global Nav, Search and Watchlist Screenshots", groups = {"SubscriberFlowProfiles", "SubUI"})
    public void captureSubscriberUIFlowProfiles() throws URISyntaxException {
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
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

        DisneyAccount user = disneyAccountApi.createAccount(ENTITLEMENT_LOOKUP, region.get(), regionLanguage.get(), SUB_VERSION);
        baseDirectory.set("Screenshots/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "Subscriber", DateUtils.now()));
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        disneyAccountApi.addProfile(user, ADULT_PROFILE_NAME, regionLanguage.get(), null, false);

        pause(10); //handle initial load of app
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());
        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        user.setProfileId(disneyAccountApi.getDisneyProfiles(user).get(1).getProfileId());
        disneyAccountApi.patchStarOnboardingStatus(user,true);
        disneyPlusAppleTVWhoIsWatchingPage.getTypeCellLabelContains(ADULT_PROFILE_NAME).clickIfPresent();
        disneyPlusAppleTVHomePage.isOpened();


        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.isGlobalNavExpanded();
        disneyPlusAppleTVHomePage.clickProfileTab();
        disneyPlusAppleTVWhoIsWatchingPage.isOpened();
        getScreenshots("1_Profile", baseDirectory);

        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();
        disneyPlusAppleTVEditProfilePage.isOpened();
        getScreenshots("2_Edit_Profiles", baseDirectory);

        disneyPlusAppleTVEditProfilePage.getTypeCellLabelContains(ADULT_PROFILE_NAME).clickIfPresent();
        pause(5); //load Edit Adult Profile Screen
        disneyPlusAppleTVEditProfilePage.isOpened();
        disneyPlusAppleTVEditProfilePage.moveDown(2, 1);
        disneyPlusAppleTVEditProfilePage.isAutoplayToggleFocused();
        getScreenshots("3_Edit_Profile_Autoplay", baseDirectory);

        disneyPlusAppleTVEditProfilePage.moveDown(1, 1);
        disneyPlusAppleTVEditProfilePage.isBackgroundVideoFocused();
        getScreenshots("4_Edit_Profile_Background_Video", baseDirectory);

        disneyPlusAppleTVEditProfilePage.moveDown(1, 1);
        disneyPlusAppleTVEditProfilePage.isAppLanguageCellFocused();
        pause(10);
        getScreenshots("5_Edit_Profile_App_Language", baseDirectory);

        disneyPlusAppleTVEditProfilePage.clickAppLanguage();
        disneyPlusAppleTVAppLanguagePage.isOpened();
        getScreenshots("6_App_Language", baseDirectory);

        disneyPlusAppleTVAppLanguagePage.clickMenuTimes(1, 1);
        disneyPlusAppleTVEditProfilePage.moveDown(1, 1);
        disneyPlusAppleTVEditProfilePage.isParentalControlsCellFocused();
        disneyPlusAppleTVEditProfilePage.clickParentalControls();
        pause(10);
        getScreenshots("7_Parental_Controls", baseDirectory);

        pause(30);
        disneyPlusAppleTVEditProfilePage.clickMenuTimes(2, 1);

        disneyPlusAppleTVWhoIsWatchingPage.clickAddProfile();
        pause(5); //load page fully
        disneyPlusAppleTVChooseAvatarPage.isOpened();
        getScreenshots("8_Choose_Avatar", baseDirectory);

        ExtendedWebElement[] avatars = disneyPlusAppleTVAddProfilePage.getCellsWithLabels().toArray(new ExtendedWebElement[0]);
        disneyPlusAppleTVAddProfilePage.clickAny(avatars[0]);
        disneyPlusAppleTVAddProfilePage.isOpened();
        disneyPlusAppleTVAddProfilePage.moveRight(1, 1);
        disneyPlusAppleTVAddProfilePage.clickSelect();
        pause(5);
        getScreenshots("9_No_Profile_Name_Error", baseDirectory);

        disneyPlusAppleTVAddProfilePage.moveLeft(1, 1);
        disneyPlusAppleTVAddProfilePage.clickSelect();
        disneyPlusAppleTVAddProfilePage.enterProfileNameOnLocalizedKeyboard(ADULT_PROFILE_NAME);
        disneyPlusAppleTVAddProfilePage.clickSelect();
        disneyPlusAppleTVAddProfilePage.isOpened();
        getScreenshots("10_Add_Existing_Profile_Name_Error", baseDirectory);

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
        getScreenshots("11_Added_Kids_Profile", baseDirectory);

        disneyPlusAppleTVWhoIsWatchingPage.isOpened();
        disneyPlusAppleTVWhoIsWatchingPage.clickEditProfile();

        disneyPlusAppleTVWhoIsWatchingPage.moveRight(1, 1);
        disneyPlusAppleTVWhoIsWatchingPage.clickSelect();
        disneyPlusAppleTVEditProfilePage.clickDeleteButton();
        pause(5); //load delete view
        getScreenshots("12_Delete_Profile", baseDirectory);

        disneyPlusAppleTVEditProfilePage.dismissUnexpectedErrorAlert();
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
        getScreenshots("13_Kids_Settings", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Account Settings Capture Screenshots", groups = {"SubscriberFlowAccountSettings", "SubUI"})
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
        EmailApi emailApi = new EmailApi();

        DisneyAccount user = disneyAccountApi.createAccountForOTP(region.get(), regionLanguage.get());
        baseDirectory.set("Screenshots/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "AccountSettings", DateUtils.now()));

        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());
        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.clickNavMenuSettings();

        disneyPlusAppleTVSettingsPage.moveRight(1, 1);
        disneyPlusAppleTVSettingsPage.clickAccountBtn();
        disneyPlusAppleTVAccountPage.isOpened();
        getScreenshots("01_AccountPage", baseDirectory);
        //Screenshot 1. Apple TV S4.1 From the left navigation panel > Settings, click on Account and review the page. - Take SS

        Date startTime = emailApi.getStartTime();
        disneyPlusAppleTVAccountPage.clickChangeEmailBtn();
        disneyPlusAppleTVChangeEmailPage.isOpened();
        getScreenshots("02_One_Time_Password", baseDirectory);
        //Screenshot 2. Apple TV S4.2 Click on Change next to the email address and review the page.- Take SS

        String otp = emailApi.getDisneyOTP(user.getEmail(), EmailApi.getOtpAccountPassword(), startTime);
        disneyPlusAppleTVForgotPasswordPage.clickOnOtpField();
        disneyPlusAppleTVForgotPasswordPage.enterOTPLocalized("000000");
        disneyPlusAppleTVChangeEmailPage.isOpened();
        getScreenshots("03_Incorrect_OTP", baseDirectory);
        //Screenshot 3. Apple TV S4.3 Leave code empty and enter a wrong code and review the error message. (same error)- Take SS

        disneyPlusAppleTVForgotPasswordPage.clickResend();
        pause(5);
        getScreenshots("04_Resend_email_popup", baseDirectory);
        //Screenshot 4. Apple TV S4.4 Now click on Resend email and review the pop up message.- Take SS

        disneyPlusAppleTVHomePage.clickMenu();
        disneyPlusAppleTVForgotPasswordPage.clickOnOtpField();
        disneyPlusAppleTVForgotPasswordPage.enterOTPLocalized(otp);
        pause(2);

        //Move down to select checkbox for "Log out of all devices" Appium seems to be unable to click on this element by any kind of identifier or xpath/iosclasschain
        disneyPlusAppleTVHomePage.moveDown(1, 1);
        disneyPlusAppleTVHomePage.clickSelect();
        pause(2);
        getScreenshots("05_LogOutAll_Devices", baseDirectory);
        //Screenshot 5. Apple TV S4.5 Enter a correct code now and click Continue. Select Log out of devices and review the page.- Take SS

        disneyPlusAppleTVChangeEmailPage.clickSave();
        pause(2);
        getScreenshots("06_Empty_Email", baseDirectory);
        //Screenshot 6. Apple TV S4.6 Leave the field empty, review error message.- Take SS

        disneyPlusAppleTVChangeEmailPage.clickEmailField();
        disneyPlusAppleTVLoginPage.clickLocalizationEnterNewBtn();
        disneyPlusAppleTVLoginPage.enterEmail("c");
        disneyPlusAppleTVLoginPage.keyPressTimes(disneyPlusApplePageBase.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        disneyPlusAppleTVLoginPage.clickSelect();
        disneyPlusAppleTVChangeEmailPage.clickSave();
        pause(2);
        getScreenshots("07_Invalid_Email", baseDirectory);
        //Screenshot 7. Apple TV S4.7 Enter invalid email address, review error message.- Take SS

        disneyPlusAppleTVHomePage.clickMenu();
        pause(1);
        disneyPlusAppleTVHomePage.clickMenu();

        Date startTime2 = emailApi.getStartTime();
        disneyPlusAppleTVAccountPage.clickChangePasswordBtn();
        String otp2 = emailApi.getDisneyOTP(user.getEmail(), EmailApi.getOtpAccountPassword(), startTime2);
        pause(5);
        //Screenshot 8. Apple TV S4.8 Go back to the Account section, now click on Change next to Password. Follow the steps again for Check your email inbox flow (Same as S4.2 - S4.5).- Take SS
        getScreenshots("08_One_Time_Password", baseDirectory);

        disneyPlusAppleTVForgotPasswordPage.clickOnOtpField();
        disneyPlusAppleTVForgotPasswordPage.enterOTPLocalized(otp2);
        disneyPlusAppleTVChangePasswordPage.isOpened();
        getScreenshots("09_Change_Password_Page", baseDirectory);
        //Screenshot 9. Apple TV S4.9 Review Create a password page. - Take SS (These two don't have a step in between so they are the same)

        disneyPlusAppleTVHomePage.moveDown(1, 1);
        disneyPlusAppleTVHomePage.clickSelect();
        disneyPlusAppleTVChangePasswordPage.clickSave();
        pause(2);
        getScreenshots("10_Empty_Password", baseDirectory);
        //Screenshot 10. Apple TV S4.10 Enter no password and select "Log out of all device" checkbox. Review the error and additional text. - Take SS

        disneyPlusAppleTVChangePasswordPage.clickPasswordField();
        disneyPlusAppleTVChangePasswordPage.enterPassword("c");
        disneyPlusAppleTVForgotPasswordPage.keyPressTimes(disneyPlusApplePageBase.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        disneyPlusAppleTVForgotPasswordPage.clickSelect();
        disneyPlusAppleTVChangePasswordPage.clickSave();
        pause(2);
        getScreenshots("11_Invalid_Password", baseDirectory);
        //Screenshot 11. Apple TV S4.11 Enter an invalid password, such as "a", and click Continue. Review error message. - Take SS

        disneyPlusAppleTVChangePasswordPage.clickPasswordField();
        disneyPlusAppleTVChangePasswordPage.enterPassword("Abc12!");
        pause(2);
        getScreenshots("12_1_Weak_Password", baseDirectory);
        //Screenshot 12.1. Apple TV S4.12 Enter a random password and review the text changing (Do not save!!). Review Fair and Good. (shown in the screenshot here). - Take SS

        disneyPlusAppleTVHomePage.clickMenu();
        disneyPlusAppleTVChangePasswordPage.clickPasswordField();
        disneyPlusAppleTVChangePasswordPage.enterPassword("Abc12!qrs");
        pause(2);
        getScreenshots("12_2_Fair_Password", baseDirectory);
        //Screenshot 12.2. Apple TV S4.12 Enter a random password and review the text changing (Do not save!!). Review Fair and Good. (shown in the screenshot here). - Take SS

        disneyPlusAppleTVHomePage.clickMenu();
        disneyPlusAppleTVChangePasswordPage.clickPasswordField();
        disneyPlusAppleTVChangePasswordPage.enterPassword("Abc12!qrsdeF");
        pause(2);
        getScreenshots("12_3_Strong_Password", baseDirectory);
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
        getScreenshots("13_Logout_All_Devices_Empty_Password", baseDirectory);
        //Screenshot 13. Apple TV S4.13 Go back to Account Setting menu and now select Log out of all devices. Leave the field empty and select Log Out. Review error message. - Take SS

        disneyPlusAppleTVChangePasswordPage.clickPasswordField();
        disneyPlusAppleTVChangePasswordPage.enterPassword("a");
        disneyPlusAppleTVForgotPasswordPage.keyPressTimes(disneyPlusApplePageBase.getClickActionBasedOnLocalizedKeyboardOrientation(), 6, 1);
        disneyPlusAppleTVForgotPasswordPage.clickSelect();
        disneyPlusAppleTVHomePage.clickPrimaryButton();
        pause(2);
        getScreenshots("14_Logout_All_Devices_Incorrect_Password", baseDirectory);
        //Screenshot 14. Apple TV S4.14 Enter incorrect password and and select Log Out. Review error messages. - Take SS

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = " Capture Subscriber UI Flows: PCON", groups = {"SubscriberFlowSettingsPCON", "SubUI"})
    public void captureSubscriberUIFlowSettingsPCON() {
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVSearchPage disneyPlusAppleTVSearchPage = new DisneyPlusAppleTVSearchPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());

        baseDirectory.set("Screenshots/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "Subscriber", DateUtils.now()));

        DisneyAccount entitledUser = disneyAccountApi.createAccount(ENTITLEMENT_LOOKUP, region.get(), regionLanguage.get(), SUB_VERSION);
        disneyAccountApi.addProfile(entitledUser, KIDS_PROFILE_NAME, regionLanguage.get(), null, true);
        List<DisneyProfile> profiles = disneyAccountApi.getDisneyProfiles(entitledUser);
        entitledUser.setProfiles(profiles);

        //S6.5 Set the kid-proof exit in web or mobile.
        disneyAccountApi.editKidProofExitSettingForKidProfile(entitledUser, KIDS_PROFILE_NAME, true);

        //  S6.1 Set profile PIN for main profile
        try {
            disneyAccountApi.updateProfilePin(entitledUser, entitledUser.getProfileId(DEFAULT_PROFILE), "1234");
        } catch (Exception e) {
            throw new SkipException("Failed to update Profile pin: {}", e);
        }

        //S6.3 Set the Content rating to the lowest level
        List<DisneyProfile> profileList = disneyAccountApi.getDisneyProfiles(entitledUser);
        String system = profileList.get(0).getAttributes().getParentalControls().getMaturityRating().getRatingSystem();
        List<String> ratings = profileList.get(0).getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues();
        disneyAccountApi.editContentRatingProfileSetting(entitledUser, system, ratings.get(0));

        disneyPlusAppleTVHomePage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVHomePage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(entitledUser.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(entitledUser.getUserPass());
        disneyPlusAppleTVHomePage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWhoIsWatchingPage.getTypeCellLabelContains(DEFAULT_PROFILE).clickIfPresent();
        disneyPlusAppleTVHomePage.isOpened();
        pause(5);// wait for screen to load
        getScreenshots("1_enter_your_pin_screen", baseDirectory);

        //S6.2 Enter incorrect PIN. Review the error message.
        IntStream.range(0, 4).forEach(i ->
                disneyPlusAppleTVHomePage.clickSelect()
        );
        pause(5);
        getScreenshots("2_incorrect_pin_error_message", baseDirectory);

        //enter correct pin, this will enter "1234"
        disneyPlusAppleTVHomePage.clickRight();
        IntStream.range(0, 4).forEach(i -> {
            disneyPlusAppleTVHomePage.clickSelect();
            disneyPlusAppleTVHomePage.clickRight();

        });
        //S6.3  Go to Search menu and enter "avatar" in search field. Review the message on the bottom.
        disneyPlusAppleTVHomePage.isOpened();
        pause(5);// wait for screen to load
        disneyPlusAppleTVHomePage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(SEARCH.getText());
        disneyPlusAppleTVSearchPage.isOpened();
        disneyPlusAppleTVSearchPage.typeInSearchField("Avatar");
        pause(3);
        getScreenshots("3_avatar_search_on_lowest_rating_profile", baseDirectory);

        //S6.4 Go to Kids profile, search for "avatar", review different disclaimer.
        disneyPlusAppleTVHomePage.openGlobalNavAndSelectOneMenu(PROFILE.getText());
        new DisneyPlusAppleTVWhoIsWatchingPage(getDriver()).isOpened();
        disneyPlusAppleTVHomePage.getTypeCellLabelContains(KIDS_PROFILE_NAME).clickIfPresent();
        disneyPlusAppleTVHomePage.dismissUnexpectedErrorAlert();
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
        getScreenshots("4_avatar_search_on_kids_profile", baseDirectory);

        //S6.5 Go to kids profile in connected device. Review the kid-proof exit page.
        disneyPlusAppleTVHomePage.clickBack();
        disneyPlusAppleTVHomePage.clickLeft();
        disneyPlusAppleTVHomePage.navigateToOneGlobalNavMenu(PROFILE.getText());
        disneyPlusAppleTVHomePage.clickSelect();
        pause(5);//wait for screen to load
        getScreenshots("5_kid_proof_exit_screen", baseDirectory);

        //S6.6 Enter the incorrect number, then review the error message.
        IntStream.range(0, 4).forEach(i -> disneyPlusAppleTVHomePage.clickSelect());
        getScreenshots("6_kid_proof_exit_incorrect_number_error_message", baseDirectory);
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Capture Subscriber UI Flows: Settings Screenshots", groups = {"SubscriberFlowSettings", "SubUI"})
    public void captureSubscriberUIFlowSettings() {
        DisneyPlusApplePageBase disneyPlusApplePageBase = new DisneyPlusApplePageBase(getDriver());
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVHomePage disneyPlusAppleTVHomePage = new DisneyPlusAppleTVHomePage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVWhoIsWatchingPage disneyPlusAppleTVWhoIsWatchingPage = new DisneyPlusAppleTVWhoIsWatchingPage(getDriver());
        DisneyPlusAppleTVSettingsPage disneyPlusAppleTVSettingsPage = new DisneyPlusAppleTVSettingsPage(getDriver());
        DisneyPlusAppleTVLegalPage disneyPlusAppleTVLegalPage = new DisneyPlusAppleTVLegalPage(getDriver());

        DisneyAccount user = disneyAccountApi.createAccount(ENTITLEMENT_LOOKUP, region.get(), regionLanguage.get(), SUB_VERSION);
        baseDirectory.set("Screenshots/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "Subscriber", DateUtils.now()));
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();

        disneyPlusAppleTVWelcomeScreenPage.waitForWelcomePageToLoad();
        disneyPlusAppleTVWelcomeScreenPage.moveDown(1, 1);
        disneyPlusAppleTVWelcomeScreenPage.clickSelect();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());
        disneyPlusApplePageBase.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWhoIsWatchingPage.getTypeCellLabelContains(ADULT_PROFILE_NAME).clickIfPresent();
        disneyPlusAppleTVHomePage.isOpened();

        disneyPlusAppleTVHomePage.moveDownFromHeroTileToBrandTile();
        disneyPlusAppleTVHomePage.openGlobalNavWithClickingMenu();
        disneyPlusAppleTVHomePage.isGlobalNavExpanded();
        disneyPlusAppleTVHomePage.clickSettingsTab();

        disneyPlusAppleTVSettingsPage.isOpened();
        getScreenshots("1_Settings", baseDirectory);

        //Apple TV SS3.2 Click on App settings and review the page
        disneyPlusAppleTVSettingsPage.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS);
        disneyPlusAppleTVSettingsPage.isMenuOptionPresent(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS);
        getScreenshots("2_App_Settings", baseDirectory);
        disneyPlusAppleTVSettingsPage.clickMenuTimes(1, 1);

        //Apple TV S3.3 Click on Help and Review the page
        disneyPlusAppleTVSettingsPage.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.HELP);
        disneyPlusAppleTVSettingsPage.isHelpWebviewOpen();
        getScreenshots("3_Settings_Help", baseDirectory);
        disneyPlusAppleTVSettingsPage.clickTypeButton();

        //Apple TV S3.4 Back to the settings page, click on Legal.
        disneyPlusAppleTVSettingsPage.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LEGAL);
        disneyPlusAppleTVLegalPage.isOpened();
        getScreenshots("4_Settings_Legal", baseDirectory);

        disneyPlusAppleTVLegalPage.isOpened();
        disneyPlusAppleTVLegalPage.getAllLegalSectionsScreenshot("5_Settings_Legal_", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
        disneyPlusAppleTVLoginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }

    @Test(description = "Capture Subscriber UI Flows: Player Screenshots", groups = {"SubscriberFlowPlayer", "SubUI"})
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

        DisneyAccount user = disneyAccountApi.createAccount(ENTITLEMENT_LOOKUP, region.get(), regionLanguage.get(), SUB_VERSION);
        baseDirectory.set("Screenshots/");
        pathToZip.set(String.format("%s_%s_%s_%s.zip", region.get(), regionLanguage.get(), "Subscriber", DateUtils.now()));
        loginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
        applePageBase.dismissUnexpectedErrorAlert();

        welcomeScreenPage.waitForWelcomePageToLoad();
        welcomeScreenPage.moveDown(1, 1);
        welcomeScreenPage.clickSelect();
        loginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        passwordPage.logInWithPasswordLocalized(user.getUserPass());
        applePageBase.dismissUnexpectedErrorAlert();
        whoIsWatchingPage.getTypeCellLabelContains(ADULT_PROFILE_NAME).clickIfPresent();
        homePage.isOpened();
        homePage.moveDownFromHeroTileToBrandTile();
        homePage.openGlobalNavWithClickingMenu();
        homePage.clickSearchIcon();
        searchPage.isOpened();
        searchPage.typeInSearchField(SHANG_CHI);
        searchPage.clickLocalizedSearchResult(SHANG_CHI);
        detailsPage.isOpened();
        getScreenshots("1_IMAX_Enhanced_Movie_Details_Page", baseDirectory);

        detailsPage.moveDown(1, 1);
        detailsPage.moveRight(1, 1);
        pause(1); //transition
        getScreenshots("2_Details_Page_IMAX_Tab", baseDirectory);

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
        getScreenshots("3_Video_Player_Audio", baseDirectory);

        videoPlayerPage.moveRight(2, 1);
        getScreenshots("4_Video_Player_Subtitles", baseDirectory);

        videoPlayerPage.clickMenuTimes(3, 1);
        detailsPage.isOpened();
        getScreenshots("5_Details_Page_Continue", baseDirectory);

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
        getScreenshots("6_Video_Player_Because_You_Watched", baseDirectory);

        upNextPage.clickUpNextExtraActionButton();
        videoPlayerPage.moveRight(1, 1);
        videoPlayerPage.clickSelect();
        pause(2); //transition from video to details
        detailsPage.moveDown(1, 1);
        pause(1); //transition below detail fold
        detailsPage.moveRight(1, 1);
        pause(1); //transition to details tab
        getScreenshots("7_Details_Tab", baseDirectory);

        detailsPage.clickMenuTimes(2, 1);
        searchPage.isOpened();
        searchPage.moveUp(1, 1);
        searchPage.typeInSearchField(THE_SIMPSONS);
        pause(1);
        searchPage.moveDown(1, 1);
        searchPage.clickSelect();
        detailsPage.isOpened();
        getScreenshots("8_The_Simpsons_Play_Episode", baseDirectory);

        detailsPage.moveDown(1, 1);
        getScreenshots("9_The_Simpsons_Episodes", baseDirectory);

        detailsPage.moveDown(1, 1);
        detailsPage.clickSelect();
        videoPlayerPage.isSkipIntroButtonPresent();
        getScreenshots("10_The_Simpsons_S1E1_Video_Player_Skip_Intro", baseDirectory);

        commonPage.goRightTillLocalizedEndPageAppears(2, 4, 40, upNextPage.upNextExtraActionButton);
        upNextPage.isUpNextExtraActionButtonPresent();
        getScreenshots("11_The_Simpsons_Video_Player_Play_Next_Episode", baseDirectory);

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
        loginPage.pressMenuBackIfPreviouslyUsedEmailScreen();
    }
}
