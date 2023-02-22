package com.disney.qa.disney.web.appex.playback;

import com.disney.qa.disney.web.DisneyMediaPlayerCommands;
import com.disney.qa.disney.web.appex.media.DisneyPlusVideoPlayerPage;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusPlaybackPage extends DisneyPlusVideoPlayerPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @FindBy(xpath = "//*[@data-testid='up-next-play-button']")
    private ExtendedWebElement playNextButton;

    public DisneyPlusPlaybackPage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getPlayNextButton(){
        return playNextButton;
    }

    public DisneyPlusPlaybackPage pauseMediaPlayer() {
        LOGGER.info("Pause Media");
        trigger(DisneyMediaPlayerCommands.MEDIA_PLAYER.getValue() + DisneyMediaPlayerCommands.PAUSE.getValue());
        return this;
    }

    public void mediaPlayerSeek(int seek){
        waitForPageToFinishLoading();
        trigger(DisneyMediaPlayerCommands.MEDIA_PLAYER.getValue() +
                String.format(DisneyMediaPlayerCommands.SEEK.getValue(), seek));
    }

    public boolean mediaSeekToEnd(int seekToEnd){
        int maxAttempts = 5;

        while(maxAttempts-- > 0 && !playNextButton.isElementPresent(DELAY)) {
            LOGGER.info("Scrubbing to the end with remaining attempts:" + maxAttempts);
            trigger(DisneyMediaPlayerCommands.MEDIA_PLAYER.getValue() +
                    String.format(DisneyMediaPlayerCommands.SEEK.getValue(), seekToEnd));
        }
        return playNextButton.isElementPresent(DELAY);
    }

    public void mediaPlayerSeekToStart(){
        LOGGER.info("Seek to start");
        trigger(DisneyMediaPlayerCommands.MEDIA_PLAYER.getValue() + DisneyMediaPlayerCommands.SEEK_TO_START.getValue());
    }

    public DisneyPlusPlaybackPage playMediaPlayer(){
        LOGGER.info("playing Media");
        trigger(DisneyMediaPlayerCommands.MEDIA_PLAYER.getValue() + DisneyMediaPlayerCommands.PLAY.getValue());
        return this;
    }

    public double getCurrentTime() {
        LOGGER.info("Current Video Time");
        return Double.parseDouble(String.valueOf(trigger(DisneyMediaPlayerCommands.RETURN.getValue() +
                DisneyMediaPlayerCommands.MEDIA_PLAYER.getValue() +
                DisneyMediaPlayerCommands.CURRENT_TIME.getValue())));
    }

    public Double getDurationTime() {
        LOGGER.info("Total Duration of Video Time");
        return Double.parseDouble(String.valueOf(trigger(DisneyMediaPlayerCommands.RETURN.getValue()+ DisneyMediaPlayerCommands.MEDIA_PLAYER.getValue() +
                DisneyMediaPlayerCommands.DURATION.getValue())));
    }

    public DisneyPlusPlaybackPage clickPlayNextEpisode(){
        LOGGER.info("Click on play-next button");
        getPlayNextButton().click();
        return this;
    }
}
