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
}
