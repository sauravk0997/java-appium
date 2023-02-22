package com.disney.qa.api.nhl.feed;

import com.disney.qa.api.nhl.NhlContentProvider;
import com.disney.qa.api.nhl.NhlObjectConverter;
import com.disney.qa.api.nhl.pojo.Game;
import com.disney.qa.api.nhl.pojo.Schedule;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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

public class NhlFeedContentProvider implements NhlContentProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String NHL_FEED_EXPANDED_SCHEDULE_METHOD = "feed/nhl/league/schedule/expanded/schedule.json";

    public static final String NHL_FEED_EXPANDED_STANDINGS_METHOD = "feed/nhl/club/expanded/standings.json";

    public static final String NHL_FEED_EXPANDED_LEADERS_METHOD = "feed/nhl/gamedata/expanded/statsleaders.json";

    public static final String NHL_FEED_EXPANDED_GAMES_METHOD = "feed/nhl/gamedata/expanded";

    public static final String NHL_FEED_EXPANDED_PLAYERS_METHOD = "feed/nhl/playerdata/expanded";

    public static final String NHL_FEED_EXPANDED_BOXSCORE_METHOD = "feed/nhl/gamedata/expanded/boxscore.json";

    protected String nhlFeedHost;

    protected RestTemplate restTemplate;

    protected String secretApiKey;

    protected String partnerName;

    public String getNhlFeedHost() {
        return nhlFeedHost;
    }

    public void setNhlFeedHost(String nhlFeedHost) {
        this.nhlFeedHost = nhlFeedHost;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getSecretApiKey() {
        return secretApiKey;
    }

    public void setSecretApiKey(String secretApiKey) {
        this.secretApiKey = secretApiKey;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    @Override
    public Schedule getSchedule() {
        MultiValueMap queryParams = new LinkedMultiValueMap();
        queryParams.add("auth", new NhlFeedAuthGenerator().generateAuth(partnerName, secretApiKey));

        URI uri = UriComponentsBuilder.fromHttpUrl(nhlFeedHost).
                path(NHL_FEED_EXPANDED_SCHEDULE_METHOD).queryParams(queryParams).build().toUri();

        RequestEntity<?> readRequestEntity = new RequestEntity(HttpMethod.GET, uri);

        LOGGER.info(String.format("Requesting Feed Expanded Schedule method: %s", uri));

        com.disney.qa.api.nhl.pojo.feed.Schedule feedSchedule =
                restTemplate.exchange(readRequestEntity, com.disney.qa.api.nhl.pojo.feed.Schedule.class).getBody();

        return new NhlObjectConverter().toSchedule(feedSchedule);
    }

    @Override
    public Schedule getSchedule(String startDate, String endDate) {
        String authString = new NhlFeedAuthGenerator().generateAuth(partnerName, secretApiKey);

        MultiValueMap queryParams = new LinkedMultiValueMap();
        queryParams.add("auth", authString);
        queryParams.add("startDate", startDate);
        queryParams.add("endDate", endDate);

        URI uri = UriComponentsBuilder.fromHttpUrl(nhlFeedHost).
                path(NHL_FEED_EXPANDED_SCHEDULE_METHOD).queryParams(queryParams).build().toUri();

        RequestEntity<?> readRequestEntity = new RequestEntity(HttpMethod.GET, uri);

        LOGGER.info(String.format("Requesting Feed Expanded Schedule method: %s", uri));

        com.disney.qa.api.nhl.pojo.feed.Schedule feedSchedule =
                restTemplate.exchange(readRequestEntity, com.disney.qa.api.nhl.pojo.feed.Schedule.class).getBody();

        return new NhlObjectConverter().toSchedule(feedSchedule);
    }

    @Override
    public Schedule getSchedule(final Integer teamId, String startDate, String endDate) {
        Schedule schedule = getSchedule(startDate, endDate);

        LOGGER.info(String.format("Filtering output of Feed Expanded Schedule method by team {ID: %s}", teamId));

        Iterable<Game> gameList = Iterables.filter(schedule.getGameList(), new Predicate<Game>() {
            @Override
            public boolean apply(Game input) {
                return teamId.equals(input.getAwayTeam().getId()) || teamId.equals(input.getHomeTeam().getId());
            }
        });

        return new Schedule(Lists.newArrayList(gameList));
    }

    @Override
    public Schedule getSchedule(final Map<String, String> parameters) {
        String authString = new NhlFeedAuthGenerator().generateAuth(partnerName, secretApiKey);

        boolean filterResponseByTeamId = false;

        MultiValueMap queryParams = new LinkedMultiValueMap();
        queryParams.add("auth", authString);

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if ("teamId".equals(entry.getKey())) {
                filterResponseByTeamId = true;
            } else {
                queryParams.add(entry.getKey(), entry.getValue());
            }
        }

        URI uri = UriComponentsBuilder.fromHttpUrl(nhlFeedHost).
                path(NHL_FEED_EXPANDED_SCHEDULE_METHOD).queryParams(queryParams).build().toUri();

        RequestEntity<?> readRequestEntity = new RequestEntity(HttpMethod.GET, uri);

        LOGGER.info(String.format("Requesting Feed Expanded Schedule method: %s", uri));

        com.disney.qa.api.nhl.pojo.feed.Schedule feedSchedule =
                restTemplate.exchange(readRequestEntity, com.disney.qa.api.nhl.pojo.feed.Schedule.class).getBody();

        Schedule schedule = new NhlObjectConverter().toSchedule(feedSchedule);

        if (filterResponseByTeamId) {
            Iterable<Game> gameList = Iterables.filter(schedule.getGameList(), new Predicate<Game>() {
                @Override
                public boolean apply(Game input) {
                    return Integer.valueOf(parameters.get("teamId")).equals(input.getAwayTeam().getId()) ||
                            Integer.valueOf(parameters.get("teamId")).equals(input.getHomeTeam().getId());
                }
            });

            return new Schedule(Lists.newArrayList(gameList));
        }

        return new NhlObjectConverter().toSchedule(feedSchedule);
    }

    @Override
    public <T> T getSchedule(Map<String, String> parameters, Class<T> returnType) {
        return getMethod(NHL_FEED_EXPANDED_SCHEDULE_METHOD, parameters, returnType, null);
    }

    @Override
    public <T> T getSchedule(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getSchedule(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getStandings(Map<String, String> parameters, Class<T> returnType) {
        return getMethod(NHL_FEED_EXPANDED_STANDINGS_METHOD, parameters, returnType);
    }

    @Override
    public <T> T getStandings(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getTeams(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getTeams(Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getTeams(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getLeaders(Map<String, String> parameters, Class<T> returnType) {
        return getLeaders(parameters, returnType, null);
    }

    @Override
    public <T> T getLeaders(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getMethod(NHL_FEED_EXPANDED_LEADERS_METHOD, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getPeople(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getMethod(NHL_FEED_EXPANDED_PLAYERS_METHOD, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getPeople(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getDivisions(Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getConferences(Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getSeasons(Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getSeasons(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getGames(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getMethod(NHL_FEED_EXPANDED_GAMES_METHOD, parameters, returnType, pathSegments);
    }

    @Override
    public <T> T getGames(Map<String, String> headers, Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getBoxScore(Map<String, String> parameters, Class<T> returnType) {
        return getMethod(NHL_FEED_EXPANDED_BOXSCORE_METHOD, parameters, returnType);
    }

    @Override
    public <T> T getTournaments(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getAwards(Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getAwards(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    // TODO [normal] method is the same as in stats api provider - think how to combine
    protected <T> T getMethod(String methodPath, Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        String authString = new NhlFeedAuthGenerator().generateAuth(partnerName, secretApiKey);

        MultiValueMap queryParams = new LinkedMultiValueMap();
        queryParams.add("auth", authString);

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            queryParams.add(entry.getKey(), entry.getValue());
        }

        UriComponentsBuilder builder =
                UriComponentsBuilder.fromHttpUrl(nhlFeedHost).path(methodPath).queryParams(queryParams);

        if (!(pathSegments == null)) {
            builder = builder.pathSegment(pathSegments);
        }

        URI uri = builder.build().toUri();

        RequestEntity<?> readRequestEntity = new RequestEntity(HttpMethod.GET, uri);

        LOGGER.info(String.format("Requesting Feed method '%s': %s", methodPath, uri));

        T statsSchedule = restTemplate.exchange(readRequestEntity, returnType).getBody();

        return statsSchedule;
    }

	@Override
	public <T> T getStats(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
		 throw new UnsupportedOperationException("Method is not implemented yet");
	}

	@Override
	public <T> T getConfigurations(Map<String, String> parameters, Class<T> returnType) {
		throw new UnsupportedOperationException("Method is not implemented yet");
	}

    @Override
    public <T> T getRosterStatuses(Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getRosterStatuses(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getPlatforms(Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getPlatforms(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

	@Override
	public <T> T getDraft(Map<String, String> parameters, Class<T> returnType) {
		throw new UnsupportedOperationException("Method is not implemented yet");
	}

	@Override
	public <T> T getDraft(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
		throw new UnsupportedOperationException("Method is not implemented yet");
	}

    @Override
    public <T> T getProvidedEndpoint(Map<String, String> parameters, Class<T> returnType) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }

    @Override
    public <T> T getProvidedEndpoint(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        throw new UnsupportedOperationException("Method is not implemented yet");
    }
}
