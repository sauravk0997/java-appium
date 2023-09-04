package com.disney.qa.tests.disney.apple;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.jarvisutils.parameters.apple.JarvisAppleParameters;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.tests.BaseMobileTest;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.carina.appcenter.AppCenterManager;
import com.zebrunner.carina.utils.DateUtils;
import com.zebrunner.carina.utils.R;

@SuppressWarnings("squid:S2187")
public class DisneyAppleBaseTest extends BaseMobileTest {

    public static final String APPLE = "apple";
    public static final String DISNEY = "disney";
    public static final String APP = "app";
    private static final String TVOS = "tvOS";
    public static final String IOS = "ios";
    protected static final boolean USE_MULTIVERSE = R.CONFIG.getBoolean("useMultiverse");

    protected DisneyBaseTest.BuildType buildType;
    protected Map<String, String> sessionBundles = new HashMap<>();

    //API Threads
    protected ThreadLocal<DisneyContentApiChecker> apiProvider = new ThreadLocal<>();
    protected ThreadLocal<DisneyLocalizationUtils> languageUtils = new ThreadLocal<>();

    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected ThreadLocal<IOSUtils> iosUtils = new ThreadLocal<>();
    protected ThreadLocal<DisneyMobileConfigApi> configApi = new ThreadLocal<>();

    public enum BuildType {
        ENTERPRISE("com.disney.disneyplus.enterprise", JarvisAppleParameters.getEnterpriseBundle()),
        AD_HOC("com.bamtech.dominguez", JarvisAppleParameters.getAdhocBundle()),
        IAP("com.disney.disneyplus", JarvisAppleParameters.getIapBundle());

        private String disneyBundle;
        private String jarvisBundle;

        BuildType(String disneyBundle, String jarvisBundle) {
            this.disneyBundle = disneyBundle;
            this.jarvisBundle = jarvisBundle;
        }

        public String getDisneyBundle() {
            return this.disneyBundle;
        }

        public String getJarvisBundle() {
            return this.jarvisBundle;
        }
    }

    public void setBuildType() {
        sessionBundles.put(APP, getDevice().getCapabilities().getCapability("app").toString());
        LOGGER.info("App Download: {}", sessionBundles.get(APP));
        if(sessionBundles.get(APP).contains("Enterprise")) {
            buildType = BuildType.ENTERPRISE;
            sessionBundles.put(DISNEY, buildType.getDisneyBundle());
            sessionBundles.put(JarvisAppleBase.JARVIS, JarvisAppleParameters.getEnterpriseBundle());
            removeAdHocApps();
            removePurchaseApps();
        } else if (sessionBundles.get(APP).contains("Disney_iOS_AdHoc") || sessionBundles.get(APP).contains("Disney_IAP")) {
            buildType = BuildType.IAP;
            sessionBundles.put(JarvisAppleBase.JARVIS, JarvisAppleParameters.getIapBundle());
            removeEnterpriseApps();
            removeAdHocApps();
        } else {
            buildType = BuildType.AD_HOC;
            sessionBundles.put(JarvisAppleBase.JARVIS, JarvisAppleParameters.getAdhocBundle());
            removeEnterpriseApps();
            removePurchaseApps();
        }
        sessionBundles.put(DISNEY, buildType.getDisneyBundle());
    }

    /**
     * Executes a launchApp command depending on the bundle being used in test.
     */
    public void relaunch() {
        LOGGER.info("Executing relaunch command...");
        iosUtils.get().launchApp(sessionBundles.get(DISNEY));
        pause(3);
    }

    private void removeEnterpriseApps() {
        LOGGER.info("Removing Enterprise apps");
        iosUtils.get().removeApp(BuildType.ENTERPRISE.getDisneyBundle());
        iosUtils.get().removeApp(BuildType.ENTERPRISE.getJarvisBundle());
    }

    private void removeAdHocApps() {
        LOGGER.info("Removing AdHoc apps");
        iosUtils.get().removeApp(BuildType.AD_HOC.getDisneyBundle());
        iosUtils.get().removeApp(BuildType.AD_HOC.getJarvisBundle());
    }

    private void removePurchaseApps() {
        LOGGER.info("Removing Purchase apps");
        iosUtils.get().removeApp(BuildType.IAP.getDisneyBundle());
        iosUtils.get().removeApp(BuildType.IAP.getJarvisBundle());
    }

    protected void installJarvis() {
        String platformName;

        if (getDevice().getCapabilities().getCapability("deviceType").toString().equals(TVOS)) {
            platformName = TVOS;
        } else {
            platformName = getDevice().getCapabilities().getCapability("platformName").toString();
        }

        switch (buildType) {
            case ENTERPRISE:
                iosUtils.get().installApp(AppCenterManager.getInstance().getDownloadUrl("Dominguez-Jarvis-Enterprise", platformName, "enterprise", "latest"));
                break;
            case AD_HOC:
                iosUtils.get().installApp(AppCenterManager.getInstance().getDownloadUrl("Dominguez-Jarvis", platformName, "adhoc", "latest"));
                break;
            case IAP:
                iosUtils.get().installApp(AppCenterManager.getInstance().getDownloadUrl("Disney-Jarvis", platformName, "adhoc", "latest"));
        }
    }

    public String getDate() {
        String date = DateUtils.now();
        return date.replace(":", "_");
    }
}
