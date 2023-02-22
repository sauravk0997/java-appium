
package com.disney.qa.api.nhl.pojo.stats.games;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.processing.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "copyright",
    "link",
    "editorial",
    "media",
    "highlights"
})
public class Games {

    @JsonProperty("copyright")
    private String copyright;
    @JsonProperty("link")
    private String link;
    @JsonProperty("editorial")
    private Editorial editorial;
    @JsonProperty("media")
    private Media media;
    @JsonProperty("highlights")
    private Highlights highlights;

    /**
     * 
     * @return
     *     The copyright
     */
    @JsonProperty("copyright")
    public String getCopyright() {
        return copyright;
    }

    /**
     * 
     * @param copyright
     *     The copyright
     */
    @JsonProperty("copyright")
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * 
     * @return
     *     The link
     */
    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    /**
     * 
     * @param link
     *     The link
     */
    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * 
     * @return
     *     The editorial
     */
    @JsonProperty("editorial")
    public Editorial getEditorial() {
        return editorial;
    }

    /**
     * 
     * @param editorial
     *     The editorial
     */
    @JsonProperty("editorial")
    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    /**
     * 
     * @return
     *     The media
     */
    @JsonProperty("media")
    public Media getMedia() {
        return media;
    }

    /**
     * 
     * @param media
     *     The media
     */
    @JsonProperty("media")
    public void setMedia(Media media) {
        this.media = media;
    }

    /**
     * 
     * @return
     *     The highlights
     */
    @JsonProperty("highlights")
    public Highlights getHighlights() {
        return highlights;
    }

    /**
     * 
     * @param highlights
     *     The highlights
     */
    @JsonProperty("highlights")
    public void setHighlights(Highlights highlights) {
        this.highlights = highlights;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(copyright).append(link).append(editorial).append(media).append(highlights).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Games) == false) {
            return false;
        }
        Games rhs = ((Games) other);
        return new EqualsBuilder().append(copyright, rhs.copyright).append(link, rhs.link).append(editorial, rhs.editorial).append(media, rhs.media).append(highlights, rhs.highlights).isEquals();
    }

}
