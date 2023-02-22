package com.disney.qa.tests.nhl.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by boyle on 4/13/17.
 */
public class NhlBaseDailyApiSetup extends BaseNhlApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    List<Object[]> gameList = new CopyOnWriteArrayList<>();

    private Configuration jsonPathJsonConfig = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider()).options(Option.DEFAULT_PATH_LEAF_TO_NULL)
            .build();

    @DataProvider
    public Iterator<Object[]> previousDayGamesList() throws IOException, URISyntaxException {

        return retrieveGameList(1, 365);
    }

    @DataProvider
    public Iterator<Object[]> currentDayGamesList() throws IOException, URISyntaxException {

        return retrieveGameList(0, 1);
    }

    private Iterator<Object[]> retrieveGameList(int daysToSubtractToStart, int maxDaysToScan) throws IOException, URISyntaxException {

        for (int i = daysToSubtractToStart; i < maxDaysToScan; i++) {
            String gameDate = getDateForGames(i);

            URI uri = new URI(String.format("https://statsapi.web.nhl.com/api/v1/schedule?startDate=%s&endDate=%s", gameDate, gameDate));
            LOGGER.info(String.format("Checking URL (%s) for games.", uri));
            ArrayNode games = jsonContext().parse(uri.toURL().openStream()).read("$.dates[*].games[*]");

            if (games.size() != 0) {
                addGamesToProviderList(games, gameDate);
                break;
            }
        }

        return gameList.iterator();
    }

    private String getDateForGames(int daysToSubtract) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysToSubtract);

        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(cal.getTimeInMillis()));
    }

    private void addGamesToProviderList(ArrayNode games, String gameDate) {
        for (JsonNode game : games) {
            JsonNode gamePk = jsonContext().parse(game).read("$.gamePk");
            JsonNode teams = jsonContext().parse(game).read("$.teams[*].team.name");

            LOGGER.info("Checking gamePk: " + gamePk);
            LOGGER.info("Checking teams: " + teams);

            gameList.add(
                    new Object[]{
                            String.format("TUID: Game #: (%s) GamePk: (%s) Teams: %s vs. %s (%s)", gameList.size() + 1, gamePk.asText(), teams.get(0).asText(), teams.get(1).asText(), gameDate), gamePk.asText()});
        }
    }

    protected ParseContext jsonContext() {
        return JsonPath.using(jsonPathJsonConfig);
    }
}
