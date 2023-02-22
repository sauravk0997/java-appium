package com.disney.qa.api.tvos;

import com.qaprosoft.carina.core.foundation.api.AbstractApiMethodV2;

import java.util.Properties;

import static com.disney.qa.disney.apple.pages.tv.AppleTVConstants.ENDPOINT;
import static com.disney.qa.disney.apple.pages.tv.AppleTVConstants.RQ_POST_BUNDLE_ID;

public class PostRemoveAppMethod extends AbstractApiMethodV2 {

    public PostRemoveAppMethod(String sessionWDA, String bundleId) {
        super(RQ_POST_BUNDLE_ID, null, new Properties());
        replaceUrlPlaceholder("url", ENDPOINT);
        replaceUrlPlaceholder("session_id", sessionWDA);
        addProperty("bundleId", bundleId);
    }
}