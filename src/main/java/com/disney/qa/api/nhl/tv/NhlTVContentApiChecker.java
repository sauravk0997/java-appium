package com.disney.qa.api.nhl.tv;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.collections4.map.MultiValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NhlTVContentApiChecker extends NhlTVContentApiCaller {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /** Get team info data **/
    public MultiValueMap<String, String> getTeamInfo(JsonNode rawResponse){
        MultiValueMap<String, String> teams = new MultiValueMap<>();
        String jsonPathTeamNames = "$..name";

        JsonNode teamsNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamNames);

        if(!teamsNode.toString().isEmpty()){
            for(JsonNode node : teamsNode){
                String name = node.asText();

                teams.put(name, getTeamName(name, rawResponse));
                teams.put(name, getTeamDivision(name, rawResponse));
                teams.put(name, getTeamConference(name, rawResponse));
                teams.put(name, getTeamAbreviation(name, rawResponse));
                teams.put(name, getTeamName(name, rawResponse));
                teams.put(name, getTeamActiveStatus(name, rawResponse));
            }
        }

        return teams;
    }

    private String getTeamName(String teamFullName, JsonNode rawResponse){
        String jsonPathTeamNames = "$..teams[?(@.name=='"+ teamFullName +"')].name";

        JsonNode teamsNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamNames);

        return teamsNode.asText();
    }

    private String getTeamShortName(String teamFullName, JsonNode rawResponse){
        String jsonPathTeamNames = "$..teams[?(@.name=='"+ teamFullName +"')].shortName";

        JsonNode teamsNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamNames);

        return teamsNode.asText();
    }

    private String getTeamAbreviation(String teamFullName, JsonNode rawResponse){
        String jsonPathTeamNames = "$..teams[?(@.name=='"+ teamFullName +"')].abbreviation";

        JsonNode teamsNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamNames);

        return teamsNode.asText();
    }


    private String getTeamDivision(String teamFullName, JsonNode rawResponse){
        String jsonPathTeamNames = "$..teams[?(@.name=='"+ teamFullName +"')].division.name";

        JsonNode teamsNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamNames);

        return teamsNode.asText();
    }

    private String getTeamConference(String teamFullName, JsonNode rawResponse){
        String jsonPathTeamNames = "$..teams[?(@.name=='"+ teamFullName +"')].conference.name";

        JsonNode teamsNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamNames);

        return teamsNode.asText();
    }

    //TODO: create a helper method to obtain list of items from a jsonNode for all methods that do that operation
    public List<String> getTeamsCarouselHeaderTopicIds(JsonNode rawResponse){
        String contentId = "$..topicId";

        LinkedList<String> sectionTopicID = new LinkedList<>();
        JsonNode moduleTitles = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(contentId);

        if(moduleTitles.size() >= 1){
            for(JsonNode node : moduleTitles){
                sectionTopicID.add(node.asText());
            }
        }
        return sectionTopicID;
    }

    public List<String> getTeamsCarouselHeaderTitles(JsonNode rawResponse){
        String contentTitle = "$..itemsList..title";

        LinkedList<String> sectionHeaders = new LinkedList<>();
        JsonNode moduleTitles = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(contentTitle);

        if(moduleTitles.size() >= 1){
            for(JsonNode node : moduleTitles){
                sectionHeaders.add(node.asText());
            }
        }
        return sectionHeaders;
    }

    public List<String> getTeamsCarouselRowTitles(JsonNode rawResponse){
        String contentIdQuery = "$..blurb";

        LinkedList<String> sectionHeaders = new LinkedList<>();
        JsonNode moduleTitles = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(contentIdQuery);

        if(moduleTitles.size() >= 1){
            for(JsonNode node : moduleTitles){
                sectionHeaders.add(node.asText());
            }
        }

        Set<String> blurbs = new LinkedHashSet<>(sectionHeaders);
        sectionHeaders.clear();
        sectionHeaders.addAll(blurbs);

        return sectionHeaders;
    }

    private String getTeamActiveStatus(String teamFullName, JsonNode rawResponse){
        String jsonPathTeamNames = "$..teams[?(@.name=='"+ teamFullName +"')].active";

        JsonNode teamsNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamNames);

        return teamsNode.asText();
    }

    /** Get Schedule/Scores data **/
    public List<NhlTVGameModel> getScoresGamesInfo(JsonNode rawResponse){
        List<NhlTVGameModel> scoresGames = new LinkedList<>();
        String jsonPathTeamNames = "$..games";

        JsonNode gamesNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamNames);

        if(!gamesNode.toString().isEmpty()){
            for(JsonNode node : gamesNode){
                //using game status as a base for total games
                Map<String, String> awayTeamAbbrs = getTeamAbbrs(node, true);
                Map<String, String> homeTeamAbbrs = getTeamAbbrs(node, false);
                List<String> gameStatuses = getGameStatuses(node);

                for (int i = 0; i < gameStatuses.size(); ++i) {
                    NhlTVGameModel game = new NhlTVGameModel();
                    Map.Entry<String, String> awayEntry = (Map.Entry<String, String>) awayTeamAbbrs.entrySet().toArray()[i];
                    Map.Entry<String, String> homeEntry = (Map.Entry<String, String>) homeTeamAbbrs.entrySet().toArray()[i];
                    game.setAwayTeam(awayEntry.getKey());
                    game.setHomeTeam(homeEntry.getKey());
                    game.setGameStatus(gameStatuses.get(i));
                    game.setGameScore(getTeamScores(node, true).get(i).concat(" - ").
                            concat(getTeamScores(node, false).get(i)));
                    scoresGames.add(game);
                }
            }
        }

        return scoresGames;
    }

    private Map<String, String> getTeamAbbrs(JsonNode rawResponse, boolean away){
        Map<String, String> teams = new LinkedHashMap<>();
        String jsonPathTeamAbbr = "";
        if (away) {
            jsonPathTeamAbbr = "$..teams.away..abbreviation";
        } else {
            jsonPathTeamAbbr = "$..teams.home..abbreviation";
        }

        JsonNode teamNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamAbbr);

        if (!teamNode.toString().isEmpty()) {
            for (int i = 0; i < teamNode.size(); ++i){
                String teamName = "";
                String teamDivision = "";

                if (teamNode.get(i).asText().length() == 1){
                    teamDivision = teamNode.get(i).asText();
                    teamName = teamNode.get(i-1).asText();

                    LOGGER.info("Team name: " + teamName);
                    LOGGER.info("Team Division: " + teamDivision);

                    teams.put(teamName, teamDivision);
                }
            }
        }

        return teams;
    }

    private List<String> getTeamScores(JsonNode rawResponse, boolean away){
        List<String> scores = new LinkedList<>();
        String jsonPathTeamScore = "";
        if (away) {
            jsonPathTeamScore = "$..teams.away.score";
        } else {
            jsonPathTeamScore = "$..teams.home.score";
        }

        JsonNode scoresNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamScore);

        if (!scoresNode.toString().isEmpty()) {
            for (JsonNode node : scoresNode){
                scores.add(node.asText());
            }
        }

        return scores;
    }

    private List<String> getGameStatuses(JsonNode rawResponse){
        List<String> statuses = new LinkedList<>();
        String jsonPathTeamNames = "$..status.abstractGameState";

        JsonNode statusesNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathTeamNames);

        if (!statusesNode.toString().isEmpty()) {
            for (JsonNode node : statusesNode){
                statuses.add(node.asText());
            }
        }

        return statuses;
    }

    /** Get Scores > Selected Game > Highlight titles **/
    public List<String> getScoresGameHighlightsTitles(JsonNode rawResponse){
        List<String> highlightsTitles = new LinkedList<>();
        String jsonPathHighlightsTitles = "$..highlights..items[?(@.type == 'video')].description";

        JsonNode gamesNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathHighlightsTitles);

        if(!gamesNode.toString().isEmpty()){
            for(JsonNode node : gamesNode){
                highlightsTitles.add(node.textValue());
            }
        }

        return highlightsTitles;
    }

    /** Get Scores > Selected Game > Game Feeds titles **/
    public List<String> getScoresGameFeedsTitles(JsonNode rawResponse){
        List<String> gameFeeds = new LinkedList<>();
        String jsonPathGameFeedsTitles = "$..media..[?(@.title == 'NHLTV')].items..mediaFeedType";
        String jsonPathGameFeedsCallLetters = "$..media..[?(@.title == 'NHLTV')].items..callLetters";
        String jsonPathGameFeedsFeedName = "$..media..[?(@.title == 'NHLTV')].items..feedName";
        String jsonPathCondensedGameFeed = "$..media..[?(@.title == 'Extended Highlights')].items..duration";
        String jsonPathRecapFeed = "$..media..[?(@.title == 'Recap')].items..duration";

        JsonNode gameFeedsNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathGameFeedsTitles);
        JsonNode gameFeedsNodeCallLetters = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathGameFeedsCallLetters);
        JsonNode gameFeedsNodeFeedName = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathGameFeedsFeedName);
        JsonNode recapFeedsNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathRecapFeed);
        JsonNode condensedGameFeedNode = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPathCondensedGameFeed);

        if(!gameFeedsNodeCallLetters.toString().isEmpty()){
            addData(gameFeedsNodeCallLetters, gameFeeds);
        }

        if(!gameFeedsNodeFeedName.toString().isEmpty()){
            addData(gameFeedsNodeFeedName, gameFeeds);
        }

        if(!recapFeedsNode.toString().isEmpty()) {
            addData(recapFeedsNode, gameFeeds);
        }

        if(!condensedGameFeedNode.toString().isEmpty()) {
            addData(condensedGameFeedNode, gameFeeds);
        }

        return gameFeeds;
    }

    private void addData(JsonNode node, List<String> list){
        for(JsonNode nd : node){
            if (!nd.textValue().isEmpty()) {
                list.add(nd.textValue());
            }
        }
    }
}
