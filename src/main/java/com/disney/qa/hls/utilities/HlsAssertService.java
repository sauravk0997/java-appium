package com.disney.qa.hls.utilities;


import com.qaprosoft.carina.core.foundation.utils.R;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class HlsAssertService extends HlsFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public HlsAssertService (WebDriver driver) {super (driver);}

    /** Generic Universal strings for Assertion **/

    public static final String MEDIA_PLAYER_STATE_BUFFERING = "__media_player_state_buffering";

    public static final String MEDIA_PLAYER_STATE_PLAYING = "__media_player_state_playing";

    public static final String MEDIA_PLAYER_STATE_PAUSED = "__media_player_state_paused";

    public static final String MEDIA_PLAYER_STATE_ERROR = "__media_player_state_media_error";

    public static final String MEDIA_PLAYER_STATE_ENDED = "__media_player_state_ended";

    public static final String BITRATE_NOT_DROPPING = "__bitrate_did_not_drop";

    public static final String MAX_BITRATE_REACHED = "__max_bitrate_reached";

    public static final String MEDIA_PLAYER_PLAYBACK_STATE_PLAYING = "PLAYING";

    public static final String PLAYING_ASSERT = "Expected (__media_player_state_playing), Actual: %s";

    /** End Generic Univeral strings for Assertion **/

    /** Assertion Strings for Offset/Seek checks **/

    public static final String TIME_60_ASSERT = "Expected (60) for current Media Time, Actual: %s";

    public static final String TIME_900_ASSERT = "Expected (900) for current Media Time, Actual: %s";

    /** End Assertion Strings for Offset/Seek checks **/

    //LIBRARY STRINGS//
    public static final String SPACE = " ";
    public static final String GET_STATUS = "return ";
    public static final String PLAYER_VIDEO_CLIENT = "this_is_not_a_player.videoClient";
    public static final String PLAYER_CURRENT_VIDEO_CLIENT = "this_is_not_a_player.currentVideoClient";
    public static final String PLAYER = "this_is_not_a_player";
    public static final String PLAY = ".play()";
    public static final String PAUSE = ".pause();";
    public static final String DESTROY = ".destroy();";
    public static final String OBJECT = "object";
    public static final String OBJECT_INITIALIZE = "var object = '%s';";
    public static final String GET_CURRENT_BITRATE = ".bitrates.getCurrentKbps();";
    public static final String GET_CURRENT_TIME_IN_SECS = ".getCurrentTimeInSecs();";
    public static final String GET_CURRENT_TIME_IN_PDT = ".getCurrentTimeInPDT();";
    public static final String STATE_GET_PLAYER_STATE = ".state.getPlayerState();";
    public static final String STATE_GET_PLAYER_PLAYBACK_STATE = ".player.playbackState;";
    public static final String PLAYER_SEEK = ".seek(%s);";
    public static final String PLAYER_SEEK_TO_START = ".seekToStart();";
    public static final String VOLUME_CHANGE_LISTENER = "this_is_not_a_player.dispatcher.on(this_is_not_a_player.events.getEventList().PlayerEvents.MEDIA_VOLUME_CHANGED, function (e) {myOutput.volume = e.volume});";
    public static final String VOLUME_STATE = "myOutput.volume;";
    public static final String SET_VOLUME = ".setVolume('%s');";
    public static final String HAS_CAPTIONS = ".captions.hasCaptions();";
    public static final String ARE_CAPTIONS_ENABLED = ".captions.areCaptionsEnabled()";
    public static final String ENABLE_CAPTIONS = ".captions.enableCaptions();";
    public static final String DISABLE_CAPTIONS = ".captions.disableCaptions();";
    public static final String AUDIO_TRACK_NAME = "audioTrackName";
    public static final String AUDIO_TRACK_INITIALIZE = "var audioTrackName = '%s';";
    public static final String AVAILABLE_AUDIO_TRACKS = ".audioTracks.getAvailableAudioTracks();";
    public static final String AVAILABLE_AUDIO_TRACK_CURRENT = ".audioTracks.getSelectedAudioTrackLanguage();";
    public static final String AVAILABLE_AUDIO_TRACK_BY_LANGUAGE = ".audioTracks.setSelectedAudioTrackByLanguage('%s')";
    public static final String AVAILABLE_AUDIO_TRACK_BY_NAME = ".audioTracks.setSelectedAudioTrackByName(%s);";
    public static final String AVAILABLE_AUDIO_TRACK_SELECT = ".audioTracks.getSelectedAudioTrack();";
    public static final String SEEK_TO_LIVE = ".seekToLive();";
    public static final String SEEK_TO_DATE = ".seekToDate();";
    public static final String SEEK_TO_DATE_SPECIFIC = ".seekToDate('%s');";
    public static final String STATE_IS_LIVE_RIGHT_NOW = ".state.isLiveRightNow();";
    public static final String AUDIO_FEED_GUID_REGEX_READ = "/[g][u][i][d]/\\w\\w\\w\\w\\w\\w\\w\\w\\D\\w\\w\\w\\w\\D\\w\\w\\w\\w\\D\\w\\w\\w\\w\\D\\w\\w\\w\\w\\w\\w\\w\\w\\w\\w\\w\\w\\D";
    public static final String AUDIO_FEED_GUID_REGEX_REPLACEMENT = "/guid/00000000-0000-0000-0000-000000000000/";
    public static final String OUT_PUT_INITIALIZE = "var myOutput = {};";
    public static final String GET_PLAYBACK_RATE = ".playback.getPlaybackRate()";
    //LIBRARY STRINGS//


    private String browserStack = R.CONFIG.get("browser");

    /** Generic Universal HlsPlayer Methods for test Assertion **/

    public Object getMediaPlayerStateStatus() {

        return trigger(GET_STATUS + PLAYER + STATE_GET_PLAYER_STATE);
    }

    public Object getMediaPlaybackStateStatus() {

        return trigger(GET_STATUS + PLAYER_CURRENT_VIDEO_CLIENT + STATE_GET_PLAYER_PLAYBACK_STATE);
    }

    public Object getMediaPlayerVolumeStatus() {

        return trigger(GET_STATUS + PLAYER + VOLUME_STATE).toString();
    }

    public Object getMediaStateCaptionsPresence() {

        return trigger(GET_STATUS + PLAYER + ARE_CAPTIONS_ENABLED).toString();
    }

    public Object getMediaStateCurrentTimeSeconds() {
        Object currentTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS);
        if (currentTime instanceof Double) {
            int cTime = ((Double)currentTime).intValue();
            LOGGER.info("Current time is: " + cTime);
            return Integer.toString(cTime);
        } else {
            LOGGER.info("Current time is: " + currentTime);
            return currentTime.toString();
        }

    }

    public double getMediaStateCurrentAssertRange() {
        String currentTime = trigger(GET_STATUS + PLAYER + GET_CURRENT_TIME_IN_SECS).toString();
        LOGGER.info("Current time is " + currentTime);
        return Double.parseDouble(currentTime);

    }

    public Object getMediaCurrentBitrate() {
        Object currentBitrate = trigger (GET_STATUS + PLAYER + GET_CURRENT_BITRATE);
        Double dCurrentBitrate = ((Long) currentBitrate).doubleValue();
        LOGGER.info("Current bitrate is: " + dCurrentBitrate + " kilobit per second, " + dCurrentBitrate/1000 + " megabit per second") ;
        return currentBitrate.toString();
    }

    public Object getMediaMaxBitrateTime(String maxBitrate) {
        long timeStart;
        long timeFinish;
        long timeElapsed;

        timeStart = System.currentTimeMillis();
        LOGGER.info("Starting max bitrate speed test..." + timeStart);
        pause(9);
        Object currentBitrate = trigger (GET_STATUS + PLAYER + GET_CURRENT_BITRATE);
        LOGGER.info("Current bitrate is: " + currentBitrate);
        timeFinish = System.currentTimeMillis();
        LOGGER.info("Ending max bitrate speed test..." + timeFinish);
        timeElapsed = timeFinish - timeStart;
        LOGGER.info("Time elapsed..." + timeElapsed);
        if(timeElapsed <= 10000 && (currentBitrate.toString().contains(maxBitrate))) {
            return MAX_BITRATE_REACHED;
        } else
            return "Too slow!";
    }

    public Object getMediaLiveState() {
        Object response = trigger(GET_STATUS + PLAYER + STATE_IS_LIVE_RIGHT_NOW);
        LOGGER.info("Is Media Live: " + response);
        return response.toString();
    }

    public Object getMediaPlaybackRate() {
        Object playbackRate = trigger(GET_STATUS + PLAYER + GET_PLAYBACK_RATE);
        LOGGER.info("Playback rate is: " + playbackRate);
        if (playbackRate instanceof Double) {
            return String.format("%.2f",playbackRate);
        } else
            return playbackRate.toString();
    }

    /** Audio **/

    public String getMediaAudioFeeds() {
        Object response = trigger(GET_STATUS + PLAYER + AVAILABLE_AUDIO_TRACKS);
        LOGGER.info("Audio Feeds: " + response.toString());
        LOGGER.info("Audio Feeds converted for Assert: " + response.toString().replaceAll(AUDIO_FEED_GUID_REGEX_READ, AUDIO_FEED_GUID_REGEX_REPLACEMENT));

        LOGGER.info("browser being tested: " + browserStack);
        if ("ie".equals(browserStack)) {
            return response.toString().replace("[]", "{}").replaceAll(AUDIO_FEED_GUID_REGEX_READ, AUDIO_FEED_GUID_REGEX_REPLACEMENT);
       } else {
            return response.toString().replaceAll(AUDIO_FEED_GUID_REGEX_READ, AUDIO_FEED_GUID_REGEX_REPLACEMENT);
        }
    }

    public Object getMediaAudioFeedsNoReadout() {
        Object response = trigger(GET_STATUS + PLAYER + AVAILABLE_AUDIO_TRACKS);

        if ("ie".equals(browserStack)) {
            return response.toString().replace("[]", "{}").replaceAll(AUDIO_FEED_GUID_REGEX_READ, AUDIO_FEED_GUID_REGEX_REPLACEMENT);
      } else {
            return response.toString().replaceAll(AUDIO_FEED_GUID_REGEX_READ, AUDIO_FEED_GUID_REGEX_REPLACEMENT);
        }
    }

    public Object getMediaAudioFeedTrack() {

        Object response = trigger(GET_STATUS + PLAYER + AVAILABLE_AUDIO_TRACK_CURRENT);
        LOGGER.info("Current Media Audio Track: " + response);
        return response.toString();

    }

    public Object getMediaAudioFeedTrackNoReadout() {

        Object response = trigger(GET_STATUS + PLAYER + AVAILABLE_AUDIO_TRACK_CURRENT);
        return response.toString();

    }

    public Object getMediaAudioFeedTrackSelect() {
        Object response = trigger(GET_STATUS + PLAYER + AVAILABLE_AUDIO_TRACK_SELECT);
        LOGGER.info("Audio Track Select: " + response);
        LOGGER.info("Audio Track Converted for Assert: " + response.toString().replaceAll(AUDIO_FEED_GUID_REGEX_READ, AUDIO_FEED_GUID_REGEX_REPLACEMENT));
        return response.toString().replaceAll(AUDIO_FEED_GUID_REGEX_READ, AUDIO_FEED_GUID_REGEX_REPLACEMENT);
    }

    public Object getMediaAudioFeedTrackSelectNoReadout() {
        Object response = trigger(GET_STATUS + PLAYER + AVAILABLE_AUDIO_TRACK_SELECT);
        return response.toString().replaceAll(AUDIO_FEED_GUID_REGEX_READ, AUDIO_FEED_GUID_REGEX_REPLACEMENT);
    }

    /** End Audio **/
}