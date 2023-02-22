package com.disney.qa.tests.nhl.api;

import com.qaprosoft.carina.core.foundation.report.qtest.QTestCases;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.annotations.Test;

import java.io.IOException;

public class NhlConfigurationsTest extends BaseNhlApiTest {

    /**
     * JIRA https://jira.mlbam.com/browse/SDAPINHL-1116
     */
    @QTestCases(id = "42873")
    @Test
    public void verifyConfigurations() throws IOException, JSONException {
        String rawResponse = nhlStatsApiContentService.getConfigurations(null, String.class);
        String configurationsSnapshot = fileUtils.getResourceFileAsString("nhl/api/configurations/configurations.json");
        JSONAssert.assertEquals(configurationsSnapshot, rawResponse, JSONCompareMode.LENIENT);
    }

}
