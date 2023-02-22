package com.disney.qa.api.nhl;

import com.disney.qa.api.nhl.pojo.Leader;
import com.disney.qa.api.nhl.pojo.Leaders;
import com.disney.qa.api.nhl.pojo.LeagueLeader;
import com.disney.qa.api.nhl.pojo.MetaData;
import com.disney.qa.api.nhl.pojo.Person;
import com.disney.qa.api.nhl.pojo.Schedule;
import com.disney.qa.api.nhl.pojo.Team;
import com.disney.qa.api.nhl.pojo.stats.schedule.Date;
import com.disney.qa.api.nhl.pojo.stats.schedule.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Class for converting JSON POJO classes to NHL domain objects
 */
public class NhlObjectConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Schedule toSchedule(Object schedule) {
        if (schedule instanceof com.disney.qa.api.nhl.pojo.stats.schedule.Schedule) {
            return toScheduleObjectFromStatsObject((com.disney.qa.api.nhl.pojo.stats.schedule.Schedule) schedule);
        } else if (schedule instanceof com.disney.qa.api.nhl.pojo.feed.Schedule) {
            return toScheduleObjectFromFeedObject((com.disney.qa.api.nhl.pojo.feed.Schedule) schedule);
        }
        throw new RuntimeException(String.format("Unsupported type of object %s", schedule.getClass().getName()));
    }

    public Leaders toLeaders(Object leaders) {
        if (leaders instanceof com.disney.qa.api.nhl.pojo.stats.leaders.Leaders) {
            return toLeadersObjectFromStatsObject((com.disney.qa.api.nhl.pojo.stats.leaders.Leaders) leaders);
        } else if (leaders instanceof com.disney.qa.api.nhl.pojo.feed.leaders.Leaders) {
            return toLeadersObjectFromFeedObject((com.disney.qa.api.nhl.pojo.feed.leaders.Leaders) leaders);
        }
        throw new RuntimeException(String.format("Unsupported type of object %s", leaders.getClass().getName()));
    }

    protected Schedule toScheduleObjectFromStatsObject(com.disney.qa.api.nhl.pojo.stats.schedule.Schedule statsSchedule) {
        Schedule schedule = new Schedule();

        for (Date date : statsSchedule.getDates()) {

            for (Game statsGame : date.getGames()) {

                com.disney.qa.api.nhl.pojo.Game game = new com.disney.qa.api.nhl.pojo.Game();
                game.setGameId(statsGame.getGamePk());
                game.setDate(statsGame.getGameDate());

                if (statsGame.getTeams() != null) {
                    Team homeTeam = new Team();
                    homeTeam.setId(statsGame.getTeams().getHome().getTeam().getId());
                    homeTeam.setName(statsGame.getTeams().getHome().getTeam().getName());
                    homeTeam.setScore(statsGame.getTeams().getHome().getScore());
                    homeTeam.setWins(statsGame.getTeams().getHome().getLeagueRecord().getWins());
                    homeTeam.setLosses(statsGame.getTeams().getHome().getLeagueRecord().getLosses());
                    homeTeam.setOt(statsGame.getTeams().getHome().getLeagueRecord().getOt());

                    Team awayTeam = new Team();
                    awayTeam.setId(statsGame.getTeams().getAway().getTeam().getId());
                    awayTeam.setName(statsGame.getTeams().getAway().getTeam().getName());
                    awayTeam.setScore(statsGame.getTeams().getAway().getScore());
                    awayTeam.setWins(statsGame.getTeams().getAway().getLeagueRecord().getWins());
                    awayTeam.setLosses(statsGame.getTeams().getAway().getLeagueRecord().getLosses());
                    awayTeam.setOt(statsGame.getTeams().getAway().getLeagueRecord().getOt());

                    game.setHomeTeam(homeTeam);
                    game.setAwayTeam(awayTeam);
                }

                if (null != statsGame.getAdditionalProperties().get("metadata")) {
                    MetaData metaData = new MetaData();

                    metaData.setIsManuallyScored(Boolean.valueOf(
                            ((Map) statsGame.getAdditionalProperties().get("metadata")).get("isManuallyScored").toString()));
                    metaData.setIsSplitSquad(Boolean.valueOf(
                            ((Map) statsGame.getAdditionalProperties().get("metadata")).get("isSplitSquad").toString()));

                    game.setMetaData(metaData);
                }

                schedule.getGameList().add(game);
            }
        }

        return schedule;
    }

    protected Schedule toScheduleObjectFromFeedObject(com.disney.qa.api.nhl.pojo.feed.Schedule feedSchedule) {
        Schedule schedule = new Schedule();

        for (com.disney.qa.api.nhl.pojo.feed.Game feedGame : feedSchedule.getGames()) {
            Team homeTeam = new Team();
            homeTeam.setId(feedGame.getHomeTeam().getId());
            homeTeam.setName(feedGame.getHomeTeam().getName());
            homeTeam.setScore(feedGame.getHomeTeam().getT());
            if (null != feedGame.getHomeTeam().getRecord()) {
                homeTeam.setWins(Integer.valueOf(feedGame.getHomeTeam().getRecord().split("-")[0]));
                homeTeam.setLosses(Integer.valueOf(feedGame.getHomeTeam().getRecord().split("-")[1]));
                try {
                    homeTeam.setOt(Integer.valueOf(feedGame.getHomeTeam().getRecord().split("-")[2]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    LOGGER.warn(e.getMessage(), e);
                }
            }

            Team awayTeam = new Team();
            awayTeam.setId(feedGame.getAwayTeam().getId());
            awayTeam.setName(feedGame.getAwayTeam().getName());
            awayTeam.setScore(feedGame.getAwayTeam().getT());
            if (null != feedGame.getHomeTeam().getRecord()) {
                awayTeam.setWins(Integer.valueOf(feedGame.getAwayTeam().getRecord().split("-")[0]));
                awayTeam.setLosses(Integer.valueOf(feedGame.getAwayTeam().getRecord().split("-")[1]));
                try {
                    awayTeam.setOt(Integer.valueOf(feedGame.getAwayTeam().getRecord().split("-")[2]));
                } catch (ArrayIndexOutOfBoundsException e) {
                    LOGGER.warn(e.getMessage(), e);
                }
            }   


            com.disney.qa.api.nhl.pojo.Game game = new com.disney.qa.api.nhl.pojo.Game();
            game.setGameId(feedGame.getGameId());
            game.setHomeTeam(homeTeam);
            game.setAwayTeam(awayTeam);

            schedule.getGameList().add(game);
        }

        return schedule;
    }

    protected Leaders toLeadersObjectFromStatsObject(com.disney.qa.api.nhl.pojo.stats.leaders.Leaders statsLeaders) {
        Leaders leaders = new Leaders();

        for (com.disney.qa.api.nhl.pojo.stats.leaders.LeagueLeader statsLeagueLeader : statsLeaders.getLeagueLeaders()) {
            LeagueLeader leagueLeader = new LeagueLeader();

            leagueLeader.setLeaderCategory(statsLeagueLeader.getLeaderCategory());

            for (com.disney.qa.api.nhl.pojo.stats.leaders.Leader statsLeader : statsLeagueLeader.getLeaders()) {
                Leader leader = new Leader();

                leader.setRank(statsLeader.getRank());
                leader.setValue(statsLeader.getValue());
                leader.setTeam(new Team(statsLeader.getTeam().getId(), statsLeader.getTeam().getName()));
                leader.setPerson(new Person(statsLeader.getPerson().getId(), statsLeader.getPerson().getFullName()));

                leagueLeader.getLeaders().add(leader);
            }

            leaders.getLeagueLeaders().add(leagueLeader);
        }

        Collections.sort(leaders.getLeagueLeaders());

        return leaders;
    }

    protected Leaders toLeadersObjectFromFeedObject(com.disney.qa.api.nhl.pojo.feed.leaders.Leaders feedLeaders) {
        Leaders leaders = new Leaders();

        LeagueLeader pointsLeagueLeader = toLeagueLeaderFromFeedObject(feedLeaders.getOffense().getPoints(), "points");
        LeagueLeader goalsLeagueLeader = toLeagueLeaderFromFeedObject(feedLeaders.getOffense().getGoals(), "goals");
        LeagueLeader assistsLeagueLeader = toLeagueLeaderFromFeedObject(feedLeaders.getOffense().getAssists(), "assists");
        LeagueLeader gaaLeagueLeader = toLeagueLeaderFromFeedObject(feedLeaders.getGoaltending().getGaa(), "gaa");

        leaders.getLeagueLeaders().add(pointsLeagueLeader);
        leaders.getLeagueLeaders().add(goalsLeagueLeader);
        leaders.getLeagueLeaders().add(assistsLeagueLeader);
        leaders.getLeagueLeaders().add(gaaLeagueLeader);

        Collections.sort(leaders.getLeagueLeaders());

        return leaders;
    }

    protected LeagueLeader toLeagueLeaderFromFeedObject(List<com.disney.qa.api.nhl.pojo.feed.leaders.Leader> feedLeaders, String leaderCategory) {
        LeagueLeader leagueLeader = new LeagueLeader();
        leagueLeader.setLeaderCategory(leaderCategory);

        for (com.disney.qa.api.nhl.pojo.feed.leaders.Leader feedLeader : feedLeaders) {

            Leader leader = new Leader();

            // TODO refactor assigning rank value - it's possible that rank is the same if 'values' are the same
            // leader.setRank(feedLeaders.indexOf(feedLeader) + 1);
            leader.setValue(feedLeader.getStat());
            leader.setTeam(new Team(feedLeader.getTeamId(), feedLeader.getTeamName()));
            leader.setPerson(new Person(feedLeader.getPid(), feedLeader.getName()));

            leagueLeader.getLeaders().add(leader);
        }

        return leagueLeader;
    }
}
