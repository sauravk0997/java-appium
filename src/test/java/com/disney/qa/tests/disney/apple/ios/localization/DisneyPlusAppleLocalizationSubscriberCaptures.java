package com.disney.qa.tests.disney.apple.ios.localization;

import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.client.requests.content.CollectionRequest;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.client.responses.profile.DisneyProfile;
import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyEntitlement;
import com.disney.qa.api.pojos.DisneyOffer;
import com.disney.qa.api.search.assets.DisneyStandardCollection;
import com.disney.qa.api.search.sets.DisneyCollectionSet;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.common.web.VerifyEmail;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.disney.util.ZipUtils;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.utils.mobile.IMobileUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.List;

import static com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase.getDictionary;

public class DisneyPlusAppleLocalizationSubscriberCaptures extends DisneyPlusAppleLocalizationBaseTest {

    private static final String SECONDARY_PROFILE = "Test_2";
    private static final String WATCH_AGAIN_SET_REF_ID = "6e365205-5805-c877-9e04-6443a40523f2";
    private static final String BECAUSE_YOU_WATCHED_SET_ID = "2724a4f6-caf9-4b9a-9b7f-54f1f108d833";
    private static final String BECAUSE_YOU_WATCHED_SET_ID_2 = "bd1bfb9a-bbf7-43a0-ac5e-3e3889d7224d";
    public static final int SWIPE_COUNTER = 5;

    //TODO: Replace this with the createProfile in AddProfilePage
    private void createProfile(String profileName) {
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase avatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        moreMenuPage.clickAddProfile();

        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, SWIPE_COUNTER, "Skip button is not present.")
                .until(it -> avatarPage.isSkipButtonPresent());
        avatarPage.clickSkipButton();

