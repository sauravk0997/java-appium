package com.disney.qa.api.disney.pojo;

import com.fasterxml.jackson.databind.JsonNode;

public class DisneySku {

    private String sku;
    private String name;
    private String platform;
    private String country;
    private String promotionGroup;
    private String promotionCode;
    private String type;

    public DisneySku(JsonNode node) {
        this.sku = node.findValue("sku").asText();
        this.name = node.findValue("name").asText();
        this.platform = node.findValue("platform").asText();
        this.country = node.findValue("country").asText();
        this.promotionGroup = node.findValue("promotionGroup").asText();
        this.promotionCode = node.findValue("promotionCode").asText();
        this.type = node.findValue("type").asText();
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public String getPromotionGroup() {
        return promotionGroup;
    }

    public String getSku() {
        return sku;
    }

    public String getType() {
        return type;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setPromotionGroup(String promotionGroup) {
        this.promotionGroup = promotionGroup;
    }

    public void setType(String type) {
        this.type = type;
    }
}
