package com.disney.qa.api.tvos;

import com.zebrunner.carina.api.AbstractApiMethodV2;

import java.util.Properties;

import static com.disney.qa.disney.apple.pages.tv.AppleTVConstants.ENDPOINT;
import static com.disney.qa.disney.apple.pages.tv.AppleTVConstants.RQ_POST_BUTTON;

public class PostUnlockMethod extends AbstractApiMethodV2 {

    public PostUnlockMethod(String sessionWDA) {
        super(RQ_POST_BUTTON, null, new Properties());
        replaceUrlPlaceholder("url", ENDPOINT);
        replaceUrlPlaceholder("session_id", sessionWDA);
    }
}