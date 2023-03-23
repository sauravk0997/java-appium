package com.disney.qa.tests.disney.apple;

import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.common.jarvis.apple.JarvisAppleBase;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.tests.BaseMobileTest;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.qaprosoft.appcenter.AppCenterManager;
import com.qaprosoft.carina.core.foundation.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("squid:S2187")
public class DisneyAppleBaseTest extends BaseMobileTest {

    public static final String APPLE = "apple";
    public static final String DISNEY = "disney";
    public static final String APP = "app";
    private static final String TVOS = "tvOS";
    public static final String IOS = "ios";

    protected DisneyBaseTest.BuildType buildType;
    protected Map<String, String> sessionBundles = new HashMap<>();

    //API Threads
    protected ThreadLocal<DisneyContentApiChecker> apiProvider = new ThreadLocal<>();
    protected ThreadLocal<DisneyLocalizationUtils> languageUtils = new ThreadLocal<>();

    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected ThreadLocal<IOSUtils> iosUtils = new ThreadLocal<>();
    protected ThreadLocal<DisneyMobileConfigApi> configApi = new ThreadLocal<>();

    public enum BuildType {
        ENTERPRISE("com.disney.disneyplus.enterprise", JarvisAppleBase.JARVIS_ENTERPRISE_BUNDLE),
        AD_HOC("com.bamtech.dominguez", JarvisAppleBase.JARVIS_BUNDLE),
        IAP("com.disney.disneyplus", JarvisAppleBase.JARVIS_IAP_BUNDLE);

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
            sessionBundles.put(JarvisAppleBase.JARVIS, JarvisAppleBase.JARVIS_ENTERPRISE_BUNDLE);
            removeAdHocApps();
            removePurchaseApps();
        } else if (sessionBundles.get(APP).contains("Disney_iOS_AdHoc") || sessionBundles.get(APP).contains("Disney_IAP")) {
            buildType = BuildType.IAP;
            sessionBundles.put(JarvisAppleBase.JARVIS, JarvisAppleBase.JARVIS_IAP_BUNDLE);
            removeEnterpriseApps();
            removeAdHocApps();
        } else {
            buildType = BuildType.AD_HOC;
            sessionBundles.put(JarvisAppleBase.JARVIS, JarvisAppleBase.JARVIS_BUNDLE);
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
        iosUtils.get().removeApp(BuildType.ENTERPRISE.getDisneyBundle());
        iosUtils.get().removeApp(BuildType.ENTERPRISE.getJarvisBundle());
    }

    private void removeAdHocApps() {
        iosUtils.get().removeApp(BuildType.AD_HOC.getDisneyBundle());
        iosUtils.get().removeApp(BuildType.AD_HOC.getJarvisBundle());
    }

    private void removePurchaseApps() {
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
