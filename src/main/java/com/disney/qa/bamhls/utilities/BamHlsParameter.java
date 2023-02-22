package com.disney.qa.bamhls.utilities;

import com.qaprosoft.carina.core.foundation.utils.R;

public enum BamHlsParameter {

    BAMHLS_DEVELOP_URL("bamhls_develop_url"),
    BAMHLS_MASTER_URL("bamhls_master_url");

    private String key;

    BamHlsParameter(String key) { this.key = key;}

    public String getKey() {return this.key; }

    public String getValue() { return R.TESTDATA.get(this.key); }
}
