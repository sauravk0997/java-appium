package com.disney.qa.espn.ios.pages.common;

import com.disney.qa.common.DisneyAbstractPage;
import com.disney.qa.common.utils.IOSUtils;
import com.disney.qa.espn.ios.pages.authentication.EspnPaywallIOSPageBase;
import com.disney.qa.espn.ios.pages.authentication.EspnPaywallLogInIOSPageBase;
import com.disney.qa.espn.ios.pages.home.EspnHomeIOSPageBase;
import com.disney.qa.espn.ios.pages.listen.EspnListenIOSPageBase;
import com.disney.qa.espn.ios.pages.scores.EspnScoresIOSPageBase;
import com.disney.qa.espn.ios.pages.sports.EspnSportsIOSPageBase;
import com.disney.qa.espn.ios.pages.watch.EspnWatchIOSPageBase;
import com.qaprosoft.carina.core.foundation.utils.R;
import com.qaprosoft.carina.core.foundation.webdriver.DriverHelper;
import com.qaprosoft.carina.core.foundation.webdriver.Screenshot;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.annotations.AccessibilityId;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.annotations.ClassChain;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.disney.qa.espn.ios.pages.common.EspnIOSPageBase.TabOptions.WATCH;


public class EspnIOSPageBase extends DisneyAbstractPage {


    //OBJECTS


    //Alerts

    private static final String LOCATION_ALERT = "Location";

    private static final String NOTIFICATION_ALERT = "Notification";

    private static final String CUSTOMIZATION_ALERT = "Customization";

    private static final String LOCATION_ALERT_ACCESSIBILITY_ID =
            "This allows you to stream live content in the app, " +
                    "and recommends local teams to add to your favorites.";

    private static final String NOTIFICATION_ALERT_ACCESSIBILITY_ID =
            "Notifications may include alerts, sounds, and icon badges. " +
                    "These can be configured in Settings.";

    private static final String CUSTOMIZATION_ALERT_ACCESSIBILITY_ID = "You didn't customize the app! " +
            "Don't worry, you can always add favorites later from Settings.";


    //Tab bar

    public static final String WATCH_TAB_BTN_ACCESSIBILITY_ID = "Watch";

    @FindBy(xpath = "**/XCUIElementTypeButton[`name == 'watch'`]")
    @ClassChain
    private ExtendedWebElement watchTab;

    @FindBy(xpath = "**/XCUIElementTypeTabBar/XCUIElementTypeButton[`name == 'Scores'`]")
    @ClassChain
    private ExtendedWebElement scoresTabBtn;

    private static final String HOME_TAB_BTN_ACCESSIBILITY_ID = "Home";

    private static final String SCORES_TAB_BTN_ACCESSIBILITY_ID = "Scores";

    private static final String LISTEN_TAB_BTN_ACCESSIBILITY_ID = "Listen";

    private static final String SPORTS_TAB_BTN_ACCESSIBILITY_ID = "Sports";

    private static final String SETTINGS_BTN_ACCESSIBILITY_ID = "button.settings";


    @FindBy(id = "loginButton")
    private ExtendedWebElement logInBtn;

    @FindBy(name = "customButton")
    @AccessibilityId
    private ExtendedWebElement freeTrialBtn;

    @FindBy(name = "button.search")
    @AccessibilityId
    private ExtendedWebElement searchBtn;


    //METHODS

