package com.disney.qa.api.disney;

/**
 * Entitlement Sku's for Disney.
 * SKU's pulled from: https://github.bamtech.co/data-eng/sublime/blob/676148d2a7da0421762260055c35e20b555b9363/tests/integration/inputs/product_catalog.json
 * DISNEY_FIXED_DATE - This is added to enable the "auto Delorean" feature on an account.
 * DISNEY_FLEX_DATE - This is added to allow use of the "X-DELOREAN" header in tests.
 * DISNEY_PLUS_PS4 - This provides standard access.
 * DISNEY_PLUS_TIZEN - This provides standard access.
 * DISNEY_PLUS_XBOX - This provides standard access.
 * DISNEY_REGION_FREE - This allows the header of "X-GEO-OVERRIDE" to be used.
 *
 * When adding a new SKU please make sure that the naming convention is as follows:
 * PRODUCT_TYPE_PARTNER_COUNTRY_BOILERPLATE
 * PRODUCT - DISNEY / HULU / STAR
 * TYPE - EXTERNAL / IAP / PARTNER / D2C
 * PARTNER - Izzo / Verizon
 * COUNTRY - US / NL / AR
 * BOILERPLATE - BUNDLE / SUPER BUNDLE
 *
 * The ONLY Sku's that should be added here should fall under EXTERNAL, IAP or PARTNER!
 */
public enum DisneySkuParameters {

