package com.disney.qa.disney.web.entities;

public class Contents {
    private Contents(){}

    public static final String HITCHCOCK = "Hitchcock";
    public static final String AD_SVOD ="Limitless with Chris Hemsworth";
    public static final String FREE_GUY = "Free Guy";
    public static final String MANDALORIAN = "Mandalorian";
    public static final String FROZEN_FEVER ="Frozen Fever";
    public static final String THE_SIMPSONS ="The Simpsons";
    public static final String TURNING_RED ="Turning Red";
    public static final String NON_DISNEY_TITLE ="Dark Knight";
    public static final String POWER ="Power";


    private static final String[] WATCHLIST_CONTENT = {THE_SIMPSONS, FREE_GUY};
    public static String[] getWatchlistContent() {
        return WATCHLIST_CONTENT;
    }
}