    public EspnIOSPageBase(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    /**
     * Watch Tab options
     **/
    public enum TabOptions {
        WATCH("WATCH"),
        HOME("HOME"),
        SCORES("SCORES"),
        LISTEN("LISTEN"),
        SPORTS("SPORTS"),
        SETTINGS("SETTINGS");

        private String tab;

        TabOptions(String tab) {
            this.tab = tab;
        }

        public String getTab() {
            return tab;
        }
    }

    /**
     * Watch Subnavs enum
     **/
    public enum WatchSubnavOptions {
        FEATURED("FEATURED"),
        ESPNPLUS("ESPN PLUS"),
        ORIGINALS("ORIGINALS");

        private String tab;

        WatchSubnavOptions(String tab) {
            this.tab = tab;
        }

        public String getText() {
            return tab;
        }
    }


    /**
     * Used for navigating to Watch tab on first try,
     * to overcome invisible layer on top of button
     **/
    public void getWatchPageFirstTry(int maxAttempts) {
        IOSUtils iosUtils = new IOSUtils();
        do {
            LOGGER.info("Trying to click on Watch tab | Max attempts remaining: " + maxAttempts);
            getPageFluently(watchTab, 3, 5, 1);
            if (iosUtils.isAlertPresent()) {
                LOGGER.info("iTunes alert found.. dismissing alert");
                DriverHelper driver = new DriverHelper(getDriver());
                driver.acceptAlert();
            }
            maxAttempts--;
        } while (!logInBtn.isElementPresent(DELAY) && maxAttempts > 0);

        if ("Phone".equalsIgnoreCase(R.CONFIG.get("capabilities.deviceType"))) {
            try {
                while (!logInBtn.isElementPresent(5) && !freeTrialBtn.isElementPresent(5)) {
                    LOGGER.info("Unable find Watch tab button with locator, trying to tap using co-ordinates");
                    LOGGER.info("Tapping on Watch tab using " + R.CONFIG.get("capabilities.deviceType")
                            + " (Handset) co-ordinates");
                    new IOSUtils().screenPress(2, 1);
                }
            } catch (NoSuchElementException | TimeoutException e) {
                LOGGER.info(e.getMessage());
            }
        } else if ("Tablet".equalsIgnoreCase(R.CONFIG.get("capabilities.deviceType"))) {
            try {
                while (!logInBtn.isElementPresent(5) && !freeTrialBtn.isElementPresent(5)) {
                    LOGGER.info("Unable find Watch tab button with locator, trying to tap using co-ordinates");
                    LOGGER.info("Tapping on Watch tab using " + R.CONFIG.get("capabilities.deviceName")
                            + " (Tablet) co-ordinates");
                    iosUtils.screenPress(5, 1);
                }
            } catch (NoSuchElementException | TimeoutException e) {
                LOGGER.info(e.getMessage());
            }
        }
    }

    private void getPageFluently(ExtendedWebElement element, int maxAttempt, int timeout, int polling) {
        final boolean[] result = new boolean[1];
        FluentWait<WebDriver> wait = new FluentWait(getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(polling))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class)
                .ignoring(WebDriverException.class);
        do {
            try {
                wait.until(driver -> {
                    result[0] = element.clickIfPresent(SHORT_TIMEOUT);
                    return result[0];
                });
            } catch (RuntimeException e) {
                LOGGER.info("Type of exception found: " + e);
            }
        } while (!result[0] && maxAttempt-- > 0);
    }

