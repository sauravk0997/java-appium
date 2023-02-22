package com.disney.qa.tests.disney.android.tv;

import com.disney.charles.CharlesProxy;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.jarvis.JarvisParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.utils.DisneyCountryData;
import com.disney.qa.common.jarvis.android.JarvisAndroidBase;
import com.disney.qa.common.jarvis.android.JarvisAndroidTV;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.tv.*;
import com.disney.qa.disney.android.pages.tv.globalnav.*;
import com.disney.qa.disney.android.pages.tv.pages.*;
import com.disney.qa.tests.BaseAndroidTVTest;
import com.disney.util.disney.ZebrunnerXrayLabels;
import com.fasterxml.jackson.databind.JsonNode;
import com.qaprosoft.appcenter.AppCenterManager;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.qaprosoft.carina.core.foundation.webdriver.TestPhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

public class DisneyPlusAndroidTVBaseTest extends BaseAndroidTVTest {
    public static final ThreadLocal<DisneyAccount> entitledUser = new ThreadLocal<>();
    protected static final String APP_PACKAGE_DISNEY = "com.disney.disneyplus";
    protected static final String APP_PACKAGE_STAR = "com.disney.starplus";
    protected static final String APP_LAUNCH_ACTIVITY = "com.bamtechmedia.dominguez.main.MainActivity";
    public static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final String WATCHLIST_REF_TYPE_SERIES = "seriesId";
    public static final String WATCHLIST_REF_TYPE_MOVIES = "programId";
    protected ThreadLocal<CharlesProxy> charlesProxy = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVMoviesPageBase> disneyPlusAndroidTVMoviesPageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVOriginalsPageBase> disneyPlusAndroidTVOriginalsPageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVProfilePageBase> disneyPlusAndroidTVProfilePageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVSeriesPageBase> disneyPlusAndroidTVSeriesPageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVSettingsPageBase> disneyPlusAndroidTVSettingsPageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVWatchlistPageBase> disneyPlusAndroidTVWatchlistPageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVCompletePurchasePageBase> disneyPlusAndroidTVCompletePurchasePageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVDetailsPageBase> disneyPlusAndroidTVDetailsPageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVForgotPasswordPageBase> disneyPlusAndroidTVForgotPasswordPageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVLegalPageBase> disneyPlusAndroidTVLegalPageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVBrandPage> disneyPlusAndroidTVBrandPage = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVCommonPage> disneyPlusAndroidTVCommonPage = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVDiscoverPage> disneyPlusAndroidTVDiscoverPage = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVLoginPage> disneyPlusAndroidTVLoginPage = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVPaywallPage> disneyPlusAndroidTVPaywallPage = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVVideoPlayerPage> disneyPlusAndroidTVVideoPlayerPage = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVWelcomePage> disneyPlusAndroidTVWelcomePage = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVSearchPage> disneyPlusAndroidTVSearchPage = new ThreadLocal<>();
    protected ThreadLocal<DisneyPlusAndroidTVWelchPageBase> disneyPlusAndroidTVWelchPageBase = new ThreadLocal<>();
    protected ThreadLocal<StarPlusAndroidTVESPNPageBase> starPlusAndroidTVESPNPageBase = new ThreadLocal<>();
    protected ThreadLocal<DisneyContentApiChecker> apiProvider = new ThreadLocal<>();
    protected ThreadLocal<DisneyAccountApi> disneyAccountApi = new ThreadLocal<>();
    protected ThreadLocal<DisneySearchApi> disneySearchApi = new ThreadLocal<>();
    protected ThreadLocal<JsonNode> fullDictionary = new ThreadLocal<>();
    protected ThreadLocal<DisneyLocalizationUtils> disneyLanguageUtils = new ThreadLocal<>();
    protected ThreadLocal<DisneyMobileConfigApi> configApi = new ThreadLocal<>();

    public static final String BRAND_PAGE_LOAD_ERROR = "Brand page should be open.";
    public static final String DATE_OF_BIRTH_PAGE_LOAD_ERROR = "DOB page should be open.";
    public static final String DELETE_PROFILE_PAGE_LOAD_ERROR = "Delete profile page should be open.";
    public static final String DETAILS_PAGE_LOAD_ERROR = "Details page should be open.";
    public static final String EDIT_PROFILE_PAGE_LOAD_ERROR = "Edit profile page should be open.";
    public static final String GLOBAL_NAV_LOAD_ERROR = "Global Nav should be open.";
    public static final String GLOBAL_NAV_COLLAPSE_ERROR = "Global Nav should not be open.";
    public static final String HOME_PAGE_LOAD_ERROR = "Home page should be open.";
    public static final String LANGUAGE_SETTINGS_PAGE_LOAD_ERROR = "Language settings page should be open.";
    public static final String LOGIN_PAGE_LOAD_ERROR = "Login page should be open.";
    public static final String MOVIES_PAGE_LOAD_ERROR = "Movies page should be open.";
    public static final String ORIGINALS_PAGE_LOAD_ERROR = "Originals page should be open.";
    public static final String PASSWORD_PAGE_LOAD_ERROR = "Password page should be open.";
    public static final String PAYWALL_PAGE_LOAD_ERROR = "Paywall page should be open.";
    public static final String PROFILE_PAGE_LOAD_ERROR = "Profile page should be open.";
    public static final String PROFILE_NAME_PAGE_LOAD_ERROR = "Profile name page should be open.";
    public static final String PROFILE_ICON_SELECTION_PAGE_LOAD_ERROR = "Profile icon selection page should be open.";
    public static final String SEARCH_PAGE_LOAD_ERROR = "Search page should be open.";
    public static final String SERIES_PAGE_LOAD_ERROR = "Series page should be open.";
    public static final String SETTINGS_PAGE_LOAD_ERROR = "Settings page should be open.";
    public static final String SIGN_UP_PAGE_LOAD_ERROR = "Sign up page should be open.";
    public static final String VIDEO_PLAYER_PAGE_LOAD_ERROR = "Video player page should be open.";
    public static final String WATCHLIST_PAGE_LOAD_ERROR = "Watchlist page should be open.";
    public static final String WELCOME_PAGE_LOAD_ERROR = "Welcome page should be open.";


