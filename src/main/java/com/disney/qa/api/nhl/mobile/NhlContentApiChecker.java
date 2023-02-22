package com.disney.qa.api.nhl.mobile;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.testng.SkipException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NhlContentApiChecker extends NhlContentApiCaller {

    //Queries are used on Team Page api responses to get section headers, topic IDs, and content titles
    private String sectionHeadersQuery = "$..content..itemsList[?(@.type=='%s')].title";
    private String contentIdQuery = "$..content..itemsList[?(@.title==\"%s\")].topicId";
    private String menuContent = "$..[?(@.title=='%s' && @.type=='more')]..content..title";
    private String headerlessMenuItemsQuery = "$..content..itemsList[?(@.title=='')].*..title";

    //News queries return various aspects of a specific media items's metadata
    private String authorQuery = "$..docs..partner.byline";
    private String postDateQuery  = "$..docs.[%s].partner.date";
    private String keywordQuery = "$..docs.[%s].partner.primaryKeyword.displayName";
    private String headlineQuery = "$..docs.[%s].partner.headline";

    //Team page queries
    private String sourceAuthorQuery = "$..content..[?(@.title=='%s')]..articlesLongList[?(@.type=='article')].contributor..name";
    private String sourceDateQuery = "$..content..[?(@.title=='%s')]..articlesLongList[?(@.type=='article')].date";
    private String sourceHeadlineQuery = "$..content..[?(@.title=='%s')]..articlesLongList[?(@.type=='article')]..seoTitle";

    //Video queries return metadata for video items
    private String videoHeadlineQuery = "$..docs[?(@.type=='video')].blurb";
    private String videoPostDateQuery = "$..display_timestamp";
    private String videoKeywordQuery = "$..title";
    private String videoDurationQuery = "$..duration";

    //Arena queries return items to check in the Arena API attached to a Team Page for display tests
    private String arenaSdkTextQuery = "$..itemsList[?(@.type=='venue')].%s";
    private String arenaSdkListMenuHeadersQuery = "$..menu[?(@.menuLayout=='%s')].displayName";
    private String arenaSdkMenuItemsQuery = "$..menu[?(@.displayName=='%s')].menuItems..%s";
    private String arenaSdkVenueFloorsQuery = "$..zones[*].name";
    private String arenaSdkVenueDefaultFloorQuery = "$..zones[?(@.isDefaultZone==true)].name";
    private String arenaSdkVenueFloor1 = "$..zones[0].name";
    private String arenaSdkSearchResultsFloorQuery = "$..locationHierarchy[0]..value";
    private String arenaSdkSearchResultsVenuesQuery = "$..[?(@.type=='place')].title";
    private String arenaSdkSearchResultsVenueLocationsQuery = "$..results[%s]..locationHierarchy..value";

    //Config queries
    private String configWhatsNewQuery = "$.whatsNewVersion";
    private String configMoreMenuItemsQuery = "$.navigationMenuOrderableItems";
    private String configGameCenterTabs = "$..disabledLiveTabs.[*]";
    private String configPlayersToWatch = "$..playersToWatch.enabled";

    //Scoreboard queries
    private String scheduleGamePks = "$..gamePk";
    private String scheduleHomeAwayTeamName = "$..games.[?(@.gamePk=='%s')].teams.*.team.teamName";
    private String scheduleHomeAwayTeamID = "$..games.[?(@.gamePk=='%s')].teams.*.team.id";
    private String scheduleGameTickets = "$..games.[?(@.gamePk=='%s')]..ticketType";
    private String schedulePreviewMetadata = "$..[?(@.gamePk=='%s')]..[?(@.type=='article')].%s";
    private String scheduleGameBroadcasters = "$..[?(@.gamePk=='%s')]..broadcasts..name";
    private String scheduleGameVenue = "$..[?(@.gamePk=='%s')].venue.name";
    private String scheduleGameLabel = "$..[?(@.gamePk=='%s')]..[?(@.title=='NHLTV')].items.[?(@.mediaFeedType=='HOME')].gameLabel";
    private String scheduleGameDate = "$..[?(@.gamePk=='%s')].gameDate";

    //live?timecode queries
    private String coachesQuery = "$..%s.coaches..fullName";
    private String officialsQuery = "$..[?(@.officialType=='%s')]..fullName";

    //nhlstatic api queries
    private String nhlstaticPlayerShortNames = "$..%s..shortName";

    //roster queries
    private String rosterPlayerId = "$..[?(@.teamName=='%s')]..person.id";
    private String rosterPlayerVariableData = "$..[?(@.id=='%s')].%s";

    //team stat queries
    private String teamVariableData = "$..[?(@.teamName=='%s')].%s";
    private String teamNamesByDivision = "$..records.[%s].teamRecords..team.name";
    private String teamLastTenStat = "$..records.[%s].teamRecords.[%s].records..[?(@.type=='lastTen')]";

    //Gamecenter module query
    private String gamecenterModuleEnabled = "$..gameCenterConfig.sections.%s.enabled";

    private static String error = "ERROR: Invalid Data Type. Check parameters. Was sent '%s'";

    private static final String VIDEO = "video";
    private static final String ARTICLE = "article";

    private static final String [] MODULE_TYPES = {"schedule", ARTICLE, VIDEO, "stats", "insider", "more"};
    private static final String [] LEADER_CATEGORIES = {"points", "assists", "goals", "plusMinus"};

    /** Returns a column ID for team page layout tests.
     * Only Schedule and a News module should have a value of 1. Everything else should be a 2.
     * Nothing except an ad banner should have a value of 0.
     *
     * @param rawResponse - Team Page API data
     */
    public List<String> getTeamPageModuleColumnIDs(JsonNode rawResponse){
        String jsonPath = "$..tabletColumn";

        JsonNode moduleLocations = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPath);

        LinkedList<String> tabletCols = new LinkedList<>();

        if(moduleLocations.size() >= 1){
            for(JsonNode node : moduleLocations){
                tabletCols.add(node.asText());
            }
        }

        return tabletCols;
    }

    /** Returns a list of the titles for a team page's modules that are shown in the app.
     * Modules have custom values which are controlled by CMS, so parsing the values
     * in accordance with the mobile tests was done.
     *
     * @param rawResponse - Team Page API data
     * @return list of section headers
     */
    public List<String> getCustomTeamModuleTitles(JsonNode rawResponse){
        LinkedList<String> sectionHeaders = new LinkedList<>();

        for(String sectionType: MODULE_TYPES) {
            JsonNode moduleTitles = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                    .read(String.format(sectionHeadersQuery, sectionType));
            if(moduleTitles.size() >= 1){
                for(JsonNode node : moduleTitles){
                    sectionHeaders.add(node.asText());
                }
            }
        }
        if(sectionHeaders.contains("")){
            sectionHeaders.removeIf(""::equals);
            sectionHeaders.addAll(getMenuItemsWithNoHeader(rawResponse));
        }
        return sectionHeaders;
    }

    /** Returns media items that are present in a module that has no section header but instead
     * display the text as an overlay or are items contained in the listed items
     *
     * @param rawResponse - Team Page API data
     * @return
     */
    private List<String> getMenuItemsWithNoHeader(JsonNode rawResponse){
        LinkedList<String> menuItems = new LinkedList<>();
        JsonNode noTitleItems = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(headerlessMenuItemsQuery));
        for (JsonNode newNode : noTitleItems){
            menuItems.add(newNode.asText());
        }
        return menuItems;
    }

    /** Returns a list of items contained in a specific team page's module
     *
     * @param rawResponse - Team Page API data
     * @param moduleType - The name of the module you want to get items for
     * @return
     */
    public List<String> getSpecificSectionTitles(JsonNode rawResponse, String moduleType){
        LinkedList<String> sectionHeaders = new LinkedList<>();
        JsonNode sectionTitles = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(sectionHeadersQuery, moduleType));
        if(!sectionTitles.toString().isEmpty()){
            for(JsonNode node : sectionTitles){
                if(!node.asText().isEmpty()) {
                    sectionHeaders.add(node.asText());
                }
            }
        }
        return sectionHeaders;
    }

    /** Returns a list of items from a "more' module type, which is a list of items contained within the module.
     * A team page can have multiple 'more' modules, as this is controlled by the team and CMS
     *
     * @param rawResponse - Team Page API data
     * @param menuTitle - The name of the more menu module you want to get items for
     * @return
     */
    public List<String> getMoreMenuListedItems(JsonNode rawResponse, String menuTitle){
        LinkedList<String> menuList = new LinkedList<>();
        JsonNode menuItems = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(menuContent, menuTitle));
        if(!menuItems.toString().isEmpty()){
            for(JsonNode node : menuItems){
                menuList.add(node.asText());
            }
        }
        return menuList;
    }

    /** Returns the query to be used by passing a user-friendly String type
     *
     * @param data - A user friendly String that corresponds to a specific JsonPath query
     * @return
     */
    private String getDataType(String data){
        switch(data){
            case "Author":
                return authorQuery;
            case "Source Author":
                return sourceAuthorQuery;
            case "Source Date":
                return sourceDateQuery;
            case "Source Headline":
                return sourceHeadlineQuery;
            case "Video Headline":
                return videoHeadlineQuery;
            case "Video Duration":
                return videoDurationQuery;
            case "Video Date":
                return videoPostDateQuery;
            case "Video Keyword":
                return videoKeywordQuery;
            default:
                throw new SkipException(String.format(error, data));
        }
    }

    /** Returns a query for a specifc data type contained within a specific module on a team page
     *
     * @param data - User Friendly String input for what you want
     * @param moduleName - The name of the module you want to query
     * @return
     */
    private String getDataTypeWithModule(String data, String moduleName){
        switch(data){
            case "Source Author":
                return String.format(sourceAuthorQuery, moduleName);
            case "Source Date":
                return String.format(sourceDateQuery, moduleName);
            case "Source Headline":
                return String.format(sourceHeadlineQuery, moduleName);
            default:
                throw new SkipException(String.format(error, data));
        }
    }

    /** Returns a query for a data type at a specific index in a team's api response.
     * This should only be used against an iterated list where the index is guaranteed
     * to exist, as well as the fact that media is fluid and does not stay in the same
     * index for long.
     *
     * @param data - User friendly String for what you want to retrieve
     * @param index - The index location of the item you are checking (should be an iterator generated by a list of items)
     * @return
     */
    private String getDataTypeWithIndex(String data, int index){
        switch(data){
            case "Keyword":
                return String.format(keywordQuery, index);
            case "Headline":
                return String.format(headlineQuery, index);
            case "Date":
                return String.format(postDateQuery, index);
            default:
                throw new SkipException(String.format(error, data));
        }
    }

    /** Returns a module's data content as a lit (Author, date, etc.)
     *
     * @param rawResponse
     * @param dataType
     * @return
     */
    public List<String> getModuleContent(JsonNode rawResponse, String dataType){
        JsonNode content = JsonPath.using(jsonPathJacksonConfiguration).
                parse(rawResponse).read(getDataType(dataType));

        LinkedList<String> contentList = new LinkedList<>();

        if(!content.toString().isEmpty()){
            for(JsonNode node : content){
                contentList.add(node.asText());
            }
        }
        return contentList;
    }

    /** Returns a video module's content. Contains an empty String check due to differences in
     * video module display requirements.
     *
     * @param rawResponse
     * @param dataType
     * @return
     */
    public List<String> getClubVideoModuleContent(JsonNode rawResponse, String dataType){
        LinkedList<String> data = new LinkedList<>();

        JsonNode content = JsonPath.using(jsonPathJacksonConfiguration).
                parse(rawResponse).read(getDataType(dataType));

        if(!content.toString().isEmpty()){
            for(JsonNode node : content){
                if(!"".equals(node.asText())) {
                    data.add(node.asText());
                }
            }
        }
        return data;
    }

    /** Returns data specific to a news module based on the contents of the SearchAPI response passed.
     *
     * @param rawResponse - The SearchAPI json data
     * @param dataType - The data you wish to test
     * @param totalSize - An integer that should be generated prior by counting how many individual items exist
     *                  in the API's response
     * @return
     */
    public List<String> getNewsModuleContent(JsonNode rawResponse, String dataType, int totalSize){
        LinkedList<String> contentList = new LinkedList<>();
        for(int i = 0; i < totalSize; i++) {
            JsonNode content = JsonPath.using(jsonPathJacksonConfiguration).
                    parse(rawResponse).read(getDataTypeWithIndex(dataType, i));

            String formattedContent = content.toString();
            if(!"[]".equals(formattedContent)) {
                formattedContent = formattedContent.substring(2, formattedContent.length()-2);
                formattedContent = textFormatter(formattedContent);
            } else {
                formattedContent = formattedContent.substring(1, formattedContent.length()-1);
            }
            contentList.add(formattedContent);
        }
        return contentList;
    }

    /** Source Articles are media items that are provided on the actual Team API response and are not tied to the
     * searchApi response that is referenced.
     *
     * @param rawResponse - The team API response
     * @param dataType - The data type looking to be tested (Author, post date, etc.)
     * @param module - The module name the article appears in
     * @return
     */
    public List<String> getClubSourceArticleContent(JsonNode rawResponse, String dataType, String module){
        LinkedList<String> data = new LinkedList<>();

        JsonNode content = JsonPath.using(jsonPathJacksonConfiguration).
                parse(rawResponse).read(getDataTypeWithModule(dataType, module));

        if(!content.toString().isEmpty()){
            for(JsonNode node : content){
                data.add(textFormatter(node.asText()));
            }
        }
        return data;
    }

    /** Removes encoded characters that are passed from CMS which are then formatted in the app upon display.
     * Used for verifying test checks that contain unique characters.
     *
     * @param data - The string being formatted
     * @return
     */
    private String textFormatter(String data){
        data = data.replace("&amp;", "&")
                .replace("&#235;", "ë")
                .replace("\\", "")
                .replace("&quot;", "\"");
        return data;
    }

    /** Returns the RGB hex code value of a team page as listed in the configClubPage json file
     *
     * @param rawResponse - config data
     * @param teamId - The team ID being checked
     * @return
     */
    public String teamPageBackgroundColorHexValue(JsonNode rawResponse, int teamId){
        JsonNode rgbHexValue = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..[?(@.teamId=='" + teamId + "')]..background-color");

        return StringUtils.substringBetween(rgbHexValue.toString(), "[\"#", "\"]");
    }

    /** Runs a check if a a module that had an tabletColumn value of 0 is a sponsorship.
     * Is only run if the above comes back as true at test time.
     *
     * @param index - The location of the module in the team json
     * @param rawResponse - The team json response
     * @return
     */
    public boolean checkIfModuleIsSponsorship(int index, JsonNode rawResponse){
        String jsonPath = "$..itemsList["+index+"]..type";

        JsonNode moduleLocations = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse).read(jsonPath);

        return "sponsorship".equals(moduleLocations.toString().substring(2, moduleLocations.toString().length()-2));
    }

    /** Returns a list of news modules present in the team page api
     *
     * @param rawResponse
     * @return
     */
    public List<String> getCustomTeamNewsModuleTitles(JsonNode rawResponse){
        LinkedList<String> sectionHeaders = new LinkedList<>();
        JsonNode moduleTitles = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(sectionHeadersQuery, ARTICLE));

        if(moduleTitles.size() >= 1){
            for(JsonNode node : moduleTitles){
                sectionHeaders.add(node.asText());
            }
        }
        return sectionHeaders;
    }

    /** Returns a list of video modules in the team page api
     *
     * @param rawResponse
     * @return
     */
    public List<String> getCustomTeamVideoModuleTitles(JsonNode rawResponse){
        LinkedList<String> sectionHeaders = new LinkedList<>();
        JsonNode moduleTitles = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(sectionHeadersQuery, VIDEO));

        if(moduleTitles.size() >= 1){
            for(JsonNode node : moduleTitles){
                sectionHeaders.add(node.asText());
            }
        }
        return sectionHeaders;
    }

    /** Returns a specific module's content ID, which is used in the searchApi for returning its content
     *
     * @param rawResponse
     * @param moduleTitle
     * @return
     */
    public String getTeamModuleContentId(JsonNode rawResponse, String moduleTitle){
        return JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(contentIdQuery, moduleTitle)).toString();
    }

    /** Returns a list of the Arena's caption and venueID for other uses
     *
     * @param rawResponse
     * @return
     */
    public List<String> getArenaSdkText(JsonNode rawResponse){
        LinkedList<String> responseData = new LinkedList<>();
        JsonNode arenaData = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(arenaSdkTextQuery, "caption"));
        if(!"[]".equals(arenaData.toString())){
            responseData.add(arenaData.get(0).asText());
            arenaData = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                    .read(String.format(arenaSdkTextQuery, "venueID"));
            responseData.add(arenaData.get(0).asText());
        }
        return responseData;
    }

    /** Returns the address values of the Areana SDK venue
     *
     * @param rawResponse
     * @return
     */
    public String getArenaSdkVenueAddress(JsonNode rawResponse){
        JsonNode addressLine = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..address");
        String addressLine1 = addressLine.get(0).asText();
        addressLine = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..addressLineTwo");
        String addressLine2 = addressLine.get(0).asText();
        return addressLine1 + "\n" + addressLine2;
    }

    /** Returns the Arena SDK's internal 'More Menu' module headers
     *
     * @param rawResponse - THe arena response
     * @param type - The module type being tested
     * @return
     */
    public List<String> getArenaSdkMoreMenuHeaders(JsonNode rawResponse, String type){
        LinkedList<String> listMenus = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(arenaSdkListMenuHeadersQuery, type));
        if(!data.toString().isEmpty()){
            for(JsonNode node : data){
                listMenus.add(node.asText());
            }
        }
        return listMenus;
    }

    /** Returns menu contents. Can be used for either the grid or event items, as well as
     * returning the event time.
     *
     * @param rawResponse - Arena API data
     * @param menu - The menu item being checked
     * @param time - Boolean for whether to return the time or name
     * @return
     */
    public List<String> getArenaSdkMenuContents(JsonNode rawResponse, String menu, boolean time){
        LinkedList<String> menuItems = new LinkedList<>();
        String nodeType;
        if(time){
            nodeType = "eventTime";
        } else {
            nodeType = "displayName";
        }
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(arenaSdkMenuItemsQuery, menu, nodeType));
        if(!data.toString().isEmpty()){
            for(JsonNode node : data){
                menuItems.add(node.asText());
            }
        }
        return menuItems;
    }

    /** Returns all floor names contained in a given Arena's response
     *
     * @param rawResponse
     * @return
     */
    public List<String> getMapFloors(JsonNode rawResponse){
        LinkedList<String> venueFloors = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(arenaSdkVenueFloorsQuery));

        if(!data.toString().isEmpty()){
            for(JsonNode node : data){
                venueFloors.add(node.asText());
            }
        }
        return venueFloors;
    }

    /** Returns the default floor name the map should open to in the Arena SDK
     *
     * @param rawResponse
     * @return
     */
    public String getDefaultArenaFloor(JsonNode rawResponse){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(arenaSdkVenueDefaultFloorQuery);
        if(data.toString().equals("[]")){
            data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                    .read(arenaSdkVenueFloor1);
        }
        return data.toString().substring(2, data.toString().length() - 2);
    }

    /** Returns the venues listed within the Arena SDK search result.
     *
     * @param rawResponse - The search result query done with a specific keyword (eg. food)
     * @return
     */
    public List<String> getArenaSdkSearchResultVenues(JsonNode rawResponse){
        LinkedList<String> venues = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(arenaSdkSearchResultsVenuesQuery);
        for(JsonNode node : data){
            venues.add(node.asText());
        }
        return venues;
    }

    /** Returns a list of venue locations on a given floor of an arena which is used in text checks
     * of the search results
     *
     * @param rawResponse
     * @param venues
     * @return
     */
    public List<String> getArenaSdkSearchResultLocations(JsonNode rawResponse, List<String> venues){
        LinkedList<String> locations = new LinkedList<>();
        for(int i = 0; i < venues.size(); i++){
            JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                    .read(String.format(arenaSdkSearchResultsVenueLocationsQuery, i));
            locations.add(combineVenueInformation(data));
        }
        return locations;
    }

    /** Combines the values of a venue's location into 1 String to match app formatting
     *
     * @param node
     * @return
     */
    private String combineVenueInformation(JsonNode node) {
        String value = "";
        for(int j = 0; j < node.size(); j++ ){
            String currentValue = node.get(j).asText();
            if(!currentValue.isEmpty()) {
                value = value.concat(currentValue);
            }
            if(j == 0){
                value = value.concat(": ");
            }
        }
        return value;
    }

    /** Returns the floors where a search's results are located in a list
     *
     * @param rawResponse
     * @return
     */
    public List<String> getArenaSdkSearchResultFloors(JsonNode rawResponse){
        LinkedList<String> floors = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(arenaSdkSearchResultsFloorQuery);

        if(!data.toString().isEmpty()) {
            for (JsonNode node : data) {
                floors.add(node.asText());
            }
            return floors;
        } else
            throw new SkipException("ERROR: Floor Data is missing. Check the Point Inside API response for issues");
    }

    /** Returns a boolean for if the config has the What's New module enabled and the criteria for it's display
     * is applicable to the verison being tested
     *
     * @param rawResponse
     * @return
     */
    public boolean isWhatsNewEnabled(JsonNode rawResponse){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(configWhatsNewQuery);

        if(data.toString().isEmpty()){
            return false;
        } else {
            return !data.toString().isEmpty() && Integer.valueOf(data.toString()) > 0;
        }
    }

    /** Returns a boolean whether a specific menu item in the app is active based on the config listing
     *
     * @param rawResponse - App Config response
     * @param section - Menu item to see if present for active
     * @return
     */
    public boolean isMenuSectionActive(JsonNode rawResponse, String section){
        List<String> menuItems = getActiveMenuItems(rawResponse);

        return menuItems.contains(section.toLowerCase());
    }

    /** Returns the list of active menu items in the app config
     *
     * @param rawResponse
     * @return
     */
    public List<String> getActiveMenuItems(JsonNode rawResponse) {
        LinkedList<String> menuItems = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(configMoreMenuItemsQuery);

        if (!data.toString().isEmpty()) {
            for (JsonNode node : data) {
                menuItems.add(node.asText());
            }
            return menuItems;
        } else {
            throw new SkipException("ERROR: navigationMenuOrderableItems is empty. Check manually check config.");
        }
    }

    /** Returns a list of tabs explicitly listed as disabled in the app config for gamecenter
     *
     * @param rawResponse - AppConfig being checked
     * @return
     */
    public List<String> getDisabledGamecenterTabs(JsonNode rawResponse){
        LinkedList<String> disabledItems = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(configGameCenterTabs);

        if (!data.toString().isEmpty()) {
            for (JsonNode node : data) {
                disabledItems.add(node.toString().substring(1, node.toString().length()-1));
            }
        }
        return disabledItems;
    }

    /** Returns a boolean if 'players to watch' is active in the app config.
     * Differs from getDisabledGamecenterTabs in that players to watch is simply omitted if disabled.
     *
     * @param rawResponse
     * @return
     */
    public boolean isPlayersToWatchEnabled(JsonNode rawResponse){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(configPlayersToWatch);

        if(!data.asText().isEmpty()){
            return data.asBoolean();
        } else {
            return true;
        }
    }

    /** Returns a list of GamePk values from a passed Schedule (statsApi) response
     *
     * @param rawResponse
     * @return
     */
    public List<String> getScheduleGamePks(JsonNode rawResponse){
        LinkedList<String> gamePks = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(scheduleGamePks);

        if(!data.toString().isEmpty()){
            for(JsonNode node : data){
                gamePks.add(node.toString());
            }
            return gamePks;
        } else {
            throw new SkipException("ERROR: Could not find GamePKs for API validation. Manual verification required");
        }
    }

    /** Returns a list of a given game's team names in the matchup. Works in conjunction with getScheduleGamePks
     * when checking dynamic data.
     *
     * @param rawResponse - statsApi response
     * @param gamePk - GamePK being checked
     * @return
     */
    public List<String> getGameEventTeamNames(JsonNode rawResponse, String gamePk){
        LinkedList<String> teamData = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(scheduleHomeAwayTeamName, gamePk));

        if(!data.toString().isEmpty()){
            for(JsonNode node : data){
                teamData.add(node.toString().substring(1, node.toString().length()-1));
            }
            return teamData;
        } else {
            throw new SkipException("ERROR: Could not find Team Names for API validation. Manual verification required");
        }
    }

    /** Returns a list of a given game's team IDs in the matchup. Works in conjunction with getScheduleGamePks
     * when checking dynamic data.
     *
     * @param rawResponse - statsApi response
     * @param gamePk - GamePK being checked
     * @return
     */
    public List<String> getGameEventTeamIDs(JsonNode rawResponse, String gamePk) {
        LinkedList<String> teamData = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(scheduleHomeAwayTeamID, gamePk));

        if (!data.toString().isEmpty()) {
            for (JsonNode node : data) {
                teamData.add(node.toString());
            }
            return teamData;
        } else {
            throw new SkipException("ERROR: Could not find Team IDs for API validation. Manual verification required");
        }
    }

    /** Returns a gameCenter displays's "last five leaders" for checking that specific module in the display
     * from nhlstatic.com
     *
     * @param rawResponse - nhlstatic.com response data
     * @return
     */
    public List<String> getLastFiveGameLeaders(JsonNode rawResponse){
        LinkedList<String> players = new LinkedList<>();

        for(String type: LEADER_CATEGORIES) {
            JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                    .read(String.format(nhlstaticPlayerShortNames, type));

            if(!data.toString().substring(1, data.toString().length()-1).isEmpty()){
                players.add(data.get(0).toString().substring(1, data.get(0).toString().length()-1));
            } else {
                throw new SkipException("ERROR: Could not find player data for API validation. Manual verification required");
            }
        }

        return players;
    }

    /** Verifies the roster displayed in the gamecenter for a given team in a given game
     *
     * @param rawResponse - Schedule data used in the test returned by passing a gamePk
     * @param teamName - The team name of the roster you need
     * @return
     */
    public List<String> getTeamRosterPlayers(JsonNode rawResponse, String teamName){
        LinkedList<String> playerIds = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(rosterPlayerId, teamName));

        for(JsonNode node : data){
            playerIds.add(node.asText());
        }

        return playerIds;
    }

    /** Returns a String of a given player's specific stat attrubyte
     *
     * @param rawResponse - Schedule data returned from statsApi with a specific gamePk
     * @param playerId - The player ID being checked
     * @param attribute - The attribute being checked
     * @return
     */
    public String getPlayerAttribute(JsonNode rawResponse, String playerId, String attribute){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(rosterPlayerVariableData, playerId, attribute));

        String value = StringUtils.substringBetween(data.toString(), "[", "]");
        if(value.contains("\"")){
            return StringUtils.substringBetween(value, "\"", "\"");
        } else {
            return value;
        }
    }

    /** Returns a boolean if Tickets are enabled for a specific game on the statsApi schedule call
     *
     * @param rawResponse
     * @param gamePk
     * @return
     */
    public boolean isTicketsEnabledForGame(JsonNode rawResponse, String gamePk){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(scheduleGameTickets, gamePk));

        boolean ticketsEnabled = false;

        for(JsonNode node : data){
            if(node.toString().contains("leagueAppScoreTab")){
                ticketsEnabled = true;
            }
        }

        return ticketsEnabled;
    }

    /** Returns preview article metadata based on passed metadata String
     *
     * @param rawResponse - Schedule data
     * @param gamePk - GamePk being checked
     * @param metadata - Desired metadata value (headline, subhead, or date)
     * @return
     */
    public String getScoreboardPreviewArticleData(JsonNode rawResponse, String gamePk, String metadata){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(schedulePreviewMetadata, gamePk, metadata));

        String value = StringUtils.substringBetween(data.toString(), "[", "]");
        if(!value.isEmpty()){
            return StringUtils.substringBetween(value, "\"", "\"");
        } else {
            return value;
        }
    }

    /** Returns a list of stat values used in building an NHL Team object for a specific game
     *
     * @param rawResponse - Schedule data returned with a given GamePK
     * @param teamName - The team being built
     * @param stat - The stat value being added (see NhlTeamData class)
     * @return
     */
    public List<String> getTeamVariableData(JsonNode rawResponse, String teamName, String stat){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(teamVariableData, teamName, stat));

        LinkedList<String> dataValues = new LinkedList<>();

        for(JsonNode node : data){
            dataValues.add(node.asText());
        }

        return dataValues;
    }

    /** Returns teh color of the bar shown in the gamecetner matchup being tested
     *
     * @param rawResponse
     * @return
     */
    public List<String> getGamecenterMatchupColor(JsonNode rawResponse){
        LinkedList<String> hexValues = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$");

        for(JsonNode node : data){
            hexValues.add(node.asText());
        }
        return hexValues;
    }

    /*
     * Due to the way the Json is designed, identifying the corresponding lastTen stats
     * node requires matching index values to the division and team.
     * The data itself is designed with these aspects matching.
     *
     * Please note that standings data is not discoverable using team parameters, such as name or ID. See API below for reference
     * http://statsapi.web.nhl.com/api/v1/standings?leagueId=133&standingsType=regularSeason&expand=standings.team,standings.record.overall
     */
    public List<String> getLastTenStats(JsonNode rawResponse, String team){
        LinkedList<String> dataList = new LinkedList<>();
        int divIndex;
        int teamIndex = 0;

        //Cycles through each division, populating the teams that are present in each records node.
        for(divIndex = 0; divIndex < 4; divIndex++){
            boolean foundTeam = false;
            JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                    .read(String.format(teamNamesByDivision, divIndex));

            for(JsonNode node : data){
                dataList.add(node.asText());
            }

            /*
             * Cycles through each team in the records node for a match to what we're looking for.
             * The loop breaks when the desired team is found, saving the record and team placement
             * index values for the lastTen node query.
             */
            for(teamIndex = 0; teamIndex < dataList.size(); teamIndex++){
                if(dataList.get(teamIndex).equals(team)){
                    dataList.clear();
                    foundTeam = true;
                    break;
                }
            }
            if(foundTeam){
                break;
            }
            dataList.clear();
        }

        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(teamLastTenStat, divIndex, teamIndex));

        for(JsonNode node : data.get(0)){
            dataList.add(node.asText());
        }

        if(!dataList.isEmpty()){
            return dataList;
        } else {
            throw new SkipException("ERROR: STAT VALUES COULD NOT BE LOCATED. CHECK API PATHS AND QUERIES");
        }
    }

    /** Returns the broadcasts for a given game
     *
     * @param rawResponse - statsApi schedule data
     * @param gamePk - The gamePk being tested
     * @return
     */
    public String getGameBroadcasts(JsonNode rawResponse, String gamePk){
        LinkedList<String> broadcasts = new LinkedList<>();

        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(scheduleGameBroadcasters, gamePk));

        for(JsonNode node : data){
            broadcasts.add(node.asText());
        }

        return StringUtils.substringBetween(broadcasts.toString(), "[", "]");
    }

    /** Returns the venue of the given game
     *
     * @param rawResponse
     * @param gamePk
     * @return
     */
    public String getGameVenue(JsonNode rawResponse, String gamePk){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(scheduleGameVenue, gamePk));

        return data.get(0).asText();
    }

    /** Returns the Game Label text of a given game (Free Game, various other texts)
     *
     * @param rawResponse
     * @param gamePk
     * @return
     */
    public String getGameLabel(JsonNode rawResponse, String gamePk){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(scheduleGameLabel, gamePk));

        return data.asText();
    }

    /** Returns the Game Date text of a given game for formatting display tests
     *
     * @param rawResponse
     * @param gamePk
     * @return
     */
    public String getGameDate(JsonNode rawResponse, String gamePk){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(scheduleGameDate, gamePk));

        return data.get(0).asText();
    }

    /** Returns a boolean if the GameInfo module is available in a given game's data
     *
     * @param rawResponse
     * @return
     */
    public boolean isGameInfoAvailable(JsonNode rawResponse){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(coachesQuery, "home"));

        return data.asText().isEmpty();
    }

    /** Returns a String of a given game's coach data depending on the homeAway boolean
     *
     * @param rawResponse - A game's data
     * @param homeAway - 'home' or 'away'
     * @return
     */
    public String getCoachInfo(JsonNode rawResponse, String homeAway){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(coachesQuery, homeAway));

        return data.get(0).asText();
    }

    /** Returns a list of the officials of a given game based on the type passed
     *
     * @param rawResponse
     * @param type - 'Referee' or 'Linesman'
     * @return
     */
    public List<String> getOfficialsInfo(JsonNode rawResponse, String type){
        List<String> officials = new ArrayList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read(String.format(officialsQuery, type));

        if(!data.toString().isEmpty()) {
            for (JsonNode node : data) {
                officials.add(node.asText());
            }
        }
        return officials;
    }

    /** Returns a list of dates where games were played for checking archived feeds.
     *
     * @param rawResponse - A schedule response with date parameters passed to set a range.
     * @return
     */
    public List<String> getDatesWithGamesPlayed(JsonNode rawResponse){
        LinkedList<String> dates = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..date");

        if(!data.toString().isEmpty()){
            for(JsonNode node : data){
                dates.add(node.asText());
            }
        }
        return dates;
    }

    /** Returns a game's away team feed title. Finds the first one found in the schedule response passed
     *
     * @param rawResponse - A single day's statsApi response
     * @return
     */
    public String getAwayTeamWithEntitledFeed(JsonNode rawResponse){
        LinkedList<String> gamePks = new LinkedList<>();
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..gamePk");
        for(JsonNode node : data){
            gamePks.add(node.asText());
        }

        for(String game : gamePks){
            data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                    .read(String.format("$..[?(@.gamePk=='%s')].[?(@.title=='NHLTV')]..freeGame", game));

            List<Boolean> feeds = new LinkedList<>();

            for(JsonNode node : data){
                feeds.add(node.asBoolean());
            }

            if(!feeds.contains(true)){
                data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                        .read(String.format("$..[?(@.gamePk=='%s')]..away.team.teamName", game));
                return data.get(0).asText();
            }
        }
        return "";
    }

    /** Returns if a given date has entitled archives
     *
     * @param date
     * @return
     */
    public boolean doesDateHaveEntitledFeeds(String date){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(getScheduleData(date))
                .read("$..[?(@.title=='NHLTV')]..freeGame");

        List<Boolean> feeds = new LinkedList<>();

        for(JsonNode node : data){
            feeds.add(node.asBoolean());
        }
        return !feeds.contains(true);
    }

    /** Returns the current season's end date for forward navigation limits on Preview tests
     *
     * @param rawResponse - App config response
     * @return
     */
    public String getSeasonEndDate(JsonNode rawResponse){
        JsonNode data = JsonPath.using(jsonPathJacksonConfiguration).parse(rawResponse)
                .read("$..ranges..end");
        return data.get(data.size()-1).asText();
    }

    /**
     * Verifies if a passed Gamecenter module is enabled in the core app config
     * @param rawResponse - The core app config response
     * @param module - The gamecenter module being checked
     * @return - boolean value of the module listing (true as default, parse if present)
     */
    public boolean checkGamecenterModuleEnabled(JsonNode rawResponse, String module){
        String response = JsonPath.using(jsonPathJacksonConfiguration)
                .parse(rawResponse)
                .read(String.format(gamecenterModuleEnabled, module))
                .toString()
                .replace("[", "")
                .replace("]", "");

        if(response.isEmpty()){
            return true;
        } else {
            return Boolean.parseBoolean(response);
        }
    }
}
