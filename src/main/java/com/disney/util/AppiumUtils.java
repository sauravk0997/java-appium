package com.disney.util;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.remote.SupportsContextSwitching;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.disney.qa.common.DisneyAbstractPage;
import com.zebrunner.carina.utils.android.IAndroidUtils;
import com.zebrunner.carina.utils.mobile.IMobileUtils;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;

import io.appium.java_client.AppiumBy;

/**
 * Created by bogdan.zayats on 01/10/18.
 */
public class AppiumUtils extends DisneyAbstractPage implements IMobileUtils {
    private static final int MAX_SEARCH_SWIPES = 55;
    private static final long TIMEOUT = 300;


    public AppiumUtils(WebDriver driver) {
        super(driver);
    }

    /**
     * Scrolls into view in specified container by text only and return boolean
     *
     * @param container  ExtendedWebElement - defaults to id Selector Type
     * @param scrollToElement String defaults to text Selector Type
     * @return boolean
     * <p>
     * example of usage:
     * ExtendedWebElement res = AndroidUtils.scroll("News", newsListContainer);
     **/
    public boolean scroll(String scrollToElement, ExtendedWebElement container) {
        return scroll(scrollToElement, container, IAndroidUtils.SelectorType.ID, IAndroidUtils.SelectorType.TEXT);
    }

    /** Scrolls into view in a container specified by it's instance (index)
     * @param scrollToEle - has to be id, text, contentDesc or className
     * @param scrollableContainer - ExtendedWebElement type
     * @param containerSelectorType - has to be id, text, textContains, textStartsWith, Description, DescriptionContains
     *                             or className
     * @param eleSelectorType -  has to be id, text, textContains, textStartsWith, Description, DescriptionContains
     *                             or className
     * @return boolean
     * <p>
     * example of usage:
     * ExtendedWebElement res = AndroidUtils.scroll("News", newsListContainer, AndroidUtils.SelectorType.CLASS_NAME, 1,
     *                          AndroidUtils.SelectorType.TEXT);
     **/
    public boolean scroll(String scrollToEle, ExtendedWebElement scrollableContainer, IAndroidUtils.SelectorType containerSelectorType,
                          int containerInstance, IAndroidUtils.SelectorType eleSelectorType) {
        boolean res = false;
        long startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        for (int i = 0; i < MAX_SEARCH_SWIPES; i++) {

            try {
                WebElement ele = getDriver().findElement(AppiumBy.androidUIAutomator("new UiScrollable(" +
                        getScrollContainerSelector(scrollableContainer, containerSelectorType) +
                        ".instance(" + containerInstance + "))"+
                        ".setMaxSearchSwipes(" + MAX_SEARCH_SWIPES + ")" + ".scrollIntoView(" +
                        getScrollToElementSelector(scrollToEle, eleSelectorType) + ")"));
                if (ele.isDisplayed()) {
                    LOGGER.info("Element found!!!");
                    res = true;
                    break;
                }
            } catch (NoSuchElementException noSuchElement) {
                LOGGER.error(String.format("Element %s:%s was NOT found.", eleSelectorType, scrollToEle), noSuchElement);
            }

            for (int j = 0; j < i; j++) {
                checkTimeout(startTime);

                AppiumBy.androidUIAutomator("new UiScrollable(" +
                        getScrollContainerSelector(scrollableContainer, containerSelectorType)
                        + ".instance("+ containerInstance + ")).scrollForward()");
                LOGGER.info("Scroller got stuck on a page, scrolling forward to next page of elements..");
            }
        }

        return res;
    }

