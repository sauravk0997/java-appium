package com.disney.qa.tests.nhl.api;

import com.qaprosoft.carina.core.foundation.dataprovider.annotations.XlsDataSourceParameters;
import com.qaprosoft.carina.core.foundation.utils.ownership.MethodOwner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.lang.invoke.MethodHandles;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NhlCrawlerTest extends BaseNhlApiTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2";

    @DataProvider(name = "nhlCrawlerUrl")
    public static Object[][] nhlCrawlerSiteRequestsDataProvider() {
        return new Object[][]{

                {"TUID: Nhl Homepage", "https://www.nhl.com"},
                {"TUID: Nhl Scores", "https://www.nhl.com/scores"},
                {"TUID: Nhl News", "https://www.nhl.com/news"},
                {"TUID: Nhl News Articles", "https://www.nhl.com/news/cam-atkinson-added-to-nhl-all-star-game-roster/c-286128574"},
                {"TUID: Nhl Videos", "https://www.nhl.com/video"},
                {"TUID: Nhl VTP", "https://www.nhl.com/video/nhl100-king-clancy/t-277350912/c-47880603"},
                {"TUID: Nhl Stats", "http://www.nhl.com/stats"},
                {"TUID: Nhl Stats Leaders", "http://www.nhl.com/stats/leaders"},
                {"TUID: Nhl Player Stats", "http://www.nhl.com/stats/player?aggregate=0&gameType=2&report=skatersummary&pos=S&reportType=season&seasonFrom=20162017&seasonTo=20162017&filter=gamesPlayed,gte,1&sort=points,goals,assists"},
                {"TUID: Nhl Team Stats", "http://www.nhl.com/stats/team?aggregate=0&gameType=2&report=teamsummary&reportType=season&seasonFrom=20162017&seasonTo=20162017&filter=gamesPlayed,gte,1&sort=points,wins"},
                {"TUID: Nhl Standings", "https://www.nhl.com/standings"},
                {"TUID: Nhl Standings by year", "https://www.nhl.com/standings/2017"},
                {"TUID: Nhl Schedule", "https://www.nhl.com/schedule"},
                {"TUID: Nhl Most Searched Players", "https://www.nhl.com/player"},
                {"TUID: Nhl Player page", "https://www.nhl.com/player/sidney-crosby-8471675"},
        };
    }

    /**
     * JIRA ticket: https://jira.bamtechmedia.com/browse/QAA-1624
     */
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "nhlCrawlerUrl")
    public void verifyCrawler(String TUID, String siteUrl) throws Exception {
        List<String> links = NhlCrawlerTest.getLinks(siteUrl);

        SoftAssert softAssert = new SoftAssert();

        getUrl(softAssert, links);

        softAssert.assertAll();
    }

    @DataProvider(name = "nhlCrawlerLanguageUrl")
    public static Object[][] nhlCrawlerLanguageSiteRequestsDataProvider() {
        return new Object[][]{

                {"TUID: Language fr", "fr"},
                {"TUID: Language ru", "ru"},
                {"TUID: Language fi", "fi"},
                {"TUID: Language cs", "cs"},
                {"TUID: Language sk", "sk"},
                {"TUID: Language sv", "sv"},
                {"TUID: Language de", "de"},
        };
    }

    /**
     * JIRA ticket: https://jira.bamtechmedia.com/browse/QAA-1624
     */
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "nhlCrawlerLanguageUrl")
    public void verifyCrawlerLanguages(String TUID, String languages) throws Exception {

        String site = "https://www.nhl.com/" + languages ;
        List<String> links = NhlCrawlerTest.getLinks(site);

        SoftAssert softAssert = new SoftAssert();

        getUrl(softAssert, links);

        softAssert.assertAll();
    }

    /**
     * JIRA ticket: https://jira.bamtechmedia.com/browse/QAA-1624
     */
    @MethodOwner(owner = "shashem")
    @Test(description = "Verify: Schedule Timezone")
    public void verifyScheduleTimezone() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        SoftAssert softAssert = new SoftAssert();

        List<String> allTimeZone = new ArrayList<>();
        allTimeZone.add("ET");
        allTimeZone.add("NT");
        allTimeZone.add("AT");
        allTimeZone.add("CT");
        allTimeZone.add("MT");
        allTimeZone.add("RT");
        allTimeZone.add("PT");
        allTimeZone.add("VENUE");

        for(String timezone : allTimeZone){
            String site = "https://www.nhl.com/schedule/" + date + "/" + timezone;
            List<String> links = NhlCrawlerTest.getLinks(site);

            getUrlForTimezone(softAssert, links, timezone);
        }

        softAssert.assertAll();
    }

    /**
     * JIRA ticket: https://jira.bamtechmedia.com/browse/QAA-1624
     */
    @MethodOwner(owner = "shashem")
    @Test(dataProvider = "DataProvider", description = "Verify: Team Timezone")
    @XlsDataSourceParameters(sheet = "abbreviation_with_nhl", dsUid = "TUID, Name")
    public void verifyTeamScheduleTimezone(Map<String, String> data) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        SoftAssert softAssert = new SoftAssert();

        List<String> allTimeZone = new ArrayList<>();
        allTimeZone.add("ET");
        allTimeZone.add("NT");
        allTimeZone.add("AT");
        allTimeZone.add("CT");
        allTimeZone.add("MT");
        allTimeZone.add("RT");
        allTimeZone.add("PT");
        allTimeZone.add("VENUE");

        for(String timezone : allTimeZone){
            String site = "https://www.nhl.com/" + "ducks" + "/" + "schedule/" + date + "/" + timezone;
            List<String> links = NhlCrawlerTest.getLinks(site);

            getUrlForTimezone(softAssert, links, timezone);
        }

        softAssert.assertAll();
    }

    private static List<String> getLinks(String url) throws Exception {
        ArrayList<String> result = new ArrayList<>();

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");

        for (Element link : links) {
            result.add(link.attr("abs:href"));
        }
        return result;
    }

    private void getUrl(SoftAssert softAssert, List<String> links){
        try {
            List<String> uniqueLinks = links.stream().distinct().collect(Collectors.toList());
            for (String link : uniqueLinks) {
                if (!link.contains("ticketmaster")) {
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(0);
                    connection.setRequestProperty("User-Agent", USER_AGENT);
                    connection.setRequestMethod("GET");
                    connection.connect();

                    LOGGER.info("Site urls : " + url);

                    String code = Integer.toString(connection.getResponseCode());

                    if (!(code.startsWith("2") || code.startsWith("3"))) {
                        softAssert.fail("Page broken: " + "Status Code: " + code + " Link: " + link);
                    }
                }
            }
        }
        catch (Exception e) {
            LOGGER.info("Full error: " + e);
        }
    }

    private void getUrlForTimezone(SoftAssert softAssert, List<String> links, String timeZone){
        try {
            List<String> uniqueLinks = links.stream().distinct().collect(Collectors.toList());
            for (String link : uniqueLinks) {
                if(!link.contains("linkedin")) {
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent", USER_AGENT);
                    connection.setRequestMethod("GET");
                    connection.connect();

                    LOGGER.info("Site Urls: " + url );

                    String code = Integer.toString(connection.getResponseCode());

                    if (!(code.startsWith("2") || code.startsWith("3"))) {
                        softAssert.fail("Page broken for timezone: " + timeZone + " Status Code: " + code + " Link: " + link);
                    }
                }
            }
        }
        catch (Exception e) {
            LOGGER.info("Full error: " + e);
        }
    }
}
