package com.disney.qa.espn;

import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

/**
 * ESPN - ESPN Parameter page
 *
 * @author bzayats
 */
public enum EspnParameter {

    ESPN_DEFAULT_USER("espn_base_plus_mlb_upsell_mobile_user"),
    ESPN_PLUS_WATCH_API_HOST("espn_watch_api_host"),
    ESPN_PLUS_WATCH_API_HOST_QA("espn_watch_api_host_qa"),
    ESPN_BASE_E_USER("espn_base_plus_mobile_user"),
    ESPN_NO_ENTITLEMENTS_USER("espn_no_entitlements_mobile_user"),
    ESPN_DEFAULT_PASSWORD("espn_plus_mobile_pass"),
    ESPN_QE_USER("espn_mobile_qe_user"),
    ESPN_QE_PASS("espn_mobile_qe_pass");

    private CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));
    private String key;

    String userName = System.getenv("espn_base_plus_mlb_upsell_mobile_user");
    String passwd = System.getenv("espn_plus_mobile_pass");
    String userNameBaseE = System.getenv("espn_base_plus_mobile_user");
    String userNameNoEntitlements = System.getenv("espn_no_entitlements_mobile_user");
    String userNameQE = System.getenv("espn_mobile_qe_user");

    private EspnParameter(String key){
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }

    public String getValue() {
        return getJenkinsValue();
    }

    public String getJenkinsValue() {
        String ret;
        switch (this.key) {
            case "espn_base_plus_mlb_upsell_mobile_user":
                ret = userName;
                break;
            case "espn_plus_mobile_pass":
                ret = passwd;
                break;
            case "espn_no_entitlements_mobile_user":
                ret = userNameNoEntitlements;
                break;
            case "espn_base_plus_mobile_user":
                ret = userNameBaseE;
                break;
            case "espn_mobile_qe_user":
                ret = userNameQE;
                break;
            default:
                ret = "";
        }
        if ((ret == null) || (ret.isEmpty())) {
            ret = R.TESTDATA.get(this.key);
        }
        return ret;
    }

    public String getDecryptedValue() {
        return cryptoTool.decrypt(getValue());
    }

    public static String getEspnApiHost() {
        String environment = R.CONFIG.get("env");
        String host;
        switch (environment) {
            case "QA":
                host = ESPN_PLUS_WATCH_API_HOST_QA.getValue();
                break;
            case "PROD":
                host = ESPN_PLUS_WATCH_API_HOST.getValue();
                break;
            case "ESPN Adhoc - sc-release-6.0":
                host = ESPN_PLUS_WATCH_API_HOST.getValue();
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is invalid environment parameter for ESPN API tests", environment));
        }
        return host;
    }

}
