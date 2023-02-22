package com.disney.qa.disney.web.appex;

import com.disney.qa.common.web.LocalStorageUtils;
import com.disney.qa.disney.web.common.DisneyPlusBasePage;
import com.disney.qa.disney.web.entities.TestContext;
import com.disney.qa.disney.web.entities.WebConstant;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyAppExUtil extends DisneyPlusBasePage {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyAppExUtil(WebDriver driver) {
        super(driver);
    }

    public DisneyAppExUtil setPlayerQaMode() {
        LocalStorageUtils localStorage = new LocalStorageUtils(getDriver());
        localStorage.setItemInLocalStorage("enableQaMode", "true");
        Assert.assertTrue(localStorage.isItemPresentInLocalStorage("enableQaMode"), "Expected 'enableQaMode' to be set");
        return this;
    }

    public DisneyAppExUtil verifyValueIncreased(String initialValue, String finalValue, String message) {
        LOGGER.info("Check if value increased...");
        Assert.assertTrue(Integer.parseInt(
                        TestContext.getvalue(finalValue).toString())
                        > Integer.parseInt(TestContext.getvalue(initialValue).toString()),
                message);
        return this;
    }

    public DisneyAppExUtil verifyValueDecreased(String initialValue, String finalValue, String message) {
        LOGGER.info("Check if value decreased...");
        Assert.assertTrue(Integer.parseInt(
                        TestContext.getvalue(initialValue).toString())
                        > Integer.parseInt(TestContext.getvalue(finalValue).toString()),
                message);
        return this;
    }
    public DisneyAppExUtil scrollToTop() {
        LOGGER.info("Scroll to the top of the screen");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0,0)");
        return this;
    }

    public boolean isAttributePresent(ExtendedWebElement extendedWebElement, String attribute) {
        Boolean result = false;
        String value = extendedWebElement.getAttribute(attribute);
        if (value != null) {
            result = true;
        }
        return result;
    }

    public int generateRandomNumber(int i) {
        LOGGER.info("Generate a random number from 0 to i-1");
        return new SecureRandom().nextInt(i);
    }

    public String formatDate(String currentFormat, String date, String expectedFormat) {
        LOGGER.info("Change the format of date");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(currentFormat);
        Date d = new Date();
        try {
            d = simpleDateFormat.parse(date);
        }
        catch(ParseException p) {
            LOGGER.info("Date format failed with exception");
        }
        return new SimpleDateFormat(expectedFormat).format(d);
    }

    /**
     * Generate DOB as a date from current date
     * minus yearsToSubtract to be able to generate
     * primary profile with 18years, ineligible DOB with more than 125 years
     * DOB for any other profile that need not be either
     */
    public String generateDOB(int yearsToSubtract) {
        LOGGER.info("Generate DOB");
        LocalDate localDate = LocalDate.now().minusYears(yearsToSubtract);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(WebConstant.DATE_FORMAT_TWO);
        return simpleDateFormat.format(java.sql.Date.valueOf(localDate));
    }

    public String getCurrentDate() {
        LOGGER.info("Get current date");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(WebConstant.DATE_FORMAT_TWO);
        return simpleDateFormat.format(java.sql.Date.valueOf(LocalDate.now()));
    }
}
