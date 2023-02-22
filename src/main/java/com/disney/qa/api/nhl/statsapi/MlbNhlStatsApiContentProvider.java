package com.disney.qa.api.nhl.statsapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class MlbNhlStatsApiContentProvider extends NhlStatsApiContentProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static final String NHL_STATS_API_GAMES_METHOD = "api/v1.1/game";

    @Override
    public <T> T getGames(Map<String, String> parameters, Class<T> returnType, String... pathSegments) {
        return getEndPoint(NHL_STATS_API_GAMES_METHOD, null, parameters, returnType, pathSegments);
    }
}
