package com.disney.qa.tests.disney.apple.tvos;

import static com.disney.jarvisutils.pages.apple.JarvisAppleBase.*;
import static com.disney.jarvisutils.pages.apple.JarvisAppleTV.Configs.*;
import static com.disney.jarvisutils.pages.apple.JarvisAppleTV.DictionaryResourceKeys.*;
import static com.disney.qa.common.DisneyAbstractPage.TEN_SEC_TIMEOUT;
import static com.disney.qa.common.DisneyAbstractPage.THREE_SEC_TIMEOUT;
import static com.disney.qa.common.constant.IConstantHelper.*;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.utils.DisneySkuParameters;
import com.disney.qa.disney.apple.pages.common.*;
import com.disney.qa.disney.apple.pages.tv.*;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import com.zebrunner.carina.utils.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.jarvisutils.pages.apple.JarvisAppleTV;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.common.utils.UniversalUtils;
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

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        setBuildType();
    }

    public void jarvisOverrideDisableCompanionConfig() {
        boolean isJarvisConfigured = false;
        int jarvisAttempt = 1;
        String isEnabled = "isEnabled";
        JarvisAppleTV jarvis = new JarvisAppleTV(getDriver());
        DisneyPlusApplePageBase appleBase = new DisneyPlusApplePageBase(getDriver());

        while (!isJarvisConfigured && jarvisAttempt < 4) {
            try {
                LOGGER.info("Attempt {} to configure Jarvis", jarvisAttempt);

                launchJarvis(true);

                jarvis.navigateToConfig(APP_CONFIG.getText(), Direction.DOWN);
                jarvis.navigateToConfig(EDIT_CONFIG.getText(), Direction.DOWN);
                jarvis.navigateToConfig(COMPANION_CONFIG.getText(), Direction.DOWN);
                jarvis.navigateToConfig(isEnabled, Direction.DOWN);

                if (appleBase.getStaticTextByLabelContains(JARVIS_OVERRIDE_IN_USE).isPresent(SHORT_TIMEOUT) ||
                        appleBase.getStaticTextByLabelContains(JARVIS_NO_OVERRIDE_IN_USE_TEXT).isPresent(SHORT_TIMEOUT)) {
                    appleBase.moveUp(1, 1);
                    fluentWait(getDriver(), TEN_SEC_TIMEOUT, THREE_SEC_TIMEOUT, "Unable to set IsEnabled flag to 'false'")
                            .until(it -> {
                                appleBase.clickSelect();
                                return appleBase.getStaticTextByLabelContains(JARVIS_NO_OVERRIDE_IN_USE).isPresent(THREE_SEC_TIMEOUT);
                            });
                }
                isJarvisConfigured = true;
                LOGGER.info("Successfully configured Jarvis on attempt {}", jarvisAttempt);
            } catch (Exception e) {
                LOGGER.error("Exception occurred configuring Jarvis on attempt {}", jarvisAttempt);
                e.printStackTrace();
                jarvisAttempt++;
            }
        }

        if(!isJarvisConfigured) {
            throw new RuntimeException("Couldn't configure Jarvis companion flag");
        }
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
        super.installJarvis();
    }

    public void logInWithoutHomeCheck(DisneyAccount user) {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        DisneyPlusAppleTVOneTimePasscodePage disneyPlusAppleTVOneTimePasscodePage = new DisneyPlusAppleTVOneTimePasscodePage(getDriver());
        selectAppleUpdateLaterAndDismissAppTracking();
        Assert.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), WELCOME_SCREEN_NOT_DISPLAYED);
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToLocalizedPasswordScreen(user.getEmail());
        disneyPlusAppleTVOneTimePasscodePage.clickLoginWithPassword();
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
        applePageBase.dismissATVAppTrackingPopUp(5);
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
