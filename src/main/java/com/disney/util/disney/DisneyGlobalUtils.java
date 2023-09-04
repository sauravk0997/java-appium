package com.disney.util.disney;

import com.disney.qa.api.disney.DisneyApiCommon;
import com.disney.qa.api.utils.DisneyCountryData;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.config.ConfigurationHolder;
import org.joda.time.DateTime;

public class DisneyGlobalUtils {
    protected DisneyApiCommon disneyApiCommon = new DisneyApiCommon();
    protected DisneyCountryData disneyCountryData = new DisneyCountryData();

    //Returns host config property value
    public static String getProject() {
        if (
            ConfigurationHolder.getProjectKey().isEmpty() ||
            ConfigurationHolder.getProjectKey().equalsIgnoreCase("DEF") ||
            ConfigurationHolder.getProjectKey().equalsIgnoreCase("UNKNOWN")
        ) {
            return R.CONFIG.get("reporting.project-key");
        }
        return ConfigurationHolder.getProjectKey();
    }

    public boolean isDateWithinRange(DateTime startDate, DateTime endDate) {
        DateTime today = DateTime.parse(disneyApiCommon.formatDateForQuery(DateTime.now().toString()));

        return today.toDate().getTime() >= startDate.toDate().getTime() &&
            today.toDate().getTime() <= endDate.toDate().getTime();
    }

    //Returns boolean value from country yaml
    public boolean getBooleanFromCountries(String locale, String itemToSearch) {
        try {
            return Boolean.parseBoolean((String) disneyCountryData.searchAndReturnCountryData(locale, "code", itemToSearch));
        } catch (NullPointerException e) {
            return false;
        }
    }
}
