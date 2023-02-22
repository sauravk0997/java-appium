package com.disney.qa.tests.disney.android.tv.localization.general;

import com.disney.charles.CharlesProxy;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.client.requests.CreateDisneyAccountRequest;
import com.disney.qa.api.disney.DisneyApiProvider;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.pojos.DisneyOrder;
import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.common.jarvis.android.JarvisAndroidBase;
import com.disney.qa.common.jarvis.android.JarvisAndroidTV;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVLoginPage;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVPaywallPage;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVWelcomePage;
import com.disney.qa.disney.android.pages.tv.pages.DisneyPlusAndroidTVLegalPageBase;
import com.disney.qa.tests.BaseAndroidTVTest;
import com.disney.util.ZipUtils;
import com.qaprosoft.appcenter.AppCenterManager;
import com.qaprosoft.carina.core.foundation.listeners.CarinaListener;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.TestPhase;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.annotation.Maintainer;
import com.zebrunner.agent.core.registrar.Artifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

@Maintainer("Furqan")
public class DisneyPlusFireTVAndroidTVLocalizationTests extends BaseAndroidTVTest {

    private final ThreadLocal<String> baseFile = new ThreadLocal<>();
    private final ThreadLocal<String> regionLanguage = new ThreadLocal<>();
    private final ThreadLocal<String> region = new ThreadLocal<>();
    private final ThreadLocal<CharlesProxy> charlesProxy = new ThreadLocal<>();
    private final ThreadLocal<Boolean> isAmazon = new ThreadLocal<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        CarinaListener.disableDriversCleanup();
        R.CONFIG.put("forcibly_disable_driver_quit", "true");
        DisneyCountryData countryData = new DisneyCountryData();
        regionLanguage.set(R.CONFIG.get("custom_string2"));
        region.set(R.CONFIG.get("custom_string3"));
        String country = (String) countryData.searchAndReturnCountryData(region.get(),"code", "country");
        JarvisAndroidBase.DisneyEnvironments jarvisEnv = JarvisAndroidTV.getJarvisDisneyEnvironment(R.CONFIG.get("capabilities.custom_env"));
        JarvisAndroidTV jarvisAndroidTV = new JarvisAndroidTV(getDriver());
        isAmazon.set(AndroidTVUtils.isAmazon());
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
        clearCache(PLAY_STORE_PACKAGE);
        pause(10);
        clearAppCache();
    }

    @Test(groups = {"BGImage"})
    public void backgroundImage() throws IOException {
        baseFile.set("Screenshots-BGImage/");
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVPaywallPage disneyPlusAndroidTVPaywallPage = new DisneyPlusAndroidTVPaywallPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyAccountApi api = new DisneyAccountApi("android", DisneyParameters.getEnv(), "disney");
        DisneyAccount user = api.createAccountForOTP(region.get(), regionLanguage.get());
        AtomicInteger count = new AtomicInteger(0);
        String pathToZip = String.format("Screenshots_BG_Image_%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());
        disneyPlusAndroidTVWelcomePage.isOpened();
        pause(5);
        UniversalUtils.storeAndUploadSS("WelcomeScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());
        //ANDTV/FireTV if there is no login button then the R.ID for login button = sign up button
        disneyPlusAndroidTVWelcomePage.continueToLogin();

        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().addDefaultEntitlement(true).country(region.get()).language(regionLanguage.get()).build();
        request.addOrderSetting(DisneyOrder.SET_EXPIRED);
        DisneyAccount account = api.createAccount(request);
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(account.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
        disneyPlusAndroidTVLoginPage.isRestartSubscriptionPageOpened();
        pause(5);
        UniversalUtils.storeAndUploadSS("WelcomeBackNoSubscriptionScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());
        disneyPlusAndroidTVPaywallPage.clickOnLogOut();
        disneyPlusAndroidTVPaywallPage.isTierTwoTitleOpen();
        disneyPlusAndroidTVCommonPage.pressDownAndSelect();
        disneyPlusAndroidTVWelcomePage.isOpened();
        if (disneyPlusAndroidTVWelcomePage.isLoginButtonPresent()) {
            disneyPlusAndroidTVWelcomePage.proceedToSignUp();
            disneyPlusAndroidTVLoginPage.signUpPageErrorHandler();
            disneyPlusAndroidTVLoginPage.isSignUpPageOpen();
            disneyPlusAndroidTVLoginPage.enterEmail(new CreateDisneyAccountRequest().getEmail());
            disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
            dismissDisclosurePageIfPresent(disneyPlusAndroidTVLoginPage, count, disneyPlusAndroidTVLoginPage.getCreatePasswordTitle());
            disneyPlusAndroidTVCommonPage.getAndroidTVUtilsInstance().hideKeyboardIfPresent();
            disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
            disneyPlusAndroidTVPaywallPage.isOpened();
            disneyPlusAndroidTVCommonPage.pressBackTimes(1);
            disneyPlusAndroidTVCommonPage.selectFocusedElement();
            disneyPlusAndroidTVPaywallPage.isCompleteSubscriptionScreenOpened();
            UniversalUtils.storeAndUploadSS("OneStepAwayScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());
        }

        ZipUtils.zipDirectory(baseFile.get(), pathToZip);
        Artifact.attachToTest(pathToZip, Path.of(pathToZip));
    }

    @Test(groups = {"Onboarding"})
    public void signUpScreenshots() throws IOException {
        baseFile.set("Screenshots-Onboarding/");
        boolean isKR = region.get().equalsIgnoreCase("kr");
        DisneyPlusAndroidTVWelcomePage disneyPlusAndroidTVWelcomePage = new DisneyPlusAndroidTVWelcomePage(getDriver());
        DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage = new DisneyPlusAndroidTVLoginPage(getDriver());
        DisneyPlusAndroidTVPaywallPage disneyPlusAndroidTVPaywallPage = new DisneyPlusAndroidTVPaywallPage(getDriver());
        DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage = new DisneyPlusAndroidTVCommonPage(getDriver());
        DisneyPlusAndroidTVLegalPageBase disneyPlusAndroidTVLegalPageBase = new DisneyPlusAndroidTVLegalPageBase(getDriver());
        DisneyApiProvider apiProvider = new DisneyApiProvider();
        DisneyAccountApi api = new DisneyAccountApi("android", DisneyParameters.getEnv(), "disney");
        DisneyAccount user = api.createAccountForOTP(region.get(), regionLanguage.get());
        CreateDisneyAccountRequest request = CreateDisneyAccountRequest.builder().addDefaultEntitlement(true).country(region.get()).language(regionLanguage.get()).build();
        request.addOrderSetting(DisneyOrder.SET_EXPIRED);
        DisneyAccount expired = api.createAccount(request);
        AtomicInteger count = new AtomicInteger(0);
        String pathToZip = String.format("Screenshots_Onboarding_%s_%s-%s.zip", System.nanoTime(), regionLanguage.get(), region.get());
        String validEmail = apiProvider.getUniqueUserEmail();
        //ANDTV/FireTV if there is no login button then the R.ID for login button = sign up button
        disneyPlusAndroidTVWelcomePage.isOpened();
        if (disneyPlusAndroidTVWelcomePage.isLoginButtonPresent()) {
            disneyPlusAndroidTVWelcomePage.forceFocusSignUp();
            disneyPlusAndroidTVCommonPage.getAndroidTVUtilsInstance().pressSelect();
            disneyPlusAndroidTVLoginPage.signUpPageErrorHandler();
            disneyPlusAndroidTVLoginPage.isSignUpPageOpen();
            UniversalUtils.storeAndUploadSS("SignUpScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());
            LOGGER.info(getDriver().getPageSource());
            disneyPlusAndroidTVLoginPage.selectSignUp(isKR);
            disneyPlusAndroidTVCommonPage.isErrorViewPresent();
            UniversalUtils.storeAndUploadSS("SignUpScreenEmailError", count.addAndGet(1), baseFile.get(), getCastedDriver());
            disneyPlusAndroidTVLoginPage.proceedToLegalPage(isKR);
            disneyPlusAndroidTVLegalPageBase.isDocumentTitlePresent();
            pause(5);
            UniversalUtils.storeAndUploadSS("LegalScreenFromSignUp", count.addAndGet(1), baseFile.get(), getCastedDriver());
            getAllLegalScreens(count, disneyPlusAndroidTVLegalPageBase, disneyPlusAndroidTVCommonPage);
            disneyPlusAndroidTVCommonPage.pressBackTimes(1);
            disneyPlusAndroidTVLoginPage.enterEmail(validEmail);
            disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
            dismissDisclosurePageIfPresent(disneyPlusAndroidTVLoginPage, count, disneyPlusAndroidTVLoginPage.getCreatePasswordTitle());
            disneyPlusAndroidTVCommonPage.getAndroidTVUtilsInstance().hideKeyboardIfPresent();
            UniversalUtils.storeAndUploadSS("CreatePasswordScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());
            disneyPlusAndroidTVLoginPage.clickAgreeAndContinueButton();
            disneyPlusAndroidTVCommonPage.getAndroidTVUtilsInstance().hideKeyboardIfPresent();
            pause(20);
            UniversalUtils.storeAndUploadSS("CreatePasswordScreenError", count.addAndGet(1), baseFile.get(), getCastedDriver());
            handlePasswordStrength(count, disneyPlusAndroidTVLoginPage);
            disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
            disneyPlusAndroidTVPaywallPage.isOpened();
            pause(20);
            UniversalUtils.storeAndUploadSS("StartStreamingScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());

            disneyPlusAndroidTVCommonPage.pressBackTimes(1);
            UniversalUtils.storeAndUploadSS("FinishSubscribingLaterScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());
            disneyPlusAndroidTVCommonPage.selectFocusedElement();
            disneyPlusAndroidTVPaywallPage.isCompleteSubscriptionScreenOpened();
            pause(20);
            UniversalUtils.storeAndUploadSS("CompleteSubscriptionScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());
            disneyPlusAndroidTVPaywallPage.clickOnLogOut();
            disneyPlusAndroidTVCommonPage.isTierTwoTitleOpen();
            UniversalUtils.storeAndUploadSS("LogOutConfirmation", count.addAndGet(1), baseFile.get(), getCastedDriver());
            disneyPlusAndroidTVCommonPage.pressDownAndSelect();
        }
        disneyPlusAndroidTVWelcomePage.isOpened();
        UniversalUtils.storeAndUploadSS("WelcomeScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());
        disneyPlusAndroidTVWelcomePage.continueToLogin();
        disneyPlusAndroidTVLoginPage.proceedToPasswordMode(expired.getEmail());
        disneyPlusAndroidTVLoginPage.logInWithPassword(user.getUserPass());
        disneyPlusAndroidTVLoginPage.isRestartSubscriptionPageOpened();
        pause(20);
        UniversalUtils.storeAndUploadSS("WelcomeBackNoSubscriptionScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());
        disneyPlusAndroidTVLoginPage.clickRestartSubscriptionButton();
        dismissDisclosurePageIfPresent(disneyPlusAndroidTVLoginPage, count, disneyPlusAndroidTVPaywallPage.getPaywallButton());
        disneyPlusAndroidTVPaywallPage.isRestartSubscriptionScreenOpen();
        UniversalUtils.storeAndUploadSS("RestartSubscriptionScreen", count.addAndGet(1), baseFile.get(), getCastedDriver());
        ZipUtils.zipDirectory(baseFile.get(), pathToZip);
        Artifact.attachToTest(pathToZip, Path.of(pathToZip));
    }

    public void getAllLegalScreens(AtomicInteger count, DisneyPlusAndroidTVLegalPageBase disneyPlusAndroidTVLegalPageBase, DisneyPlusAndroidTVCommonPage disneyPlusAndroidTVCommonPage) {
        String previousTitle = disneyPlusAndroidTVLegalPageBase.getDocumentTitle();
        disneyPlusAndroidTVCommonPage.pressDownAndSelect();
        int counter = 1;
        while (!previousTitle.equals(disneyPlusAndroidTVLegalPageBase.getDocumentTitle())) {
            previousTitle = disneyPlusAndroidTVLegalPageBase.getDocumentTitle();
            UniversalUtils.storeAndUploadSS("LegalDocument" + ++counter, count.addAndGet(1), baseFile.get(), getCastedDriver());
            disneyPlusAndroidTVCommonPage.pressDownAndSelect();
        }
    }

    public void handlePasswordStrength(AtomicInteger count, DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage) {
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        disneyPlusAndroidTVLoginPage.clickPasswordField();
        androidTVUtils.sendInput("a");
        if (AndroidTVUtils.isAmazon()) {
            androidTVUtils.hideKeyboardIfPresent();
        }
        androidTVUtils.hideKeyboardIfPresent();
        UniversalUtils.storeAndUploadSS("PasswordStrengthPoor", count.addAndGet(1), baseFile.get(), getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("123456");
        androidTVUtils.hideKeyboardIfPresent();
        UniversalUtils.storeAndUploadSS("PasswordStrengthPoor", count.addAndGet(1), baseFile.get(), getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("abcdefghi");
        androidTVUtils.hideKeyboardIfPresent();
        UniversalUtils.storeAndUploadSS("PasswordStrengthFair", count.addAndGet(1), baseFile.get(), getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("abcdefghij");
        androidTVUtils.hideKeyboardIfPresent();
        UniversalUtils.storeAndUploadSS("PasswordStrengthGood", count.addAndGet(1), baseFile.get(), getCastedDriver());
        disneyPlusAndroidTVLoginPage.enterPassword("abcdefghijkl123$");
        androidTVUtils.hideKeyboardIfPresent();
        UniversalUtils.storeAndUploadSS("PasswordStrengthGreat", count.addAndGet(1), baseFile.get(), getCastedDriver());
    }

    public void dismissDisclosurePageIfPresent(DisneyPlusAndroidTVLoginPage disneyPlusAndroidTVLoginPage, AtomicInteger count, ExtendedWebElement element) {
        if (disneyPlusAndroidTVLoginPage.isDisclosurePageOpen()) {
            pause(10);
            UniversalUtils.storeAndUploadSS("SubscriberAgreement", count.addAndGet(1), baseFile.get(), getCastedDriver());
            disneyPlusAndroidTVLoginPage.clickConfirmIfPresent();
            disneyPlusAndroidTVLoginPage.handleDisclosurePage(element);
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
