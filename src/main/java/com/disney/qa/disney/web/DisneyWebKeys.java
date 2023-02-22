package com.disney.qa.disney.web;

import com.qaprosoft.carina.core.foundation.utils.resources.L10N;

public enum DisneyWebKeys {

    //Homepage

    BTN_BUY_NOW("btn_buy_now"),
    BTN_COMPLETE_SUBSCRIPTION("btn_complete_subscription"),
    BTN_COOKIE_BANNER("btn_cookiebanner"),
    BUNDLE_EMPHASIZED_CTA("bundle_emphasized_cta"),
    BUNDLE_SECONDARY_CTA("bundle_secondary_cta"),
    LOGIN_BTN("btn_login"),
    WELCOME_SIGNUP_BTN("btn_welcome_signup_cta"),

    //Login/Purchase Flow

    BTN_AGREE_CONTINUE("btn_agree_continue"),
    BTN_AGREE_CONTINUE_FR("btn_agree_continue_dplus-sub-fr_proxy"),
    BTN_AGREE_SUBSCRIBE("btn_agree_subscribe"),
    BTN_CONTINUE("btn_continue"),
    BTN_CONTINUE_FR("btn_agree_continue_gtou_dplus-sub-fr_ppv2_fln_proxy"),
    BTN_CHANGE_PAYMENT_METHOD("btn_change_payment_method"),
    BTN_FINISH_LATER("btn_finish_later"),
    BTN_PAYMENTMETHOD_PAYPAL("btn_paymentmethod_paypal"),
    BTN_PAYWITH("btn_paywith"),
    BTN_RESTART_SUB("btn_restartsub"),
    BTN_REVIEW_SUBSCRIPTION("btn_review_subscription"),
    BTN_START_WATCHING("btn_start_watching"),
    BTN_YES_CONTINUE("btn_yes_continue"),
    BUNDLE_BILLING_TITLE("paymentterms_DISNEY_SB3_PURCHASE_CMPGN_DISNEY_SB3_PURCHASE_VOCHR_title"),
    BUNDLE_START_STREEMING("bundle_start_streeming"),
    BUNDLE_SUCCESS_COPY("bundle_success_copy"),
    BUNDLE_SUCCESS_SUBCOPY("bundle_success_subcopy"),
    CREATE_PASSWORD_TITLE("create_password_title"),
    CONTINUE_BTN("btn_continue"),
    ENTER_EMAIL("enter_email"),
    ERROR_BIN_BLOCKED_TITLE("error_bin_blocked_title"),
    EUP_LOGIN_NOTE("eup_login_note"),
    FORGOT_PASSWORD("forgot_password"),
    FORMERROR_NOTIFYUSER_PAYMENT("formerror_notifyuser_payment"),
    FREETRIAL_FRAUD_PREVENTION_TITLE("freetrial_fraud_prevention_title"),
    HULU_DOUBLE_BILLED_TITLE("hulu_double_billed_title"),
    IDEAL_BANK_SELECTOR("formplaceholder_idealbankselector"),
    LEGAL_CENTER_TITLE("legalcenter_title"),
    ONBOARDING_STEPPER("onboarding_stepper"),
    PAYMENTPROCESSING_COPY("paymentprocessing_copy"),
    PAYMENT_TERMS_PAYPAL_DISCLAIMER_COPY("paymentterms_DISNEY_FT_CMPGN_DISNEY_FT_7D_VOCHR_billingform_paypal"),
    PAYMENT_TERMS_PAYPAL_RESTART("paymentterms_billingform_paypal_restart"),
    SIGN_UP_TITLE("sign_up_title"),
    SIGN_UP("sign_up"),
    STARZ_TAGLINE("starz_tagline"),
    STARZ_BUY_NOW("starz_buy_now"),
    SUCCESS_TITLE("success_title"),
    SUCCESS_COPY("success_copy"),

    //Early Access
    EA_STANDALONE_OFFER_PRICE("ea_standalone_offer_price"),
    EA_OFFER_PRICE("ea_offer_price"),
    EA_ACCESS_CONFIRMATION("ea_access_confirmation"),
    EA_TITLE("ea_title"),

    //Portability

