package com.disney.qa.tests.disney.android.tv.localization;

import com.disney.charles.CharlesProxy;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.responses.content.ContentCollection;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.jarvis.JarvisParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.common.jarvis.android.JarvisAndroidBase;
import com.disney.qa.common.jarvis.android.JarvisAndroidTV;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.common.web.VerifyEmail;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.disney.qa.disney.android.pages.tv.*;
import com.disney.qa.disney.android.pages.tv.globalnav.*;
import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVDetailsPageBase;
import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVForgotPasswordPageBase;
import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVLegalPageBase;
import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVWelchPageBase;
import com.disney.qa.tests.BaseAndroidTVTest;
import com.disney.util.ZipUtils;
import com.qaprosoft.appcenter.AppCenterManager;
import com.qaprosoft.carina.core.foundation.listeners.CarinaListener;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.qaprosoft.carina.core.foundation.webdriver.TestPhase;
import com.zebrunner.agent.core.annotation.Maintainer;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage.HomePageItems.CONTINUE_WATCHING_SHELF_TITLE;

@Maintainer("fkhan")
public class DisneyPlusAndroidTVLocalizationTests extends BaseAndroidTVTest {

    private final String baseFile = "Screenshots/";
    ThreadLocal<String> regionLanguage = new ThreadLocal<>();
    private final ThreadLocal<CharlesProxy> charlesProxy = new ThreadLocal<>();
    private final ThreadLocal<Boolean> isAmazon = new ThreadLocal<>();

    ThreadLocal<String> region = new ThreadLocal<>();

    private static final String SIMPSONS_SERIES_ID = "6fffa028-8c70-43f1-9e7a-364e8c4562c4";
    private static final String MANDALORIAN_SERIES_ID = "ac084ba7-9a40-49e2-b3b7-ca1a3eeeb54b";
    private static final String SHANG_CHI_ID = "9757626b-fe7a-47f1-91ac-15b1c7543f19";

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws JSONException {
        CarinaListener.disableDriversCleanup();
        R.CONFIG.put("forcibly_disable_driver_quit", "true");
        DisneyCountryData countryData = new DisneyCountryData();
        regionLanguage.set(R.CONFIG.get("custom_string2"));
        region.set(R.CONFIG.get("custom_string3"));
        String country = (String) countryData.searchAndReturnCountryData(region.get(), "code", "country");
        JarvisAndroidBase.DisneyEnvironments jarvisEnv = JarvisAndroidTV.getJarvisDisneyEnvironment(R.CONFIG.get("capabilities.custom_env"));
        JarvisAndroidTV jarvisAndroidTV = new JarvisAndroidTV(getDriver());
        isAmazon.set(AndroidTVUtils.isAmazon());
        if (!AndroidService.getInstance().executeAdbCommand("shell pm list packages " + JarvisParameters.getAndroidJarvisPackage()).isEmpty())
            uninstallApp(JarvisParameters.getAndroidJarvisPackage());
        new AndroidUtilsExtended().installApp(AppCenterManager.getInstance().getDownloadUrl("Disney-Jarvis-1", "android", "jarvis-dev-release", "latest"));
        if (!isAmazon.get()) {
            setProxyToAgent(getDevice().getProxyPort());
            charlesProxy.set(new CharlesProxy(Integer.parseInt(currentDevice.get().getProxyPort()), null));
            charlesProxy.get().startProxy(country, getDeviceIp());
        }
        jarvisAndroidTV.setDisneyEnvironment(jarvisEnv);
        String globalizationVersion = R.CONFIG.get("custom_string4");

        if (Boolean.parseBoolean(R.CONFIG.get("custom_string"))) {
            JarvisAndroidBase.Dictionaries.UNPIN_DICTIONARIES.setOverrideValue(true);
        }
        if (!globalizationVersion.isEmpty() && !globalizationVersion.equalsIgnoreCase("null")) {
            JarvisAndroidBase.Localization.GLOBALIZATION_API_VER.setOverrideValue(globalizationVersion);
        }
        if(Boolean.parseBoolean(R.CONFIG.get("custom_string5"))) {
            JarvisAndroidBase.Dictionaries.ENABLE_DEBUG_DICTIONARY.setOverrideValue(true);
        }
        jarvisAndroidTV.activateConfigOverrides();
        if (isAmazon.get()) {
            R.CONFIG.put("browsermob_proxy", "true");
            setSystemLanguage(regionLanguage.get(), region.get());
            restartDriver(true);
            initiateProxy(country);
        } else {
            setSystemLanguage(regionLanguage.get(), region.get());
            restartDriver(true);
        }
        regionLanguage.set(handleAppLanguage(regionLanguage.get()));
        pause(10);
        clearAppCache();
    }

