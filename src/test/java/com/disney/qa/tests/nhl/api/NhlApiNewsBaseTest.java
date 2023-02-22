package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.api.nhl.pojo.news.ImageRatios;
import org.apache.commons.lang3.StringUtils;
import org.testng.asserts.SoftAssert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boyle on 10/17/16.
 */
public class NhlApiNewsBaseTest extends AtBatApiBaseTest {

    public void retrieveMediaUrl(SoftAssert softAssert, String fileName, String tokenGuid, ImageRatios image) {

        if (image != null) {
            if (image.getSrc().contains("http")) {
                executeUrl(softAssert, fileName,
                        String.format("%s %sx%s", tokenGuid, image.getWidth(), image.getHeight()),
                        image.getSrc());
            } else {
                executeUrl(softAssert, fileName,
                        String.format("%s %sx%s", tokenGuid, image.getWidth(), image.getHeight()),
                        String.format("%s%s",
                                NhlParameters.NHL_CONTENT_API_HOST.getValue() + "/images/photos/", image.getSrc()));
            }
        }
    }

    @Override
    public void retrieveMediaUrl(SoftAssert softAssert, String fileName, String fieldName, String url) {

        if (url != null && !StringUtils.containsIgnoreCase(url, "ticketmaster")) {
            if (url.contains("http") && exceptionList().contains(url)) {
                executeUrl(softAssert, fileName, fieldName, url);
            } else if (url.contains("http") && exceptionList().contains(url)) {
                executeUrl(softAssert, fileName, fieldName, url.toLowerCase());
            } else {
                extendedExecution(softAssert, fileName, fieldName, url);
            }
        }
    }

    private void extendedExecution(SoftAssert softAssert, String fileName, String fieldName, String url) {
        if (url.contains(".") && !url.contains("/")) {
            executeUrl(softAssert, fileName, fieldName, "http://" + url);
        } else if (url.contains("json")) {
            executeUrl(softAssert, fileName, fieldName, NhlParameters.NHL_CONTENT_API_HOST.getValue() + url);
        } else if (url.startsWith("/")) {
            executeUrl(softAssert, fileName, fieldName, NhlParameters.NHL_FEED_HOST.getValue() + url);
        }
    }

    private List<String> exceptionList() {

        List<String> exceptionUrls = new ArrayList<>();
        exceptionUrls.add("www.Meigray.com");

        return exceptionUrls;
    }
}
