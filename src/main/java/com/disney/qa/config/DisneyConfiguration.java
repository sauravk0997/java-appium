package com.disney.qa.config;

import com.zebrunner.carina.utils.R;

// should be refactored since carina 1.1.5+
public final class DisneyConfiguration {

    private DisneyConfiguration() {
        //hide
    }

    public static boolean useMultiverse() {
        return R.CONFIG.getBoolean("useMultiverse");
    }

    public static String deviceType() {
        return R.CONFIG.get("capabilities.deviceType");
    }

    public static String partner() {
        return R.CONFIG.get("partner");
    }

    public static boolean isHoraEnabled() {
        return R.CONFIG.getBoolean("enable_hora_validation");
    }

}
