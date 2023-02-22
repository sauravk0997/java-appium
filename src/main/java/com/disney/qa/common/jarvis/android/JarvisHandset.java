package com.disney.qa.common.jarvis.android;

import com.disney.qa.api.jarvis.JarvisParameters;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.Map;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.ANDROID_PHONE, parentClass = JarvisAndroidBase.class)
public class JarvisHandset extends JarvisAndroidBase {

    @FindBy(id = "com.disney.disneyplus.jarvis:id/nav_config_overrides")
    protected ExtendedWebElement navConfig;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/nav_delorean")
    protected ExtendedWebElement navDelorean;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/nav_env_switcher")
    protected ExtendedWebElement navEnvironment;

    @FindBy(id = "com.disney.disneyplus.jarvis:id/bottom_sheet_active_transforms")
    protected ExtendedWebElement activeOverrides;

    AndroidUtilsExtended util;

    public JarvisHandset(WebDriver driver) {
        super(driver);
        util = new AndroidUtilsExtended();
    }

    public void openMenu() {
        getMenuBtn().isElementPresent(LONG_TIMEOUT);
        getMenuBtn().click();
        util.hideKeyboard();
    }

    public void openConfigOverrides() {
        openMenu();
        navConfig.click();
    }

    public void openConfigOverrideGroup(String header) {
        if(!currentGroup.equals(header)) {
            openConfigOverrides();
            util.swipe(getTextElement().format(header));
            int tries = 0;
            boolean groupOpen = getConfigOverrideItem().format(header).isElementPresent(1);
            while (!groupOpen && tries < 2) {
                tries++;
                getTextElement().format(header).click();
                groupOpen = getConfigOverrideItem().format(header).isElementPresent(1);
                if(!groupOpen && getTextElement().format("ACTIVE CONFIG OVERRIDES").isElementPresent(1)) {
                    util.pressBack();
                    util.swipeUp(1, 500);
                }
            }
            currentGroup = header;
        }
    }

    public void openDelorean(){
        if(!currentGroup.equals(DELOREAN)) {
            openMenu();
            navDelorean.click();
            currentGroup = DELOREAN;
        }
    }

    public void launchDisneyPlus() {
        getLaunchDisneyBtn().click();
    }

    public void clickText(String text) {
        getTextElement().format(text).click();
    }

    /**
     * The methods here are used in conjuction with setOverrideValue after the type has been checked.
     * All 3 of them take the same params.
     * @param overrideItem - Item to be changed
     * @param value - Value to change it to
     */
    public void setSwitchValue(String overrideItem, String value) {
        ExtendedWebElement override = getOverrideTransformSwitch().format(overrideItem);
        if(!override.getAttribute("checked").equals(value)) {
            override.click();
        }
    }

    public void setTransformValue(String overrideItem, String value) {
        ExtendedWebElement override = getOverrideTransformValue().format(overrideItem);
        if(!override.getText().equals(value)) {
            override.click();
            util.scroll(value, getTransformContainer()).click();
        }
    }

    public void setCustomValue(String overrideItem, String value) {
        ExtendedWebElement override = getOverrideTransformCustomValue().format(overrideItem);
        if(!override.getText().equals(value)) {
            override.click();
            override.type(value);
            AppiumDriver driver = (AppiumDriver) getCastedDriver();
            driver.executeScript("mobile: performEditorAction", Map.of("action", "done"));
            util.hideKeyboard();
        }
    }

    public void setOverrideValue(String overrideItem, String type, String value) {
        if(!util.swipe(getTextElement().format(overrideItem), 10)) {
            throw new NoSuchElementException(String.format("Override '%s' was not found in the group '%s.", overrideItem, currentGroup));
        }

        switch (type) {
            case SWITCH:
                setSwitchValue(overrideItem, value);
                break;
            case TRANSFORM:
                setTransformValue(overrideItem, value);
                break;
            case CUSTOM:
                setCustomValue(overrideItem, value);
                break;
            default:
                Assert.fail("Override type invalid.");
        }
    }

