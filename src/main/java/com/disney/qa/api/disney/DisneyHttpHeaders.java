package com.disney.qa.api.disney;

import org.springframework.http.HttpHeaders;

public class DisneyHttpHeaders extends HttpHeaders {

    public static final String BAMTECH_COUNTRY = "X-BAMTech-Location";
    public static final String BAMTECH_PARTNER = "X-BAMTech-Partner";
    public static final String BAMTECH_PLATFORM = "X-BAMSDK-Platform";
    public static final String BAMTECH_PARTNER_PLATFORM = "X-BAMTech-Platform";
    public static final String BAMTECH_VERSION = "X-BAMTECH-Version";
    public static final String BAMTECH_VPN_OVERRIDE = "x-bamtech-override-vpn-detection";
    public static final String BAMTECH_GEO_OVERRIDE = "x-geo-override";
    public static final String BAMTECH_GEO_ALLOW = "x-geo-allow";
    public static final String BAMTECH_IS_TEST = "x-bamtech-is-test";
    public static final String BAMTECH_LOCATION = "x-bamtech-location";
    public static final String BAMTECH_DSS_PHYSICAL_COUNTRY_OVERRIDE = "dss-physical-country";
    public static final String BAMTECH_AKA_USER_GEO_OVERRIDE = "x-aka-user-geo";
    public static final String DISNEY_STAGING = "x-dss-staging";
    public static final String BAMTECH_REDIRECT_BYPASS = "x-redirect-bypass";
    public static final String BAMTECH_ACCOUNT_ID = "X-BAMTech-Account-Id";
    public static final String BAMTECH_PROFILE_ID = "X-BAMTECH-PROFILE-ID";
    public static final String BAMTECH_DEVICE_ID = "X-BAMTECH-DEVICE-ID";
    public static final String BAMTECH_OVERRIDE_SUPPORTED_LOCATION = "x-bamtech-override-supported-location";
    public static final String BAMTECH_CDN_BYPASS = "x-rbp-wfu";
    public static final String BAMTECH_TDA_ENABLE = "X-Mercury-Enable-Tda";
    public static final String BAMTECH_TDA_DURATION = "X-Mercury-Tda-Duration";
    public static final String BAMTECH_PURCHASE_PLATFORM = "X-BAMTech-Purchase-Platform";
    public static final String BAMTECH_BROWSER_INFO = "X-BAMTech-Browser-Info";
    public static final String BAMTECH_SALES_PLATFORM = "X-BAMTech-Sales-Platform";
    public static final String MERCURY_SOURCE = "x-mercury-source";
    public static final String BAMTECH_IDENTITY_ID = "x-bamtech-identity-id";
    public static final String BAMTECH_CANONBALL_PREVIEW = "x-cnbl-v2-preview";
    public static final String BAMTECH_WPNX_DISABLE = "X-BAMTech-wpnx-disable";
    public static final String DISNEY_DICTIONARY = "x-dmgz-dictionary";
    public static final String PREVIEW_ENVIRONMENT = "x-cnbl-v2-preview";
}
