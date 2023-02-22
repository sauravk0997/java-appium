package com.disney.qa.common.utils;

import com.disney.util.ZipUtils;
import com.qaprosoft.carina.core.foundation.report.ReportContext;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.IDriverPool;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.agent.core.registrar.Artifact;
import com.zebrunner.agent.core.registrar.Screenshot;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Range;
import org.imgscalr.Scalr;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class UniversalUtils implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String ERROR_IMAGE_CAPTURE = "Something went wrong capturing the image.\n";

    /**
     * Gets the element back as a Buffered Image
     * @param element   - The element you need an Image of
     * @return          - The element as a BufferedImage
     */
    public BufferedImage getElementImage(ExtendedWebElement element) {
        WebDriver driver = getDriver();
        BufferedImage fullImage = null;

        new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(element.getBy()));
        File srcFile = driver.findElement(element.getBy()).getScreenshotAs(OutputType.FILE);
        try {
            fullImage = ImageIO.read(srcFile);

            //Debug Tools. Uncomment for local testing
//            String deviceName = IDriverPool.getDefaultDevice().getName().replaceAll(" ", "_").concat(File.separator);
//            String deviceType = IDriverPool.getDefaultDevice().getDeviceType().toString();
//            File directory = new File("SCREENSHOTS" + File.separator + "EN" + File.separator + deviceType + File.separator + deviceName + "DISNEY");
//            directory.mkdirs();
//            File screenshotFile = File.createTempFile("FULL_" + deviceType + "_", ".png", directory);
//            ImageIO.write(fullImage, "png", screenshotFile);
        } catch (IOException e) {
            LOGGER.error(ERROR_IMAGE_CAPTURE, e);
        }
        return fullImage;
    }

    /**
     * An alternate to when elements become stale
     *
     * @param point     - Location of element
     * @param eleWidth  - Width of element
     * @param eleHeight - height of element
     * @return - cropped image
     */
    public BufferedImage getElementImage(Point point, int eleWidth, int eleHeight) {
        WebDriver driver = getDriver();
        BufferedImage fullImage;
        BufferedImage eleScreenshot = null;
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            fullImage = ImageIO.read(srcFile);

            // Crop the entire page screenshot to get only element screenshot
            eleScreenshot = fullImage.getSubimage(point.getX(), point.getY(),
                    eleWidth, eleHeight);
        } catch (IOException e) {
            LOGGER.error(ERROR_IMAGE_CAPTURE, e);
        }
        return eleScreenshot;
    }

    public BufferedImage getCurrentScreenView() {
        WebDriver driver = getDriver();
        BufferedImage image = null;
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            image = ImageIO.read(srcFile);
        } catch (IOException e) {
            LOGGER.error(ERROR_IMAGE_CAPTURE, e);
        }
        return image;
    }

    /**
     * Returns the passed image scaled to the desired dimensions using ULTRA_QUALITY to retain the RGB values as much as possible
     * @param image         - The image to be scaled
     * @param targetWidth   - Desired width
     * @param targetHeight  - Desired height
     * @return              - Scaled image
     */
    public BufferedImage getScaledImage(BufferedImage image, int targetWidth, int targetHeight) {
        return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight);
    }

    public static boolean areImagesDifferent(BufferedImage beforeChange, BufferedImage afterChange) {
        if (beforeChange.getWidth() == afterChange.getWidth() && beforeChange.getHeight() == afterChange.getHeight()) {
            for (int x = 0; x < beforeChange.getWidth(); x++) {
                for (int y = 0; y < beforeChange.getHeight(); y++) {
                    if (beforeChange.getRGB(x, y) != afterChange.getRGB(x, y))
                        return true;
                }
            }
        }
        LOGGER.info("Image Sizes are different. Comparison will be false");
        return false;
    }

    /**
     * Returns a boolean value comparing if the total fail percentage of the scan exceeds the passed margin of error.
     * Images must be the same dimensions.
     *
     * @param image1        - One of the images to compare
     * @param image2        - One of the images to compare
     * @param marginOfError - The maximum percentage allowed for errors to exist (to account for overlays)
     * @return              - totalFailPercent < marginOfError
     */
    public boolean areImagesTheSame(BufferedImage image1, BufferedImage image2, int marginOfError) {
        List<Boolean> passFail = new LinkedList<>();
        if (image1.getWidth() == image2.getWidth() && image1.getHeight() == image2.getHeight()) {
            int range = getColorAcceptanceRange();
            for (int x = 0; x < image1.getWidth(); x++) {
                for (int y = 0; y < image1.getHeight(); y++) {
                    passFail.add(
                            isValueInRange(getPixelRGB(image1, x, y).getRed(), getPixelRGB(image2, x, y).getRed(), range) &&
                            isValueInRange(getPixelRGB(image1, x, y).getGreen(), getPixelRGB(image2, x, y).getGreen(), range) &&
                            isValueInRange(getPixelRGB(image1, x, y).getBlue(), getPixelRGB(image2, x, y).getBlue(), range));
                }
            }
        } else {
            LOGGER.info("Image Sizes are different. Comparison is false by default");
            return false;
        }

        long passes = passFail.stream().filter(test -> test).count();
        long fails = passFail.stream().filter(test -> !test).count();
        double passPercent = (double) passes/passFail.size();
        double failPercent = (double) fails/passFail.size();

        LOGGER.debug("TOTAL PASS: {}", passes);
        LOGGER.info("SIMILARITY RATE: %{}", passPercent*100);
        LOGGER.debug("TOTAL FAIL: {}", fails);
        LOGGER.debug("DIFFERENCE RATE: %{}", failPercent*100);

        return failPercent*100 < marginOfError;
    }

    /**
     * Creates a copy of the passed image that can be used for manipulation without altering the original.
     * Useful for using the same image in multiple areas that needs to be scaled multiple times
     * @param image - The original image
     * @return - The copy
     */
    public BufferedImage cloneBufferedImage(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        boolean alphaCheck = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, alphaCheck, null);
    }

    /**
     * Returns the RGB value of a given pixel as a Color type
     * @param image - The image being parsed
     * @param x - The X coordinate
     * @param y - The Y coordinate
     * @return - The RGB Color
     */
    private Color getPixelRGB(BufferedImage image, int x, int y) {
        int pixel = image.getRGB(x, y);
        int red = (pixel & 0x00ff0000) >> 16;
        int green = (pixel & 0x0000ff00) >> 8;
        int blue = pixel & 0x000000ff;

        LOGGER.debug("RGB Value: ({}}, {}}, {}})", red, green, blue);
        return new Color(red, blue, green, 0);
    }

    /*
     * @param colorStr is String of color in hexadecimal format
     */
    public Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(0, 2), 16),
                Integer.valueOf(colorStr.substring(2, 4), 16),
                Integer.valueOf(colorStr.substring(4, 6), 16));
    }

    public boolean isImagePointDesiredColor(BufferedImage image, String color) {
        return isImagePointDesiredColor(image, color, .5, .9);
    }

    /**
     * Verifies the established color of pixel at point of element middle width, 90% height location matches expected
     * RGB value
     *
     * @param image - Element image to be used
     * @param color - Color (must be in HEX format)
     * @return - Boolean comparison
     */
    public boolean isImagePointDesiredColor(BufferedImage image, String color, double width, double height) {
        Color rgbFromSource = hex2Rgb(color);
        int pixel = image.getRGB((int) (image.getWidth() * width), (int) (image.getHeight() * height));
        int red = (pixel & 0x00ff0000) >> 16;
        int green = (pixel & 0x0000ff00) >> 8;
        int blue = pixel & 0x000000ff;

        int range = getColorAcceptanceRange();

        LOGGER.info("Expected RGB Value: ({}, {}}, {}})", rgbFromSource.getRed(), rgbFromSource.getGreen(), rgbFromSource.getBlue());
        LOGGER.info("Found RGB Value: ({}}, {}}, {}})", red, green, blue);

        return isValueInRange(red, rgbFromSource.getRed(), range) &&
                isValueInRange(green, rgbFromSource.getGreen(), range) &&
                isValueInRange(blue, rgbFromSource.getBlue(), range);
    }

    public boolean isValueInRange(int rgbVal, int rgbSource, int range) {
        boolean ret = false;
        Range<Integer> valueRange = Range.between(rgbSource - range, rgbSource + range);
        if (valueRange.contains(rgbVal)) {
            ret = true;
        }
        return ret;
    }

    private int getColorAcceptanceRange() {
        int colorRange = 31;
        try {
            int inputPercentile = R.CONFIG.getInt("custom_string");
            colorRange = ((inputPercentile % 100) * 255) / 100 / 2;
            LOGGER.debug("Using range of {}}%%, +/- {}} of each primary color", inputPercentile, colorRange);
        } catch (Exception e) {
            LOGGER.debug("Using default 25% range, +/- 31 of each primary color");
        }
        return colorRange;
    }

    public void launchWithDeeplinkAddress(String deeplink) {
        LOGGER.info("Launching app with provided URL: {}", deeplink);
        getDriver().get(deeplink);
    }

    public Keys sendKeysNumpadConverter(String number) {
        Keys numpadEntry = null;
        switch(number) {
            case "0": numpadEntry = Keys.NUMPAD0; break;
            case "1": numpadEntry = Keys.NUMPAD1; break;
            case "2": numpadEntry = Keys.NUMPAD2; break;
            case "3": numpadEntry = Keys.NUMPAD3; break;
            case "4": numpadEntry = Keys.NUMPAD4; break;
            case "5": numpadEntry = Keys.NUMPAD5; break;
            case "6": numpadEntry = Keys.NUMPAD6; break;
            case "7": numpadEntry = Keys.NUMPAD7; break;
            case "8": numpadEntry = Keys.NUMPAD8; break;
            case "9": numpadEntry = Keys.NUMPAD9; break;
        }
        return numpadEntry;
    }

    public static byte[] convertImageToByteArray(BufferedImage image, String formatType) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, formatType, byteArrayOutputStream);
        } catch (IOException e) {
            LOGGER.error("Failed to convert Image to byte array: " + e);
        }
        return Base64.getEncoder().encode(byteArrayOutputStream.toByteArray());
    }

    public static class ImageMeta {
        private Point point;
        private int width;
        private int height;

        public ImageMeta(ExtendedWebElement element) {
            this.point = element.getLocation();
            width = element.getSize().getWidth();
            height = element.getSize().getHeight();
        }

        public Point getPoint() {
            return point;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
    }

    public static void captureAndUpload(WebDriver driver) {
        captureAndUpload(driver, " ");
    }

    public static void captureAndUpload(WebDriver driver, String name) {
        uploadScreenshot(((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE),name);
    }

    public static void uploadScreenshot(File screenshot, String name){
        File testScreenRootDir = ReportContext.getTestDir();
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss"));
        String screenName = currentDate + "_" + UUID.randomUUID() + ".png";
        var screenPath = String.format("%s/%s", testScreenRootDir.getAbsolutePath(), screenName);
        try {
            FileUtils.copyFile(screenshot, new File(screenPath));
            Screenshot.upload(Files.readAllBytes(screenshot.toPath()), System.currentTimeMillis());
            ReportContext.addScreenshotComment(screenName, name);
        } catch (Exception e) {
            LOGGER.info("Unable to copy screenshot {}", e.getMessage());
        }
    }

    public static void uploadScreenshot(File screenshot){
        uploadScreenshot(screenshot, " ");
    }

    public static void storeScreenshot(WebDriver driver, String name, String destFile) {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(String.format("%s%s.png", destFile, name)));
        } catch (Exception e) {
            LOGGER.info("Failed to capture screenshot with exception {}",e.getMessage());
        }
    }

    public static void storeAndUploadSS(String name, Integer count, String file, WebDriver driver) {
        UniversalUtils.storeScreenshot(driver, String.format("%d-%s", count, name), file);
        UniversalUtils.captureAndUpload(driver, name);
    }

    public static void archiveAndUploadsScreenshots(String dir, String archive) {
        try {
            ZipUtils.zipDirectory(dir, archive);
        } catch (IOException e) {
            LOGGER.error(String.valueOf(e));
        }
        Artifact.attachToTest(archive, Path.of(archive));
    }
}
