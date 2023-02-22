package com.disney.qa.api.dgi.dust;

@SuppressWarnings("squid:S3066")
public enum DustPageKeys {

    DETAILS(""), //Set in test
    EXPLORE("explore"),
    HOME("home"),
    LOG_IN_ENTER_EMAIL("log_in__enter_email"),
    LOG_IN_ENTER_PASSWORD("log_in__enter_password"),
    PIXAR("pixar"),
    VIDEO_PLAYER("video_player"),
    WELCOME("welcome");

    private String pageKeys;

    DustPageKeys(String pageKeys){
        this.pageKeys = pageKeys;
    }

    public void setPageKey(String pageKeys) {
        this.pageKeys = pageKeys;
    }

    public String getPageKey(){
        return pageKeys;
    }
}
