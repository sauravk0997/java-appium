package com.disney.qa.disney.apple.pages.common;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusUpNextIOSPageBase extends DisneyPlusApplePageBase {

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
        fluentWait(getCastedDriver(), EXPLICIT_TIMEOUT, 0, "Unable to see 'See All Episodes' button")
                .until(it -> seeAllEpisodeButton.isElementPresent(SHORT_TIMEOUT));
        staticTextLabelName.format(seeAllEpisodeButton.getText()).click();
    }

    public void tapPlayIconOnUpNext() {
        LOGGER.info("Tapping on 'play' button");
        fluentWait(getCastedDriver(), EXPLICIT_TIMEOUT, 0, "Unable to see 'play' button")
                .until(it -> playButton.isElementPresent(SHORT_TIMEOUT));
        playButton.click();
    }

    public String getNextEpisodeInfo() {
        return contentTitleLabel.getText();
    }

    public void waitForUpNextUIToDisappear() {
        fluentWait(getCastedDriver(), EXPLICIT_TIMEOUT, 0, "Unable to start autoplay, check your autoplay settings in more menu")
                .until(it -> playButton.isElementNotPresent(LONG_TIMEOUT));
    }

    public boolean waitForUpNextUIToAppear() {
       return (fluentWait(getCastedDriver(), EXPLICIT_TIMEOUT, 0, "upNext UI didn't appear on video player")
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