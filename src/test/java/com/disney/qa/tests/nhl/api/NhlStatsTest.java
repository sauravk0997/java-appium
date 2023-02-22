package com.disney.qa.tests.nhl.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NhlStatsTest extends BaseNhlApiTest {
	
	/**
	 * JIRA https://jira.mlbam.com/browse/SDAPINHL-1034
	 */
	@QTestCases(id = "42966")
	@Test
	public void verifyStatLeaders() {
		JsonNode rawResponse = nhlStatsApiContentService.getStats(
				ImmutableMap.of("leaderCategories", "savePct", "seriesCodes", "O", "season", "20152016"),
				JsonNode.class, "leaders");
		JsonNode leadersNodesList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..leaders");
		Assert.assertTrue(leadersNodesList.size() > 0, "'leaders' node is not present!");
	}

	/**
	 * JIRA https://jira.mlbam.com/browse/SDAPINHL-1230
	 */
	@QTestCases(id = "42967")
	@MethodOwner(owner = "shashem")
	@Test
	public void verifySeasonParameter() {
		JsonNode rawResponse = nhlStatsApiContentService.getStats(
				ImmutableMap.of("leaderCategories", "points", "limit", "1", "hydrate", "person(stats(splits=statsSingleSeason))", "season", "20102011"), JsonNode.class, "leaders");

		ArrayNode season = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..leaders[*].person.stats[*].splits[*].season");

		Assert.assertTrue(season.toString().contains("20102011"), String.format("Expected 'season' to populate '20102011', but instead found: %s", season));
	}
}
