package com.disney.util.disney;

import com.disney.qa.api.utils.DisneyCountryData;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.config.ConfigurationHolder;

public class DisneyGlobalUtils {
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

    //Returns boolean value from country yaml
    public boolean getBooleanFromCountries(String locale, String itemToSearch) {
        try {
            return Boolean.parseBoolean((String) disneyCountryData.searchAndReturnCountryData(locale, "code", itemToSearch));
        } catch (NullPointerException e) {
            return false;
        }
    }
}
