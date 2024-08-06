package com.disney.qa.common.constant;

public class RatingConstant {

    public static final String BRAZIL = "BR";
    public static final String GERMANY = "DE";
    public static final String JAPAN = "JP";
    public static final String KOREA = "KR";
    public static final String NEW_ZEALAND = "NZ";
    public static final String SINGAPORE = "SG";
    public static final String TURKEY = "TR";
    public static final String USA = "US";
    public static final String HAITI = "HT";
    public static final String MAURITIUS = "MU";
    public static final String MAYOTTE = "YT";
    public static final String REUNION = "RE";
    public static final String UNITED_KINGDOM = "GB";

    public enum Rating {
        G("G"),
        L("L"),
        GA("General Audience"),
        M("M"),
        PG("PG"),
        R21("R21"),
        ZERO("0"),
        SIX("6"),
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
                return "1850";
            default:
                throw new IllegalArgumentException(String.format("Max maturity rating for %s locale is not found", locale));
        }
    }

    public static String getRoamingDas(String locale) {
        switch (locale) {
            case "BR":
                return "23044";
            case "CA":
                return "23065";
            case "DE":
                return "23117";
            case "JP":
                return "23098";
            case "KR":
                return "23126";
            case "NZ":
                return "23150";
            case "SG":
                return "23116";
            case "TR":
                return "23144";
            case "US":
                return "23831";
            case "HT":
                return "23155";
            case "MU":
                return "23114";
            case "YT":
                return "23124";
            case "RE":
                return "23101";
            case "GB":
                return "23136";
            default:
                throw new IllegalArgumentException(String.format("Max maturity rating for %s locale is not found", locale));
        }
    }
}
