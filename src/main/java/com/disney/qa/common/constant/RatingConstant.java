package com.disney.qa.common.constant;

@SuppressWarnings({"squid:S1479"})
public class RatingConstant {

    public static final String AMERICAN_SAMOA = "AS";
    public static final String ARGENTINA = "AR";
    public static final String AUSTRALIA = "AU";
    public static final String BOLIVIA = "BO";
    public static final String BRAZIL = "BR";
    public static final String CANADA = "CA";
    public static final String CHILE = "CL";
    public static final String COLOMBIA = "CO";
    public static final String COSTA_RICA = "CR";
    public static final String DOMINICAN_REPUBLIC = "DO";
    public static final String ECUADOR = "EC";
    public static final String EL_SALVADOR = "SV";
    public static final String FRANCE = "FR";
    public static final String GERMANY = "DE";
    public static final String GUAM = "GU";
    public static final String GUATEMALA = "GT";
    public static final String HAITI = "HT";
    public static final String HONDURAS = "HN";
    public static final String JAPAN = "JP";
    public static final String KOREA = "KR";
    public static final String MARSHALL_ISLANDS = "MH";
    public static final String MAURITIUS = "MU";
    public static final String MAYOTTE = "YT";
    public static final String MEXICO = "MX";
    public static final String NETHERLANDS = "NL";
    public static final String NEW_ZEALAND = "NZ";
    public static final String NICARAGUA = "NI";
    public static final String NORTHERN_MARINA_ISLANDS = "MP";
    public static final String PANAMA = "PA";
    public static final String PARAGUAY = "PY";
    public static final String PERU = "PE";
    public static final String PUERTO_RICO = "PR";
    public static final String REUNION = "RE";
    public static final String SINGAPORE = "SG";
    public static final String SPAIN = "ES";
    public static final String SWEDEN = "SE";
    public static final String TURKEY = "TR";
    public static final String URUGUAY = "UY";
    public static final String UNITED_KINGDOM = "GB";
    public static final String UNITED_STATES = "US";
    public static final String UNITED_STATES_OUTLYING_ISLANDS = "UM";
    public static final String UNITED_STATES_VIRGIN_ISLANDS = "VI";

    public enum Rating {
        AL("AL"),
        G("G"),
        GA("General Audience"),
        L("L"),
        M("M"),
        M18("M18"),
        NC16("NC16"),
        PG("PG"),
        PG13("PG13"),
        PG_13("PG-13"),
        R15("R15"),
        R16("R16"),
        R18("R18"),
        R21("R21"),
        RP13("RP13"),
        RP16("RP16"),
        RP18("RP18"),
        RESTRICTED("R"),
        TV_14("TV-14"),
        TV_G("TV-G"),
        TV_PG("TV-PG"),
        TV_MA("TV-MA"),
        TV_Y("TV-Y"),
        TV_Y7("TV-Y7"),
        TV_Y7_FV("TV-Y7-FV"),
        ZERO("0"),
        SIX("6"),
        NINE("9"),
        TEN("10"),
        TWELVE("12"),
        THIRTEEN("13"),
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
        NINETEEN_PLUS("19+"),
        MA15_PLUS("MA15+"),
        R18_PLUS("R18+");

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
            case "AS":
            case "GU":
            case "MH":
            case "MP":
            case "PR":
            case "UM":
            case "VI":
                return "1830";
            case "AU":
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
            case "ES":
            case "SE":
                return "1850";
            default:
                throw new IllegalArgumentException(String.format("Max maturity rating for %s locale is not found", locale));
        }
    }

    public static String getRoamingDas(String locale) {
        switch (locale) {
            case "AU":
                return "23151";
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
                return "1";
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
            case "AR":
                return "23034";
            case "BO":
                return "23042";
            case "CL":
                return "23028";
            case "CO":
                return "23040";
            case "CR":
                return "23033";
            case "DO":
                return "23038";
            case "EC":
                return "23039";
            case "SV":
                return "23043";
            case "GT":
                return "23031";
            case "HN":
                return "23041";
            case "MX":
                return "23045";
            case "NI":
                return "23035";
            case "PA":
                return "23029";
            case "PY":
                return "23036";
            case "PE":
                return "23032";
            case "UY":
                return "23030";
            case "NL":
                return "23104";
            case "MH":
                return "23061";
            case "PR":
                return "23062";
            case "MP":
                return "23063";
            case "VI":
                return "23064";
            case "AS":
                return "23066";
            case "GU":
                return "23067";
            case "UM":
                return "23068";
            case "ES":
                return "23105";
            case "SE":
                return "23113";
            default:
                throw new IllegalArgumentException(String.format("Max maturity rating for %s locale is not found", locale));
        }
    }
}
