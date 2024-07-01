package com.disney.qa.common.constant;

public class RatingConstant {

    public static final String JAPAN = "JP";
    public static final String KOREA = "KR";
    public static final String SINGAPORE = "SG";
    public static final String TURKEY = "TR";
    public static final String USA = "US";

    public enum Rating {
        G("G"),
        GA("General Audience"),
        PG("PG"),
        R21("R21"),
        SEVEN_PLUS("7+"),
        TWELVE_PLUS("12+"),
        THIRTEEN_PLUS("13+"),
        FIFTEEN_PLUS("15+"),
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
                return "1870";
            case "NL":
            case "JP":
            case "DE":
            case "TR":
                return "1850";
            default:
                throw new IllegalArgumentException(String.format("Max maturity rating for %s locale is not found", locale));
        }
    }

    public static String getRoamingDas(String locale) {
        switch (locale) {
            case "CA":
                return "23065";
            case "DE":
                return "23117";
            case "JP":
                return "23098";
            case "KR":
                return "23126";
            case "SG":
                return "23116";
            case "TR":
                return "23144";
            case "US":
                return "23831";
            default:
                throw new IllegalArgumentException(String.format("Max maturity rating for %s locale is not found", locale));
        }
    }
}
