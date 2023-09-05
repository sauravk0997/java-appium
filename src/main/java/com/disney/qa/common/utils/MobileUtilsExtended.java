package com.disney.qa.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.decorators.Decorated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.utils.resources.L10N;
import com.zebrunner.carina.webdriver.IDriverPool;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;

/**
 * Keep this file for possible future extending Android and iOS universal utils creation.
 */

public class MobileUtilsExtended extends UniversalUtils implements IMobileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /*
    This method gets screenshots of the device in use at the time it is called and buils them to a local directory
    on the machine executing the code. This method should only be called when running a job locally due to
    how retrieving the files is a strictly manual process. Calling this method from a Jenkins job
    will render the files unreachable once execution is completed.
     */

    //TODO: In the event we lose access to Carina, we should add an Artifact method to this class to export the files
    public void exportImagesToDirectory(){
        WebDriver driver = getDriver();
        BufferedImage image;
        File srcFile;


        String app = ((HasCapabilities) getDriver()).getCapabilities().getCapability("app").toString();

        for(String val : app.split("/")){
            if(val.contains(".")){
                app = val;
            }
        }

        String lang = "";
        try{
             lang = ((HasCapabilities) getDriver()).getCapabilities().getCapability("language").toString().toUpperCase();
        } catch (NullPointerException e){
            LOGGER.info("No language set in capabilities. Defaulting to ENGLISH.");
            lang = "EN";
        }

        lang = lang.concat(File.separator);

        String deviceName = IDriverPool.getDefaultDevice().getName().replaceAll(" ", "_").concat(File.separator);
        String deviceType = IDriverPool.getDefaultDevice().getDeviceType().toString();
        File directory = new File("SCREENSHOTS" + File.separator + lang + deviceType + File.separator + deviceName + app);
        directory.mkdirs();
        LOGGER.info("Files being written to: " + directory.toString());

        try {
            File screenshotTest = File.createTempFile(deviceType + "_", ".png", directory);
            srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            image = ImageIO.read(srcFile);
            ImageIO.write(image, "png", screenshotTest);
        } catch (IOException e) {
            LOGGER.info("Something went wrong. See log:\n" + e);
        }
    }

    public void wideSwipeRight(int times, int duration){
        WebDriver driver = getDriver();
        org.openqa.selenium.Dimension size = driver.manage().window().getSize();

        int startx = (int)(size.width * 0.1);
        int endx = (int)(size.width * 0.9);
        int y = size.height / 2;

        for(int i = 0; i < times; ++i) {
            LOGGER.info("Swipe right will be executed.");
            swipe(startx, y, endx, y, duration);
        }
    }

    public void wideSwipeLeft(int times, int duration){
        WebDriver driver = getDriver();
        Dimension size = driver.manage().window().getSize();

        int startx = (int)(size.width * 0.9);
        int endx = (int)(size.width * 0.1);
        int y = size.height / 2;

        for(int i = 0; i < times; ++i) {
            LOGGER.info("Swipe left will be executed.");
            swipe(startx, y, endx, y, duration);
        }
    }

    public String getL10Nvalue(String text){
        if (text.contains("{L10N:")) {
            String key = text.replace("{L10N:", "").replace("}", "");
            text = L10N.getText(key);
        }
        return text;
    }

    /**
     * Function takes before/after screenshots that are used to determine if we've reached
     * the bottom of the page. Only swipes once per use. Designed to be done in a loop.
     * Returns a boolean to be referenced.
     */
    public boolean swipeAndCompareBeforeAndAfterPlacement(){
        UniversalUtils universalUtils = new UniversalUtils();
        BufferedImage beforeSwipe = universalUtils.getCurrentScreenView();
        new MobileUtilsExtended().swipeUp(1000);
        BufferedImage afterSwipe = universalUtils.getCurrentScreenView();

        return areImagesDifferent(beforeSwipe, afterSwipe);
    }

    /**
     * Swipes the device back to the top of the display. Stops once the images before and after a swipe
     * are a match. Has a timeout of 50 swipes before continuing
     * @param container - This is a high level container of the app's display which is used
     *                  to compare if the swipe action has reached the top of the screen.
     *                  The container is used to exclude the device tray, which may fluctuate
     *                  rapidly due to the LTE connection display.
     */
    public void returnToTopOfScreen(ExtendedWebElement container){
        BufferedImage beforeSwipe;
        BufferedImage afterSwipe;
        int timeout = 0;
        do {
            UniversalUtils universalUtils = new UniversalUtils();
            beforeSwipe = universalUtils.getElementImage(container);
            new MobileUtilsExtended().swipeDown(1000);
            afterSwipe = universalUtils.getElementImage(container);
            timeout++;
        } while (areImagesDifferent(beforeSwipe, afterSwipe) && timeout < 50);
    }

    /**
     * Returns installed app version based on the filename pulled from AppCenter when the test started.
     * Similar to getInstalledAppVersionFull() except it does not include the build number
     * @return The app version number used in config calls and other displays (ex. 1.16.0)
     */
    @Deprecated
    public String getInstalledAppVersion() {
        String fullBuild = getInstalledAppVersionFull();
        List<String> list = new ArrayList<>(Arrays.asList(fullBuild.split("\\.")));
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<list.size()-1; i++){
            sb.append(list.get(i));
            if(i != list.size()-2){
                sb.append(".");
            }
        }
        return sb.toString();
    }

    /**
     * Returns the full version number of the installed APK or IPA file, depending on if the platform
     * is Apple based or Android based
     * @return - The full build of the installed app version (ex. 1.16.0.12345)
     */
    @Deprecated
    public String getInstalledAppVersionFull() {
        StringBuilder sb = new StringBuilder();

        String build = getDevice().getCapabilities().getCapability("app").toString();

        List<String> raw = new ArrayList<>(Arrays.asList(build.split("/")));
        var deviceType = IDriverPool.currentDevice.get().getDeviceType();
        switch (deviceType) {
            case APPLE_TV:
            case IOS_PHONE:
            case IOS_TABLET:
                raw.removeIf(entry -> !entry.contains(".ipa"));
                break;
            default:
                raw.removeIf(entry -> !entry.contains(".apk"));
        }

        String buildTrim = StringUtils.substringBefore(raw.get(0), "?");

        List<String> list = new ArrayList<>(Arrays.asList(buildTrim.split("\\D+")));
        list.removeAll(Collections.singleton(""));

        for(int i=0; i<list.size()-1; i++){
            sb.append(list.get(i)).append(".");
        }
        sb.append(list.get(list.size()-1));

        return sb.toString();
    }

    /**
     * Clicks at a specific height/width percentage location of an element. Useful for items that are not
     * visible to the driver but are contained within a parent object.
     * @param element - The element being interacted with
     * @param height - The desired height percentage
     * @param width - The desired width percentage
     */
    public void clickElementAtLocation(ExtendedWebElement element, int height, int width) {
        var dimension = element.getSize();
        Point location = element.getLocation();

        int x = (int) Math.round(dimension.getWidth() * Double.parseDouble("." + width));
        int y = (int) Math.round(dimension.getHeight() * Double.parseDouble("." + height));
        tap(location.getX() + x, location.getY() + y);
    }

    public void clickElementAtAbsoluteLocation(ExtendedWebElement element, int height, double width) {
        var dimension = element.getSize();
        var location = element.getLocation();

        double x = (dimension.getWidth() * width);
        int y = (int) Math.round(dimension.getHeight() * Double.parseDouble("." + height));
        tap(location.getX() + (int) x, location.getY() + y);
    }

    public WebDriver getCastedDriver() {
        WebDriver drv = getDriver();
        if (drv instanceof Decorated<?>) {
            drv = (WebDriver) ((Decorated<?>) drv).getOriginal();
        }
        return drv;
    }
}
