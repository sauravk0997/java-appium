package com.disney.qa.common.constant;

import com.disney.qa.api.explore.request.ExploreSearchRequest;

import java.util.EnumSet;

public interface IConstantHelper {
    EnumSet<ExploreSearchRequest.ContentEntitlement> CONTENT_ENTITLEMENT_DISNEY =
            EnumSet.of(ExploreSearchRequest.ContentEntitlement.DISNEY_PLUS_BASE);
    EnumSet<ExploreSearchRequest.ContentEntitlement> CONTENT_ENTITLEMENT_HULU =
            EnumSet.of(ExploreSearchRequest.ContentEntitlement.HULU_BASE);
    String JARVIS_APP_CONFIG = "App Config";
    String JARVIS_APP_EDIT_CONFIG = "Edit Config";
    String JARVIS_APP_PLATFORM_CONFIG = "platformConfig";
    String JARVIS_APP_ONE_TRUST_CONFIG = "oneTrustConfig";
    String JARVIS_APP_IS_ENABLED = "isEnabled";
    String JARVIS_NO_OVERRIDE_IN_USE = "Override in use! Set to: false";
    String JARVIS_OVERRIDE_IN_USE = "Override in use! Set to: true";
    String JARVIS_NO_OVERRIDE_IN_USE_TEXT = "NO override in use!";

    String SUBSCRIPTION_ID_KEY = "id";
    String SUBSCRIPTION_PRODUCT_KEY = "product";
    String SUBSCRIPTION_PRODUCT_SKU_KEY = "sku";
    String UNIFIED_ORDER = "UNIFIED-ORDER";

    //Common Error Messages
    String ARTWORK_IMAGE_NOT_DISPLAYED = "Artwork Image is not displayed";
    String BACK_BUTTON_NOT_DISPLAYED = "Back button is not present";
    String CONTINUE_BTN_NOT_DISPLAYED = "Continue Button is not displayed";
    String DETAILS_CONTENT_DESCRIPTION_NOT_DISPLAYED = "Details Content Description is not displayed";
    String DETAILS_EPISODE_TITLE_NOT_DISPLAYED = "Details Episode Title is not displayed";
    String DETAILS_PAGE_NOT_DISPLAYED = "Details Page is not displayed";
    String DETAILS_TAB_NOT_DISPLAYED = "Details Tab is not displayed";
    String DOWNLOADS_PAGE_NOT_DISPLAYED = "Downloads Page is not displayed";
    String EPISODE_TAB_NOT_DISPLAYED = "Episode Tab is not displayed";
    String EXTRAS_TAB_NOT_DISPLAYED = "Extras Tab is not displayed";
    String MEDIA_TITLE_NOT_DISPLAYED = "Media Title is not displayed";
    String HOME_PAGE_NOT_DISPLAYED = "Home Page is not displayed";
    String MORE_MENU_NOT_DISPLAYED = "More Menu is not displayed";
    String ORIGINALS_PAGE_NOT_DISPLAYED = "Originals Page did not open";
    String PROGRESS_BAR_NOT_DISPLAYED = "Progress Bar is not displayed";
    String RESTART_BTN_NOT_DISPLAYED = "Restart Button is not displayed";
    String SEARCH_PAGE_NOT_DISPLAYED = "Search page is not displayed";
    String SHARE_BTN_NOT_DISPLAYED = "Share Button is not displayed";
    String STUDIOS_AND_NETWORKS_NOT_DISPLAYED = "Studio And Networks is not displayed";
    String SUGGESTED_TAB_NOT_DISPLAYED = "Suggested Tab is not displayed";
    String TRAILER_BTN_NOT_DISPLAYED = "Trailer Button is not displayed";
    String VIDEO_PLAYER_NOT_DISPLAYED = "Video Player is not displayed";
    String WATCHLIST_BTN_NOT_DISPLAYED = "Watchlist button is not displayed";
    String WATCHLIST_PAGE_NOT_DISPLAYED = "Watchlist is not displayed";
    String WELCOME_SCREEN_NOT_DISPLAYED = "Welcome screen did not launch";
    String WHOS_WATCHING_NOT_DISPLAYED = "Who's Watching Page is not displayed";

    //Profile Names
    String PROFILE_NAME_SECONDARY = "Secondary";

    //Profile Avatars
    String AVATAR_BUZZ = "a3860164-54df-5722-864b-54612e3b0adf";
    String AVATAR_CHILD = "a7ca5c71-68f8-5b46-95e0-c3c15a89bde0";
    String AVATAR_DIFFERENT = "d7a334d2-035c-5b18-a4dd-ac11f2448c95";
    String AVATAR_ID = "e995032f-630e-54c6-9520-6f0959152115";
    String AVATAR_WITCH = "daef63d8-da01-52c0-a631-4fce48ccfd74";

    //DOB
    String DOB_1990 = "1990-01-01";

    //Content
    String SERIES_LOKI = "Loki";

    //country codes
    String AT = "AT";
    String AU = "AU";
    String BR = "BR";
    String CA = "CA";
    String CH = "CH";
    String DE = "DE";
    String JP = "JP";
    String KR = "KR";
    String NL = "NL";
    String NZ = "NZ";
    String SG = "SG";
    String TR = "TR";
    String US = "US";

    //language codes
    String EN_LANG = "en";
    String ES_LANG = "es";
    String DE_LANG = "de";
    String FR_LANG = "fr";
    String JA_LANG = "ja";
    String KO_LANG = "ko";
    String PT_LANG = "pt";
    String TR_LANG = "tr";

    //device
    String DEVICE_TYPE_TVOS = "tvOS";
    String PHONE = "Phone";
    String TABLET = "Tablet";

    //capabilities
    String CAPABILITIES_DEVICE_NAME = "capabilities.deviceName";

    //element attributes
    String LABEL = "label";
}
