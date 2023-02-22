package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.atbat.AtBatApiContentProvider;
import com.disney.qa.api.nhl.pojo.video.Image;
import com.disney.qa.api.nhl.pojo.video.VideoReader;
import com.disney.qa.api.nhl.pojo.video.VideosLongList;
import com.disney.qa.api.nhl.pojo.video.query.Cuts;
import com.disney.qa.api.nhl.pojo.video.query.Doc;
import com.disney.qa.api.nhl.pojo.video.query.Playbacks;
import com.disney.qa.api.nhl.pojo.video.query.VideoQueryReader;
import com.qaprosoft.carina.core.foundation.dataprovider.annotations.XlsDataSourceParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by boyle on 11/30/16.
 * 1. Check that file loads [COMPLETED]
 * 2. Check that JSON is valid [COMPLETED]
 * 3. Check that playback URL's do not 404 [COMPLETED]
 *      "HTTP_CLOUD_MOBILE"
 *      "HTTP_CLOUD_TABLET"
 *      "HTTP_CLOUD_TABLET_60"
 * 4. Check that URLs do not 404 [COMPLETED]
 * 5. Check that images do not 404 [COMPLETED]
 * 6. Check that the following nodes exist:
 *      headline [COMPLETED]
 *      blurb [COMPLETED]
 *      duration [COMPLETED]
 *      date [COMPLETED]
 */
