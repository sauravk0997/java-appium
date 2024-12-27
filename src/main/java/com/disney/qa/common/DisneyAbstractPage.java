package com.disney.qa.common;

import com.disney.qa.common.utils.IOSUtils;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public abstract class DisneyAbstractPage extends AbstractPage implements IOSUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final int ONE_SEC_TIMEOUT = 1;
    public static final int THREE_SEC_TIMEOUT = 3;
    public static final int FIVE_SEC_TIMEOUT = 5;
    public static final int TEN_SEC_TIMEOUT = 10;
    public static final int FIFTEEN_SEC_TIMEOUT = 15;
    protected static final int TWENTY_FIVE_SEC_TIMEOUT = 25;
    public static final int FORTY_FIVE_SEC_TIMEOUT = 45;
    public static final int SIXTY_SEC_TIMEOUT = 60;
    public static final int ONE_HUNDRED_TWENTY_SEC_TIMEOUT = 120;
    protected static final long THREE_HUNDRED_SEC_TIMEOUT = 300;
    public static final int FIFTEEN_HUNDRED_SEC_TIMEOUT = 1500;

    public DisneyAbstractPage(WebDriver driver) {
        super(driver);
    }

    /**
     * @return true by default. Override it in child classes
     */
    public abstract boolean isOpened();

    public String getElementText(ExtendedWebElement element) {
        try {
            return element.getText();
        } catch (NoSuchElementException nse) {
            LOGGER.error("Element not found!");
            return "";
        }
    }

}
