package com.disney.qa.espn.web;

import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public enum EspnWebParameters {

    ESPN_WEB_PROD_URL("espn_web_prod_url"),
    ESPN_WEB_BETA_URL("espn_web_beta_url"),
    ESPN_WEB_QA_URL("espn_web_qa_url"),
    ESPN_WEB_PROD_LANDING_URL("espn_web_prod_landing_url"),
    ESPN_WEB_BETA_LANDING_URL("espn_web_beta_landing_url"),
    ESPN_WEB_QA_LANDING_URL("espn_web_qa_landing_url"),
    ESPN_WEB_PROD_SECURE_URL("espn_web_prod_secure_url"),
    ESPN_WEB_QA_SECURE_URL("espn_web_qa_secure_url"),
    ESPN_WEB_PROD_LANDING_MLB_URL("espn_web_prod_landing_mlb_url"),
    ESPN_WEB_BETA_LANDING_MLB_URL("espn_web_beta_landing_mlb_url"),
    ESPN_WEB_QA_LANDING_MLB_URL("espn_web_qa_landing_mlb_url"),
    ESPN_WEB_PROD_LANDING_BASEBALL_URL("espn_web_prod_landing_baseball_url"),
    ESPN_WEB_BETA_LANDING_BASEBALL_URL("espn_web_beta_landing_baseball_url"),
    ESPN_WEB_QA_LANDING_BASEBALL_URL("espn_web_qa_landing_baseball_url"),
    ESPN_WEB_PROD_LANDING_UFC_URL("espn_web_prod_landing_ufc_url"),
    ESPN_WEB_BETA_LANDING_UFC_URL("espn_web_beta_landing_ufc_url"),
    ESPN_WEB_QA_LANDING_UFC_URL("espn_web_qa_landing_ufc_url"),
    ESPN_WEB_PROD_LANDING_UFC_PPV_URL("espn_web_prod_landing_ufc_ppv_url"),
    ESPN_WEB_BETA_LANDING_UFC_PPV_URL("espn_web_beta_landing_ufc_ppv_url"),
    ESPN_WEB_QA_LANDING_UFC_PPV_URL("espn_web_qa_landing_ufc_ppv_url"),
    ESPN_WEB_PROD_MLBTV_URL("espn_web_prod_mlbtv_url"),
    ESPN_WEB_BETA_MLBTV_URL("espn_web_beta_mlbtv_url"),
    ESPN_WEB_BASE_PURCHSE_DIRECT_URL("espn_web_base_purchase_direct_url"),
    ESPN_WEB_BASE_MONTHLY_DIRECT_URL("espn_web_base_monthly_direct_url"),
    ESPN_WEB_BASE_ANNUAL_DIRECT_URL("espn_web_base_annual_direct_url"),
    ESPN_WEB_MLB_BUNDLE_DIRECT_URL("espn_web_mlb_bundle_direct_url"),
    ESPN_WEB_UFC_DIRECT_URL("espn_web_ufc_direct_url"),
    ESPN_WEB_UFC_UPGRADE_DIRECT_URL("espn_web_ufc_upgrade_direct_url"),
    ESPN_WEB_SUB_MANAGEMENT_URL("espn_web_sub_management_url"),
    ESPN_WEB_QA_SUB_MANAGEMENT_URL("espn_web_qa_sub_management_url"),
    ESPN_WEB_QA_BETA_MLBTV_URL("espn_web_qa_beta_mlbtv_url"),
    ESPN_WEB_BETA_MLS_URL("espn_web_beta_mls_url"),
    ESPN_WEB_MLS_URL("espn_web_mls_url"),
    ESPN_WEB_USER("espn_web_user"),
    ESPN_WEB_PASS("espn_web_pass"),
    ESPN_WEB_BETA_USER("espn_web_beta_user"),
    ESPN_WEB_BETA_PASS("espn_web_beta_pass"),
    ESPN_WEB_GMAIL_USER("espn_web_gmail_user"),
    ESPN_WEB_GMAIL_PASS("espn_web_gmail_pass"),
    MASTERCARD_CREDIT_CARD("mastercard_credit_card"),
    VISA_CREDIT_CARD("visa_credit_card"),
    DISCOVER_CREDIT_CARD("discover_credit_card"),
    CVV("cvv"),
    AMEX_CREDIT_CARD("amex_credit_card"),
    AMEX_CVV("amex_cvv"),
    ESPN_WEB_PWD_URL("espn_web_pwd_url"),
    ESPN_WEB_PROD_QE_ESPN_BASE_EMAIL("espn_web_prod_qe_base_gmail_user"),
    ESPN_WEB_PROD_QE_ESPN_BASE_PPV_EMAIL("espn_web_prod_qe_base_ppv_gmail_user"),
    ESPN_WEB_PROD_QE_ESPN_BASE_PWD("espn_web_prod_qe_gmail_pass");

    private CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));
    private String key;

    private EspnWebParameters(String key) {
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }

    public String getValue(){
        return R.TESTDATA.get(this.key);
    }

    public String getDecryptedValue() {
        return cryptoTool.decrypt(getValue());
    }
}
