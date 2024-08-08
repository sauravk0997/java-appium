package com.disney.qa.common.constant;

import lombok.Data;

import java.util.List;

public class RatingConstant {

    public static final String ARGENTINA = "AR";
    public static final String BOLIVIA = "BO";
    public static final String BRAZIL = "BR";
    public static final String CHILE = "CL";
    public static final String COLOMBIA = "CO";
    public static final String COSTA_RICA = "CR";
    public static final String DOMINICAN_REPUBLIC = "DO";
    public static final String ECUADOR = "EC";
    public static final String EL_SALVADOR = "SV";
    public static final String GERMANY = "DE";
    public static final String GUATEMALA = "GT";
    public static final String HAITI = "HT";
    public static final String HONDURAS = "HN";
    public static final String JAPAN = "JP";
    public static final String KOREA = "KR";
    public static final String MAURITIUS = "MU";
    public static final String MAYOTTE = "YT";
    public static final String MEXICO = "MX";
    public static final String NETHERLANDS = "NL";
    public static final String NEW_ZEALAND = "NZ";
    public static final String NICARAGUA = "NI";
    public static final String PANAMA = "PA";
    public static final String PARAGUAY = "PY";
    public static final String PERU = "PE";
    public static final String REUNION = "RE";
    public static final String SINGAPORE = "SG";
    public static final String TURKEY = "TR";
    public static final String URUGUAY = "UY";
    public static final String UNITED_KINGDOM = "GB";

    public enum Rating {
        AL("AL"),
        G("G"),
        L("L"),
        GA("General Audience"),
        M("M"),
        PG("PG"),
        R21("R21"),
        ZERO("0"),
        SIX("6"),
        NINE("9"),
        TEN("10"),
        TWELVE("12"),
        FOURTEEN("14"),
        SIXTEEN("16"),
        EIGHTEEN("18"),
        ZERO_PLUS("0+"),
        SIX_PLUS("6+"),
        SEVEN_PLUS("7+"),
        NINE_PLUS("9+"),
        TEN_PLUS("10+"),
        TWELVE_PLUS("12+"),
        THIRTEEN_PLUS("13+"),
        FOURTEEN_PLUS("14+"),
        FIFTEEN_PLUS("15+"),
        SIXTEEN_PLUS("16+"),
        EIGHTEEN_PLUS("18+"),
        NINETEEN_PLUS("19+");

        private final String contentRating;

        Rating(String rating) {
            this.contentRating = rating;
        }

        public String getContentRating() {
            return contentRating;
        }
    }

    public static String getMaxMaturityRating(String locale) {
        switch (locale) {
            case "US":
            case "CA":
                return "1830";
            case "KR":
            case "SG":
            case "NZ":
                return "1870";
            case "BR":
            case "NL":
            case "JP":
            case "DE":
            case "TR":
            case "HT":
            case "MU":
            case "YT":
            case "RE":
            case "GB":
            case "AR":
            case "BO":
            case "CL":
            case "CO":
            case "CR":
            case "DO":
            case "EC":
            case "SV":
            case "GT":
            case "HN":
            case "MX":
            case "NI":
            case "PA":
            case "PY":
            case "PE":
            case "UY":
                return "1850";
            default:
                throw new IllegalArgumentException(String.format("Max maturity rating for %s locale is not found", locale));
        }
    }
}
