package com.disney.qa.disney.web.entities;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanCardTypes {

    @Getter
    public enum PlanSelectCard {
        DISNEY_BUNDLE_HULU_WITH_ADS("Duo Basic", WebConstant.WITH_ADS, SubscriptionPlan.DISNEY_BUNDLE_2P.getPrice(), WebConstant.BUNDLE_LOGO_ALT_TXT, "plan-bundle-hulu-with-ads"),
        DISNEY_BUNDLE_WITH_ADS("Trio Basic", WebConstant.WITH_ADS, SubscriptionPlan.DISNEY_BUNDLE_TRIO_BASIC.getPrice(), WebConstant.BUNDLE_LOGO_ALT_TXT, "plan-bundle-with-ads"),
        DISNEY_BUNDLE_NO_ADS("Trio Premium", WebConstant.NO_ADS_BUNDLE, SubscriptionPlan.DISNEY_BUNDLE_TRIO_PREMIUM.getPrice(), WebConstant.BUNDLE_LOGO_ALT_TXT, "plan-bundle-no-ads"),
        DISNEY_PLUS_WITH_ADS("Basic", WebConstant.WITH_ADS, SubscriptionPlan.DISNEY_PLUS_BASIC.getPrice(), WebConstant.STANDALONE_LOGO_ALT_TXT, "plan-standalone-with-ads"),
        DISNEY_PLUS_WITH_ADS_PROMO("Basic", WebConstant.WITH_ADS, SubscriptionPlan.DISNEY_PLUS_BASIC_PROMO.getPrice(), WebConstant.STANDALONE_LOGO_ALT_TXT, "plan-standalone-with-ads"),
        DISNEY_PLUS_NO_ADS("Premium", WebConstant.NO_ADS, SubscriptionPlan.DISNEY_PLUS_PREMIUM.getPrice(), WebConstant.STANDALONE_LOGO_ALT_TXT, "plan-standalone-no-ads");

        private String planTitle;
        private String planSubCopy;
        private String planPrice;
        private String planLogoAltTxt;
        private String planDataTestId;

        PlanSelectCard(String planTitle, String planSubCopy, String planPrice, String planLogoAltTxt, String planDataTestId) {
            this.planTitle = planTitle;
            this.planSubCopy = planSubCopy;
            this.planPrice = planPrice;
            this.planLogoAltTxt = planLogoAltTxt;
            this.planDataTestId = planDataTestId;
        }
    }

    @Getter
    public enum PlanSwitchCard {
        DISNEY_BUNDLE_TRIO_BASIC("Disney Bundle Trio Basic", "Disney+, Hulu, and ESPN+, all with ads", SubscriptionPlan.DISNEY_BUNDLE_TRIO_BASIC.getPrice().substring(0, SubscriptionPlan.DISNEY_BUNDLE_TRIO_BASIC.getPrice().indexOf('/')), "fec06302-b2f6-356a-8516-2b1819147313"),
        DISNEY_BUNDLE_LEGACY("Legacy Disney Bundle", "Disney+ with no ads, Hulu and ESPN+ with ads", SubscriptionPlan.DISNEY_BUNDLE_2P.getPrice(), "9dacbf9b-7ec0-3172-a031-4756b6fe8e39"),
        DISNEY_BUNDLE_PREMIUM("Disney Bundle Trio Premium", "Disney+ and Hulu with no ads, ESPN+ with ads",  SubscriptionPlan.DISNEY_BUNDLE_TRIO_PREMIUM.getPrice().substring(0, SubscriptionPlan.DISNEY_BUNDLE_TRIO_PREMIUM.getPrice().indexOf('/')), "d93ddd9f-c008-3264-bd2a-208e3a0ef3fc"),
        DISNEY_PLUS_BASIC("Disney+ Basic", "Disney+ with ads at a lower price", SubscriptionPlan.DISNEY_PLUS_BASIC.getPrice().substring(0, SubscriptionPlan.DISNEY_PLUS_BASIC.getPrice().indexOf('/')), "cd52b21c-6403-4de1-975c-b2609ae7b11e"),
        DISNEY_PLUS_PREMIUM("Disney+ Premium Monthly", "Disney+ with no ads", SubscriptionPlan.DISNEY_PLUS_PREMIUM.getPrice().substring(0, SubscriptionPlan.DISNEY_PLUS_PREMIUM.getPrice().indexOf('/')), "8c7e06f0-8f53-3750-9ed5-a7b0447c3f1d"),
        DISNEY_PLUS_PREMIUM_YEARLY("Disney+ Premium Yearly", "Disney+ with no ads. Switch to an annual subscription and save!", SubscriptionPlan.DISNEY_PLUS_PREMIUM_YEARLY.getPrice().substring(0, SubscriptionPlan.DISNEY_PLUS_PREMIUM_YEARLY.getPrice().indexOf('/')), "9c84e417-d496-3d87-8f9f-16dfd13ea1a6");

        private String planTitle;
        private String planDescription;
        private String planPrice;
        private String planDataTestId;

        PlanSwitchCard(String planTitle, String planDescription, String planPrice, String planDataTestId) {
            this.planTitle = planTitle;
            this.planDescription = planDescription;
            this.planPrice = planPrice;
            this.planDataTestId = planDataTestId;
        }
    }

    @Getter
    public enum PlanCancelSwitchCard {
        DISNEY_BUNDLE_TRIO_BASIC("Disney Bundle Trio Basic", "Stream endless entertainment on Disney+ and Hulu, plus live sports and Originals on ESPN+", "Disney+, Hulu, and ESPN+, all with ads", SubscriptionPlan.DISNEY_BUNDLE_TRIO_BASIC.getPrice(), "plan-card-disney_bundle"),
        DISNEY_BUNDLE_LEGACY("Legacy Disney Bundle", "", "Disney+ with no ads, Hulu and ESPN+ with ads", SubscriptionPlan.DISNEY_BUNDLE_2P.getPrice(), ""),
        DISNEY_BUNDLE_PREMIUM("Disney Bundle Trio Premium", "", "Disney+ and Hulu with no ads, ESPN+ with ads", SubscriptionPlan.DISNEY_BUNDLE_TRIO_PREMIUM.getPrice(), ""),
        DISNEY_PLUS_BASIC("Disney+ Basic", "Home of Disney, Pixar, Marvel, Star Wars, and National Geographic", "Disney+ with ads at a lower price", SubscriptionPlan.DISNEY_PLUS_BASIC.getPrice(), "plan-card-disney_standalone"),
        DISNEY_PLUS_PREMIUM("Disney+ Premium", "Home of Disney, Pixar, Marvel, Star Wars, and National Geographic", "Disney+ with no ads", SubscriptionPlan.DISNEY_PLUS_PREMIUM.getPrice(), "plan-card-disney_standalone"),
        DISNEY_PLUS_PREMIUM_YEARLY("Disney+ Premium Yearly", "", "Disney+ with no ads. Switch to an annual subscription and save!", SubscriptionPlan.DISNEY_PLUS_PREMIUM_YEARLY.getPrice(), "");

        private String planTitle;
        private String planSummary;
        private String planDescription;
        private String planPrice;
        private String planDataTestId;

        PlanCancelSwitchCard(String planTitle, String planSummary, String planDescription, String planPrice, String planDataTestId) {
            this.planTitle = planTitle;
            this.planSummary = planSummary;
            this.planDescription = planDescription;
            this.planPrice = planPrice;
            this.planDataTestId = planDataTestId;
        }

        public List<String> getCancelSwitchDescription() {
            return new ArrayList<>(Arrays.asList(planSummary, planDescription));
        }
    }

    @Getter
    public enum SubscriptionPlan {
        DISNEY_PLUS_BASIC("$7.99/month", "Disney+ Basic (Monthly)"),
        DISNEY_PLUS_BASIC_PROMO("$6.99/month", "Disney+ Basic (Monthly)"),
        DISNEY_PLUS_PREMIUM("$10.99/month", "Disney+ Premium (Monthly)"),
        DISNEY_PLUS_PREMIUM_YEARLY("$109.99/year", "Disney+ Premium (Annual)"),
        DISNEY_BUNDLE_TRIO_BASIC("$12.99/month", "Disney Bundle Trio Basic (Monthly)"),
        DISNEY_BUNDLE_TRIO_PREMIUM("$19.99/month", "Disney Bundle Trio Premium (Monthly)"),
        DISNEY_BUNDLE_2P("$9.99/month", "Legacy Disney Bundle (Monthly)");

        private String price;
        private String planName;

        SubscriptionPlan(String price, String planName) {
            this.price = price;
            this.planName = planName;
        }
    }
}
