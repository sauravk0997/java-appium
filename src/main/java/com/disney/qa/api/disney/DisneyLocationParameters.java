package com.disney.qa.api.disney;

import com.zebrunner.carina.utils.R;

public enum DisneyLocationParameters {


    DISNEY_AU_LOCATION_ZIP_CODE("disney_au_location_zip_code"),
    DISNEY_CA_LOCATION_ZIP_CODE("disney_ca_location_zip_code"),
    DISNEY_GR_LOCATION_ZIP_CODE("disney_gr_location_zip_code"),
    DISNEY_IM_LOCATION_ZIP_CODE("disney_im_location_zip_code"),
    DISNEY_DK_LOCATION_ZIP_CODE("disney_dk_location_zip_code"),
    DISNEY_FI_LOCATION_ZIP_CODE("disney_fi_location_zip_code"),
    DISNEY_NO_LOCATION_ZIP_CODE("disney_no_location_zip_code"),
    DISNEY_SE_LOCATION_ZIP_CODE("disney_se_location_zip_code"),
    DISNEY_BE_LOCATION_ZIP_CODE("disney_be_location_zip_code"),
    DISNEY_PT_LOCATION_ZIP_CODE("disney_pt_location_zip_code"),
    DISNEY_NL_LOCATION_ZIP_CODE("disney_nl_location_zip_code"),
    DISNEY_US_LOCATION_ZIP_CODE("disney_us_location_zip_code"),
    DISNEY_FR_LOCATION_ZIP_CODE("disney_fr_location_zip_code"),
    DISNEY_DE_LOCATION_ZIP_CODE("disney_de_location_zip_code"),
    DISNEY_PR_LOCATION_ZIP_CODE("disney_pr_location_zip_code"),
    DISNEY_NZ_LOCATION_ZIP_CODE("disney_nz_location_zip_code"),
    DISNEY_GB_LOCATION_ZIP_CODE("disney_gb_location_zip_code"),
    DISNEY_ES_LOCATION_ZIP_CODE("disney_es_location_zip_code"),
    DISNEY_CR_LOCATION_ZIP_CODE("disney_cr_location_zip_code"),
    DISNEY_AT_LOCATION_ZIP_CODE("disney_at_location_zip_code"),
    DISNEY_CH_LOCATION_ZIP_CODE("disney_ch_location_zip_code"),
    DISNEY_IT_LOCATION_ZIP_CODE("disney_it_location_zip_code"),
    DISNEY_IE_LOCATION_ZIP_CODE("disney_ie_location_zip_code"),
    DISNEY_GG_LOCATION_ZIP_CODE("disney_gg_location_zip_code"),
    DISNEY_JE_LOCATION_ZIP_CODE("disney_je_location_zip_code"),
    DISNEY_GF_LOCATION_ZIP_CODE("disney_gf_location_zip_code"),
    DISNEY_GP_LOCATION_ZIP_CODE("disney_gp_location_zip_code"),
    DISNEY_MQ_LOCATION_ZIP_CODE("disney_mq_location_zip_code"),
    DISNEY_BL_LOCATION_ZIP_CODE("disney_bl_location_zip_code"),
    DISNEY_MF_LOCATION_ZIP_CODE("disney_mf_location_zip_code"),
    DISNEY_NC_LOCATION_ZIP_CODE("disney_nc_location_zip_code"),
    DISNEY_WF_LOCATION_ZIP_CODE("disney_wf_location_zip_code"),
    DISNEY_LU_LOCATION_ZIP_CODE("disney_lu_location_zip_code"),
    DISNEY_IS_LOCATION_ZIP_CODE("disney_is_location_zip_code"),
    DISNEY_YT_LOCATION_ZIP_CODE("disney_yt_location_zip_code"),
    DISNEY_RE_LOCATION_ZIP_CODE("disney_re_location_zip_code"),
    DISNEY_MU_LOCATION_ZIP_CODE("disney_mu_location_zip_code"),
    DISNEY_BR_LOCATION_ZIP_CODE("disney_br_location_zip_code"),
    DISNEY_AR_LOCATION_ZIP_CODE("disney_ar_location_zip_code"),
    DISNEY_CL_LOCATION_ZIP_CODE("disney_cl_location_zip_code"),
    DISNEY_CO_LOCATION_ZIP_CODE("disney_co_location_zip_code"),
    DISNEY_MX_LOCATION_ZIP_CODE("disney_mx_location_zip_code"),
    DISNEY_PE_LOCATION_ZIP_CODE("disney_pe_location_zip_code"),
    DISNEY_EC_LOCATION_ZIP_CODE("disney_ec_location_zip_code"),
    DISNEY_UY_LOCATION_ZIP_CODE("disney_uy_location_zip_code"),
    DISNEY_PA_LOCATION_ZIP_CODE("disney_pa_location_zip_code"),
    DISNEY_AW_LOCATION_ZIP_CODE("disney_aw_location_zip_code");

