package com.disney.qa.api.nhl.pojo.video.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "640x360",
        "568x320",
        "1136x640",
        "248x140",
        "768x432",
        "1024x576",
        "320x180",
        "960x540",
        "372x210",
        "124x70"
})
public class Cuts {

    @JsonProperty("640x360")
    private Aspects cut640x360;
    @JsonProperty("568x320")
    private Aspects cut568x320;
    @JsonProperty("1136x640")
    private Aspects cut1136x640;
    @JsonProperty("248x140")
    private Aspects cut248x140;
    @JsonProperty("768x432")
    private Aspects cut768x432;
    @JsonProperty("1024x576")
    private Aspects cut1024x576;
    @JsonProperty("320x180")
    private Aspects cut320x180;
    @JsonProperty("960x540")
    private Aspects cut960x540;
    @JsonProperty("372x210")
    private Aspects cut372x210;
    @JsonProperty("124x70")
    private Aspects cut124x70;

    /**
     *
     * @return
     * The cut640x360
     */
    @JsonProperty("640x360")
    public Aspects get640x360() {
        return cut640x360;
    }

    /**
     *
     * @return
     * The cut568x320
     */
    @JsonProperty("568x320")
    public Aspects get568x320() {
        return cut568x320;
    }

    /**
     *
     * @return
     * The cut1136x640
     */
    @JsonProperty("1136x640")
    public Aspects get1136x640() {
        return cut1136x640;
    }

    /**
     *
     * @return
     * The cut248x140
     */
    @JsonProperty("248x140")
    public Aspects get248x140() {
        return cut248x140;
    }

    /**
     *
     * @return
     * The cut768x432
     */
    @JsonProperty("768x432")
    public Aspects get768x432() {
        return cut768x432;
    }

    /**
     *
     * @return
     * The cut1024x576
     */
    @JsonProperty("1024x576")
    public Aspects get1024x576() {
        return cut1024x576;
    }

    /**
     *
     * @return
     * The cut320x180
     */
    @JsonProperty("320x180")
    public Aspects get320x180() {
        return cut320x180;
    }

    /**
     *
     * @return
     * The cut960x540
     */
    @JsonProperty("960x540")
    public Aspects get960x540() {
        return cut960x540;
    }

    /**
     *
     * @return
     * The cut372x210
     */
    @JsonProperty("372x210")
    public Aspects get372x210() {
        return cut372x210;
    }

    /**
     *
     * @return
     * The cut124x70
     */
    @JsonProperty("124x70")
    public Aspects get124x70() {
        return cut124x70;
    }
}