package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.atbat.AtBatApiContentProvider;
import com.disney.qa.api.nhl.pojo.news.Image;
import com.disney.qa.api.nhl.pojo.news.Media;
import com.disney.qa.api.nhl.pojo.news.NewsItem;
import com.disney.qa.api.nhl.pojo.news.NewsReader;
import com.disney.qa.api.nhl.pojo.news.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qaprosoft.carina.core.foundation.dataprovider.annotations.XlsDataSourceParameters;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by boyle on 10/14/16.
 */
public class NhlApiNewsReaderTest extends NhlApiNewsBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    AtBatApiContentProvider contentProvider = new AtBatApiContentProvider();

    @Test(dataProvider = "DataProvider", description = "Verify: Team News Reader")
    @XlsDataSourceParameters(sheet = "abbreviation_with_nhl", dsUid = "TUID, Name")
    public void verifyTeamNewsReader(Map<String, String> data) {

        SoftAssert softAssert = new SoftAssert();

        List<String> languages = new ArrayList<>();
        languages.add("en");
        languages.add("fr");

        List<String> newsEndPoints = new ArrayList<String>();
        newsEndPoints.add("ios-phone-v1.json");
        newsEndPoints.add("ios-tablet-v1.json");
        newsEndPoints.add("android-phone-v1.json");

        //https://nhl.bamcontent.com/nhl/en/section/v1/news/nhl
        //https://nhl.bamcontent.com/nhl/en/section/v1/news/nhl/ios-phone-v1.json
        //https://nhl.bamcontent.com/nhl/en/section/v1/news/1/ios-phone-v1.json

        for (String language : languages) {
            for (String endpoint : newsEndPoints) {
                RequestEntity<String> checkUrl = contentProvider.buildRequestEntity(
                        "http",
                        "nhl.bamcontent.com",
                        "/nhl/" + language +"/section/v1/news/" + data.get("id") + "/" + endpoint,
                        HttpMethod.GET
                );
                validateEndPoint(softAssert, checkUrl, endpoint);
            }
        }
        softAssert.assertAll();
    }

    private void validateEndPoint(SoftAssert softAssert, RequestEntity<String> checkUrl, String fileName) {
        String httpStatus = safeStatus(checkUrl, String.class);
        softAssert.assertTrue("200".equalsIgnoreCase(httpStatus),
                String.format("\nFile Name (%s) HTTP Status (%s) for URL (%s)", fileName, httpStatus, checkUrl.getUrl()));

        if ("200".equalsIgnoreCase(httpStatus)) {

            LOGGER.info(String.format("Preparing to Retrieve Items from: %s", checkUrl.getUrl()));
            NewsReader newsReader = restTemplate.exchange(checkUrl, NewsReader.class).getBody();
            LOGGER.info("Checking NewsReader: " + newsReader.getNewsItem().size());
            checkNewsArticles(softAssert, checkUrl.getUrl().toString(), newsReader, fileName);
        }
    }

    private void checkNewsArticles(SoftAssert softAssert, String url, NewsReader newsReader, String fileName) {
        LOGGER.info(
                String.format("Retrieved NewsReader Articles (%s) for the following URL (%s)",
                        newsReader.getNewsItem().size(), url));

        for (NewsItem newsItem : newsReader.getNewsItem()) {
            String enhancedDesc = String.format(FILE_CONTENT_DESC_STRING, fileName, newsItem.getId(), newsItem.getHeadline());
            LOGGER.info(String.format("  - Retrieving News Item ContentId (%s)", newsItem.getId()));

            checkTokenUrls(softAssert, newsItem, fileName);

            checkMedia(softAssert, newsItem.getMedia(), fileName, newsItem.getId(), newsItem.getHeadline());

            checkImages(softAssert, newsItem.getImage(), fileName, newsItem.getId(), newsItem.getHeadline());

            retrieveMediaUrl(softAssert, enhancedDesc, "Article", newsItem.getUrl());

            softAssert.assertTrue(newsItem.getHeadline().length() > 0,
                    String.format("Expected Headline to Be Populated for Article (%s)", newsItem.getId()));
            softAssert.assertTrue(newsItem.getBody().length() > 0,
                    String.format("Expected Body to be Populated for Article (%s)", newsItem.getId()));

            retrieveMediaUrl(softAssert, enhancedDesc, "dataURI", newsItem.getDataURI());
        }
    }

    private void checkTokenUrls(SoftAssert softAssert, NewsItem newsItem, String fileName) {

        if (newsItem.getTokenData() != null) {
            try {
                JSONObject json = new JSONObject(newsItem.getTokenData().toString());
                Iterator<?> keys = json.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    Token token = new ObjectMapper().readValue(json.getJSONObject(key).toString(), Token.class);
                    scanToken(softAssert, fileName, newsItem.getId(), newsItem.getHeadline(), token);
                }
            } catch (Exception ex) {
                LOGGER.debug("File Name Full Error: " + ex.getMessage(), ex);
                softAssert.assertTrue(false,
                        String.format("%nFile Name: (%s) Error: %s", fileName, ex.getMessage()));
            }
        }
    }

    private void scanToken(SoftAssert softAssert, String fileName, Integer contentId, String desc, Token token) {
        String enhancedTokenDesc = String.format(FILE_CONTENT_DESC_STRING, fileName, contentId, desc);

        if (!"video".equalsIgnoreCase(token.getType())) {
            retrieveMediaUrl(softAssert, enhancedTokenDesc, "Token HREF", token.getHref());
            retrieveMediaUrl(softAssert, enhancedTokenDesc, "Token HREF Mobile", token.getHrefMobile());
        }

        if (token.getImage() != null) {
            checkCuts(softAssert, token.getImage(), fileName, contentId, desc, token.getTokenGUID());
        }

        if (token.getMediaUrls() != null) {
            retrieveMediaUrl(softAssert, enhancedTokenDesc, "Token HTTP_CLOUD_MOBILE", token.getMediaUrls().getHttpCloudMobile());
            retrieveMediaUrl(softAssert, enhancedTokenDesc, "Token HTTP_CLOUD_TABLET", token.getMediaUrls().getHttpCloudTablet());
            retrieveMediaUrl(softAssert, enhancedTokenDesc, "Token HTTP_CLOUD_TABLET60", token.getMediaUrls().getHttpCloudTablet60());
        }

        if (token.getBlurb() != null) {
            softAssert.assertTrue(token.getBlurb().length() > 0,
                    String.format("Expected Blurb to Be Populated for Media (%s)", token.getTokenGUID()));
        }
    }

    private void checkCuts(SoftAssert softAssert, Image image, String fileName, Integer contentId, String desc, String tokenId) {

        if (image.getCuts() != null) {
            String enhancedCutsDesc = String.format(FILE_CONTENT_DESC_STRING, fileName, contentId, desc);

            retrieveMediaUrl(softAssert, enhancedCutsDesc, tokenId, image.getCuts().get124x70());
            retrieveMediaUrl(softAssert, enhancedCutsDesc, tokenId, image.getCuts().get248x140());
            retrieveMediaUrl(softAssert, enhancedCutsDesc, tokenId, image.getCuts().get320x180());
            retrieveMediaUrl(softAssert, enhancedCutsDesc, tokenId, image.getCuts().get372x210());
            retrieveMediaUrl(softAssert, enhancedCutsDesc, tokenId, image.getCuts().get568x320());
            retrieveMediaUrl(softAssert, enhancedCutsDesc, tokenId, image.getCuts().get640x360());
            retrieveMediaUrl(softAssert, enhancedCutsDesc, tokenId, image.getCuts().get768x432());
            retrieveMediaUrl(softAssert, enhancedCutsDesc, tokenId, image.getCuts().get960x540());
            retrieveMediaUrl(softAssert, enhancedCutsDesc, tokenId, image.getCuts().get1024x576());
            retrieveMediaUrl(softAssert, enhancedCutsDesc, tokenId, image.getCuts().get1136x640());
        }
    }

    private void checkMedia(SoftAssert softAssert, Media media, String fileName, Integer contentId, String desc) {

        if (media != null) {
            String enhancedMediaDesc = String.format(FILE_CONTENT_DESC_STRING, fileName, contentId, desc);

            checkImages(softAssert, media.getImage(), fileName, contentId, desc + " [Media Node]");

            retrieveMediaUrl(softAssert, enhancedMediaDesc, "mediaPlaybackURL", media.getMediaPlaybackUrl());

            if (media.getBlurb() != null) {
                softAssert.assertTrue(media.getBlurb().length() > 0,
                        String.format("Expected Blurb to Be Populated for Media (%s)", media.getId()));
            }
        }

    }

    private void checkImages(SoftAssert softAssert, Image image, String fileName, Integer contentId, String desc) {

        if (image != null) {
            String enhancedImageDesc = String.format(FILE_CONTENT_DESC_STRING, fileName, contentId, desc);

            retrieveMediaUrl(softAssert, enhancedImageDesc, "124 x 70", image.get124x70());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "248 x 140", image.get248x140());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "320 x 180", image.get320x180());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "372 x 210", image.get372x210());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "568 x 320", image.get568x320());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "640 x 360", image.get640x360());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "768 x 432", image.get768x432());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "960 x 540", image.get960x540());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "1024 x 576", image.get1024x576());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "1136 x 640", image.get1136x640());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "1280 x 720", image.get1280x720());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "1284 x 722", image.get1284x722());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "1536 x 864", image.get1536x864());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "1704 x 960", image.get1704x960());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "2048 x 1152", image.get2048x1152());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "2208 x 1242", image.get2208x1242());
            retrieveMediaUrl(softAssert, enhancedImageDesc, "2568 x 1444", image.get2568x1444());
        }
    }
}