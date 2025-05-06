package com.disney.qa.disney.apple.pages.tv;

import com.disney.config.DisneyParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.google.common.collect.ImmutableMap;
import com.zebrunner.carina.utils.appletv.RemoteControlKeyword;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.stream.IntStream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVCommonPage extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusAppleTVCommonPage(WebDriver driver) {
        super(driver);
    }

    public String getDisplayedDurationFromMS(int timeInMs) {
        long hour = Math.floorDiv(timeInMs, 3_600_000);
        long min = Math.round((timeInMs % 3_600_000.0) / 60_000.0);
        if (hour == 0) return min + "m";
        return hour + "h " + min + "m";
    }

    public static boolean isProd() {
        return DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).equalsIgnoreCase("prod");
    }

    public void pressButtonForDuration(RemoteControlKeyword keyword, int duration) {
        ((JavascriptExecutor) getDriver()).executeScript("mobile: pressButton",
                ImmutableMap.of("name", keyword.getControlKeyword(), "durationSeconds", duration));
        String controlKeywordName = keyword.name();
        LOGGER.info("TV OS RemoteController '{}' clicked", controlKeywordName);
    }

    public void clickRight(int duration) {
        pressButtonForDuration(RemoteControlKeyword.RIGHT, duration);
    }

    public void clickRight(int times, int timeout, int duration) {
        IntStream.range(0, times).forEach(i -> {
            clickRight(duration);
            pause(timeout);
        });
    }

    public void clickRightTillEndOfPlaybackIsReached(
            ExtendedWebElement currentPosition, int attempts, int timeout, int duration) {
        String previousPositionTimestamp;
        do {
            previousPositionTimestamp = currentPosition.getAttribute(VALUE);
            clickRight(duration);
            pause(timeout);
            attempts--;
        } while (attempts > 0 && !currentPosition.getAttribute(VALUE).equals(previousPositionTimestamp) );
    }

    public void clickLeft(int duration) {
        pressButtonForDuration(RemoteControlKeyword.LEFT, duration);
    }

    public void clickLeft(int times, int timeout, int duration) {
        IntStream.range(0, times).forEach(i -> {
            clickLeft(duration);
            pause(timeout);
        });
    }

    public void clickLeftTillBeginningOfPlaybackIsReached(
            ExtendedWebElement currentPosition, int attempts, int timeout, int duration) {
        do {
            clickLeft(duration);
            pause(timeout);
            attempts--;
        } while (attempts > 0 && !currentPosition.getAttribute(VALUE).equals("0:00") );
    }

    public void clickUp(int duration) {
        pressButtonForDuration(RemoteControlKeyword.UP, duration);
    }

    public void clickDown(int duration) {
        pressButtonForDuration(RemoteControlKeyword.DOWN, duration);
    }

    public void clickMenu(int duration) {
        pressButtonForDuration(RemoteControlKeyword.MENU, duration);
    }

    public void clickPlay(int duration) {
        pressButtonForDuration(RemoteControlKeyword.PLAY, duration);
    }

    public void clickSelect(int duration) {
        pressButtonForDuration(RemoteControlKeyword.SELECT, duration);
    }

    public void goRightTillLocalizedEndPageAppears(int attempts, int duration, int pauseNum, ExtendedWebElement element) {
        while (!element.isElementPresent() && attempts > 0) {
            LOGGER.info("click Right for duration {} ", duration);
            clickRight(duration);
            LOGGER.info("pause for {} seconds since player is not respecting duration..", pauseNum);
            pause(pauseNum);
            attempts--;
        }
    }

    public void goRightOnPlayerForDuration(int attempts, int duration, int pauseNum) {
        while (attempts > 0) {
            LOGGER.info("click Right for duration {} ", duration);
            clickRight(duration);
            LOGGER.info("pause for {} seconds since player is not respecting duration..", pauseNum);
            pause(pauseNum);
            attempts--;
        }
    }
}
