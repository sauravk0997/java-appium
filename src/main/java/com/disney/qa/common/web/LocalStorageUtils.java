package com.disney.qa.common.web;

import com.qaprosoft.carina.core.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class LocalStorageUtils extends AbstractPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public LocalStorageUtils(WebDriver driver) {
        super(driver);
    }

    public void removeItemFromLocalStorage(String item) {
        LOGGER.info(String.format("Remove '%s' from local storage", item));
        trigger(String.format(
                "window.localStorage.removeItem('%s');", item));
    }

    public boolean isItemPresentInLocalStorage(String item) {
        LOGGER.info(String.format("Verify '%s' is present", item));
        return (trigger(String.format(
                "return window.localStorage.getItem('%s');", item)) != null);
    }

    public String getItemFromLocalStorage(String item) {
        LOGGER.info(String.format("Get '%s' from local storage", item));
        return (String) trigger(String.format(
                "window.localStorage.getItem('%s');", item));
    }

    public String getKeyFromLocalStorage(int key) {
        LOGGER.info(String.format("Get '%s' key", key));
        return (String) trigger(String.format(
                "return window.localStorage.key('%s');", key));
    }

    public Long getLocalStorageLength() {
        LOGGER.info("Get local storage length");
        return (Long) trigger("return window.localStorage.length;");
    }

    public void setItemInLocalStorage(String item, String value) {
        LOGGER.info(String.format("Set key:'%s' value:'%s'", item, value));
        trigger(String.format(
                "window.localStorage.setItem('%s','%s');", item, value));
    }

    public void clearLocalStorage() {
        LOGGER.info("Clear local storage");
        trigger("window.localStorage.clear();");
    }
}
