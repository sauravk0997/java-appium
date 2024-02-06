package com.disney.qa.disney.apple.pages.common;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusUpNextIOSPageBase extends DisneyPlusApplePageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    //LOCATORS
    @ExtendedFindBy(accessibilityId = "upNextImageView")
    private ExtendedWebElement upNextImageView;
    @ExtendedFindBy(accessibilityId = "nextEpisodeHeader")
    private ExtendedWebElement nextEpisodeHeader;
    @ExtendedFindBy(accessibilityId = "contentTitleLabel")
    private ExtendedWebElement contentTitleLabel;
    @ExtendedFindBy(accessibilityId = "countdownProgressButton")
    private ExtendedWebElement playButton;
    @ExtendedFindBy(accessibilityId = "seeAllEpisodeButton")
    private ExtendedWebElement seeAllEpisodeButton;
    @ExtendedFindBy(accessibilityId = "restartButton")
    private ExtendedWebElement restartButton;
    @ExtendedFindBy(accessibilityId = "ucp.fastForward")
    private ExtendedWebElement fastForward;

    //FUNCTIONS
    public DisneyPlusUpNextIOSPageBase(WebDriver driver) {
        super(driver);
    }


    @Override
    public boolean isOpened() {
        return isUpNextViewPresent();
    }

    public boolean verifyUpNextUI() {
        LOGGER.info("Verifying Up Next UI on video player screen..");
        return isUpNextViewPresent() &&
                isNextEpisodeInfoPresent() &&
                seeAllEpisodeButton.isElementPresent();
    }
    public void tapSeeAllEpisodesButton() {
        LOGGER.info("Tapping on 'See All Episodes' button");
        fluentWait(getDriver(), getDefaultWaitTimeout().toSeconds(), 0, "Unable to see 'See All Episodes' button")
                .until(it -> seeAllEpisodeButton.isElementPresent(SHORT_TIMEOUT));
        staticTextLabelName.format(seeAllEpisodeButton.getText()).click();
    }

    public void tapPlayIconOnUpNext() {
        LOGGER.info("Tapping on 'play' button");
        fluentWait(getDriver(), getDefaultWaitTimeout().toSeconds(), 0, "Unable to see 'play' button")
                .until(it -> playButton.isElementPresent(SHORT_TIMEOUT));
        playButton.click();
    }

    public String getNextEpisodeInfo() {
        return contentTitleLabel.getText();
    }

    public void waitForUpNextUIToDisappear() {
        fluentWait(getDriver(), getDefaultWaitTimeout().toSeconds(), 0, "Unable to start autoplay, check your autoplay settings in more menu")
                .until(it -> playButton.isElementNotPresent(LONG_TIMEOUT));
    }

    public boolean waitForUpNextUIToAppear() {
       return (fluentWait(getDriver(), getDefaultWaitTimeout().toSeconds(), 0, "upNext UI didn't appear on video player")
                .until(it -> upNextImageView.isElementPresent(LONG_TIMEOUT)));
    }

    public boolean isUpNextViewPresent() {
        return upNextImageView.isElementPresent() &&
                playButton.isElementPresent();
    }

    private boolean isNextEpisodeInfoPresent() {
        return nextEpisodeHeader.isElementPresent() &&
        contentTitleLabel.isElementPresent();
    }
}