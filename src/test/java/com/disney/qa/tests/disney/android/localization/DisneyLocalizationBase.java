package com.disney.qa.tests.disney.android.localization;

import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.common.jarvis.android.JarvisAndroidBase;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.tests.disney.android.BaseDisneyTest;
import com.qaprosoft.carina.core.foundation.utils.DateUtils;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.IDriverPool;
import org.testng.annotations.DataProvider;

public class DisneyLocalizationBase extends BaseDisneyTest {
    public final ThreadLocal<Integer> count = new ThreadLocal<>();
    public final ThreadLocal<String> baseDirectory = new ThreadLocal<>();
    public final ThreadLocal<String> pathToZip = new ThreadLocal<>();

    @DataProvider
    public Object[] tuidGenerator() {
        languageUtils.set(new DisneyLocalizationUtils(R.CONFIG.get("locale"), R.CONFIG.get("language"), PLATFORM, R.CONFIG.get("capabilities.custom_env"), PARTNER));
        return new String[]{String.format("TUID: %s | %s", languageUtils.get().getCountryName(), languageUtils.get().getUserLanguage())};
    }

    protected void getScreenshots(String fileName) {
        AndroidUtilsExtended util = new AndroidUtilsExtended();
        if (IDriverPool.currentDevice.get().getDeviceType() == DeviceType.Type.ANDROID_TABLET) {
            UniversalUtils.storeAndUploadSS(fileName + "Portrait", count.get(), baseDirectory.get(), getCastedDriver());
            util.setOrientation(AndroidUtilsExtended.Orientations.LANDSCAPE);
            pause(2);
            UniversalUtils.storeAndUploadSS(fileName + "Landscape", count.get(), baseDirectory.get(), getCastedDriver());
            util.setOrientation(AndroidUtilsExtended.Orientations.PORTRAIT);
            pause(2);
        } else {
            UniversalUtils.storeAndUploadSS(fileName, count.get(), baseDirectory.get(), getCastedDriver());
        }
        count.set(count.get() + 1);
    }

    public void setJarvisOverrides() {
        JarvisAndroidBase jarvis = initPage(JarvisAndroidBase.class);
        if (Boolean.parseBoolean(R.CONFIG.get("custom_string2"))) {
            JarvisAndroidBase.Dictionaries.UNPIN_DICTIONARIES.setOverrideValue(true);
        }
        if (Boolean.parseBoolean(R.CONFIG.get("custom_string3"))) {
            JarvisAndroidBase.Localization.GLOBALIZATION_API_VER_1.setOverrideValue(true);
            String version = R.CONFIG.get("custom_string4");
            if (!version.equals("1.x.x")) {
                JarvisAndroidBase.Localization.GLOBALIZATION_API_VER.setOverrideValue(version);
            }
        }
        if(Boolean.parseBoolean(R.CONFIG.get("custom_string5"))) {
            JarvisAndroidBase.Dictionaries.ENABLE_DEBUG_DICTIONARY.setOverrideValue(true);
        }
        jarvis.activateOverrides();
    }

    public void setDirectories(String testName) {
        baseDirectory.set(String.format("Screenshots/%s/%s/", languageUtils.get().getCountryName(), languageUtils.get().getUserLanguage()));
        pathToZip.set(String.format("%s_%s_%s_%s.zip",
                testName,
                languageUtils.get().getCountryName(),
                languageUtils.get().getUserLanguage(),
                DateUtils.now()));
    }
}
