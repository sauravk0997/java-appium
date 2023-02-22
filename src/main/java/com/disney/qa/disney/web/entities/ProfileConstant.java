package com.disney.qa.disney.web.entities;

public class ProfileConstant {
    private ProfileConstant(){}

    public static final String MAIN_TEST = "Test";

    public static final String PROFILE = "Profile";

    public static final String PROFILE1 = "Profile1";

    public static final String JUNIOR = "junior";
    public static final String DARTH_VADER = "DARTH VADER";

    public static final String MICKEY = "Mickey";

    public static final String DARTH_VADER_INCOMPLETE = "DARTH VADE...";

    public static final String JUNIOR_FIRST_TILE_CARD_VALUE = "Mickey ";

    public static final int MAX_PROFILES = 7;

    public static final String PRIMARY_PROFILE = "primary profile";

    private static final String [] avatarNames = {PROFILE, DARTH_VADER_INCOMPLETE};
    public static String[] getAvatarNames() {
        return avatarNames;
    }

    private static final String[] profileNames = {"Test1", "Test2", "Test3", "Test4", "Test5", "Test6"};

    public static String[] getProfileNames() {
        return profileNames;
    }
}
