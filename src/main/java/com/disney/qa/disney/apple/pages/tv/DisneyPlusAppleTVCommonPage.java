package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.google.common.collect.ImmutableMap;
import com.qaprosoft.carina.core.foundation.utils.appletv.RemoteControlKeyword;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVCommonPage extends DisneyPlusApplePageBase {

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
    }

    public void clickRight(int duration) {
        pressButtonForDuration(RemoteControlKeyword.RIGHT, duration);
    }

    public void clickLeft(int duration) {
        pressButtonForDuration(RemoteControlKeyword.LEFT, duration);
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
}
