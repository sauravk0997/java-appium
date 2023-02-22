package com.disney.qa.api.nhl.pojo.video;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;

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
public class Image {

    @JsonProperty("1136x640")
    private String image1136x640;
    @JsonProperty("1024x576")
    private String image1024x576;
    @JsonProperty("960x540")
    private String image960x540;
    @JsonProperty("768x432")
    private String image768x432;
    @JsonProperty("640x360")
    private String image640x360;
    @JsonProperty("568x320")
    private String image568x320;
    @JsonProperty("372x210")
    private String image372x210;
    @JsonProperty("320x180")
    private String image320x180;
    @JsonProperty("248x140")
    private String image248x140;
    @JsonProperty("124x70")
    private String image124x70;
    @JsonProperty("cuts")
    private Image cuts;

    /**
     *
     * @return
     * The image1136x640
     */
    @JsonProperty("1136x640")
    public String get1136x640() {
        return image1136x640;
    }

    /**
     *
     * @return
     * The image1024x576
     */
    @JsonProperty("1024x576")
    public String get1024x576() {
        return image1024x576;
    }

    /**
     *
     * @return
     * The image960x540
     */
    @JsonProperty("960x540")
    public String get960x540() {
        return image960x540;
    }

    /**
     *
     * @return
     * The image768x432
     */
    @JsonProperty("768x432")
    public String get768x432() {
        return image768x432;
    }

    /**
     *
     * @return
     * The image640x360
     */
    @JsonProperty("640x360")
    public String get640x360() {
        return image640x360;
    }

    /**
     *
     * @return
     * The image568x320
     */
    @JsonProperty("568x320")
    public String get568x320() {
        return image568x320;
    }

    /**
     *
     * @return
     * The image372x210
     */
    @JsonProperty("372x210")
    public String get372x210() {
        return image372x210;
    }

    /**
     *
     * @return
     * The image320x180
     */
    @JsonProperty("320x180")
    public String get320x180() {
        return image320x180;
    }

    /**
     *
     * @return
     * The image248x140
     */
    @JsonProperty("248x140")
    public String get248x140() {
        return image248x140;
    }

    /**
     *
     * @return
     * The image124x70
     */
    @JsonProperty("124x70")
    public String get124x70() {
        return image124x70;
    }

    /**
     *
     * @return
     * The cuts
     */
    @JsonProperty("cuts")
    public Image getCuts() {
        return cuts;
    }
}