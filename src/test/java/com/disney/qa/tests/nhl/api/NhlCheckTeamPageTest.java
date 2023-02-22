package com.disney.qa.tests.nhl.api;

import com.disney.qa.api.atbat.AtBatApiContentProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.dataprovider.annotations.XlsDataSourceParameters;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Map;

public class NhlCheckTeamPageTest extends BaseNhlApiTest {

    private AtBatApiContentProvider contentProvider = new AtBatApiContentProvider();

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/QAA-2092
     */
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "DataProvider", description = "Verify: Team Page")
    @XlsDataSourceParameters(sheet = "abbreviation_with_nhl", dsUid = "TUID, Name")
    public void verifyTeamPageVTPs (Map<String, String> data) {

        String rawResponse = nhlStatsApiContentService.getTeams(ImmutableMap.of("teamId", data.get("id"), "expand", "team.playoffs,team.content.sections"), String.class);
        JsonNode typeVideos = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..content.sections.itemsList[?(@.type == 'video')]");

        SoftAssert softAssert = new SoftAssert();

        for(JsonNode typeVideo : typeVideos){
            ArrayNode topicIds = JsonPath.using(jsonPathJacksonConfiguration).parse(typeVideo).read("$..topicId");

            MultiValueMap<String, String> listQueryParams = new LinkedMultiValueMap<>();
            listQueryParams.add("page", "1");
            listQueryParams.add("expand", "playbacks.HTTP_CLOUD_TABLET_60,partner");

            for(JsonNode topicId : topicIds){
                RequestEntity<String> checkUrls = contentProvider.buildRequestEntity(
                        "https",
                        "search-api.svc.nhl.com",
                        "/svc/search/v2/nhl_global_en/topic/" + topicId,
                        listQueryParams,
                        HttpMethod.GET
                );

                String httpStatus = safeStatus(checkUrls, String.class);
                softAssert.assertTrue("200".equalsIgnoreCase(httpStatus),
                        String.format("HTTP Status (%s) for URL (%s)", httpStatus, checkUrls.getUrl()));
            }
        }
        softAssert.assertAll();
    }

    /**
     * JIRA ticket: https://jira.mlbam.com/browse/QAA-2091
     */
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "DataProvider", description = "Verify: Team Page")
    @XlsDataSourceParameters(sheet = "abbreviation_with_nhl", dsUid = "TUID, Name")
    public void verifyTeamPageATPs (Map<String, String> data) {

        String rawResponse = nhlStatsApiContentService.getTeams(ImmutableMap.of("teamId", data.get("id"), "expand", "team.playoffs,team.content.sections"), String.class);
        JsonNode typeArticles = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..content.sections.itemsList[?(@.type == 'article')]");

        SoftAssert softAssert = new SoftAssert();

        for(JsonNode typeArticle : typeArticles){
            ArrayNode topicIds = JsonPath.using(jsonPathJacksonConfiguration).parse(typeArticle).read("$..topicId");

            MultiValueMap<String, String> listQueryParams = new LinkedMultiValueMap<>();
            listQueryParams.add("page", "1");
            listQueryParams.add("expand", "partner");

            for(JsonNode topicId : topicIds){
                RequestEntity<String> checkUrls = contentProvider.buildRequestEntity(
                        "https",
                        "search-api.svc.nhl.com",
                        "/svc/search/v2/nhl_global_en/topic/" + topicId,
                        listQueryParams,
                        HttpMethod.GET
                );

                String httpStatus = safeStatus(checkUrls, String.class);
                softAssert.assertTrue("200".equalsIgnoreCase(httpStatus),
                        String.format("HTTP Status (%s) for URL (%s)", httpStatus, checkUrls.getUrl()));
            }
        }
        softAssert.assertAll();
    }
}
