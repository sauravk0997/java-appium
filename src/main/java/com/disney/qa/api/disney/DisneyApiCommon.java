package com.disney.qa.api.disney;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.text.SimpleDateFormat;
import java.time.Instant;

public class DisneyApiCommon {

    public String formatDateForQuery(String date) {

        String updatedDate = new SimpleDateFormat("yyyy-MM-dd").format(DateTime.parse(date).withZone(DateTimeZone.forID("EST")).toDate());
        String updatedTime = new SimpleDateFormat("HH:mm:ss.S").format(DateTime.parse(date).withZone(DateTimeZone.forID("EST")).toDate());

        return updatedDate + "T" + updatedTime + "Z";
    }

    public String getCurrentEpochTime() {
        return Long.toString(Instant.now().toEpochMilli());
    }

    public String getCurrentSystemNanoTime() {
        return Long.toString(System.nanoTime());
    }
}
