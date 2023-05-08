package com.disney.qa.common.utils;

import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.DriverHelper;
import com.zebrunner.carina.webdriver.IDriverPool;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.TapOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.disney.qa.common.constant.TimeConstant.SHORT_TIMEOUT;

@SuppressWarnings({"squid:S135"})
public class IOSUtils extends MobileUtilsExtended implements IMobileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String DIRECTION = "direction";
    private static final String ELEMENT = "element";
    private static final String DURATION = "duration";
    private static final String BUNDLE_ID = "bundleId";
    private static final String ACTION = "action";

    private static final String ALERT_PREDICATE = "type = 'XCUIElementTypeAlert'";
    public static final String DEVICE_TYPE = "capabilities.deviceType";

    private static final String PICKER_WHEEL_PREDICATE = "type = 'XCUIElementTypePickerWheel'";

    public enum ButtonStatus {
        ON, OFF, INVALID
    }

    public enum AlertButtonCommand {
        ACCEPT("accept"),
        DISMISS("dismiss");

        private String command;

        AlertButtonCommand(String command) {
            this.command = command;
        }

        public String getCommand() {
            return command;
        }
    }

    public enum AlertButton {
        LATER("Later"),
        REMIND_ME_LATER("Remind Me Later"),
        NOT_NOW("Not Now"),
        CLOSE("Close"),
        OK("OK"),
        DONT_ALLOW("Don’t Allow");

        private String alertbtn;

        AlertButton(String alertbtn) {
            this.alertbtn = alertbtn;
        }

        public String getAlertbtn() {
            return alertbtn;
        }
    }

    public enum Direction {
        UP("up"),
        DOWN("down"),
        LEFT("left"),
        RIGHT("right");

        private String dir;

        Direction(String dir) {
            this.dir = dir;
        }

        public String getDirection() {
            return dir;
        }
    }

    public enum Gestures {
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

        private String gesture;

        Gestures(String gesture) {
            this.gesture = gesture;
        }

        public String getGesture() {
            return this.gesture;
        }

    }

    public enum Attributes {
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

        private String attribute;

        Attributes(String attribute) {
            this.attribute = attribute;
        }

        public String getAttribute() {
            return this.attribute;
        }
    }

    public enum SystemBundles {
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
        SHORTCUTS("is.workflow.my.app"),
        STOCKS("com.apple.stocks"),
        TIPS("com.apple.tips"),
        TV("com.apple.tv"),
        VIDEOS("com.apple.videos"),
        VOICE_MEMOS("com.apple.VoiceMemos"),
        WALLET("com.apple.Passbook"),
        WATCH("com.apple.Bridge"),
        WEATHER("com.apple.weather");

        private String bundleId;

        SystemBundles(String bundleId) {
            this.bundleId = bundleId;
        }

        public String getBundleId() {
            return this.bundleId;
        }
    }

    /**
     * Press element for a number of seconds
     *
     * @param @element
     * @param @seconds
     */
    public void pressByElement(ExtendedWebElement element, long seconds) {
        Dimension dimension = element.getSize();
        Point location = element.getLocation();
        int x = (int) Math.round(dimension.getWidth() * Double.parseDouble("." + 50));
        int y = (int) Math.round(dimension.getHeight() * Double.parseDouble("." + 50));
        IOSDriver iosDriver = (IOSDriver) getCastedDriver();
        LOGGER.info("Press {} for {}..", element, seconds);
        TouchAction touchActions = new TouchAction(iosDriver);
        touchActions.press(new PointOption().withCoordinates(location.getX() + x, location.getY() + y)).
                waitAction(WaitOptions.waitOptions(Duration.ofSeconds(seconds))).release().perform();
    }

    /**
     * Press screen using coordinates
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void press(int x, int y) {
        TouchAction<? extends TouchAction> touchAction = new TouchAction<>((IOSDriver) getDriver());
        touchAction.press(new PointOption<>().withCoordinates(x, y)).release().perform();
    }

    /**
     * Long Press screen using coordinates
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void longPress(int x, int y) {
        TouchAction<? extends TouchAction> touchAction = new TouchAction<>((IOSDriver) getDriver());
        touchAction.longPress(new PointOption<>().withCoordinates(x, y)).release().perform();
    }

    /**
     * Press screen using coordinates
     *
     * @param x (distance (in integer) from left of screen to element)
     * @param y (distance (in integer) from top of screen to element)
     */
    public void screenPress(int x, int y) {
        Dimension scrSize = getDriver().manage().window().getSize();
        int a = scrSize.width / x;
        int b = scrSize.height / y;
        tap(a, b, 2);
    }

    /**
     * Press screen using coordinates
     *
     * @param x (distance (in double) from left of screen to element)
     * @param y (distance (in double) from top of screen to element)
     */
    public void screenPress(Double x, Double y) {
        Dimension scrSize = getDriver().manage().window().getSize();
        int a = (int) (scrSize.width / x);
        int b = (int) (scrSize.height / y);
        TouchAction<? extends TouchAction> touchAction = new TouchAction<>((IOSDriver) getDriver());
        LOGGER.info("Tapping on co-ordinates: [{}, {}]", a, b);
        touchAction.press(new PointOption<>().withCoordinates(a, b)).release().perform();
    }

    /**
     * Long press and hold on element for the given seconds
     *
     * @param element long press will be performed on this element
     * @param seconds long press will happen for this time duration (in double)
     */

    public boolean longPressAndHoldElement(ExtendedWebElement element, long seconds) {
        try {
            var dimension = element.getSize();
            Point location = element.getLocation();
            int x = (int) Math.round(dimension.getWidth() * Double.parseDouble("." + 50));
            int y = (int) Math.round(dimension.getHeight() * Double.parseDouble("." + 50));
            IOSDriver iosDriver = (IOSDriver) getCastedDriver();
            TouchAction touchActions = new TouchAction(iosDriver);
            touchActions.longPress(PointOption.point(location.getX() + x, location.getY() + y)).waitAction(WaitOptions.waitOptions(Duration.ofSeconds(seconds)));
            touchActions.perform();
            return true;
        } catch (Exception e) {
            LOGGER.error("Error occurred during longPress and hold", e);
            return false;
        }
    }

    /**
     * Tap for a given number of times at the given x,y coordinates
     *
     * @param xOffset   x-coordinate
     * @param yOffset   y-coordinate
     * @param numOfTaps tap count
     */
    public void tapAtCoordinateNoOfTimes(int xOffset, int yOffset, int numOfTaps) {
        IOSDriver iosDriver = (IOSDriver) getCastedDriver();
        TouchAction<?> touchActions = new TouchAction<>(iosDriver);
        touchActions.tap(TapOptions.tapOptions().withPosition(PointOption.point(xOffset, yOffset))
                .withTapsCount(numOfTaps)).perform();
    }

    /**
     * Drag and Drop an element to a given position
     *
     * @param startX X coord of the element
     * @param startY Y coord of the element
     * @param endX   X coord of element's destination
     * @param endY   Y coord of element's destination
     * @param wait   seconds
     */

    public boolean dragAndDropElement(int startX, int startY, int endX, int endY, long wait) {
        try {
            IOSDriver iosDriver = (IOSDriver) getCastedDriver();
            TouchAction touchActions = new TouchAction(iosDriver);
            touchActions.longPress(PointOption.point(startX, startY)).moveTo(PointOption.point(endX, endY))
                    .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(wait))).perform();
            return true;
        } catch (Exception e) {
            LOGGER.error("Error occurred during drag and drop", e);
            return false;
        }
    }

    /**
     * Checks if a system alert is present
     *
     * @return foundAlert
     */
    public boolean isAlertPresent() {
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
                LOGGER.error("Alert not found.", e);
                foundAlert = false;
            }
        }
        return foundAlert;
    }

    public void acceptAlert() {
        DriverHelper driver = new DriverHelper(getDriver());
        driver.acceptAlert();
    }

    public void cancelAlert() {
        DriverHelper driver = new DriverHelper(getDriver());
        driver.cancelAlert();
    }

    /**
     * Scroll down looking for iOSNsPredicateString locator
     *
     * @param predicateLocator
     */
    public void scrollUsingPredicate(String predicateLocator) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        HashMap<String, String> scrollObject = new HashMap<>();
        LOGGER.info("Scrolling down to predicate string: {}", predicateLocator);
        scrollObject.put("predicateString", predicateLocator);
        js.executeScript(Gestures.SCROLL.getGesture(), scrollObject);
    }

    /**
     * A generic scroll down once/twice
     */
    public void scrollDown() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        HashMap<String, String> scrollObject = new HashMap<>();
        LOGGER.info("Scrolling down..");
        scrollObject.put(DIRECTION, Direction.DOWN.getDirection());
        js.executeScript(Gestures.SCROLL.getGesture(), scrollObject);
    }

    /**
     * A generic scroll up once/twice
     */
    public void scrollUp() {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        HashMap<String, String> scrollObject = new HashMap<>();
        LOGGER.info("Scrolling up..");
        scrollObject.put(DIRECTION, Direction.UP.getDirection());
        js.executeScript(Gestures.SCROLL.getGesture(), scrollObject);
    }

    /**
     * A generic scroll down to element
     * Parent Container must be scrollable!
     */
    public void scrollTo(ExtendedWebElement element) {
        if (element.isVisible()) {
            LOGGER.info("Element already visible before swiping");
        } else {
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            HashMap<String, Object> swipeObject = new java.util.HashMap<>();
            LOGGER.info("Starting to fetch ID");
            swipeObject.put(ELEMENT, ((RemoteWebElement) getDriver().findElement(element.getBy())).getId());
            swipeObject.put("toVisible", "scrolling till visible");
            LOGGER.info("Scrolling to {}, found {}", element, element.getBy());
            js.executeScript(Gestures.SCROLL.getGesture(), swipeObject);
            LOGGER.info("Scroll complete");
        }
    }

    /**
     * Scroll for pickerwheel using predicate
     * <p>
     * Usage Note:
     * order should be "next" or "previous"
     * offset should be 0.1 or 0.15 so you don't scroll more than one item at a time
     */
    public void scrollPickerWheelUsingPredicate(String order, String predicateString, double offset) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        Map<String, Object> params = new HashMap<>();
        params.put("order", order);
        params.put("offset", offset);
        params.put(ELEMENT, ((RemoteWebElement) getDriver().findElement(MobileBy.iOSNsPredicateString(predicateString))).getId());
        try {
            js.executeScript(Gestures.SELECT_PICKER_WHEEL_VALUE.getGesture(), params);
        } catch (WebDriverException wde) {
            LOGGER.error(wde.getMessage());
        }
    }

    /**
     * Swipe left using co-ordinates
     */
    public void swipeLeft(double startx, double starty, double endx, double endy, int duration) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        HashMap<String, Object> swipeObject = new java.util.HashMap<>();
        LOGGER.info("Swiping from ({},{}) to ({},{})", startx, starty, endx, endy);
        swipeObject.put("startX", startx);
        swipeObject.put("startY", starty);
        swipeObject.put("endX", endx);
        swipeObject.put("endY", endy);
        swipeObject.put(DIRECTION, Direction.LEFT.getDirection());
        swipeObject.put(DURATION, duration);
        js.executeScript(Gestures.SWIPE.getGesture(), swipeObject);
    }

    /**
     * Swipe using element
     *
     * @param direction
     * @param duration
     */
    public void swipe(Direction direction, int duration, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        HashMap<String, Object> swipeObject = new HashMap<>();
        LOGGER.info("Scrolling {}...", direction);
        swipeObject.put(DIRECTION, direction.getDirection());
        swipeObject.put(DURATION, duration);
        swipeObject.put(ELEMENT, element);
        js.executeScript(Gestures.SWIPE.getGesture(), swipeObject);
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

    public void swipeInContainerTillElementIsPresent(ExtendedWebElement container, ExtendedWebElement element, int count, IMobileUtils.Direction direction) {
        while (element.isElementNotPresent(SHORT_TIMEOUT) && count >= 0) {
            new MobileUtilsExtended().swipeInContainer(container, direction, 1, 900);
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
    public void clickNearElement(ExtendedWebElement element, double timesW, int subHeight) {
        var dimension = element.getSize();
        Point location = element.getLocation();
        int x = (int) Math.round(dimension.getWidth() * timesW);
        int y = dimension.getHeight() - subHeight;
        tap(location.getX() + x, location.getY() + y, 0);
    }

    /**
     * Restarts currently running app
     *
     * @param appName
     */
    public void appRestart(String appName) {
        LOGGER.info("Restarting {} app", appName);
        ((IOSDriver) getDriver()).closeApp();
        ((IOSDriver) getDriver()).launchApp();
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
    public void appReinstall(String appName, String bundleId) {
        LOGGER.info("Reinstalling {} app ", appName);
        ((IOSDriver) getCastedDriver()).terminateApp(bundleId);
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
    public void installApp(String appPath) {
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
    public boolean isAppInstalled(String bundleId) {
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
    public void launchApp(String bundleId) {
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

    public void runAppInBackground(long duration) {
        IOSDriver driver = (IOSDriver) getCastedDriver();
        driver.runAppInBackground(Duration.ofSeconds(duration));
    }

    /**
     * Locks the device either for a given 'duration' or permanently.
     *
     * @param duration is of type 'Duration' <p> When specified, it will keep the device in locked state
     *                 for the given Duration then will unlock it as soon as the Duration expires.
     *                 When this param is not present then the device will remain in the locked state
     */

    public void lockDevice(Duration... duration) {
        IOSDriver driver = (IOSDriver) getCastedDriver();
        LOGGER.info("Locking the device");
        if (duration.length > 0) {
            driver.lockDevice(duration[0]);
            LOGGER.info("Device unlocked after {} seconds", duration[0].toSeconds());
        } else {
            driver.lockDevice();
        }
    }

    /**
     * unlocks the device
     */

    public void unlockDevice() {
        IOSDriver driver = (IOSDriver) getCastedDriver();
        LOGGER.info("Unlocking the device");
        driver.unlockDevice();
    }

    /**
     * Activates an existing application on the device under test and moves it to the foreground.
     * The application should be already running in order to activate it.
     * The call is ignored if the application is already in foreground.
     *
     * @param bundleId
     */
    public void activateApp(String bundleId) {
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
    public long detectAppState(String bundleId) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        Map<String, Object> params = new HashMap<>();
        params.put(BUNDLE_ID, bundleId);
        final long state = (long) js.executeScript(Gestures.QUERY_APP_STATE.getGesture(), params);
        LOGGER.info("Application state code is : {}", state);
        return state;
    }

    /**
     * Will click on the system alert button for the given accept/dismiss command up to the maxAttempts.
     * If maxAttempts reaches 0, a RuntimeException is thrown.
     *
     * @param command     - The command type to be issues (ACCEPT or DISMISS)
     * @param maxAttempts - Maximum number of attempts for alerts that are stacked for some reason.
     */
    public void handleSystemAlert(AlertButtonCommand command, int maxAttempts) {
        while (maxAttempts > 0 && isAlertPresent()) {
            maxAttempts--;
            if (command.equals(AlertButtonCommand.ACCEPT)) {
                acceptAlert();
            } else {
                cancelAlert();
            }
        }

        if (isAlertPresent()) {
            LOGGER.error("An alert is still visible on screen. Test may fail.");
        } else {
            LOGGER.info("No alerts found on screen. Proceeding with test...");
        }
    }

    /**
     * Checks for pre-existing app/system alerts and takes values - 'dismiss', 'accept', 'buttonLabel'
     *
     * @param alertButtonCommand
     * @deprecated - Deprecated as of Jun 17 2022. Change to handleSystemAlert.
     */
    @Deprecated(forRemoval = true)
    public void handleAlert(AlertButtonCommand alertButtonCommand) {
        Wait<WebDriver> wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5)).ignoring(WebDriverException.class).ignoring(NoSuchSessionException.class)
                .ignoring(TimeoutException.class);
        try {
            do {
                if (wait.until(ExpectedConditions.visibilityOfElementLocated
                        (MobileBy.iOSNsPredicateString(ALERT_PREDICATE))).isDisplayed()) {
                    JavascriptExecutor js = (JavascriptExecutor) getDriver();
                    HashMap<String, String> buttonMap = new HashMap<>();
                    buttonMap.put(ACTION, "getButtons");
                    List<String> buttons = (List<String>) js.executeScript(Gestures.ALERT.getGesture(), buttonMap);
                    LOGGER.info("Buttons Present: {}", buttons);
                    buttonMap.put(ACTION, alertButtonCommand.getCommand());
                    if (AlertButtonCommand.ACCEPT.getCommand().equalsIgnoreCase(alertButtonCommand.getCommand())) {
                        LOGGER.info("Clicking '{}'", buttons.get(1));
                    } else if (AlertButtonCommand.DISMISS.getCommand().equalsIgnoreCase(alertButtonCommand.getCommand())) {
                        LOGGER.info("Clicking '{}'", buttons.get(0));
                    }
                    js.executeScript(Gestures.ALERT.getGesture(), buttonMap);
                }
            }
            while (wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.iOSNsPredicateString(ALERT_PREDICATE))).isDisplayed());
        } catch (TimeoutException | NoAlertPresentException | NoSuchElementException e) {
            LOGGER.info("No pre-existing Alert present");
            LOGGER.debug(e.getMessage());
        }
    }

    /**
     * handle Alerts depending on type
     *
     * @param systemAlertCommand
     * @param
     * @param timeOutInSeconds
     * @param maxAttempts
     * @deprecated - Deprecated as of Jun 17 2022. Change to handleSystemAlert.
     */
    @Deprecated(forRemoval = true)
    public void handleAlert(AlertButtonCommand systemAlertCommand, AlertButtonCommand locationAlertCommand, AlertButtonCommand networkAlert, int timeOutInSeconds, int maxAttempts) {

        Wait<WebDriver> wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeOutInSeconds))
                .ignoring(WebDriverException.class)
                .ignoring(NoSuchSessionException.class)
                .ignoring(TimeoutException.class);
        try {
            do {
                if (wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.iOSNsPredicateString(ALERT_PREDICATE))).isDisplayed()) {
                    JavascriptExecutor js = (JavascriptExecutor) getDriver();
                    HashMap<String, String> buttonMap = new HashMap<>();
                    buttonMap.put(ACTION, "getButtons");
                    List<String> buttons = (List<String>) js.executeScript(Gestures.ALERT.getGesture(), buttonMap);
                    LOGGER.info("Buttons Present: {}", buttons);
                    interactWithButton(buttons, buttonMap, systemAlertCommand, locationAlertCommand, networkAlert, maxAttempts);
                }
            }
            while (wait.until(ExpectedConditions.visibilityOfElementLocated(MobileBy.iOSNsPredicateString(ALERT_PREDICATE))).isDisplayed() && maxAttempts > 0);
        } catch (TimeoutException | NoAlertPresentException | NoSuchElementException e) {
            LOGGER.debug("No pre-existing Alert present", e);
        }
    }

    /**
     * @deprecated - Deprecated as of Jun 17 2022. Change to handleSystemAlert.
     */
    @Deprecated(forRemoval = true)
    private void interactWithButton(List<String> buttonList, Map<String, String> buttonMap, AlertButtonCommand systemAlertCommand, AlertButtonCommand locationAlertCommand, AlertButtonCommand networkAlertCommand, int maxAttempts) {
        String buttonLabel;
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        if (buttonList.size() == 1) {
            buttonMap.put(ACTION, AlertButtonCommand.ACCEPT.getCommand());
            buttonMap.put("buttonLabel", buttonList.get(0));
            js.executeScript(Gestures.ALERT.getGesture(), buttonMap);
            return;
        }
        for (String button : buttonList) {
            if (AlertButtonCommand.DISMISS.getCommand().equalsIgnoreCase(systemAlertCommand.getCommand())
                    || AlertButtonCommand.DISMISS.getCommand().equalsIgnoreCase(networkAlertCommand.getCommand())) {
                if (button.contains(AlertButton.LATER.getAlertbtn())
                        || button.contains(AlertButton.REMIND_ME_LATER.getAlertbtn())
                        || button.contains(AlertButton.NOT_NOW.getAlertbtn())
                        || button.contains(AlertButton.CLOSE.getAlertbtn())
                        || button.contains(AlertButton.OK.getAlertbtn())
                        || button.contains(AlertButton.DONT_ALLOW.getAlertbtn())) {
                    LOGGER.info("System Alert found! Clicking '{}'", button);
                    buttonLabel = button;
                    buttonMap.put(ACTION, AlertButtonCommand.ACCEPT.getCommand());
                    buttonMap.put("buttonLabel", buttonLabel);
                    js.executeScript(Gestures.ALERT.getGesture(), buttonMap);
                    break;
                } else if (button.contains("Allow")) {
                    LOGGER.info("Location Alert found! Clicking '{}'", button);
                    buttonMap.put(ACTION, locationAlertCommand.getCommand());
                    js.executeScript(Gestures.ALERT.getGesture(), buttonMap);
                    break;
                }
                maxAttempts--;
            }
        }
    }

    /**
     * Launches deeplink using driver.get()
     *
     * @param url
     */
    public void launchDeeplink(String url) {
        new UniversalUtils().launchWithDeeplinkAddress(url);
    }

    /**
     * Launches deeplink using safari
     *
     * @param useSafari    a boolean param to determine if deeplink should be launched in safari
     * @param url          url address
     * @param explicitWait the wait time for the expected condition
     */
    public void launchDeeplink(Boolean useSafari, String url, int explicitWait) {
        if (useSafari != null && useSafari) {
            HashMap<String, Object> args = new HashMap<>();
            args.put(BUNDLE_ID, SystemBundles.SAFARI.getBundleId());
            JavascriptExecutor js = (JavascriptExecutor) getDriver();
            js.executeScript(Gestures.LAUNCH_APP.getGesture(), args);
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(explicitWait));
            String accessibilityID = "Phone".equalsIgnoreCase(R.CONFIG.get(DEVICE_TYPE)) ? "CapsuleNavigationBar?isSelected=true" : "UnifiedTabBarItemView?isSelected=true";
            By urlField = By.id(accessibilityID);
            wait.until(ExpectedConditions.presenceOfElementLocated(urlField));
            getDriver().findElement(urlField).click();
            wait.until(ExpectedConditions.presenceOfElementLocated(urlField));
            String enterBtnUnicode = "\uE007";
            getDriver().findElement(urlField).sendKeys(url + enterBtnUnicode);
        } else launchDeeplink(url);
    }

    public class NetworkHandler {
        private final By wifiSwitch = By.xpath("//XCUIElementTypeSwitch[@name='Wi-Fi']");
        private final By wifi = MobileBy.AccessibilityId("WIFI");
        private static final String SELECTED_WIFI = "//XCUIElementTypeStaticText[@name=\"%s\"]/following-sibling::XCUIElementTypeOther/XCUIElementTypeImage";

        public void getWifiPage() {
            terminateApp(SystemBundles.SETTINGS.getBundleId());
            launchApp(SystemBundles.SETTINGS.getBundleId());
            WebDriver driver = getDriver();
            driver.findElement(wifi).click();
        }

        public ButtonStatus getBtnStatus(int maxAttempts) {
            do {
                try {
                    int buttonValue = Integer.parseInt(getDriver().findElement(wifiSwitch).getAttribute(Attributes.VALUE.getAttribute()));
                    if (buttonValue == 1) {
                        return ButtonStatus.ON;
                    } else if (buttonValue == 0) {
                        return ButtonStatus.OFF;
                    }
                } catch (NoSuchElementException | NumberFormatException | StaleElementReferenceException e) {
                    LOGGER.debug("Button status couldn't be fetched due to:\n{}\nMax attempts remaining: {}", e, +maxAttempts);
                }
            } while (maxAttempts-- > 0);
            return ButtonStatus.INVALID;
        }

        public boolean checkIfWiFiSelected(String wifiName, int maxAttempts, int explicitWait) {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(explicitWait));
            do {
                try {
                    if ("checkmark".equalsIgnoreCase(wait.until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath(String.format(SELECTED_WIFI, wifiName)))).getAttribute(Attributes.NAME.getAttribute()))) {
                        return true;
                    }
                } catch (Exception e) {
                    if (getBtnStatus(3) == ButtonStatus.OFF) {
                        LOGGER.info("Wifi switch is turned off, needs to be toggled on..");
                        break;
                    } else if (getBtnStatus(3) == ButtonStatus.ON) {
                        LOGGER.info("Different wifi network selected, need to pick correct wifi");
                        break;
                    } else {
                        LOGGER.debug("Unable to verify 'checkmark' for '{}', due to:" +
                                "\n Re-verifying, max attempts remaining: ", wifiName, e, maxAttempts);
                    }
                }
            } while (maxAttempts-- > 0);
            return false;
        }

        public boolean toggleWiFiButtonOnAndOff(String defaultWifiName, int maxAttempts) {
            LOGGER.debug("Attempting to connect to '{}' by toggling wifi button ON and OFF", defaultWifiName);
            toggleWifiBtn("OFF", maxAttempts, ButtonStatus.OFF);
            LOGGER.debug("Wifi switch turned OFF, trying to turn ON now");
            return toggleWifiBtn("ON", maxAttempts, ButtonStatus.ON);
        }

        public boolean toggleWifiBtn(String buttonOnOff, int maxAttempts, ButtonStatus buttonStatus) {
            do {
                LOGGER.info("Trying to switch {} wifi", buttonOnOff);
                try {
                    getDriver().findElement(wifiSwitch).click();
                    if (getBtnStatus(maxAttempts).equals(buttonStatus)) {
                        LOGGER.info("Successfully turned {} wifi", buttonOnOff);
                        if (ButtonStatus.OFF.toString().equalsIgnoreCase(buttonOnOff)) {
                            break;
                        } else if (ButtonStatus.ON.toString().equalsIgnoreCase(buttonOnOff))
                            return true;
                    } else {
                        LOGGER.info("Failed to turn {} wifi, retrying..", buttonOnOff);
                    }
                } catch (WebDriverException e) {
                    LOGGER.debug("Unable to turn {} wifi due to:\n{}\nRetrying, max attempts remaining: {}", buttonOnOff, e, maxAttempts);
                }
            } while (maxAttempts-- > 0 && !getBtnStatus(maxAttempts).equals(buttonStatus));
            return false;
        }

        public boolean selectWifi(String wifiName, int maxAttempts, int explicitWait) {
            while (!checkIfWiFiSelected(wifiName, maxAttempts, 10) && maxAttempts-- > 0) {
                LOGGER.info("{} is not set as current wifi/locator not found", wifiName);
                try {
                    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(explicitWait));
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id(wifiName))).click();
                    break; //TODO may need to replace with a check
                } catch (Exception e) {
                    LOGGER.debug("{} wifi could not be selected due to:\n{}" +
                            "\nTrying again, max attempts remaining: ", wifiName, e, maxAttempts);
                }
            }
            LOGGER.info("{} is set as current wifi", wifiName);
            return true;
        }

        public void toggleWifi(IOSUtils.ButtonStatus status) {
            getWifiPage();
            toggleWifiBtn(status.toString(), 3, status);
            terminateApp(IOSUtils.SystemBundles.SETTINGS.getBundleId());
        }

    }

    /**
     * based on deviceType and current screen orientation, rotates to new orientation
     *
     * @param deviceType
     * @param oldOrientation
     * @param newOrientation
     */
    public void setToNewOrientation(DeviceType.Type deviceType, ScreenOrientation oldOrientation, ScreenOrientation newOrientation) {
        IOSDriver driver = (IOSDriver) getCastedDriver();
        if (IDriverPool.currentDevice.get().getDeviceType().equals(deviceType) && driver.getOrientation().equals(oldOrientation)) {
            LOGGER.info("Changing current {} from {} orientation to {}", IDriverPool.currentDevice.get().getDeviceType(), oldOrientation, newOrientation);
            driver.rotate(newOrientation);
        } else {
            LOGGER.info("Orientation is as expected.");
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

    public void swipePageTillElementPresent(ExtendedWebElement element, int swipes, ExtendedWebElement container, IMobileUtils.Direction direction, int duration) {
        while (!element.isPresent() && swipes > 0) {
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
    public void swipePageTillElementTappable(ExtendedWebElement element, int swipes, ExtendedWebElement container, IMobileUtils.Direction direction, int duration) {
        while (!element.isElementPresent() && swipes > 0) {
            swipeInContainer(container, direction, duration);
            swipes--;
        }

        int maxHeight = getDriver().manage().window().getSize().getHeight();
        int threshold = (int) (maxHeight - maxHeight * .05);
        if (element.getLocation().getY() > threshold) {
            swipeUp(1, 1000);
        }
    }

    public boolean detectDevice(DeviceType.Type device) {
        return IDriverPool.currentDevice.get().getDeviceType().equals(device);
    }

    public boolean detectOrientation(ScreenOrientation orientation) {
        IOSDriver driver = (IOSDriver) getCastedDriver();
        return driver.getOrientation().equals(orientation);
    }

    public void dismissKeyboardByClicking(int x, int y) {
        if (!isKeyboardShown()) {
            return;
        }
        DisneyPlusApplePageBase.fluentWait(getDriver(), 60, 5, "Keyboard was not dismissed")
                .until(it -> {
                    screenPress(x, y);
                    return !isKeyboardShown();
                });
    }

    public void dismissKeyboardForPhone() {
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
    public void setBirthDate(String month, String day, String year) {
        IOSDriver iosDriver = (IOSDriver) getCastedDriver();
        HashMap<String, Object> scrollObject = new HashMap<>();
        scrollObject.put(DIRECTION, Direction.DOWN.getDirection());
        iosDriver.executeScript(Gestures.SCROLL.getGesture(), scrollObject);

        List<WebElement> pickers = iosDriver.findElements(AppiumBy.iOSNsPredicateString(PICKER_WHEEL_PREDICATE));
        Instant waitTimeout = Instant.now().plus(30, ChronoUnit.SECONDS);
        while (pickers.isEmpty() && Instant.now().isBefore(waitTimeout)) {
            LOGGER.info("DOB picker is empty waiting for it to show on screen");
            pickers = iosDriver.findElements(AppiumBy.iOSNsPredicateString(PICKER_WHEEL_PREDICATE));
        }
        pickers.get(0).sendKeys(month);
        pickers.get(1).sendKeys(day);
        pickers.get(2).sendKeys(year);
        UniversalUtils.captureAndUpload(getCastedDriver());
    }
}

class IosUtilsException extends RuntimeException {
    public IosUtilsException(String message) {
        super(message);
    }
}
