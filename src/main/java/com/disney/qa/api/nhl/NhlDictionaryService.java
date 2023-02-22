package com.disney.qa.api.nhl;

import com.disney.qa.api.nhl.statsapi.NhlStatsApiDictionaryProvider;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class NhlDictionaryService {

    protected NhlDictionaryProvider nhlDictionaryProvider;

    public NhlDictionaryProvider getNhlDictionaryProvider() {
        return nhlDictionaryProvider;
    }

    public void setNhlDictionaryProvider(NhlDictionaryProvider nhlDictionaryProvider) {
        this.nhlDictionaryProvider = nhlDictionaryProvider;
    }

    public String getTeamMetadataSourceKey(){
        return nhlDictionaryProvider.getTeamMetadataSourceKey();
    }

    public String getStatRankSourceKey() {
        return nhlDictionaryProvider.getStatRankSourceKey();
    }

    public String getDivAtlanticSourceKey() {
        return nhlDictionaryProvider.getDivAtlanticSourceKey();
    }

    public String getDivPacificSourceKey() {
        return nhlDictionaryProvider.getDivPacificSourceKey();
    }

    public String getDivCentralSourceKey() {
        return nhlDictionaryProvider.getDivCentralSourceKey();
    }

    public String getDivMetroSourceKey() {
        return nhlDictionaryProvider.getDivMetroSourceKey();
    }

    public String getDivLabelSourceKey(){
        return nhlDictionaryProvider.getDivLabelSourceKey();
    }

    public String getConfLabelSourceKey(){
        return nhlDictionaryProvider.getConfLabelSourceKey();
    }

    public String getDivTricodeSourceKey() {
        return nhlDictionaryProvider.getDivTricodeSourceKey();
    }

    public String getPlayoffsScheduleSourceKey() {
        return nhlDictionaryProvider.getPlayoffsScheduleSourceKey();
    }

    public String getPlayoffsRoundSourceKey() {
        return nhlDictionaryProvider.getPlayoffsRoundSourceKey();
    }

    public String getPlayoffsSeriesMatchupSourceKey(){
        return nhlDictionaryProvider.getPlayoffsSeriesMatchupSourceKey();
    }

    public String getPlayoffsSeriesStatusSourceKey(){
        return nhlDictionaryProvider.getPlayoffsSeriesStatusSourceKey();
    }

    public String getStandingsSourceKey(){
        return nhlDictionaryProvider.getStandingsSourceKey();
    }

    public String getPeriodSourceKey(){
        return nhlDictionaryProvider.getPeriodSourceKey();
    }

    public List<String> getTeamIds() {
        return nhlDictionaryProvider.getTeamIds();
    }

    public List<String> getTeamResponseFilter(){
        return nhlDictionaryProvider.getTeamResponseFilter();
    }

    public List<String> getStandingsWildcardFilter(){
        return nhlDictionaryProvider.getStandingsWildcardFilter();
    }

    public List<String> getPlayoffsScheduleFilter(){
        return nhlDictionaryProvider.getPlayoffsScheduleFilter();
    }

    public List<String> getStandingsLossFilter(){
        return nhlDictionaryProvider.getStandingsLossFilter();
    }

    public List<String> getPeriodAbbrevFilter(){
        return nhlDictionaryProvider.getPeriodAbbrevFilter();
    }

    public List<String> getIntTeamIds() {
        return nhlDictionaryProvider.getIntTeamIds();
    }

    public JsonNode getDictionary(NhlStatsApiDictionaryProvider.DictionaryLanguage language) {
        return nhlDictionaryProvider.getDictionary(language);
    }

    public JsonNode getDictionary(NhlStatsApiDictionaryProvider.DictionaryLanguage language, String version) {
        return nhlDictionaryProvider.getDictionary(language, version);
    }

    public String getDictionaryResponse(NhlStatsApiDictionaryProvider.DictionaryLanguage language) {
        return nhlDictionaryProvider.getDictionaryResponse(language);
    }

    public String getFullTeamName(String teamId, String dictionary) {
        return nhlDictionaryProvider.getFullTeamName(teamId, dictionary);
    }

    public String getTeamLocation(String teamId, String dictionary) {
        return nhlDictionaryProvider.getTeamLocation(teamId, dictionary);
    }

    public String getTeamNickname(String teamId, String dictionary) {
        return nhlDictionaryProvider.getTeamNickname(teamId, dictionary);
    }

    public String getTeamTricode(String teamId, String dictionary) {
        return nhlDictionaryProvider.getTeamTricode(teamId, dictionary);
    }

    public String getStandingsWildcardShort(String dictionary) {
        return nhlDictionaryProvider.getStandingsWildcardShort(dictionary);
    }

    public String getPlayoffsScheduleGame(String dictionary) {
        return nhlDictionaryProvider.getPlayoffsScheduleGame(dictionary);
    }

    public String getPlayoffRoundName(String dictionary, String round){
        return nhlDictionaryProvider.getPlayoffRoundName(dictionary, round);
    }

    public String getDivisionTricodes(String dictionary, int labelNum){
        return nhlDictionaryProvider.getDivisionTricodes(dictionary, labelNum);
    }

    public String getDivisionLabels(String dictionary, int labelNum){
        return nhlDictionaryProvider.getDivisionLabels(dictionary, labelNum);
    }

    public String getConferenceLables(String dictionary, int labelNum){
        return nhlDictionaryProvider.getConferenceLables(dictionary, labelNum);
    }

    public String getJsonKeyValue(String dictionary, String key) {
        return nhlDictionaryProvider.getJsonKeyValue(dictionary, key);
    }

    public List<String> getRoundsList() {
        return nhlDictionaryProvider.getRoundsList();
    }

    public List<String> getDivisionTagList() {
        return nhlDictionaryProvider.getDivisionTagList();
    }
}
