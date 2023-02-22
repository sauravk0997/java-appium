package com.disney.qa.disney.web;

import com.qaprosoft.carina.core.foundation.crypto.CryptoTool;
import com.qaprosoft.carina.core.foundation.utils.Configuration;
import com.qaprosoft.carina.core.foundation.utils.R;

public enum DisneyWebParameters {


    //Environments

    //Stage
    STAGE_WEB_DISNEY_PLUS("disney_plus_stage_web"),
    PREVIEW_STAGE_WEB_DISNEY_PLUS("disney_plus_stage_preview_web"),

    //Disney
    DISNEY_BETA_WEB("disney_beta_web"),
    DISNEY_PROD_WEB("disney_prod_web"),
    DISNEY_PROD_WEB_D23URL("disney_prod_web_d23url"),
    DISNEY_PROD_WEB_PREVIEW("disney_prod_web_preview"),
    DISNEY_QA_WEB_D23URL("disney_qa_web_d23url"),
    DISNEY_LOCAL_WEB("disney_local_web"),
    DISNEY_EDGE_DUST_PROD("disney_edge_dust_prod_service"),
    DISNEY_EDGE_DUST_QA("disney_edge_dust_qa_service"),

    //Commerce
    DISNEY_PROD_ACTIVATION_TOKEN_URL("disney_prod_activation_token_url"),
    DISNEY_QA_ACTIVATION_TOKEN_URL("disney_qa_activation_token_url"),
    DISNEY_INCOMING_ACTIVATION_URL("disney_incoming_activation_url"),
    DISNEY_WEB_DEEPLINK_URL("disney_web_deeplink_url"),
    DISNEY_WEB_SUPERBUNDLE_NOAH_DEEPLINK_URL("disney_web_superbundle_noah_deeplink_url"),
    DISNEY_WEB_PORTABILITY_PASS("disney_portability_pass"),
    DISNEY_WEB_SUB_MANAGEMENT_URL("disney_web_sub_management_url"),
    DISNEY_QA_WEB_D23REDEMPTION_CODE("disney_qa_d23redemption_code"),
    DISNEY_QA_WEB_D23PASSWORD("disney_qa_web_d23password"),
    DISNEY_QA_WEB_GENERIC_PASS("disney_qa_web_generic_pass"),
    DISNEY_QA_WEB_LATAM_URL("disney_latam_url"),
    DISNEY_QA_WEB_REDEMPTION_RWOP_GIFT_CODE("disney_qa_redemption_rwop_gift"),
    DISNEY_QA_WEB_REDEMPTION_VERIZON_TEST("disney_qa_redemption_verizon_test"),
    DISNEY_QA_WEB_SUB_MANAGEMENT_URL("disney_web_qa_sub_management_url"),
    DISNEY_QA_WEB_SUPERBUNDLE_USER("disney_qa_web_superbundle_user"),
    DISNEY_QA_WEB_SUPERBUNDLE_PASS("disney_qa_web_superbundle_pass"),
    DISNEY_QA_WEB_SUPERBUNDLE_INELIGIBLE_SASH_USER("disney_qa_web_superbundle_ineligible_sash_user"),
    DISNEY_QA_WEB_SUPERBUNDLE_INELIGIBLE_SASH_PASS("disney_qa_web_superbundle_ineligible_sash_pass"),
    DISNEY_QA_WEB_SUPERBUNDLE_INELIGIBLE_USER("disney_qa_web_superbundle_ineligible_user"),
    DISNEY_QA_WEB_SUPERBUNDLE_INELIGIBLE_PASS("disney_qa_web_superbundle_ineligible_pass"),
    DISNEY_QA_WEB_SUPERBUNDLE_REDIRECT_USER("disney_qa_web_superbundle_redirect_user"),
    DISNEY_QA_WEB_SUPERBUNDLE_REDIRECT_PASS("disney_qa_web_superbundle_redirect_pass"),
    DISNEY_QA_WEB_SUPERBUNDLE_REDIRECT_2_USER("disney_qa_web_superbundle_redirect_2_user"),
    DISNEY_QA_WEB_SUPERBUNDLE_REDIRECT_2_PASS("disney_qa_web_superbundle_redirect_2_pass"),
    DISNEY_WEB_AKAMAI_ID("disney_web_akamai_id"),
    DISNEY_WEB_CLOUDFRONT_POLICY("disney_web_cloudfront_policy"),
    DISNEY_WEB_CLOUDFRONT_SIGNATURE("disney_web_cloudfront_signature"),
    DISNEY_WEB_CLOUDFRONT_KEYPAIR_ID("disney_web_cloudfront_keypair_id"),
    DISNEY_WEB_MERCADO_PROCESSING_LINK("disney_web_mercado_processing_link"),
    DISNEY_WEB_MERCADO_CHANGING_LINK("disney_web_mercado_changing_link"),
    DISNEY_WEB_SPECIAL_OFFER_ACTIVATED("disney_web_special_offer_activated"),

    //Credit Cards
    DISNEY_VISA_CREDIT_CARD("disneyvisa_credit_card"),
    DISNEY_VISA_CVV("disneyvisa_cvv"),
    MASTERCARD_CA_CREDIT_CARD("disney_mastercard_ca_credit_card"),
    MASTERCARD_CREDIT_CARD("disney_mastercard_credit_card"),
    MASTERCARD_CVV("disney_mastercard_cvv"),
    VISA_CREDIT_CARD("disney_visa_credit_card"),
    VISA_CVV("disney_visa_cvv");

    private CryptoTool cryptoTool = new CryptoTool(Configuration.get(Configuration.Parameter.CRYPTO_KEY_PATH));
    private String key;

    DisneyWebParameters(String key) {
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
