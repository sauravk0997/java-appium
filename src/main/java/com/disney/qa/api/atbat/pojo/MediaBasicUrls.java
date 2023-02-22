package com.disney.qa.api.atbat.pojo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "FLASH_1200K_640X360",
        "FLASH_1800K_960X540",
        "FLASH_2500K_1280X720",
        "FLASH_450K_400X224",
        "HTTP_CLOUD_ANDROID_TABLET",
        "HTTP_CLOUD_MOBILE",
        "HTTP_CLOUD_TABLET",
        "HTTP_CLOUD_TABLET_60",
        "HTTP_CLOUD_WIRED",
        "HTTP_CLOUD_WIRED_60",
        "HTTP_CLOUD_WIRED_WEB"
})
public class MediaBasicUrls {

    @JsonProperty("FLASH_1200K_640X360")
    private String flash1200K640X360;
    @JsonProperty("FLASH_1800K_960X540")
    private String flash1800K960X540;
    @JsonProperty("FLASH_2500K_1280X720")
    private String flash2500K1280X720;
    @JsonProperty("FLASH_450K_400X224")
    private String flash450K400X224;
    @JsonProperty("HTTP_CLOUD_ANDROID_TABLET")
    private String httpCloudAndroidTablet;
    @JsonProperty("HTTP_CLOUD_MOBILE")
    private String httpCloudMobile;
    @JsonProperty("HTTP_CLOUD_TABLET")
    private String httpCloudTablet;
    @JsonProperty("HTTP_CLOUD_TABLET_60")
    private String httpCloudTablet60;
    @JsonProperty("HTTP_CLOUD_WIRED")
    private String httpCloudWired;
    @JsonProperty("HTTP_CLOUD_WIRED_60")
    private String httpCloudWired60;
    @JsonProperty("HTTP_CLOUD_WIRED_WEB")
    private String httpCloudWiredWeb;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The flash1200K640X360
     */
    @JsonProperty("FLASH_1200K_640X360")
    public String getFlash1200K640X360() {
        return flash1200K640X360;
    }

    /**
     *
     * @param flash1200K640X360
     * The FLASH_1200K_640X360
     */
    @JsonProperty("FLASH_1200K_640X360")
    public void setFlash1200K640X360(String flash1200K640X360) {
        this.flash1200K640X360 = flash1200K640X360;
    }

    /**
     *
     * @return
     * The flash1800K960X540
     */
    @JsonProperty("FLASH_1800K_960X540")
    public String getFlash1800K960X540() {
        return flash1800K960X540;
    }

    /**
     *
     * @param flash1800K960X540
     * The FLASH_1800K_960X540
     */
    @JsonProperty("FLASH_1800K_960X540")
    public void setFlash1800K960X540(String flash1800K960X540) {
        this.flash1800K960X540 = flash1800K960X540;
    }

    /**
     *
     * @return
     * The flash2500K1280X720
     */
    @JsonProperty("FLASH_2500K_1280X720")
    public String getFlash2500K1280X720() {
        return flash2500K1280X720;
    }

    /**
     *
     * @param flash2500K1280X720
     * The FLASH_2500K_1280X720
     */
    @JsonProperty("FLASH_2500K_1280X720")
    public void setFlash2500K1280X720(String flash2500K1280X720) {
        this.flash2500K1280X720 = flash2500K1280X720;
    }

    /**
     *
     * @return
     * The flash450K400X224
     */
    @JsonProperty("FLASH_450K_400X224")
    public String getFlash450K400X224() {
        return flash450K400X224;
    }

    /**
     *
     * @param flash450K400X224
     * The FLASH_450K_400X224
     */
    @JsonProperty("FLASH_450K_400X224")
    public void setFlash450K400X224(String flash450K400X224) {
        this.flash450K400X224 = flash450K400X224;
    }

    /**
     *
     * @return
     * The httpCloudAndroidTablet
     */
    @JsonProperty("HTTP_CLOUD_ANDROID_TABLET")
    public String getHttpCloudAndroidTablet() {
        return httpCloudAndroidTablet;
    }

    /**
     *
     * @param httpCloudAndroidTablet
     * The HTTP_CLOUD_ANDROID_TABLET
     */
    @JsonProperty("HTTP_CLOUD_ANDROID_TABLET")
    public void setHttpCloudAndroidTablet(String httpCloudAndroidTablet) {
        this.httpCloudAndroidTablet = httpCloudAndroidTablet;
    }

    /**
     *
     * @return
     * The httpCloudMobile
     */
    @JsonProperty("HTTP_CLOUD_MOBILE")
    public String getHttpCloudMobile() {
        return httpCloudMobile;
    }

    /**
     *
     * @param httpCloudMobile
     * The HTTP_CLOUD_MOBILE
     */
    @JsonProperty("HTTP_CLOUD_MOBILE")
    public void setHttpCloudMobile(String httpCloudMobile) {
        this.httpCloudMobile = httpCloudMobile;
    }

    /**
     *
     * @return
     * The httpCloudTablet
     */
    @JsonProperty("HTTP_CLOUD_TABLET")
    public String getHttpCloudTablet() {
        return httpCloudTablet;
    }

    /**
     *
     * @param httpCloudTablet
     * The HTTP_CLOUD_TABLET
     */
    @JsonProperty("HTTP_CLOUD_TABLET")
    public void setHttpCloudTablet(String httpCloudTablet) {
        this.httpCloudTablet = httpCloudTablet;
    }

    /**
     *
     * @return
     * The httpCloudTablet60
     */
    @JsonProperty("HTTP_CLOUD_TABLET_60")
    public String getHttpCloudTablet60() {
        return httpCloudTablet60;
    }

    /**
     *
     * @param httpCloudTablet60
     * The HTTP_CLOUD_TABLET_60
     */
    @JsonProperty("HTTP_CLOUD_TABLET_60")
    public void setHttpCloudTablet60(String httpCloudTablet60) {
        this.httpCloudTablet60 = httpCloudTablet60;
    }

    /**
     *
     * @return
     * The httpCloudWired
     */
    @JsonProperty("HTTP_CLOUD_WIRED")
    public String getHttpCloudWired() {
        return httpCloudWired;
    }

    /**
     *
     * @param httpCloudWired
     * The HTTP_CLOUD_WIRED
     */
    @JsonProperty("HTTP_CLOUD_WIRED")
    public void setHttpCloudWired(String httpCloudWired) {
        this.httpCloudWired = httpCloudWired;
    }

    /**
     *
     * @return
     * The httpCloudWired60
     */
    @JsonProperty("HTTP_CLOUD_WIRED_60")
    public String getHttpCloudWired60() {
        return httpCloudWired60;
    }

    /**
     *
     * @param httpCloudWired60
     * The HTTP_CLOUD_WIRED_60
     */
    @JsonProperty("HTTP_CLOUD_WIRED_60")
    public void setHttpCloudWired60(String httpCloudWired60) {
        this.httpCloudWired60 = httpCloudWired60;
    }

    /**
     *
     * @return
     * The httpCloudWiredWeb
     */
    @JsonProperty("HTTP_CLOUD_WIRED_WEB")
    public String getHttpCloudWiredWeb() {
        return httpCloudWiredWeb;
    }

    /**
     *
     * @param httpCloudWiredWeb
     * The HTTP_CLOUD_WIRED_WEB
     */
    @JsonProperty("HTTP_CLOUD_WIRED_WEB")
    public void setHttpCloudWiredWeb(String httpCloudWiredWeb) {
        this.httpCloudWiredWeb = httpCloudWiredWeb;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}