package com.disney.qa.tests.nhl.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NhlPlatformsTest extends BaseNhlApiTest {
    /**
     * Jira ticket: https://jira.mlbam.com/browse/SDAPINHL-1223
     */
    @QTestCases(id = "42927")
    @MethodOwner(owner = "shashem")
    @Test
    public void verifyFireTv(){
        JsonNode rawPlatform = nhlStatsApiContentService.getPlatforms(null, JsonNode.class);

        JsonNode platformDes = JsonPath.using(jsonPathJacksonConfiguration).parse(rawPlatform).read("$..platformDescription");

        Assert.assertTrue(platformDes.toString().contains("Fire TV"),
                String.format("Expected 'platformDescription' to contain 'Fire TV', instead found %s", platformDes.get(12)));
    }
}
