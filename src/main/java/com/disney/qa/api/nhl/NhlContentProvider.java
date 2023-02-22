package com.disney.qa.api.nhl;

import com.disney.qa.api.nhl.pojo.Schedule;

import java.util.Map;

/**
 * TODO [normal] remove specific methods, leave only with map
 */
public interface NhlContentProvider {

    Schedule getSchedule();

    Schedule getSchedule(String startDate, String endDate);

    Schedule getSchedule(Integer teamId, String startDate, String endDate);

    Schedule getSchedule(Map<String, String> parameters);

    <T> T getSchedule(Map<String, String> parameters, Class<T> returnType);

    <T> T getSchedule(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType);

    <T> T getSchedule(Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getStandings(Map<String, String> parameters, Class<T> returnType);

    <T> T getStandings(Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getTeams(Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getTeams(Map<String, String> parameters, Class<T> returnType);

    <T> T getTeams(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType);

    <T> T getLeaders(Map<String, String> parameters, Class<T> returnType);

    <T> T getLeaders(Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getPeople(Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getPeople(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getDivisions(Map<String, String> parameters, Class<T> returnType);

    <T> T getConferences(Map<String, String> parameters, Class<T> returnType);

    <T> T getSeasons(Map<String, String> parameters, Class<T> returnType);

    <T> T getSeasons(Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getGames(Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getGames(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getBoxScore(Map<String, String> parameters, Class<T> returnType);

    <T> T getTournaments(Map<String, String> parameters, Class<T> returnType, String... pathSegments);
    
    <T> T getAwards(Map<String, String> parameters, Class<T> returnType);

    <T> T getAwards(Map<String, String> parameters, Class<T> returnType, String... pathSegments);
    
    <T> T getStats(Map<String, String> parameters, Class<T> returnType, String... pathSegments);
    
    <T> T getConfigurations(Map<String, String> parameters, Class<T> returnType);

    <T> T getRosterStatuses(Map<String, String> parameters, Class<T> returnType);

    <T> T getRosterStatuses(Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getPlatforms(Map<String, String> parameters, Class<T> returnType);

    <T> T getPlatforms(Map<String, String> parameters, Class<T> returnType, String... pathSegments);
    
    <T> T getDraft(Map<String, String> parameters, Class<T> returnType);

    <T> T getDraft(Map<String, String> parameters, Class<T> returnType, String... pathSegments);

    <T> T getProvidedEndpoint(Map<String, String> parameters, Class<T> returnType);

    <T> T getProvidedEndpoint(Map<String, String> parameters, Class<T> returnType, String... pathSegments);
}
