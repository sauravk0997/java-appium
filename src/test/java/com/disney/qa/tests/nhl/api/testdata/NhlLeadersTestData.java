package com.disney.qa.tests.nhl.api.testdata;

import org.testng.annotations.DataProvider;

/**
 * Created by mk on 2/19/17.
 */
public class NhlLeadersTestData {

    @DataProvider(name = "allTimeStatsLeadersRequests")
    public static Object[][] allTimeStatsLeadersRequestsDataProvider() {
        return new Object[][]{
                // 1.1 api/v1/stats/leaders depth
                {"TUID: depth_260", "/api/v1/stats/leaders?leaderCategories=assists&depth=allTimeCareer",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Wayne Gretzky", "260"},
                {"TUID: depth_31", "/api/v1/stats/leaders?leaderCategories=assists&depth=allTimeSeason",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Wayne Gretzky", "31"},
                {"TUID: depth_60", "/api/v1/stats/leaders?leaderCategories=assists&depth=singleSeason&season=20142015",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Nicklas Backstrom", "60"},

                // 1.2 /api/v1/teams/{id}/leaders depth
                {"TUID: depth_104", "/api/v1/teams/21/leaders?leaderCategories=assists&depth=allTimeCareer",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Joe Sakic", "104"},
                {"TUID: depth_18", "/api/v1/teams/21/leaders?leaderCategories=assists&depth=allTimeSeason",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Peter Forsberg", "18"},
                {"TUID: depth_41", "/api/v1/teams/21/leaders?leaderCategories=assists&depth=singleSeason&season=20142015",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Tyson Barrie", "41"},

                // 1.3 /api/v1/teams depth with hydrate
                {"TUID: depth_80", "/api/v1/teams?hydrate=leaders(categories=assists,depth=allTimeCareer)",
                        "$.teams[0]..leaders[0]..fullName", "$.teams[0]..leaders[0].value",
                        "Patrik Elias", "80"},
                {"TUID: depth_16", "/api/v1/teams?hydrate=leaders(categories=assists,depth=allTimeSeason)",
                        "$.teams[0]..leaders[0]..fullName", "$.teams[0]..leaders[0].value",
                        "Doug Gilmour", "16"},
                {"TUID: depth_27", "/api/v1/teams?hydrate=leaders(categories=assists,depth=singleSeason,season=20142015)",
                        "$.teams[0]..leaders[0]..fullName", "$.teams[0]..leaders[0].value",
                        "Adam Henrique", "27"},

                // 2.1 /api/v1/stats/leaders playerStatus
                {"TUID: playerStatus_wg_123", "/api/v1/stats/leaders?leaderCategories=assists&playerStatus=allPlayers",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Wayne Gretzky", "123"},
                {"TUID: playerStatus_jt_120", "/api/v1/stats/leaders?leaderCategories=assists&playerStatus=activePlayers",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Sidney Crosby", "120"},

                // 2.2 /api/v1/teams/{id}/leaders playerStatus
                {"TUID: playerStatus_104", "/api/v1/teams/21/leaders?leaderCategories=assists&playerStatus=allPlayers",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Joe Sakic", "104"},
                {"TUID: playerStatus_10", "/api/v1/teams/21/leaders?leaderCategories=assists&playerStatus=activePlayers",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Nathan MacKinnon", "10"},

                // 2.3 /api/v1/teams playerStatus with hydrate
                {"TUID: playerStatus_80", "/api/v1/teams?hydrate=leaders(categories=assists,playerStatus=allPlayers)",
                        "$.teams[0]..leaders[0]..fullName", "$.teams[0]..leaders[0].value",
                        "Patrik Elias", "80"},
                {"TUID: playerStatus_22", "/api/v1/teams?hydrate=leaders(categories=assists,playerStatus=activePlayers)",
                        "$.teams[0]..leaders[0]..fullName", "$.teams[0]..leaders[0].value",
                        "Zach Parise", "22"},

                // 3 /api/v1/stats/leaders teamId
                // depth=singleSeason is default value, so it's not specified in first response explicitly  
                {"TUID: teamId_39", "/api/v1/stats/leaders?leaderCategories=assists&teamId=21&season=20152016",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Carl Soderberg", "39"},
                {"TUID: teamId_104", "/api/v1/stats/leaders?leaderCategories=assists&depth=allTimeCareer&teamId=21",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Joe Sakic", "104"},
                {"TUID: teamId_18", "/api/v1/stats/leaders?leaderCategories=assists&depth=allTimeSeason&teamId=21",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Peter Forsberg", "18"},

                // 4.1 /api/v1/stats/leaders leaderGameType
                // TODO single season? Possible issues: http://qa-statsapi.web.nhl.com/api/v1/stats/leaders?leaderCategories=assists&leaderGameTypes=P
                {"TUID: leaderGameType_260", "/api/v1/stats/leaders?leaderCategories=assists&depth=allTimeCareer&leaderGameTypes=P",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Wayne Gretzky", "260"},
                {"TUID: leaderGameType_1963", "/api/v1/stats/leaders?leaderCategories=assists&depth=allTimeCareer&leaderGameTypes=R",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Wayne Gretzky", "1963"},
                {"TUID: leaderGameType_31", "/api/v1/stats/leaders?leaderCategories=assists&depth=allTimeSeason&leaderGameTypes=P",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Wayne Gretzky", "31"},

                // 4.2 /api/v1/teams/{id}/leaders leaderGameType
                {"TUID: leaderGameType_P_104", "/api/v1/teams/21/leaders?leaderCategories=assists&depth=allTimeCareer&leaderGameTypes=P",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Joe Sakic", "104"},
                {"TUID: leaderGameType_R_104", "/api/v1/teams/21/leaders?leaderCategories=assists&depth=allTimeCareer&leaderGameTypes=R",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Joe Sakic", "104"},
                {"TUID: leaderGameType_18", "/api/v1/teams/21/leaders?leaderCategories=assists&depth=allTimeSeason&leaderGameTypes=P",
                        "$..leaders[0]..fullName", "$..leaders[0].value",
                        "Cale Makar", "18"},

                // 4.3 /api/v1/teams gameTypes with hydrate
                {"TUID: gameTypes_P_80", "/api/v1/teams?hydrate=leaders(categories=assists,depth=allTimeCareer,gameTypes=P)",
                        "$.teams[0]..leaders[0]..fullName", "$.teams[0]..leaders[0].value",
                        "Patrik Elias", "80"},
                {"TUID: gameTypes_R_80", "/api/v1/teams?hydrate=leaders(categories=assists,depth=allTimeCareer,gameTypes=R)",
                        "$.teams[0]..leaders[0]..fullName", "$.teams[0]..leaders[0].value",
                        "Patrik Elias", "80"},
                {"TUID: gameTypes_16", "/api/v1/teams?hydrate=leaders(categories=assists,depth=allTimeSeason,gameTypes=P)",
                        "$.teams[0]..leaders[0]..fullName", "$.teams[0]..leaders[0].value",
                        "Scott Niedermayer", "16"}
        };
    }
}
