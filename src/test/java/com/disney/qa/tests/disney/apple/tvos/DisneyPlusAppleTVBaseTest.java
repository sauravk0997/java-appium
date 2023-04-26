package com.disney.qa.tests.disney.apple.tvos;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.config.DisneyMobileConfigApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.disney.DisneyContentApiChecker;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.pojos.ApiConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.api.tvos.*;
import com.disney.jarvisutils.pages.apple.JarvisAppleBase;
import com.disney.jarvisutils.pages.apple.JarvisAppleTV;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVHomePage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVLoginPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVPasswordPage;
import com.disney.qa.disney.apple.pages.tv.DisneyPlusAppleTVWelcomeScreenPage;
import com.disney.qa.tests.disney.apple.DisneyAppleBaseTest;
import com.qaprosoft.carina.core.foundation.api.http.HttpResponseStatusType;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.appletv.IRemoteControllerAppleTV;
import io.appium.java_client.ComparesImages;
import io.appium.java_client.imagecomparison.*;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions.VideoQuality;
import io.restassured.path.json.JsonPath;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import static com.disney.jarvisutils.pages.apple.JarvisAppleBase.JARVIS;
import static com.disney.jarvisutils.pages.apple.JarvisAppleTV.Configs.*;
import static com.disney.jarvisutils.pages.apple.JarvisAppleTV.DictionaryResourceKeys.DECORATIONS;
import static com.disney.qa.disney.apple.pages.tv.AppleTVConstants.*;
import static org.testng.Assert.fail;

@SuppressWarnings("squid:S2187")
public class DisneyPlusAppleTVBaseTest extends DisneyAppleBaseTest {

