package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.*;
import com.disney.qa.disney.apple.pages.common.DisneyPlusVideoPlayerIOSPageBase;
import com.disney.qa.disney.dictionarykeys.*;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusVideoPlayerIOSPageBase.class)
public class DisneyPlusAppleTVVideoPlayerPage extends DisneyPlusVideoPlayerIOSPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeButton[`name == 'restartButton'`]")
    private ExtendedWebElement restartBtn;

    public DisneyPlusAppleTVVideoPlayerPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getRestartBtn() {
        return restartBtn;
    }

    @Override
    public ExtendedWebElement getNextEpisodeButton() {
        return getTypeButtonByLabel(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.APPLICATION,
                DictionaryKeys.BTN_NEXT_EPISODE.getText()));
    }

    public void waitUntilDetailsPageIsLoadedFromTrailer(long timeout, int polling) {
        DisneyPlusAppleTVDetailsPage disneyPlusAppleTVDetailsPage = new DisneyPlusAppleTVDetailsPage(getDriver());
        fluentWait(getDriver(), timeout, polling, "Details page did not load after " + timeout)
                .until(it -> disneyPlusAppleTVDetailsPage.isOpened());
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
        clickPlay();
        return titleLabel.getText();
    }

    @Override
    public DisneyPlusVideoPlayerIOSPageBase displayVideoController() {
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        LOGGER.info("Activating video player controls...");
        fluentWait(getDriver(), FIFTEEN_SEC_TIMEOUT, FIVE_SEC_TIMEOUT, "Seek bar is present")
                .until(it -> !seekBar.isPresent(ONE_SEC_TIMEOUT));
        int attempts = 0;
        do {
            commonPage.clickDown(2);
        } while (attempts++ < 5 && !seekBar.isElementPresent(ONE_SEC_TIMEOUT));
        if (attempts == 6) {
            Assert.fail("Seek bar was present and attempts exceeded over 5.");
        }
        return initPage(DisneyPlusVideoPlayerIOSPageBase.class);
    }

    public boolean isThumbnailAlignedWithTheSeekBar() {
        int seekBarLeftXCoordinate = seekBar.getLocation().getX();
        int thumbnailLeftXCoordinate = thumbnailView.getLocation().getX();

        return  seekBarLeftXCoordinate == thumbnailLeftXCoordinate;
    }

    public boolean isThumbnailAlignedWithTheEndOfTheSeekBar() {
        int seekBarRightXCoordinate = seekBar.getLocation().getX() + seekBar.getSize().getWidth();
        int thumbnailRightXCoordinate = thumbnailView.getLocation().getX() + thumbnailView.getSize().getWidth();

        return  seekBarRightXCoordinate == thumbnailRightXCoordinate;
    }

    public void tapFwdToPlaybackPercentage(int remainingTime, double playbackPercent, int maxTapCount) {
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        LOGGER.info("Setting video playback to {}% started...", playbackPercent);
        double percentageExpectedRemainingTime = (remainingTime * (playbackPercent / 100));

        do {
            commonPage.clickRight(1, 2, 1);
        } while (getCurrentTime() < percentageExpectedRemainingTime && maxTapCount-- > 0);
        LOGGER.info("Setting video playback to {}% completed...", playbackPercent);
    }

    @Override
    public boolean isServiceAttributionLabelVisibleWithControls() {
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        commonPage.clickDown(1);
        return getServiceAttributionLabel().isPresent();
    }

    @Override
    public ExtendedWebElement getBroadcastMenu() {
        return dynamicBtnFindByLabel.format(broadcastMenuButton);
    }

    public void clickBroadcastMenu() {
        DisneyPlusAppleTVCommonPage commonPage = new DisneyPlusAppleTVCommonPage(getDriver());
        ExtendedWebElement broadcastMenu = getBroadcastMenu();
        commonPage.clickPlay();
        commonPage.clickUp(1);
        commonPage.clickRight(2, 1, 1);
        broadcastMenu.click();
    }

    @Override
    public boolean isFeedOptionSelected(String feedOption) {
        return feedOptionCheckmark.format(feedOption).getAttribute(Attributes.VALUE.getAttribute()).equals("1");
    }
}
