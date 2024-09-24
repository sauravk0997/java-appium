package com.disney.qa.tests.disney.apple.tvos;

import static com.disney.jarvisutils.pages.apple.JarvisAppleBase.*;
import static com.disney.jarvisutils.pages.apple.JarvisAppleTV.Configs.*;
import static com.disney.jarvisutils.pages.apple.JarvisAppleTV.DictionaryResourceKeys.*;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.carina.utils.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.jarvisutils.pages.apple.JarvisAppleTV;
import com.disney.config.DisneyParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.appletv.IRemoteControllerAppleTV;

/**
 * Base class for tvos
 */
@SuppressWarnings("squid:S2187")
public class DisneyPlusAppleTVBaseTest extends DisneyBaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String SUB_VERSION = "V1";
    public static final String ENTITLEMENT_LOOKUP = "Yearly";
    protected boolean IS_PROD = DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).equalsIgnoreCase("prod");

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        setBuildType();
    }

    public void addHoraValidationSku(DisneyAccount accountToEntitle) {
        if (Configuration.getRequired(DisneyConfiguration.Parameter.ENABLE_HORA_VALIDATION, Boolean.class)) {
            try {
                getSubscriptionApi().addEntitlementBySku(accountToEntitle, DisneySkuParameters.DISNEY_HORA_VALIDATION, "V2");
            } catch (URISyntaxException | MalformedURLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void installJarvis() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        super.installJarvis();
    }

    public void logInWithoutHomeCheck(DisneyAccount user) {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage disneyPlusAppleTVOneTimePasscodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());
        Assert.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        Assert.assertTrue(disneyPlusAppleTVOneTimePasscodePage.getLoginButtonWithPassword().isPresent(), "Log in option is not present in screen");
        clickElementAtLocation(disneyPlusAppleTVOneTimePasscodePage.getLoginButtonWithPassword(), 35, 50);
        disneyPlusAppleTVOneTimePasscodePage.clickLoginWithPasswordButton();
        disneyPlusAppleTVPasswordPage.logInWithPasswordLocalized(user.getUserPass());
    }

    public void logIn(DisneyAccount user) {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        logInWithoutHomeCheck(user);

        //Wait to handle the expanded validation
        pause(5);
        collapseGlobalNav();

        Assert.assertTrue(homePage.isOpened(),
                "Home page did not launch for single profile user after logging in");
    }

    public void getScreenshots(String filename, String directory) {
        UniversalUtils.storeScreenshot(getDriver(), filename, directory);
    }

    /**
     * Sets jarvis Overrides for localization
     */
    public void setJarvisOverrides() {
        JarvisAppleTV jarvis = new JarvisAppleTV(getDriver());
        boolean unpinDictionaries = Configuration.get(DisneyConfiguration.Parameter.UNPIN_DICTIONARIES, Boolean.class)
                .orElse(false);
        boolean displayDictionaryKeys = Boolean.parseBoolean(R.CONFIG.get("custom_string5"));
        String globalizationVersion = R.CONFIG.get("custom_string4");

        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 0, "Unable to launch Jarvis")
                .until(it -> {
                    LOGGER.info("Jarvis is not launched, launching jarvis...");
                    startApp(sessionBundles.get(JARVIS));
                    pause(1);
                    boolean isRunning = isAppRunning(sessionBundles.get(JARVIS));
                    LOGGER.info("Is app running: {}", isRunning);
                    return isRunning;
                });

        Assert.assertTrue(jarvis.isOpened(), "Jarvis App selection page did not launch");
        jarvis.selectApp(JarvisAppleBase.AppName.TVOS_DISNEY);

        if (!globalizationVersion.isEmpty() && !globalizationVersion.equalsIgnoreCase("null")) {
            LOGGER.info("Setting globalization version");
            jarvis.selectApp(JarvisAppleBase.AppName.TVOS_DISNEY);
            jarvis.clickConfig(APP_CONFIG.getText());
            jarvis.clickConfig(EDIT_CONFIG.getText());
            jarvis.clickConfig(GLOBALIZATION_VERSION.getText());
            jarvis.setOverride(globalizationVersion);
            jarvis.keyPressTimes(IRemoteControllerAppleTV::clickMenu, 3, 1);
        }

        jarvis.navigateToConfig(DICTIONARY_DEBUG_MODE.getText(), Direction.DOWN);
        if (displayDictionaryKeys) {
            LOGGER.info("Enabling Debug Dictionary display...");
            while (!jarvis.getDebugDisplayOverrideValue().equals("key")) {
                jarvis.clickConfig(DICTIONARY_DEBUG_MODE.getText());
            }
        } else {
            LOGGER.info("Disabling Debug Dictionary display...");
            while (!jarvis.getDebugDisplayOverrideValue().equals("off")) {
                jarvis.clickConfig(DICTIONARY_DEBUG_MODE.getText());
            }
        }

        if (unpinDictionaries) {
            LOGGER.info("Unpinning Dictionaries");
            jarvis.clickConfig(DICTIONARY_VERSIONS.getText());
            jarvis.isAIDElementPresentWithScreenshot(DECORATIONS.getText());
            jarvis.setDictionaryKey("0.0");
        }
    }

    /**
     * Below are methods to support temp setup of tvOS tests by disabling flexWelcomeConfig
     * To be deprecated when IOS-7629 is fixed
     */

    public void logInTemp(DisneyAccount user) {
        selectAppleUpdateLaterAndDismissAppTracking();
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        logInWithoutHomeCheck(user);

        //Wait to handle the expanded validation
        pause(5);
        collapseGlobalNav();

        Assert.assertTrue(homePage.isOpened(),
                "Home page did not launch for single profile user after logging in");
    }

    public void selectAppleUpdateLaterAndDismissAppTracking() {
        DisneyPlusApplePageBase applePageBase = new DisneyPlusApplePageBase(getDriver());
        pause(5);
        applePageBase.detectAppleUpdateAndClickUpdateLater();
        applePageBase.dismissAppTrackingPopUp(5);
    }

    public void launchJarvis() {
        DisneyPlusApplePageBase applePageBase = new DisneyPlusApplePageBase(getDriver());
        applePageBase.fluentWait(getDriver(), 30, 0, "Unable to launch Jarvis")
                .until(it -> {
                    LOGGER.info("Jarvis is not launched, launching jarvis...");
                    startApp(sessionBundles.get(JARVIS));
                    pause(1);
                    boolean isRunning = isAppRunning(sessionBundles.get(JARVIS));
                    LOGGER.info("Is app running: {}", isRunning);
                    return isRunning;
                });
    }

    public void installJarvisForConfig() {
        DisneyPlusAppleTVWelcomeScreenPage welcomeScreen = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        welcomeScreen.isOpened();
        super.installJarvis();
    }

    public void collapseGlobalNav() {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        pause(3);
        if (homePage.isGlobalNavExpanded()) {
            LOGGER.warn("Menu was opened before landing. Closing menu.");
            homePage.clickSelect();
        }
    }
}
