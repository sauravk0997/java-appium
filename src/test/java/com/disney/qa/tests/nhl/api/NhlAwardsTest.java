package com.disney.qa.tests.nhl.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NhlAwardsTest extends BaseNhlApiTest {

    @QTestCases(id = "42872")
    @Test
    public void verifyResultsHydrate() {
        JsonNode rawResponseActual = nhlStatsApiContentService.getAwards(
                ImmutableMap.of("hydrate", "team"), JsonNode.class, "1/results");
        JsonNode rawResponseExpected = nhlStatsApiContentService.getAwards(
                ImmutableMap.of("hydrate", "results(team)"), JsonNode.class, "1");

        JsonNode resultsActual = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseActual).read("$..results");
        JsonNode resultsExpected = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponseExpected).read("$..results");

        Assert.assertEquals(resultsActual, resultsExpected, "Results are not the same");
    }
}
