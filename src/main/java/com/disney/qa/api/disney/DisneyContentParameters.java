package com.disney.qa.api.disney;

public enum DisneyContentParameters {

    SERVICE_CONTENT_FORK("/svc/content"),
    STANDARD_COLLECTION_PATH("/Collection/StandardCollection/version/%s"),
    REGIONAL_CURATED_SET_ENDPOINT("/CuratedSet/version/5.0/region/%s/audience/false/maturity/1899/language/%s/setId/%s/pageSize/60/page/1"),
    REGIONAL_SLUG_ENDPOINT("/region/%s/audience/false/maturity/1899/language/%s/contentClass/%s/slug/%s");

    private String key;
    DisneyContentParameters(String key) {
        this.key = key;
    }

    private String getValue() {
        return this.key;
    }

    public static String getServiceContentFork(){
        return SERVICE_CONTENT_FORK.getValue();
    }

    public static String getStandardCollectionPath(){
        return STANDARD_COLLECTION_PATH.getValue();
    }

    public static String getRegionSlugPath(){
        return REGIONAL_SLUG_ENDPOINT.getValue();
    }

    public static String getRegionalCuratedSetPath(){
        return REGIONAL_CURATED_SET_ENDPOINT.getValue();
    }
}
