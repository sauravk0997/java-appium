package com.disney.qa.api.disney.pojo;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedList;
import java.util.List;

/**
 * Allows for lookup of D2C sku's to reduce the number of Sku's being added to
 * the DisneySkuParameter class, the default here will be just the yearly for Web.
 */
public class DisneyOffer {

    private String offerId = "cefd35c0-073d-3b61-ab37-de3054ec63b6";
    private String offerName = "Disney+ Yearly - 79.99 USD - signup";
    private String period = "YEARLY";
    private String sku = "1999199999999910121999000_disney";
    private String skuName = "Disney Plus Yearly - US -  Web";
    private List<DisneySku> skuList = new LinkedList<>();
    private String promotionGroup = "DISNEY_PURCHASE_CMPGN";
    private String promotionCode = "DISNEY_PURCHASE_VOCHR";
    private String price = "79.99";
    private String platform = "web";
    private String type = "D2C";
    private String subType = "";
    private String partner = "DISNEY";
    private String country = "US";
    private String provider = "";

    public DisneyOffer(JsonNode object) {

        this.setOfferId(object.findValue("offerId").asText());
        this.setOfferName(object.findValue("offerName").asText());
        this.setPeriod(object.findValue("period").asText());
        this.setSku(object.findValue("sku").asText());
        this.setSkuName(object.findPath("skus").findPath("name").asText());
        this.setCountry(object.findPath("skus").findPath("country").asText());
        this.setPromotionCode(object.findValue("promotionCode").asText());
        this.setPromotionGroup(object.findValue("promotionGroup").asText());
        this.setPrice(object.findValue("amount").asText());
        this.setPlatform(object.findValue("platform").asText());
        this.setType(object.findValue("offerType").asText());
        this.setPartner(object.findValue("partner").asText());
        for (JsonNode skuNode : object.findPath("skus")) {
            this.skuList.add(new DisneySku(skuNode));
        }
    }

    public DisneyOffer() {

    }

    public String getOfferId() {
        return offerId;
    }

    public String getOfferName() {
        return offerName;
    }

    public String getPeriod() {
        return period;
    }

    public String getSku() {
        return sku;
    }

    public List<DisneySku> getSkuList() {
        return skuList;
    }

    public String getPromotionGroup() {
        return promotionGroup;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public String getPrice() {
        return price;
    }

    public String getSkuName() {
        return skuName;
    }

    public String getPlatform() {
        return platform;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getPartner() {
        return partner;
    }

    public String getCountry() {
        return country;
    }

    public String getProvider() {
        return provider;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public void setSkuList(List<DisneySku> skuList) {
        this.skuList = skuList;
    }

    public void setPromotionGroup(String promotionGroup) {
        this.promotionGroup = promotionGroup;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}
