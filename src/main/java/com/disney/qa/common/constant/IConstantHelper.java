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

    String BACK_BUTTON_NOT_DISPLAYED = "Back button is not present";
    String DETAILS_PAGE_NOT_DISPLAYED = "Details Page is not displayed";
    String HOME_PAGE_NOT_DISPLAYED = "Home Page is not displayed";
    String MORE_MENU_NOT_DISPLAYED = "More Menu is not displayed";
    String ORIGINALS_PAGE_NOT_DISPLAYED = "Originals Page did not open";
    String SEARCH_PAGE_NOT_DISPLAYED = "Search page is not displayed";
    String WATCHLIST_PAGE_NOT_DISPLAYED = "Watchlist is not displayed";

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