    public static final String SUB_VERSION = "V1";
    public static final String ENTITLEMENT_LOOKUP = "Yearly";
    public static final String PLATFORM = "apple";
    public static final String PARTNER = "disney";
    protected static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected String language = R.CONFIG.get("language");
    protected String country = R.CONFIG.get("locale");
    protected boolean IS_PROD = DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).equalsIgnoreCase("prod");

    protected DisneySearchApi searchApi;
    protected DisneyAccountApi disneyAccountApi;


    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {
        getDriver();
        iosUtils.set(new IOSUtils());
        setBuildType();
        languageUtils.set(new DisneyLocalizationUtils(country, language, "tvos", "prod", PARTNER));
        String version = iosUtils.get().getInstalledAppVersion();
        configApi.set(new DisneyMobileConfigApi("tvos", "prod", PARTNER, version));
        languageUtils.get().setDictionaries(configApi.get().getDictionaryVersions());
        languageUtils.get().setLegalDocuments();
        DisneyPlusApplePageBase.setDictionary(languageUtils.get());
        apiProvider.set(new DisneyContentApiChecker());
        searchApi = new DisneySearchApi(PLATFORM, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()), apiProvider.get().getPartner());
        disneyAccountApi =  new DisneyAccountApi(getApiConfiguration(apiProvider.get().getPartner()));
    }

    public void installJarvis() {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        disneyPlusAppleTVWelcomeScreenPage.dismissUnexpectedErrorAlert();
        disneyPlusAppleTVWelcomeScreenPage.isOpened();
        super.installJarvis();
    }

    public void verifyAppleTVStatus() {
        String sessionWDA = getSessionWDA();
        if (isAppleTVLocked(sessionWDA)) {
            LOGGER.info("The device is lock!");
            unlockAppleTv(sessionWDA);
            pause(10);
            if (isAppleTVLocked(sessionWDA)) {
                fail("The display is not available. Please connect a display to Apple TV and try again!");
            } else {
                LOGGER.info("The device is unlocked!");
            }
        } else {
            LOGGER.info("The device is active!");
        }
    }

    public void getAppStatus(String sessionWDA, String bundleId) {
        switch (getAppState(sessionWDA, bundleId)) {
            case "1":
                LOGGER.info("The application is not running");
                break;
            case "2":
                LOGGER.info("The application is running in the background and is suspended");
                launchApp(sessionWDA, bundleId);
                terminateApp(sessionWDA, bundleId);
                break;
            case "3":
                LOGGER.info("The application is running in the background and is not suspended");
                launchApp(sessionWDA, bundleId);
                terminateApp(sessionWDA, bundleId);
                break;
            case "4":
                LOGGER.info("The application is running in the foreground");
                terminateApp(sessionWDA, bundleId);
                break;
            default:
                LOGGER.info("The current application state cannot be determined/is unknown");
                break;
        }
    }

    //TODO This is used to upload a video to your local computer. Use Carina to upload data to a remote server
    public void startScreenRecording() {
        IOSStartScreenRecordingOptions options = new IOSStartScreenRecordingOptions()
                .withVideoQuality(VideoQuality.MEDIUM).withVideoType("libx264").withTimeLimit(Duration.ofSeconds(600));
        IOSDriver driver = (IOSDriver) ((EventFiringWebDriver) getDriver()).getWrappedDriver();
        driver.startRecordingScreen(options);
        LOGGER.info("Starting screen capture ...");
    }

    //TODO This is used to upload a video to your local computer. Use Carina to upload data to a remote server
    public void stopScreenRecording() {
        IOSDriver driver = (IOSDriver) ((EventFiringWebDriver) getDriver()).getWrappedDriver();
        String videoBase64 = driver.stopRecordingScreen();
        String pathToSaved = String.format(DEFAULT_VIDEO_PATH, "VideoRecording" + new Date().getTime());
        decodeBase64StringToVideo(videoBase64, pathToSaved);
        LOGGER.info("Screen recording is stopped ...");
    }

    public String getSessionWDA() {
        GetStatusWDAMethod getStatusWDAMethod = new GetStatusWDAMethod();
        getStatusWDAMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        String sessionWDA = JsonPath.from(getStatusWDAMethod.callAPI().asString()).getString("sessionId");
        LOGGER.info("Session WDA = " + sessionWDA);
        return sessionWDA;
    }

    public void unlockAppleTv(String sessionWDA) {
        PostUnlockMethod postMenuBtn = new PostUnlockMethod(sessionWDA);
        postMenuBtn.expectResponseStatus(HttpResponseStatusType.OK_200);
        postMenuBtn.callAPI();
        LOGGER.info("Unlocking Apple TV ...");
    }

    public boolean isAppleTVLocked(String sessionWDA) {
        GetScreenshotMethod getScreenshotMethod = new GetScreenshotMethod(sessionWDA);
        getScreenshotMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        String screenshotValue = JsonPath.from(getScreenshotMethod.callAPI().asString()).getString("value");
        return screenshotValue.contains(LOCKED_DEVICE_SCREENSHOT);
    }

    public String getBundleId(String sessionWDA) {
        GetSessionWDAMethod getSessionWDAMethod = new GetSessionWDAMethod(sessionWDA);
        getSessionWDAMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        String bundleID = JsonPath.from(getSessionWDAMethod.callAPI().asString())
                .getString("value.capabilities.CFBundleIdentifier");
        LOGGER.info("BundleID = " + bundleID);
        return bundleID;
    }

    public String getAppState(String sessionWDA, String bundleID) {
        PostQueryAppStateMethod postQueryAppStateMethod = new PostQueryAppStateMethod(sessionWDA, bundleID);
        postQueryAppStateMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        String appStateStatus = JsonPath.from(postQueryAppStateMethod.callAPI().asString()).getString("value");
        return appStateStatus;
    }

    public void terminateApp(String sessionWDA, String bundleID) {
        PostTerminateAppMethod terminateAppMethod = new PostTerminateAppMethod(sessionWDA, bundleID);
        terminateAppMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        terminateAppMethod.callAPI();
        LOGGER.info("The application is terminated!");
    }

    public void launchApp(String sessionWDA, String bundleID) {
        PostActivateAppMethod postActivateAppMethod = new PostActivateAppMethod(sessionWDA, bundleID);
        postActivateAppMethod.expectResponseStatus(HttpResponseStatusType.OK_200);
        postActivateAppMethod.callAPI();
        LOGGER.info("The application is activated!");
    }

    public void lockDevice() {
        IOSDriver driver = (IOSDriver) getCastedDriver();
        driver.lockDevice();
    }

    public void decodeBase64StringToImage(String encodedString, String imageName) {
        byte[] base64Decoded = Base64.getMimeDecoder().decode(encodedString);
        decodeBase64StringToImage(base64Decoded, imageName);
    }

    public String decodeBase64StringToImage(byte[] encodedString, String imageName) {
        byte[] base64Decoded = Base64.getMimeDecoder().decode(encodedString);
        String pathToSaved = String.format(DEFAULT_IMAGE_PATH, imageName + new Date().getTime());
        File targetFile = new File(pathToSaved);
        BufferedImage image;
        try {
            image = ImageIO.read(new ByteArrayInputStream(base64Decoded));
            ImageIO.write(image, "png", targetFile);
            LOGGER.info(String.format("Image is saved to file %s", pathToSaved));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pathToSaved;
    }

    public void decodeBase64StringToVideo(String encodedString, String pathToSaved) {
        byte[] decodedBytes = Base64.getMimeDecoder().decode(encodedString);
        try {
            FileOutputStream out = new FileOutputStream(pathToSaved);
            out.write(decodedBytes);
            out.close();
            LOGGER.info(String.format("Video is saved to file %s", pathToSaved));
        } catch (IOException e) {
            LOGGER.info("Failed to write video to file " + e);
        }
    }

    public FeaturesMatchingResult verificationTwoScreenshots(String pathToImage) {
        File file = new File(pathToImage);
        byte[] originalScreenshot = null;
        try {
            originalScreenshot = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            LOGGER.info("Couldn't encode a file", e);
        }
        byte[] expectedScreenshot = Base64.getEncoder()
                .encode(((RemoteWebDriver) ((EventFiringWebDriver) getDriver()).getWrappedDriver())
                        .getScreenshotAs(OutputType.BYTES));
        LOGGER.info("Screenshot is taken ...");
        FeaturesMatchingResult result = ((ComparesImages) getCastedDriver()).matchImagesFeatures(expectedScreenshot,
                originalScreenshot,
                new FeaturesMatchingOptions().withDetectorName(FeatureDetector.ORB).withGoodMatchesFactor(2)
                        .withMatchFunc(MatchingFunction.BRUTE_FORCE).withEnabledVisualization());
        decodeBase64StringToImage(result.getVisualization(), "/CompareScreenshots");
        return result;
    }

    public boolean isScreenshotContainsImage(String pathToImage) {
        File file = new File(pathToImage);
        byte[] partialImage = null;
        try {
            partialImage = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            LOGGER.info("Couldn't encode a file", e);
        }
        byte[] screenshot = Base64.getEncoder()
                .encode(((RemoteWebDriver) ((EventFiringWebDriver) getDriver()).getWrappedDriver())
                        .getScreenshotAs(OutputType.BYTES));
        LOGGER.info("Screenshot is taken ...");
        OccurrenceMatchingResult result = ((ComparesImages) getCastedDriver()).findImageOccurrence(screenshot,
                partialImage, new OccurrenceMatchingOptions().withEnabledVisualization());
        decodeBase64StringToImage(result.getVisualization(), "result/ResultScreehshot");
        return !result.getRect().equals(null);
    }

    public void takeScreenshot() {
        File screenFile = ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.FILE);
        LOGGER.info("Screenshot is taken ...");
        try {
            String fileName = String.format(DEFAULT_IMAGE_PATH, "Screenshot" + new Date().getTime());
            FileUtils.copyFile(screenFile, new File(fileName));
            LOGGER.info(String.format("Screenshot is saved to file %s", fileName));
        } catch (IOException e) {
            LOGGER.info("Failed to write screenshot to file ", e);
        }
    }

    public WebDriver getCastedDriver() {
        WebDriver drv = getDriver();
        if (drv instanceof EventFiringWebDriver) {
            return ((EventFiringWebDriver) drv).getWrappedDriver();
        } else {
            return drv;
        }
    }

    public void logInWithoutHomeCheck(DisneyAccount user) {
        DisneyPlusAppleTVWelcomeScreenPage disneyPlusAppleTVWelcomeScreenPage = new DisneyPlusAppleTVWelcomeScreenPage(getDriver());
        DisneyPlusAppleTVLoginPage disneyPlusAppleTVLoginPage = new DisneyPlusAppleTVLoginPage(getDriver());
        DisneyPlusAppleTVPasswordPage disneyPlusAppleTVPasswordPage = new DisneyPlusAppleTVPasswordPage(getDriver());
        Assert.assertTrue(disneyPlusAppleTVWelcomeScreenPage.isOpened(), "Welcome screen did not launch");
        disneyPlusAppleTVWelcomeScreenPage.clickLogInButton();
        disneyPlusAppleTVLoginPage.proceedToPasswordScreen(user.getEmail());
        disneyPlusAppleTVPasswordPage.logInWithPassword(user.getUserPass());
    }

    public void logIn(DisneyAccount user) {
        DisneyPlusAppleTVHomePage homePage = new DisneyPlusAppleTVHomePage(getDriver());
        logInWithoutHomeCheck(user);

        if (homePage.isGlobalNavExpanded()) {
            LOGGER.warn("Menu was opened before landing. Closing menu.");
            homePage.clickSelect();
        }

        Assert.assertTrue(homePage.isOpened(),
                "Home page did not launch for single profile user after logging in");
    }

    public void setSystemLanguage(String language, String country) {
        if (language.contains("-")) {
            String[] languageList = language.split("-");
            String desiredLanguage = languageList[0];
            if (desiredLanguage.equalsIgnoreCase("zh") || (desiredLanguage.equalsIgnoreCase("es")
                    && languageList[1].equalsIgnoreCase("419"))) {
                R.CONFIG.put("language", desiredLanguage);
                R.CONFIG.put("locale", country);
            } else {
                R.CONFIG.put("language", desiredLanguage);
                R.CONFIG.put("locale", languageList[1]);
            }
        } else {
            R.CONFIG.put("language", language);
            R.CONFIG.put("locale", country);
        }
    }

    public String handleAppLanguage(String language) {
        switch (language) {
            case "en-GB":
            case "es-419":
            case "es-ES":
            case "fr-CA":
            case "fr-FR":
            case "pt-BR":
            case "zh-hans":
            case "zh-hant":
            case "zh-HK":
                return language;
            default:
                if (language.contains("-")) {
                    return language.split("-")[0];
                } else {
                    return language;
                }
        }
    }

    public void getScreenshots(String filename, ThreadLocal<String> directory) {
        UniversalUtils.storeScreenshot(getCastedDriver(), filename, directory.get());
    }

    /**
     * Sets jarvis Overrides for localization
     */
    public void setJarvisOverrides() {
        JarvisAppleTV jarvis = new JarvisAppleTV(getDriver());
        boolean unpinDictionaries = Boolean.parseBoolean(R.CONFIG.get("custom_string"));
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
            while(!jarvis.getDebugDisplayOverrideValue().equals("key")) {
                jarvis.clickConfig(DICTIONARY_DEBUG_MODE.getText());
            }
        } else {
            LOGGER.info("Disabling Debug Dictionary display...");
            while(!jarvis.getDebugDisplayOverrideValue().equals("off")) {
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

    public ApiConfiguration getApiConfiguration(String partner) {
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                .platform(APPLE)
                .environment(DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).toLowerCase())
                .partner(partner)
                .useMultiverse(USE_MULTIVERSE)
                .build();
        return apiConfiguration;
    }
}