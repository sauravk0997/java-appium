package com.disney.qa.common.utils;

import com.disney.util.ZipUtils;
import com.zebrunner.agent.core.registrar.Artifact;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.IDriverPool;
import com.zebrunner.carina.webdriver.Screenshot;
import com.zebrunner.carina.webdriver.ScreenshotType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.imgscalr.Scalr;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public interface UniversalUtils extends IDriverPool {
     static final Logger UNIVERSAL_UTILS_LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Gets the element back as a Buffered Image
     * @param element   - The element you need an Image of
     * @return          - The element as a BufferedImage
     */
    default BufferedImage getElementImage(ExtendedWebElement element) {
        element.assertElementPresent();
        File elementScreenshot = Screenshot.capture(element.getElement(), ScreenshotType.EXPLICIT_VISIBLE)
                .orElseThrow()
                .toFile();
        try {
            return ImageIO.read(elementScreenshot);
        } catch (IOException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    /**
     * An alternate to when elements become stale
     *
     * @param point     - Location of element
     * @param eleWidth  - Width of element
     * @param eleHeight - height of element
     * @return - cropped image
     */
    default BufferedImage getElementImage(Point point, int eleWidth, int eleHeight) {
        File screenshot = Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE)
                .orElseThrow()
                .toFile();
        try {
            // Crop the entire page screenshot to get only element screenshot
            return ImageIO.read(screenshot)
                    .getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);
        } catch (IOException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    default BufferedImage getCurrentScreenView() {
        File screenshot = Screenshot.capture(getDriver(), ScreenshotType.EXPLICIT_VISIBLE)
                .orElseThrow()
                .toFile();
        try {
            // Crop the entire page screenshot to get only element screenshot
            return ImageIO.read(screenshot);
        } catch (IOException e) {
            return ExceptionUtils.rethrow(e);
        }
    }

    /**
     * Returns the passed image scaled to the desired dimensions using ULTRA_QUALITY to retain the RGB values as much as possible
     * @param image         - The image to be scaled
     * @param targetWidth   - Desired width
     * @param targetHeight  - Desired height
     * @return              - Scaled image
     */
    default BufferedImage getScaledImage(BufferedImage image, int targetWidth, int targetHeight) {
        return Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight);
    }

    default boolean areImagesDifferent(BufferedImage beforeChange, BufferedImage afterChange) {
        if (beforeChange.getWidth() == afterChange.getWidth() && beforeChange.getHeight() == afterChange.getHeight()) {
            for (int x = 0; x < beforeChange.getWidth(); x++) {
                for (int y = 0; y < beforeChange.getHeight(); y++) {
                    if (beforeChange.getRGB(x, y) != afterChange.getRGB(x, y))
                        return true;
                }
            }
        }
        UNIVERSAL_UTILS_LOGGER.info("Image Sizes are different. Comparison will be false");
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
    default boolean areImagesTheSame(BufferedImage image1, BufferedImage image2, int marginOfError) {
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
            UNIVERSAL_UTILS_LOGGER.info("Image Sizes are different. Comparison is false by default");
            return false;
        }

        long passes = passFail.stream().filter(test -> test).count();
        long fails = passFail.stream().filter(test -> !test).count();
        double passPercent = (double) passes/passFail.size();
        double failPercent = (double) fails/passFail.size();

        UNIVERSAL_UTILS_LOGGER.debug("TOTAL PASS: {}", passes);
        UNIVERSAL_UTILS_LOGGER.info("SIMILARITY RATE: %{}", passPercent*100);
        UNIVERSAL_UTILS_LOGGER.debug("TOTAL FAIL: {}", fails);
        UNIVERSAL_UTILS_LOGGER.debug("DIFFERENCE RATE: %{}", failPercent*100);

        return failPercent*100 < marginOfError;
    }

    /**
     * Creates a copy of the passed image that can be used for manipulation without altering the original.
     * Useful for using the same image in multiple areas that needs to be scaled multiple times
     * @param image - The original image
     * @return - The copy
     */
    default BufferedImage cloneBufferedImage(BufferedImage image) {
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

        UNIVERSAL_UTILS_LOGGER.debug("RGB Value: ({}}, {}}, {}})", red, green, blue);
        return new Color(red, blue, green, 0);
    }

    /*
     * @param colorStr is String of color in hexadecimal format
     */
    default Color hex2Rgb(String colorStr) {
        return new Color(
                Integer.valueOf(colorStr.substring(0, 2), 16),
                Integer.valueOf(colorStr.substring(2, 4), 16),
                Integer.valueOf(colorStr.substring(4, 6), 16));
    }

    default boolean isImagePointDesiredColor(BufferedImage image, String color) {
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
    default boolean isImagePointDesiredColor(BufferedImage image, String color, double width, double height) {
        Color rgbFromSource = hex2Rgb(color);
        int pixel = image.getRGB((int) (image.getWidth() * width), (int) (image.getHeight() * height));
        int red = (pixel & 0x00ff0000) >> 16;
        int green = (pixel & 0x0000ff00) >> 8;
        int blue = pixel & 0x000000ff;

        int range = getColorAcceptanceRange();

        UNIVERSAL_UTILS_LOGGER.info("Expected RGB Value: ({}, {}}, {}})", rgbFromSource.getRed(), rgbFromSource.getGreen(), rgbFromSource.getBlue());
        UNIVERSAL_UTILS_LOGGER.info("Found RGB Value: ({}}, {}}, {}})", red, green, blue);

        return isValueInRange(red, rgbFromSource.getRed(), range) &&
                isValueInRange(green, rgbFromSource.getGreen(), range) &&
                isValueInRange(blue, rgbFromSource.getBlue(), range);
    }

    default boolean isValueInRange(int rgbVal, int rgbSource, int range) {
        Range<Integer> valueRange = Range.of(rgbSource - range, rgbSource + range);
        return valueRange.contains(rgbVal);
    }

    private int getColorAcceptanceRange() {
        int colorRange = 31;
        try {
            int inputPercentile = R.CONFIG.getInt("custom_string");
            colorRange = ((inputPercentile % 100) * 255) / 100 / 2;
            UNIVERSAL_UTILS_LOGGER.debug("Using range of {}}%%, +/- {}} of each primary color", inputPercentile, colorRange);
        } catch (Exception e) {
            UNIVERSAL_UTILS_LOGGER.debug("Using default 25% range, +/- 31 of each primary color");
        }
        return colorRange;
    }

    default void launchWithDeeplinkAddress(String deeplink) {
        UNIVERSAL_UTILS_LOGGER.info("Launching app with provided URL: {}", deeplink);
        getDriver().get(deeplink);
    }

    public static void storeScreenshot(WebDriver driver, String name, String destFile) {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(srcFile, new File(String.format("%s%s.png", destFile, name)));
        } catch (Exception e) {
            UNIVERSAL_UTILS_LOGGER.info("Failed to capture screenshot with exception {}",e.getMessage());
        }
    }

    public static void archiveAndUploadsScreenshots(String dir, String archive) {
        try {
            ZipUtils.zipDirectory(dir, archive);
        } catch (IOException e) {
            UNIVERSAL_UTILS_LOGGER.error(String.valueOf(e));
        }
        Artifact.attachToTest(archive, Path.of(archive));
    }
}
