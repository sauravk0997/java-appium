package com.disney.qa.disney.android.pages.tv.utility.navhelper;

import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.utils.androidtv.AndroidTVUtils;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVCommonPage;
import com.disney.qa.disney.android.pages.tv.DisneyPlusAndroidTVDiscoverPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.function.BooleanSupplier;

public class NavHelper extends DisneyAbstractPage {
    AndroidTVUtils utils;
    static final int DEFAULT_TIMEOUT = 30;
    static final int DEFAULT_POLL_INTERVAL = 1;

    public NavHelper(WebDriver driver) {
        super(driver);
        utils = new AndroidTVUtils(driver);
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    enum ElementState {
        VISIBLE,
        FOCUSED,
        DESC_CHANGED,
        DESC_CONTAINS
    }

    // Copied from CommonBasePage...
    private static FluentWait<WebDriver> fluentWait(WebDriver driver, long timeOut, int polling) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeOut))
                .pollingEvery(Duration.ofSeconds(polling))
                .withMessage("Element not found before timeout.")
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class);
    }

    // General key masher. Presses key until getter evaluates true.
    public void keyUntilTrue(BooleanSupplier getter, AndroidKey key, long timeOut, int pollInterval) {
        fluentWait(getDriver(), timeOut, pollInterval)
                .until(it -> {
                    if (getter.getAsBoolean()) {
                        return true;
                    }

                    press(key);
                    return false;
                });
    }

    public void waitUntilTrue(BooleanSupplier getter, long timeOut, int pollInterval) {
        fluentWait(getDriver(), timeOut, pollInterval)
                .until(it -> getter.getAsBoolean());
    }

    public void keyUntilElementState(ElementSupplier getter, AndroidKey key, ElementState targetState, String text, long timeOut, int pollInterval) {

        String desc =  "";

        if(targetState == ElementState.DESC_CHANGED && getter.getAsElement().isVisible(1)) {
            desc = utils.getContentDescription(getter.getAsElement());
        }

        String initialContentDesc = desc;

        switch(targetState) {
            case VISIBLE:
                keyUntilTrue(()-> getter.getAsElement().isVisible(1), key, timeOut, pollInterval);
                break;
            case FOCUSED:
                keyUntilTrue(()-> {
                    if (getter.getAsElement().isVisible(1)) {
                        return utils.isFocused(getter.getAsElement());
                    }
                    return false;
                    }, key, timeOut, pollInterval);
                break;
            case DESC_CHANGED:
                keyUntilTrue(()-> !initialContentDesc.equals(utils.getContentDescription(getter.getAsElement())),
                        key, timeOut, pollInterval);
                break;
            case DESC_CONTAINS:
                keyUntilTrue(()-> utils.getContentDescription(getter.getAsElement()).toLowerCase().contains(text.toLowerCase()),
                        key, timeOut, pollInterval);
                break;
        }
    }

    public void keyUntilElementVisible(ElementSupplier getter, AndroidKey key) {
        keyUntilElementState(getter, key, ElementState.VISIBLE, "", DEFAULT_TIMEOUT, DEFAULT_POLL_INTERVAL);
    }

    public void keyUntilElementVisible(ElementSupplier getter, AndroidKey key, long timeOut, int pollInterval) {
        keyUntilElementState(getter, key, ElementState.VISIBLE, "", timeOut, pollInterval);
    }

    public void keyUntilElementFocused(ElementSupplier getter, AndroidKey key) {
        keyUntilElementState(getter, key, ElementState.FOCUSED, "", DEFAULT_TIMEOUT, DEFAULT_POLL_INTERVAL);
    }

    public void keyUntilElementFocused(ElementSupplier getter, AndroidKey key, long timeOut, int pollInterval) {
        keyUntilElementState(getter, key, ElementState.VISIBLE, "", timeOut, pollInterval);
    }

    public void keyUntilElementDescChanged(ElementSupplier getter, AndroidKey key) {
        keyUntilElementState(getter, key, ElementState.DESC_CHANGED, "", DEFAULT_TIMEOUT, DEFAULT_POLL_INTERVAL);
    }

    public void keyUntilElementDescChanged(ElementSupplier getter, AndroidKey key, long timeOut, int pollInterval) {
        keyUntilElementState(getter, key, ElementState.DESC_CHANGED, "", timeOut, pollInterval);
    }

    public void keyUntilElementDescContains(ElementSupplier getter, AndroidKey key, String text) {
        keyUntilElementState(getter, key, ElementState.DESC_CONTAINS, text, DEFAULT_TIMEOUT, DEFAULT_POLL_INTERVAL);
    }

    public void keyUntilElementDescContains(ElementSupplier getter, AndroidKey key, String text, long timeOut, int pollInterval) {
        keyUntilElementState(getter, key, ElementState.DESC_CONTAINS, text, timeOut, pollInterval);
    }
    public void press (AndroidKey key) {
        ((AndroidDriver<?>) driver).pressKey(new KeyEvent(key));
    }

    public void openNavBarAndSelect(String target) {
        DisneyPlusAndroidTVDiscoverPage discoverPage = new DisneyPlusAndroidTVDiscoverPage(getDriver());
        keyUntilElementVisible(discoverPage::getSideMenuBackground, AndroidKey.BACK);
        navigateNavBar(target);
        press(AndroidKey.ENTER);
    }

    /**
     * Navigates to the desired nav item on an open and focused nav bar.
     * This method gets the index based on available nav elements instead
     * of requiring an index to reference an enum for the order.
     * This is useful if the nav items change or when testing between different apps with different navs.
     * @param navItem - Name of the nav item you want to go to.
     */
    public void navigateNavBar(String navItem) {
        By getGlobalNavOptions = new DisneyPlusAndroidTVCommonPage(getDriver()).getGlobalNavOptions().getBy();
        List<ExtendedWebElement> elements = findExtendedWebElements(getGlobalNavOptions);
        int focusedIndex = -1;
        int targetIndex = -1;
        for (int i = 0; i < elements.size(); i++) {
            if (utils.getContentDescription(elements.get(i)).contains(navItem)) {
                targetIndex = i;
            }
            if (utils.isElementFocused(elements.get(i))) {
                focusedIndex = i;
            }
        }
        if (focusedIndex == -1 || targetIndex == -1) {
            throw new IndexOutOfBoundsException("Target index or focused index not found");
        }
        if (focusedIndex == targetIndex) {
            return;
        }
        ExtendedWebElement targetElement = elements.get(targetIndex);
        AndroidKey direction = focusedIndex > targetIndex ? AndroidKey.DPAD_UP : AndroidKey.DPAD_DOWN;
        keyUntilElementFocused(() -> targetElement, direction);
    }
}
