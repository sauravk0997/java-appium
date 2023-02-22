package com.disney.qa.genie;

import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public enum GenieParameter {

    DISNEY_GENIE_DEV_WEB("disney_genie_dev_web"),
    DISNEY_GENIE_QA_WEB("disney_genie_qa_web"),
    GENIE_ADMIN_EMAIL("genie_admin_email"),
    GENIE_ADMIN_PASSWORD("genie_admin_password");

    private CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));
    private String key;

    GenieParameter(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public String getValue() {
        return R.TESTDATA.get(this.key);
    }

    public String getDecryptedValue() {
        return cryptoTool.decrypt(getValue());
    }
}


