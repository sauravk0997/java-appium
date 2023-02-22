package com.disney.qa.api.nhl.pojo.news;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "1136x640",
        "1024x576",
        "960x540",
        "768x432",
        "640x360",
        "568x320",
        "372x210",
        "320x180",
        "248x140",
        "124x70"
})
public class Cuts {

    @JsonProperty("1136x640")
    private ImageRatios image1136x640;
    @JsonProperty("1024x576")
    private ImageRatios image1024x576;
    @JsonProperty("960x540")
    private ImageRatios image960x540;
    @JsonProperty("768x432")
    private ImageRatios image768x432;
    @JsonProperty("640x360")
    private ImageRatios image640x360;
    @JsonProperty("568x320")
    private ImageRatios image568x320;
    @JsonProperty("372x210")
    private ImageRatios image372x210;
    @JsonProperty("320x180")
    private ImageRatios image320x180;
    @JsonProperty("248x140")
    private ImageRatios image248x140;
    @JsonProperty("124x70")
    private ImageRatios image124x70;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The image1136x640
     */
    @JsonProperty("1136x640")
    public ImageRatios get1136x640() {
        return image1136x640;
    }

    /**
     *
     * @return
     * The image1024x576
     */
    @JsonProperty("1024x576")
    public ImageRatios get1024x576() {
        return image1024x576;
    }

    /**
     *
     * @return
     * The image960x540
     */
    @JsonProperty("960x540")
    public ImageRatios get960x540() {
        return image960x540;
    }

    /**
     *
     * @return
     * The image768x432
     */
    @JsonProperty("768x432")
    public ImageRatios get768x432() {
        return image768x432;
    }

    /**
     *
     * @return
     * The image640x360
     */
    @JsonProperty("640x360")
    public ImageRatios get640x360() {
        return image640x360;
    }

    /**
     *
     * @return
     * The image568x320
     */
    @JsonProperty("568x320")
    public ImageRatios get568x320() {
        return image568x320;
    }

    /**
     *
     * @return
     * The image372x210
     */
    @JsonProperty("372x210")
    public ImageRatios get372x210() {
        return image372x210;
    }

    /**
     *
     * @return
     * The image320x180
     */
    @JsonProperty("320x180")
    public ImageRatios get320x180() {
        return image320x180;
    }

    /**
     *
     * @return
     * The image248x140
     */
    @JsonProperty("248x140")
    public ImageRatios get248x140() {
        return image248x140;
    }

    /**
     *
     * @return
     * The image124x70
     */
    @JsonProperty("124x70")
    public ImageRatios get124x70() {
        return image124x70;
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