package com.disney.qa.api.nhl.statsapi;

import com.disney.qa.api.ApiContentProvider;
import com.disney.qa.api.nhl.NhlDictionaryProvider;
import com.disney.qa.api.nhl.NhlParameters;
import com.disney.qa.common.http.resttemplate.RestTemplateBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class NhlStatsApiDictionaryProvider extends ApiContentProvider implements NhlDictionaryProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String NHL_DICTIONARY_HOST = NhlParameters.getNhlDictionaryHost();
    private RestTemplate restTemplate;
    protected Configuration jsonPathJacksonConfiguration;
    protected String nhlStatsApiHost;

    //Strings for returning Json keys to test that contain these values
    private String teamMetadataSourceKey = "team";
    private String statRankSourceKey = "InNHL";
    private String divAtlanticSourceKey = "divAtlantic";
    private String divPacificSourceKey = "divPacific";
    private String divCentralSourceKey = "divCentral";
    private String divMetroSourceKey = "divMetropolitan";
    private String divLabelSourceKey = "divLabel_";
    private String confLabelSourceKey = "confLabel_";
    private String divTricodeSourceKey  = "divTricode_";
    private String playoffsScheduleSourceKey = "playoffsScheduleGame";
    private String playoffsRoundSourceKey = "tournament.round";
    private String playoffsSeriesMatchupSourceKey = "series.matchup";
    private String playoffsSeriesStatusSourceKey = "series.status";
    private String standingsSourceKey = "standings";
    private String periodSourceKey = "period";

    //Filters for determining if a key in a specific test should be asserted
    private static final List<String> teamResponseFilter = Lists.newArrayList("teamFull_", "teamLocation_", "teamNickname_", "teamTri");
    private static final List<String> standingsWildcardFilter = Lists.newArrayList("standingsWildCardShort");
    private static final List<String> playoffsScheduleFilter = Lists.newArrayList("playoffsScheduleGame");
    private static final List<String> standingsLossFilter = Lists.newArrayList("standingsLossAbbrev", "standingsOvertimeShootoutLossAbbrev");
    private static final List<String> periodAbbrevFilter = Lists.newArrayList("Abbrev");

    private static final List<String> teamIds = Lists.newArrayList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "28", "29", "30",
            "52", "53", "54");

    private static final List<String> intTeamIds = Lists.newArrayList("7401", "7402", "7403", "7404", "7405",
            "7406", "7407", "7408");

    private List<String> roundsList = Lists.newArrayList("R1", "R2", "DSF", "DF", "CQF",
            "CSF", "CF", "F", "SCF");

    private List<String> divisionTag = Lists.newArrayList("", "Abbrev", "Short", "Tri");

    public String getNhlStatsApiHost() {
        return nhlStatsApiHost;
    }

    public void setNhlStatsApiHost(String nhlStatsApiHost) {
        this.nhlStatsApiHost = nhlStatsApiHost;
    }

    public enum DictionaryLanguage {
        EN("en"),
        ES("es"),
        FR("fr"),
        RU("ru"),
        FI("fi"),
        SV("sv"),
        CS("cs"),
        SK("sk"),
        DE("de");

        private String language;

        DictionaryLanguage(String language) {
            this.language = language;
        }

        public String getLanguage() {
            return language;
        }
    }

    @Override
    public String getHost() {
        return getNhlStatsApiHost();
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    public List<String> getTeamIds() {
        return teamIds;
    }

    @Override
    public List<String> getTeamResponseFilter(){
        return teamResponseFilter;
    }

    @Override
    public List<String> getStandingsWildcardFilter() {
        return standingsWildcardFilter;
    }

    @Override
    public List<String> getPlayoffsScheduleFilter() {
        return playoffsScheduleFilter;
    }

    @Override
    public List<String> getStandingsLossFilter() {
        return standingsLossFilter;
    }

    @Override
    public List<String> getPeriodAbbrevFilter() {
        return periodAbbrevFilter;
    }

    @Override
    public List<String> getIntTeamIds() {
        return intTeamIds;
    }

    @Override
    public List<String> getRoundsList() {
        return roundsList;
    }

    @Override
    public List<String> getDivisionTagList() {
        return divisionTag;
    }

    @Override
    public String getTeamMetadataSourceKey(){
        return teamMetadataSourceKey;
    }

    @Override
    public String getStatRankSourceKey() {
        return statRankSourceKey;
    }

    @Override
    public String getDivAtlanticSourceKey() {
        return divAtlanticSourceKey;
    }

    @Override
    public String getDivPacificSourceKey() {
        return divPacificSourceKey;
    }

    @Override
    public String getDivCentralSourceKey() {
        return divCentralSourceKey;
    }

    @Override
    public String getDivMetroSourceKey() {
        return divMetroSourceKey;
    }

    @Override
    public String getDivLabelSourceKey(){
        return divLabelSourceKey;
    }

    @Override
    public String getConfLabelSourceKey(){
        return confLabelSourceKey;
    }

    @Override
    public String getDivTricodeSourceKey() {
        return divTricodeSourceKey;
    }

    @Override
    public String getPlayoffsScheduleSourceKey() {
        return playoffsScheduleSourceKey;
    }

    @Override
    public String getPlayoffsSeriesMatchupSourceKey(){
        return playoffsSeriesMatchupSourceKey;
    }

    @Override
    public String getPlayoffsSeriesStatusSourceKey(){
        return playoffsSeriesStatusSourceKey;
    }

    @Override
    public String getPeriodSourceKey(){
        return periodSourceKey;
    }

    @Override
    public String getPlayoffsRoundSourceKey() {
        return playoffsRoundSourceKey;
    }

    @Override
    public String getStandingsSourceKey(){
        return standingsSourceKey;
    }

    public NhlStatsApiDictionaryProvider(){
        restTemplate = RestTemplateBuilder.newInstance().
                withSpecificJsonMessageConverter().withUtf8EncodingMessageConverter().build();
        jsonPathJacksonConfiguration = Configuration.builder()
                .mappingProvider(new JacksonMappingProvider()).jsonProvider(new JacksonJsonNodeJsonProvider()).build();
    }

    public JsonNode getDictionary(DictionaryLanguage language){
        String requestPath = NHL_DICTIONARY_HOST + language.getLanguage() + "-ALL/default-v1.json";
        LOGGER.debug("Pulling Dictionary response from: " + requestPath);
        return restTemplate.getForEntity(requestPath, JsonNode.class).getBody();
    }


    public JsonNode getDictionary(DictionaryLanguage language, String version){
        String requestPath = NHL_DICTIONARY_HOST + language.getLanguage() + "-ALL/default-".concat(version).concat(".json");
        LOGGER.debug("Pulling Dictionary response from: " + requestPath);
        return restTemplate.getForEntity(requestPath, JsonNode.class).getBody();
    }


    //Methods for testing the Dictionary file local against acceptance file
    @Override
    public String getDictionaryResponse(NhlStatsApiDictionaryProvider.DictionaryLanguage language){
        return new NhlStatsApiDictionaryProvider().getDictionary(language).toString();
    }

    @Override
    public String getFullTeamName(String teamId, String dictionary){
        String fullNameQuery = "$..teamFull_" + teamId;
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read(fullNameQuery));
    }

    @Override
    public String getTeamLocation(String teamId, String dictionary){
        String fullNameQuery = "$..teamLocation_" + teamId;
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read(fullNameQuery));
    }

    @Override
    public String getTeamNickname(String teamId, String dictionary){
        String fullNameQuery = "$..teamNickname_" + teamId;
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read(fullNameQuery));
    }

    @Override
    public String getTeamTricode(String teamId, String dictionary){
        String fullNameQuery = "$..teamTri_" + teamId;
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read(fullNameQuery));
    }

    @Override
    public String getStandingsWildcardShort(String dictionary){
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read("$..standingsWildCard"));
    }

    @Override
    public String getPlayoffsScheduleGame(String dictionary){
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read("$..playoffsScheduleGame"));
    }

    @Override
    public String getPlayoffRoundName(String dictionary, String round){
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read(String.format("$..['tournament.round.%s.name']", round)));
    }

    @Override
    public String getDivisionTricodes(String dictionary, int labelNum){
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read(String.format("$..divTricode_%s:", labelNum)));
    }

    @Override
    public String getDivisionLabels(String dictionary, int labelNum){
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read(String.format("$..divLabel_%s", labelNum)));
    }

    @Override
    public String getConferenceLables(String dictionary, int labelNum){
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read(String.format("$..confLabel_%s", labelNum)));
    }

    @Override
    public String getJsonKeyValue(String dictionary, String key){
        return simpleNodeReturn(JsonPath.using(jsonPathJacksonConfiguration).parse(dictionary)
                .read(String.format("$..['%s']", key)));
    }

    private String simpleNodeReturn(JsonNode node){
        if(node.size() == 0){
            return "";
        }
        return node.get(0).asText();
    }
}
