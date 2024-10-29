package com.disney.qa.common.constant;

import com.disney.qa.api.explore.request.ExploreSearchRequest;

import java.util.EnumSet;

public interface IConstantHelper {
    EnumSet<ExploreSearchRequest.ContentEntitlement> CONTENT_ENTITLEMENT_DISNEY =
            EnumSet.of(ExploreSearchRequest.ContentEntitlement.DISNEY_PLUS_BASE);
    EnumSet<ExploreSearchRequest.ContentEntitlement> CONTENT_ENTITLEMENT_HULU =
            EnumSet.of(ExploreSearchRequest.ContentEntitlement.HULU_BASE);
    public static final String JARVIS_APP_CONFIG = "App Config";
    public static final String JARVIS_APP_EDIT_CONFIG = "Edit Config";
    public static final String JARVIS_APP_PLATFORM_CONFIG = "platformConfig";
    public static final String JARVIS_APP_ONE_TRUST_CONFIG = "oneTrustConfig";
    public static final String JARVIS_APP_IS_ENABLED = "isEnabled";
    public static final String JARVIS_NO_OVERRIDE_IN_USE = "Override in use! Set to: false";

    //country codes
    String AT = "AT";
    String AU = "AU";
    String BR = "BR";
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
}
