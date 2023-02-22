package com.disney.qa.common.utils.androidtv;

import com.disney.exceptions.AndroidTVRestartFailedException;
import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.utils.AndroidKeys;
import com.disney.qa.common.utils.AndroidUtilsExtended;
import com.disney.qa.common.utils.UniversalUtils;
import com.disney.qa.disney.android.pages.common.DisneyPlusCommonPageBase;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.android.AndroidService;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.KeyEventMetaModifier;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AndroidTVUtils extends DisneyAbstractPage {

    private static final String DRIVER_NAME = "rebootedDriver";
    private static final String AMAZON = "amazon";

    public AndroidTVUtils(WebDriver driver) {
        super(driver);
    }

    public static String getManufacturer() {
        return AndroidService.getInstance().executeShell("getprop ro.product.manufacturer");
    }

    public static boolean isAmazon() {
        return getManufacturer().toLowerCase().contains(AMAZON);
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    public boolean isElementFocused(ExtendedWebElement ele) {
        boolean result = false;
        if (ele.isElementPresent(ONE_SEC_TIMEOUT)) {
            result = ele.getAttribute("focused").equalsIgnoreCase("true");
        }
        return result;
    }

    public boolean isFocused(ExtendedWebElement element) {
        return element.getAttribute("focused").equalsIgnoreCase("true");
    }

    public boolean isSelected(ExtendedWebElement element) {
        return element.getAttribute("selected").equalsIgnoreCase("true");
    }

    public String getContentDescription(ExtendedWebElement element) {
        return element.getAttribute("content-desc");
    }

    public boolean isElementSelected(ExtendedWebElement ele) {
        boolean result = false;
        if (ele.isElementPresent(DELAY)) {
            result = ele.getAttribute("selected").equalsIgnoreCase("true");
        }
        return result;
    }

    public void keyPressTimes(Consumer<AndroidTVUtils> action, int times, int timeout) {
        IntStream.range(0, times).forEach(i -> {
            action.accept(this);
            try {
                TimeUnit.SECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                LOGGER.info("Sleep was interrupted during key press with exception: " + e);
                Thread.currentThread().interrupt();
            }
        });
    }

    public void pressEnter() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.ENTER))));
    }

    public void pressRight() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.DPAD_RIGHT))));
    }

    public void pressLeft() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.DPAD_LEFT))));
    }

    public void pressUp() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.DPAD_UP))));
    }

    public void pressDown() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.DPAD_DOWN))));
    }

    public void pressEscape() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.ESCAPE))));
    }

    public void pressHome() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.HOME))));
    }

    public void pressMenu() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.MENU))));
    }

    public void pressTab() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.TAB))));
    }

    public void pressDelete() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.DEL))));
    }

    public void pressPlayOrPause() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.MEDIA_PLAY_PAUSE))));
    }

    public void pressSelect() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.DPAD_CENTER))));
    }

    public void pressBack() {
        ((AndroidDriver) getCastedDriver()).pressKey((new KeyEvent((AndroidKey.BACK))));
    }

    public WebDriver rebootAndroidTv() {
        AndroidService androidService = new AndroidService();
        Map<String, Object> caps = ((AndroidDriver) getCastedDriver()).getSessionDetails();
        HashMap<String, Object> map = new HashMap<>();
        caps.forEach((k, v) -> {
            LOGGER.info(String.format("key: %s  value %s", k, v));
            map.put(k, v);
        });
        androidService.executeAdbCommand("reboot");
        // wait for device to be restarted
        pause(60);
        androidService.executeAdbCommand("start-server");
        // wait for adb to be restarted
        pause(30);
        String seleniumServer = Configuration.getSeleniumUrl();
        LOGGER.info("Selenium server is " + seleniumServer);
        map.put("noReset", "true");
        map.put("fullReset", "false");
        try {
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities(map);
            return getDriver(DRIVER_NAME, desiredCapabilities, seleniumServer);
        } catch (Exception e) {
            throw new AndroidTVRestartFailedException("Failed to create driver after restarting Android TV " + e);
        }
    }

    public void sendInput(String input) {
        List<Character> list = input.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
        list.forEach(character -> {
            if (Character.isUpperCase(character)) {
                ((AndroidDriver) getCastedDriver()).pressKey(new KeyEvent(AndroidKeys.getKeyCodes().get(Character.toLowerCase(character)))
                        .withMetaModifier(KeyEventMetaModifier.CAP_LOCKED));
            } else {
                ((AndroidDriver) getCastedDriver()).pressKey(new KeyEvent(AndroidKeys.getKeyCodes().get(character)));
            }
        });
    }
    public void hideKeyboardIfPresent() {
        hideKeyboardIfPresent(2);
    }

    public void hideKeyboardIfPresent(int waitDelay) {
        AndroidUtilsExtended utilsEx = new AndroidUtilsExtended();

        // In some scenarios it appears this can be called before the
        // keyboard is shown. Delay to allow keyboard to appear.
        pause(waitDelay);

        if (utilsEx.isKeyboardShown()) {
            AtomicReference<String> manufacturer = new AtomicReference<>(StringUtils.EMPTY);
            LOGGER.info("Keyboard is open");
            DisneyPlusCommonPageBase.fluentWait(getCastedDriver(), LONG_TIMEOUT, 1, "Unable to get an output from ADB")
                    .until(it -> {
                        manufacturer.set(getManufacturer());
                        return !manufacturer.get().isEmpty();
                    });
            // Since "TAB" can cause a focus change as it's sometime mapped to "next",
            // try "BACK" first. Otherwise, fail-over to "TAB."
            if (isAmazon()) {
                pressBack();
                pause(1);
                // Fail-over to TAB
                if (utilsEx.isKeyboardShown()) {
                    pressTab();
                }
            }
            else
                pressBack();

            pause(1);
            UniversalUtils.captureAndUpload(getCastedDriver());
        }
    }
}
