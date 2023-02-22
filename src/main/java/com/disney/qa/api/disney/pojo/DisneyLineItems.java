package com.disney.qa.api.disney.pojo;

import com.disney.qa.api.disney.DisneySkuParameters;

public class DisneyLineItems {

    private String sku;

    public DisneyLineItems(DisneySkuParameters skuPassed) {
        this.sku = skuPassed.getValue();
    }

    public DisneyLineItems(DisneyOffer offer) {
        this.sku = offer.getSku();
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
