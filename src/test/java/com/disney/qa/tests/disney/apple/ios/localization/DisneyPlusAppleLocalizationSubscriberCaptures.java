package com.disney.qa.tests.disney.apple.ios.localization;

import com.disney.qa.api.client.requests.CreateDisneyProfileRequest;

import java.util.Date;
import java.util.List;

import com.disney.qa.api.client.responses.profile.Profile;
import com.disney.qa.gmail.exceptions.GMailUtilsException;
import com.disney.util.TestGroup;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.DisneyPlusAccountIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusAddProfileIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusBrandIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusChangeEmailIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusChangePasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusChooseAvatarIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusContentRatingIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusDetailsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusEditProfileIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusHomeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusLoginIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMediaCollectionIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusMoreMenuIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusOriginalsIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPasswordIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusPinIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusSearchIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWelcomeScreenIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWhoseWatchingIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyplusLegalIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;

public class DisneyPlusAppleLocalizationSubscriberCaptures extends DisneyPlusAppleLocalizationBaseTest {
    private static final String SECONDARY_PROFILE = "Test_2";
    private static final String NINETEEN_EIGHTY = "1980";
    private static final String FIRST = "01";
    private static final int SWIPE_COUNTER = 5;

    //TODO: Replace this with the createProfile in AddProfilePage
    private void createProfile(String profileName, boolean isArielRegion) {
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase avatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        moreMenuPage.clickAddProfile();

        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 5, "Skip button is not present.")
                .until(it -> avatarPage.isSkipButtonPresent());
        avatarPage.clickSkipButton();