    private String key;

    DisneyLocationParameters(String key) {
        this.key = key;
    }

    public String getValue() {
        return R.TESTDATA.get(this.key);

    }

    public static String getZipCodeLocationOverride(String country) {
        String countryCode = country.toUpperCase();
        switch (countryCode.toUpperCase()) {
            case "CA": return DISNEY_CA_LOCATION_ZIP_CODE.getValue();
            case "AU": return DISNEY_AU_LOCATION_ZIP_CODE.getValue();
            case "NL": return DISNEY_NL_LOCATION_ZIP_CODE.getValue();
            case "NZ": return DISNEY_NZ_LOCATION_ZIP_CODE.getValue();
            case "US": return DISNEY_US_LOCATION_ZIP_CODE.getValue();
            case "FR": return DISNEY_FR_LOCATION_ZIP_CODE.getValue();
            case "DE": return DISNEY_DE_LOCATION_ZIP_CODE.getValue();
            case "PR": return DISNEY_PR_LOCATION_ZIP_CODE.getValue();
            case "GR": return DISNEY_GR_LOCATION_ZIP_CODE.getValue();
            case "IM": return DISNEY_IM_LOCATION_ZIP_CODE.getValue();
            case "DK": return DISNEY_DK_LOCATION_ZIP_CODE.getValue();
            case "FI": return DISNEY_FI_LOCATION_ZIP_CODE.getValue();
            case "NO": return DISNEY_NO_LOCATION_ZIP_CODE.getValue();
            case "SE": return DISNEY_SE_LOCATION_ZIP_CODE.getValue();
            case "BE": return DISNEY_BE_LOCATION_ZIP_CODE.getValue();
            case "PT": return DISNEY_PT_LOCATION_ZIP_CODE.getValue();
            case "GB": return DISNEY_GB_LOCATION_ZIP_CODE.getValue();
            case "ES": return DISNEY_ES_LOCATION_ZIP_CODE.getValue();
            case "CR": return DISNEY_CR_LOCATION_ZIP_CODE.getValue();
            case "AT": return DISNEY_AT_LOCATION_ZIP_CODE.getValue();
            case "CH": return DISNEY_CH_LOCATION_ZIP_CODE.getValue();
            case "IT": return DISNEY_IT_LOCATION_ZIP_CODE.getValue();
            case "IE": return DISNEY_IE_LOCATION_ZIP_CODE.getValue();
            case "GG": return DISNEY_GG_LOCATION_ZIP_CODE.getValue();
            case "JE": return DISNEY_JE_LOCATION_ZIP_CODE.getValue();
            case "GF": return DISNEY_GF_LOCATION_ZIP_CODE.getValue();
            case "GP": return DISNEY_GP_LOCATION_ZIP_CODE.getValue();
            case "MQ": return DISNEY_MQ_LOCATION_ZIP_CODE.getValue();
            case "BL": return DISNEY_BL_LOCATION_ZIP_CODE.getValue();
            case "MF": return DISNEY_MF_LOCATION_ZIP_CODE.getValue();
            case "NC": return DISNEY_NC_LOCATION_ZIP_CODE.getValue();
            case "WF": return DISNEY_WF_LOCATION_ZIP_CODE.getValue();
            case "LU": return DISNEY_LU_LOCATION_ZIP_CODE.getValue();
            case "IS": return DISNEY_IS_LOCATION_ZIP_CODE.getValue();
            case "YT": return DISNEY_YT_LOCATION_ZIP_CODE.getValue();
            case "RE": return DISNEY_RE_LOCATION_ZIP_CODE.getValue();
            case "MU": return DISNEY_MU_LOCATION_ZIP_CODE.getValue();
            case "BR": return DISNEY_BR_LOCATION_ZIP_CODE.getValue();
            case "AR": return DISNEY_AR_LOCATION_ZIP_CODE.getValue();
            case "CL": return DISNEY_CL_LOCATION_ZIP_CODE.getValue();
            case "CO": return DISNEY_CO_LOCATION_ZIP_CODE.getValue();
            case "MX": return DISNEY_MX_LOCATION_ZIP_CODE.getValue();
            case "PE": return DISNEY_PE_LOCATION_ZIP_CODE.getValue();
            case "EC": return DISNEY_EC_LOCATION_ZIP_CODE.getValue();
            case "UY": return DISNEY_UY_LOCATION_ZIP_CODE.getValue();
            case "PA": return DISNEY_PA_LOCATION_ZIP_CODE.getValue();
            case "AW": return DISNEY_AW_LOCATION_ZIP_CODE.getValue();
            default:
                throw new IllegalArgumentException(
                        String.format("'%s' is an invalid country Code parameter", countryCode));
        }
    }
}