    private void getPageFluently(String accessibilityId, int maxAttempt, int timeout, int polling) {
        final boolean[] result = new boolean[1];
        FluentWait<WebDriver> wait = new FluentWait(getDriver())
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(polling))
                .ignoring(NoSuchElementException.class)
                .ignoring(TimeoutException.class)
                .ignoring(WebDriverException.class);
        do {
            try {
                wait.until(driver -> {
                    result[0] = findExtendedWebElement(MobileBy.AccessibilityId(accessibilityId)).clickIfPresent(SHORT_TIMEOUT);
                    return result[0];
                });
            } catch (RuntimeException e) {
                LOGGER.info("Type of exception found: " + e);
            }
        } while (!result[0] && maxAttempt-- > 0);
    }


    public boolean isLocationPromptPresent() {
        WebDriverWait wait = new WebDriverWait(getDriver(), DELAY);
        try {
            if (wait.until(ExpectedConditions.visibilityOfElementLocated
                    (MobileBy.AccessibilityId(LOCATION_ALERT_ACCESSIBILITY_ID))).isDisplayed()) {
                Screenshot.capture(getDriver(), "Location Prompt");
                LOGGER.info("Location Alert is Present");
                return true;
            }
        } catch (TimeoutException e) {
            LOGGER.info("Alert was not found \n", e);
        }
        return false;
    }

    public void refreshPage() {
        LOGGER.info("Scrolling up to refresh page");
        new IOSUtils().scrollUp();
    }

    public void getTab(TabOptions tabName) {
        LOGGER.info("Searching for " + tabName + " footer tab");
        switch (tabName) {
            case WATCH:
                do {
                    getPageFluently(watchTab, 3, 5, 1);
                } while (!initPage(EspnWatchIOSPageBase.class)
                        .isOpened());
                break;
            case HOME:
                do {
                    getPageFluently(HOME_TAB_BTN_ACCESSIBILITY_ID, 3, 5, 1);
                } while (!initPage(EspnHomeIOSPageBase.class)
                        .isOpened());
                break;
            case SCORES:
                do {
                    getPageFluently(SCORES_TAB_BTN_ACCESSIBILITY_ID, 3, 5, 1);
                } while (!initPage(EspnScoresIOSPageBase.class)
                        .isOpened());
                break;
            case LISTEN:
                do {
                    getPageFluently(LISTEN_TAB_BTN_ACCESSIBILITY_ID, 3, 5, 1);
                } while (!initPage(EspnListenIOSPageBase.class)
                        .isOpened());
                break;
            case SPORTS:
                do {
                    getPageFluently(SPORTS_TAB_BTN_ACCESSIBILITY_ID, 3, 5, 1);
                } while (!initPage(EspnSportsIOSPageBase.class)
                        .isOpened());
                break;
            default:
                LOGGER.error(tabName + " is invalid tab");
        }
    }

    public void getSettingsPage() {
        getPageFluently(SETTINGS_BTN_ACCESSIBILITY_ID, 3, 5, 1);
    }

    public void handleAlert(String type) {
        WebDriverWait wait = new WebDriverWait(getDriver(), DELAY);
        DriverHelper driver = new DriverHelper(getDriver());
        switch (type) {
            case LOCATION_ALERT:
                try {
                    if (wait.until(ExpectedConditions.visibilityOfElementLocated
                            (MobileBy.AccessibilityId(LOCATION_ALERT_ACCESSIBILITY_ID))).isDisplayed()) {
                        Screenshot.capture(getDriver(), String.format(type, "%s Prompt"));
                        LOGGER.info("Location Alert is Present");
                        driver.acceptAlert();
                    }
                } catch (TimeoutException exception) {
                    LOGGER.info("Location Alert is not present", exception);
                }
                break;
            case NOTIFICATION_ALERT:
                try {
                    if (wait.until(ExpectedConditions.visibilityOfElementLocated
                            (MobileBy.AccessibilityId(NOTIFICATION_ALERT_ACCESSIBILITY_ID))).isDisplayed()) {
                        Screenshot.capture(getDriver(), String.format(type, "%s Prompt"));
                        LOGGER.info("Notification Alert is Present");
                        driver.acceptAlert();
                    }
                } catch (TimeoutException exception) {
                    LOGGER.info("Notification Alert is not present", exception);
                }
                break;
            case CUSTOMIZATION_ALERT:
                try {
                    if (getDriver().findElement(MobileBy.AccessibilityId(CUSTOMIZATION_ALERT_ACCESSIBILITY_ID)).isDisplayed()) {
                        LOGGER.info("Handling customization alert");
                        acceptAlert();
                    }
                } catch (Exception ex) {
                    LOGGER.info("Unable to locate Customization Alert with exception: ", ex);
                }
                break;
            default:
                LOGGER.error(type + " is invalid alert type");
        }
    }

    public EspnWatchIOSPageBase logInNavigateToWatchPage(String user) {

        handleAlert(LOCATION_ALERT);

        new IOSUtils().handleAlert(IOSUtils.AlertButtonCommand.DISMISS);

        initPage(EspnLandingIOSPageBase.class).getEditionPage().getFavoriteLeaguePage().getFavoriteTeamsSelectionPage().getHomePage();

        handleAlert(CUSTOMIZATION_ALERT);

        EspnIOSPageBase espnIOSPageBase = initPage(EspnIOSPageBase.class);

        espnIOSPageBase.getWatchPageFirstTry(1);

        EspnPaywallIOSPageBase espnPaywallIOSPageBase = initPage(EspnPaywallIOSPageBase.class);

        freeTrialBtn.clickIfPresent(5);

        espnPaywallIOSPageBase.waitForLoginPage();

        espnPaywallIOSPageBase.getLogInPage();

        initPage(EspnPaywallLogInIOSPageBase.class).login(user);

        return initPage(EspnWatchIOSPageBase.class);
    }

    public void navigateToSearchWithLogin(String user) {
        handleAlert(LOCATION_ALERT);

        new IOSUtils().handleAlert(IOSUtils.AlertButtonCommand.DISMISS);

        initPage(EspnLandingIOSPageBase.class).getLogInPage();

        initPage(EspnPaywallLogInIOSPageBase.class).login(user);

        initPage(EspnEditionIOSPageBase.class).getFavoriteLeaguePage().getFavoriteTeamsSelectionPage();

        handleAlert(CUSTOMIZATION_ALERT);

        initPage(EspnIOSPageBase.class).getTab(WATCH);

        searchBtn.click();
    }

    public EspnWatchIOSPageBase navigateToWatchPageWithoutLogin() {

        handleAlert(LOCATION_ALERT);

        handleAlert(NOTIFICATION_ALERT);

        initPage(EspnLandingIOSPageBase.class).getEditionPage();

        initPage(EspnFavoriteLeagueSelectionIOSPageBase.class)
                .getFavoriteTeamsSelectionPage();

        initPage(EspnFavoriteTeamsSelectionIOSPageBase.class).getHomePage();

        handleAlert(CUSTOMIZATION_ALERT);

        initPage(EspnIOSPageBase.class).getTab(WATCH);

        EspnPaywallIOSPageBase espnPaywallIOSPageBase = initPage(EspnPaywallIOSPageBase.class);

        espnPaywallIOSPageBase.waitForLoginPage();

        espnPaywallIOSPageBase.dismissPaywall();

        return initPage(EspnWatchIOSPageBase.class);
    }

    public EspnHomeIOSPageBase navigateToHomePageWithoutLogin() {

        handleAlert(LOCATION_ALERT);

        handleAlert(NOTIFICATION_ALERT);

        initPage(EspnLandingIOSPageBase.class)
                .getEditionPage()
                .getFavoriteLeaguePage()
                .getFavoriteTeamsSelectionPage()
                .getHomePage();

        handleAlert(CUSTOMIZATION_ALERT);

        return initPage(EspnHomeIOSPageBase.class);
    }


}