package com.disney.qa.api.nhl;

import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public enum NhlParameters {
    NHL_STATS_API_HOST_CMS("nhl_stats_api_host_cms"),
    NHL_STATS_API_HOST_DEV("nhl_stats_api_host_dev"),
    NHL_STATS_API_HOST_QA("nhl_stats_api_host_qa"),
    NHL_STATS_API_HOST_PROD("nhl_stats_api_host_prod"),
    MLB_NHL_STATS_API_HOST("mlb_nhl_stats_api_host"),
    NHL_SEARCH_API_HOST("nhl_search_api_host_prod"),
    NHL_FEED_HOST("nhl_feed_host"),
    NHL_FEED_HOST_F("nhl_feed_host_f"),
    NHL_FEED_AUTH_SECRET_API_KEY("nhl_feed_auth_secret_api_key"),
    NHL_FEED_PARTNER_NAME("nhl_feed_partner_name"),
    NHL_BASIC_AUTHENTICATION_LOGIN("nhl_basic_authentication_login"),
    NHL_BASIC_AUTHENTICATION_PASSWORD("nhl_basic_authentication_password"),
    NHL_CONTENT_API_HOST("nhl_content_api_host"),
    NHL_SEARCH_API_HOST_PROD("nhl_search_api_host_prod"),
    NHL_SEARCH_API_HOST_QA("nhl_search_api_host_qa"),
    NHL_POINT_INSIDE_API_HOST("nhl_point_inside_api_host"),
    NHL_CONFIG_HOST("nhl_config_host"),
    NHL_CONFIG_HOST_QA("nhl_config_host_qa"),
    NHL_LEAGUE_STATIC_HOST("nhl_league_static_host"),
    NHL_SPORTSDATA_DICTIONARY_HOST_PROD("nhl_sportsdata_dictionary_host_prod"),
    NHL_SPORTSDATA_DICTIONARY_HOST_QA("nhl_sportsdata_dictionary_host_qa"),
    NHL_PUSH_NOTIFICATIONS_HOST("nhl_push_notifications_host");
    private String key;

    private CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));

    private static final String ANDROID_QA_RELEASE = "mobile.qa.google.release";
    private static final String ANDROID_QA_DEBUG = "mobile.qa.google.debug";
    private static final String ANDROID_PROD_RELEASE = "mobile.prod.google.release";
    private static final String ANDROID_PROD_DEBUG = "mobile.prod.google.debug";

    private static final String AMAZON_PROD_RELEASE = "mobile.prod.amazon.release";
    private static final String AMAZON_QA_RELEASE = "mobile.qa.amazon.release";
    private static final String IOS_PROD_ADHOC = "Prod Adhoc";

    NhlParameters(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return R.TESTDATA.get(this.key);
    }

    public String getDecryptedValue() {
        return cryptoTool.decrypt(getValue());
    }

    public static String getNhlStatsApiHost() {
        String environment = R.CONFIG.get("env");
        String host;
        switch (environment) {
            case "CMS":
                host = NHL_STATS_API_HOST_CMS.getValue();
                break;
            case "DEV":
                host = NHL_STATS_API_HOST_DEV.getValue();
                break;
            case "QA Release":
            case "QA.Release":
            case ANDROID_QA_RELEASE:
            case ANDROID_QA_DEBUG:
            case AMAZON_QA_RELEASE:
            case "QA Internal":
            case "QA.Debug":
            case "QA":
                host = NHL_STATS_API_HOST_QA.getValue();
                break;
            case "PROD":
            case "Prod.Release":
            case "Prod.Google.Release":
            case IOS_PROD_ADHOC:
            case ANDROID_PROD_RELEASE:
            case AMAZON_PROD_RELEASE:
            case ANDROID_PROD_DEBUG:
                host = NHL_STATS_API_HOST_PROD.getValue();
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is invalid environment parameter for NHL Stats API tests", environment));
        }
        return host;
    }

    public static String getNhlSeachApiHost() {
        String environment = R.CONFIG.get("env");
        String host = "";
        switch (environment) {
            case "DEV":
            case "QA Release":
            case "QA.Release":
            case ANDROID_QA_RELEASE:
            case "QA Internal":
            case "QA.Debug":
            case "QA":
            case "PROD":
            case "Prod.Release":
            case "Prod.Google.Release":
            case IOS_PROD_ADHOC:
            case ANDROID_PROD_RELEASE:
            case ANDROID_PROD_DEBUG:
                host = NHL_SEARCH_API_HOST.getValue();
                break;

            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is invalid environment parameter for NHL Stats API tests", environment));
        }
        return host;
    }

    public static String getNhlContentApiHost() {
        String environment = R.CONFIG.get("env");
        String host;
        switch (environment) {
            case IOS_PROD_ADHOC:
                host = NHL_CONTENT_API_HOST.getValue();
                break;
            case "Prod Enterprise":
                host = NHL_CONTENT_API_HOST.getValue();
                break;
            case ANDROID_PROD_RELEASE:
            case ANDROID_PROD_DEBUG:
            case AMAZON_PROD_RELEASE:
            case "PROD":
                host = NHL_CONTENT_API_HOST.getValue();
                break;
            case ANDROID_QA_RELEASE:
            case AMAZON_QA_RELEASE:
                host = NHL_CONTENT_API_HOST.getValue();
                break;
            case ANDROID_QA_DEBUG:
                host = NHL_CONTENT_API_HOST.getValue();
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is invalid environment parameter for NHL Content API tests", environment));
        }
        return host;
    }

    public static String getNhlSearchApiHost(){
        String environment = R.CONFIG.get("env");
        String host;
        switch(environment) {
            case IOS_PROD_ADHOC:
                host = NHL_SEARCH_API_HOST_PROD.getValue();
                break;
            case "Prod Enterprise":
                host = NHL_SEARCH_API_HOST_PROD.getValue();
                break;
            case ANDROID_PROD_RELEASE:
            case AMAZON_PROD_RELEASE:
            case ANDROID_PROD_DEBUG:
            case "PROD":
                host = NHL_SEARCH_API_HOST_PROD.getValue();
                break;
            case ANDROID_QA_RELEASE:
            case AMAZON_QA_RELEASE:
                host = NHL_SEARCH_API_HOST_QA.getValue();
                break;
            case ANDROID_QA_DEBUG:
                host = NHL_SEARCH_API_HOST_QA.getValue();
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is invalid environment parameter for NHL Search API tests", environment));
        }
        return host;
    }

    public static String getPointInsideApiHost(){
        return NHL_POINT_INSIDE_API_HOST.getValue();
    }

    public static String getConfigHost(){
        return R.CONFIG.get("env").contains("qa") ? NHL_CONFIG_HOST_QA.getValue() : NHL_CONFIG_HOST.getValue();
    }

    public static String getNhlStaticHost(){
        return NHL_LEAGUE_STATIC_HOST.getValue();
    }

    public static String getNhlDictionaryHost(){
        String environment = R.CONFIG.get("env");
        switch (environment){
            case "PROD":
                return NHL_SPORTSDATA_DICTIONARY_HOST_PROD.getValue();
            case "QA":
                return NHL_SPORTSDATA_DICTIONARY_HOST_QA.getValue();
            case "CMS":
                return NHL_STATS_API_HOST_CMS.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is invalid environment parameter for Dictionary tests", environment));
        }
    }

    public static String getPushNotificationsHost(){
        return NHL_PUSH_NOTIFICATIONS_HOST.getValue();
    }
}
