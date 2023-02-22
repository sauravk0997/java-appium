package com.disney.qa.api.nhl.pojo.news;

import com.disney.qa.api.atbat.pojo.MediaBasicUrls;
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
        "tokenGUID",
        "type",
        "id",
        "teamId",
        "name",
        "seoName",
        "href",
        "hrefMobile",
        "videoId",
        "image",
        "mediaURLS",
        "blurb"
})
public class Token {

    @JsonProperty("tokenGUID")
    private String tokenGUID;
    @JsonProperty("type")
    private String type;
    @JsonProperty("id")
    private String id;
    @JsonProperty("teamId")
    private String teamId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("seoName")
    private String seoName;
    @JsonProperty("href")
    private String href;
    @JsonProperty("hrefMobile")
    private String hrefMobile;
    @JsonProperty("videoId")
    private String videoId;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("mediaURLS")
    private MediaBasicUrls mediaUrls;
    @JsonProperty("blurb")
    private String blurb;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The tokenGUID
     */
    @JsonProperty("tokenGUID")
    public String getTokenGUID() {
        return tokenGUID;
    }

    /**
     *
     * @return
     * The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     *
     * @return
     * The teamId
     */
    @JsonProperty("teamId")
    public String getTeamId() {
        return teamId;
    }

    /**
     *
     * @return
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     * The seoName
     */
    @JsonProperty("seoName")
    public String getSeoName() {
        return seoName;
    }

    /**
     *
     * @return
     * The seoName
     */
    @JsonProperty("href")
    public String getHref() {
        return href;
    }

    /**
     *
     * @return
     * The seoName
     */
    @JsonProperty("hrefMobile")
    public String getHrefMobile() {
        return hrefMobile;
    }

    /**
     *
     * @return
     * The seoName
     */
    @JsonProperty("videoId")
    public String getVideoId() {
        return videoId;
    }

    /**
     *
     * @return
     * The image
     */
    @JsonProperty("image")
    public Image getImage() {
        return image;
    }

    /**
     *
     * @return
     * The mediaURLS
     */
    @JsonProperty("mediaURLS")
    public MediaBasicUrls getMediaUrls() {
        return mediaUrls;
    }

    /**
     *
     * @return
     * The blurb
     */
    @JsonProperty("blurb")
    public String getBlurb() {
        return blurb;
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