    DISNEY_D2C_EA_TOY_STORY("disney_plus_premier_access_toy_story_qa_us_web_web_224b06c"),
    DISNEY_D2C_FIXED_DATE("1999199999999916621999000_disney"),
    DISNEY_D2C_FLEX_DATE("1999199999999918421999000_disney"),
    DISNEY_D2C_QC_VIEWER("1999599999999917821899000_disney"),
    DISNEY_D2C_REGION_FREE("1999199999999913621999000_disney"),
    DISNEY_D2C_RESTRICTION("1999399999999918931999000_disney"),
    DISNEY_EXTERNAL_CANAL_BUNDLE("com.disney.canal.bundle"),
    DISNEY_EXTERNAL_O2_BUNDLE("com.disney.O2.bundle"),
    DISNEY_EXTERNAL_O2_PROMO_BUNDLE_3MONTH("com.disney.O2.bundle.3month.promo"),
    DISNEY_EXTERNAL_DETELEKOM_STANDALONE("com.disney.deutschetelekom.standalone"),
    DISNEY_EXTERNAL_MOVISTAR_STANDALONE("com.disney.movistar.standalone"),
    DISNEY_EXTERNAL_SKYUK_STANDALONE("com.disney.skyuk.standalone"),
    DISNEY_EXTERNAL_VERIZON_PROMO_BUNDLE_12MONTH("com.disney.verizon.standalone.12month.promo"),
    DISNEY_EXTERNAL_VERIZON_MONTHLY_STANDALONE("com.disney.verizon.standalone"),
    DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE("com.disney.verizon.disneybundle"),
    DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE_PROMO("com.disney.verizon.disneybundle.promo"),
    DISNEY_EXTERNAL_VERIZON_SUPER_BUNDLE_UPGRADE("com.disney.verizon.disneybundle.upgrade"),
    DISNEY_IAP_AMAZON_MONTHLY("com.disney.monthly.disneyplus.amazon"),
    DISNEY_IAP_AMAZON_YEARLY("com.disney.yearly.disneyplus.amazon"),
    DISNEY_IAP_APPLE_MONTHLY("com.disney.monthly.disneyplus.apple"),
    DISNEY_IAP_APPLE_YEARLY("com.disney.yearly.disneyplus.apple"),
    DISNEY_IAP_GOOGLE_MONTHLY("com.disney.monthly.disneyplus.google"),
    DISNEY_IAP_GOOGLE_YEARLY("com.disney.yearly.disneyplus.google"),
    DISNEY_IAP_ROKU_MONTHLY("com.disney.monthly.disneyplus.roku"),
    DISNEY_IAP_ROKU_YEARLY("com.disney.yearly.disneyplus.roku"),
    DISNEY_PARTNER_BRADESCO_BANK_BR_STANDALONE("com.disney.bradesco.bank.standalone"),
    DISNEY_PARTNER_BRADESCO_NEXT_BR_STANDALONE("com.disney.bradesco.next.standalone"),
    DISNEY_PARTNER_TELECOM_AR_STANDAONE("com.disney.telecom.ar.standalone"),
    DISNEY_PARTNER_IZZI_MX_TRIPLEPLAY("com.disney.izzi.tripleplay.standalone"),
    DISNEY_PARTNER_IZZI_MX_BUNDLE("com.disney.izzi.bundle"),
    DISNEY_PARTNER_IZZI_MX_STANDALONE_1MONTH_TRIAL("com.disney.izzi.standalone.1month.trial"),
    DISNEY_PARTNER_IZZI_MX_STANDALONE_7DAY_TRIAL("com.disney.izzi.standalone.7day.trial"),
    DISNEY_PARTNER_IZZI_MX_STANDALONE("com.disney.izzi.standalone"),
    DISNEY_PARTNER_MERCADOLIBRE_BR_STANDALONE("com.disney.mercadolibre.br.standalone"),
    DISNEY_PARTNER_MERCADOLIBRE_MX_STANDALONE("com.disney.mercadolibre.standalone"),
    DISNEY_PARTNER_O2_DE_STANDALONE("com.disney.O2.standalone"),
    DISNEY_PARTNER_TIM_IT_STANDALONE("com.disney.tim.standalone"),
    DISNEY_PARTNER_TELMEX_MX_STANDALONE("com.disney.telmex.standalone"),
    DISNEY_PARTNER_VIVO_BR_3MONTH_PROMO("com.disney.vivo.standalone.3month.promo"),
    DISNEY_PARTNER_VIVO_BR_3MONTH_INTRO_PROMO("com.disney.vivo.standalone.3month.intro.promo"),
    DISNEY_PARTNER_VIVO_BR_BUNDLE("com.disney.vivo.bundle"),
    DISNEY_PARTNER_VIVO_BR_STANDALONE("com.disney.vivo.standalone"),
    DISNEY_PARTNER_VIVO_BR_BUNDLE_3MONTH_PROMO("com.disney.vivo.bundle.3month.promo"),
    HULU_EXTERNAL_HULU_SUPER_BUNDLE_SASH("com.hulu.bundle.sashdisneyplusespnplus"),
    HULU_EXTERNAL_HULU_SUPER_BUNDLE_NOAH("com.hulu.bundle.noahdisneyplusespnplus"),
    HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_SASH("com.hulu.bundle.livesashdisneyplusespnplus"),
    HULU_EXTERNAL_HULU_SUPER_BUNDLE_LIVE_NOAH("com.hulu.bundle.livenoahdisneyplusespnplus"),
    STAR_D2C_PREMIERE_ACCESS("star_premier_access_live_free_or_die_hard_br_web_tes_web_0961e7f"),
    STAR_D2C_FLEX_DATE("star_special_content_access_flex_date_web_9a0af45"),
    STAR_D2C_REGION_FREE("star_special_content_access_region_free_web_f37d187"),
    STAR_IAP_APPLE_MONTHLY("com.star.monthly.starplus.apple"),
    STAR_IAP_APPLE_YEARLY("com.star.yearly.starplus.apple"),
    STAR_IAP_GOOGLE_MONTHLY("com.star.monthly.starplus.google"),
    STAR_PARTNER_TELMEX_STANDALONE("com.star.telmex.standalone"),
    STAR_PARTNER_TELMEX_BUNDLE("com.combo.telmex.bundle.upgrade.star.1month.promo"),
    STAR_PARTNER_VIVO_BR_STANDALONE("com.star.vivo.standalone"),
    STAR_PARTNER_IZZI_MX_STANDALONE("com.star.izzi.standalone");

    private String key;

    DisneySkuParameters(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.key;
    }
}