package com.disney.qa.tests;

import com.disney.listeners.MultiverseDevicesListener;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.search.DisneySearchApi;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.utility.Environments;
import com.nordstrom.automation.testng.LinkedListeners;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.zebrunner.agent.core.registrar.Screenshot;
import io.appium.java_client.ComparesImages;
import io.appium.java_client.imagecomparison.SimilarityMatchingOptions;
import io.appium.java_client.imagecomparison.SimilarityMatchingResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@LinkedListeners(MultiverseDevicesListener.class)
public class BaseAndroidTVTest extends BaseMobileTest {
    protected static final String PLAY_STORE_PACKAGE = "com.android.vending";
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected String country = R.CONFIG.get("locale");
    protected String language = R.CONFIG.get("language");
    protected String tizenIp = R.TESTDATA.get("mcloud_tv_agent_ip");
    ThreadLocal<DisneyAccountApi> disneyAccountApi = new ThreadLocal<>();
    ThreadLocal<DisneySearchApi> disneySearchApi = new ThreadLocal<>();

    public DisneyAccountApi getAccountApi() {
        if (disneyAccountApi.get() == null) {
            disneyAccountApi.set(new DisneyAccountApi("android", DisneyParameters.getEnv(), PARTNER));
        }
        return disneyAccountApi.get();
    }

    public DisneySearchApi getSearchApi() {
        if (disneySearchApi.get() == null) {
            disneySearchApi.set(new DisneySearchApi("android", DisneyParameters.getEnv(), PARTNER));
        }
        return disneySearchApi.get();
    }

    public void uninstallApp(String packageName) {
        AndroidService.getInstance().executeAdbCommand("uninstall " + packageName);
    }

    public void setProxyToAgent(String port) {
        AndroidService.getInstance()
                .executeShell(String.format("settings put global http_proxy %s:%s", tizenIp, port));
    }

    public void resetProxy() {
        AndroidService.getInstance()
                .executeShell("settings put global http_proxy :0");
    }

    /**
     * Returns device IP for devices connected via Wi-Fi
     */
    public String getDeviceIp() {
        String command = "ip addr show wlan0  | grep 'inet ' | cut -d ' ' -f 6 | cut -d / -f 1";
        String ip = AndroidService.getInstance().executeShell(command);
        return StringUtils.isNotBlank(ip) ? ip.trim() : AndroidService.getInstance().executeShell(command.replace("wlan0", "eth0")).trim();
    }

    public void clearCache(String packageName) {
        AndroidService.getInstance().executeShell("pm clear " + packageName);
    }

    /**
     * This method will be deprecated once all languages are handled in the new {@link #setSystemLanguage(String, String)} method
     */
    public void setLanguage() {
        switch (country) {
            case "US":
                if (language.equals("es")) language = "es-419";
                break;
            case "GB":
            case "ES":
            case "FR":
            case "CA":
                if (language.equals("en"))
                    break;
            case "PT":
            case "BR":
                language = language.concat("-") + country;
                break;
            default:
                break;
        }
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

    protected void launchTVapp(String appPackage, String appLaunchActivity) {
        LOGGER.info("Launching app..");
        UniversalUtils.captureAndUpload(getCastedDriver());
        AndroidService launchInstance = AndroidService.getInstance();
        launchInstance.executeAdbCommand("shell am start -n " + appPackage.trim() +
                "/" + appLaunchActivity.trim());
    }

    protected void resetSystemLangToEnglish() {
        AndroidService.getInstance().executeAdbCommand("shell am broadcast -a io.appium.settings.locale -n io.appium.settings/.receivers.LocaleSettingReceiver --es lang en --es country US");
    }

    protected void switchEnv(Environments environment) {
        AndroidService.getInstance().executeAdbCommand("shell am broadcast -a com.disney.disneyplus.jarvis.environment -n " +
                "com.disney.disneyplus.jarvis/.environment.EnvironmentReceiver --es environment " + environment.getEnvironment());
        pause(10);
    }

    /**
     * @param path  - Expected Image
     * @param image - Actual Image
     * @param score - Score of the resulting match, max is 1.0
     * @return
     */
    protected boolean compareImages(String path, BufferedImage image, double score) {
        File file = new File(path);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] actualScreenshot;
        try {
            ImageIO.write(image, "png", byteArrayOutputStream);
            actualScreenshot = Base64.getEncoder().encode(FileUtils.readFileToByteArray(file));
            byte[] expectedScreenshot = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
            LOGGER.info("Screenshot is taken ...");
            SimilarityMatchingResult result = ((ComparesImages) getCastedDriver()).getImagesSimilarity(expectedScreenshot,
                    actualScreenshot, new SimilarityMatchingOptions().withEnabledVisualization());
            Screenshot.upload(Base64.getDecoder().decode(result.getVisualization()), System.currentTimeMillis());
            LOGGER.info(String.valueOf(result.getScore()));
            return result.getScore() > score;
        } catch (IOException e) {
            throw new RuntimeException("File not found", e);
        }
    }

    protected boolean compareImages(byte[] expectImage, BufferedImage image, double score) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] actualScreenshot;
        try {
            ImageIO.write(image, "png", byteArrayOutputStream);

        } catch (IOException e) {
            LOGGER.info("File not found " + e);
        }
        actualScreenshot = Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
        SimilarityMatchingResult result = ((ComparesImages) getCastedDriver()).getImagesSimilarity(expectImage,
                actualScreenshot, new SimilarityMatchingOptions().withEnabledVisualization());
        Screenshot.upload(Base64.getDecoder().decode(result.getVisualization()), System.currentTimeMillis());
        LOGGER.info(String.valueOf(result.getScore()));
        return result.getScore() > score;
    }

    protected List<File> getAllFilesFromDirectory(String directory) {
        try {
            return Files.walk(Paths.get(directory))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Folder not found with Exception: " + e);
        }
    }

}