    ZERO_AUTH_TITLE("zero_auth_title"),
    ZERO_AUTH_EU_COPY("zero_auth_eu_copy"),


    //Global Nav

    NAV_HOME("nav_home"),
    NAV_MOVIES("nav_movies"),
    NAV_ORIGINALS("nav_originals"),
    NAV_SEARCH("nav_search"),
    NAV_SERIES("nav_series"),
    NAV_WATCHLIST("nav_watchlist"),

    //Movies

    MOVIES_DETAILS_TAB("nav_details"),
    MOVIES_EXTRAS_TAB("nav_extras"),
    MOVIE_PLAY_BUTTON("btn_play"),
    MOVIE_NAV_BUTTON("nav_movies"),
    MOVIES_SUGGESTED_TAB("nav_related"),

    //Series

    SERIES_DETAILS_TAB("nav_details"),
    SERIES_EPISODIES_TAB("nav_episodes"),
    SERIES_EXTRAS_TAB("nav_extras"),
    SERIES_NAV_BUTTON("nav_series"),
    SERIES_SUGGESTED_TAB("nav_related"),

    //Profile Views

    ACCOUNT_ADD_BTN("create_profile_add_profile"),
    NAV_ACCOUNT("nav_account"),
    NAV_ACCOUNT_HELP("nav_help_link_text"),
    NAV_ACCOUNT_LOGOUT("nav_log_out"),

    //Account

    ACCOUNT_DETAILS("account_details"),
    ACCOUNT_EDIT_BTN("edit_profile_title"),
    ACCOUNT_LOGOUT("nav_log_out"),
    AUTOPLAY("create_profile_autoplay"),
    AUTOPLAY_SUB_COPY("autoplay_subcopy"),
    BACKGROUND_VIDEO("settings_background_video"),
    BACKGROUND_VIDEO_EXPLAINER("settings_background_video_subcopy"),
    BUNDLE_BILLING_DETAILS_MY_SERVICES("bundle_billing_details_my_services"),
    BILLING_HISTORY("billing_history"),
    BILLING_DETAILS("billing_details"),
    BTN_CANCEL("btn_cancel"),
    BTN_SUBSCRIPTION_BACKTOACCOUNT("btn_subscription_backtoaccount"),
    BTN_SUBSCRIPTION_GOBACK("btn_subscription_goback"),
    CANCEL_BTN("btn_cancel"),
    CHANGE_PASSWORD("change_password"),
    CHOOSE_ICON("chooseprofileicon_title"),
    DELETE_BTN("btn_delete"),
    DELETE_MSG("delete_profile_copy"),
    DELETE_PROFILE("btn_delete_profile"),
    DONE_BTN("btn_done"),
    EDIT_PROFILE_TITLE_2("edit_profile_title_2"),
    EDIT_PROFILE_TITLE_2_EXPLAINER("primaryprofileexplainer"),
    EMAIL_CHANGE_TITLE("email_change_title"),
    JUNIOR_PROFILE_EXPLAINER("juniorprofile_subcopy"),
    JUNIOR_PROFILE_TITLE("juniorprofile"),
    PAGE_EDIT_BTN("btn_edit_profile"),
    PROFILE_NAME_PLACEHOLDER("profile_name_placeholder"),
    SAVE_BTN("btn_save"),
    SKIP_BTN("chooseprofileicon_skip"),
    SUBSCRIPTION_CANCEL_COPY("subscription_cancel_copy"),
    SUBSCRIPTIONCANCEL_CONFIRMCANCELMSG_COPY("subscriptioncancel_confirmcancelmsg_copy"),
    SUBSCRIPTION_CHANGEPAYMENT_COPY("subscription_changepayment_copy"),
    UI_LANG_SETTING("ui_language_setting"),

    //Watchlist

    NO_SEARCH_RESULTS("no_search_results"),
    NO_SEARCH_RESULTS_COPY("no_search_results_copy");

    private final String disneyBase;

    DisneyWebKeys(String key) {
        this.disneyBase = key;
    }

    public String getText() {
        String lion = "{L10N:";
        String baseBtns = this.disneyBase;
        if (baseBtns.contains(lion)) {
            String key = baseBtns.replace(lion, "").replace("}", "");
            baseBtns = L10N.getText(key);
        }
        return baseBtns;
    }
}