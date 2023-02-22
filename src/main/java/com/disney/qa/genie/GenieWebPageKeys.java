package com.disney.qa.genie;

public enum GenieWebPageKeys {
    //Entitlement page
    PREMIERE_ACCESS("Premiere_Access"),
    DISNEY_PLUS_STANDALONE("Disney_Plus_Standalone"),
    ESPN_PLUS_STANDALONE("Espn_Plus_Standalone"),
    ESPN_PLUS_ADDON("Espn_Plus_Addon"),
    HULU("Hulu"),
    PPV("PPV"),
    STAR_PLUS_STANDALONE("Star_Plus_Standalone"),

    //Product page
    STANDALONE("Standalone"),
    BUNDLE("Bundle"),
    PREMIER_ACCESS("PREMIER_ACCESS"),
    PAY_PER_VIEW("PPV"),
    SEASONAL("Seasonal"),

    //SKUs page
    XBOX("XBOX"),
    WEB("WEB"),
    VIZIO("VIZIO"),
    UWP("UWP"),
    SONY("SONY"),
    SAMSUNG_TIZEN("SAMSUNG_TIZEN"),
    PORTAL_TV("PORTAL_TV"),
    PORTAL_TOUCH("PORTAL_TOUCH"),
    OCULUS("OCULUS"),
    LG("LG"),
    COMCAST_XCLASS_TV("COMCAST_XCLASS_TV"),
    COMCAST_VERIFIABLE("COMCAST_VERIFIABLE"),
    COMCAST_US_VERIFIABLE("COMCAST_US_VERIFIABLE"),
    ANDROID_TV("ANDROID_TV"),
    ANDROID_MOBILE("ANDROID_MOBILE"),
    PPARTNER("3PP_PARTNER"),

    US("United States of America");

    private String key;

    GenieWebPageKeys(String key){
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }
}
