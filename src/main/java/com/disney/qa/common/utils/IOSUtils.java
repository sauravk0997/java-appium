package com.disney.qa.common.utils;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.utils.messager.Messager;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.IDriverPool;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.helper.IPageActionsHelper;
import io.appium.java_client.AppiumBy;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.*;

import static org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT;

@SuppressWarnings({"squid:S135"})
public interface IOSUtils extends MobileUtilsExtended, IMobileUtils, IPageActionsHelper {
    Logger IOS_UTILS_LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    String DIRECTION = "direction";
    String BUNDLE_ID = "bundleId";
    String DEVICE_TYPE = "capabilities.deviceType";

    String PICKER_WHEEL_PREDICATE = "type = 'XCUIElementTypePickerWheel'";
    String TYPE_OTHER_ELEMENTS_CLASS_CHAIN = "**/XCUIElementTypeOther";
    Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    String RIGHT_POSITION = "RIGHT";
    String LEFT_POSITION = "LEFT";
    String CENTER_POSITION = "CENTER";
    String TOP = "TOP";
    String BOTTOM = "BOTTOM";

    enum ButtonStatus {
        ON, OFF, INVALID
    }

    @Getter
    enum AlertButtonCommand {
        ACCEPT("accept"),
        DISMISS("dismiss");

        private final String command;

        AlertButtonCommand(String command) {
            this.command = command;
        }
    }

    @Getter
    enum AlertButton {
        LATER("Later"),
        REMIND_ME_LATER("Remind Me Later"),
        NOT_NOW("Not Now"),
        CLOSE("Close"),
        OK("OK"),
        DONT_ALLOW("Don’t Allow");

        private final String alertbtn;

        AlertButton(String alertbtn) {
            this.alertbtn = alertbtn;
        }

    }

    enum Direction2 {
        UP("up"),
        DOWN("down"),
        LEFT("left"),
        RIGHT("right");

        private final String dir;

        Direction2(String dir) {
            this.dir = dir;
        }

        public String getDirection() {
            return dir;
        }
    }

    enum Gestures {
        SCROLL("mobile: scroll"),
        SWIPE("mobile: swipe"),
        ALERT("mobile: alert"),
        SELECT_PICKER_WHEEL_VALUE("mobile: selectPickerWheelValue"),
        QUERY_APP_STATE("mobile: queryAppState"),
        PRESS_BUTTON("mobile: pressButton"),
        TOUCH_AND_HOLD("mobile: touchAndHold"),
        INSTALL_APP("mobile: installApp"),
        REMOVE_APP("mobile: removeApp"),
        IS_APP_INSTALLED("mobile: isAppInstalled"),
        LAUNCH_APP("mobile: launchApp"),
        TERMINATE_APP("mobile: terminateApp"),
        ACTIVATE_APP("mobile: activateApp");

        private final String gesture;

        Gestures(String gesture) {
            this.gesture = gesture;
        }

        public String getGesture() {
            return this.gesture;
        }

    }

    enum Attributes {
        UID("UID"),
        ACCESSIBILITY_CONTAINER("accessibilityContainer"),
        ACCESSIBLE("accessible"),
        ENABLED("enabled"),
        FRAME("frame"),
        LABEL("label"),
        NAME("name"),
        RECT("rect"),
        SELECTED("selected"),
        TYPE("type"),
        VALUE("value"),
        VISIBLE("visible"),
        WD_ACCESSIBILITY_CONTAINER("wdAccessibilityContainer"),
        WD_ACCESSIBLE("wdAccessible"),
        WD_ENABLED("wdEnabled"),
        WD_FRAME("wdFrame"),
        WD_LABEL("wdLabel"),
        WD_NAME("wdName"),
        WD_RECT("wdRect"),
        WD_SELECTED("wdSelected"),
        WD_TYPE("wdType"),
        WD_UID("wdUID"),
        WD_VALUE("wdValue"),
        WD_VISIBLE("wdVisible");

        private final String attribute;

        Attributes(String attribute) {
            this.attribute = attribute;
        }

