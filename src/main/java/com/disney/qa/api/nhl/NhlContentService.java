package com.disney.qa.api.nhl;

import com.disney.qa.api.nhl.pojo.Schedule;

import java.util.Map;

/**
 * Created by mk on 11/1/15.
 */
public class NhlContentService {

    protected NhlContentProvider nhlContentProvider;

    public NhlContentProvider getNhlContentProvider() {
        return nhlContentProvider;
    }

    public void setNhlContentProvider(NhlContentProvider nhlContentProvider) {
        this.nhlContentProvider = nhlContentProvider;
    }

    public Schedule getSchedule() {
        return nhlContentProvider.getSchedule();
    }

    public Schedule getSchedule(String startDate, String endDate) {
        return nhlContentProvider.getSchedule(startDate, endDate);
    }

    public Schedule getSchedule(Integer teamId, String startDate, String endDate) {
        return nhlContentProvider.getSchedule(teamId, startDate, endDate);
    }

    public Schedule getSchedule(Map<String, String> parameters) {
        return nhlContentProvider.getSchedule(parameters);
    }

    public <T> T getSchedule(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getSchedule(parameters, returnType);
    }

    public <T> T getSchedule(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getSchedule(parameters, returnType, pathSegments);
    }

    public <T> T getSchedule(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getSchedule(headers, parameters, returnType);
    }

    public <T> T getStandings(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getStandings(parameters, returnType);
    }

    public <T> T getStandings(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getStandings(parameters, returnType, pathSegments);
    }

    public <T> T getTeams(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getTeams(parameters, returnType);
    }

    public <T> T getTeams(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getTeams(headers, parameters, returnType);
    }

    public <T> T getTeams(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getTeams(parameters, returnType, pathSegments);
    }

    public <T> T getLeaders(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getLeaders(parameters, returnType);
    }

    public <T> T getLeaders(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getLeaders(parameters, returnType, pathSegments);
    }

    public <T> T getPeople(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getPeople(parameters, returnType, pathSegments);
    }

    public <T> T getPeople(Map<String, String> header, Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getPeople(header, parameters, returnType, pathSegments);
    }

    public <T> T getDivisions(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getDivisions(parameters, returnType);
    }

    public <T> T getConferences(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getConferences(parameters, returnType);
    }

    public <T> T getSeasons(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getSeasons(parameters, returnType);
    }

    public <T> T getSeasons(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getSeasons(parameters, returnType, pathSegments);
    }


    public <T> T getGames(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getGames(parameters, returnType, pathSegments);
    }

    public <T> T getGames(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getGames(headers, parameters, returnType, pathSegments);
    }

    public <T> T getBoxScore(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getBoxScore(parameters, returnType);
    }

    public <T> T getTournaments(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getTournaments(parameters, returnType, pathSegments);
    }

    public <T> T getAwards(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getAwards(parameters, returnType);
    }

    public <T> T getAwards(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getAwards(parameters, returnType, pathSegments);
    }
    
    public <T> T getStats(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getStats(parameters, returnType, pathSegments);
    }
    
    public <T> T getConfigurations(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getConfigurations(parameters, returnType);
    }

    public <T> T getRosterStatuses(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getRosterStatuses(parameters, returnType);
    }

    public <T> T getRosterStatuses(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getRosterStatuses(parameters, returnType, pathSegments);
    }

    public <T> T getPlatforms(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getPlatforms(parameters, returnType);
    }

    public <T> T getPlatforms(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getPlatforms(parameters, returnType, pathSegments);
    }
    
    public <T> T getDraft(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getDraft(parameters, returnType);
    }

    public <T> T getDraft(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getDraft(parameters, returnType, pathSegments);
    }

    public <T> T getProvidedEndpoint(Map<String, String> parameters, Class<T> returnType) {
        return nhlContentProvider.getProvidedEndpoint(parameters, returnType);
    }

    public <T> T getProvidedEndpoint(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return nhlContentProvider.getProvidedEndpoint(parameters, returnType, pathSegments);
    }
}
