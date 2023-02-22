package com.disney.qa.api.nhl.pojo.news;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "type",
        "state",
        "date",
        "id",
        "headline",
        "subhead",
        "seoTitle",
        "seoDescription",
        "seoKeywords",
        "slug",
        "commenting",
        "tagline",
        "tokenData",
        "body",
        "contributor",
        "image",
        "keywordsDisplay",
        "keywordsAll",
        "approval",
        "url",
        "dataURI",
        "primaryKeyword",
        "media",
        "preview"
})
public class NewsItem {

    @JsonProperty("type")
    private String type;
    @JsonProperty("state")
    private String state;
    @JsonProperty("date")
    private String date;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("headline")
    private String headline;
    @JsonProperty("subhead")
    private String subhead;
    @JsonProperty("seoTitle")
    private String seoTitle;
    @JsonProperty("seoDescription")
    private String seoDescription;
    @JsonProperty("seoKeywords")
    private String seoKeywords;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("commenting")
    private Boolean commenting;
    @JsonProperty("tagline")
    private String tagline;
    @JsonProperty("tokenData")
    private JsonNode tokenData;
    @JsonProperty("body")
    private String body;
    @JsonProperty("contributor")
    private Contributors contributors;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("keywordsDisplay")
    private List<KeywordsDisplay> keywordsDisplay = new ArrayList<KeywordsDisplay>();
    @JsonProperty("keywordsAll")
    private List<KeywordsAll> keywordsAll = new ArrayList<KeywordsAll>();
    @JsonProperty("approval")
    private String approval;
    @JsonProperty("url")
    private String url;
    @JsonProperty("dataURI")
    private String dataURI;
    @JsonProperty("primaryKeyword")
    private PrimaryKeyword primaryKeyword;
    @JsonProperty("media")
    private Media media;
    @JsonProperty("preview")
    private String preview;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * The state
     */
    @JsonProperty("state")
    public String getState() {
        return state;
    }

    /**
     *
     * @return
     * The date
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    /**
     *
     * @return
     * The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     *
     * @return
     * The headline
     */
    @JsonProperty("headline")
    public String getHeadline() {
        return headline;
    }

    /**
     *
     * @return
     * The subhead
     */
    @JsonProperty("subhead")
    public String getSubhead() {
        return subhead;
    }

    /**
     *
     * @return
     * The seoTitle
     */
    @JsonProperty("seoTitle")
    public String getSeoTitle() {
        return seoTitle;
    }

    /**
     *
     * @return
     * The seoDescription
     */
    @JsonProperty("seoDescription")
    public String getSeoDescription() {
        return seoDescription;
    }

    /**
     *
     * @return
     * The seoKeywords
     */
    @JsonProperty("seoKeywords")
    public String getSeoKeywords() {
        return seoKeywords;
    }

    /**
     *
     * @return
     * The slug
     */
    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    /**
     *
     * @return
     * The commenting
     */
    @JsonProperty("commenting")
    public Boolean getCommenting() {
        return commenting;
    }

    /**
     *
     * @return
     * The tagline
     */
    @JsonProperty("tagline")
    public String getTagline() {
        return tagline;
    }

    /**
     *
     * @return
     * The tokenData
     */
    @JsonProperty("tokenData")
    public JsonNode getTokenData() {
        return tokenData;
    }

    /**
     *
     * @return
     * The body
     */
    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    /**
     *
     * @return
     * The contributor
     */
    @JsonProperty("contributor")
    public Contributors getContributors() {
        return contributors;
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
     * The keywordsDisplay
     */
    @JsonProperty("keywordsDisplay")
    public List<KeywordsDisplay> getKeywordsDisplay() {
        return keywordsDisplay;
    }

    /**
     *
     * @return
     * The keywordsAll
     */
    @JsonProperty("keywordsAll")
    public List<KeywordsAll> getKeywordsAll() {
        return keywordsAll;
    }

    /**
     *
     * @return
     * The approval
     */
    @JsonProperty("approval")
    public String getApproval() {
        return approval;
    }

    /**
     *
     * @return
     * The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return
     * The dataURI
     */
    @JsonProperty("dataURI")
    public String getDataURI() {
        return dataURI;
    }

    /**
     *
     * @return
     * The primaryKeyword
     */
    @JsonProperty("primaryKeyword")
    public PrimaryKeyword getPrimaryKeyword() {
        return primaryKeyword;
    }

    /**
     *
     * @return
     * The media
     */
    @JsonProperty("media")
    public Media getMedia() {
        return media;
    }

    /**
     *
     * @return
     * The preview
     */
    @JsonProperty("preview")
    public String getPreview() {
        return preview;
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