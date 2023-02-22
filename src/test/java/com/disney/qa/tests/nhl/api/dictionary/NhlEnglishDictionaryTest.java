package com.disney.qa.tests.nhl.api.dictionary;

import com.disney.qa.api.nhl.statsapi.NhlStatsApiDictionaryProvider;
import com.disney.qa.tests.nhl.api.BaseNhlApiTest;
import org.json.JSONException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

public class NhlEnglishDictionaryTest extends BaseNhlApiTest {

    private static final NhlStatsApiDictionaryProvider.DictionaryLanguage DEFAULT_DICTIONARY_LANGUAGE =
            NhlStatsApiDictionaryProvider.DictionaryLanguage.EN;

    @BeforeMethod
    public void setAssertTest(){
        assertDictionaryTest = false;
    }

    /*
     * Test does not have a skip check because every team should be present on the baseline file with all corresponding
     * data. If a test is reported with a expectation of blank (""), then CMS should be contacted immediately.
     */
    @Test(description = "Verify response of Team naming texts")
    public void verifyTeamNamingItemsAreCorrect() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getTeamMetadataSourceKey()),
                nhlStatsApiDictionaryService.getTeamResponseFilter());

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getTeamResponseFilter().toString());
    }

    @Test(description = "Verify response for Wildcard short")
    public void verifyWildcardShortIsCorrect() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getStandingsSourceKey()),
                nhlStatsApiDictionaryService.getStandingsWildcardFilter());

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getStandingsWildcardFilter().toString());
    }

    @Test(description = "Verify response for Playoffs Schedule Game")
    public void verifyPlayoffsScheduleGameIsCorrect() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getPlayoffsScheduleSourceKey()),
                nhlStatsApiDictionaryService.getPlayoffsScheduleFilter());

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getPlayoffsScheduleFilter().toString());
    }

    @Test(description = "Verify returned values for tournament rounds are correct")
    public void testCheckPlayoffRoundValues() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getPlayoffsRoundSourceKey()));

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getPlayoffsRoundSourceKey());
    }

    @Test(description = "Verify returned division tricode values")
    public void testCheckDivisionTricodes() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getDivTricodeSourceKey()));

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getDivTricodeSourceKey());
    }

    @Test(description = "Verify returned division label values")
    public void testCheckDivisionLabels() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getDivLabelSourceKey()));

        performDictionaryAssertion(sa,  nhlStatsApiDictionaryService.getDivLabelSourceKey());
    }

    @Test(description = "Verify returned conference label values")
    public void testCheckConferenceLabels() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getConfLabelSourceKey()));

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getConfLabelSourceKey());
    }

    @Test(description = "Verify Atlantic division Abbreviation, Tricode, and Shortname")
    public void testCheckAtlanticDivisionValues() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getDivAtlanticSourceKey()));

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getDivAtlanticSourceKey());
    }

    @Test(description = "Verify Central division Abbreviation, Tricode, and Shortname")
    public void testCheckCentralDivisionValues() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getDivCentralSourceKey()));

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getDivCentralSourceKey());
    }

    @Test(description = "Verify Metropolitan division Abbreviation, Tricode, and Shortname")
    public void testCheckMetropolitanDivisionValues() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getDivMetroSourceKey()));

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getDivMetroSourceKey());
    }

    @Test(description = "Verify Pacific division Abbreviation, Tricode, and Shortname")
    public void testCheckPacificDivisionValues() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getDivPacificSourceKey()));

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getDivPacificSourceKey());
    }

    @Test(description = "Verify the returned values of all 25 stored stat ranks")
    public void testCheckStatRankValues() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getStatRankSourceKey()));

        performDictionaryAssertion(sa, "statRank%sInNHL");
    }

    @Test(description = "Verify Playoff matchup values")
    public void testCheckPlayoffMatchupValues() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getPlayoffsSeriesMatchupSourceKey()));

        performDictionaryAssertion(sa, "series.matchup.*");
    }

    @Test(description = "Verify the returned values of Series Status values")
    public void testCheckPlayoffSeriesStatusValues() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getPlayoffsSeriesStatusSourceKey()));

        performDictionaryAssertion(sa, "series.status.*");
    }

    @Test(description = "Verify the returned values standings loss abbreviations")
    public void testCheckStandingsLossValues() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getStandingsSourceKey()),
                nhlStatsApiDictionaryService.getStandingsLossFilter());

        performDictionaryAssertion(sa, nhlStatsApiDictionaryService.getStandingsLossFilter().toString());
    }

    @Test(description = "Verify the returned values for Period Ordinal abbreviations")
    public void testCheckPeriodOrdinalAbbreviations() throws IOException, JSONException {
        SoftAssert sa = assertKeys(DEFAULT_DICTIONARY_LANGUAGE,
                getJsonKeys(expectedDictionaryResponse(DEFAULT_DICTIONARY_LANGUAGE), nhlStatsApiDictionaryService.getPeriodSourceKey()),
                nhlStatsApiDictionaryService.getPeriodAbbrevFilter());

        performDictionaryAssertion(sa, "period%sAbbrev");
    }
}
