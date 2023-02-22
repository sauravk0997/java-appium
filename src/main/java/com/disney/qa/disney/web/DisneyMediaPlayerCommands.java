package com.disney.qa.disney.web;

public enum DisneyMediaPlayerCommands {

    CURRENT_TIME(".currentTime;"),
    DURATION(".duration;"),
    IS_BUFFERING(".isBuffering"),
    IS_EMPTY(".isEmpty"),
    IS_ERROR(".isError"),
    IS_FINISHED(".isFinished"),
    IS_PAUSED(".isPaused"),
    IS_PLAYING(".isPlaying"),
    MEDIA_PLAYER("window.mediaPlayer"),
    PAUSE(".pause();"),
    PLAY(".play();"),
    PLAYER_STATE(".qa.playerState"),
    RETURN("return "),
    SEEK(".seek(%s);"),
    SEEK_TO_START(".seekToStart();");

    private String key;

    DisneyMediaPlayerCommands(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.key;
    }
}
