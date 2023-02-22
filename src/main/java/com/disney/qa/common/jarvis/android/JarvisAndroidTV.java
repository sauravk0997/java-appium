package com.disney.qa.common.jarvis.android;

import com.disney.qa.api.jarvis.JarvisParameters;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * Backup code has already been written and is stored in backup branch QAA-7338-AlternateJarvisAndroidTVControls
 * which can be applied in the event mobile interface code is no longer sufficient for this device type.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_TV, parentClass = JarvisHandset.class)
public class JarvisAndroidTV extends JarvisHandset {

    @FindBy(id = "com.disney.disneyplus.jarvis:id/launch_disney")
    private ExtendedWebElement disneyLaunchBtn;

    @FindBy(xpath = "//*[@resource-id = 'com.disney.disneyplus.jarvis:id/available_transforms_recycler_view']" +
            "/*[@focused= 'true']" +
            "/*[@resource-id = 'com.disney.disneyplus.jarvis:id/group_title' and @text = '%s']")
    private ExtendedWebElement configOverrideOption;

    @FindBy(xpath = "//*[@resource-id = 'com.disney.disneyplus.jarvis:id/transform_item_root' and @focused= 'true']" +
            "/*[@resource-id = 'com.disney.disneyplus.jarvis:id/transform_name' and @text = '%s']")
    private ExtendedWebElement configOverrideSubOption;

    public JarvisAndroidTV(WebDriver driver) {
        super(driver);
    }

    @Override
    public void setDisneyEnvironment(DisneyEnvironments environment) {
        if(useGUI) {
            launchJarvis();
            openMenu();
            UniversalUtils.captureAndUpload(getCastedDriver());
            navEnvironment.click();
            UniversalUtils.captureAndUpload(getCastedDriver());
            selectDisneyEnvironment(environment);
            UniversalUtils.captureAndUpload(getCastedDriver());
            AndroidTVUtils androidTVUtils = new AndroidTVUtils(getCastedDriver());
            DisneyPlusCommonPageBase.fluentWait(getCastedDriver(), LONG_TIMEOUT, 1, "UNABLE TO FIND CONFIRM BUTTON")
                    .until(it -> {
                        androidTVUtils.pressDown();
                        return getSwitchEnvBtn().isElementPresent(ONE_SEC_TIMEOUT);
                    });
            UniversalUtils.captureAndUpload(getCastedDriver());
            getSwitchEnvBtn().click();
        } else {
            String activeEnv = StringUtils.substringBetween(util.executeShell(JarvisParameters.getEnvCheckCommand()), "Active environment: ", "\"");
            if(!activeEnv.equals(environment.getEnvSwitchAdbValue())) {
                String output = util.executeShell(JarvisParameters.getEnvSwitchCommand(environment.getEnvSwitchAdbValue()));
                if(output.contains("Launching an activity is required")) {
                    util.executeShell(JarvisParameters.getOldEnvSwitchCommand(environment.getEnvSwitchAdbValue()));
                }
            }
        }
    }

    @Override
    public void launchDisneyPlus() {
        disneyLaunchBtn.click();
    }

    public static DisneyEnvironments getJarvisDisneyEnvironment(String environment) {
        switch (environment) {
            case "Production":
                return DisneyEnvironments.PRODUCTION;
            case "QA":
                return DisneyEnvironments.QA;
            case "Editorial":
                return DisneyEnvironments.EDITORIAL;
            case "Staging":
                return DisneyEnvironments.STAGING;
            default:
                throw new IllegalArgumentException("Incorrect Jarvis D+ environment provided");
        }
    }

    public void selectOptionAndSubOption(String option, String subOption){
        openMenu();
        UniversalUtils.captureAndUpload(getCastedDriver());
        navConfig.click();
        AndroidTVUtils androidTVUtils = new AndroidTVUtils(getDriver());
        DisneyPlusCommonPageBase.fluentWait(getDriver(), (long) LONG_TIMEOUT * 5, 0,
                String.format("Unable to find %s option", option))
                .until(it -> {
                    androidTVUtils.pressDown();
                    return configOverrideOption.format(option).isElementPresent(1);
                });
        androidTVUtils.pressSelect();
        DisneyPlusCommonPageBase.fluentWait(getDriver(), LONG_TIMEOUT, 0,
                String.format("Unable to find %s sub option", subOption))
                .until(it -> {
                    androidTVUtils.pressDown();
                    return configOverrideSubOption.format(subOption).isElementPresent(1);
                });
        androidTVUtils.pressSelect();
    }

    public void setGlobalizationAPIVersionDefault() {
        selectOptionAndSubOption("Localization", "Globalization API 1.x.x");
    }

    public void unpinDictionaries(){
        selectOptionAndSubOption("Dictionaries", "Unpin Dictionaries");
    }

    @Override
    public void clearOverrides() {
        AndroidService.getInstance().executeAdbCommand("shell pm clear " + JarvisParameters.getAndroidJarvisPackage());
    }
}
