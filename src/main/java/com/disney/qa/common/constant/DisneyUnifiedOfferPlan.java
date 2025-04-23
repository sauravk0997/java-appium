package com.disney.qa.common.constant;

public enum DisneyUnifiedOfferPlan {
    //US disney plans
    DISNEY_PLUS_PREMIUM("Disney+ Premium"),
    DISNEY_PLUS_PREMIUM_YEARLY("Disney+ Premium Yearly"),
    DISNEY_BASIC_MONTHLY("Disney+ Basic - 9.99 USD - Monthly"),
    DISNEY_BUNDLE_TRIO_BASIC("Disney Bundle Trio Basic"),
    DISNEY_BUNDLE_TRIO_PREMIUM_MONTHLY("Disney Bundle Trio Premium - 26.99 USD - Monthly"),
    DISNEY_PREMIUM_MONTHLY_AMAZON("Disney Plus Premium - Monthly - Amazon"),
    DISNEY_PREMIUM_MONTHLY_ROKU("Promotion - Disney+ Premium Monthly - 13.99 USD - 7 Day FT Roku ISU"),

    //Disney plans in non US countries
    DISNEY_PLUS_STANDARD("Disney+ Standard"),
    DISNEY_PLUS_STANDARD_WITH_ADS_NON_US("Disney+ Standard with Ads"),
    DISNEY_PLUS_PREMIUM_MONTHLY("Disney+ Premium Monthly"),
    DISNEY_PREMIUM_MONTHLY_CANADA("Disney+ Standard - 129.99 CAD - Yearly - Signup"),
    DISNEY_PREMIUM_MONTHLY_SINGAPORE("Disney+ Premium - 18.98 SGD - Monthly - Signup"),
    DISNEY_PREMIUM_YEARLY_NETHERLANDS("Disney+ Premium - P2 - 139.90 EUR - Yearly - Signup"),
    DISNEY_PREMIUM_YEARLY_TURKEY("Disney+ Without Ads Annual Sign-Up"),
    DISNEY_EXTRA_MEMBER_ADD_ON("Extra Member Add-On"),
    DISNEY_PLUS_PREMIUM_YEARLY_DE("Disney+ Premium - DE - 139.90 EUR - Yearly"),
    DISNEY_PLUS_PREMIUM_YEARLY_AT("Disney+ Premium - 139.90 EUR - Yearly - Signup"),
    DISNEY_PLUS_PREMIUM_YEARLY_CH("Disney+ Premium - 209.00 CHF - P1 - Yearly - Signup");

    private final String key;

    DisneyUnifiedOfferPlan(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.key;
    }
}
