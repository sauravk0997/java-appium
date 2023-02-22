package com.disney.qa.api.espn;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EspnApiCommon {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Configuration jsonPathJsonConfig = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider()).options(Option.DEFAULT_PATH_LEAF_TO_NULL)
            .build();

    public String getDateForGames(int daysToAdd, String format) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysToAdd);

        return new SimpleDateFormat(format).format(new Date(cal.getTimeInMillis()));
    }

    public String formatDateForQuery(String date) {

        String updatedDate = new SimpleDateFormat("yyyy-MM-dd").format(DateTime.parse(date).withZone(DateTimeZone.forID("EST")).toDate());
        String updatedTime = new SimpleDateFormat("HH:mm:ssZ").format(DateTime.parse(date).withZone(DateTimeZone.forID("EST")).toDate());

        return updatedDate + "T" + updatedTime;
    }

    public ParseContext jsonContext() {
        return JsonPath.using(jsonPathJsonConfig);
    }
}
