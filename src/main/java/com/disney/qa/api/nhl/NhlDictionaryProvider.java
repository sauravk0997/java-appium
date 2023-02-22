package com.disney.qa.api.nhl;

import com.disney.qa.api.nhl.statsapi.NhlStatsApiDictionaryProvider;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface NhlDictionaryProvider {

    List<String> getTeamIds();

    List<String> getTeamResponseFilter();

    List<String> getStandingsWildcardFilter();

    List<String> getPlayoffsScheduleFilter();

    List<String> getStandingsLossFilter();

    List<String> getPeriodAbbrevFilter();

    String getTeamMetadataSourceKey();

    String getStatRankSourceKey();

    String getDivAtlanticSourceKey();

    String getDivPacificSourceKey();

    String getDivCentralSourceKey();

    String getDivMetroSourceKey();

    String getDivTricodeSourceKey();

    String getPlayoffsScheduleSourceKey();

    String getPlayoffsSeriesMatchupSourceKey();

    String getPlayoffsSeriesStatusSourceKey();

    String getStandingsSourceKey();

    String getPeriodSourceKey();

    String getPlayoffsRoundSourceKey();

    String getDivLabelSourceKey();

    String getConfLabelSourceKey();

    List<String> getIntTeamIds();

    List<String> getRoundsList();

    List<String> getDivisionTagList();

    JsonNode getDictionary(NhlStatsApiDictionaryProvider.DictionaryLanguage language);

    JsonNode getDictionary(NhlStatsApiDictionaryProvider.DictionaryLanguage language, String version);

    String getDictionaryResponse(NhlStatsApiDictionaryProvider.DictionaryLanguage language);

    String getFullTeamName(String teamId, String dictionary);

    String getTeamLocation(String teamId, String dictionary);

    String getTeamNickname(String teamId, String dictionary);

    String getTeamTricode(String teamId, String dictionary);

    String getStandingsWildcardShort(String dictionary);

    String getPlayoffsScheduleGame(String dictionary);

    String getPlayoffRoundName(String dictionary, String round);

    String getDivisionTricodes(String dictionary, int labelNum);

    String getDivisionLabels(String dictionary, int labelNum);

    String getConferenceLables(String dictionary, int labelNum);

    String getJsonKeyValue(String dictionary, String key);
}
