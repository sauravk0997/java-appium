package com.disney.qa.tests.nhl.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NhlDraftTest extends BaseNhlApiTest{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * JIRA https://jira.mlbam.com/browse/SDAPINHL-1190
	 * 
	 */
	@QTestCases(id = "42874")
	@Test()
	public void verifyDraftProspectInformation() {
		JsonNode rawResponse1 = nhlStatsApiContentService
				.getDraft(ImmutableMap.of("year", "2020", "limit", "4", "offset", "4"), JsonNode.class, "prospects");

		JsonNode rawResponse2 = nhlStatsApiContentService
				.getDraft(ImmutableMap.of("prospectIds", "84827,82651", "year", "2020"), JsonNode.class, "prospects");

		JsonNode prospectsNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
				.read("$..prospects.*");
		JsonNode ranksNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
				.read("$..ranks[?(@.draftYear== 2020)]");

		JsonNode prospectsNodeList2 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2)
				.read("$..prospects.*");
		JsonNode ranksNodeList2 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2)
				.read("$..ranks[?(@.draftYear== 2020)]");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(prospectsNodeList.size() == 4, "The number of 'prospects' is not 4!");
		softAssert.assertTrue(ranksNodeList.size() == 4, "Not all rankings are for the 2016 year!");

		softAssert.assertTrue(prospectsNodeList2.size() == 2, "The number of 'prospects' is not 4 for prospects specified by id!");
		softAssert.assertTrue(ranksNodeList2.size() == 2, "Not all rankings are for the 2016 year for prospects specified by id!");
		softAssert.assertAll();
	}
	
	/**
	 * JIRA https://jira.mlbam.com/browse/SDAPINHL-1193
	 * 
	 */
	@QTestCases(id = "42875")
	@Test()
	public void verifyPlayerDraftProspectById() {
		JsonNode rawResponse1 = nhlStatsApiContentService.getDraft(null, JsonNode.class, "prospects/43088");

		JsonNode prospectsNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
				.read("$..prospects[?(@.fullName == 'Alexander Ovechkin')]");

		Assert.assertTrue(prospectsNodeList.size() > 0,
				"Draft prospect for player with name 'Alexander Ovechkin' not present!");
	}
	
	/**
	 * JIRA https://jira.mlbam.com/browse/SDAPINHL-1189
	 * 
	 */
	@QTestCases(id = "42876")
	@Test()
	public void verifyDraftByYearAndRound() {
		Calendar now = Calendar.getInstance();
		int currentYear = now.get(Calendar.YEAR);
		JsonNode rawResponse1 = nhlStatsApiContentService.getDraft(null, JsonNode.class);
		JsonNode rawResponse2 = nhlStatsApiContentService.getDraft(
				ImmutableMap.of("startPick", "1", "endPick", "4", "hydrate", "prospects,team"), JsonNode.class, "2004");
		JsonNode rawResponse3 = nhlStatsApiContentService
				.getDraft(ImmutableMap.of("rounds", "1,2", "years", "2003,2004"), JsonNode.class);

		JsonNode draftNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1)
				.read("$..drafts[?(@.draftYear == " + currentYear + ")]");
		JsonNode draftNodeList2 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2)
				.read("$..drafts[?(@.draftYear == 2004)]");
		JsonNode pickInRoundNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2)
				.read("$..pickInRound");
		JsonNode draftNodeList3 = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse3).read("$..draftYear");
		JsonNode roundNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse3).read("$..round");

		SoftAssert softAssert = new SoftAssert();
		softAssert.assertTrue(draftNodeList.size() == 1,
				"Draft with 'draftYear' = " + currentYear + " doesn't present!");
		softAssert.assertTrue(draftNodeList2.size() == 1,
				"Draft with 'draftYear' = " + currentYear + " doesn't present!");
		verifyPickInRounds(pickInRoundNodeList, softAssert);
		verifyDraftYear(draftNodeList3, softAssert);
		verifyRounds(roundNodeList, softAssert);
		softAssert.assertAll();
	}

	/**
	 * JIRA tickets: https://jira.mlbam.com/browse/SDAPINHL-1250
	 */
	@QTestCases(id = "42878")
	@Test
	public void verifyProspectPosition(){
		JsonNode rawResponse = nhlStatsApiContentService.getDraft(ImmutableMap.of("rounds", "2", "limit", "25", "hydrate", "prospects"), JsonNode.class, "2017");

		JsonNode primaryPositionName = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read("$..prospect.primaryPosition.name");

		SoftAssert softAssert = new SoftAssert();

		List<String> prospectNames = new ArrayList<>();
		prospectNames.add("Defenseman");
		prospectNames.add("Right Wing");
		prospectNames.add("Left Wing");
		prospectNames.add("Center");
		prospectNames.add("Goalie");

		LOGGER.info("Prospect primary position names to be verified: " + prospectNames);

		if(primaryPositionName.size() > 0){
			for (String prospectNameNode : prospectNames) {
				softAssert.assertTrue(primaryPositionName.toString().contains(prospectNameNode),
						String.format("Expected 'prospectName' to populate: %s", prospectNameNode));
			}
		} else{
			softAssert.fail("Primary position 'name' node is missing");
		}
		softAssert.assertAll();
	}

	/**
	 * https://jira.mlbam.com/browse/SDAPINHL-1229
	 * 
	 */
	@QTestCases(id = "42877")
	@Test()
	public void verifyDraftCurrentPickLogic() throws JSONException {
		JsonNode rawResponse1 = nhlStatsApiContentService.getDraft(ImmutableMap.of("startPick", "1", "years", "2016"),
				JsonNode.class);
		JsonNode rawResponse2 = nhlStatsApiContentService
				.getDraft(ImmutableMap.of("startPick", "last", "years", "2016"), JsonNode.class);
		JsonNode rawResponse3 = nhlStatsApiContentService.getDraft(
				ImmutableMap.of("startPick", "last", "years", "2016", "limit", "5", "offset", "-5"), JsonNode.class);

		JsonNode lastPicksNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse2)
				.read("$..picks.*");

		JsonNode picksNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse1).read("$..picks.*");

		JsonNode offsetNegativeValuePicksNodeList = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse3)
				.read("$..picks.*");

		SoftAssert softAssert = new SoftAssert();

		softAssert.assertTrue(lastPicksNodeList.size() == 1, "Incorrect number of Picks!");
		if (picksNodeList.size() > 0) {
			JsonNode lastPick = picksNodeList.get(picksNodeList.size() - 1);
			JSONAssert.assertEquals(lastPicksNodeList.get(0).toString(), lastPick.toString(), JSONCompareMode.STRICT);
		} else {
			softAssert.fail("picksNodeList is empty!");
		}
		softAssert.assertTrue(offsetNegativeValuePicksNodeList.size() == 5, "Incorrect number of Picks!");
	}

	private void verifyPickInRounds(JsonNode nodeList, SoftAssert softAssert) {
		if (nodeList.size() > 0) {
			for (JsonNode node : nodeList) {
				softAssert.assertTrue((node.asInt() >= 1 && node.asInt() <= 4), "'PickInRound' node is invalid!");
			}
		} else {
			softAssert.fail("'PickInRound' nodes is not present!");
		}
	}

	private void verifyDraftYear(JsonNode nodeList, SoftAssert softAssert) {
		if (nodeList.size() > 0) {
			for (JsonNode node : nodeList) {
				softAssert.assertTrue((node.asInt() == 2003 || node.asInt() == 2004), "'PickInRound' node is invalid!");
			}
		} else {
			softAssert.fail("'PickInRound' nodes is not present!");
		}
	}

	private void verifyRounds(JsonNode nodeList, SoftAssert softAssert) {
		if (nodeList.size() > 0) {
			for (JsonNode node : nodeList) {
				softAssert.assertTrue((node.asInt() == 1 || node.asInt() == 2), "'round' node is invalid!");
			}
		} else {
			softAssert.fail("'PickInRound' nodes is not present!");
		}
	}
}
