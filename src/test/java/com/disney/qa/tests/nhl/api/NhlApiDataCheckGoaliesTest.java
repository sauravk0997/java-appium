package com.disney.qa.tests.nhl.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.dataprovider.annotations.XlsDataSourceParameters;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by salma.hashem on 5/15/18.
 */
public class NhlApiDataCheckGoaliesTest extends BaseNhlApiTest {

    /**
     * JIRA ticket: https://jira.bamtechmedia.com/browse/QAA-1361
     */
    @QTestCases(id = "42997")
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "DataProvider", description = "Verify: Team Page - Goalies")
    @XlsDataSourceParameters(sheet = "abbreviation", dsUid = "TUID, Name")
    public void verifyTeamsGoalies(Map<String, String> data) throws IOException, ProcessingException {

        List<String> leaderCategoryNodes = new ArrayList<>();
        leaderCategoryNodes.add("gaa");
        leaderCategoryNodes.add("savePct");
        leaderCategoryNodes.add("wins");
        leaderCategoryNodes.add("shutouts");

        SoftAssert softAssert = new SoftAssert();

        for (String nodes : leaderCategoryNodes) {
            String rawResponse = nhlStatsApiContentService.getTeams(ImmutableMap.of("leaderCategories", nodes, "expand", "leaders.team,leaders.person", "limit", "25"), String.class, data.get("id"), "leaders");
            String statusCode = getHttpStatus("/api/v1/teams/" + data.get("id") + "/leaders?leaderCategories=" + nodes + "&expand=leaders.team,leaders.person&limit=25");

            JsonNode value = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..leaders..value");
            JsonNode teamAbbreviation = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..team.abbreviation");
            JsonNode person = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..person");

            if (person != null) {
                verifyPersonNodes(softAssert, rawResponse, "$..person.id");
                verifyPersonNodes(softAssert, rawResponse, "$..person.fullName");
                verifyPersonNodes(softAssert, rawResponse, "$..person.link");
                verifyPersonNodes(softAssert, rawResponse, "$..person.primaryNumber");
                verifyPersonNodes(softAssert, rawResponse, "$..person.primaryPosition.abbreviation");

            }

            softAssert.assertTrue("200".equals(statusCode), String.format("Status code should be '200': %s", statusCode));
            softAssert.assertTrue(!value.toString().isEmpty(), String.format("Expected 'value' field under 'leader' node to populate", value.toString()));
            softAssert.assertTrue(rawResponse.contains(teamAbbreviation.asText()), String.format("Expected team abbreviation node to be present %s", teamAbbreviation));
            softAssert.assertNotNull(teamAbbreviation, String.format("Expected team abbreviation node to populate", teamAbbreviation));
        }

        softAssert.assertAll();
    }

        private void verifyPersonNodes(SoftAssert softAssert, String response, String path){
            JsonNode personNodes = jsonContext().parse(response).read(path);
            softAssert.assertTrue(response.contains(personNodes.asText()), String.format("Expected person nodes to be present %s", personNodes));
            softAssert.assertTrue(!personNodes.toString().isEmpty(), String.format("Expected person nodes to populate %s", personNodes));
        }
}
