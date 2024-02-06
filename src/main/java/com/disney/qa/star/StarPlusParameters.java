package com.disney.qa.star;

import com.zebrunner.carina.utils.R;

public enum StarPlusParameters {

    //Stage
    STAR_PLUS_STAGE_WEB("star_plus_stage_web"),

    //Star
    STAR_PROD_WEB("star_plus_prod_web"),
    STAR_LOCAL_WEB("star_plus_local_web"),
    STAR_PREVIEW_WEB("star_plus_preview_web"),

    //API
    STAR_EDGE_DUST_PROD("star_edge_dust_prod_service"),
    STAR_EDGE_DUST_QA("star_edge_dust_qa_service"),
    STAR_NAMESPACE_ID("star_namespace_id");

    private final String key;

    StarPlusParameters(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return R.TESTDATA.get(this.key);
    }
}
