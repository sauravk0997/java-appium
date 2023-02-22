package com.disney.qa.api.disney.pojo;

import com.disney.qa.api.disney.DisneySkuParameters;

public class DisneyCampaigns {

    private String voucherCode;
    private String campaignCode;

    public DisneyCampaigns(DisneySkuParameters sku) {
        if (sku.name().contains("STAR")) {
            this.campaignCode = "STAR_PURCHASE_CMPGN";
            this.voucherCode = "STAR_PURCHASE_VOCHR";
        } else {
            this.campaignCode = "DISNEY_PURCHASE_CMPGN";
            this.voucherCode = "DISNEY_PURCHASE_VOCHR";
        }
    }

    public DisneyCampaigns(DisneyOffer offer) {

        this.campaignCode = offer.getPromotionGroup();
        this.voucherCode = offer.getPromotionCode();

    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }
}