        public String getAttribute() {
            return this.attribute;
        }
    }

    enum SystemBundles {
        ACTIVITY("com.apple.Fitness"),
        APP_STORE("com.apple.AppStore"),
        BOOKS("com.apple.iBooks"),
        CALCULATOR("com.apple.calculator"),
        CALENDAR("com.apple.mobilecal"),
        CAMERA("com.apple.camera"),
        CLIPS("com.apple.clips"),
        CLOCK("com.apple.mobiletimer"),
        COMPASS("com.apple.compass"),
        CONTACTS("com.apple.MobileAddressBook"),
        FACETIME("com.apple.facetime"),
        FILES("com.apple.DocumentsApp"),
        FIND_FRIENDS("com.apple.mobileme.fmf1"),
        FIND_IPHONE("com.apple.mobileme.fmip1"),
        GARAGE_BAND("com.apple.mobilegarageband"),
        HEALTH("com.apple.Health"),
        HOME("com.apple.Home"),
        I_CLOUD_DRIVE("com.apple.iCloudDriveApp"),
        I_MOVIE("com.apple.iMovie"),
        I_TUNES_STORE("com.apple.MobileStore"),
        I_TUNES_U("com.apple.itunesu"),
        MAIL("com.apple.mobilemail"),
        MAPS("com.apple.Maps"),
        MESSAGES("com.apple.MobileSMS"),
        MEASURE("com.apple.measure"),
        MUSIC("com.apple.Music"),
        NEWS("com.apple.news"),
        NOTES("com.apple.mobilenotes"),
        PHONE("com.apple.mobilephone"),
        PHOTOS("com.apple.mobileslideshow"),
        PHOTO_BOOTH("com.apple.Photo-Booth"),
        PODCASTS("com.apple.podcasts"),
        REMINDERS("com.apple.reminders"),
        SAFARI("com.apple.mobilesafari"),
        SETTINGS("com.apple.Preferences"),
        TVOS_SETTINGS("com.apple.TVSettings"),
        SHORTCUTS("is.workflow.my.app"),
        STOCKS("com.apple.stocks"),
        TIPS("com.apple.tips"),
        TV("com.apple.tv"),
        VIDEOS("com.apple.videos"),
        VOICE_MEMOS("com.apple.VoiceMemos"),
        WALLET("com.apple.Passbook"),
        WATCH("com.apple.Bridge"),
        WEATHER("com.apple.weather");

        private final String bundleId;

        SystemBundles(String bundleId) {
            this.bundleId = bundleId;
        }

        public String getBundleId() {
            return this.bundleId;
        }
    }

    /**
     * Press screen using coordinates
     *
     * @param x (distance (in integer) from left of screen to element)
     * @param y (distance (in integer) from top of screen to element)
     */
    default void screenPress(int x, int y) {
        Dimension scrSize = getDriver().manage().window().getSize();
        int a = scrSize.width / x;
        int b = scrSize.height / y;
        tap(a, b, 2);
    }