    public void setDeloreanData(String timeCircuit, String value) {
        getResourceIdElement().format(timeCircuit).type(value);
    }

    public void resetDeloreanData(){
        clickText(DeloreanItems.RESET.getItem());
    }

    public void activateDelorean(){
        util.hideKeyboard();
        clickText(DeloreanItems.TRAVEL.getItem());
    }

    public void setDisneyEnvironment(DisneyEnvironments environment) {
        if(useGUI) {
            launchJarvis();
            openMenu();
            navEnvironment.click();
            selectDisneyEnvironment(environment);
            new AndroidUtilsExtended().swipe(getSwitchEnvBtn());
            getSwitchEnvBtn().click();
        } else {
            String activeEnv = StringUtils.substringBetween(util.executeShell(JarvisParameters.getEnvCheckCommand()), "Active environment: ", "\"");
            if(activeEnv == null || !activeEnv.equals(environment.getEnvSwitchAdbValue())) {
                util.executeShell(JarvisParameters.getEnvSwitchCommand(environment.getEnvSwitchAdbValue()));
            }
        }
    }

    public void selectDisneyEnvironment(DisneyEnvironments environment){
        switch (environment) {
            case PRODUCTION:
                LOGGER.info("Setting environment to PRODUCTION...");
                clickText(DisneyEnvironments.PRODUCTION.getEnvSwitchItem());
                break;
            case QA:
                LOGGER.info("Setting environment to QA...");
                clickText(DisneyEnvironments.QA.getEnvSwitchItem());
                break;
            case EDITORIAL:
                LOGGER.info("Setting environment to EDITORIAL...");
                clickText(DisneyEnvironments.EDITORIAL.getEnvSwitchItem());
                break;
            case STAGING:
                LOGGER.info("Setting environment to STAGING...");
                clickText(DisneyEnvironments.STAGING.getEnvSwitchItem());
                break;
            case STAR_PRODUCTION:
                LOGGER.info("Setting environment to STAR+ PRODUCTION...");
                clickText(DisneyEnvironments.STAR_PRODUCTION.getEnvSwitchItem());
                break;
            case STAR_QA:
                LOGGER.info("Setting environment to STAR+ QA...");
                clickText(DisneyEnvironments.STAR_QA.getEnvSwitchItem());
                break;
            default:
                LOGGER.info("Something broke");
                Assert.fail("Invalid Environment for test");
        }
    }

    public void setStarEnvironment(StarEnvironments environment) {
        openMenu();
        navEnvironment.click();
        switch (environment) {
            case PRODUCTION:
                LOGGER.info("Setting environment to PRODUCTION...");
                clickText(StarEnvironments.PRODUCTION.getEnvSwitchItem());
                break;
            case QA:
                LOGGER.info("Setting environment to QA...");
                clickText(StarEnvironments.QA.getEnvSwitchItem());
                break;
            case EDITORIAL:
                LOGGER.info("Setting environment to EDITORIAL...");
                clickText(StarEnvironments.EDITORIAL.getEnvSwitchItem());
                break;
            case STAGING:
                LOGGER.info("Setting environment to STAGING...");
                clickText(StarEnvironments.STAGING.getEnvSwitchItem());
                break;
            case DISNEY_PRODUCTION:
                LOGGER.info("Setting environment to STAR+ PRODUCTION...");
                clickText(StarEnvironments.DISNEY_PRODUCTION.getEnvSwitchItem());
                break;
            case DISNEY_QA:
                LOGGER.info("Setting environment to STAR+ QA...");
                clickText(StarEnvironments.DISNEY_QA.getEnvSwitchItem());
                break;
            default:
                Assert.fail("Invalid Environment for test");
        }
        getSwitchEnvBtn().click();
    }

    public void clearOverrides() {
        launchJarvis();
        openMenu();
        navConfig.click();
        clickText("ACTIVE CONFIG OVERRIDES & CONFIG JSON");

        while(getDeleteOverrideBtn().isElementPresent(SHORT_TIMEOUT)) {
            getDeleteOverrideBtn().click();
        }
    }
}