    /** Scrolls into view in specified container
     * @param scrollToEle - has to be id, text, contentDesc or className
     * @param scrollableContainer - ExtendedWebElement type
     * @param containerSelectorType - has to be id, text, textContains, textStartsWith, Description, DescriptionContains
     *                             or className
     * @param containerInstance - has to an instance number of desired container
     * @param eleSelectorType -  has to be id, text, textContains, textStartsWith, Description, DescriptionContains
     *                             or className
     * @param eleSelectorInstance - has to an instance number of desired container
     * @return boolean
     * <p>
     * example of usage:
     * ExtendedWebElement res = AndroidUtils.scroll("News", newsListContainer, AndroidUtils.SelectorType.CLASS_NAME, 1,
     *                          AndroidUtils.SelectorType.TEXT, 2);
     **/
    public boolean scroll(String scrollToEle, ExtendedWebElement scrollableContainer, IAndroidUtils.SelectorType containerSelectorType,
                          int containerInstance, IAndroidUtils.SelectorType eleSelectorType, int eleSelectorInstance) {
        boolean res = false;
        long startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        for (int i = 0; i < MAX_SEARCH_SWIPES; i++) {

            try {
                WebElement ele = getDriver().findElement(AppiumBy.androidUIAutomator("new UiScrollable(" +
                        getScrollContainerSelector(scrollableContainer, containerSelectorType) +
                        ".instance(" + containerInstance + "))" +
                        ".setMaxSearchSwipes(" + MAX_SEARCH_SWIPES + ")" + ".scrollIntoView(" +
                        getScrollToElementSelector(scrollToEle, eleSelectorType) + ".instance(" + eleSelectorInstance + "))"));
                if (ele.isDisplayed()) {
                    LOGGER.info("Element found!!!");
                    res = true;
                    break;
                }
            } catch (NoSuchElementException noSuchElement) {
                LOGGER.error(String.format("Element %s:%s was NOT found.", eleSelectorType, scrollToEle), noSuchElement);
            }

            for (int j = 0; j < i; j++) {
                checkTimeout(startTime);

                AppiumBy.androidUIAutomator("new UiScrollable(" +
                        getScrollContainerSelector(scrollableContainer, containerSelectorType)
                        + ".instance("+ containerInstance + ")).scrollForward()");
                LOGGER.info("Scroller got stuck on a page, scrolling forward to next page of elements..");
            }
        }

        return res;
    }

    /** Scrolls into view in specified container
     * @param scrollToEle - has to be id, text, contentDesc or className
     * @param scrollableContainer - ExtendedWebElement type
     * @param containerSelectorType - container Selector type: has to be id, text, textContains, textStartsWith, Description, DescriptionContains
     *                             or className
     * @param eleSelectorType -  scrollToEle Selector type: has to be id, text, textContains, textStartsWith, Description, DescriptionContains
     *                             or className
     * @return boolean
     * <p>
     * example of usage:
     * ExtendedWebElement res = AndroidUtils.scroll("News", newsListContainer, AndroidUtils.SelectorType.CLASS_NAME,
     *                          AndroidUtils.SelectorType.TEXT);
     **/
    public boolean scroll(String scrollToEle, ExtendedWebElement scrollableContainer, IAndroidUtils.SelectorType containerSelectorType,
                          IAndroidUtils.SelectorType eleSelectorType){
        boolean res = false;
        long startTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        for (int i = 0; i < MAX_SEARCH_SWIPES; i++) {

            try {
                WebElement ele = getDriver().findElement(AppiumBy.androidUIAutomator("new UiScrollable(" +
                        getScrollContainerSelector(scrollableContainer, containerSelectorType) + ")" +
                        ".setMaxSearchSwipes(" + MAX_SEARCH_SWIPES + ")" + ".scrollIntoView(" +
                        getScrollToElementSelector(scrollToEle, eleSelectorType) + ")"));
                if (ele.isDisplayed()) {
                    LOGGER.info("Element found!!!");
                    res = true;
                    break;
                }
            } catch (NoSuchElementException noSuchElement) {
                LOGGER.error(String.format("Element %s:%s was NOT found.", eleSelectorType, scrollToEle), noSuchElement);
            }

            for (int j = 0; j < i; j++) {
                checkTimeout(startTime);

                AppiumBy.androidUIAutomator("new UiScrollable(" +
                        getScrollContainerSelector(scrollableContainer, containerSelectorType) + ").scrollForward()");
                LOGGER.info("Scroller got stuck on a page, scrolling forward to next page of elements..");
            }
        }

        return res;
    }

