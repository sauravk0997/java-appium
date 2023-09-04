package com.disney.qa.api.disney;

import com.disney.qa.star.StarPlusParameters;
import com.disney.util.disney.DisneyGlobalUtils;
import com.zebrunner.carina.crypto.CryptoTool;
import com.zebrunner.carina.crypto.CryptoToolBuilder;
import com.zebrunner.carina.utils.R;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.IDriverPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.zebrunner.carina.crypto.Algorithm.AES_ECB_PKCS5_PADDING;

public enum DisneyParameters {
    API_PROD_ENVIRONMENT("disney_api_prod_service"),
    API_QA_ENVIRONMENT("disney_api_qa_service"),
    API_ACCOUNT_PRIVATE_PROD_ENVIRONMENT("disney_api_account_private_prod_service"),
    API_ACCOUNT_PRIVATE_QA_ENVIRONMENT("disney_api_account_private_qa_service"),
    API_IDP_PRIVATE_PROD_ENVIRONMENT("disney_api_idp_private_prod_service"),
    API_IDP_PRIVATE_QA_ENVIRONMENT("disney_api_idp_private_qa_service"),
    API_DETECTIVE_PROD_ENVIRONMENT("disney_api_detective_prod_service"),
    API_DETECTIVE_QA_ENVIRONMENT("disney_api_detective_qa_service"),
    EDGE_QA_ENVIRONMENT("disney_edge_qa_service"),
    EDGE_PROD_ENVIRONMENT("disney_edge_prod_service"),
    ONEID_CLIENT("disney_oneid_client"),
    ONEID_PROD_ENVIRONMENT("disney_oneid_prod_service"),
    ONEID_STAGE_ENVIRONMENT("disney_oneid_stage_service"),
    ONEID_PROD_ENVNAME("disney_oneid_prod_env_name"),
    ONEID_STAGE_ENVNAME("disney_oneid_stage_env_name"),
    SUBSCRIPTION_PROD_ENVIRONMENT("disney_subscription_prod_service"),
    SUBSCRIPTION_QA_ENVIRONMENT("disney_subscription_qa_service"),
    SUBSCRIPTION_V2_PROD_ENVIRONMENT("disney_subscription_prod_service_v2"),
    SUBSCRIPTION_V2_QA_ENVIRONMENT("disney_subscription_qa_service_v2"),
    OFFER_QA_ENVIRONMENT("disney_qa_offer_service"),
    OFFER_PROD_ENVIRONMENT("disney_prod_offer_service"),
    ORDERS_QA_ENVIRONMENT("disney_orders_qa_service"),
    ORDERS_PROD_ENVIRONMENT("disney_orders_prod_service"),
    ORDER_SUBMISSION_PROD_ENVIRONMENT("disney_order_submission_prod_service"),
    ORDER_SUBMISSION_QA_ENVIRONMENT("disney_order_submission_qa_service"),
    LOCATION_SERVICE_PROD("disney_location_prod_service"),
    LOCATION_SERVICE_QA("disney_location_qa_service"),
    SEARCH_API_HOST("disney_search_api_host"),
    SEARCH_API_HOST_INTERNAL("disney_search_api_host_internal"),
    SEARCH_API_HOST_QA("disney_search_api_host_qa"),
    SEARCH_API_HOST_QA_INTERNAL("disney_search_api_host_qa_internal"),
    APPCONFIGS_HOST("disney_appconfigs_host"),
    WEBCONFIGS_HOST("disney_webconfigs_host"),
    DISNEY_GLOBAL_CONFIGS("disney_global_configs"),
    APPLICATION_RESOURCE_KEY("disney_application_resource_key"),
    PAYWALL_RESOURCE_KEY("disney_paywall_resource_key"),
    SDK_ERRORS_RESOURCE_KEY("disney_sdk_errors_resource_key"),
    LANGUAGE_SETTINGS_RESOURCE_KEY("disney_language_settings_resource_key"),
    ACCESSIBILITY_RESOURCE_KEY("disney_accessibility_resource_key"),
    RATINGS_RESOURCE_KEY("disney_ratings_resource_key"),
    COMMERCE_RESOURCE_KEY("disney_commerce_resource_key"),
    PROMO_RESOURCE_KEY("disney_promo_resource_key"),
    SEO_RESOURCE_KEY("disney_seo_resource_key"),
    WELCOME_MARKETING_RESOURCE_KEY("disney_welcome_marketing_resource_key"),
    WELCH_RESOURCE_KEY("disney_welch_resource_key"),
    PCON_RESOURCE_KEY("disney_pcon_resource_key"),
    SUBSCRIPTIONS_RESOURCE_KEY("disney_subscriptions_resource_key"),
    ANDROID_PACKAGE("disney_android_package"),
    ANDROID_ACTIVITY("disney_android_activity"),
    DISNEY_DOWNLOAD_HISTORY_SERVICE_HOST_PROD("disney_download_history_service_host_prod"),
    DISNEY_DOWNLOAD_HISTORY_SERVICE_HOST_QA("disney_download_history_service_host_qa"),
    DISNEY_CONTENT_BAMGRID_HOST("disney_content_bamgrid_host"),
    DISNEY_CONTENT_BAMGRID_HOST_QA("disney_content_bamgrid_host_qa"),
    WALLET_PROD_ENVIRONMENT("disney_wallet_prod_service"),
    WALLET_QA_ENVIRONMENT("disney_wallet_qa_service"),
    PAYPAL_TOKEN_PROD_ENVIRONMENT("disney_paypal_token_prod_service"),
    PAYPAL_TOKEN_QA_ENVIRONMENT("disney_paypal_token_qa_service"),
    REDEMPTION_PROD_ENVIRONMENT("disney_redemption_prod_service"),
    REDEMPTION_QA_ENVIRONMENT("disney_redemption_qa_service"),
    DISNEY_PROD_RIPCUT_HOST("disney_prod_ripcut_host"),
    DISNEY_QA_RIPCUT_HOST("disney_qa_ripcut_host"),
    DISNEY_FUZZBUCKET_SCALING_SERVICE("disney_fuzzbucket_scaling_service"),
    DISNEY_FUZZBUCKET_SCALING_CREDENTIALS("disney_fuzzbucket_scaling_credentials"),
    DISNEY_NAMESPACE_ID("disney_namespace_id"),
    DISNEY_PROD_TAXATION_SERVICE_HOST("disney_prod_taxation_service"),
    DISNEY_QA_TAXATION_SERVICE_HOST("disney_qa_taxation_service"),
    STAR_QA_ORDER_CALL("star_qa_order_call"),
    STAR_PROD_ORDER_CALL("star_prod_order_call"),
    JIRA_BASE_URL("jira_base_url");