    protected JsonNode getApplicationDictionary(String lang) {
        return disneyLanguageUtils.get().getDictionaryBody(lang, DisneyParameters.getApplicationKey(), new HashMap<>(), true);
    }

    protected JsonNode getErrorsDictionary(String lang) {
        return disneyLanguageUtils.get().getDictionaryBody(lang, DisneyParameters.getSdkErrorKey(), new HashMap<>(), true);
    }

    protected JsonNode getPaywallDictionary(String lang) {
        return disneyLanguageUtils.get().getDictionaryBody(lang, DisneyParameters.getPaywallKey(), new HashMap<>(), true);
    }

    public JsonNode getFullDictionary() {
        return disneyLanguageUtils.get().getFullDictionaryBody(language, locale, configApi.get().getDictionaryVersions());
    }

    public void loginWithoutHomeCheck(DisneyAccount account) {
        Assert.assertTrue(disneyPlusAndroidTVWelcomePage.get().isOpened(), "Welcome screen did not launch");
        UniversalUtils.captureAndUpload(getCastedDriver());
        disneyPlusAndroidTVWelcomePage.get().continueToLogin();
        disneyPlusAndroidTVLoginPage.get().proceedToPasswordMode(account.getEmail());
        disneyPlusAndroidTVLoginPage.get().logInWithPassword(account.getUserPass());
    }

    public void login(DisneyAccount disneyAccount) {
        loginWithoutHomeCheck(disneyAccount);
        if (!disneyPlusAndroidTVDiscoverPage.get().isOpened()) {
            if (disneyPlusAndroidTVProfilePageBase.get().isOpened()) {
                disneyPlusAndroidTVProfilePageBase.get().selectDefaultProfileAfterFocused();
            }
        }
        Assert.assertTrue(disneyPlusAndroidTVDiscoverPage.get().isOpened(), "Home page should launch");
    }

    public boolean isJarvisInstalled() {
        return !AndroidService.getInstance().executeShell("pm list packages " + JarvisParameters.getAndroidJarvisPackage()).isEmpty();
    }

    public void setEnvironment() {
        if (!DisneyPlusAndroidTVCommonPage.isProd()) {
            JarvisAndroidTV jarvisAndroidTV = new JarvisAndroidTV((getDriver()));
            if (isJarvisInstalled())
                uninstallApp(JarvisParameters.getAndroidJarvisPackage());
            new AndroidUtilsExtended().installApp(AppCenterManager.getInstance().getDownloadUrl("Disney-Jarvis-1", "android", "jarvis-dev-release", "21-10-22_08:15.21102200"));

            jarvisAndroidTV.launchJarvis();
            jarvisAndroidTV.setDisneyEnvironment(JarvisAndroidBase.DisneyEnvironments.QA);
            jarvisAndroidTV.launchDisneyPlus();
            UniversalUtils.captureAndUpload(getCastedDriver());
        } else {
            getDriver();
            if (isJarvisInstalled()) {
                uninstallApp(JarvisParameters.getAndroidJarvisPackage());
                clearAppCache();
                launchTVapp(APP_PACKAGE_DISNEY, APP_LAUNCH_ACTIVITY);
            }
        }
    }

    public void startProxyAndRestart() {
        if (horaEnabled()) {
            startProxyAndRestart(disneyLanguageUtils.get().getCountryName());
        }
    }

    public void startProxyAndRestart(String country) {
        R.CONFIG.put(BROWSERMOB_PROXY, TRUE, true);
        if (country.contains("United States")) {
            locale = "US";
        }
        restartDriver(true);
        initiateProxy(country);
        if (horaEnabled()) {
            proxy.get().newHar();
        }
        clearAppCache();
    }

    public boolean isConfigLocaleUS() {
        return R.CONFIG.get("locale").equals("US");
    }

