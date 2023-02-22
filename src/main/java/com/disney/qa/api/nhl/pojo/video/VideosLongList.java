package com.disney.qa.api.nhl.pojo.video;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.processing.Generated;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "type",
        "date",
        "headline",
        "blurb",
        "eventId",
        "duration",
        "image",
        "mediaPlaybackURL",
        "id",
        "authFlow",
        "flags"
})
public class VideosLongList {

    @JsonProperty("type")
    private String type;
    @JsonProperty("date")
    private String date;
    @JsonProperty("headline")
    private String headline;
    @JsonProperty("blurb")
    private String blurb;
    @JsonProperty("eventId")
    private String eventId;
    @JsonProperty("duration")
    private String duration;
    @JsonProperty("image")
    private Image image;
    @JsonProperty("mediaPlaybackURL")
    private String mediaPlaybackURL;
    @JsonProperty("id")
    private String id;
    @JsonProperty("authFlow")
    private boolean authFlow;
    @JsonProperty("flags")
    private List<String> flags = new ArrayList<String>();

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
     * The date
     */
    @JsonProperty("date")
    public String getDate() {
        return date;
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
     * The blurb
     */
    @JsonProperty("blurb")
    public String getBlurb() {
        return blurb;
    }

    /**
     *
     * @return
     * The eventId
     */
    @JsonProperty("eventId")
    public String getEventId() {
        return eventId;
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
     * The image
     */
    @JsonProperty("image")
    public Image getImage() {
        return image;
    }

    /**
     *
     * @return
     * The mediaPlaybackURL
     */
    @JsonProperty("mediaPlaybackURL")
    public String getMediaPlaybackURL() {
        return mediaPlaybackURL;
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
     * The authFlow
     */
    @JsonProperty("authFlow")
    public boolean isAuthFlow() {
        return authFlow;
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
}