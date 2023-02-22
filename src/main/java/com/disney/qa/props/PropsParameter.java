package com.disney.qa.props;

import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public enum PropsParameter {

	PROPS_PASSWORD("props_password"),
	PROPS_INITIAL("props_initial"),
	PROPS_ADMIN_EMAIL("props_admin_email"),
	PROPS_PRODUCT_EMAIL_A("props_product_email_a"),
	PROPS_PRODUCT_EMAIL_B("props_product_email_b"),
	PROPS_IAP_EMAIL("props_iap_email"),
	PROPS_APPROVER_EMAIL("props_approver_email"),
	PROPS_BASIC_EMAIL("props_basic_email");

	private CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));
	private String key;

	private PropsParameter(String key) {
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
