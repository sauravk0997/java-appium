package com.disney.qa.disney.web.entities;

public enum ProfileEligibility {
    ELIGIBLE_DOB("eligible dob", 18),
    INELIGIBLE_DOB("ineligible dob", 126),
    INVALID_DOB("invalid dob", 0),
    UNDER_THIRTEEN_DOB("under thirteen dob", 10),
    UNDER_EIGHTEEN_DOB("under eighteen dob", 16);

    private String eligibilityCriteria;
    private int eligibilityAge;

    ProfileEligibility(String eligibilityCriteria, int eligibilityAge) {
        this.eligibilityCriteria = eligibilityCriteria;
        this.eligibilityAge = eligibilityAge;
    }

    public String getEligibilityCriteria() {
        return this.eligibilityCriteria;
    }

    public int getEligibilityAge() {
        return this.eligibilityAge;
    }
}
