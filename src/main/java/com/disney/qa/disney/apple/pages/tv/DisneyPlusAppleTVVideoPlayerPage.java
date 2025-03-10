package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusVideoPlayerIOSPageBase.class)
public class DisneyPlusAppleTVVideoPlayerPage extends DisneyPlusVideoPlayerIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusAppleTVVideoPlayerPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilDetailsPageIsLoadedFromTrailer(long timeout, int polling) {
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        fluentWait(getDriver(), timeout, polling, "Details page did not load after " + timeout)
                .until(it -> disneyPlusAppleTVDetailsPage.isOpened());
    }

    /**
     * Waits for content to end in player until Up Next End Card present.
     * Returns the object of DisneyPlusAppleTVVideoPlayerPage.
     * @param timeout
     * @param polling
     */
    public DisneyPlusAppleTVVideoPlayerPage waitForTvosContentToEnd(long timeout, int polling) {
        fluentWait(getDriver(), timeout, polling, "Up Next End Card did not load after " + timeout).until(it -> isUpNextHeaderPresent());
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    /**
     * Waits for trailer to end in player until video player is not open.
     * Returns the object of DisneyPlusAppleTVVideoPlayerPage.
     * @param timeout
     * @param polling
     */
    public DisneyPlusAppleTVVideoPlayerPage waitForTvosTrailerToEnd(long timeout, int polling) {
        fluentWait(getDriver(), timeout, polling, "Trailer did not end after " + timeout).until(it -> !isOpened());
        return new DisneyPlusAppleTVVideoPlayerPage(getDriver());
    }

    public void pauseAndPlayVideo() {
        LOGGER.info("Pause video..");
        clickSelect();
        pause(1);
        LOGGER.info("Play video..");
        clickSelect();
    }

    @Override
    public String getTitleLabel() {
        LOGGER.info("Pause/play player to see title..");
        clickSelect();
        return titleLabel.getText();
    }

    public DisneyPlusVideoPlayerIOSPageBase displayVideoController() {
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        LOGGER.info("Activating video player controls...");
        commonPage.clickDown(2);
        //Check is due to placement of PlayPause, which will pause the video if clicked
        fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, FIVE_SEC_TIMEOUT, "Seek bar is present").until(it -> !seekBar.isPresent(ONE_SEC_TIMEOUT));
        int attempts = 0;
        do {
            commonPage.clickDown(2);
        } while (attempts++ < 5 && !seekBar.isElementPresent(THREE_SEC_TIMEOUT));
        if (attempts == 6) {
            Assert.fail("Seek bar was present and attempts exceeded over 5.");
        }
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public boolean isServiceAttributionLabelVisibleWithControls() {
        displayVideoController();
        return getServiceAttributionLabel().isPresent();
    }
}