    @Test(groups = {"Login","SubUI"})
    public void loginScreenEmailScreenshots() {
        AtomicInteger count = new AtomicInteger();
        String pathToZip = String.format("LoginScreenshots_%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyContentApiChecker apiProvider = new DisneyContentApiChecker();
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        disneyPlusAndroidTVWelcomePage.isOpened();
        UniversalUtils.storeAndUploadSS("WelcomeScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.isOpened();
        UniversalUtils.storeAndUploadSS("EnterEmailScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
        disneyPlusAndroidTVLoginPage.isOpened();
        UniversalUtils.storeAndUploadSS("LoginPageEmptyEmailError", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterEmail(apiProvider.getUniqueUserEmail());
        androidTVUtils.hideKeyboardIfPresent();
        disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
        disneyPlusAndroidTVLoginPage.isUnknownUserPageOpened();
        UniversalUtils.storeAndUploadSS("UnknownUserScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        disneyPlusAndroidTVLoginPage.isOpened();
        UniversalUtils.storeAndUploadSS("LoginScreenWithError", count.addAndGet(1), baseFile, getCastedDriver());
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseFile, pathToZip);
    }

    @Test(groups = {"LoginPasswordOTP","SubUI"})
    public void loginPasswordAndOTPScreenshots() throws URISyntaxException {
        AtomicInteger count = new AtomicInteger();
        DisneyAccountApi disneyAccountApi = new DisneyAccountApi("android", DisneyParameters.getEnv(), "disney");
        com.disney.qa.api.pojos.DisneyAccount user = disneyAccountApi.createAccountForOTP( region.get(), regionLanguage.get());
        disneyAccountApi.entitleAccount(user, com.disney.qa.api.utils.DisneySkuParameters.DISNEY_D2C_REGION_FREE, "V1");
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        VerifyEmail verifyEmail = new VerifyEmail();
        DisneyPlusAndroidTVForgotPasswordPageBase disneyPlusAndroidTVForgotPasswordPageBase = new DisneyPlusAndroidTVForgotPasswordPageBase(getDriver());
        String pathToZip = String.format("LoginPasswordAndOTP_%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());

        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.isOpened(), "Welcome screen did not launch");
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.enterEmail(user.getEmail());
        disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
        disneyPlusAndroidTVLoginPage.isEnterPasswordPageOpen();
        UniversalUtils.storeAndUploadSS("EnterPasswordScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.clickLoginBtn();
        disneyPlusAndroidTVLoginPage.isEnterPasswordPageOpen();
        UniversalUtils.storeAndUploadSS("PasswordScreenEmptyPassError", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("1234");
        disneyPlusAndroidTVLoginPage.clickLoginBtn();
        disneyPlusAndroidTVLoginPage.isEnterPasswordPageOpen();
        UniversalUtils.storeAndUploadSS("PasswordScreenIncorrectPasswordError", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.clickForgotPassword();
        Date startTime = verifyEmail.getStartTime();
        disneyPlusAndroidTVForgotPasswordPageBase.isOTPScreenOpened();
        disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
        disneyPlusAndroidTVForgotPasswordPageBase.isOTPScreenOpened();
        UniversalUtils.storeAndUploadSS("OTPScreenError", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVForgotPasswordPageBase.clickResendEmailBtn();
        disneyPlusAndroidTVForgotPasswordPageBase.isResendEmailScreenOpen();
        UniversalUtils.storeAndUploadSS("ResendEmailScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        disneyPlusAndroidTVForgotPasswordPageBase.isOTPScreenOpened();
        UniversalUtils.storeAndUploadSS("OTPScreen", count.addAndGet(1), baseFile, getCastedDriver());
        if (!DisneyParameters.getEnv().equalsIgnoreCase("qa")) {
            disneyPlusAndroidTVForgotPasswordPageBase.enterOtp(verifyEmail.getDisneyOTP(user.getEmail(), EmailApi.getOtpAccountPassword(), startTime));
            disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
            disneyPlusAndroidTVForgotPasswordPageBase.isResetPasswordScreenOpened();
            UniversalUtils.storeAndUploadSS("ResetPasswordScreen", count.addAndGet(1), baseFile, getCastedDriver());
            disneyPlusAndroidTVLoginPage.clickLoginBtn();
            UniversalUtils.storeAndUploadSS("ResetPasswordScreenError", count.addAndGet(1), baseFile, getCastedDriver());
            disneyPlusAndroidTVCommonPage.pressBackTimes(1);
            disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
        }
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseFile, pathToZip);
    }

    @Test(groups = {"Home","SubUI"})
    public void homeScreenshots() throws URISyntaxException {
        com.disney.qa.api.pojos.DisneyAccount user = getAccountApi().createAccount("Yearly", region.get(), regionLanguage.get(), "V1");
        getAccountApi().entitleAccount(user, DisneySkuParameters.DISNEY_D2C_REGION_FREE, "V1");
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyPlusAndroidTVProfilePageBase disneyPlusAndroidTVProfilePageBase = new DisneyPlusAndroidTVProfilePage(getDriver());
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVDiscoverPage disneyPlusAndroidTVDiscoverPage = new DisneyPlusAndroidTVDiscoverPage(getDriver());
        AtomicInteger count = new AtomicInteger();
        String pathToZip = String.format("HomesScreenshots_%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());

        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.isOpened(), "Welcome screen did not launch");
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(user.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
        disneyPlusAndroidTVProfilePageBase.selectOkayOnTravelingScreenIfPresent();
        disneyPlusAndroidTVDiscoverPage.isOpened();
        disneyPlusAndroidTVCommonPage.pressRight(1);
        UniversalUtils.storeAndUploadSS("HomeScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus();
        UniversalUtils.storeAndUploadSS("GlobalNavigationBar", count.addAndGet(1), baseFile, getCastedDriver());
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseFile, pathToZip);
    }

    @Test(groups = {"Profile","SubUI"})
    public void profileScreenshots() throws URISyntaxException {
        DisneyAccount user = getAccountApi().createAccount("Yearly", region.get(), regionLanguage.get(), "V1");
        getAccountApi().entitleAccount(user, DisneySkuParameters.DISNEY_D2C_REGION_FREE, "V1");
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyPlusAndroidTVProfilePageBase disneyPlusAndroidTVProfilePageBase = new DisneyPlusAndroidTVProfilePage(getDriver());
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVDiscoverPage disneyPlusAndroidTVDiscoverPage = new DisneyPlusAndroidTVDiscoverPage(getDriver());
        DisneyPlusAndroidTVSettingsPageBase disneyPlusAndroidTVSettingsPageBase = new DisneyPlusAndroidTVSettingsPageBase(getDriver());
        AtomicInteger count = new AtomicInteger();
        String pathToZip = String.format("ProfileScreenshots_%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());

        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.isOpened(), "Welcome screen did not launch");
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(user.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
        disneyPlusAndroidTVProfilePageBase.selectOkayOnTravelingScreenIfPresent();
        disneyPlusAndroidTVDiscoverPage.isOpened();
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.PROFILE, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVProfilePageBase.isOpened();
        UniversalUtils.storeAndUploadSS("ProfileSelectionScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.pressDownAndSelect();
        disneyPlusAndroidTVProfilePageBase.isOpened();
        UniversalUtils.storeAndUploadSS("EditProfilesScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.selectFocusedElement();
        disneyPlusAndroidTVProfilePageBase.isEditProfileOpen();
        UniversalUtils.storeAndUploadSS("EditProfileScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.focusOptionEditProfile(disneyPlusAndroidTVProfilePageBase.getAutoPlayToggle());
        UniversalUtils.storeAndUploadSS("AutoPlayFocused", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.focusOptionEditProfile(disneyPlusAndroidTVProfilePageBase.getBackgroundVideoOption());
        UniversalUtils.storeAndUploadSS("BackgroundVideoOptionFocused", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.getEditProfileLanguageOption());
        disneyPlusAndroidTVProfilePageBase.isLanguageScreenOpen();
        UniversalUtils.storeAndUploadSS("LanguageSelectionScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        disneyPlusAndroidTVProfilePageBase.isEditProfileOpen();
        disneyPlusAndroidTVProfilePageBase.selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.getParentalControlOption());
        disneyPlusAndroidTVProfilePageBase.isParentalScreenOpen();
        UniversalUtils.storeAndUploadSS("ParentalControlScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressBackTimes(2);
        disneyPlusAndroidTVProfilePageBase.selectAddProfile();
        disneyPlusAndroidTVProfilePageBase.isSkipBtnFocused();
        UniversalUtils.storeAndUploadSS("IconSelectionScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.selectFirstProfileIcon();
        disneyPlusAndroidTVProfilePageBase.isDoneBtnPresent();
        UniversalUtils.storeAndUploadSS("CreateProfileScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.selectEditProfileDoneBtn();
        UniversalUtils.storeAndUploadSS("CreateProfileScreenEmptyNameError", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.inputProfileName("Profile", false);
        disneyPlusAndroidTVProfilePageBase.selectEditProfileDoneBtn();
        UniversalUtils.storeAndUploadSS("CreateProfileScreenDuplicateError", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.inputProfileName("KidsProfile", true);
        disneyPlusAndroidTVProfilePageBase.selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.getAutoPlayToggle());
        UniversalUtils.storeAndUploadSS("CreateProfileKidsModeEnabled", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.selectOptionFromEditProfile(disneyPlusAndroidTVProfilePageBase.getEditProfileDoneBtn());
        disneyPlusAndroidTVProfilePageBase.isOpened();
        disneyPlusAndroidTVCommonPage.selectFocusedElement();
        disneyPlusAndroidTVDiscoverPage.isOpened();
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.PROFILE, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVProfilePageBase.isOpened();
        disneyPlusAndroidTVProfilePageBase.editProfile(0);
        disneyPlusAndroidTVProfilePageBase.isEditProfileOpen();
        UniversalUtils.storeAndUploadSS("EditProfileKidsProfile", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.clickDeleteProfile();
        disneyPlusAndroidTVProfilePageBase.isDeleteProfileOpened();
        UniversalUtils.storeAndUploadSS("DeleteProfileScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVProfilePageBase.pressBackTimes(3);
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SETTINGS, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVSettingsPageBase.isOpened();
        UniversalUtils.storeAndUploadSS("SettingsScreenKidsProfile", count.addAndGet(1), baseFile, getCastedDriver());
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseFile, pathToZip);
    }

    @Test(groups = {"GlobalNavigationMenus","SubUI"})
    public void navigationMenuOptionsScreenshots() throws URISyntaxException {
        DisneyAccount user = getAccountApi().createAccount("Yearly", region.get(), regionLanguage.get(), "V1");
        getAccountApi().entitleAccount(user, DisneySkuParameters.DISNEY_D2C_REGION_FREE, "V1");
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyPlusAndroidTVProfilePageBase disneyPlusAndroidTVProfilePageBase = new DisneyPlusAndroidTVProfilePage(getDriver());
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVDiscoverPage disneyPlusAndroidTVDiscoverPage = new DisneyPlusAndroidTVDiscoverPage(getDriver());
        DisneyPlusAndroidTVWatchlistPageBase disneyPlusAndroidTVWatchlistPageBase = new DisneyPlusAndroidTVWatchlistPageBase(getDriver());
        DisneyPlusAndroidTVDetailsPageBase disneyPlusAndroidTVDetailsPageBase = new DisneyPlusAndroidTVDetailsPageBase(getDriver());

        AtomicInteger count = new AtomicInteger();
        String pathToZip = String.format("NavBarMenuScreenshots_%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());
        DisneyPlusAndroidTVSearchPage disneyPlusAndroidTVSearchPage = new DisneyPlusAndroidTVSearchPage(getDriver());
        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.isOpened(), "Welcome screen did not launch");
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(user.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
        disneyPlusAndroidTVProfilePageBase.selectOkayOnTravelingScreenIfPresent();
        disneyPlusAndroidTVDiscoverPage.isOpened();
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SEARCH, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVSearchPage.isOpened();
        disneyPlusAndroidTVSearchPage.typeInSearchBox("jjjj");
        pause(5);
        UniversalUtils.storeAndUploadSS("NoSearchResults", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVWatchlistPageBase.isOpened();
        UniversalUtils.storeAndUploadSS("WatchlistScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.ORIGINALS, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        new DisneyPlusAndroidTVOriginalsPageBase(getDriver()).isOpened();
        UniversalUtils.storeAndUploadSS("OriginalsScreen", count.addAndGet(1), baseFile, getCastedDriver());
        getSearchApi().addSeriesToWatchlist(user, MANDALORIAN_SERIES_ID);
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVWatchlistPageBase.isOpened();
        UniversalUtils.storeAndUploadSS("WatchlistOneItem", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.selectFocusedElement();
        retryOpeningDetailsPageAfterError(disneyPlusAndroidTVDetailsPageBase, disneyPlusAndroidTVWatchlistPageBase);
        disneyPlusAndroidTVDetailsPageBase.isPlayBtnPresent();
        UniversalUtils.storeAndUploadSS("OriginalsDetailsScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressDown(1);
        disneyPlusAndroidTVCommonPage.pressRight(4);
        disneyPlusAndroidTVCommonPage.selectFocusedElement();
        UniversalUtils.storeAndUploadSS("OriginalsDetailsTab", count.addAndGet(1), baseFile, getCastedDriver());
        getSearchApi().addMovieToWatchlist(user, SHANG_CHI_ID);
        disneyPlusAndroidTVWatchlistPageBase.useActionToOpenWatchListPage(AndroidTVUtils::pressBack);
        disneyPlusAndroidTVWatchlistPageBase.isOpened();
        disneyPlusAndroidTVCommonPage.selectFocusedElement();
        retryOpeningDetailsPageAfterError(disneyPlusAndroidTVDetailsPageBase, disneyPlusAndroidTVWatchlistPageBase);
        disneyPlusAndroidTVDetailsPageBase.isPlayBtnPresent();
        UniversalUtils.storeAndUploadSS("ShangChiDetailsScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressDown(1);
        disneyPlusAndroidTVCommonPage.pressRight(2);
        UniversalUtils.storeAndUploadSS("ShangChiVersionsTab", count.addAndGet(1), baseFile, getCastedDriver());
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseFile, pathToZip);
    }

    @Test(groups = {"MoviesDetailAndPlayback","SubUI"})
    public void moviesPageAndPlaybackScreenshots() throws URISyntaxException {
        DisneyAccount user = getAccountApi().createAccount("Yearly", region.get(), regionLanguage.get(), "V1");
        getAccountApi().entitleAccount(user, DisneySkuParameters.DISNEY_D2C_REGION_FREE, "V1");
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyPlusAndroidTVProfilePageBase disneyPlusAndroidTVProfilePageBase = new DisneyPlusAndroidTVProfilePage(getDriver());
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVDiscoverPage disneyPlusAndroidTVDiscoverPage = new DisneyPlusAndroidTVDiscoverPage(getDriver());
        DisneyPlusAndroidTVVideoPlayerPage disneyPlusAndroidTVVideoPlayerPage = new DisneyPlusAndroidTVVideoPlayerPage(getDriver());
        DisneyPlusAndroidTVDetailsPageBase disneyPlusAndroidTVDetailsPageBase = new DisneyPlusAndroidTVDetailsPageBase(getDriver());
        DisneyPlusAndroidTVMoviesPageBase disneyPlusAndroidTVMoviesPageBase = new DisneyPlusAndroidTVMoviesPageBase(getDriver());
        AtomicInteger count = new AtomicInteger();
        String pathToZip = String.format("MoviesPageAndPlaybackScreenshots_%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());

        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.isOpened(), "Welcome screen did not launch");
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(user.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
        disneyPlusAndroidTVProfilePageBase.selectOkayOnTravelingScreenIfPresent();
        disneyPlusAndroidTVDiscoverPage.isOpened();
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.MOVIES, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVMoviesPageBase.isOpened();
        UniversalUtils.storeAndUploadSS("MoviesScreen", count.addAndGet(1), baseFile, getCastedDriver());
        pause(10);
        disneyPlusAndroidTVCommonPage.pressDown(1);
        disneyPlusAndroidTVCommonPage.selectFocusedElement();
        disneyPlusAndroidTVDetailsPageBase.isOpened();
        UniversalUtils.storeAndUploadSS("MovieDetailsScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVDetailsPageBase.clickStartPlayerBtn();
        pause(50);
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        disneyPlusAndroidTVDetailsPageBase.isOpened();
        UniversalUtils.storeAndUploadSS("MovieDetailsScreenPartiallyPlayed", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.HOME, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVDiscoverPage.isOpened();
        disneyPlusAndroidTVCommonPage.pressDown(getContinueWatchingIndex(user) + 2);
        UniversalUtils.storeAndUploadSS("MovieRemainingTime", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.MOVIES, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVMoviesPageBase.isOpened();
        pause(10);
        disneyPlusAndroidTVCommonPage.pressDown(1);
        disneyPlusAndroidTVCommonPage.selectFocusedElement();
        disneyPlusAndroidTVDetailsPageBase.isOpened();
        disneyPlusAndroidTVDetailsPageBase.clickStartPlayerBtn();
        disneyPlusAndroidTVVideoPlayerPage.selectFastForwardBtn(3);
        disneyPlusAndroidTVVideoPlayerPage.isVideoFinishedAfterFastForwarding(50);

        disneyPlusAndroidTVCommonPage.pressRight(1);
        disneyPlusAndroidTVCommonPage.selectFocusedElement();
        disneyPlusAndroidTVDetailsPageBase.isPlayBtnPresent();
        disneyPlusAndroidTVCommonPage.pressDownAndSelect();
        UniversalUtils.storeAndUploadSS("MovieDetailsForUpNextMovie", count.addAndGet(1), baseFile, getCastedDriver());
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseFile, pathToZip);
    }

    @Test(groups = {"SeriesDetailAndPlayback","SubUI"})
    public void seriesPageAndPlaybackScreenshots() throws URISyntaxException {
        DisneyAccount user = getAccountApi().createAccount("Yearly", region.get(), regionLanguage.get(), "V1");
        getAccountApi().entitleAccount(user, DisneySkuParameters.DISNEY_D2C_REGION_FREE, "V1");
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyPlusAndroidTVProfilePageBase disneyPlusAndroidTVProfilePageBase = new DisneyPlusAndroidTVProfilePage(getDriver());
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVDiscoverPage disneyPlusAndroidTVDiscoverPage = new DisneyPlusAndroidTVDiscoverPage(getDriver());
        DisneyPlusAndroidTVVideoPlayerPage disneyPlusAndroidTVVideoPlayerPage = new DisneyPlusAndroidTVVideoPlayerPage(getDriver());
        DisneyPlusAndroidTVDetailsPageBase disneyPlusAndroidTVDetailsPageBase = new DisneyPlusAndroidTVDetailsPageBase(getDriver());
        DisneyPlusAndroidTVSeriesPageBase disneyPlusAndroidTVSeriesPageBase = new DisneyPlusAndroidTVSeriesPageBase(getDriver());
        DisneyPlusAndroidTVWatchlistPageBase disneyPlusAndroidTVWatchlistPageBase = new DisneyPlusAndroidTVWatchlistPageBase(getDriver());

        AtomicInteger count = new AtomicInteger();
        String pathToZip = String.format("SeriesPageAndPlaybackScreenshots_%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());
        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.isOpened(), "Welcome screen did not launch");
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(user.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
        disneyPlusAndroidTVProfilePageBase.selectOkayOnTravelingScreenIfPresent();
        disneyPlusAndroidTVDiscoverPage.isOpened();
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SERIES, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVSeriesPageBase.isOpened();
        UniversalUtils.storeAndUploadSS("SeriesScreen", count.addAndGet(1), baseFile, getCastedDriver());
        getSearchApi().addSeriesToWatchlist(user, SIMPSONS_SERIES_ID);
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVWatchlistPageBase.isOpened();
        disneyPlusAndroidTVCommonPage.selectFocusedElement();
        retryOpeningDetailsPageAfterError(disneyPlusAndroidTVDetailsPageBase, disneyPlusAndroidTVWatchlistPageBase);
        disneyPlusAndroidTVDetailsPageBase.isOpened();
        UniversalUtils.storeAndUploadSS("SeriesDetailsScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressDownAndSelect();
        UniversalUtils.storeAndUploadSS("SeriesEpisodesTab", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressDown(1);
        disneyPlusAndroidTVCommonPage.pressRight(1);
        disneyPlusAndroidTVCommonPage.selectFocusedElement();
        pause(40);
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        disneyPlusAndroidTVDetailsPageBase.isOpened();
        disneyPlusAndroidTVCommonPage.pressUp(2);
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.HOME, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVDiscoverPage.isOpened();

        disneyPlusAndroidTVCommonPage.pressDown(getContinueWatchingIndex(user) + 2);
        UniversalUtils.storeAndUploadSS("EpisodeRemainingTime", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.WATCHLIST, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVWatchlistPageBase.isOpened();
        disneyPlusAndroidTVCommonPage.selectFocusedElement();
        retryOpeningDetailsPageAfterError(disneyPlusAndroidTVDetailsPageBase, disneyPlusAndroidTVWatchlistPageBase);
        disneyPlusAndroidTVDetailsPageBase.isOpened();
        UniversalUtils.storeAndUploadSS("SeriesDetailPartiallyPlayedEpisode", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVDetailsPageBase.clickStartPlayerBtn();
        disneyPlusAndroidTVVideoPlayerPage.selectFastForwardBtn(2);
        disneyPlusAndroidTVVideoPlayerPage.isVideoFinishedAfterFastForwarding(40);
        disneyPlusAndroidTVCommonPage.pressBackTimes(2);
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.HOME, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVDiscoverPage.isOpened();
        disneyPlusAndroidTVCommonPage.pressDown(getContinueWatchingIndex(user) + 2);
        UniversalUtils.storeAndUploadSS("StartEpisodeContinueWatching", count.addAndGet(1), baseFile, getCastedDriver());
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseFile, pathToZip);
    }

    @Test(groups = {"SettingMenus","SubUI"})
    public void settingsScreenshot() throws URISyntaxException {
        DisneyAccountApi disneyAccountApi = new DisneyAccountApi("android", DisneyParameters.getEnv(), "disney");
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyPlusAndroidTVProfilePageBase disneyPlusAndroidTVProfilePageBase = new DisneyPlusAndroidTVProfilePage(getDriver());
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVDiscoverPage disneyPlusAndroidTVDiscoverPage = new DisneyPlusAndroidTVDiscoverPage(getDriver());
        DisneyPlusAndroidTVSettingsPageBase disneyPlusAndroidTVSettingsPageBase = new DisneyPlusAndroidTVSettingsPageBase(getDriver());
        DisneyPlusAndroidTVForgotPasswordPageBase disneyPlusAndroidTVForgotPasswordPageBase = new DisneyPlusAndroidTVForgotPasswordPageBase(getDriver());
        DisneyPlusAndroidTVLegalPageBase disneyPlusAndroidTVLegalPageBase = new DisneyPlusAndroidTVLegalPageBase(getDriver());
        VerifyEmail verifyEmail = new VerifyEmail();
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());

        AtomicInteger count = new AtomicInteger();
        String pathToZip = String.format("SettingsScreenshots_%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());
        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.isOpened(), "Welcome screen did not launch");
        DisneyAccount account = disneyAccountApi.createAccountForOTP(region.get(), regionLanguage.get());
        disneyAccountApi.entitleAccount(account, com.disney.qa.api.utils.DisneySkuParameters.DISNEY_D2C_REGION_FREE, "V1");
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(account.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(account.getUserPass());
        disneyPlusAndroidTVProfilePageBase.selectOkayOnTravelingScreenIfPresent();
        disneyPlusAndroidTVDiscoverPage.isOpened();
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SETTINGS, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVSettingsPageBase.isOpened();
        UniversalUtils.storeAndUploadSS("SettingsScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVSettingsPageBase.selectSettingsBasedOnIndex(1, false);
        disneyPlusAndroidTVSettingsPageBase.isAppSettingsOpened();
        UniversalUtils.storeAndUploadSS("AppSettingsScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        disneyPlusAndroidTVSettingsPageBase.selectSettingsBasedOnIndex(2, false);
        disneyPlusAndroidTVSettingsPageBase.isSettingsAccountOpened();
        UniversalUtils.storeAndUploadSS("AccountSettingsScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVSettingsPageBase.selectSettingsBasedOnIndex(0, true);
        disneyPlusAndroidTVForgotPasswordPageBase.isOTPScreenOpened();
        Date startTime = verifyEmail.getStartTime();
        if (!disneyPlusAndroidTVForgotPasswordPageBase.isForgotPasswordTitlePresent(5) || AndroidTVUtils.isAmazon()) {
            disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        }
        UniversalUtils.storeAndUploadSS("OTPScreenFromAccountSettings", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
        disneyPlusAndroidTVForgotPasswordPageBase.isOTPScreenOpened();
        UniversalUtils.storeAndUploadSS("OTPScreenErrorFromAccountSettings", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVForgotPasswordPageBase.clickResendEmailBtn();

        disneyPlusAndroidTVForgotPasswordPageBase.isResendEmailScreenOpen();
        UniversalUtils.storeAndUploadSS("ResendEmailScreenFromAccountSettings", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVForgotPasswordPageBase.clickOkBtn();
        disneyPlusAndroidTVForgotPasswordPageBase.enterOtp(verifyEmail.getDisneyOTP(account.getEmail(), EmailApi.getOtpAccountPassword(), startTime));
        if (!disneyPlusAndroidTVForgotPasswordPageBase.isForgotPasswordTitlePresent(5) || AndroidTVUtils.isAmazon()) {
            disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        }
        androidTVUtils.hideKeyboardIfPresent();
        if(!disneyPlusAndroidTVSettingsPageBase.isChangeEmailScreenOpened())
        {
            disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
        }
        disneyPlusAndroidTVSettingsPageBase.isChangeEmailScreenOpened();
        UniversalUtils.storeAndUploadSS("ChangeEmailScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVSettingsPageBase.clickLogoutAllCheckBox();
        disneyPlusAndroidTVSettingsPageBase.clickSaveBtn();
        UniversalUtils.storeAndUploadSS("ChangeEmailScreenEmptyError", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterEmail("test");
        disneyPlusAndroidTVCommonPage.getAndroidTVUtilsInstance().hideKeyboardIfPresent();
        disneyPlusAndroidTVSettingsPageBase.clickSaveBtn();
        UniversalUtils.storeAndUploadSS("ChangeEmailScreenIncorrectEmailError", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        startTime = verifyEmail.getStartTime();
        disneyPlusAndroidTVSettingsPageBase.selectSettingsBasedOnIndex(1, true);
        pause(5);
        if (!disneyPlusAndroidTVForgotPasswordPageBase.isForgotPasswordTitlePresent(5) || AndroidTVUtils.isAmazon()) {
            disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        }
        UniversalUtils.captureAndUpload(getCastedDriver());
        disneyPlusAndroidTVForgotPasswordPageBase.enterOtp(verifyEmail.getDisneyOTP(account.getEmail(), EmailApi.getOtpAccountPassword(), startTime));
        androidTVUtils.hideKeyboardIfPresent();
        if(!disneyPlusAndroidTVForgotPasswordPageBase.isResetPasswordScreenOpened())
        {
            disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
        }
        disneyPlusAndroidTVForgotPasswordPageBase.isResetPasswordScreenOpened();
        UniversalUtils.storeAndUploadSS("ResetPasswordScreenFromAccountSettings", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVSettingsPageBase.clickLogoutAllCheckBox();
        disneyPlusAndroidTVLoginPage.clickLoginBtn();
        UniversalUtils.storeAndUploadSS("ResetPasswordScreenEmptyError", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("a");
        androidTVUtils.hideKeyboardIfPresent();
        disneyPlusAndroidTVSettingsPageBase.clickLogoutAllCheckBox();
        disneyPlusAndroidTVLoginPage.clickLoginBtn();
        UniversalUtils.storeAndUploadSS("ResetPasswordScreenIncorrectPasswordError", count.addAndGet(1), baseFile, getCastedDriver());
        handlePasswordStrength(count, disneyPlusAndroidTVLoginPage, androidTVUtils);
        androidTVUtils.hideKeyboardIfPresent();
        androidTVUtils.pressBack();
        disneyPlusAndroidTVSettingsPageBase.selectSettingsBasedOnIndex(2, true);
        disneyPlusAndroidTVSettingsPageBase.isLogoutAllPageOpened();
        UniversalUtils.storeAndUploadSS("LogOutAllScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVSettingsPageBase.clickLogOutAll();
        UniversalUtils.storeAndUploadSS("LogOutAllScreenEmptyPassError", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("ab");
        disneyPlusAndroidTVSettingsPageBase.clickLogOutAll();
        pause(5);
        UniversalUtils.storeAndUploadSS("LogOutAllScreenIncorrectPassError", count.addAndGet(1), baseFile, getCastedDriver());

        disneyPlusAndroidTVCommonPage.pressBackTimes(2);
        disneyPlusAndroidTVSettingsPageBase.selectSettingsBasedOnIndex(3, false);
        disneyPlusAndroidTVSettingsPageBase.isHelpOpen();
        UniversalUtils.storeAndUploadSS("HelpScreen", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        disneyPlusAndroidTVSettingsPageBase.selectSettingsBasedOnIndex(4, false);
        boolean isLegalScreenOpen = disneyPlusAndroidTVLegalPageBase.isOpened();
        int retries = 0;
        while (!isLegalScreenOpen && retries < 3) {
            disneyPlusAndroidTVCommonPage.clickPositiveButton();
            disneyPlusAndroidTVSettingsPageBase.selectSettingsBasedOnIndex(4, false);
            isLegalScreenOpen = disneyPlusAndroidTVLegalPageBase.isOpened();
            retries++;
        }
        UniversalUtils.storeAndUploadSS("LegalDocument1", count.addAndGet(1), baseFile, getCastedDriver());
        getAllLegalScreens(count, disneyPlusAndroidTVLegalPageBase, disneyPlusAndroidTVCommonPage);
        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseFile, pathToZip);
    }

    @Test(groups = {"Welch"})
    public void welchExistingIAP() throws IOException, URISyntaxException {
        String flow = "Existing-Sub-CD-IAP";
        DisneyAccountApi disneyAccountApi = new DisneyAccountApi("android", DisneyParameters.getEnv(), "disney");
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyPlusAndroidTVProfilePageBase disneyPlusAndroidTVProfilePageBase = new DisneyPlusAndroidTVProfilePage(getDriver());
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVWelchPageBase disneyPlusAndroidTVWelchPageBase = new DisneyPlusAndroidTVWelchPageBase(getDriver());
        String pathToZip = String.format("Welch-%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());
        com.disney.qa.api.pojos.DisneyAccount entitledUser = disneyAccountApi.createAccount("Yearly", region.get(), regionLanguage.get(), "V1");
        disneyAccountApi.patchStarOnboardingStatus(entitledUser, false);
        disneyAccountApi.addFlex(entitledUser);
        disneyAccountApi.addProfile(entitledUser, "Profile", regionLanguage.get(), null, null, false, true);
        disneyAccountApi.overrideLocations(entitledUser, region.get());
        int count = 0;

        disneyPlusAndroidTVWelcomePage.isOpened();
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(entitledUser.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(entitledUser.getUserPass());

        disneyPlusAndroidTVProfilePageBase.isAddProfilePresent();
        disneyPlusAndroidTVProfilePageBase.selectDefaultProfile();

        disneyPlusAndroidTVWelchPageBase.isOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, baseFile, getCastedDriver());
        //Select Now on Disney+ STAR continue button
        disneyPlusAndroidTVCommonPage.selectFocusedElement();

        disneyPlusAndroidTVWelchPageBase.isFullCatalogScreenOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, baseFile, getCastedDriver());
        //Verify the focus is on the Not now button on Access Full catalog screen
        disneyPlusAndroidTVCommonPage.pressDown(1);
        //Select the Not now button on Access Full catalog screen
        disneyPlusAndroidTVCommonPage.selectFocusedElement();

        disneyPlusAndroidTVWelchPageBase.isMaturityRatingConfirmationScreenOpen();
        //Verify the focus is on got it button on rating confirmation screen
        UniversalUtils.storeAndUploadSS(flow, ++count, baseFile, getCastedDriver());
        disneyPlusAndroidTVCommonPage.pressDown(1);
        //Select the back button on maturity rating confirmation screen
        disneyPlusAndroidTVCommonPage.selectFocusedElement();

        //Select continue on the you have access the full catalog screen
        disneyPlusAndroidTVWelchPageBase.isYouHaveNowFullAccessScreenOpened();
        disneyPlusAndroidTVCommonPage.selectFocusedElement();

        if(!disneyPlusAndroidTVWelchPageBase.isConfirmPasswordScreenOpened())
            disneyPlusAndroidTVCommonPage.pressBackTimes(1);

        disneyPlusAndroidTVWelchPageBase.isConfirmPasswordScreenOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, baseFile, getCastedDriver());
        //Attempt to login with the wrong password
        disneyPlusAndroidTVLoginPage.logInWithPassword("1234");
        if (disneyPlusAndroidTVWelchPageBase.isErrorViewPresent())
            UniversalUtils.storeAndUploadSS(flow, ++count, baseFile, getCastedDriver());

        //Focus the input password screen
        disneyPlusAndroidTVWelchPageBase.pressUp(1);
        //Login to the start welcome screen
        disneyPlusAndroidTVLoginPage.logInWithPassword(entitledUser.getUserPass());

        disneyPlusAndroidTVWelchPageBase.isWelcomeToStarScreenOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, baseFile, getCastedDriver());
        //Open Create Profile pin screen
        disneyPlusAndroidTVWelchPageBase.selectFocusedElement();

        disneyPlusAndroidTVWelchPageBase.isCreatePinScreenOpened();
        UniversalUtils.storeAndUploadSS(flow, ++count, baseFile, getCastedDriver());
        //Generate the error message
        disneyPlusAndroidTVWelchPageBase.clickSetProfilePin();
        if (disneyPlusAndroidTVWelchPageBase.isPinErrorMessagePresent())
            UniversalUtils.storeAndUploadSS(flow, ++count, baseFile, getCastedDriver());

        disneyPlusAndroidTVWelchPageBase.typeUsingKeyEvents("1235");
        //move down to set profile pin button
        disneyPlusAndroidTVWelchPageBase.pressDown(3);
        disneyPlusAndroidTVWelchPageBase.selectFocusedElement();

        //Wait for pin message and take screenshot
        if (disneyPlusAndroidTVWelchPageBase.isPinSetMessagePresent())
            UniversalUtils.storeAndUploadSS(flow, ++count, baseFile, getCastedDriver());

        disneyPlusAndroidTVWelchPageBase.isMaturityRatingScreenOpened();
        //Select rating for 2nd profile
        disneyPlusAndroidTVWelchPageBase.selectFocusedElement();
        UniversalUtils.storeAndUploadSS(flow, ++count, baseFile, getCastedDriver());

        clearAppCache();
        disneyPlusAndroidTVWelcomePage.isOpened();
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(entitledUser.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(entitledUser.getUserPass());

        disneyPlusAndroidTVProfilePageBase.isAddProfilePresent();
        disneyPlusAndroidTVProfilePageBase.pressRight(1);
        disneyPlusAndroidTVProfilePageBase.selectFocusedElement();

        ZipUtils.uploadZipFileToJenkinsAsArtifact(baseFile, pathToZip);
    }

    public void handlePasswordStrength(AtomicInteger count, DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage, AndroidTVUtils androidTVUtils) {
        disneyPlusAndroidTVLoginPage.clickPasswordField();
        androidTVUtils.sendInput("a");
        if (AndroidTVUtils.isAmazon()) {
            androidTVUtils.hideKeyboardIfPresent();
        }
        UniversalUtils.storeAndUploadSS("PasswordStrengthPoor", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("123456");
        UniversalUtils.storeAndUploadSS("PasswordStrengthPoor", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("abcdefghi");
        UniversalUtils.storeAndUploadSS("PasswordStrengthFair", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("abcdefghij");
        UniversalUtils.storeAndUploadSS("PasswordStrengthGood", count.addAndGet(1), baseFile, getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("abcdefghijkl123$");
        UniversalUtils.storeAndUploadSS("PasswordStrengthGreat", count.addAndGet(1), baseFile, getCastedDriver());
    }

    public int getContinueWatchingIndex(DisneyAccount user) {
        ContentCollection homePageResponse = getSearchApi().getPersonalizedCollection(user, regionLanguage.get(), region.get(), "home", "home");
        List<String> rowTitles = homePageResponse.getCollectionSetTitles();
        String continueWatchingTitle = DisneySearchApi.parseValueFromJson(homePageResponse.getJsonNode().toString(), CONTINUE_WATCHING_SHELF_TITLE.getValue()).get(0);
        rowTitles.removeIf(item -> item.contains("${title}"));
        return rowTitles.indexOf(continueWatchingTitle);
    }

    //TODO: Create a separate ticket for fixing this
    public void turnOnDictionaryKeys() throws URISyntaxException {
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyPlusAndroidTVSettingsPageBase disneyPlusAndroidTVSettingsPageBase = new DisneyPlusAndroidTVSettingsPageBase(getDriver());
        DisneyAccount user = getAccountApi().createAccount("Yearly", region.get(), regionLanguage.get(), "V1");
        getAccountApi().entitleAccount(user, DisneySkuParameters.DISNEY_D2C_REGION_FREE, "V1");
        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.isOpened(), "Welcome screen did not launch");
        UniversalUtils.captureAndUpload(getCastedDriver());
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(user.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
        Assert.assertTrue(new DisneyPlusAndroidTVDiscoverPage(getDriver()).isOpened(), "Home page did not launch");
        disneyPlusAndroidTVCommonPage.navigateNavBarAndSelect(DisneyPlusAndroidTVDiscoverPage.GlobalNavItem.SETTINGS, disneyPlusAndroidTVCommonPage.openGlobalNavAndFocus());
        disneyPlusAndroidTVSettingsPageBase.selectSettingsBasedOnIndex(5, false);
        DisneyPlusCommonPageBase.fluentWait(getDriver(), 120, 1, "Unable to find 'Debug Dictionary'")
                .until(it -> {
                    disneyPlusAndroidTVCommonPage.pressDown(3);
                    return disneyPlusAndroidTVCommonPage.isTextElementPresent("Debug Dictionary");
                });
        disneyPlusAndroidTVCommonPage.clickOnGenericTextElement("Debug Dictionary");
        disneyPlusAndroidTVCommonPage.pressBackTimes(1);
        disneyPlusAndroidTVCommonPage.pressDown(1);
        disneyPlusAndroidTVSettingsPageBase.selectSettingsBasedOnIndex(4, false);

    }

    public void getAllLegalScreens(AtomicInteger count, DisneyPlusAndroidTVLegalPageBase disneyPlusAndroidTVLegalPageBase, DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage) {
        String previousTitle = disneyPlusAndroidTVLegalPageBase.getDocumentTitle();
        disneyPlusAndroidTVCommonPage.pressDownAndSelect();
        int counter = 1;
        while (!previousTitle.equals(disneyPlusAndroidTVLegalPageBase.getDocumentTitle())) {
            previousTitle = disneyPlusAndroidTVLegalPageBase.getDocumentTitle();
            UniversalUtils.storeAndUploadSS("LegalDocument" + ++counter, count.addAndGet(1), baseFile, getCastedDriver());
            disneyPlusAndroidTVCommonPage.pressDownAndSelect();
        }
    }

    public void retryOpeningDetailsPageAfterError(DisneyPlusAndroidTVDetailsPageBase disneyPlusAndroidTVDetailsPageBase, DisneyPlusAndroidTVWatchlistPageBase disneyPlusAndroidTVWatchlistPageBase) {
        if (!disneyPlusAndroidTVDetailsPageBase.isOpened()) {
            disneyPlusAndroidTVDetailsPageBase.selectFocusedElement();
            disneyPlusAndroidTVWatchlistPageBase.isOpened();
            disneyPlusAndroidTVWatchlistPageBase.pressRight(1);
            disneyPlusAndroidTVDetailsPageBase.selectFocusedElement();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if (!isAmazon.get()) {
            charlesProxy.get().stopProxy();
            resetProxy();
        }
        resetSystemLangToEnglish();
        quitDrivers(TestPhase.Phase.AFTER_METHOD, TestPhase.Phase.METHOD);
    }
}