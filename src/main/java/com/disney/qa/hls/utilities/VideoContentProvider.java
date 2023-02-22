package com.disney.qa.hls.utilities;

public interface VideoContentProvider {

    boolean waitForVideoPlayerPresence();

    <T> T getMediaPlayByUrl(String first);
    <T> T getMediaPlayByUrl(String url, int offset);
    <T> T getMediaPlayByUrl(String url, String date);

}