    /** Scrolls into view in specified container
     * @param scrollableContainer - ExtendedWebElement type
     * @param containerSelectorType - Selector type: has to be id, text, contentDesc or className
     * @return boolean
     * <p>
     **/
    private String getScrollContainerSelector(ExtendedWebElement scrollableContainer, IAndroidUtils.SelectorType containerSelectorType){
        LOGGER.debug(scrollableContainer.getBy().toString());
        String scrollableContainerBy;
        String scrollViewContainerFinder = "";

        switch (containerSelectorType){
            case TEXT:
                scrollableContainerBy = scrollableContainer.getBy().
                        toString().replace("By.text:", "").trim();
                scrollViewContainerFinder = "new UiSelector().text(\"" + scrollableContainerBy + "\")";
                break;
            case TEXT_CONTAINS:
                scrollableContainerBy = scrollableContainer.getBy().
                        toString().replace("By.textContains:", "").trim();
                scrollViewContainerFinder = "new UiSelector().textContains(\"" + scrollableContainerBy + "\")";
                break;
            case TEXT_STARTS_WITH:
                scrollableContainerBy = scrollableContainer.getBy().
                        toString().replace("By.textStartsWith:", "").trim();
                scrollViewContainerFinder = "new UiSelector().textStartsWith(\"" + scrollableContainerBy + "\")";
                break;
            case ID:
                scrollableContainerBy = scrollableContainer.getBy().
                        toString().replace("By.id:", "").trim();
                scrollViewContainerFinder = "new UiSelector().resourceId(\"" + scrollableContainerBy + "\")";
                break;
            case DESCRIPTION:
                scrollableContainerBy = scrollableContainer.getBy().
                        toString().replace("By.description:", "").trim();
                scrollViewContainerFinder = "new UiSelector().description(\"" + scrollableContainerBy + "\")";
                break;
            case DESCRIPTION_CONTAINS:
                scrollableContainerBy = scrollableContainer.getBy().
                        toString().replace("By.descriptionContains:", "").trim();
                scrollViewContainerFinder = "new UiSelector().descriptionContains(\"" + scrollableContainerBy + "\")";
                break;
            case CLASS_NAME:
                scrollableContainerBy = scrollableContainer.getBy().
                        toString().replace("By.className:", "").trim();
                scrollViewContainerFinder = "new UiSelector().className(\"" + scrollableContainerBy + "\")";
                break;
            default:
                LOGGER.info("Please provide valid selectorType for element to be found...");
                break;
        }

        return scrollViewContainerFinder;

    }

    /** Scrolls into view in specified container
     * @param scrollToEle - String type
     * @param eleSelectorType - Selector type: has to be id, text, contentDesc or className
     * @return String
     * <p>
     **/
    private String getScrollToElementSelector(String scrollToEle, IAndroidUtils.SelectorType eleSelectorType){
        String neededElementFinder = "";
        String scrollToEleTrimmed = "";

        switch (eleSelectorType){
            case TEXT:
                neededElementFinder= "new UiSelector().text(\"" + scrollToEle + "\")";
                break;
            case TEXT_CONTAINS:
                neededElementFinder = "new UiSelector().textContains(\"" + scrollToEle + "\")";
                break;
            case TEXT_STARTS_WITH:
                neededElementFinder = "new UiSelector().textStartsWith(\"" + scrollToEle + "\")";
                break;
            case ID:
                scrollToEleTrimmed = scrollToEle.replace("By.id:", "").trim();
                neededElementFinder = "new UiSelector().resourceId(\"" + scrollToEleTrimmed + "\")";
                break;
            case DESCRIPTION:
                neededElementFinder = "new UiSelector().description(\"" + scrollToEle + "\")";
                break;
            case DESCRIPTION_CONTAINS:
                neededElementFinder = "new UiSelector().descriptionContains(\"" + scrollToEle + "\")";
                break;
            case CLASS_NAME:
                scrollToEleTrimmed = scrollToEle.replace("By.className:", "").trim();
                neededElementFinder = "new UiSelector().className(\"" + scrollToEleTrimmed + "\")";
                break;
            default:
                LOGGER.info("Please provide valid selectorType for element to be found...");
                break;
        }

        return neededElementFinder;
    }

    /** switches to a webView **/
    public void switchToWebview() {
        LOGGER.info(StringUtils.join(getDriver().getWindowHandles(), ","));
        Set<String> contextNames = ((SupportsContextSwitching) getDriver()).getContextHandles();
        for (String contextName : contextNames) {
            LOGGER.info("Available contexts: " + contextName);
            if (contextName.contains("WEBVIEW")){
                ((SupportsContextSwitching) getDriver()).context(contextName);
                LOGGER.info("Switched to " + contextName);
                break;
            }
        }
    }

    /** Scroll > Timeout check **/
    public void checkTimeout(long startTime){
        long elapsed = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())-startTime;

        if (elapsed > TIMEOUT) {
            throw new NoSuchElementException("Scroll timeout has been reached..");
        }
    }

    @Override
    public boolean isOpened(){
        return true;
    }
}