        avatarPage.typeProfileName(profileName);
        if (isArielRegion) {
            addProfilePage.enterDOB(DateHelper.Month.JANUARY, FIRST, NINETEEN_EIGHTY);
            addProfilePage.chooseGender();
        }
        avatarPage.clickSaveBtn();
    }

    @Test(dataProvider = "tuidGenerator", description = "(iOS) Section 1| Welcome page and login flow", groups = { "Subscriber - UI",
            "Subscriber - UI - S1", TestGroup.PRE_CONFIGURATION, TestGroup.PROXY })
    public void captureWelcomePageAndLoginFlow(String TUID) {
        setup();
        setZipTestName("SubscriberUI_1_welcomePage");
        String localeForTravelling;

        if (!getCountry().equalsIgnoreCase("US")) {
            localeForTravelling = "US";
        } else {
            localeForTravelling = "GB";
        }

        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(localeForTravelling)
                .language(getLocalizationUtils().getUserLanguage()).build();
        DisneyOffer disneyOffer = getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V1").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccount = getAccountApi().createAccount(request);

        getAccountApi().addFlex(testAccount);
        setAccount(testAccount);

        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase forgotPasswordPage = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);

        getScreenshots("WelcomePage");
        welcomePage.clickLogInButton();
        pause(3);
        getScreenshots("LoginPageBlank");
        loginPage.clickPrimaryButton();
        pause(3);
        getScreenshots("LoginPageContinueBlankError");
        loginPage.submitEmail("iteazxc@uyteesx.utew");
        loginPage.clickPrimaryButton();
        pause(3);
        getScreenshots("LoginPageBadEmail");
        handleSystemAlert(IOSUtils.AlertButtonCommand.ACCEPT, 10);
        pause(3);
        loginPage.submitEmail(getAccount().getEmail());
        pause(3);
        getScreenshots("LoginPageEnterYourPassword");
        loginPage.clickPrimaryButton();
        pause(3);
        getScreenshots("PasswordPageBlankPassword");
        passwordPage.clickForgotPasswordLink();
        pause(3);
        getScreenshots("PasswordPageForgotPassword");
        forgotPasswordPage.clickPrimaryButton();
        pause(3);
        getScreenshots("ForgotPasswordPageBlankCode");
        dismissKeyboardForPhone();
        forgotPasswordPage.clickResend();
        pause(3);
        getScreenshots("ForgotPasswordPageResend");
        forgotPasswordPage.clickAlertDismissBtn();
        dismissKeyboardForPhone();
        forgotPasswordPage.getBackArrow().click();
        passwordPage.submitPasswordForLogin("badpassword");
        pause(3);
        getScreenshots("PasswordPageBadPassword");
        setAppToHomeScreen(getAccount());
        pause(3);
        getScreenshots("TravellingMessage");
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S2 Watchlist and App Settings", groups = { "Subscriber - UI", "Subscriber - UI - S2",
            TestGroup.PRE_CONFIGURATION, TestGroup.PROXY })
    public void watchlistAndAppSettings(String TUID) {
        setup();
        setZipTestName("SubscriberUI_2_watchlist");
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoseWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyAccount testAccount = getAccount();

        String cellOption = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.CELLULAR_DATA_USAGE.getText());
        String wifiOption = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.WIFI_DATA_USAGE.getText());
        String videoQuality = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.VIDEO_QUALITY_TITLE.getText());

        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(testAccount).profileName("SecondaryTestProfile").language(getAccount().getProfileLang()).avatarId(null).kidsModeEnabled(false).dateOfBirth(null).build());

        welcomePage.clickLogInButton();
        loginPage.submitEmail(testAccount.getEmail());
        passwordPage.typePassword(testAccount.getUserPass());
        dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();
        pause(5);
        getScreenshots("WhoseWatchingPage");

        whoseWatchingPage.clickProfile("Test");
        //        handleAlert();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        pause(3);
        getScreenshots("ProfilePage");

        moreMenuPage.getDynamicCellByLabel(moreMenuPage.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST)).click();
        pause(3);
        getScreenshots("EmptyWatchlist");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.getDynamicCellByLabel(moreMenuPage.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS)).click();
        pause(3);
        getScreenshots("AppSettings");

        moreMenuPage.getDynamicXpathContainsName(wifiOption).click();
        pause(3);
        getScreenshots("WifiOptions");
        moreMenuPage.getBackArrow().click();

        moreMenuPage.getDynamicXpathContainsName(cellOption).click();
        pause(3);
        getScreenshots("CellOptions");
        moreMenuPage.getBackArrow().click();

        moreMenuPage.getDynamicXpathContainsName(videoQuality).click();
        pause(3);
        getScreenshots("VideoQuality");
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S3 Profile menu: Account & Help", groups = { "Subscriber - UI", "Subscriber - UI - S3",
            TestGroup.PRE_CONFIGURATION, TestGroup.PROXY })
    public void AccountsAndHelp(String TUID) throws GMailUtilsException {
        setup();
        setZipTestName("SubscriberUI_3_accounts");
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase forgotPasswordPage = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);
        EmailApi emailApi = getEmailApi();

        String locale = getLocalizationUtils().getLocale();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).language(getLocalizationUtils().getUserLanguage())
                .build();
        DisneyOffer disneyOffer = getAccountApi().lookupOfferToUse(locale, "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V1").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccount = getAccountApi().createAccountForOTP(request);
        setAccount(testAccount);

        welcomePage.clickLogInButton();
        loginPage.submitEmail(testAccount.getEmail());
        passwordPage.typePassword(testAccount.getUserPass());
        dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickMenuOptionByIndex(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        pause(3);
        int accountPageShotCount = 1;
        getScreenshots("AccountPagePart_" + accountPageShotCount);

        while (!moreMenuPage.getDeleteAccountButton().isElementPresent()) {
            accountPageShotCount += 1;
            swipeInContainer(null, Direction.UP, 500);
            pause(2);
            getScreenshotsNoCountUpdate("AccountPagePart" + accountPageShotCount);
        }

        while (!accountPage.isChangeLinkPresent(testAccount.getEmail())) {
            swipeInContainer(null, Direction.DOWN, 500);
        }

        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 5, "Change link was not present")
                .until(it -> accountPage.isChangeLinkPresent(testAccount.getEmail()));
        accountPage.clickChangeLink(testAccount.getEmail());
        pause(2);
        getScreenshots("ChangeEmailPage");
        forgotPasswordPage.clickPrimaryButton();

        dismissKeyboardForPhone();
        pause(3);
        getScreenshots("ChangeEmailPageBlankCode");
        dismissKeyboardForPhone();
        forgotPasswordPage.clickResend();
        pause(3);
        getScreenshots("ChangeEmailPageResend");
        forgotPasswordPage.clickAlertDismissBtn();
        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValueDismissKeys(
                emailApi.getDisneyOTP(testAccount.getEmail()));
        pause(3);
        dismissKeyboardForPhone();
        getScreenshots("ChangeEmailPageAfterOtp");

        changeEmailPage.clickLogoutAllDevices();
        pause(3);
        getScreenshots("EmailLogOutOfAllDevices");

        changeEmailPage.clickSaveBtn();
        dismissKeyboardForPhone();
        pause(3);
        getScreenshots("EmailEmptySave");

        changeEmailPage.submitNewEmailAddress("a");
        dismissKeyboardForPhone();
        pause(3);
        getScreenshots("EmailBadEmailSave");

        dismissKeyboardForPhone();
        changeEmailPage.clickCancelBtn();

        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 5, "Change link was not present")
                .until(it -> {
                    if (!DEBUG_MODE) {
                        return accountPage.isChangeLinkPresent(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                                DictionaryKeys.HIDDEN_PASSWORD.getText()));
                    } else {
                        return accountPage.isChangeLinkPresent(DictionaryKeys.HIDDEN_PASSWORD.getText());
                    }
                });
        if (!DEBUG_MODE) {
            accountPage.clickChangeLink(
                    getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.HIDDEN_PASSWORD.getText()));
        } else {
            accountPage.clickChangeLink(DictionaryKeys.HIDDEN_PASSWORD.getText());
        }

        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValueDismissKeys(
                emailApi.getDisneyOTP(testAccount.getEmail()));
        pause(3);
        dismissKeyboardForPhone();
        getScreenshots("ChangePasswordPage");

        DisneyPlusChangePasswordIOSPageBase changePasswordPage = initPage(DisneyPlusChangePasswordIOSPageBase.class);
        changePasswordPage.clickLogoutAllDevices();
        pause(3);
        getScreenshots("ChangePasswordPageLogOutOfAll");

        passwordPage.typePassword("123");
        pause(2);
        getScreenshots("BadPassword");

        passwordPage.typePassword("local123");
        pause(2);
        getScreenshots("FairPassword");

        passwordPage.typePassword("local1234");
        pause(2);
        getScreenshots("GoodPassword");

        passwordPage.typePassword("local123b456@");
        pause(2);
        getScreenshots("ExcellentPassword");

        passwordPage.typePassword("a");
        changePasswordPage.clickSaveBtn();
        dismissKeyboardForPhone();
        pause(3);
        getScreenshots("InvalidPasswordError");

        changePasswordPage.clickCancelBtn();
        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 2, "Log out of all devices is not present")
                .until(it -> accountPage.isLogOutOfAllDevicesLinkPresent());
        accountPage.clickLogOutOfAllDevices();
        pause(3);
        getScreenshots("LogoutAllDevicesPage");

        accountPage.clickPrimaryButton();
        dismissKeyboardForPhone();
        pause(3);
        getScreenshots("LogoutAllDevicesEmptyPassword");

        passwordPage.typePassword("a");
        accountPage.clickPrimaryButton();
        dismissKeyboardForPhone();
        pause(3);
        getScreenshots("LogoutAllDevicesWrongPassword");
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S4 Legal", groups = { "Subscriber - UI", "Subscriber - UI - S4",
            TestGroup.PRE_CONFIGURATION, TestGroup.PROXY })
    public void legal(String TUID) {
        setup();
        setZipTestName("SubscriberUI_4_legal");
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyplusLegalIOSPageBase legalPage = initPage(DisneyplusLegalIOSPageBase.class);
        DisneyAccount testAccount = getAccount();

        welcomePage.clickLogInButton();
        loginPage.submitEmail(testAccount.getEmail());
        passwordPage.typePassword(testAccount.getUserPass());

        dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.getDynamicCellByLabel(moreMenuPage.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LEGAL)).click();
        pause(3);
        getScreenshots("LegalLandingPage");

        List<ExtendedWebElement> legalTitles = legalPage.findExtendedWebElements(legalPage.getTypeButtonBy());
        legalTitles.forEach(legalTitle -> {
            if (!legalTitle.getAttribute("name").equals("buttonBack")) {
                legalTitle.click();
                getScreenshots("LegalOptions" + legalTitle.getAttribute("label"));
            }
        });

        legalPage.getBackArrow().click();

        moreMenuPage.getDynamicCellByLabel(moreMenuPage.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.HELP)).click();
        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 5, "Help page did not open.")
                .until(it -> moreMenuPage.isHelpWebviewOpen());
        pause(10);
        getScreenshots("HelpWebview");
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S5 Profile menu: Edit profiles", groups = { "Subscriber - UI", "Subscriber - UI - S5",
            TestGroup.PRE_CONFIGURATION, TestGroup.PROXY })
    public void profileMenu(String TUID) {
        setup();
        setZipTestName("SubscriberUI_5_profileMenu");
        boolean isArielRegion = getLocalizationUtils().getCountryName().equals("United States");

        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase avatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyAccount testAccount = getAccount();

        welcomePage.clickLogInButton();
        loginPage.fillOutEmailField(testAccount.getEmail());
        loginPage.clickPrimaryButton();
        passwordPage.typePassword(testAccount.getUserPass());
        dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickEditProfilesBtn();

        editProfilePage.clickEditModeProfile(DEFAULT_PROFILE);

        pause(3);
        getScreenshots("EditProfilePage");

        editProfilePage.clickDoneBtn();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        pause(3);
        getScreenshots("ProfilePage");

        moreMenuPage.clickAddProfile();

        avatarPage.clickSkipButton();

        avatarPage.clickSaveBtn();
        hideKeyboard();
        pause(2);
        getScreenshots("EmptyProfileName");

        avatarPage.typeProfileName(DEFAULT_PROFILE);
        if (isArielRegion) {
            addProfilePage.enterDOB(DateHelper.Month.JANUARY, FIRST, NINETEEN_EIGHTY);
            addProfilePage.chooseGender();
        }
        avatarPage.clickSaveBtn();
        hideKeyboard();
        pause(2);
        getScreenshots("DuplicateProfileName");

        avatarPage.typeProfileName("Test_a");
        avatarPage.clickSaveBtn();
        pause(2);
        getScreenshots("ReviewProfile");

        avatarPage.clickPrimaryButton();
        pause(2);
        getScreenshots("PinPage");

        avatarPage.clickPrimaryButton();
        pause(2);
        getScreenshots("EmptyPinError");

        avatarPage.clickSecondaryButton();

        //TODO: Replace this method call with call to createProfile in addProfilePage
        createProfile("Test_b", isArielRegion);
        avatarPage.clickSecondaryButton();

        moreMenuPage.getProfileAvatar("Test_b").click();
        pause(2);
        getScreenshots("ContentRatingPage");

        moreMenuPage.clickPrimaryButton();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);

        moreMenuPage.clickMenuOption(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT);

        for (int i = 0; i < 4; i++) {
            getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(testAccount).profileName("Test_" + i).language(getLanguage()).avatarId(null).kidsModeEnabled(false).dateOfBirth(null).build());
        }
        restart();

        welcomePage.clickLogInButton();
        loginPage.fillOutEmailField(testAccount.getEmail());
        loginPage.clickPrimaryButton();
        passwordPage.typePassword(testAccount.getUserPass());
        dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();

        whoPage.clickProfile(DEFAULT_PROFILE);

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickEditProfilesBtn();
        pause(2);
        getScreenshots("MaxProfileEditPage");

        moreMenuPage.getProfileAvatar("Test_0").click();

        int profilePageShotCount = 1;
        pause(2);
        getScreenshots("EditProfilePagePart" + profilePageShotCount);

        while (!editProfilePage.getDeleteProfileButton().isElementPresent()) {
            profilePageShotCount += 1;
            swipeInContainer(null, Direction.UP, 500);
            pause(2);
            getScreenshotsNoCountUpdate("EditProfilePagePart" + profilePageShotCount);
        }

        while (!editProfilePage.isAppLanguageCellPresent()) {
            swipeInContainer(null, Direction.DOWN, 500);
        }

        editProfilePage.clickAppLanguage();
        getScreenshotsNoCountUpdate("AppLanguagePage");

        editProfilePage.getBackArrow().click();

        while (!editProfilePage.getDeleteProfileButton().isElementPresent()) {
            swipeInContainer(null, Direction.UP, 500);
        }

        editProfilePage.getDeleteProfileButton().click();
        pause(2);
        getScreenshots("DeleteProfileConfirmation");

        editProfilePage.clickConfirmDeleteButton();

        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 3, "Who is Watching Page never appeared.")
                .until(it -> whoPage.isHeaderTextPresent());

        swipeInContainer(null, Direction.UP, 500);
        pause(2);
        getScreenshots("AfterDeleteWhoIsWatchingPage");
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S6 PCON", groups = { "Subscriber - UI", "Subscriber - UI - S6",
            TestGroup.PRE_CONFIGURATION, TestGroup.PROXY })
    public void PCON(String TUID) {
        setup();
        setZipTestName("SubscriberUI_6_PCON");
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRatingPage = initPage(DisneyPlusContentRatingIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusPinIOSPageBase pinPage = initPage(DisneyPlusPinIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyAccount testAccount = getAccount();
        Profile profile = getAccount().getProfile(DEFAULT_PROFILE);
        String ratingSystem = profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystem();

        //create a second account for a later step
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(getLocalizationUtils().getLocale())
                .language(getLocalizationUtils().getUserLanguage()).build();
        DisneyOffer disneyOffer = getAccountApi().lookupOfferToUse(getLocalizationUtils().getLocale(), "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V2").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccountTwoProfiles = getAccountApi().createAccount(request);
        getAccountApi().addProfile(CreateDisneyProfileRequest.builder().disneyAccount(testAccountTwoProfiles).profileName(SECONDARY_PROFILE).dateOfBirth("2018-01-01").language(getLocalizationUtils().getUserLanguage()).avatarId(null).kidsModeEnabled(false).isStarOnboarded(true).build());
        //set account to the lowest rating for a step later on
        getAccountApi().editContentRatingProfileSetting(testAccount,
                getAccountApi().getProfiles(testAccountTwoProfiles).get(1).getProfileId(),
                ratingSystem,
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues().get(0));

        loginDismiss(testAccount);

        //S6.1
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickEditProfilesBtn();
        pause(3);
        editProfilePage.clickEditModeProfile(DEFAULT_PROFILE);

        swipePageTillElementTappable(editProfilePage.getPinSettingsCell(), 3, null, Direction.UP, 500);

        pause(2);
        getScreenshots("PconMenuEditProfilePage");

        //S6.2
        editProfilePage.getContentRatingHeader().click();
        pause(2);
        getScreenshots("ContentRatingPasswordPage");

        //S6.3
        passwordPage.clickPrimaryButton();
        pause(2);
        getScreenshots("EmptyPassword");

        passwordPage.typePassword("a");
        passwordPage.clickPrimaryButton();
        pause(2);
        getScreenshots("BadPassword");

        //S6.4
        passwordPage.typePassword(testAccount.getUserPass());
        passwordPage.clickPrimaryButton();
        pause(2);
        getScreenshots("RatingLevels");

        //S6.5 - S6.11
        for (String rating : profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues()) {
            String localizedRating = getLocalizationUtils().getLocalizedRating(ratingSystem, rating);
            ExtendedWebElement currentRating = contentRatingPage
                    .getDynamicAccessibilityId(localizedRating);

            swipePageTillElementTappable(currentRating, 3, null, Direction.UP, 1000);

            currentRating.click();
            pause(2);
            getScreenshots("Rating" + localizedRating);

            contentRatingPage.clickSaveBtn();
            pause(2);
            getScreenshotsNoCountUpdate("RatingProfilePage" + localizedRating);

            editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
            editProfilePage.getContentRatingHeader().click();
            passwordPage.typePassword(testAccount.getUserPass());
            passwordPage.clickPrimaryButton();
        }

        //S6.12
        contentRatingPage.getContentRatingInfoButton().click();
        pause(2);
        getScreenshots("ContentRatingInfo");

        //S6.13
        contentRatingPage.getGotItButton().click();
        contentRatingPage.clickSaveBtn();
        contentRatingPage.clickSaveProfileButton();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.getDynamicCellByLabel(moreMenuPage.selectMoreMenu(DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT)).click();
        welcomePage.clickLogInButton();
        loginPage.fillOutEmailField(testAccountTwoProfiles.getEmail());
        loginPage.clickPrimaryButton();
        passwordPage.typePassword(testAccountTwoProfiles.getUserPass());
        dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();

        //S6.28
        whoPage.clickProfile(SECONDARY_PROFILE);
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);
        searchPage.searchForMedia("Avatar");
        searchPage.getKeyboardSearchButton().click();
        pause(3);
        getScreenshots("AvatarSearchResultLowestRating");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickEditProfilesBtn();
        editProfilePage.clickEditModeProfile(SECONDARY_PROFILE);

        swipePageTillElementTappable(editProfilePage.getKidProofExitLabel(), 3, null, Direction.UP, 1000);
        editProfilePage.getKidProofExitLabel().click();
        pause(2);
        getScreenshots("KidExitProofError");

        //S6.14
        editProfilePage.getKidsProfileToggleSwitch().click();

        //S6.15
        swipePageTillElementTappable(editProfilePage.getSharePlay(), 3, null, Direction.DOWN, 1000);
        editProfilePage.getSharePlay().click();
        pause(2);
        getScreenshots("GroupWatchKidsModeError");

        //S6.16
        editProfilePage.clickDoneBtn();
        pause(3);
        initPage(DisneyPlusApplePageBase.class).getDynamicAccessibilityId(DisneyPlusApplePageBase.FooterTabs.MORE_MENU.getLocator()).click();
        whoPage.clickEditProfile();
        editProfilePage.clickEditModeProfile(SECONDARY_PROFILE);

        swipePageTillElementTappable(editProfilePage.getPinSettingsCell(), 3, null, Direction.UP, 1000);
        editProfilePage.getPinSettingsCell().click();
        passwordPage.typePassword(testAccount.getUserPass());
        passwordPage.clickPrimaryButton();
        pause(2);
        getScreenshots("PinPage");

        //S6.17
        pinPage.getPinCheckBox().click();
        editProfilePage.clickSaveBtn();
        pause(2);
        getScreenshots("PinEmptyPinError");

        //S6.18
        pinPage.getPinInputField().click();
        pinPage.getPinInputField().type("1111");

        editProfilePage.clickSaveBtn();
        pause(2);
        getScreenshots("PinSetPage");

        //S6.19
        editProfilePage.clickSaveProfileButton();
        initPage(DisneyPlusApplePageBase.class)
                .getDynamicAccessibilityId(DisneyPlusApplePageBase.FooterTabs.MORE_MENU.getLocator()).click();

        whoPage.clickPinProtectedProfile(SECONDARY_PROFILE);
        pause(2);
        getScreenshots("WhosWatchingPagePin");

        //S6.20
        pinPage.getPinInputField().click();
        pinPage.getPinInputField().type("2222");
        pause(2);
        getScreenshots("WrongPinError");

        //S6.21
        pinPage.getForgotPinButton().click();
        pause(2);
        getScreenshots("ForgotPin");

        //S6.22
        pinPage.getBackArrow().click();
        pause(5);
        pinPage.getPinCancelButton().click();
        pause(5);
        whoPage.clickEditProfile();
        editProfilePage.clickEditModeProfile(SECONDARY_PROFILE);

        swipePageTillElementTappable(editProfilePage.getKidProofExitToggleSwitch(), 3, null, Direction.UP, 1000);
        editProfilePage.getKidProofExitToggleSwitch().click();
        passwordPage.typePassword(testAccount.getUserPass());
        passwordPage.clickPrimaryButton();
        pause(5);
        getScreenshots("KidsProofModeOn");

        //S6.23
        editProfilePage.clickDoneBtn();
        initPage(DisneyPlusApplePageBase.class)
                .getDynamicAccessibilityId(DisneyPlusApplePageBase.FooterTabs.MORE_MENU.getLocator()).click();
        whoPage.clickPinProtectedProfile(SECONDARY_PROFILE);
        pinPage.getPinInputField().click();
        pinPage.getPinInputField().type("1111");
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);
        searchPage.searchForMedia("Avatar");
        searchPage.getKeyboardSearchButton().click();
        pause(3);
        getScreenshots("AvatarSearchResultKidsProfile");

        //S6.24
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        pause(3);
        getScreenshots("ExitKidsProfilePage");

        //S6.25
        moreMenuPage.getExitKidsProfile().click();
        pause(2);
        getScreenshots("ExitKidsProfileCodeGiven");

        moreMenuPage.getExitJuniorModePin().click();
        moreMenuPage.getExitJuniorModePin().type("1111");
        dismissKeyboardForPhone();
        pause(2);
        getScreenshots("ExitKidsProfileWrongCodeError");
    }


    @Test(dataProvider = "tuidGenerator", description = "iOS S8 Bottom navigation - Search, Movies, Originals and Series", groups = {
            "Subscriber - UI", "Subscriber - UI - S8", TestGroup.PRE_CONFIGURATION, TestGroup.PROXY })
    public void searchMoviesAndSeries(String TUID) {
        setup();
        setZipTestName("SubscriberUI_8_search");
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        DisneyPlusMediaCollectionIOSPageBase mediaCollectionPage = initPage(DisneyPlusMediaCollectionIOSPageBase.class);
        DisneyAccount testAccount = getAccount();

        loginDismiss(testAccount);

        //S8.1
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);
        searchPage.getOriginalsTile().isElementPresent();
        getScreenshots("SearchPage");

        //S8.2
        searchPage.searchForMedia(
                "jjjjjjjlkshjdai8or7325uihad87t87t86686&^%&#^%!^@%&#^!@%($^%#@!&SUAS&^$#&124124345^%!$#&^#%sadas2451234124!&$^%!&@$sasda&^#535323$%*#&^$%&*");
        getScreenshots("BadSearch");

        //S8.3
        searchPage.searchForMedia("Avatar");
        List<ExtendedWebElement> movies = searchPage.getDisplayedTitles();
        movies.get(0).click();
        detailsPage.getBackArrow().click();
        //Empty string to clear the keys
        searchPage.searchForMedia("");
        getScreenshots("RecentSearch", false);
        searchPage.getCancelButton().click();

        //S8.4
        CollectionRequest collectionRequest = CollectionRequest.builder()
                .region(getLocalizationUtils().getLocale())
                .audience("false")
                .language(getLocalizationUtils().getUserLanguage())
                .slug(DisneyStandardCollection.ORIGINALS.getSlug())
                .contentClass(DisneyStandardCollection.ORIGINALS.getContentClass())
                .account(testAccount)
                .build();

        ContentCollection contentCollection = getSearchApi().getCollection(collectionRequest);
        List<DisneyCollectionSet> setInfo = contentCollection.getCollectionSetsInfo();

        searchPage.clickOriginalsTab();
        originalsPage.isOpened();
        getScreenshots("OriginalsLandingPage");

        ExtendedWebElement currentElement;
        for (DisneyCollectionSet set : setInfo) {
            currentElement = searchPage.getXpathName(set.getContent());
            swipe(currentElement);
            getScreenshotsNoCountUpdate("Originals" + set.getContent());
        }

        //S8.5
        searchPage.getBackArrow().click();
        searchPage.clickMoviesTab();
        mediaCollectionPage.getMoviesHeader().isElementPresent();
        getScreenshots("MoviesLandingPage");

        //S8.6
        collectionRequest = CollectionRequest.builder()
                .region(getLocalizationUtils().getLocale())
                .audience("false")
                .language(getLocalizationUtils().getUserLanguage())
                .slug(DisneyStandardCollection.MOVIES.getSlug())
                .contentClass(DisneyStandardCollection.MOVIES.getContentClass())
                .account(testAccount)
                .build();

        contentCollection = getSearchApi().getCollection(collectionRequest);
        setInfo = contentCollection.getCollectionSetsInfo();
        //remove first object since it is in a different format and will be in the screenshot regardless
        setInfo.remove(0);

        //Tablets and phones have a different UI setup thus we have to split the flow in this loop depending on our device
        for (DisneyCollectionSet set : setInfo) {
            if (getDevice().getDeviceType().equals(DeviceType.Type.IOS_PHONE)) {
                currentElement = searchPage.getStaticTextByLabel(set.getContent());
                mediaCollectionPage.getMediaCategoryDropdown().click();
                swipe(currentElement);
            } else {
                currentElement = searchPage.getXpathName(set.getContent());
                swipe(currentElement, mediaCollectionPage.getCategoryScroller(), Direction.LEFT);
            }
            currentElement.click();
            mediaCollectionPage.getMoviesHeader().isElementPresent();
            getScreenshotsNoCountUpdate("Movies" + set.getContent());
        }

        //S8.7
        searchPage.getBackArrow().click();
        searchPage.clickSeriesTab();
        mediaCollectionPage.getSeriesHeader().isElementPresent();
        getScreenshots("SeriesLandingPage");

        collectionRequest = CollectionRequest.builder()
                .region(getLocalizationUtils().getLocale())
                .audience("false")
                .language(getLocalizationUtils().getUserLanguage())
                .slug(DisneyStandardCollection.SERIES.getSlug())
                .contentClass(DisneyStandardCollection.SERIES.getContentClass())
                .account(testAccount)
                .build();

        contentCollection = getSearchApi().getCollection(collectionRequest);
        setInfo = contentCollection.getCollectionSetsInfo();
        //remove first object since it is in a different format and will be in the screenshot regardless
        setInfo.remove(0);

        for (DisneyCollectionSet set : setInfo) {
            if (getDevice().getDeviceType().equals(DeviceType.Type.IOS_PHONE)) {
                currentElement = searchPage.getStaticTextByLabel(set.getContent());
                mediaCollectionPage.getMediaCategoryDropdown().click();
                swipe(currentElement);
            } else {
                currentElement = searchPage.getXpathName(set.getContent());
                swipe(currentElement, mediaCollectionPage.getCategoryScroller(), Direction.LEFT);
            }
            currentElement.click();
            mediaCollectionPage.getSeriesHeader().isElementPresent();
            getScreenshotsNoCountUpdate("Series" + set.getContent());
        }
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S10 - Welch Onboarding", groups = { "Subscriber - UI", "Subscriber - UI - S10",
            TestGroup.PRE_CONFIGURATION, TestGroup.PROXY })
    public void welchOnboarding(String TUID) {
        setup();
        setZipTestName("SubscriberUI_10_welch");
        String locale = getLocalizationUtils().getLocale();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).language(getLocalizationUtils().getUserLanguage())
                .isStarOnboarded(false).build();
        DisneyOffer disneyOffer = getAccountApi().lookupOfferToUse(locale, "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V1").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccount = getAccountApi().createAccount(request);
        getAccountApi().addFlex(testAccount);
        setAccount(testAccount);

        DisneyPlusWhoseWatchingIOSPageBase whoPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAddProfileIOSPageBase addProfilePage = initPage(DisneyPlusAddProfileIOSPageBase.class);
        DisneyPlusPinIOSPageBase pinPage = initPage(DisneyPlusPinIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase avatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);

        loginDismiss(testAccount);

        //S10.4
        whoPage.isHeaderTextPresent();
        whoPage.clickProfile(DEFAULT_PROFILE);
        homePage.isOpened();

        whoPage.clickPrimaryButton();
        pause(2);
        getScreenshots("WelchAccessTheFullCatalog");

        //S10.5
        whoPage.clickSecondaryButton();
        pause(2);
        getScreenshots("WelchNotNow");

        //S10.6
        whoPage.clickPrimaryButton();
        pause(2);
        getScreenshots("WelchYourContentRatingIsSet");

        //S10.7
        whoPage.clickPrimaryButton();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickAddProfile();
        avatarPage.waitUntil(ExpectedConditions.elementToBeClickable(avatarPage.getSkipButton().getBy()), 60);
        avatarPage.clickSkipButton();

        if (getLocalizationUtils().getLocale().equals("US")) {
            addProfilePage.createProfile(SECONDARY_PROFILE, DateHelper.Month.MARCH, "01", "2000");
        } else {
            addProfilePage.createProfile(SECONDARY_PROFILE);
        }

        pause(2);
        getScreenshots("WelchAccessTheFullCatalog");

        //S10.8
        addProfilePage.clickPrimaryButton();
        pause(2);
        getScreenshots("WelchPinPage");

        //S10.9
        pinPage.getPinInputField().type("1111");
        pinPage.clickPrimaryButton();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        pause(2);
        getScreenshots("WelchLockedProfile");
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S13 Editorial Contents", groups = { "Subscriber - UI", "Subscriber - UI - S13",
            TestGroup.PRE_CONFIGURATION, TestGroup.PROXY })
    public void editorialContents(String TUID) {
        setup();
        setZipTestName("SubscriberUI_13_editorial_contents");
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyAccount testAccount = getAccount();
        loginDismiss(testAccount);

        //setup the continue watching collection since this is a fresh account
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);
        searchPage.searchForMedia("Simpsons");
        List<ExtendedWebElement> movies = searchPage.getDisplayedTitles();
        movies.get(0).click();
        detailsPage.clickPlayButton();
        //let it play for just a few seconds to go into the "continue watching" collection
        pause(10);
        videoPlayerPage.clickBackButton();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.HOME);

        //S13.1
        homePage.isOpened();
        pause(2);
        getScreenshots("HomeLandingPage");

        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, Direction.UP, 500);
            getScreenshotsNoCountUpdate("Home" + i);
        }

        //S13.2
        //Swipe to a brand page, swipe a few times and take screenshots
        swipe(homePage.getDisneyTile(), Direction.DOWN);
        homePage.clickDisneyTile();
        brandPage.isOpened();
        pause(2);
        getScreenshots("DisneyLandingPage");

        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, Direction.UP, 500);
            getScreenshotsNoCountUpdate("DisneyBrand" + i);
        }

        swipe(homePage.getBackArrow(), Direction.DOWN);
        homePage.getBackArrow().click();
        homePage.clickPixarTile();
        brandPage.isOpened();
        pause(2);
        getScreenshots("PixarLandingPage");

        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, Direction.UP, 500);
            getScreenshotsNoCountUpdate("PixarBrand" + i);
        }

        swipe(homePage.getBackArrow(), Direction.DOWN);
        homePage.getBackArrow().click();
        homePage.clickMarvelTile();
        brandPage.isOpened();
        pause(2);
        getScreenshots("MarvelLandingPage");

        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, Direction.UP, 500);
            getScreenshotsNoCountUpdate("MarvelBrand" + i);
        }

        swipe(homePage.getBackArrow(), Direction.DOWN);
        homePage.getBackArrow().click();
        homePage.clickStarWarsTile();
        brandPage.isOpened();
        pause(2);
        getScreenshots("StarWarsLandingPage");

        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, Direction.UP, 500);
            getScreenshotsNoCountUpdate("StarWarsBrand" + i);
        }

        swipe(homePage.getBackArrow(), Direction.DOWN);
        homePage.getBackArrow().click();
        homePage.clickNatGeoTile();
        brandPage.isOpened();
        pause(2);
        getScreenshots("NatGeoLandingPage");

        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, Direction.UP, 500);
            getScreenshotsNoCountUpdate("NatGeoBrand" + i);
        }

        swipe(homePage.getBackArrow(), Direction.DOWN);
        homePage.getBackArrow().click();

        if (homePage.isStarTilePresent()) {
            homePage.clickStarTile();
            brandPage.isOpened();
            pause(2);
            getScreenshots("StarLandingPage");

            for (int i = 0; i < SWIPE_COUNTER; i++) {
                swipeInContainer(null, Direction.UP, 500);
                getScreenshotsNoCountUpdate("StarBrand" + i);
            }

        }
    }

}