    /**
     * Tap for a given number of times at the given x,y coordinates
     *
     * @param xOffset   x-coordinate
     * @param yOffset   y-coordinate
     * @param numOfTaps tap count
     */
    default void tapAtCoordinateNoOfTimes(int xOffset, int yOffset, int numOfTaps) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence doubleTap = new Sequence(finger, 1);
        doubleTap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), xOffset,
                yOffset));
        IntStream.range(0, numOfTaps).forEach(i -> addPointerInputAction(finger, doubleTap));

        try {
            Interactive driver = (Interactive) this.getDriver();
            driver.perform(List.of(doubleTap));
        } catch (ClassCastException | WebDriverException e) {
            throw new UnsupportedOperationException("Driver does not support tap method", e);
        }
    }

    default void addPointerInputAction(PointerInput finger, Sequence tap) {
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(100)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(50)));
    }

    /**
     * Tap right above an element
     * @param element ExtendedWebElement
     */
    default void tapAboveElement(ExtendedWebElement element) {
        Point point = element.getLocation();
        tap(point.getX() + 10, point.getY() - 10, 0);
    }

    /**
     * Checks if a system alert is present
     *
     * @return foundAlert
     */
    @Override
    default boolean isAlertPresent() {
        if (!isDriverRegistered(IDriverPool.DEFAULT)) {
            // no need to verify alert if driver has not started yet
            return false;
        }
        boolean foundAlert = false;
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            foundAlert = true;
        } catch (final WebDriverException e) {
            if (e.getMessage().contains("An attempt was made to operate on a modal dialog when one was not open")) {
                IOS_UTILS_LOGGER.error("Alert not found.", e);
            }
        }
        return foundAlert;
    }

    @Override
    default void acceptAlert() {
        WebDriver drv = getDriver();
        Wait<WebDriver> wait = new WebDriverWait(drv, Duration.ofSeconds(3), Duration.ofMillis(1));
        try {
            wait.until((Function<WebDriver, Object>) dr -> isAlertPresent());
            drv.switchTo().alert().accept();
            Messager.ALERT_ACCEPTED.info("");
        } catch (Exception e) {
            Messager.ALERT_NOT_ACCEPTED.error("");
        }
    }

    @Override
    default void cancelAlert() {
        WebDriver drv = getDriver();
        Wait<WebDriver> wait = new WebDriverWait(drv, Duration.ofSeconds(8), Duration.ofMillis(1));
        try {
            wait.until((Function<WebDriver, Object>) dr -> isAlertPresent());
            IOS_UTILS_LOGGER.info("Alert is present, canceling it");
            drv.switchTo().alert().dismiss();
            Messager.ALERT_CANCELED.info("");
        } catch (Exception e) {
            Messager.ALERT_NOT_CANCELED.error("");
        }
    }

    /**
     * A generic scroll down once/twice
     */
    default void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        HashMap<String, String> scrollObject = new HashMap<>();
        IOS_UTILS_LOGGER.info("Scrolling down..");
        scrollObject.put(DIRECTION, Direction2.DOWN.getDirection());
        js.executeScript(Gestures.SCROLL.getGesture(), scrollObject);
    }

    /**
     * A generic scroll up once/twice
     */
    default void scrollUp() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        HashMap<String, String> scrollObject = new HashMap<>();
        IOS_UTILS_LOGGER.info("Scrolling up..");
        scrollObject.put(DIRECTION, Direction2.UP.getDirection());
        js.executeScript(Gestures.SCROLL.getGesture(), scrollObject);
    }

    /**
     * Swipes in the given direction for the given number of times in the container till
     * it finds the desired element
     *
     * @param container
     * @param element
     * @param count
     * @param direction
     */

    default void swipeInContainerTillElementIsPresent(ExtendedWebElement container, ExtendedWebElement element,
                                                      int count, Direction direction, int... duration) {
        int swipeDuration = duration.length > 0 ? duration[0] : 900;
        while (element.isElementNotPresent(1) && count >= 0) {
            swipeInContainer(container, direction, 1, swipeDuration);
            count--;
        }
    }

    /**
     * Clicks near specified element based on element
     *
     * @param element
     * @param timesW
     * @param subHeight
     */
    default void clickNearElement(ExtendedWebElement element, double timesW, int subHeight) {
        var dimension = element.getSize();
        Point location = element.getLocation();
        int x = (int) Math.round(dimension.getWidth() * timesW);
        int y = dimension.getHeight() - subHeight;
        tap(location.getX() + x, location.getY() + y, 0);
    }

    default boolean verifyElementIsOnRightSide(ExtendedWebElement element) {
        LOGGER.info("Verifying if the element is on the right side of the screen");
        Dimension screenSize = getDriver().manage().window().getSize();
        int screenWidth = screenSize.width;
        int elementPosition = getCenterCoordinate(element).getX();
        // Get 50 percent of the screen width size to validate if elements are on the right or left
        double percentageToValidate = 0.5 * screenWidth;
        return elementPosition > percentageToValidate;
    }

    /**
     * Closes the app sessions via Terminate and issues a new reinstall command, then relaunches the app.
     * Fixes invalid session state data (Network visibility for example).
     * Can be used to redirect app traffic any any point during a test run.
     * <p>
     * DOES NOT WIPE SESSION DATA (user is not returned to Welcome screens)
     *
     * @param appName  (capabilities.app value, not the normal name)
     * @param bundleId
     */
    default void appReinstall(String appName, String bundleId) {
        IOS_UTILS_LOGGER.info("Reinstalling {} app ", appName);
        terminateApp(bundleId);
        installApp(appName);
        launchApp(bundleId);
    }

    /**
     * Installs given application to the device under test.
     * If the same application is already installed then it's going to be installed over the existing one,
     * which allows you to test upgrades. Be careful while reinstalling the main application under test:
     * make sure that terminateApp has been called first, otherwise WebDriverAgent will detect the state as
     * a potential crash of the application.
     *
     * @param appPath
     */
    default void installApp(String appPath) {
        Map<String, Object> params = new HashMap<>();
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        params.put("app", appPath);
        js.executeScript(Gestures.INSTALL_APP.getGesture(), params);
    }

    /**
     * Verifies whether the application with given bundle identifier is installed on the device.
     *
     * @param bundleId
     * @return true/false
     */
    default boolean isAppInstalled(String bundleId) {
        Map<String, Object> params = new HashMap<>();
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        params.put(BUNDLE_ID, bundleId);
        return (Boolean) js.executeScript(Gestures.IS_APP_INSTALLED.getGesture(), params);
    }

    /**
     * Executes an existing application on the device. If the application is
     * already running then it will be brought to the foreground.
     *
     * @param bundleId
     */
    default void launchApp(String bundleId) {
        HashMap<String, Object> args = new HashMap<>();
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        args.put(BUNDLE_ID, bundleId);
        js.executeScript(Gestures.LAUNCH_APP.getGesture(), args);
    }

    /**
     * Sends the currently active app to the background,
     * and either return after a certain amount of time, or leave the app deactivated.
     *
     * @param duration duration is an integer designating how long, in seconds, to background the app for
     *                 To leave the app deactivated, pass either null or -1 value.
     */

    default void runAppInBackground(long duration) {
       runAppInBackground(Duration.ofSeconds(duration));
    }

    /**
     * Locks the device either for a given 'duration' or permanently.
     *
     * @param duration is of type 'Duration' <p> When specified, it will keep the device in locked state
     *                 for the given Duration then will unlock it as soon as the Duration expires.
     *                 When this param is not present then the device will remain in the locked state
     */

    default void lockDevice(Duration... duration) {
        IOS_UTILS_LOGGER.info("Locking the device");
        if (duration.length > 0) {
            lockDevice(duration[0]);
            IOS_UTILS_LOGGER.info("Device unlocked after {} seconds", duration[0].toSeconds());
        } else {
            lockDevice();
        }
    }

    /**
     * Activates an existing application on the device under test and moves it to the foreground.
     * The application should be already running in order to activate it.
     * The call is ignored if the application is already in foreground.
     *
     * @param bundleId
     */
    default void activateApp(String bundleId) {
        HashMap<String, Object> args = new HashMap<>();
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        args.put(BUNDLE_ID, bundleId);
        js.executeScript(Gestures.ACTIVATE_APP.getGesture(), args);
    }

    /**
     * Checks for application state
     * <p>
     * There are five possible application states:
     * 0: The current application state cannot be determined/is unknown
     * 1: The application is not running
     * 2: The application is running in the background and is suspended
     * 3: The application is running in the background and is not suspended
     * 4: The application is running in the foreground
     *
     * @param bundleId
     * @return state
     */
    default long detectAppState(String bundleId) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        Map<String, Object> params = new HashMap<>();
        params.put(BUNDLE_ID, bundleId);
        final long state = (long) js.executeScript(Gestures.QUERY_APP_STATE.getGesture(), params);
        IOS_UTILS_LOGGER.info("Application state code is : {}", state);
        return state;
    }

    /**
     * Will click on the system alert button for the given accept/dismiss command up to the maxAttempts.
     * If maxAttempts reaches 0, a RuntimeException is thrown.
     *
     * @param command     - The command type to be issues (ACCEPT or DISMISS)
     * @param maxAttempts - Maximum number of attempts for alerts that are stacked for some reason.
     */
    default void handleSystemAlert(AlertButtonCommand command, int maxAttempts) {
        while (maxAttempts > 0 && isAlertPresent()) {
            maxAttempts--;
            if (command.equals(AlertButtonCommand.ACCEPT)) {
                acceptAlert();
            } else {
                cancelAlert();
            }
        }

        if (isAlertPresent()) {
            IOS_UTILS_LOGGER.error("An alert is still visible on screen. Test may fail.");
        } else {
            IOS_UTILS_LOGGER.info("No alerts found on screen. Proceeding with test...");
        }
    }

    /**
     * Launches deeplink using driver.get()
     *
     * @param url
     */
    default void launchDeeplink(String url) {
        launchWithDeeplinkAddress(url);
    }

    /**
     * based on deviceType and current screen orientation, rotates to new orientation
     *
     * @param deviceType
     * @param oldOrientation
     * @param newOrientation
     */
    default void setToNewOrientation(DeviceType.Type deviceType, ScreenOrientation oldOrientation, ScreenOrientation newOrientation) {
        if (IDriverPool.currentDevice.get().getDeviceType().equals(deviceType) && getOrientation().equals(oldOrientation)) {
            IOS_UTILS_LOGGER.info("Changing current {} from {} orientation to {}", IDriverPool.currentDevice.get().getDeviceType(), oldOrientation, newOrientation);
            rotate(newOrientation);
        } else {
            IOS_UTILS_LOGGER.info("Orientation is as expected.");
        }
    }

    /**
     * Set the following to swipe till an element is present (not to be used for tappable)
     *
     * @param swipes
     * @param element
     * @param container
     * @param direction
     * @param duration
     */

    default void swipePageTillElementPresent(ExtendedWebElement element, int swipes, ExtendedWebElement container, Direction direction, int duration) {
        while (!element.isPresent(5) && swipes > 0) {
            swipeInContainer(container, direction, duration);
            swipes--;
        }
    }

    /**
     * Set the following to swipe till an element is tappable
     *
     * @param swipes
     * @param element
     * @param container
     * @param direction
     * @param duration
     */
    default void swipePageTillElementTappable(ExtendedWebElement element, int swipes, ExtendedWebElement container, Direction direction, int duration) {
        while (!element.isPresent(5) && swipes > 0) {
            swipeInContainer(container, direction, duration);
            swipes--;
        }

        int maxHeight = getDriver().manage().window().getSize().getHeight();
        int minThreshold = (int) (maxHeight * .1);
        int threshold = (int) (maxHeight - maxHeight * .05);
        int yCoordinate = element.getLocation().getY();
        if (yCoordinate > threshold) {
            swipeUp(1, 1000);
        } else if (yCoordinate < minThreshold) {
            swipeDown(1, 1000);
        }
    }

    /**
     * Swipe until the given element is no longer visible. Retry for the amount of given swipes.
     * If container is set to 'null' it will scroll over the whole screen.
     *
     * @param swipes
     * @param element
     * @param container
     * @param direction
     * @param duration
     */
    default void swipeTillElementIsNotVisible(ExtendedWebElement element, int swipes, ExtendedWebElement container,
                                                  Direction direction, int duration) {
        while (element.isPresent(1) && swipes > 0) {
            swipeInContainer(container, direction, duration);
            swipes--;
        }
    }

    default boolean detectDevice(DeviceType.Type device) {
        return IDriverPool.currentDevice.get().getDeviceType().equals(device);
    }

    default void dismissKeyboardByClicking(int x, int y) {
        if (!isKeyboardShown()) {
            return;
        }
        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 5, "Keyboard was not dismissed")
                .until(it -> {
                    screenPress(x, y);
                    return !isKeyboardShown();
                });
    }

    default void dismissKeyboardForPhone() {
        if (IDriverPool.currentDevice.get().getDeviceType().equals(DeviceType.Type.IOS_PHONE)) {
            dismissKeyboardByClicking(2, 4);
        }
    }

    /**
     * Set month, day, year for birthdate on picker wheel
     *
     * @param month
     * @param day
     * @param year
     */
    default void setBirthDate(String month, String day, String year) {
        HashMap<String, Object> scrollObject = new HashMap<>();
        scrollObject.put(DIRECTION, Direction2.DOWN.getDirection());
        ( (JavascriptExecutor)getDriver()).executeScript(Gestures.SCROLL.getGesture(), scrollObject);

        List<WebElement> pickers = getDriver().findElements(AppiumBy.iOSNsPredicateString(PICKER_WHEEL_PREDICATE));
        Instant waitTimeout = Instant.now().plus(30, ChronoUnit.SECONDS);
        while (pickers.isEmpty() && Instant.now().isBefore(waitTimeout)) {
            IOS_UTILS_LOGGER.info("DOB picker is empty waiting for it to show on screen");
            pickers = getDriver().findElements(AppiumBy.iOSNsPredicateString(PICKER_WHEEL_PREDICATE));
        }
        pickers.get(0).sendKeys(month);
        pickers.get(1).sendKeys(day);
        pickers.get(2).sendKeys(year);
    }

    /**
     * Scroll from element to a given position
     *
     * @param startX X coord of the element
     * @param startY Y coord of the element
     * @param endX   X coord of element's destination
     * @param endY   Y coord of element's destination
     */
    default void scrollFromTo(int startX, int startY, int endX, int endY) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence scroll = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX,
                        startY))
                .addAction(finger.createPointerDown(LEFT.asArg()))
                .addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), endX, endY))
                .addAction(finger.createPointerUp(LEFT.asArg()));

        Interactive driver = null;
        try {
            driver = (Interactive) getDriver();
            driver.perform(List.of(scroll));
        } catch (Exception e) {
            Assert.fail(String.format("Error occurred during scrolling from (X = %d; Y = %d) to (X = %d; Y = %d): %s",
                    startX, startY, endX, endY, e));
        }
    }

    default String convertToTitleCase(String text, String separator) {
        return Arrays.stream(text.split(separator))
                .map(word -> word.isEmpty()
                        ? word
                        : Character.toTitleCase(word.charAt(0)) + word.substring(1).toLowerCase()
                )
                .collect(Collectors.joining(separator));
    }

    default int getDistanceBetweenElements(ExtendedWebElement element1, ExtendedWebElement element2) {
        Point position1 = getCenterCoordinate(element1);
        Point position2 = getCenterCoordinate(element2);
        double pointXSqr = Math.pow((double) position2.getX() - (double) position1.getX(), 2);
        double pointYSqr = Math.pow((double) position2.getY() - (double) position1.getY(), 2);
        return (int) Math.sqrt(pointXSqr + pointYSqr);
    }

    default Point getCenterCoordinate(ExtendedWebElement element) {
        Point elementLocation = element.getLocation();
        Dimension elementSize = element.getSize();
        int startX = elementLocation.getX();
        int startY = elementLocation.getY();
        int width = elementSize.getWidth();
        int height = elementSize.getHeight();
        int centerX = startX + (width / 2);
        int centerY = startY + (height / 2);
        return new Point(centerX, centerY);
    }

    default void validateElementPositionAlignment(ExtendedWebElement element, String alignment) {
        int elementPosition = getCenterCoordinate(element).getX();
        LOGGER.info("elementPosition: {} ", elementPosition);
        Dimension screenSize = getDriver().manage().window().getSize();
        int screenWidth = screenSize.width;
        LOGGER.info("screen size width: {} ", screenWidth);
        // Get 50 percent of the screen width size to validate if elements are on the right or left
        double percentageToValidate = 0.5 * screenWidth;
        int limit = 10;
        LOGGER.info("percentageToValidate size: {} ", percentageToValidate);
        switch(alignment) {
            case RIGHT_POSITION:
                Assert.assertTrue(elementPosition > percentageToValidate,
                        "Element is not at the right position");
                break;
            case LEFT_POSITION:
                Assert.assertTrue(elementPosition < percentageToValidate, "Element is not at the left position");
                break;
            case CENTER_POSITION:
                int position = elementPosition - (int) (percentageToValidate);
                ValueRange range = ValueRange.of(0, limit);
                Assert.assertTrue(range.isValidIntValue(position), "Element is not at the center position");
                break;
            default: throw new IllegalArgumentException("Invalid alignment String");
        }
    }

    default void validateElementPositionAlignmentInContainer(ExtendedWebElement element,
                                                                         ExtendedWebElement container,
                                                                         String alignment) {
        int elementPosition = getCenterCoordinate(element).getX();
        LOGGER.info("elementPosition: {} ", elementPosition);
        int cellElementPosition = getCenterCoordinate(container).getX();
        LOGGER.info("Container center Position: {} ", cellElementPosition);
        switch (alignment) {
            case RIGHT_POSITION:
                Assert.assertTrue(elementPosition > cellElementPosition,
                        "Element is not at the right position");
                break;
            case LEFT_POSITION:
                Assert.assertTrue(elementPosition < cellElementPosition,
                        "Element is not at the left position");
                break;
            default:
                throw new IllegalArgumentException("Invalid alignment String");
        }
    }

    default void validateElementExpectedHeightPosition(ExtendedWebElement element, String position) {
        int elementPosition = getCenterCoordinate(element).getY();
        Dimension screenSize = getDriver().manage().window().getSize();
        int screenHeight = screenSize.height;
        double halfHeightScreen = 0.5 * screenHeight;
        LOGGER.info("Screen size height: {}", screenHeight);
        LOGGER.info("Element position: {}", elementPosition);

        switch (position) {
            case TOP:
                Assert.assertTrue(elementPosition < halfHeightScreen, "Element is not at the top position");
                break;
            case BOTTOM:
                Assert.assertTrue(elementPosition > halfHeightScreen, "Element is not at the bottom position");
                break;
            default:
                throw new IllegalArgumentException("Invalid alignment String");
        }
    }

    default void validateElementExpectedHeightPositionInContainer(ExtendedWebElement element,
                                                                              ExtendedWebElement container,
                                                                              String position) {
        int elementPosition = getCenterCoordinate(element).getY();
        LOGGER.info("elementPosition: {} ", elementPosition);
        int cellElementPosition = getCenterCoordinate(container).getY();
        LOGGER.info("Container center Position: {} ", cellElementPosition);
        switch (position) {
            case TOP:
                Assert.assertTrue(elementPosition < cellElementPosition,
                        "Element is not at the top position");
                break;
            case BOTTOM:
                Assert.assertTrue(elementPosition > cellElementPosition,
                        "Element is not at the bottom position");
                break;
            default:
                throw new IllegalArgumentException("Invalid alignment String");
        }
    }

    default boolean isToggleEnabled(ExtendedWebElement toggle) {
        if (!toggle.isPresent()) {
            throw new java.util.NoSuchElementException("Given Jarvis toggle was not present");
        }
        List<ExtendedWebElement> toggleSubElements = toggle.findExtendedWebElements(
                AppiumBy.iOSClassChain(TYPE_OTHER_ELEMENTS_CLASS_CHAIN));
        if (toggleSubElements.isEmpty() || toggleSubElements.size() < 2) {
            throw new IndexOutOfBoundsException("Unable to find given Jarvis toggle track and slider");
        }
        ExtendedWebElement toggleTrack = toggleSubElements.get(0);
        ExtendedWebElement toggleSlider = toggleSubElements.get(1);
        return toggleSlider.getLocation().getX() > toggleTrack.getLocation().getX();
    }

    default String escapeSingleQuotes(String input) {
        return input.replaceAll("'", "\\\\'");
    }
}
