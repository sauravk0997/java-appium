package com.disney.qa.api.nhl.statsapi;

import com.disney.qa.api.ApiContentProvider;
import com.disney.qa.api.nhl.NhlContentProvider;
import com.disney.qa.api.nhl.NhlObjectConverter;
import com.disney.qa.api.nhl.pojo.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.util.Map;

/**
 * Created by mk on 11/3/15.
 * <p/>
 * TODO [minor] combine getSchedule and getStandings generics methods
 */
public class NhlStatsApiContentProvider extends ApiContentProvider implements NhlContentProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String NHL_STATS_API_SCHEDULE_METHOD = "api/v1/schedule";

    public static final String NHL_STATS_API_STANDINGS_METHOD = "/api/v1/standings";

    public static final String NHL_STATS_API_TEAMS_METHOD = "/api/v1/teams";

    public static final String NHL_STATS_API_LEADERS_METHOD = "api/v1/stats/leaders";

    public static final String NHL_STATS_API_PEOPLE_METHOD = "api/v1/people";

    public static final String NHL_STATS_API_DIVISION_METHOD = "api/v1/divisions";

    public static final String NHL_STATS_API_CONFERENCES_METHOD = "api/v1/conferences";

    public static final String NHL_STATS_API_SEASONS_METHOD = "api/v1/seasons";

    public static final String NHL_STATS_API_GAMES_METHOD = "api/v1/game";

    public static final String NHL_STATS_API_TOURNAMENTS_METHOD = "api/v1/tournaments";

    public static final String NHL_STATS_API_AWARDS_METHOD = "api/v1/awards";
    
    public static final String NHL_STATS_API_STATS_METHOD = "api/v1/stats";
    
    public static final String NHL_STATS_API_CONFIGURATIONS_METHOD = "api/v1/configurations";

    public static final String NHL_STATS_API_ROSTER_STATUSES_METHOD = "api/v1/rosterStatuses";

    public static final String NHL_STATS_API_PLATFORMS_METHOD = "api/v1/platforms";
    
    public static final String NHL_STATS_API_DRAFT_METHOD = "api/v1/draft";

    public static final String NHL_STATS_API_ENDPOINT_METHOD = "api/v1/";

    protected String nhlStatsApiHost;

    protected RestTemplate restTemplate;

    public String getNhlStatsApiHost() {
        return nhlStatsApiHost;
    }

    public void setNhlStatsApiHost(String nhlStatsApiHost) {
        this.nhlStatsApiHost = nhlStatsApiHost;
    }

    @Override
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getHost() {
        return getNhlStatsApiHost();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    public Schedule getSchedule() {
        URI uri = UriComponentsBuilder.fromHttpUrl(nhlStatsApiHost).path(NHL_STATS_API_SCHEDULE_METHOD).build().toUri();

        RequestEntity<?> readRequestEntity = new RequestEntity(HttpMethod.GET, uri);

        LOGGER.info(String.format("Requesting Stats API Schedule method: %s", uri));

        com.disney.qa.api.nhl.pojo.stats.schedule.Schedule statsSchedule =
                restTemplate.exchange(readRequestEntity, com.disney.qa.api.nhl.pojo.stats.schedule.Schedule.class).getBody();

        return new NhlObjectConverter().toSchedule(statsSchedule);
    }

    @Override
    public Schedule getSchedule(String startDate, String endDate) {
        throw new RuntimeException("Method not implemented yet");
    }

    @Override
    public Schedule getSchedule(Integer teamId, String startDate, String endDate) {
        MultiValueMap queryParams = new LinkedMultiValueMap();
        queryParams.add("teamId", teamId);
        queryParams.add("startDate", startDate);
        queryParams.add("endDate", endDate);

        URI uri = UriComponentsBuilder.fromHttpUrl(nhlStatsApiHost).path(NHL_STATS_API_SCHEDULE_METHOD).
                queryParams(queryParams).build().toUri();

        RequestEntity<?> readRequestEntity = new RequestEntity(HttpMethod.GET, uri);

        LOGGER.info(String.format("Requesting Stats API Schedule method: %s", uri));

        com.disney.qa.api.nhl.pojo.stats.schedule.Schedule statsSchedule =
                restTemplate.exchange(readRequestEntity, com.disney.qa.api.nhl.pojo.stats.schedule.Schedule.class).getBody();

        return new NhlObjectConverter().toSchedule(statsSchedule);
    }

    @Override
    public Schedule getSchedule(Map<String, String> parameters) {
        com.disney.qa.api.nhl.pojo.stats.schedule.Schedule statsSchedule =
                getSchedule(parameters, com.disney.qa.api.nhl.pojo.stats.schedule.Schedule.class);

        return new NhlObjectConverter().toSchedule(statsSchedule);
    }

    @Override
    public <T> T getSchedule(Map<String, String> parameters, Class<T> returnType) {
        return getSchedule(parameters, returnType, null);
    }

    @Override
    public <T> T getSchedule(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType) {
        return getEndPoint(NHL_STATS_API_SCHEDULE_METHOD, headers, parameters, returnType, null);
    }

    @Override
    public <T> T getSchedule(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_SCHEDULE_METHOD, null, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getStandings(Map<String, String> parameters, Class<T> returnType) {
        return getStandings(parameters, returnType, null);
    }

    @Override
    public <T> T getStandings(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_STANDINGS_METHOD, null, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getTeams(Map<String, String> parameters, Class<T> returnType) {
        return getTeams(parameters, returnType, null);
    }

    @Override
    public <T> T getTeams(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType) {
        return getEndPoint(NHL_STATS_API_TEAMS_METHOD, headers, parameters, returnType, null);
    }

    @Override
    public <T> T getTeams(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_TEAMS_METHOD, null, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getLeaders(Map<String, String> parameters, Class<T> returnType) {
        return getLeaders(parameters, returnType, null);
    }

    @Override
    public <T> T getLeaders(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_LEADERS_METHOD, null, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getPeople(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_PEOPLE_METHOD, null, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getPeople(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_PEOPLE_METHOD, headers, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getDivisions(Map<String, String> parameters, Class<T> returnType) {
        return getEndPoint(NHL_STATS_API_DIVISION_METHOD, null, parameters, returnType, null);
    }

    @Override
    public <T> T getConferences(Map<String, String> parameters, Class<T> returnType) {
        return getEndPoint(NHL_STATS_API_CONFERENCES_METHOD, null, parameters, returnType, null);
    }

    @Override
    public <T> T getSeasons(Map<String, String> parameters, Class<T> returnType) {
        return getEndPoint(NHL_STATS_API_SEASONS_METHOD, null, parameters, returnType);
    }

    @Override
    public <T> T getSeasons(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_SEASONS_METHOD, null, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getGames(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_GAMES_METHOD, null, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getGames(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_GAMES_METHOD, headers, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getBoxScore(Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getTournaments(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_TOURNAMENTS_METHOD, null, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getAwards(Map<String, String> parameters, Class<T> returnType) {
        return getEndPoint(NHL_STATS_API_AWARDS_METHOD, null, parameters, returnType, null);
    }

    @Override
    public <T> T getAwards(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_AWARDS_METHOD, null, parameters, returnType, pathSegments);
    }

	@Override
	public <T> T getStats(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
		return getEndPoint(NHL_STATS_API_STATS_METHOD, null, parameters, returnType, pathSegments);
	}

	@Override
	public <T> T getConfigurations(Map<String, String> parameters, Class<T> returnType) {
		return getEndPoint(NHL_STATS_API_CONFIGURATIONS_METHOD, null, parameters, returnType, null);
	}

    @Override
    public <T> T getRosterStatuses(Map<String, String> parameters, Class<T> returnType) {
        return getEndPoint(NHL_STATS_API_ROSTER_STATUSES_METHOD, null, parameters, returnType, null);
    }

    @Override
    public <T> T getRosterStatuses(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_ROSTER_STATUSES_METHOD, null, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getPlatforms(Map<String, String> parameters, Class<T> returnType) {
        return getEndPoint(NHL_STATS_API_PLATFORMS_METHOD, null, parameters, returnType, null);
    }

    @Override
    public <T> T getPlatforms(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_PLATFORMS_METHOD, null, parameters, returnType, pathSegments);
    }

	@Override
	public <T> T getDraft(Map<String, String> parameters, Class<T> returnType) {
		return getEndPoint(NHL_STATS_API_DRAFT_METHOD, null, parameters, returnType, null);
	}

	@Override
	public <T> T getDraft(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
		return getEndPoint(NHL_STATS_API_DRAFT_METHOD, null, parameters, returnType, pathSegments);
	}

    @Override
    public <T> T getProvidedEndpoint(Map<String, String> parameters, Class<T> returnType) {
        return getEndPoint(NHL_STATS_API_ENDPOINT_METHOD, null, parameters, returnType, null);
    }

    @Override
    public <T> T getProvidedEndpoint(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_ENDPOINT_METHOD, null, parameters, returnType, pathSegments);
    }
}
