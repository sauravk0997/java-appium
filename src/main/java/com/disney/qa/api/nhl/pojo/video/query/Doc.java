package com.disney.qa.api.nhl.pojo.video.query;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "subhead",
        "last_updated",
        "image",
        "playbacks",
        "authFlow",
        "duration",
        "titles",
        "asset_id",
        "byline",
        "title",
        "tagline",
        "type",
        "blurb",
        "lang",
        "bigBlurb",
        "display_timestamp",
        "first_published",
        "sort_timestamp",
        "seoTitle",
        "langs",
        "bylines",
        "slug",
        "seoKeywords",
        "mediaPlaybackURL",
        "commenting",
        "keywordsDisplay",
        "url",
        "flags",
        "seoDescription"
})
public class Doc {

    @JsonProperty("subhead")
    private Object subhead;
    @JsonProperty("last_updated")
    private String lastUpdated;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("playbacks")
    private Playbacks playbacks;
    @JsonProperty("authFlow")
    private boolean authFlow;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("titles")
    private List<String> titles = new ArrayList<String>();
    @JsonProperty("asset_id")
    private String assetId;
    @JsonProperty("byline")
    private Object byline;
    @JsonProperty("title")
    private String title;
    @JsonProperty("tagline")
    private Object tagline;
    @JsonProperty("type")
    private String type;
    @JsonProperty("blurb")
    private String blurb;
    @JsonProperty("lang")
    private String lang;
    @JsonProperty("bigBlurb")
    private String bigBlurb;
    @JsonProperty("display_timestamp")
    private String displayTimestamp;
    @JsonProperty("first_published")
    private String firstPublished;
    @JsonProperty("sort_timestamp")
    private String sortTimestamp;
    @JsonProperty("seoTitle")
    private String seoTitle;
    @JsonProperty("langs")
    private List<String> langs = new ArrayList<String>();
    @JsonProperty("bylines")
    private List<Object> bylines = new ArrayList<Object>();
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("seoKeywords")
    private Object seoKeywords;
    @JsonProperty("mediaPlaybackURL")
    private Object mediaPlaybackURL;
    @JsonProperty("commenting")
    private Object commenting;
    @JsonProperty("keywordsDisplay")
    private List<KeywordsDisplay> keywordsDisplay = new ArrayList<KeywordsDisplay>();
    @JsonProperty("url")
    private String url;
    @JsonProperty("flags")
    private List<String> flags = new ArrayList<String>();
    @JsonProperty("seoDescription")
    private Object seoDescription;

    /**
     *
     * @return
     * The subhead
     */
    @JsonProperty("subhead")
    public Object getSubhead() {
        return subhead;
    }

    /**
     *
     * @return
     * The lastUpdated
     */
    @JsonProperty("last_updated")
    public String getLastUpdated() {
        return lastUpdated;
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
     * The playbacks
     */
    @JsonProperty("playbacks")
    public Playbacks getPlaybacks() {
        return playbacks;
    }

    /**
     *
     * @return
     * The authFlow
     */
    @JsonProperty("authFlow")
    public boolean isAuthFlow() {
        return authFlow;
    }

    /**
     *
     * @return
     * The duration
     */
    @JsonProperty("duration")
    public String getDuration() {
        return duration;
    }

    /**
     *
     * @return
     * The titles
     */
    @JsonProperty("titles")
    public List<String> getTitles() {
        return titles;
    }

    /**
     *
     * @return
     * The assetId
     */
    @JsonProperty("asset_id")
    public String getAssetId() {
        return assetId;
    }

    /**
     *
     * @return
     * The byline
     */
    @JsonProperty("byline")
    public Object getByline() {
        return byline;
    }

    /**
     *
     * @return
     * The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return
     * The tagline
     */
    @JsonProperty("tagline")
    public Object getTagline() {
        return tagline;
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
     * The blurb
     */
    @JsonProperty("blurb")
    public String getBlurb() {
        return blurb;
    }

    /**
     *
     * @return
     * The lang
     */
    @JsonProperty("lang")
    public String getLang() {
        return lang;
    }

    /**
     *
     * @return
     * The bigBlurb
     */
    @JsonProperty("bigBlurb")
    public String getBigBlurb() {
        return bigBlurb;
    }

    /**
     *
     * @return
     * The displayTimestamp
     */
    @JsonProperty("display_timestamp")
    public String getDisplayTimestamp() {
        return displayTimestamp;
    }

    /**
     *
     * @return
     * The firstPublished
     */
    @JsonProperty("first_published")
    public String getFirstPublished() {
        return firstPublished;
    }

    /**
     *
     * @return
     * The sortTimestamp
     */
    @JsonProperty("sort_timestamp")
    public String getSortTimestamp() {
        return sortTimestamp;
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
     * The langs
     */
    @JsonProperty("langs")
    public List<String> getLangs() {
        return langs;
    }

    /**
     *
     * @return
     * The bylines
     */
    @JsonProperty("bylines")
    public List<Object> getBylines() {
        return bylines;
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
     * The seoKeywords
     */
    @JsonProperty("seoKeywords")
    public Object getSeoKeywords() {
        return seoKeywords;
    }

    /**
     *
     * @return
     * The mediaPlaybackURL
     */
    @JsonProperty("mediaPlaybackURL")
    public Object getMediaPlaybackURL() {
        return mediaPlaybackURL;
    }

    /**
     *
     * @return
     * The commenting
     */
    @JsonProperty("commenting")
    public Object getCommenting() {
        return commenting;
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
     * The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     *
     * @return
     * The flags
     */
    @JsonProperty("flags")
    public List<String> getFlags() {
        return flags;
    }

    /**
     *
     * @return
     * The seoDescription
     */
    @JsonProperty("seoDescription")
    public Object getSeoDescription() {
        return seoDescription;
    }
}