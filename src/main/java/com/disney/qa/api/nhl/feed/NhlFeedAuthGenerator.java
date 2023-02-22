package com.disney.qa.api.nhl.feed;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by mk on 11/12/15.
 */
public class NhlFeedAuthGenerator {

    public static final String AUTH_PATTERN = "%s|%s|%s";

    public String generateAuth(String partnerName, String secretApiKey) {

        String unixTimeStamp = String.valueOf(System.currentTimeMillis());

        String sha1string = partnerName + unixTimeStamp + secretApiKey;

        return String.format(AUTH_PATTERN, partnerName, unixTimeStamp, DigestUtils.sha1Hex(sha1string));
    }
}