    @Parameters({"star-proxy-optional"})
    @BeforeMethod(alwaysRun = true)
    public void setConfig(@Optional("false") String starProxy) {
        LOGGER.info("Star optional value is " + starProxy);
        R.CONFIG.put("forcibly_disable_driver_quit", starProxy);
        setLanguage();
        setEnvironment();
        //set the Language to English due to localization tests
        apiProvider.set(new DisneyContentApiChecker());
        disneyAccountApi.set(new DisneyAccountApi("android", "PROD", PARTNER));
        disneySearchApi.set(new DisneySearchApi("android", "PROD", PARTNER));
        entitledUser.set(disneyAccountApi.get().createAccount("Yearly", country, language, "V1"));
        String version = new AndroidUtilsExtended().getAppVersionName(isStar() ? APP_PACKAGE_STAR : APP_PACKAGE_DISNEY);
        String[] sections = version.split("\\.");
        version = String.format("%s.%s", sections[0], sections[1]);
        configApi.set(new DisneyMobileConfigApi("android-tv", DisneyParameters.getEnv(), PARTNER, version));
        disneyLanguageUtils.set(new DisneyLocalizationUtils(country, language, "android-tv", DisneyParameters.getEnv(), PARTNER));
        disneyLanguageUtils.get().setDictionaries(configApi.get().getDictionaryVersions());
        fullDictionary.set(getFullDictionary());
        setPartnerProxy();
        startProxyAndRestart();
        disneyPlusAndroidTVMoviesPageBase.set(new DisneyPlusAndroidTVMoviesPageBase(getDriver()));
        disneyPlusAndroidTVOriginalsPageBase.set(new DisneyPlusAndroidTVOriginalsPage(getDriver()));
        disneyPlusAndroidTVProfilePageBase.set(new DisneyPlusAndroidTVProfilePageBase(getDriver()));
        disneyPlusAndroidTVSeriesPageBase.set(new DisneyPlusAndroidTVSeriesPageBase(getDriver()));
        disneyPlusAndroidTVSettingsPageBase.set(new DisneyPlusAndroidTVSettingsPageBase(getDriver()));
        disneyPlusAndroidTVWatchlistPageBase.set(new DisneyPlusAndroidTVWatchlistPageBase(getDriver()));
        disneyPlusAndroidTVCompletePurchasePageBase.set(new DisneyPlusAndroidTVCompletePurchasePageBase(getDriver()));
        disneyPlusAndroidTVDetailsPageBase.set(new DisneyPlusAndroidTVDetailsPageBase(getDriver()));
        disneyPlusAndroidTVForgotPasswordPageBase.set(new DisneyPlusAndroidTVForgotPasswordPageBase(getDriver()));
        disneyPlusAndroidTVLegalPageBase.set(new DisneyPlusAndroidTVLegalPageBase(getDriver()));
        disneyPlusAndroidTVBrandPage.set(new DisneyPlusAndroidTVBrandPage(getDriver()));
        disneyPlusAndroidTVCommonPage.set(new DisneyPlusAndroidTVCommonPage(getDriver()));
        disneyPlusAndroidTVDiscoverPage.set(new DisneyPlusAndroidTVDiscoverPage(getDriver()));
        disneyPlusAndroidTVLoginPage.set(new DisneyPlusAndroidTVLoginPage(getDriver()));
        disneyPlusAndroidTVPaywallPage.set(new DisneyPlusAndroidTVPaywallPage(getDriver()));
        disneyPlusAndroidTVVideoPlayerPage.set(new DisneyPlusAndroidTVVideoPlayerPage(getDriver()));
        disneyPlusAndroidTVWelcomePage.set(new DisneyPlusAndroidTVWelcomePage(getDriver()));
        disneyPlusAndroidTVSearchPage.set(new DisneyPlusAndroidTVSearchPage(getDriver()));
        disneyPlusAndroidTVWelchPageBase.set(new DisneyPlusAndroidTVWelchPageBase(getDriver()));
        starPlusAndroidTVESPNPageBase.set(new StarPlusAndroidTVESPNPageBase(getDriver()));
    }

    @AfterMethod(alwaysRun = true)
    public void cleanup() {
        if (isStar()) {
            resetProxy();
            charlesProxy.get().stopProxy();
            quitDrivers(TestPhase.Phase.AFTER_METHOD, TestPhase.Phase.METHOD);
        }
    }

    public void setPartnerProxy() {
        if (isStar()) {
            setProxyToAgent(getDevice().getProxyPort());
            DisneyCountryData countryData = new DisneyCountryData();
            String country = (String) countryData.searchAndReturnCountryData(this.country, "code", "country");
            charlesProxy.set(new CharlesProxy(Integer.parseInt(getDevice().getProxyPort()), null));
            charlesProxy.get().startProxy(country, getDeviceIp());
            clearAppCache();
        }
    }

    public void setPartnerZebrunnerXrayLabels(ZebrunnerXrayLabels xrayLabels) {
        String partner = xrayLabels.getPartner().equalsIgnoreCase("sta") ? "star" : "disney";
        if (partner.equalsIgnoreCase(PARTNER) && !horaEnabled())
            setZebrunnerXrayLabels(xrayLabels);
    }

    public boolean isStar() {
        return PARTNER.equals("star");
    }

    public DisneyAccountApi getAccountApi() {
        return disneyAccountApi.get();
    }
}
