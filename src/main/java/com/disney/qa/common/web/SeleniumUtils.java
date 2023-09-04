package com.disney.qa.common.web;

import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.SkipException;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * Created by bogdan.zayats on 8/22/16.
 */
public class SeleniumUtils extends AbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    protected static final long LONG_TIMEOUT = 60;

    @FindBy(css = "body")
    private ExtendedWebElement body;

    public SeleniumUtils(WebDriver driver) {
        super(driver);
    }

    /** Direction of scroll **/
    public enum ScrollDirection {
        UP, DOWN, RIGHT, LEFT;
    }

    /** Open new browser tab **/
    public void openNewTab(){
        getDriver().findElement(body.getBy()).sendKeys(Keys.COMMAND + "t");
    }

    /** switch to a browers new tab **/
    public void switchToNewBrowserTab(){
        /* Selenium does not support working with multi-tabs, therefore, windows
        *  handles will always return 1.
        * */

        ArrayList<String> tabs = new ArrayList(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(tabs.size()-1));
    }

    /** Resize browser window **/
    public void setBrowserSize(int width, int height) {
        Dimension d = new Dimension(width, height);
        driver.manage().window().setSize(d);
        LOGGER.info("Window resized to " + width + " x " + height);
    }

    /** Skip test defined by Environment **/
    public void skipTestbyEnvironment(String env) {
        String environment = R.CONFIG.get("env");

        if (environment.equalsIgnoreCase(env)) {
            throw new SkipException(String.format("Skipping test due in %s environment", env));
        }
    }


    /** wait for all items on a page load **/
    public void waitUntilDOMready(){
        LOGGER.info("Starting waitUntil DOM ready...");
        waitUntil(ExpectedConditions.jsReturnsValue("return document.readyState=='complete';"), LONG_TIMEOUT * 2);
        LOGGER.info("Finished waitUntil DOM ready.");
    }

    /** Retrieve Window X Y Offsets **/

    public Dimension getPageOffsets() {

        int xOffset = Integer.parseInt(trigger("return window.pageXOffset;").toString());
        int yOffset = Integer.parseInt(trigger("return window.pageYOffset;").toString());

        return new Dimension(xOffset, yOffset);

    }

    /** scroll to top **/

    public void scrolltoTop() {
        trigger("window.scrollTo(0, -document.body.scrollHeight)");
    }

    /** scroll to bottom **/

    public void scrollToBottom() {
        trigger("window.scrollTo(0, document.body.scrollHeight)");
    }

    /*
     * Scroll down the entire page in view
     *
     * @param numberOfTimes: Amount of times to perform scroll action
     * @param direction: The direction of scroll
     */
    public void scroll(ScrollDirection direction, int numberOfTimes) {
        scroll(0,0, direction, numberOfTimes);
    }

    /**
     * Scroll by pixel.
     * To scroll up, use negative co-ordinate.
     *
     * @param x co-ordinate
     * @param y co-ordinate
     */
    public void scroll(int x, int y) {
        scroll(x, y, null, 0);
    }

    private void scroll(int x, int y, ScrollDirection direction, int numberOfTimes) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        if(x != 0 && y != 0) {
            LOGGER.info(String.format("Scrolling to co-ordinates (%d, %d)", x, y));
            jsExecutor.executeScript("window.scrollBy(" + x + "," + y + ")", "");
        } else {
            for(int i = 1; i <= numberOfTimes; i++) {
                if(direction.equals(ScrollDirection.RIGHT) || direction.equals(ScrollDirection.LEFT)) {
                    LOGGER.error("Right and Left scroll not supported!");
                    break;
                }
                if(direction.equals(ScrollDirection.UP)) {
                    LOGGER.info(String.format("Scrolling up (number of times: %d)", numberOfTimes));
                    jsExecutor.executeScript("window.scrollBy(0, " + -getBrowserSize().height + ")", "");
                } else {
                    LOGGER.info(String.format("Scrolling down (number of times: %d)", numberOfTimes));
                    jsExecutor.executeScript("window.scrollBy(0, " + getBrowserSize().height + ")", "");
                }
            }
        }
    }

    public Dimension getBrowserSize() {
        return getDriver().manage().window().getSize();
    }

    /** Fluent Wait for Element **/

    public boolean waitAndCheckElementPresence(ExtendedWebElement element, int poll, int timeout) {

        try {
            FluentWait<WebDriver> wait = new FluentWait<>(getDriver());
            wait.pollingEvery(Duration.ofSeconds(poll));
            wait.withTimeout(Duration.ofSeconds(timeout));
            wait.ignoring(NoSuchElementException.class);
            wait.ignoring(TimeoutException.class);
            wait.ignoring(WebDriverException.class);

            Function<WebDriver, Boolean> waitCheck = arg0 -> {
                wait.until(ExpectedConditions.presenceOfElementLocated(element.getBy()));
                element.scrollTo();
                return element.isElementPresent();
            };

            return wait.until(waitCheck);

        } catch (TimeoutException e) {
            LOGGER.error("Timeout Exception encountered", e);
            return false;
        }
    }

    //Assertion to verify if current url contains desired text/
    public boolean verifyUrlText(String expectedUrlText) {
        LOGGER.info("URL contains: {}", expectedUrlText);
        String url = getDriver().getCurrentUrl();
        return url.contains(expectedUrlText);
    }

    public String createRandomAlphabeticString(int characterCount) {
        return RandomStringUtils.randomAlphabetic(characterCount);
    }

    @MethodOwner(owner = "shashem")
    public void getTestExecutionTime(ITestResult executeTime) {
        String methodName = executeTime.getMethod().getMethodName();
        long executionTime = (executeTime.getEndMillis() - executeTime.getStartMillis()) / 1000;
        LOGGER.info("Execution time for {}: {} seconds", methodName, executionTime);
    }

}
