package com.disney.qa.api.disney.pojo;

import com.disney.qa.disney.DisneyCountryData;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.URLDecoder;

public class DisneyPaymentMethodPayPal {

    private String token = "";
    private String usage = "multi_use";
    private DisneyPayPalPayerInfo payerInfo = new DisneyPayPalPayerInfo();
    private boolean isDefault = false;
    private boolean isShared = true;
    private DisneyBillingDetails billingAddress = new DisneyBillingDetails();
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPaymentMethodPayPal(DisneyAccount account, String paypalToken) {
        String countryCode = account.getProfileCountry();
        DisneyCountryData countryData = new DisneyCountryData();
        DisneyPayPalPayerInfo paypalPayerInfo = new DisneyPayPalPayerInfo(account);
        paypalPayerInfo.setPayerCountry(countryCode.toUpperCase());
        this.setPayerInfo(paypalPayerInfo);

        DisneyBillingDetails billingDetails = new DisneyBillingDetails(account);
        billingDetails.setState(countryCode);
        billingDetails.setCity(countryCode);
        billingDetails.setCountry(countryCode.toUpperCase());
        billingDetails.setPostalCode(countryData.searchAndReturnCountryData(countryCode, "code", "zip"));
        this.setBillingAddress(billingDetails);

        this.setToken(paypalToken);
    }

    public String getToken() {
        return token;
    }

    public DisneyPayPalPayerInfo getPayerInfo() {
        return payerInfo;
    }

    public String getUsage() {
        return usage;
    }

    public DisneyBillingDetails getBillingAddress() {
        return billingAddress;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean isShared() {
        return isShared;
    }

    public void setPayerInfo(DisneyPayPalPayerInfo payerInfo) {
        this.payerInfo = payerInfo;
    }

    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
    }

    public void setToken(String token) {

        String tokenDecoded = token;

        try {
            tokenDecoded = URLDecoder.decode(tokenDecoded, "UTF-8");
        } catch (Exception ex) {
            LOGGER.error("Decode Error", ex);
        }

        this.token = tokenDecoded;
    }

    public void setShared(boolean shared) {
        this.isShared = shared;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public void setBillingAddress(DisneyBillingDetails billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String toJsonString() {
        return replaceSpecialFields(new JSONObject(this).toString());
    }

    /**
     *  This fixes a json string for this class by undoing the JSON parsing stripping 'is'.
     * @param string takes a string to fix the JSON parsing.
     * @return provides a valid string for use.
     */
    private String replaceSpecialFields(String string) {
        String[] fieldsToReplace = {"default", "shared"};

        for (String field : fieldsToReplace) {
            string = string.replace(field, "is" + StringUtils.capitalize(field));
        }
        return string;
    }
}
