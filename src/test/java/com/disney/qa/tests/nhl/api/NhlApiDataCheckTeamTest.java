package com.disney.qa.tests.nhl.api;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.google.common.collect.ImmutableMap;
import com.qaprosoft.carina.core.foundation.dataprovider.annotations.XlsDataSourceParameters;
import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.Map;

public class NhlApiDataCheckTeamTest extends BaseNhlApiTest {

    /**
     * JIRA ticket: https://jira.bamtechmedia.com/browse/QAA-1349
     */
    @QTestCases(id = "42996")
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "DataProvider", description = "Verify: Team Page")
    @XlsDataSourceParameters(sheet = "abbreviation", dsUid = "TUID, Name")
    public void verifyTeams(Map<String, String> data) throws IOException, ProcessingException {

        String rawResponse = nhlStatsApiContentService.getTeams(ImmutableMap.of("teamId", data.get("id"), "fields", "", "expand", "team.stats"), String.class);
        String statusCode = getHttpStatus("/api/v1/teams?teamId=" + data.get("id") + "&fields=&expand=team.stats");

        SoftAssert softAssert = new SoftAssert();

        String[] statNodes = getStatNodes();
        for (String nodeName : statNodes) {
            verifyNodeIsPresent(softAssert, nodeName, rawResponse);
            softAssert.assertTrue(!nodeName.isEmpty(), String.format("Expected nodes to be populating: %s", nodeName));
        }

        softAssert.assertTrue("200".equals(statusCode), String.format("Status code should be '200': %s", statusCode));

        softAssert.assertAll();
    }

    private String[] getStatNodes() {
        return new String[]{
                "gamesPlayed",
                "wins",
                "losses",
                "ot",
                "pts",
                "ptPctg",
                "powerPlayPercentage",
                "penaltyKillPercentage",
                "shotsAllowed",
                "faceOffWinPercentage",
        };
    }
}
