package com.disney.qa.api.disney.pojo;

import com.disney.qa.api.disney.DisneyOrder;

import java.util.LinkedList;
import java.util.List;

public class DisneyAccount {

    private String accountId = "";

    private String deviceId = "";

    private String email = "";

    private String firstName = "";

    private String identityPointId = "";

    private String lastName = "";

    private String profileId = "";

    private String profileName = "";

    private String sessionId = "";

    private String userName = "";

    private String userPass = "";

    private String profileLang = "";

    private String countryCode = "";

    private String partner = "";

    private String orderVersion = "";

    private List<DisneySubscriptions> subscriptions = new LinkedList<>();

    private List<DisneyOrder> orderSettings;

    public String getAccountId() {
        return accountId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getIdentityPointId() {
        return identityPointId;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public String getProfileLang() {
        return profileLang;
    }

    public String getProfileCountry() {
        return countryCode;
    }

    public List<DisneySubscriptions> getSubscriptions() {
        return subscriptions;
    }

    public DisneySubscriptions findSubscriptionById(String subscriptionId) {
        for (DisneySubscriptions subscription : subscriptions)
            if (subscription.getSubscriptionId().equalsIgnoreCase(subscriptionId))
                return subscription;
        return null;
    }

    public DisneySubscriptions findSubscriptionBySku(String subscriptionSku) {
        for (DisneySubscriptions subscription : subscriptions)
            if (subscription.getSubscriptionSku().equalsIgnoreCase(subscriptionSku))
                return subscription;
        return null;
    }

    public List<DisneyOrder> getOrderSettings() {
        return orderSettings;
    }

    public String getPartner() {
        return partner;
    }

    public String getOrderVersion() {
        return orderVersion;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setIdentityPointId(String identityPointId) {
        this.identityPointId = identityPointId;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setProfileLang(String profileLang) {
        this.profileLang = profileLang;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setSubscriptions(List<DisneySubscriptions> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setOrderSettings(List<DisneyOrder> orderSettings) {
        this.orderSettings = orderSettings;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public void setOrderVersion(String orderVersion) {
        this.orderVersion = orderVersion;
    }
}