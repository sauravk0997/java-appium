package com.disney.qa.disney.web.entities;

public class WebConstant {
    private WebConstant(){}

    public static final String WEB = "web";

    public static final String HULU = "hulu";

    public static final String PROD = "PROD";
    public static final String BETA = "BETA";
    public static final String QA = "QA";

    public static final String CODE = "code";
    public static final String COUNTRY = "country";
    public static final String EXP = "exp";
    public static final String CVV = "cvv";
    public static final String ZIP = "zip";
    public static final String CC = "cc";
    public static final String PROD_FTF_CC = "prod-ftf-cc";
    public static final String FTF_CC = "ftf-cc";
    public static final String PROD_CC = "prod-cc";
    public static final String ADULT_DOB = "03/03/1993";
    public static final String UNDER_18_DOB = "03/03/2013";

    public static final String US = "US";
    public static final String EN = "EN";

    public static final String COMPLETE_PURCHASE = "complete-purchase";
    public static final String DEUTSCH = "Deutsch";

    public static final String WATCHLIST = "Watchlist";

    public static final String TOGGLE_BUTTON_ACTIVE = "active";

    public static final String BUNDLE = "bundle";
    public static final String STANDARD = "standard";

    public static final String BUNDLE_LOGO_ALT_TXT = "Disney Plus bundle logo";
    public static final String STANDALONE_LOGO_ALT_TXT = "Disney Plus logo";

    public static final String AD_BADGE = "AD";

    public static final String AD_INTERACTION_BADGE = "LEARN MORE";

    public static final String WITH_ADS = "With Ads";
    public static final String NO_ADS = "No Ads";
    public static final String NO_ADS_BUNDLE = "No Ads on Disney+ & Hulu";

    public static final String REGEX_HULU_ACCOUNT_QA_URL = "hulu\\w*.com\\/\\?from=hulu-disneyplus-offer-disney";
    public static final String REGEX_HULU_ACCOUNT_URL = "hulu\\w*.com\\/\\w*";

    public static final String DISNEY_SUBSCRIPTION = "Disney+";
    public static final String DISNEY_BUNDLE_SUBSCRIPTION = "Disney Bundle";

    public static final String NAME_ON_CARD = "WebQA Automation";
    public static final String EU_NAME_ON_CARD = "3DS_V2_CHALLENGE_IDENTIFIED";
    public static final String CREDIT_CARD_PAYMENT_TYPE = "credit";
    public static final String DE_CANCEL_CONFORMATION_COPY = "Ok, your subscription has been cancelled";

    public static final String US_CANCEL_CONFORMATION_COPY = "Your Disney+ subscription has been canceled";
    public static final String ACCOUNT_PAGE_CANCEL_COPY = "Cancelled";

    public static final String IS_EU_COUNTRY = "isEuCountry";
    public static final String IS_ZIPCODE_COUNTRY = "isZipCodeCountry";

    public static final String BRANCH_AD_TIER_NOVEMBER = "ad-tier-november";
    public static final String SCROLL_VERTICAL_PIXELS = "return window.scrollY;";
    public static final String SCROLL_HORIZONTAL_PIXELS = "return window.scrollX;";
    public static final String INVALID_BIRTHDATE = "Invalid birthdate";
    public static final String NOT_ELIGIBLE_TO_USE_SERVICE = "Sorry, you're not eligible to set up an account";
    public static final String DATE_FORMAT_ONE = "MMddyyyy";
    public static final String DATE_FORMAT_TWO = "MM/dd/yyyy";
    public static final String ENTER_VALID_DOB = "Enter a valid birthdate";
    public static final String FIELD_REQUIRED_ERROR_LABEL = "This field is required";
    public static final String JUNIOR_MODE = "Junior Mode";
    public static final String AUTO_PLAY = "autoplay";
    public static final String CURATED_CONTENT = "curated content";
    public static final String LIONSGATE_UNENTITLED_TEXT_HEADLINE = "Your subscription must include LIONSGATE+ to view this.";
    public static final String LIONSGATE_UNENTITLED_TEXT = "Please come back when your Star+ subscription includes LIONSGATE+.";
    public static final String ARIA_LABEL = "aria-label";

}
