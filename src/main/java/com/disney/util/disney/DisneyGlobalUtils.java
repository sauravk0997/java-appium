package com.disney.util.disney;

import com.zebrunner.carina.utils.R;
import com.zebrunner.agent.core.config.ConfigurationHolder;

public class DisneyGlobalUtils {

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
}
