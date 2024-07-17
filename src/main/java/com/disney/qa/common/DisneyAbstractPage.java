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
    protected static final int ONE_SEC_TIMEOUT = 1;
    protected static final int SHORT_TIMEOUT = 3;
    protected static final int HALF_TIMEOUT = 5;
    protected static final int TEN_SEC_TIMEOUT = 10;
    protected static final int FIFTEEN_SEC_TIMEOUT = 15;
    protected static final int FORTY_FIVE_SEC_TIMEOUT = 45;
    protected static final int LONG_TIMEOUT = 60;
    protected static final long EXTRA_LONG_TIMEOUT = 300;
    public static final int FIFTEEN_HUNDRED_TIMEOUT = 1500;

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
