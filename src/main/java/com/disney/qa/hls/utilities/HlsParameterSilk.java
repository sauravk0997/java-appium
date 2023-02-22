package com.disney.qa.hls.utilities;

import com.qaprosoft.carina.core.foundation.utils.R;

public enum HlsParameterSilk {

    /** Base URL Test Harness **/

    HLS_DEV_MASTER_SILK_URL("hlsplayer_master_silk_url");

    private String key;

    HlsParameterSilk(String key) { this.key = key;}

    public String getKey() {return this.key; }

    public String getValue() { return R.TESTDATA.get(this.key); }

}