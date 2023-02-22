package com.disney.util.disney;

public class ZebrunnerXrayLabels {
    private String partner;
    private String locale;
    private String[] xrayTestIds;

    public ZebrunnerXrayLabels(String partner, String locale, String... xrayTestIds) {
        this.partner = partner;
        this.locale = locale;
        this.xrayTestIds = xrayTestIds;
    }

    public String getPartner() {
        return partner;
    }

    public String getLocale() {
        return locale;
    }

    public String[] getXrayTestIds() {
        return xrayTestIds;
    }
}
