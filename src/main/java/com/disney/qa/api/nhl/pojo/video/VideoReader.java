
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
        "videosLongList"
})
public class VideoReader {

    @JsonProperty("videosLongList")
    private List<VideosLongList> videosLongList = new ArrayList<VideosLongList>();
    @JsonProperty("list")
    private List<VideosLongList> videosList = new ArrayList<VideosLongList>();

    /**
     *
     * @return
     * The videosLongList
     */
    @JsonProperty("videosLongList")
    public List<VideosLongList> getVideosLongList() {
        return videosLongList;
    }

    /**
     *
     * @return
     * The videosLongList
     */
    @JsonProperty("list")
    public List<VideosLongList> getVideoList() {
        return videosList;
    }
}