public class NhlApiVideoReaderTest extends NhlApiNewsBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private AtBatApiContentProvider contentProvider = new AtBatApiContentProvider();

    @Test(dataProvider = "DataProvider", description = "Verify: Team Video Reader")
    @XlsDataSourceParameters(sheet = "abbreviation_with_nhl", dsUid = "TUID, Name")
    public void verifyTeamVideoReader(Map<String, String> data) {

        SoftAssert softAssert = new SoftAssert();

        List<String> languages = new ArrayList<>();
        languages.add("en");
        languages.add("fr");

        List<String> mediaEndPoints = new ArrayList<String>();
        mediaEndPoints.add("ios-phone-v1.json");
        mediaEndPoints.add("ios-tablet-v1.json");
        mediaEndPoints.add("android-phone-v1.json");

        for (String language : languages) {
            for (String endpoint : mediaEndPoints) {
                RequestEntity<String> checkUrl = contentProvider.buildRequestEntity(
                        "http",
                        "nhl.bamcontent.com",
                        "/nhl/" + language +"/section/v1/video/" + data.get("id") + "/" + endpoint,
                        HttpMethod.GET
                );
                validateEndPoint(softAssert, checkUrl, endpoint);
            }
        }
        softAssert.assertAll();
    }

    @DataProvider
    public Iterator<Object[]> listOfCustomPrograms() {

        List<Object[]> listPrograms = new ArrayList<>();

        String query = "TUID:Search by Shortcut %s";

        List<String> programString = new ArrayList<>();
        programString.add("program-gorgeousGoals");
        programString.add("program-greatSaves");
        programString.add("program-stats");
        programString.add("program-theVault");
        //programString.add("277350912");

        for (String program : programString) {
            listPrograms.add(new Object[]{
                    String.format(query, program), program});
        }

        return listPrograms.iterator();
    }

    @Test(dataProvider = "listOfCustomPrograms", description = "Verify: Video by Shortcut")
    public void verifyVideoByShortcut(String TUID, String programName) {

        SoftAssert softAssert = new SoftAssert();

        List<String> mediaEndPoints = new ArrayList<String>();
        mediaEndPoints.add("ios-phone-v1.json");
        mediaEndPoints.add("ios-tablet-v1.json");
        mediaEndPoints.add("android-phone-v1.json");

        for (String endpoint : mediaEndPoints) {
            RequestEntity<String> checkUrl = contentProvider.buildRequestEntity(
                    "http",
                    "nhl.bamcontent.com",
                    "/nhl/id/v1/" + programName + "/details/" + endpoint,
                    HttpMethod.GET
            );
            validateEndPoint(softAssert, checkUrl, endpoint);
        }
        softAssert.assertAll();
    }

    @DataProvider
    public Iterator<Object[]> listOfQueries() {

        List<Object[]> listPrograms = new ArrayList<>();

        String query = "TUID:Search by Query (%s): %s";

        Map<String, String> programString = new HashMap<>();
        programString.put("Team", "Bruins");
        programString.put("Player", "Kinkaid");
        programString.put("Keyword", "Goal");

        for (Map.Entry<String, String> entry : programString.entrySet()) {
            listPrograms.add(new Object[]{
                    String.format(query, entry.getKey(), entry.getValue()), entry.getValue()});
        }

        return listPrograms.iterator();
    }

    @Test(dataProvider = "listOfQueries", description = "Verify: Video by Query")
    public void verifyVideoByQuery(String TUID, String query) {
        SoftAssert softAssert = new SoftAssert();

        List<String> languages = new ArrayList<>();
        languages.add("en");
        languages.add("fr");

        MultiValueMap<String, String> listQueryParams = new LinkedMultiValueMap<>();
        listQueryParams.add("q", query);
        listQueryParams.add("type", "video");
        listQueryParams.add("sort", "new");
        listQueryParams.add("expand", "playbacks.HTTP_CLOUD_TABLET_60");

        for (String language : languages) {
            RequestEntity<String> checkUrl = contentProvider.buildRequestEntity(
                    "http",
                    "search-api.svc.nhl.com",
                    "/svc/search/v2/nhl_global_" + language + "/query",
                    listQueryParams,
                    HttpMethod.GET
            );
            validateQuery(softAssert, checkUrl, query);
        }
        softAssert.assertAll();
    }

    private void validateEndPoint(SoftAssert softAssert, RequestEntity<String> checkUrl, String fileName) {
        String httpStatus = safeStatus(checkUrl, String.class);
        softAssert.assertTrue("200".equalsIgnoreCase(httpStatus),
                String.format("\nFile Name (%s) HTTP Status (%s) for URL (%s)", fileName, httpStatus, checkUrl.getUrl()));

        if ("200".equalsIgnoreCase(httpStatus)) {

            VideoReader videoReader = restTemplate.exchange(checkUrl, VideoReader.class).getBody();

            List<VideosLongList> videoList;
            if (!videoReader.getVideosLongList().isEmpty()) {
                videoList = videoReader.getVideosLongList();
            } else {
                videoList = videoReader.getVideoList();
            }
            checkVideos(softAssert, checkUrl.getUrl().toString(), videoList, fileName);
        }
    }

    private void checkVideos(SoftAssert softAssert, String url, List<VideosLongList> videoList, String fileName) {
        LOGGER.info(
                String.format("Retrieved VideoReader Items (%s) for the following URL (%s)",
                        videoList.size(), url));

        for (VideosLongList videoItem : videoList) {
            String enhancedDesc = String.format(FILE_CONTENT_DESC_STRING, fileName, videoItem.getId(), videoItem.getHeadline());
            LOGGER.info(String.format("  - Retrieving Video Item ContentId (%s)", videoItem.getId()));

            retrieveMediaUrl(softAssert, enhancedDesc, "mediaPlaybackURL", videoItem.getMediaPlaybackURL());

            softAssert.assertTrue(!videoItem.getHeadline().isEmpty(),
                    String.format("Expected Headline to be Populated for Media (%s)", videoItem.getId()));
            if (!videoItem.getMediaPlaybackURL().contains("live")) {
                softAssert.assertTrue(!videoItem.getDuration().isEmpty(),
                        String.format("Expected Duration to be Populated for Media (%s)", videoItem.getId()));
            }
            softAssert.assertTrue(!videoItem.getDate().isEmpty(),
                    String.format("Expected Date to be Populated for Media (%s)", videoItem.getId()));
            softAssert.assertTrue(!videoItem.getBlurb().isEmpty(),
                    String.format("Expected Blurb to Be Populated for Media (%s)", videoItem.getId()));


            if (videoItem.getImage().getCuts() != null) {
                checkImages(softAssert, videoItem.getImage().getCuts(), fileName, videoItem.getId(), videoItem.getHeadline());
            } else {
                checkImages(softAssert, videoItem.getImage(), fileName, videoItem.getId(), videoItem.getHeadline());
            }
        }
    }

    private void checkImages(SoftAssert softAssert, Image image, String fileName, String id, String headline) {

        String enhancedImageDesc = String.format(FILE_CONTENT_DESC_STRING, fileName, id, headline);

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
    }

    private void validateQuery(SoftAssert softAssert, RequestEntity<String> checkUrl, String query) {
        String httpStatus = safeStatus(checkUrl, String.class);
        softAssert.assertTrue("200".equalsIgnoreCase(httpStatus),
                String.format("\nQuery (%s) HTTP Status (%s) for URL (%s)", query, httpStatus, checkUrl.getUrl()));

        if ("200".equalsIgnoreCase(httpStatus)) {
            VideoQueryReader videoQueryReader = restTemplate.exchange(checkUrl, VideoQueryReader.class).getBody();
            LOGGER.info("Checking Size: " + videoQueryReader.getDocs().size());
            checkDocs(softAssert, checkUrl.getUrl().toString(), videoQueryReader.getDocs(), query);
        }
    }

    private void checkDocs(SoftAssert softAssert, String url, List<Doc> docs, String query) {
        LOGGER.info(
                String.format("Retrieved VideoQuery Items (%s) for the following URL (%s)",
                        docs.size(), url));

        for (Doc doc : docs) {
            String enhancedDesc = String.format(FILE_CONTENT_DESC_STRING, query, doc.getAssetId(), doc.getTitle());
            LOGGER.info(String.format(" - Retrieving Doc Asset Id (%s)", doc.getAssetId()));

            retrieveMediaUrl(softAssert, enhancedDesc, "Url Field's", doc.getUrl());

            if (doc.getImage().getCuts() != null) {
                checkImages(softAssert, doc.getImage().getCuts(), query, doc.getAssetId(), doc.getTitle());
            }
            if (doc.getPlaybacks() != null) {
                checkPlayback(softAssert, doc.getPlaybacks(), query, doc.getAssetId(), doc.getTitle());
            }

            softAssert.assertTrue(!doc.getBlurb().isEmpty(),
                    String.format("Expected Blurb to Be Populated for Media (%s)", doc.getAssetId()));
            softAssert.assertTrue(!doc.getBigBlurb().isEmpty(),
                    String.format("Expected Big Blurb to Be Populated for Media (%s)", doc.getAssetId()));
            softAssert.assertTrue(!doc.getTitle().isEmpty(),
                    String.format("Expected Title to Be Populated for Media (%s)", doc.getAssetId()));
        }
    }

    private void checkImages(SoftAssert softAssert, Cuts cuts, String query, String id, String title) {
        String enhancedImageDesc = String.format(FILE_CONTENT_DESC_STRING, query, id, title);

        retrieveMediaUrl(softAssert, enhancedImageDesc, "124 x 70", cuts.get124x70().getSrc());
        retrieveMediaUrl(softAssert, enhancedImageDesc, "248 x 140", cuts.get248x140().getSrc());
        retrieveMediaUrl(softAssert, enhancedImageDesc, "320 x 180", cuts.get320x180().getSrc());
        retrieveMediaUrl(softAssert, enhancedImageDesc, "372 x 210", cuts.get372x210().getSrc());
        retrieveMediaUrl(softAssert, enhancedImageDesc, "568 x 320", cuts.get568x320().getSrc());
        retrieveMediaUrl(softAssert, enhancedImageDesc, "640 x 360", cuts.get640x360().getSrc());
        retrieveMediaUrl(softAssert, enhancedImageDesc, "768 x 342", cuts.get768x432().getSrc());
        retrieveMediaUrl(softAssert, enhancedImageDesc, "960 x 540", cuts.get960x540().getSrc());
        retrieveMediaUrl(softAssert, enhancedImageDesc, "1024 x 576", cuts.get1024x576().getSrc());
        retrieveMediaUrl(softAssert, enhancedImageDesc, "1136 x 640", cuts.get1136x640().getSrc());
    }

    private void checkPlayback(SoftAssert softAssert, Playbacks playbacks, String query, String id, String title) {
        String enhancedPlaybackDesc = String.format(FILE_CONTENT_DESC_STRING, query, id, title);

        retrieveMediaUrl(softAssert, enhancedPlaybackDesc, "HTTP_CLOUD_TABLET_60", playbacks.getHttpCloudTablet60().getUrl());
    }
}