    private String key;
    private static String runtimeEnvironment;
    // ****** DO NOT MODIFY THE LIST OF IPs BELOW ******
    protected static ArrayList<String> elasticIpAddresses = new ArrayList<>(Arrays.asList("52.202.75.170", "52.203.210.143", "52.203.219.69","52.203.238.163","52.203.238.174","52.203.238.166"));
    protected static final String INVALID_ENVIRONMENT = "is an invalid environment parameter for available";
    private CryptoTool cryptoTool = CryptoToolBuilder.builder().chooseAlgorithm(AES_ECB_PKCS5_PADDING).setKey(R.CONFIG.get("crypto_key_value")).build();

    DisneyParameters(String key) {
        this.key = key;
    }

    public String getValue() {
        return R.TESTDATA.get(this.key);
    }

    public static String getApiDetective() {
        String environment = getEnv();
        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return API_DETECTIVE_PROD_ENVIRONMENT.getValue();
            case "QA":
                return API_DETECTIVE_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s API Detective Hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public String getDecryptedValue() {
        return cryptoTool.decrypt(getValue());
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Returns an environment value based on original env field (Web)
     * or custom_env field specific to Disney+/Star+ (Mobile/Android TV)
     */
    private static void setEnv() {
        String env = R.CONFIG.get("env");
        String jarvisEnv = R.CONFIG.get("capabilities.custom_env");
        if (!jarvisEnv.isEmpty()) {
            switch (jarvisEnv) {
                case "QA":
                case "STAR_QA":
                    runtimeEnvironment = "QA";
                    break;
                default:
                    runtimeEnvironment = "PROD";
            }
        } else if (env.contains("Enterprise") || env.contains("AdHoc")) {
            runtimeEnvironment = env.split(" ")[1];
        } else {
            runtimeEnvironment = env;
        }
    }

    public static String getEnv() {
        if (runtimeEnvironment == null) {
            setEnv();
        }
        return runtimeEnvironment;
    }

    public static String getApiHost() {
        String environment = getEnv();
        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return API_PROD_ENVIRONMENT.getValue();
            case "QA":
                return API_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s API Hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static String getNameSpaceId() {
        switch (DisneyGlobalUtils.getProject()) {
            case "ANA01":
            case "DIS":
            case "DGI": return DISNEY_NAMESPACE_ID.getValue();
            case "STA": return StarPlusParameters.STAR_NAMESPACE_ID.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("Invalid Project for Name Space Id: %s", DisneyGlobalUtils.getProject() ));
        }
    }

    public static String getAppConfigsHost() {
        return APPCONFIGS_HOST.getValue();
    }

    public static String getWebConfigsHost() {
        return WEBCONFIGS_HOST.getValue();
    }

    public static String getAndroidPackage() {
        return ANDROID_PACKAGE.getValue();
    }

    public static String getAndroidActivity() {
        return ANDROID_ACTIVITY.getValue();
    }

    public static String getEdgeHost() {
        String environment = getEnv();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return EDGE_PROD_ENVIRONMENT.getValue();
            case "QA":
                return EDGE_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s Edge Hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static String getPrivateAccountHost() {
        String environment = getEnv();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return API_ACCOUNT_PRIVATE_PROD_ENVIRONMENT.getValue();
            case "QA":
                return API_ACCOUNT_PRIVATE_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s Account Private Hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static String getPrivateIdpHost() {
        String environment = getEnv();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return API_IDP_PRIVATE_PROD_ENVIRONMENT.getValue();
            case "QA":
                return API_IDP_PRIVATE_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s IDP Private Hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static Map<String, String> getOneIdEnvironment() {
        String environment = getEnv();
        Map<String, String> envMap = new HashMap<>();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                envMap.put("client", ONEID_CLIENT.getValue());
                envMap.put("host", ONEID_PROD_ENVIRONMENT.getValue());
                envMap.put("env", ONEID_PROD_ENVNAME.getValue());
                break;
            case "QA":
                envMap.put("client", ONEID_CLIENT.getValue());
                envMap.put("host", ONEID_STAGE_ENVIRONMENT.getValue());
                envMap.put("env", ONEID_STAGE_ENVNAME.getValue());
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s OneID Hosts", environment, INVALID_ENVIRONMENT));
        }
        return envMap;
    }

    public static String getSubscriptionHost() {
        String environment = getEnv();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return SUBSCRIPTION_PROD_ENVIRONMENT.getValue();
            case "QA":
                return SUBSCRIPTION_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s Subscription Hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static String getSubscriptionHostV2() {
        String environment = getEnv();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return SUBSCRIPTION_V2_PROD_ENVIRONMENT.getValue();
            case "QA":
                return SUBSCRIPTION_V2_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s Subscription V2 Hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static String getOrdersHost() {
        String environment = getEnv();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return ORDERS_PROD_ENVIRONMENT.getValue();
            case "QA":
                return ORDERS_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s order hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static String getOrderSubmissionHost() {
        String environment = getEnv();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return ORDER_SUBMISSION_PROD_ENVIRONMENT.getValue();
            case "QA":
                return ORDER_SUBMISSION_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s order submission hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static String getWalletsHost() {
        String environment = getEnv();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return WALLET_PROD_ENVIRONMENT.getValue();
            case "QA":
                return WALLET_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s order hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static String getPayPalTokenHost() {
        String environment = getEnv();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return PAYPAL_TOKEN_PROD_ENVIRONMENT.getValue();
            case "QA":
                return PAYPAL_TOKEN_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s paypal token hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static String getRedemptionsHost() {
        String environment = getEnv();

        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return REDEMPTION_PROD_ENVIRONMENT.getValue();
            case "QA":
                return REDEMPTION_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' %s order hosts", environment, INVALID_ENVIRONMENT));
        }
    }

    public static String getEnvironmentType(String environment) {

        String currentEnvironment = environment.toUpperCase();
        String environmentToUse;

        switch (currentEnvironment) {
            case "QA":
            case "DEV":
            case "DEV-NEXT":
            case "LOCAL":
            case "ENTERPRISE QA":
            case "ADHOC QA":
                environmentToUse = "QA";
                break;
            default:
                environmentToUse = "PROD";
                break;
        }

        return environmentToUse;
    }


    public static List<String> getRegionsForLocationHost(String partner) {
        String environment = R.CONFIG.get("env");
        List<String> awsRegionalList = retrieveAwsRegions();
        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return loadLocationList(LOCATION_SERVICE_PROD.getValue(), partner, awsRegionalList);
            case "QA":
                return loadLocationList(LOCATION_SERVICE_QA.getValue(), partner, awsRegionalList);
            default:
                throw new IllegalArgumentException(
                        String.format(INVALID_ENVIRONMENT, environment));
        }
    }

    private static List<String> loadLocationList(String locationHost, String partner, List<String> awsRegionalList) {
        List<String> locationList = new ArrayList<>();
        for (String region : awsRegionalList) {
            locationList.add(String.format(locationHost, partner, region));
        }
        return locationList;
    }

    public static List<String> retrieveAwsRegions() {
        List<String> awsRegionalList = new ArrayList<>();
        awsRegionalList.add("us-east-1");
        awsRegionalList.add("us-east-2");
        awsRegionalList.add("us-west-2");
        awsRegionalList.add("eu-central-1");
        awsRegionalList.add("eu-west-1");

        return awsRegionalList;
    }

    public static List<String> returnQaAwsNatIps() {
        return elasticIpAddresses;
    }

    public static String getOneIdProductClient() {
        String environment = getEnv();
        String prodHost = ONEID_PROD_ENVIRONMENT.getValue() + "/jgc/v8/client/";
        String qaHost = ONEID_STAGE_ENVIRONMENT.getValue() + "/jgc/v8/client/";
        DeviceType.Type type = IDriverPool.currentDevice.get().getDeviceType();
        switch (getEnvironmentType(environment)) {
            case "PROD":
                switch (type) {
                    case ANDROID_PHONE:
                        return prodHost + "DTCI-DISNEYPLUS.GC.AND-PROD";
                    case IOS_PHONE:
                        return prodHost + "DTCI-DISNEYPLUS.GC.IOS-PROD";
                    case ANDROID_TV:
                        return prodHost + "DTCI-DISNEYPLUS.GC.ANDTV-PROD";
                    case APPLE_TV:
                        return prodHost + "DTCI-DISNEYPLUS.GC.TVOS-PROD";
                    case ANDROID_TABLET:
                        return prodHost + "DTCI-DISNEYPLUS.GC.AND-PROD";
                    case IOS_TABLET:
                        return prodHost + "DTCI-DISNEYPLUS.GC.IOS-PROD";
                    case DESKTOP:
                        return prodHost + "DTCI-DISNEYPLUS.GC.WEB-PROD";
                    default:
                        throw new IllegalArgumentException(
                                String.format("'%s' is not a valid device type", type));
                }
            case "QA":
                switch (type) {
                    case ANDROID_PHONE:
                        return qaHost + "DTCI-DISNEYPLUS.GC.AND-STAGE";
                    case IOS_PHONE:
                        return qaHost + "DTCI-DISNEYPLUS.GC.IOS-STAGE";
                    case ANDROID_TV:
                        return qaHost + "DTCI-DISNEYPLUS.GC.ANDTV-STAGE";
                    case APPLE_TV:
                        return qaHost + "DTCI-DISNEYPLUS.GC.TVOS-STAGE";
                    case ANDROID_TABLET:
                        return qaHost + "DTCI-DISNEYPLUS.GC.AND-STAGE";
                    case IOS_TABLET:
                        return qaHost + "DTCI-DISNEYPLUS.GC.IOS-STAGE";
                    case DESKTOP:
                        return qaHost + "DTCI-DISNEYPLUS.GC.WEB-STAGE";
                    default:
                        throw new IllegalArgumentException(
                                String.format("'%s' is not a valid device type", type));
                }
            default:
                throw new IllegalArgumentException(
                        String.format(INVALID_ENVIRONMENT, environment));
        }
    }

    public static String getSearchApiHost(String platform) {
        String environment = getEnv();
        switch (getEnvironmentType(environment)) {
            case "QA":
                if (platform.equalsIgnoreCase("web")) {
                    //Web is a special case where they use Prod across all environments
                    return SEARCH_API_HOST_INTERNAL.getValue();
                } else {
                    return SEARCH_API_HOST_QA.getValue();
                }
            case "PROD":
            case "BETA":
            default:
                return SEARCH_API_HOST.getValue();
        }
    }

    public static String getApplicationKey() {
        return APPLICATION_RESOURCE_KEY.getValue();
    }

    protected static final List<String> resourceKeys = Stream.of(APPLICATION_RESOURCE_KEY.getValue(), ACCESSIBILITY_RESOURCE_KEY.getValue(),
            SDK_ERRORS_RESOURCE_KEY.getValue(), PAYWALL_RESOURCE_KEY.getValue(), COMMERCE_RESOURCE_KEY.getValue(), PROMO_RESOURCE_KEY.getValue(),
            SEO_RESOURCE_KEY.getValue(), WELCOME_MARKETING_RESOURCE_KEY.getValue(), RATINGS_RESOURCE_KEY.getValue(), WELCH_RESOURCE_KEY.getValue(),
            PCON_RESOURCE_KEY.getValue(), SUBSCRIPTIONS_RESOURCE_KEY.getValue()).collect(Collectors.toList());

    public static List<String> getResourceKeys() {
        return resourceKeys;
    }

    public static String getRatingsKey() {
        return RATINGS_RESOURCE_KEY.getValue();
    }

    public static String getAccessibilityKey() {
        return ACCESSIBILITY_RESOURCE_KEY.getValue();
    }

    public static String getSdkErrorKey() {
        return SDK_ERRORS_RESOURCE_KEY.getValue();
    }

    public static String getSubscriptionsKey() {
        return SUBSCRIPTIONS_RESOURCE_KEY.getValue();
    }

    public static String getLanguageKey() {
        return LANGUAGE_SETTINGS_RESOURCE_KEY.getValue();
    }

    public static String getPaywallKey() {
        return PAYWALL_RESOURCE_KEY.getValue();
    }

    public static String getCommerceKey() {
        return COMMERCE_RESOURCE_KEY.getValue();
    }

    public static String getPromoKey() {
        return PROMO_RESOURCE_KEY.getValue();
    }

    public static String getSeoKey() {
        return SEO_RESOURCE_KEY.getValue();
    }

    public static String getMarketingResourceKey() {
        return WELCOME_MARKETING_RESOURCE_KEY.getValue();
    }

    public static String getDownloadHistoryServiceHost() {
        String environment = getEnv();
        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return DISNEY_DOWNLOAD_HISTORY_SERVICE_HOST_PROD.getValue();
            case "QA":
                return DISNEY_DOWNLOAD_HISTORY_SERVICE_HOST_QA.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format(INVALID_ENVIRONMENT, environment));
        }
    }

    public static String getContentBamgridHost() {
        String environment = getEnv();
        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return DISNEY_CONTENT_BAMGRID_HOST.getValue();
            case "QA":
                return DISNEY_CONTENT_BAMGRID_HOST_QA.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format(INVALID_ENVIRONMENT, environment));
        }
    }

    public static String getOfferHost() {
        String environment = getEnv();
        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return OFFER_PROD_ENVIRONMENT.getValue();
            case "QA":
                return OFFER_QA_ENVIRONMENT.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format(INVALID_ENVIRONMENT, environment));
        }
    }

    public static String getWelchResourceKey() {
        return WELCH_RESOURCE_KEY.getValue();
    }

    public static String getPconResourceKey() {
        return PCON_RESOURCE_KEY.getValue();
    }

    public static String getRipcutHost() {
        switch (getEnvironmentType(getEnv())) {
            case "QA":
                return DISNEY_QA_RIPCUT_HOST.getValue();
            default:
                return DISNEY_PROD_RIPCUT_HOST.getValue();
        }
    }

    public static Object getTaxationServiceHost() {
        String environment = getEnv();
        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return DISNEY_PROD_TAXATION_SERVICE_HOST.getValue();
            case "QA":
                return DISNEY_QA_TAXATION_SERVICE_HOST.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format(INVALID_ENVIRONMENT, environment));
        }
    }

    public static Object getStarOrderHost() {
        String environment = getEnv();
        switch (getEnvironmentType(environment)) {
            case "PROD":
            case "BETA":
                return STAR_PROD_ORDER_CALL.getValue();
            case "QA":
                return STAR_QA_ORDER_CALL.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format(INVALID_ENVIRONMENT, environment));
        }
    }

    public static String getJiraBaseUrl() {
        return JIRA_BASE_URL.getValue();
    }
}