package com.disney.qa.tests.nhl.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class NhlRosterStatusesTest extends BaseNhlApiTest {

    /**
     * JIRA https://jira.mlbam.com/browse/SDAPINHL-1151
     */
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyRosterStatusListDescription() {
        JsonNode rawRosterStatuses = nhlStatsApiContentService.getRosterStatuses(null, JsonNode.class);

        JsonNode rosterStatuses = JsonPath.using(jsonPathJacksonConfiguration).parse(rawRosterStatuses).read("$..description");

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(rosterStatuses.get(0).toString().contains("Currently on the roster"), String.format("Expected 'Currently on the roster' as the value of 'Code Y', and found %s instead", rosterStatuses.get(0)));
        softAssert.assertTrue(rosterStatuses.get(1).toString().contains("Not currently on the roster"), String.format("Expected 'Not currently on the roster' as the value of 'Code N', and found %s instead", rosterStatuses.get(1)));
        softAssert.assertTrue(rosterStatuses.get(2).toString().contains("Currently on injured reserve"), String.format("Expected 'Currently on injured reserve' as the value of 'Code I', and found %s instead", rosterStatuses.get(2)));

        softAssert.assertAll();
    }
}
