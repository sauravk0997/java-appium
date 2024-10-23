package com.disney.qa.common.constant;

import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.api.explore.request.ExploreSearchRequest;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
    String LATAM = "LATAM";
    String EMEA = "EMEA";
    String MPAA = "MPAA";

    Map<ImmutablePair<String, String>, DisneyLocalizationUtils> LOCALIZATION_UTILS = new ConcurrentHashMap<>();
    Logger I_API_HELPER_LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
}
