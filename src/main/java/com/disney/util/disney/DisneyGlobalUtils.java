package com.disney.util.disney;

import com.disney.qa.api.utils.DisneyCountryData;
import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.config.ConfigurationHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class DisneyGlobalUtils {
    protected DisneyCountryData disneyCountryData = new DisneyCountryData();
    Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

    public List<String> getRatingValueFromCountries(String locale, String itemToSearch) {
        try {
            return (List<String>) disneyCountryData.searchAndReturnCountryData(locale, "code", itemToSearch);
        } catch (NullPointerException e) {
            return List.of();
        }
    }
}