        avatarPage.typeProfileName(profileName);
        avatarPage.clickSaveBtn();
    }

    @Test(dataProvider = "tuidGenerator", description = "(iOS) Section 1| Welcome page and login flow", groups = {"Subscriber - UI", "Subscriber - UI - S1"})
    public void captureWelcomePageAndLoginFlow(String TUID) {
        setup();
        String localeForTravelling;
        setPathToZip("SubscriberUI_1_welcomePage");

        if (!R.CONFIG.get("locale").equals("US")) {
            localeForTravelling = "US";
        } else {
            localeForTravelling = "GB";
        }

        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(localeForTravelling).language(languageUtils.get().getUserLanguage()).build();
        DisneyOffer disneyOffer = disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V1").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccount = disneyAccountApi.get().createAccount(request);

        disneyAccountApi.get().addFlex(testAccount);
        disneyAccount.set(testAccount);

        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase forgotPasswordPage = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        IOSUtils utils = new IOSUtils();

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
        new IOSUtils().handleSystemAlert(IOSUtils.AlertButtonCommand.ACCEPT, 10);
        pause(3);
        loginPage.submitEmail(disneyAccount.get().getEmail());
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
        utils.dismissKeyboardForPhone();
        forgotPasswordPage.clickResend();
        pause(3);
        getScreenshots("ForgotPasswordPageResend");
        forgotPasswordPage.clickAlertDismissBtn();
        utils.dismissKeyboardForPhone();
        forgotPasswordPage.getBackArrow().click();
        passwordPage.submitPasswordForLogin("badpassword");
        pause(3);
        getScreenshots("PasswordPageBadPassword");
        setAppToHomeScreen(disneyAccount.get());
        pause(3);
        getScreenshots("TravellingMessage");

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S2 Watchlist and App Settings", groups = {"Subscriber - UI", "Subscriber - UI - S2"})
    public void watchlistAndAppSettings(String TUID) {
        setup();
        setPathToZip("SubscriberUI_2_watchlist");
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoseWatchingPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyAccount testAccount = disneyAccount.get();

        String cellOption = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.CELLULAR_DATA_USAGE.getText());
        String wifiOption = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.WIFI_DATA_USAGE.getText());
        String videoQuality = languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.VIDEO_QUALITY_TITLE.getText());

        disneyAccountApi.get().addProfile(testAccount, "SecondaryTestProfile",
                disneyAccount.get().getProfileLang(), null, false);

        welcomePage.clickLogInButton();
        loginPage.submitEmail(testAccount.getEmail());
        passwordPage.submitPasswordForLogin(testAccount.getUserPass());
        pause(SWIPE_COUNTER);
        getScreenshots("WhoseWatchingPage");

        whoseWatchingPage.clickProfile("Test");
        handleAlert();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        pause(3);
        getScreenshots("ProfilePage");

        moreMenuPage.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.WATCHLIST.getMenuOption()).click();
        pause(3);
        getScreenshots("EmptyWatchlist");

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS.getMenuOption()).click();
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

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S3 Profile menu: Account & Help", groups = {"Subscriber - UI", "Subscriber - UI - S3"})
    public void AccountsAndHelp(String TUID) {
        setup();
        setPathToZip("SubscriberUI_3_accounts");
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase forgotPasswordPage = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusAccountIOSPageBase accountPage = initPage(DisneyPlusAccountIOSPageBase.class);
        DisneyPlusOneTimePasscodeIOSPageBase disneyPlusOneTimePasscodeIOSPageBase = initPage(DisneyPlusOneTimePasscodeIOSPageBase.class);
        DisneyPlusChangeEmailIOSPageBase changeEmailPage = initPage(DisneyPlusChangeEmailIOSPageBase.class);
        VerifyEmail verifyEmail = new VerifyEmail();

        String locale = languageUtils.get().getLocale();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).language(languageUtils.get().getUserLanguage()).build();
        DisneyOffer disneyOffer = disneyAccountApi.get().lookupOfferToUse(locale, "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V1").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccount = disneyAccountApi.get().createAccountForOTP(request);
        disneyAccount.set(testAccount);

        welcomePage.clickLogInButton();
        loginPage.submitEmail(testAccount.getEmail());
        passwordPage.typePassword(testAccount.getUserPass());
        iosUtils.get().dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();


        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickMenuOptionByIndex(DisneyPlusMoreMenuIOSPageBase.MoreMenu.ACCOUNT);
        pause(3);
        int accountPageShotCount = 1;
        getScreenshots("AccountPagePart_" + accountPageShotCount);

        while (!moreMenuPage.getDeleteAccountButton().isElementPresent()) {
            accountPageShotCount += 1;
            swipeInContainer(null, IMobileUtils.Direction.UP, 500);
            pause(2);
            getScreenshotsNoCountUpdate("AccountPagePart" + accountPageShotCount);
        }

        while (!accountPage.isChangeLinkPresent(testAccount.getEmail())) {
            swipeInContainer(null, Direction.DOWN, 500);
        }

        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, SWIPE_COUNTER, "Change link was not present")
                .until(it -> accountPage.isChangeLinkPresent(testAccount.getEmail()));
        Date startTime = verifyEmail.getStartTime();
        accountPage.clickChangeLink(testAccount.getEmail());
        pause(2);
        getScreenshots("ChangeEmailPage");
        forgotPasswordPage.clickPrimaryButton();

        iosUtils.get().dismissKeyboardForPhone();
        pause(3);
        getScreenshots("ChangeEmailPageBlankCode");
        iosUtils.get().dismissKeyboardForPhone();
        forgotPasswordPage.clickResend();
        pause(3);
        getScreenshots("ChangeEmailPageResend");
        forgotPasswordPage.clickAlertDismissBtn();
        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValueDismissKeys(verifyEmail.getDisneyOTP(testAccount.getEmail(), EmailApi.getOtpAccountPassword(), startTime));
        pause(3);
        iosUtils.get().dismissKeyboardForPhone();
        getScreenshots("ChangeEmailPageAfterOtp");

        changeEmailPage.clickLogoutAllDevices();
        pause(3);
        getScreenshots("EmailLogOutOfAllDevices");

        changeEmailPage.clickSaveBtn();
        iosUtils.get().dismissKeyboardForPhone();
        pause(3);
        getScreenshots("EmailEmptySave");

        changeEmailPage.submitNewEmailAddress("a");
        iosUtils.get().dismissKeyboardForPhone();
        pause(3);
        getScreenshots("EmailBadEmailSave");

        iosUtils.get().dismissKeyboardForPhone();
        changeEmailPage.clickCancelBtn();

        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, SWIPE_COUNTER, "Change link was not present")
                .until(it -> {
                    if (!debugMode) {
                        return accountPage.isChangeLinkPresent(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.HIDDEN_PASSWORD.getText()));
                    } else {
                        return accountPage.isChangeLinkPresent(DictionaryKeys.HIDDEN_PASSWORD.getText());
                    }
                });
        startTime = verifyEmail.getStartTime();
        if (!debugMode) {
            accountPage.clickChangeLink(languageUtils.get().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.HIDDEN_PASSWORD.getText()));
        } else {
            accountPage.clickChangeLink(DictionaryKeys.HIDDEN_PASSWORD.getText());
        }

        disneyPlusOneTimePasscodeIOSPageBase.enterOtpValueDismissKeys(verifyEmail.getDisneyOTP(testAccount.getEmail(), EmailApi.getOtpAccountPassword(), startTime));
        pause(3);
        iosUtils.get().dismissKeyboardForPhone();
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
        iosUtils.get().dismissKeyboardForPhone();
        pause(3);
        getScreenshots("InvalidPasswordError");

        changePasswordPage.clickCancelBtn();
        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 2, "Log out of all devices is not present")
                .until(it -> accountPage.isLogOutOfAllDevicesLinkPresent());
        accountPage.clickLogOutOfAllDevices();
        pause(3);
        getScreenshots("LogoutAllDevicesPage");

        accountPage.clickPrimaryButton();
        iosUtils.get().dismissKeyboardForPhone();
        pause(3);
        getScreenshots("LogoutAllDevicesEmptyPassword");

        passwordPage.typePassword("a");
        accountPage.clickPrimaryButton();
        iosUtils.get().dismissKeyboardForPhone();
        pause(3);
        getScreenshots("LogoutAllDevicesWrongPassword");

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S4 Legal", groups = {"Subscriber - UI", "Subscriber - UI - S4"})
    public void legal(String TUID) {
        setup();
        setPathToZip("SubscriberUI_4_legal");
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyplusLegalIOSPageBase legalPage = initPage(DisneyplusLegalIOSPageBase.class);
        IOSUtils utils = new IOSUtils();
        DisneyAccount testAccount = disneyAccount.get();

        welcomePage.clickLogInButton();
        loginPage.submitEmail(testAccount.getEmail());
        passwordPage.typePassword(testAccount.getUserPass());

        utils.dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();

        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.LEGAL.getMenuOption()).click();
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

        moreMenuPage.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.HELP.getMenuOption()).click();
        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, SWIPE_COUNTER, "Help page did not open.")
                .until(it -> moreMenuPage.isHelpWebviewOpen());
        pause(10);
        getScreenshots("HelpWebview");

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S5 Profile menu: Edit profiles", groups = {"Subscriber - UI", "Subscriber - UI - S5"})
    public void profileMenu(String TUID) {
        setup();
        setPathToZip("SubscriberUI_5_profileMenu");
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusChooseAvatarIOSPageBase avatarPage = initPage(DisneyPlusChooseAvatarIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        IOSUtils utils = iosUtils.get();
        DisneyAccount testAccount = disneyAccount.get();

        welcomePage.clickLogInButton();
        loginPage.fillOutEmailField(testAccount.getEmail());
        loginPage.clickPrimaryButton();
        passwordPage.typePassword(testAccount.getUserPass());

        utils.dismissKeyboardForPhone();
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
        utils.hideKeyboard();
        pause(2);
        getScreenshots("EmptyProfileName");

        avatarPage.typeProfileName(DEFAULT_PROFILE);
        avatarPage.clickSaveBtn();
        utils.hideKeyboard();
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
        createProfile("Test_b");
        avatarPage.clickSecondaryButton();

        moreMenuPage.getProfileAvatar("Test_b").click();
        pause(2);
        getScreenshots("ContentRatingPage");

        moreMenuPage.clickPrimaryButton();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);

        for (int i = 0; i < 4; i++) {
            disneyAccountApi.get().addProfile(testAccount, "Test_" + i, language,
                    null, false);
        }
        restart();

        welcomePage.clickLogInButton();
        loginPage.fillOutEmailField(testAccount.getEmail());
        loginPage.clickPrimaryButton();
        passwordPage.typePassword(testAccount.getUserPass());
        utils.dismissKeyboardForPhone();
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
            swipeInContainer(null, IMobileUtils.Direction.UP, 500);
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
            swipeInContainer(null, IMobileUtils.Direction.UP, 500);
        }

        editProfilePage.getDeleteProfileButton().click();
        pause(2);
        getScreenshots("DeleteProfileConfirmation");

        editProfilePage.clickConfirmDeleteButton();

        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 3, "Who is Watching Page never appeared.")
                .until(it -> whoPage.isHeaderTextPresent());

        swipeInContainer(null, IMobileUtils.Direction.UP, 500);
        pause(2);
        getScreenshots("AfterDeleteWhoIsWatchingPage");

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S6 PCON", groups = {"Subscriber - UI", "Subscriber - UI - S6"})
    public void PCON(String TUID) {
        setup();
        setPathToZip("SubscriberUI_6_PCON");
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusEditProfileIOSPageBase editProfilePage = initPage(DisneyPlusEditProfileIOSPageBase.class);
        DisneyPlusContentRatingIOSPageBase contentRatingPage = initPage(DisneyPlusContentRatingIOSPageBase.class);
        DisneyPlusWhoseWatchingIOSPageBase whoPage = initPage(DisneyPlusWhoseWatchingIOSPageBase.class);
        DisneyPlusPinIOSPageBase pinPage = initPage(DisneyPlusPinIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        IOSUtils utils = iosUtils.get();
        DisneyAccount testAccount = disneyAccount.get();
        DisneyProfile profile = disneyAccount.get().getProfile(DEFAULT_PROFILE);
        String ratingSystem = profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystem();

        //create a second account for a later step
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(languageUtils.get().getLocale()).language(languageUtils.get().getUserLanguage()).build();
        DisneyOffer disneyOffer = disneyAccountApi.get().lookupOfferToUse(languageUtils.get().getLocale(), "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V2").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccountTwoProfiles = disneyAccountApi.get().createAccount(request);
        disneyAccountApi.get().addProfile(testAccountTwoProfiles, SECONDARY_PROFILE, "2018-01-01", languageUtils.get().getUserLanguage(),
                null, false, true);
        //set account to the lowest rating for a step later on
        disneyAccountApi.get().editContentRatingProfileSetting(testAccount, disneyAccountApi.get().getDisneyProfiles(testAccountTwoProfiles).get(1).getProfileId(), ratingSystem,
                profile.getAttributes().getParentalControls().getMaturityRating().getRatingSystemValues().get(0));

        loginDismiss(testAccount);

        //S6.1
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.MORE_MENU);
        moreMenuPage.clickEditProfilesBtn();
        pause(3);
        editProfilePage.clickEditModeProfile(DEFAULT_PROFILE);

        utils.swipePageTillElementTappable(editProfilePage.getPinSettingsCell(), 3, null, Direction.UP, 500);

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
            String localizedRating = languageUtils.get().getLocalizedRating(ratingSystem, rating);
            ExtendedWebElement currentRating = contentRatingPage
                    .getDynamicAccessibilityId(localizedRating);

            utils.swipePageTillElementTappable(currentRating, 3, null, Direction.UP, 1000);

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
        moreMenuPage.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT.getMenuOption()).click();
        welcomePage.clickLogInButton();
        loginPage.fillOutEmailField(testAccountTwoProfiles.getEmail());
        loginPage.clickPrimaryButton();
        passwordPage.typePassword(testAccountTwoProfiles.getUserPass());
        utils.dismissKeyboardForPhone();
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

        utils.swipePageTillElementTappable(editProfilePage.getKidProofExitLabel(), 3, null, Direction.UP, 1000);
        editProfilePage.getKidProofExitLabel().click();
        pause(2);
        getScreenshots("KidExitProofError");

        //S6.14
        editProfilePage.getKidsProfileToggleSwitch().click();

        //S6.15
        utils.swipePageTillElementTappable(editProfilePage.getGroupWatchAndShareplay(), 3, null, Direction.DOWN, 1000);
        editProfilePage.getGroupWatchAndShareplay().click();
        pause(2);
        getScreenshots("GroupWatchKidsModeError");

        //S6.16
        editProfilePage.clickDoneBtn();
        pause(3);
        initPage(DisneyPlusApplePageBase.class).getDynamicAccessibilityId(DisneyPlusApplePageBase.FooterTabs.MORE_MENU.getLocator()).click();
        whoPage.clickEditProfile();
        editProfilePage.clickEditModeProfile(SECONDARY_PROFILE);

        utils.swipePageTillElementTappable(editProfilePage.getPinSettingsCell(), 3, null, Direction.UP, 1000);
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
        pause(SWIPE_COUNTER);
        pinPage.getPinCancelButton().click();
        pause(SWIPE_COUNTER);
        whoPage.clickEditProfile();
        editProfilePage.clickEditModeProfile(SECONDARY_PROFILE);

        utils.swipePageTillElementTappable(editProfilePage.getKidProofExitToggleSwitch(), 3, null, Direction.UP, 1000);
        editProfilePage.getKidProofExitToggleSwitch().click();
        passwordPage.typePassword(testAccount.getUserPass());
        passwordPage.clickPrimaryButton();
        pause(SWIPE_COUNTER);
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
        utils.dismissKeyboardForPhone();
        pause(2);
        getScreenshots("ExitKidsProfileWrongCodeError");

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S7 Bottom navigation - Downloads and Search", groups = {"Subscriber - UI", "Subscriber - UI - S7"})
    public void downloadsAndSearch(String TUID) {
        setup();
        setPathToZip("SubscriberUI_7_downloads");
        DisneyPlusWelcomeScreenIOSPageBase welcomePage = initPage(DisneyPlusWelcomeScreenIOSPageBase.class);
        DisneyPlusLoginIOSPageBase loginPage = initPage(DisneyPlusLoginIOSPageBase.class);
        DisneyPlusPasswordIOSPageBase passwordPage = initPage(DisneyPlusPasswordIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusDownloadsIOSPageBase downloadsPage = initPage(DisneyPlusDownloadsIOSPageBase.class);
        DisneyPlusMoreMenuIOSPageBase moreMenuPage = initPage(DisneyPlusMoreMenuIOSPageBase.class);
        DisneyPlusAppSettingsIOSPageBase appSettingsPage = initPage(DisneyPlusAppSettingsIOSPageBase.class);
        IOSUtils utils = iosUtils.get();
        DisneyAccount testAccount = disneyAccount.get();

        welcomePage.clickLogInButton();
        loginPage.fillOutEmailField(testAccount.getEmail());
        loginPage.clickPrimaryButton();
        passwordPage.typePassword(testAccount.getUserPass());
        utils.dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();

        //S7.1
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        pause(2);
        getScreenshots("EmptyDownloadsPage");

        //S7.2
        String movieTitle = searchApi.get().getMovie("aRbVJUb2h2Rf", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()).getVideoTitle();
        navigateToTab((DisneyPlusApplePageBase.FooterTabs.SEARCH));
        searchPage.searchForMedia(movieTitle);
        List<ExtendedWebElement> movies = searchPage.getDisplayedTitles();
        movies.get(0).click();
        pause(2);
        getScreenshots("MovieLandingPage");

        utils.swipePageTillElementTappable(detailsPage.getTabBar(), 3, null, Direction.UP, 1000);

        detailsPage.clickDetailsTab();
        pause(2);
        getScreenshots("MovieDetailsTab");

        utils.swipePageTillElementTappable(detailsPage.getActors(), 3, null, Direction.UP, 1000);
        pause(2);
        getScreenshots("MovieMiscellaneousdetails");

        //S7.3
        utils.swipePageTillElementTappable(detailsPage.getPlayButton(), 3, null, Direction.DOWN, 1000);

        detailsPage.addToWatchlist();
        detailsPage.startDownload();
        pause(2);

        //S7.6
        navigateToTab((DisneyPlusApplePageBase.FooterTabs.DOWNLOADS));
        pause(2);
        getScreenshots("DownloadsPage");

        //S7.7
        downloadsPage
                .getDynamicRowButtonLabel(
                        getDictionary()
                                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOAD_STOP_IOS.getText()), 1).click();

        pause(2);
        getScreenshots("MovieDownload");
        detailsPage.clickAlertConfirm();

        //S7.8
        downloadsPage
                .getDynamicRowButtonLabel(
                        getDictionary()
                                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOAD_PAUSED.getText()), 1).click();
        pause(2);
        getScreenshots("MoviePausedDownload");
        detailsPage.clickAlertConfirm();

        navigateToTab((DisneyPlusApplePageBase.FooterTabs.SEARCH));
        searchPage.searchForMedia("The Simpsons");
        List<ExtendedWebElement> series = searchPage.getDisplayedTitles();
        series.get(0).click();
        pause(SWIPE_COUNTER);
        getScreenshots("SeriesLandingPage");

        utils.swipePageTillElementTappable(detailsPage.getEpisodesTab(), 3, null, Direction.UP, 1000);
        pause(2);
        getScreenshotsNoCountUpdate("SeriesEpisodes");

        detailsPage.clickDetailsTab();
        pause(2);
        getScreenshotsNoCountUpdate("SeriesDetailsTab");

        utils.swipePageTillElementTappable(detailsPage.getInfoView(), 3, null, Direction.UP, 1000);
        detailsPage.getInfoView().click();
        pause(2);
        getScreenshotsNoCountUpdate("SeriesInfoView");

        utils.swipePageTillElementTappable(detailsPage.getActors(), 3, null, Direction.UP, 1000);
        pause(2);
        getScreenshotsNoCountUpdate("SeriesMiscellaneousDetails");

        navigateToTab((DisneyPlusApplePageBase.FooterTabs.SEARCH));
        series = searchPage.getDisplayedTitles();
        series.get(0).click();
        pause(SWIPE_COUNTER);

        //S7.4
        utils.swipePageTillElementTappable(detailsPage.getSeasonSelectorButton(), 3, null, Direction.UP, 1000);
        detailsPage.getSeasonSelectorButton().click();
        pause(2);
        getScreenshots("SeasonSelector");

        //S7.5
        detailsPage.getItemPickerClose().click();
        pause(1);
        detailsPage.downloadAllOfSeason();
        pause(1);
        getScreenshots("DownloadSeasonLessThan20");

        detailsPage.clickAlertDismissBtn();

        detailsPage.getSeasonSelectorButton().click();
        String seasonsButton = getDictionary().replaceValuePlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), "3");
        detailsPage.getDynamicAccessibilityId(seasonsButton).click();
        detailsPage.downloadAllOfSeason();
        pause(2);
        getScreenshotsNoCountUpdate("DownloadSeasonMoreThan20");
        detailsPage.clickAlertConfirm();

        //download a couple of seasons for download in progress button
        detailsPage.getSeasonSelectorButton().click();
        seasonsButton = getDictionary().replaceValuePlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), "4");
        detailsPage.getDynamicAccessibilityId(seasonsButton).click();
        detailsPage.downloadAllOfSeason();
        detailsPage.clickAlertConfirm();

        detailsPage.getSeasonSelectorButton().click();
        seasonsButton = getDictionary().replaceValuePlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), "5");
        detailsPage.getDynamicAccessibilityId(seasonsButton).click();
        detailsPage.downloadAllOfSeason();
        detailsPage.clickAlertConfirm();

        detailsPage.getSeasonSelectorButton().click();
        seasonsButton = getDictionary().replaceValuePlaceholders(getDictionary().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.BTN_SEASON_NUMBER.getText()), "6");
        detailsPage.getDynamicAccessibilityId(seasonsButton).click();
        detailsPage.downloadAllOfSeason();
        detailsPage.clickAlertConfirm();

        String seriesTitle = searchApi.get().getSeries("3ZoBZ52QHb4x", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()).getSeriesTitle();

        navigateToTab((DisneyPlusApplePageBase.FooterTabs.DOWNLOADS));
        pause(2);
        getScreenshotsNoCountUpdate("DownloadsPageWithSeries");

        //S7.9
        downloadsPage
                .getDynamicIosClassChainElementTypeImage(
                        getDictionary().replaceValuePlaceholders(
                                getDictionary().getDictionaryItem(
                                        DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY,
                                        DictionaryKeys.DOWNLOADS_DISCLOSURE_ACCESSIBILITY_TITLE.getText()),
                                seriesTitle)).click();
        pause(2);
        getScreenshots("SeriesDownloadPage");

        //S7.10
        ExtendedWebElement lastEpisodeButton = downloadsPage
                .getDynamicRowButtonLabel(
                        getDictionary()
                                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.DOWNLOAD_STOP_IOS.getText()), 7);
        utils.swipePageTillElementTappable(lastEpisodeButton, SWIPE_COUNTER, null, Direction.UP, 500);
        lastEpisodeButton.click();
        pause(2);
        getScreenshots("DownloadIsQueued");
        detailsPage.clickAlertDismissBtn();

        //S7.11
        downloadsPage.getEditButton().click();
        pause(2);
        getScreenshots("EditMode");

        //S7.12
        downloadsPage
                .getDynamicRowButtonLabel(
                        getDictionary()
                                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.CHECKBOX_UNCHECKED.getText()), 3).click();

        pause(2);
        getScreenshots("SingleCheckbox");

        downloadsPage
                .getDynamicRowButtonLabel(
                        getDictionary()
                                .getDictionaryItem(DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.CHECKBOX_UNCHECKED.getText()), 4).click();

        pause(2);
        getScreenshotsNoCountUpdate("MultipleCheckbox");

        //S7.13
        downloadsPage.getSelectAllButton().click();
        pause(2);
        getScreenshots("SelectAll");
        downloadsPage.getCancelButton().click();

        //S7.14
        navigateToTab((DisneyPlusApplePageBase.FooterTabs.MORE_MENU));
        moreMenuPage.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS.getMenuOption()).click();

        utils.swipePageTillElementTappable(appSettingsPage.getDeleteAllDownloadsButton(), 3, null, Direction.UP, 1000);
        appSettingsPage.getDeleteAllDownloadsButton().click();
        pause(2);
        getScreenshots("DeleteAllMultipleDownloads");
        downloadsPage.getSystemAlertDestructiveButton().click();

        //Have to restart and relog here because when appium tells D+ to "restart"
        // IOS re-installs D+.
        restart();
        welcomePage.clickLogInButton();
        loginPage.fillOutEmailField(testAccount.getEmail());
        loginPage.clickPrimaryButton();
        passwordPage.typePassword(testAccount.getUserPass());

        utils.dismissKeyboardForPhone();
        passwordPage.clickPrimaryButton();

        movieTitle = searchApi.get().getMovie("5MpPFhS8FTXh", languageUtils.get().getLocale(), languageUtils.get().getUserLanguage()).getVideoTitle();
        navigateToTab((DisneyPlusApplePageBase.FooterTabs.SEARCH));
        searchPage.searchForMedia(movieTitle);
        movies = searchPage.getDisplayedTitles();
        movies.get(0).click();
        detailsPage.startDownload();
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.DOWNLOADS);
        downloadsPage.waitForDownloadToComplete();

        navigateToTab((DisneyPlusApplePageBase.FooterTabs.MORE_MENU));
        moreMenuPage.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.APP_SETTINGS.getMenuOption()).click();

        utils.swipePageTillElementTappable(appSettingsPage.getDeleteAllDownloadsButton(), 3, null, Direction.UP, 1000);
        appSettingsPage.getDeleteAllDownloadsButton().click();
        pause(1);
        getScreenshotsNoCountUpdate("DeleteAllSingleDownload");
        downloadsPage.clickDefaultAlertBtn();

        //S7.15
        navigateToTab((DisneyPlusApplePageBase.FooterTabs.MORE_MENU));
        moreMenuPage.getDynamicCellByLabel(
                DisneyPlusMoreMenuIOSPageBase.MoreMenu.LOG_OUT.getMenuOption()).click();
        getScreenshots("LogOut");


        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S8 Bottom navigation - Search, Movies, Originals and Series", groups = {"Subscriber - UI", "Subscriber - UI - S8"})
    public void searchMoviesAndSeries(String TUID) {
        setup();
        setPathToZip("SubscriberUI_8_search");
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusOriginalsIOSPageBase originalsPage = initPage(DisneyPlusOriginalsIOSPageBase.class);
        DisneyPlusMediaCollectionIOSPageBase mediaCollectionPage = initPage(DisneyPlusMediaCollectionIOSPageBase.class);
        DisneyAccount testAccount = disneyAccount.get();

        loginDismiss(testAccount);

        //S8.1
        navigateToTab(DisneyPlusApplePageBase.FooterTabs.SEARCH);
        searchPage.getOriginalsTile().isElementPresent();
        getScreenshots("SearchPage");

        //S8.2
        searchPage.searchForMedia("jjjjjjjlkshjdai8or7325uihad87t87t86686&^%&#^%!^@%&#^!@%($^%#@!&SUAS&^$#&124124345^%!$#&^#%sadas2451234124!&$^%!&@$sasda&^#535323$%*#&^$%&*");
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
                .region(languageUtils.get().getLocale())
                .audience("false")
                .language(languageUtils.get().getUserLanguage())
                .slug(DisneyStandardCollection.ORIGINALS.getSlug())
                .contentClass(DisneyStandardCollection.ORIGINALS.getContentClass())
                .account(testAccount)
                .build();

        ContentCollection contentCollection = searchApi.get().getCollection(collectionRequest);
        List<DisneyCollectionSet> setInfo = contentCollection.getCollectionSetsInfo();

        searchPage.clickOriginalsTab();
        originalsPage.isOriginalPageLoadPresent();
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
                .region(languageUtils.get().getLocale())
                .audience("false")
                .language(languageUtils.get().getUserLanguage())
                .slug(DisneyStandardCollection.MOVIES.getSlug())
                .contentClass(DisneyStandardCollection.MOVIES.getContentClass())
                .account(testAccount)
                .build();

        contentCollection = searchApi.get().getCollection(collectionRequest);
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
                .region(languageUtils.get().getLocale())
                .audience("false")
                .language(languageUtils.get().getUserLanguage())
                .slug(DisneyStandardCollection.SERIES.getSlug())
                .contentClass(DisneyStandardCollection.SERIES.getContentClass())
                .account(testAccount)
                .build();

        contentCollection = searchApi.get().getCollection(collectionRequest);
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

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S10 - Welch Onboarding", groups = {"Subscriber - UI", "Subscriber - UI - S10"})
    public void welchOnboarding(String TUID) {
        setup();
        setPathToZip("SubscriberUI_10_welch");
        String locale = languageUtils.get().getLocale();
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().country(locale).language(languageUtils.get().getUserLanguage()).isStarOnboarded(false).build();
        DisneyOffer disneyOffer = disneyAccountApi.get().lookupOfferToUse(locale, "Yearly");
        DisneyEntitlement entitlement = DisneyEntitlement.builder().offer(disneyOffer).subVersion("V1").build();
        request.addEntitlement(entitlement);
        DisneyAccount testAccount = disneyAccountApi.get().createAccount(request);
        disneyAccountApi.get().addFlex(testAccount);
        disneyAccount.set(testAccount);

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

        if (languageUtils.get().getLocale().equals("US")) {
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

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

    @Test(dataProvider = "tuidGenerator", description = "iOS S13 Editorial Contents", groups = {"Subscriber - UI", "Subscriber - UI - S13"})
    public void editorialContents(String TUID) {
        setup();
        setPathToZip("SubscriberUI_13_editorial_contents");
        DisneyPlusHomeIOSPageBase homePage = initPage(DisneyPlusHomeIOSPageBase.class);
        DisneyPlusSearchIOSPageBase searchPage = initPage(DisneyPlusSearchIOSPageBase.class);
        DisneyPlusDetailsIOSPageBase detailsPage = initPage(DisneyPlusDetailsIOSPageBase.class);
        DisneyPlusVideoPlayerIOSPageBase videoPlayerPage = initPage(DisneyPlusVideoPlayerIOSPageBase.class);
        DisneyPlusBrandIOSPageBase brandPage = initPage(DisneyPlusBrandIOSPageBase.class);
        DisneyAccount testAccount = disneyAccount.get();
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
        getScreenshots("HomeLandingPage");

        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, IMobileUtils.Direction.UP, 500);
            getScreenshotsNoCountUpdate("Home" + i);
        }

        //S13.2
        //Swipe to a brand page, swipe a few times and take screenshots
        swipe(homePage.getDisneyTile(), Direction.DOWN);
        homePage.clickDisneyTile();
        brandPage.isOpened();
        getScreenshots("DisneyLandingPage");
        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, IMobileUtils.Direction.UP, 500);
            getScreenshotsNoCountUpdate("DisneyBrand" + i);
        }

        swipe(homePage.getBackArrow(), Direction.DOWN);
        homePage.getBackArrow().click();
        homePage.clickPixarTile();
        brandPage.isOpened();
        getScreenshots("PixarLandingPage");
        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, IMobileUtils.Direction.UP, 500);
            getScreenshotsNoCountUpdate("PixarBrand" + i);
        }

        swipe(homePage.getBackArrow(), Direction.DOWN);
        homePage.getBackArrow().click();
        homePage.clickMarvelTile();
        brandPage.isOpened();
        getScreenshots("MarvelLandingPage");
        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, IMobileUtils.Direction.UP, 500);
            getScreenshotsNoCountUpdate("MarvelBrand" + i);
        }

        swipe(homePage.getBackArrow(), Direction.DOWN);
        homePage.getBackArrow().click();
        homePage.clickStarWarsTile();
        brandPage.isOpened();
        getScreenshots("StarWarsLandingPage");
        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, IMobileUtils.Direction.UP, 500);
            getScreenshotsNoCountUpdate("StarWarsBrand" + i);
        }

        swipe(homePage.getBackArrow(), Direction.DOWN);
        homePage.getBackArrow().click();
        homePage.clickNatGeoTile();
        brandPage.isOpened();
        getScreenshots("NatGeoLandingPage");
        for (int i = 0; i < SWIPE_COUNTER; i++) {
            swipeInContainer(null, IMobileUtils.Direction.UP, 500);
            getScreenshotsNoCountUpdate("NatGeoBrand" + i);
        }

        swipe(homePage.getBackArrow(), Direction.DOWN);
        homePage.getBackArrow().click();

        if(homePage.isStarTilePresent()) {
            homePage.clickStarTile();
            brandPage.isOpened();

            getScreenshots("StarLandingPage");
            for (int i = 0; i < SWIPE_COUNTER; i++) {
                swipeInContainer(null, IMobileUtils.Direction.UP, 500);
                getScreenshotsNoCountUpdate("StarBrand" + i);
            }

        }

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseDirectory.get(), pathToZip.get());
    }

}
