package com.disney.qa.nhl;

import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public enum NhlParameter {
	NHL_CURRENT_SEASON("nhl_current_season");

	private CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));

	private String key;

	String username = System.getenv("nhl_android_default_user");
	String passwd = System.getenv("nhl_android_default_password");

	private NhlParameter(String key){
		this.key = key;
	}

	public String getKey(){
		return this.key;
	}

	public String getValue() {
		return getJenkinsValue();
	}

	public String getDecryptedValue() {
		return cryptoTool.decrypt(getValue());
	}

	public String getJenkinsValue() {
		String ret;
		switch (this.key) {
			case "nhl_android_default_user":
				ret = username;
				break;
			case "nhl_android_default_password":
				ret = passwd;
				break;
			default:
				ret = "";
		}
		if ((ret == null) || (ret.isEmpty())) {
			ret = R.TESTDATA.get(this.key);
		}
		return ret;
	}

}
